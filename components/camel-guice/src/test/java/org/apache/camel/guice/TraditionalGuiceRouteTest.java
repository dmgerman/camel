begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Guice
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
name|multibindings
operator|.
name|Multibinder
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
comment|/**  * Lets use a custom CamelModule to perform explicit binding of route builders  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|TraditionalGuiceRouteTest
specifier|public
class|class
name|TraditionalGuiceRouteTest
extends|extends
name|TestCase
block|{
DECL|class|MyModule
specifier|public
specifier|static
class|class
name|MyModule
extends|extends
name|CamelModule
block|{
comment|/**          * Lets add the routes          */
annotation|@
name|Override
DECL|method|configureRoutes (Multibinder<Routes> binder)
specifier|protected
name|void
name|configureRoutes
parameter_list|(
name|Multibinder
argument_list|<
name|Routes
argument_list|>
name|binder
parameter_list|)
block|{
comment|// use traditional Guice approach to configure routes
name|binder
operator|.
name|addBinding
argument_list|()
operator|.
name|to
argument_list|(
name|MyHardcodeRoute
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|addBinding
argument_list|()
operator|.
name|to
argument_list|(
name|MyRouteInstaller
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// TODO allow injection by @Named("foo") endpoints? Let the CamelContext provide it??
block|}
comment|// TODO demo binding endpoints?
comment|//bind(Endpoint.class).annotatedWith(Names.name("")).
block|}
DECL|method|testGuice ()
specifier|public
name|void
name|testGuice
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|MyModule
argument_list|()
argument_list|)
decl_stmt|;
name|CamelContext
name|camelContext
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

