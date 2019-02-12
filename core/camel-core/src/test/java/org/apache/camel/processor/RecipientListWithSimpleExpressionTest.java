begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
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
name|ContextTestSupport
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
name|Header
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
DECL|class|RecipientListWithSimpleExpressionTest
specifier|public
class|class
name|RecipientListWithSimpleExpressionTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testRecipientList ()
specifier|public
name|void
name|testRecipientList
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|simple
argument_list|(
literal|"mock:${in.header.queue}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:"
operator|+
name|i
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|50
argument_list|)
expr_stmt|;
block|}
comment|// use concurrent producers to send a lot of messages
name|ExecutorService
name|executors
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|10
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50
condition|;
name|i
operator|++
control|)
block|{
name|executors
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|,
literal|"queue"
argument_list|,
name|i
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|executors
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|class|MyBeanRouter
specifier|public
specifier|static
class|class
name|MyBeanRouter
block|{
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RecipientList
DECL|method|route (@eaderR) String queue)
specifier|public
name|String
name|route
parameter_list|(
annotation|@
name|Header
argument_list|(
literal|"queue"
argument_list|)
name|String
name|queue
parameter_list|)
block|{
return|return
literal|"mock:"
operator|+
name|queue
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|testStatic ()
specifier|public
name|void
name|testStatic
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:0"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:4"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:4"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:5"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:5"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:6"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:6"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:7"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:7"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:8"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:8"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:9"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:9"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:"
operator|+
name|i
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|50
argument_list|)
expr_stmt|;
block|}
comment|// use concurrent producers to send a lot of messages
name|ExecutorService
name|executors
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|10
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50
condition|;
name|i
operator|++
control|)
block|{
name|executors
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:"
operator|+
name|i
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|,
literal|"queue"
argument_list|,
name|i
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|executors
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
