begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bonita.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bonita
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|bonita
operator|.
name|api
operator|.
name|model
operator|.
name|ProcessDefinitionResponse
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
name|bonita
operator|.
name|api
operator|.
name|util
operator|.
name|BonitaAPIConfig
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

begin_class
DECL|class|BonitaAPITest
specifier|public
class|class
name|BonitaAPITest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testGetProcessDefinitionEmptyInput ()
specifier|public
name|void
name|testGetProcessDefinitionEmptyInput
parameter_list|()
block|{
name|BonitaAPI
name|bonitaApi
init|=
name|BonitaAPIBuilder
operator|.
name|build
argument_list|(
operator|new
name|BonitaAPIConfig
argument_list|(
literal|"hostname"
argument_list|,
literal|"port"
argument_list|,
literal|"username"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
decl_stmt|;
name|bonitaApi
operator|.
name|getProcessDefinition
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testStartCaseEmptyProcessDefinitionId ()
specifier|public
name|void
name|testStartCaseEmptyProcessDefinitionId
parameter_list|()
throws|throws
name|Exception
block|{
name|BonitaAPI
name|bonitaApi
init|=
name|BonitaAPIBuilder
operator|.
name|build
argument_list|(
operator|new
name|BonitaAPIConfig
argument_list|(
literal|"hostname"
argument_list|,
literal|"port"
argument_list|,
literal|"username"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
decl_stmt|;
name|bonitaApi
operator|.
name|startCase
argument_list|(
literal|null
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Serializable
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testStartCaseNUllContractInput ()
specifier|public
name|void
name|testStartCaseNUllContractInput
parameter_list|()
throws|throws
name|Exception
block|{
name|BonitaAPI
name|bonitaApi
init|=
name|BonitaAPIBuilder
operator|.
name|build
argument_list|(
operator|new
name|BonitaAPIConfig
argument_list|(
literal|"hostname"
argument_list|,
literal|"port"
argument_list|,
literal|"username"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
decl_stmt|;
name|ProcessDefinitionResponse
name|processDefinition
init|=
operator|new
name|ProcessDefinitionResponse
argument_list|()
decl_stmt|;
name|bonitaApi
operator|.
name|startCase
argument_list|(
name|processDefinition
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

