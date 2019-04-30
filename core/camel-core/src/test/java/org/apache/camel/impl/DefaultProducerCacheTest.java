begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
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
name|AsyncProducer
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
name|impl
operator|.
name|engine
operator|.
name|DefaultProducerCache
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
name|EndpointUtilizationStatistics
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
name|support
operator|.
name|DefaultComponent
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
name|support
operator|.
name|DefaultEndpoint
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
name|support
operator|.
name|DefaultProducer
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
DECL|class|DefaultProducerCacheTest
specifier|public
class|class
name|DefaultProducerCacheTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|stopCounter
specifier|private
specifier|final
name|AtomicInteger
name|stopCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|shutdownCounter
specifier|private
specifier|final
name|AtomicInteger
name|shutdownCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|component
specifier|private
name|MyComponent
name|component
decl_stmt|;
annotation|@
name|Test
DECL|method|testCacheProducerAcquireAndRelease ()
specifier|public
name|void
name|testCacheProducerAcquireAndRelease
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultProducerCache
name|cache
init|=
operator|new
name|DefaultProducerCache
argument_list|(
name|this
argument_list|,
name|context
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that we cache at most 1000 producers to avoid it eating to much memory
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1003
condition|;
name|i
operator|++
control|)
block|{
name|Endpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:queue:"
operator|+
name|i
argument_list|)
decl_stmt|;
name|AsyncProducer
name|p
init|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
comment|// the eviction is async so force cleanup
name|cache
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 1000"
argument_list|,
literal|1000
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCacheStopExpired ()
specifier|public
name|void
name|testCacheStopExpired
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultProducerCache
name|cache
init|=
operator|new
name|DefaultProducerCache
argument_list|(
name|this
argument_list|,
name|context
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|cache
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
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|Endpoint
name|e
init|=
name|newEndpoint
argument_list|(
literal|true
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|e
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|AsyncProducer
name|p
init|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
comment|// the eviction is async so force cleanup
name|cache
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 5"
argument_list|,
literal|5
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// the eviction listener is async so sleep a bit
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// should have stopped the 3 evicted
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|stopCounter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should have stopped all 8
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|stopCounter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtendedStatistics ()
specifier|public
name|void
name|testExtendedStatistics
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultProducerCache
name|cache
init|=
operator|new
name|DefaultProducerCache
argument_list|(
name|this
argument_list|,
name|context
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|cache
operator|.
name|setExtendedStatistics
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// use 1 = 2 times
comment|// use 2 = 3 times
comment|// use 3..4 = 1 times
comment|// use 5 = 0 times
name|Endpoint
name|e
init|=
name|newEndpoint
argument_list|(
literal|true
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|AsyncProducer
name|p
init|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|e
operator|=
name|newEndpoint
argument_list|(
literal|true
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|p
operator|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|e
operator|=
name|newEndpoint
argument_list|(
literal|true
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|p
operator|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|e
operator|=
name|newEndpoint
argument_list|(
literal|true
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|p
operator|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|e
operator|=
name|newEndpoint
argument_list|(
literal|true
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|p
operator|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|e
operator|=
name|newEndpoint
argument_list|(
literal|true
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|p
operator|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|e
operator|=
name|newEndpoint
argument_list|(
literal|true
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|p
operator|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 4"
argument_list|,
literal|4
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EndpointUtilizationStatistics
name|stats
init|=
name|cache
operator|.
name|getEndpointUtilizationStatistics
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|stats
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|recent
init|=
name|stats
operator|.
name|getStatistics
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|recent
operator|.
name|get
argument_list|(
literal|"my://1"
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|recent
operator|.
name|get
argument_list|(
literal|"my://2"
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|recent
operator|.
name|get
argument_list|(
literal|"my://3"
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|recent
operator|.
name|get
argument_list|(
literal|"my://4"
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|recent
operator|.
name|get
argument_list|(
literal|"my://5"
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|component
operator|=
operator|new
name|MyComponent
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|newEndpoint (boolean isSingleton, int number)
specifier|protected
name|MyEndpoint
name|newEndpoint
parameter_list|(
name|boolean
name|isSingleton
parameter_list|,
name|int
name|number
parameter_list|)
block|{
return|return
operator|new
name|MyEndpoint
argument_list|(
name|component
argument_list|,
name|isSingleton
argument_list|,
name|number
argument_list|)
return|;
block|}
DECL|class|MyComponent
specifier|private
specifier|final
class|class
name|MyComponent
extends|extends
name|DefaultComponent
block|{
DECL|method|MyComponent (CamelContext context)
specifier|public
name|MyComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
DECL|class|MyEndpoint
specifier|private
specifier|final
class|class
name|MyEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|isSingleton
specifier|private
specifier|final
name|boolean
name|isSingleton
decl_stmt|;
DECL|field|number
specifier|private
specifier|final
name|int
name|number
decl_stmt|;
DECL|method|MyEndpoint (MyComponent component, boolean isSingleton, int number)
specifier|private
name|MyEndpoint
parameter_list|(
name|MyComponent
name|component
parameter_list|,
name|boolean
name|isSingleton
parameter_list|,
name|int
name|number
parameter_list|)
block|{
name|super
argument_list|(
literal|"my://"
operator|+
name|number
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|isSingleton
operator|=
name|isSingleton
expr_stmt|;
name|this
operator|.
name|number
operator|=
name|number
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|MyProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
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
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|isSingleton
return|;
block|}
block|}
DECL|class|MyProducer
specifier|private
specifier|final
class|class
name|MyProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|MyProducer (Endpoint endpoint)
name|MyProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|stopCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|shutdownCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

