begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch.converter
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
operator|.
name|converter
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
name|Converter
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
name|component
operator|.
name|elasticsearch
operator|.
name|ElasticsearchConstants
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
name|WriteConsistencyLevel
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
name|support
operator|.
name|replication
operator|.
name|ReplicationType
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
name|common
operator|.
name|xcontent
operator|.
name|XContentBuilder
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|ElasticsearchActionRequestConverter
specifier|public
specifier|final
class|class
name|ElasticsearchActionRequestConverter
block|{
DECL|method|ElasticsearchActionRequestConverter ()
specifier|private
name|ElasticsearchActionRequestConverter
parameter_list|()
block|{     }
comment|// Update requests
DECL|method|createUpdateRequest (Object document, Exchange exchange)
specifier|private
specifier|static
name|UpdateRequest
name|createUpdateRequest
parameter_list|(
name|Object
name|document
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|UpdateRequest
name|updateRequest
init|=
operator|new
name|UpdateRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|document
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|updateRequest
operator|.
name|doc
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
name|updateRequest
operator|.
name|doc
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
name|updateRequest
operator|.
name|doc
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
name|updateRequest
operator|.
name|doc
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
return|return
literal|null
return|;
block|}
return|return
name|updateRequest
operator|.
name|consistencyLevel
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
name|PARAM_CONSISTENCY_LEVEL
argument_list|,
name|WriteConsistencyLevel
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|replicationType
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
name|PARAM_REPLICATION_TYPE
argument_list|,
name|ReplicationType
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|parent
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
name|PARENT
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|index
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
operator|.
name|type
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
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
comment|// Index requests
DECL|method|createIndexRequest (Object document, Exchange exchange)
specifier|private
specifier|static
name|IndexRequest
name|createIndexRequest
parameter_list|(
name|Object
name|document
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|IndexRequest
name|indexRequest
init|=
operator|new
name|IndexRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|document
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|indexRequest
operator|.
name|source
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
name|indexRequest
operator|.
name|source
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
name|indexRequest
operator|.
name|source
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
name|indexRequest
operator|.
name|source
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
return|return
literal|null
return|;
block|}
return|return
name|indexRequest
operator|.
name|consistencyLevel
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
name|PARAM_CONSISTENCY_LEVEL
argument_list|,
name|WriteConsistencyLevel
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|replicationType
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
name|PARAM_REPLICATION_TYPE
argument_list|,
name|ReplicationType
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|parent
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
name|PARENT
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|index
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
operator|.
name|type
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
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toIndexRequest (Object document, Exchange exchange)
specifier|public
specifier|static
name|IndexRequest
name|toIndexRequest
parameter_list|(
name|Object
name|document
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|createIndexRequest
argument_list|(
name|document
argument_list|,
name|exchange
argument_list|)
operator|.
name|id
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
name|PARAM_INDEX_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toUpdateRequest (Object document, Exchange exchange)
specifier|public
specifier|static
name|UpdateRequest
name|toUpdateRequest
parameter_list|(
name|Object
name|document
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|createUpdateRequest
argument_list|(
name|document
argument_list|,
name|exchange
argument_list|)
operator|.
name|id
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
name|PARAM_INDEX_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toGetRequest (String id, Exchange exchange)
specifier|public
specifier|static
name|GetRequest
name|toGetRequest
parameter_list|(
name|String
name|id
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|GetRequest
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
operator|.
name|type
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
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toDeleteRequest (String id, Exchange exchange)
specifier|public
specifier|static
name|DeleteRequest
name|toDeleteRequest
parameter_list|(
name|String
name|id
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|DeleteRequest
argument_list|()
operator|.
name|index
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
operator|.
name|type
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
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toSearchRequest (Object queryObject, Exchange exchange)
specifier|public
specifier|static
name|SearchRequest
name|toSearchRequest
parameter_list|(
name|Object
name|queryObject
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
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
operator|.
name|types
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
name|PARAM_INDEX_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
comment|// Setup the query object into the search request
if|if
condition|(
name|queryObject
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|searchRequest
operator|.
name|source
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|queryObject
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|queryObject
operator|instanceof
name|Map
condition|)
block|{
name|searchRequest
operator|.
name|source
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|queryObject
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|queryObject
operator|instanceof
name|String
condition|)
block|{
name|searchRequest
operator|.
name|source
argument_list|(
operator|(
name|String
operator|)
name|queryObject
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|queryObject
operator|instanceof
name|XContentBuilder
condition|)
block|{
name|searchRequest
operator|.
name|source
argument_list|(
operator|(
name|XContentBuilder
operator|)
name|queryObject
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Cannot convert the queryObject into SearchRequest
return|return
literal|null
return|;
block|}
return|return
name|searchRequest
return|;
block|}
annotation|@
name|Converter
DECL|method|toBulkRequest (List<Object> documents, Exchange exchange)
specifier|public
specifier|static
name|BulkRequest
name|toBulkRequest
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|documents
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|BulkRequest
name|request
init|=
operator|new
name|BulkRequest
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|document
range|:
name|documents
control|)
block|{
name|request
operator|.
name|add
argument_list|(
name|createIndexRequest
argument_list|(
name|document
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|request
return|;
block|}
block|}
end_class

end_unit

