begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jvnet
operator|.
name|mock_javamail
operator|.
name|Mailbox
import|;
end_import

begin_comment
comment|/**  * Unit test to verify that we can have multiple recipients in To, CC and BCC  */
end_comment

begin_class
DECL|class|MailMultipleRecipientsTest
specifier|public
class|class
name|MailMultipleRecipientsTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendWithMultipleRecipientsInHeader ()
specifier|public
name|void
name|testSendWithMultipleRecipientsInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
comment|// START SNIPPET: e1
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// test with both comma and semi colon as Camel supports both kind of separators
name|headers
operator|.
name|put
argument_list|(
literal|"to"
argument_list|,
literal|"claus@localhost, willem@localhost ; hadrian@localhost, \"Snell, Tracy\"<tracy@localhost>"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"cc"
argument_list|,
literal|"james@localhost"
argument_list|)
expr_stmt|;
name|assertMailbox
argument_list|(
literal|"claus"
argument_list|)
expr_stmt|;
name|assertMailbox
argument_list|(
literal|"willem"
argument_list|)
expr_stmt|;
name|assertMailbox
argument_list|(
literal|"hadrian"
argument_list|)
expr_stmt|;
name|assertMailbox
argument_list|(
literal|"tracy"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"smtp://localhost"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendWithMultipleRecipientsPreConfigured ()
specifier|public
name|void
name|testSendWithMultipleRecipientsPreConfigured
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|assertMailbox
argument_list|(
literal|"claus"
argument_list|)
expr_stmt|;
name|assertMailbox
argument_list|(
literal|"willem"
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e2
comment|// here we have pre configured the to receivers to claus and willem. Notice we use comma to separate
comment|// the two recipients. Camel also support using colon as separator char
name|template
operator|.
name|sendBody
argument_list|(
literal|"smtp://localhost?to=claus@localhost,willem@localhost&cc=james@localhost"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|assertMailbox (String name)
specifier|private
name|void
name|assertMailbox
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:"
operator|+
name|name
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"cc"
argument_list|,
literal|"james@localhost"
argument_list|)
expr_stmt|;
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
literal|"pop3://claus@localhost?initialDelay=100&delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:claus"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"pop3://willem@localhost?initialDelay=100&delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:willem"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"pop3://hadrian@localhost?initialDelay=100&delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:hadrian"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"pop3://tracy@localhost?initialDelay=100&delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:tracy"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

