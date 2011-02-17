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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|IrcMultiChannelRouteTest
specifier|public
class|class
name|IrcMultiChannelRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|field|body1
specifier|protected
name|String
name|body1
init|=
literal|"Message One"
decl_stmt|;
DECL|field|body2
specifier|protected
name|String
name|body2
init|=
literal|"Message Two"
decl_stmt|;
DECL|field|body3
specifier|protected
name|String
name|body3
init|=
literal|"Message Three"
decl_stmt|;
DECL|field|sentMessages
specifier|private
name|boolean
name|sentMessages
decl_stmt|;
annotation|@
name|Ignore
argument_list|(
literal|"test manual"
argument_list|)
annotation|@
name|Test
DECL|method|testIrcMessages ()
specifier|public
name|void
name|testIrcMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|=
operator|(
name|MockEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|//consumer is going to receive two copies of body3
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body1
argument_list|,
name|body2
argument_list|,
name|body3
argument_list|,
name|body3
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Received exchange: "
operator|+
name|exchange
operator|+
literal|" headers: "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|fromUri
argument_list|()
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|IrcConstants
operator|.
name|IRC_MESSAGE_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"PRIVMSG"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|IrcConstants
operator|.
name|IRC_MESSAGE_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"JOIN"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:consumerJoined"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:consumerJoined"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
name|sendMessages
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|sendUri ()
specifier|protected
name|String
name|sendUri
parameter_list|()
block|{
return|return
literal|"irc://camel-prd@irc.codehaus.org:6667?nickname=camel-prd&channels=#camel-test1,#camel-test2"
return|;
block|}
DECL|method|fromUri ()
specifier|protected
name|String
name|fromUri
parameter_list|()
block|{
return|return
literal|"irc://camel-con@irc.codehaus.org:6667?nickname=camel-con&channels=#camel-test1,#camel-test2"
return|;
block|}
comment|/**      * Lets send messages once the consumer has joined      */
DECL|method|sendMessages ()
specifier|protected
name|void
name|sendMessages
parameter_list|()
block|{
if|if
condition|(
operator|!
name|sentMessages
condition|)
block|{
name|sentMessages
operator|=
literal|true
expr_stmt|;
comment|// now the consumer has joined, lets send some messages
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sendUri
argument_list|()
argument_list|,
name|body1
argument_list|,
literal|"irc.target"
argument_list|,
literal|"#camel-test1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sendUri
argument_list|()
argument_list|,
name|body2
argument_list|,
literal|"irc.target"
argument_list|,
literal|"#camel-test2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|sendUri
argument_list|()
argument_list|,
name|body3
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

