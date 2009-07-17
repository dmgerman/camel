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
name|ObjectHelper
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
name|AbstractXmlApplicationContext
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
comment|/**  * @version $Revision$  */
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
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
DECL|method|createApplicationContext ()
specifier|protected
specifier|abstract
name|AbstractXmlApplicationContext
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|ExcludingPackageScanClassResolver
specifier|private
specifier|static
class|class
name|ExcludingPackageScanClassResolver
extends|extends
name|DefaultPackageScanClassResolver
block|{
DECL|method|setExcludedClasses (Set<Class> excludedClasses)
specifier|public
name|void
name|setExcludedClasses
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|>
name|excludedClasses
parameter_list|)
block|{
name|excludedClasses
operator|=
name|excludedClasses
operator|==
literal|null
condition|?
name|Collections
operator|.
name|EMPTY_SET
else|:
name|excludedClasses
expr_stmt|;
name|addFilter
argument_list|(
operator|new
name|InvertingPackageScanFilter
argument_list|(
operator|new
name|AssignableToPackageScanFilter
argument_list|(
name|excludedClasses
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a parent context that initializes a      * {@link org.apache.camel.spi.PackageScanClassResolver} to exclude a set of given classes from      * being resolved. Typically this is used at test time to exclude certain routes,      * which might otherwise be just noisy, from being discovered and initialized.      *<p/>      * To use this filtering mechanism it is necessary to provide the      * {@link ApplicationContext} returned from here as the parent context to      * your test context e.g.      *      *<pre>      * protected AbstractXmlApplicationContext createApplicationContext() {      *     return new ClassPathXmlApplicationContext(new String[] {&quot;test-context.xml&quot;}, getRouteExcludingApplicationContext());      * }      *</pre>      *      * This will, in turn, call the template methods<code>excludedRoutes</code>      * and<code>excludedRoute</code> to determine the classes to be excluded from scanning.      *      * @see org.apache.camel.spring.config.scan.SpringComponentScanTest for an example.      * @return ApplicationContext a parent {@link ApplicationContext} configured      *         to exclude certain classes from package scanning      */
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
operator|(
name|ExcludingPackageScanClassResolver
operator|)
name|routeExcludingContext
operator|.
name|getBean
argument_list|(
literal|"excludingResolver"
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
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|excludeRoutes
argument_list|()
argument_list|)
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
index|[]
name|excludeRoutes
parameter_list|()
block|{
name|Class
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
name|Object
name|value
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No spring bean found for name<"
operator|+
name|name
operator|+
literal|">"
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Spring bean<"
operator|+
name|name
operator|+
literal|"> is not an instanceof "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" but is of type "
operator|+
name|ObjectHelper
operator|.
name|className
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
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

