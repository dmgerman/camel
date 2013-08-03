begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz2
package|;
end_package

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
name|Route
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Job
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
name|SchedulerContext
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
name|TriggerKey
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
comment|/**  * This is a Quartz Job that is scheduled by QuartzEndpoint's Consumer and will call it to  * produce a QuartzMessage sending to a route.  *  */
end_comment

begin_class
DECL|class|CamelJob
specifier|public
class|class
name|CamelJob
implements|implements
name|Job
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelJob
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|execute (JobExecutionContext context)
specifier|public
name|void
name|execute
parameter_list|(
name|JobExecutionContext
name|context
parameter_list|)
throws|throws
name|JobExecutionException
block|{
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
try|try
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
literal|"Running CamelJob jobExecutionContext={}"
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
name|CamelContext
name|camelContext
init|=
name|getCamelContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|QuartzEndpoint
name|endpoint
init|=
name|lookupQuartzEndpoint
argument_list|(
name|camelContext
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|=
name|endpoint
operator|.
name|createExchange
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|QuartzMessage
argument_list|(
name|exchange
argument_list|,
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConsumerLoadBalancer
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
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
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
block|}
else|else
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed to execute CamelJob."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getCamelContext (JobExecutionContext context)
specifier|private
name|CamelContext
name|getCamelContext
parameter_list|(
name|JobExecutionContext
name|context
parameter_list|)
throws|throws
name|JobExecutionException
block|{
name|SchedulerContext
name|schedulerContext
init|=
name|getSchedulerContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|camelContextName
init|=
name|context
operator|.
name|getMergedJobDataMap
argument_list|()
operator|.
name|getString
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_CONTEXT_NAME
argument_list|)
decl_stmt|;
name|CamelContext
name|result
init|=
operator|(
name|CamelContext
operator|)
name|schedulerContext
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_CONTEXT
operator|+
literal|"-"
operator|+
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JobExecutionException
argument_list|(
literal|"No CamelContext could be found with name: "
operator|+
name|camelContextName
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getSchedulerContext (JobExecutionContext context)
specifier|private
name|SchedulerContext
name|getSchedulerContext
parameter_list|(
name|JobExecutionContext
name|context
parameter_list|)
throws|throws
name|JobExecutionException
block|{
try|try
block|{
return|return
name|context
operator|.
name|getScheduler
argument_list|()
operator|.
name|getContext
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JobExecutionException
argument_list|(
literal|"Failed to obtain scheduler context for job "
operator|+
name|context
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|lookupQuartzEndpoint (CamelContext camelContext, JobExecutionContext quartzContext)
specifier|private
name|QuartzEndpoint
name|lookupQuartzEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|JobExecutionContext
name|quartzContext
parameter_list|)
throws|throws
name|JobExecutionException
block|{
name|TriggerKey
name|triggerKey
init|=
name|quartzContext
operator|.
name|getTrigger
argument_list|()
operator|.
name|getKey
argument_list|()
decl_stmt|;
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
literal|"Looking up existing QuartzEndpoint with triggerKey={}"
argument_list|,
name|triggerKey
argument_list|)
expr_stmt|;
block|}
comment|// check all active routes for the quartz endpoint this task matches
comment|// as we prefer to use the existing endpoint from the routes
for|for
control|(
name|Route
name|route
range|:
name|camelContext
operator|.
name|getRoutes
argument_list|()
control|)
block|{
if|if
condition|(
name|route
operator|.
name|getEndpoint
argument_list|()
operator|instanceof
name|QuartzEndpoint
condition|)
block|{
name|QuartzEndpoint
name|quartzEndpoint
init|=
operator|(
name|QuartzEndpoint
operator|)
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|TriggerKey
name|checkTriggerKey
init|=
name|quartzEndpoint
operator|.
name|getTriggerKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Checking route endpoint={} with checkTriggerKey={}"
argument_list|,
name|quartzEndpoint
argument_list|,
name|checkTriggerKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|triggerKey
operator|.
name|equals
argument_list|(
name|checkTriggerKey
argument_list|)
condition|)
block|{
return|return
name|quartzEndpoint
return|;
block|}
block|}
block|}
comment|// fallback and lookup existing from registry (eg maybe a @Consume POJO with a quartz endpoint, and thus not from a route)
name|String
name|endpointUri
init|=
name|quartzContext
operator|.
name|getMergedJobDataMap
argument_list|()
operator|.
name|getString
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_ENDPOINT_URI
argument_list|)
decl_stmt|;
name|QuartzEndpoint
name|result
init|=
literal|null
decl_stmt|;
comment|// Even though the same camelContext.getEndpoint call, but if/else display different log.
if|if
condition|(
name|camelContext
operator|.
name|hasEndpoint
argument_list|(
name|endpointUri
argument_list|)
operator|!=
literal|null
condition|)
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
literal|"Getting Endpoint from camelContext."
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|QuartzEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot find existing QuartzEndpoint with uri: {}. Creating new endpoint instance."
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
name|result
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|QuartzEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JobExecutionException
argument_list|(
literal|"No QuartzEndpoint could be found with endpointUri: "
operator|+
name|endpointUri
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

