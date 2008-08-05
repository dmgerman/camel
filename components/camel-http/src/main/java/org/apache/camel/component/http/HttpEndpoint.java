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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|params
operator|.
name|HttpClientParams
import|;
end_import

begin_comment
comment|/**  * Represents a<a href="http://activemq.apache.org/camel/http.html">HTTP  * endpoint</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpEndpoint
specifier|public
class|class
name|HttpEndpoint
extends|extends
name|DefaultPollingEndpoint
argument_list|<
name|HttpExchange
argument_list|>
block|{
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
name|httpConnectionManager
operator|=
name|httpConnectionManager
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|HttpExchange
argument_list|>
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
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
argument_list|<
name|HttpExchange
argument_list|>
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
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|HttpExchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|HttpExchange
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|)
return|;
block|}
DECL|method|createExchange (HttpServletRequest request, HttpServletResponse response)
specifier|public
name|HttpExchange
name|createExchange
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
return|return
operator|new
name|HttpExchange
argument_list|(
name|this
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
comment|/**      * Factory method used by producers and consumers to create a new {@link HttpClient} instance      */
DECL|method|createHttpClient ()
specifier|public
name|HttpClient
name|createHttpClient
parameter_list|()
block|{
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
comment|/**      * Provide access to the client parameters used on new {@link HttpClient} instances      * used by producers or consumers of this endpoint.      *      * @param clientParams      */
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
name|HttpBinding
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|component
operator|.
name|getHeaderFilterStrategy
argument_list|()
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
block|}
end_class

end_unit

