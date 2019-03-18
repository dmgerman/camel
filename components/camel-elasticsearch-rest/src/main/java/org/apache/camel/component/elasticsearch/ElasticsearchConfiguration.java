begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|http
operator|.
name|HttpHost
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
DECL|field|hostAddressesList
specifier|private
name|List
argument_list|<
name|HttpHost
argument_list|>
name|hostAddressesList
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|clusterName
specifier|private
name|String
name|clusterName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|operation
specifier|private
name|ElasticsearchOperation
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
literal|""
operator|+
name|ElasticsearchConstants
operator|.
name|DEFAULT_FOR_WAIT_ACTIVE_SHARDS
argument_list|)
DECL|field|waitForActiveShards
specifier|private
name|int
name|waitForActiveShards
init|=
name|ElasticsearchConstants
operator|.
name|DEFAULT_FOR_WAIT_ACTIVE_SHARDS
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|hostAddresses
specifier|private
name|String
name|hostAddresses
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|ElasticsearchConstants
operator|.
name|DEFAULT_SOCKET_TIMEOUT
argument_list|)
DECL|field|socketTimeout
specifier|private
name|int
name|socketTimeout
init|=
name|ElasticsearchConstants
operator|.
name|DEFAULT_SOCKET_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|ElasticsearchConstants
operator|.
name|MAX_RETRY_TIMEOUT
argument_list|)
DECL|field|maxRetryTimeout
specifier|private
name|int
name|maxRetryTimeout
init|=
name|ElasticsearchConstants
operator|.
name|MAX_RETRY_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|ElasticsearchConstants
operator|.
name|DEFAULT_CONNECTION_TIMEOUT
argument_list|)
DECL|field|connectionTimeout
specifier|private
name|int
name|connectionTimeout
init|=
name|ElasticsearchConstants
operator|.
name|DEFAULT_CONNECTION_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|disconnect
specifier|private
name|boolean
name|disconnect
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|enableSSL
specifier|private
name|boolean
name|enableSSL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useScroll
specifier|private
name|boolean
name|useScroll
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|ElasticsearchConstants
operator|.
name|DEFAULT_SCROLL_KEEP_ALIVE_MS
argument_list|)
DECL|field|scrollKeepAliveMs
specifier|private
name|int
name|scrollKeepAliveMs
init|=
name|ElasticsearchConstants
operator|.
name|DEFAULT_SCROLL_KEEP_ALIVE_MS
decl_stmt|;
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|//Sniffer parameter.
DECL|field|enableSniffer
specifier|private
name|boolean
name|enableSniffer
decl_stmt|;
DECL|field|snifferInterval
specifier|private
name|int
name|snifferInterval
init|=
name|ElasticsearchConstants
operator|.
name|DEFAULT_SNIFFER_INTERVAL
decl_stmt|;
DECL|field|sniffAfterFailureDelay
specifier|private
name|int
name|sniffAfterFailureDelay
init|=
name|ElasticsearchConstants
operator|.
name|DEFAULT_AFTER_FAILURE_DELAY
decl_stmt|;
comment|/**      * Name of the cluster      */
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
name|ElasticsearchOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (ElasticsearchOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|ElasticsearchOperation
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
comment|/**      * Comma separated list with ip:port formatted remote transport addresses to use.      * The ip and port options must be left blank for hostAddresses to be considered instead.      */
DECL|method|getHostAddresses ()
specifier|public
name|String
name|getHostAddresses
parameter_list|()
block|{
return|return
name|hostAddresses
return|;
block|}
DECL|method|setHostAddresses (String hostAddresses)
specifier|public
name|void
name|setHostAddresses
parameter_list|(
name|String
name|hostAddresses
parameter_list|)
block|{
name|this
operator|.
name|hostAddresses
operator|=
name|hostAddresses
expr_stmt|;
block|}
comment|/**      * Index creation waits for the write consistency number of shards to be available      */
DECL|method|getWaitForActiveShards ()
specifier|public
name|int
name|getWaitForActiveShards
parameter_list|()
block|{
return|return
name|waitForActiveShards
return|;
block|}
DECL|method|setWaitForActiveShards (int waitForActiveShards)
specifier|public
name|void
name|setWaitForActiveShards
parameter_list|(
name|int
name|waitForActiveShards
parameter_list|)
block|{
name|this
operator|.
name|waitForActiveShards
operator|=
name|waitForActiveShards
expr_stmt|;
block|}
DECL|method|getHostAddressesList ()
specifier|public
name|List
argument_list|<
name|HttpHost
argument_list|>
name|getHostAddressesList
parameter_list|()
block|{
return|return
name|hostAddressesList
return|;
block|}
DECL|method|setHostAddressesList (List<HttpHost> hostAddressesList)
specifier|public
name|void
name|setHostAddressesList
parameter_list|(
name|List
argument_list|<
name|HttpHost
argument_list|>
name|hostAddressesList
parameter_list|)
block|{
name|this
operator|.
name|hostAddressesList
operator|=
name|hostAddressesList
expr_stmt|;
block|}
comment|/**      * The timeout in ms to wait before the socket will timeout.      */
DECL|method|getSocketTimeout ()
specifier|public
name|int
name|getSocketTimeout
parameter_list|()
block|{
return|return
name|socketTimeout
return|;
block|}
DECL|method|setSocketTimeout (int socketTimeout)
specifier|public
name|void
name|setSocketTimeout
parameter_list|(
name|int
name|socketTimeout
parameter_list|)
block|{
name|this
operator|.
name|socketTimeout
operator|=
name|socketTimeout
expr_stmt|;
block|}
comment|/**      *  The time in ms to wait before connection will timeout.      */
DECL|method|getConnectionTimeout ()
specifier|public
name|int
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|setConnectionTimeout (int connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|int
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
comment|/**      *  Basic authenticate user      */
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|setUser (String user)
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
comment|/**      *  Password for authenticate      */
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|/**      * Enable SSL      */
DECL|method|getEnableSSL ()
specifier|public
name|Boolean
name|getEnableSSL
parameter_list|()
block|{
return|return
name|enableSSL
return|;
block|}
DECL|method|setEnableSSL (Boolean enableSSL)
specifier|public
name|void
name|setEnableSSL
parameter_list|(
name|Boolean
name|enableSSL
parameter_list|)
block|{
name|this
operator|.
name|enableSSL
operator|=
name|enableSSL
expr_stmt|;
block|}
comment|/**      * The time in ms before retry      */
DECL|method|getMaxRetryTimeout ()
specifier|public
name|int
name|getMaxRetryTimeout
parameter_list|()
block|{
return|return
name|maxRetryTimeout
return|;
block|}
DECL|method|setMaxRetryTimeout (int maxRetryTimeout)
specifier|public
name|void
name|setMaxRetryTimeout
parameter_list|(
name|int
name|maxRetryTimeout
parameter_list|)
block|{
name|this
operator|.
name|maxRetryTimeout
operator|=
name|maxRetryTimeout
expr_stmt|;
block|}
comment|/**      * Disconnect after it finish calling the producer      */
DECL|method|getDisconnect ()
specifier|public
name|Boolean
name|getDisconnect
parameter_list|()
block|{
return|return
name|disconnect
return|;
block|}
DECL|method|setDisconnect (Boolean disconnect)
specifier|public
name|void
name|setDisconnect
parameter_list|(
name|Boolean
name|disconnect
parameter_list|)
block|{
name|this
operator|.
name|disconnect
operator|=
name|disconnect
expr_stmt|;
block|}
comment|/**      * Enable automatically discover nodes from a running Elasticsearch cluster      */
DECL|method|getEnableSniffer ()
specifier|public
name|Boolean
name|getEnableSniffer
parameter_list|()
block|{
return|return
name|enableSniffer
return|;
block|}
DECL|method|setEnableSniffer (Boolean enableSniffer)
specifier|public
name|void
name|setEnableSniffer
parameter_list|(
name|Boolean
name|enableSniffer
parameter_list|)
block|{
name|this
operator|.
name|enableSniffer
operator|=
name|enableSniffer
expr_stmt|;
block|}
comment|/**      * The interval between consecutive ordinary sniff executions in milliseconds. Will be honoured when      * sniffOnFailure is disabled or when there are no failures between consecutive sniff executions      */
DECL|method|getSnifferInterval ()
specifier|public
name|int
name|getSnifferInterval
parameter_list|()
block|{
return|return
name|snifferInterval
return|;
block|}
DECL|method|setSnifferInterval (int snifferInterval)
specifier|public
name|void
name|setSnifferInterval
parameter_list|(
name|int
name|snifferInterval
parameter_list|)
block|{
name|this
operator|.
name|snifferInterval
operator|=
name|snifferInterval
expr_stmt|;
block|}
comment|/**      * The delay of a sniff execution scheduled after a failure (in milliseconds)      */
DECL|method|getSniffAfterFailureDelay ()
specifier|public
name|int
name|getSniffAfterFailureDelay
parameter_list|()
block|{
return|return
name|sniffAfterFailureDelay
return|;
block|}
DECL|method|setSniffAfterFailureDelay (int sniffAfterFailureDelay)
specifier|public
name|void
name|setSniffAfterFailureDelay
parameter_list|(
name|int
name|sniffAfterFailureDelay
parameter_list|)
block|{
name|this
operator|.
name|sniffAfterFailureDelay
operator|=
name|sniffAfterFailureDelay
expr_stmt|;
block|}
comment|/**      * Enable scroll usage      */
DECL|method|getUseScroll ()
specifier|public
name|boolean
name|getUseScroll
parameter_list|()
block|{
return|return
name|useScroll
return|;
block|}
DECL|method|setUseScroll (boolean useScroll)
specifier|public
name|void
name|setUseScroll
parameter_list|(
name|boolean
name|useScroll
parameter_list|)
block|{
name|this
operator|.
name|useScroll
operator|=
name|useScroll
expr_stmt|;
block|}
comment|/**      * Time in ms during which elasticsearch will keep search context alive      */
DECL|method|getScrollKeepAliveMs ()
specifier|public
name|int
name|getScrollKeepAliveMs
parameter_list|()
block|{
return|return
name|scrollKeepAliveMs
return|;
block|}
DECL|method|setScrollKeepAliveMs (int scrollKeepAliveMs)
specifier|public
name|void
name|setScrollKeepAliveMs
parameter_list|(
name|int
name|scrollKeepAliveMs
parameter_list|)
block|{
name|this
operator|.
name|scrollKeepAliveMs
operator|=
name|scrollKeepAliveMs
expr_stmt|;
block|}
block|}
end_class

end_unit

