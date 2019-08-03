begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InputStream
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
name|Queue
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
name|converter
operator|.
name|stream
operator|.
name|CachedOutputStream
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
name|StorageMetadata
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
name|StorageType
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
name|options
operator|.
name|ListContainerOptions
import|;
end_import

begin_class
DECL|class|JcloudsBlobStoreConsumer
specifier|public
class|class
name|JcloudsBlobStoreConsumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|JcloudsBlobStoreEndpoint
name|endpoint
decl_stmt|;
DECL|field|container
specifier|private
specifier|final
name|String
name|container
decl_stmt|;
DECL|field|blobStore
specifier|private
specifier|final
name|BlobStore
name|blobStore
decl_stmt|;
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
init|=
literal|10
decl_stmt|;
DECL|method|JcloudsBlobStoreConsumer (JcloudsBlobStoreEndpoint endpoint, Processor processor, BlobStore blobStore)
specifier|public
name|JcloudsBlobStoreConsumer
parameter_list|(
name|JcloudsBlobStoreEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|BlobStore
name|blobStore
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|blobStore
operator|=
name|blobStore
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|container
operator|=
name|endpoint
operator|.
name|getContainer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
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
name|container
init|=
name|endpoint
operator|.
name|getContainer
argument_list|()
decl_stmt|;
name|String
name|locationId
init|=
name|endpoint
operator|.
name|getLocationId
argument_list|()
decl_stmt|;
name|JcloudsBlobStoreHelper
operator|.
name|ensureContainerExists
argument_list|(
name|blobStore
argument_list|,
name|container
argument_list|,
name|locationId
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
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
name|pendingExchanges
operator|=
literal|0
expr_stmt|;
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|directory
init|=
name|endpoint
operator|.
name|getDirectory
argument_list|()
decl_stmt|;
name|ListContainerOptions
name|opt
init|=
operator|new
name|ListContainerOptions
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|directory
argument_list|)
condition|)
block|{
name|opt
operator|=
name|opt
operator|.
name|inDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|StorageMetadata
name|md
range|:
name|blobStore
operator|.
name|list
argument_list|(
name|container
argument_list|,
name|opt
operator|.
name|maxResults
argument_list|(
name|maxMessagesPerPoll
argument_list|)
operator|.
name|recursive
argument_list|()
argument_list|)
control|)
block|{
name|String
name|blobName
init|=
name|md
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|md
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|StorageType
operator|.
name|BLOB
argument_list|)
condition|)
block|{
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
name|InputStream
name|body
init|=
name|JcloudsBlobStoreHelper
operator|.
name|readBlob
argument_list|(
name|blobStore
argument_list|,
name|container
argument_list|,
name|blobName
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|body
argument_list|,
name|cos
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|cos
operator|.
name|newStreamCache
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|JcloudsConstants
operator|.
name|BLOB_NAME
argument_list|,
name|blobName
argument_list|)
expr_stmt|;
name|queue
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|queue
operator|.
name|isEmpty
argument_list|()
condition|?
literal|0
else|:
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|queue
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}]..."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// if we failed then throw exception
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
name|blobStore
operator|.
name|removeBlob
argument_list|(
name|container
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|JcloudsConstants
operator|.
name|BLOB_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
block|}
end_class

end_unit

