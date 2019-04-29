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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|Metadata
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
name|UriEndpoint
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
name|UriPath
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The azure-blob component is used for storing and retrieving blobs from Azure Storage Blob Service.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.19.0"
argument_list|,
name|scheme
operator|=
literal|"azure-blob"
argument_list|,
name|title
operator|=
literal|"Azure Storage Blob Service"
argument_list|,
name|syntax
operator|=
literal|"azure-blob:containerOrBlobUri"
argument_list|,
name|label
operator|=
literal|"cloud,database,nosql"
argument_list|)
DECL|class|BlobServiceEndpoint
specifier|public
class|class
name|BlobServiceEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Container or Blob compact Uri"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|containerOrBlobUri
specifier|private
name|String
name|containerOrBlobUri
decl_stmt|;
comment|// to support component docs
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|BlobServiceConfiguration
name|configuration
decl_stmt|;
DECL|method|BlobServiceEndpoint (String uri, Component comp, BlobServiceConfiguration configuration)
specifier|public
name|BlobServiceEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|comp
parameter_list|,
name|BlobServiceConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|comp
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Creating a consumer"
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getBlobName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Blob name must be specified."
argument_list|)
throw|;
block|}
name|BlobServiceConsumer
name|consumer
init|=
operator|new
name|BlobServiceConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Creating a producer"
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getBlobName
argument_list|()
operator|==
literal|null
operator|&&
name|getConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
operator|!=
literal|null
operator|&&
name|BlobServiceOperations
operator|.
name|listBlobs
operator|!=
name|configuration
operator|.
name|getOperation
argument_list|()
condition|)
block|{
comment|// Omitting a blob name is only possible it is a (default) listBlobs producer operation
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Blob name must be specified."
argument_list|)
throw|;
block|}
return|return
operator|new
name|BlobServiceProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|BlobServiceConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (BlobServiceConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|BlobServiceConfiguration
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
block|}
end_class

end_unit

