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
name|Consumer
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
name|Producer
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
name|RuntimeCamelException
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
name|DefaultEndpoint
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
name|UriEndpoint
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
name|ImmutableSettings
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
name|node
operator|.
name|Node
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
comment|/**  * Represents an Elasticsearch endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"elasticsearch"
argument_list|,
name|title
operator|=
literal|"Elasticsearch"
argument_list|,
name|syntax
operator|=
literal|"elasticsearch:clusterName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"monitoring,search"
argument_list|)
DECL|class|ElasticsearchEndpoint
specifier|public
class|class
name|ElasticsearchEndpoint
extends|extends
name|DefaultEndpoint
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
name|ElasticsearchEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|node
specifier|private
name|Node
name|node
decl_stmt|;
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|ElasticsearchConfiguration
name|configuration
decl_stmt|;
DECL|method|ElasticsearchEndpoint (String uri, ElasticsearchComponent component, Map<String, Object> parameters)
specifier|public
name|ElasticsearchEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|ElasticsearchComponent
name|component
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
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ElasticsearchProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot consume to a ElasticsearchEndpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
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
if|if
condition|(
name|configuration
operator|.
name|isLocal
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting local ElasticSearch server"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Joining ElasticSearch cluster "
operator|+
name|configuration
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|this
operator|.
name|client
operator|=
operator|new
name|TransportClient
argument_list|(
name|getSettings
argument_list|()
argument_list|)
operator|.
name|addTransportAddress
argument_list|(
operator|new
name|InetSocketTransportAddress
argument_list|(
name|configuration
operator|.
name|getIp
argument_list|()
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
name|getTransportAddresses
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|configuration
operator|.
name|getTransportAddresses
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
argument_list|(
name|configuration
operator|.
name|getTransportAddresses
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
name|getTransportAddresses
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
name|this
operator|.
name|client
operator|=
operator|new
name|TransportClient
argument_list|(
name|getSettings
argument_list|()
argument_list|)
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
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|node
operator|=
name|configuration
operator|.
name|buildNode
argument_list|()
expr_stmt|;
name|client
operator|=
name|node
operator|.
name|client
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getSettings ()
specifier|private
name|Settings
name|getSettings
parameter_list|()
block|{
return|return
name|ImmutableSettings
operator|.
name|settingsBuilder
argument_list|()
comment|// setting the classloader here will allow the underlying elasticsearch-java
comment|// class to find its names.txt in an OSGi environment (otherwise the thread
comment|// classloader is used, which won't be able to see the file causing a startup
comment|// exception).
operator|.
name|classLoader
argument_list|(
name|Settings
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
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
literal|"client.transport.ignore_cluster_name"
argument_list|,
literal|false
argument_list|)
operator|.
name|put
argument_list|(
literal|"node.client"
argument_list|,
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"client.transport.sniff"
argument_list|,
literal|true
argument_list|)
operator|.
name|build
argument_list|()
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
name|configuration
operator|.
name|isLocal
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping local ElasticSearch server"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Leaving ElasticSearch cluster "
operator|+
name|configuration
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|close
argument_list|()
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
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|getConfig ()
specifier|public
name|ElasticsearchConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|configuration
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
name|configuration
operator|.
name|setOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

