begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.javaconfig.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|javaconfig
operator|.
name|test
package|;
end_package

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
name|Arrays
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
name|config
operator|.
name|BeanPostProcessor
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
name|AnnotationConfigApplicationContext
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
name|test
operator|.
name|context
operator|.
name|ContextLoader
import|;
end_import

begin_comment
comment|/**  * Implementation of the {@link ContextLoader} strategy for creating a  * {@link org.springframework.context.annotation.AnnotationConfigApplicationContext} for a test's  * {@link org.springframework.test.context.ContextConfiguration&#064;ContextConfiguration}  *<p/>  *  * Example usage:<p/>  *<pre class="code">  *&#064;RunWith(SpringJUnit4ClassRunner.class)  *&#064;ContextConfiguration(locations = {"com.myco.TestDatabaseConfiguration", "com.myco.config"},  *                       loader = JavaConfigContextLoader.class)  * public MyTests { ... }  *</pre>  *<p/>  *  * Implementation note: At this time, due to restrictions in Java annotations and Spring's  * TestContext framework, locations of classes / packages must be specified as strings to  * the ContextConfiguration annotation.  It is understood that this has a detrimental effect  * on type safety, discoverability and refactoring, and for these reasons may change in  * future revisions, possibly with a customized version of the ContextConfiguration annotation  * that accepts an array of class literals to load.  *  * @see org.springframework.test.context.ContextConfiguration  * @deprecated Use org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader from  * camel-test-spring jar instead.  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|JavaConfigContextLoader
specifier|public
class|class
name|JavaConfigContextLoader
implements|implements
name|ContextLoader
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
comment|/**      * Simply returns the supplied<var>locations</var> unchanged.      *<p/>      *      * @param clazz the class with which the locations are associated: used to determine how to      *            process the supplied locations.      * @param locations the unmodified locations to use for loading the application context; can be      *            {@code null} or empty.      * @return an array of application context resource locations      * @see org.springframework.test.context.ContextLoader#processLocations(Class, String[])      */
DECL|method|processLocations (Class<?> clazz, String... locations)
specifier|public
name|String
index|[]
name|processLocations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
modifier|...
name|locations
parameter_list|)
block|{
return|return
name|locations
return|;
block|}
comment|/**      * Loads a new {@link ApplicationContext context} based on the supplied {@code locations},      * configures the context, and finally returns the context in fully<em>refreshed</em> state.      *<p/>      *      * Configuration locations are either fully-qualified class names or base package names. These      * locations will be given to a {@link AnnotationConfigApplicationContext} for configuration via the      * {@link AnnotationConfigApplicationContext#register(Class[])} and      * {@link AnnotationConfigApplicationContext#scan(String...)} methods.      *      * @param locations the locations to use to load the application context      * @return a new application context      * @throws IllegalArgumentException if any of<var>locations</var> are not valid fully-qualified      * Class or Package names      */
DECL|method|loadContext (String... locations)
specifier|public
name|ApplicationContext
name|loadContext
parameter_list|(
name|String
modifier|...
name|locations
parameter_list|)
block|{
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
literal|"Creating a JavaConfigApplicationContext for {}"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|locations
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|AnnotationConfigApplicationContext
name|context
init|=
operator|new
name|AnnotationConfigApplicationContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|configClasses
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|basePackages
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|location
range|:
name|locations
control|)
block|{
comment|// if the location refers to a class, use it. Otherwise assume it's a base package name
try|try
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|location
argument_list|)
decl_stmt|;
name|configClasses
operator|.
name|add
argument_list|(
name|aClass
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
if|if
condition|(
name|Package
operator|.
name|getPackage
argument_list|(
name|location
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"A non-existent class or package name was specified: [%s]"
argument_list|,
name|location
argument_list|)
argument_list|)
throw|;
block|}
name|basePackages
operator|.
name|add
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"Setting config classes to {}"
argument_list|,
name|configClasses
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Setting base packages to {}"
argument_list|,
name|basePackages
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|configClass
range|:
name|configClasses
control|)
block|{
name|context
operator|.
name|register
argument_list|(
name|configClass
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|basePackage
range|:
name|basePackages
control|)
block|{
name|context
operator|.
name|scan
argument_list|(
name|basePackage
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|refresh
argument_list|()
expr_stmt|;
comment|// Have to create a child context that implements BeanDefinitionRegistry
comment|// to pass to registerAnnotationConfigProcessors, since
comment|// JavaConfigApplicationContext does not
specifier|final
name|GenericApplicationContext
name|gac
init|=
operator|new
name|GenericApplicationContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|AnnotationConfigUtils
operator|.
name|registerAnnotationConfigProcessors
argument_list|(
name|gac
argument_list|)
expr_stmt|;
comment|// copy BeanPostProcessors to the child context
for|for
control|(
name|String
name|bppName
range|:
name|context
operator|.
name|getBeanFactory
argument_list|()
operator|.
name|getBeanNamesForType
argument_list|(
name|BeanPostProcessor
operator|.
name|class
argument_list|)
control|)
block|{
name|gac
operator|.
name|registerBeanDefinition
argument_list|(
name|bppName
argument_list|,
name|context
operator|.
name|getBeanFactory
argument_list|()
operator|.
name|getBeanDefinition
argument_list|(
name|bppName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|gac
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|gac
operator|.
name|registerShutdownHook
argument_list|()
expr_stmt|;
return|return
name|gac
return|;
block|}
block|}
end_class

end_unit

