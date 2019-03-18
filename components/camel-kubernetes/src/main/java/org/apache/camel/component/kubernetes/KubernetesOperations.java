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
DECL|interface|KubernetesOperations
specifier|public
interface|interface
name|KubernetesOperations
block|{
comment|// Namespaces
DECL|field|LIST_NAMESPACE_OPERATION
name|String
name|LIST_NAMESPACE_OPERATION
init|=
literal|"listNamespaces"
decl_stmt|;
DECL|field|LIST_NAMESPACE_BY_LABELS_OPERATION
name|String
name|LIST_NAMESPACE_BY_LABELS_OPERATION
init|=
literal|"listNamespacesByLabels"
decl_stmt|;
DECL|field|GET_NAMESPACE_OPERATION
name|String
name|GET_NAMESPACE_OPERATION
init|=
literal|"getNamespace"
decl_stmt|;
DECL|field|CREATE_NAMESPACE_OPERATION
name|String
name|CREATE_NAMESPACE_OPERATION
init|=
literal|"createNamespace"
decl_stmt|;
DECL|field|DELETE_NAMESPACE_OPERATION
name|String
name|DELETE_NAMESPACE_OPERATION
init|=
literal|"deleteNamespace"
decl_stmt|;
comment|// Services
DECL|field|LIST_SERVICES_OPERATION
name|String
name|LIST_SERVICES_OPERATION
init|=
literal|"listServices"
decl_stmt|;
DECL|field|LIST_SERVICES_BY_LABELS_OPERATION
name|String
name|LIST_SERVICES_BY_LABELS_OPERATION
init|=
literal|"listServicesByLabels"
decl_stmt|;
DECL|field|GET_SERVICE_OPERATION
name|String
name|GET_SERVICE_OPERATION
init|=
literal|"getService"
decl_stmt|;
DECL|field|CREATE_SERVICE_OPERATION
name|String
name|CREATE_SERVICE_OPERATION
init|=
literal|"createService"
decl_stmt|;
DECL|field|DELETE_SERVICE_OPERATION
name|String
name|DELETE_SERVICE_OPERATION
init|=
literal|"deleteService"
decl_stmt|;
comment|// Replication Controllers
DECL|field|LIST_REPLICATION_CONTROLLERS_OPERATION
name|String
name|LIST_REPLICATION_CONTROLLERS_OPERATION
init|=
literal|"listReplicationControllers"
decl_stmt|;
DECL|field|LIST_REPLICATION_CONTROLLERS_BY_LABELS_OPERATION
name|String
name|LIST_REPLICATION_CONTROLLERS_BY_LABELS_OPERATION
init|=
literal|"listReplicationControllersByLabels"
decl_stmt|;
DECL|field|GET_REPLICATION_CONTROLLER_OPERATION
name|String
name|GET_REPLICATION_CONTROLLER_OPERATION
init|=
literal|"getReplicationController"
decl_stmt|;
DECL|field|CREATE_REPLICATION_CONTROLLER_OPERATION
name|String
name|CREATE_REPLICATION_CONTROLLER_OPERATION
init|=
literal|"createReplicationController"
decl_stmt|;
DECL|field|DELETE_REPLICATION_CONTROLLER_OPERATION
name|String
name|DELETE_REPLICATION_CONTROLLER_OPERATION
init|=
literal|"deleteReplicationController"
decl_stmt|;
DECL|field|SCALE_REPLICATION_CONTROLLER_OPERATION
name|String
name|SCALE_REPLICATION_CONTROLLER_OPERATION
init|=
literal|"scaleReplicationController"
decl_stmt|;
comment|// Pods
DECL|field|LIST_PODS_OPERATION
name|String
name|LIST_PODS_OPERATION
init|=
literal|"listPods"
decl_stmt|;
DECL|field|LIST_PODS_BY_LABELS_OPERATION
name|String
name|LIST_PODS_BY_LABELS_OPERATION
init|=
literal|"listPodsByLabels"
decl_stmt|;
DECL|field|GET_POD_OPERATION
name|String
name|GET_POD_OPERATION
init|=
literal|"getPod"
decl_stmt|;
DECL|field|CREATE_POD_OPERATION
name|String
name|CREATE_POD_OPERATION
init|=
literal|"createPod"
decl_stmt|;
DECL|field|DELETE_POD_OPERATION
name|String
name|DELETE_POD_OPERATION
init|=
literal|"deletePod"
decl_stmt|;
comment|// Persistent Volumes
DECL|field|LIST_PERSISTENT_VOLUMES
name|String
name|LIST_PERSISTENT_VOLUMES
init|=
literal|"listPersistentVolumes"
decl_stmt|;
DECL|field|LIST_PERSISTENT_VOLUMES_BY_LABELS_OPERATION
name|String
name|LIST_PERSISTENT_VOLUMES_BY_LABELS_OPERATION
init|=
literal|"listPersistentVolumesByLabels"
decl_stmt|;
DECL|field|GET_PERSISTENT_VOLUME_OPERATION
name|String
name|GET_PERSISTENT_VOLUME_OPERATION
init|=
literal|"getPersistentVolume"
decl_stmt|;
comment|// Persistent Volumes Claims
DECL|field|LIST_PERSISTENT_VOLUMES_CLAIMS
name|String
name|LIST_PERSISTENT_VOLUMES_CLAIMS
init|=
literal|"listPersistentVolumesClaims"
decl_stmt|;
DECL|field|LIST_PERSISTENT_VOLUMES_CLAIMS_BY_LABELS_OPERATION
name|String
name|LIST_PERSISTENT_VOLUMES_CLAIMS_BY_LABELS_OPERATION
init|=
literal|"listPersistentVolumesClaimsByLabels"
decl_stmt|;
DECL|field|GET_PERSISTENT_VOLUME_CLAIM_OPERATION
name|String
name|GET_PERSISTENT_VOLUME_CLAIM_OPERATION
init|=
literal|"getPersistentVolumeClaim"
decl_stmt|;
DECL|field|CREATE_PERSISTENT_VOLUME_CLAIM_OPERATION
name|String
name|CREATE_PERSISTENT_VOLUME_CLAIM_OPERATION
init|=
literal|"createPersistentVolumeClaim"
decl_stmt|;
DECL|field|DELETE_PERSISTENT_VOLUME_CLAIM_OPERATION
name|String
name|DELETE_PERSISTENT_VOLUME_CLAIM_OPERATION
init|=
literal|"deletePersistentVolumeClaim"
decl_stmt|;
comment|// Secrets
DECL|field|LIST_SECRETS
name|String
name|LIST_SECRETS
init|=
literal|"listSecrets"
decl_stmt|;
DECL|field|LIST_SECRETS_BY_LABELS_OPERATION
name|String
name|LIST_SECRETS_BY_LABELS_OPERATION
init|=
literal|"listSecretsByLabels"
decl_stmt|;
DECL|field|GET_SECRET_OPERATION
name|String
name|GET_SECRET_OPERATION
init|=
literal|"getSecret"
decl_stmt|;
DECL|field|CREATE_SECRET_OPERATION
name|String
name|CREATE_SECRET_OPERATION
init|=
literal|"createSecret"
decl_stmt|;
DECL|field|DELETE_SECRET_OPERATION
name|String
name|DELETE_SECRET_OPERATION
init|=
literal|"deleteSecret"
decl_stmt|;
comment|// Resources quota
DECL|field|LIST_RESOURCES_QUOTA
name|String
name|LIST_RESOURCES_QUOTA
init|=
literal|"listResourcesQuota"
decl_stmt|;
DECL|field|LIST_RESOURCES_QUOTA_BY_LABELS_OPERATION
name|String
name|LIST_RESOURCES_QUOTA_BY_LABELS_OPERATION
init|=
literal|"listResourcesQuotaByLabels"
decl_stmt|;
DECL|field|GET_RESOURCE_QUOTA_OPERATION
name|String
name|GET_RESOURCE_QUOTA_OPERATION
init|=
literal|"getResourceQuota"
decl_stmt|;
DECL|field|CREATE_RESOURCE_QUOTA_OPERATION
name|String
name|CREATE_RESOURCE_QUOTA_OPERATION
init|=
literal|"createResourceQuota"
decl_stmt|;
DECL|field|DELETE_RESOURCE_QUOTA_OPERATION
name|String
name|DELETE_RESOURCE_QUOTA_OPERATION
init|=
literal|"deleteResourceQuota"
decl_stmt|;
comment|// Service Accounts
DECL|field|LIST_SERVICE_ACCOUNTS
name|String
name|LIST_SERVICE_ACCOUNTS
init|=
literal|"listServiceAccounts"
decl_stmt|;
DECL|field|LIST_SERVICE_ACCOUNTS_BY_LABELS_OPERATION
name|String
name|LIST_SERVICE_ACCOUNTS_BY_LABELS_OPERATION
init|=
literal|"listServiceAccountsByLabels"
decl_stmt|;
DECL|field|GET_SERVICE_ACCOUNT_OPERATION
name|String
name|GET_SERVICE_ACCOUNT_OPERATION
init|=
literal|"getServiceAccount"
decl_stmt|;
DECL|field|CREATE_SERVICE_ACCOUNT_OPERATION
name|String
name|CREATE_SERVICE_ACCOUNT_OPERATION
init|=
literal|"createServiceAccount"
decl_stmt|;
DECL|field|DELETE_SERVICE_ACCOUNT_OPERATION
name|String
name|DELETE_SERVICE_ACCOUNT_OPERATION
init|=
literal|"deleteServiceAccount"
decl_stmt|;
comment|// Nodes
DECL|field|LIST_NODES
name|String
name|LIST_NODES
init|=
literal|"listNodes"
decl_stmt|;
DECL|field|LIST_NODES_BY_LABELS_OPERATION
name|String
name|LIST_NODES_BY_LABELS_OPERATION
init|=
literal|"listNodesByLabels"
decl_stmt|;
DECL|field|GET_NODE_OPERATION
name|String
name|GET_NODE_OPERATION
init|=
literal|"getNode"
decl_stmt|;
comment|// HPA
DECL|field|LIST_HPA
name|String
name|LIST_HPA
init|=
literal|"listHPA"
decl_stmt|;
DECL|field|LIST_HPA_BY_LABELS_OPERATION
name|String
name|LIST_HPA_BY_LABELS_OPERATION
init|=
literal|"listHPAByLabels"
decl_stmt|;
DECL|field|GET_HPA_OPERATION
name|String
name|GET_HPA_OPERATION
init|=
literal|"getHPA"
decl_stmt|;
DECL|field|CREATE_HPA_OPERATION
name|String
name|CREATE_HPA_OPERATION
init|=
literal|"createHPA"
decl_stmt|;
DECL|field|DELETE_HPA_OPERATION
name|String
name|DELETE_HPA_OPERATION
init|=
literal|"deleteHPA"
decl_stmt|;
comment|// Deployments
DECL|field|LIST_DEPLOYMENTS
name|String
name|LIST_DEPLOYMENTS
init|=
literal|"listDeployments"
decl_stmt|;
DECL|field|LIST_DEPLOYMENTS_BY_LABELS_OPERATION
name|String
name|LIST_DEPLOYMENTS_BY_LABELS_OPERATION
init|=
literal|"listDeploymentsByLabels"
decl_stmt|;
DECL|field|GET_DEPLOYMENT
name|String
name|GET_DEPLOYMENT
init|=
literal|"getDeployment"
decl_stmt|;
DECL|field|DELETE_DEPLOYMENT
name|String
name|DELETE_DEPLOYMENT
init|=
literal|"deleteDeployment"
decl_stmt|;
DECL|field|CREATE_DEPLOYMENT
name|String
name|CREATE_DEPLOYMENT
init|=
literal|"createDeployment"
decl_stmt|;
DECL|field|SCALE_DEPLOYMENT
name|String
name|SCALE_DEPLOYMENT
init|=
literal|"scaleDeployment"
decl_stmt|;
comment|// Config Maps
DECL|field|LIST_CONFIGMAPS
name|String
name|LIST_CONFIGMAPS
init|=
literal|"listConfigMaps"
decl_stmt|;
DECL|field|LIST_CONFIGMAPS_BY_LABELS_OPERATION
name|String
name|LIST_CONFIGMAPS_BY_LABELS_OPERATION
init|=
literal|"listConfigMapsByLabels"
decl_stmt|;
DECL|field|GET_CONFIGMAP_OPERATION
name|String
name|GET_CONFIGMAP_OPERATION
init|=
literal|"getConfigMap"
decl_stmt|;
DECL|field|CREATE_CONFIGMAP_OPERATION
name|String
name|CREATE_CONFIGMAP_OPERATION
init|=
literal|"createConfigMap"
decl_stmt|;
DECL|field|DELETE_CONFIGMAP_OPERATION
name|String
name|DELETE_CONFIGMAP_OPERATION
init|=
literal|"deleteConfigMap"
decl_stmt|;
comment|// Builds
DECL|field|LIST_BUILD
name|String
name|LIST_BUILD
init|=
literal|"listBuilds"
decl_stmt|;
DECL|field|LIST_BUILD_BY_LABELS_OPERATION
name|String
name|LIST_BUILD_BY_LABELS_OPERATION
init|=
literal|"listBuildsByLabels"
decl_stmt|;
DECL|field|GET_BUILD_OPERATION
name|String
name|GET_BUILD_OPERATION
init|=
literal|"getBuild"
decl_stmt|;
comment|// Build Configs
DECL|field|LIST_BUILD_CONFIGS
name|String
name|LIST_BUILD_CONFIGS
init|=
literal|"listBuildConfigs"
decl_stmt|;
DECL|field|LIST_BUILD_CONFIGS_BY_LABELS_OPERATION
name|String
name|LIST_BUILD_CONFIGS_BY_LABELS_OPERATION
init|=
literal|"listBuildConfigsByLabels"
decl_stmt|;
DECL|field|GET_BUILD_CONFIG_OPERATION
name|String
name|GET_BUILD_CONFIG_OPERATION
init|=
literal|"getBuildConfig"
decl_stmt|;
comment|// Secrets
DECL|field|LIST_JOB
name|String
name|LIST_JOB
init|=
literal|"listJob"
decl_stmt|;
DECL|field|LIST_JOB_BY_LABELS_OPERATION
name|String
name|LIST_JOB_BY_LABELS_OPERATION
init|=
literal|"listJobByLabels"
decl_stmt|;
DECL|field|GET_JOB_OPERATION
name|String
name|GET_JOB_OPERATION
init|=
literal|"getJob"
decl_stmt|;
DECL|field|CREATE_JOB_OPERATION
name|String
name|CREATE_JOB_OPERATION
init|=
literal|"createJob"
decl_stmt|;
DECL|field|DELETE_JOB_OPERATION
name|String
name|DELETE_JOB_OPERATION
init|=
literal|"deleteJob"
decl_stmt|;
block|}
end_interface

end_unit

