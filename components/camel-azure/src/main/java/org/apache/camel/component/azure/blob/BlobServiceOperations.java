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

begin_enum
DECL|enum|BlobServiceOperations
specifier|public
enum|enum
name|BlobServiceOperations
block|{
comment|/**      * Common to all block types      */
comment|// Get the content of the blob, can be restricted to a blob range
DECL|enumConstant|getBlob
name|getBlob
block|,
comment|// Delete the blob
DECL|enumConstant|deleteBlob
name|deleteBlob
block|,
comment|// List the blobs
DECL|enumConstant|listBlobs
name|listBlobs
block|,
comment|/*      * Bloc blob operations      */
comment|// Put a block blob content which either creates a new block blob
comment|// or overwrites the existing block blob content
DECL|enumConstant|updateBlockBlob
name|updateBlockBlob
block|,
comment|// Upload a block blob content as a sequence of blob blocks first and then
comment|// commit them to a blob. The commit can be executed later with the
comment|// commitBlobBlockList operation if a message "CommitBlockListLater"
comment|// property is enabled. Individual block blobs can be updated later.
DECL|enumConstant|uploadBlobBlocks
name|uploadBlobBlocks
block|,
comment|// Commit a sequence of blob blocks to the block list which was previously
comment|// uploaded to the blob service with the putBlockBlob operation with the commit
comment|// being delayed
DECL|enumConstant|commitBlobBlockList
name|commitBlobBlockList
block|,
comment|// Get the block blob list,
DECL|enumConstant|getBlobBlockList
name|getBlobBlockList
block|,
comment|/*      * Append blob operations      */
comment|// Create an append block. By default if the block already exists then it is not reset.
comment|// Note the updateAppendBlob will also try to create an append blob first unless
comment|// a message "AppendBlobCreated" property is enabled
DECL|enumConstant|createAppendBlob
name|createAppendBlob
block|,
comment|// Create an append block unless a message "AppendBlobCreated" property is enabled and no
comment|// the identically named block already exists and append the new content to this blob.
DECL|enumConstant|updateAppendBlob
name|updateAppendBlob
block|,
comment|/**      * Page Block operations      */
comment|// Create a page block. By default if the block already exists then it is not reset.
comment|// Note the updatePageBlob will also try to create a page blob first unless
comment|// a message "PageBlobCreated" property is enabled
DECL|enumConstant|createPageBlob
name|createPageBlob
block|,
comment|// Create a page block unless a message "PageBlobCreated" property is enabled and no
comment|// the identically named block already exists and set the content of this blob.
DECL|enumConstant|updatePageBlob
name|updatePageBlob
block|,
comment|// Resize the page blob
DECL|enumConstant|resizePageBlob
name|resizePageBlob
block|,
comment|// Clear the page blob
DECL|enumConstant|clearPageBlob
name|clearPageBlob
block|,
comment|// Get the page blob page ranges
DECL|enumConstant|getPageBlobRanges
name|getPageBlobRanges
block|}
end_enum

end_unit

