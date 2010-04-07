begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Endpoint
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
name|Producer
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
name|ServicePoolAware
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ServicePoolTest
specifier|public
class|class
name|ServicePoolTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|cleanup
specifier|private
specifier|static
name|boolean
name|cleanup
decl_stmt|;
DECL|field|pool
specifier|private
name|DefaultProducerServicePool
name|pool
decl_stmt|;
DECL|class|MyProducer
specifier|private
class|class
name|MyProducer
extends|extends
name|DefaultProducer
implements|implements
name|ServicePoolAware
block|{
DECL|field|start
specifier|private
name|boolean
name|start
decl_stmt|;
DECL|field|stop
specifier|private
name|boolean
name|stop
decl_stmt|;
DECL|method|MyProducer (Endpoint endpoint)
specifier|public
name|MyProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
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
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not be started twice"
argument_list|,
literal|false
argument_list|,
name|start
argument_list|)
expr_stmt|;
name|start
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not be stopped twice"
argument_list|,
literal|false
argument_list|,
name|stop
argument_list|)
expr_stmt|;
name|stop
operator|=
literal|true
expr_stmt|;
name|cleanup
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
name|pool
operator|=
operator|new
name|DefaultProducerServicePool
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|pool
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|pool
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have stopped the produers"
argument_list|,
literal|true
argument_list|,
name|cleanup
argument_list|)
expr_stmt|;
block|}
DECL|method|testSingleEntry ()
specifier|public
name|void
name|testSingleEntry
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|producer
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|=
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTwoEntries ()
specifier|public
name|void
name|testTwoEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|Producer
name|producer1
init|=
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Producer
name|producer2
init|=
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|producer1
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|producer1
argument_list|)
expr_stmt|;
name|producer2
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|producer2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testThreeEntries ()
specifier|public
name|void
name|testThreeEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|Producer
name|producer1
init|=
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Producer
name|producer2
init|=
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Producer
name|producer3
init|=
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|producer1
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|producer1
argument_list|)
expr_stmt|;
name|producer2
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|producer2
argument_list|)
expr_stmt|;
name|producer3
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|producer3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAcquireAddRelease ()
specifier|public
name|void
name|testAcquireAddRelease
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|Producer
name|producer
init|=
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
name|producer
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAcquireAdd ()
specifier|public
name|void
name|testAcquireAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Producer
argument_list|>
name|producers
init|=
operator|new
name|ArrayList
argument_list|<
name|Producer
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|Producer
name|producer
init|=
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
name|producer
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|producers
operator|.
name|add
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
comment|// release afterwards
for|for
control|(
name|Producer
name|producer
range|:
name|producers
control|)
block|{
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAcquireAddQueueFull ()
specifier|public
name|void
name|testAcquireAddQueueFull
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|Producer
name|producer
init|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
argument_list|)
decl_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
comment|// when adding a 6 we get a queue full
try|try
block|{
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Queue full"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConcurrent ()
specifier|public
name|void
name|testConcurrent
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Future
argument_list|>
name|response
init|=
operator|new
name|ArrayList
argument_list|<
name|Future
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|index
init|=
name|i
decl_stmt|;
name|Future
name|out
init|=
name|executor
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
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|Producer
name|producer
init|=
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
name|producer
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
return|return
name|index
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|response
operator|.
name|add
argument_list|(
name|out
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|i
argument_list|,
name|response
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testConcurrentStress ()
specifier|public
name|void
name|testConcurrentStress
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Future
argument_list|>
name|response
init|=
operator|new
name|ArrayList
argument_list|<
name|Future
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|index
init|=
name|i
decl_stmt|;
name|Future
name|out
init|=
name|executor
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
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
literal|100
condition|;
name|j
operator|++
control|)
block|{
name|Producer
name|producer
init|=
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
name|producer
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
return|return
name|index
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|response
operator|.
name|add
argument_list|(
name|out
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|i
argument_list|,
name|response
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

