begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.solr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|solr
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
name|javax
operator|.
name|activation
operator|.
name|MimetypesFileTypeMap
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
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|SolrClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|SolrServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|request
operator|.
name|ContentStreamUpdateRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|request
operator|.
name|DirectXmlRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|request
operator|.
name|UpdateRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|common
operator|.
name|SolrException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|common
operator|.
name|SolrInputDocument
import|;
end_import

begin_comment
comment|/**  * The Solr producer.  */
end_comment

begin_class
DECL|class|SolrProducer
specifier|public
class|class
name|SolrProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|httpServer
specifier|private
name|SolrClient
name|httpServer
decl_stmt|;
DECL|field|concSolrServer
specifier|private
name|SolrClient
name|concSolrServer
decl_stmt|;
DECL|field|cloudSolrServer
specifier|private
name|SolrClient
name|cloudSolrServer
decl_stmt|;
DECL|method|SolrProducer (SolrEndpoint endpoint, SolrClient solrServer, SolrClient concSolrServer, SolrClient cloudSolrServer)
specifier|public
name|SolrProducer
parameter_list|(
name|SolrEndpoint
name|endpoint
parameter_list|,
name|SolrClient
name|solrServer
parameter_list|,
name|SolrClient
name|concSolrServer
parameter_list|,
name|SolrClient
name|cloudSolrServer
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|httpServer
operator|=
name|solrServer
expr_stmt|;
name|this
operator|.
name|concSolrServer
operator|=
name|concSolrServer
expr_stmt|;
name|this
operator|.
name|cloudSolrServer
operator|=
name|cloudSolrServer
expr_stmt|;
block|}
DECL|method|getBestSolrServer (String operation)
specifier|private
name|SolrClient
name|getBestSolrServer
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|cloudSolrServer
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|cloudSolrServer
return|;
block|}
elseif|else
if|if
condition|(
name|SolrConstants
operator|.
name|OPERATION_INSERT_STREAMING
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
return|return
name|this
operator|.
name|concSolrServer
return|;
block|}
else|else
block|{
return|return
name|this
operator|.
name|httpServer
return|;
block|}
block|}
annotation|@
name|Override
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
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SolrConstants
operator|.
name|OPERATION
argument_list|)
decl_stmt|;
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
name|SolrConstants
operator|.
name|OPERATION
operator|+
literal|" header is missing"
argument_list|)
throw|;
block|}
name|SolrClient
name|serverToUse
init|=
name|getBestSolrServer
argument_list|(
name|operation
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|SolrConstants
operator|.
name|OPERATION_INSERT
argument_list|)
condition|)
block|{
name|insert
argument_list|(
name|exchange
argument_list|,
name|serverToUse
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
name|SolrConstants
operator|.
name|OPERATION_INSERT_STREAMING
argument_list|)
condition|)
block|{
name|insert
argument_list|(
name|exchange
argument_list|,
name|serverToUse
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
name|SolrConstants
operator|.
name|OPERATION_DELETE_BY_ID
argument_list|)
condition|)
block|{
name|serverToUse
operator|.
name|deleteById
argument_list|(
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
name|SolrConstants
operator|.
name|OPERATION_DELETE_BY_QUERY
argument_list|)
condition|)
block|{
name|serverToUse
operator|.
name|deleteByQuery
argument_list|(
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
name|SolrConstants
operator|.
name|OPERATION_ADD_BEAN
argument_list|)
condition|)
block|{
name|serverToUse
operator|.
name|addBean
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
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
name|SolrConstants
operator|.
name|OPERATION_ADD_BEANS
argument_list|)
condition|)
block|{
name|serverToUse
operator|.
name|addBeans
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Collection
operator|.
name|class
argument_list|)
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
name|SolrConstants
operator|.
name|OPERATION_COMMIT
argument_list|)
condition|)
block|{
name|serverToUse
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|SolrConstants
operator|.
name|OPERATION_ROLLBACK
argument_list|)
condition|)
block|{
name|serverToUse
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|SolrConstants
operator|.
name|OPERATION_OPTIMIZE
argument_list|)
condition|)
block|{
name|serverToUse
operator|.
name|optimize
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|SolrConstants
operator|.
name|OPERATION
operator|+
literal|" header value '"
operator|+
name|operation
operator|+
literal|"' is not supported"
argument_list|)
throw|;
block|}
block|}
DECL|method|insert (Exchange exchange, SolrClient solrServer)
specifier|private
name|void
name|insert
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SolrClient
name|solrServer
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|boolean
name|invalid
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|WrappedFile
condition|)
block|{
name|body
operator|=
operator|(
operator|(
name|WrappedFile
argument_list|<
name|?
argument_list|>
operator|)
name|body
operator|)
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|File
condition|)
block|{
name|MimetypesFileTypeMap
name|mimeTypesMap
init|=
operator|new
name|MimetypesFileTypeMap
argument_list|()
decl_stmt|;
name|String
name|mimeType
init|=
name|mimeTypesMap
operator|.
name|getContentType
argument_list|(
operator|(
name|File
operator|)
name|body
argument_list|)
decl_stmt|;
name|ContentStreamUpdateRequest
name|updateRequest
init|=
operator|new
name|ContentStreamUpdateRequest
argument_list|(
name|getRequestHandler
argument_list|()
argument_list|)
decl_stmt|;
name|updateRequest
operator|.
name|addFile
argument_list|(
operator|(
name|File
operator|)
name|body
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|SolrConstants
operator|.
name|PARAM
argument_list|)
condition|)
block|{
name|String
name|paramName
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|substring
argument_list|(
name|SolrConstants
operator|.
name|PARAM
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|updateRequest
operator|.
name|setParam
argument_list|(
name|paramName
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|updateRequest
operator|.
name|process
argument_list|(
name|solrServer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|SolrInputDocument
condition|)
block|{
name|UpdateRequest
name|updateRequest
init|=
operator|new
name|UpdateRequest
argument_list|(
name|getRequestHandler
argument_list|()
argument_list|)
decl_stmt|;
name|updateRequest
operator|.
name|add
argument_list|(
operator|(
name|SolrInputDocument
operator|)
name|body
argument_list|)
expr_stmt|;
name|updateRequest
operator|.
name|process
argument_list|(
name|solrServer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|body
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|SolrInputDocument
condition|)
block|{
name|UpdateRequest
name|updateRequest
init|=
operator|new
name|UpdateRequest
argument_list|(
name|getRequestHandler
argument_list|()
argument_list|)
decl_stmt|;
name|updateRequest
operator|.
name|add
argument_list|(
operator|(
name|List
argument_list|<
name|SolrInputDocument
argument_list|>
operator|)
name|list
argument_list|)
expr_stmt|;
name|updateRequest
operator|.
name|process
argument_list|(
name|solrServer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|invalid
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|boolean
name|hasSolrHeaders
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|SolrConstants
operator|.
name|FIELD
argument_list|)
condition|)
block|{
name|hasSolrHeaders
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|hasSolrHeaders
condition|)
block|{
name|UpdateRequest
name|updateRequest
init|=
operator|new
name|UpdateRequest
argument_list|(
name|getRequestHandler
argument_list|()
argument_list|)
decl_stmt|;
name|SolrInputDocument
name|doc
init|=
operator|new
name|SolrInputDocument
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|SolrConstants
operator|.
name|FIELD
argument_list|)
condition|)
block|{
name|String
name|fieldName
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|substring
argument_list|(
name|SolrConstants
operator|.
name|FIELD
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|doc
operator|.
name|setField
argument_list|(
name|fieldName
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|updateRequest
operator|.
name|add
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|updateRequest
operator|.
name|process
argument_list|(
name|solrServer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
name|String
name|bodyAsString
init|=
operator|(
name|String
operator|)
name|body
decl_stmt|;
if|if
condition|(
operator|!
name|bodyAsString
operator|.
name|startsWith
argument_list|(
literal|"<add"
argument_list|)
condition|)
block|{
name|bodyAsString
operator|=
literal|"<add>"
operator|+
name|bodyAsString
operator|+
literal|"</add>"
expr_stmt|;
block|}
name|DirectXmlRequest
name|xmlRequest
init|=
operator|new
name|DirectXmlRequest
argument_list|(
name|getRequestHandler
argument_list|()
argument_list|,
name|bodyAsString
argument_list|)
decl_stmt|;
name|solrServer
operator|.
name|request
argument_list|(
name|xmlRequest
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|invalid
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|SolrException
argument_list|(
name|SolrException
operator|.
name|ErrorCode
operator|.
name|BAD_REQUEST
argument_list|,
literal|"unable to find data in Exchange to update Solr"
argument_list|)
throw|;
block|}
block|}
end_class

begin_function
DECL|method|getRequestHandler ()
specifier|private
name|String
name|getRequestHandler
parameter_list|()
block|{
name|String
name|requestHandler
init|=
name|getEndpoint
argument_list|()
operator|.
name|getRequestHandler
argument_list|()
decl_stmt|;
return|return
operator|(
name|requestHandler
operator|==
literal|null
operator|)
condition|?
literal|"/update"
else|:
name|requestHandler
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SolrEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SolrEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|onProducerShutdown
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

