begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spring
operator|.
name|SpringCamelContext
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
name|BeanDefinitionRegistry
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
name|DelegatingSmartContextLoader
import|;
end_import

begin_comment
comment|/**  * CamelSpringDelegatingTestContextLoader which fixes issues in Camel's JavaConfigContextLoader. (adds support for Camel's test annotations)  *<br>  *<em>This loader can handle either classes or locations for configuring the context.</em>  *<br>  * NOTE: This TestContextLoader doesn't support the annotation of ExcludeRoutes now.  *  * @deprecated use {@link CamelSpringRunner} or {@link CamelSpringBootRunner} instead.  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|CamelSpringDelegatingTestContextLoader
specifier|public
class|class
name|CamelSpringDelegatingTestContextLoader
extends|extends
name|DelegatingSmartContextLoader
block|{
DECL|field|logger
specifier|protected
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
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
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Loading ApplicationContext for merged context configuration [{}]."
argument_list|,
name|mergedConfig
argument_list|)
expr_stmt|;
block|}
comment|// Pre CamelContext(s) instantiation setup
name|CamelAnnotationsHandler
operator|.
name|handleDisableJmx
argument_list|(
literal|null
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
try|try
block|{
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ConfigurableApplicationContext
name|context
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|super
operator|.
name|loadContext
argument_list|(
name|mergedConfig
argument_list|)
decl_stmt|;
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"skipStartingCamelContext"
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
comment|/**      * Performs the bulk of the Spring application context loading/customization.      *      * @param context the partially configured context.  The context should have the bean definitions loaded, but nothing else.      * @param testClass the test class being executed      * @return the initialized (refreshed) Spring application context      *      * @throws Exception if there is an error during initialization/customization      */
DECL|method|loadContext (ConfigurableApplicationContext context, Class<?> testClass)
specifier|public
name|ApplicationContext
name|loadContext
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
name|AnnotationConfigUtils
operator|.
name|registerAnnotationConfigProcessors
argument_list|(
operator|(
name|BeanDefinitionRegistry
operator|)
name|context
argument_list|)
expr_stmt|;
comment|// Post CamelContext(s) instantiation but pre CamelContext(s) start setup
name|CamelAnnotationsHandler
operator|.
name|handleRouteCoverage
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|,
name|s
lambda|->
name|getTestMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleProvidesBreakpoint
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleShutdownTimeout
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleMockEndpoints
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleMockEndpointsAndSkip
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleUseOverridePropertiesWithPropertiesComponent
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
comment|// CamelContext(s) startup
name|CamelAnnotationsHandler
operator|.
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
comment|/**      * Returns the test method under test.      *      * @return the method that is being executed      * @see CamelSpringTestHelper      */
DECL|method|getTestMethod ()
specifier|protected
name|Method
name|getTestMethod
parameter_list|()
block|{
return|return
name|CamelSpringTestHelper
operator|.
name|getTestMethod
argument_list|()
return|;
block|}
block|}
end_class

end_unit

