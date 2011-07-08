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
name|http4
operator|.
name|helper
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
name|impl
operator|.
name|DefaultPollingEndpoint
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
name|HeaderFilterStrategy
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
name|HeaderFilterStrategyAware
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
name|conn
operator|.
name|ClientConnectionManager
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
name|params
operator|.
name|ConnRoutePNames
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
name|DefaultHttpClient
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
name|params
operator|.
name|BasicHttpParams
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
name|params
operator|.
name|HttpParams
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
comment|/**  * Represents a<a href="http://camel.apache.org/http.html">HTTP endpoint</a>  *  * @version   */
end_comment

begin_class
DECL|class|HttpEndpoint
specifier|public
class|class
name|HttpEndpoint
extends|extends
name|DefaultPollingEndpoint
implements|implements
name|HeaderFilterStrategyAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
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
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|HttpHeaderFilterStrategy
argument_list|()
decl_stmt|;
DECL|field|binding
specifier|private
name|HttpBinding
name|binding
decl_stmt|;
DECL|field|component
specifier|private
name|HttpComponent
name|component
decl_stmt|;
DECL|field|httpUri
specifier|private
name|URI
name|httpUri
decl_stmt|;
DECL|field|clientParams
specifier|private
name|HttpParams
name|clientParams
decl_stmt|;
DECL|field|httpClientConfigurer
specifier|private
name|HttpClientConfigurer
name|httpClientConfigurer
decl_stmt|;
DECL|field|clientConnectionManager
specifier|private
name|ClientConnectionManager
name|clientConnectionManager
decl_stmt|;
DECL|field|httpClient
specifier|private
name|HttpClient
name|httpClient
decl_stmt|;
DECL|field|throwExceptionOnFailure
specifier|private
name|boolean
name|throwExceptionOnFailure
init|=
literal|true
decl_stmt|;
DECL|field|bridgeEndpoint
specifier|private
name|boolean
name|bridgeEndpoint
decl_stmt|;
DECL|field|matchOnUriPrefix
specifier|private
name|boolean
name|matchOnUriPrefix
decl_stmt|;
DECL|field|chunked
specifier|private
name|boolean
name|chunked
init|=
literal|true
decl_stmt|;
DECL|field|disableStreamCache
specifier|private
name|boolean
name|disableStreamCache
decl_stmt|;
DECL|field|transferException
specifier|private
name|boolean
name|transferException
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
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, URI httpURI, ClientConnectionManager clientConnectionManager)
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
name|ClientConnectionManager
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
operator|new
name|BasicHttpParams
argument_list|()
argument_list|,
name|clientConnectionManager
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, URI httpURI, HttpParams clientParams, ClientConnectionManager clientConnectionManager, HttpClientConfigurer clientConfigurer)
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
name|HttpParams
name|clientParams
parameter_list|,
name|ClientConnectionManager
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|httpUri
operator|=
name|httpURI
expr_stmt|;
name|this
operator|.
name|clientParams
operator|=
name|clientParams
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
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|HttpPollingConsumer
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Gets the HttpClient to be used by {@link org.apache.camel.component.http4.HttpProducer}      */
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
name|clientParams
argument_list|,
literal|"clientParams"
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
name|HttpClient
name|answer
init|=
operator|new
name|DefaultHttpClient
argument_list|(
name|clientConnectionManager
argument_list|,
name|getClientParams
argument_list|()
argument_list|)
decl_stmt|;
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
name|getProperties
argument_list|()
operator|.
name|get
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
name|getProperties
argument_list|()
operator|.
name|get
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
name|getProperties
argument_list|()
operator|.
name|get
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
name|getProperties
argument_list|()
operator|.
name|get
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
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"http.proxyScheme"
argument_list|)
decl_stmt|;
comment|// fallback and use either http4 or https4 depending on secure
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
literal|"https4"
else|:
literal|"http4"
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
name|answer
operator|.
name|getParams
argument_list|()
operator|.
name|setParameter
argument_list|(
name|ConnRoutePNames
operator|.
name|DEFAULT_PROXY
argument_list|,
name|proxy
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
name|answer
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created HttpClient {}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|connect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|disconnect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// true to allow dynamic URI options to be configured and passed to external system for eg. the HttpProducer
return|return
literal|true
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
comment|/**      * Provide access to the client parameters used on new {@link HttpClient} instances      * used by producers or consumers of this endpoint.      */
DECL|method|getClientParams ()
specifier|public
name|HttpParams
name|getClientParams
parameter_list|()
block|{
return|return
name|clientParams
return|;
block|}
comment|/**      * Provide access to the client parameters used on new {@link HttpClient} instances      * used by producers or consumers of this endpoint.      */
DECL|method|setClientParams (HttpParams clientParams)
specifier|public
name|void
name|setClientParams
parameter_list|(
name|HttpParams
name|clientParams
parameter_list|)
block|{
name|this
operator|.
name|clientParams
operator|=
name|clientParams
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
comment|/**      * Register a custom configuration strategy for new {@link HttpClient} instances      * created by producers or consumers such as to configure authentication mechanisms etc      *      * @param httpClientConfigurer the strategy for configuring new {@link HttpClient} instances      */
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
DECL|method|getBinding ()
specifier|public
name|HttpBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|DefaultHttpBinding
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
DECL|method|setBinding (HttpBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|HttpBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
comment|/**      * Used from the IntrospectionSupport in HttpComponent.      * @param binding      */
DECL|method|setHttpBinding (HttpBinding binding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|HttpBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
comment|/**      * Used from the IntrospectionSupport in HttpComponent.      * @param binding      */
DECL|method|setHttpBindingRef (HttpBinding binding)
specifier|public
name|void
name|setHttpBindingRef
parameter_list|(
name|HttpBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|httpUri
operator|.
name|getPath
argument_list|()
return|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
if|if
condition|(
name|httpUri
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
literal|"https"
operator|.
name|equals
argument_list|(
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|443
return|;
block|}
else|else
block|{
return|return
literal|80
return|;
block|}
block|}
return|return
name|httpUri
operator|.
name|getPort
argument_list|()
return|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|httpUri
operator|.
name|getScheme
argument_list|()
return|;
block|}
DECL|method|getHttpUri ()
specifier|public
name|URI
name|getHttpUri
parameter_list|()
block|{
return|return
name|httpUri
return|;
block|}
DECL|method|setHttpUri (URI httpUri)
specifier|public
name|void
name|setHttpUri
parameter_list|(
name|URI
name|httpUri
parameter_list|)
block|{
name|this
operator|.
name|httpUri
operator|=
name|httpUri
expr_stmt|;
block|}
DECL|method|getClientConnectionManager ()
specifier|public
name|ClientConnectionManager
name|getClientConnectionManager
parameter_list|()
block|{
return|return
name|clientConnectionManager
return|;
block|}
DECL|method|setClientConnectionManager (ClientConnectionManager clientConnectionManager)
specifier|public
name|void
name|setClientConnectionManager
parameter_list|(
name|ClientConnectionManager
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
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|isThrowExceptionOnFailure ()
specifier|public
name|boolean
name|isThrowExceptionOnFailure
parameter_list|()
block|{
return|return
name|throwExceptionOnFailure
return|;
block|}
DECL|method|setThrowExceptionOnFailure (boolean throwExceptionOnFailure)
specifier|public
name|void
name|setThrowExceptionOnFailure
parameter_list|(
name|boolean
name|throwExceptionOnFailure
parameter_list|)
block|{
name|this
operator|.
name|throwExceptionOnFailure
operator|=
name|throwExceptionOnFailure
expr_stmt|;
block|}
DECL|method|isBridgeEndpoint ()
specifier|public
name|boolean
name|isBridgeEndpoint
parameter_list|()
block|{
return|return
name|bridgeEndpoint
return|;
block|}
DECL|method|setBridgeEndpoint (boolean bridge)
specifier|public
name|void
name|setBridgeEndpoint
parameter_list|(
name|boolean
name|bridge
parameter_list|)
block|{
name|this
operator|.
name|bridgeEndpoint
operator|=
name|bridge
expr_stmt|;
block|}
DECL|method|isMatchOnUriPrefix ()
specifier|public
name|boolean
name|isMatchOnUriPrefix
parameter_list|()
block|{
return|return
name|matchOnUriPrefix
return|;
block|}
DECL|method|setMatchOnUriPrefix (boolean match)
specifier|public
name|void
name|setMatchOnUriPrefix
parameter_list|(
name|boolean
name|match
parameter_list|)
block|{
name|this
operator|.
name|matchOnUriPrefix
operator|=
name|match
expr_stmt|;
block|}
DECL|method|isDisableStreamCache ()
specifier|public
name|boolean
name|isDisableStreamCache
parameter_list|()
block|{
return|return
name|this
operator|.
name|disableStreamCache
return|;
block|}
DECL|method|setDisableStreamCache (boolean disable)
specifier|public
name|void
name|setDisableStreamCache
parameter_list|(
name|boolean
name|disable
parameter_list|)
block|{
name|this
operator|.
name|disableStreamCache
operator|=
name|disable
expr_stmt|;
block|}
DECL|method|isChunked ()
specifier|public
name|boolean
name|isChunked
parameter_list|()
block|{
return|return
name|this
operator|.
name|chunked
return|;
block|}
DECL|method|setChunked (boolean chunked)
specifier|public
name|void
name|setChunked
parameter_list|(
name|boolean
name|chunked
parameter_list|)
block|{
name|this
operator|.
name|chunked
operator|=
name|chunked
expr_stmt|;
block|}
DECL|method|isTransferException ()
specifier|public
name|boolean
name|isTransferException
parameter_list|()
block|{
return|return
name|transferException
return|;
block|}
DECL|method|setTransferException (boolean transferException)
specifier|public
name|void
name|setTransferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
block|{
name|this
operator|.
name|transferException
operator|=
name|transferException
expr_stmt|;
block|}
block|}
end_class

end_unit

