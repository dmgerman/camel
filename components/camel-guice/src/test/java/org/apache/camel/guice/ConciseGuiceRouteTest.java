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
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Uses a RouteBuilder to bind instances of routes to the CamelContext  *  * @version   */
end_comment

begin_class
DECL|class|ConciseGuiceRouteTest
specifier|public
class|class
name|ConciseGuiceRouteTest
extends|extends
name|Assert
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testGuice ()
specifier|public
name|void
name|testGuice
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets disable resource injection to avoid JNDI being used
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|CamelModuleWithRouteTypes
argument_list|(
name|MyRouteInstaller
operator|.
name|class
argument_list|,
name|MyHardcodeRoute
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
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

