begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
package|;
end_package

begin_interface
DECL|interface|EtcdConstants
specifier|public
interface|interface
name|EtcdConstants
block|{
DECL|field|ETCD_DEFAULT_URIS
name|String
name|ETCD_DEFAULT_URIS
init|=
literal|"http://localhost:2379,http://localhost:4001"
decl_stmt|;
DECL|field|ETCD_ACTION
name|String
name|ETCD_ACTION
init|=
literal|"CamelEtcdAction"
decl_stmt|;
DECL|field|ETCD_NAMESPACE
name|String
name|ETCD_NAMESPACE
init|=
literal|"CamelEtcdNamespace"
decl_stmt|;
DECL|field|ETCD_PATH
name|String
name|ETCD_PATH
init|=
literal|"CamelEtcdPath"
decl_stmt|;
DECL|field|ETCD_TIMEOUT
name|String
name|ETCD_TIMEOUT
init|=
literal|"CamelEtcdTimeout"
decl_stmt|;
DECL|field|ETCD_RECURSIVE
name|String
name|ETCD_RECURSIVE
init|=
literal|"CamelEtcdRecursive"
decl_stmt|;
DECL|field|ETCD_TTL
name|String
name|ETCD_TTL
init|=
literal|"CamelEtcdTtl"
decl_stmt|;
DECL|field|ETCD_KEYS_ACTION_SET
name|String
name|ETCD_KEYS_ACTION_SET
init|=
literal|"set"
decl_stmt|;
DECL|field|ETCD_KEYS_ACTION_DELETE
name|String
name|ETCD_KEYS_ACTION_DELETE
init|=
literal|"delete"
decl_stmt|;
DECL|field|ETCD_KEYS_ACTION_DELETE_DIR
name|String
name|ETCD_KEYS_ACTION_DELETE_DIR
init|=
literal|"deleteDir"
decl_stmt|;
DECL|field|ETCD_KEYS_ACTION_GET
name|String
name|ETCD_KEYS_ACTION_GET
init|=
literal|"get"
decl_stmt|;
DECL|field|ETCD_LEADER_STATS_PATH
name|String
name|ETCD_LEADER_STATS_PATH
init|=
literal|"/leader"
decl_stmt|;
DECL|field|ETCD_SELF_STATS_PATH
name|String
name|ETCD_SELF_STATS_PATH
init|=
literal|"/self"
decl_stmt|;
DECL|field|ETCD_STORE_STATS_PATH
name|String
name|ETCD_STORE_STATS_PATH
init|=
literal|"/store"
decl_stmt|;
block|}
end_interface

end_unit

