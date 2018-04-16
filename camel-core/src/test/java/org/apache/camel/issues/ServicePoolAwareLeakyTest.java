begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|AtomicLong
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
name|Route
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
name|Service
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ServiceStatus
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
name|impl
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
name|impl
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
name|impl
operator|.
name|DefaultProducer
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
name|LifecycleStrategySupport
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
name|ServiceSupport
import|;
end_import

begin_class
DECL|class|ServicePoolAwareLeakyTest
specifier|public
class|class
name|ServicePoolAwareLeakyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LEAKY_SIEVE_STABLE
specifier|private
specifier|static
specifier|final
name|String
name|LEAKY_SIEVE_STABLE
init|=
literal|"leaky://sieve-stable"
decl_stmt|;
DECL|field|LEAKY_SIEVE_TRANSIENT
specifier|private
specifier|static
specifier|final
name|String
name|LEAKY_SIEVE_TRANSIENT
init|=
literal|"leaky://sieve-transient"
decl_stmt|;
comment|/**      * Component that provides leaks producers.      */
DECL|class|LeakySieveComponent
specifier|private
specifier|static
class|class
name|LeakySieveComponent
extends|extends
name|DefaultComponent
block|{
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
return|return
operator|new
name|LeakySieveEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
block|}
comment|/**      * Endpoint that provides leaky producers.      */
DECL|class|LeakySieveEndpoint
specifier|private
specifier|static
class|class
name|LeakySieveEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|method|LeakySieveEndpoint (String uri)
name|LeakySieveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
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
name|LeakySieveProducer
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
block|}
comment|/**      * Leaky producer - implements {@link ServicePoolAware}.      */
DECL|class|LeakySieveProducer
specifier|private
specifier|static
class|class
name|LeakySieveProducer
extends|extends
name|DefaultProducer
implements|implements
name|ServicePoolAware
block|{
DECL|method|LeakySieveProducer (Endpoint endpoint)
name|LeakySieveProducer
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
comment|// do nothing
block|}
block|}
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
comment|// only occurs when using JMX as the GC root for the producer is through a ManagedProducer created by the
comment|// context.addService() invocation
return|return
literal|true
return|;
block|}
comment|/**      * Returns true if verification of state should be performed during the test as opposed to at the end.      */
DECL|method|isFailFast ()
specifier|public
name|boolean
name|isFailFast
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Returns true if during fast failure we should verify that the service pool remains in the started state.      */
DECL|method|isVerifyProducerServicePoolRemainsStarted ()
specifier|public
name|boolean
name|isVerifyProducerServicePoolRemainsStarted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|testForMemoryLeak ()
specifier|public
name|void
name|testForMemoryLeak
parameter_list|()
throws|throws
name|Exception
block|{
name|registerLeakyComponent
argument_list|()
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|AtomicLong
argument_list|>
name|references
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// track LeakySieveProducer lifecycle
name|context
operator|.
name|addLifecycleStrategy
argument_list|(
operator|new
name|LifecycleStrategySupport
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onServiceAdd
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|LeakySieveProducer
condition|)
block|{
name|String
name|key
init|=
operator|(
operator|(
name|LeakySieveProducer
operator|)
name|service
operator|)
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
name|AtomicLong
name|num
init|=
name|references
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|num
operator|==
literal|null
condition|)
block|{
name|num
operator|=
operator|new
name|AtomicLong
argument_list|()
expr_stmt|;
name|references
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|num
argument_list|)
expr_stmt|;
block|}
name|num
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onServiceRemove
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|LeakySieveProducer
condition|)
block|{
name|String
name|key
init|=
operator|(
operator|(
name|LeakySieveProducer
operator|)
name|service
operator|)
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
name|AtomicLong
name|num
init|=
name|references
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|num
operator|==
literal|null
condition|)
block|{
name|num
operator|=
operator|new
name|AtomicLong
argument_list|()
expr_stmt|;
name|references
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|num
argument_list|)
expr_stmt|;
block|}
name|num
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
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
literal|"direct:sieve-transient"
argument_list|)
operator|.
name|id
argument_list|(
literal|"sieve-transient"
argument_list|)
operator|.
name|to
argument_list|(
name|LEAKY_SIEVE_TRANSIENT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:sieve-stable"
argument_list|)
operator|.
name|id
argument_list|(
literal|"sieve-stable"
argument_list|)
operator|.
name|to
argument_list|(
name|LEAKY_SIEVE_STABLE
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
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|ServiceSupport
name|service
init|=
operator|(
name|ServiceSupport
operator|)
name|context
operator|.
name|getProducerServicePool
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|service
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFailFast
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getProducerServicePool
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
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_TRANSIENT
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_STABLE
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|stopRoute
argument_list|(
literal|"sieve-transient"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFailFast
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Expected no service references to remain"
argument_list|,
literal|0
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_TRANSIENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isFailFast
argument_list|()
condition|)
block|{
comment|// looks like we cleared more than just our route, we've stopped and cleared the global ProducerServicePool
comment|// since SendProcessor.stop() invokes ServiceHelper.stopServices(producerCache, producer); which in turn invokes
comment|// ServiceHelper.stopAndShutdownService(pool);.
comment|//
comment|// Whilst stop on the SharedProducerServicePool is a NOOP shutdown is not and effects a stop of the pool.
if|if
condition|(
name|isVerifyProducerServicePoolRemainsStarted
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|service
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Expected one stable producer to remain pooled"
argument_list|,
literal|1
argument_list|,
name|context
operator|.
name|getProducerServicePool
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected one stable producer to remain as service"
argument_list|,
literal|1
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_STABLE
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Send a body to verify behaviour of send producer after another route has been stopped
name|sendBody
argument_list|(
literal|"direct:sieve-stable"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFailFast
argument_list|()
condition|)
block|{
comment|// shared pool is used despite being 'Stopped'
if|if
condition|(
name|isVerifyProducerServicePoolRemainsStarted
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|service
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Expected only stable producer in pool"
argument_list|,
literal|1
argument_list|,
name|context
operator|.
name|getProducerServicePool
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected no references to transient producer"
argument_list|,
literal|0
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_TRANSIENT
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected reference to stable producer"
argument_list|,
literal|1
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_STABLE
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|startRoute
argument_list|(
literal|"sieve-transient"
argument_list|)
expr_stmt|;
comment|// ok, back to normal
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|service
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFailFast
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Expected both producers in pool"
argument_list|,
literal|2
argument_list|,
name|context
operator|.
name|getProducerServicePool
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected one transient producer as service"
argument_list|,
literal|1
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_TRANSIENT
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected one stable producer as service"
argument_list|,
literal|1
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_STABLE
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|isFailFast
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Expected both producers in pool"
argument_list|,
literal|2
argument_list|,
name|context
operator|.
name|getProducerServicePool
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// if not fixed these will equal the number of iterations in the loop + 1
name|assertEquals
argument_list|(
literal|"Expected one transient producer as service"
argument_list|,
literal|1
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_TRANSIENT
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected one stable producer as service"
argument_list|,
literal|1
argument_list|,
name|references
operator|.
name|get
argument_list|(
name|LEAKY_SIEVE_STABLE
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|registerLeakyComponent ()
specifier|private
name|void
name|registerLeakyComponent
parameter_list|()
block|{
comment|// register leaky component
name|context
operator|.
name|addComponent
argument_list|(
literal|"leaky"
argument_list|,
operator|new
name|LeakySieveComponent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

