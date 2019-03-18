begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|File
import|;
end_import

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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Random
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
name|AggregationStrategy
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

begin_class
DECL|class|SplitterParallelAggregateTest
specifier|public
class|class
name|SplitterParallelAggregateTest
extends|extends
name|ContextTestSupport
block|{
comment|// run this test manually as it takes some time to process, but shows that parallel aggregate can
comment|// be faster when enabled.
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
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
literal|"direct:splitSynchronizedAggregation"
argument_list|)
operator|.
name|split
argument_list|(
name|method
argument_list|(
operator|new
name|MySplitter
argument_list|()
argument_list|,
literal|"rowIterator"
argument_list|)
argument_list|,
operator|new
name|MyAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:someSplitProcessing?groupSize=500"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:splitUnsynchronizedAggregation"
argument_list|)
operator|.
name|split
argument_list|(
name|method
argument_list|(
operator|new
name|MySplitter
argument_list|()
argument_list|,
literal|"rowIterator"
argument_list|)
argument_list|,
operator|new
name|MyAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|parallelAggregate
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:someSplitProcessing?groupSize=500"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|test1 ()
specifier|public
name|void
name|test1
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
return|return;
block|}
name|int
name|numberOfRequests
init|=
literal|1
decl_stmt|;
name|timeSplitRoutes
argument_list|(
name|numberOfRequests
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test2 ()
specifier|public
name|void
name|test2
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
return|return;
block|}
name|int
name|numberOfRequests
init|=
literal|2
decl_stmt|;
name|timeSplitRoutes
argument_list|(
name|numberOfRequests
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test4 ()
specifier|public
name|void
name|test4
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
return|return;
block|}
name|int
name|numberOfRequests
init|=
literal|4
decl_stmt|;
name|timeSplitRoutes
argument_list|(
name|numberOfRequests
argument_list|)
expr_stmt|;
block|}
DECL|method|timeSplitRoutes (int numberOfRequests)
specifier|protected
name|void
name|timeSplitRoutes
parameter_list|(
name|int
name|numberOfRequests
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|endpoints
init|=
operator|new
name|String
index|[]
block|{
literal|"direct:splitSynchronizedAggregation"
block|,
literal|"direct:splitUnsynchronizedAggregation"
block|}
decl_stmt|;
name|List
argument_list|<
name|Future
argument_list|<
name|File
argument_list|>
argument_list|>
name|futures
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|StopWatch
name|stopWatch
init|=
operator|new
name|StopWatch
argument_list|(
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|endpoint
range|:
name|endpoints
control|)
block|{
name|stopWatch
operator|.
name|restart
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|requestIndex
init|=
literal|0
init|;
name|requestIndex
operator|<
name|numberOfRequests
condition|;
name|requestIndex
operator|++
control|)
block|{
name|futures
operator|.
name|add
argument_list|(
name|template
operator|.
name|asyncRequestBody
argument_list|(
name|endpoint
argument_list|,
literal|null
argument_list|,
name|File
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|futures
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Future
argument_list|<
name|File
argument_list|>
name|future
init|=
name|futures
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|stopWatch
operator|.
name|taken
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"test%d.%s=%d\n"
argument_list|,
name|numberOfRequests
argument_list|,
name|endpoint
argument_list|,
name|stopWatch
operator|.
name|taken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MySplitter
specifier|public
specifier|static
class|class
name|MySplitter
block|{
DECL|method|rowIterator ()
specifier|public
name|Iterator
argument_list|<
name|String
index|[]
argument_list|>
name|rowIterator
parameter_list|()
block|{
comment|// we would normally be reading a large file but for this test,
comment|// we'll just manufacture a bunch of string
comment|// arrays
name|LinkedList
argument_list|<
name|String
index|[]
argument_list|>
name|rows
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
index|[]
name|row
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
literal|10000
condition|;
name|i
operator|++
control|)
block|{
name|row
operator|=
operator|new
name|String
index|[
literal|10
index|]
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|row
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|row
index|[
name|j
index|]
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|rows
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|rows
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
DECL|class|MyAggregationStrategy
specifier|public
specifier|static
class|class
name|MyAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
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
comment|// emulate some processing
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
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
literal|10000
condition|;
name|i
operator|++
control|)
block|{
name|random
operator|.
name|nextGaussian
argument_list|()
expr_stmt|;
block|}
return|return
name|newExchange
return|;
block|}
block|}
block|}
end_class

end_unit

