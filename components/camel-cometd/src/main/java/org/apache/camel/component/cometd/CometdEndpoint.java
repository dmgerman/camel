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
name|support
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

begin_comment
comment|/**  * The cometd component is a transport for working with the Jetty implementation of the cometd/bayeux protocol.  *  * Using this component in combination with the dojo toolkit library it's possible to push Camel messages directly  * into the browser using an AJAX based mechanism.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.0.0"
argument_list|,
name|scheme
operator|=
literal|"cometd,cometds"
argument_list|,
name|title
operator|=
literal|"CometD"
argument_list|,
name|syntax
operator|=
literal|"cometd:host:port/channelName"
argument_list|,
name|label
operator|=
literal|"websocket"
argument_list|)
DECL|class|CometdEndpoint
specifier|public
class|class
name|CometdEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|component
specifier|private
name|CometdComponent
name|component
decl_stmt|;
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Hostname"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Host port number"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The channelName represents a topic that can be subscribed to by the Camel endpoints."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|channelName
specifier|private
name|String
name|channelName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|baseResource
specifier|private
name|String
name|baseResource
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"240000"
argument_list|)
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
literal|240000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|interval
specifier|private
name|int
name|interval
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|maxInterval
specifier|private
name|int
name|maxInterval
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1500"
argument_list|)
DECL|field|multiFrameInterval
specifier|private
name|int
name|multiFrameInterval
init|=
literal|1500
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|jsonCommented
specifier|private
name|boolean
name|jsonCommented
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|sessionHeadersEnabled
specifier|private
name|boolean
name|sessionHeadersEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|enums
operator|=
literal|"0,1,2"
argument_list|)
DECL|field|logLevel
specifier|private
name|int
name|logLevel
init|=
literal|1
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
argument_list|(
name|defaultValue
operator|=
literal|"*"
argument_list|)
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
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|disconnectLocalSession
specifier|private
name|boolean
name|disconnectLocalSession
decl_stmt|;
DECL|method|CometdEndpoint (CometdComponent component, String uri, String remaining, Map<String, Object> parameters)
specifier|public
name|CometdEndpoint
parameter_list|(
name|CometdComponent
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
name|this
operator|.
name|host
operator|=
name|this
operator|.
name|uri
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|this
operator|.
name|uri
operator|.
name|getPort
argument_list|()
expr_stmt|;
name|this
operator|.
name|channelName
operator|=
name|remaining
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
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
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
name|CometdProducer
name|producer
init|=
operator|new
name|CometdProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
name|producer
return|;
block|}
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
name|CometdConsumer
name|consumer
init|=
operator|new
name|CometdConsumer
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
DECL|method|connect (CometdProducerConsumer prodcons)
specifier|public
name|void
name|connect
parameter_list|(
name|CometdProducerConsumer
name|prodcons
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|connect
argument_list|(
name|prodcons
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (CometdProducerConsumer prodcons)
specifier|public
name|void
name|disconnect
parameter_list|(
name|CometdProducerConsumer
name|prodcons
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|disconnect
argument_list|(
name|prodcons
argument_list|)
expr_stmt|;
block|}
DECL|method|getComponent ()
specifier|public
name|CometdComponent
name|getComponent
parameter_list|()
block|{
return|return
name|component
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
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
if|if
condition|(
name|uri
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
literal|"cometds"
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
name|uri
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
name|uri
operator|.
name|getScheme
argument_list|()
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
DECL|method|getBaseResource ()
specifier|public
name|String
name|getBaseResource
parameter_list|()
block|{
return|return
name|baseResource
return|;
block|}
comment|/**      * The root directory for the web resources or classpath. Use the protocol file: or classpath: depending      * if you want that the component loads the resource from file system or classpath.      * Classpath is required for OSGI deployment where the resources are packaged in the jar      */
DECL|method|setBaseResource (String baseResource)
specifier|public
name|void
name|setBaseResource
parameter_list|(
name|String
name|baseResource
parameter_list|)
block|{
name|this
operator|.
name|baseResource
operator|=
name|baseResource
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * The server side poll timeout in milliseconds. This is how long the server will hold a reconnect request before responding.      */
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getInterval ()
specifier|public
name|int
name|getInterval
parameter_list|()
block|{
return|return
name|interval
return|;
block|}
comment|/**      * The client side poll timeout in milliseconds. How long a client will wait between reconnects      */
DECL|method|setInterval (int interval)
specifier|public
name|void
name|setInterval
parameter_list|(
name|int
name|interval
parameter_list|)
block|{
name|this
operator|.
name|interval
operator|=
name|interval
expr_stmt|;
block|}
DECL|method|getMaxInterval ()
specifier|public
name|int
name|getMaxInterval
parameter_list|()
block|{
return|return
name|maxInterval
return|;
block|}
comment|/**      * The max client side poll timeout in milliseconds. A client will be removed if a connection is not received in this time.      */
DECL|method|setMaxInterval (int maxInterval)
specifier|public
name|void
name|setMaxInterval
parameter_list|(
name|int
name|maxInterval
parameter_list|)
block|{
name|this
operator|.
name|maxInterval
operator|=
name|maxInterval
expr_stmt|;
block|}
DECL|method|getMultiFrameInterval ()
specifier|public
name|int
name|getMultiFrameInterval
parameter_list|()
block|{
return|return
name|multiFrameInterval
return|;
block|}
comment|/**      * The client side poll timeout, if multiple connections are detected from the same browser.      */
DECL|method|setMultiFrameInterval (int multiFrameInterval)
specifier|public
name|void
name|setMultiFrameInterval
parameter_list|(
name|int
name|multiFrameInterval
parameter_list|)
block|{
name|this
operator|.
name|multiFrameInterval
operator|=
name|multiFrameInterval
expr_stmt|;
block|}
DECL|method|isJsonCommented ()
specifier|public
name|boolean
name|isJsonCommented
parameter_list|()
block|{
return|return
name|jsonCommented
return|;
block|}
comment|/**      * If true, the server will accept JSON wrapped in a comment and will generate JSON wrapped in a comment. This is a defence against Ajax Hijacking.      */
DECL|method|setJsonCommented (boolean commented)
specifier|public
name|void
name|setJsonCommented
parameter_list|(
name|boolean
name|commented
parameter_list|)
block|{
name|jsonCommented
operator|=
name|commented
expr_stmt|;
block|}
comment|/**      * Whether to include the server session headers in the Camel message when creating a Camel Message for incoming requests.      */
DECL|method|setSessionHeadersEnabled (boolean enable)
specifier|public
name|void
name|setSessionHeadersEnabled
parameter_list|(
name|boolean
name|enable
parameter_list|)
block|{
name|this
operator|.
name|sessionHeadersEnabled
operator|=
name|enable
expr_stmt|;
block|}
DECL|method|isSessionHeadersEnabled ()
specifier|public
name|boolean
name|isSessionHeadersEnabled
parameter_list|()
block|{
return|return
name|sessionHeadersEnabled
return|;
block|}
DECL|method|getLogLevel ()
specifier|public
name|int
name|getLogLevel
parameter_list|()
block|{
return|return
name|logLevel
return|;
block|}
comment|/**      * Logging level. 0=none, 1=info, 2=debug.      */
DECL|method|setLogLevel (int logLevel)
specifier|public
name|void
name|setLogLevel
parameter_list|(
name|int
name|logLevel
parameter_list|)
block|{
name|this
operator|.
name|logLevel
operator|=
name|logLevel
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
comment|/**      * The origins domain that support to cross, if the crosssOriginFilterOn is true      */
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
comment|/**      * If true, the server will support for cross-domain filtering      */
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
comment|/**      * The filterPath will be used by the CrossOriginFilter, if the crosssOriginFilterOn is true      */
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
DECL|method|isDisconnectLocalSession ()
specifier|public
name|boolean
name|isDisconnectLocalSession
parameter_list|()
block|{
return|return
name|disconnectLocalSession
return|;
block|}
comment|/**      * Whether to disconnect local sessions after publishing a message to its channel.      * Disconnecting local session is needed as they are not swept by default by CometD, and therefore you can run out of memory.      */
DECL|method|setDisconnectLocalSession (boolean disconnectLocalSession)
specifier|public
name|void
name|setDisconnectLocalSession
parameter_list|(
name|boolean
name|disconnectLocalSession
parameter_list|)
block|{
name|this
operator|.
name|disconnectLocalSession
operator|=
name|disconnectLocalSession
expr_stmt|;
block|}
block|}
end_class

end_unit

