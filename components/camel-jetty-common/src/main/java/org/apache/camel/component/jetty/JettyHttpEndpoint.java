begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|servlet
operator|.
name|Filter
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
name|ResolveEndpointFailedException
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
name|HttpConsumer
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
name|SynchronousDelegateProducer
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
name|eclipse
operator|.
name|jetty
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Handler
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JettyHttpEndpoint
specifier|public
specifier|abstract
class|class
name|JettyHttpEndpoint
extends|extends
name|HttpCommonEndpoint
block|{
DECL|field|handlers
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
decl_stmt|;
DECL|field|client
specifier|private
name|HttpClient
name|client
decl_stmt|;
DECL|field|multipartFilter
specifier|private
name|Filter
name|multipartFilter
decl_stmt|;
DECL|field|filters
specifier|private
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
decl_stmt|;
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|field|httpClientParameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientParameters
decl_stmt|;
DECL|field|jettyBinding
specifier|private
name|JettyHttpBinding
name|jettyBinding
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Specifies whether to enable the session manager on the server side of Jetty."
argument_list|)
DECL|field|sessionSupport
specifier|private
name|boolean
name|sessionSupport
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
literal|"8"
argument_list|,
name|description
operator|=
literal|"To set a value for minimum number of threads in HttpClient thread pool."
operator|+
literal|" This setting override any setting configured on component level."
operator|+
literal|" Notice that both a min and max size must be configured. If not set it default to min 8 threads used in Jettys thread pool."
argument_list|)
DECL|field|httpClientMinThreads
specifier|private
name|Integer
name|httpClientMinThreads
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
literal|"254"
argument_list|,
name|description
operator|=
literal|"To set a value for maximum number of threads in HttpClient thread pool."
operator|+
literal|" This setting override any setting configured on component level."
operator|+
literal|" Notice that both a min and max size must be configured. If not set it default to max 254 threads used in Jettys thread pool."
argument_list|)
DECL|field|httpClientMaxThreads
specifier|private
name|Integer
name|httpClientMaxThreads
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"If this option is true, Jetty JMX support will be enabled for this endpoint. See Jetty JMX support for more details."
argument_list|)
DECL|field|enableJmx
specifier|private
name|boolean
name|enableJmx
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Whether Jetty org.eclipse.jetty.servlets.MultiPartFilter is enabled or not."
operator|+
literal|" You should set this value to false when bridging endpoints, to ensure multipart requests is proxied/bridged as well."
argument_list|)
DECL|field|enableMultipartFilter
specifier|private
name|boolean
name|enableMultipartFilter
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"If the option is true, jetty will send the server header with the jetty version information to the client which sends the request."
operator|+
literal|" NOTE please make sure there is no any other camel-jetty endpoint is share the same port, otherwise this option may not work as expected."
argument_list|)
DECL|field|sendServerVersion
specifier|private
name|boolean
name|sendServerVersion
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"If the option is true, jetty server will send the date header to the client which sends the request."
operator|+
literal|" NOTE please make sure there is no any other camel-jetty endpoint is share the same port, otherwise this option may not work as expected."
argument_list|)
DECL|field|sendDateHeader
specifier|private
name|boolean
name|sendDateHeader
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"30000"
argument_list|,
name|description
operator|=
literal|"Allows to set a timeout in millis when using Jetty as consumer (server)."
operator|+
literal|" By default Jetty uses 30000. You can use a value of<= 0 to never expire."
operator|+
literal|" If a timeout occurs then the request will be expired and Jetty will return back a http error 503 to the client."
operator|+
literal|" This option is only in use when using Jetty with the Asynchronous Routing Engine."
argument_list|)
DECL|field|continuationTimeout
specifier|private
name|Long
name|continuationTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Whether or not to use Jetty continuations for the Jetty Server."
argument_list|)
DECL|field|useContinuation
specifier|private
name|Boolean
name|useContinuation
decl_stmt|;
DECL|method|JettyHttpEndpoint (JettyHttpComponent component, String uri, URI httpURL)
specifier|public
name|JettyHttpEndpoint
parameter_list|(
name|JettyHttpComponent
name|component
parameter_list|,
name|String
name|uri
parameter_list|,
name|URI
name|httpURL
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|httpURL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|JettyHttpComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|JettyHttpComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|JettyHttpProducer
name|answer
init|=
operator|new
name|JettyHttpProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
comment|// use shared client, and ensure its started so we can use it
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
name|answer
operator|.
name|setSharedClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBinding
argument_list|(
name|getJettyBinding
argument_list|(
name|client
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|HttpClient
name|httpClient
init|=
name|createJettyHttpClient
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setClient
argument_list|(
name|httpClient
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBinding
argument_list|(
name|getJettyBinding
argument_list|(
name|httpClient
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|answer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
block|}
DECL|method|createJettyHttpClient ()
specifier|protected
name|HttpClient
name|createJettyHttpClient
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create a new client
comment|// thread pool min/max from endpoint take precedence over from component
name|Integer
name|min
init|=
name|httpClientMinThreads
operator|!=
literal|null
condition|?
name|httpClientMinThreads
else|:
name|getComponent
argument_list|()
operator|.
name|getHttpClientMinThreads
argument_list|()
decl_stmt|;
name|Integer
name|max
init|=
name|httpClientMaxThreads
operator|!=
literal|null
condition|?
name|httpClientMaxThreads
else|:
name|getComponent
argument_list|()
operator|.
name|getHttpClientMaxThreads
argument_list|()
decl_stmt|;
name|HttpClient
name|httpClient
init|=
name|getComponent
argument_list|()
operator|.
name|createHttpClient
argument_list|(
name|this
argument_list|,
name|min
argument_list|,
name|max
argument_list|,
name|sslContextParameters
argument_list|)
decl_stmt|;
comment|// set optional http client parameters
if|if
condition|(
name|httpClientParameters
operator|!=
literal|null
condition|)
block|{
comment|// copy parameters as we need to re-use them again if creating a new producer later
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|httpClientParameters
argument_list|)
decl_stmt|;
comment|// Can not be set on httpClient for jetty 9
name|params
operator|.
name|remove
argument_list|(
literal|"timeout"
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|httpClient
argument_list|,
name|params
argument_list|)
expr_stmt|;
comment|// validate we could set all parameters
if|if
condition|(
name|params
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|,
literal|"There are "
operator|+
name|params
operator|.
name|size
argument_list|()
operator|+
literal|" parameters that couldn't be set on the endpoint."
operator|+
literal|" Check the uri if the parameters are spelt correctly and that they are properties of the endpoint."
operator|+
literal|" Unknown parameters=["
operator|+
name|params
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
return|return
name|httpClient
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
name|HttpConsumer
name|answer
init|=
operator|new
name|HttpConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Specifies whether to enable the session manager on the server side of Jetty.      */
DECL|method|setSessionSupport (boolean support)
specifier|public
name|void
name|setSessionSupport
parameter_list|(
name|boolean
name|support
parameter_list|)
block|{
name|sessionSupport
operator|=
name|support
expr_stmt|;
block|}
DECL|method|isSessionSupport ()
specifier|public
name|boolean
name|isSessionSupport
parameter_list|()
block|{
return|return
name|sessionSupport
return|;
block|}
DECL|method|getHandlers ()
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|getHandlers
parameter_list|()
block|{
return|return
name|handlers
return|;
block|}
DECL|method|setHandlers (List<Handler> handlers)
specifier|public
name|void
name|setHandlers
parameter_list|(
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|handlers
operator|=
name|handlers
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|HttpClient
name|getClient
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|client
return|;
block|}
comment|/**      * Sets a shared {@link HttpClient} to use for all producers      * created by this endpoint. By default each producer will      * use a new http client, and not share.      *<p/>      *<b>Important:</b> Make sure to handle the lifecycle of the shared      * client, such as stopping the client, when it is no longer in use.      * Camel will call the<tt>start</tt> method on the client to ensure      * its started when this endpoint creates a producer.      *<p/>      * This options should only be used in special circumstances.      */
DECL|method|setClient (HttpClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|HttpClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getJettyBinding (HttpClient httpClient)
specifier|public
specifier|synchronized
name|JettyHttpBinding
name|getJettyBinding
parameter_list|(
name|HttpClient
name|httpClient
parameter_list|)
block|{
if|if
condition|(
name|jettyBinding
operator|==
literal|null
condition|)
block|{
name|jettyBinding
operator|=
operator|new
name|DefaultJettyHttpBinding
argument_list|()
expr_stmt|;
name|jettyBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|jettyBinding
operator|.
name|setThrowExceptionOnFailure
argument_list|(
name|isThrowExceptionOnFailure
argument_list|()
argument_list|)
expr_stmt|;
name|jettyBinding
operator|.
name|setTransferException
argument_list|(
name|isTransferException
argument_list|()
argument_list|)
expr_stmt|;
name|jettyBinding
operator|.
name|setOkStatusCodeRange
argument_list|(
name|getOkStatusCodeRange
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|jettyBinding
return|;
block|}
DECL|method|setJettyBinding (JettyHttpBinding jettyBinding)
specifier|public
name|void
name|setJettyBinding
parameter_list|(
name|JettyHttpBinding
name|jettyBinding
parameter_list|)
block|{
name|this
operator|.
name|jettyBinding
operator|=
name|jettyBinding
expr_stmt|;
block|}
DECL|method|isEnableJmx ()
specifier|public
name|boolean
name|isEnableJmx
parameter_list|()
block|{
return|return
name|this
operator|.
name|enableJmx
return|;
block|}
comment|/**      * If this option is true, Jetty JMX support will be enabled for this endpoint. See Jetty JMX support for more details.      */
DECL|method|setEnableJmx (boolean enableJmx)
specifier|public
name|void
name|setEnableJmx
parameter_list|(
name|boolean
name|enableJmx
parameter_list|)
block|{
name|this
operator|.
name|enableJmx
operator|=
name|enableJmx
expr_stmt|;
block|}
DECL|method|isSendServerVersion ()
specifier|public
name|boolean
name|isSendServerVersion
parameter_list|()
block|{
return|return
name|sendServerVersion
return|;
block|}
comment|/**      * If the option is true, jetty will send the server header with the jetty version information to the client which sends the request.      * NOTE please make sure there is no any other camel-jetty endpoint is share the same port, otherwise this option may not work as expected.      */
DECL|method|setSendServerVersion (boolean sendServerVersion)
specifier|public
name|void
name|setSendServerVersion
parameter_list|(
name|boolean
name|sendServerVersion
parameter_list|)
block|{
name|this
operator|.
name|sendServerVersion
operator|=
name|sendServerVersion
expr_stmt|;
block|}
DECL|method|isSendDateHeader ()
specifier|public
name|boolean
name|isSendDateHeader
parameter_list|()
block|{
return|return
name|sendDateHeader
return|;
block|}
comment|/**      * If the option is true, jetty server will send the date header to the client which sends the request.      * NOTE please make sure there is no any other camel-jetty endpoint is share the same port, otherwise this option may not work as expected.      */
DECL|method|setSendDateHeader (boolean sendDateHeader)
specifier|public
name|void
name|setSendDateHeader
parameter_list|(
name|boolean
name|sendDateHeader
parameter_list|)
block|{
name|this
operator|.
name|sendDateHeader
operator|=
name|sendDateHeader
expr_stmt|;
block|}
DECL|method|isEnableMultipartFilter ()
specifier|public
name|boolean
name|isEnableMultipartFilter
parameter_list|()
block|{
return|return
name|enableMultipartFilter
return|;
block|}
comment|/**      * Whether Jetty org.eclipse.jetty.servlets.MultiPartFilter is enabled or not.      * You should set this value to false when bridging endpoints, to ensure multipart requests is proxied/bridged as well.      */
DECL|method|setEnableMultipartFilter (boolean enableMultipartFilter)
specifier|public
name|void
name|setEnableMultipartFilter
parameter_list|(
name|boolean
name|enableMultipartFilter
parameter_list|)
block|{
name|this
operator|.
name|enableMultipartFilter
operator|=
name|enableMultipartFilter
expr_stmt|;
block|}
comment|/**      * Allows using a custom multipart filter. Note: setting multipartFilter forces the value of enableMultipartFilter to true.      */
DECL|method|setMultipartFilter (Filter filter)
specifier|public
name|void
name|setMultipartFilter
parameter_list|(
name|Filter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|multipartFilter
operator|=
name|filter
expr_stmt|;
block|}
DECL|method|getMultipartFilter ()
specifier|public
name|Filter
name|getMultipartFilter
parameter_list|()
block|{
return|return
name|multipartFilter
return|;
block|}
DECL|method|setFilters (List<Filter> filterList)
specifier|public
name|void
name|setFilters
parameter_list|(
name|List
argument_list|<
name|Filter
argument_list|>
name|filterList
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|=
name|filterList
expr_stmt|;
block|}
DECL|method|getFilters ()
specifier|public
name|List
argument_list|<
name|Filter
argument_list|>
name|getFilters
parameter_list|()
block|{
return|return
name|filters
return|;
block|}
DECL|method|getContinuationTimeout ()
specifier|public
name|Long
name|getContinuationTimeout
parameter_list|()
block|{
return|return
name|continuationTimeout
return|;
block|}
comment|/**      * Allows to set a timeout in millis when using Jetty as consumer (server).      * By default Jetty uses 30000. You can use a value of<= 0 to never expire.      * If a timeout occurs then the request will be expired and Jetty will return back a http error 503 to the client.      * This option is only in use when using Jetty with the Asynchronous Routing Engine.      */
DECL|method|setContinuationTimeout (Long continuationTimeout)
specifier|public
name|void
name|setContinuationTimeout
parameter_list|(
name|Long
name|continuationTimeout
parameter_list|)
block|{
name|this
operator|.
name|continuationTimeout
operator|=
name|continuationTimeout
expr_stmt|;
block|}
DECL|method|getUseContinuation ()
specifier|public
name|Boolean
name|getUseContinuation
parameter_list|()
block|{
return|return
name|useContinuation
return|;
block|}
comment|/**      * Whether or not to use Jetty continuations for the Jetty Server.      */
DECL|method|setUseContinuation (Boolean useContinuation)
specifier|public
name|void
name|setUseContinuation
parameter_list|(
name|Boolean
name|useContinuation
parameter_list|)
block|{
name|this
operator|.
name|useContinuation
operator|=
name|useContinuation
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * To configure security using SSLContextParameters      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getHttpClientMinThreads ()
specifier|public
name|Integer
name|getHttpClientMinThreads
parameter_list|()
block|{
return|return
name|httpClientMinThreads
return|;
block|}
comment|/**      * To set a value for minimum number of threads in HttpClient thread pool.      * This setting override any setting configured on component level.      * Notice that both a min and max size must be configured. If not set it default to min 8 threads used in Jettys thread pool.      */
DECL|method|setHttpClientMinThreads (Integer httpClientMinThreads)
specifier|public
name|void
name|setHttpClientMinThreads
parameter_list|(
name|Integer
name|httpClientMinThreads
parameter_list|)
block|{
name|this
operator|.
name|httpClientMinThreads
operator|=
name|httpClientMinThreads
expr_stmt|;
block|}
DECL|method|getHttpClientMaxThreads ()
specifier|public
name|Integer
name|getHttpClientMaxThreads
parameter_list|()
block|{
return|return
name|httpClientMaxThreads
return|;
block|}
comment|/**      * To set a value for maximum number of threads in HttpClient thread pool.      * This setting override any setting configured on component level.      * Notice that both a min and max size must be configured. If not set it default to max 254 threads used in Jettys thread pool.      */
DECL|method|setHttpClientMaxThreads (Integer httpClientMaxThreads)
specifier|public
name|void
name|setHttpClientMaxThreads
parameter_list|(
name|Integer
name|httpClientMaxThreads
parameter_list|)
block|{
name|this
operator|.
name|httpClientMaxThreads
operator|=
name|httpClientMaxThreads
expr_stmt|;
block|}
DECL|method|getHttpClientParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHttpClientParameters
parameter_list|()
block|{
return|return
name|httpClientParameters
return|;
block|}
comment|/**      * Configuration of Jetty's HttpClient. For example, setting httpClient.idleTimeout=30000 sets the idle timeout to 30 seconds.      * And httpClient.timeout=30000 sets the request timeout to 30 seconds, in case you want to timeout sooner if you have long running request/response calls.      */
DECL|method|setHttpClientParameters (Map<String, Object> httpClientParameters)
specifier|public
name|void
name|setHttpClientParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientParameters
parameter_list|)
block|{
name|this
operator|.
name|httpClientParameters
operator|=
name|httpClientParameters
expr_stmt|;
block|}
DECL|method|createContentExchange ()
specifier|public
specifier|abstract
name|JettyContentExchange
name|createContentExchange
parameter_list|()
function_decl|;
block|}
end_class

end_unit

