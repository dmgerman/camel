begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|EndpointConsumerBuilder
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
name|EndpointProducerBuilder
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
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * Provides a scheduled delivery of messages using the Quartz 2.x scheduler.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|QuartzEndpointBuilderFactory
specifier|public
interface|interface
name|QuartzEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Quartz component.      */
DECL|interface|QuartzEndpointBuilder
specifier|public
interface|interface
name|QuartzEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedQuartzEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedQuartzEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Specifies a cron expression to define when to trigger.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|cron (String cron)
specifier|default
name|QuartzEndpointBuilder
name|cron
parameter_list|(
name|String
name|cron
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"cron"
argument_list|,
name|cron
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true, then the trigger automatically delete when route          * stop. Else if set to false, it will remain in scheduler. When set to          * false, it will also mean user may reuse pre-configured trigger with          * camel Uri. Just ensure the names match. Notice you cannot have both          * deleteJob and pauseJob set to true.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|deleteJob (boolean deleteJob)
specifier|default
name|QuartzEndpointBuilder
name|deleteJob
parameter_list|(
name|boolean
name|deleteJob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"deleteJob"
argument_list|,
name|deleteJob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true, then the trigger automatically delete when route          * stop. Else if set to false, it will remain in scheduler. When set to          * false, it will also mean user may reuse pre-configured trigger with          * camel Uri. Just ensure the names match. Notice you cannot have both          * deleteJob and pauseJob set to true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|deleteJob (String deleteJob)
specifier|default
name|QuartzEndpointBuilder
name|deleteJob
parameter_list|(
name|String
name|deleteJob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"deleteJob"
argument_list|,
name|deleteJob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not the job should remain stored after it is orphaned (no          * triggers point to it).          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|durableJob (boolean durableJob)
specifier|default
name|QuartzEndpointBuilder
name|durableJob
parameter_list|(
name|boolean
name|durableJob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"durableJob"
argument_list|,
name|durableJob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not the job should remain stored after it is orphaned (no          * triggers point to it).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|durableJob (String durableJob)
specifier|default
name|QuartzEndpointBuilder
name|durableJob
parameter_list|(
name|String
name|durableJob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"durableJob"
argument_list|,
name|durableJob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true, then the trigger automatically pauses when route          * stop. Else if set to false, it will remain in scheduler. When set to          * false, it will also mean user may reuse pre-configured trigger with          * camel Uri. Just ensure the names match. Notice you cannot have both          * deleteJob and pauseJob set to true.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|pauseJob (boolean pauseJob)
specifier|default
name|QuartzEndpointBuilder
name|pauseJob
parameter_list|(
name|boolean
name|pauseJob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"pauseJob"
argument_list|,
name|pauseJob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true, then the trigger automatically pauses when route          * stop. Else if set to false, it will remain in scheduler. When set to          * false, it will also mean user may reuse pre-configured trigger with          * camel Uri. Just ensure the names match. Notice you cannot have both          * deleteJob and pauseJob set to true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|pauseJob (String pauseJob)
specifier|default
name|QuartzEndpointBuilder
name|pauseJob
parameter_list|(
name|String
name|pauseJob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"pauseJob"
argument_list|,
name|pauseJob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Instructs the scheduler whether or not the job should be re-executed          * if a 'recovery' or 'fail-over' situation is encountered.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|recoverableJob (boolean recoverableJob)
specifier|default
name|QuartzEndpointBuilder
name|recoverableJob
parameter_list|(
name|boolean
name|recoverableJob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"recoverableJob"
argument_list|,
name|recoverableJob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Instructs the scheduler whether or not the job should be re-executed          * if a 'recovery' or 'fail-over' situation is encountered.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|recoverableJob (String recoverableJob)
specifier|default
name|QuartzEndpointBuilder
name|recoverableJob
parameter_list|(
name|String
name|recoverableJob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"recoverableJob"
argument_list|,
name|recoverableJob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Uses a Quartz PersistJobDataAfterExecution and          * DisallowConcurrentExecution instead of the default job.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|stateful (boolean stateful)
specifier|default
name|QuartzEndpointBuilder
name|stateful
parameter_list|(
name|boolean
name|stateful
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"stateful"
argument_list|,
name|stateful
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Uses a Quartz PersistJobDataAfterExecution and          * DisallowConcurrentExecution instead of the default job.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|stateful (String stateful)
specifier|default
name|QuartzEndpointBuilder
name|stateful
parameter_list|(
name|String
name|stateful
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"stateful"
argument_list|,
name|stateful
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not the scheduler should be auto started.          *           * The option is a:<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|autoStartScheduler ( boolean autoStartScheduler)
specifier|default
name|QuartzEndpointBuilder
name|autoStartScheduler
parameter_list|(
name|boolean
name|autoStartScheduler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"autoStartScheduler"
argument_list|,
name|autoStartScheduler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not the scheduler should be auto started.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|autoStartScheduler ( String autoStartScheduler)
specifier|default
name|QuartzEndpointBuilder
name|autoStartScheduler
parameter_list|(
name|String
name|autoStartScheduler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"autoStartScheduler"
argument_list|,
name|autoStartScheduler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If it is true will fire the trigger when the route is start when          * using SimpleTrigger.          *           * The option is a:<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|fireNow (boolean fireNow)
specifier|default
name|QuartzEndpointBuilder
name|fireNow
parameter_list|(
name|boolean
name|fireNow
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"fireNow"
argument_list|,
name|fireNow
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If it is true will fire the trigger when the route is start when          * using SimpleTrigger.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|fireNow (String fireNow)
specifier|default
name|QuartzEndpointBuilder
name|fireNow
parameter_list|(
name|String
name|fireNow
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"fireNow"
argument_list|,
name|fireNow
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Seconds to wait before starting the quartz scheduler.          *           * The option is a:<code>int</code> type.          *           * Group: scheduler          */
DECL|method|startDelayedSeconds ( int startDelayedSeconds)
specifier|default
name|QuartzEndpointBuilder
name|startDelayedSeconds
parameter_list|(
name|int
name|startDelayedSeconds
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"startDelayedSeconds"
argument_list|,
name|startDelayedSeconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Seconds to wait before starting the quartz scheduler.          *           * The option will be converted to a<code>int</code> type.          *           * Group: scheduler          */
DECL|method|startDelayedSeconds ( String startDelayedSeconds)
specifier|default
name|QuartzEndpointBuilder
name|startDelayedSeconds
parameter_list|(
name|String
name|startDelayedSeconds
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"startDelayedSeconds"
argument_list|,
name|startDelayedSeconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * In case of scheduler has already started, we want the trigger start          * slightly after current time to ensure endpoint is fully started          * before the job kicks in.          *           * The option is a:<code>long</code> type.          *           * Group: scheduler          */
DECL|method|triggerStartDelay (long triggerStartDelay)
specifier|default
name|QuartzEndpointBuilder
name|triggerStartDelay
parameter_list|(
name|long
name|triggerStartDelay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"triggerStartDelay"
argument_list|,
name|triggerStartDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * In case of scheduler has already started, we want the trigger start          * slightly after current time to ensure endpoint is fully started          * before the job kicks in.          *           * The option will be converted to a<code>long</code> type.          *           * Group: scheduler          */
DECL|method|triggerStartDelay (String triggerStartDelay)
specifier|default
name|QuartzEndpointBuilder
name|triggerStartDelay
parameter_list|(
name|String
name|triggerStartDelay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"triggerStartDelay"
argument_list|,
name|triggerStartDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Quartz component.      */
DECL|interface|AdvancedQuartzEndpointBuilder
specifier|public
interface|interface
name|AdvancedQuartzEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|QuartzEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|QuartzEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies a custom calendar to avoid specific range of date.          *           * The option is a:<code>org.quartz.Calendar</code> type.          *           * Group: advanced          */
DECL|method|customCalendar ( Object customCalendar)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|customCalendar
parameter_list|(
name|Object
name|customCalendar
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"customCalendar"
argument_list|,
name|customCalendar
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies a custom calendar to avoid specific range of date.          *           * The option will be converted to a<code>org.quartz.Calendar</code>          * type.          *           * Group: advanced          */
DECL|method|customCalendar ( String customCalendar)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|customCalendar
parameter_list|(
name|String
name|customCalendar
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"customCalendar"
argument_list|,
name|customCalendar
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure additional options on the job.          *           * The option is a:<code>java.util.Map&lt;java.lang.String,          * java.lang.Object&gt;</code> type.          *           * Group: advanced          */
DECL|method|jobParameters ( Map<String, Object> jobParameters)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|jobParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jobParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"jobParameters"
argument_list|,
name|jobParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure additional options on the job.          *           * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String, java.lang.Object&gt;</code>          * type.          *           * Group: advanced          */
DECL|method|jobParameters (String jobParameters)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|jobParameters
parameter_list|(
name|String
name|jobParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"jobParameters"
argument_list|,
name|jobParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the job name should be prefixed with endpoint id.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|prefixJobNameWithEndpointId ( boolean prefixJobNameWithEndpointId)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|prefixJobNameWithEndpointId
parameter_list|(
name|boolean
name|prefixJobNameWithEndpointId
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"prefixJobNameWithEndpointId"
argument_list|,
name|prefixJobNameWithEndpointId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the job name should be prefixed with endpoint id.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|prefixJobNameWithEndpointId ( String prefixJobNameWithEndpointId)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|prefixJobNameWithEndpointId
parameter_list|(
name|String
name|prefixJobNameWithEndpointId
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"prefixJobNameWithEndpointId"
argument_list|,
name|prefixJobNameWithEndpointId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure additional options on the trigger.          *           * The option is a:<code>java.util.Map&lt;java.lang.String,          * java.lang.Object&gt;</code> type.          *           * Group: advanced          */
DECL|method|triggerParameters ( Map<String, Object> triggerParameters)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|triggerParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|triggerParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"triggerParameters"
argument_list|,
name|triggerParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure additional options on the trigger.          *           * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String, java.lang.Object&gt;</code>          * type.          *           * Group: advanced          */
DECL|method|triggerParameters ( String triggerParameters)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|triggerParameters
parameter_list|(
name|String
name|triggerParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"triggerParameters"
argument_list|,
name|triggerParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If it is true, JobDataMap uses the CamelContext name directly to          * reference the CamelContext, if it is false, JobDataMap uses use the          * CamelContext management name which could be changed during the deploy          * time.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|usingFixedCamelContextName ( boolean usingFixedCamelContextName)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|usingFixedCamelContextName
parameter_list|(
name|boolean
name|usingFixedCamelContextName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"usingFixedCamelContextName"
argument_list|,
name|usingFixedCamelContextName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If it is true, JobDataMap uses the CamelContext name directly to          * reference the CamelContext, if it is false, JobDataMap uses use the          * CamelContext management name which could be changed during the deploy          * time.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|usingFixedCamelContextName ( String usingFixedCamelContextName)
specifier|default
name|AdvancedQuartzEndpointBuilder
name|usingFixedCamelContextName
parameter_list|(
name|String
name|usingFixedCamelContextName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"usingFixedCamelContextName"
argument_list|,
name|usingFixedCamelContextName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Quartz (camel-quartz)      * Provides a scheduled delivery of messages using the Quartz 2.x scheduler.      *       * Category: scheduling      * Available as of version: 2.12      * Maven coordinates: org.apache.camel:camel-quartz      *       * Syntax:<code>quartz:groupName/triggerName</code>      *       * Path parameter: groupName      * The quartz group name to use. The combination of group name and timer      * name should be unique.      * Default value: Camel      *       * Path parameter: triggerName (required)      * The quartz timer name to use. The combination of group name and timer      * name should be unique.      */
DECL|method|quartz (String path)
specifier|default
name|QuartzEndpointBuilder
name|quartz
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|QuartzEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|QuartzEndpointBuilder
implements|,
name|AdvancedQuartzEndpointBuilder
block|{
specifier|public
name|QuartzEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"quartz"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|QuartzEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

