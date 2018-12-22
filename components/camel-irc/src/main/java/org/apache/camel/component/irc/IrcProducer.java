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
name|RuntimeCamelException
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
name|IRCUser
import|;
end_import

begin_class
DECL|class|IrcProducer
specifier|public
class|class
name|IrcProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|COMMANDS
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|COMMANDS
init|=
operator|new
name|String
index|[]
block|{
literal|"AWAY"
block|,
literal|"INVITE"
block|,
literal|"ISON"
block|,
literal|"JOIN"
block|,
literal|"KICK"
block|,
literal|"LIST"
block|,
literal|"NAMES"
block|,
literal|"PRIVMSG"
block|,
literal|"MODE"
block|,
literal|"NICK"
block|,
literal|"NOTICE"
block|,
literal|"PART"
block|,
literal|"PONG"
block|,
literal|"QUIT"
block|,
literal|"TOPIC"
block|,
literal|"WHO"
block|,
literal|"WHOIS"
block|,
literal|"WHOWAS"
block|,
literal|"USERHOST"
block|}
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|IrcConfiguration
name|configuration
decl_stmt|;
DECL|field|connection
specifier|private
name|IRCConnection
name|connection
decl_stmt|;
DECL|field|endpoint
specifier|private
name|IrcEndpoint
name|endpoint
decl_stmt|;
DECL|field|listener
specifier|private
name|IRCEventAdapter
name|listener
decl_stmt|;
DECL|method|IrcProducer (IrcEndpoint endpoint, IRCConnection connection)
specifier|public
name|IrcProducer
parameter_list|(
name|IrcEndpoint
name|endpoint
parameter_list|,
name|IRCConnection
name|connection
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
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
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
throws|throws
name|Exception
block|{
specifier|final
name|String
name|msg
init|=
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
decl_stmt|;
specifier|final
name|String
name|targetChannel
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IrcConstants
operator|.
name|IRC_TARGET
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|connection
operator|.
name|isConnected
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Lost connection to "
operator|+
name|connection
operator|.
name|getHost
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isMessageACommand
argument_list|(
name|msg
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending command: {}"
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|connection
operator|.
name|send
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|targetChannel
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending to: {} message: {}"
argument_list|,
name|targetChannel
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|connection
operator|.
name|doPrivmsg
argument_list|(
name|targetChannel
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|IrcChannel
name|channel
range|:
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getChannels
argument_list|()
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending to: {} message: {}"
argument_list|,
name|channel
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|connection
operator|.
name|doPrivmsg
argument_list|(
name|channel
operator|.
name|getName
argument_list|()
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
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
name|listener
operator|=
name|getListener
argument_list|()
expr_stmt|;
name|connection
operator|.
name|addIRCEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Sleeping for {} seconds before sending commands."
argument_list|,
name|configuration
operator|.
name|getCommandTimeout
argument_list|()
operator|/
literal|1000
argument_list|)
expr_stmt|;
comment|// sleep for a few seconds as the server sometimes takes a moment to fully connect, print banners, etc after connection established
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|configuration
operator|.
name|getCommandTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|endpoint
operator|.
name|joinChannels
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
condition|)
block|{
for|for
control|(
name|IrcChannel
name|channel
range|:
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getChannels
argument_list|()
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Parting: {}"
argument_list|,
name|channel
argument_list|)
expr_stmt|;
name|connection
operator|.
name|doPart
argument_list|(
name|channel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|removeIRCEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|isMessageACommand (String msg)
specifier|protected
name|boolean
name|isMessageACommand
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
for|for
control|(
name|String
name|command
range|:
name|COMMANDS
control|)
block|{
if|if
condition|(
name|msg
operator|.
name|startsWith
argument_list|(
name|command
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|getListener ()
specifier|public
name|IRCEventAdapter
name|getListener
parameter_list|()
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
name|listener
operator|=
operator|new
name|FilteredIRCEventAdapter
argument_list|()
expr_stmt|;
block|}
return|return
name|listener
return|;
block|}
DECL|method|setListener (IRCEventAdapter listener)
specifier|public
name|void
name|setListener
parameter_list|(
name|IRCEventAdapter
name|listener
parameter_list|)
block|{
name|this
operator|.
name|listener
operator|=
name|listener
expr_stmt|;
block|}
DECL|class|FilteredIRCEventAdapter
class|class
name|FilteredIRCEventAdapter
extends|extends
name|IRCEventAdapter
block|{
annotation|@
name|Override
DECL|method|onKick (String channel, IRCUser user, String passiveNick, String msg)
specifier|public
name|void
name|onKick
parameter_list|(
name|String
name|channel
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|passiveNick
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
comment|// check to see if I got kick and if so rejoin if autoRejoin is on
if|if
condition|(
name|passiveNick
operator|.
name|equals
argument_list|(
name|connection
operator|.
name|getNick
argument_list|()
argument_list|)
operator|&&
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAutoRejoin
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|joinChannel
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onError (int num, String msg)
specifier|public
name|void
name|onError
parameter_list|(
name|int
name|num
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|IrcProducer
operator|.
name|this
operator|.
name|endpoint
operator|.
name|handleIrcError
argument_list|(
name|num
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

