begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.blob
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|azure
operator|.
name|blob
package|;
end_package

begin_interface
DECL|interface|BlobServiceConstants
specifier|public
interface|interface
name|BlobServiceConstants
block|{
DECL|field|OPERATION
name|String
name|OPERATION
init|=
literal|"operation"
decl_stmt|;
DECL|field|BLOB_CLIENT
name|String
name|BLOB_CLIENT
init|=
literal|"AzureBlobClient"
decl_stmt|;
DECL|field|SERVICE_URI_SEGMENT
name|String
name|SERVICE_URI_SEGMENT
init|=
literal|".blob.core.windows.net"
decl_stmt|;
DECL|field|BLOB_SERVICE_REQUEST_OPTIONS
name|String
name|BLOB_SERVICE_REQUEST_OPTIONS
init|=
literal|"BlobServiceRequestOptions"
decl_stmt|;
DECL|field|ACCESS_CONDITION
name|String
name|ACCESS_CONDITION
init|=
literal|"BlobAccessCondition"
decl_stmt|;
DECL|field|BLOB_REQUEST_OPTIONS
name|String
name|BLOB_REQUEST_OPTIONS
init|=
literal|"BlobRequestOptions"
decl_stmt|;
DECL|field|OPERATION_CONTEXT
name|String
name|OPERATION_CONTEXT
init|=
literal|"BlobOperationContext"
decl_stmt|;
DECL|field|BLOB_LISTING_DETAILS
name|String
name|BLOB_LISTING_DETAILS
init|=
literal|"BlobListingDetails"
decl_stmt|;
DECL|field|COMMIT_BLOCK_LIST_LATER
name|String
name|COMMIT_BLOCK_LIST_LATER
init|=
literal|"CommitBlobBlockListLater"
decl_stmt|;
DECL|field|APPEND_BLOCK_CREATED
name|String
name|APPEND_BLOCK_CREATED
init|=
literal|"AppendBlobCreated"
decl_stmt|;
DECL|field|PAGE_BLOCK_CREATED
name|String
name|PAGE_BLOCK_CREATED
init|=
literal|"PageBlobCreated"
decl_stmt|;
DECL|field|PAGE_BLOB_RANGE
name|String
name|PAGE_BLOB_RANGE
init|=
literal|"PageBlobRange"
decl_stmt|;
DECL|field|PAGE_BLOB_SIZE
name|String
name|PAGE_BLOB_SIZE
init|=
literal|"PageBlobSize"
decl_stmt|;
block|}
end_interface

end_unit

