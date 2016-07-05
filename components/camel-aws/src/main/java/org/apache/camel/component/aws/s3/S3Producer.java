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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|cloudfront
operator|.
name|model
operator|.
name|InvalidArgumentException
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
name|AmazonS3
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
name|AbortMultipartUploadRequest
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
name|AccessControlList
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
name|CannedAccessControlList
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
name|CompleteMultipartUploadRequest
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
name|CompleteMultipartUploadResult
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
name|CopyObjectRequest
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
name|CopyObjectResult
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
name|InitiateMultipartUploadRequest
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
name|InitiateMultipartUploadResult
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
name|PartETag
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
name|StorageClass
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
name|UploadPartRequest
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
name|WrappedFile
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
name|aws
operator|.
name|ec2
operator|.
name|EC2Constants
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
name|CastUtils
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
name|FileUtil
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
name|IOHelper
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
name|ObjectHelper
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

begin_import
import|import static
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
name|common
operator|.
name|AwsExchangeUtil
operator|.
name|getMessageForResponse
import|;
end_import

begin_comment
comment|/**  * A Producer which sends messages to the Amazon Web Service Simple Storage Service<a  * href="http://aws.amazon.com/s3/">AWS S3</a>  */
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
DECL|field|s3ProducerToString
specifier|private
specifier|transient
name|String
name|s3ProducerToString
decl_stmt|;
DECL|method|S3Producer (final Endpoint endpoint)
specifier|public
name|S3Producer
parameter_list|(
specifier|final
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
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|S3Operations
name|operation
init|=
name|determineOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|operation
argument_list|)
condition|)
block|{
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isMultiPartUpload
argument_list|()
condition|)
block|{
name|processMultiPart
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processSingleOp
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|copyObject
case|:
name|copyObject
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getS3Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operation"
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|processMultiPart (final Exchange exchange)
specifier|public
name|void
name|processMultiPart
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|filePayload
init|=
literal|null
decl_stmt|;
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|()
decl_stmt|;
comment|// Need to check if the message body is WrappedFile
if|if
condition|(
name|obj
operator|instanceof
name|WrappedFile
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|WrappedFile
argument_list|<
name|?
argument_list|>
operator|)
name|obj
operator|)
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|File
condition|)
block|{
name|filePayload
operator|=
operator|(
name|File
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidArgumentException
argument_list|(
literal|"aws-s3: MultiPart upload requires a File input."
argument_list|)
throw|;
block|}
name|ObjectMetadata
name|objectMetadata
init|=
name|determineMetadata
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectMetadata
operator|.
name|getContentLength
argument_list|()
operator|==
literal|0
condition|)
block|{
name|objectMetadata
operator|.
name|setContentLength
argument_list|(
name|filePayload
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|keyName
init|=
name|determineKey
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
specifier|final
name|InitiateMultipartUploadRequest
name|initRequest
init|=
operator|new
name|InitiateMultipartUploadRequest
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|,
name|keyName
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
name|initRequest
operator|.
name|setStorageClass
argument_list|(
name|StorageClass
operator|.
name|fromValue
argument_list|(
name|storageClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|cannedAcl
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
name|CANNED_ACL
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cannedAcl
operator|!=
literal|null
condition|)
block|{
name|CannedAccessControlList
name|objectAcl
init|=
name|CannedAccessControlList
operator|.
name|valueOf
argument_list|(
name|cannedAcl
argument_list|)
decl_stmt|;
name|initRequest
operator|.
name|setCannedACL
argument_list|(
name|objectAcl
argument_list|)
expr_stmt|;
block|}
name|AccessControlList
name|acl
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
name|ACL
argument_list|,
name|AccessControlList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|acl
operator|!=
literal|null
condition|)
block|{
comment|// note: if cannedacl and acl are both specified the last one will be used. refer to
comment|// PutObjectRequest#setAccessControlList for more details
name|initRequest
operator|.
name|setAccessControlList
argument_list|(
name|acl
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Initiating multipart upload [{}] from exchange [{}]..."
argument_list|,
name|initRequest
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
specifier|final
name|InitiateMultipartUploadResult
name|initResponse
init|=
name|getEndpoint
argument_list|()
operator|.
name|getS3Client
argument_list|()
operator|.
name|initiateMultipartUpload
argument_list|(
name|initRequest
argument_list|)
decl_stmt|;
specifier|final
name|long
name|contentLength
init|=
name|objectMetadata
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|PartETag
argument_list|>
name|partETags
init|=
operator|new
name|ArrayList
argument_list|<
name|PartETag
argument_list|>
argument_list|()
decl_stmt|;
name|long
name|partSize
init|=
name|getConfiguration
argument_list|()
operator|.
name|getPartSize
argument_list|()
decl_stmt|;
name|CompleteMultipartUploadResult
name|uploadResult
init|=
literal|null
decl_stmt|;
name|long
name|filePosition
init|=
literal|0
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|part
init|=
literal|1
init|;
name|filePosition
operator|<
name|contentLength
condition|;
name|part
operator|++
control|)
block|{
name|partSize
operator|=
name|Math
operator|.
name|min
argument_list|(
name|partSize
argument_list|,
name|contentLength
operator|-
name|filePosition
argument_list|)
expr_stmt|;
name|UploadPartRequest
name|uploadRequest
init|=
operator|new
name|UploadPartRequest
argument_list|()
operator|.
name|withBucketName
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|)
operator|.
name|withKey
argument_list|(
name|keyName
argument_list|)
operator|.
name|withUploadId
argument_list|(
name|initResponse
operator|.
name|getUploadId
argument_list|()
argument_list|)
operator|.
name|withPartNumber
argument_list|(
name|part
argument_list|)
operator|.
name|withFileOffset
argument_list|(
name|filePosition
argument_list|)
operator|.
name|withFile
argument_list|(
name|filePayload
argument_list|)
operator|.
name|withPartSize
argument_list|(
name|partSize
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Uploading part [{}] for {}"
argument_list|,
name|part
argument_list|,
name|keyName
argument_list|)
expr_stmt|;
name|partETags
operator|.
name|add
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getS3Client
argument_list|()
operator|.
name|uploadPart
argument_list|(
name|uploadRequest
argument_list|)
operator|.
name|getPartETag
argument_list|()
argument_list|)
expr_stmt|;
name|filePosition
operator|+=
name|partSize
expr_stmt|;
block|}
name|CompleteMultipartUploadRequest
name|compRequest
init|=
operator|new
name|CompleteMultipartUploadRequest
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|,
name|keyName
argument_list|,
name|initResponse
operator|.
name|getUploadId
argument_list|()
argument_list|,
name|partETags
argument_list|)
decl_stmt|;
name|uploadResult
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getS3Client
argument_list|()
operator|.
name|completeMultipartUpload
argument_list|(
name|compRequest
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getS3Client
argument_list|()
operator|.
name|abortMultipartUpload
argument_list|(
operator|new
name|AbortMultipartUploadRequest
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|,
name|keyName
argument_list|,
name|initResponse
operator|.
name|getUploadId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
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
name|uploadResult
operator|.
name|getETag
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|uploadResult
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
name|uploadResult
operator|.
name|getVersionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isDeleteAfterWrite
argument_list|()
operator|&&
name|filePayload
operator|!=
literal|null
condition|)
block|{
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|filePayload
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processSingleOp (final Exchange exchange)
specifier|public
name|void
name|processSingleOp
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectMetadata
name|objectMetadata
init|=
name|determineMetadata
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|File
name|filePayload
init|=
literal|null
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|()
decl_stmt|;
name|PutObjectRequest
name|putObjectRequest
init|=
literal|null
decl_stmt|;
comment|// Need to check if the message body is WrappedFile
if|if
condition|(
name|obj
operator|instanceof
name|WrappedFile
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|WrappedFile
argument_list|<
name|?
argument_list|>
operator|)
name|obj
operator|)
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|File
condition|)
block|{
name|filePayload
operator|=
operator|(
name|File
operator|)
name|obj
expr_stmt|;
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|filePayload
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|is
operator|=
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
expr_stmt|;
block|}
name|putObjectRequest
operator|=
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
name|is
argument_list|,
name|objectMetadata
argument_list|)
expr_stmt|;
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
name|String
name|cannedAcl
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
name|CANNED_ACL
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cannedAcl
operator|!=
literal|null
condition|)
block|{
name|CannedAccessControlList
name|objectAcl
init|=
name|CannedAccessControlList
operator|.
name|valueOf
argument_list|(
name|cannedAcl
argument_list|)
decl_stmt|;
name|putObjectRequest
operator|.
name|setCannedAcl
argument_list|(
name|objectAcl
argument_list|)
expr_stmt|;
block|}
name|AccessControlList
name|acl
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
name|ACL
argument_list|,
name|AccessControlList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|acl
operator|!=
literal|null
condition|)
block|{
comment|// note: if cannedacl and acl are both specified the last one will be used. refer to
comment|// PutObjectRequest#setAccessControlList for more details
name|putObjectRequest
operator|.
name|setAccessControlList
argument_list|(
name|acl
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
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isDeleteAfterWrite
argument_list|()
operator|&&
name|filePayload
operator|!=
literal|null
condition|)
block|{
comment|// close streams
name|IOHelper
operator|.
name|close
argument_list|(
name|putObjectRequest
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|filePayload
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copyObject (AmazonS3 s3Client, Exchange exchange)
specifier|private
name|void
name|copyObject
parameter_list|(
name|AmazonS3
name|s3Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|bucketNameDestination
decl_stmt|;
name|String
name|destinationKey
decl_stmt|;
name|String
name|sourceKey
decl_stmt|;
name|String
name|bucketName
decl_stmt|;
name|String
name|versionId
decl_stmt|;
name|bucketName
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|BUCKET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|bucketName
argument_list|)
condition|)
block|{
name|bucketName
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
expr_stmt|;
block|}
name|sourceKey
operator|=
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
expr_stmt|;
name|destinationKey
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|DESTINATION_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|bucketNameDestination
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|BUCKET_DESTINATION_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|versionId
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|VERSION_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|bucketName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bucket Name must be specified for copyObject Operation"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|bucketNameDestination
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bucket Name Destination must be specified for copyObject Operation"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|sourceKey
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Source Key must be specified for copyObject Operation"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|destinationKey
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Destination Key must be specified for copyObject Operation"
argument_list|)
throw|;
block|}
name|CopyObjectRequest
name|copyObjectRequest
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|versionId
argument_list|)
condition|)
block|{
name|copyObjectRequest
operator|=
operator|new
name|CopyObjectRequest
argument_list|(
name|bucketName
argument_list|,
name|sourceKey
argument_list|,
name|bucketNameDestination
argument_list|,
name|destinationKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|copyObjectRequest
operator|=
operator|new
name|CopyObjectRequest
argument_list|(
name|bucketName
argument_list|,
name|sourceKey
argument_list|,
name|versionId
argument_list|,
name|bucketNameDestination
argument_list|,
name|destinationKey
argument_list|)
expr_stmt|;
block|}
name|CopyObjectResult
name|copyObjectResult
init|=
name|s3Client
operator|.
name|copyObject
argument_list|(
name|copyObjectRequest
argument_list|)
decl_stmt|;
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
name|copyObjectResult
operator|.
name|getETag
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|copyObjectResult
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
name|copyObjectResult
operator|.
name|getVersionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|determineOperation (Exchange exchange)
specifier|private
name|S3Operations
name|determineOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|S3Operations
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|OPERATION
argument_list|,
name|S3Operations
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
name|operation
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
expr_stmt|;
block|}
return|return
name|operation
return|;
block|}
DECL|method|determineMetadata (final Exchange exchange)
specifier|private
name|ObjectMetadata
name|determineMetadata
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|userMetadata
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|USER_METADATA
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|userMetadata
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setUserMetadata
argument_list|(
name|userMetadata
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|s3Headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|S3_HEADERS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|s3Headers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|s3Headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|objectMetadata
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|encryption
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
name|SERVER_SIDE_ENCRYPTION
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getServerSideEncryption
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryption
operator|!=
literal|null
condition|)
block|{
name|objectMetadata
operator|.
name|setSSEAlgorithm
argument_list|(
name|encryption
argument_list|)
expr_stmt|;
block|}
return|return
name|objectMetadata
return|;
block|}
DECL|method|determineKey (final Exchange exchange)
specifier|private
name|String
name|determineKey
parameter_list|(
specifier|final
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
DECL|method|determineStorageClass (final Exchange exchange)
specifier|private
name|String
name|determineStorageClass
parameter_list|(
specifier|final
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
if|if
condition|(
name|s3ProducerToString
operator|==
literal|null
condition|)
block|{
name|s3ProducerToString
operator|=
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
expr_stmt|;
block|}
return|return
name|s3ProducerToString
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

