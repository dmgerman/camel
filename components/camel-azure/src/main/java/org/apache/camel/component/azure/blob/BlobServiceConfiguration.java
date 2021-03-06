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
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|blob
operator|.
name|CloudBlob
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
name|RuntimeCamelException
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
name|azure
operator|.
name|common
operator|.
name|AbstractConfiguration
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|BlobServiceConfiguration
specifier|public
class|class
name|BlobServiceConfiguration
extends|extends
name|AbstractConfiguration
block|{
DECL|field|containerName
specifier|private
name|String
name|containerName
decl_stmt|;
DECL|field|blobName
specifier|private
name|String
name|blobName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|azureBlobClient
specifier|private
name|CloudBlob
name|azureBlobClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"blockblob"
argument_list|)
DECL|field|blobType
specifier|private
name|BlobType
name|blobType
init|=
name|BlobType
operator|.
name|blockblob
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"listBlobs"
argument_list|)
DECL|field|operation
specifier|private
name|BlobServiceOperations
name|operation
init|=
name|BlobServiceOperations
operator|.
name|listBlobs
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|streamWriteSize
specifier|private
name|int
name|streamWriteSize
decl_stmt|;
annotation|@
name|UriParam
DECL|field|streamReadSize
specifier|private
name|int
name|streamReadSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|blobMetadata
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|blobMetadata
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|closeStreamAfterRead
specifier|private
name|boolean
name|closeStreamAfterRead
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|closeStreamAfterWrite
specifier|private
name|boolean
name|closeStreamAfterWrite
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|fileDir
specifier|private
name|String
name|fileDir
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|blobOffset
specifier|private
name|Long
name|blobOffset
init|=
literal|0L
decl_stmt|;
annotation|@
name|UriParam
DECL|field|dataLength
specifier|private
name|Long
name|dataLength
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|blobPrefix
specifier|private
name|String
name|blobPrefix
decl_stmt|;
annotation|@
name|UriParam
DECL|field|publicForRead
specifier|private
name|boolean
name|publicForRead
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|useFlatListing
specifier|private
name|boolean
name|useFlatListing
init|=
literal|true
decl_stmt|;
DECL|method|getOperation ()
specifier|public
name|BlobServiceOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Blob service operation hint to the producer       */
DECL|method|setOperation (BlobServiceOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|BlobServiceOperations
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getContainerName ()
specifier|public
name|String
name|getContainerName
parameter_list|()
block|{
return|return
name|containerName
return|;
block|}
comment|/**      * Set the blob service container name       */
DECL|method|setContainerName (String containerName)
specifier|public
name|void
name|setContainerName
parameter_list|(
name|String
name|containerName
parameter_list|)
block|{
name|this
operator|.
name|containerName
operator|=
name|containerName
expr_stmt|;
block|}
DECL|method|getBlobName ()
specifier|public
name|String
name|getBlobName
parameter_list|()
block|{
return|return
name|blobName
return|;
block|}
comment|/**      * Blob name, required for most operations      */
DECL|method|setBlobName (String blobName)
specifier|public
name|void
name|setBlobName
parameter_list|(
name|String
name|blobName
parameter_list|)
block|{
name|this
operator|.
name|blobName
operator|=
name|blobName
expr_stmt|;
block|}
DECL|method|getBlobType ()
specifier|public
name|BlobType
name|getBlobType
parameter_list|()
block|{
return|return
name|blobType
return|;
block|}
comment|/**      * Set a blob type, 'blockblob' is default      */
DECL|method|setBlobType (BlobType blobType)
specifier|public
name|void
name|setBlobType
parameter_list|(
name|BlobType
name|blobType
parameter_list|)
block|{
name|this
operator|.
name|blobType
operator|=
name|blobType
expr_stmt|;
block|}
DECL|method|getStreamWriteSize ()
specifier|public
name|int
name|getStreamWriteSize
parameter_list|()
block|{
return|return
name|streamWriteSize
return|;
block|}
comment|/**      * Set the size of the buffer for writing block and page blocks       */
DECL|method|setStreamWriteSize (int streamWriteSize)
specifier|public
name|void
name|setStreamWriteSize
parameter_list|(
name|int
name|streamWriteSize
parameter_list|)
block|{
name|this
operator|.
name|streamWriteSize
operator|=
name|streamWriteSize
expr_stmt|;
block|}
DECL|method|getStreamReadSize ()
specifier|public
name|int
name|getStreamReadSize
parameter_list|()
block|{
return|return
name|streamReadSize
return|;
block|}
comment|/**      * Set the minimum read size in bytes when reading the blob content       */
DECL|method|setStreamReadSize (int streamReadSize)
specifier|public
name|void
name|setStreamReadSize
parameter_list|(
name|int
name|streamReadSize
parameter_list|)
block|{
name|this
operator|.
name|streamReadSize
operator|=
name|streamReadSize
expr_stmt|;
block|}
DECL|method|getBlobMetadata ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getBlobMetadata
parameter_list|()
block|{
return|return
name|blobMetadata
return|;
block|}
comment|/**      * Set the blob meta-data       */
DECL|method|setBlobMetadata (Map<String, String> blobMetadata)
specifier|public
name|void
name|setBlobMetadata
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|blobMetadata
parameter_list|)
block|{
name|this
operator|.
name|blobMetadata
operator|=
name|blobMetadata
expr_stmt|;
block|}
DECL|method|getAzureBlobClient ()
specifier|public
name|CloudBlob
name|getAzureBlobClient
parameter_list|()
block|{
return|return
name|azureBlobClient
return|;
block|}
comment|/**      * The blob service client       */
DECL|method|setAzureBlobClient (CloudBlob azureBlobClient)
specifier|public
name|void
name|setAzureBlobClient
parameter_list|(
name|CloudBlob
name|azureBlobClient
parameter_list|)
block|{
name|this
operator|.
name|azureBlobClient
operator|=
name|azureBlobClient
expr_stmt|;
block|}
DECL|method|isCloseStreamAfterWrite ()
specifier|public
name|boolean
name|isCloseStreamAfterWrite
parameter_list|()
block|{
return|return
name|closeStreamAfterWrite
return|;
block|}
comment|/**      * Close the stream after write or keep it open, default is true      */
DECL|method|setCloseStreamAfterWrite (boolean closeStreamAfterWrite)
specifier|public
name|void
name|setCloseStreamAfterWrite
parameter_list|(
name|boolean
name|closeStreamAfterWrite
parameter_list|)
block|{
name|this
operator|.
name|closeStreamAfterWrite
operator|=
name|closeStreamAfterWrite
expr_stmt|;
block|}
DECL|method|isCloseStreamAfterRead ()
specifier|public
name|boolean
name|isCloseStreamAfterRead
parameter_list|()
block|{
return|return
name|closeStreamAfterRead
return|;
block|}
comment|/**      * Close the stream after read or keep it open, default is true      */
DECL|method|setCloseStreamAfterRead (boolean closeStreamAfterRead)
specifier|public
name|void
name|setCloseStreamAfterRead
parameter_list|(
name|boolean
name|closeStreamAfterRead
parameter_list|)
block|{
name|this
operator|.
name|closeStreamAfterRead
operator|=
name|closeStreamAfterRead
expr_stmt|;
block|}
DECL|method|getFileDir ()
specifier|public
name|String
name|getFileDir
parameter_list|()
block|{
return|return
name|fileDir
return|;
block|}
comment|/**      * Set the file directory where the downloaded blobs will be saved to       */
DECL|method|setFileDir (String fileDir)
specifier|public
name|void
name|setFileDir
parameter_list|(
name|String
name|fileDir
parameter_list|)
block|{
name|this
operator|.
name|fileDir
operator|=
name|fileDir
expr_stmt|;
block|}
DECL|method|getBlobOffset ()
specifier|public
name|Long
name|getBlobOffset
parameter_list|()
block|{
return|return
name|blobOffset
return|;
block|}
comment|/**      *  Set the blob offset for the upload or download operations, default is 0       */
DECL|method|setBlobOffset (Long dataOffset)
specifier|public
name|void
name|setBlobOffset
parameter_list|(
name|Long
name|dataOffset
parameter_list|)
block|{
name|this
operator|.
name|blobOffset
operator|=
name|dataOffset
expr_stmt|;
block|}
DECL|method|getDataLength ()
specifier|public
name|Long
name|getDataLength
parameter_list|()
block|{
return|return
name|dataLength
return|;
block|}
comment|/**      * Set the data length for the download or page blob upload operations       */
DECL|method|setDataLength (Long dataLength)
specifier|public
name|void
name|setDataLength
parameter_list|(
name|Long
name|dataLength
parameter_list|)
block|{
name|this
operator|.
name|dataLength
operator|=
name|dataLength
expr_stmt|;
block|}
DECL|method|getBlobPrefix ()
specifier|public
name|String
name|getBlobPrefix
parameter_list|()
block|{
return|return
name|blobPrefix
return|;
block|}
comment|/**      * Set a prefix which can be used for listing the blobs       */
DECL|method|setBlobPrefix (String blobPrefix)
specifier|public
name|void
name|setBlobPrefix
parameter_list|(
name|String
name|blobPrefix
parameter_list|)
block|{
name|this
operator|.
name|blobPrefix
operator|=
name|blobPrefix
expr_stmt|;
block|}
DECL|method|isPublicForRead ()
specifier|public
name|boolean
name|isPublicForRead
parameter_list|()
block|{
return|return
name|publicForRead
return|;
block|}
comment|/**      * Storage resources can be public for reading their content, if this property is enabled      * then the credentials do not have to be set      */
DECL|method|setPublicForRead (boolean publicForRead)
specifier|public
name|void
name|setPublicForRead
parameter_list|(
name|boolean
name|publicForRead
parameter_list|)
block|{
name|this
operator|.
name|publicForRead
operator|=
name|publicForRead
expr_stmt|;
block|}
DECL|method|isUseFlatListing ()
specifier|public
name|boolean
name|isUseFlatListing
parameter_list|()
block|{
return|return
name|useFlatListing
return|;
block|}
comment|/**      * Specify if the flat or hierarchical blob listing should be used       */
DECL|method|setUseFlatListing (boolean useFlatListing)
specifier|public
name|void
name|setUseFlatListing
parameter_list|(
name|boolean
name|useFlatListing
parameter_list|)
block|{
name|this
operator|.
name|useFlatListing
operator|=
name|useFlatListing
expr_stmt|;
block|}
comment|// *************************************************
comment|//
comment|// *************************************************
DECL|method|copy ()
specifier|public
name|BlobServiceConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|BlobServiceConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

