begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|spi
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
name|ScheduledExecutorService
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
name|ScheduledFuture
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|MeterRegistry
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Tag
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|internal
operator|.
name|TimedExecutorService
import|;
end_import

begin_class
DECL|class|TimedScheduledExecutorService
specifier|public
class|class
name|TimedScheduledExecutorService
extends|extends
name|TimedExecutorService
implements|implements
name|ScheduledExecutorService
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ScheduledExecutorService
name|delegate
decl_stmt|;
DECL|field|registry
specifier|private
specifier|final
name|MeterRegistry
name|registry
decl_stmt|;
DECL|method|TimedScheduledExecutorService (MeterRegistry registry, ScheduledExecutorService delegate, String executorServiceName, Iterable<Tag> tags)
specifier|public
name|TimedScheduledExecutorService
parameter_list|(
name|MeterRegistry
name|registry
parameter_list|,
name|ScheduledExecutorService
name|delegate
parameter_list|,
name|String
name|executorServiceName
parameter_list|,
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
block|{
name|super
argument_list|(
name|registry
argument_list|,
name|delegate
argument_list|,
name|executorServiceName
argument_list|,
name|tags
argument_list|)
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|schedule (Runnable command, long delay, TimeUnit unit)
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|schedule
argument_list|(
name|meter
argument_list|()
operator|.
name|wrap
argument_list|(
name|command
argument_list|)
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|schedule (Callable<V> callable, long delay, TimeUnit unit)
specifier|public
parameter_list|<
name|V
parameter_list|>
name|ScheduledFuture
argument_list|<
name|V
argument_list|>
name|schedule
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|schedule
argument_list|(
name|meter
argument_list|()
operator|.
name|wrap
argument_list|(
name|callable
argument_list|)
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleAtFixedRate (Runnable command, long initialDelay, long period, TimeUnit unit)
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduleAtFixedRate
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|initialDelay
parameter_list|,
name|long
name|period
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|scheduleAtFixedRate
argument_list|(
name|meter
argument_list|()
operator|.
name|wrap
argument_list|(
name|command
argument_list|)
argument_list|,
name|initialDelay
argument_list|,
name|period
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleWithFixedDelay (Runnable command, long initialDelay, long delay, TimeUnit unit)
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduleWithFixedDelay
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|initialDelay
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|meter
argument_list|()
operator|.
name|wrap
argument_list|(
name|command
argument_list|)
argument_list|,
name|initialDelay
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
DECL|method|meter ()
specifier|private
name|Timer
name|meter
parameter_list|()
block|{
return|return
name|registry
operator|.
name|find
argument_list|(
literal|"executor"
argument_list|)
operator|.
name|timer
argument_list|()
return|;
block|}
block|}
end_class

end_unit

