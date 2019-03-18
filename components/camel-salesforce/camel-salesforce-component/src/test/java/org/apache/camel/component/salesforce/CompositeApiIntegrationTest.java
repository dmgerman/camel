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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|junittoolbox
operator|.
name|ParallelParameterized
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamImplicit
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
name|CamelExecutionException
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
name|AbstractQueryRecordsBase
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
name|CreateSObjectResult
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
name|SObjectComposite
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
name|SObjectComposite
operator|.
name|Method
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
name|SObjectCompositeResponse
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
name|SObjectCompositeResult
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
name|Version
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
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|ParallelParameterized
operator|.
name|class
argument_list|)
DECL|class|CompositeApiIntegrationTest
specifier|public
class|class
name|CompositeApiIntegrationTest
extends|extends
name|AbstractSalesforceTestBase
block|{
DECL|class|Accounts
specifier|public
specifier|static
class|class
name|Accounts
extends|extends
name|AbstractQueryRecordsBase
block|{
annotation|@
name|XStreamImplicit
DECL|field|records
specifier|private
name|List
argument_list|<
name|Account
argument_list|>
name|records
decl_stmt|;
DECL|method|getRecords ()
specifier|public
name|List
argument_list|<
name|Account
argument_list|>
name|getRecords
parameter_list|()
block|{
return|return
name|records
return|;
block|}
DECL|method|setRecords (final List<Account> records)
specifier|public
name|void
name|setRecords
parameter_list|(
specifier|final
name|List
argument_list|<
name|Account
argument_list|>
name|records
parameter_list|)
block|{
name|this
operator|.
name|records
operator|=
name|records
expr_stmt|;
block|}
block|}
DECL|field|VERSIONS
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|VERSIONS
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"38.0"
argument_list|,
literal|"41.0"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|accountId
specifier|private
name|String
name|accountId
decl_stmt|;
DECL|field|compositeUri
specifier|private
specifier|final
name|String
name|compositeUri
decl_stmt|;
DECL|field|version
specifier|private
specifier|final
name|String
name|version
decl_stmt|;
DECL|method|CompositeApiIntegrationTest (final String format, final String version)
specifier|public
name|CompositeApiIntegrationTest
parameter_list|(
specifier|final
name|String
name|format
parameter_list|,
specifier|final
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|compositeUri
operator|=
literal|"salesforce:composite?format="
operator|+
name|format
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|removeRecords ()
specifier|public
name|void
name|removeRecords
parameter_list|()
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"salesforce:deleteSObject?sObjectName=Account&sObjectId="
operator|+
name|accountId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|CamelExecutionException
name|ignored
parameter_list|)
block|{
comment|// other tests run in parallel could have deleted the Account
block|}
name|template
operator|.
name|request
argument_list|(
literal|"direct:deleteBatchAccounts"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setupRecords ()
specifier|public
name|void
name|setupRecords
parameter_list|()
block|{
specifier|final
name|Account
name|account
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|account
operator|.
name|setName
argument_list|(
literal|"Composite API Batch"
argument_list|)
expr_stmt|;
specifier|final
name|CreateSObjectResult
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"salesforce:createSObject"
argument_list|,
name|account
argument_list|,
name|CreateSObjectResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|accountId
operator|=
name|result
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSubmitBatchUsingCompositeApi ()
specifier|public
name|void
name|shouldSubmitBatchUsingCompositeApi
parameter_list|()
block|{
specifier|final
name|SObjectComposite
name|composite
init|=
operator|new
name|SObjectComposite
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|Account
name|updates
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|updates
operator|.
name|setName
argument_list|(
literal|"NewName"
argument_list|)
expr_stmt|;
name|composite
operator|.
name|addUpdate
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
name|updates
argument_list|,
literal|"UpdateExistingAccountReferenceId"
argument_list|)
expr_stmt|;
specifier|final
name|Account
name|newAccount
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|newAccount
operator|.
name|setName
argument_list|(
literal|"Account created from Composite batch API"
argument_list|)
expr_stmt|;
name|composite
operator|.
name|addCreate
argument_list|(
name|newAccount
argument_list|,
literal|"CreateAccountReferenceId"
argument_list|)
expr_stmt|;
name|composite
operator|.
name|addGet
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
literal|"GetAccountReferenceId"
argument_list|,
literal|"Name"
argument_list|,
literal|"BillingPostalCode"
argument_list|)
expr_stmt|;
name|composite
operator|.
name|addDelete
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
literal|"DeleteAccountReferenceId"
argument_list|)
expr_stmt|;
name|testComposite
argument_list|(
name|composite
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportGenericCompositeRequests ()
specifier|public
name|void
name|shouldSupportGenericCompositeRequests
parameter_list|()
block|{
specifier|final
name|SObjectComposite
name|composite
init|=
operator|new
name|SObjectComposite
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|composite
operator|.
name|addGeneric
argument_list|(
name|Method
operator|.
name|GET
argument_list|,
literal|"/sobjects/Account/"
operator|+
name|accountId
argument_list|,
literal|"GetExistingAccountReferenceId"
argument_list|)
expr_stmt|;
name|testComposite
argument_list|(
name|composite
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportObjectCreation ()
specifier|public
name|void
name|shouldSupportObjectCreation
parameter_list|()
block|{
specifier|final
name|SObjectComposite
name|compoiste
init|=
operator|new
name|SObjectComposite
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|Account
name|newAccount
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|newAccount
operator|.
name|setName
argument_list|(
literal|"Account created from Composite batch API"
argument_list|)
expr_stmt|;
name|compoiste
operator|.
name|addCreate
argument_list|(
name|newAccount
argument_list|,
literal|"CreateAccountReferenceId"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectCompositeResponse
name|response
init|=
name|testComposite
argument_list|(
name|compoiste
argument_list|)
decl_stmt|;
name|assertResponseContains
argument_list|(
name|response
argument_list|,
literal|"id"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportObjectDeletion ()
specifier|public
name|void
name|shouldSupportObjectDeletion
parameter_list|()
block|{
specifier|final
name|SObjectComposite
name|composite
init|=
operator|new
name|SObjectComposite
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|composite
operator|.
name|addDelete
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
literal|"DeleteAccountReferenceId"
argument_list|)
expr_stmt|;
name|testComposite
argument_list|(
name|composite
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportObjectRetrieval ()
specifier|public
name|void
name|shouldSupportObjectRetrieval
parameter_list|()
block|{
specifier|final
name|SObjectComposite
name|composite
init|=
operator|new
name|SObjectComposite
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|composite
operator|.
name|addGet
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
literal|"GetExistingAccountReferenceId"
argument_list|,
literal|"Name"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectCompositeResponse
name|response
init|=
name|testComposite
argument_list|(
name|composite
argument_list|)
decl_stmt|;
name|assertResponseContains
argument_list|(
name|response
argument_list|,
literal|"Name"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportObjectUpdates ()
specifier|public
name|void
name|shouldSupportObjectUpdates
parameter_list|()
block|{
specifier|final
name|SObjectComposite
name|composite
init|=
operator|new
name|SObjectComposite
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|Account
name|updates
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|updates
operator|.
name|setName
argument_list|(
literal|"NewName"
argument_list|)
expr_stmt|;
name|updates
operator|.
name|setAccountNumber
argument_list|(
literal|"AC12345"
argument_list|)
expr_stmt|;
name|composite
operator|.
name|addUpdate
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
name|updates
argument_list|,
literal|"UpdateAccountReferenceId"
argument_list|)
expr_stmt|;
name|testComposite
argument_list|(
name|composite
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportQuery ()
specifier|public
name|void
name|shouldSupportQuery
parameter_list|()
block|{
specifier|final
name|SObjectComposite
name|composite
init|=
operator|new
name|SObjectComposite
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|composite
operator|.
name|addQuery
argument_list|(
literal|"SELECT Id, Name FROM Account"
argument_list|,
literal|"SelectQueryReferenceId"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectCompositeResponse
name|response
init|=
name|testComposite
argument_list|(
name|composite
argument_list|)
decl_stmt|;
name|assertResponseContains
argument_list|(
name|response
argument_list|,
literal|"totalSize"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportQueryAll ()
specifier|public
name|void
name|shouldSupportQueryAll
parameter_list|()
block|{
specifier|final
name|SObjectComposite
name|composite
init|=
operator|new
name|SObjectComposite
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|composite
operator|.
name|addQueryAll
argument_list|(
literal|"SELECT Id, Name FROM Account"
argument_list|,
literal|"SelectQueryReferenceId"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectCompositeResponse
name|response
init|=
name|testComposite
argument_list|(
name|composite
argument_list|)
decl_stmt|;
name|assertResponseContains
argument_list|(
name|response
argument_list|,
literal|"totalSize"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportRelatedObjectRetrieval ()
specifier|public
name|void
name|shouldSupportRelatedObjectRetrieval
parameter_list|()
block|{
if|if
condition|(
name|Version
operator|.
name|create
argument_list|(
name|version
argument_list|)
operator|.
name|compareTo
argument_list|(
name|Version
operator|.
name|create
argument_list|(
literal|"36.0"
argument_list|)
argument_list|)
operator|<
literal|0
condition|)
block|{
return|return;
block|}
specifier|final
name|SObjectComposite
name|composite
init|=
operator|new
name|SObjectComposite
argument_list|(
literal|"36.0"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|composite
operator|.
name|addGetRelated
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
literal|"CreatedBy"
argument_list|,
literal|"GetRelatedAccountReferenceId"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectCompositeResponse
name|response
init|=
name|testComposite
argument_list|(
name|composite
argument_list|)
decl_stmt|;
name|assertResponseContains
argument_list|(
name|response
argument_list|,
literal|"Username"
argument_list|)
expr_stmt|;
block|}
DECL|method|testComposite (final SObjectComposite batch)
name|SObjectCompositeResponse
name|testComposite
parameter_list|(
specifier|final
name|SObjectComposite
name|batch
parameter_list|)
block|{
specifier|final
name|SObjectCompositeResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|compositeUri
argument_list|,
name|batch
argument_list|,
name|SObjectCompositeResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|response
argument_list|)
operator|.
name|as
argument_list|(
literal|"Response should be provided"
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|response
operator|.
name|getCompositeResponse
argument_list|()
argument_list|)
operator|.
name|as
argument_list|(
literal|"Received errors in: "
operator|+
name|response
argument_list|)
operator|.
name|allMatch
argument_list|(
name|val
lambda|->
name|val
operator|.
name|getHttpStatusCode
argument_list|()
operator|>=
literal|200
operator|&&
name|val
operator|.
name|getHttpStatusCode
argument_list|()
operator|<=
literal|299
argument_list|)
expr_stmt|;
return|return
name|response
return|;
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
block|{
name|from
argument_list|(
literal|"direct:deleteBatchAccounts"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:query?sObjectClass="
operator|+
name|Accounts
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"&sObjectQuery=SELECT Id FROM Account WHERE Name = 'Account created from Composite batch API'"
argument_list|)
operator|.
name|split
argument_list|(
name|simple
argument_list|(
literal|"${body.records}"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"sObjectId"
argument_list|,
name|simple
argument_list|(
literal|"${body.id}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:deleteSObject?sObjectName=Account"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|salesforceApiVersionToUse ()
specifier|protected
name|String
name|salesforceApiVersionToUse
parameter_list|()
block|{
return|return
name|version
return|;
block|}
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"format = {0}, version = {1}"
argument_list|)
DECL|method|formats ()
specifier|public
specifier|static
name|Iterable
argument_list|<
name|Object
index|[]
argument_list|>
name|formats
parameter_list|()
block|{
return|return
name|VERSIONS
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|v
lambda|->
operator|new
name|Object
index|[]
block|{
literal|"JSON"
block|,
name|v
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|assertResponseContains (final SObjectCompositeResponse response, final String key)
specifier|static
name|void
name|assertResponseContains
parameter_list|(
specifier|final
name|SObjectCompositeResponse
name|response
parameter_list|,
specifier|final
name|String
name|key
parameter_list|)
block|{
name|Assertions
operator|.
name|assertThat
argument_list|(
name|response
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectCompositeResult
argument_list|>
name|compositeResponse
init|=
name|response
operator|.
name|getCompositeResponse
argument_list|()
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|compositeResponse
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|SObjectCompositeResult
name|firstCompositeResponse
init|=
name|compositeResponse
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|firstCompositeResponse
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
specifier|final
name|Object
name|firstCompositeResponseBody
init|=
name|firstCompositeResponse
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|firstCompositeResponseBody
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|body
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
operator|)
name|firstCompositeResponseBody
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|body
argument_list|)
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|body
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

