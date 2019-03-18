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
name|IRCEventAdapter
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
DECL|class|IrcConsumerTest
specifier|public
class|class
name|IrcConsumerTest
block|{
DECL|field|connection
specifier|private
name|IRCConnection
name|connection
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|endpoint
specifier|private
name|IrcEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
name|IrcConfiguration
name|configuration
decl_stmt|;
DECL|field|consumer
specifier|private
name|IrcConsumer
name|consumer
decl_stmt|;
DECL|field|listener
specifier|private
name|IRCEventAdapter
name|listener
decl_stmt|;
annotation|@
name|Before
DECL|method|doSetup ()
specifier|public
name|void
name|doSetup
parameter_list|()
block|{
name|connection
operator|=
name|mock
argument_list|(
name|IRCConnection
operator|.
name|class
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|mock
argument_list|(
name|IrcEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|processor
operator|=
name|mock
argument_list|(
name|Processor
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
name|listener
operator|=
name|mock
argument_list|(
name|IRCEventAdapter
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
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|consumer
operator|=
operator|new
name|IrcConsumer
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|connection
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doStopTest ()
specifier|public
name|void
name|doStopTest
parameter_list|()
throws|throws
name|Exception
block|{
name|consumer
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|doPart
argument_list|(
literal|"#chan1"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|doPart
argument_list|(
literal|"#chan2"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|removeIRCEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doStartTest ()
specifier|public
name|void
name|doStartTest
parameter_list|()
throws|throws
name|Exception
block|{
name|consumer
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|)
operator|.
name|addIRCEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|endpoint
argument_list|)
operator|.
name|joinChannels
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

