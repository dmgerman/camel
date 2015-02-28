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
name|impl
operator|.
name|DefaultProducer
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
name|packet
operator|.
name|Message
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|XmppPrivateChatProducer
specifier|public
class|class
name|XmppPrivateChatProducer
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
name|XmppPrivateChatProducer
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
name|XMPPConnection
name|connection
decl_stmt|;
DECL|field|participant
specifier|private
specifier|final
name|String
name|participant
decl_stmt|;
DECL|method|XmppPrivateChatProducer (XmppEndpoint endpoint, String participant)
specifier|public
name|XmppPrivateChatProducer
parameter_list|(
name|XmppEndpoint
name|endpoint
parameter_list|,
name|String
name|participant
parameter_list|)
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
name|this
operator|.
name|participant
operator|=
name|participant
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|participant
argument_list|,
literal|"participant"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating XmppPrivateChatProducer to participant {}"
argument_list|,
name|participant
argument_list|)
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
comment|// make sure we are connected
try|try
block|{
if|if
condition|(
name|connection
operator|==
literal|null
condition|)
block|{
name|connection
operator|=
name|endpoint
operator|.
name|createConnection
argument_list|()
expr_stmt|;
block|}
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
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
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
name|String
name|participant
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|XmppConstants
operator|.
name|TO
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|thread
init|=
name|endpoint
operator|.
name|getChatId
argument_list|()
decl_stmt|;
if|if
condition|(
name|participant
operator|==
literal|null
condition|)
block|{
name|participant
operator|=
name|getParticipant
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|thread
operator|=
literal|"Chat:"
operator|+
name|participant
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getUser
argument_list|()
expr_stmt|;
block|}
name|ChatManager
name|chatManager
init|=
name|ChatManager
operator|.
name|getInstanceFor
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|Chat
name|chat
init|=
name|getOrCreateChat
argument_list|(
name|chatManager
argument_list|,
name|participant
argument_list|,
name|thread
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
literal|null
decl_stmt|;
try|try
block|{
name|message
operator|=
operator|new
name|Message
argument_list|()
expr_stmt|;
name|message
operator|.
name|setTo
argument_list|(
name|participant
argument_list|)
expr_stmt|;
name|message
operator|.
name|setThread
argument_list|(
name|thread
argument_list|)
expr_stmt|;
name|message
operator|.
name|setType
argument_list|(
name|Message
operator|.
name|Type
operator|.
name|normal
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
literal|"Sending XMPP message to {} from {} : {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|participant
block|,
name|endpoint
operator|.
name|getUser
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
name|chat
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
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
literal|"Could not send XMPP message to "
operator|+
name|participant
operator|+
literal|" from "
operator|+
name|endpoint
operator|.
name|getUser
argument_list|()
operator|+
literal|" : "
operator|+
name|message
operator|+
literal|" to: "
operator|+
name|XmppEndpoint
operator|.
name|getConnectionMessage
argument_list|(
name|connection
argument_list|)
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getOrCreateChat (ChatManager chatManager, final String participant, String thread)
specifier|private
specifier|synchronized
name|Chat
name|getOrCreateChat
parameter_list|(
name|ChatManager
name|chatManager
parameter_list|,
specifier|final
name|String
name|participant
parameter_list|,
name|String
name|thread
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Looking for existing chat instance with thread ID {}"
argument_list|,
name|endpoint
operator|.
name|getChatId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Chat
name|chat
init|=
name|chatManager
operator|.
name|getThreadChat
argument_list|(
name|thread
argument_list|)
decl_stmt|;
if|if
condition|(
name|chat
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating new chat instance with thread ID {}"
argument_list|,
name|thread
argument_list|)
expr_stmt|;
block|}
name|chat
operator|=
name|chatManager
operator|.
name|createChat
argument_list|(
name|participant
argument_list|,
name|thread
argument_list|,
operator|new
name|MessageListener
argument_list|()
block|{
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
comment|// not here to do conversation
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
literal|"Received and discarding message from {} : {}"
argument_list|,
name|participant
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|chat
return|;
block|}
DECL|method|reconnect ()
specifier|private
specifier|synchronized
name|void
name|reconnect
parameter_list|()
throws|throws
name|XMPPException
throws|,
name|SmackException
throws|,
name|IOException
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
literal|"Could not establish connection to XMPP server:  "
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
DECL|method|getParticipant ()
specifier|public
name|String
name|getParticipant
parameter_list|()
block|{
return|return
name|participant
return|;
block|}
block|}
end_class

end_unit

