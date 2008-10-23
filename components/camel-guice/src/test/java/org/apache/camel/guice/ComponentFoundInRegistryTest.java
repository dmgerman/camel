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
name|Component
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
name|Endpoint
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
name|component
operator|.
name|mock
operator|.
name|MockComponent
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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

begin_import
import|import
name|org
operator|.
name|guiceyfruit
operator|.
name|jndi
operator|.
name|GuiceInitialContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|guiceyfruit
operator|.
name|jndi
operator|.
name|JndiBind
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|is
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_comment
comment|/**  * Lets use a custom CamelModule to perform explicit binding of route builders  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ComponentFoundInRegistryTest
specifier|public
class|class
name|ComponentFoundInRegistryTest
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
name|JndiBind
argument_list|(
literal|"foo"
argument_list|)
DECL|method|foo ()
name|MockComponent
name|foo
parameter_list|()
block|{
return|return
operator|new
name|MockComponent
argument_list|()
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
name|Hashtable
name|env
init|=
operator|new
name|Hashtable
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|InitialContext
operator|.
name|PROVIDER_URL
argument_list|,
name|GuiceInitialContextFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Injectors
operator|.
name|MODULE_CLASS_NAMES
argument_list|,
name|MyModule
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|InitialContext
name|context
init|=
operator|new
name|InitialContext
argument_list|(
name|env
argument_list|)
decl_stmt|;
name|Injector
name|injector
init|=
operator|(
name|Injector
operator|)
name|context
operator|.
name|lookup
argument_list|(
name|Injector
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Found injector"
argument_list|,
name|injector
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|context
operator|.
name|lookup
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found a value for foo!"
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
name|Component
name|component
init|=
name|camelContext
operator|.
name|getComponent
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|component
argument_list|,
name|is
argument_list|(
name|MockComponent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"foo:cheese"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoint
argument_list|,
name|is
argument_list|(
name|MockEndpoint
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

