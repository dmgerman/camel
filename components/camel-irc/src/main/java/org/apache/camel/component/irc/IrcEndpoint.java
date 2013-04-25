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
name|ExchangePattern
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
name|DefaultExchange
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|UnsafeUriCharactersEncoder
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
name|IRCConnection
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
name|IRCEventAdapter
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
name|IRCModeParser
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
comment|/**  * Defines the<a href="http://camel.apache.org/irc.html">IRC Endpoint</a>  *  * @version   */
end_comment

begin_class
DECL|class|IrcEndpoint
specifier|public
class|class
name|IrcEndpoint
extends|extends
name|DefaultEndpoint
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
name|IrcEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|binding
specifier|private
name|IrcBinding
name|binding
decl_stmt|;
DECL|field|configuration
specifier|private
name|IrcConfiguration
name|configuration
decl_stmt|;
DECL|field|component
specifier|private
name|IrcComponent
name|component
decl_stmt|;
DECL|method|IrcEndpoint (String endpointUri, IrcComponent component, IrcConfiguration configuration)
specifier|public
name|IrcEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|IrcComponent
name|component
parameter_list|,
name|IrcConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|endpointUri
argument_list|)
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
name|this
operator|.
name|configuration
operator|=
name|configuration
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
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnPrivmsgExchange (String target, IRCUser user, String msg)
specifier|public
name|Exchange
name|createOnPrivmsgExchange
parameter_list|(
name|String
name|target
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"PRIVMSG"
argument_list|,
name|target
argument_list|,
name|user
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnNickExchange (IRCUser user, String newNick)
specifier|public
name|Exchange
name|createOnNickExchange
parameter_list|(
name|IRCUser
name|user
parameter_list|,
name|String
name|newNick
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"NICK"
argument_list|,
name|user
argument_list|,
name|newNick
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnQuitExchange (IRCUser user, String msg)
specifier|public
name|Exchange
name|createOnQuitExchange
parameter_list|(
name|IRCUser
name|user
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"QUIT"
argument_list|,
name|user
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnJoinExchange (String channel, IRCUser user)
specifier|public
name|Exchange
name|createOnJoinExchange
parameter_list|(
name|String
name|channel
parameter_list|,
name|IRCUser
name|user
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"JOIN"
argument_list|,
name|channel
argument_list|,
name|user
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnKickExchange (String channel, IRCUser user, String whoWasKickedNick, String msg)
specifier|public
name|Exchange
name|createOnKickExchange
parameter_list|(
name|String
name|channel
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|whoWasKickedNick
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"KICK"
argument_list|,
name|channel
argument_list|,
name|user
argument_list|,
name|whoWasKickedNick
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnModeExchange (String channel, IRCUser user, IRCModeParser modeParser)
specifier|public
name|Exchange
name|createOnModeExchange
parameter_list|(
name|String
name|channel
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|IRCModeParser
name|modeParser
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"MODE"
argument_list|,
name|channel
argument_list|,
name|user
argument_list|,
name|modeParser
operator|.
name|getLine
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnPartExchange (String channel, IRCUser user, String msg)
specifier|public
name|Exchange
name|createOnPartExchange
parameter_list|(
name|String
name|channel
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"PART"
argument_list|,
name|channel
argument_list|,
name|user
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnReplyExchange (int num, String value, String msg)
specifier|public
name|Exchange
name|createOnReplyExchange
parameter_list|(
name|int
name|num
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"REPLY"
argument_list|,
name|num
argument_list|,
name|value
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createOnTopicExchange (String channel, IRCUser user, String topic)
specifier|public
name|Exchange
name|createOnTopicExchange
parameter_list|(
name|String
name|channel
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|topic
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|IrcMessage
argument_list|(
literal|"TOPIC"
argument_list|,
name|channel
argument_list|,
name|user
argument_list|,
name|topic
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|IrcProducer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|IrcProducer
argument_list|(
name|this
argument_list|,
name|component
operator|.
name|getIRCConnection
argument_list|(
name|configuration
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|IrcConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|IrcConsumer
name|answer
init|=
operator|new
name|IrcConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|component
operator|.
name|getIRCConnection
argument_list|(
name|configuration
argument_list|)
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
DECL|method|getComponent ()
specifier|public
name|IrcComponent
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
DECL|method|setComponent (IrcComponent component)
specifier|public
name|void
name|setComponent
parameter_list|(
name|IrcComponent
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
DECL|method|getBinding ()
specifier|public
name|IrcBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|IrcBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
DECL|method|setBinding (IrcBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|IrcBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|IrcConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (IrcConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|IrcConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|handleIrcError (int num, String msg)
specifier|public
name|void
name|handleIrcError
parameter_list|(
name|int
name|num
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|IRCEventAdapter
operator|.
name|ERR_NICKNAMEINUSE
operator|==
name|num
condition|)
block|{
name|handleNickInUse
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|handleNickInUse ()
specifier|private
name|void
name|handleNickInUse
parameter_list|()
block|{
name|IRCConnection
name|connection
init|=
name|component
operator|.
name|getIRCConnection
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|String
name|nick
init|=
name|connection
operator|.
name|getNick
argument_list|()
operator|+
literal|"-"
decl_stmt|;
comment|// hackish but working approach to prevent an endless loop. Abort after 4 nick attempts.
if|if
condition|(
name|nick
operator|.
name|endsWith
argument_list|(
literal|"----"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Unable to set nick: "
operator|+
name|nick
operator|+
literal|" disconnecting"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to set nick: "
operator|+
name|nick
operator|+
literal|" Retrying with "
operator|+
name|nick
operator|+
literal|"-"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|doNick
argument_list|(
name|nick
argument_list|)
expr_stmt|;
comment|// if the nick failure was doing startup channels weren't joined. So join
comment|// the channels now. It's a no-op if the channels are already joined.
name|joinChannels
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getExchange ()
specifier|private
name|DefaultExchange
name|getExchange
parameter_list|()
block|{
name|DefaultExchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|joinChannels ()
specifier|public
name|void
name|joinChannels
parameter_list|()
block|{
for|for
control|(
name|IrcChannel
name|channel
range|:
name|configuration
operator|.
name|getChannels
argument_list|()
control|)
block|{
name|joinChannel
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|joinChannel (String name)
specifier|public
name|void
name|joinChannel
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|joinChannel
argument_list|(
name|configuration
operator|.
name|findChannel
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|joinChannel (IrcChannel channel)
specifier|public
name|void
name|joinChannel
parameter_list|(
name|IrcChannel
name|channel
parameter_list|)
block|{
if|if
condition|(
name|channel
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|IRCConnection
name|connection
init|=
name|component
operator|.
name|getIRCConnection
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|String
name|chn
init|=
name|channel
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|channel
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|key
argument_list|)
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
literal|"Joining: {} using {} with secret key"
argument_list|,
name|channel
argument_list|,
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|doJoin
argument_list|(
name|chn
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
else|else
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
literal|"Joining: {} using {}"
argument_list|,
name|channel
argument_list|,
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|doJoin
argument_list|(
name|chn
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

