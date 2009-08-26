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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CodehausIrcChat
specifier|public
specifier|final
class|class
name|CodehausIrcChat
block|{
DECL|class|CodehausIRCEventAdapter
specifier|private
specifier|static
specifier|final
class|class
name|CodehausIRCEventAdapter
extends|extends
name|IRCEventAdapter
block|{
annotation|@
name|Override
DECL|method|onRegistered ()
specifier|public
name|void
name|onRegistered
parameter_list|()
block|{
name|super
operator|.
name|onRegistered
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onRegistered"
argument_list|)
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
name|super
operator|.
name|onDisconnected
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onDisconnected"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMode (String string, IRCUser ircUser, IRCModeParser ircModeParser)
specifier|public
name|void
name|onMode
parameter_list|(
name|String
name|string
parameter_list|,
name|IRCUser
name|ircUser
parameter_list|,
name|IRCModeParser
name|ircModeParser
parameter_list|)
block|{
name|super
operator|.
name|onMode
argument_list|(
name|string
argument_list|,
name|ircUser
argument_list|,
name|ircModeParser
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onMode.string = "
operator|+
name|string
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onMode.ircUser = "
operator|+
name|ircUser
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onMode.ircModeParser = "
operator|+
name|ircModeParser
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMode (IRCUser ircUser, String string, String string1)
specifier|public
name|void
name|onMode
parameter_list|(
name|IRCUser
name|ircUser
parameter_list|,
name|String
name|string
parameter_list|,
name|String
name|string1
parameter_list|)
block|{
name|super
operator|.
name|onMode
argument_list|(
name|ircUser
argument_list|,
name|string
argument_list|,
name|string1
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onMode.ircUser = "
operator|+
name|ircUser
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onMode.string = "
operator|+
name|string
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onMode.string1 = "
operator|+
name|string1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onPing (String string)
specifier|public
name|void
name|onPing
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|super
operator|.
name|onPing
argument_list|(
name|string
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onPing.string = "
operator|+
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onError (String string)
specifier|public
name|void
name|onError
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onError.string = "
operator|+
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onError (int i, String string)
specifier|public
name|void
name|onError
parameter_list|(
name|int
name|i
parameter_list|,
name|String
name|string
parameter_list|)
block|{
name|super
operator|.
name|onError
argument_list|(
name|i
argument_list|,
name|string
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onError.i = "
operator|+
name|i
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"onError.string = "
operator|+
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unknown (String string, String string1, String string2, String string3)
specifier|public
name|void
name|unknown
parameter_list|(
name|String
name|string
parameter_list|,
name|String
name|string1
parameter_list|,
name|String
name|string2
parameter_list|,
name|String
name|string3
parameter_list|)
block|{
name|super
operator|.
name|unknown
argument_list|(
name|string
argument_list|,
name|string1
argument_list|,
name|string2
argument_list|,
name|string3
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"unknown.string = "
operator|+
name|string
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"unknown.string1 = "
operator|+
name|string1
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"unknown.string2 = "
operator|+
name|string2
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"unknown.string3 = "
operator|+
name|string3
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|CodehausIrcChat ()
specifier|private
name|CodehausIrcChat
parameter_list|()
block|{     }
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|//final IrcConfiguration config = new IrcConfiguration("irc.codehaus.org", "camel-irc", "Camel IRC Component", "#camel-test");
name|List
argument_list|<
name|String
argument_list|>
name|channels
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|channels
operator|.
name|add
argument_list|(
literal|"#camel-test"
argument_list|)
expr_stmt|;
specifier|final
name|IrcConfiguration
name|config
init|=
operator|new
name|IrcConfiguration
argument_list|(
literal|"irc.codehaus.org"
argument_list|,
literal|"camel-rc"
argument_list|,
literal|"Camel IRC Component"
argument_list|,
name|channels
argument_list|)
decl_stmt|;
specifier|final
name|IRCConnection
name|conn
init|=
operator|new
name|IRCConnection
argument_list|(
name|config
operator|.
name|getHostname
argument_list|()
argument_list|,
name|config
operator|.
name|getPorts
argument_list|()
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|,
name|config
operator|.
name|getNickname
argument_list|()
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|,
name|config
operator|.
name|getRealname
argument_list|()
argument_list|)
decl_stmt|;
name|conn
operator|.
name|addIRCEventListener
argument_list|(
operator|new
name|CodehausIRCEventAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
comment|// conn.setDaemon(true);
name|conn
operator|.
name|setColors
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setPong
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|conn
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
comment|// while (!conn.isConnected()) {
comment|// Thread.sleep(1000);
comment|// System.out.println("Sleeping");
comment|// }
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Connected"
argument_list|)
expr_stmt|;
comment|// conn.send("/JOIN #camel-test");
comment|// System.out.println("Joining Channel: " + config.getTarget());
for|for
control|(
name|String
name|channel
range|:
name|config
operator|.
name|getChannels
argument_list|()
control|)
block|{
name|conn
operator|.
name|doJoin
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
name|conn
operator|.
name|doPrivmsg
argument_list|(
literal|"#camel-test"
argument_list|,
literal|"hi!"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

