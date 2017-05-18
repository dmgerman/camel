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
name|SmackException
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
name|StanzaListener
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
name|chat2
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
name|chat2
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
name|chat2
operator|.
name|IncomingChatMessageListener
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
name|MessageTypeFilter
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
name|OrFilter
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
name|StanzaTypeFilter
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
name|Presence
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
name|Stanza
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
name|tcp
operator|.
name|XMPPTCPConnection
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
name|MucEnterConfiguration
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
name|jivesoftware
operator|.
name|smackx
operator|.
name|muc
operator|.
name|MultiUserChatException
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
name|MultiUserChatManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jxmpp
operator|.
name|jid
operator|.
name|EntityBareJid
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jxmpp
operator|.
name|jid
operator|.
name|impl
operator|.
name|JidCreate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jxmpp
operator|.
name|jid
operator|.
name|parts
operator|.
name|Resourcepart
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
comment|/**  * A {@link org.apache.camel.Consumer Consumer} which listens to XMPP packets  */
end_comment

begin_class
DECL|class|XmppConsumer
specifier|public
class|class
name|XmppConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|IncomingChatMessageListener
implements|,
name|MessageListener
implements|,
name|StanzaListener
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
name|XMPPTCPConnection
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
name|SmackException
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getExceptionHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|scheduleDelayedStart
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
name|chatManager
operator|=
name|ChatManager
operator|.
name|getInstanceFor
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|chatManager
operator|.
name|addIncomingListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|OrFilter
name|pubsubPacketFilter
init|=
operator|new
name|OrFilter
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isPubsub
argument_list|()
condition|)
block|{
comment|//xep-0060: pubsub#notification_type can be 'headline' or 'normal'
name|pubsubPacketFilter
operator|.
name|addFilter
argument_list|(
name|MessageTypeFilter
operator|.
name|HEADLINE
argument_list|)
expr_stmt|;
name|pubsubPacketFilter
operator|.
name|addFilter
argument_list|(
name|MessageTypeFilter
operator|.
name|NORMAL
argument_list|)
expr_stmt|;
name|connection
operator|.
name|addSyncStanzaListener
argument_list|(
name|this
argument_list|,
name|pubsubPacketFilter
argument_list|)
expr_stmt|;
block|}
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
name|chatWith
argument_list|(
name|JidCreate
operator|.
name|entityBareFrom
argument_list|(
name|endpoint
operator|.
name|getChatId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// add the presence packet listener to the connection so we only get packets that concerns us
comment|// we must add the listener before creating the muc
specifier|final
name|AndFilter
name|packetFilter
init|=
operator|new
name|AndFilter
argument_list|(
operator|new
name|StanzaTypeFilter
argument_list|(
name|Presence
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|connection
operator|.
name|addSyncStanzaListener
argument_list|(
name|this
argument_list|,
name|packetFilter
argument_list|)
expr_stmt|;
name|MultiUserChatManager
name|mucm
init|=
name|MultiUserChatManager
operator|.
name|getInstanceFor
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|muc
operator|=
name|mucm
operator|.
name|getMultiUserChat
argument_list|(
name|JidCreate
operator|.
name|entityBareFrom
argument_list|(
name|endpoint
operator|.
name|resolveRoom
argument_list|(
name|connection
argument_list|)
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
name|MucEnterConfiguration
name|mucc
init|=
name|muc
operator|.
name|getEnterConfigurationBuilder
argument_list|(
name|Resourcepart
operator|.
name|from
argument_list|(
name|endpoint
operator|.
name|getNickname
argument_list|()
argument_list|)
argument_list|)
operator|.
name|requestNoHistory
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|muc
operator|.
name|join
argument_list|(
name|mucc
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
name|warn
argument_list|(
literal|"Ignoring an exception caught in the startup connection poller thread."
argument_list|,
name|e
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
name|warn
argument_list|(
literal|"Ignoring an exception caught in the connection poller thread."
argument_list|,
name|e
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Successfully connected to XMPP server through: {}"
argument_list|,
name|connection
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SmackException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Connection to XMPP server failed. Will try to reconnect later again."
argument_list|,
name|e
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
comment|// stop scheduler first
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
block|}
annotation|@
name|Override
DECL|method|newIncomingMessage (EntityBareJid from, Message message, Chat chat)
specifier|public
name|void
name|newIncomingMessage
parameter_list|(
name|EntityBareJid
name|from
parameter_list|,
name|Message
name|message
parameter_list|,
name|Chat
name|chat
parameter_list|)
block|{
name|processMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processMessage (Message message)
specifier|public
name|void
name|processMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|processMessage
argument_list|(
literal|null
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processStanza (Stanza stanza)
specifier|public
name|void
name|processStanza
parameter_list|(
name|Stanza
name|stanza
parameter_list|)
throws|throws
name|SmackException
operator|.
name|NotConnectedException
throws|,
name|InterruptedException
block|{
if|if
condition|(
name|stanza
operator|instanceof
name|Message
condition|)
block|{
name|processMessage
argument_list|(
operator|(
name|Message
operator|)
name|stanza
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
if|if
condition|(
name|endpoint
operator|.
name|isDoc
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|XmppConstants
operator|.
name|DOC_HEADER
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
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
try|try
block|{
name|muc
operator|.
name|pollMessage
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MultiUserChatException
operator|.
name|MucNotJoinedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error while polling message from MultiUserChat. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

