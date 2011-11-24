begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.s3
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|s3
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
name|Date
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|s3
operator|.
name|model
operator|.
name|ObjectMetadata
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|s3
operator|.
name|model
operator|.
name|PutObjectRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|s3
operator|.
name|model
operator|.
name|PutObjectResult
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
name|Exchange
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
name|Message
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
name|impl
operator|.
name|DefaultProducer
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
name|util
operator|.
name|URISupport
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

begin_comment
comment|/**  * A Producer which sends messages to the Amazon Web Service Simple Storage Service  *<a href="http://aws.amazon.com/s3/">AWS S3</a>  */
end_comment

begin_class
DECL|class|S3Producer
specifier|public
class|class
name|S3Producer
extends|extends
name|DefaultProducer
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
name|S3Producer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|S3Producer (Endpoint endpoint)
specifier|public
name|S3Producer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
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
name|ObjectMetadata
name|objectMetadata
init|=
operator|new
name|ObjectMetadata
argument_list|()
decl_stmt|;
name|Long
name|contentLength
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_LENGTH
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentLength
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setContentLength
argument_list|(
name|contentLength
argument_list|)
expr_stmt|;
block|}
name|String
name|contentType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
name|String
name|cacheControl
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CACHE_CONTROL
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheControl
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setCacheControl
argument_list|(
name|cacheControl
argument_list|)
expr_stmt|;
block|}
name|String
name|contentDisposition
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_DISPOSITION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentDisposition
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setContentDisposition
argument_list|(
name|contentDisposition
argument_list|)
expr_stmt|;
block|}
name|String
name|contentEncoding
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentEncoding
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setContentEncoding
argument_list|(
name|contentEncoding
argument_list|)
expr_stmt|;
block|}
name|String
name|contentMD5
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_MD5
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentMD5
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setContentMD5
argument_list|(
name|contentMD5
argument_list|)
expr_stmt|;
block|}
name|Date
name|lastModified
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|LAST_MODIFIED
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastModified
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setLastModified
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
block|}
name|PutObjectRequest
name|putObjectRequest
init|=
operator|new
name|PutObjectRequest
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|,
name|determineKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
name|objectMetadata
argument_list|)
decl_stmt|;
name|String
name|storageClass
init|=
name|determineStorageClass
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|storageClass
operator|!=
literal|null
condition|)
block|{
name|putObjectRequest
operator|.
name|setStorageClass
argument_list|(
name|storageClass
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Put object [{}] from exchange [{}]..."
argument_list|,
name|putObjectRequest
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|PutObjectResult
name|putObjectResult
init|=
name|getEndpoint
argument_list|()
operator|.
name|getS3Client
argument_list|()
operator|.
name|putObject
argument_list|(
name|putObjectRequest
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Received result [{}]"
argument_list|,
name|putObjectResult
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|E_TAG
argument_list|,
name|putObjectResult
operator|.
name|getETag
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|putObjectResult
operator|.
name|getVersionId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|VERSION_ID
argument_list|,
name|putObjectResult
operator|.
name|getVersionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|determineKey (Exchange exchange)
specifier|private
name|String
name|determineKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|key
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"AWS S3 Key header missing."
argument_list|)
throw|;
block|}
return|return
name|key
return|;
block|}
DECL|method|determineStorageClass (Exchange exchange)
specifier|private
name|String
name|determineStorageClass
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|storageClass
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|STORAGE_CLASS
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|storageClass
operator|==
literal|null
condition|)
block|{
name|storageClass
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getStorageClass
argument_list|()
expr_stmt|;
block|}
return|return
name|storageClass
return|;
block|}
DECL|method|getMessageForResponse (Exchange exchange)
specifier|private
name|Message
name|getMessageForResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|S3Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"S3Producer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|S3Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|S3Endpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

