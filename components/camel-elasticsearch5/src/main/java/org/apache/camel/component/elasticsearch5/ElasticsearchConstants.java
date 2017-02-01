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
DECL|field|PARENT
name|String
name|PARENT
init|=
literal|"parent"
decl_stmt|;
DECL|field|TRANSPORT_ADDRESSES
name|String
name|TRANSPORT_ADDRESSES
init|=
literal|"transportAddresses"
decl_stmt|;
DECL|field|PROTOCOL
name|String
name|PROTOCOL
init|=
literal|"elasticsearch"
decl_stmt|;
DECL|field|IP
name|String
name|IP
init|=
literal|"ip"
decl_stmt|;
DECL|field|PORT
name|String
name|PORT
init|=
literal|"port"
decl_stmt|;
DECL|field|DEFAULT_PORT
name|int
name|DEFAULT_PORT
init|=
literal|9300
decl_stmt|;
DECL|field|DEFAULT_FOR_WAIT_ACTIVE_SHARDS
name|int
name|DEFAULT_FOR_WAIT_ACTIVE_SHARDS
init|=
literal|1
decl_stmt|;
comment|// Meaning only wait for the primary shard
DECL|field|TRANSPORT_ADDRESSES_SEPARATOR_REGEX
name|String
name|TRANSPORT_ADDRESSES_SEPARATOR_REGEX
init|=
literal|","
decl_stmt|;
DECL|field|IP_PORT_SEPARATOR_REGEX
name|String
name|IP_PORT_SEPARATOR_REGEX
init|=
literal|":"
decl_stmt|;
DECL|field|ES_QUERY_DSL_PREFIX
name|String
name|ES_QUERY_DSL_PREFIX
init|=
literal|"query"
decl_stmt|;
block|}
end_interface

end_unit

