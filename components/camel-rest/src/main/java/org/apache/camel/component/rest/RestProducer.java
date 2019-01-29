begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|bind
operator|.
name|JAXBContext
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
name|CamelContext
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
name|spi
operator|.
name|DataFormat
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
name|spi
operator|.
name|RestConfiguration
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
name|support
operator|.
name|AsyncProcessorConverterHelper
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
name|support
operator|.
name|DefaultAsyncProducer
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
name|support
operator|.
name|EndpointHelper
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
name|support
operator|.
name|IntrospectionSupport
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
name|CollectionStringBuffer
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
name|FileUtil
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
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isEmpty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
import|;
end_import

begin_comment
comment|/**  * Rest producer for calling remote REST services.  */
end_comment

begin_class
DECL|class|RestProducer
specifier|public
class|class
name|RestProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|ACCEPT
specifier|private
specifier|static
specifier|final
name|String
name|ACCEPT
init|=
literal|"Accept"
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|RestConfiguration
name|configuration
decl_stmt|;
DECL|field|prepareUriTemplate
specifier|private
name|boolean
name|prepareUriTemplate
init|=
literal|true
decl_stmt|;
DECL|field|bindingMode
specifier|private
name|RestConfiguration
operator|.
name|RestBindingMode
name|bindingMode
decl_stmt|;
DECL|field|skipBindingOnErrorCode
specifier|private
name|Boolean
name|skipBindingOnErrorCode
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|outType
specifier|private
name|String
name|outType
decl_stmt|;
comment|// the producer of the Camel component that is used as the HTTP client to call the REST service
DECL|field|producer
specifier|private
name|AsyncProcessor
name|producer
decl_stmt|;
comment|// if binding is enabled then this processor should be used to wrap the call with binding before/after
DECL|field|binding
specifier|private
name|AsyncProcessor
name|binding
decl_stmt|;
DECL|method|RestProducer (Endpoint endpoint, Producer producer, RestConfiguration configuration)
specifier|public
name|RestProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
try|try
block|{
name|prepareExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|binding
operator|!=
literal|null
condition|)
block|{
return|return
name|binding
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
else|else
block|{
comment|// no binding in use call the producer directly
return|return
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|RestEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|RestEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|isPrepareUriTemplate ()
specifier|public
name|boolean
name|isPrepareUriTemplate
parameter_list|()
block|{
return|return
name|prepareUriTemplate
return|;
block|}
comment|/**      * Whether to prepare the uri template and replace {key} with values from the exchange, and set      * as {@link Exchange#HTTP_URI} header with the resolved uri to use instead of uri from endpoint.      */
DECL|method|setPrepareUriTemplate (boolean prepareUriTemplate)
specifier|public
name|void
name|setPrepareUriTemplate
parameter_list|(
name|boolean
name|prepareUriTemplate
parameter_list|)
block|{
name|this
operator|.
name|prepareUriTemplate
operator|=
name|prepareUriTemplate
expr_stmt|;
block|}
DECL|method|getBindingMode ()
specifier|public
name|RestConfiguration
operator|.
name|RestBindingMode
name|getBindingMode
parameter_list|()
block|{
return|return
name|bindingMode
return|;
block|}
DECL|method|setBindingMode (RestConfiguration.RestBindingMode bindingMode)
specifier|public
name|void
name|setBindingMode
parameter_list|(
name|RestConfiguration
operator|.
name|RestBindingMode
name|bindingMode
parameter_list|)
block|{
name|this
operator|.
name|bindingMode
operator|=
name|bindingMode
expr_stmt|;
block|}
DECL|method|getSkipBindingOnErrorCode ()
specifier|public
name|Boolean
name|getSkipBindingOnErrorCode
parameter_list|()
block|{
return|return
name|skipBindingOnErrorCode
return|;
block|}
DECL|method|setSkipBindingOnErrorCode (Boolean skipBindingOnErrorCode)
specifier|public
name|void
name|setSkipBindingOnErrorCode
parameter_list|(
name|Boolean
name|skipBindingOnErrorCode
parameter_list|)
block|{
name|this
operator|.
name|skipBindingOnErrorCode
operator|=
name|skipBindingOnErrorCode
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getOutType ()
specifier|public
name|String
name|getOutType
parameter_list|()
block|{
return|return
name|outType
return|;
block|}
DECL|method|setOutType (String outType)
specifier|public
name|void
name|setOutType
parameter_list|(
name|String
name|outType
parameter_list|)
block|{
name|this
operator|.
name|outType
operator|=
name|outType
expr_stmt|;
block|}
DECL|method|prepareExchange (Exchange exchange)
specifier|protected
name|void
name|prepareExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|hasPath
init|=
literal|false
decl_stmt|;
comment|// uri template with path parameters resolved
comment|// uri template may be optional and the user have entered the uri template in the path instead
name|String
name|resolvedUriTemplate
init|=
name|getEndpoint
argument_list|()
operator|.
name|getUriTemplate
argument_list|()
operator|!=
literal|null
condition|?
name|getEndpoint
argument_list|()
operator|.
name|getUriTemplate
argument_list|()
else|:
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|prepareUriTemplate
condition|)
block|{
if|if
condition|(
name|resolvedUriTemplate
operator|.
name|contains
argument_list|(
literal|"{"
argument_list|)
condition|)
block|{
comment|// resolve template and replace {key} with the values form the exchange
comment|// each {} is a parameter (url templating)
name|String
index|[]
name|arr
init|=
name|resolvedUriTemplate
operator|.
name|split
argument_list|(
literal|"\\/"
argument_list|)
decl_stmt|;
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|a
range|:
name|arr
control|)
block|{
if|if
condition|(
name|a
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|a
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|String
name|key
init|=
name|a
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|a
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|hasPath
operator|=
literal|true
expr_stmt|;
name|csb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|csb
operator|.
name|append
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|csb
operator|.
name|append
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
name|resolvedUriTemplate
operator|=
name|csb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
comment|// resolve uri parameters
name|String
name|query
init|=
name|createQueryParameters
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getQueryParameters
argument_list|()
argument_list|,
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
comment|// the query parameters for the rest call to be used
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REST_HTTP_QUERY
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasPath
condition|)
block|{
name|String
name|host
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|String
name|basePath
init|=
name|getEndpoint
argument_list|()
operator|.
name|getUriTemplate
argument_list|()
operator|!=
literal|null
condition|?
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
else|:
literal|null
decl_stmt|;
name|basePath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
name|resolvedUriTemplate
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|resolvedUriTemplate
argument_list|)
expr_stmt|;
comment|// if so us a header for the dynamic uri template so we reuse same endpoint but the header overrides the actual url to use
name|String
name|overrideUri
init|=
name|host
decl_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|overrideUri
operator|+=
literal|"/"
operator|+
name|basePath
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|resolvedUriTemplate
argument_list|)
condition|)
block|{
name|overrideUri
operator|+=
literal|"/"
operator|+
name|resolvedUriTemplate
expr_stmt|;
block|}
comment|// the http uri for the rest call to be used
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REST_HTTP_URI
argument_list|,
name|overrideUri
argument_list|)
expr_stmt|;
comment|// when chaining RestConsumer with RestProducer, the
comment|// HTTP_PATH header will be present, we remove it here
comment|// as the REST_HTTP_URI contains the full URI for the
comment|// request and every other HTTP producer will concatenate
comment|// REST_HTTP_URI with HTTP_PATH resulting in incorrect URIs
name|inMessage
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|)
expr_stmt|;
block|}
comment|// method
name|String
name|method
init|=
name|getEndpoint
argument_list|()
operator|.
name|getMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
comment|// the method should be in upper case
name|String
name|upper
init|=
name|method
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|upper
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|produces
init|=
name|getEndpoint
argument_list|()
operator|.
name|getProduces
argument_list|()
decl_stmt|;
if|if
condition|(
name|isEmpty
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
operator|&&
name|isNotEmpty
argument_list|(
name|produces
argument_list|)
condition|)
block|{
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|produces
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|consumes
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConsumes
argument_list|()
decl_stmt|;
if|if
condition|(
name|isEmpty
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|ACCEPT
argument_list|)
argument_list|)
operator|&&
name|isNotEmpty
argument_list|(
name|consumes
argument_list|)
condition|)
block|{
name|inMessage
operator|.
name|setHeader
argument_list|(
name|ACCEPT
argument_list|,
name|consumes
argument_list|)
expr_stmt|;
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
comment|// create binding processor (returns null if binding is not in use)
name|binding
operator|=
name|createBindingProcessor
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|binding
argument_list|,
name|producer
argument_list|)
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producer
argument_list|,
name|binding
argument_list|)
expr_stmt|;
block|}
DECL|method|createBindingProcessor ()
specifier|protected
name|AsyncProcessor
name|createBindingProcessor
parameter_list|()
throws|throws
name|Exception
block|{
comment|// these options can be overridden per endpoint
name|String
name|mode
init|=
name|configuration
operator|.
name|getBindingMode
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|bindingMode
operator|!=
literal|null
condition|)
block|{
name|mode
operator|=
name|bindingMode
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
name|boolean
name|skip
init|=
name|configuration
operator|.
name|isSkipBindingOnErrorCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|skipBindingOnErrorCode
operator|!=
literal|null
condition|)
block|{
name|skip
operator|=
name|skipBindingOnErrorCode
expr_stmt|;
block|}
if|if
condition|(
name|mode
operator|==
literal|null
operator|||
literal|"off"
operator|.
name|equals
argument_list|(
name|mode
argument_list|)
condition|)
block|{
comment|// binding mode is off
return|return
literal|null
return|;
block|}
comment|// setup json data format
name|String
name|name
init|=
name|configuration
operator|.
name|getJsonDataFormat
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
comment|// must only be a name, not refer to an existing instance
name|Object
name|instance
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"JsonDataFormat name: "
operator|+
name|name
operator|+
literal|" must not be an existing bean instance from the registry"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|name
operator|=
literal|"json-jackson"
expr_stmt|;
block|}
comment|// this will create a new instance as the name was not already pre-created
name|DataFormat
name|json
init|=
name|camelContext
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|DataFormat
name|outJson
init|=
name|camelContext
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// is json binding required?
if|if
condition|(
name|mode
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
operator|&&
name|json
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"JSon DataFormat "
operator|+
name|name
operator|+
literal|" not found."
argument_list|)
throw|;
block|}
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|String
name|typeName
init|=
name|type
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|?
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|type
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
else|:
name|type
decl_stmt|;
name|clazz
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|json
argument_list|,
literal|"unmarshalType"
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|json
argument_list|,
literal|"useList"
argument_list|,
name|type
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setAdditionalConfiguration
argument_list|(
name|configuration
argument_list|,
name|camelContext
argument_list|,
name|json
argument_list|,
literal|"json.in."
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|outClazz
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|outType
operator|!=
literal|null
condition|)
block|{
name|String
name|typeName
init|=
name|outType
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|?
name|outType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|outType
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
else|:
name|outType
decl_stmt|;
name|outClazz
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outClazz
operator|!=
literal|null
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|outJson
argument_list|,
literal|"unmarshalType"
argument_list|,
name|outClazz
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|outJson
argument_list|,
literal|"useList"
argument_list|,
name|outType
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setAdditionalConfiguration
argument_list|(
name|configuration
argument_list|,
name|camelContext
argument_list|,
name|outJson
argument_list|,
literal|"json.out."
argument_list|)
expr_stmt|;
block|}
comment|// setup xml data format
name|name
operator|=
name|configuration
operator|.
name|getXmlDataFormat
argument_list|()
expr_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
comment|// must only be a name, not refer to an existing instance
name|Object
name|instance
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"XmlDataFormat name: "
operator|+
name|name
operator|+
literal|" must not be an existing bean instance from the registry"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|name
operator|=
literal|"jaxb"
expr_stmt|;
block|}
comment|// this will create a new instance as the name was not already pre-created
name|DataFormat
name|jaxb
init|=
name|camelContext
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|DataFormat
name|outJaxb
init|=
name|camelContext
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// is xml binding required?
if|if
condition|(
name|mode
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
operator|&&
name|jaxb
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"XML DataFormat "
operator|+
name|name
operator|+
literal|" not found."
argument_list|)
throw|;
block|}
if|if
condition|(
name|jaxb
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|String
name|typeName
init|=
name|type
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|?
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|type
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
else|:
name|type
decl_stmt|;
name|clazz
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|jaxb
argument_list|,
literal|"context"
argument_list|,
name|jc
argument_list|)
expr_stmt|;
block|}
name|setAdditionalConfiguration
argument_list|(
name|configuration
argument_list|,
name|camelContext
argument_list|,
name|jaxb
argument_list|,
literal|"xml.in."
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|outClazz
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|outType
operator|!=
literal|null
condition|)
block|{
name|String
name|typeName
init|=
name|outType
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|?
name|outType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|outType
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
else|:
name|outType
decl_stmt|;
name|outClazz
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outClazz
operator|!=
literal|null
condition|)
block|{
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|outClazz
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|outJaxb
argument_list|,
literal|"context"
argument_list|,
name|jc
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
comment|// fallback and use the context from the input
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|outJaxb
argument_list|,
literal|"context"
argument_list|,
name|jc
argument_list|)
expr_stmt|;
block|}
name|setAdditionalConfiguration
argument_list|(
name|configuration
argument_list|,
name|camelContext
argument_list|,
name|outJaxb
argument_list|,
literal|"xml.out."
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|RestProducerBindingProcessor
argument_list|(
name|producer
argument_list|,
name|camelContext
argument_list|,
name|json
argument_list|,
name|jaxb
argument_list|,
name|outJson
argument_list|,
name|outJaxb
argument_list|,
name|mode
argument_list|,
name|skip
argument_list|,
name|outType
argument_list|)
return|;
block|}
DECL|method|setAdditionalConfiguration (RestConfiguration config, CamelContext context, DataFormat dataFormat, String prefix)
specifier|private
name|void
name|setAdditionalConfiguration
parameter_list|(
name|RestConfiguration
name|config
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|DataFormat
name|dataFormat
parameter_list|,
name|String
name|prefix
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|config
operator|.
name|getDataFormatProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getDataFormatProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// must use a copy as otherwise the options gets removed during introspection setProperties
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|copy
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// filter keys on prefix
comment|// - either its a known prefix and must match the prefix parameter
comment|// - or its a common configuration that we should always use
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|config
operator|.
name|getDataFormatProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|copyKey
decl_stmt|;
name|boolean
name|known
init|=
name|isKeyKnownPrefix
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|known
condition|)
block|{
comment|// remove the prefix from the key to use
name|copyKey
operator|=
name|key
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use the key as is
name|copyKey
operator|=
name|key
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|known
operator|||
name|key
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|copy
operator|.
name|put
argument_list|(
name|copyKey
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// set reference properties first as they use # syntax that fools the regular properties setter
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|context
argument_list|,
name|dataFormat
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|context
argument_list|,
name|dataFormat
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isKeyKnownPrefix (String key)
specifier|private
name|boolean
name|isKeyKnownPrefix
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|key
operator|.
name|startsWith
argument_list|(
literal|"json.in."
argument_list|)
operator|||
name|key
operator|.
name|startsWith
argument_list|(
literal|"json.out."
argument_list|)
operator|||
name|key
operator|.
name|startsWith
argument_list|(
literal|"xml.in."
argument_list|)
operator|||
name|key
operator|.
name|startsWith
argument_list|(
literal|"xml.out."
argument_list|)
return|;
block|}
DECL|method|createQueryParameters (String query, Message inMessage)
specifier|static
name|String
name|createQueryParameters
parameter_list|(
name|String
name|query
parameter_list|,
name|Message
name|inMessage
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|UnsupportedEncodingException
block|{
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|givenParams
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|givenParams
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|givenParams
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|v
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|String
name|a
init|=
name|v
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// decode the key as { may be decoded to %NN
name|a
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|a
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
if|if
condition|(
name|a
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|a
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|String
name|key
init|=
name|a
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|a
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|boolean
name|optional
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|optional
operator|=
literal|true
expr_stmt|;
block|}
name|String
name|value
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|optional
condition|)
block|{
comment|// value is null and parameter is not optional
name|params
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
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|params
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|query
operator|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
return|return
name|query
return|;
block|}
block|}
end_class

end_unit
