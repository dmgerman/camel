begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mqtt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mqtt
package|;
end_package

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
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
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
name|AsyncEndpoint
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
name|Exchange
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
name|fusesource
operator|.
name|hawtbuf
operator|.
name|Buffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtbuf
operator|.
name|UTF8Buffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdispatch
operator|.
name|Task
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|CallbackConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|Listener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|Promise
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|QoS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|Topic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|Tracer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|CONNACK
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|CONNECT
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|DISCONNECT
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|MQTTFrame
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|PINGREQ
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|PINGRESP
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|PUBACK
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|PUBCOMP
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|PUBLISH
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|PUBREC
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|PUBREL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|SUBACK
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|SUBSCRIBE
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|mqtt
operator|.
name|codec
operator|.
name|UNSUBSCRIBE
import|;
end_import

begin_comment
comment|/**  * Component for communicating with MQTT M2M message brokers using FuseSource MQTT Client.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.10.0"
argument_list|,
name|scheme
operator|=
literal|"mqtt"
argument_list|,
name|title
operator|=
literal|"MQTT"
argument_list|,
name|syntax
operator|=
literal|"mqtt:name"
argument_list|,
name|consumerClass
operator|=
name|MQTTConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"messaging,iot"
argument_list|)
DECL|class|MQTTEndpoint
specifier|public
class|class
name|MQTTEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|AsyncEndpoint
block|{
DECL|field|PUBLISH_MAX_RECONNECT_ATTEMPTS
specifier|private
specifier|static
specifier|final
name|int
name|PUBLISH_MAX_RECONNECT_ATTEMPTS
init|=
literal|3
decl_stmt|;
DECL|field|connection
specifier|private
name|CallbackConnection
name|connection
decl_stmt|;
DECL|field|connected
specifier|private
specifier|volatile
name|boolean
name|connected
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|List
argument_list|<
name|MQTTConsumer
argument_list|>
name|consumers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
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
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|MQTTConfiguration
name|configuration
decl_stmt|;
DECL|method|MQTTEndpoint (final String uri, MQTTComponent component, MQTTConfiguration properties)
specifier|public
name|MQTTEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
name|MQTTComponent
name|component
parameter_list|,
name|MQTTConfiguration
name|properties
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
name|configuration
operator|=
name|properties
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|configuration
operator|.
name|setTracer
argument_list|(
operator|new
name|Tracer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"tracer.debug() "
operator|+
name|this
operator|+
literal|": uri="
operator|+
name|uri
operator|+
literal|", message="
operator|+
name|String
operator|.
name|format
argument_list|(
name|message
argument_list|,
name|args
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSend
parameter_list|(
name|MQTTFrame
name|frame
parameter_list|)
block|{
name|String
name|decoded
init|=
literal|null
decl_stmt|;
try|try
block|{
switch|switch
condition|(
name|frame
operator|.
name|messageType
argument_list|()
condition|)
block|{
case|case
name|PINGREQ
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PINGREQ
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PINGRESP
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PINGRESP
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|CONNECT
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|CONNECT
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|DISCONNECT
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|DISCONNECT
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|SUBSCRIBE
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|SUBSCRIBE
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|UNSUBSCRIBE
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|UNSUBSCRIBE
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBLISH
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBLISH
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBACK
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBACK
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBREC
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBREC
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBREL
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBREL
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBCOMP
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBCOMP
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|CONNACK
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|CONNACK
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|SUBACK
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|SUBACK
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
default|default:
name|decoded
operator|=
name|frame
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|decoded
operator|=
name|frame
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"tracer.onSend() "
operator|+
name|this
operator|+
literal|":  uri="
operator|+
name|uri
operator|+
literal|", frame="
operator|+
name|decoded
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onReceive
parameter_list|(
name|MQTTFrame
name|frame
parameter_list|)
block|{
name|String
name|decoded
init|=
literal|null
decl_stmt|;
try|try
block|{
switch|switch
condition|(
name|frame
operator|.
name|messageType
argument_list|()
condition|)
block|{
case|case
name|PINGREQ
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PINGREQ
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PINGRESP
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PINGRESP
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|CONNECT
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|CONNECT
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|DISCONNECT
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|DISCONNECT
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|SUBSCRIBE
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|SUBSCRIBE
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|UNSUBSCRIBE
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|UNSUBSCRIBE
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBLISH
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBLISH
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBACK
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBACK
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBREC
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBREC
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBREL
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBREL
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|PUBCOMP
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|PUBCOMP
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|CONNACK
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|CONNACK
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|SUBACK
operator|.
name|TYPE
case|:
name|decoded
operator|=
operator|new
name|SUBACK
argument_list|()
operator|.
name|decode
argument_list|(
name|frame
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
default|default:
name|decoded
operator|=
name|frame
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|decoded
operator|=
name|frame
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"tracer.onReceive() "
operator|+
name|this
operator|+
literal|":  uri="
operator|+
name|uri
operator|+
literal|", frame="
operator|+
name|decoded
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
name|MQTTConsumer
name|answer
init|=
operator|new
name|MQTTConsumer
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
name|MQTTProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|MQTTConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * A logical name to use which is not the topic name.      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
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
name|createConnection
argument_list|()
expr_stmt|;
name|connect
argument_list|()
expr_stmt|;
block|}
DECL|method|createConnection ()
specifier|protected
name|void
name|createConnection
parameter_list|()
block|{
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
comment|// In connect(), in the connection.connect() callback, onFailure() doesn't seem to ever be called, so forcing the disconnect here.
comment|// Without this, the fusesource MQTT client seems to be holding the old connection object, and connection contention can ensue.
name|connection
operator|.
name|disconnect
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|connection
operator|=
name|configuration
operator|.
name|callbackConnection
argument_list|()
expr_stmt|;
name|connection
operator|.
name|listener
argument_list|(
operator|new
name|Listener
argument_list|()
block|{
specifier|public
name|void
name|onConnected
parameter_list|()
block|{
name|connected
operator|=
literal|true
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"MQTT Connection connected to {}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onDisconnected
parameter_list|()
block|{
comment|// no connected = false required here because the MQTT client should trigger its own reconnect;
comment|// setting connected = false would make the publish() method to launch a new connection while the original
comment|// one is still reconnecting, likely leading to duplicate messages as observed in CAMEL-9092;
comment|// if retries are exhausted and it desists, we should get a callback on onFailure, and then we can set
comment|// connected = false safely
name|log
operator|.
name|debug
argument_list|(
literal|"MQTT Connection disconnected from {}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onPublish
parameter_list|(
name|UTF8Buffer
name|topic
parameter_list|,
name|Buffer
name|body
parameter_list|,
name|Runnable
name|ack
parameter_list|)
block|{
if|if
condition|(
operator|!
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MQTTConfiguration
operator|.
name|MQTT_SUBSCRIBE_TOPIC
argument_list|,
name|topic
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|MQTTConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|consumer
operator|.
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ack
operator|!=
literal|null
condition|)
block|{
name|ack
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|value
parameter_list|)
block|{
comment|// mark this connection as disconnected so we force re-connect
name|connected
operator|=
literal|false
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Connection to "
operator|+
name|configuration
operator|.
name|getHost
argument_list|()
operator|+
literal|" failure due "
operator|+
name|value
operator|.
name|getMessage
argument_list|()
operator|+
literal|". Forcing a disconnect to re-connect on next attempt."
argument_list|)
expr_stmt|;
name|connection
operator|.
name|disconnect
argument_list|(
operator|new
name|Callback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|value
parameter_list|)
block|{                     }
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Failed to disconnect from "
operator|+
name|configuration
operator|.
name|getHost
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|connection
operator|!=
literal|null
operator|&&
name|connected
condition|)
block|{
specifier|final
name|Promise
argument_list|<
name|Void
argument_list|>
name|promise
init|=
operator|new
name|Promise
argument_list|<>
argument_list|()
decl_stmt|;
name|connection
operator|.
name|getDispatchQueue
argument_list|()
operator|.
name|execute
argument_list|(
operator|new
name|Task
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|connection
operator|.
name|disconnect
argument_list|(
operator|new
name|Callback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|value
parameter_list|)
block|{
name|connected
operator|=
literal|false
expr_stmt|;
name|promise
operator|.
name|onSuccess
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|value
parameter_list|)
block|{
name|promise
operator|.
name|onFailure
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|promise
operator|.
name|await
argument_list|(
name|configuration
operator|.
name|getDisconnectWaitInSeconds
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|connect ()
name|void
name|connect
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Promise
argument_list|<
name|Object
argument_list|>
name|promise
init|=
operator|new
name|Promise
argument_list|<>
argument_list|()
decl_stmt|;
name|connection
operator|.
name|connect
argument_list|(
operator|new
name|Callback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|value
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connected to {}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|Topic
index|[]
name|topics
init|=
name|createSubscribeTopics
argument_list|()
decl_stmt|;
if|if
condition|(
name|topics
operator|!=
literal|null
operator|&&
name|topics
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|connection
operator|.
name|subscribe
argument_list|(
name|topics
argument_list|,
operator|new
name|Callback
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
name|promise
operator|.
name|onSuccess
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|connected
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|value
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Failed to subscribe"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|promise
operator|.
name|onFailure
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|connection
operator|.
name|disconnect
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|connected
operator|=
literal|false
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|promise
operator|.
name|onSuccess
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|connected
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|value
parameter_list|)
block|{
comment|// this doesn't appear to ever be called
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to connect to "
operator|+
name|configuration
operator|.
name|getHost
argument_list|()
operator|+
literal|" due "
operator|+
name|value
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|promise
operator|.
name|onFailure
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|connection
operator|.
name|disconnect
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|connected
operator|=
literal|false
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Connecting to {} using {} seconds timeout"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getConnectWaitInSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|promise
operator|.
name|await
argument_list|(
name|configuration
operator|.
name|getConnectWaitInSeconds
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|createSubscribeTopics ()
name|Topic
index|[]
name|createSubscribeTopics
parameter_list|()
block|{
name|String
name|subscribeTopicList
init|=
name|configuration
operator|.
name|getSubscribeTopicNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|subscribeTopicList
operator|!=
literal|null
operator|&&
operator|!
name|subscribeTopicList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
index|[]
name|topicNames
init|=
name|subscribeTopicList
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|Topic
index|[]
name|topics
init|=
operator|new
name|Topic
index|[
name|topicNames
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|topicNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|topics
index|[
name|i
index|]
operator|=
operator|new
name|Topic
argument_list|(
name|topicNames
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
argument_list|,
name|configuration
operator|.
name|getQoS
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|topics
return|;
block|}
else|else
block|{
comment|// fall back on singular topic name
name|String
name|subscribeTopicName
init|=
name|configuration
operator|.
name|getSubscribeTopicName
argument_list|()
decl_stmt|;
name|subscribeTopicName
operator|=
name|subscribeTopicName
operator|!=
literal|null
condition|?
name|subscribeTopicName
operator|.
name|trim
argument_list|()
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|subscribeTopicName
operator|!=
literal|null
operator|&&
operator|!
name|subscribeTopicName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Topic
index|[]
name|topics
init|=
block|{
operator|new
name|Topic
argument_list|(
name|subscribeTopicName
argument_list|,
name|configuration
operator|.
name|getQoS
argument_list|()
argument_list|)
block|}
decl_stmt|;
return|return
name|topics
return|;
block|}
block|}
name|log
operator|.
name|warn
argument_list|(
literal|"No topic subscriptions were specified in configuration"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|isConnected ()
name|boolean
name|isConnected
parameter_list|()
block|{
return|return
name|connected
return|;
block|}
DECL|method|publish (final String topic, final byte[] payload, final QoS qoS, final boolean retain, final Callback<Void> callback)
name|void
name|publish
parameter_list|(
specifier|final
name|String
name|topic
parameter_list|,
specifier|final
name|byte
index|[]
name|payload
parameter_list|,
specifier|final
name|QoS
name|qoS
parameter_list|,
specifier|final
name|boolean
name|retain
parameter_list|,
specifier|final
name|Callback
argument_list|<
name|Void
argument_list|>
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
comment|// if not connected then create a new connection to re-connect
name|boolean
name|done
init|=
name|isConnected
argument_list|()
decl_stmt|;
name|int
name|attempt
init|=
literal|0
decl_stmt|;
name|TimeoutException
name|timeout
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|!
name|done
operator|&&
name|attempt
operator|<=
name|PUBLISH_MAX_RECONNECT_ATTEMPTS
condition|)
block|{
name|attempt
operator|++
expr_stmt|;
try|try
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"#{} attempt to re-create connection to {} before publishing"
argument_list|,
name|attempt
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|createConnection
argument_list|()
expr_stmt|;
name|connect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|timeout
operator|=
name|e
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Timed out after {} seconds after {} attempt to re-create connection to {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|configuration
operator|.
name|getConnectWaitInSeconds
argument_list|()
block|,
name|attempt
block|,
name|configuration
operator|.
name|getHost
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// other kind of error then exit asap
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
name|done
operator|=
name|isConnected
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|attempt
operator|>
literal|3
operator|&&
operator|!
name|isConnected
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot re-connect to {} after {} attempts"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|attempt
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onFailure
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
return|return;
block|}
name|connection
operator|.
name|getDispatchQueue
argument_list|()
operator|.
name|execute
argument_list|(
operator|new
name|Task
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Publishing to {}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|connection
operator|.
name|publish
argument_list|(
name|topic
argument_list|,
name|payload
argument_list|,
name|qoS
argument_list|,
name|retain
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|addConsumer (MQTTConsumer consumer)
name|void
name|addConsumer
parameter_list|(
name|MQTTConsumer
name|consumer
parameter_list|)
block|{
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|removeConsumer (MQTTConsumer consumer)
name|void
name|removeConsumer
parameter_list|(
name|MQTTConsumer
name|consumer
parameter_list|)
block|{
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
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
block|}
end_class

end_unit

