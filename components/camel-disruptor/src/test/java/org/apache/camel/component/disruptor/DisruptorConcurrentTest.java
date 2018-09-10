begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|ProducerTemplate
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
name|impl
operator|.
name|DefaultProducerTemplate
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

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|DisruptorConcurrentTest
specifier|public
class|class
name|DisruptorConcurrentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testDisruptorConcurrentInOnly ()
specifier|public
name|void
name|testDisruptorConcurrentInOnly
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
name|expectedMessageCount
argument_list|(
literal|20
argument_list|)
expr_stmt|;
comment|// should at least take 3 sec
name|mock
operator|.
name|setResultMinimumWaitTime
argument_list|(
literal|3000
argument_list|)
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisruptorConcurrentInOnlyWithAsync ()
specifier|public
name|void
name|testDisruptorConcurrentInOnlyWithAsync
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
name|expectedMessageCount
argument_list|(
literal|20
argument_list|)
expr_stmt|;
comment|// should at least take 3 sec
name|mock
operator|.
name|setResultMinimumWaitTime
argument_list|(
literal|3000
argument_list|)
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|asyncSendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisruptorConcurrentInOut ()
specifier|public
name|void
name|testDisruptorConcurrentInOut
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
name|expectedMessageCount
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Bye"
argument_list|)
expr_stmt|;
comment|// should at least take 3 sec
name|mock
operator|.
name|setResultMinimumWaitTime
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
specifier|final
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
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|replies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|20
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|num
init|=
name|i
decl_stmt|;
specifier|final
name|Object
name|out
init|=
name|executors
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|template
operator|.
name|requestBody
argument_list|(
literal|"disruptor:bar"
argument_list|,
literal|"Message "
operator|+
name|num
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|replies
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|replies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|executors
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisruptorConcurrentInOutWithAsync ()
specifier|public
name|void
name|testDisruptorConcurrentInOutWithAsync
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
name|expectedMessageCount
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Bye"
argument_list|)
expr_stmt|;
comment|// should at least take 3 sec
name|mock
operator|.
name|setResultMinimumWaitTime
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// use our own template that has a higher thread pool than default camel that uses 5
specifier|final
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|10
argument_list|)
decl_stmt|;
specifier|final
name|ProducerTemplate
name|pt
init|=
operator|new
name|DefaultProducerTemplate
argument_list|(
name|context
argument_list|,
name|executor
argument_list|)
decl_stmt|;
comment|// must start the template
name|pt
operator|.
name|start
argument_list|()
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Future
argument_list|<
name|Object
argument_list|>
argument_list|>
name|replies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|20
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Future
argument_list|<
name|Object
argument_list|>
name|out
init|=
name|pt
operator|.
name|asyncRequestBody
argument_list|(
literal|"disruptor:bar"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
decl_stmt|;
name|replies
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|replies
operator|.
name|size
argument_list|()
argument_list|)
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|out
init|=
operator|(
name|String
operator|)
name|replies
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|startsWith
argument_list|(
literal|"Bye"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|pt
operator|.
name|stop
argument_list|()
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
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
literal|"disruptor:foo?concurrentConsumers=10"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|delay
argument_list|(
literal|2000
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor:bar?concurrentConsumers=10"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|delay
argument_list|(
literal|2000
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Bye "
argument_list|)
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

