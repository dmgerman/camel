begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|MediaType
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|servicenow
operator|.
name|annotations
operator|.
name|ServiceNowSysParm
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
name|servicenow
operator|.
name|auth
operator|.
name|AuthenticationRequestFilter
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
name|jsse
operator|.
name|SSLContextParameters
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSClientParameters
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
name|configuration
operator|.
name|security
operator|.
name|ProxyAuthorizationPolicy
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
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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

begin_class
DECL|class|ServiceNowClient
specifier|public
specifier|final
class|class
name|ServiceNowClient
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ServiceNowClient
operator|.
name|class
argument_list|)
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
name|ServiceNowConfiguration
name|configuration
decl_stmt|;
DECL|field|client
specifier|private
specifier|final
name|WebClient
name|client
decl_stmt|;
DECL|method|ServiceNowClient (CamelContext camelContext, ServiceNowConfiguration configuration)
name|ServiceNowClient
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ServiceNowConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|configuration
operator|.
name|getApiUrl
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|AuthenticationRequestFilter
argument_list|(
name|configuration
argument_list|)
argument_list|,
operator|new
name|JacksonJsonProvider
argument_list|(
name|configuration
operator|.
name|getOrCreateMapper
argument_list|()
argument_list|)
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|configureRequestContext
argument_list|(
name|camelContext
argument_list|,
name|configuration
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|configureTls
argument_list|(
name|camelContext
argument_list|,
name|configuration
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|configureHttpClientPolicy
argument_list|(
name|camelContext
argument_list|,
name|configuration
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|configureProxyAuthorizationPolicy
argument_list|(
name|camelContext
argument_list|,
name|configuration
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
DECL|method|types (MediaType type)
specifier|public
name|ServiceNowClient
name|types
parameter_list|(
name|MediaType
name|type
parameter_list|)
block|{
return|return
name|types
argument_list|(
name|type
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|types (MediaType accept, MediaType type)
specifier|public
name|ServiceNowClient
name|types
parameter_list|(
name|MediaType
name|accept
parameter_list|,
name|MediaType
name|type
parameter_list|)
block|{
name|client
operator|.
name|accept
argument_list|(
name|accept
argument_list|)
expr_stmt|;
name|client
operator|.
name|type
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|path (Object path)
specifier|public
name|ServiceNowClient
name|path
parameter_list|(
name|Object
name|path
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|path
argument_list|)
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
return|return
name|this
return|;
block|}
DECL|method|type (MediaType ct)
specifier|public
name|ServiceNowClient
name|type
parameter_list|(
name|MediaType
name|ct
parameter_list|)
block|{
name|client
operator|.
name|type
argument_list|(
name|ct
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|type (String type)
specifier|public
name|ServiceNowClient
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|client
operator|.
name|type
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|accept (MediaType... types)
specifier|public
name|ServiceNowClient
name|accept
parameter_list|(
name|MediaType
modifier|...
name|types
parameter_list|)
block|{
name|client
operator|.
name|accept
argument_list|(
name|types
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|accept (String... types)
specifier|public
name|ServiceNowClient
name|accept
parameter_list|(
name|String
modifier|...
name|types
parameter_list|)
block|{
name|client
operator|.
name|accept
argument_list|(
name|types
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|query (String name, Object... values)
specifier|public
name|ServiceNowClient
name|query
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|client
operator|.
name|query
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|queryF (String name, String format, Object... values)
specifier|public
name|ServiceNowClient
name|queryF
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|format
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|client
operator|.
name|query
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|query (ServiceNowParam param, Message message)
specifier|public
name|ServiceNowClient
name|query
parameter_list|(
name|ServiceNowParam
name|param
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|Object
name|value
init|=
name|param
operator|.
name|getHeaderValue
argument_list|(
name|message
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|query
argument_list|(
name|param
operator|.
name|getId
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|query (Class<?> model)
specifier|public
name|ServiceNowClient
name|query
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|model
parameter_list|)
block|{
if|if
condition|(
name|model
operator|!=
literal|null
condition|)
block|{
name|String
name|name
decl_stmt|;
name|String
name|value
decl_stmt|;
for|for
control|(
name|ServiceNowSysParm
name|parm
range|:
name|model
operator|.
name|getAnnotationsByType
argument_list|(
name|ServiceNowSysParm
operator|.
name|class
argument_list|)
control|)
block|{
name|name
operator|=
name|parm
operator|.
name|name
argument_list|()
expr_stmt|;
name|value
operator|=
name|parm
operator|.
name|value
argument_list|()
expr_stmt|;
comment|// SysParms defined on model have precedence and replace query param
comment|// with same name set via Message headers.
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|name
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Replace query param {} with value {}"
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|client
operator|.
name|replaceQueryParam
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|this
return|;
block|}
DECL|method|invoke (String httpMethod)
specifier|public
name|Response
name|invoke
parameter_list|(
name|String
name|httpMethod
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|invoke
argument_list|(
name|client
argument_list|,
name|httpMethod
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|invoke (String httpMethod, Object body)
specifier|public
name|Response
name|invoke
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|invoke
argument_list|(
name|client
argument_list|,
name|httpMethod
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|trasform (String httpMethod, Function<Response, T> function)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|trasform
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|Function
argument_list|<
name|Response
argument_list|,
name|T
argument_list|>
name|function
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|invoke
argument_list|(
name|client
argument_list|,
name|httpMethod
argument_list|,
literal|null
argument_list|)
argument_list|)
return|;
block|}
DECL|method|trasform (String httpMethod, Object body, Function<Response, T> function)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|trasform
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|Object
name|body
parameter_list|,
name|Function
argument_list|<
name|Response
argument_list|,
name|T
argument_list|>
name|function
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|invoke
argument_list|(
name|client
argument_list|,
name|httpMethod
argument_list|,
name|body
argument_list|)
argument_list|)
return|;
block|}
DECL|method|reset ()
specifier|public
name|ServiceNowClient
name|reset
parameter_list|()
block|{
name|client
operator|.
name|back
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|client
operator|.
name|reset
argument_list|()
expr_stmt|;
name|client
operator|.
name|resetQuery
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// *******************************
comment|// Helpers
comment|// *******************************
DECL|method|invoke (WebClient client, String httpMethod, Object body)
specifier|private
name|Response
name|invoke
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|String
name|httpMethod
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|Response
name|response
init|=
name|client
operator|.
name|invoke
argument_list|(
name|httpMethod
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|int
name|code
init|=
name|response
operator|.
name|getStatus
argument_list|()
decl_stmt|;
comment|// Only ServiceNow known error status codes are mapped
comment|// See http://wiki.servicenow.com/index.php?title=REST_API#REST_Response_HTTP_Status_Codes
switch|switch
condition|(
name|code
condition|)
block|{
case|case
literal|200
case|:
case|case
literal|201
case|:
case|case
literal|204
case|:
comment|// Success
break|break;
case|case
literal|400
case|:
case|case
literal|401
case|:
case|case
literal|403
case|:
case|case
literal|404
case|:
case|case
literal|405
case|:
case|case
literal|406
case|:
case|case
literal|415
case|:
name|ServiceNowExceptionModel
name|model
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|ServiceNowExceptionModel
operator|.
name|class
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ServiceNowException
argument_list|(
name|code
argument_list|,
name|model
operator|.
name|getStatus
argument_list|()
argument_list|,
name|model
operator|.
name|getError
argument_list|()
operator|.
name|get
argument_list|(
literal|"message"
argument_list|)
argument_list|,
name|model
operator|.
name|getError
argument_list|()
operator|.
name|get
argument_list|(
literal|"detail"
argument_list|)
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|ServiceNowException
argument_list|(
name|code
argument_list|,
name|response
operator|.
name|readEntity
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|response
return|;
block|}
DECL|method|configureRequestContext ( CamelContext context, ServiceNowConfiguration configuration, WebClient client)
specifier|private
specifier|static
name|void
name|configureRequestContext
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ServiceNowConfiguration
name|configuration
parameter_list|,
name|WebClient
name|client
parameter_list|)
throws|throws
name|Exception
block|{
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.http.header.split"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|configureTls ( CamelContext camelContext, ServiceNowConfiguration configuration, WebClient client)
specifier|private
specifier|static
name|void
name|configureTls
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ServiceNowConfiguration
name|configuration
parameter_list|,
name|WebClient
name|client
parameter_list|)
throws|throws
name|Exception
block|{
name|SSLContextParameters
name|sslContextParams
init|=
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|sslContextParams
operator|!=
literal|null
condition|)
block|{
name|HTTPConduit
name|conduit
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
decl_stmt|;
name|TLSClientParameters
name|tlsClientParams
init|=
name|conduit
operator|.
name|getTlsClientParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|tlsClientParams
operator|==
literal|null
condition|)
block|{
name|tlsClientParams
operator|=
operator|new
name|TLSClientParameters
argument_list|()
expr_stmt|;
block|}
name|SSLContext
name|sslContext
init|=
name|sslContextParams
operator|.
name|createSSLContext
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|tlsClientParams
operator|.
name|setSSLSocketFactory
argument_list|(
name|sslContext
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsClientParams
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureHttpClientPolicy ( CamelContext context, ServiceNowConfiguration configuration, WebClient client)
specifier|private
specifier|static
name|void
name|configureHttpClientPolicy
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ServiceNowConfiguration
name|configuration
parameter_list|,
name|WebClient
name|client
parameter_list|)
throws|throws
name|Exception
block|{
name|HTTPClientPolicy
name|httpPolicy
init|=
name|configuration
operator|.
name|getHttpClientPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|httpPolicy
operator|==
literal|null
condition|)
block|{
name|String
name|host
init|=
name|configuration
operator|.
name|getProxyHost
argument_list|()
decl_stmt|;
name|Integer
name|port
init|=
name|configuration
operator|.
name|getProxyPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
operator|&&
name|port
operator|!=
literal|null
condition|)
block|{
name|httpPolicy
operator|=
operator|new
name|HTTPClientPolicy
argument_list|()
expr_stmt|;
name|httpPolicy
operator|.
name|setProxyServer
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|httpPolicy
operator|.
name|setProxyServerPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|httpPolicy
operator|!=
literal|null
condition|)
block|{
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|setClient
argument_list|(
name|httpPolicy
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureProxyAuthorizationPolicy ( CamelContext context, ServiceNowConfiguration configuration, WebClient client)
specifier|private
specifier|static
name|void
name|configureProxyAuthorizationPolicy
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ServiceNowConfiguration
name|configuration
parameter_list|,
name|WebClient
name|client
parameter_list|)
throws|throws
name|Exception
block|{
name|ProxyAuthorizationPolicy
name|proxyPolicy
init|=
name|configuration
operator|.
name|getProxyAuthorizationPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|proxyPolicy
operator|==
literal|null
condition|)
block|{
name|String
name|username
init|=
name|configuration
operator|.
name|getProxyUserName
argument_list|()
decl_stmt|;
name|String
name|password
init|=
name|configuration
operator|.
name|getProxyPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|proxyPolicy
operator|=
operator|new
name|ProxyAuthorizationPolicy
argument_list|()
expr_stmt|;
name|proxyPolicy
operator|.
name|setAuthorizationType
argument_list|(
literal|"Basic"
argument_list|)
expr_stmt|;
name|proxyPolicy
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|proxyPolicy
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|proxyPolicy
operator|!=
literal|null
condition|)
block|{
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|setProxyAuthorization
argument_list|(
name|proxyPolicy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

