begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring.junit5
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
operator|.
name|junit5
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
name|ExtendedCamelContext
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
name|api
operator|.
name|management
operator|.
name|JmxSystemPropertyKeys
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
name|api
operator|.
name|management
operator|.
name|ManagedCamelContext
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedCamelContextMBean
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
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
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
name|engine
operator|.
name|InterceptSendToMockEndpointStrategy
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
name|interceptor
operator|.
name|DefaultDebugger
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
name|Breakpoint
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
name|Debugger
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
name|spring
operator|.
name|SpringCamelContext
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
name|test
operator|.
name|junit5
operator|.
name|CamelTestSupport
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
name|CollectionStringBuffer
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
name|ConfigurableApplicationContext
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
name|annotation
operator|.
name|AnnotationUtils
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
operator|.
name|junit5
operator|.
name|CamelSpringTestHelper
operator|.
name|getAllMethods
import|;
end_import

begin_class
DECL|class|CamelAnnotationsHandler
specifier|public
specifier|final
class|class
name|CamelAnnotationsHandler
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelAnnotationsHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|CamelAnnotationsHandler ()
specifier|private
name|CamelAnnotationsHandler
parameter_list|()
block|{     }
comment|/**      * Handles @ExcludeRoutes to make it easier to exclude other routes when testing with Spring Boot.      *      * @param testClass the test class being executed      */
DECL|method|handleExcludeRoutesForSpringBoot (Class<?> testClass)
specifier|public
specifier|static
name|void
name|handleExcludeRoutesForSpringBoot
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
block|{
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|ExcludeRoutes
operator|.
name|class
argument_list|)
condition|)
block|{
name|Class
index|[]
name|routes
init|=
name|testClass
operator|.
name|getAnnotation
argument_list|(
name|ExcludeRoutes
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
comment|// need to setup this as a JVM system property
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
name|clazz
range|:
name|routes
control|)
block|{
name|csb
operator|.
name|append
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|key
init|=
literal|"CamelTestSpringExcludeRoutes"
decl_stmt|;
name|String
name|value
init|=
name|csb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|exists
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|exists
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Cannot use @ExcludeRoutes as JVM property "
operator|+
name|key
operator|+
literal|" has already been set."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"@ExcludeRoutes annotation found. Setting up JVM property {}={}"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Handles disabling of JMX on Camel contexts based on {@link DisableJmx}.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleDisableJmx (ConfigurableApplicationContext context, Class<?> testClass)
specifier|public
specifier|static
name|void
name|handleDisableJmx
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
block|{
name|CamelSpringTestHelper
operator|.
name|setOriginalJmxDisabledValue
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|DisableJmx
operator|.
name|class
argument_list|)
condition|)
block|{
if|if
condition|(
name|testClass
operator|.
name|getAnnotation
argument_list|(
name|DisableJmx
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Disabling Camel JMX globally as DisableJmx annotation was found and disableJmx is set to true."
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Enabling Camel JMX as DisableJmx annotation was found and disableJmx is set to false."
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Disabling Camel JMX globally for tests by default. Use the DisableJMX annotation to override the default setting."
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Handles disabling of JMX on Camel contexts based on {@link DisableJmx}.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleRouteCoverage (ConfigurableApplicationContext context, Class<?> testClass, Function testMethod)
specifier|public
specifier|static
name|void
name|handleRouteCoverage
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|,
name|Function
name|testMethod
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|EnableRouteCoverage
operator|.
name|class
argument_list|)
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|CamelTestSupport
operator|.
name|ROUTE_COVERAGE_ENABLED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|contextName
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Enabling RouteCoverage"
argument_list|)
expr_stmt|;
name|EventNotifier
name|notifier
init|=
operator|new
name|RouteCoverageEventNotifier
argument_list|(
name|testClass
operator|.
name|getName
argument_list|()
argument_list|,
name|testMethod
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addService
argument_list|(
name|notifier
argument_list|,
literal|true
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
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleRouteCoverageDump (ConfigurableApplicationContext context, Class<?> testClass, Function testMethod)
specifier|public
specifier|static
name|void
name|handleRouteCoverageDump
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|,
name|Function
name|testMethod
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|EnableRouteCoverage
operator|.
name|class
argument_list|)
condition|)
block|{
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|contextName
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Dumping RouteCoverage"
argument_list|)
expr_stmt|;
name|String
name|testMethodName
init|=
operator|(
name|String
operator|)
name|testMethod
operator|.
name|apply
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|RouteCoverageDumper
operator|.
name|dumpRouteCoverage
argument_list|(
name|camelContext
argument_list|,
name|testClass
operator|.
name|getName
argument_list|()
argument_list|,
name|testMethodName
argument_list|)
expr_stmt|;
comment|// reset JMX statistics
name|ManagedCamelContextMBean
name|managedCamelContext
init|=
name|camelContext
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getManagedCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|managedCamelContext
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Resetting JMX statistics for RouteCoverage"
argument_list|)
expr_stmt|;
name|managedCamelContext
operator|.
name|reset
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// turn off dumping one more time by removing the event listener (which would dump as well when Camel is stopping)
comment|// but this method was explicit invoked to dump such as from afterTest callbacks from JUnit.
name|RouteCoverageEventNotifier
name|eventNotifier
init|=
name|camelContext
operator|.
name|hasService
argument_list|(
name|RouteCoverageEventNotifier
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventNotifier
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|removeEventNotifier
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|removeService
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleProvidesBreakpoint (ConfigurableApplicationContext context, Class<?> testClass)
specifier|public
specifier|static
name|void
name|handleProvidesBreakpoint
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|Method
argument_list|>
name|methods
init|=
name|getAllMethods
argument_list|(
name|testClass
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Breakpoint
argument_list|>
name|breakpoints
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
name|AnnotationUtils
operator|.
name|findAnnotation
argument_list|(
name|method
argument_list|,
name|ProvidesBreakpoint
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|argTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|argTypes
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] is annotated with ProvidesBreakpoint but is not a no-argument method."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Breakpoint
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] is annotated with ProvidesBreakpoint but does not return a Breakpoint."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] is annotated with ProvidesBreakpoint but is not static."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] is annotated with ProvidesBreakpoint but is not public."
argument_list|)
throw|;
block|}
try|try
block|{
name|breakpoints
operator|.
name|add
argument_list|(
operator|(
name|Breakpoint
operator|)
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
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
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] threw exception during evaluation."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|breakpoints
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|contextName
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Debugger
name|debugger
init|=
name|camelContext
operator|.
name|getDebugger
argument_list|()
decl_stmt|;
if|if
condition|(
name|debugger
operator|==
literal|null
condition|)
block|{
name|debugger
operator|=
operator|new
name|DefaultDebugger
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|setDebugger
argument_list|(
name|debugger
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Breakpoint
name|breakpoint
range|:
name|breakpoints
control|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Adding Breakpoint [{}] to CamelContext with name [{}]."
argument_list|,
name|breakpoint
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|debugger
operator|.
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Handles updating shutdown timeouts on Camel contexts based on {@link ShutdownTimeout}.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleShutdownTimeout (ConfigurableApplicationContext context, Class<?> testClass)
specifier|public
specifier|static
name|void
name|handleShutdownTimeout
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|int
name|shutdownTimeout
decl_stmt|;
specifier|final
name|TimeUnit
name|shutdownTimeUnit
decl_stmt|;
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|ShutdownTimeout
operator|.
name|class
argument_list|)
condition|)
block|{
name|shutdownTimeout
operator|=
name|testClass
operator|.
name|getAnnotation
argument_list|(
name|ShutdownTimeout
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
expr_stmt|;
name|shutdownTimeUnit
operator|=
name|testClass
operator|.
name|getAnnotation
argument_list|(
name|ShutdownTimeout
operator|.
name|class
argument_list|)
operator|.
name|timeUnit
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|shutdownTimeout
operator|=
literal|10
expr_stmt|;
name|shutdownTimeUnit
operator|=
name|TimeUnit
operator|.
name|SECONDS
expr_stmt|;
block|}
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|contextName
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Setting shutdown timeout to [{} {}] on CamelContext with name [{}]."
argument_list|,
name|shutdownTimeout
argument_list|,
name|shutdownTimeUnit
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
name|shutdownTimeout
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeUnit
argument_list|(
name|shutdownTimeUnit
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Handles auto-intercepting of endpoints with mocks based on {@link MockEndpoints}.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleMockEndpoints (ConfigurableApplicationContext context, Class<?> testClass)
specifier|public
specifier|static
name|void
name|handleMockEndpoints
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|MockEndpoints
operator|.
name|class
argument_list|)
condition|)
block|{
specifier|final
name|String
name|mockEndpoints
init|=
name|testClass
operator|.
name|getAnnotation
argument_list|(
name|MockEndpoints
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|contextName
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Enabling auto mocking of endpoints matching pattern [{}] on CamelContext with name [{}]."
argument_list|,
name|mockEndpoints
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|registerEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
name|mockEndpoints
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Handles auto-intercepting of endpoints with mocks based on {@link MockEndpointsAndSkip} and skipping the      * original endpoint.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleMockEndpointsAndSkip (ConfigurableApplicationContext context, Class<?> testClass)
specifier|public
specifier|static
name|void
name|handleMockEndpointsAndSkip
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|MockEndpointsAndSkip
operator|.
name|class
argument_list|)
condition|)
block|{
specifier|final
name|String
name|mockEndpoints
init|=
name|testClass
operator|.
name|getAnnotation
argument_list|(
name|MockEndpointsAndSkip
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|contextName
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// resolve the property place holders of the mockEndpoints
name|String
name|mockEndpointsValue
init|=
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|mockEndpoints
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Enabling auto mocking and skipping of endpoints matching pattern [{}] on CamelContext with name [{}]."
argument_list|,
name|mockEndpointsValue
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|registerEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
name|mockEndpointsValue
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Handles override this method to include and override properties with the Camel {@link org.apache.camel.component.properties.PropertiesComponent}.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleUseOverridePropertiesWithPropertiesComponent (ConfigurableApplicationContext context, Class<?> testClass)
specifier|public
specifier|static
name|void
name|handleUseOverridePropertiesWithPropertiesComponent
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|Method
argument_list|>
name|methods
init|=
name|getAllMethods
argument_list|(
name|testClass
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Properties
argument_list|>
name|properties
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
name|AnnotationUtils
operator|.
name|findAnnotation
argument_list|(
name|method
argument_list|,
name|UseOverridePropertiesWithPropertiesComponent
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|argTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|argTypes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] is annotated with UseOverridePropertiesWithPropertiesComponent but is not a no-argument method."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Properties
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] is annotated with UseOverridePropertiesWithPropertiesComponent but does not return a java.util.Properties."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] is annotated with UseOverridePropertiesWithPropertiesComponent but is not static."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] is annotated with UseOverridePropertiesWithPropertiesComponent but is not public."
argument_list|)
throw|;
block|}
try|try
block|{
name|properties
operator|.
name|add
argument_list|(
operator|(
name|Properties
operator|)
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
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
literal|"Method ["
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"] threw exception during evaluation."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|properties
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|contextName
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|PropertiesComponent
name|pc
init|=
name|camelContext
operator|.
name|getComponent
argument_list|(
literal|"properties"
argument_list|,
name|PropertiesComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Properties
name|extra
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
for|for
control|(
name|Properties
name|prop
range|:
name|properties
control|)
block|{
name|extra
operator|.
name|putAll
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|extra
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Using {} properties to override any existing properties on the PropertiesComponent on CamelContext with name [{}]."
argument_list|,
name|extra
operator|.
name|size
argument_list|()
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|pc
operator|.
name|setOverrideProperties
argument_list|(
name|extra
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Handles starting of Camel contexts based on {@link UseAdviceWith} and other state in the JVM.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleCamelContextStartup (ConfigurableApplicationContext context, Class<?> testClass)
specifier|public
specifier|static
name|void
name|handleCamelContextStartup
parameter_list|(
name|ConfigurableApplicationContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
throws|throws
name|Exception
block|{
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
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Skipping starting CamelContext(s) as system property skipStartingCamelContext is set to be true."
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|UseAdviceWith
operator|.
name|class
argument_list|)
condition|)
block|{
if|if
condition|(
name|testClass
operator|.
name|getAnnotation
argument_list|(
name|UseAdviceWith
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Skipping starting CamelContext(s) as UseAdviceWith annotation was found and isUseAdviceWith is set to true."
argument_list|)
expr_stmt|;
name|skip
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Starting CamelContext(s) as UseAdviceWith annotation was found, but isUseAdviceWith is set to false."
argument_list|)
expr_stmt|;
name|skip
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|skip
condition|)
block|{
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|contextName
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|camelContext
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Starting CamelContext with name [{}]."
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"CamelContext with name [{}] already started."
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

