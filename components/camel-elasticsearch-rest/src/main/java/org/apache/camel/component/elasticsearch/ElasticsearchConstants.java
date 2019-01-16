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

begin_interface
DECL|interface|ElasticsearchConstants
specifier|public
interface|interface
name|ElasticsearchConstants
block|{
DECL|field|PARAM_OPERATION
name|String
name|PARAM_OPERATION
init|=
literal|"operation"
decl_stmt|;
DECL|field|PARAM_INDEX_ID
name|String
name|PARAM_INDEX_ID
init|=
literal|"indexId"
decl_stmt|;
DECL|field|PARAM_INDEX_NAME
name|String
name|PARAM_INDEX_NAME
init|=
literal|"indexName"
decl_stmt|;
DECL|field|PARAM_INDEX_TYPE
name|String
name|PARAM_INDEX_TYPE
init|=
literal|"indexType"
decl_stmt|;
DECL|field|PARAM_WAIT_FOR_ACTIVE_SHARDS
name|String
name|PARAM_WAIT_FOR_ACTIVE_SHARDS
init|=
literal|"waitForActiveShards"
decl_stmt|;
DECL|field|PARAM_SCROLL_KEEP_ALIVE_MS
name|String
name|PARAM_SCROLL_KEEP_ALIVE_MS
init|=
literal|"scrollKeepAliveMs"
decl_stmt|;
DECL|field|PARAM_SCROLL
name|String
name|PARAM_SCROLL
init|=
literal|"useScroll"
decl_stmt|;
DECL|field|PROPERTY_SCROLL_ES_QUERY_COUNT
name|String
name|PROPERTY_SCROLL_ES_QUERY_COUNT
init|=
literal|"CamelElasticsearchScrollQueryCount"
decl_stmt|;
DECL|field|DEFAULT_PORT
name|int
name|DEFAULT_PORT
init|=
literal|9200
decl_stmt|;
DECL|field|DEFAULT_FOR_WAIT_ACTIVE_SHARDS
name|int
name|DEFAULT_FOR_WAIT_ACTIVE_SHARDS
init|=
literal|1
decl_stmt|;
comment|// Meaning only wait for the primary shard
DECL|field|DEFAULT_SOCKET_TIMEOUT
name|int
name|DEFAULT_SOCKET_TIMEOUT
init|=
literal|30000
decl_stmt|;
comment|// Meaning how long time to wait before the socket timeout
DECL|field|MAX_RETRY_TIMEOUT
name|int
name|MAX_RETRY_TIMEOUT
init|=
literal|30000
decl_stmt|;
comment|// Meaning how long to wait before retry again
DECL|field|DEFAULT_CONNECTION_TIMEOUT
name|int
name|DEFAULT_CONNECTION_TIMEOUT
init|=
literal|30000
decl_stmt|;
comment|// Meaning how many seconds before it timeout when establish connection
DECL|field|DEFAULT_SNIFFER_INTERVAL
name|int
name|DEFAULT_SNIFFER_INTERVAL
init|=
literal|60000
operator|*
literal|5
decl_stmt|;
comment|// Meaning how often it should search for elasticsearch nodes
DECL|field|DEFAULT_AFTER_FAILURE_DELAY
name|int
name|DEFAULT_AFTER_FAILURE_DELAY
init|=
literal|60000
decl_stmt|;
comment|// Meaning when should the sniff execution scheduled after a failure
DECL|field|DEFAULT_SCROLL_KEEP_ALIVE_MS
name|int
name|DEFAULT_SCROLL_KEEP_ALIVE_MS
init|=
literal|60000
decl_stmt|;
comment|// Meaning how many milliseconds elasticsearch will keep the search context
block|}
end_interface

end_unit

