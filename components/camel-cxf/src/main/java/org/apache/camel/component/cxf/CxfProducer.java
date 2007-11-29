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
name|util
operator|.
name|List
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
name|CamelException
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
name|RuntimeCamelException
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
name|invoker
operator|.
name|CxfClient
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
name|invoker
operator|.
name|CxfClientFactoryBean
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
name|invoker
operator|.
name|InvokingContext
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
name|spring
operator|.
name|CxfEndpointBean
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
name|impl
operator|.
name|DefaultProducer
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
name|ObjectHelper
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
name|BusFactory
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
name|binding
operator|.
name|Binding
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
name|binding
operator|.
name|BindingFactory
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
name|binding
operator|.
name|BindingFactoryManager
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|Client
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
name|Endpoint
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
name|message
operator|.
name|Message
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
name|MessageImpl
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|transport
operator|.
name|Conduit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_comment
comment|/**  * Sends messages from Camel into the CXF endpoint  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfProducer
specifier|public
class|class
name|CxfProducer
extends|extends
name|DefaultProducer
argument_list|<
name|CxfExchange
argument_list|>
block|{
DECL|field|endpoint
specifier|private
name|CxfEndpoint
name|endpoint
decl_stmt|;
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
DECL|field|dataFormat
specifier|private
name|DataFormat
name|dataFormat
decl_stmt|;
DECL|method|CxfProducer (CxfEndpoint endpoint)
specifier|public
name|CxfProducer
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|)
throws|throws
name|CamelException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|dataFormat
operator|=
name|CxfEndpointUtils
operator|.
name|getDataFormat
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|POJO
argument_list|)
condition|)
block|{
name|client
operator|=
name|createClientFormClientFactoryBean
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// create CxfClient for message
name|client
operator|=
name|createClientForStreamMessge
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createClientForStreamMessge ()
specifier|private
name|Client
name|createClientForStreamMessge
parameter_list|()
throws|throws
name|CamelException
block|{
name|CxfClientFactoryBean
name|cfb
init|=
operator|new
name|CxfClientFactoryBean
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|.
name|getServiceClass
argument_list|()
condition|)
block|{
try|try
block|{
name|Class
name|serviceClass
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|jsr181Enabled
init|=
name|CxfEndpointUtils
operator|.
name|hasWebServiceAnnotation
argument_list|(
name|serviceClass
argument_list|)
decl_stmt|;
name|cfb
operator|.
name|setJSR181Enabled
argument_list|(
name|jsr181Enabled
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|createClientFormClientFactoryBean
argument_list|(
name|cfb
argument_list|)
return|;
block|}
comment|//If cfb is null ,we will try to find a right cfb to use.
DECL|method|createClientFormClientFactoryBean (ClientFactoryBean cfb)
specifier|private
name|Client
name|createClientFormClientFactoryBean
parameter_list|(
name|ClientFactoryBean
name|cfb
parameter_list|)
throws|throws
name|CamelException
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isSpringContextEndpoint
argument_list|()
condition|)
block|{
name|CxfEndpointBean
name|endpointBean
init|=
name|endpoint
operator|.
name|getCxfEndpointBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|cfb
operator|==
literal|null
condition|)
block|{
name|cfb
operator|=
name|CxfEndpointUtils
operator|.
name|getClientFactoryBean
argument_list|(
name|endpointBean
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|configure
argument_list|(
name|cfb
argument_list|)
expr_stmt|;
name|CxfEndpointBean
name|cxfEndpointBean
init|=
name|endpoint
operator|.
name|getCxfEndpointBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|cxfEndpointBean
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
name|cxfEndpointBean
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cxfEndpointBean
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
name|cxfEndpointBean
operator|.
name|getEndpointName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// set up the clientFactoryBean by using URI information
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|.
name|getServiceClass
argument_list|()
condition|)
block|{
try|try
block|{
comment|//we need to choice the right front end to create the clientFactoryBean
name|Class
name|serviceClass
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfb
operator|==
literal|null
condition|)
block|{
name|cfb
operator|=
name|CxfEndpointUtils
operator|.
name|getClientFactoryBean
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
block|}
name|cfb
operator|.
name|setAddress
argument_list|(
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|.
name|getServiceClass
argument_list|()
condition|)
block|{
name|cfb
operator|.
name|setServiceClass
argument_list|(
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
condition|)
block|{
name|cfb
operator|.
name|setWsdlURL
argument_list|(
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// we can't see any service class from the endpoint
if|if
condition|(
name|cfb
operator|==
literal|null
condition|)
block|{
name|cfb
operator|=
operator|new
name|ClientFactoryBean
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
condition|)
block|{
name|cfb
operator|.
name|setWsdlURL
argument_list|(
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// throw the exception for insufficiency of the endpoint info
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Insufficiency of the endpoint info"
argument_list|)
throw|;
block|}
block|}
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
name|CxfEndpointUtils
operator|.
name|getServiceName
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getPortName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cfb
operator|.
name|setEndpointName
argument_list|(
name|CxfEndpointUtils
operator|.
name|getPortName
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cfb
operator|.
name|setWsdlURL
argument_list|(
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|cfb
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
return|return
name|cfb
operator|.
name|create
argument_list|()
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
block|{
name|CxfExchange
name|cxfExchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|process
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|!=
name|ExchangePattern
operator|.
name|InOnly
condition|)
block|{
name|exchange
operator|.
name|copyFrom
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|process (CxfExchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|)
block|{
name|CxfBinding
name|cxfBinding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|Message
name|inMessage
init|=
name|cxfBinding
operator|.
name|createCxfMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|POJO
argument_list|)
condition|)
block|{
comment|//InputStream is = m.getContent(InputStream.class);
comment|// now we just deal with the POJO invocations
name|List
name|paraments
init|=
name|inMessage
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|operation
init|=
name|inMessage
operator|.
name|getContent
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|response
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|operation
operator|!=
literal|null
operator|&&
name|paraments
operator|!=
literal|null
condition|)
block|{
comment|// now we just deal with the invoking the paraments
try|try
block|{
name|Object
index|[]
name|result
init|=
name|client
operator|.
name|invoke
argument_list|(
name|operation
argument_list|,
name|paraments
operator|.
name|toArray
argument_list|()
argument_list|)
decl_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|cxfBinding
operator|.
name|storeCxfResponse
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|response
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|cxfBinding
operator|.
name|storeCxfFault
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// get the invocation context
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|ex
init|=
name|exchange
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|InvokingContext
name|invokingContext
init|=
name|ex
operator|.
name|get
argument_list|(
name|InvokingContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|params
init|=
name|invokingContext
operator|.
name|getRequestContent
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
comment|// invoke the stream message with the exchange context
name|CxfClient
name|cxfClient
init|=
operator|(
name|CxfClient
operator|)
name|client
decl_stmt|;
comment|// invoke the message
comment|//TODO need setup the call context here
comment|//TODO need to handle the one way message
name|Object
name|result
init|=
name|cxfClient
operator|.
name|dispatch
argument_list|(
name|params
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
decl_stmt|;
comment|// need to get the binding object to create the message
name|BindingOperationInfo
name|boi
init|=
name|ex
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|response
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
comment|// it should be the raw message
name|response
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// create the message here
name|Endpoint
name|ep
init|=
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|response
operator|=
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
expr_stmt|;
block|}
name|response
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setOutMessage
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|invokingContext
operator|.
name|setResponseContent
argument_list|(
name|response
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|cxfBinding
operator|.
name|storeCxfResponse
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//TODO add the falut message handling work
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

