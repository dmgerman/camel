begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A helper class which logs errors  */
end_comment

begin_class
DECL|class|IrcLogger
specifier|public
class|class
name|IrcLogger
extends|extends
name|IRCEventAdapter
block|{
DECL|field|log
specifier|private
name|Logger
name|log
decl_stmt|;
DECL|field|server
specifier|private
name|String
name|server
decl_stmt|;
DECL|method|IrcLogger (Logger log, String server)
specifier|public
name|IrcLogger
parameter_list|(
name|Logger
name|log
parameter_list|,
name|String
name|server
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onDisconnected ()
specifier|public
name|void
name|onDisconnected
parameter_list|()
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Server: {} - onDisconnected"
argument_list|,
name|server
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|error
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onError num="
operator|+
name|num
operator|+
literal|" msg=\""
operator|+
name|msg
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onError (String msg)
specifier|public
name|void
name|onError
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onError msg=\""
operator|+
name|msg
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInvite (String chan, IRCUser user, String passiveNick)
specifier|public
name|void
name|onInvite
parameter_list|(
name|String
name|chan
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|passiveNick
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onInvite chan="
operator|+
name|chan
operator|+
literal|" user="
operator|+
name|user
operator|+
literal|" passiveNick="
operator|+
name|passiveNick
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onJoin (String chan, IRCUser user)
specifier|public
name|void
name|onJoin
parameter_list|(
name|String
name|chan
parameter_list|,
name|IRCUser
name|user
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onJoin chan="
operator|+
name|chan
operator|+
literal|" user="
operator|+
name|user
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onKick (String chan, IRCUser user, String passiveNick, String msg)
specifier|public
name|void
name|onKick
parameter_list|(
name|String
name|chan
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
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onKick chan="
operator|+
name|chan
operator|+
literal|" user="
operator|+
name|user
operator|+
literal|" passiveNick="
operator|+
name|passiveNick
operator|+
literal|" msg=\""
operator|+
name|msg
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMode (String chan, IRCUser user, IRCModeParser ircModeParser)
specifier|public
name|void
name|onMode
parameter_list|(
name|String
name|chan
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|IRCModeParser
name|ircModeParser
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onMode chan="
operator|+
name|chan
operator|+
literal|" user="
operator|+
name|user
operator|+
literal|" ircModeParser="
operator|+
name|ircModeParser
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMode (IRCUser user, String passiveNick, String mode)
specifier|public
name|void
name|onMode
parameter_list|(
name|IRCUser
name|user
parameter_list|,
name|String
name|passiveNick
parameter_list|,
name|String
name|mode
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onMode user="
operator|+
name|user
operator|+
literal|" passiveNick="
operator|+
name|passiveNick
operator|+
literal|" mode="
operator|+
name|mode
argument_list|)
expr_stmt|;
block|}
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
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onNick user="
operator|+
name|user
operator|+
literal|" newNick="
operator|+
name|newNick
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onNotice (String target, IRCUser user, String msg)
specifier|public
name|void
name|onNotice
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
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onNotice target="
operator|+
name|target
operator|+
literal|" user="
operator|+
name|user
operator|+
literal|" msg=\""
operator|+
name|msg
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onPart (String chan, IRCUser user, String msg)
specifier|public
name|void
name|onPart
parameter_list|(
name|String
name|chan
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onPart chan="
operator|+
name|chan
operator|+
literal|" user="
operator|+
name|user
operator|+
literal|" msg=\""
operator|+
name|msg
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onPing (String ping)
specifier|public
name|void
name|onPing
parameter_list|(
name|String
name|ping
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onPing ping="
operator|+
name|ping
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onPrivmsg target="
operator|+
name|target
operator|+
literal|" user="
operator|+
name|user
operator|+
literal|" msg=\""
operator|+
name|msg
operator|+
literal|"\""
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onQuit user="
operator|+
name|user
operator|+
literal|" msg=\""
operator|+
name|msg
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onRegistered ()
specifier|public
name|void
name|onRegistered
parameter_list|()
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Server: {} - onRegistered"
argument_list|,
name|server
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onReply num="
operator|+
name|num
operator|+
literal|" value=\""
operator|+
name|value
operator|+
literal|"\" msg=\""
operator|+
name|msg
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onTopic (String chan, IRCUser user, String topic)
specifier|public
name|void
name|onTopic
parameter_list|(
name|String
name|chan
parameter_list|,
name|IRCUser
name|user
parameter_list|,
name|String
name|topic
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - onTopic chan="
operator|+
name|chan
operator|+
literal|" user="
operator|+
name|user
operator|+
literal|" topic="
operator|+
name|topic
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unknown (String prefix, String command, String middle, String trailing)
specifier|public
name|void
name|unknown
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|command
parameter_list|,
name|String
name|middle
parameter_list|,
name|String
name|trailing
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Server: "
operator|+
name|server
operator|+
literal|" - unknown prefix="
operator|+
name|prefix
operator|+
literal|" command="
operator|+
name|command
operator|+
literal|" middle="
operator|+
name|middle
operator|+
literal|" trailing="
operator|+
name|trailing
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

