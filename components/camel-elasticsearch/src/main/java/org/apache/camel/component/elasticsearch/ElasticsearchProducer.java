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
name|ArrayList
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
name|BulkRequest
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
name|DeleteRequest
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
name|GetRequest
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
name|IndexRequest
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
name|search
operator|.
name|SearchRequest
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
name|update
operator|.
name|UpdateRequest
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
DECL|method|resolveOperation (Exchange exchange)
specifier|private
name|String
name|resolveOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// 1. Operation can be driven by either (in order of preference):
comment|// a. If the body is an ActionRequest the operation is set by the type
comment|// of request.
comment|// b. If the body is not an ActionRequest, the operation is set by the
comment|// header if it exists.
comment|// c. If neither the operation can not be derived from the body or
comment|// header, the configuration is used.
comment|// In the event we can't discover the operation from a, b or c we throw
comment|// an error.
name|Object
name|request
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
name|request
operator|instanceof
name|IndexRequest
condition|)
block|{
return|return
name|ElasticsearchConstants
operator|.
name|OPERATION_INDEX
return|;
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|GetRequest
condition|)
block|{
return|return
name|ElasticsearchConstants
operator|.
name|OPERATION_GET_BY_ID
return|;
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|UpdateRequest
condition|)
block|{
return|return
name|ElasticsearchConstants
operator|.
name|OPERATION_UPDATE
return|;
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|BulkRequest
condition|)
block|{
comment|// do we want bulk or bulk_index?
if|if
condition|(
literal|"BULK_INDEX"
operator|.
name|equals
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getOperation
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|ElasticsearchConstants
operator|.
name|OPERATION_BULK_INDEX
return|;
block|}
else|else
block|{
return|return
name|ElasticsearchConstants
operator|.
name|OPERATION_BULK
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|DeleteRequest
condition|)
block|{
return|return
name|ElasticsearchConstants
operator|.
name|OPERATION_DELETE
return|;
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|SearchRequest
condition|)
block|{
return|return
name|ElasticsearchConstants
operator|.
name|OPERATION_SEARCH
return|;
block|}
name|String
name|operationConfig
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConstants
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
name|operationConfig
operator|==
literal|null
condition|)
block|{
name|operationConfig
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
name|operationConfig
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
operator|+
literal|" value '"
operator|+
name|operationConfig
operator|+
literal|"' is not supported"
argument_list|)
throw|;
block|}
return|return
name|operationConfig
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
comment|// 2. Index and type will be set by:
comment|// a. If the incoming body is already an action request
comment|// b. If the body is not an action request we will use headers if they
comment|// are set.
comment|// c. If the body is not an action request and the headers aren't set we
comment|// will use the configuration.
comment|// No error is thrown by the component in the event none of the above
comment|// conditions are met. The java es client
comment|// will throw.
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|operation
init|=
name|resolveOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// Set the index/type headers on the exchange if necessary. This is used
comment|// for type conversion.
name|boolean
name|configIndexName
init|=
literal|false
decl_stmt|;
name|String
name|indexName
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConstants
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
name|message
operator|.
name|setHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexName
argument_list|()
argument_list|)
expr_stmt|;
name|configIndexName
operator|=
literal|true
expr_stmt|;
block|}
name|boolean
name|configIndexType
init|=
literal|false
decl_stmt|;
name|String
name|indexType
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConstants
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
name|message
operator|.
name|setHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_TYPE
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getIndexType
argument_list|()
argument_list|)
expr_stmt|;
name|configIndexType
operator|=
literal|true
expr_stmt|;
block|}
name|boolean
name|configConsistencyLevel
init|=
literal|false
decl_stmt|;
name|String
name|consistencyLevel
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_CONSISTENCY_LEVEL
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|consistencyLevel
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_CONSISTENCY_LEVEL
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getConsistencyLevel
argument_list|()
argument_list|)
expr_stmt|;
name|configConsistencyLevel
operator|=
literal|true
expr_stmt|;
block|}
name|boolean
name|configReplicationType
init|=
literal|false
decl_stmt|;
name|String
name|replicationType
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_REPLICATION_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|replicationType
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_REPLICATION_TYPE
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getReplicationType
argument_list|()
argument_list|)
expr_stmt|;
name|configReplicationType
operator|=
literal|true
expr_stmt|;
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
name|ElasticsearchConstants
operator|.
name|OPERATION_INDEX
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|IndexRequest
name|indexRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|IndexRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|client
operator|.
name|index
argument_list|(
name|indexRequest
argument_list|)
operator|.
name|actionGet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ElasticsearchConstants
operator|.
name|OPERATION_UPDATE
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|UpdateRequest
name|updateRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|UpdateRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|client
operator|.
name|update
argument_list|(
name|updateRequest
argument_list|)
operator|.
name|actionGet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ElasticsearchConstants
operator|.
name|OPERATION_GET_BY_ID
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|GetRequest
name|getRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|GetRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|client
operator|.
name|get
argument_list|(
name|getRequest
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ElasticsearchConstants
operator|.
name|OPERATION_BULK
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|BulkRequest
name|bulkRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|BulkRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|client
operator|.
name|bulk
argument_list|(
name|bulkRequest
argument_list|)
operator|.
name|actionGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ElasticsearchConstants
operator|.
name|OPERATION_BULK_INDEX
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|BulkRequest
name|bulkRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|BulkRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|indexedIds
init|=
operator|new
name|ArrayList
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
name|client
operator|.
name|bulk
argument_list|(
name|bulkRequest
argument_list|)
operator|.
name|actionGet
argument_list|()
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
name|message
operator|.
name|setBody
argument_list|(
name|indexedIds
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ElasticsearchConstants
operator|.
name|OPERATION_DELETE
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|DeleteRequest
name|deleteRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|DeleteRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|client
operator|.
name|delete
argument_list|(
name|deleteRequest
argument_list|)
operator|.
name|actionGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ElasticsearchConstants
operator|.
name|OPERATION_SEARCH
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|SearchRequest
name|searchRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|SearchRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|client
operator|.
name|search
argument_list|(
name|searchRequest
argument_list|)
operator|.
name|actionGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ElasticsearchConstants
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
comment|// If we set params via the configuration on this exchange, remove them
comment|// now. This preserves legacy behavior for this component and enables a
comment|// use case where one message can be sent to multiple elasticsearch
comment|// endpoints where the user is relying on the endpoint configuration
comment|// (index/type) rather than header values. If we do not clear this out
comment|// sending the same message (index request, for example) to multiple
comment|// elasticsearch endpoints would have the effect overriding any
comment|// subsequent endpoint index/type with the first endpoint index/type.
if|if
condition|(
name|configIndexName
condition|)
block|{
name|message
operator|.
name|removeHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configIndexType
condition|)
block|{
name|message
operator|.
name|removeHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_TYPE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configConsistencyLevel
condition|)
block|{
name|message
operator|.
name|removeHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_CONSISTENCY_LEVEL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configReplicationType
condition|)
block|{
name|message
operator|.
name|removeHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_REPLICATION_TYPE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

