begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
package|;
end_package

begin_interface
DECL|interface|KubernetesCategory
specifier|public
interface|interface
name|KubernetesCategory
block|{
DECL|field|NAMESPACES
name|String
name|NAMESPACES
init|=
literal|"namespaces"
decl_stmt|;
DECL|field|SERVICES
name|String
name|SERVICES
init|=
literal|"services"
decl_stmt|;
DECL|field|REPLICATION_CONTROLLERS
name|String
name|REPLICATION_CONTROLLERS
init|=
literal|"replicationControllers"
decl_stmt|;
DECL|field|PODS
name|String
name|PODS
init|=
literal|"pods"
decl_stmt|;
DECL|field|PERSISTENT_VOLUMES
name|String
name|PERSISTENT_VOLUMES
init|=
literal|"persistentVolumes"
decl_stmt|;
DECL|field|PERSISTENT_VOLUMES_CLAIMS
name|String
name|PERSISTENT_VOLUMES_CLAIMS
init|=
literal|"persistentVolumesClaims"
decl_stmt|;
DECL|field|SECRETS
name|String
name|SECRETS
init|=
literal|"secrets"
decl_stmt|;
DECL|field|RESOURCES_QUOTA
name|String
name|RESOURCES_QUOTA
init|=
literal|"resourcesQuota"
decl_stmt|;
DECL|field|SERVICE_ACCOUNTS
name|String
name|SERVICE_ACCOUNTS
init|=
literal|"serviceAccounts"
decl_stmt|;
DECL|field|NODES
name|String
name|NODES
init|=
literal|"nodes"
decl_stmt|;
DECL|field|CONFIGMAPS
name|String
name|CONFIGMAPS
init|=
literal|"configMaps"
decl_stmt|;
DECL|field|BUILDS
name|String
name|BUILDS
init|=
literal|"builds"
decl_stmt|;
DECL|field|BUILD_CONFIGS
name|String
name|BUILD_CONFIGS
init|=
literal|"buildConfigs"
decl_stmt|;
block|}
end_interface

end_unit

