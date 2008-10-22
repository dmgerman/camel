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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Inject
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
name|spi
operator|.
name|CloseFailedException
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|GuiceTest
specifier|public
class|class
name|GuiceTest
extends|extends
name|TestCase
block|{
comment|/**      * Asserts that the CamelContext is available in the given Injector, that its been started, then close the injector      * @param injector      * @throws CloseFailedException      */
DECL|method|assertCamelContextRunningThenCloseInjector (Injector injector)
specifier|public
specifier|static
name|void
name|assertCamelContextRunningThenCloseInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
throws|throws
name|Exception
block|{
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
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
argument_list|(
name|camelContext
argument_list|,
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|is
argument_list|(
name|GuiceCamelContext
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|GuiceCamelContext
name|guiceContext
init|=
operator|(
name|GuiceCamelContext
operator|)
name|camelContext
decl_stmt|;
name|assertTrue
argument_list|(
literal|"is started!"
argument_list|,
name|guiceContext
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|injector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|class|Cheese
specifier|public
specifier|static
class|class
name|Cheese
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Inject
DECL|method|Cheese (CamelContext camelContext)
specifier|public
name|Cheese
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
block|}
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
name|CamelModuleWithMatchingRoutes
argument_list|()
argument_list|)
decl_stmt|;
name|Cheese
name|cheese
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|Cheese
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have cheese"
argument_list|,
name|cheese
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have camelContext"
argument_list|,
name|cheese
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Got "
operator|+
name|cheese
argument_list|)
expr_stmt|;
name|assertCamelContextRunningThenCloseInjector
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

