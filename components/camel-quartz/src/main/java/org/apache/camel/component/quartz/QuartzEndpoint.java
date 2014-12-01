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
name|CamelExchangeException
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
name|ShutdownableService
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
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/quartz.html">Quartz Endpoint</a>  *  * @version   */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"quartz"
argument_list|,
name|consumerClass
operator|=
name|QuartzConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"scheduling"
argument_list|)
DECL|class|QuartzEndpoint
specifier|public
class|class
name|QuartzEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|ShutdownableService
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|QuartzEndpoint
operator|.
name|class
argument_list|)
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
init|=
operator|new
name|JobDetail
argument_list|()
decl_stmt|;
DECL|field|started
specifier|private
specifier|volatile
name|boolean
name|started
decl_stmt|;
annotation|@
name|UriPath
DECL|field|groupName
specifier|private
name|String
name|groupName
decl_stmt|;
annotation|@
name|UriPath
DECL|field|timerName
specifier|private
name|String
name|timerName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|stateful
specifier|private
name|boolean
name|stateful
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|deleteJob
specifier|private
name|boolean
name|deleteJob
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|pauseJob
specifier|private
name|boolean
name|pauseJob
decl_stmt|;
comment|/** If it is true, the CamelContext name is used,      *  if it is false, use the CamelContext management name which could be changed during the deploy time       **/
annotation|@
name|UriParam
DECL|field|usingFixedCamelContextName
specifier|private
name|boolean
name|usingFixedCamelContextName
decl_stmt|;
DECL|method|QuartzEndpoint (final String endpointUri, final QuartzComponent component)
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
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|getJobDetail
argument_list|()
operator|.
name|setName
argument_list|(
literal|"quartz-"
operator|+
name|getId
argument_list|()
argument_list|)
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
name|QUARTZ_ENDPOINT_URI
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isUsingFixedCamelContextName
argument_list|()
condition|)
block|{
name|detail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_CONTEXT_NAME
argument_list|,
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// must use management name as it should be unique in the same JVM
name|detail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_CONTEXT_NAME
argument_list|,
name|QuartzHelper
operator|.
name|getQuartzContextName
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|getJobName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getComponent
argument_list|()
operator|.
name|addJob
argument_list|(
name|detail
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
DECL|method|pauseTrigger (final Trigger trigger)
specifier|public
name|void
name|pauseTrigger
parameter_list|(
specifier|final
name|Trigger
name|trigger
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|getComponent
argument_list|()
operator|.
name|pauseJob
argument_list|(
name|trigger
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteTrigger (final Trigger trigger)
specifier|public
name|void
name|deleteTrigger
parameter_list|(
specifier|final
name|Trigger
name|trigger
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|getComponent
argument_list|()
operator|.
name|deleteJob
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
comment|/**      * This method is invoked when a Quartz job is fired.      *      * @param jobExecutionContext the Quartz Job context      */
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
name|boolean
name|run
init|=
literal|true
decl_stmt|;
name|LoadBalancer
name|balancer
init|=
name|getLoadBalancer
argument_list|()
decl_stmt|;
if|if
condition|(
name|balancer
operator|instanceof
name|ServiceSupport
condition|)
block|{
name|run
operator|=
operator|(
operator|(
name|ServiceSupport
operator|)
name|balancer
operator|)
operator|.
name|isRunAllowed
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|run
condition|)
block|{
comment|// quartz scheduler could potential trigger during a route has been shutdown
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot execute Quartz Job with context: "
operator|+
name|jobExecutionContext
operator|+
literal|" because processor is not started: "
operator|+
name|balancer
argument_list|)
expr_stmt|;
return|return;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Firing Quartz Job with context: {}"
argument_list|,
name|jobExecutionContext
argument_list|)
expr_stmt|;
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
name|balancer
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
name|CamelExchangeException
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
name|QuartzConsumer
name|answer
init|=
operator|new
name|QuartzConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
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
DECL|method|getJobName ()
specifier|protected
name|String
name|getJobName
parameter_list|()
block|{
return|return
name|getJobDetail
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
DECL|method|getLoadBalancer ()
specifier|public
name|LoadBalancer
name|getLoadBalancer
parameter_list|()
block|{
return|return
name|loadBalancer
return|;
block|}
DECL|method|getGroupName ()
specifier|public
name|String
name|getGroupName
parameter_list|()
block|{
return|return
name|groupName
return|;
block|}
DECL|method|setGroupName (String groupName)
specifier|public
name|void
name|setGroupName
parameter_list|(
name|String
name|groupName
parameter_list|)
block|{
name|this
operator|.
name|groupName
operator|=
name|groupName
expr_stmt|;
block|}
DECL|method|getTimerName ()
specifier|public
name|String
name|getTimerName
parameter_list|()
block|{
return|return
name|timerName
return|;
block|}
DECL|method|setTimerName (String timerName)
specifier|public
name|void
name|setTimerName
parameter_list|(
name|String
name|timerName
parameter_list|)
block|{
name|this
operator|.
name|timerName
operator|=
name|timerName
expr_stmt|;
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
DECL|method|isDeleteJob ()
specifier|public
name|boolean
name|isDeleteJob
parameter_list|()
block|{
return|return
name|deleteJob
return|;
block|}
DECL|method|setDeleteJob (boolean deleteJob)
specifier|public
name|void
name|setDeleteJob
parameter_list|(
name|boolean
name|deleteJob
parameter_list|)
block|{
name|this
operator|.
name|deleteJob
operator|=
name|deleteJob
expr_stmt|;
block|}
DECL|method|isPauseJob ()
specifier|public
name|boolean
name|isPauseJob
parameter_list|()
block|{
return|return
name|pauseJob
return|;
block|}
DECL|method|setPauseJob (boolean pauseJob)
specifier|public
name|void
name|setPauseJob
parameter_list|(
name|boolean
name|pauseJob
parameter_list|)
block|{
name|this
operator|.
name|pauseJob
operator|=
name|pauseJob
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|isUsingFixedCamelContextName ()
specifier|public
name|boolean
name|isUsingFixedCamelContextName
parameter_list|()
block|{
return|return
name|usingFixedCamelContextName
return|;
block|}
DECL|method|setUsingFixedCamelContextName (boolean usingFixedCamelContextName)
specifier|public
name|void
name|setUsingFixedCamelContextName
parameter_list|(
name|boolean
name|usingFixedCamelContextName
parameter_list|)
block|{
name|this
operator|.
name|usingFixedCamelContextName
operator|=
name|usingFixedCamelContextName
expr_stmt|;
block|}
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding consumer {}"
argument_list|,
name|consumer
operator|.
name|getProcessor
argument_list|()
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
if|if
condition|(
name|started
condition|)
block|{
name|pauseTrigger
argument_list|(
name|getTrigger
argument_list|()
argument_list|)
expr_stmt|;
name|started
operator|=
literal|false
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Removing consumer {}"
argument_list|,
name|consumer
operator|.
name|getProcessor
argument_list|()
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getComponent
argument_list|()
argument_list|,
literal|"QuartzComponent"
argument_list|,
name|this
argument_list|)
expr_stmt|;
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|loadBalancer
argument_list|)
expr_stmt|;
if|if
condition|(
name|isDeleteJob
argument_list|()
operator|&&
name|isPauseJob
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot have both options deleteJob and pauseJob enabled"
argument_list|)
throw|;
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|loadBalancer
argument_list|)
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|trigger
argument_list|,
literal|"trigger"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isDeleteJob
argument_list|()
condition|)
block|{
name|deleteTrigger
argument_list|(
name|getTrigger
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isPauseJob
argument_list|()
condition|)
block|{
name|pauseTrigger
argument_list|(
name|getTrigger
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

