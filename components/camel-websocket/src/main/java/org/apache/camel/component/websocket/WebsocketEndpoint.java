begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|websocket
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
name|impl
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|UriPath
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
name|server
operator|.
name|Handler
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"websocket"
argument_list|,
name|title
operator|=
literal|"Jetty Websocket"
argument_list|,
name|syntax
operator|=
literal|"websocket:host:port/resourceUri"
argument_list|,
name|consumerClass
operator|=
name|WebsocketConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"websocket"
argument_list|)
DECL|class|WebsocketEndpoint
specifier|public
class|class
name|WebsocketEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|sync
specifier|private
name|NodeSynchronization
name|sync
decl_stmt|;
DECL|field|memoryStore
specifier|private
name|WebsocketStore
name|memoryStore
decl_stmt|;
DECL|field|component
specifier|private
name|WebsocketComponent
name|component
decl_stmt|;
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
DECL|field|handlers
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|"0.0.0.0"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|"9292"
argument_list|)
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|sendToAll
specifier|private
name|Boolean
name|sendToAll
decl_stmt|;
annotation|@
name|UriParam
DECL|field|enableJmx
specifier|private
name|boolean
name|enableJmx
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sessionSupport
specifier|private
name|boolean
name|sessionSupport
decl_stmt|;
annotation|@
name|UriParam
DECL|field|crossOriginFilterOn
specifier|private
name|boolean
name|crossOriginFilterOn
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|UriParam
DECL|field|allowedOrigins
specifier|private
name|String
name|allowedOrigins
decl_stmt|;
annotation|@
name|UriParam
DECL|field|filterPath
specifier|private
name|String
name|filterPath
decl_stmt|;
annotation|@
name|UriParam
DECL|field|staticResources
specifier|private
name|String
name|staticResources
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"8192"
argument_list|)
DECL|field|bufferSize
specifier|private
name|Integer
name|bufferSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"300000"
argument_list|)
DECL|field|maxIdleTime
specifier|private
name|Integer
name|maxIdleTime
decl_stmt|;
annotation|@
name|UriParam
DECL|field|maxTextMessageSize
specifier|private
name|Integer
name|maxTextMessageSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|maxBinaryMessageSize
specifier|private
name|Integer
name|maxBinaryMessageSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"13"
argument_list|)
DECL|field|minVersion
specifier|private
name|Integer
name|minVersion
decl_stmt|;
DECL|method|WebsocketEndpoint (WebsocketComponent component, String uri, String resourceUri, Map<String, Object> parameters)
specifier|public
name|WebsocketEndpoint
parameter_list|(
name|WebsocketComponent
name|component
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|resourceUri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
name|this
operator|.
name|memoryStore
operator|=
operator|new
name|MemoryWebsocketStore
argument_list|()
expr_stmt|;
name|this
operator|.
name|sync
operator|=
operator|new
name|DefaultNodeSynchronization
argument_list|(
name|memoryStore
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
try|try
block|{
name|this
operator|.
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|WebsocketComponent
name|getComponent
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|component
argument_list|,
literal|"component"
argument_list|)
expr_stmt|;
return|return
operator|(
name|WebsocketComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|component
argument_list|,
literal|"component"
argument_list|)
expr_stmt|;
name|WebsocketConsumer
name|consumer
init|=
operator|new
name|WebsocketConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
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
return|return
operator|new
name|WebsocketProducer
argument_list|(
name|this
argument_list|,
name|memoryStore
argument_list|)
return|;
block|}
DECL|method|connect (WebsocketConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|WebsocketConsumer
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
DECL|method|disconnect (WebsocketConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|WebsocketConsumer
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
DECL|method|connect (WebsocketProducer producer)
specifier|public
name|void
name|connect
parameter_list|(
name|WebsocketProducer
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|connect
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (WebsocketProducer producer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|WebsocketProducer
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|disconnect
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * The hostname. The default value is<tt>0.0.0.0</tt>.      * Setting this option on the component will use the component configured value as default.      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
comment|/**      * The port number. The default value is<tt>9292</tt>.      * Setting this option on the component will use the component configured value as default.      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getStaticResources ()
specifier|public
name|String
name|getStaticResources
parameter_list|()
block|{
return|return
name|staticResources
return|;
block|}
comment|/**      * Set a resource path for static resources (such as .html files etc).      *<p/>      * The resources can be loaded from classpath, if you prefix with<tt>classpath:</tt>,      * otherwise the resources is loaded from file system or from JAR files.      *<p/>      * For example to load from root classpath use<tt>classpath:.</tt>, or      *<tt>classpath:WEB-INF/static</tt>      *<p/>      * If not configured (eg<tt>null</tt>) then no static resource is in use.      */
DECL|method|setStaticResources (String staticResources)
specifier|public
name|void
name|setStaticResources
parameter_list|(
name|String
name|staticResources
parameter_list|)
block|{
name|this
operator|.
name|staticResources
operator|=
name|staticResources
expr_stmt|;
block|}
DECL|method|getSendToAll ()
specifier|public
name|Boolean
name|getSendToAll
parameter_list|()
block|{
return|return
name|sendToAll
return|;
block|}
comment|/**      * To send to all websocket subscribers. Can be used to configure on endpoint level, instead of having to use the WebsocketConstants.SEND_TO_ALL header on the message.      */
DECL|method|setSendToAll (Boolean sendToAll)
specifier|public
name|void
name|setSendToAll
parameter_list|(
name|Boolean
name|sendToAll
parameter_list|)
block|{
name|this
operator|.
name|sendToAll
operator|=
name|sendToAll
expr_stmt|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|uri
operator|.
name|getScheme
argument_list|()
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|uri
operator|.
name|getPath
argument_list|()
return|;
block|}
comment|/**      * Whether to enable session support which enables HttpSession for each http request.      */
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
DECL|method|getBufferSize ()
specifier|public
name|Integer
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
comment|/**      * Set the buffer size of the websocketServlet, which is also the max frame byte size (default 8192)      */
DECL|method|setBufferSize (Integer bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|Integer
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|getMaxIdleTime ()
specifier|public
name|Integer
name|getMaxIdleTime
parameter_list|()
block|{
return|return
name|maxIdleTime
return|;
block|}
comment|/**      * Set the time in ms that the websocket created by the websocketServlet may be idle before closing. (default is 300000)      */
DECL|method|setMaxIdleTime (Integer maxIdleTime)
specifier|public
name|void
name|setMaxIdleTime
parameter_list|(
name|Integer
name|maxIdleTime
parameter_list|)
block|{
name|this
operator|.
name|maxIdleTime
operator|=
name|maxIdleTime
expr_stmt|;
block|}
DECL|method|getMaxTextMessageSize ()
specifier|public
name|Integer
name|getMaxTextMessageSize
parameter_list|()
block|{
return|return
name|maxTextMessageSize
return|;
block|}
comment|/**      * Can be used to set the size in characters that the websocket created by the websocketServlet may be accept before closing.      */
DECL|method|setMaxTextMessageSize (Integer maxTextMessageSize)
specifier|public
name|void
name|setMaxTextMessageSize
parameter_list|(
name|Integer
name|maxTextMessageSize
parameter_list|)
block|{
name|this
operator|.
name|maxTextMessageSize
operator|=
name|maxTextMessageSize
expr_stmt|;
block|}
DECL|method|getMaxBinaryMessageSize ()
specifier|public
name|Integer
name|getMaxBinaryMessageSize
parameter_list|()
block|{
return|return
name|maxBinaryMessageSize
return|;
block|}
comment|/**      * Can be used to set the size in bytes that the websocket created by the websocketServlet may be accept before closing. (Default is -1 - or unlimited)      */
DECL|method|setMaxBinaryMessageSize (Integer maxBinaryMessageSize)
specifier|public
name|void
name|setMaxBinaryMessageSize
parameter_list|(
name|Integer
name|maxBinaryMessageSize
parameter_list|)
block|{
name|this
operator|.
name|maxBinaryMessageSize
operator|=
name|maxBinaryMessageSize
expr_stmt|;
block|}
DECL|method|getMinVersion ()
specifier|public
name|Integer
name|getMinVersion
parameter_list|()
block|{
return|return
name|minVersion
return|;
block|}
comment|/**      * Can be used to set the minimum protocol version accepted for the websocketServlet. (Default 13 - the RFC6455 version)      */
DECL|method|setMinVersion (Integer minVersion)
specifier|public
name|void
name|setMinVersion
parameter_list|(
name|Integer
name|minVersion
parameter_list|)
block|{
name|this
operator|.
name|minVersion
operator|=
name|minVersion
expr_stmt|;
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
DECL|method|getAllowedOrigins ()
specifier|public
name|String
name|getAllowedOrigins
parameter_list|()
block|{
return|return
name|allowedOrigins
return|;
block|}
comment|/**      * The CORS allowed origins. Use * to allow all.      */
DECL|method|setAllowedOrigins (String allowedOrigins)
specifier|public
name|void
name|setAllowedOrigins
parameter_list|(
name|String
name|allowedOrigins
parameter_list|)
block|{
name|this
operator|.
name|allowedOrigins
operator|=
name|allowedOrigins
expr_stmt|;
block|}
DECL|method|isCrossOriginFilterOn ()
specifier|public
name|boolean
name|isCrossOriginFilterOn
parameter_list|()
block|{
return|return
name|crossOriginFilterOn
return|;
block|}
comment|/**      * Whether to enable CORS      */
DECL|method|setCrossOriginFilterOn (boolean crossOriginFilterOn)
specifier|public
name|void
name|setCrossOriginFilterOn
parameter_list|(
name|boolean
name|crossOriginFilterOn
parameter_list|)
block|{
name|this
operator|.
name|crossOriginFilterOn
operator|=
name|crossOriginFilterOn
expr_stmt|;
block|}
DECL|method|getFilterPath ()
specifier|public
name|String
name|getFilterPath
parameter_list|()
block|{
return|return
name|filterPath
return|;
block|}
comment|/**      * Context path for filtering CORS      */
DECL|method|setFilterPath (String filterPath)
specifier|public
name|void
name|setFilterPath
parameter_list|(
name|String
name|filterPath
parameter_list|)
block|{
name|this
operator|.
name|filterPath
operator|=
name|filterPath
expr_stmt|;
block|}
DECL|method|getResourceUri ()
specifier|public
name|String
name|getResourceUri
parameter_list|()
block|{
return|return
name|resourceUri
return|;
block|}
comment|/**      * Name of the websocket channel to use      */
DECL|method|setResourceUri (String resourceUri)
specifier|public
name|void
name|setResourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
comment|/**      * NodeSynchronization      * @return NodeSynchronization      */
DECL|method|getNodeSynchronization ()
specifier|public
name|NodeSynchronization
name|getNodeSynchronization
parameter_list|()
block|{
return|return
name|this
operator|.
name|sync
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|memoryStore
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|memoryStore
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

