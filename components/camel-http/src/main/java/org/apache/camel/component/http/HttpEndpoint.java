begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|Iterator
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
name|commons
operator|.
name|httpclient
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
name|commons
operator|.
name|httpclient
operator|.
name|HttpConnectionManager
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
name|httpclient
operator|.
name|auth
operator|.
name|AuthPolicy
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
name|httpclient
operator|.
name|params
operator|.
name|HttpClientParams
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
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"http,https"
argument_list|,
name|title
operator|=
literal|"HTTP,HTTPS"
argument_list|,
name|syntax
operator|=
literal|"http:httpUri"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"http"
argument_list|)
DECL|class|HttpEndpoint
specifier|public
class|class
name|HttpEndpoint
extends|extends
name|HttpCommonEndpoint
block|{
comment|// Note: all options must be documented with description in annotations so extended components can access the documentation
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
DECL|field|clientParams
specifier|private
name|HttpClientParams
name|clientParams
decl_stmt|;
DECL|field|httpClientConfigurer
specifier|private
name|HttpClientConfigurer
name|httpClientConfigurer
decl_stmt|;
DECL|field|httpConnectionManager
specifier|private
name|HttpConnectionManager
name|httpConnectionManager
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
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, URI httpURI, HttpConnectionManager httpConnectionManager)
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
name|HttpConnectionManager
name|httpConnectionManager
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
name|HttpClientParams
argument_list|()
argument_list|,
name|httpConnectionManager
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, HttpClientParams clientParams, HttpConnectionManager httpConnectionManager, HttpClientConfigurer clientConfigurer)
specifier|public
name|HttpEndpoint
parameter_list|(
name|String
name|endPointURI
parameter_list|,
name|HttpComponent
name|component
parameter_list|,
name|HttpClientParams
name|clientParams
parameter_list|,
name|HttpConnectionManager
name|httpConnectionManager
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
name|clientParams
argument_list|,
name|httpConnectionManager
argument_list|,
name|clientConfigurer
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpEndpoint (String endPointURI, HttpComponent component, URI httpURI, HttpClientParams clientParams, HttpConnectionManager httpConnectionManager, HttpClientConfigurer clientConfigurer)
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
name|HttpClientParams
name|clientParams
parameter_list|,
name|HttpConnectionManager
name|httpConnectionManager
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
name|component
operator|=
name|component
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
name|httpConnectionManager
operator|=
name|httpConnectionManager
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
comment|/**      * Factory method used by producers and consumers to create a new {@link HttpClient} instance      */
DECL|method|createHttpClient ()
specifier|public
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
name|httpConnectionManager
argument_list|,
literal|"httpConnectionManager"
argument_list|)
expr_stmt|;
name|HttpClient
name|answer
init|=
operator|new
name|HttpClient
argument_list|(
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"CamelContext properties http.proxyHost and http.proxyPort detected. Using http proxy host: {} port: {}"
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|answer
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|setProxy
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|proxyHost
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using proxy: {}:{}"
argument_list|,
name|proxyHost
argument_list|,
name|proxyPort
argument_list|)
expr_stmt|;
name|answer
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|setProxy
argument_list|(
name|proxyHost
argument_list|,
name|proxyPort
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|authMethodPriority
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|authPrefs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Iterator
operator|.
name|class
argument_list|,
name|authMethodPriority
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|AuthMethod
name|auth
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|AuthMethod
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|auth
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown authMethod: "
operator|+
name|value
operator|+
literal|" in authMethodPriority: "
operator|+
name|authMethodPriority
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using authSchemePriority #{}: {}"
argument_list|,
name|i
argument_list|,
name|auth
argument_list|)
expr_stmt|;
name|authPrefs
operator|.
name|add
argument_list|(
name|auth
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|authPrefs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|answer
operator|.
name|getParams
argument_list|()
operator|.
name|setParameter
argument_list|(
name|AuthPolicy
operator|.
name|AUTH_SCHEME_PRIORITY
argument_list|,
name|authPrefs
argument_list|)
expr_stmt|;
block|}
block|}
name|answer
operator|.
name|setHttpConnectionManager
argument_list|(
name|httpConnectionManager
argument_list|)
expr_stmt|;
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
return|return
name|answer
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
comment|/**      * Provide access to the client parameters used on new {@link HttpClient} instances      * used by producers or consumers of this endpoint.      */
DECL|method|getClientParams ()
specifier|public
name|HttpClientParams
name|getClientParams
parameter_list|()
block|{
return|return
name|clientParams
return|;
block|}
comment|/**      * Provide access to the client parameters used on new {@link HttpClient} instances      * used by producers or consumers of this endpoint.      */
DECL|method|setClientParams (HttpClientParams clientParams)
specifier|public
name|void
name|setClientParams
parameter_list|(
name|HttpClientParams
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
DECL|method|getHttpConnectionManager ()
specifier|public
name|HttpConnectionManager
name|getHttpConnectionManager
parameter_list|()
block|{
return|return
name|httpConnectionManager
return|;
block|}
comment|/**      * To use a custom HttpConnectionManager to manage connections      */
DECL|method|setHttpConnectionManager (HttpConnectionManager httpConnectionManager)
specifier|public
name|void
name|setHttpConnectionManager
parameter_list|(
name|HttpConnectionManager
name|httpConnectionManager
parameter_list|)
block|{
name|this
operator|.
name|httpConnectionManager
operator|=
name|httpConnectionManager
expr_stmt|;
block|}
block|}
end_class

end_unit

