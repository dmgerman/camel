begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|Limits
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
name|junit4
operator|.
name|CamelTestSupport
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assume
operator|.
name|assumeNotNull
import|;
end_import

begin_class
DECL|class|SalesforceJwtBearerTokenFlowIntegrationTest
specifier|public
class|class
name|SalesforceJwtBearerTokenFlowIntegrationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|shouldLoginUsingJwtBearerToken ()
specifier|public
name|void
name|shouldLoginUsingJwtBearerToken
parameter_list|()
block|{
specifier|final
name|Limits
name|limits
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"salesforce:limits"
argument_list|,
literal|null
argument_list|,
name|Limits
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|limits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|SalesforceComponent
name|salesforce
init|=
operator|new
name|SalesforceComponent
argument_list|()
decl_stmt|;
specifier|final
name|SalesforceLoginConfig
name|loginConfig
init|=
name|LoginConfigHelper
operator|.
name|getLoginConfig
argument_list|()
decl_stmt|;
name|assumeNotNull
argument_list|(
name|loginConfig
operator|.
name|getKeystore
argument_list|()
argument_list|)
expr_stmt|;
name|assumeNotNull
argument_list|(
name|loginConfig
operator|.
name|getKeystore
argument_list|()
operator|.
name|getResource
argument_list|()
argument_list|)
expr_stmt|;
comment|// force authentication type to JWT
name|loginConfig
operator|.
name|setType
argument_list|(
name|AuthenticationType
operator|.
name|JWT
argument_list|)
expr_stmt|;
name|salesforce
operator|.
name|setLoginConfig
argument_list|(
name|loginConfig
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"salesforce"
argument_list|,
name|salesforce
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

