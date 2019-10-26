begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.irc.it
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
operator|.
name|it
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
name|irc
operator|.
name|IrcConstants
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
DECL|class|IrcPrivmsgTest
specifier|public
class|class
name|IrcPrivmsgTest
extends|extends
name|IrcIntegrationTestSupport
block|{
DECL|field|expectedBody1
specifier|protected
name|String
name|expectedBody1
init|=
literal|"Message One"
decl_stmt|;
DECL|field|expectedBody2
specifier|protected
name|String
name|expectedBody2
init|=
literal|"Message Two"
decl_stmt|;
DECL|field|body1
specifier|protected
name|String
name|body1
init|=
name|expectedBody1
decl_stmt|;
DECL|field|body2
specifier|protected
name|String
name|body2
init|=
name|expectedBody2
decl_stmt|;
DECL|field|sentMessages
specifier|private
name|boolean
name|sentMessages
decl_stmt|;
annotation|@
name|Test
DECL|method|testIrcPrivateMessages ()
specifier|public
name|void
name|testIrcPrivateMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody1
argument_list|,
name|expectedBody2
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
annotation|@
name|Override
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
literal|"direct:mock"
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
name|from
argument_list|(
literal|"direct:mock"
argument_list|)
operator|.
name|filter
argument_list|(
name|e
lambda|->
operator|!
name|e
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
operator|.
name|contains
argument_list|(
literal|"VERSION"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|resultEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|sendUri ()
specifier|protected
name|String
name|sendUri
parameter_list|()
block|{
return|return
literal|"ircs://{{camelTo}}@{{server}}?channels={{channel1}}&username={{username}}&password={{password}}"
return|;
block|}
comment|/**      * Lets send messages once the consumer has joined      */
DECL|method|sendMessages ()
specifier|protected
name|void
name|sendMessages
parameter_list|()
throws|throws
name|InterruptedException
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
name|properties
operator|.
name|get
argument_list|(
literal|"camelFrom"
argument_list|)
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
name|properties
operator|.
name|get
argument_list|(
literal|"camelFrom"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

