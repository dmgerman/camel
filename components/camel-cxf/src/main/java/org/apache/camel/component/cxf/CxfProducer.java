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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|feature
operator|.
name|MessageDataFormatFeature
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
name|feature
operator|.
name|PayLoadDataFormatFeature
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
name|invoker
operator|.
name|InvokingContextFactory
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|feature
operator|.
name|AbstractFeature
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
name|MessageContentsList
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

begin_comment
comment|/**  * Sends messages from Camel into the CXF endpoint  *  * @version $Revision$  */
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
name|createClientFromClientFactoryBean
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Create CxfClient for message or payload type
name|client
operator|=
name|createClientForStreamMessage
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createClientForStreamMessage ()
specifier|private
name|Client
name|createClientForStreamMessage
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
name|Class
name|serviceClass
init|=
literal|null
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
name|cxfEndpointBean
init|=
name|endpoint
operator|.
name|getCxfEndpointBean
argument_list|()
decl_stmt|;
name|serviceClass
operator|=
name|cxfEndpointBean
operator|.
name|getServiceClass
argument_list|()
expr_stmt|;
name|CxfEndpointUtils
operator|.
name|checkServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CxfEndpointUtils
operator|.
name|checkServiceClassName
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|serviceClass
operator|=
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
name|dataFormat
operator|=
name|CxfEndpointUtils
operator|.
name|getDataFormat
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractFeature
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|MESSAGE
argument_list|)
condition|)
block|{
name|features
operator|.
name|add
argument_list|(
operator|new
name|MessageDataFormatFeature
argument_list|()
argument_list|)
expr_stmt|;
comment|// features.add(new LoggingFeature());
block|}
elseif|else
if|if
condition|(
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
condition|)
block|{
name|features
operator|.
name|add
argument_list|(
operator|new
name|PayLoadDataFormatFeature
argument_list|()
argument_list|)
expr_stmt|;
comment|// features.add(new LoggingFeature());
block|}
name|cfb
operator|.
name|setFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
return|return
name|createClientFromClientFactoryBean
argument_list|(
name|cfb
argument_list|)
return|;
block|}
comment|// If cfb is null, we will try to find the right cfb to use.
DECL|method|createClientFromClientFactoryBean (ClientFactoryBean cfb)
specifier|private
name|Client
name|createClientFromClientFactoryBean
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
literal|null
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getApplicationContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|(
name|endpoint
operator|.
name|getApplicationContext
argument_list|()
argument_list|)
decl_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|()
expr_stmt|;
if|if
condition|(
name|CxfEndpointUtils
operator|.
name|getSetDefaultBus
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// now we just use the default bus here
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isSpringContextEndpoint
argument_list|()
condition|)
block|{
name|CxfEndpointBean
name|cxfEndpointBean
init|=
name|endpoint
operator|.
name|getCxfEndpointBean
argument_list|()
decl_stmt|;
name|CxfEndpointUtils
operator|.
name|checkServiceClass
argument_list|(
name|cxfEndpointBean
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
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
name|cxfEndpointBean
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
block|}
else|else
block|{
comment|// set up the clientFactoryBean by using URI information
name|CxfEndpointUtils
operator|.
name|checkServiceClassName
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|// We need to choose the right front end to create the
comment|// clientFactoryBean
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
name|exchange
operator|.
name|copyFrom
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
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
name|Message
name|inMessage
init|=
name|CxfBinding
operator|.
name|createCxfMessage
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfExchange
operator|.
name|DATA_FORMAT
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
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
comment|// InputStream is = m.getContent(InputStream.class);
comment|// now we just deal with the POJO invocations
name|List
name|parameters
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
if|if
condition|(
name|parameters
operator|==
literal|null
condition|)
block|{
name|parameters
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
name|String
name|operationName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|operationNameSpace
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAMESPACE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Get context from message
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
name|CxfBinding
operator|.
name|propogateContext
argument_list|(
name|inMessage
argument_list|,
name|context
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
name|operationName
operator|!=
literal|null
condition|)
block|{
comment|// we need to check out the operation Namespace
try|try
block|{
name|Object
index|[]
name|result
init|=
literal|null
decl_stmt|;
comment|// call for the client with the parameters
name|result
operator|=
name|invokeClient
argument_list|(
name|operationNameSpace
argument_list|,
name|operationName
argument_list|,
name|parameters
argument_list|,
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
operator|new
name|MessageContentsList
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// copy the response context to the response
name|CxfBinding
operator|.
name|storeCXfResponseContext
argument_list|(
name|response
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
name|CxfBinding
operator|.
name|storeCxfResponse
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|,
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
name|CxfBinding
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
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Can't find the operation name in the message!"
argument_list|)
throw|;
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
if|if
condition|(
name|ex
operator|==
literal|null
condition|)
block|{
name|ex
operator|=
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|CxfConstants
operator|.
name|CXF_EXCHANGE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ex
operator|==
literal|null
condition|)
block|{
name|ex
operator|=
operator|new
name|ExchangeImpl
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
assert|assert
name|ex
operator|!=
literal|null
assert|;
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
if|if
condition|(
name|invokingContext
operator|==
literal|null
condition|)
block|{
name|invokingContext
operator|=
name|InvokingContextFactory
operator|.
name|createContext
argument_list|(
name|dataFormat
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|InvokingContext
operator|.
name|class
argument_list|,
name|invokingContext
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
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
comment|// invoke the message prepare the context
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
name|CxfBinding
operator|.
name|propogateContext
argument_list|(
name|inMessage
argument_list|,
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|Object
name|result
init|=
name|cxfClient
operator|.
name|dispatch
argument_list|(
name|params
argument_list|,
name|context
argument_list|,
name|ex
argument_list|)
decl_stmt|;
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
comment|// copy the response context to the response
name|CxfBinding
operator|.
name|storeCXfResponseContext
argument_list|(
name|response
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
name|CxfBinding
operator|.
name|storeCxfResponse
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
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
name|e
argument_list|)
expr_stmt|;
name|CxfBinding
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO add the fault message handling work
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
DECL|method|invokeClient (String operationNameSpace, String operationName, List parameters, Map<String, Object> context)
specifier|private
name|Object
index|[]
name|invokeClient
parameter_list|(
name|String
name|operationNameSpace
parameter_list|,
name|String
name|operationName
parameter_list|,
name|List
name|parameters
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|QName
name|operationQName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|operationNameSpace
operator|==
literal|null
condition|)
block|{
name|operationQName
operator|=
operator|new
name|QName
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|operationName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|operationQName
operator|=
operator|new
name|QName
argument_list|(
name|operationNameSpace
argument_list|,
name|operationName
argument_list|)
expr_stmt|;
block|}
name|BindingOperationInfo
name|op
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
name|operationQName
argument_list|)
decl_stmt|;
if|if
condition|(
name|op
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"No operation found in the CXF client, the operation is "
operator|+
name|operationQName
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isWrapped
argument_list|()
condition|)
block|{
if|if
condition|(
name|op
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|op
operator|=
name|op
operator|.
name|getUnwrappedOperation
argument_list|()
expr_stmt|;
block|}
block|}
name|Object
index|[]
name|result
init|=
name|client
operator|.
name|invoke
argument_list|(
name|op
argument_list|,
name|parameters
operator|.
name|toArray
argument_list|()
argument_list|,
name|context
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

