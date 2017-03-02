begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|CountDownLatch
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
name|TimeUnit
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
name|AtomicBoolean
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
name|RoutesBuilder
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
name|main
operator|.
name|MainDurationEventNotifier
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
name|model
operator|.
name|RouteDefinition
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
name|model
operator|.
name|RoutesDefinition
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
name|model
operator|.
name|rest
operator|.
name|RestDefinition
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
name|model
operator|.
name|rest
operator|.
name|RestsDefinition
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
name|EventNotifier
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ConfigurableApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|event
operator|.
name|ContextRefreshedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * Collects routes and rests from the various sources (like Spring application context beans registry or opinionated  * classpath locations) and injects these into the Camel context.  */
end_comment

begin_class
DECL|class|RoutesCollector
specifier|public
class|class
name|RoutesCollector
implements|implements
name|ApplicationListener
argument_list|<
name|ContextRefreshedEvent
argument_list|>
block|{
comment|// Static collaborators
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
name|RoutesCollector
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Collaborators
DECL|field|applicationContext
specifier|private
specifier|final
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|camelContextConfigurations
specifier|private
specifier|final
name|List
argument_list|<
name|CamelContextConfiguration
argument_list|>
name|camelContextConfigurations
decl_stmt|;
DECL|field|configurationProperties
specifier|private
specifier|final
name|CamelConfigurationProperties
name|configurationProperties
decl_stmt|;
comment|// Constructors
DECL|method|RoutesCollector (ApplicationContext applicationContext, List<CamelContextConfiguration> camelContextConfigurations, CamelConfigurationProperties configurationProperties)
specifier|public
name|RoutesCollector
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|List
argument_list|<
name|CamelContextConfiguration
argument_list|>
name|camelContextConfigurations
parameter_list|,
name|CamelConfigurationProperties
name|configurationProperties
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
name|this
operator|.
name|camelContextConfigurations
operator|=
operator|new
name|ArrayList
argument_list|<
name|CamelContextConfiguration
argument_list|>
argument_list|(
name|camelContextConfigurations
argument_list|)
expr_stmt|;
name|this
operator|.
name|configurationProperties
operator|=
name|configurationProperties
expr_stmt|;
block|}
comment|// Overridden
annotation|@
name|Override
DECL|method|onApplicationEvent (ContextRefreshedEvent event)
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ContextRefreshedEvent
name|event
parameter_list|)
block|{
name|ApplicationContext
name|applicationContext
init|=
name|event
operator|.
name|getApplicationContext
argument_list|()
decl_stmt|;
comment|// only listen to context refresh of "my" applicationContext
if|if
condition|(
name|this
operator|.
name|applicationContext
operator|.
name|equals
argument_list|(
name|applicationContext
argument_list|)
condition|)
block|{
name|CamelContext
name|camelContext
init|=
name|event
operator|.
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// only add and start Camel if its stopped (initial state)
if|if
condition|(
name|camelContext
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Post-processing CamelContext bean: {}"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|RoutesBuilder
name|routesBuilder
range|:
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|RoutesBuilder
operator|.
name|class
argument_list|,
name|configurationProperties
operator|.
name|isIncludeNonSingletons
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|values
argument_list|()
control|)
block|{
comment|// filter out abstract classes
name|boolean
name|abs
init|=
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|routesBuilder
operator|.
name|getClass
argument_list|()
operator|.
name|getModifiers
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|abs
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Injecting following route into the CamelContext: {}"
argument_list|,
name|routesBuilder
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|routesBuilder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelSpringBootInitializationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
try|try
block|{
name|boolean
name|scan
init|=
operator|!
name|configurationProperties
operator|.
name|getXmlRoutes
argument_list|()
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scan
condition|)
block|{
name|loadXmlRoutes
argument_list|(
name|applicationContext
argument_list|,
name|camelContext
argument_list|,
name|configurationProperties
operator|.
name|getXmlRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|scanRests
init|=
operator|!
name|configurationProperties
operator|.
name|getXmlRests
argument_list|()
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scanRests
condition|)
block|{
name|loadXmlRests
argument_list|(
name|applicationContext
argument_list|,
name|camelContext
argument_list|,
name|configurationProperties
operator|.
name|getXmlRests
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CamelContextConfiguration
name|camelContextConfiguration
range|:
name|camelContextConfigurations
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CamelContextConfiguration found. Invoking beforeApplicationStart: {}"
argument_list|,
name|camelContextConfiguration
argument_list|)
expr_stmt|;
name|camelContextConfiguration
operator|.
name|beforeApplicationStart
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationProperties
operator|.
name|isMainRunController
argument_list|()
condition|)
block|{
name|CamelMainRunController
name|controller
init|=
operator|new
name|CamelMainRunController
argument_list|(
name|applicationContext
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurationProperties
operator|.
name|getDurationMaxMessages
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelSpringBoot will terminate after processing {} messages"
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxMessages
argument_list|()
argument_list|)
expr_stmt|;
comment|// register lifecycle so we can trigger to shutdown the JVM when maximum number of messages has been processed
name|EventNotifier
name|notifier
init|=
operator|new
name|MainDurationEventNotifier
argument_list|(
name|camelContext
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxMessages
argument_list|()
argument_list|,
name|controller
operator|.
name|getCompleted
argument_list|()
argument_list|,
name|controller
operator|.
name|getLatch
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// register our event notifier
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationProperties
operator|.
name|getDurationMaxSeconds
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelSpringBoot will terminate after {} seconds"
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|terminateMainControllerAfter
argument_list|(
name|camelContext
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxSeconds
argument_list|()
argument_list|,
name|controller
operator|.
name|getCompleted
argument_list|()
argument_list|,
name|controller
operator|.
name|getLatch
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// controller will start Camel
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting CamelMainRunController to ensure the main thread keeps running"
argument_list|)
expr_stmt|;
name|controller
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|applicationContext
operator|instanceof
name|ConfigurableApplicationContext
condition|)
block|{
name|ConfigurableApplicationContext
name|cac
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|applicationContext
decl_stmt|;
if|if
condition|(
name|configurationProperties
operator|.
name|getDurationMaxSeconds
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelSpringBoot will terminate after {} seconds"
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|terminateApplicationContext
argument_list|(
name|cac
argument_list|,
name|camelContext
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxSeconds
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationProperties
operator|.
name|getDurationMaxMessages
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// needed by MainDurationEventNotifier to signal when we have processed the max messages
specifier|final
name|AtomicBoolean
name|completed
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// register lifecycle so we can trigger to shutdown the JVM when maximum number of messages has been processed
name|EventNotifier
name|notifier
init|=
operator|new
name|MainDurationEventNotifier
argument_list|(
name|camelContext
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxMessages
argument_list|()
argument_list|,
name|completed
argument_list|,
name|latch
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// register our event notifier
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelSpringBoot will terminate after processing {} messages"
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxMessages
argument_list|()
argument_list|)
expr_stmt|;
name|terminateApplicationContext
argument_list|(
name|cac
argument_list|,
name|camelContext
argument_list|,
name|configurationProperties
operator|.
name|getDurationMaxMessages
argument_list|()
argument_list|,
name|latch
argument_list|)
expr_stmt|;
block|}
block|}
comment|// start camel manually
name|maybeStart
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CamelContextConfiguration
name|camelContextConfiguration
range|:
name|camelContextConfigurations
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CamelContextConfiguration found. Invoking afterApplicationStart: {}"
argument_list|,
name|camelContextConfiguration
argument_list|)
expr_stmt|;
name|camelContextConfiguration
operator|.
name|afterApplicationStart
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelSpringBootInitializationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Camel already started, not adding routes."
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignore ContextRefreshedEvent: {}"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|maybeStart (CamelContext camelContext)
specifier|private
name|void
name|maybeStart
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// for example from unit testing we want to start Camel later and not when Spring framework
comment|// publish a ContextRefreshedEvent
name|boolean
name|skip
init|=
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|skip
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Skipping starting CamelContext as system property skipStartingCamelContext is set to be true."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Helpers
DECL|method|loadXmlRoutes (ApplicationContext applicationContext, CamelContext camelContext, String directory)
specifier|private
name|void
name|loadXmlRoutes
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|directory
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Loading additional Camel XML routes from: {}"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
try|try
block|{
name|Resource
index|[]
name|xmlRoutes
init|=
name|applicationContext
operator|.
name|getResources
argument_list|(
name|directory
argument_list|)
decl_stmt|;
for|for
control|(
name|Resource
name|xmlRoute
range|:
name|xmlRoutes
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found XML route: {}"
argument_list|,
name|xmlRoute
argument_list|)
expr_stmt|;
name|RoutesDefinition
name|xmlDefinition
init|=
name|camelContext
operator|.
name|loadRoutesDefinition
argument_list|(
name|xmlRoute
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addRouteDefinitions
argument_list|(
name|xmlDefinition
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No XML routes found in {}. Skipping XML routes detection."
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|loadXmlRests (ApplicationContext applicationContext, CamelContext camelContext, String directory)
specifier|private
name|void
name|loadXmlRests
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|directory
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Loading additional Camel XML rests from: {}"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|Resource
index|[]
name|xmlRests
init|=
name|applicationContext
operator|.
name|getResources
argument_list|(
name|directory
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Resource
name|xmlRest
range|:
name|xmlRests
control|)
block|{
specifier|final
name|RestsDefinition
name|xmlDefinitions
init|=
name|camelContext
operator|.
name|loadRestsDefinition
argument_list|(
name|xmlRest
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addRestDefinitions
argument_list|(
name|xmlDefinitions
operator|.
name|getRests
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|RestDefinition
name|xmlDefinition
range|:
name|xmlDefinitions
operator|.
name|getRests
argument_list|()
control|)
block|{
specifier|final
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routeDefinitions
init|=
name|xmlDefinition
operator|.
name|asRouteDefinition
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addRouteDefinitions
argument_list|(
name|routeDefinitions
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No XML rests found in {}. Skipping XML rests detection."
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|terminateMainControllerAfter (final CamelContext camelContext, int seconds, final AtomicBoolean completed, final CountDownLatch latch)
specifier|private
name|void
name|terminateMainControllerAfter
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
name|int
name|seconds
parameter_list|,
specifier|final
name|AtomicBoolean
name|completed
parameter_list|,
specifier|final
name|CountDownLatch
name|latch
parameter_list|)
block|{
name|ScheduledExecutorService
name|executorService
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"CamelSpringBootTerminateTask"
argument_list|)
decl_stmt|;
name|Runnable
name|task
init|=
parameter_list|()
lambda|->
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelSpringBoot max seconds triggering shutdown of the JVM."
argument_list|)
expr_stmt|;
try|try
block|{
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error during stopping CamelContext"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|completed
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|executorService
operator|.
name|schedule
argument_list|(
name|task
argument_list|,
name|seconds
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|terminateApplicationContext (final ConfigurableApplicationContext applicationContext, final CamelContext camelContext, int seconds)
specifier|private
name|void
name|terminateApplicationContext
parameter_list|(
specifier|final
name|ConfigurableApplicationContext
name|applicationContext
parameter_list|,
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
name|int
name|seconds
parameter_list|)
block|{
name|ScheduledExecutorService
name|executorService
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"CamelSpringBootTerminateTask"
argument_list|)
decl_stmt|;
name|Runnable
name|task
init|=
parameter_list|()
lambda|->
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelSpringBoot max seconds triggering shutdown of the JVM."
argument_list|)
expr_stmt|;
comment|// we need to run a daemon thread to stop ourselves so this thread pool can be stopped nice also
operator|new
name|Thread
argument_list|(
name|applicationContext
operator|::
name|close
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
decl_stmt|;
name|executorService
operator|.
name|schedule
argument_list|(
name|task
argument_list|,
name|seconds
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|terminateApplicationContext (final ConfigurableApplicationContext applicationContext, final CamelContext camelContext, int messages, final CountDownLatch latch)
specifier|private
name|void
name|terminateApplicationContext
parameter_list|(
specifier|final
name|ConfigurableApplicationContext
name|applicationContext
parameter_list|,
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
name|int
name|messages
parameter_list|,
specifier|final
name|CountDownLatch
name|latch
parameter_list|)
block|{
name|ExecutorService
name|executorService
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"CamelSpringBootTerminateTask"
argument_list|)
decl_stmt|;
name|Runnable
name|task
init|=
parameter_list|()
lambda|->
block|{
try|try
block|{
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelSpringBoot max messages "
operator|+
name|messages
operator|+
literal|" triggering shutdown of the JVM."
argument_list|)
expr_stmt|;
comment|// we need to run a daemon thread to stop ourselves so this thread pool can be stopped nice also
operator|new
name|Thread
argument_list|(
name|applicationContext
operator|::
name|close
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
decl_stmt|;
name|executorService
operator|.
name|submit
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

