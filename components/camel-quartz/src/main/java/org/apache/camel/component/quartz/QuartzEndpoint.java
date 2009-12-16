begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|processor
operator|.
name|loadbalancer
operator|.
name|LoadBalancer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|RoundRobinLoadBalancer
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
name|ExchangeHelper
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDetail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobExecutionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Scheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SchedulerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Trigger
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/quartz.html">Quartz Endpoint</a>  *   * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|QuartzEndpoint
specifier|public
class|class
name|QuartzEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|QuartzEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|scheduler
specifier|private
name|Scheduler
name|scheduler
decl_stmt|;
DECL|field|loadBalancer
specifier|private
name|LoadBalancer
name|loadBalancer
decl_stmt|;
DECL|field|trigger
specifier|private
name|Trigger
name|trigger
decl_stmt|;
DECL|field|jobDetail
specifier|private
name|JobDetail
name|jobDetail
decl_stmt|;
DECL|field|started
specifier|private
name|boolean
name|started
decl_stmt|;
DECL|field|stateful
specifier|private
name|boolean
name|stateful
decl_stmt|;
DECL|method|QuartzEndpoint ()
specifier|public
name|QuartzEndpoint
parameter_list|()
block|{     }
DECL|method|QuartzEndpoint (final String endpointUri, final QuartzComponent component, final Scheduler scheduler)
specifier|public
name|QuartzEndpoint
parameter_list|(
specifier|final
name|String
name|endpointUri
parameter_list|,
specifier|final
name|QuartzComponent
name|component
parameter_list|,
specifier|final
name|Scheduler
name|scheduler
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
DECL|method|QuartzEndpoint (final String endpointUri, final Scheduler scheduler)
specifier|public
name|QuartzEndpoint
parameter_list|(
specifier|final
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Scheduler
name|scheduler
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
DECL|method|addTrigger (final Trigger trigger, final JobDetail detail)
specifier|public
name|void
name|addTrigger
parameter_list|(
specifier|final
name|Trigger
name|trigger
parameter_list|,
specifier|final
name|JobDetail
name|detail
parameter_list|)
throws|throws
name|SchedulerException
block|{
comment|// lets default the trigger name to the job name
if|if
condition|(
name|trigger
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|trigger
operator|.
name|setName
argument_list|(
name|detail
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// lets default the trigger group to the job group
if|if
condition|(
name|trigger
operator|.
name|getGroup
argument_list|()
operator|==
literal|null
condition|)
block|{
name|trigger
operator|.
name|setGroup
argument_list|(
name|detail
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// default start time to now if not specified
if|if
condition|(
name|trigger
operator|.
name|getStartTime
argument_list|()
operator|==
literal|null
condition|)
block|{
name|trigger
operator|.
name|setStartTime
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|detail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_ENDPOINT
argument_list|,
name|isStateful
argument_list|()
condition|?
name|getEndpointUri
argument_list|()
else|:
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|detail
operator|.
name|getJobClass
argument_list|()
operator|==
literal|null
condition|)
block|{
name|detail
operator|.
name|setJobClass
argument_list|(
name|isStateful
argument_list|()
condition|?
name|StatefulCamelJob
operator|.
name|class
else|:
name|CamelJob
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|detail
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|detail
operator|.
name|setName
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getScheduler
argument_list|()
operator|.
name|scheduleJob
argument_list|(
name|detail
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
DECL|method|removeTrigger (final Trigger trigger, final JobDetail jobDetail)
specifier|public
name|void
name|removeTrigger
parameter_list|(
specifier|final
name|Trigger
name|trigger
parameter_list|,
specifier|final
name|JobDetail
name|jobDetail
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|getScheduler
argument_list|()
operator|.
name|unscheduleJob
argument_list|(
name|trigger
operator|.
name|getName
argument_list|()
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method is invoked when a Quartz job is fired.      *       * @param jobExecutionContext the Quartz Job context      */
DECL|method|onJobExecute (final JobExecutionContext jobExecutionContext)
specifier|public
name|void
name|onJobExecute
parameter_list|(
specifier|final
name|JobExecutionContext
name|jobExecutionContext
parameter_list|)
throws|throws
name|JobExecutionException
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Firing Quartz Job with context: "
operator|+
name|jobExecutionContext
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|jobExecutionContext
argument_list|)
decl_stmt|;
try|try
block|{
name|getLoadBalancer
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// propagate the exception back to Quartz
throw|throw
operator|new
name|JobExecutionException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// log the error
name|LOG
operator|.
name|error
argument_list|(
name|ExchangeHelper
operator|.
name|createExceptionMessage
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
comment|// and rethrow to let quartz handle it
if|if
condition|(
name|e
operator|instanceof
name|JobExecutionException
condition|)
block|{
throw|throw
operator|(
name|JobExecutionException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|JobExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createExchange (final JobExecutionContext jobExecutionContext)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
specifier|final
name|JobExecutionContext
name|jobExecutionContext
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|QuartzMessage
argument_list|(
name|exchange
argument_list|,
name|jobExecutionContext
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
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
literal|"You cannot send messages to this endpoint"
argument_list|)
throw|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|QuartzConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|QuartzConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
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
literal|"quartz://"
operator|+
name|getTrigger
argument_list|()
operator|.
name|getGroup
argument_list|()
operator|+
literal|"/"
operator|+
name|getTrigger
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|QuartzComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|QuartzComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
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
DECL|method|getScheduler ()
specifier|public
name|Scheduler
name|getScheduler
parameter_list|()
block|{
return|return
name|scheduler
return|;
block|}
DECL|method|getLoadBalancer ()
specifier|public
name|LoadBalancer
name|getLoadBalancer
parameter_list|()
block|{
if|if
condition|(
name|loadBalancer
operator|==
literal|null
condition|)
block|{
name|loadBalancer
operator|=
name|createLoadBalancer
argument_list|()
expr_stmt|;
block|}
return|return
name|loadBalancer
return|;
block|}
DECL|method|setLoadBalancer (final LoadBalancer loadBalancer)
specifier|public
name|void
name|setLoadBalancer
parameter_list|(
specifier|final
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{
name|this
operator|.
name|loadBalancer
operator|=
name|loadBalancer
expr_stmt|;
block|}
DECL|method|getJobDetail ()
specifier|public
name|JobDetail
name|getJobDetail
parameter_list|()
block|{
if|if
condition|(
name|jobDetail
operator|==
literal|null
condition|)
block|{
name|jobDetail
operator|=
name|createJobDetail
argument_list|()
expr_stmt|;
block|}
return|return
name|jobDetail
return|;
block|}
DECL|method|setJobDetail (final JobDetail jobDetail)
specifier|public
name|void
name|setJobDetail
parameter_list|(
specifier|final
name|JobDetail
name|jobDetail
parameter_list|)
block|{
name|this
operator|.
name|jobDetail
operator|=
name|jobDetail
expr_stmt|;
block|}
DECL|method|getTrigger ()
specifier|public
name|Trigger
name|getTrigger
parameter_list|()
block|{
return|return
name|trigger
return|;
block|}
DECL|method|setTrigger (final Trigger trigger)
specifier|public
name|void
name|setTrigger
parameter_list|(
specifier|final
name|Trigger
name|trigger
parameter_list|)
block|{
name|this
operator|.
name|trigger
operator|=
name|trigger
expr_stmt|;
block|}
DECL|method|isStateful ()
specifier|public
name|boolean
name|isStateful
parameter_list|()
block|{
return|return
name|this
operator|.
name|stateful
return|;
block|}
DECL|method|setStateful (final boolean stateful)
specifier|public
name|void
name|setStateful
parameter_list|(
specifier|final
name|boolean
name|stateful
parameter_list|)
block|{
name|this
operator|.
name|stateful
operator|=
name|stateful
expr_stmt|;
block|}
DECL|method|setScheduler (Scheduler scheduler)
specifier|public
name|void
name|setScheduler
parameter_list|(
name|Scheduler
name|scheduler
parameter_list|)
block|{
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|consumerStarted (final QuartzConsumer consumer)
specifier|public
specifier|synchronized
name|void
name|consumerStarted
parameter_list|(
specifier|final
name|QuartzConsumer
name|consumer
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|trigger
argument_list|,
literal|"trigger"
argument_list|)
expr_stmt|;
name|getLoadBalancer
argument_list|()
operator|.
name|addProcessor
argument_list|(
name|consumer
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
comment|// if we have not yet added our default trigger, then lets do it
if|if
condition|(
operator|!
name|started
condition|)
block|{
name|addTrigger
argument_list|(
name|getTrigger
argument_list|()
argument_list|,
name|getJobDetail
argument_list|()
argument_list|)
expr_stmt|;
name|started
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|consumerStopped (final QuartzConsumer consumer)
specifier|public
specifier|synchronized
name|void
name|consumerStopped
parameter_list|(
specifier|final
name|QuartzConsumer
name|consumer
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|trigger
argument_list|,
literal|"trigger"
argument_list|)
expr_stmt|;
name|getLoadBalancer
argument_list|()
operator|.
name|removeProcessor
argument_list|(
name|consumer
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getLoadBalancer
argument_list|()
operator|.
name|getProcessors
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|started
condition|)
block|{
name|removeTrigger
argument_list|(
name|getTrigger
argument_list|()
argument_list|,
name|getJobDetail
argument_list|()
argument_list|)
expr_stmt|;
name|started
operator|=
literal|false
expr_stmt|;
block|}
block|}
DECL|method|createLoadBalancer ()
specifier|protected
name|LoadBalancer
name|createLoadBalancer
parameter_list|()
block|{
return|return
operator|new
name|RoundRobinLoadBalancer
argument_list|()
return|;
block|}
DECL|method|createJobDetail ()
specifier|protected
name|JobDetail
name|createJobDetail
parameter_list|()
block|{
return|return
operator|new
name|JobDetail
argument_list|()
return|;
block|}
block|}
end_class

end_unit

