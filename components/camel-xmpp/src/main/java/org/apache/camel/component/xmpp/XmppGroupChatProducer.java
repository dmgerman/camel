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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|smackx
operator|.
name|muc
operator|.
name|MultiUserChat
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

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
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
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
DECL|field|room
specifier|private
specifier|final
name|String
name|room
decl_stmt|;
DECL|field|chat
specifier|private
name|MultiUserChat
name|chat
decl_stmt|;
DECL|method|XmppGroupChatProducer (XmppEndpoint endpoint, String room)
specifier|public
name|XmppGroupChatProducer
parameter_list|(
name|XmppEndpoint
name|endpoint
parameter_list|,
name|String
name|room
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
name|room
operator|=
name|room
expr_stmt|;
if|if
condition|(
name|room
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No room property specified"
argument_list|)
throw|;
block|}
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
comment|// TODO it would be nice if we could reuse the message from the exchange
name|Message
name|message
init|=
name|chat
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setTo
argument_list|(
name|room
argument_list|)
expr_stmt|;
name|message
operator|.
name|setFrom
argument_list|(
name|endpoint
operator|.
name|getUser
argument_list|()
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
literal|">>>> message: "
operator|+
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
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
name|XMPPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeXmppException
argument_list|(
name|e
argument_list|)
throw|;
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
if|if
condition|(
name|chat
operator|==
literal|null
condition|)
block|{
name|chat
operator|=
operator|new
name|MultiUserChat
argument_list|(
name|endpoint
operator|.
name|getConnection
argument_list|()
argument_list|,
name|room
argument_list|)
expr_stmt|;
name|String
name|nickname
init|=
name|this
operator|.
name|endpoint
operator|.
name|getNickname
argument_list|()
decl_stmt|;
name|chat
operator|.
name|join
argument_list|(
name|nickname
operator|!=
literal|null
condition|?
name|nickname
else|:
name|this
operator|.
name|endpoint
operator|.
name|getUser
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
name|chat
operator|.
name|leave
argument_list|()
expr_stmt|;
name|chat
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getChat ()
specifier|public
name|MultiUserChat
name|getChat
parameter_list|()
block|{
return|return
name|chat
return|;
block|}
DECL|method|setChat (MultiUserChat chat)
specifier|public
name|void
name|setChat
parameter_list|(
name|MultiUserChat
name|chat
parameter_list|)
block|{
name|this
operator|.
name|chat
operator|=
name|chat
expr_stmt|;
block|}
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

