begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.client
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
name|internal
operator|.
name|client
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
name|SalesforceException
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

begin_comment
comment|/**  * Client interface for Salesforce Bulk API  */
end_comment

begin_interface
DECL|interface|BulkApiClient
specifier|public
interface|interface
name|BulkApiClient
block|{
DECL|interface|JobInfoResponseCallback
specifier|public
interface|interface
name|JobInfoResponseCallback
block|{
DECL|method|onResponse (JobInfo jobInfo, Map<String, String> headers, SalesforceException ex)
name|void
name|onResponse
parameter_list|(
name|JobInfo
name|jobInfo
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
function_decl|;
block|}
DECL|interface|BatchInfoResponseCallback
specifier|public
interface|interface
name|BatchInfoResponseCallback
block|{
DECL|method|onResponse (BatchInfo batchInfo, Map<String, String> headers, SalesforceException ex)
name|void
name|onResponse
parameter_list|(
name|BatchInfo
name|batchInfo
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
function_decl|;
block|}
DECL|interface|BatchInfoListResponseCallback
specifier|public
interface|interface
name|BatchInfoListResponseCallback
block|{
DECL|method|onResponse (List<BatchInfo> batchInfoList, Map<String, String> headers, SalesforceException ex)
name|void
name|onResponse
parameter_list|(
name|List
argument_list|<
name|BatchInfo
argument_list|>
name|batchInfoList
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
function_decl|;
block|}
DECL|interface|StreamResponseCallback
specifier|public
interface|interface
name|StreamResponseCallback
block|{
DECL|method|onResponse (InputStream inputStream, Map<String, String> headers, SalesforceException ex)
name|void
name|onResponse
parameter_list|(
name|InputStream
name|inputStream
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
function_decl|;
block|}
DECL|interface|QueryResultIdsCallback
specifier|public
interface|interface
name|QueryResultIdsCallback
block|{
DECL|method|onResponse (List<String> ids, Map<String, String> headers, SalesforceException ex)
name|void
name|onResponse
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
function_decl|;
block|}
comment|/**      * Creates a Bulk Job      *      * @param jobInfo {@link JobInfo} with required fields      * @param callback {@link JobInfoResponseCallback} to be invoked on response      *            or error      */
DECL|method|createJob (JobInfo jobInfo, Map<String, List<String>> header, JobInfoResponseCallback callback)
name|void
name|createJob
parameter_list|(
name|JobInfo
name|jobInfo
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|JobInfoResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getJob (String jobId, Map<String, List<String>> header, JobInfoResponseCallback callback)
name|void
name|getJob
parameter_list|(
name|String
name|jobId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|JobInfoResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|closeJob (String jobId, Map<String, List<String>> header, JobInfoResponseCallback callback)
name|void
name|closeJob
parameter_list|(
name|String
name|jobId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|JobInfoResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|abortJob (String jobId, Map<String, List<String>> header, JobInfoResponseCallback callback)
name|void
name|abortJob
parameter_list|(
name|String
name|jobId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|JobInfoResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|createBatch (InputStream batchStream, String jobId, ContentType contentTypeEnum, Map<String, List<String>> header, BatchInfoResponseCallback callback)
name|void
name|createBatch
parameter_list|(
name|InputStream
name|batchStream
parameter_list|,
name|String
name|jobId
parameter_list|,
name|ContentType
name|contentTypeEnum
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|BatchInfoResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getBatch (String jobId, String batchId, Map<String, List<String>> header, BatchInfoResponseCallback callback)
name|void
name|getBatch
parameter_list|(
name|String
name|jobId
parameter_list|,
name|String
name|batchId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|BatchInfoResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getAllBatches (String jobId, Map<String, List<String>> header, BatchInfoListResponseCallback callback)
name|void
name|getAllBatches
parameter_list|(
name|String
name|jobId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|BatchInfoListResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getRequest (String jobId, String batchId, Map<String, List<String>> header, StreamResponseCallback callback)
name|void
name|getRequest
parameter_list|(
name|String
name|jobId
parameter_list|,
name|String
name|batchId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|StreamResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getResults (String jobId, String batchId, Map<String, List<String>> header, StreamResponseCallback callback)
name|void
name|getResults
parameter_list|(
name|String
name|jobId
parameter_list|,
name|String
name|batchId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|StreamResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|createBatchQuery (String jobId, String soqlQuery, ContentType jobContentType, Map<String, List<String>> header, BatchInfoResponseCallback callback)
name|void
name|createBatchQuery
parameter_list|(
name|String
name|jobId
parameter_list|,
name|String
name|soqlQuery
parameter_list|,
name|ContentType
name|jobContentType
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|BatchInfoResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getQueryResultIds (String jobId, String batchId, Map<String, List<String>> header, QueryResultIdsCallback callback)
name|void
name|getQueryResultIds
parameter_list|(
name|String
name|jobId
parameter_list|,
name|String
name|batchId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|QueryResultIdsCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getQueryResult (String jobId, String batchId, String resultId, Map<String, List<String>> header, StreamResponseCallback callback)
name|void
name|getQueryResult
parameter_list|(
name|String
name|jobId
parameter_list|,
name|String
name|batchId
parameter_list|,
name|String
name|resultId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
parameter_list|,
name|StreamResponseCallback
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

