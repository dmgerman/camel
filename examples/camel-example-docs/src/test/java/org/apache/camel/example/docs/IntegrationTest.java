begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.docs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|docs
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
comment|// lets boot up the Spring application context for 2 seconds to check it works OK
name|Main
operator|.
name|main
argument_list|(
literal|"-duration"
argument_list|,
literal|"2s"
argument_list|,
literal|"-o"
argument_list|,
literal|"target/site/cameldoc"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

