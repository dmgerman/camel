begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Route
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
name|DefaultPackageScanClassResolver
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
name|scan
operator|.
name|AssignableToPackageScanFilter
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
name|scan
operator|.
name|InvertingPackageScanFilter
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
name|CamelBeanPostProcessor
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
name|util
operator|.
name|CastUtils
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
name|support
operator|.
name|AbstractApplicationContext
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CamelSpringTestSupport
specifier|public
specifier|abstract
class|class
name|CamelSpringTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|applicationContext
specifier|protected
name|AbstractApplicationContext
name|applicationContext
decl_stmt|;
DECL|method|createApplicationContext ()
specifier|protected
specifier|abstract
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
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
condition|)
block|{
comment|// tell camel-spring it should not trigger starting CamelContext, since we do that later
comment|// after we are finished setting up the unit test
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|applicationContext
operator|=
name|createApplicationContext
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have created a valid spring context"
argument_list|,
name|applicationContext
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Skipping starting CamelContext as system property skipStartingCamelContext is set to be true."
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|postProcessTest ()
specifier|protected
name|void
name|postProcessTest
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use the bean post processor from camel-spring
name|CamelBeanPostProcessor
name|processor
init|=
operator|new
name|CamelBeanPostProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|processor
operator|.
name|postProcessBeforeInitialization
argument_list|(
name|this
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|processor
operator|.
name|postProcessAfterInitialization
argument_list|(
name|this
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|ExcludingPackageScanClassResolver
specifier|private
specifier|static
class|class
name|ExcludingPackageScanClassResolver
extends|extends
name|DefaultPackageScanClassResolver
block|{
DECL|method|setExcludedClasses (Set<Class<?>> excludedClasses)
specifier|public
name|void
name|setExcludedClasses
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|excludedClasses
parameter_list|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|parents
init|=
name|excludedClasses
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
else|:
name|excludedClasses
decl_stmt|;
name|addFilter
argument_list|(
operator|new
name|InvertingPackageScanFilter
argument_list|(
operator|new
name|AssignableToPackageScanFilter
argument_list|(
name|parents
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a parent context that initializes a      * {@link org.apache.camel.spi.PackageScanClassResolver} to exclude a set of given classes from      * being resolved. Typically this is used at test time to exclude certain routes,      * which might otherwise be just noisy, from being discovered and initialized.      *<p/>      * To use this filtering mechanism it is necessary to provide the      * {@link ApplicationContext} returned from here as the parent context to      * your test context e.g.      *      *<pre>      * protected AbstractXmlApplicationContext createApplicationContext() {      *     return new ClassPathXmlApplicationContext(new String[] {&quot;test-context.xml&quot;}, getRouteExcludingApplicationContext());      * }      *</pre>      *      * This will, in turn, call the template methods<code>excludedRoutes</code>      * and<code>excludedRoute</code> to determine the classes to be excluded from scanning.      *      * @return ApplicationContext a parent {@link ApplicationContext} configured      *         to exclude certain classes from package scanning      */
DECL|method|getRouteExcludingApplicationContext ()
specifier|protected
name|ApplicationContext
name|getRouteExcludingApplicationContext
parameter_list|()
block|{
name|GenericApplicationContext
name|routeExcludingContext
init|=
operator|new
name|GenericApplicationContext
argument_list|()
decl_stmt|;
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
name|excludeRoutes
argument_list|()
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
return|return
name|routeExcludingContext
return|;
block|}
comment|/**      * Template method used to exclude {@link org.apache.camel.Route} from the test time context      * route scanning      *      * @return Class[] the classes to be excluded from test time context route scanning      */
DECL|method|excludeRoutes ()
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|excludeRoutes
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|excludedRoute
init|=
name|excludeRoute
argument_list|()
decl_stmt|;
return|return
name|excludedRoute
operator|!=
literal|null
condition|?
operator|new
name|Class
index|[]
block|{
name|excludedRoute
block|}
else|:
operator|new
name|Class
index|[
literal|0
index|]
return|;
block|}
comment|/**      * Template method used to exclude a {@link org.apache.camel.Route} from the test camel context      */
DECL|method|excludeRoute ()
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|excludeRoute
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Looks up the mandatory spring bean of the given name and type, failing if      * it is not present or the correct type      */
DECL|method|getMandatoryBean (Class<T> type, String name)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryBean
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|applicationContext
operator|.
name|getBean
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|assertValidContext (CamelContext context)
specifier|protected
name|void
name|assertValidContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
operator|.
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|context
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|int
name|routeCount
init|=
name|getExpectedRouteCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|routeCount
operator|>
literal|0
condition|)
block|{
name|assertNotNull
argument_list|(
literal|"Should have some routes defined"
argument_list|,
name|routes
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have at least one route"
argument_list|,
name|routes
operator|.
name|size
argument_list|()
operator|>=
name|routeCount
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Camel Routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
block|}
DECL|method|getExpectedRouteCount ()
specifier|protected
name|int
name|getExpectedRouteCount
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

