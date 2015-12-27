begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|ScheduledPollEndpoint
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
name|spi
operator|.
name|UriEndpoint
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
name|UriParam
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The scheduler component is used for generating message exchanges when a scheduler fires.  *  * This component is similar to the timer component, but it offers more functionality in terms of scheduling.  * Also this component uses JDK ScheduledExecutorService. Where as the timer uses a JDK Timer.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"scheduler"
argument_list|,
name|title
operator|=
literal|"Scheduler"
argument_list|,
name|syntax
operator|=
literal|"scheduler:name"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|consumerClass
operator|=
name|SchedulerConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"core,scheduling"
argument_list|)
DECL|class|SchedulerEndpoint
specifier|public
class|class
name|SchedulerEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
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
DECL|method|SchedulerEndpoint (String uri, SchedulerComponent component, String remaining)
specifier|public
name|SchedulerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SchedulerComponent
name|component
parameter_list|,
name|String
name|remaining
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|remaining
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|SchedulerComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|SchedulerComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Scheduler cannot be used as a producer"
argument_list|)
throw|;
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
name|SchedulerConsumer
name|consumer
init|=
operator|new
name|SchedulerConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
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
literal|true
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * The name of the scheduler      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
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
DECL|method|onConsumerStart (SchedulerConsumer consumer)
specifier|public
name|void
name|onConsumerStart
parameter_list|(
name|SchedulerConsumer
name|consumer
parameter_list|)
block|{
comment|// if using default scheduler then obtain thread pool from component which manages their lifecycle
if|if
condition|(
name|consumer
operator|.
name|getScheduler
argument_list|()
operator|==
literal|null
operator|&&
name|consumer
operator|.
name|getScheduledExecutorService
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ScheduledExecutorService
name|scheduler
init|=
name|getComponent
argument_list|()
operator|.
name|addConsumer
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setScheduledExecutorService
argument_list|(
name|scheduler
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onConsumerStop (SchedulerConsumer consumer)
specifier|public
name|void
name|onConsumerStop
parameter_list|(
name|SchedulerConsumer
name|consumer
parameter_list|)
block|{
name|getComponent
argument_list|()
operator|.
name|removeConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

