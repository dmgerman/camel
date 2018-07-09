begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
DECL|interface|KubernetesConstants
specifier|public
interface|interface
name|KubernetesConstants
block|{
comment|// Producer
DECL|field|KUBERNETES_OPERATION
name|String
name|KUBERNETES_OPERATION
init|=
literal|"CamelKubernetesOperation"
decl_stmt|;
DECL|field|KUBERNETES_NAMESPACE_NAME
name|String
name|KUBERNETES_NAMESPACE_NAME
init|=
literal|"CamelKubernetesNamespaceName"
decl_stmt|;
DECL|field|KUBERNETES_NAMESPACE_LABELS
name|String
name|KUBERNETES_NAMESPACE_LABELS
init|=
literal|"CamelKubernetesNamespaceLabels"
decl_stmt|;
DECL|field|KUBERNETES_SERVICE_LABELS
name|String
name|KUBERNETES_SERVICE_LABELS
init|=
literal|"CamelKubernetesServiceLabels"
decl_stmt|;
DECL|field|KUBERNETES_SERVICE_NAME
name|String
name|KUBERNETES_SERVICE_NAME
init|=
literal|"CamelKubernetesServiceName"
decl_stmt|;
DECL|field|KUBERNETES_SERVICE_SPEC
name|String
name|KUBERNETES_SERVICE_SPEC
init|=
literal|"CamelKubernetesServiceSpec"
decl_stmt|;
DECL|field|KUBERNETES_REPLICATION_CONTROLLERS_LABELS
name|String
name|KUBERNETES_REPLICATION_CONTROLLERS_LABELS
init|=
literal|"CamelKubernetesReplicationControllersLabels"
decl_stmt|;
DECL|field|KUBERNETES_REPLICATION_CONTROLLER_NAME
name|String
name|KUBERNETES_REPLICATION_CONTROLLER_NAME
init|=
literal|"CamelKubernetesReplicationControllerName"
decl_stmt|;
DECL|field|KUBERNETES_REPLICATION_CONTROLLER_SPEC
name|String
name|KUBERNETES_REPLICATION_CONTROLLER_SPEC
init|=
literal|"CamelKubernetesReplicationControllerSpec"
decl_stmt|;
DECL|field|KUBERNETES_REPLICATION_CONTROLLER_REPLICAS
name|String
name|KUBERNETES_REPLICATION_CONTROLLER_REPLICAS
init|=
literal|"CamelKubernetesReplicationControllerReplicas"
decl_stmt|;
DECL|field|KUBERNETES_PODS_LABELS
name|String
name|KUBERNETES_PODS_LABELS
init|=
literal|"CamelKubernetesPodsLabels"
decl_stmt|;
DECL|field|KUBERNETES_POD_NAME
name|String
name|KUBERNETES_POD_NAME
init|=
literal|"CamelKubernetesPodName"
decl_stmt|;
DECL|field|KUBERNETES_POD_SPEC
name|String
name|KUBERNETES_POD_SPEC
init|=
literal|"CamelKubernetesPodSpec"
decl_stmt|;
DECL|field|KUBERNETES_PERSISTENT_VOLUMES_LABELS
name|String
name|KUBERNETES_PERSISTENT_VOLUMES_LABELS
init|=
literal|"CamelKubernetesPersistentVolumesLabels"
decl_stmt|;
DECL|field|KUBERNETES_PERSISTENT_VOLUME_NAME
name|String
name|KUBERNETES_PERSISTENT_VOLUME_NAME
init|=
literal|"CamelKubernetesPersistentVolumeName"
decl_stmt|;
DECL|field|KUBERNETES_PERSISTENT_VOLUMES_CLAIMS_LABELS
name|String
name|KUBERNETES_PERSISTENT_VOLUMES_CLAIMS_LABELS
init|=
literal|"CamelKubernetesPersistentVolumesClaimsLabels"
decl_stmt|;
DECL|field|KUBERNETES_PERSISTENT_VOLUME_CLAIM_NAME
name|String
name|KUBERNETES_PERSISTENT_VOLUME_CLAIM_NAME
init|=
literal|"CamelKubernetesPersistentVolumeClaimName"
decl_stmt|;
DECL|field|KUBERNETES_PERSISTENT_VOLUME_CLAIM_SPEC
name|String
name|KUBERNETES_PERSISTENT_VOLUME_CLAIM_SPEC
init|=
literal|"CamelKubernetesPersistentVolumeClaimSpec"
decl_stmt|;
DECL|field|KUBERNETES_SECRETS_LABELS
name|String
name|KUBERNETES_SECRETS_LABELS
init|=
literal|"CamelKubernetesSecretsLabels"
decl_stmt|;
DECL|field|KUBERNETES_SECRET_NAME
name|String
name|KUBERNETES_SECRET_NAME
init|=
literal|"CamelKubernetesSecretName"
decl_stmt|;
DECL|field|KUBERNETES_SECRET
name|String
name|KUBERNETES_SECRET
init|=
literal|"CamelKubernetesSecret"
decl_stmt|;
DECL|field|KUBERNETES_RESOURCES_QUOTA_LABELS
name|String
name|KUBERNETES_RESOURCES_QUOTA_LABELS
init|=
literal|"CamelKubernetesResourcesQuotaLabels"
decl_stmt|;
DECL|field|KUBERNETES_RESOURCES_QUOTA_NAME
name|String
name|KUBERNETES_RESOURCES_QUOTA_NAME
init|=
literal|"CamelKubernetesResourcesQuotaName"
decl_stmt|;
DECL|field|KUBERNETES_RESOURCE_QUOTA_SPEC
name|String
name|KUBERNETES_RESOURCE_QUOTA_SPEC
init|=
literal|"CamelKubernetesResourceQuotaSpec"
decl_stmt|;
DECL|field|KUBERNETES_SERVICE_ACCOUNTS_LABELS
name|String
name|KUBERNETES_SERVICE_ACCOUNTS_LABELS
init|=
literal|"CamelKubernetesServiceAccountsLabels"
decl_stmt|;
DECL|field|KUBERNETES_SERVICE_ACCOUNT_NAME
name|String
name|KUBERNETES_SERVICE_ACCOUNT_NAME
init|=
literal|"CamelKubernetesServiceAccountName"
decl_stmt|;
DECL|field|KUBERNETES_SERVICE_ACCOUNT
name|String
name|KUBERNETES_SERVICE_ACCOUNT
init|=
literal|"CamelKubernetesServiceAccount"
decl_stmt|;
DECL|field|KUBERNETES_NODES_LABELS
name|String
name|KUBERNETES_NODES_LABELS
init|=
literal|"CamelKubernetesNodesLabels"
decl_stmt|;
DECL|field|KUBERNETES_NODE_NAME
name|String
name|KUBERNETES_NODE_NAME
init|=
literal|"CamelKubernetesNodeName"
decl_stmt|;
DECL|field|KUBERNETES_DEPLOYMENTS_LABELS
name|String
name|KUBERNETES_DEPLOYMENTS_LABELS
init|=
literal|"CamelKubernetesDeploymentsLabels"
decl_stmt|;
DECL|field|KUBERNETES_DEPLOYMENT_NAME
name|String
name|KUBERNETES_DEPLOYMENT_NAME
init|=
literal|"CamelKubernetesDeploymentName"
decl_stmt|;
DECL|field|KUBERNETES_DEPLOYMENT_SPEC
name|String
name|KUBERNETES_DEPLOYMENT_SPEC
init|=
literal|"CamelKubernetesDeploymentSpec"
decl_stmt|;
DECL|field|KUBERNETES_CONFIGMAPS_LABELS
name|String
name|KUBERNETES_CONFIGMAPS_LABELS
init|=
literal|"CamelKubernetesConfigMapsLabels"
decl_stmt|;
DECL|field|KUBERNETES_CONFIGMAP_NAME
name|String
name|KUBERNETES_CONFIGMAP_NAME
init|=
literal|"CamelKubernetesConfigMapName"
decl_stmt|;
DECL|field|KUBERNETES_CONFIGMAP_DATA
name|String
name|KUBERNETES_CONFIGMAP_DATA
init|=
literal|"CamelKubernetesConfigData"
decl_stmt|;
DECL|field|KUBERNETES_BUILDS_LABELS
name|String
name|KUBERNETES_BUILDS_LABELS
init|=
literal|"CamelKubernetesBuildsLabels"
decl_stmt|;
DECL|field|KUBERNETES_BUILD_NAME
name|String
name|KUBERNETES_BUILD_NAME
init|=
literal|"CamelKubernetesBuildName"
decl_stmt|;
DECL|field|KUBERNETES_BUILD_CONFIGS_LABELS
name|String
name|KUBERNETES_BUILD_CONFIGS_LABELS
init|=
literal|"CamelKubernetesBuildConfigsLabels"
decl_stmt|;
DECL|field|KUBERNETES_BUILD_CONFIG_NAME
name|String
name|KUBERNETES_BUILD_CONFIG_NAME
init|=
literal|"CamelKubernetesBuildConfigName"
decl_stmt|;
DECL|field|KUBERNETES_DEPLOYMENT_REPLICAS
name|String
name|KUBERNETES_DEPLOYMENT_REPLICAS
init|=
literal|"CamelKubernetesDeploymentReplicas"
decl_stmt|;
DECL|field|KUBERNETES_HPA_NAME
name|String
name|KUBERNETES_HPA_NAME
init|=
literal|"CamelKubernetesHPAName"
decl_stmt|;
DECL|field|KUBERNETES_HPA_SPEC
name|String
name|KUBERNETES_HPA_SPEC
init|=
literal|"CamelKubernetesHPASpec"
decl_stmt|;
comment|// Consumer
DECL|field|KUBERNETES_EVENT_ACTION
name|String
name|KUBERNETES_EVENT_ACTION
init|=
literal|"CamelKubernetesEventAction"
decl_stmt|;
DECL|field|KUBERNETES_EVENT_TIMESTAMP
name|String
name|KUBERNETES_EVENT_TIMESTAMP
init|=
literal|"CamelKubernetesEventTimestamp"
decl_stmt|;
block|}
end_interface

end_unit

