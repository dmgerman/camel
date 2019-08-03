begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.javaconfig.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|javaconfig
operator|.
name|test
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
operator|.
name|CamelSpringDelegatingTestContextLoader
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
name|test
operator|.
name|spring
operator|.
name|CamelSpringJUnit4ClassRunner
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
block|{
name|JavaConfigWithNestedConfigClassTest
operator|.
name|ContextConfig
operator|.
name|class
block|}
argument_list|,
name|loader
operator|=
name|CamelSpringDelegatingTestContextLoader
operator|.
name|class
argument_list|)
annotation|@
name|Component
DECL|class|JavaConfigWithNestedConfigClassTest
specifier|public
class|class
name|JavaConfigWithNestedConfigClassTest
extends|extends
name|AbstractJUnit4SpringContextTests
implements|implements
name|Cheese
block|{
DECL|field|doCheeseCalled
specifier|private
name|boolean
name|doCheeseCalled
decl_stmt|;
annotation|@
name|Test
DECL|method|testPostProcessorInjectsMe ()
specifier|public
name|void
name|testPostProcessorInjectsMe
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"doCheese() should be called"
argument_list|,
literal|true
argument_list|,
name|doCheeseCalled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCheese ()
specifier|public
name|void
name|doCheese
parameter_list|()
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"doCheese called!"
argument_list|)
expr_stmt|;
name|doCheeseCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Configuration
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
block|{
annotation|@
name|Bean
DECL|method|myPostProcessor ()
specifier|public
name|MyPostProcessor
name|myPostProcessor
parameter_list|()
block|{
return|return
operator|new
name|MyPostProcessor
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

