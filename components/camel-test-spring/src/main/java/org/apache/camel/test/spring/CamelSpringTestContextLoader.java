begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
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
name|Arrays
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
name|HashSet
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
name|Set
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|impl
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
name|ExcludingPackageScanClassResolver
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
name|spring
operator|.
name|CamelSpringTestHelper
operator|.
name|DoToSpringCamelContextsStrategy
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
name|CastUtils
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
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|RootBeanDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|XmlBeanDefinitionReader
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
name|annotation
operator|.
name|AnnotationConfigUtils
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
name|support
operator|.
name|GenericApplicationContext
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
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|MergedContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractContextLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractGenericContextLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|support
operator|.
name|GenericXmlContextLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Replacement for the default {@link GenericXmlContextLoader} that provides hooks for  * processing some class level Camel related test annotations.  */
end_comment

begin_class
DECL|class|CamelSpringTestContextLoader
specifier|public
class|class
name|CamelSpringTestContextLoader
extends|extends
name|AbstractContextLoader
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
name|CamelSpringTestContextLoader
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      *  Modeled after the Spring implementation in {@link AbstractGenericContextLoader},      *  this method creates and refreshes the application context while providing for      *  processing of additional Camel specific post-refresh actions.  We do not provide the      *  pre-post hooks for customization seen in {@link AbstractGenericContextLoader} because      *  they probably are unnecessary for 90+% of users.      *<p/>      *  For some functionality, we cannot use {@link TestExecutionListener} because we need      *  to both produce the desired outcome during application context loading, and also cleanup      *  after ourselves even if the test class never executes.  Thus the listeners, which      *  only run if the application context is successfully initialized are insufficient to      *  provide the behavior described above.      */
annotation|@
name|Override
DECL|method|loadContext (MergedContextConfiguration mergedConfig)
specifier|public
name|ApplicationContext
name|loadContext
parameter_list|(
name|MergedContextConfiguration
name|mergedConfig
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
init|=
name|getTestClass
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
name|String
operator|.
name|format
argument_list|(
literal|"Loading ApplicationContext for merged context configuration [%s]."
argument_list|,
name|mergedConfig
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|GenericApplicationContext
name|context
init|=
name|createContext
argument_list|(
name|testClass
argument_list|)
decl_stmt|;
name|context
operator|.
name|getEnvironment
argument_list|()
operator|.
name|setActiveProfiles
argument_list|(
name|mergedConfig
operator|.
name|getActiveProfiles
argument_list|()
argument_list|)
expr_stmt|;
name|loadBeanDefinitions
argument_list|(
name|context
argument_list|,
name|mergedConfig
argument_list|)
expr_stmt|;
return|return
name|loadContext
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
return|;
block|}
finally|finally
block|{
name|cleanup
argument_list|(
name|testClass
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *  Modeled after the Spring implementation in {@link AbstractGenericContextLoader},      *  this method creates and refreshes the application context while providing for      *  processing of additional Camel specific post-refresh actions.  We do not provide the      *  pre-post hooks for customization seen in {@link AbstractGenericContextLoader} because      *  they probably are unnecessary for 90+% of users.      *<p/>      *  For some functionality, we cannot use {@link TestExecutionListener} because we need      *  to both produce the desired outcome during application context loading, and also cleanup      *  after ourselves even if the test class never executes.  Thus the listeners, which      *  only run if the application context is successfully initialized are insufficient to      *  provide the behavior described above.      */
annotation|@
name|Override
DECL|method|loadContext (String... locations)
specifier|public
name|ApplicationContext
name|loadContext
parameter_list|(
name|String
modifier|...
name|locations
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
init|=
name|getTestClass
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
literal|"Loading ApplicationContext for locations ["
operator|+
name|StringUtils
operator|.
name|arrayToCommaDelimitedString
argument_list|(
name|locations
argument_list|)
operator|+
literal|"]."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|GenericApplicationContext
name|context
init|=
name|createContext
argument_list|(
name|testClass
argument_list|)
decl_stmt|;
name|loadBeanDefinitions
argument_list|(
name|context
argument_list|,
name|locations
argument_list|)
expr_stmt|;
return|return
name|loadContext
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
return|;
block|}
finally|finally
block|{
name|cleanup
argument_list|(
name|testClass
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns&quot;<code>-context.xml</code>&quot;.      */
annotation|@
name|Override
DECL|method|getResourceSuffix ()
specifier|public
name|String
name|getResourceSuffix
parameter_list|()
block|{
return|return
literal|"-context.xml"
return|;
block|}
comment|/**      * Performs the bulk of the Spring application context loading/customization.      *      * @param context the partially configured context.  The context should have the bean definitions loaded, but nothing else.      * @param testClass the test class being executed      *      * @return the initialized (refreshed) Spring application context      *      * @throws Exception if there is an error during initialization/customization      */
DECL|method|loadContext (GenericApplicationContext context, Class<?> testClass)
specifier|protected
name|ApplicationContext
name|loadContext
parameter_list|(
name|GenericApplicationContext
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
name|AnnotationConfigUtils
operator|.
name|registerAnnotationConfigProcessors
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// Pre CamelContext(s) instantiation setup
name|handleDisableJmx
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
comment|// Temporarily disable CamelContext start while the contexts are instantiated.
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|context
operator|.
name|registerShutdownHook
argument_list|()
expr_stmt|;
comment|// Turn CamelContext startup back on since the context's have now been instantiated.
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Post CamelContext(s) instantiation but pre CamelContext(s) start setup
name|handleProvidesBreakpoint
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|handleShutdownTimeout
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|handleMockEndpoints
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|handleLazyLoadTypeConverters
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
comment|// CamelContext(s) startup
name|handleCamelContextStartup
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * Cleanup/restore global state to defaults / pre-test values after the test setup      * is complete.       *       * @param testClass the test class being executed      */
DECL|method|cleanup (Class<?> testClass)
specifier|protected
name|void
name|cleanup
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
block|{
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|false
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
name|CamelSpringTestHelper
operator|.
name|getOriginalJmxDisabled
argument_list|()
operator|==
literal|null
condition|)
block|{
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
else|else
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|,
name|CamelSpringTestHelper
operator|.
name|getOriginalJmxDisabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|loadBeanDefinitions (GenericApplicationContext context, MergedContextConfiguration mergedConfig)
specifier|protected
name|void
name|loadBeanDefinitions
parameter_list|(
name|GenericApplicationContext
name|context
parameter_list|,
name|MergedContextConfiguration
name|mergedConfig
parameter_list|)
block|{
operator|(
operator|new
name|XmlBeanDefinitionReader
argument_list|(
name|context
argument_list|)
operator|)
operator|.
name|loadBeanDefinitions
argument_list|(
name|mergedConfig
operator|.
name|getLocations
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|loadBeanDefinitions (GenericApplicationContext context, String... locations)
specifier|protected
name|void
name|loadBeanDefinitions
parameter_list|(
name|GenericApplicationContext
name|context
parameter_list|,
name|String
modifier|...
name|locations
parameter_list|)
block|{
operator|(
operator|new
name|XmlBeanDefinitionReader
argument_list|(
name|context
argument_list|)
operator|)
operator|.
name|loadBeanDefinitions
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns all methods defined in {@code clazz} and its superclasses/interfaces.      */
DECL|method|getAllMethods (Class<?> clazz)
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|getAllMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|Set
argument_list|<
name|Method
argument_list|>
name|methods
init|=
operator|new
name|HashSet
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|currentClass
init|=
name|clazz
decl_stmt|;
while|while
condition|(
name|currentClass
operator|!=
literal|null
condition|)
block|{
name|methods
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|clazz
operator|.
name|getMethods
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|currentClass
operator|=
name|currentClass
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
return|return
name|methods
return|;
block|}
comment|/**      * Creates and starts the Spring context while optionally starting any loaded Camel contexts.      *      * @param testClass the test class that is being executed      *      * @return the loaded Spring context      */
DECL|method|createContext (Class<?> testClass)
specifier|protected
name|GenericApplicationContext
name|createContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
block|{
name|GenericApplicationContext
name|routeExcludingContext
init|=
literal|null
decl_stmt|;
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
argument_list|<
name|?
argument_list|>
index|[]
name|excludedClasses
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
if|if
condition|(
name|excludedClasses
operator|.
name|length
operator|>
literal|0
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
literal|"Setting up package scanning excluded classes as ExcludeRoutes "
operator|+
literal|"annotation was found.  Excluding ["
operator|+
name|StringUtils
operator|.
name|arrayToCommaDelimitedString
argument_list|(
name|excludedClasses
argument_list|)
operator|+
literal|"]."
argument_list|)
expr_stmt|;
block|}
name|routeExcludingContext
operator|=
operator|new
name|GenericApplicationContext
argument_list|()
expr_stmt|;
name|routeExcludingContext
operator|.
name|registerBeanDefinition
argument_list|(
literal|"excludingResolver"
argument_list|,
operator|new
name|RootBeanDefinition
argument_list|(
name|ExcludingPackageScanClassResolver
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|routeExcludingContext
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|ExcludingPackageScanClassResolver
name|excludingResolver
init|=
name|routeExcludingContext
operator|.
name|getBean
argument_list|(
literal|"excludingResolver"
argument_list|,
name|ExcludingPackageScanClassResolver
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|excluded
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|excludedClasses
argument_list|)
argument_list|)
decl_stmt|;
name|excludingResolver
operator|.
name|setExcludedClasses
argument_list|(
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|excluded
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
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
literal|"Not enabling package scanning excluded classes as ExcludeRoutes "
operator|+
literal|"annotation was found but no classes were excluded."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|GenericApplicationContext
name|context
decl_stmt|;
if|if
condition|(
name|routeExcludingContext
operator|!=
literal|null
condition|)
block|{
name|context
operator|=
operator|new
name|GenericApplicationContext
argument_list|(
name|routeExcludingContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|=
operator|new
name|GenericApplicationContext
argument_list|()
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
comment|/**      * Handles disabling of JMX on Camel contexts based on {@link DisableJmx}.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleDisableJmx (GenericApplicationContext context, Class<?> testClass)
specifier|protected
name|void
name|handleDisableJmx
parameter_list|(
name|GenericApplicationContext
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Disabling Camel JMX globally as DisableJmx annotation was found "
operator|+
literal|"and disableJmx is set to true."
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Enabling Camel JMX as DisableJmx annotation was found "
operator|+
literal|"and disableJmx is set to false."
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Disabling Camel JMX globally for tests by default.  Use the DisableJMX annotation to "
operator|+
literal|"override the default setting."
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
comment|/**      * Handles the processing of the {@link ProvidesBreakpoint} annotation on a test class.  Exists here      * as it is needed in       *      * @param context the initialized Spring context containing the Camel context(s) to insert breakpoints into       * @param testClass the test class being processed      * @param log the logger to use      * @param statics if static methods or instance methods should be processed      *      * @throws Exception if there is an error processing the class      */
DECL|method|handleProvidesBreakpoint (GenericApplicationContext context, Class<?> testClass)
specifier|protected
name|void
name|handleProvidesBreakpoint
parameter_list|(
name|GenericApplicationContext
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
argument_list|<
name|Breakpoint
argument_list|>
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
argument_list|,
operator|new
name|Object
index|[]
block|{}
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
name|LOG
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
DECL|method|handleShutdownTimeout (GenericApplicationContext context, Class<?> testClass)
specifier|protected
name|void
name|handleShutdownTimeout
parameter_list|(
name|GenericApplicationContext
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Setting shutdown timeout to [{} {}] on CamelContext with name [{}]."
argument_list|,
operator|new
name|Object
index|[]
block|{
name|shutdownTimeout
block|,
name|shutdownTimeUnit
block|,
name|contextName
block|}
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
DECL|method|handleMockEndpoints (GenericApplicationContext context, Class<?> testClass)
specifier|protected
name|void
name|handleMockEndpoints
parameter_list|(
name|GenericApplicationContext
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Enabling auto mocking of endpoints matching pattern [{}] on "
operator|+
literal|"CamelContext with name [{}]."
argument_list|,
name|mockEndpoints
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addRegisterEndpointCallback
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|handleLazyLoadTypeConverters (GenericApplicationContext context, Class<?> testClass)
specifier|protected
name|void
name|handleLazyLoadTypeConverters
parameter_list|(
name|GenericApplicationContext
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
name|boolean
name|lazy
decl_stmt|;
if|if
condition|(
name|testClass
operator|.
name|isAnnotationPresent
argument_list|(
name|LazyLoadTypeConverters
operator|.
name|class
argument_list|)
condition|)
block|{
name|lazy
operator|=
name|testClass
operator|.
name|getAnnotation
argument_list|(
name|LazyLoadTypeConverters
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|lazy
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|lazy
condition|)
block|{
name|CamelSpringTestHelper
operator|.
name|doToSpringCamelContexts
argument_list|(
name|context
argument_list|,
operator|new
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Enabling lazy loading of type converters on "
operator|+
literal|"CamelContext with name [{}]."
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setLazyLoadTypeConverters
argument_list|(
name|lazy
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Handles starting of Camel contexts based on {@link UseAdviceWith} and other state in the JVM.      *      * @param context the initialized Spring context      * @param testClass the test class being executed      */
DECL|method|handleCamelContextStartup (GenericApplicationContext context, Class<?> testClass)
specifier|protected
name|void
name|handleCamelContextStartup
parameter_list|(
name|GenericApplicationContext
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Skipping starting CamelContext(s) as system property "
operator|+
literal|"skipStartingCamelContext is set to be true."
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Skipping starting CamelContext(s) as UseAdviceWith annotation was found "
operator|+
literal|"and isUseAdviceWith is set to true."
argument_list|)
expr_stmt|;
name|skip
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting CamelContext(s) as UseAdviceWith annotation was found, but "
operator|+
literal|"isUseAdviceWith is set to false."
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
name|LOG
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
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the class under test in order to enable inspection of annotations while the      * Spring context is being created.      *       * @return the test class that is being executed      * @see CamelSpringTestHelper      */
DECL|method|getTestClass ()
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getTestClass
parameter_list|()
block|{
return|return
name|CamelSpringTestHelper
operator|.
name|getTestClass
argument_list|()
return|;
block|}
block|}
end_class

end_unit

