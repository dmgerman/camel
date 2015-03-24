begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|builder
operator|.
name|RouteBuilder
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
name|dto
operator|.
name|generated
operator|.
name|Merchandise__c
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

begin_class
DECL|class|AbstractSalesforceTestBase
specifier|public
specifier|abstract
class|class
name|AbstractSalesforceTestBase
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// only create the context once for this class
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create the test component
name|createComponent
argument_list|()
expr_stmt|;
return|return
name|doCreateRouteBuilder
argument_list|()
return|;
block|}
DECL|method|doCreateRouteBuilder ()
specifier|protected
specifier|abstract
name|RouteBuilder
name|doCreateRouteBuilder
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|createComponent ()
specifier|protected
name|void
name|createComponent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create the component
name|SalesforceComponent
name|component
init|=
operator|new
name|SalesforceComponent
argument_list|()
decl_stmt|;
specifier|final
name|SalesforceEndpointConfig
name|config
init|=
operator|new
name|SalesforceEndpointConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setApiVersion
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"apiVersion"
argument_list|,
name|SalesforceEndpointConfig
operator|.
name|DEFAULT_VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|component
operator|.
name|setLoginConfig
argument_list|(
name|LoginConfigHelper
operator|.
name|getLoginConfig
argument_list|()
argument_list|)
expr_stmt|;
comment|// set DTO package
name|component
operator|.
name|setPackages
argument_list|(
operator|new
name|String
index|[]
block|{
name|Merchandise__c
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
comment|// add it to context
name|context
argument_list|()
operator|.
name|addComponent
argument_list|(
literal|"salesforce"
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

