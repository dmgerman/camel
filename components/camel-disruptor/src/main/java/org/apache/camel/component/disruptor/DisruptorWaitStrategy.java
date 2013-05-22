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
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|BlockingWaitStrategy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|BusySpinWaitStrategy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|EventProcessor
import|;
end_import

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|SleepingWaitStrategy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|WaitStrategy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|YieldingWaitStrategy
import|;
end_import

begin_comment
comment|/**  * This enumeration holds all values that may be used as the {@link WaitStrategy} used by producers on a Disruptor.  * Blocking is the default {@link WaitStrategy}.  */
end_comment

begin_enum
DECL|enum|DisruptorWaitStrategy
specifier|public
enum|enum
name|DisruptorWaitStrategy
block|{
comment|/**      * Blocking strategy that uses a lock and condition variable for {@link EventProcessor}s waiting on a barrier.      *<p/>      * This strategy can be used when throughput and low-latency are not as important as CPU resource.      */
DECL|enumConstant|Blocking
name|Blocking
parameter_list|(
name|BlockingWaitStrategy
operator|.
name|class
parameter_list|)
operator|,
comment|/**      * Sleeping strategy that initially spins, then uses a Thread.yield(), and eventually for the minimum number of nanos      * the OS and JVM will allow while the {@link com.lmax.disruptor.EventProcessor}s are waiting on a barrier.      *<p/>      * This strategy is a good compromise between performance and CPU resource. Latency spikes can occur after quiet periods.      */
DECL|enumConstant|Sleeping
constructor|Sleeping(SleepingWaitStrategy.class
block|)
enum|,
comment|/**      * Busy Spin strategy that uses a busy spin loop for {@link com.lmax.disruptor.EventProcessor}s waiting on a barrier.      *<p/>      * This strategy will use CPU resource to avoid syscalls which can introduce latency jitter.  It is best      * used when threads can be bound to specific CPU cores.      */
DECL|enumConstant|BusySpin
name|BusySpin
argument_list|(
name|BusySpinWaitStrategy
operator|.
name|class
argument_list|)
operator|,
comment|/**      * Yielding strategy that uses a Thread.yield() for {@link com.lmax.disruptor.EventProcessor}s waiting on a barrier      * after an initially spinning.      *<p/>      * This strategy is a good compromise between performance and CPU resource without incurring significant latency spikes.      */
DECL|enumConstant|Yielding
name|Yielding
argument_list|(
name|YieldingWaitStrategy
operator|.
name|class
argument_list|)
enum|;
end_enum

begin_comment
comment|//    TODO PhasedBackoffWaitStrategy constructor requires parameters, unlike the other strategies. We leave it out for now
end_comment

begin_comment
comment|//    /**
end_comment

begin_comment
comment|//     * Phased wait strategy for waiting {@link EventProcessor}s on a barrier.<p/>
end_comment

begin_comment
comment|//     *
end_comment

begin_comment
comment|//     * This strategy can be used when throughput and low-latency are not as important as CPU resource.<\p></\p>
end_comment

begin_comment
comment|//     *
end_comment

begin_comment
comment|//     * Spins, then yields, then blocks on the configured BlockingStrategy.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    PHASED_BACKOFF(PhasedBackoffWaitStrategy.class),
end_comment

begin_comment
comment|//    TODO TimeoutBlockingWaitStrategy constructor requires parameters, unlike the other strategies. We leave it out for now
end_comment

begin_comment
comment|//    /**
end_comment

begin_comment
comment|//     * TODO, wait for documentation from LMAX
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    TIMEOUT_BLOCKING(TimeoutBlockingWaitStrategy.class);
end_comment

begin_decl_stmt
DECL|field|waitStrategyClass
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|WaitStrategy
argument_list|>
name|waitStrategyClass
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|DisruptorWaitStrategy (final Class<? extends WaitStrategy> waitStrategyClass)
specifier|private
name|DisruptorWaitStrategy
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|WaitStrategy
argument_list|>
name|waitStrategyClass
parameter_list|)
block|{
name|this
operator|.
name|waitStrategyClass
operator|=
name|waitStrategyClass
expr_stmt|;
block|}
end_constructor

begin_function
DECL|method|createWaitStrategyInstance ()
specifier|public
name|WaitStrategy
name|createWaitStrategyInstance
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|waitStrategyClass
operator|.
name|getConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
return|;
block|}
end_function

unit|}
end_unit

