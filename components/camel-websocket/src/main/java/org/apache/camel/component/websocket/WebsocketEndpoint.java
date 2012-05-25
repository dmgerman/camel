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

begin_class
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
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
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
DECL|field|sendToAll
specifier|private
name|Boolean
name|sendToAll
decl_stmt|;
DECL|field|enableJmx
specifier|private
name|boolean
name|enableJmx
decl_stmt|;
DECL|field|sessionSupport
specifier|private
name|boolean
name|sessionSupport
decl_stmt|;
DECL|field|remaining
specifier|private
name|String
name|remaining
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|// Base Resource for the ServletContextHandler
DECL|field|home
specifier|private
name|String
name|home
decl_stmt|;
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
DECL|method|WebsocketEndpoint (WebsocketComponent component, String uri, String remaining, Map<String, Object> parameters)
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
name|remaining
operator|=
name|remaining
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
comment|// We will create the servlet when we
comment|// will call connect method and Jetty Server created
comment|// getComponent().addServlet(sync, consumer, remaining);
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
comment|// We will create the servlet when we
comment|// will call connect method and Jetty Server created
comment|// getComponent().addServlet(sync, null, remaining);
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
comment|// Jetty instance will be created
comment|// if it does not exist
name|component
operator|.
name|connect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
comment|// We will add a WebSocket servlet
comment|// to a Jetty server using Handler
name|getComponent
argument_list|()
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
name|consumer
argument_list|,
name|remaining
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
comment|// Servlet should be removed
comment|// getComponent().addServlet(sync, consumer, remaining);
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
DECL|method|getHome ()
specifier|public
name|String
name|getHome
parameter_list|()
block|{
return|return
name|home
return|;
block|}
DECL|method|setHome (String home)
specifier|public
name|void
name|setHome
parameter_list|(
name|String
name|home
parameter_list|)
block|{
name|this
operator|.
name|home
operator|=
name|home
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

