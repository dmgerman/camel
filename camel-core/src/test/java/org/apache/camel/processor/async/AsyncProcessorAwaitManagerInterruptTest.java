begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.async
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|async
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|RejectedExecutionException
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
name|CamelExecutionException
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
name|spi
operator|.
name|AsyncProcessorAwaitManager
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|AsyncProcessorAwaitManagerInterruptTest
specifier|public
class|class
name|AsyncProcessorAwaitManagerInterruptTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testAsyncAwaitInterrupt ()
specifier|public
name|void
name|testAsyncAwaitInterrupt
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|setStatisticsEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|RejectedExecutionException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|RejectedExecutionException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Interrupted while waiting for asynchronous callback"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getThreadsBlocked
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getThreadsInterrupted
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
name|context
operator|.
name|addComponent
argument_list|(
literal|"async"
argument_list|,
operator|new
name|MyAsyncComponent
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|to
argument_list|(
literal|"async:bye:camel?delay=1000"
argument_list|)
operator|.
name|id
argument_list|(
literal|"myAsync"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|int
name|size
init|=
name|context
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"async inflight: {}"
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
argument_list|>
name|threads
init|=
name|context
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
operator|.
name|browse
argument_list|()
decl_stmt|;
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
name|thread
init|=
name|threads
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// lets interrupt it
name|String
name|id
init|=
name|thread
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|context
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
operator|.
name|interrupt
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hi Camel"
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

