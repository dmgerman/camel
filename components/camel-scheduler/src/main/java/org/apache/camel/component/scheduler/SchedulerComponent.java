begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.scheduler
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|scheduler
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
name|spi
operator|.
name|Metadata
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

begin_class
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|annotations
operator|.
name|Component
argument_list|(
literal|"scheduler"
argument_list|)
DECL|class|SchedulerComponent
specifier|public
class|class
name|SchedulerComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|executors
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ScheduledExecutorService
argument_list|>
name|executors
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|refCounts
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
name|refCounts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|label
operator|=
literal|"scheduler"
argument_list|)
DECL|field|concurrentTasks
specifier|private
name|int
name|concurrentTasks
init|=
literal|1
decl_stmt|;
DECL|method|SchedulerComponent ()
specifier|public
name|SchedulerComponent
parameter_list|()
block|{     }
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
name|SchedulerEndpoint
name|answer
init|=
operator|new
name|SchedulerEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setConcurrentTasks
argument_list|(
name|getConcurrentTasks
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getConcurrentTasks ()
specifier|public
name|int
name|getConcurrentTasks
parameter_list|()
block|{
return|return
name|concurrentTasks
return|;
block|}
comment|/**      * Number of threads used by the scheduling thread pool.      *<p/>      * Is by default using a single thread      */
DECL|method|setConcurrentTasks (int concurrentTasks)
specifier|public
name|void
name|setConcurrentTasks
parameter_list|(
name|int
name|concurrentTasks
parameter_list|)
block|{
name|this
operator|.
name|concurrentTasks
operator|=
name|concurrentTasks
expr_stmt|;
block|}
DECL|method|addConsumer (SchedulerConsumer consumer)
specifier|protected
name|ScheduledExecutorService
name|addConsumer
parameter_list|(
name|SchedulerConsumer
name|consumer
parameter_list|)
block|{
name|String
name|name
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|concurrentTasks
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getConcurrentTasks
argument_list|()
decl_stmt|;
name|ScheduledExecutorService
name|answer
decl_stmt|;
synchronized|synchronized
init|(
name|executors
init|)
block|{
name|answer
operator|=
name|executors
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|this
argument_list|,
literal|"scheduler://"
operator|+
name|name
argument_list|,
name|concurrentTasks
argument_list|)
expr_stmt|;
name|executors
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|answer
argument_list|)
expr_stmt|;
comment|// store new reference counter
name|refCounts
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// increase reference counter
name|AtomicInteger
name|counter
init|=
name|refCounts
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|removeConsumer (SchedulerConsumer consumer)
specifier|protected
name|void
name|removeConsumer
parameter_list|(
name|SchedulerConsumer
name|consumer
parameter_list|)
block|{
name|String
name|name
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|executors
init|)
block|{
comment|// decrease reference counter
name|AtomicInteger
name|counter
init|=
name|refCounts
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
operator|&&
name|counter
operator|.
name|decrementAndGet
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|refCounts
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
comment|// remove scheduler as its no longer in use
name|ScheduledExecutorService
name|scheduler
init|=
name|executors
operator|.
name|remove
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|scheduler
operator|!=
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|scheduler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|Collection
argument_list|<
name|ScheduledExecutorService
argument_list|>
name|collection
init|=
name|executors
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|ScheduledExecutorService
name|scheduler
range|:
name|collection
control|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|scheduler
argument_list|)
expr_stmt|;
block|}
name|executors
operator|.
name|clear
argument_list|()
expr_stmt|;
name|refCounts
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

