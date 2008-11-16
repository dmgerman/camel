begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ExchangePattern
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Producer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|util
operator|.
name|CxfEndpointUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|util
operator|.
name|Dummy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|util
operator|.
name|NullConduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|util
operator|.
name|NullConduitSelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|AsyncProcessorHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|ClientImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxyFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|InterceptorChain
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|OutgoingChainInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|io
operator|.
name|CachedOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * A CXF based soap provider.  * The consumer will delegate to another endpoint for the transport layer  * and will provide SOAP support on top of it.  */
end_comment

begin_class
DECL|class|CxfSoapProducer
specifier|public
class|class
name|CxfSoapProducer
implements|implements
name|Producer
implements|,
name|AsyncProcessor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CxfSoapProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|CxfSoapEndpoint
name|endpoint
decl_stmt|;
DECL|field|producer
specifier|private
specifier|final
name|Producer
name|producer
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|client
specifier|private
name|ClientImpl
name|client
decl_stmt|;
DECL|method|CxfSoapProducer (CxfSoapEndpoint endpoint)
specifier|public
name|CxfSoapProducer
parameter_list|(
name|CxfSoapEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|endpoint
operator|.
name|getInnerEndpoint
argument_list|()
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|this
operator|.
name|processor
operator|=
operator|new
name|AsyncProcessorDecorator
argument_list|(
name|producer
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|processSoapProviderIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|processSoapProviderOut
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|//create the endpoint and setup the interceptors
name|Class
name|sei
init|=
name|CxfEndpointUtils
operator|.
name|getSEIClass
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
decl_stmt|;
name|ClientProxyFactoryBean
name|cfb
init|=
name|CxfEndpointUtils
operator|.
name|getClientFactoryBean
argument_list|(
name|sei
argument_list|)
decl_stmt|;
if|if
condition|(
name|sei
operator|==
literal|null
condition|)
block|{
name|cfb
operator|.
name|setServiceClass
argument_list|(
name|Dummy
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfb
operator|.
name|setServiceClass
argument_list|(
name|sei
argument_list|)
expr_stmt|;
block|}
name|cfb
operator|.
name|setWsdlURL
argument_list|(
name|endpoint
operator|.
name|getWsdl
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getServiceName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cfb
operator|.
name|setServiceName
argument_list|(
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getEndpointName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cfb
operator|.
name|setEndpointName
argument_list|(
name|endpoint
operator|.
name|getEndpointName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cfb
operator|.
name|setConduitSelector
argument_list|(
operator|new
name|NullConduitSelector
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|=
call|(
name|ClientImpl
call|)
argument_list|(
operator|(
name|ClientProxy
operator|)
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|cfb
operator|.
name|create
argument_list|()
argument_list|)
argument_list|)
operator|.
name|getClient
argument_list|()
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|producer
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|()
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|createExchange (Exchange exchange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|processSoapProviderOut (Exchange exchange)
specifier|protected
name|void
name|processSoapProviderOut
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"processSoapProviderOut: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|inMessage
init|=
name|CxfSoapBinding
operator|.
name|getCxfInMessage
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|,
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|client
operator|.
name|setInInterceptors
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|inMessage
operator|.
name|getContent
argument_list|(
name|Source
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|getBus ()
specifier|protected
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getBus
argument_list|()
return|;
block|}
DECL|method|processSoapProviderIn (Exchange exchange)
specifier|protected
name|void
name|processSoapProviderIn
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"processSoapProviderIn: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Endpoint
name|cxfEndpoint
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|cxfExchange
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Endpoint
operator|.
name|class
argument_list|,
name|cxfEndpoint
argument_list|)
expr_stmt|;
name|cxfExchange
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|cxfExchange
operator|.
name|setConduit
argument_list|(
operator|new
name|NullConduit
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|CXF_EXCHANGE
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|outMessage
init|=
name|CxfSoapBinding
operator|.
name|getCxfOutMessage
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|,
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|InterceptorChain
name|chain
init|=
name|OutgoingChainInterceptor
operator|.
name|getOutInterceptorChain
argument_list|(
name|cxfExchange
argument_list|)
decl_stmt|;
name|outMessage
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doIntercept
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|outputStream
init|=
operator|(
name|CachedOutputStream
operator|)
name|outMessage
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|outputStream
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|outputStream
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

