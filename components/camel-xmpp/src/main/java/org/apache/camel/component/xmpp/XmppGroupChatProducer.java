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
name|io
operator|.
name|IOException
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
name|RuntimeExchangeException
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
name|DefaultProducer
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
name|jxmpp
operator|.
name|stringprep
operator|.
name|XmppStringprepException
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

begin_class
DECL|class|XmppGroupChatProducer
specifier|public
class|class
name|XmppGroupChatProducer
extends|extends
name|DefaultProducer
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
name|XmppGroupChatProducer
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
DECL|field|connection
specifier|private
name|XMPPTCPConnection
name|connection
decl_stmt|;
DECL|field|chat
specifier|private
name|MultiUserChat
name|chat
decl_stmt|;
DECL|field|room
specifier|private
name|String
name|room
decl_stmt|;
DECL|method|XmppGroupChatProducer (XmppEndpoint endpoint)
specifier|public
name|XmppGroupChatProducer
parameter_list|(
name|XmppEndpoint
name|endpoint
parameter_list|)
throws|throws
name|XMPPException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|connection
operator|==
literal|null
condition|)
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
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Could not connect to XMPP server."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|chat
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|initializeChat
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
name|RuntimeExchangeException
argument_list|(
literal|"Could not initialize XMPP chat."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|Message
name|message
init|=
name|chat
operator|.
name|createMessage
argument_list|()
decl_stmt|;
try|try
block|{
name|message
operator|.
name|setTo
argument_list|(
name|JidCreate
operator|.
name|from
argument_list|(
name|room
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setFrom
argument_list|(
name|JidCreate
operator|.
name|from
argument_list|(
name|endpoint
operator|.
name|getUser
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|populateXmppMessage
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// make sure we are connected
if|if
condition|(
operator|!
name|connection
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|this
operator|.
name|reconnect
argument_list|()
expr_stmt|;
block|}
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
literal|"Sending XMPP message: {}"
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|chat
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// must invoke nextMessage to consume the response from the server
comment|// otherwise the client local queue will fill up (CAMEL-1467)
name|chat
operator|.
name|pollMessage
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
name|RuntimeExchangeException
argument_list|(
literal|"Could not send XMPP message: "
operator|+
name|message
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|reconnect ()
specifier|private
specifier|synchronized
name|void
name|reconnect
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|SmackException
throws|,
name|XMPPException
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
literal|"Reconnecting to: {}"
argument_list|,
name|XmppEndpoint
operator|.
name|getConnectionMessage
argument_list|(
name|connection
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|connect
argument_list|()
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
if|if
condition|(
name|connection
operator|==
literal|null
condition|)
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
literal|"Could not connect to XMPP server:  "
operator|+
name|endpoint
operator|.
name|getConnectionDescription
argument_list|()
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
literal|"Could not connect to XMPP server. {}  Producer will attempt lazy connection when needed."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|chat
operator|==
literal|null
operator|&&
name|connection
operator|!=
literal|null
condition|)
block|{
name|initializeChat
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|initializeChat ()
specifier|protected
specifier|synchronized
name|void
name|initializeChat
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|SmackException
throws|,
name|XMPPException
throws|,
name|XmppStringprepException
block|{
if|if
condition|(
name|chat
operator|==
literal|null
condition|)
block|{
name|room
operator|=
name|endpoint
operator|.
name|resolveRoom
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|MultiUserChatManager
name|chatManager
init|=
name|MultiUserChatManager
operator|.
name|getInstanceFor
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|chat
operator|=
name|chatManager
operator|.
name|getMultiUserChat
argument_list|(
name|JidCreate
operator|.
name|entityBareFrom
argument_list|(
name|room
argument_list|)
argument_list|)
expr_stmt|;
name|MucEnterConfiguration
name|mucc
init|=
name|chat
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
name|chat
operator|.
name|join
argument_list|(
name|mucc
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Joined room: {} as: {}"
argument_list|,
name|room
argument_list|,
name|endpoint
operator|.
name|getNickname
argument_list|()
argument_list|)
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
if|if
condition|(
name|chat
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Leaving room: {}"
argument_list|,
name|room
argument_list|)
expr_stmt|;
name|chat
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
name|chat
operator|=
literal|null
expr_stmt|;
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
name|connection
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getRoom ()
specifier|public
name|String
name|getRoom
parameter_list|()
block|{
return|return
name|room
return|;
block|}
block|}
end_class

end_unit

