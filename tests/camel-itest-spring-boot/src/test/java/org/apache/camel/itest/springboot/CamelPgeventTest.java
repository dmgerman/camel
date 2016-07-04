begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
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
name|itest
operator|.
name|springboot
operator|.
name|util
operator|.
name|ArquillianPackager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|Deployment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|junit
operator|.
name|Arquillian
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|Arquillian
operator|.
name|class
argument_list|)
DECL|class|CamelPgeventTest
specifier|public
class|class
name|CamelPgeventTest
extends|extends
name|AbstractSpringBootTestSupport
block|{
annotation|@
name|Deployment
DECL|method|createSpringBootPackage ()
specifier|public
specifier|static
name|Archive
argument_list|<
name|?
argument_list|>
name|createSpringBootPackage
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|ArquillianPackager
operator|.
name|springBootPackage
argument_list|(
name|createTestConfig
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createTestConfig ()
specifier|public
specifier|static
name|ITestConfig
name|createTestConfig
parameter_list|()
block|{
return|return
operator|new
name|ITestConfigBuilder
argument_list|()
operator|.
name|module
argument_list|(
name|inferModuleName
argument_list|(
name|CamelPgeventTest
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|unitTestExpectedNumber
argument_list|(
literal|0
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|componentTests ()
specifier|public
name|void
name|componentTests
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runComponentTest
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|runModuleUnitTestsIfEnabled
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

