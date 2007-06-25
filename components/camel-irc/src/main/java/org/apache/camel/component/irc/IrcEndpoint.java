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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|IrcEndpoint
specifier|public
class|class
name|IrcEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|IrcExchange
argument_list|>
block|{
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
name|endpointUri
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
literal|false
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|IrcExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createOnPrivmsgExchange (String target, IRCUser user, String msg)
specifier|public
name|IrcExchange
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
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
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
return|;
block|}
DECL|method|createOnNickExchange (IRCUser user, String newNick)
specifier|public
name|IrcExchange
name|createOnNickExchange
parameter_list|(
name|IRCUser
name|user
parameter_list|,
name|String
name|newNick
parameter_list|)
block|{
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
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
return|;
block|}
DECL|method|createOnQuitExchange (IRCUser user, String msg)
specifier|public
name|IrcExchange
name|createOnQuitExchange
parameter_list|(
name|IRCUser
name|user
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
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
return|;
block|}
DECL|method|createOnJoinExchange (String channel, IRCUser user)
specifier|public
name|IrcExchange
name|createOnJoinExchange
parameter_list|(
name|String
name|channel
parameter_list|,
name|IRCUser
name|user
parameter_list|)
block|{
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
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
return|;
block|}
DECL|method|createOnKickExchange (String channel, IRCUser user, String whoWasKickedNick, String msg)
specifier|public
name|IrcExchange
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
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
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
return|;
block|}
DECL|method|createOnModeExchange (String channel, IRCUser user, IRCModeParser modeParser)
specifier|public
name|IrcExchange
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
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
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
return|;
block|}
DECL|method|createOnPartExchange (String channel, IRCUser user, String msg)
specifier|public
name|IrcExchange
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
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
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
return|;
block|}
DECL|method|createOnTopicExchange (String channel, IRCUser user, String topic)
specifier|public
name|IrcExchange
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
return|return
operator|new
name|IrcExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
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
return|return
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
specifier|final
name|IrcConfiguration
name|config
init|=
operator|new
name|IrcConfiguration
argument_list|(
literal|"irc.codehaus.org"
argument_list|,
literal|"camel-irc"
argument_list|,
literal|"Camel IRC Component"
argument_list|,
literal|"#camel-irc"
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
comment|//        conn.addIRCEventListener(new IRCEventAdapter() {
comment|//
comment|//            @Override
comment|//            public void onRegistered() {
comment|//                super.onRegistered();
comment|//                System.out.println("onRegistered");
comment|//            }
comment|//
comment|//            @Override
comment|//            public void onDisconnected() {
comment|//                super.onDisconnected();
comment|//                System.out.println("onDisconnected");
comment|//            }
comment|//
comment|//            @Override
comment|//            public void onMode(String string, IRCUser ircUser, IRCModeParser ircModeParser) {
comment|//                super.onMode(string, ircUser, ircModeParser);
comment|//                System.out.println("onMode.string = " + string);
comment|//                System.out.println("onMode.ircUser = " + ircUser);
comment|//                System.out.println("onMode.ircModeParser = " + ircModeParser);
comment|//            }
comment|//
comment|//            @Override
comment|//            public void onMode(IRCUser ircUser, String string, String string1) {
comment|//                super.onMode(ircUser, string, string1);
comment|//                System.out.println("onMode.ircUser = " + ircUser);
comment|//                System.out.println("onMode.string = " + string);
comment|//                System.out.println("onMode.string1 = " + string1);
comment|//            }
comment|//
comment|//            @Override
comment|//            public void onPing(String string) {
comment|//                super.onPing(string);
comment|//                System.out.println("onPing.string = " + string);
comment|//            }
comment|//
comment|//            @Override
comment|//            public void onError(String string) {
comment|//                System.out.println("onError.string = " + string);
comment|//            }
comment|//
comment|//            @Override
comment|//            public void onError(int i, String string) {
comment|//                super.onError(i, string);
comment|//                System.out.println("onError.i = " + i);
comment|//                System.out.println("onError.string = " + string);
comment|//            }
comment|//
comment|//            @Override
comment|//            public void unknown(String string, String string1, String string2, String string3) {
comment|//                super.unknown(string, string1, string2, string3);
comment|//                System.out.println("unknown.string = " + string);
comment|//                System.out.println("unknown.string1 = " + string1);
comment|//                System.out.println("unknown.string2 = " + string2);
comment|//                System.out.println("unknown.string3 = " + string3);
comment|//            }
comment|//        });
name|conn
operator|.
name|setEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
comment|//        while (!conn.isConnected()) {
comment|//            Thread.sleep(1000);
comment|//            System.out.println("Sleeping");
comment|//        }
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Connected"
argument_list|)
expr_stmt|;
comment|//        conn.send("/JOIN #CAMEL");
comment|//        conn.doPrivmsg("nnordrum", "hi!");
comment|//        System.out.println("Joining Channel");
name|conn
operator|.
name|doJoin
argument_list|(
name|config
operator|.
name|getTarget
argument_list|()
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

