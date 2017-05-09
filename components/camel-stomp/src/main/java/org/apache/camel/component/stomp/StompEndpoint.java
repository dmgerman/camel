begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stomp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stomp
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
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
name|camel
operator|.
name|spi
operator|.
name|HeaderFilterStrategyAware
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
name|AsciiBuffer
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
name|stomp
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
name|stomp
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
name|stomp
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
name|stomp
operator|.
name|client
operator|.
name|Stomp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|codec
operator|.
name|StompFrame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|hawtbuf
operator|.
name|UTF8Buffer
operator|.
name|utf8
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|DESTINATION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|DISCONNECT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|ID
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|SEND
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|SUBSCRIBE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|UNSUBSCRIBE
import|;
end_import

begin_comment
comment|/**  * The stomp component is used for communicating with Stomp compliant message brokers.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"stomp"
argument_list|,
name|title
operator|=
literal|"Stomp"
argument_list|,
name|syntax
operator|=
literal|"stomp:destination"
argument_list|,
name|consumerClass
operator|=
name|StompConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|StompEndpoint
specifier|public
class|class
name|StompEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|AsyncEndpoint
implements|,
name|HeaderFilterStrategyAware
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of the queue"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|destination
specifier|private
name|String
name|destination
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|StompConfiguration
name|configuration
decl_stmt|;
DECL|field|connection
specifier|private
name|CallbackConnection
name|connection
decl_stmt|;
DECL|field|stomp
specifier|private
name|Stomp
name|stomp
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom HeaderFilterStrategy to filter header to and from Camel message."
argument_list|)
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|List
argument_list|<
name|StompConsumer
argument_list|>
name|consumers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|StompConsumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|StompEndpoint (String uri, StompComponent component, StompConfiguration configuration, String destination)
specifier|public
name|StompEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|StompComponent
name|component
parameter_list|,
name|StompConfiguration
name|configuration
parameter_list|,
name|String
name|destination
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
name|configuration
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
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
name|StompProducer
argument_list|(
name|this
argument_list|)
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
return|return
operator|new
name|StompConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
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
specifier|final
name|Promise
argument_list|<
name|CallbackConnection
argument_list|>
name|promise
init|=
operator|new
name|Promise
argument_list|<>
argument_list|()
decl_stmt|;
name|stomp
operator|=
operator|new
name|Stomp
argument_list|(
name|configuration
operator|.
name|getBrokerURL
argument_list|()
argument_list|)
expr_stmt|;
name|stomp
operator|.
name|setLogin
argument_list|(
name|configuration
operator|.
name|getLogin
argument_list|()
argument_list|)
expr_stmt|;
name|stomp
operator|.
name|setPasscode
argument_list|(
name|configuration
operator|.
name|getPasscode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|stomp
operator|.
name|setSslContext
argument_list|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|stomp
operator|.
name|connectCallback
argument_list|(
name|promise
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|configuration
operator|.
name|getHost
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|stomp
operator|.
name|setHost
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connection
operator|=
name|promise
operator|.
name|await
argument_list|()
expr_stmt|;
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
name|receive
argument_list|(
operator|new
name|Callback
argument_list|<
name|StompFrame
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|value
parameter_list|)
block|{
if|if
condition|(
name|started
operator|.
name|get
argument_list|()
condition|)
block|{
name|connection
operator|.
name|close
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|StompFrame
name|value
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
name|value
operator|.
name|content
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|StompConsumer
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
block|}
block|}
argument_list|)
expr_stmt|;
name|connection
operator|.
name|resume
argument_list|()
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
name|StompFrame
name|frame
init|=
operator|new
name|StompFrame
argument_list|(
name|DISCONNECT
argument_list|)
decl_stmt|;
name|connection
operator|.
name|send
argument_list|(
name|frame
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|connection
operator|.
name|close
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|send (final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|void
name|send
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|StompFrame
name|frame
init|=
operator|new
name|StompFrame
argument_list|(
name|SEND
argument_list|)
decl_stmt|;
comment|// feature for CAMEL-7672
name|populateCamelMessageHeadersToStompFrames
argument_list|(
name|exchange
argument_list|,
name|frame
argument_list|)
expr_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|DESTINATION
argument_list|,
name|StompFrame
operator|.
name|encodeHeader
argument_list|(
name|destination
argument_list|)
argument_list|)
expr_stmt|;
comment|//Fix for CAMEL-9506 leveraging the camel converter to do the change
name|frame
operator|.
name|content
argument_list|(
name|utf8
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
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
name|send
argument_list|(
name|frame
argument_list|,
operator|new
name|Callback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|v
parameter_list|)
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
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
DECL|method|populateCamelMessageHeadersToStompFrames (final Exchange exchange, final StompFrame frame)
specifier|private
name|void
name|populateCamelMessageHeadersToStompFrames
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|StompFrame
name|frame
parameter_list|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|headerName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|headerValue
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|headerName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"camel"
argument_list|)
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
if|if
condition|(
name|headerValue
operator|!=
literal|null
condition|)
block|{
name|frame
operator|.
name|addHeader
argument_list|(
operator|new
name|AsciiBuffer
argument_list|(
name|headerName
argument_list|)
argument_list|,
name|StompFrame
operator|.
name|encodeHeader
argument_list|(
name|headerValue
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|addConsumer (final StompConsumer consumer)
name|void
name|addConsumer
parameter_list|(
specifier|final
name|StompConsumer
name|consumer
parameter_list|)
block|{
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
name|StompFrame
name|frame
init|=
operator|new
name|StompFrame
argument_list|(
name|SUBSCRIBE
argument_list|)
decl_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|DESTINATION
argument_list|,
name|StompFrame
operator|.
name|encodeHeader
argument_list|(
name|destination
argument_list|)
argument_list|)
expr_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|ID
argument_list|,
name|consumer
operator|.
name|id
argument_list|)
expr_stmt|;
name|connection
operator|.
name|send
argument_list|(
name|frame
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|removeConsumer (final StompConsumer consumer)
name|void
name|removeConsumer
parameter_list|(
specifier|final
name|StompConsumer
name|consumer
parameter_list|)
block|{
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
name|StompFrame
name|frame
init|=
operator|new
name|StompFrame
argument_list|(
name|UNSUBSCRIBE
argument_list|)
decl_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|DESTINATION
argument_list|,
name|StompFrame
operator|.
name|encodeHeader
argument_list|(
name|destination
argument_list|)
argument_list|)
expr_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|ID
argument_list|,
name|consumer
operator|.
name|id
argument_list|)
expr_stmt|;
name|connection
operator|.
name|send
argument_list|(
name|frame
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|getNextId ()
name|AsciiBuffer
name|getNextId
parameter_list|()
block|{
return|return
name|connection
operator|.
name|nextId
argument_list|()
return|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
if|if
condition|(
name|headerFilterStrategy
operator|==
literal|null
condition|)
block|{
name|headerFilterStrategy
operator|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|headerFilterStrategy
return|;
block|}
comment|/**      * To use a custom HeaderFilterStrategy to filter header to and from Camel message.      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
block|}
end_class

end_unit

