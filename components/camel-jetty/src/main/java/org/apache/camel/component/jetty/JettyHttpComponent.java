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
name|http
operator|.
name|CamelServlet
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
name|http
operator|.
name|HttpComponent
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
name|http
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
name|component
operator|.
name|http
operator|.
name|HttpEndpoint
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
name|CastUtils
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
name|URISupport
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
name|UnsafeUriCharactersEncoder
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|component
operator|.
name|LifeCycle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Connector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|client
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
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
name|mortbay
operator|.
name|jetty
operator|.
name|handler
operator|.
name|ContextHandlerCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|nio
operator|.
name|SelectChannelConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|security
operator|.
name|SslSocketConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|SessionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|thread
operator|.
name|QueuedThreadPool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|thread
operator|.
name|ThreadPool
import|;
end_import

begin_comment
comment|/**  * An HttpComponent which starts an embedded Jetty for to handle consuming from  * the http endpoints.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JettyHttpComponent
specifier|public
class|class
name|JettyHttpComponent
extends|extends
name|HttpComponent
block|{
DECL|field|CONNECTORS
specifier|protected
specifier|static
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|ConnectorRef
argument_list|>
name|CONNECTORS
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ConnectorRef
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JettyHttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|JETTY_SSL_KEYSTORE
specifier|private
specifier|static
specifier|final
name|String
name|JETTY_SSL_KEYSTORE
init|=
literal|"jetty.ssl.keystore"
decl_stmt|;
DECL|field|sslKeyPassword
specifier|protected
name|String
name|sslKeyPassword
decl_stmt|;
DECL|field|sslPassword
specifier|protected
name|String
name|sslPassword
decl_stmt|;
DECL|field|sslKeystore
specifier|protected
name|String
name|sslKeystore
decl_stmt|;
DECL|field|sslSocketConnectors
specifier|protected
name|Map
argument_list|<
name|Integer
argument_list|,
name|SslSocketConnector
argument_list|>
name|sslSocketConnectors
decl_stmt|;
DECL|field|httpClient
specifier|protected
name|HttpClient
name|httpClient
decl_stmt|;
DECL|field|httpClientThreadPool
specifier|protected
name|ThreadPool
name|httpClientThreadPool
decl_stmt|;
DECL|field|httpClientMinThreads
specifier|protected
name|Integer
name|httpClientMinThreads
decl_stmt|;
DECL|field|httpClientMaxThreads
specifier|protected
name|Integer
name|httpClientMaxThreads
decl_stmt|;
DECL|class|ConnectorRef
class|class
name|ConnectorRef
block|{
DECL|field|server
name|Server
name|server
decl_stmt|;
DECL|field|connector
name|Connector
name|connector
decl_stmt|;
DECL|field|servlet
name|CamelServlet
name|servlet
decl_stmt|;
DECL|field|refCount
name|int
name|refCount
decl_stmt|;
DECL|method|ConnectorRef (Server server, Connector connector, CamelServlet servlet)
specifier|public
name|ConnectorRef
parameter_list|(
name|Server
name|server
parameter_list|,
name|Connector
name|connector
parameter_list|,
name|CamelServlet
name|servlet
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
name|this
operator|.
name|connector
operator|=
name|connector
expr_stmt|;
name|this
operator|.
name|servlet
operator|=
name|servlet
expr_stmt|;
name|increment
argument_list|()
expr_stmt|;
block|}
DECL|method|increment ()
specifier|public
name|int
name|increment
parameter_list|()
block|{
return|return
operator|++
name|refCount
return|;
block|}
DECL|method|decrement ()
specifier|public
name|int
name|decrement
parameter_list|()
block|{
return|return
operator|--
name|refCount
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|uri
operator|=
name|uri
operator|.
name|startsWith
argument_list|(
literal|"jetty:"
argument_list|)
condition|?
name|remaining
else|:
name|uri
expr_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|handlerList
init|=
name|resolveAndRemoveReferenceListParameter
argument_list|(
name|parameters
argument_list|,
literal|"handlers"
argument_list|,
name|Handler
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// configure regular parameters
name|configureParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|JettyHttpEndpoint
name|result
init|=
operator|new
name|JettyHttpEndpoint
argument_list|(
name|this
argument_list|,
name|uri
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpBinding
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|setBinding
argument_list|(
name|httpBinding
argument_list|)
expr_stmt|;
block|}
name|setEndpointHeaderFilterStrategy
argument_list|(
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
name|handlerList
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|result
operator|.
name|setHandlers
argument_list|(
name|handlerList
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|result
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// configure http client if we have url configuration for it
if|if
condition|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
condition|)
block|{
comment|// configure Jetty http client
name|result
operator|.
name|setClient
argument_list|(
name|getHttpClient
argument_list|()
argument_list|)
expr_stmt|;
comment|// set additional parameters on http client
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getHttpClient
argument_list|()
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
comment|// validate that we could resolve all httpClient. parameters as this component is lenient
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
block|}
comment|// create the http uri after we have configured all the parameters on the camel objects
name|URI
name|httpUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|)
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|parameters
argument_list|)
argument_list|)
decl_stmt|;
name|result
operator|.
name|setHttpUri
argument_list|(
name|httpUri
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Connects the URL specified on the endpoint to the specified processor.      */
annotation|@
name|Override
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
comment|// Make sure that there is a connector for the requested endpoint.
name|JettyHttpEndpoint
name|endpoint
init|=
operator|(
name|JettyHttpEndpoint
operator|)
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|connectorKey
init|=
name|getConnectorKey
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|CONNECTORS
init|)
block|{
name|ConnectorRef
name|connectorRef
init|=
name|CONNECTORS
operator|.
name|get
argument_list|(
name|connectorKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectorRef
operator|==
literal|null
condition|)
block|{
name|Connector
name|connector
decl_stmt|;
if|if
condition|(
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
name|connector
operator|=
name|getSslSocketConnector
argument_list|(
name|endpoint
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connector
operator|=
operator|new
name|SelectChannelConnector
argument_list|()
expr_stmt|;
block|}
name|connector
operator|.
name|setPort
argument_list|(
name|endpoint
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setHost
argument_list|(
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"localhost"
operator|.
name|equalsIgnoreCase
argument_list|(
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getHost
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"You use localhost interface! It means that no external connections will be available. Don't you want to use 0.0.0.0 instead (all network interfaces)?"
argument_list|)
expr_stmt|;
block|}
name|Server
name|server
init|=
name|createServer
argument_list|()
decl_stmt|;
name|server
operator|.
name|addConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|connectorRef
operator|=
operator|new
name|ConnectorRef
argument_list|(
name|server
argument_list|,
name|connector
argument_list|,
name|createServletForConnector
argument_list|(
name|server
argument_list|,
name|connector
argument_list|,
name|endpoint
operator|.
name|getHandlers
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|connector
operator|.
name|start
argument_list|()
expr_stmt|;
name|CONNECTORS
operator|.
name|put
argument_list|(
name|connectorKey
argument_list|,
name|connectorRef
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ref track the connector
name|connectorRef
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
comment|// check the session support
if|if
condition|(
name|endpoint
operator|.
name|isSessionSupport
argument_list|()
condition|)
block|{
name|enableSessionSupport
argument_list|(
name|connectorRef
operator|.
name|server
argument_list|)
expr_stmt|;
block|}
name|connectorRef
operator|.
name|servlet
operator|.
name|connect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|enableSessionSupport (Server server)
specifier|private
name|void
name|enableSessionSupport
parameter_list|(
name|Server
name|server
parameter_list|)
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
operator|(
name|Context
operator|)
name|server
operator|.
name|getChildHandlerByClass
argument_list|(
name|Context
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|getSessionHandler
argument_list|()
operator|==
literal|null
condition|)
block|{
name|SessionHandler
name|sessionHandler
init|=
operator|new
name|SessionHandler
argument_list|()
decl_stmt|;
name|context
operator|.
name|setSessionHandler
argument_list|(
name|sessionHandler
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|isStarted
argument_list|()
condition|)
block|{
comment|// restart the context
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Disconnects the URL specified on the endpoint from the specified processor.      */
annotation|@
name|Override
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
comment|// If the connector is not needed anymore then stop it
name|HttpEndpoint
name|endpoint
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|connectorKey
init|=
name|getConnectorKey
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|CONNECTORS
init|)
block|{
name|ConnectorRef
name|connectorRef
init|=
name|CONNECTORS
operator|.
name|get
argument_list|(
name|connectorKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectorRef
operator|!=
literal|null
condition|)
block|{
name|connectorRef
operator|.
name|servlet
operator|.
name|disconnect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
if|if
condition|(
name|connectorRef
operator|.
name|decrement
argument_list|()
operator|==
literal|0
condition|)
block|{
name|connectorRef
operator|.
name|server
operator|.
name|removeConnector
argument_list|(
name|connectorRef
operator|.
name|connector
argument_list|)
expr_stmt|;
name|connectorRef
operator|.
name|connector
operator|.
name|stop
argument_list|()
expr_stmt|;
name|connectorRef
operator|.
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|CONNECTORS
operator|.
name|remove
argument_list|(
name|connectorKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getConnectorKey (HttpEndpoint endpoint)
specifier|private
name|String
name|getConnectorKey
parameter_list|(
name|HttpEndpoint
name|endpoint
parameter_list|)
block|{
return|return
name|endpoint
operator|.
name|getProtocol
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getPort
argument_list|()
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getSslKeyPassword ()
specifier|public
name|String
name|getSslKeyPassword
parameter_list|()
block|{
return|return
name|sslKeyPassword
return|;
block|}
DECL|method|setSslKeyPassword (String sslKeyPassword)
specifier|public
name|void
name|setSslKeyPassword
parameter_list|(
name|String
name|sslKeyPassword
parameter_list|)
block|{
name|this
operator|.
name|sslKeyPassword
operator|=
name|sslKeyPassword
expr_stmt|;
block|}
DECL|method|getSslPassword ()
specifier|public
name|String
name|getSslPassword
parameter_list|()
block|{
return|return
name|sslPassword
return|;
block|}
DECL|method|setSslPassword (String sslPassword)
specifier|public
name|void
name|setSslPassword
parameter_list|(
name|String
name|sslPassword
parameter_list|)
block|{
name|this
operator|.
name|sslPassword
operator|=
name|sslPassword
expr_stmt|;
block|}
DECL|method|setKeystore (String sslKeystore)
specifier|public
name|void
name|setKeystore
parameter_list|(
name|String
name|sslKeystore
parameter_list|)
block|{
name|this
operator|.
name|sslKeystore
operator|=
name|sslKeystore
expr_stmt|;
block|}
DECL|method|getKeystore ()
specifier|public
name|String
name|getKeystore
parameter_list|()
block|{
return|return
name|sslKeystore
return|;
block|}
DECL|method|getSslSocketConnector (int port)
specifier|public
name|SslSocketConnector
name|getSslSocketConnector
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|SslSocketConnector
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|sslSocketConnectors
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|sslSocketConnectors
operator|.
name|get
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|createSslSocketConnector
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// try the keystore system property as a backup, jetty doesn't seem
comment|// to read this property anymore
name|String
name|keystoreProperty
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|JETTY_SSL_KEYSTORE
argument_list|)
decl_stmt|;
if|if
condition|(
name|keystoreProperty
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setKeystore
argument_list|(
name|keystoreProperty
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|createSslSocketConnector ()
specifier|public
name|SslSocketConnector
name|createSslSocketConnector
parameter_list|()
block|{
name|SslSocketConnector
name|answer
init|=
operator|new
name|SslSocketConnector
argument_list|()
decl_stmt|;
comment|// with default null values, jetty ssl system properties
comment|// and console will be read by jetty implementation
name|answer
operator|.
name|setPassword
argument_list|(
name|sslPassword
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setKeyPassword
argument_list|(
name|sslKeyPassword
argument_list|)
expr_stmt|;
if|if
condition|(
name|sslKeystore
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setKeystore
argument_list|(
name|sslKeystore
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// try the keystore system property as a backup, jetty doesn't seem
comment|// to read this property anymore
name|String
name|keystoreProperty
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|JETTY_SSL_KEYSTORE
argument_list|)
decl_stmt|;
if|if
condition|(
name|keystoreProperty
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setKeystore
argument_list|(
name|keystoreProperty
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|setSslSocketConnectors (Map <Integer, SslSocketConnector> connectors)
specifier|public
name|void
name|setSslSocketConnectors
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|SslSocketConnector
argument_list|>
name|connectors
parameter_list|)
block|{
name|sslSocketConnectors
operator|=
name|connectors
expr_stmt|;
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
operator|new
name|HttpClient
argument_list|()
expr_stmt|;
name|httpClient
operator|.
name|setConnectorType
argument_list|(
name|HttpClient
operator|.
name|CONNECTOR_SELECT_CHANNEL
argument_list|)
expr_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyHost"
argument_list|)
operator|!=
literal|null
operator|&&
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPort"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|host
init|=
name|System
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
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPort"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Java System Property http.proxyHost and http.proxyPort detected. Using http proxy host: "
operator|+
name|host
operator|+
literal|" port: "
operator|+
name|port
argument_list|)
expr_stmt|;
block|}
name|httpClient
operator|.
name|setProxy
argument_list|(
operator|new
name|Address
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// use QueueThreadPool as the default bounded is deprecated (see SMXCOMP-157)
if|if
condition|(
name|getHttpClientThreadPool
argument_list|()
operator|==
literal|null
condition|)
block|{
name|QueuedThreadPool
name|qtp
init|=
operator|new
name|QueuedThreadPool
argument_list|()
decl_stmt|;
if|if
condition|(
name|httpClientMinThreads
operator|!=
literal|null
condition|)
block|{
name|qtp
operator|.
name|setMinThreads
argument_list|(
name|httpClientMinThreads
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpClientMaxThreads
operator|!=
literal|null
condition|)
block|{
name|qtp
operator|.
name|setMaxThreads
argument_list|(
name|httpClientMaxThreads
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|qtp
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error starting JettyHttpClient thread pool: "
operator|+
name|qtp
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|setHttpClientThreadPool
argument_list|(
name|qtp
argument_list|)
expr_stmt|;
block|}
name|httpClient
operator|.
name|setThreadPool
argument_list|(
name|getHttpClientThreadPool
argument_list|()
argument_list|)
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
DECL|method|getHttpClientThreadPool ()
specifier|public
name|ThreadPool
name|getHttpClientThreadPool
parameter_list|()
block|{
return|return
name|httpClientThreadPool
return|;
block|}
DECL|method|setHttpClientThreadPool (ThreadPool httpClientThreadPool)
specifier|public
name|void
name|setHttpClientThreadPool
parameter_list|(
name|ThreadPool
name|httpClientThreadPool
parameter_list|)
block|{
name|this
operator|.
name|httpClientThreadPool
operator|=
name|httpClientThreadPool
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
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|createServletForConnector (Server server, Connector connector, List<Handler> handlers)
specifier|protected
name|CamelServlet
name|createServletForConnector
parameter_list|(
name|Server
name|server
parameter_list|,
name|Connector
name|connector
parameter_list|,
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelServlet
name|camelServlet
init|=
operator|new
name|CamelServlet
argument_list|()
decl_stmt|;
name|Context
name|context
init|=
operator|new
name|Context
argument_list|(
name|server
argument_list|,
literal|"/"
argument_list|,
name|Context
operator|.
name|NO_SECURITY
operator||
name|Context
operator|.
name|NO_SESSIONS
argument_list|)
decl_stmt|;
name|context
operator|.
name|setConnectorNames
argument_list|(
operator|new
name|String
index|[]
block|{
name|connector
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|handlers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Handler
name|handler
range|:
name|handlers
control|)
block|{
name|context
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
name|ServletHolder
name|holder
init|=
operator|new
name|ServletHolder
argument_list|()
decl_stmt|;
name|holder
operator|.
name|setServlet
argument_list|(
name|camelServlet
argument_list|)
expr_stmt|;
name|context
operator|.
name|addServlet
argument_list|(
name|holder
argument_list|,
literal|"/*"
argument_list|)
expr_stmt|;
name|connector
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|camelServlet
return|;
block|}
DECL|method|createServer ()
specifier|protected
name|Server
name|createServer
parameter_list|()
throws|throws
name|Exception
block|{
name|Server
name|server
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|ContextHandlerCollection
name|collection
init|=
operator|new
name|ContextHandlerCollection
argument_list|()
decl_stmt|;
name|collection
operator|.
name|setServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|server
operator|.
name|addHandler
argument_list|(
name|collection
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|server
return|;
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
if|if
condition|(
name|httpClientThreadPool
operator|!=
literal|null
operator|&&
name|httpClientThreadPool
operator|instanceof
name|LifeCycle
condition|)
block|{
name|LifeCycle
name|lc
init|=
operator|(
name|LifeCycle
operator|)
name|httpClientThreadPool
decl_stmt|;
name|lc
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|httpClient
operator|!=
literal|null
operator|&&
operator|!
name|httpClient
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|httpClient
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|CONNECTORS
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|ConnectorRef
name|connectorRef
range|:
name|CONNECTORS
operator|.
name|values
argument_list|()
control|)
block|{
name|connectorRef
operator|.
name|server
operator|.
name|removeConnector
argument_list|(
name|connectorRef
operator|.
name|connector
argument_list|)
expr_stmt|;
name|connectorRef
operator|.
name|connector
operator|.
name|stop
argument_list|()
expr_stmt|;
name|connectorRef
operator|.
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|CONNECTORS
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|httpClient
operator|!=
literal|null
condition|)
block|{
name|httpClient
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|httpClientThreadPool
operator|!=
literal|null
operator|&&
name|httpClientThreadPool
operator|instanceof
name|LifeCycle
condition|)
block|{
name|LifeCycle
name|lc
init|=
operator|(
name|LifeCycle
operator|)
name|httpClientThreadPool
decl_stmt|;
name|lc
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

