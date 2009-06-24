begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.irc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|irc
package|;
end_package

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
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|schwering
operator|.
name|irc
operator|.
name|lib
operator|.
name|IRCUser
import|;
end_import

begin_class
DECL|class|IrcMessage
specifier|public
class|class
name|IrcMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|messageType
specifier|private
name|String
name|messageType
decl_stmt|;
DECL|field|target
specifier|private
name|String
name|target
decl_stmt|;
DECL|field|user
specifier|private
name|IRCUser
name|user
decl_stmt|;
DECL|field|whoWasKickedNick
specifier|private
name|String
name|whoWasKickedNick
decl_stmt|;
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
DECL|method|IrcMessage ()
specifier|public
name|IrcMessage
parameter_list|()
block|{     }
DECL|method|IrcMessage (String messageType, IRCUser user, String message)
specifier|public
name|IrcMessage
parameter_list|(
name|String
name|messageType
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|messageType
operator|=
name|messageType
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|IrcMessage (String messageType, String target, IRCUser user, String message)
specifier|public
name|IrcMessage
parameter_list|(
name|String
name|messageType
parameter_list|,
name|String
name|target
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|messageType
operator|=
name|messageType
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|IrcMessage (String messageType, String target, IRCUser user, String whoWasKickedNick, String message)
specifier|public
name|IrcMessage
parameter_list|(
name|String
name|messageType
parameter_list|,
name|String
name|target
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|whoWasKickedNick
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|messageType
operator|=
name|messageType
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|whoWasKickedNick
operator|=
name|whoWasKickedNick
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|IrcMessage (String messageType, String target, IRCUser user)
specifier|public
name|IrcMessage
parameter_list|(
name|String
name|messageType
parameter_list|,
name|String
name|target
parameter_list|,
name|IRCUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|messageType
operator|=
name|messageType
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|getMessageType ()
specifier|public
name|String
name|getMessageType
parameter_list|()
block|{
return|return
name|messageType
return|;
block|}
DECL|method|setMessageType (String messageType)
specifier|public
name|void
name|setMessageType
parameter_list|(
name|String
name|messageType
parameter_list|)
block|{
name|this
operator|.
name|messageType
operator|=
name|messageType
expr_stmt|;
block|}
DECL|method|getTarget ()
specifier|public
name|String
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
DECL|method|setTarget (String target)
specifier|public
name|void
name|setTarget
parameter_list|(
name|String
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|IRCUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|setUser (IRCUser user)
specifier|public
name|void
name|setUser
parameter_list|(
name|IRCUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|getWhoWasKickedNick ()
specifier|public
name|String
name|getWhoWasKickedNick
parameter_list|()
block|{
return|return
name|whoWasKickedNick
return|;
block|}
DECL|method|setWhoWasKickedNick (String whoWasKickedNick)
specifier|public
name|void
name|setWhoWasKickedNick
parameter_list|(
name|String
name|whoWasKickedNick
parameter_list|)
block|{
name|this
operator|.
name|whoWasKickedNick
operator|=
name|whoWasKickedNick
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|setMessage (String message)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|IrcBinding
name|binding
init|=
name|exchange
operator|!=
literal|null
condition|?
operator|(
name|IrcBinding
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|)
else|:
literal|null
decl_stmt|;
return|return
name|binding
operator|!=
literal|null
condition|?
name|binding
operator|.
name|extractBodyFromIrc
argument_list|(
name|exchange
argument_list|,
name|this
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|IrcMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|IrcMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|populateInitialHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateInitialHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|map
operator|.
name|put
argument_list|(
name|IrcConstants
operator|.
name|IRC_MESSAGE_TYPE
argument_list|,
name|messageType
argument_list|)
expr_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|IrcConstants
operator|.
name|IRC_TARGET
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|whoWasKickedNick
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|IrcConstants
operator|.
name|IRC_USER_KICKED
argument_list|,
name|whoWasKickedNick
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|IrcConstants
operator|.
name|IRC_USER_HOST
argument_list|,
name|user
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|IrcConstants
operator|.
name|IRC_USER_NICK
argument_list|,
name|user
operator|.
name|getNick
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|IrcConstants
operator|.
name|IRC_USER_SERVERNAME
argument_list|,
name|user
operator|.
name|getServername
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|IrcConstants
operator|.
name|IRC_USER_USERNAME
argument_list|,
name|user
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
return|return
literal|"IrcMessage: "
operator|+
name|message
return|;
block|}
else|else
block|{
return|return
literal|"IrcMessage: "
operator|+
name|getBody
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

