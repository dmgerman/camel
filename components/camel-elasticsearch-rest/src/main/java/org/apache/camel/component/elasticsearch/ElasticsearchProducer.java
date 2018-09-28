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
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
operator|.
name|AuthScope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
operator|.
name|UsernamePasswordCredentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|CredentialsProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|BasicCredentialsProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|ElasticsearchStatusException
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
name|RestClient
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
name|RestClientBuilder
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
name|RestHighLevelClient
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
name|sniff
operator|.
name|Sniffer
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
name|sniff
operator|.
name|SnifferBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|rest
operator|.
name|RestStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|search
operator|.
name|builder
operator|.
name|SearchSourceBuilder
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
DECL|field|configuration
specifier|protected
specifier|final
name|ElasticsearchConfiguration
name|configuration
decl_stmt|;
DECL|field|client
specifier|private
name|RestClient
name|client
decl_stmt|;
DECL|field|sniffer
specifier|private
name|Sniffer
name|sniffer
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
name|Index
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
name|GetById
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
name|MultiGet
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
name|Update
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
name|BulkIndex
condition|)
block|{
return|return
name|ElasticsearchOperation
operator|.
name|BulkIndex
return|;
block|}
else|else
block|{
return|return
name|ElasticsearchOperation
operator|.
name|Bulk
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
name|Delete
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
name|Search
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
name|DeleteIndex
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
if|if
condition|(
name|configuration
operator|.
name|getDisconnect
argument_list|()
operator|&&
name|client
operator|==
literal|null
condition|)
block|{
name|startClient
argument_list|()
expr_stmt|;
block|}
name|RestHighLevelClient
name|restHighLevelClient
init|=
operator|new
name|HighLevelClient
argument_list|(
name|client
argument_list|)
decl_stmt|;
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
name|Index
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
name|restHighLevelClient
operator|.
name|index
argument_list|(
name|indexRequest
argument_list|)
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
name|Update
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
name|restHighLevelClient
operator|.
name|update
argument_list|(
name|updateRequest
argument_list|)
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
name|GetById
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
name|restHighLevelClient
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
name|Bulk
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
name|restHighLevelClient
operator|.
name|bulk
argument_list|(
name|bulkRequest
argument_list|)
operator|.
name|getItems
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
name|BulkIndex
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
name|restHighLevelClient
operator|.
name|bulk
argument_list|(
name|bulkRequest
argument_list|)
operator|.
name|getItems
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
name|Delete
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
name|restHighLevelClient
operator|.
name|delete
argument_list|(
name|deleteRequest
argument_list|)
operator|.
name|getResult
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
name|DeleteIndex
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
name|performRequest
argument_list|(
literal|"Delete"
argument_list|,
name|deleteRequest
operator|.
name|index
argument_list|()
argument_list|)
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
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
name|Exists
condition|)
block|{
comment|// ExistsRequest API is deprecated, using SearchRequest instead with size=0 and terminate_after=1
name|SearchSourceBuilder
name|sourceBuilder
init|=
operator|new
name|SearchSourceBuilder
argument_list|()
decl_stmt|;
name|sourceBuilder
operator|.
name|size
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sourceBuilder
operator|.
name|terminateAfter
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|searchRequest
operator|.
name|source
argument_list|(
name|sourceBuilder
argument_list|)
expr_stmt|;
try|try
block|{
name|restHighLevelClient
operator|.
name|search
argument_list|(
name|searchRequest
argument_list|)
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
name|ElasticsearchStatusException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|status
argument_list|()
operator|.
name|equals
argument_list|(
name|RestStatus
operator|.
name|NOT_FOUND
argument_list|)
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|operation
operator|==
name|ElasticsearchOperation
operator|.
name|Search
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
name|restHighLevelClient
operator|.
name|search
argument_list|(
name|searchRequest
argument_list|)
operator|.
name|getHits
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
name|Ping
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|restHighLevelClient
operator|.
name|ping
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
name|Info
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|restHighLevelClient
operator|.
name|info
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
if|if
condition|(
name|configuration
operator|.
name|getDisconnect
argument_list|()
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getEnableSniffer
argument_list|()
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|sniffer
argument_list|)
expr_stmt|;
name|sniffer
operator|=
literal|null
expr_stmt|;
block|}
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
operator|!
name|configuration
operator|.
name|getDisconnect
argument_list|()
condition|)
block|{
name|startClient
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|startClient ()
specifier|private
name|void
name|startClient
parameter_list|()
throws|throws
name|NoSuchMethodException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
throws|,
name|InstantiationException
throws|,
name|UnknownHostException
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Connecting to the ElasticSearch cluster: {}"
argument_list|,
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
name|getHostAddressesList
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|configuration
operator|.
name|getHostAddressesList
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|client
operator|=
name|createClient
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Incorrect ip address and port parameters settings for ElasticSearch cluster"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createClient ()
specifier|private
name|RestClient
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
name|RestClientBuilder
name|builder
init|=
name|RestClient
operator|.
name|builder
argument_list|(
name|configuration
operator|.
name|getHostAddressesList
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|HttpHost
index|[
literal|0
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setMaxRetryTimeoutMillis
argument_list|(
name|configuration
operator|.
name|getMaxRetryTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setRequestConfigCallback
argument_list|(
name|requestConfigBuilder
lambda|->
name|requestConfigBuilder
operator|.
name|setConnectTimeout
argument_list|(
name|configuration
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
operator|.
name|setSocketTimeout
argument_list|(
name|configuration
operator|.
name|getSocketTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getUser
argument_list|()
operator|!=
literal|null
operator|&&
name|configuration
operator|.
name|getPassword
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|CredentialsProvider
name|credentialsProvider
init|=
operator|new
name|BasicCredentialsProvider
argument_list|()
decl_stmt|;
name|credentialsProvider
operator|.
name|setCredentials
argument_list|(
name|AuthScope
operator|.
name|ANY
argument_list|,
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|configuration
operator|.
name|getUser
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setHttpClientConfigCallback
argument_list|(
name|httpClientBuilder
lambda|->
block|{
name|httpClientBuilder
operator|.
name|setDefaultCredentialsProvider
argument_list|(
name|credentialsProvider
argument_list|)
expr_stmt|;
return|return
name|httpClientBuilder
return|;
block|}
argument_list|)
expr_stmt|;
block|}
specifier|final
name|RestClient
name|restClient
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getEnableSniffer
argument_list|()
condition|)
block|{
name|SnifferBuilder
name|snifferBuilder
init|=
name|Sniffer
operator|.
name|builder
argument_list|(
name|restClient
argument_list|)
decl_stmt|;
name|snifferBuilder
operator|.
name|setSniffIntervalMillis
argument_list|(
name|configuration
operator|.
name|getSnifferInterval
argument_list|()
argument_list|)
expr_stmt|;
name|snifferBuilder
operator|.
name|setSniffAfterFailureDelayMillis
argument_list|(
name|configuration
operator|.
name|getSniffAfterFailureDelay
argument_list|()
argument_list|)
expr_stmt|;
name|sniffer
operator|=
name|snifferBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|restClient
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
name|log
operator|.
name|info
argument_list|(
literal|"Disconnecting from ElasticSearch cluster: {}"
argument_list|,
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
if|if
condition|(
name|sniffer
operator|!=
literal|null
condition|)
block|{
name|sniffer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|RestClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|class|HighLevelClient
specifier|private
specifier|final
class|class
name|HighLevelClient
extends|extends
name|RestHighLevelClient
block|{
DECL|method|HighLevelClient (RestClient restClient)
specifier|private
name|HighLevelClient
parameter_list|(
name|RestClient
name|restClient
parameter_list|)
block|{
name|super
argument_list|(
name|restClient
argument_list|,
parameter_list|(
name|client
parameter_list|)
lambda|->
block|{ }
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

