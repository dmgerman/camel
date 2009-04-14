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
name|internal
operator|.
name|Lists
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
name|name
operator|.
name|Named
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
name|Routes
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
name|List
import|;
end_import

begin_comment
comment|/**  * Create a collection of routes via a provider method  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CollectionOfCustomRoutesTest
specifier|public
class|class
name|CollectionOfCustomRoutesTest
extends|extends
name|TestCase
block|{
DECL|class|MyModule
specifier|public
specifier|static
class|class
name|MyModule
extends|extends
name|CamelModuleWithMatchingRoutes
block|{
annotation|@
name|Provides
annotation|@
name|Named
argument_list|(
literal|"foo"
argument_list|)
DECL|method|myRoutes ()
specifier|protected
name|Collection
argument_list|<
name|?
extends|extends
name|Routes
argument_list|>
name|myRoutes
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
operator|new
name|MyConfigurableRoute2
argument_list|(
literal|"direct:a"
argument_list|,
literal|"direct:b"
argument_list|)
argument_list|,
operator|new
name|MyConfigurableRoute2
argument_list|(
literal|"direct:c"
argument_list|,
literal|"direct:d"
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|testDummy ()
specifier|public
name|void
name|testDummy
parameter_list|()
throws|throws
name|Exception
block|{      }
DECL|method|DONTtestGuice ()
specifier|public
name|void
name|DONTtestGuice
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
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|camelContext
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"size of "
operator|+
name|list
argument_list|,
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|GuiceTest
operator|.
name|assertCamelContextRunningThenCloseInjector
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

