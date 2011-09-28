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
name|ByteArrayOutputStream
import|;
end_import

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
name|ObjectOutputStream
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
name|Exchange
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|JcloudsBlobStoreProducer
specifier|public
class|class
name|JcloudsBlobStoreProducer
extends|extends
name|JcloudsProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JcloudsBlobStoreProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|blobStore
specifier|private
name|BlobStore
name|blobStore
decl_stmt|;
DECL|method|JcloudsBlobStoreProducer (JcloudsEndpoint endpoint, BlobStore blobStore)
specifier|public
name|JcloudsBlobStoreProducer
parameter_list|(
name|JcloudsEndpoint
name|endpoint
parameter_list|,
name|BlobStore
name|blobStore
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|blobStore
operator|=
name|blobStore
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|container
init|=
name|getContainerName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|blobName
init|=
name|getBlobName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|operation
init|=
name|getOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|JcloudsConstants
operator|.
name|GET
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|JcloudsBlobStoreHelper
operator|.
name|readBlob
argument_list|(
name|blobStore
argument_list|,
name|container
argument_list|,
name|blobName
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|JcloudsBlobStoreHelper
operator|.
name|writeBlob
argument_list|(
name|blobStore
argument_list|,
name|container
argument_list|,
name|blobName
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Retrieves the blobName from the URI or from the exchange headers. The header will take precedence over the URI.      *      * @param exchange      * @return      */
DECL|method|getBlobName (Exchange exchange)
specifier|protected
name|String
name|getBlobName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|blobName
init|=
operator|(
operator|(
name|JcloudsBlobStoreEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getBlobName
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|BLOB_NAME
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|blobName
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|BLOB_NAME
argument_list|)
expr_stmt|;
block|}
return|return
name|blobName
return|;
block|}
comment|/**      * Retrieves the containerName from the URI or from the exchange headers. The header will take precedence over the URI.      *      * @param exchange      * @return      */
DECL|method|getContainerName (Exchange exchange)
specifier|protected
name|String
name|getContainerName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|containerName
init|=
operator|(
operator|(
name|JcloudsBlobStoreEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getContainer
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|CONTAINER_NAME
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|containerName
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
block|}
return|return
name|containerName
return|;
block|}
comment|/**      * Retrieves the operation from the URI or from the exchange headers. The header will take precedence over the URI.      *      * @param exchange      * @return      */
DECL|method|getOperation (Exchange exchange)
specifier|public
name|String
name|getOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|operation
init|=
operator|(
operator|(
name|JcloudsBlobStoreEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getOperation
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|OPERATION
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|operation
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|OPERATION
argument_list|)
expr_stmt|;
block|}
return|return
name|operation
return|;
block|}
block|}
end_class

end_unit

