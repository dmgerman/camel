begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|SoftReference
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
name|Method
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
name|ParameterizedType
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
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|CamelExchangeException
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
name|Message
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
name|CxfConstants
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
name|CxfOperationException
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
name|LRUCache
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
name|jaxrs
operator|.
name|JAXRSServiceFactoryBean
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
name|jaxrs
operator|.
name|client
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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * CxfRsProducer binds a Camel exchange to a CXF exchange, acts as a CXF  * JAXRS client, it will turn the normal Object invocation to a RESTful request  * according to resource annotation.  Any response will be bound to Camel exchange.  */
end_comment

begin_class
DECL|class|CxfRsProducer
specifier|public
class|class
name|CxfRsProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CxfRsProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|throwException
specifier|private
name|boolean
name|throwException
decl_stmt|;
comment|// using a cache of factory beans instead of setting the address of a single cfb
comment|// to avoid concurrent issues
DECL|field|clientFactoryBeanCache
specifier|private
name|ClientFactoryBeanCache
name|clientFactoryBeanCache
decl_stmt|;
DECL|method|CxfRsProducer (CxfRsEndpoint endpoint)
specifier|public
name|CxfRsProducer
parameter_list|(
name|CxfRsEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|throwException
operator|=
name|endpoint
operator|.
name|isThrowExceptionOnFailure
argument_list|()
expr_stmt|;
name|clientFactoryBeanCache
operator|=
operator|new
name|ClientFactoryBeanCache
argument_list|(
name|endpoint
operator|.
name|getMaxClientCacheSize
argument_list|()
argument_list|)
expr_stmt|;
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
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Boolean
name|httpClientAPI
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// set the value with endpoint's option
if|if
condition|(
name|httpClientAPI
operator|==
literal|null
condition|)
block|{
name|httpClientAPI
operator|=
operator|(
operator|(
name|CxfRsEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|isHttpClientAPI
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|httpClientAPI
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|invokeHttpClient
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|invokeProxyClient
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|invokeHttpClient (Exchange exchange)
specifier|protected
name|void
name|invokeHttpClient
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
name|clientFactoryBeanCache
operator|.
name|get
argument_list|(
name|CxfEndpointUtils
operator|.
name|getEffectiveAddress
argument_list|(
name|exchange
argument_list|,
operator|(
operator|(
name|CxfRsEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|WebClient
name|client
init|=
name|cfb
operator|.
name|createWebClient
argument_list|()
decl_stmt|;
name|String
name|httpMethod
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Class
name|responseClass
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|Type
name|genericType
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_GENERIC_TYPE
argument_list|,
name|Type
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"HTTP method = "
operator|+
name|httpMethod
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"path = "
operator|+
name|path
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"responseClass = "
operator|+
name|responseClass
argument_list|)
expr_stmt|;
block|}
comment|// set the path
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|path
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
name|CxfRsEndpoint
name|cxfRsEndpoint
init|=
operator|(
name|CxfRsEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
comment|// check if there is a query map in the message header
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|maps
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_QUERY_MAP
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|==
literal|null
condition|)
block|{
name|maps
operator|=
name|cxfRsEndpoint
operator|.
name|getParameters
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|maps
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|maps
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|client
operator|.
name|query
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|CxfRsBinding
name|binding
init|=
name|cxfRsEndpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
comment|// set the body
name|Object
name|body
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
literal|"GET"
operator|.
name|equals
argument_list|(
name|httpMethod
argument_list|)
condition|)
block|{
comment|// need to check the request object.
name|body
operator|=
name|binding
operator|.
name|bindCamelMessageBodyToRequestBody
argument_list|(
name|inMessage
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Request body = "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
block|}
comment|// set headers
name|client
operator|.
name|headers
argument_list|(
name|binding
operator|.
name|bindCamelHeadersToRequestHeaders
argument_list|(
name|inMessage
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
comment|// invoke the client
name|Object
name|response
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|responseClass
operator|==
literal|null
operator|||
name|Response
operator|.
name|class
operator|.
name|equals
argument_list|(
name|responseClass
argument_list|)
condition|)
block|{
name|response
operator|=
name|client
operator|.
name|invoke
argument_list|(
name|httpMethod
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|responseClass
argument_list|)
condition|)
block|{
if|if
condition|(
name|genericType
operator|instanceof
name|ParameterizedType
condition|)
block|{
comment|// Get the collection member type first
name|Type
index|[]
name|actualTypeArguments
init|=
operator|(
operator|(
name|ParameterizedType
operator|)
name|genericType
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
name|response
operator|=
name|client
operator|.
name|invokeAndGetCollection
argument_list|(
name|httpMethod
argument_list|,
name|body
argument_list|,
operator|(
name|Class
operator|)
name|actualTypeArguments
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Header "
operator|+
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_GENERIC_TYPE
operator|+
literal|" not found in message"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|response
operator|=
name|client
operator|.
name|invoke
argument_list|(
name|httpMethod
argument_list|,
name|body
argument_list|,
name|responseClass
argument_list|)
expr_stmt|;
block|}
block|}
comment|//Throw exception on a response> 207
comment|//http://en.wikipedia.org/wiki/List_of_HTTP_status_codes
if|if
condition|(
name|throwException
condition|)
block|{
if|if
condition|(
name|response
operator|instanceof
name|Response
condition|)
block|{
name|Integer
name|respCode
init|=
operator|(
operator|(
name|Response
operator|)
name|response
operator|)
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|respCode
operator|>
literal|207
condition|)
block|{
throw|throw
name|populateCxfRsProducerException
argument_list|(
name|exchange
argument_list|,
operator|(
name|Response
operator|)
name|response
argument_list|,
name|respCode
argument_list|)
throw|;
block|}
block|}
block|}
comment|// set response
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Response body = "
operator|+
name|response
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|binding
operator|.
name|bindResponseToCamelBody
argument_list|(
name|response
argument_list|,
name|exchange
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
name|binding
operator|.
name|bindResponseHeadersToCamelHeaders
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|invokeProxyClient (Exchange exchange)
specifier|protected
name|void
name|invokeProxyClient
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
index|[]
name|varValues
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_VAR_VALUES
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|String
name|methodName
init|=
name|inMessage
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
name|Client
name|target
init|=
literal|null
decl_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
name|clientFactoryBeanCache
operator|.
name|get
argument_list|(
name|CxfEndpointUtils
operator|.
name|getEffectiveAddress
argument_list|(
name|exchange
argument_list|,
operator|(
operator|(
name|CxfRsEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|varValues
operator|==
literal|null
condition|)
block|{
name|target
operator|=
name|cfb
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|target
operator|=
name|cfb
operator|.
name|createWithValues
argument_list|(
name|varValues
argument_list|)
expr_stmt|;
block|}
comment|// find out the method which we want to invoke
name|JAXRSServiceFactoryBean
name|sfb
init|=
name|cfb
operator|.
name|getServiceFactory
argument_list|()
decl_stmt|;
name|sfb
operator|.
name|getResourceClasses
argument_list|()
expr_stmt|;
name|Object
index|[]
name|parameters
init|=
name|inMessage
operator|.
name|getBody
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
comment|// get the method
name|Method
name|method
init|=
name|findRightMethod
argument_list|(
name|sfb
operator|.
name|getResourceClasses
argument_list|()
argument_list|,
name|methodName
argument_list|,
name|getParameterTypes
argument_list|(
name|parameters
argument_list|)
argument_list|)
decl_stmt|;
comment|// Will send out the message to
comment|// Need to deal with the sub resource class
name|Object
name|response
init|=
name|method
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|throwException
condition|)
block|{
if|if
condition|(
name|response
operator|instanceof
name|Response
condition|)
block|{
name|Integer
name|respCode
init|=
operator|(
operator|(
name|Response
operator|)
name|response
operator|)
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|respCode
operator|>
literal|207
condition|)
block|{
throw|throw
name|populateCxfRsProducerException
argument_list|(
name|exchange
argument_list|,
operator|(
name|Response
operator|)
name|response
argument_list|,
name|respCode
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|findRightMethod (List<Class<?>> resourceClasses, String methodName, Class[] parameterTypes)
specifier|private
name|Method
name|findRightMethod
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resourceClasses
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
index|[]
name|parameterTypes
parameter_list|)
throws|throws
name|NoSuchMethodException
block|{
name|Method
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|resourceClasses
control|)
block|{
try|try
block|{
name|answer
operator|=
name|clazz
operator|.
name|getMethod
argument_list|(
name|methodName
argument_list|,
name|parameterTypes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
comment|// keep looking
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
comment|// keep looking
block|}
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
throw|throw
operator|new
name|NoSuchMethodException
argument_list|(
literal|"Cannot find method with name: "
operator|+
name|methodName
operator|+
literal|" having parameters: "
operator|+
name|arrayToString
argument_list|(
name|parameterTypes
argument_list|)
argument_list|)
throw|;
block|}
DECL|method|getParameterTypes (Object[] objects)
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getParameterTypes
parameter_list|(
name|Object
index|[]
name|objects
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|answer
init|=
operator|new
name|Class
index|[
name|objects
operator|.
name|length
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|objects
control|)
block|{
name|answer
index|[
name|i
index|]
operator|=
name|obj
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|arrayToString (Object[] array)
specifier|private
name|String
name|arrayToString
parameter_list|(
name|Object
index|[]
name|array
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"["
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|array
control|)
block|{
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|2
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|obj
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|populateCxfRsProducerException (Exchange exchange, Response response, int responseCode)
specifier|protected
name|CxfOperationException
name|populateCxfRsProducerException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|,
name|int
name|responseCode
parameter_list|)
block|{
name|CxfOperationException
name|exception
decl_stmt|;
name|String
name|uri
init|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|String
name|statusText
init|=
name|Response
operator|.
name|Status
operator|.
name|fromStatusCode
argument_list|(
name|responseCode
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|parseResponseHeaders
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|copy
init|=
name|response
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|responseCode
operator|>=
literal|300
operator|&&
name|responseCode
operator|<
literal|400
condition|)
block|{
name|String
name|redirectLocation
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"Location"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|redirectLocation
operator|=
name|response
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"location"
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|exception
operator|=
operator|new
name|CxfOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
name|redirectLocation
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//no redirect location
name|exception
operator|=
operator|new
name|CxfOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//internal server error(error code 500)
name|exception
operator|=
operator|new
name|CxfOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
return|return
name|exception
return|;
block|}
DECL|method|parseResponseHeaders (Object response, Exchange camelExchange)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseResponseHeaders
parameter_list|(
name|Object
name|response
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|instanceof
name|Response
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|entry
range|:
operator|(
operator|(
name|Response
operator|)
name|response
operator|)
operator|.
name|getMetadata
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Parse external header "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Cache contains {@link org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean}      */
DECL|class|ClientFactoryBeanCache
specifier|private
class|class
name|ClientFactoryBeanCache
block|{
DECL|field|cache
specifier|private
name|LRUCache
argument_list|<
name|String
argument_list|,
name|SoftReference
argument_list|<
name|JAXRSClientFactoryBean
argument_list|>
argument_list|>
name|cache
decl_stmt|;
DECL|method|ClientFactoryBeanCache (final int maxCacheSize)
specifier|public
name|ClientFactoryBeanCache
parameter_list|(
specifier|final
name|int
name|maxCacheSize
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|SoftReference
argument_list|<
name|JAXRSClientFactoryBean
argument_list|>
argument_list|>
argument_list|(
name|maxCacheSize
argument_list|)
expr_stmt|;
block|}
DECL|method|get (String address)
specifier|public
name|JAXRSClientFactoryBean
name|get
parameter_list|(
name|String
name|address
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXRSClientFactoryBean
name|retval
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|cache
init|)
block|{
name|SoftReference
argument_list|<
name|JAXRSClientFactoryBean
argument_list|>
name|ref
init|=
name|cache
operator|.
name|get
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|ref
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|CxfRsEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|createJAXRSClientFactoryBean
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|address
argument_list|,
operator|new
name|SoftReference
argument_list|<
name|JAXRSClientFactoryBean
argument_list|>
argument_list|(
name|retval
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Created client factory bean and add to cache for address '"
operator|+
name|address
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Retrieved client factory bean from cache for address '"
operator|+
name|address
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
block|}
block|}
end_class

end_unit

