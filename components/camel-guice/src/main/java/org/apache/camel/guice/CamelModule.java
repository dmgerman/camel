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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|AbstractModule
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
name|jsr250
operator|.
name|Jsr250
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
name|jsr250
operator|.
name|Jsr250Module
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
name|Routes
import|;
end_import

begin_comment
comment|/**  * A base Guice module for creating a {@link CamelContext} leaving it up to the users module  * to bind a Set<Routes> for the routing rules.  *<p>  * To bind the routes you should create a provider method annotated with @Provides and returning Set<Routes> such as  *<code><pre>  * public class MyModule extends CamelModule {  *&#64;Provides  *   Set&lt;Routes&gt; routes(Injector injector) { ... }  * }  *</pre></code>  * If you wish to bind all of the bound {@link Routes} implementations available - maybe with some filter applied - then  * please use the {@link org.apache.camel.guice.CamelModuleWithMatchingRoutes}.  *<p>  * Otherwise if you wish to list all of the classes of the {@link Routes} implementations then use the  * {@link org.apache.camel.guice.CamelModuleWithRouteTypes} module instead.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelModule
specifier|public
class|class
name|CamelModule
extends|extends
name|Jsr250Module
block|{
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|super
operator|.
name|configure
argument_list|()
expr_stmt|;
name|bind
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|GuiceCamelContext
operator|.
name|class
argument_list|)
operator|.
name|asEagerSingleton
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

