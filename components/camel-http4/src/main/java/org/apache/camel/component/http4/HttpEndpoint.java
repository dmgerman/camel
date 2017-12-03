begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|util
operator|.
name|Map
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
name|HostnameVerifier
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
name|Consumer
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
name|PollingConsumer
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
name|http
operator|.
name|common
operator|.
name|HttpCommonEndpoint
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
name|http
operator|.
name|common
operator|.
name|HttpHelper
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
name|http
operator|.
name|common
operator|.
name|cookie
operator|.
name|CookieHandler
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
name|UriEndpoint
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
name|UriParam
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
name|IOHelper
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
name|http
operator|.
name|HttpHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|CookieStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|config
operator|.
name|RequestConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|HttpClientConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|ssl
operator|.
name|DefaultHostnameVerifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|BasicCookieStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
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
comment|/**  * For calling out to external HTTP servers using Apache HTTP Client 4.x.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.3.0"
argument_list|,
name|scheme
operator|=
literal|"http4,https4"
argument_list|,
name|title
operator|=
literal|"HTTP4,HTTPS4"
argument_list|,
name|syntax
operator|=
literal|"http4:httpUri"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"http"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|HttpEndpoint
specifier|public
class|class
name|HttpEndpoint
extends|extends
name|HttpCommonEndpoint
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
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom HttpContext instance"
argument_list|)
DECL|field|httpContext
specifier|private
name|HttpContext
name|httpContext
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Register a custom configuration strategy for new HttpClient instances"
operator|+
literal|" created by producers or consumers such as to configure authentication mechanisms etc."
argument_list|)
DECL|field|httpClientConfigurer
specifier|private
name|HttpClientConfigurer
name|httpClientConfigurer
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|prefix
operator|=
literal|"httpClient."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"To configure the HttpClient using the key/values from the Map."
argument_list|)
DECL|field|httpClientOptions
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientOptions
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom HttpClientConnectionManager to manage connections"
argument_list|)
DECL|field|clientConnectionManager
specifier|private
name|HttpClientConnectionManager
name|clientConnectionManager
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Provide access to the http client request parameters used on new RequestConfig instances used by producers or consumers of this endpoint."
argument_list|)
DECL|field|clientBuilder
specifier|private
name|HttpClientBuilder
name|clientBuilder
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Sets a custom HttpClient to be used by the producer"
argument_list|)
DECL|field|httpClient
specifier|private
name|HttpClient
name|httpClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|,
name|description
operator|=
literal|"To use System Properties as fallback for configuration"
argument_list|)
DECL|field|useSystemProperties
specifier|private
name|boolean
name|useSystemProperties
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"To use a custom CookieStore."
operator|+
literal|" By default the BasicCookieStore is used which is an in-memory only cookie store."
operator|+
literal|" Notice if bridgeEndpoint=true then the cookie store is forced to be a noop cookie store as cookie shouldn't be stored as we are just bridging (eg acting as a proxy)."
operator|+
literal|" If a cookieHandler is set then the cookie store is also forced to be a noop cookie store as cookie handling is then performed by the cookieHandler."
argument_list|)
DECL|field|cookieStore
specifier|private
name|CookieStore
name|cookieStore
init|=
operator|new
name|BasicCookieStore
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Whether to clear expired cookies before sending the HTTP request."
operator|+
literal|" This ensures the cookies store does not keep growing by adding new cookies which is newer removed when they are expired."
argument_list|)
DECL|field|clearExpiredCookies
specifier|private
name|boolean
name|clearExpiredCookies
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"If this option is true, camel-http4 sends preemptive basic authentication to the server."
argument_list|)
DECL|field|authenticationPreemptive
specifier|private
name|boolean
name|authenticationPreemptive
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Whether the HTTP DELETE should include the message body or not."
operator|+
literal|" By default HTTP DELETE do not include any HTTP message. However in some rare cases users may need to be able to include the message body."
argument_list|)
DECL|field|deleteWithBody
specifier|private
name|boolean
name|deleteWithBody
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"The maximum number of connections."
argument_list|)
DECL|field|maxTotalConnections
specifier|private
name|int
name|maxTotalConnections
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"20"
argument_list|,
name|description
operator|=
literal|"The maximum number of connections per route."
argument_list|)
DECL|field|connectionsPerRoute
specifier|private
name|int
name|connectionsPerRoute
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|description
operator|=
literal|"To use a custom X509HostnameVerifier such as DefaultHostnameVerifier or NoopHostnameVerifier"
argument_list|)
DECL|field|x509HostnameVerifier
specifier|private
name|HostnameVerifier
name|x509HostnameVerifier
decl_stmt|;
DECL|method|HttpEndpoint ()
specifier|public
name|HttpEndpoint
parameter_list|()
block|{     }
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, URI httpURI)
specifier|public
name|HttpEndpoint
parameter_list|(
name|String
name|endPointURI
parameter_list|,
name|HttpComponent
name|component
parameter_list|,
name|URI
name|httpURI
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|this
argument_list|(
name|endPointURI
argument_list|,
name|component
argument_list|,
name|httpURI
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, URI httpURI, HttpClientConnectionManager clientConnectionManager)
specifier|public
name|HttpEndpoint
parameter_list|(
name|String
name|endPointURI
parameter_list|,
name|HttpComponent
name|component
parameter_list|,
name|URI
name|httpURI
parameter_list|,
name|HttpClientConnectionManager
name|clientConnectionManager
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|this
argument_list|(
name|endPointURI
argument_list|,
name|component
argument_list|,
name|httpURI
argument_list|,
name|HttpClientBuilder
operator|.
name|create
argument_list|()
argument_list|,
name|clientConnectionManager
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, HttpClientBuilder clientBuilder, HttpClientConnectionManager clientConnectionManager, HttpClientConfigurer clientConfigurer)
specifier|public
name|HttpEndpoint
parameter_list|(
name|String
name|endPointURI
parameter_list|,
name|HttpComponent
name|component
parameter_list|,
name|HttpClientBuilder
name|clientBuilder
parameter_list|,
name|HttpClientConnectionManager
name|clientConnectionManager
parameter_list|,
name|HttpClientConfigurer
name|clientConfigurer
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|this
argument_list|(
name|endPointURI
argument_list|,
name|component
argument_list|,
literal|null
argument_list|,
name|clientBuilder
argument_list|,
name|clientConnectionManager
argument_list|,
name|clientConfigurer
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, URI httpURI, HttpClientBuilder clientBuilder, HttpClientConnectionManager clientConnectionManager, HttpClientConfigurer clientConfigurer)
specifier|public
name|HttpEndpoint
parameter_list|(
name|String
name|endPointURI
parameter_list|,
name|HttpComponent
name|component
parameter_list|,
name|URI
name|httpURI
parameter_list|,
name|HttpClientBuilder
name|clientBuilder
parameter_list|,
name|HttpClientConnectionManager
name|clientConnectionManager
parameter_list|,
name|HttpClientConfigurer
name|clientConfigurer
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endPointURI
argument_list|,
name|component
argument_list|,
name|httpURI
argument_list|)
expr_stmt|;
name|this
operator|.
name|clientBuilder
operator|=
name|clientBuilder
expr_stmt|;
name|this
operator|.
name|httpClientConfigurer
operator|=
name|clientConfigurer
expr_stmt|;
name|this
operator|.
name|clientConnectionManager
operator|=
name|clientConnectionManager
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|HttpProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot consume from http endpoint"
argument_list|)
throw|;
block|}
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpPollingConsumer
name|answer
init|=
operator|new
name|HttpPollingConsumer
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|configurePollingConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getHttpClient ()
specifier|public
specifier|synchronized
name|HttpClient
name|getHttpClient
parameter_list|()
block|{
if|if
condition|(
name|httpClient
operator|==
literal|null
condition|)
block|{
name|httpClient
operator|=
name|createHttpClient
argument_list|()
expr_stmt|;
block|}
return|return
name|httpClient
return|;
block|}
comment|/**      * Sets a custom HttpClient to be used by the producer      */
DECL|method|setHttpClient (HttpClient httpClient)
specifier|public
name|void
name|setHttpClient
parameter_list|(
name|HttpClient
name|httpClient
parameter_list|)
block|{
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
block|}
comment|/**      * Factory method to create a new {@link HttpClient} instance      *<p/>      * Producers and consumers should use the {@link #getHttpClient()} method instead.      */
DECL|method|createHttpClient ()
specifier|protected
name|HttpClient
name|createHttpClient
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clientBuilder
argument_list|,
literal|"httpClientBuilder"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clientConnectionManager
argument_list|,
literal|"httpConnectionManager"
argument_list|)
expr_stmt|;
comment|// setup the cookieStore
name|clientBuilder
operator|.
name|setDefaultCookieStore
argument_list|(
name|cookieStore
argument_list|)
expr_stmt|;
comment|// setup the httpConnectionManager
name|clientBuilder
operator|.
name|setConnectionManager
argument_list|(
name|clientConnectionManager
argument_list|)
expr_stmt|;
if|if
condition|(
name|getComponent
argument_list|()
operator|!=
literal|null
operator|&&
name|getComponent
argument_list|()
operator|.
name|getClientConnectionManager
argument_list|()
operator|==
name|getClientConnectionManager
argument_list|()
condition|)
block|{
name|clientBuilder
operator|.
name|setConnectionManagerShared
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|useSystemProperties
condition|)
block|{
comment|// configure http proxy from camelContext
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"http.proxyHost"
argument_list|)
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPort"
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|host
init|=
name|getCamelContext
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"http.proxyHost"
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPort"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|scheme
init|=
name|getCamelContext
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"http.proxyScheme"
argument_list|)
decl_stmt|;
comment|// fallback and use either http or https depending on secure
if|if
condition|(
name|scheme
operator|==
literal|null
condition|)
block|{
name|scheme
operator|=
name|HttpHelper
operator|.
name|isSecureConnection
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
condition|?
literal|"https"
else|:
literal|"http"
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"CamelContext properties http.proxyHost, http.proxyPort, and http.proxyScheme detected. Using http proxy host: {} port: {} scheme: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|host
block|,
name|port
block|,
name|scheme
block|}
argument_list|)
expr_stmt|;
name|HttpHost
name|proxy
init|=
operator|new
name|HttpHost
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|scheme
argument_list|)
decl_stmt|;
name|clientBuilder
operator|.
name|setProxy
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|clientBuilder
operator|.
name|useSystemProperties
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isAuthenticationPreemptive
argument_list|()
condition|)
block|{
comment|// setup the PreemptiveAuthInterceptor here
name|clientBuilder
operator|.
name|addInterceptorFirst
argument_list|(
operator|new
name|PreemptiveAuthInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|HttpClientConfigurer
name|configurer
init|=
name|getHttpClientConfigurer
argument_list|()
decl_stmt|;
if|if
condition|(
name|configurer
operator|!=
literal|null
condition|)
block|{
name|configurer
operator|.
name|configureHttpClient
argument_list|(
name|clientBuilder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
comment|// need to use noop cookiestore as we do not want to keep cookies in memory
name|clientBuilder
operator|.
name|setDefaultCookieStore
argument_list|(
operator|new
name|NoopCookieStore
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Setup the HttpClientBuilder {}"
argument_list|,
name|clientBuilder
argument_list|)
expr_stmt|;
return|return
name|clientBuilder
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|HttpComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|HttpComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
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
if|if
condition|(
name|getComponent
argument_list|()
operator|!=
literal|null
operator|&&
name|getComponent
argument_list|()
operator|.
name|getClientConnectionManager
argument_list|()
operator|!=
name|clientConnectionManager
condition|)
block|{
comment|// need to shutdown the ConnectionManager
name|clientConnectionManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|httpClient
operator|instanceof
name|Closeable
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
operator|(
name|Closeable
operator|)
name|httpClient
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getClientBuilder ()
specifier|public
name|HttpClientBuilder
name|getClientBuilder
parameter_list|()
block|{
return|return
name|clientBuilder
return|;
block|}
comment|/**      * Provide access to the http client request parameters used on new {@link RequestConfig} instances      * used by producers or consumers of this endpoint.      */
DECL|method|setClientBuilder (HttpClientBuilder clientBuilder)
specifier|public
name|void
name|setClientBuilder
parameter_list|(
name|HttpClientBuilder
name|clientBuilder
parameter_list|)
block|{
name|this
operator|.
name|clientBuilder
operator|=
name|clientBuilder
expr_stmt|;
block|}
DECL|method|getHttpClientConfigurer ()
specifier|public
name|HttpClientConfigurer
name|getHttpClientConfigurer
parameter_list|()
block|{
return|return
name|httpClientConfigurer
return|;
block|}
comment|/**      * Register a custom configuration strategy for new {@link HttpClient} instances      * created by producers or consumers such as to configure authentication mechanisms etc      */
DECL|method|setHttpClientConfigurer (HttpClientConfigurer httpClientConfigurer)
specifier|public
name|void
name|setHttpClientConfigurer
parameter_list|(
name|HttpClientConfigurer
name|httpClientConfigurer
parameter_list|)
block|{
name|this
operator|.
name|httpClientConfigurer
operator|=
name|httpClientConfigurer
expr_stmt|;
block|}
DECL|method|getHttpContext ()
specifier|public
name|HttpContext
name|getHttpContext
parameter_list|()
block|{
return|return
name|httpContext
return|;
block|}
comment|/**      * To use a custom HttpContext instance      */
DECL|method|setHttpContext (HttpContext httpContext)
specifier|public
name|void
name|setHttpContext
parameter_list|(
name|HttpContext
name|httpContext
parameter_list|)
block|{
name|this
operator|.
name|httpContext
operator|=
name|httpContext
expr_stmt|;
block|}
DECL|method|getClientConnectionManager ()
specifier|public
name|HttpClientConnectionManager
name|getClientConnectionManager
parameter_list|()
block|{
return|return
name|clientConnectionManager
return|;
block|}
comment|/**      * To use a custom HttpClientConnectionManager to manage connections      */
DECL|method|setClientConnectionManager (HttpClientConnectionManager clientConnectionManager)
specifier|public
name|void
name|setClientConnectionManager
parameter_list|(
name|HttpClientConnectionManager
name|clientConnectionManager
parameter_list|)
block|{
name|this
operator|.
name|clientConnectionManager
operator|=
name|clientConnectionManager
expr_stmt|;
block|}
DECL|method|isClearExpiredCookies ()
specifier|public
name|boolean
name|isClearExpiredCookies
parameter_list|()
block|{
return|return
name|clearExpiredCookies
return|;
block|}
comment|/**      * Whether to clear expired cookies before sending the HTTP request.      * This ensures the cookies store does not keep growing by adding new cookies which is newer removed when they are expired.      */
DECL|method|setClearExpiredCookies (boolean clearExpiredCookies)
specifier|public
name|void
name|setClearExpiredCookies
parameter_list|(
name|boolean
name|clearExpiredCookies
parameter_list|)
block|{
name|this
operator|.
name|clearExpiredCookies
operator|=
name|clearExpiredCookies
expr_stmt|;
block|}
DECL|method|isDeleteWithBody ()
specifier|public
name|boolean
name|isDeleteWithBody
parameter_list|()
block|{
return|return
name|deleteWithBody
return|;
block|}
comment|/**      * Whether the HTTP DELETE should include the message body or not.      *<p/>      * By default HTTP DELETE do not include any HTTP message. However in some rare cases users may need to be able to include the      * message body.      */
DECL|method|setDeleteWithBody (boolean deleteWithBody)
specifier|public
name|void
name|setDeleteWithBody
parameter_list|(
name|boolean
name|deleteWithBody
parameter_list|)
block|{
name|this
operator|.
name|deleteWithBody
operator|=
name|deleteWithBody
expr_stmt|;
block|}
DECL|method|getCookieStore ()
specifier|public
name|CookieStore
name|getCookieStore
parameter_list|()
block|{
return|return
name|cookieStore
return|;
block|}
comment|/**      * To use a custom CookieStore.      * By default the BasicCookieStore is used which is an in-memory only cookie store.      * Notice if bridgeEndpoint=true then the cookie store is forced to be a noop cookie store as cookie      * shouldn't be stored as we are just bridging (eg acting as a proxy).      * If a cookieHandler is set then the cookie store is also forced to be a noop cookie store as cookie handling is      * then performed by the cookieHandler.      */
DECL|method|setCookieStore (CookieStore cookieStore)
specifier|public
name|void
name|setCookieStore
parameter_list|(
name|CookieStore
name|cookieStore
parameter_list|)
block|{
name|this
operator|.
name|cookieStore
operator|=
name|cookieStore
expr_stmt|;
block|}
DECL|method|setCookieHandler (CookieHandler cookieHandler)
specifier|public
name|void
name|setCookieHandler
parameter_list|(
name|CookieHandler
name|cookieHandler
parameter_list|)
block|{
name|super
operator|.
name|setCookieHandler
argument_list|(
name|cookieHandler
argument_list|)
expr_stmt|;
comment|// if we set an explicit cookie handler
name|this
operator|.
name|cookieStore
operator|=
operator|new
name|NoopCookieStore
argument_list|()
expr_stmt|;
block|}
DECL|method|isAuthenticationPreemptive ()
specifier|public
name|boolean
name|isAuthenticationPreemptive
parameter_list|()
block|{
return|return
name|authenticationPreemptive
return|;
block|}
comment|/**      * If this option is true, camel-http4 sends preemptive basic authentication to the server.      */
DECL|method|setAuthenticationPreemptive (boolean authenticationPreemptive)
specifier|public
name|void
name|setAuthenticationPreemptive
parameter_list|(
name|boolean
name|authenticationPreemptive
parameter_list|)
block|{
name|this
operator|.
name|authenticationPreemptive
operator|=
name|authenticationPreemptive
expr_stmt|;
block|}
DECL|method|getHttpClientOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHttpClientOptions
parameter_list|()
block|{
return|return
name|httpClientOptions
return|;
block|}
comment|/**      * To configure the HttpClient using the key/values from the Map.      */
DECL|method|setHttpClientOptions (Map<String, Object> httpClientOptions)
specifier|public
name|void
name|setHttpClientOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientOptions
parameter_list|)
block|{
name|this
operator|.
name|httpClientOptions
operator|=
name|httpClientOptions
expr_stmt|;
block|}
DECL|method|isUseSystemProperties ()
specifier|public
name|boolean
name|isUseSystemProperties
parameter_list|()
block|{
return|return
name|useSystemProperties
return|;
block|}
comment|/**      * To use System Properties as fallback for configuration      */
DECL|method|setUseSystemProperties (boolean useSystemProperties)
specifier|public
name|void
name|setUseSystemProperties
parameter_list|(
name|boolean
name|useSystemProperties
parameter_list|)
block|{
name|this
operator|.
name|useSystemProperties
operator|=
name|useSystemProperties
expr_stmt|;
block|}
DECL|method|getMaxTotalConnections ()
specifier|public
name|int
name|getMaxTotalConnections
parameter_list|()
block|{
return|return
name|maxTotalConnections
return|;
block|}
comment|/**      * The maximum number of connections.      */
DECL|method|setMaxTotalConnections (int maxTotalConnections)
specifier|public
name|void
name|setMaxTotalConnections
parameter_list|(
name|int
name|maxTotalConnections
parameter_list|)
block|{
name|this
operator|.
name|maxTotalConnections
operator|=
name|maxTotalConnections
expr_stmt|;
block|}
DECL|method|getConnectionsPerRoute ()
specifier|public
name|int
name|getConnectionsPerRoute
parameter_list|()
block|{
return|return
name|connectionsPerRoute
return|;
block|}
comment|/**      * The maximum number of connections per route.      */
DECL|method|setConnectionsPerRoute (int connectionsPerRoute)
specifier|public
name|void
name|setConnectionsPerRoute
parameter_list|(
name|int
name|connectionsPerRoute
parameter_list|)
block|{
name|this
operator|.
name|connectionsPerRoute
operator|=
name|connectionsPerRoute
expr_stmt|;
block|}
DECL|method|getX509HostnameVerifier ()
specifier|public
name|HostnameVerifier
name|getX509HostnameVerifier
parameter_list|()
block|{
return|return
name|x509HostnameVerifier
return|;
block|}
comment|/**      * To use a custom X509HostnameVerifier such as {@link DefaultHostnameVerifier}      * or {@link org.apache.http.conn.ssl.NoopHostnameVerifier}.      */
DECL|method|setX509HostnameVerifier (HostnameVerifier x509HostnameVerifier)
specifier|public
name|void
name|setX509HostnameVerifier
parameter_list|(
name|HostnameVerifier
name|x509HostnameVerifier
parameter_list|)
block|{
name|this
operator|.
name|x509HostnameVerifier
operator|=
name|x509HostnameVerifier
expr_stmt|;
block|}
block|}
end_class

end_unit

