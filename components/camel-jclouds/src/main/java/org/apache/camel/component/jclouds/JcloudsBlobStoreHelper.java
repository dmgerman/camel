begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jclouds
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jclouds
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|blobstore
operator|.
name|BlobStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|blobstore
operator|.
name|domain
operator|.
name|Blob
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|blobstore
operator|.
name|util
operator|.
name|BlobStoreUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|domain
operator|.
name|Location
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|io
operator|.
name|Payload
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|jclouds
operator|.
name|blobstore
operator|.
name|options
operator|.
name|PutOptions
operator|.
name|Builder
operator|.
name|multipart
import|;
end_import

begin_class
DECL|class|JcloudsBlobStoreHelper
specifier|public
specifier|final
class|class
name|JcloudsBlobStoreHelper
block|{
DECL|method|JcloudsBlobStoreHelper ()
specifier|private
name|JcloudsBlobStoreHelper
parameter_list|()
block|{
comment|//Utility Class
block|}
comment|/**      * Creates all directories that are part of the blobName.      */
DECL|method|mkDirs (BlobStore blobStore, String container, String blobName)
specifier|public
specifier|static
name|void
name|mkDirs
parameter_list|(
name|BlobStore
name|blobStore
parameter_list|,
name|String
name|container
parameter_list|,
name|String
name|blobName
parameter_list|)
block|{
if|if
condition|(
name|blobStore
operator|!=
literal|null
operator|&&
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|blobName
argument_list|)
operator|&&
name|blobName
operator|.
name|contains
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|String
name|directory
init|=
name|BlobStoreUtils
operator|.
name|parseDirectoryFromPath
argument_list|(
name|blobName
argument_list|)
decl_stmt|;
name|blobStore
operator|.
name|createDirectory
argument_list|(
name|container
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Checks if container exists and creates one if not.      *      * @param blobStore  The {@link BlobStore} to use.      * @param container  The container name to check against.      * @param locationId The locationId to create the container if not found.      */
DECL|method|ensureContainerExists (BlobStore blobStore, String container, String locationId)
specifier|public
specifier|static
name|void
name|ensureContainerExists
parameter_list|(
name|BlobStore
name|blobStore
parameter_list|,
name|String
name|container
parameter_list|,
name|String
name|locationId
parameter_list|)
block|{
if|if
condition|(
name|blobStore
operator|!=
literal|null
operator|&&
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|container
argument_list|)
operator|&&
operator|!
name|blobStore
operator|.
name|containerExists
argument_list|(
name|container
argument_list|)
condition|)
block|{
name|blobStore
operator|.
name|createContainerInLocation
argument_list|(
name|getLocationById
argument_list|(
name|blobStore
argument_list|,
name|locationId
argument_list|)
argument_list|,
name|container
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the {@link Location} that matches the locationId.      */
DECL|method|getLocationById (BlobStore blobStore, String locationId)
specifier|public
specifier|static
name|Location
name|getLocationById
parameter_list|(
name|BlobStore
name|blobStore
parameter_list|,
name|String
name|locationId
parameter_list|)
block|{
if|if
condition|(
name|blobStore
operator|!=
literal|null
operator|&&
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|locationId
argument_list|)
condition|)
block|{
for|for
control|(
name|Location
name|location
range|:
name|blobStore
operator|.
name|listAssignableLocations
argument_list|()
control|)
block|{
if|if
condition|(
name|locationId
operator|.
name|equals
argument_list|(
name|location
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|location
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Writes {@link Payload} to the the {@link BlobStore}.      */
DECL|method|writeBlob (BlobStore blobStore, String container, String blobName, Payload payload)
specifier|public
specifier|static
name|void
name|writeBlob
parameter_list|(
name|BlobStore
name|blobStore
parameter_list|,
name|String
name|container
parameter_list|,
name|String
name|blobName
parameter_list|,
name|Payload
name|payload
parameter_list|)
block|{
if|if
condition|(
name|blobName
operator|!=
literal|null
operator|&&
name|payload
operator|!=
literal|null
condition|)
block|{
name|mkDirs
argument_list|(
name|blobStore
argument_list|,
name|container
argument_list|,
name|blobName
argument_list|)
expr_stmt|;
name|Blob
name|blob
init|=
name|blobStore
operator|.
name|blobBuilder
argument_list|(
name|blobName
argument_list|)
operator|.
name|payload
argument_list|(
name|payload
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM
argument_list|)
operator|.
name|contentDisposition
argument_list|(
name|blobName
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|blobStore
operator|.
name|putBlob
argument_list|(
name|container
argument_list|,
name|blob
argument_list|,
name|multipart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Reads from a {@link BlobStore}. It returns an Object.      */
DECL|method|readBlob (BlobStore blobStore, String container, String blobName)
specifier|public
specifier|static
name|InputStream
name|readBlob
parameter_list|(
name|BlobStore
name|blobStore
parameter_list|,
name|String
name|container
parameter_list|,
name|String
name|blobName
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|blobName
argument_list|)
condition|)
block|{
name|Blob
name|blob
init|=
name|blobStore
operator|.
name|getBlob
argument_list|(
name|container
argument_list|,
name|blobName
argument_list|)
decl_stmt|;
if|if
condition|(
name|blob
operator|!=
literal|null
operator|&&
name|blob
operator|.
name|getPayload
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
name|blobStore
operator|.
name|getBlob
argument_list|(
name|container
argument_list|,
name|blobName
argument_list|)
operator|.
name|getPayload
argument_list|()
operator|.
name|openStream
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|is
return|;
block|}
comment|/**      * Return the count of all the blobs in the container      */
DECL|method|countBlob (BlobStore blobStore, String container)
specifier|public
specifier|static
name|long
name|countBlob
parameter_list|(
name|BlobStore
name|blobStore
parameter_list|,
name|String
name|container
parameter_list|)
block|{
name|long
name|blobsCount
init|=
name|blobStore
operator|.
name|countBlobs
argument_list|(
name|container
argument_list|)
decl_stmt|;
return|return
name|blobsCount
return|;
block|}
block|}
end_class

end_unit

