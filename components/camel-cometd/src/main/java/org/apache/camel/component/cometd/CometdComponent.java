begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cometd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cometd
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|EnumSet
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
name|servlet
operator|.
name|DispatcherType
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
name|SSLContextParametersAware
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
name|Metadata
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|BayeuxServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|SecurityPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|server
operator|.
name|BayeuxServerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|server
operator|.
name|CometDServlet
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
name|Connector
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
name|handler
operator|.
name|ContextHandlerCollection
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
name|session
operator|.
name|SessionHandler
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
name|servlet
operator|.
name|FilterHolder
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
name|servlet
operator|.
name|ServletContextHandler
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
name|servlet
operator|.
name|ServletHolder
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
name|servlets
operator|.
name|CrossOriginFilter
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
name|resource
operator|.
name|Resource
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

begin_comment
comment|/**  * Component for Jetty Cometd  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"cometd,cometds"
argument_list|)
DECL|class|CometdComponent
specifier|public
class|class
name|CometdComponent
extends|extends
name|DefaultComponent
implements|implements
name|SSLContextParametersAware
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
name|CometdComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|connectors
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ConnectorRef
argument_list|>
name|connectors
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|serverListeners
specifier|private
name|List
argument_list|<
name|BayeuxServer
operator|.
name|BayeuxServerListener
argument_list|>
name|serverListeners
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|sslKeyPassword
specifier|private
name|String
name|sslKeyPassword
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|sslPassword
specifier|private
name|String
name|sslPassword
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|sslKeystore
specifier|private
name|String
name|sslKeystore
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|securityPolicy
specifier|private
name|SecurityPolicy
name|securityPolicy
decl_stmt|;
DECL|field|extensions
specifier|private
name|List
argument_list|<
name|BayeuxServer
operator|.
name|Extension
argument_list|>
name|extensions
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useGlobalSslContextParameters
specifier|private
name|boolean
name|useGlobalSslContextParameters
decl_stmt|;
DECL|class|ConnectorRef
class|class
name|ConnectorRef
block|{
DECL|field|connector
name|Connector
name|connector
decl_stmt|;
DECL|field|servlet
name|CometDServlet
name|servlet
decl_stmt|;
DECL|field|server
name|Server
name|server
decl_stmt|;
DECL|field|refCount
name|int
name|refCount
decl_stmt|;
DECL|method|ConnectorRef (Connector connector, CometDServlet servlet, Server server)
name|ConnectorRef
parameter_list|(
name|Connector
name|connector
parameter_list|,
name|CometDServlet
name|servlet
parameter_list|,
name|Server
name|server
parameter_list|)
block|{
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
name|this
operator|.
name|server
operator|=
name|server
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
DECL|method|CometdComponent ()
specifier|public
name|CometdComponent
parameter_list|()
block|{     }
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
name|CometdEndpoint
name|endpoint
init|=
operator|new
name|CometdEndpoint
argument_list|(
name|this
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Connects the URL specified on the endpoint to the specified processor.      */
DECL|method|connect (CometdProducerConsumer prodcon)
specifier|public
name|void
name|connect
parameter_list|(
name|CometdProducerConsumer
name|prodcon
parameter_list|)
throws|throws
name|Exception
block|{
name|Server
name|server
init|=
literal|null
decl_stmt|;
comment|// Make sure that there is a connector for the requested endpoint.
name|CometdEndpoint
name|endpoint
init|=
name|prodcon
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|connectorKey
init|=
name|endpoint
operator|.
name|getProtocol
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getUri
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
decl_stmt|;
synchronized|synchronized
init|(
name|connectors
init|)
block|{
name|ConnectorRef
name|connectorRef
init|=
name|connectors
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
name|ServerConnector
name|connector
decl_stmt|;
name|server
operator|=
name|createServer
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"cometds"
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
name|server
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connector
operator|=
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|)
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
name|getUri
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
name|getUri
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
literal|"You use localhost interface! It means that no external connections will be available."
operator|+
literal|" Don't you want to use 0.0.0.0 instead (all network interfaces)?"
argument_list|)
expr_stmt|;
block|}
name|server
operator|.
name|addConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|CometDServlet
name|servlet
init|=
name|createServletForConnector
argument_list|(
name|server
argument_list|,
name|connector
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|connectorRef
operator|=
operator|new
name|ConnectorRef
argument_list|(
name|connector
argument_list|,
name|servlet
argument_list|,
name|server
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|connectors
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
name|connectorRef
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
name|BayeuxServerImpl
name|bayeux
init|=
name|connectorRef
operator|.
name|servlet
operator|.
name|getBayeux
argument_list|()
decl_stmt|;
if|if
condition|(
name|securityPolicy
operator|!=
literal|null
condition|)
block|{
name|bayeux
operator|.
name|setSecurityPolicy
argument_list|(
name|securityPolicy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|extensions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BayeuxServer
operator|.
name|Extension
name|extension
range|:
name|extensions
control|)
block|{
name|bayeux
operator|.
name|addExtension
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|serverListeners
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BayeuxServer
operator|.
name|BayeuxServerListener
name|serverListener
range|:
name|serverListeners
control|)
block|{
name|bayeux
operator|.
name|addListener
argument_list|(
name|serverListener
argument_list|)
expr_stmt|;
block|}
block|}
name|prodcon
operator|.
name|setBayeux
argument_list|(
name|bayeux
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Disconnects the URL specified on the endpoint from the specified      * processor.      */
DECL|method|disconnect (CometdProducerConsumer prodcon)
specifier|public
name|void
name|disconnect
parameter_list|(
name|CometdProducerConsumer
name|prodcon
parameter_list|)
throws|throws
name|Exception
block|{
name|CometdEndpoint
name|endpoint
init|=
name|prodcon
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|connectorKey
init|=
name|endpoint
operator|.
name|getProtocol
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getUri
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
decl_stmt|;
synchronized|synchronized
init|(
name|connectors
init|)
block|{
name|ConnectorRef
name|connectorRef
init|=
name|connectors
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
name|connectors
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
DECL|method|createServletForConnector (Server server, Connector connector, CometdEndpoint endpoint)
specifier|protected
name|CometDServlet
name|createServletForConnector
parameter_list|(
name|Server
name|server
parameter_list|,
name|Connector
name|connector
parameter_list|,
name|CometdEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|CometDServlet
name|servlet
init|=
operator|new
name|CometDServlet
argument_list|()
decl_stmt|;
name|ServletContextHandler
name|context
init|=
operator|new
name|ServletContextHandler
argument_list|(
name|server
argument_list|,
literal|"/"
argument_list|,
name|ServletContextHandler
operator|.
name|NO_SECURITY
operator||
name|ServletContextHandler
operator|.
name|NO_SESSIONS
argument_list|)
decl_stmt|;
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
name|servlet
argument_list|)
expr_stmt|;
name|holder
operator|.
name|setAsyncSupported
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Use baseResource to pass as a parameter the url
comment|// pointing to by example classpath:webapp
if|if
condition|(
name|endpoint
operator|.
name|getBaseResource
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|resources
init|=
name|endpoint
operator|.
name|getBaseResource
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
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
literal|">>> Protocol found: "
operator|+
name|resources
index|[
literal|0
index|]
operator|+
literal|", and resource: "
operator|+
name|resources
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resources
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
name|context
operator|.
name|setBaseResource
argument_list|(
name|Resource
operator|.
name|newResource
argument_list|(
name|resources
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resources
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"classpath"
argument_list|)
condition|)
block|{
comment|// Create a URL handler using classpath protocol
name|URL
name|url
init|=
name|this
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourceAsURL
argument_list|(
name|resources
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|context
operator|.
name|setBaseResource
argument_list|(
name|Resource
operator|.
name|newResource
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|applyCrossOriginFiltering
argument_list|(
name|endpoint
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|addServlet
argument_list|(
name|holder
argument_list|,
literal|"/cometd/*"
argument_list|)
expr_stmt|;
name|context
operator|.
name|addServlet
argument_list|(
literal|"org.eclipse.jetty.servlet.DefaultServlet"
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|context
operator|.
name|setSessionHandler
argument_list|(
operator|new
name|SessionHandler
argument_list|()
argument_list|)
expr_stmt|;
name|holder
operator|.
name|setInitParameter
argument_list|(
literal|"timeout"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|endpoint
operator|.
name|getTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|holder
operator|.
name|setInitParameter
argument_list|(
literal|"interval"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|endpoint
operator|.
name|getInterval
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|holder
operator|.
name|setInitParameter
argument_list|(
literal|"maxInterval"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|endpoint
operator|.
name|getMaxInterval
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|holder
operator|.
name|setInitParameter
argument_list|(
literal|"multiFrameInterval"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|endpoint
operator|.
name|getMultiFrameInterval
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|holder
operator|.
name|setInitParameter
argument_list|(
literal|"JSONCommented"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|endpoint
operator|.
name|isJsonCommented
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|holder
operator|.
name|setInitParameter
argument_list|(
literal|"logLevel"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|endpoint
operator|.
name|getLogLevel
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|servlet
return|;
block|}
DECL|method|getSslSocketConnector (Server server)
specifier|protected
name|ServerConnector
name|getSslSocketConnector
parameter_list|(
name|Server
name|server
parameter_list|)
throws|throws
name|Exception
block|{
name|ServerConnector
name|sslSocketConnector
init|=
literal|null
decl_stmt|;
name|SSLContextParameters
name|sslParams
init|=
name|this
operator|.
name|sslContextParameters
decl_stmt|;
if|if
condition|(
name|sslParams
operator|==
literal|null
condition|)
block|{
name|sslParams
operator|=
name|retrieveGlobalSslContextParameters
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|sslParams
operator|!=
literal|null
condition|)
block|{
name|SslContextFactory
name|sslContextFactory
init|=
operator|new
name|CometdComponentSslContextFactory
argument_list|()
decl_stmt|;
name|sslContextFactory
operator|.
name|setSslContext
argument_list|(
name|sslParams
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sslSocketConnector
operator|=
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|,
name|sslContextFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SslContextFactory
name|sslContextFactory
init|=
operator|new
name|SslContextFactory
argument_list|()
decl_stmt|;
name|sslContextFactory
operator|.
name|setKeyStorePassword
argument_list|(
name|sslKeyPassword
argument_list|)
expr_stmt|;
name|sslContextFactory
operator|.
name|setKeyManagerPassword
argument_list|(
name|sslPassword
argument_list|)
expr_stmt|;
if|if
condition|(
name|sslKeystore
operator|!=
literal|null
condition|)
block|{
name|sslContextFactory
operator|.
name|setKeyStorePath
argument_list|(
name|sslKeystore
argument_list|)
expr_stmt|;
block|}
name|sslSocketConnector
operator|=
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|,
name|sslContextFactory
argument_list|)
expr_stmt|;
block|}
return|return
name|sslSocketConnector
return|;
block|}
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
DECL|method|getSslKeystore ()
specifier|public
name|String
name|getSslKeystore
parameter_list|()
block|{
return|return
name|sslKeystore
return|;
block|}
comment|/**      * The password for the keystore when using SSL.      */
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
comment|/**      * The password when using SSL.      */
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
comment|/**      * The path to the keystore.      */
DECL|method|setSslKeystore (String sslKeystore)
specifier|public
name|void
name|setSslKeystore
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
comment|/**      * To use a custom configured SecurityPolicy to control authorization      */
DECL|method|setSecurityPolicy (SecurityPolicy securityPolicy)
specifier|public
name|void
name|setSecurityPolicy
parameter_list|(
name|SecurityPolicy
name|securityPolicy
parameter_list|)
block|{
name|this
operator|.
name|securityPolicy
operator|=
name|securityPolicy
expr_stmt|;
block|}
DECL|method|getSecurityPolicy ()
specifier|public
name|SecurityPolicy
name|getSecurityPolicy
parameter_list|()
block|{
return|return
name|securityPolicy
return|;
block|}
DECL|method|getExtensions ()
specifier|public
name|List
argument_list|<
name|BayeuxServer
operator|.
name|Extension
argument_list|>
name|getExtensions
parameter_list|()
block|{
return|return
name|extensions
return|;
block|}
comment|/**      * To use a list of custom BayeuxServer.Extension that allows modifying incoming and outgoing requests.      */
DECL|method|setExtensions (List<BayeuxServer.Extension> extensions)
specifier|public
name|void
name|setExtensions
parameter_list|(
name|List
argument_list|<
name|BayeuxServer
operator|.
name|Extension
argument_list|>
name|extensions
parameter_list|)
block|{
name|this
operator|.
name|extensions
operator|=
name|extensions
expr_stmt|;
block|}
DECL|method|addExtension (BayeuxServer.Extension extension)
specifier|public
name|void
name|addExtension
parameter_list|(
name|BayeuxServer
operator|.
name|Extension
name|extension
parameter_list|)
block|{
if|if
condition|(
name|extensions
operator|==
literal|null
condition|)
block|{
name|extensions
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|extensions
operator|.
name|add
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
DECL|method|addServerListener (BayeuxServer.BayeuxServerListener serverListener)
specifier|public
name|void
name|addServerListener
parameter_list|(
name|BayeuxServer
operator|.
name|BayeuxServerListener
name|serverListener
parameter_list|)
block|{
if|if
condition|(
name|serverListeners
operator|==
literal|null
condition|)
block|{
name|serverListeners
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|serverListeners
operator|.
name|add
argument_list|(
name|serverListener
argument_list|)
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
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|this
operator|.
name|useGlobalSslContextParameters
return|;
block|}
comment|/**      * Enable usage of global SSL context parameters.      */
annotation|@
name|Override
DECL|method|setUseGlobalSslContextParameters (boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
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
name|server
operator|.
name|setHandler
argument_list|(
name|collection
argument_list|)
expr_stmt|;
return|return
name|server
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
for|for
control|(
name|ConnectorRef
name|connectorRef
range|:
name|connectors
operator|.
name|values
argument_list|()
control|)
block|{
name|connectorRef
operator|.
name|connector
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|connectors
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
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
block|}
DECL|method|applyCrossOriginFiltering (CometdEndpoint endpoint, ServletContextHandler context)
specifier|private
name|void
name|applyCrossOriginFiltering
parameter_list|(
name|CometdEndpoint
name|endpoint
parameter_list|,
name|ServletContextHandler
name|context
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isCrossOriginFilterOn
argument_list|()
condition|)
block|{
name|FilterHolder
name|filterHolder
init|=
operator|new
name|FilterHolder
argument_list|()
decl_stmt|;
name|CrossOriginFilter
name|filter
init|=
operator|new
name|CrossOriginFilter
argument_list|()
decl_stmt|;
name|filterHolder
operator|.
name|setFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|filterHolder
operator|.
name|setInitParameter
argument_list|(
literal|"allowedOrigins"
argument_list|,
name|endpoint
operator|.
name|getAllowedOrigins
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addFilter
argument_list|(
name|filterHolder
argument_list|,
name|endpoint
operator|.
name|getFilterPath
argument_list|()
argument_list|,
name|EnumSet
operator|.
name|allOf
argument_list|(
name|DispatcherType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Override the key/trust store check method as it does not account for a factory that has      * a pre-configured {@link SSLContext}.      */
DECL|class|CometdComponentSslContextFactory
specifier|private
specifier|static
specifier|final
class|class
name|CometdComponentSslContextFactory
extends|extends
name|SslContextFactory
block|{
comment|// to support jetty 9.2.
comment|// TODO: remove this class when we have upgraded to jetty 9.3
DECL|method|checkKeyStore ()
specifier|public
name|void
name|checkKeyStore
parameter_list|()
block|{         }
block|}
block|}
end_class

end_unit

