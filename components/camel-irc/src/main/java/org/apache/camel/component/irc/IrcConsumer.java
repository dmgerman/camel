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
name|support
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
name|ObjectHelper
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

begin_class
DECL|class|IrcConsumer
specifier|public
class|class
name|IrcConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|configuration
specifier|private
specifier|final
name|IrcConfiguration
name|configuration
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|IrcEndpoint
name|endpoint
decl_stmt|;
DECL|field|connection
specifier|private
specifier|final
name|IRCConnection
name|connection
decl_stmt|;
DECL|field|listener
specifier|private
name|IRCEventAdapter
name|listener
decl_stmt|;
DECL|method|IrcConsumer (IrcEndpoint endpoint, Processor processor, IRCConnection connection)
specifier|public
name|IrcConsumer
parameter_list|(
name|IrcEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|IRCConnection
name|connection
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getNickPassword
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
comment|// TODO : sleep before joinChannels() may be another useful config param (even when not identifying)
comment|// sleep for a few seconds as the server sometimes takes a moment to fully connect, print banners, etc after connection established
name|log
operator|.
name|debug
argument_list|(
literal|"Sleeping for 5 seconds before identifying to NickServ."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
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
name|log
operator|.
name|debug
argument_list|(
literal|"Identifying and enforcing nick with NickServ."
argument_list|)
expr_stmt|;
comment|// Identify nick and enforce, https://meta.wikimedia.org/wiki/IRC/Instructions#Register_your_nickname.2C_identify.2C_and_enforce
name|connection
operator|.
name|doPrivmsg
argument_list|(
literal|"nickserv"
argument_list|,
literal|"identify "
operator|+
name|configuration
operator|.
name|getNickPassword
argument_list|()
argument_list|)
expr_stmt|;
name|connection
operator|.
name|doPrivmsg
argument_list|(
literal|"nickserv"
argument_list|,
literal|"set enforce on"
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|joinChannels
argument_list|()
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|IRCConnection
name|getConnection
parameter_list|()
block|{
return|return
name|connection
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
DECL|method|onNick (IRCUser user, String newNick)
specifier|public
name|void
name|onNick
parameter_list|(
name|IRCUser
name|user
parameter_list|,
name|String
name|newNick
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isOnNick
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnNickExchange
argument_list|(
name|user
argument_list|,
name|newNick
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onQuit (IRCUser user, String msg)
specifier|public
name|void
name|onQuit
parameter_list|(
name|IRCUser
name|user
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isOnQuit
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnQuitExchange
argument_list|(
name|user
argument_list|,
name|msg
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onJoin (String channel, IRCUser user)
specifier|public
name|void
name|onJoin
parameter_list|(
name|String
name|channel
parameter_list|,
name|IRCUser
name|user
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isOnJoin
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnJoinExchange
argument_list|(
name|channel
argument_list|,
name|user
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|configuration
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
if|if
condition|(
name|configuration
operator|.
name|isOnKick
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnKickExchange
argument_list|(
name|channel
argument_list|,
name|user
argument_list|,
name|passiveNick
argument_list|,
name|msg
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onMode (String channel, IRCUser user, IRCModeParser modeParser)
specifier|public
name|void
name|onMode
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
if|if
condition|(
name|configuration
operator|.
name|isOnMode
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnModeExchange
argument_list|(
name|channel
argument_list|,
name|user
argument_list|,
name|modeParser
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onPart (String channel, IRCUser user, String msg)
specifier|public
name|void
name|onPart
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
if|if
condition|(
name|configuration
operator|.
name|isOnPart
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnPartExchange
argument_list|(
name|channel
argument_list|,
name|user
argument_list|,
name|msg
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onReply (int num, String value, String msg)
specifier|public
name|void
name|onReply
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
if|if
condition|(
name|configuration
operator|.
name|isOnReply
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnReplyExchange
argument_list|(
name|num
argument_list|,
name|value
argument_list|,
name|msg
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onTopic (String channel, IRCUser user, String topic)
specifier|public
name|void
name|onTopic
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
if|if
condition|(
name|configuration
operator|.
name|isOnTopic
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnTopicExchange
argument_list|(
name|channel
argument_list|,
name|user
argument_list|,
name|topic
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onPrivmsg (String target, IRCUser user, String msg)
specifier|public
name|void
name|onPrivmsg
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
if|if
condition|(
name|configuration
operator|.
name|isOnPrivmsg
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnPrivmsgExchange
argument_list|(
name|target
argument_list|,
name|user
argument_list|,
name|msg
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
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
block|{         }
block|}
block|}
end_class

end_unit

