begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz2.springboot
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
operator|.
name|springboot
package|;
end_package

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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Provides a scheduled delivery of messages using the Quartz 2.x scheduler.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.quartz2"
argument_list|)
DECL|class|QuartzComponentConfiguration
specifier|public
class|class
name|QuartzComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether or not the scheduler should be auto started. This options is      * default true      */
DECL|field|autoStartScheduler
specifier|private
name|Boolean
name|autoStartScheduler
init|=
literal|true
decl_stmt|;
comment|/**      * Seconds to wait before starting the quartz scheduler.      */
DECL|field|startDelayedSeconds
specifier|private
name|Integer
name|startDelayedSeconds
decl_stmt|;
comment|/**      * Whether to prefix the quartz job with the endpoint id. This option is      * default false.      */
DECL|field|prefixJobNameWithEndpointId
specifier|private
name|Boolean
name|prefixJobNameWithEndpointId
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to enable Quartz JMX which allows to manage the Quartz scheduler      * from JMX. This options is default true      */
DECL|field|enableJmx
specifier|private
name|Boolean
name|enableJmx
init|=
literal|true
decl_stmt|;
comment|/**      * Properties to configure the Quartz scheduler. The option is a      * java.util.Properties type.      */
DECL|field|properties
specifier|private
name|String
name|properties
decl_stmt|;
comment|/**      * File name of the properties to load from the classpath      */
DECL|field|propertiesFile
specifier|private
name|String
name|propertiesFile
decl_stmt|;
comment|/**      * Whether to prefix the Quartz Scheduler instance name with the      * CamelContext name. This is enabled by default, to let each CamelContext      * use its own Quartz scheduler instance by default. You can set this option      * to false to reuse Quartz scheduler instances between multiple      * CamelContext's.      */
DECL|field|prefixInstanceName
specifier|private
name|Boolean
name|prefixInstanceName
init|=
literal|true
decl_stmt|;
comment|/**      * Whether to interrupt jobs on shutdown which forces the scheduler to      * shutdown quicker and attempt to interrupt any running jobs. If this is      * enabled then any running jobs can fail due to being interrupted.      */
DECL|field|interruptJobsOnShutdown
specifier|private
name|Boolean
name|interruptJobsOnShutdown
init|=
literal|false
decl_stmt|;
comment|/**      * To use the custom SchedulerFactory which is used to create the Scheduler.      * The option is a org.quartz.SchedulerFactory type.      */
DECL|field|schedulerFactory
specifier|private
name|String
name|schedulerFactory
decl_stmt|;
comment|/**      * To use the custom configured Quartz scheduler, instead of creating a new      * Scheduler. The option is a org.quartz.Scheduler type.      */
DECL|field|scheduler
specifier|private
name|String
name|scheduler
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getAutoStartScheduler ()
specifier|public
name|Boolean
name|getAutoStartScheduler
parameter_list|()
block|{
return|return
name|autoStartScheduler
return|;
block|}
DECL|method|setAutoStartScheduler (Boolean autoStartScheduler)
specifier|public
name|void
name|setAutoStartScheduler
parameter_list|(
name|Boolean
name|autoStartScheduler
parameter_list|)
block|{
name|this
operator|.
name|autoStartScheduler
operator|=
name|autoStartScheduler
expr_stmt|;
block|}
DECL|method|getStartDelayedSeconds ()
specifier|public
name|Integer
name|getStartDelayedSeconds
parameter_list|()
block|{
return|return
name|startDelayedSeconds
return|;
block|}
DECL|method|setStartDelayedSeconds (Integer startDelayedSeconds)
specifier|public
name|void
name|setStartDelayedSeconds
parameter_list|(
name|Integer
name|startDelayedSeconds
parameter_list|)
block|{
name|this
operator|.
name|startDelayedSeconds
operator|=
name|startDelayedSeconds
expr_stmt|;
block|}
DECL|method|getPrefixJobNameWithEndpointId ()
specifier|public
name|Boolean
name|getPrefixJobNameWithEndpointId
parameter_list|()
block|{
return|return
name|prefixJobNameWithEndpointId
return|;
block|}
DECL|method|setPrefixJobNameWithEndpointId ( Boolean prefixJobNameWithEndpointId)
specifier|public
name|void
name|setPrefixJobNameWithEndpointId
parameter_list|(
name|Boolean
name|prefixJobNameWithEndpointId
parameter_list|)
block|{
name|this
operator|.
name|prefixJobNameWithEndpointId
operator|=
name|prefixJobNameWithEndpointId
expr_stmt|;
block|}
DECL|method|getEnableJmx ()
specifier|public
name|Boolean
name|getEnableJmx
parameter_list|()
block|{
return|return
name|enableJmx
return|;
block|}
DECL|method|setEnableJmx (Boolean enableJmx)
specifier|public
name|void
name|setEnableJmx
parameter_list|(
name|Boolean
name|enableJmx
parameter_list|)
block|{
name|this
operator|.
name|enableJmx
operator|=
name|enableJmx
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|String
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (String properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|String
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getPropertiesFile ()
specifier|public
name|String
name|getPropertiesFile
parameter_list|()
block|{
return|return
name|propertiesFile
return|;
block|}
DECL|method|setPropertiesFile (String propertiesFile)
specifier|public
name|void
name|setPropertiesFile
parameter_list|(
name|String
name|propertiesFile
parameter_list|)
block|{
name|this
operator|.
name|propertiesFile
operator|=
name|propertiesFile
expr_stmt|;
block|}
DECL|method|getPrefixInstanceName ()
specifier|public
name|Boolean
name|getPrefixInstanceName
parameter_list|()
block|{
return|return
name|prefixInstanceName
return|;
block|}
DECL|method|setPrefixInstanceName (Boolean prefixInstanceName)
specifier|public
name|void
name|setPrefixInstanceName
parameter_list|(
name|Boolean
name|prefixInstanceName
parameter_list|)
block|{
name|this
operator|.
name|prefixInstanceName
operator|=
name|prefixInstanceName
expr_stmt|;
block|}
DECL|method|getInterruptJobsOnShutdown ()
specifier|public
name|Boolean
name|getInterruptJobsOnShutdown
parameter_list|()
block|{
return|return
name|interruptJobsOnShutdown
return|;
block|}
DECL|method|setInterruptJobsOnShutdown (Boolean interruptJobsOnShutdown)
specifier|public
name|void
name|setInterruptJobsOnShutdown
parameter_list|(
name|Boolean
name|interruptJobsOnShutdown
parameter_list|)
block|{
name|this
operator|.
name|interruptJobsOnShutdown
operator|=
name|interruptJobsOnShutdown
expr_stmt|;
block|}
DECL|method|getSchedulerFactory ()
specifier|public
name|String
name|getSchedulerFactory
parameter_list|()
block|{
return|return
name|schedulerFactory
return|;
block|}
DECL|method|setSchedulerFactory (String schedulerFactory)
specifier|public
name|void
name|setSchedulerFactory
parameter_list|(
name|String
name|schedulerFactory
parameter_list|)
block|{
name|this
operator|.
name|schedulerFactory
operator|=
name|schedulerFactory
expr_stmt|;
block|}
DECL|method|getScheduler ()
specifier|public
name|String
name|getScheduler
parameter_list|()
block|{
return|return
name|scheduler
return|;
block|}
DECL|method|setScheduler (String scheduler)
specifier|public
name|void
name|setScheduler
parameter_list|(
name|String
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
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

