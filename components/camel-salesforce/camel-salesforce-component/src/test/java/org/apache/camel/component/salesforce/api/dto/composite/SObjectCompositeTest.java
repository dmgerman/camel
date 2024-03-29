begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.composite
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
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonPropertyOrder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|SerializationFeature
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
name|AbstractDescribedSObjectBase
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
name|SObjectDescription
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
name|utils
operator|.
name|JsonUtils
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
name|Contact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|SObjectCompositeTest
specifier|public
class|class
name|SObjectCompositeTest
block|{
comment|// CHECKSTYLE:OFF
annotation|@
name|JsonPropertyOrder
argument_list|(
block|{
literal|"account__c"
block|,
literal|"contactId__c"
block|}
argument_list|)
DECL|class|AccountContactJunction__c
specifier|public
specifier|static
class|class
name|AccountContactJunction__c
extends|extends
name|AbstractDescribedSObjectBase
block|{
DECL|method|AccountContactJunction__c ()
specifier|public
name|AccountContactJunction__c
parameter_list|()
block|{
name|getAttributes
argument_list|()
operator|.
name|setType
argument_list|(
literal|"AccountContactJunction__c"
argument_list|)
expr_stmt|;
block|}
DECL|field|account__c
specifier|private
name|String
name|account__c
decl_stmt|;
DECL|field|contactId__c
specifier|private
name|String
name|contactId__c
decl_stmt|;
annotation|@
name|Override
DECL|method|description ()
specifier|public
name|SObjectDescription
name|description
parameter_list|()
block|{
return|return
operator|new
name|SObjectDescription
argument_list|()
return|;
block|}
DECL|method|getAccount__c ()
specifier|public
name|String
name|getAccount__c
parameter_list|()
block|{
return|return
name|account__c
return|;
block|}
DECL|method|getContactId__c ()
specifier|public
name|String
name|getContactId__c
parameter_list|()
block|{
return|return
name|contactId__c
return|;
block|}
DECL|method|setAccount__c (final String account__c)
specifier|public
name|void
name|setAccount__c
parameter_list|(
specifier|final
name|String
name|account__c
parameter_list|)
block|{
name|this
operator|.
name|account__c
operator|=
name|account__c
expr_stmt|;
block|}
DECL|method|setContactId__c (final String contactId__c)
specifier|public
name|void
name|setContactId__c
parameter_list|(
specifier|final
name|String
name|contactId__c
parameter_list|)
block|{
name|this
operator|.
name|contactId__c
operator|=
name|contactId__c
expr_stmt|;
block|}
block|}
comment|// CHECKSTYLE:ON
annotation|@
name|JsonPropertyOrder
argument_list|(
block|{
literal|"Name"
block|,
literal|"BillingStreet"
block|,
literal|"BillingCity"
block|,
literal|"BillingState"
block|,
literal|"Industry"
block|}
argument_list|)
DECL|class|TestAccount
specifier|public
specifier|static
class|class
name|TestAccount
extends|extends
name|Account
block|{
comment|// just for property order
block|}
annotation|@
name|JsonPropertyOrder
argument_list|(
block|{
literal|"LastName"
block|,
literal|"Phone"
block|}
argument_list|)
DECL|class|TestContact
specifier|public
specifier|static
class|class
name|TestContact
extends|extends
name|Contact
block|{
comment|// just for property order
block|}
DECL|field|composite
specifier|private
specifier|final
name|SObjectComposite
name|composite
decl_stmt|;
DECL|method|SObjectCompositeTest ()
specifier|public
name|SObjectCompositeTest
parameter_list|()
block|{
name|composite
operator|=
operator|new
name|SObjectComposite
argument_list|(
literal|"38.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// first insert operation via an external id
specifier|final
name|Account
name|updateAccount
init|=
operator|new
name|TestAccount
argument_list|()
decl_stmt|;
name|updateAccount
operator|.
name|setName
argument_list|(
literal|"Salesforce"
argument_list|)
expr_stmt|;
name|updateAccount
operator|.
name|setBillingStreet
argument_list|(
literal|"Landmark @ 1 Market Street"
argument_list|)
expr_stmt|;
name|updateAccount
operator|.
name|setBillingCity
argument_list|(
literal|"San Francisco"
argument_list|)
expr_stmt|;
name|updateAccount
operator|.
name|setBillingState
argument_list|(
literal|"California"
argument_list|)
expr_stmt|;
name|updateAccount
operator|.
name|setIndustry
argument_list|(
name|Account_IndustryEnum
operator|.
name|TECHNOLOGY
argument_list|)
expr_stmt|;
name|composite
operator|.
name|addUpdate
argument_list|(
literal|"Account"
argument_list|,
literal|"001xx000003DIpcAAG"
argument_list|,
name|updateAccount
argument_list|,
literal|"UpdatedAccount"
argument_list|)
expr_stmt|;
specifier|final
name|Contact
name|newContact
init|=
operator|new
name|TestContact
argument_list|()
decl_stmt|;
name|newContact
operator|.
name|setLastName
argument_list|(
literal|"John Doe"
argument_list|)
expr_stmt|;
name|newContact
operator|.
name|setPhone
argument_list|(
literal|"1234567890"
argument_list|)
expr_stmt|;
name|composite
operator|.
name|addCreate
argument_list|(
name|newContact
argument_list|,
literal|"NewContact"
argument_list|)
expr_stmt|;
specifier|final
name|AccountContactJunction__c
name|junction
init|=
operator|new
name|AccountContactJunction__c
argument_list|()
decl_stmt|;
name|junction
operator|.
name|setAccount__c
argument_list|(
literal|"001xx000003DIpcAAG"
argument_list|)
expr_stmt|;
name|junction
operator|.
name|setContactId__c
argument_list|(
literal|"@{NewContact.id}"
argument_list|)
expr_stmt|;
name|composite
operator|.
name|addCreate
argument_list|(
name|junction
argument_list|,
literal|"JunctionRecord"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSerializeToJson ()
specifier|public
name|void
name|shouldSerializeToJson
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|String
name|expectedJson
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|SObjectCompositeTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/camel/component/salesforce/api/dto/composite_request_example.json"
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
specifier|final
name|ObjectMapper
name|mapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
operator|.
name|copy
argument_list|()
operator|.
name|configure
argument_list|(
name|SerializationFeature
operator|.
name|ORDER_MAP_ENTRIES_BY_KEYS
argument_list|,
literal|true
argument_list|)
operator|.
name|configure
argument_list|(
name|SerializationFeature
operator|.
name|INDENT_OUTPUT
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|String
name|serialized
init|=
name|mapper
operator|.
name|writerFor
argument_list|(
name|SObjectComposite
operator|.
name|class
argument_list|)
operator|.
name|writeValueAsString
argument_list|(
name|composite
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|serialized
argument_list|)
operator|.
name|as
argument_list|(
literal|"Should serialize as expected by Salesforce"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedJson
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

