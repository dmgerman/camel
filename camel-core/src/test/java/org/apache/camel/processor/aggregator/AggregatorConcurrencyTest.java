begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregator
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
name|atomic
operator|.
name|AtomicInteger
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|AggregatorConcurrencyTest
specifier|public
class|class
name|AggregatorConcurrencyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AggregatorConcurrencyTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|COUNTER
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|COUNTER
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|SUM
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|SUM
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
name|int
name|size
init|=
literal|100
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
init|=
literal|"direct:start"
decl_stmt|;
DECL|method|testAggregateConcurrency ()
specifier|public
name|void
name|testAggregateConcurrency
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|total
init|=
literal|0
decl_stmt|;
name|ExecutorService
name|service
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|20
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|>
name|tasks
init|=
operator|new
name|ArrayList
argument_list|<
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
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
name|size
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|count
init|=
name|i
decl_stmt|;
name|total
operator|+=
name|i
expr_stmt|;
name|tasks
operator|.
name|add
argument_list|(
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Hello World"
argument_list|,
literal|"index"
argument_list|,
name|count
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|total
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"total"
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|,
name|size
argument_list|)
expr_stmt|;
comment|// submit all tasks
name|service
operator|.
name|invokeAll
argument_list|(
name|tasks
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|COUNTER
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Need to shutdown the threadpool
name|service
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|uri
argument_list|)
operator|.
name|aggregate
argument_list|(
name|constant
argument_list|(
literal|true
argument_list|)
argument_list|,
operator|new
name|AggregationStrategy
argument_list|()
block|{
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
name|Exchange
name|answer
init|=
name|oldExchange
operator|!=
literal|null
condition|?
name|oldExchange
else|:
name|newExchange
decl_stmt|;
name|COUNTER
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
name|Integer
name|newIndex
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"index"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|total
init|=
name|SUM
operator|.
name|addAndGet
argument_list|(
name|newIndex
argument_list|)
decl_stmt|;
name|answer
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"total"
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Index: "
operator|+
name|newIndex
operator|+
literal|". Total so far: "
operator|+
name|total
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|60000
argument_list|)
operator|.
name|completionPredicate
argument_list|(
name|property
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|100
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|header
argument_list|(
literal|"total"
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

