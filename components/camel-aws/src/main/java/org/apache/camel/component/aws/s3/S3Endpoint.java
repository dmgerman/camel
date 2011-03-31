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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|BasicAWSCredentials
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
name|AmazonS3Client
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
name|Bucket
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
name|CreateBucketRequest
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
name|S3Object
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
name|ExchangePattern
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
name|impl
operator|.
name|DefaultExchange
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
name|ScheduledPollEndpoint
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
comment|/**  * Defines the<a href="http://camel.apache.org/aws.html">AWS S3 Endpoint</a>.    *  */
end_comment

begin_class
DECL|class|S3Endpoint
specifier|public
class|class
name|S3Endpoint
extends|extends
name|ScheduledPollEndpoint
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
name|S3Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|s3Client
specifier|private
name|AmazonS3Client
name|s3Client
decl_stmt|;
DECL|field|configuration
specifier|private
name|S3Configuration
name|configuration
decl_stmt|;
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
init|=
literal|10
decl_stmt|;
DECL|method|S3Endpoint (String uri, CamelContext context, S3Configuration configuration)
specifier|public
name|S3Endpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|S3Configuration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|context
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
name|S3Consumer
name|s3Consumer
init|=
operator|new
name|S3Consumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|s3Consumer
argument_list|)
expr_stmt|;
return|return
name|s3Consumer
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
return|return
operator|new
name|S3Producer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|String
name|bucketName
init|=
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Quering whether bucket [{}] already exists..."
argument_list|,
name|bucketName
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Bucket
argument_list|>
name|buckets
init|=
name|getS3Client
argument_list|()
operator|.
name|listBuckets
argument_list|()
decl_stmt|;
for|for
control|(
name|Bucket
name|bucket
range|:
name|buckets
control|)
block|{
if|if
condition|(
name|bucketName
operator|.
name|equals
argument_list|(
name|bucket
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Bucket [{}] already exist"
argument_list|,
name|bucketName
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Bucket [{}] doesn't exist yet"
argument_list|,
name|bucketName
argument_list|)
expr_stmt|;
comment|// creates the new bucket because it doesn't exist yet
name|CreateBucketRequest
name|createBucketRequest
init|=
operator|new
name|CreateBucketRequest
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|createBucketRequest
operator|.
name|setRegion
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating bucket [{}] in region [{}] with request [{}]..."
argument_list|,
operator|new
name|Object
index|[]
block|{
name|configuration
operator|.
name|getBucketName
argument_list|()
block|,
name|configuration
operator|.
name|getRegion
argument_list|()
block|,
name|createBucketRequest
block|}
argument_list|)
expr_stmt|;
name|getS3Client
argument_list|()
operator|.
name|createBucket
argument_list|(
name|createBucketRequest
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Bucket created"
argument_list|)
expr_stmt|;
block|}
DECL|method|createExchange (S3Object s3Object)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|S3Object
name|s3Object
parameter_list|)
block|{
return|return
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|,
name|s3Object
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern, S3Object s3Object)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|,
name|S3Object
name|s3Object
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Getting object with key [{}] from bucket [{}]..."
argument_list|,
name|s3Object
operator|.
name|getKey
argument_list|()
argument_list|,
name|s3Object
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectMetadata
name|objectMetadata
init|=
name|s3Object
operator|.
name|getObjectMetadata
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Got object [{}]"
argument_list|,
name|s3Object
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|s3Object
operator|.
name|getObjectContent
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|KEY
argument_list|,
name|s3Object
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|BUCKET_NAME
argument_list|,
name|s3Object
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|E_TAG
argument_list|,
name|objectMetadata
operator|.
name|getETag
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|LAST_MODIFIED
argument_list|,
name|objectMetadata
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|VERSION_ID
argument_list|,
name|objectMetadata
operator|.
name|getVersionId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_TYPE
argument_list|,
name|objectMetadata
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_MD5
argument_list|,
name|objectMetadata
operator|.
name|getContentMD5
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_LENGTH
argument_list|,
name|objectMetadata
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_ENCODING
argument_list|,
name|objectMetadata
operator|.
name|getContentEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_DISPOSITION
argument_list|,
name|objectMetadata
operator|.
name|getContentDisposition
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CACHE_CONTROL
argument_list|,
name|objectMetadata
operator|.
name|getCacheControl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|S3Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (S3Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|S3Configuration
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
DECL|method|setS3Client (AmazonS3Client s3Client)
specifier|public
name|void
name|setS3Client
parameter_list|(
name|AmazonS3Client
name|s3Client
parameter_list|)
block|{
name|this
operator|.
name|s3Client
operator|=
name|s3Client
expr_stmt|;
block|}
DECL|method|getS3Client ()
specifier|public
name|AmazonS3Client
name|getS3Client
parameter_list|()
block|{
if|if
condition|(
name|s3Client
operator|==
literal|null
condition|)
block|{
name|s3Client
operator|=
name|configuration
operator|.
name|getAmazonS3Client
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getAmazonS3Client
argument_list|()
else|:
name|createS3Client
argument_list|()
expr_stmt|;
block|}
return|return
name|s3Client
return|;
block|}
comment|/**      * Provide the possibility to override this method for an mock implementation      *      * @return AmazonS3Client      */
DECL|method|createS3Client ()
name|AmazonS3Client
name|createS3Client
parameter_list|()
block|{
name|AWSCredentials
name|credentials
init|=
operator|new
name|BasicAWSCredentials
argument_list|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|AmazonS3Client
argument_list|(
name|credentials
argument_list|)
return|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
block|}
end_class

end_unit

