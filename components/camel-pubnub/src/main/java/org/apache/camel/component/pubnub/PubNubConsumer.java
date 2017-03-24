begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pubnub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
package|;
end_package

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|PubnubError
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
name|Message
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
name|impl
operator|.
name|DefaultConsumer
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
name|DefaultExchange
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
comment|/**  * The PubNub consumer.  */
end_comment

begin_class
DECL|class|PubNubConsumer
specifier|public
class|class
name|PubNubConsumer
extends|extends
name|DefaultConsumer
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
name|PubNubConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|PubNubEndpoint
name|endpoint
decl_stmt|;
DECL|method|PubNubConsumer (PubNubEndpoint endpoint, Processor processor)
specifier|public
name|PubNubConsumer
parameter_list|(
name|PubNubEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|initCommunication ()
specifier|private
name|void
name|initCommunication
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpoint
operator|.
name|getEndpointType
argument_list|()
operator|.
name|equals
argument_list|(
name|PubNubEndpointType
operator|.
name|pubsub
argument_list|)
condition|)
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|subscribe
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
operator|new
name|PubNubCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|presence
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
operator|new
name|PubNubCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|terminateCommunication ()
specifier|private
name|void
name|terminateCommunication
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpoint
operator|.
name|getEndpointType
argument_list|()
operator|.
name|equals
argument_list|(
name|PubNubEndpointType
operator|.
name|pubsub
argument_list|)
condition|)
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|unsubscribe
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|unsubscribePresence
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|initCommunication
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doResume
argument_list|()
expr_stmt|;
name|initCommunication
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
name|terminateCommunication
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
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|terminateCommunication
argument_list|()
expr_stmt|;
name|super
operator|.
name|doSuspend
argument_list|()
expr_stmt|;
block|}
DECL|class|PubNubCallback
specifier|private
class|class
name|PubNubCallback
extends|extends
name|Callback
block|{
annotation|@
name|Override
DECL|method|successCallback (String channel, Object objectMessage, String timetoken)
specifier|public
name|void
name|successCallback
parameter_list|(
name|String
name|channel
parameter_list|,
name|Object
name|objectMessage
parameter_list|,
name|String
name|timetoken
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|endpoint
argument_list|,
name|endpoint
operator|.
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|objectMessage
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|PubNubConstants
operator|.
name|TIMETOKEN
argument_list|,
name|timetoken
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|PubNubConstants
operator|.
name|CHANNEL
argument_list|,
name|channel
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|connectCallback (String channel, Object message)
specifier|public
name|void
name|connectCallback
parameter_list|(
name|String
name|channel
parameter_list|,
name|Object
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Subscriber : Successfully connected to PubNub channel {}"
argument_list|,
name|channel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|errorCallback (String channel, PubnubError error)
specifier|public
name|void
name|errorCallback
parameter_list|(
name|String
name|channel
parameter_list|,
name|PubnubError
name|error
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Subscriber : Error [{}] received from PubNub on channel {}"
argument_list|,
name|error
argument_list|,
name|channel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reconnectCallback (String channel, Object message)
specifier|public
name|void
name|reconnectCallback
parameter_list|(
name|String
name|channel
parameter_list|,
name|Object
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Subscriber : Reconnected to PubNub channel {}"
argument_list|,
name|channel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|disconnectCallback (String channel, Object message)
specifier|public
name|void
name|disconnectCallback
parameter_list|(
name|String
name|channel
parameter_list|,
name|Object
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Subscriber : Disconnected from PubNub channel {}"
argument_list|,
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

