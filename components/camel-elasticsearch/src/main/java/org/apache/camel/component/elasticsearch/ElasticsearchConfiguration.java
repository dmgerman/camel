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
name|common
operator|.
name|transport
operator|.
name|InetSocketTransportAddress
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
DECL|field|local
specifier|private
name|boolean
name|local
decl_stmt|;
DECL|field|transportAddressesList
specifier|private
name|List
argument_list|<
name|InetSocketTransportAddress
argument_list|>
name|transportAddressesList
decl_stmt|;
annotation|@
name|UriPath
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
argument_list|(
name|enums
operator|=
literal|"INDEX,UPDATE,BULK,BULK_INDEX,GET_BY_ID,DELETE"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
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
argument_list|(
name|defaultValue
operator|=
literal|"DEFAULT"
argument_list|)
DECL|field|consistencyLevel
specifier|private
name|WriteConsistencyLevel
name|consistencyLevel
init|=
name|ElasticsearchConstants
operator|.
name|DEFAULT_CONSISTENCY_LEVEL
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
DECL|field|ip
specifier|private
name|String
name|ip
decl_stmt|;
annotation|@
name|UriParam
DECL|field|transportAddresses
specifier|private
name|String
name|transportAddresses
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"9300"
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
init|=
name|ElasticsearchConstants
operator|.
name|DEFAULT_PORT
decl_stmt|;
comment|/**      * Name of cluster or use local for local mode      */
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
comment|/**      * What operation to perform      */
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
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
comment|/**      * The name of the index to act against      */
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
comment|/**      * The type of the index to act against      */
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
comment|/**      * The write consistency level to use with INDEX and BULK operations (can be any of ONE, QUORUM, ALL or DEFAULT)      */
DECL|method|getConsistencyLevel ()
specifier|public
name|WriteConsistencyLevel
name|getConsistencyLevel
parameter_list|()
block|{
return|return
name|consistencyLevel
return|;
block|}
DECL|method|setConsistencyLevel (WriteConsistencyLevel consistencyLevel)
specifier|public
name|void
name|setConsistencyLevel
parameter_list|(
name|WriteConsistencyLevel
name|consistencyLevel
parameter_list|)
block|{
name|this
operator|.
name|consistencyLevel
operator|=
name|consistencyLevel
expr_stmt|;
block|}
comment|/**      * Is the node going to be allowed to allocate data (shards) to it or not. This setting map to the<tt>node.data</tt> setting.      */
DECL|method|getData ()
specifier|public
name|Boolean
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
DECL|method|setData (Boolean data)
specifier|public
name|void
name|setData
parameter_list|(
name|Boolean
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
comment|/**      * The TransportClient remote host ip to use      */
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
comment|/**      * Comma separated list with ip:port formatted remote transport addresses to use.      * The ip and port options must be left blank for transportAddresses to be considered instead.      */
DECL|method|getTransportAddresses ()
specifier|public
name|String
name|getTransportAddresses
parameter_list|()
block|{
return|return
name|transportAddresses
return|;
block|}
DECL|method|setTransportAddresses (String transportAddresses)
specifier|public
name|void
name|setTransportAddresses
parameter_list|(
name|String
name|transportAddresses
parameter_list|)
block|{
name|this
operator|.
name|transportAddresses
operator|=
name|transportAddresses
expr_stmt|;
block|}
comment|/**      * The TransportClient remote port to use (defaults to 9300)      */
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
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
DECL|method|getTransportAddressesList ()
specifier|public
name|List
argument_list|<
name|InetSocketTransportAddress
argument_list|>
name|getTransportAddressesList
parameter_list|()
block|{
return|return
name|transportAddressesList
return|;
block|}
DECL|method|setTransportAddressesList (List<InetSocketTransportAddress> transportAddressesList)
specifier|public
name|void
name|setTransportAddressesList
parameter_list|(
name|List
argument_list|<
name|InetSocketTransportAddress
argument_list|>
name|transportAddressesList
parameter_list|)
block|{
name|this
operator|.
name|transportAddressesList
operator|=
name|transportAddressesList
expr_stmt|;
block|}
block|}
end_class

end_unit

