begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
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
name|ThreadFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|InstrumentedExecutorService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|InstrumentedScheduledExecutorService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
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
name|ThreadPoolFactory
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
name|ThreadPoolProfile
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
name|DefaultThreadPoolFactory
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * This implements a {@link ThreadPoolFactory} and generates an Instrumented versions of ExecutorService used to  * monitor performance of each thread using Metrics.  */
end_comment

begin_class
DECL|class|InstrumentedThreadPoolFactory
specifier|public
class|class
name|InstrumentedThreadPoolFactory
implements|implements
name|ThreadPoolFactory
block|{
DECL|field|metricRegistry
specifier|private
name|MetricRegistry
name|metricRegistry
decl_stmt|;
DECL|field|threadPoolFactory
specifier|private
name|ThreadPoolFactory
name|threadPoolFactory
decl_stmt|;
DECL|method|InstrumentedThreadPoolFactory (MetricRegistry metricRegistry)
specifier|public
name|InstrumentedThreadPoolFactory
parameter_list|(
name|MetricRegistry
name|metricRegistry
parameter_list|)
block|{
name|this
argument_list|(
name|metricRegistry
argument_list|,
operator|new
name|DefaultThreadPoolFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|InstrumentedThreadPoolFactory (MetricRegistry metricRegistry, ThreadPoolFactory threadPoolFactory)
specifier|public
name|InstrumentedThreadPoolFactory
parameter_list|(
name|MetricRegistry
name|metricRegistry
parameter_list|,
name|ThreadPoolFactory
name|threadPoolFactory
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|metricRegistry
argument_list|,
literal|"metricRegistry"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|threadPoolFactory
argument_list|,
literal|"threadPoolFactory"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|metricRegistry
operator|=
name|metricRegistry
expr_stmt|;
name|this
operator|.
name|threadPoolFactory
operator|=
name|threadPoolFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newCachedThreadPool (ThreadFactory threadFactory)
specifier|public
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|ThreadFactory
name|threadFactory
parameter_list|)
block|{
return|return
operator|new
name|InstrumentedExecutorService
argument_list|(
name|threadPoolFactory
operator|.
name|newCachedThreadPool
argument_list|(
name|threadFactory
argument_list|)
argument_list|,
name|metricRegistry
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newThreadPool (ThreadPoolProfile profile, ThreadFactory threadFactory)
specifier|public
name|ExecutorService
name|newThreadPool
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
block|{
return|return
operator|new
name|InstrumentedExecutorService
argument_list|(
name|threadPoolFactory
operator|.
name|newThreadPool
argument_list|(
name|profile
argument_list|,
name|threadFactory
argument_list|)
argument_list|,
name|metricRegistry
argument_list|,
name|profile
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newScheduledThreadPool (ThreadPoolProfile profile, ThreadFactory threadFactory)
specifier|public
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
block|{
return|return
operator|new
name|InstrumentedScheduledExecutorService
argument_list|(
name|threadPoolFactory
operator|.
name|newScheduledThreadPool
argument_list|(
name|profile
argument_list|,
name|threadFactory
argument_list|)
argument_list|,
name|metricRegistry
argument_list|,
name|profile
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

