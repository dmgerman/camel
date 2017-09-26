begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty9
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty9
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
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
operator|.
name|CamelHttpClient
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
name|jetty
operator|.
name|JettyHttpComponent
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
name|jetty
operator|.
name|JettyHttpEndpoint
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
name|ObjectHelper
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
name|HttpClientTransport
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
name|AbstractConnector
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
name|ConnectionFactory
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
name|ForwardedRequestCustomizer
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
name|HttpConnectionFactory
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
name|Server
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
name|ServerConnector
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
name|SslConnectionFactory
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
name|util
operator|.
name|ssl
operator|.
name|SslContextFactory
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
DECL|class|JettyHttpComponent9
specifier|public
class|class
name|JettyHttpComponent9
extends|extends
name|JettyHttpComponent
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
name|JettyHttpComponent9
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|createCamelHttpClient (HttpClientTransport transport, SslContextFactory sslContextFactory)
specifier|protected
name|CamelHttpClient
name|createCamelHttpClient
parameter_list|(
name|HttpClientTransport
name|transport
parameter_list|,
name|SslContextFactory
name|sslContextFactory
parameter_list|)
block|{
return|return
operator|new
name|CamelHttpClient9
argument_list|(
name|transport
argument_list|,
name|sslContextFactory
argument_list|)
return|;
block|}
DECL|method|createEndpoint (URI endpointUri, URI httpUri)
specifier|protected
name|JettyHttpEndpoint
name|createEndpoint
parameter_list|(
name|URI
name|endpointUri
parameter_list|,
name|URI
name|httpUri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|JettyHttpEndpoint9
argument_list|(
name|this
argument_list|,
name|endpointUri
operator|.
name|toString
argument_list|()
argument_list|,
name|httpUri
argument_list|)
return|;
block|}
DECL|method|createConnectorJettyInternal (Server server, JettyHttpEndpoint endpoint, SslContextFactory sslcf)
specifier|protected
name|AbstractConnector
name|createConnectorJettyInternal
parameter_list|(
name|Server
name|server
parameter_list|,
name|JettyHttpEndpoint
name|endpoint
parameter_list|,
name|SslContextFactory
name|sslcf
parameter_list|)
block|{
try|try
block|{
name|String
name|host
init|=
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|endpoint
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|HttpConfiguration
name|httpConfig
init|=
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|HttpConfiguration
argument_list|()
decl_stmt|;
name|httpConfig
operator|.
name|setSendServerVersion
argument_list|(
name|endpoint
operator|.
name|isSendServerVersion
argument_list|()
argument_list|)
expr_stmt|;
name|httpConfig
operator|.
name|setSendDateHeader
argument_list|(
name|endpoint
operator|.
name|isSendDateHeader
argument_list|()
argument_list|)
expr_stmt|;
name|httpConfig
operator|.
name|setSendDateHeader
argument_list|(
name|endpoint
operator|.
name|isSendDateHeader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|requestBufferSize
operator|!=
literal|null
condition|)
block|{
comment|// Does not work
comment|//httpConfig.setRequestBufferSize(requestBufferSize);
block|}
if|if
condition|(
name|requestHeaderSize
operator|!=
literal|null
condition|)
block|{
name|httpConfig
operator|.
name|setRequestHeaderSize
argument_list|(
name|requestHeaderSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|responseBufferSize
operator|!=
literal|null
condition|)
block|{
name|httpConfig
operator|.
name|setOutputBufferSize
argument_list|(
name|responseBufferSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|responseHeaderSize
operator|!=
literal|null
condition|)
block|{
name|httpConfig
operator|.
name|setResponseHeaderSize
argument_list|(
name|responseHeaderSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useXForwardedForHeader
condition|)
block|{
name|httpConfig
operator|.
name|addCustomizer
argument_list|(
operator|new
name|ForwardedRequestCustomizer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|HttpConnectionFactory
name|httpFactory
init|=
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|HttpConnectionFactory
argument_list|(
name|httpConfig
argument_list|)
decl_stmt|;
name|ArrayList
argument_list|<
name|ConnectionFactory
argument_list|>
name|connectionFactories
init|=
operator|new
name|ArrayList
argument_list|<
name|ConnectionFactory
argument_list|>
argument_list|()
decl_stmt|;
name|ServerConnector
name|result
init|=
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|ServerConnector
argument_list|(
name|server
argument_list|)
decl_stmt|;
if|if
condition|(
name|sslcf
operator|!=
literal|null
condition|)
block|{
name|httpConfig
operator|.
name|addCustomizer
argument_list|(
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|SecureRequestCustomizer
argument_list|()
argument_list|)
expr_stmt|;
name|SslConnectionFactory
name|scf
init|=
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|SslConnectionFactory
argument_list|(
name|sslcf
argument_list|,
literal|"HTTP/1.1"
argument_list|)
decl_stmt|;
name|connectionFactories
operator|.
name|add
argument_list|(
name|scf
argument_list|)
expr_stmt|;
comment|// The protocol name can be "SSL" or "SSL-HTTP/1.1" depending on the version of Jetty
name|result
operator|.
name|setDefaultProtocol
argument_list|(
name|scf
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connectionFactories
operator|.
name|add
argument_list|(
name|httpFactory
argument_list|)
expr_stmt|;
name|result
operator|.
name|setConnectionFactories
argument_list|(
name|connectionFactories
argument_list|)
expr_stmt|;
name|result
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sslcf
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getSslSocketConnectorProperties
argument_list|()
operator|!=
literal|null
operator|&&
literal|"https"
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
comment|// must copy the map otherwise it will be deleted
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|getSslSocketConnectorProperties
argument_list|()
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|sslcf
argument_list|,
name|properties
argument_list|)
expr_stmt|;
if|if
condition|(
name|properties
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There are "
operator|+
name|properties
operator|.
name|size
argument_list|()
operator|+
literal|" parameters that couldn't be set on the SocketConnector."
operator|+
literal|" Check the uri if the parameters are spelt correctly and that they are properties of the SelectChannelConnector."
operator|+
literal|" Unknown parameters=["
operator|+
name|properties
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Connector on port: {} is using includeCipherSuites: {} excludeCipherSuites: {} includeProtocols: {} excludeProtocols: {}"
argument_list|,
name|port
argument_list|,
name|sslcf
operator|.
name|getIncludeCipherSuites
argument_list|()
argument_list|,
name|sslcf
operator|.
name|getExcludeCipherSuites
argument_list|()
argument_list|,
name|sslcf
operator|.
name|getIncludeProtocols
argument_list|()
argument_list|,
name|sslcf
operator|.
name|getExcludeProtocols
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

