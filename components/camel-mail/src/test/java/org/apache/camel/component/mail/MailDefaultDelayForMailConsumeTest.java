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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StopWatch
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
comment|/**  * Unit test for testing mail polling is happening according to the default poll interval.  */
end_comment

begin_class
DECL|class|MailDefaultDelayForMailConsumeTest
specifier|public
class|class
name|MailDefaultDelayForMailConsumeTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testConsuming ()
specifier|public
name|void
name|testConsuming
parameter_list|()
throws|throws
name|Exception
block|{
comment|// clear mailbox
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello London"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"smtp://bond@localhost"
argument_list|,
literal|"Hello London"
argument_list|)
expr_stmt|;
comment|// first poll should happen immediately
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|2000L
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Paris"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|1000L
operator|+
literal|2000L
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"smtp://bond@localhost"
argument_list|,
literal|"Hello Paris"
argument_list|)
expr_stmt|;
comment|// poll next mail and that is should be done within the default delay (overrule to 1 sec) + 2 sec slack
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|long
name|delta
init|=
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Camel should not default poll the mailbox to often"
argument_list|,
name|delta
operator|>
literal|1000
operator|-
literal|1000L
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
comment|// we overrule the default of 60 sec to 1 so the unit test is faster
name|from
argument_list|(
literal|"pop3://bond@localhost?delay=1000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

