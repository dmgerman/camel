begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.spring.javaconfig
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|spring
operator|.
name|javaconfig
package|;
end_package

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
name|ClassPathXmlApplicationContext
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
name|spring
operator|.
name|javaconfig
operator|.
name|Main
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|IntegrationTest
specifier|public
class|class
name|IntegrationTest
extends|extends
name|TestCase
block|{
DECL|method|testCamelRulesDeployCorrectlyInSpring ()
specifier|public
name|void
name|testCamelRulesDeployCorrectlyInSpring
parameter_list|()
throws|throws
name|Exception
block|{
comment|// let's boot up the Spring application context for 2 seconds to check that it works OK
name|Main
operator|.
name|main
argument_list|(
literal|"-duration"
argument_list|,
literal|"2s"
argument_list|,
literal|"-bp"
argument_list|,
literal|"org.apache.camel.example.spring.javaconfig"
argument_list|)
expr_stmt|;
block|}
DECL|method|testStartApplicationContext ()
specifier|public
name|void
name|testStartApplicationContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test to boot up the application context from spring configuration
name|ApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/META-INF/spring/camel-context.xml"
argument_list|)
decl_stmt|;
name|String
index|[]
name|names
init|=
name|context
operator|.
name|getBeanNamesForType
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should be a camel context "
argument_list|,
literal|1
argument_list|,
name|names
operator|.
name|length
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|(
name|CamelContext
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

