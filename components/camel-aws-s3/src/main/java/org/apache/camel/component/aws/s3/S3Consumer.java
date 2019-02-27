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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Queue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonClientException
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
name|GetObjectRequest
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
name|ListObjectsRequest
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
name|ObjectListing
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
name|S3ObjectSummary
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
name|AsyncCallback
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
name|NoFactoryAvailableException
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
name|spi
operator|.
name|Synchronization
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
name|ScheduledBatchPollingConsumer
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

begin_comment
comment|/**  * A Consumer of messages from the Amazon Web Service Simple Storage Service  *<a href="http://aws.amazon.com/s3/">AWS S3</a>  */
end_comment

begin_class
DECL|class|S3Consumer
specifier|public
class|class
name|S3Consumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
DECL|field|marker
specifier|private
name|String
name|marker
decl_stmt|;
DECL|field|s3ConsumerToString
specifier|private
specifier|transient
name|String
name|s3ConsumerToString
decl_stmt|;
DECL|method|S3Consumer (S3Endpoint endpoint, Processor processor)
specifier|public
name|S3Consumer
parameter_list|(
name|S3Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|NoFactoryAvailableException
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must reset for each poll
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
name|pendingExchanges
operator|=
literal|0
expr_stmt|;
name|String
name|fileName
init|=
name|getConfiguration
argument_list|()
operator|.
name|getFileName
argument_list|()
decl_stmt|;
name|String
name|bucketName
init|=
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
decl_stmt|;
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|exchanges
decl_stmt|;
if|if
condition|(
name|fileName
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Getting object in bucket [{}] with file name [{}]..."
argument_list|,
name|bucketName
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|S3Object
name|s3Object
init|=
name|getAmazonS3Client
argument_list|()
operator|.
name|getObject
argument_list|(
operator|new
name|GetObjectRequest
argument_list|(
name|bucketName
argument_list|,
name|fileName
argument_list|)
argument_list|)
decl_stmt|;
name|exchanges
operator|=
name|createExchanges
argument_list|(
name|s3Object
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Queueing objects in bucket [{}]..."
argument_list|,
name|bucketName
argument_list|)
expr_stmt|;
name|ListObjectsRequest
name|listObjectsRequest
init|=
operator|new
name|ListObjectsRequest
argument_list|()
decl_stmt|;
name|listObjectsRequest
operator|.
name|setBucketName
argument_list|(
name|bucketName
argument_list|)
expr_stmt|;
name|listObjectsRequest
operator|.
name|setPrefix
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|maxMessagesPerPoll
operator|>
literal|0
condition|)
block|{
name|listObjectsRequest
operator|.
name|setMaxKeys
argument_list|(
name|maxMessagesPerPoll
argument_list|)
expr_stmt|;
block|}
comment|// if there was a marker from previous poll then use that to continue from where we left last time
if|if
condition|(
name|marker
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Resuming from marker: {}"
argument_list|,
name|marker
argument_list|)
expr_stmt|;
name|listObjectsRequest
operator|.
name|setMarker
argument_list|(
name|marker
argument_list|)
expr_stmt|;
block|}
name|ObjectListing
name|listObjects
init|=
name|getAmazonS3Client
argument_list|()
operator|.
name|listObjects
argument_list|(
name|listObjectsRequest
argument_list|)
decl_stmt|;
if|if
condition|(
name|listObjects
operator|.
name|isTruncated
argument_list|()
condition|)
block|{
name|marker
operator|=
name|listObjects
operator|.
name|getNextMarker
argument_list|()
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Returned list is truncated, so setting next marker: {}"
argument_list|,
name|marker
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no more data so clear marker
name|marker
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Found {} objects in bucket [{}]..."
argument_list|,
name|listObjects
operator|.
name|getObjectSummaries
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|bucketName
argument_list|)
expr_stmt|;
block|}
name|exchanges
operator|=
name|createExchanges
argument_list|(
name|listObjects
operator|.
name|getObjectSummaries
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|exchanges
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createExchanges (S3Object s3Object)
specifier|protected
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|createExchanges
parameter_list|(
name|S3Object
name|s3Object
parameter_list|)
block|{
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|s3Object
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createExchanges (List<S3ObjectSummary> s3ObjectSummaries)
specifier|protected
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|createExchanges
parameter_list|(
name|List
argument_list|<
name|S3ObjectSummary
argument_list|>
name|s3ObjectSummaries
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Received {} messages in this poll"
argument_list|,
name|s3ObjectSummaries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|S3Object
argument_list|>
name|s3Objects
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|S3ObjectSummary
name|s3ObjectSummary
range|:
name|s3ObjectSummaries
control|)
block|{
name|S3Object
name|s3Object
init|=
name|getAmazonS3Client
argument_list|()
operator|.
name|getObject
argument_list|(
name|s3ObjectSummary
operator|.
name|getBucketName
argument_list|()
argument_list|,
name|s3ObjectSummary
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|s3Objects
operator|.
name|add
argument_list|(
name|s3Object
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|s3Object
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error getting S3Object due: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// ensure all previous gathered s3 objects are closed
comment|// if there was an exception creating the exchanges in this batch
name|s3Objects
operator|.
name|forEach
argument_list|(
name|IOHelper
operator|::
name|close
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|processBatch (Queue<Object> exchanges)
specifier|public
name|int
name|processBatch
parameter_list|(
name|Queue
argument_list|<
name|Object
argument_list|>
name|exchanges
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|total
operator|&&
name|isBatchAllowed
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
comment|// only loop if we are started (allowed to run)
specifier|final
name|Exchange
name|exchange
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|Exchange
operator|.
name|class
argument_list|,
name|exchanges
operator|.
name|poll
argument_list|()
argument_list|)
decl_stmt|;
comment|// add current index and total as properties
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
name|index
operator|==
name|total
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// update pending number of exchanges
name|pendingExchanges
operator|=
name|total
operator|-
name|index
operator|-
literal|1
expr_stmt|;
comment|// add on completion to handle after work when the exchange is done
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|processCommit
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|processRollback
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"S3ConsumerOnCompletion"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}]..."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}] done."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
comment|/**      * Strategy to delete the message after being processed.      *      * @param exchange the exchange      */
DECL|method|processCommit (Exchange exchange)
specifier|protected
name|void
name|processCommit
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isDeleteAfterRead
argument_list|()
condition|)
block|{
name|String
name|bucketName
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
name|BUCKET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|log
operator|.
name|trace
argument_list|(
literal|"Deleting object from bucket {} with key {}..."
argument_list|,
name|bucketName
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|getAmazonS3Client
argument_list|()
operator|.
name|deleteObject
argument_list|(
name|bucketName
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Deleted object from bucket {} with key {}..."
argument_list|,
name|bucketName
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|AmazonClientException
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error occurred during deleting object. This exception is ignored."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy when processing the exchange failed.      *      * @param exchange the exchange      */
DECL|method|processRollback (Exchange exchange)
specifier|protected
name|void
name|processRollback
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Exception
name|cause
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Exchange failed, so rolling back message status: {}"
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Exchange failed, so rolling back message status: {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getAmazonS3Client ()
specifier|protected
name|AmazonS3
name|getAmazonS3Client
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getS3Client
argument_list|()
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
name|s3ConsumerToString
operator|==
literal|null
condition|)
block|{
name|s3ConsumerToString
operator|=
literal|"S3Consumer["
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
name|s3ConsumerToString
return|;
block|}
block|}
end_class

end_unit
