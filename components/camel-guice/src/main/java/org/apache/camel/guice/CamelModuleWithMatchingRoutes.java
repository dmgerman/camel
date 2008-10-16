begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
package|;
end_package

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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provides
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|matcher
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|matcher
operator|.
name|Matchers
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
name|Routes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|guiceyfruit
operator|.
name|Injectors
import|;
end_import

begin_comment
comment|/**  * A Guice Module which injects the CamelContext with all available implementations  * of {@link Routes} which are bound to Guice with an optional {@link Matcher} to filter out the classes required.  *<p>  * Or if you would like to specify exactly which {@link Routes} to bind then use the {@link CamelModule} and create a provider  * method annotated with @Provides and returning Set<Routes> such as  *<code><pre>  * public class MyModule extends CamelModule {  *&#64;Provides  *   Set&lt;Routes&gt; routes(Injector injector) { ... }  * }  *</pre></code>  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|CamelModuleWithMatchingRoutes
specifier|public
class|class
name|CamelModuleWithMatchingRoutes
extends|extends
name|CamelModule
block|{
DECL|field|matcher
specifier|private
specifier|final
name|Matcher
argument_list|<
name|Class
argument_list|>
name|matcher
decl_stmt|;
DECL|method|CamelModuleWithMatchingRoutes ()
specifier|public
name|CamelModuleWithMatchingRoutes
parameter_list|()
block|{
name|this
argument_list|(
name|Matchers
operator|.
name|subclassesOf
argument_list|(
name|Routes
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelModuleWithMatchingRoutes (Matcher<Class> matcher)
specifier|public
name|CamelModuleWithMatchingRoutes
parameter_list|(
name|Matcher
argument_list|<
name|Class
argument_list|>
name|matcher
parameter_list|)
block|{
name|this
operator|.
name|matcher
operator|=
name|matcher
expr_stmt|;
block|}
annotation|@
name|Provides
DECL|method|routes (Injector injector)
name|Set
argument_list|<
name|Routes
argument_list|>
name|routes
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
return|return
name|Injectors
operator|.
name|getInstancesOf
argument_list|(
name|injector
argument_list|,
name|matcher
argument_list|)
return|;
block|}
block|}
end_class

end_unit

