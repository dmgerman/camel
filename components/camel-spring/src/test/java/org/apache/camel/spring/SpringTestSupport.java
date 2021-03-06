begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|ContextTestSupport
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
name|impl
operator|.
name|engine
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
DECL|class|SpringTestSupport
specifier|public
specifier|abstract
class|class
name|SpringTestSupport
extends|extends
name|ContextTestSupport
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
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we want SpringTestSupport to startup faster and not use JMX by default and should stop seda quicker
name|System
operator|.
name|setProperty
argument_list|(
literal|"CamelSedaPollTimeout"
argument_list|,
literal|"10"
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
name|Boolean
operator|.
name|toString
argument_list|(
operator|!
name|useJmx
argument_list|()
argument_list|)
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
block|}
annotation|@
name|After
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
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
name|IOHelper
operator|.
name|close
argument_list|(
name|applicationContext
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
if|if
condition|(
name|excludedClasses
operator|==
literal|null
condition|)
block|{
name|excludedClasses
operator|=
name|Collections
operator|.
name|emptySet
argument_list|()
expr_stmt|;
block|}
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
name|Arrays
operator|.
name|asList
argument_list|(
name|excludeRoutes
argument_list|()
argument_list|)
decl_stmt|;
name|excludingResolver
operator|.
name|setExcludedClasses
argument_list|(
operator|new
name|HashSet
argument_list|<>
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
name|T
name|value
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|name
argument_list|,
name|type
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
return|return
name|value
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
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
name|CamelContext
name|context
init|=
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

