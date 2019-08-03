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
name|java
operator|.
name|util
operator|.
name|Set
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
name|StorageCredentialsAnonymous
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
name|CamelContext
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
name|Endpoint
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
name|annotations
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"azure-blob"
argument_list|)
DECL|class|BlobServiceComponent
specifier|public
class|class
name|BlobServiceComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|BlobServiceConfiguration
name|configuration
decl_stmt|;
DECL|method|BlobServiceComponent ()
specifier|public
name|BlobServiceComponent
parameter_list|()
block|{     }
DECL|method|BlobServiceComponent (CamelContext context)
specifier|public
name|BlobServiceComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|BlobServiceConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|BlobServiceConfiguration
name|configuration
init|=
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|String
index|[]
name|parts
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|remaining
operator|!=
literal|null
condition|)
block|{
name|parts
operator|=
name|remaining
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parts
operator|==
literal|null
operator|||
name|parts
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"At least the account and container names must be specified."
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|setAccountName
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setContainerName
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|2
condition|)
block|{
comment|// Blob names can contain forward slashes
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|2
init|;
name|i
operator|<
name|parts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|parts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|parts
operator|.
name|length
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
block|}
name|configuration
operator|.
name|setBlobName
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|checkAndSetRegistryClient
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|checkCredentials
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|BlobServiceEndpoint
name|endpoint
init|=
operator|new
name|BlobServiceEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
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
comment|/**      * The Blob Service configuration      */
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
DECL|method|checkCredentials (BlobServiceConfiguration cfg)
specifier|private
name|void
name|checkCredentials
parameter_list|(
name|BlobServiceConfiguration
name|cfg
parameter_list|)
block|{
name|CloudBlob
name|client
init|=
name|cfg
operator|.
name|getAzureBlobClient
argument_list|()
decl_stmt|;
name|StorageCredentials
name|creds
init|=
name|client
operator|==
literal|null
condition|?
name|cfg
operator|.
name|getCredentials
argument_list|()
else|:
name|client
operator|.
name|getServiceClient
argument_list|()
operator|.
name|getCredentials
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|creds
operator|==
literal|null
operator|||
name|creds
operator|instanceof
name|StorageCredentialsAnonymous
operator|)
operator|&&
operator|!
name|cfg
operator|.
name|isPublicForRead
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Credentials must be specified."
argument_list|)
throw|;
block|}
block|}
DECL|method|checkAndSetRegistryClient (BlobServiceConfiguration configuration)
specifier|private
name|void
name|checkAndSetRegistryClient
parameter_list|(
name|BlobServiceConfiguration
name|configuration
parameter_list|)
block|{
name|Set
argument_list|<
name|CloudBlob
argument_list|>
name|clients
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|CloudBlob
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|clients
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|configuration
operator|.
name|setAzureBlobClient
argument_list|(
name|clients
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

