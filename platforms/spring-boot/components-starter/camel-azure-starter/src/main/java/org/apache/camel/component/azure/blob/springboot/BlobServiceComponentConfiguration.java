begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.blob.springboot
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
operator|.
name|springboot
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|StorageCredentials
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
name|component
operator|.
name|azure
operator|.
name|blob
operator|.
name|BlobServiceOperations
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
name|blob
operator|.
name|BlobType
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The azure-blob component is used for storing and retrieving blobs from Azure  * Storage Blob Service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.azure-blob"
argument_list|)
DECL|class|BlobServiceComponentConfiguration
specifier|public
class|class
name|BlobServiceComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the azure-blob component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The Blob Service configuration      */
DECL|field|configuration
specifier|private
name|BlobServiceConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|BlobServiceConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( BlobServiceConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|BlobServiceConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|BlobServiceConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|BlobServiceConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
operator|.
name|BlobServiceConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Blob service operation hint to the producer          */
DECL|field|operation
specifier|private
name|BlobServiceOperations
name|operation
init|=
name|BlobServiceOperations
operator|.
name|listBlobs
decl_stmt|;
comment|/**          * Set the blob service container name          */
DECL|field|containerName
specifier|private
name|String
name|containerName
decl_stmt|;
comment|/**          * Blob name, required for most operations          */
DECL|field|blobName
specifier|private
name|String
name|blobName
decl_stmt|;
comment|/**          * Set a blob type, 'blockblob' is default          */
DECL|field|blobType
specifier|private
name|BlobType
name|blobType
init|=
name|BlobType
operator|.
name|blockblob
decl_stmt|;
comment|/**          * Set the size of the buffer for writing block and page blocks          */
DECL|field|streamWriteSize
specifier|private
name|Integer
name|streamWriteSize
decl_stmt|;
comment|/**          * Set the minimum read size in bytes when reading the blob content          */
DECL|field|streamReadSize
specifier|private
name|Integer
name|streamReadSize
decl_stmt|;
comment|/**          * Set the blob meta-data          */
DECL|field|blobMetadata
specifier|private
name|Map
name|blobMetadata
decl_stmt|;
comment|/**          * The blob service client          */
DECL|field|azureBlobClient
specifier|private
name|CloudBlob
name|azureBlobClient
decl_stmt|;
comment|/**          * Close the stream after write or keep it open, default is true          */
DECL|field|closeStreamAfterWrite
specifier|private
name|Boolean
name|closeStreamAfterWrite
init|=
literal|true
decl_stmt|;
comment|/**          * Close the stream after read or keep it open, default is true          */
DECL|field|closeStreamAfterRead
specifier|private
name|Boolean
name|closeStreamAfterRead
init|=
literal|true
decl_stmt|;
comment|/**          * Set the file directory where the downloaded blobs will be saved to          */
DECL|field|fileDir
specifier|private
name|String
name|fileDir
decl_stmt|;
comment|/**          * Set the blob offset for the upload or download operations, default is          * 0          */
DECL|field|blobOffset
specifier|private
name|Long
name|blobOffset
init|=
literal|0L
decl_stmt|;
comment|/**          * Set the data length for the download or page blob upload operations          */
DECL|field|dataLength
specifier|private
name|Long
name|dataLength
decl_stmt|;
comment|/**          * Set a prefix which can be used for listing the blobs          */
DECL|field|blobPrefix
specifier|private
name|String
name|blobPrefix
decl_stmt|;
comment|/**          * Storage resources can be public for reading their content, if this          * property is enabled then the credentials do not have to be set          */
DECL|field|publicForRead
specifier|private
name|Boolean
name|publicForRead
init|=
literal|false
decl_stmt|;
comment|/**          * Specify if the flat or hierarchical blob listing should be used          */
DECL|field|useFlatListing
specifier|private
name|Boolean
name|useFlatListing
init|=
literal|true
decl_stmt|;
comment|/**          * Set the Azure account name          */
DECL|field|accountName
specifier|private
name|String
name|accountName
decl_stmt|;
comment|/**          * Set the storage credentials, required in most cases          */
DECL|field|credentials
specifier|private
name|StorageCredentials
name|credentials
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
name|Integer
name|getStreamWriteSize
parameter_list|()
block|{
return|return
name|streamWriteSize
return|;
block|}
DECL|method|setStreamWriteSize (Integer streamWriteSize)
specifier|public
name|void
name|setStreamWriteSize
parameter_list|(
name|Integer
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
name|Integer
name|getStreamReadSize
parameter_list|()
block|{
return|return
name|streamReadSize
return|;
block|}
DECL|method|setStreamReadSize (Integer streamReadSize)
specifier|public
name|void
name|setStreamReadSize
parameter_list|(
name|Integer
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
name|getBlobMetadata
parameter_list|()
block|{
return|return
name|blobMetadata
return|;
block|}
DECL|method|setBlobMetadata (Map blobMetadata)
specifier|public
name|void
name|setBlobMetadata
parameter_list|(
name|Map
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
DECL|method|getCloseStreamAfterWrite ()
specifier|public
name|Boolean
name|getCloseStreamAfterWrite
parameter_list|()
block|{
return|return
name|closeStreamAfterWrite
return|;
block|}
DECL|method|setCloseStreamAfterWrite (Boolean closeStreamAfterWrite)
specifier|public
name|void
name|setCloseStreamAfterWrite
parameter_list|(
name|Boolean
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
DECL|method|getCloseStreamAfterRead ()
specifier|public
name|Boolean
name|getCloseStreamAfterRead
parameter_list|()
block|{
return|return
name|closeStreamAfterRead
return|;
block|}
DECL|method|setCloseStreamAfterRead (Boolean closeStreamAfterRead)
specifier|public
name|void
name|setCloseStreamAfterRead
parameter_list|(
name|Boolean
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
DECL|method|setBlobOffset (Long blobOffset)
specifier|public
name|void
name|setBlobOffset
parameter_list|(
name|Long
name|blobOffset
parameter_list|)
block|{
name|this
operator|.
name|blobOffset
operator|=
name|blobOffset
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
DECL|method|getPublicForRead ()
specifier|public
name|Boolean
name|getPublicForRead
parameter_list|()
block|{
return|return
name|publicForRead
return|;
block|}
DECL|method|setPublicForRead (Boolean publicForRead)
specifier|public
name|void
name|setPublicForRead
parameter_list|(
name|Boolean
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
DECL|method|getUseFlatListing ()
specifier|public
name|Boolean
name|getUseFlatListing
parameter_list|()
block|{
return|return
name|useFlatListing
return|;
block|}
DECL|method|setUseFlatListing (Boolean useFlatListing)
specifier|public
name|void
name|setUseFlatListing
parameter_list|(
name|Boolean
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
DECL|method|getAccountName ()
specifier|public
name|String
name|getAccountName
parameter_list|()
block|{
return|return
name|accountName
return|;
block|}
DECL|method|setAccountName (String accountName)
specifier|public
name|void
name|setAccountName
parameter_list|(
name|String
name|accountName
parameter_list|)
block|{
name|this
operator|.
name|accountName
operator|=
name|accountName
expr_stmt|;
block|}
DECL|method|getCredentials ()
specifier|public
name|StorageCredentials
name|getCredentials
parameter_list|()
block|{
return|return
name|credentials
return|;
block|}
DECL|method|setCredentials (StorageCredentials credentials)
specifier|public
name|void
name|setCredentials
parameter_list|(
name|StorageCredentials
name|credentials
parameter_list|)
block|{
name|this
operator|.
name|credentials
operator|=
name|credentials
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

