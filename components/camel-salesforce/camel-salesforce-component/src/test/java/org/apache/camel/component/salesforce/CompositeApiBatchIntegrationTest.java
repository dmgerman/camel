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
name|io
operator|.
name|IOException
import|;
end_import

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
name|SObjectBatch
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
name|SObjectBatch
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
name|SObjectBatchResponse
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
name|SObjectBatchResult
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
DECL|class|CompositeApiBatchIntegrationTest
specifier|public
class|class
name|CompositeApiBatchIntegrationTest
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
name|SalesforceEndpointConfig
operator|.
name|DEFAULT_VERSION
argument_list|,
literal|"34.0"
argument_list|,
literal|"36.0"
argument_list|,
literal|"37.0"
argument_list|,
literal|"39.0"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|accountId
specifier|private
name|String
name|accountId
decl_stmt|;
DECL|field|batchuri
specifier|private
specifier|final
name|String
name|batchuri
decl_stmt|;
DECL|field|version
specifier|private
specifier|final
name|String
name|version
decl_stmt|;
DECL|method|CompositeApiBatchIntegrationTest (final String format, final String version)
specifier|public
name|CompositeApiBatchIntegrationTest
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
name|batchuri
operator|=
literal|"salesforce:composite-batch?format="
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
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
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
name|batch
operator|.
name|addUpdate
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
name|updates
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
name|batch
operator|.
name|addCreate
argument_list|(
name|newAccount
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addGet
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
literal|"Name"
argument_list|,
literal|"BillingPostalCode"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addDelete
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|batchuri
argument_list|,
name|batch
argument_list|,
name|SObjectBatchResponse
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
name|assertFalse
argument_list|(
name|response
operator|.
name|hasErrors
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportGenericBatchRequests ()
specifier|public
name|void
name|shouldSupportGenericBatchRequests
parameter_list|()
block|{
specifier|final
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|batch
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
argument_list|)
expr_stmt|;
name|testBatch
argument_list|(
name|batch
argument_list|)
expr_stmt|;
block|}
comment|/**      * The XML format fails, as Salesforce API wrongly includes whitespaces      * inside tag names. E.g.<Ant Migration Tool>      * https://www.w3.org/TR/2008/REC-xml-20081126/#NT-NameChar      */
annotation|@
name|Test
DECL|method|shouldSupportLimits ()
specifier|public
name|void
name|shouldSupportLimits
parameter_list|()
block|{
specifier|final
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|batch
operator|.
name|addLimits
argument_list|()
expr_stmt|;
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|testBatch
argument_list|(
name|batch
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
specifier|final
name|SObjectBatchResult
name|batchResult
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|Object
argument_list|>
name|result
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|batchResult
operator|.
name|getResult
argument_list|()
decl_stmt|;
comment|// JSON and XML structure are different, XML has `LimitsSnapshot` node,
comment|// JSON does not
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
name|Object
argument_list|>
name|limits
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|result
operator|.
name|getOrDefault
argument_list|(
literal|"LimitsSnapshot"
argument_list|,
name|result
argument_list|)
decl_stmt|;
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
name|String
argument_list|>
name|apiRequests
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|limits
operator|.
name|get
argument_list|(
literal|"DailyApiRequests"
argument_list|)
decl_stmt|;
comment|// for JSON value will be Integer, for XML (no type information) it will
comment|// be String
comment|// This number can be different per org, and future releases,
comment|// so let's just make sure it's greater than zero
name|assertTrue
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|apiRequests
operator|.
name|get
argument_list|(
literal|"Max"
argument_list|)
argument_list|)
argument_list|)
operator|>
literal|0
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
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
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
name|batch
operator|.
name|addCreate
argument_list|(
name|newAccount
argument_list|)
expr_stmt|;
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|testBatch
argument_list|(
name|batch
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
specifier|final
name|SObjectBatchResult
name|batchResult
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|Object
argument_list|>
name|result
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|batchResult
operator|.
name|getResult
argument_list|()
decl_stmt|;
comment|// JSON and XML structure are different, XML has `Result` node, JSON
comment|// does not
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
name|Object
argument_list|>
name|creationOutcome
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|result
operator|.
name|getOrDefault
argument_list|(
literal|"Result"
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|creationOutcome
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
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
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|batch
operator|.
name|addDelete
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
name|testBatch
argument_list|(
name|batch
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
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|batch
operator|.
name|addGet
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
literal|"Name"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|testBatch
argument_list|(
name|batch
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
specifier|final
name|SObjectBatchResult
name|batchResult
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|Object
argument_list|>
name|result
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|batchResult
operator|.
name|getResult
argument_list|()
decl_stmt|;
comment|// JSON and XML structure are different, XML has `Account` node, JSON
comment|// does not
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
name|String
argument_list|>
name|data
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|result
operator|.
name|getOrDefault
argument_list|(
literal|"Account"
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Composite API Batch"
argument_list|,
name|data
operator|.
name|get
argument_list|(
literal|"Name"
argument_list|)
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
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
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
name|batch
operator|.
name|addUpdate
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
name|updates
argument_list|)
expr_stmt|;
name|testBatch
argument_list|(
name|batch
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
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|batch
operator|.
name|addQuery
argument_list|(
literal|"SELECT Id, Name FROM Account"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|testBatch
argument_list|(
name|batch
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
specifier|final
name|SObjectBatchResult
name|batchResult
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|Object
argument_list|>
name|result
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|batchResult
operator|.
name|getResult
argument_list|()
decl_stmt|;
comment|// JSON and XML structure are different, XML has `QueryResult` node,
comment|// JSON does not
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
name|String
argument_list|>
name|data
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|result
operator|.
name|getOrDefault
argument_list|(
literal|"QueryResult"
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|"totalSize"
argument_list|)
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
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|batch
operator|.
name|addQueryAll
argument_list|(
literal|"SELECT Id, Name FROM Account"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|testBatch
argument_list|(
name|batch
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
specifier|final
name|SObjectBatchResult
name|batchResult
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|Object
argument_list|>
name|result
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|batchResult
operator|.
name|getResult
argument_list|()
decl_stmt|;
comment|// JSON and XML structure are different, XML has `QueryResult` node,
comment|// JSON does not
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
name|String
argument_list|>
name|data
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|result
operator|.
name|getOrDefault
argument_list|(
literal|"QueryResult"
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|"totalSize"
argument_list|)
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
throws|throws
name|IOException
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
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
literal|"36.0"
argument_list|)
decl_stmt|;
name|batch
operator|.
name|addGetRelated
argument_list|(
literal|"Account"
argument_list|,
name|accountId
argument_list|,
literal|"CreatedBy"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|testBatch
argument_list|(
name|batch
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
specifier|final
name|SObjectBatchResult
name|batchResult
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|Object
argument_list|>
name|result
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|batchResult
operator|.
name|getResult
argument_list|()
decl_stmt|;
comment|// JSON and XML structure are different, XML has `User` node, JSON does
comment|// not
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
name|String
argument_list|>
name|data
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|result
operator|.
name|getOrDefault
argument_list|(
literal|"User"
argument_list|,
name|result
argument_list|)
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
name|assertEquals
argument_list|(
name|loginConfig
operator|.
name|getUserName
argument_list|()
argument_list|,
name|data
operator|.
name|get
argument_list|(
literal|"Username"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportSearch ()
specifier|public
name|void
name|shouldSupportSearch
parameter_list|()
block|{
specifier|final
name|SObjectBatch
name|batch
init|=
operator|new
name|SObjectBatch
argument_list|(
name|version
argument_list|)
decl_stmt|;
comment|// we cannot rely on search returning the `Composite API Batch` account
comment|// as the search indexer runs
comment|// asynchronously to object creation, so that account might not be
comment|// indexed at this time, so we search for
comment|// `United` Account that should be created with developer instance
name|batch
operator|.
name|addSearch
argument_list|(
literal|"FIND {United} IN Name Fields RETURNING Account (Name)"
argument_list|)
expr_stmt|;
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|testBatch
argument_list|(
name|batch
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
specifier|final
name|SObjectBatchResult
name|batchResult
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|firstBatchResult
init|=
name|batchResult
operator|.
name|getResult
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|searchResult
decl_stmt|;
if|if
condition|(
name|firstBatchResult
operator|instanceof
name|Map
condition|)
block|{
comment|// the JSON and XML responses differ, XML has a root node which can
comment|// be either SearchResults or
comment|// SearchResultWithMetadata
comment|// furthermore version 37.0 search results are no longer array, but
comment|// dictionary of {
comment|// "searchRecords": [<array>] } and the XML output changed to
comment|//<SearchResultWithMetadata><searchRecords>, so
comment|// we have:
comment|// @formatter:off
comment|// | version | format | response syntax |
comment|// | 34 | JSON | {attributes={type=Account... |
comment|// | 34 | XML | {SearchResults={attributes={type=Account... |
comment|// | 37 | JSON | {searchRecords=[{attributes={type=Account... |
comment|// | 37 | XML |
comment|// {SearchResultWithMetadata={searchRecords={attributes={type=Account...
comment|// |
comment|// @formatter:on
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
name|Object
argument_list|>
name|tmp
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|firstBatchResult
decl_stmt|;
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
name|Object
argument_list|>
name|nested
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|tmp
operator|.
name|getOrDefault
argument_list|(
literal|"SearchResultWithMetadata"
argument_list|,
name|tmp
argument_list|)
decl_stmt|;
comment|// JSON and XML structure are different, XML has `SearchResults`
comment|// node, JSON does not
name|searchResult
operator|=
name|nested
operator|.
name|getOrDefault
argument_list|(
literal|"searchRecords"
argument_list|,
name|nested
operator|.
name|getOrDefault
argument_list|(
literal|"SearchResults"
argument_list|,
name|nested
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|searchResult
operator|=
name|firstBatchResult
expr_stmt|;
block|}
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
decl_stmt|;
if|if
condition|(
name|searchResult
operator|instanceof
name|List
condition|)
block|{
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
name|Object
argument_list|>
name|tmp
init|=
call|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
call|)
argument_list|(
operator|(
name|List
operator|)
name|searchResult
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|result
operator|=
name|tmp
expr_stmt|;
block|}
else|else
block|{
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
name|Object
argument_list|>
name|tmp
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|searchResult
decl_stmt|;
name|result
operator|=
name|tmp
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|"Name"
argument_list|)
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
DECL|method|testBatch (final SObjectBatch batch)
name|SObjectBatchResponse
name|testBatch
parameter_list|(
specifier|final
name|SObjectBatch
name|batch
parameter_list|)
block|{
specifier|final
name|SObjectBatchResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|batchuri
argument_list|,
name|batch
argument_list|,
name|SObjectBatchResponse
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
name|assertFalse
argument_list|(
literal|"Received errors in: "
operator|+
name|response
argument_list|,
name|response
operator|.
name|hasErrors
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
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
block|}
end_class

end_unit

