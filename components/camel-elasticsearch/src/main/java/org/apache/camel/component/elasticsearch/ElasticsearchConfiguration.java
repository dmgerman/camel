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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|spi
operator|.
name|Metadata
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
name|UriParam
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
name|UriParams
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
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|node
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|node
operator|.
name|NodeBuilder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|elasticsearch
operator|.
name|node
operator|.
name|NodeBuilder
operator|.
name|nodeBuilder
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|ElasticsearchConfiguration
specifier|public
class|class
name|ElasticsearchConfiguration
block|{
DECL|field|PARAM_OPERATION
specifier|public
specifier|static
specifier|final
name|String
name|PARAM_OPERATION
init|=
literal|"operation"
decl_stmt|;
DECL|field|OPERATION_INDEX
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_INDEX
init|=
literal|"INDEX"
decl_stmt|;
DECL|field|OPERATION_BULK
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_BULK
init|=
literal|"BULK"
decl_stmt|;
DECL|field|OPERATION_BULK_INDEX
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_BULK_INDEX
init|=
literal|"BULK_INDEX"
decl_stmt|;
DECL|field|OPERATION_GET_BY_ID
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_GET_BY_ID
init|=
literal|"GET_BY_ID"
decl_stmt|;
DECL|field|OPERATION_DELETE
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_DELETE
init|=
literal|"DELETE"
decl_stmt|;
DECL|field|PARAM_INDEX_ID
specifier|public
specifier|static
specifier|final
name|String
name|PARAM_INDEX_ID
init|=
literal|"indexId"
decl_stmt|;
DECL|field|PARAM_DATA
specifier|public
specifier|static
specifier|final
name|String
name|PARAM_DATA
init|=
literal|"data"
decl_stmt|;
DECL|field|PARAM_INDEX_NAME
specifier|public
specifier|static
specifier|final
name|String
name|PARAM_INDEX_NAME
init|=
literal|"indexName"
decl_stmt|;
DECL|field|PARAM_INDEX_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|PARAM_INDEX_TYPE
init|=
literal|"indexType"
decl_stmt|;
DECL|field|PROTOCOL
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL
init|=
literal|"elasticsearch"
decl_stmt|;
DECL|field|LOCAL_NAME
specifier|private
specifier|static
specifier|final
name|String
name|LOCAL_NAME
init|=
literal|"local"
decl_stmt|;
DECL|field|IP
specifier|private
specifier|static
specifier|final
name|String
name|IP
init|=
literal|"ip"
decl_stmt|;
DECL|field|PORT
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
literal|"port"
decl_stmt|;
DECL|field|DEFAULT_PORT
specifier|private
specifier|static
specifier|final
name|Integer
name|DEFAULT_PORT
init|=
literal|9300
decl_stmt|;
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of cluster or use local for local mode"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|clusterName
specifier|private
name|String
name|clusterName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|protocolType
specifier|private
name|String
name|protocolType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|authority
specifier|private
name|String
name|authority
decl_stmt|;
annotation|@
name|UriParam
DECL|field|indexName
specifier|private
name|String
name|indexName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|indexType
specifier|private
name|String
name|indexType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|local
specifier|private
name|boolean
name|local
decl_stmt|;
annotation|@
name|UriParam
DECL|field|data
specifier|private
name|Boolean
name|data
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"INDEX,BULK,BULK_INDEX,GET_BY_ID,DELETE"
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ip
specifier|private
name|String
name|ip
decl_stmt|;
annotation|@
name|UriParam
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
DECL|method|ElasticsearchConfiguration (URI uri, Map<String, Object> parameters)
specifier|public
name|ElasticsearchConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|protocol
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|PROTOCOL
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unrecognized elasticsearch protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|setAuthority
argument_list|(
name|uri
operator|.
name|getAuthority
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isValidAuthority
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|uri
operator|.
name|toASCIIString
argument_list|()
argument_list|,
literal|"incorrect URI syntax specified for the elasticsearch endpoint."
operator|+
literal|"please specify the syntax as \"elasticsearch:[Cluster Name | 'local']?[Query]\""
argument_list|)
throw|;
block|}
if|if
condition|(
name|LOCAL_NAME
operator|.
name|equals
argument_list|(
name|getAuthority
argument_list|()
argument_list|)
condition|)
block|{
name|setLocal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setClusterName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setLocal
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setClusterName
argument_list|(
name|getAuthority
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|data
operator|=
name|toBoolean
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|PARAM_DATA
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
name|data
operator|=
name|local
expr_stmt|;
block|}
if|if
condition|(
name|local
operator|&&
operator|!
name|data
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid to use local node without data"
argument_list|)
throw|;
block|}
name|indexName
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|remove
argument_list|(
name|PARAM_INDEX_NAME
argument_list|)
expr_stmt|;
name|indexType
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|remove
argument_list|(
name|PARAM_INDEX_TYPE
argument_list|)
expr_stmt|;
name|operation
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|remove
argument_list|(
name|PARAM_OPERATION
argument_list|)
expr_stmt|;
name|ip
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|remove
argument_list|(
name|IP
argument_list|)
expr_stmt|;
name|String
name|portParam
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|remove
argument_list|(
name|PORT
argument_list|)
decl_stmt|;
name|port
operator|=
name|portParam
operator|==
literal|null
condition|?
name|DEFAULT_PORT
else|:
name|Integer
operator|.
name|valueOf
argument_list|(
name|portParam
argument_list|)
expr_stmt|;
block|}
DECL|method|toBoolean (Object string)
specifier|protected
name|Boolean
name|toBoolean
parameter_list|(
name|Object
name|string
parameter_list|)
block|{
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|string
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
literal|"false"
operator|.
name|equals
argument_list|(
name|string
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|buildNode ()
specifier|public
name|Node
name|buildNode
parameter_list|()
block|{
name|NodeBuilder
name|builder
init|=
name|nodeBuilder
argument_list|()
operator|.
name|local
argument_list|(
name|isLocal
argument_list|()
argument_list|)
operator|.
name|data
argument_list|(
name|isData
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isLocal
argument_list|()
operator|&&
name|getClusterName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|clusterName
argument_list|(
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|node
argument_list|()
return|;
block|}
DECL|method|isValidAuthority ()
specifier|private
name|boolean
name|isValidAuthority
parameter_list|()
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|authority
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (URI uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getProtocolType ()
specifier|public
name|String
name|getProtocolType
parameter_list|()
block|{
return|return
name|protocolType
return|;
block|}
DECL|method|setProtocolType (String protocolType)
specifier|public
name|void
name|setProtocolType
parameter_list|(
name|String
name|protocolType
parameter_list|)
block|{
name|this
operator|.
name|protocolType
operator|=
name|protocolType
expr_stmt|;
block|}
DECL|method|getClusterName ()
specifier|public
name|String
name|getClusterName
parameter_list|()
block|{
return|return
name|clusterName
return|;
block|}
DECL|method|setClusterName (String clusterName)
specifier|public
name|void
name|setClusterName
parameter_list|(
name|String
name|clusterName
parameter_list|)
block|{
name|this
operator|.
name|clusterName
operator|=
name|clusterName
expr_stmt|;
block|}
DECL|method|getAuthority ()
specifier|public
name|String
name|getAuthority
parameter_list|()
block|{
return|return
name|authority
return|;
block|}
DECL|method|setAuthority (String authority)
specifier|public
name|void
name|setAuthority
parameter_list|(
name|String
name|authority
parameter_list|)
block|{
name|this
operator|.
name|authority
operator|=
name|authority
expr_stmt|;
block|}
DECL|method|getIndexName ()
specifier|public
name|String
name|getIndexName
parameter_list|()
block|{
return|return
name|indexName
return|;
block|}
DECL|method|setIndexName (String indexName)
specifier|public
name|void
name|setIndexName
parameter_list|(
name|String
name|indexName
parameter_list|)
block|{
name|this
operator|.
name|indexName
operator|=
name|indexName
expr_stmt|;
block|}
DECL|method|getIndexType ()
specifier|public
name|String
name|getIndexType
parameter_list|()
block|{
return|return
name|indexType
return|;
block|}
DECL|method|setIndexType (String indexType)
specifier|public
name|void
name|setIndexType
parameter_list|(
name|String
name|indexType
parameter_list|)
block|{
name|this
operator|.
name|indexType
operator|=
name|indexType
expr_stmt|;
block|}
DECL|method|isLocal ()
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|local
return|;
block|}
DECL|method|setLocal (boolean local)
specifier|public
name|void
name|setLocal
parameter_list|(
name|boolean
name|local
parameter_list|)
block|{
name|this
operator|.
name|local
operator|=
name|local
expr_stmt|;
block|}
DECL|method|isData ()
specifier|public
name|boolean
name|isData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
DECL|method|setData (boolean data)
specifier|public
name|void
name|setData
parameter_list|(
name|boolean
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|this
operator|.
name|operation
return|;
block|}
DECL|method|getIp ()
specifier|public
name|String
name|getIp
parameter_list|()
block|{
return|return
name|ip
return|;
block|}
DECL|method|setIp (String ip)
specifier|public
name|void
name|setIp
parameter_list|(
name|String
name|ip
parameter_list|)
block|{
name|this
operator|.
name|ip
operator|=
name|ip
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
block|}
end_class

end_unit

