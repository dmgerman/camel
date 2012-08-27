begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmpp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|Chat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|ChatManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|ChatManagerListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|PacketListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|SmackConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|XMPPConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|XMPPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|filter
operator|.
name|AndFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|filter
operator|.
name|PacketTypeFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|filter
operator|.
name|ToContainsFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Packet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Presence
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smackx
operator|.
name|muc
operator|.
name|DiscussionHistory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smackx
operator|.
name|muc
operator|.
name|MultiUserChat
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
comment|/**  * A {@link org.apache.camel.Consumer Consumer} which listens to XMPP packets  *  * @version   */
end_comment

begin_class
DECL|class|XmppConsumer
specifier|public
class|class
name|XmppConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|PacketListener
implements|,
name|MessageListener
implements|,
name|ChatManagerListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XmppConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|XmppEndpoint
name|endpoint
decl_stmt|;
DECL|field|muc
specifier|private
name|MultiUserChat
name|muc
decl_stmt|;
DECL|field|privateChat
specifier|private
name|Chat
name|privateChat
decl_stmt|;
DECL|field|chatManager
specifier|private
name|ChatManager
name|chatManager
decl_stmt|;
DECL|field|connection
specifier|private
name|XMPPConnection
name|connection
decl_stmt|;
DECL|field|scheduledExecutor
specifier|private
name|ScheduledExecutorService
name|scheduledExecutor
decl_stmt|;
DECL|method|XmppConsumer (XmppEndpoint endpoint, Processor processor)
specifier|public
name|XmppConsumer
parameter_list|(
name|XmppEndpoint
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
try|try
block|{
name|connection
operator|=
name|endpoint
operator|.
name|createConnection
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMPPException
name|e
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isTestConnectionOnStartup
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not connect to XMPP server."
argument_list|,
name|e
argument_list|)
throw|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|XmppEndpoint
operator|.
name|getXmppExceptionLogMessage
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|scheduleDelayedStart
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
name|chatManager
operator|=
name|connection
operator|.
name|getChatManager
argument_list|()
expr_stmt|;
name|chatManager
operator|.
name|addChatListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getRoom
argument_list|()
operator|==
literal|null
condition|)
block|{
name|privateChat
operator|=
name|chatManager
operator|.
name|getThreadChat
argument_list|(
name|endpoint
operator|.
name|getChatId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|privateChat
operator|!=
literal|null
condition|)
block|{
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
literal|"Adding listener to existing chat opened to "
operator|+
name|privateChat
operator|.
name|getParticipant
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|privateChat
operator|.
name|addMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|privateChat
operator|=
name|connection
operator|.
name|getChatManager
argument_list|()
operator|.
name|createChat
argument_list|(
name|endpoint
operator|.
name|getParticipant
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getChatId
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
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
literal|"Opening private chat to "
operator|+
name|privateChat
operator|.
name|getParticipant
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// add the presence packet listener to the connection so we only get packets that concerns us
comment|// we must add the listener before creating the muc
specifier|final
name|ToContainsFilter
name|toFilter
init|=
operator|new
name|ToContainsFilter
argument_list|(
name|endpoint
operator|.
name|getParticipant
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|AndFilter
name|packetFilter
init|=
operator|new
name|AndFilter
argument_list|(
operator|new
name|PacketTypeFilter
argument_list|(
name|Presence
operator|.
name|class
argument_list|)
argument_list|,
name|toFilter
argument_list|)
decl_stmt|;
name|connection
operator|.
name|addPacketListener
argument_list|(
name|this
argument_list|,
name|packetFilter
argument_list|)
expr_stmt|;
name|muc
operator|=
operator|new
name|MultiUserChat
argument_list|(
name|connection
argument_list|,
name|endpoint
operator|.
name|resolveRoom
argument_list|(
name|connection
argument_list|)
argument_list|)
expr_stmt|;
name|muc
operator|.
name|addMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|DiscussionHistory
name|history
init|=
operator|new
name|DiscussionHistory
argument_list|()
decl_stmt|;
name|history
operator|.
name|setMaxChars
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// we do not want any historical messages
name|muc
operator|.
name|join
argument_list|(
name|endpoint
operator|.
name|getNickname
argument_list|()
argument_list|,
literal|null
argument_list|,
name|history
argument_list|,
name|SmackConfiguration
operator|.
name|getPacketReplyTimeout
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Joined room: {} as: {}"
argument_list|,
name|muc
operator|.
name|getRoom
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getNickname
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|startRobustConnectionMonitor
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|scheduleDelayedStart ()
specifier|protected
name|void
name|scheduleDelayedStart
parameter_list|()
throws|throws
name|Exception
block|{
name|Runnable
name|startRunnable
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|doStart
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Delaying XMPP consumer startup for endpoint {}. Trying again in {} seconds."
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConnectionPollDelay
argument_list|()
argument_list|)
expr_stmt|;
name|getExecutor
argument_list|()
operator|.
name|schedule
argument_list|(
name|startRunnable
argument_list|,
name|endpoint
operator|.
name|getConnectionPollDelay
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|startRobustConnectionMonitor ()
specifier|private
name|void
name|startRobustConnectionMonitor
parameter_list|()
throws|throws
name|Exception
block|{
name|Runnable
name|connectionCheckRunnable
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|checkConnection
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
comment|// background thread to detect and repair lost connections
name|getExecutor
argument_list|()
operator|.
name|scheduleAtFixedRate
argument_list|(
name|connectionCheckRunnable
argument_list|,
name|endpoint
operator|.
name|getConnectionPollDelay
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getConnectionPollDelay
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|checkConnection ()
specifier|private
name|void
name|checkConnection
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|connection
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Attempting to reconnect to: {}"
argument_list|,
name|XmppEndpoint
operator|.
name|getConnectionMessage
argument_list|(
name|connection
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMPPException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|XmppEndpoint
operator|.
name|getXmppExceptionLogMessage
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getExecutor ()
specifier|private
name|ScheduledExecutorService
name|getExecutor
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|scheduledExecutor
operator|==
literal|null
condition|)
block|{
name|scheduledExecutor
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"connectionPoll"
argument_list|)
expr_stmt|;
block|}
return|return
name|scheduledExecutor
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|muc
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Leaving room: {}"
argument_list|,
name|muc
operator|.
name|getRoom
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|muc
operator|.
name|removeMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|muc
operator|.
name|leave
argument_list|()
expr_stmt|;
name|muc
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|!=
literal|null
operator|&&
name|connection
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|connection
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|scheduledExecutor
operator|!=
literal|null
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|scheduledExecutor
argument_list|)
expr_stmt|;
name|scheduledExecutor
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|chatCreated (Chat chat, boolean createdLocally)
specifier|public
name|void
name|chatCreated
parameter_list|(
name|Chat
name|chat
parameter_list|,
name|boolean
name|createdLocally
parameter_list|)
block|{
if|if
condition|(
operator|!
name|createdLocally
condition|)
block|{
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
literal|"Accepting incoming chat session from "
operator|+
name|chat
operator|.
name|getParticipant
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|chat
operator|.
name|addMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processPacket (Packet packet)
specifier|public
name|void
name|processPacket
parameter_list|(
name|Packet
name|packet
parameter_list|)
block|{
if|if
condition|(
name|packet
operator|instanceof
name|Message
condition|)
block|{
name|processMessage
argument_list|(
literal|null
argument_list|,
operator|(
name|Message
operator|)
name|packet
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processMessage (Chat chat, Message message)
specifier|public
name|void
name|processMessage
parameter_list|(
name|Chat
name|chat
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
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
literal|"Received XMPP message for {} from {} : {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|endpoint
operator|.
name|getUser
argument_list|()
block|,
name|endpoint
operator|.
name|getParticipant
argument_list|()
block|,
name|message
operator|.
name|getBody
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
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
block|}
finally|finally
block|{
comment|// must remove message from muc to avoid messages stacking up and causing OutOfMemoryError
comment|// pollMessage is a non blocking method
comment|// (see http://issues.igniterealtime.org/browse/SMACK-129)
if|if
condition|(
name|muc
operator|!=
literal|null
condition|)
block|{
name|muc
operator|.
name|pollMessage
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

