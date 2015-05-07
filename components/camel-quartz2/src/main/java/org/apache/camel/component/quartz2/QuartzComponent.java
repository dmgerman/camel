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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Properties
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
name|StartupListener
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
name|UriEndpointComponent
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
name|IOHelper
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
name|IntrospectionSupport
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
name|ResourceHelper
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
name|SchedulerFactory
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
name|quartz
operator|.
name|impl
operator|.
name|StdSchedulerFactory
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
comment|/**  * A factory for QuartzEndpoint. This component will hold a Quartz Scheduler that will provide scheduled timer based  * endpoint that generate a QuartzMessage to a route. Currently it support Cron and Simple trigger scheduling type.  *  *<p>This component uses Quartz 2.x API and provide all the features from "camel-quartz". It has reused some  * of the code, but mostly has been re-written in attempt to be more easier to maintain, and use Quartz more  * fully.</p>  */
end_comment

begin_class
DECL|class|QuartzComponent
specifier|public
class|class
name|QuartzComponent
extends|extends
name|UriEndpointComponent
implements|implements
name|StartupListener
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
name|QuartzComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|schedulerFactory
specifier|private
name|SchedulerFactory
name|schedulerFactory
decl_stmt|;
DECL|field|scheduler
specifier|private
name|Scheduler
name|scheduler
decl_stmt|;
DECL|field|properties
specifier|private
name|Properties
name|properties
decl_stmt|;
DECL|field|propertiesFile
specifier|private
name|String
name|propertiesFile
decl_stmt|;
DECL|field|startDelayedSeconds
specifier|private
name|int
name|startDelayedSeconds
decl_stmt|;
DECL|field|autoStartScheduler
specifier|private
name|boolean
name|autoStartScheduler
init|=
literal|true
decl_stmt|;
DECL|field|prefixJobNameWithEndpointId
specifier|private
name|boolean
name|prefixJobNameWithEndpointId
decl_stmt|;
DECL|field|enableJmx
specifier|private
name|boolean
name|enableJmx
init|=
literal|true
decl_stmt|;
DECL|method|QuartzComponent ()
specifier|public
name|QuartzComponent
parameter_list|()
block|{
name|super
argument_list|(
name|QuartzEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|QuartzComponent (CamelContext camelContext)
specifier|public
name|QuartzComponent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|QuartzEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|isAutoStartScheduler ()
specifier|public
name|boolean
name|isAutoStartScheduler
parameter_list|()
block|{
return|return
name|autoStartScheduler
return|;
block|}
comment|/**      * Whether or not the scheduler should be auto started.      *<p/>      * This options is default true      */
DECL|method|setAutoStartScheduler (boolean autoStartScheduler)
specifier|public
name|void
name|setAutoStartScheduler
parameter_list|(
name|boolean
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
name|int
name|getStartDelayedSeconds
parameter_list|()
block|{
return|return
name|startDelayedSeconds
return|;
block|}
comment|/**      * Seconds to wait before starting the quartz scheduler.      */
DECL|method|setStartDelayedSeconds (int startDelayedSeconds)
specifier|public
name|void
name|setStartDelayedSeconds
parameter_list|(
name|int
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
DECL|method|isPrefixJobNameWithEndpointId ()
specifier|public
name|boolean
name|isPrefixJobNameWithEndpointId
parameter_list|()
block|{
return|return
name|prefixJobNameWithEndpointId
return|;
block|}
comment|/**      * Whether to prefix the quartz job with the endpoint id.      *<p/>      * This option is default false.      */
DECL|method|setPrefixJobNameWithEndpointId (boolean prefixJobNameWithEndpointId)
specifier|public
name|void
name|setPrefixJobNameWithEndpointId
parameter_list|(
name|boolean
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
DECL|method|isEnableJmx ()
specifier|public
name|boolean
name|isEnableJmx
parameter_list|()
block|{
return|return
name|enableJmx
return|;
block|}
comment|/**      * Whether to enable Quartz JMX which allows to manage the Quartz scheduler from JMX.      *<p/>      * This options is default true      */
DECL|method|setEnableJmx (boolean enableJmx)
specifier|public
name|void
name|setEnableJmx
parameter_list|(
name|boolean
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
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * Properties to configure the Quartz scheduler.      */
DECL|method|setProperties (Properties properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
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
comment|/**      * File name of the properties to load from the classpath      */
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
DECL|method|getSchedulerFactory ()
specifier|public
name|SchedulerFactory
name|getSchedulerFactory
parameter_list|()
throws|throws
name|SchedulerException
block|{
if|if
condition|(
name|schedulerFactory
operator|==
literal|null
condition|)
block|{
name|schedulerFactory
operator|=
name|createSchedulerFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|schedulerFactory
return|;
block|}
DECL|method|createSchedulerFactory ()
specifier|private
name|SchedulerFactory
name|createSchedulerFactory
parameter_list|()
throws|throws
name|SchedulerException
block|{
name|SchedulerFactory
name|answer
decl_stmt|;
name|Properties
name|prop
init|=
name|loadProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|prop
operator|!=
literal|null
condition|)
block|{
comment|// force disabling update checker (will do online check over the internet)
name|prop
operator|.
name|put
argument_list|(
literal|"org.quartz.scheduler.skipUpdateCheck"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"org.terracotta.quartz.skipUpdateCheck"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
comment|// camel context name will be a suffix to use one scheduler per context
name|String
name|instName
init|=
name|createInstanceName
argument_list|(
name|prop
argument_list|)
decl_stmt|;
name|prop
operator|.
name|setProperty
argument_list|(
name|StdSchedulerFactory
operator|.
name|PROP_SCHED_INSTANCE_NAME
argument_list|,
name|instName
argument_list|)
expr_stmt|;
comment|// enable jmx unless configured to not do so
if|if
condition|(
name|enableJmx
operator|&&
operator|!
name|prop
operator|.
name|containsKey
argument_list|(
literal|"org.quartz.scheduler.jmx.export"
argument_list|)
condition|)
block|{
name|prop
operator|.
name|put
argument_list|(
literal|"org.quartz.scheduler.jmx.export"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Setting org.quartz.scheduler.jmx.export=true to ensure QuartzScheduler(s) will be enlisted in JMX."
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
operator|new
name|StdSchedulerFactory
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// read default props to be able to use a single scheduler per camel context
comment|// if we need more than one scheduler per context use setScheduler(Scheduler)
comment|// or setFactory(SchedulerFactory) methods
comment|// must use classloader from StdSchedulerFactory to work even in OSGi
name|InputStream
name|is
init|=
name|StdSchedulerFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/quartz/quartz.properties"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SchedulerException
argument_list|(
literal|"Quartz properties file not found in classpath: org/quartz/quartz.properties"
argument_list|)
throw|;
block|}
name|prop
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
block|{
name|prop
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SchedulerException
argument_list|(
literal|"Error loading Quartz properties file from classpath: org/quartz/quartz.properties"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
comment|// camel context name will be a suffix to use one scheduler per context
name|String
name|instName
init|=
name|createInstanceName
argument_list|(
name|prop
argument_list|)
decl_stmt|;
name|prop
operator|.
name|setProperty
argument_list|(
name|StdSchedulerFactory
operator|.
name|PROP_SCHED_INSTANCE_NAME
argument_list|,
name|instName
argument_list|)
expr_stmt|;
comment|// force disabling update checker (will do online check over the internet)
name|prop
operator|.
name|put
argument_list|(
literal|"org.quartz.scheduler.skipUpdateCheck"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"org.terracotta.quartz.skipUpdateCheck"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
comment|// enable jmx unless configured to not do so
if|if
condition|(
name|enableJmx
operator|&&
operator|!
name|prop
operator|.
name|containsKey
argument_list|(
literal|"org.quartz.scheduler.jmx.export"
argument_list|)
condition|)
block|{
name|prop
operator|.
name|put
argument_list|(
literal|"org.quartz.scheduler.jmx.export"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Setting org.quartz.scheduler.jmx.export=true to ensure QuartzScheduler(s) will be enlisted in JMX."
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
operator|new
name|StdSchedulerFactory
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|prop
operator|.
name|getProperty
argument_list|(
name|StdSchedulerFactory
operator|.
name|PROP_SCHED_INSTANCE_NAME
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating SchedulerFactory: {} with properties: {}"
argument_list|,
name|name
argument_list|,
name|prop
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createInstanceName (Properties prop)
specifier|protected
name|String
name|createInstanceName
parameter_list|(
name|Properties
name|prop
parameter_list|)
block|{
name|String
name|instName
init|=
name|prop
operator|.
name|getProperty
argument_list|(
name|StdSchedulerFactory
operator|.
name|PROP_SCHED_INSTANCE_NAME
argument_list|)
decl_stmt|;
comment|// camel context name will be a suffix to use one scheduler per context
name|String
name|identity
init|=
name|QuartzHelper
operator|.
name|getQuartzContextName
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|identity
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|instName
operator|==
literal|null
condition|)
block|{
name|instName
operator|=
literal|"scheduler-"
operator|+
name|identity
expr_stmt|;
block|}
else|else
block|{
name|instName
operator|=
name|instName
operator|+
literal|"-"
operator|+
name|identity
expr_stmt|;
block|}
block|}
return|return
name|instName
return|;
block|}
comment|/**      * Is the quartz scheduler clustered?      */
DECL|method|isClustered ()
specifier|public
name|boolean
name|isClustered
parameter_list|()
throws|throws
name|SchedulerException
block|{
return|return
name|getScheduler
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|isJobStoreClustered
argument_list|()
return|;
block|}
DECL|method|loadProperties ()
specifier|private
name|Properties
name|loadProperties
parameter_list|()
throws|throws
name|SchedulerException
block|{
name|Properties
name|answer
init|=
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|getPropertiesFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Loading Quartz properties file from: {}"
argument_list|,
name|getPropertiesFile
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|getPropertiesFile
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|answer
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SchedulerException
argument_list|(
literal|"Error loading Quartz properties file: "
operator|+
name|getPropertiesFile
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * To use the custom SchedulerFactory which is used to create the Scheduler.      */
DECL|method|setSchedulerFactory (SchedulerFactory schedulerFactory)
specifier|public
name|void
name|setSchedulerFactory
parameter_list|(
name|SchedulerFactory
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
name|Scheduler
name|getScheduler
parameter_list|()
block|{
return|return
name|scheduler
return|;
block|}
comment|/**      * To use the custom configured Quartz scheduler, instead of creating a new Scheduler.      */
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
comment|// Get couple of scheduler settings
name|Integer
name|startDelayedSeconds
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"startDelayedSeconds"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|startDelayedSeconds
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|startDelayedSeconds
operator|!=
literal|0
operator|&&
operator|!
operator|(
name|this
operator|.
name|startDelayedSeconds
operator|==
name|startDelayedSeconds
operator|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"A Quartz job is already configured with a different 'startDelayedSeconds' configuration! "
operator|+
literal|"All Quartz jobs must share the same 'startDelayedSeconds' configuration! Cannot apply the 'startDelayedSeconds' configuration!"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|startDelayedSeconds
operator|=
name|startDelayedSeconds
expr_stmt|;
block|}
block|}
name|Boolean
name|autoStartScheduler
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"autoStartScheduler"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|autoStartScheduler
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|autoStartScheduler
operator|=
name|autoStartScheduler
expr_stmt|;
block|}
name|Boolean
name|prefixJobNameWithEndpointId
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"prefixJobNameWithEndpointId"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefixJobNameWithEndpointId
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|prefixJobNameWithEndpointId
operator|=
name|prefixJobNameWithEndpointId
expr_stmt|;
block|}
comment|// Extract trigger.XXX and job.XXX properties to be set on endpoint below
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|triggerParameters
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"trigger."
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jobParameters
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"job."
argument_list|)
decl_stmt|;
comment|// Create quartz endpoint
name|QuartzEndpoint
name|result
init|=
operator|new
name|QuartzEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
name|createTriggerKey
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|result
operator|.
name|setTriggerKey
argument_list|(
name|triggerKey
argument_list|)
expr_stmt|;
name|result
operator|.
name|setTriggerParameters
argument_list|(
name|triggerParameters
argument_list|)
expr_stmt|;
name|result
operator|.
name|setJobParameters
argument_list|(
name|jobParameters
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|createTriggerKey (String uri, String remaining, QuartzEndpoint endpoint)
specifier|private
name|TriggerKey
name|createTriggerKey
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|QuartzEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Parse uri for trigger name and group
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|u
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|u
operator|.
name|getHost
argument_list|()
decl_stmt|;
comment|// host can be null if the uri did contain invalid host characters such as an underscore
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
name|host
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
name|host
operator|=
name|remaining
expr_stmt|;
block|}
block|}
comment|// Trigger group can be optional, if so set it to this context's unique name
name|String
name|name
decl_stmt|;
name|String
name|group
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|path
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
name|group
operator|=
name|host
expr_stmt|;
name|name
operator|=
name|path
expr_stmt|;
block|}
else|else
block|{
name|String
name|camelContextName
init|=
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
decl_stmt|;
name|group
operator|=
name|camelContextName
operator|==
literal|null
condition|?
literal|"Camel"
else|:
literal|"Camel_"
operator|+
name|camelContextName
expr_stmt|;
name|name
operator|=
name|host
expr_stmt|;
block|}
if|if
condition|(
name|prefixJobNameWithEndpointId
condition|)
block|{
name|name
operator|=
name|endpoint
operator|.
name|getId
argument_list|()
operator|+
literal|"_"
operator|+
name|name
expr_stmt|;
block|}
return|return
operator|new
name|TriggerKey
argument_list|(
name|name
argument_list|,
name|group
argument_list|)
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
name|createAndInitScheduler
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createAndInitScheduler ()
specifier|private
name|void
name|createAndInitScheduler
parameter_list|()
throws|throws
name|SchedulerException
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Create and initializing scheduler."
argument_list|)
expr_stmt|;
name|scheduler
operator|=
name|createScheduler
argument_list|()
expr_stmt|;
comment|// Store CamelContext into QuartzContext space
name|SchedulerContext
name|quartzContext
init|=
name|scheduler
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|String
name|camelContextName
init|=
name|QuartzHelper
operator|.
name|getQuartzContextName
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Storing camelContextName={} into Quartz Context space."
argument_list|,
name|camelContextName
argument_list|)
expr_stmt|;
name|quartzContext
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_CONTEXT
operator|+
literal|"-"
operator|+
name|camelContextName
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set camel job counts to zero. We needed this to prevent shutdown in case there are multiple Camel contexts
comment|// that has not completed yet, and the last one with job counts to zero will eventually shutdown.
name|AtomicInteger
name|number
init|=
operator|(
name|AtomicInteger
operator|)
name|quartzContext
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_JOBS_COUNT
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|==
literal|null
condition|)
block|{
name|number
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|quartzContext
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_JOBS_COUNT
argument_list|,
name|number
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createScheduler ()
specifier|private
name|Scheduler
name|createScheduler
parameter_list|()
throws|throws
name|SchedulerException
block|{
return|return
name|getSchedulerFactory
argument_list|()
operator|.
name|getScheduler
argument_list|()
return|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|scheduler
operator|!=
literal|null
condition|)
block|{
name|AtomicInteger
name|number
init|=
operator|(
name|AtomicInteger
operator|)
name|scheduler
operator|.
name|getContext
argument_list|()
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_JOBS_COUNT
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|!=
literal|null
operator|&&
name|number
operator|.
name|get
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Cannot shutdown scheduler: "
operator|+
name|scheduler
operator|.
name|getSchedulerName
argument_list|()
operator|+
literal|" as there are still "
operator|+
name|number
operator|.
name|get
argument_list|()
operator|+
literal|" jobs registered."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutting down scheduler. (will wait for all jobs to complete first.)"
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|scheduler
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onCamelContextStarted (CamelContext context, boolean alreadyStarted)
specifier|public
name|void
name|onCamelContextStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|alreadyStarted
parameter_list|)
throws|throws
name|Exception
block|{
comment|// If Camel has already started and then user add a route dynamically, we need to ensure
comment|// to create and init the scheduler first.
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
name|createAndInitScheduler
argument_list|()
expr_stmt|;
block|}
comment|// Now scheduler is ready, let see how we should start it.
if|if
condition|(
operator|!
name|autoStartScheduler
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Not starting scheduler because autoStartScheduler is set to false."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|startDelayedSeconds
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|scheduler
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The scheduler has already started. Cannot apply the 'startDelayedSeconds' configuration!"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting scheduler with startDelayedSeconds={}"
argument_list|,
name|startDelayedSeconds
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|startDelayed
argument_list|(
name|startDelayedSeconds
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|scheduler
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The scheduler has already been started."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting scheduler."
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

