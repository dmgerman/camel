begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch
package|;
end_package

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
name|Map
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
name|ExpectedBodyTypeException
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
name|elasticsearch
operator|.
name|action
operator|.
name|ListenableActionFuture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|bulk
operator|.
name|BulkItemResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|bulk
operator|.
name|BulkRequestBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|bulk
operator|.
name|BulkResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|delete
operator|.
name|DeleteResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|get
operator|.
name|GetResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|index
operator|.
name|IndexRequestBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|index
operator|.
name|IndexResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|client
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|common
operator|.
name|xcontent
operator|.
name|XContentBuilder
import|;
end_import

begin_comment
comment|/**  * Represents an Elasticsearch producer.  */
end_comment

begin_class
DECL|class|ElasticsearchProducer
specifier|public
class|class
name|ElasticsearchProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|ElasticsearchProducer (ElasticsearchEndpoint endpoint)
specifier|public
name|ElasticsearchProducer
parameter_list|(
name|ElasticsearchEndpoint
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
DECL|method|getEndpoint ()
specifier|public
name|ElasticsearchEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|ElasticsearchEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|String
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_OPERATION
argument_list|,
name|String
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
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getOperation
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_OPERATION
operator|+
literal|" is missing"
argument_list|)
throw|;
block|}
name|Client
name|client
init|=
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
decl_stmt|;
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|OPERATION_INDEX
argument_list|)
condition|)
block|{
name|addToIndex
argument_list|(
name|client
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|OPERATION_GET_BY_ID
argument_list|)
condition|)
block|{
name|getById
argument_list|(
name|client
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|OPERATION_DELETE
argument_list|)
condition|)
block|{
name|deleteById
argument_list|(
name|client
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|OPERATION_BULK_INDEX
argument_list|)
condition|)
block|{
name|addToIndexUsingBulk
argument_list|(
name|client
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_OPERATION
operator|+
literal|" value '"
operator|+
name|operation
operator|+
literal|"' is not supported"
argument_list|)
throw|;
block|}
block|}
DECL|method|getById (Client client, Exchange exchange)
specifier|public
name|void
name|getById
parameter_list|(
name|Client
name|client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|indexName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_INDEX_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexName
operator|==
literal|null
condition|)
block|{
name|indexName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexName
argument_list|()
expr_stmt|;
block|}
name|String
name|indexType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexType
operator|==
literal|null
condition|)
block|{
name|indexType
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexType
argument_list|()
expr_stmt|;
block|}
name|String
name|indexId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|GetResponse
name|response
init|=
name|client
operator|.
name|prepareGet
argument_list|(
name|indexName
argument_list|,
name|indexType
argument_list|,
name|indexId
argument_list|)
operator|.
name|execute
argument_list|()
operator|.
name|actionGet
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteById (Client client, Exchange exchange)
specifier|public
name|void
name|deleteById
parameter_list|(
name|Client
name|client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|indexName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_INDEX_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexName
operator|==
literal|null
condition|)
block|{
name|indexName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexName
argument_list|()
expr_stmt|;
block|}
name|String
name|indexType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexType
operator|==
literal|null
condition|)
block|{
name|indexType
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexType
argument_list|()
expr_stmt|;
block|}
name|String
name|indexId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteResponse
name|response
init|=
name|client
operator|.
name|prepareDelete
argument_list|(
name|indexName
argument_list|,
name|indexType
argument_list|,
name|indexId
argument_list|)
operator|.
name|execute
argument_list|()
operator|.
name|actionGet
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
DECL|method|addToIndex (Client client, Exchange exchange)
specifier|public
name|void
name|addToIndex
parameter_list|(
name|Client
name|client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|indexName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_INDEX_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexName
operator|==
literal|null
condition|)
block|{
name|indexName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexName
argument_list|()
expr_stmt|;
block|}
name|String
name|indexType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexType
operator|==
literal|null
condition|)
block|{
name|indexType
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexType
argument_list|()
expr_stmt|;
block|}
name|IndexRequestBuilder
name|prepareIndex
init|=
name|client
operator|.
name|prepareIndex
argument_list|(
name|indexName
argument_list|,
name|indexType
argument_list|)
decl_stmt|;
name|Object
name|document
init|=
name|extractDocumentFromMessage
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|setIndexRequestSource
argument_list|(
name|document
argument_list|,
name|prepareIndex
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ExpectedBodyTypeException
argument_list|(
name|exchange
argument_list|,
name|XContentBuilder
operator|.
name|class
argument_list|)
throw|;
block|}
name|ListenableActionFuture
argument_list|<
name|IndexResponse
argument_list|>
name|future
init|=
name|prepareIndex
operator|.
name|execute
argument_list|()
decl_stmt|;
name|IndexResponse
name|response
init|=
name|future
operator|.
name|actionGet
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|addToIndexUsingBulk (Client client, Exchange exchange)
specifier|public
name|void
name|addToIndexUsingBulk
parameter_list|(
name|Client
name|client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|indexName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_INDEX_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexName
operator|==
literal|null
condition|)
block|{
name|indexName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexName
argument_list|()
expr_stmt|;
block|}
name|String
name|indexType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConfiguration
operator|.
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexType
operator|==
literal|null
condition|)
block|{
name|indexType
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexType
argument_list|()
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Preparing Bulk Request"
argument_list|)
expr_stmt|;
name|BulkRequestBuilder
name|bulkRequest
init|=
name|client
operator|.
name|prepareBulk
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|document
range|:
name|body
control|)
block|{
name|IndexRequestBuilder
name|prepareIndex
init|=
name|client
operator|.
name|prepareIndex
argument_list|(
name|indexName
argument_list|,
name|indexType
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Indexing document : {}"
argument_list|,
name|document
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|setIndexRequestSource
argument_list|(
name|document
argument_list|,
name|prepareIndex
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ExpectedBodyTypeException
argument_list|(
name|exchange
argument_list|,
name|XContentBuilder
operator|.
name|class
argument_list|)
throw|;
block|}
name|bulkRequest
operator|.
name|add
argument_list|(
name|prepareIndex
argument_list|)
expr_stmt|;
block|}
name|ListenableActionFuture
argument_list|<
name|BulkResponse
argument_list|>
name|future
init|=
name|bulkRequest
operator|.
name|execute
argument_list|()
decl_stmt|;
name|BulkResponse
name|bulkResponse
init|=
name|future
operator|.
name|actionGet
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|indexedIds
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|BulkItemResponse
name|response
range|:
name|bulkResponse
operator|.
name|getItems
argument_list|()
control|)
block|{
name|indexedIds
operator|.
name|add
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"List of successfully indexed document ids : {}"
argument_list|,
name|indexedIds
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|indexedIds
argument_list|)
expr_stmt|;
block|}
DECL|method|extractDocumentFromMessage (Message msg)
specifier|private
name|Object
name|extractDocumentFromMessage
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|Object
name|body
init|=
literal|null
decl_stmt|;
comment|// order is important
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
operator|new
name|Class
index|[]
block|{
name|XContentBuilder
operator|.
name|class
block|,
name|Map
operator|.
name|class
block|,
name|byte
index|[]
operator|.
name|class
block|,
name|String
operator|.
name|class
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|types
operator|.
name|length
operator|&&
name|body
operator|==
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|types
index|[
name|i
index|]
decl_stmt|;
name|body
operator|=
name|msg
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|body
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|setIndexRequestSource (Object document, IndexRequestBuilder builder)
specifier|private
name|boolean
name|setIndexRequestSource
parameter_list|(
name|Object
name|document
parameter_list|,
name|IndexRequestBuilder
name|builder
parameter_list|)
block|{
name|boolean
name|converted
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|converted
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|document
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|builder
operator|.
name|setSource
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|document
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|document
operator|instanceof
name|Map
condition|)
block|{
name|builder
operator|.
name|setSource
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|document
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|document
operator|instanceof
name|String
condition|)
block|{
name|builder
operator|.
name|setSource
argument_list|(
operator|(
name|String
operator|)
name|document
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|document
operator|instanceof
name|XContentBuilder
condition|)
block|{
name|builder
operator|.
name|setSource
argument_list|(
operator|(
name|XContentBuilder
operator|)
name|document
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|converted
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|converted
return|;
block|}
block|}
end_class

end_unit

