begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
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
name|ShutdownRunningTask
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

begin_class
DECL|class|MyBatisShutdownAllTasksTest
specifier|public
class|class
name|MyBatisShutdownAllTasksTest
extends|extends
name|MyBatisTestSupport
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// super will insert 2 accounts already
name|Account
name|account
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|account
operator|.
name|setId
argument_list|(
literal|881
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"a@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mybatis:insertAccount?statementType=Insert"
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|account
operator|=
operator|new
name|Account
argument_list|()
expr_stmt|;
name|account
operator|.
name|setId
argument_list|(
literal|882
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"b@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mybatis:insertAccount?statementType=Insert"
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|account
operator|=
operator|new
name|Account
argument_list|()
expr_stmt|;
name|account
operator|.
name|setId
argument_list|(
literal|883
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"c@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mybatis:insertAccount?statementType=Insert"
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|account
operator|=
operator|new
name|Account
argument_list|()
expr_stmt|;
name|account
operator|.
name|setId
argument_list|(
literal|884
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"D"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"D"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"d@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mybatis:insertAccount?statementType=Insert"
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|account
operator|=
operator|new
name|Account
argument_list|()
expr_stmt|;
name|account
operator|.
name|setId
argument_list|(
literal|885
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"E"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"E"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"e@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mybatis:insertAccount?statementType=Insert"
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|account
operator|=
operator|new
name|Account
argument_list|()
expr_stmt|;
name|account
operator|.
name|setId
argument_list|(
literal|886
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"F"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"F"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"f@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mybatis:insertAccount?statementType=Insert"
argument_list|,
name|account
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testShutdownAllTasks ()
specifier|public
name|void
name|testShutdownAllTasks
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|startRoute
argument_list|(
literal|"route1"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|bar
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|bar
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setResultWaitTime
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// shutdown during processing
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// sleep a little
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// should route all 8
name|assertEquals
argument_list|(
literal|"Should complete all messages"
argument_list|,
literal|8
argument_list|,
name|bar
operator|.
name|getReceivedCounter
argument_list|()
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"mybatis:selectAllAccounts"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|routeId
argument_list|(
literal|"route1"
argument_list|)
comment|// let it complete all tasks
operator|.
name|shutdownRunningTask
argument_list|(
name|ShutdownRunningTask
operator|.
name|CompleteAllTasks
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

