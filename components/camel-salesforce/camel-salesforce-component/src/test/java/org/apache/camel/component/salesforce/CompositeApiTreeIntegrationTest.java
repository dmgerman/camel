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
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|api
operator|.
name|dto
operator|.
name|composite
operator|.
name|SObjectTree
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
name|Account
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
name|Account_IndustryEnum
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
name|Asset
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
name|Contact
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
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|CompositeApiTreeIntegrationTest
specifier|public
class|class
name|CompositeApiTreeIntegrationTest
extends|extends
name|AbstractSalesforceTestBase
block|{
DECL|field|format
specifier|private
specifier|final
name|String
name|format
decl_stmt|;
DECL|method|CompositeApiTreeIntegrationTest (final String format)
specifier|public
name|CompositeApiTreeIntegrationTest
parameter_list|(
specifier|final
name|String
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"format = {0}"
argument_list|)
DECL|method|formats ()
specifier|public
specifier|static
name|Iterable
argument_list|<
name|String
argument_list|>
name|formats
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
literal|"JSON"
argument_list|,
literal|"XML"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|shouldSubmitTreeUsingCompositeApi ()
specifier|public
name|void
name|shouldSubmitTreeUsingCompositeApi
parameter_list|()
block|{
specifier|final
name|Account
name|simpleAccount
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
specifier|final
name|Contact
name|smith
init|=
operator|new
name|Contact
argument_list|()
decl_stmt|;
specifier|final
name|Contact
name|evans
init|=
operator|new
name|Contact
argument_list|()
decl_stmt|;
specifier|final
name|Account
name|simpleAccount2
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|simpleAccount
operator|.
name|setName
argument_list|(
literal|"SampleAccount"
argument_list|)
expr_stmt|;
name|simpleAccount
operator|.
name|setPhone
argument_list|(
literal|"1234567890"
argument_list|)
expr_stmt|;
name|simpleAccount
operator|.
name|setWebsite
argument_list|(
literal|"www.salesforce.com"
argument_list|)
expr_stmt|;
name|simpleAccount
operator|.
name|setNumberOfEmployees
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|simpleAccount
operator|.
name|setIndustry
argument_list|(
name|Account_IndustryEnum
operator|.
name|BANKING
argument_list|)
expr_stmt|;
name|smith
operator|.
name|setLastName
argument_list|(
literal|"Smith"
argument_list|)
expr_stmt|;
name|smith
operator|.
name|setTitle
argument_list|(
literal|"President"
argument_list|)
expr_stmt|;
name|smith
operator|.
name|setEmail
argument_list|(
literal|"sample@salesforce.com"
argument_list|)
expr_stmt|;
name|evans
operator|.
name|setLastName
argument_list|(
literal|"Evans"
argument_list|)
expr_stmt|;
name|evans
operator|.
name|setTitle
argument_list|(
literal|"Vice President"
argument_list|)
expr_stmt|;
name|evans
operator|.
name|setEmail
argument_list|(
literal|"sample@salesforce.com"
argument_list|)
expr_stmt|;
name|simpleAccount2
operator|.
name|setName
argument_list|(
literal|"SampleAccount2"
argument_list|)
expr_stmt|;
name|simpleAccount2
operator|.
name|setPhone
argument_list|(
literal|"1234567890"
argument_list|)
expr_stmt|;
name|simpleAccount2
operator|.
name|setWebsite
argument_list|(
literal|"www.salesforce2.com"
argument_list|)
expr_stmt|;
name|simpleAccount2
operator|.
name|setNumberOfEmployees
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|simpleAccount2
operator|.
name|setIndustry
argument_list|(
name|Account_IndustryEnum
operator|.
name|BANKING
argument_list|)
expr_stmt|;
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
name|simpleAccount
argument_list|)
operator|.
name|addChildren
argument_list|(
literal|"Contacts"
argument_list|,
name|smith
argument_list|,
name|evans
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
name|simpleAccount2
argument_list|)
expr_stmt|;
specifier|final
name|Account
name|simpleAccount3
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|simpleAccount3
operator|.
name|setName
argument_list|(
literal|"SimpleAccount3"
argument_list|)
expr_stmt|;
specifier|final
name|Contact
name|contact
init|=
operator|new
name|Contact
argument_list|()
decl_stmt|;
name|contact
operator|.
name|setFirstName
argument_list|(
literal|"Simple"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setLastName
argument_list|(
literal|"Contact"
argument_list|)
expr_stmt|;
specifier|final
name|Asset
name|asset
init|=
operator|new
name|Asset
argument_list|()
decl_stmt|;
name|asset
operator|.
name|setName
argument_list|(
literal|"Asset Name"
argument_list|)
expr_stmt|;
name|asset
operator|.
name|setDescription
argument_list|(
literal|"Simple asset"
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
name|simpleAccount3
argument_list|)
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|contact
argument_list|)
operator|.
name|addChild
argument_list|(
literal|"Assets"
argument_list|,
name|asset
argument_list|)
expr_stmt|;
specifier|final
name|SObjectTree
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"salesforce:composite-tree?format="
operator|+
name|format
argument_list|,
name|tree
argument_list|,
name|SObjectTree
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Response should be provided"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"First account should have Id set"
argument_list|,
name|simpleAccount
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"President of the first account should have Id set"
argument_list|,
name|smith
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Vice president of the first account should have Id set"
argument_list|,
name|evans
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Second account should have Id set"
argument_list|,
name|simpleAccount2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Third account should have Id set"
argument_list|,
name|simpleAccount3
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Simple contact on third account should have Id set"
argument_list|,
name|contact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Simple asset on the contact of the third account should have Id set"
argument_list|,
name|asset
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateRouteBuilder ()
specifier|protected
name|RouteBuilder
name|doCreateRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{             }
block|}
return|;
block|}
block|}
end_class

end_unit

