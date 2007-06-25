begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IRCEventListener
import|;
end_import

begin_class
DECL|class|IrcProducer
specifier|public
class|class
name|IrcProducer
extends|extends
name|DefaultProducer
argument_list|<
name|IrcExchange
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|IrcProducer
operator|.
name|class
argument_list|)
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
DECL|field|ircErrorLogger
specifier|private
name|IRCEventListener
name|ircErrorLogger
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
try|try
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
if|if
condition|(
name|isMessageACommand
argument_list|(
name|msg
argument_list|)
condition|)
block|{
name|connection
operator|.
name|send
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|String
name|target
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"sending to: "
operator|+
name|target
operator|+
literal|" message: "
operator|+
name|msg
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|doPrivmsg
argument_list|(
name|target
argument_list|,
name|msg
argument_list|)
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
name|RuntimeCamelException
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
name|ircErrorLogger
operator|=
name|createIrcErrorLogger
argument_list|()
expr_stmt|;
name|connection
operator|.
name|addIRCEventListener
argument_list|(
name|ircErrorLogger
argument_list|)
expr_stmt|;
specifier|final
name|String
name|target
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTarget
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"joining: "
operator|+
name|target
argument_list|)
expr_stmt|;
name|connection
operator|.
name|doJoin
argument_list|(
name|target
argument_list|)
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|removeIRCEventListener
argument_list|(
name|ircErrorLogger
argument_list|)
expr_stmt|;
block|}
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
name|commands
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
DECL|method|createIrcErrorLogger ()
specifier|protected
name|IRCEventListener
name|createIrcErrorLogger
parameter_list|()
block|{
return|return
operator|new
name|IrcErrorLogger
argument_list|(
name|log
argument_list|)
return|;
block|}
DECL|field|commands
specifier|public
specifier|final
name|String
index|[]
name|commands
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
block|}
end_class

end_unit

