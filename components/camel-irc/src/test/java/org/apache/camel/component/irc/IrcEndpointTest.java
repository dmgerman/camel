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
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|IRCConstants
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|never
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|IrcEndpointTest
specifier|public
class|class
name|IrcEndpointTest
block|{
DECL|field|component
specifier|private
name|IrcComponent
name|component
decl_stmt|;
DECL|field|configuration
specifier|private
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
annotation|@
name|Before
DECL|method|doSetup ()
specifier|public
name|void
name|doSetup
parameter_list|()
block|{
name|component
operator|=
name|mock
argument_list|(
name|IrcComponent
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|=
name|mock
argument_list|(
name|IrcConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|connection
operator|=
name|mock
argument_list|(
name|IRCConnection
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|IrcChannel
argument_list|>
name|channels
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|channels
operator|.
name|add
argument_list|(
operator|new
name|IrcChannel
argument_list|(
literal|"#chan1"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|channels
operator|.
name|add
argument_list|(
operator|new
name|IrcChannel
argument_list|(
literal|"#chan2"
argument_list|,
literal|"chan2key"
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|configuration
operator|.
name|getChannels
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|channels
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|configuration
operator|.
name|findChannel
argument_list|(
literal|"#chan1"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|channels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|configuration
operator|.
name|findChannel
argument_list|(
literal|"#chan2"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|channels
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|component
operator|.
name|getIRCConnection
argument_list|(
name|configuration
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|IrcEndpoint
argument_list|(
literal|"foo"
argument_list|,
name|component
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doJoinChannelTestNoKey ()
specifier|public
name|void
name|doJoinChannelTestNoKey
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|joinChannel
argument_list|(
literal|"#chan1"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|doJoin
argument_list|(
literal|"#chan1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doJoinChannelTestKey ()
specifier|public
name|void
name|doJoinChannelTestKey
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|joinChannel
argument_list|(
literal|"#chan2"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|doJoin
argument_list|(
literal|"#chan2"
argument_list|,
literal|"chan2key"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doJoinChannels ()
specifier|public
name|void
name|doJoinChannels
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|joinChannels
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|doJoin
argument_list|(
literal|"#chan1"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|doJoin
argument_list|(
literal|"#chan2"
argument_list|,
literal|"chan2key"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doHandleIrcErrorNickInUse ()
specifier|public
name|void
name|doHandleIrcErrorNickInUse
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|connection
operator|.
name|getNick
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"nick"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|handleIrcError
argument_list|(
name|IRCConstants
operator|.
name|ERR_NICKNAMEINUSE
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|doNick
argument_list|(
literal|"nick-"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|connection
operator|.
name|getNick
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"nick---"
argument_list|)
expr_stmt|;
comment|// confirm doNick was not called
name|verify
argument_list|(
name|connection
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|doNick
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

