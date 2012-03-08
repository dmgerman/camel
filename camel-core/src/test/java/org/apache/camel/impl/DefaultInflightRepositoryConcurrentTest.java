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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|CamelContext
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
name|Consumer
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
name|spi
operator|.
name|InflightRepository
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|DefaultInflightRepositoryConcurrentTest
specifier|public
class|class
name|DefaultInflightRepositoryConcurrentTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|THREAD_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|THREAD_COUNT
init|=
literal|20
decl_stmt|;
DECL|field|TOTAL_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|int
name|TOTAL_ENDPOINTS
init|=
literal|10000
decl_stmt|;
DECL|field|LOOP_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|LOOP_COUNT
init|=
literal|100000
decl_stmt|;
comment|// the failed flag should be marked as volatile so that we have got guaranteed visibility inside the main thread
DECL|field|failed
specifier|private
specifier|static
specifier|volatile
name|boolean
name|failed
decl_stmt|;
DECL|field|context
specifier|private
specifier|static
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|method|testThreaded ()
specifier|public
name|void
name|testThreaded
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultInflightRepository
name|toTest
init|=
operator|new
name|DefaultInflightRepository
argument_list|()
decl_stmt|;
name|Endpoint
index|[]
name|endpoints
init|=
operator|new
name|Endpoint
index|[
name|TOTAL_ENDPOINTS
index|]
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
name|endpoints
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// create TOTAL_ENDPOINTS endpoints
name|Endpoint
name|endpoint
init|=
operator|new
name|DefaultEndpoint
argument_list|()
block|{
specifier|final
name|String
name|uri
init|=
literal|"def:"
operator|+
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|endpoints
index|[
name|i
index|]
operator|=
name|endpoint
expr_stmt|;
block|}
name|AtomicInteger
name|locker
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|Thread
index|[]
name|threads
init|=
operator|new
name|Thread
index|[
name|THREAD_COUNT
index|]
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
name|threads
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|=
operator|new
name|Thread
argument_list|(
operator|new
name|TypicalConsumer
argument_list|(
name|endpoints
argument_list|,
name|toTest
argument_list|,
name|locker
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
name|threads
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
while|while
condition|(
name|locker
operator|.
name|get
argument_list|()
operator|>
literal|0
condition|)
block|{
synchronized|synchronized
init|(
name|locker
init|)
block|{
name|locker
operator|.
name|wait
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|failed
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Failed to properly track endpoints"
argument_list|)
throw|;
block|}
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Size MUST be 0"
argument_list|,
literal|0
argument_list|,
name|toTest
operator|.
name|size
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|toTest
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Test either incomplete or tracking failed"
argument_list|)
throw|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Must not have any references left"
argument_list|,
literal|0
argument_list|,
name|toTest
operator|.
name|endpointSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|TypicalConsumer
specifier|private
specifier|static
class|class
name|TypicalConsumer
implements|implements
name|Runnable
block|{
DECL|field|endpoints
name|Endpoint
index|[]
name|endpoints
decl_stmt|;
DECL|field|repo
name|InflightRepository
name|repo
decl_stmt|;
DECL|field|locker
name|AtomicInteger
name|locker
decl_stmt|;
DECL|field|rand
name|Random
name|rand
init|=
operator|new
name|Random
argument_list|(
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|TypicalConsumer (Endpoint[] endpoints, InflightRepository repo, AtomicInteger locker)
name|TypicalConsumer
parameter_list|(
name|Endpoint
index|[]
name|endpoints
parameter_list|,
name|InflightRepository
name|repo
parameter_list|,
name|AtomicInteger
name|locker
parameter_list|)
block|{
name|this
operator|.
name|endpoints
operator|=
name|endpoints
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
name|this
operator|.
name|locker
operator|=
name|locker
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
synchronized|synchronized
init|(
name|locker
init|)
block|{
name|locker
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
try|try
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
name|LOOP_COUNT
condition|;
name|i
operator|++
control|)
block|{
name|Endpoint
name|endpoint
init|=
name|endpoints
index|[
name|Math
operator|.
name|abs
argument_list|(
name|rand
operator|.
name|nextInt
argument_list|()
operator|%
name|endpoints
operator|.
name|length
argument_list|)
index|]
decl_stmt|;
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|int
name|size
init|=
name|repo
operator|.
name|size
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|<=
literal|0
condition|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
block|}
name|repo
operator|.
name|remove
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// just to make it sure do catch any possible Throwable
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|locker
init|)
block|{
name|locker
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
name|locker
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

