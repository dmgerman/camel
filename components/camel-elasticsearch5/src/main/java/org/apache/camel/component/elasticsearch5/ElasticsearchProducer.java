begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch5
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch5
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
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
name|admin
operator|.
name|indices
operator|.
name|delete
operator|.
name|DeleteIndexRequest
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
name|get
operator|.
name|MultiGetRequest
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
name|MultiSearchRequest
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
name|transport
operator|.
name|TransportClient
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
name|settings
operator|.
name|Settings
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
name|transport
operator|.
name|InetSocketTransportAddress
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
name|transport
operator|.
name|TransportAddress
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|index
operator|.
name|IndexNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|transport
operator|.
name|client
operator|.
name|PreBuiltTransportClient
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
name|ElasticsearchProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|protected
specifier|final
name|ElasticsearchConfiguration
name|configuration
decl_stmt|;
DECL|field|client
specifier|private
name|TransportClient
name|client
decl_stmt|;
DECL|method|ElasticsearchProducer (ElasticsearchEndpoint endpoint, ElasticsearchConfiguration configuration)
specifier|public
name|ElasticsearchProducer
parameter_list|(
name|ElasticsearchEndpoint
name|endpoint
parameter_list|,
name|ElasticsearchConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|endpoint
operator|.
name|getClient
argument_list|()
expr_stmt|;
block|}
DECL|method|resolveOperation (Exchange exchange)
specifier|private
name|ElasticsearchOperation
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
name|ElasticsearchOperation
operator|.
name|INDEX
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
name|ElasticsearchOperation
operator|.
name|GET_BY_ID
return|;
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|MultiGetRequest
condition|)
block|{
return|return
name|ElasticsearchOperation
operator|.
name|MULTIGET
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
name|ElasticsearchOperation
operator|.
name|UPDATE
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
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|ElasticsearchOperation
operator|.
name|BULK_INDEX
condition|)
block|{
return|return
name|configuration
operator|.
name|getOperation
argument_list|()
operator|.
name|BULK_INDEX
return|;
block|}
else|else
block|{
return|return
name|configuration
operator|.
name|getOperation
argument_list|()
operator|.
name|BULK
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
name|ElasticsearchOperation
operator|.
name|DELETE
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
name|ElasticsearchOperation
operator|.
name|SEARCH
return|;
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|MultiSearchRequest
condition|)
block|{
return|return
name|ElasticsearchOperation
operator|.
name|MULTISEARCH
return|;
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|DeleteIndexRequest
condition|)
block|{
return|return
name|ElasticsearchOperation
operator|.
name|DELETE_INDEX
return|;
block|}
name|ElasticsearchOperation
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
name|ElasticsearchOperation
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
name|configuration
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
name|ElasticsearchOperation
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
name|configuration
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
name|configuration
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
name|configWaitForActiveShards
init|=
literal|false
decl_stmt|;
name|Integer
name|waitForActiveShards
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_WAIT_FOR_ACTIVE_SHARDS
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|waitForActiveShards
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
name|PARAM_WAIT_FOR_ACTIVE_SHARDS
argument_list|,
name|configuration
operator|.
name|getWaitForActiveShards
argument_list|()
argument_list|)
expr_stmt|;
name|configWaitForActiveShards
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|INDEX
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
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|UPDATE
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
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|GET_BY_ID
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
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|MULTIGET
condition|)
block|{
name|MultiGetRequest
name|multiGetRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|MultiGetRequest
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
name|multiGet
argument_list|(
name|multiGetRequest
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|BULK
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
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|BULK_INDEX
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
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|DELETE
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
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|EXISTS
condition|)
block|{
comment|// ExistsRequest API is deprecated, using SearchRequest instead with size=0 and terminate_after=1
name|SearchRequest
name|searchRequest
init|=
operator|new
name|SearchRequest
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
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
argument_list|)
decl_stmt|;
try|try
block|{
name|client
operator|.
name|prepareSearch
argument_list|(
name|searchRequest
operator|.
name|indices
argument_list|()
argument_list|)
operator|.
name|setSize
argument_list|(
literal|0
argument_list|)
operator|.
name|setTerminateAfter
argument_list|(
literal|1
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexNotFoundException
name|e
parameter_list|)
block|{
name|message
operator|.
name|setBody
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|SEARCH
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
elseif|else
if|if
condition|(
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|MULTISEARCH
condition|)
block|{
name|MultiSearchRequest
name|multiSearchRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|MultiSearchRequest
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
name|multiSearch
argument_list|(
name|multiSearchRequest
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|DELETE_INDEX
condition|)
block|{
name|DeleteIndexRequest
name|deleteIndexRequest
init|=
name|message
operator|.
name|getBody
argument_list|(
name|DeleteIndexRequest
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
name|admin
argument_list|()
operator|.
name|indices
argument_list|()
operator|.
name|delete
argument_list|(
name|deleteIndexRequest
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
name|configWaitForActiveShards
condition|)
block|{
name|message
operator|.
name|removeHeader
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_WAIT_FOR_ACTIVE_SHARDS
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Connecting to the ElasticSearch cluster: "
operator|+
name|configuration
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getIp
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|client
operator|=
name|createClient
argument_list|()
operator|.
name|addTransportAddress
argument_list|(
operator|new
name|InetSocketTransportAddress
argument_list|(
name|InetAddress
operator|.
name|getByName
argument_list|(
name|configuration
operator|.
name|getIp
argument_list|()
argument_list|)
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|getTransportAddressesList
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|configuration
operator|.
name|getTransportAddressesList
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|TransportAddress
argument_list|>
name|addresses
init|=
operator|new
name|ArrayList
argument_list|<
name|TransportAddress
argument_list|>
argument_list|(
name|configuration
operator|.
name|getTransportAddressesList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|TransportAddress
name|address
range|:
name|configuration
operator|.
name|getTransportAddressesList
argument_list|()
control|)
block|{
name|addresses
operator|.
name|add
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
name|client
operator|=
name|createClient
argument_list|()
operator|.
name|addTransportAddresses
argument_list|(
name|addresses
operator|.
name|toArray
argument_list|(
operator|new
name|TransportAddress
index|[
name|addresses
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Incorrect ip address and port parameters settings for ElasticSearch cluster"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createClient ()
specifier|private
name|TransportClient
name|createClient
parameter_list|()
throws|throws
name|NoSuchMethodException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
throws|,
name|InstantiationException
block|{
specifier|final
name|Settings
operator|.
name|Builder
name|settings
init|=
name|getSettings
argument_list|()
decl_stmt|;
specifier|final
name|CamelContext
name|camelContext
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
literal|"org.elasticsearch.xpack.client.PreBuiltXPackTransportClient"
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|ctor
init|=
name|clazz
operator|.
name|getConstructor
argument_list|(
name|Settings
operator|.
name|class
argument_list|,
name|Class
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|settings
operator|.
name|put
argument_list|(
literal|"xpack.security.user"
argument_list|,
name|configuration
operator|.
name|getUser
argument_list|()
operator|+
literal|":"
operator|+
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"xpack.security.transport.ssl.enabled"
argument_list|,
name|configuration
operator|.
name|getEnableSSL
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"XPack Client was found on the classpath"
argument_list|)
expr_stmt|;
return|return
operator|(
name|TransportClient
operator|)
name|ctor
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|settings
operator|.
name|build
argument_list|()
block|,
operator|new
name|Class
index|[
literal|0
index|]
block|}
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"XPack Client was not found on the classpath, using the standard client"
argument_list|)
expr_stmt|;
return|return
operator|new
name|PreBuiltTransportClient
argument_list|(
name|settings
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|getSettings ()
specifier|private
name|Settings
operator|.
name|Builder
name|getSettings
parameter_list|()
block|{
specifier|final
name|Settings
operator|.
name|Builder
name|settings
init|=
name|Settings
operator|.
name|builder
argument_list|()
operator|.
name|put
argument_list|(
literal|"cluster.name"
argument_list|,
name|configuration
operator|.
name|getClusterName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"client.transport.sniff"
argument_list|,
name|configuration
operator|.
name|getClientTransportSniff
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"transport.ping_schedule"
argument_list|,
name|configuration
operator|.
name|getPingSchedule
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"client.transport.ping_timeout"
argument_list|,
name|configuration
operator|.
name|getPingTimeout
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"client.transport.sniff"
argument_list|,
name|configuration
operator|.
name|getClientTransportSniff
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"request.headers.X-Found-Cluster"
argument_list|,
name|configuration
operator|.
name|getClusterName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|settings
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Disconnecting from ElasticSearch cluster: "
operator|+
name|configuration
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|TransportClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

