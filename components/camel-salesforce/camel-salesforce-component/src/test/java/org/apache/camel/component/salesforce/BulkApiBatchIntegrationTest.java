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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|bulk
operator|.
name|BatchInfo
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
name|bulk
operator|.
name|BatchStateEnum
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
name|bulk
operator|.
name|ContentType
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
name|bulk
operator|.
name|JobInfo
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
name|bulk
operator|.
name|OperationEnum
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
name|junit
operator|.
name|experimental
operator|.
name|theories
operator|.
name|DataPoints
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|experimental
operator|.
name|theories
operator|.
name|Theory
import|;
end_import

begin_class
DECL|class|BulkApiBatchIntegrationTest
specifier|public
class|class
name|BulkApiBatchIntegrationTest
extends|extends
name|AbstractBulkApiTestBase
block|{
DECL|field|TEST_REQUEST_XML
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REQUEST_XML
init|=
literal|"/test-request.xml"
decl_stmt|;
DECL|field|TEST_REQUEST_CSV
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REQUEST_CSV
init|=
literal|"/test-request.csv"
decl_stmt|;
annotation|@
name|DataPoints
DECL|method|getBatches ()
specifier|public
specifier|static
name|BatchTest
index|[]
name|getBatches
parameter_list|()
block|{
name|List
argument_list|<
name|BatchTest
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|BatchTest
argument_list|>
argument_list|()
decl_stmt|;
name|BatchTest
name|test
init|=
operator|new
name|BatchTest
argument_list|()
decl_stmt|;
name|test
operator|.
name|contentType
operator|=
name|ContentType
operator|.
name|XML
expr_stmt|;
name|test
operator|.
name|stream
operator|=
name|BulkApiBatchIntegrationTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_REQUEST_XML
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|test
argument_list|)
expr_stmt|;
name|test
operator|=
operator|new
name|BatchTest
argument_list|()
expr_stmt|;
name|test
operator|.
name|contentType
operator|=
name|ContentType
operator|.
name|CSV
expr_stmt|;
name|test
operator|.
name|stream
operator|=
name|BulkApiBatchIntegrationTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_REQUEST_CSV
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|test
argument_list|)
expr_stmt|;
comment|// TODO test ZIP_XML and ZIP_CSV
return|return
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|BatchTest
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Theory
DECL|method|testBatchLifecycle (BatchTest request)
specifier|public
name|void
name|testBatchLifecycle
parameter_list|(
name|BatchTest
name|request
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Testing Batch lifecycle with {} content"
argument_list|,
name|request
operator|.
name|contentType
argument_list|)
expr_stmt|;
comment|// create an UPSERT test Job for this batch request
name|JobInfo
name|jobInfo
init|=
operator|new
name|JobInfo
argument_list|()
decl_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|UPSERT
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|request
operator|.
name|contentType
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setExternalIdFieldName
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
name|jobInfo
operator|=
name|createJob
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
comment|// test createBatch
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|JOB_ID
argument_list|,
name|jobInfo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|CONTENT_TYPE
argument_list|,
name|jobInfo
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|BatchInfo
name|batchInfo
init|=
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:createBatch"
argument_list|,
name|request
operator|.
name|stream
argument_list|,
name|headers
argument_list|,
name|BatchInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null batch"
argument_list|,
name|batchInfo
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Null batch id"
argument_list|,
name|batchInfo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// test getAllBatches
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|BatchInfo
argument_list|>
name|batches
init|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:getAllBatches"
argument_list|,
name|jobInfo
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null batches"
argument_list|,
name|batches
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Empty batch list"
argument_list|,
name|batches
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// test getBatch
name|batchInfo
operator|=
name|batches
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|batchInfo
operator|=
name|getBatchInfo
argument_list|(
name|batchInfo
argument_list|)
expr_stmt|;
comment|// test getRequest
name|InputStream
name|requestStream
init|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:getRequest"
argument_list|,
name|batchInfo
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null batch request"
argument_list|,
name|requestStream
argument_list|)
expr_stmt|;
comment|// wait for batch to finish
name|log
operator|.
name|info
argument_list|(
literal|"Waiting for batch to finish..."
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|batchProcessed
argument_list|(
name|batchInfo
argument_list|)
condition|)
block|{
comment|// sleep 5 seconds
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// check again
name|batchInfo
operator|=
name|getBatchInfo
argument_list|(
name|batchInfo
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Batch finished with state "
operator|+
name|batchInfo
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Batch did not succeed"
argument_list|,
name|BatchStateEnum
operator|.
name|COMPLETED
argument_list|,
name|batchInfo
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
comment|// test getResults
name|InputStream
name|results
init|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:getResults"
argument_list|,
name|batchInfo
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null batch results"
argument_list|,
name|results
argument_list|)
expr_stmt|;
comment|// close the test job
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:closeJob"
argument_list|,
name|jobInfo
argument_list|,
name|JobInfo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|class|BatchTest
specifier|private
specifier|static
class|class
name|BatchTest
block|{
DECL|field|stream
specifier|public
name|InputStream
name|stream
decl_stmt|;
DECL|field|contentType
specifier|public
name|ContentType
name|contentType
decl_stmt|;
block|}
block|}
end_class

end_unit

