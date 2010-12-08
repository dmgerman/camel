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
name|Dictionary
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CamelContext
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
name|DefaultCamelContext
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

begin_class
DECL|class|IrcConfigurationTest
specifier|public
class|class
name|IrcConfigurationTest
extends|extends
name|TestCase
block|{
annotation|@
name|Test
DECL|method|testConfigureFormat1 ()
specifier|public
name|void
name|testConfigureFormat1
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|IrcComponent
name|component
init|=
operator|new
name|IrcComponent
argument_list|(
name|camel
argument_list|)
decl_stmt|;
comment|// irc:nick@host[:port]/#room[?options]
name|IrcEndpoint
name|endpoint
init|=
operator|(
name|IrcEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"irc://camelbot@irc.freenode.net/#camel"
argument_list|)
decl_stmt|;
name|IrcConfiguration
name|conf
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camelbot"
argument_list|,
name|conf
operator|.
name|getNickname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"irc.freenode.net"
argument_list|,
name|conf
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|channels
init|=
name|conf
operator|.
name|getChannels
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|channels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#camel"
argument_list|,
name|channels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureFormat2 ()
specifier|public
name|void
name|testConfigureFormat2
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|IrcComponent
name|component
init|=
operator|new
name|IrcComponent
argument_list|(
name|camel
argument_list|)
decl_stmt|;
comment|// irc:nick@host[:port]/#room[?options]
name|IrcEndpoint
name|endpoint
init|=
operator|(
name|IrcEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"irc://camelbot@irc.freenode.net?channels=#camel"
argument_list|)
decl_stmt|;
name|IrcConfiguration
name|conf
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camelbot"
argument_list|,
name|conf
operator|.
name|getNickname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"irc.freenode.net"
argument_list|,
name|conf
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|channels
init|=
name|conf
operator|.
name|getChannels
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|channels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#camel"
argument_list|,
name|channels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureFormat3 ()
specifier|public
name|void
name|testConfigureFormat3
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|IrcComponent
name|component
init|=
operator|new
name|IrcComponent
argument_list|(
name|camel
argument_list|)
decl_stmt|;
comment|// irc:nick@host[:port]/#room[?options]
name|IrcEndpoint
name|endpoint
init|=
operator|(
name|IrcEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"irc://irc.freenode.net?channels=#camel&nickname=camelbot"
argument_list|)
decl_stmt|;
name|IrcConfiguration
name|conf
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camelbot"
argument_list|,
name|conf
operator|.
name|getNickname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"irc.freenode.net"
argument_list|,
name|conf
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|channels
init|=
name|conf
operator|.
name|getChannels
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|channels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#camel"
argument_list|,
name|channels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureFormat4 ()
specifier|public
name|void
name|testConfigureFormat4
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|IrcComponent
name|component
init|=
operator|new
name|IrcComponent
argument_list|(
name|camel
argument_list|)
decl_stmt|;
comment|// irc:nick@host[:port]/#room[?options]
name|IrcEndpoint
name|endpoint
init|=
operator|(
name|IrcEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"irc://irc.freenode.net?keys=,foo&channels=#camel,#smx&nickname=camelbot"
argument_list|)
decl_stmt|;
name|IrcConfiguration
name|conf
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camelbot"
argument_list|,
name|conf
operator|.
name|getNickname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"irc.freenode.net"
argument_list|,
name|conf
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|channels
init|=
name|conf
operator|.
name|getChannels
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|channels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#camel"
argument_list|,
name|channels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|keys
init|=
name|conf
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|conf
operator|.
name|getKey
argument_list|(
literal|"#smx"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureFormat5 ()
specifier|public
name|void
name|testConfigureFormat5
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|IrcComponent
name|component
init|=
operator|new
name|IrcComponent
argument_list|(
name|camel
argument_list|)
decl_stmt|;
comment|// irc:nick@host[:port]/#room[?options]
name|IrcEndpoint
name|endpoint
init|=
operator|(
name|IrcEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"irc://badnick@irc.freenode.net?keys=foo,&channels=#camel,#smx&realname=Camel Bot&nickname=camelbot"
argument_list|)
decl_stmt|;
name|IrcConfiguration
name|conf
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camelbot"
argument_list|,
name|conf
operator|.
name|getNickname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"irc.freenode.net"
argument_list|,
name|conf
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|channels
init|=
name|conf
operator|.
name|getChannels
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|channels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#camel"
argument_list|,
name|channels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|keys
init|=
name|conf
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|conf
operator|.
name|getKey
argument_list|(
literal|"#camel"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel Bot"
argument_list|,
name|conf
operator|.
name|getRealname
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureFormat6 ()
specifier|public
name|void
name|testConfigureFormat6
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|IrcComponent
name|component
init|=
operator|new
name|IrcComponent
argument_list|(
name|camel
argument_list|)
decl_stmt|;
comment|// irc:nick@host[:port]/#room[?options]
name|IrcEndpoint
name|endpoint
init|=
operator|(
name|IrcEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"irc://badnick@irc.freenode.net?keys=foo,bar&channels=#camel,#smx&realname=Camel Bot&nickname=camelbot"
argument_list|)
decl_stmt|;
name|IrcConfiguration
name|conf
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camelbot"
argument_list|,
name|conf
operator|.
name|getNickname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"irc.freenode.net"
argument_list|,
name|conf
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|channels
init|=
name|conf
operator|.
name|getChannels
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|channels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#camel"
argument_list|,
name|channels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|keys
init|=
name|conf
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|conf
operator|.
name|getKey
argument_list|(
literal|"#camel"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|conf
operator|.
name|getKey
argument_list|(
literal|"#smx"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel Bot"
argument_list|,
name|conf
operator|.
name|getRealname
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

