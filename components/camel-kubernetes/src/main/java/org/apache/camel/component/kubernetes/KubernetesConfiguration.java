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

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|DefaultKubernetesClient
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

begin_class
annotation|@
name|UriParams
DECL|class|KubernetesConfiguration
specifier|public
class|class
name|KubernetesConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|masterUrl
specifier|private
name|String
name|masterUrl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"namespaces,services,replicationControllers,pods,persistentVolumes,persistentVolumesClaims,secrets,resourcesQuota,serviceAccounts,nodes,configMaps,builds,buildConfigs"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|category
specifier|private
name|String
name|category
decl_stmt|;
annotation|@
name|UriParam
DECL|field|kubernetesClient
specifier|private
name|DefaultKubernetesClient
name|kubernetesClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"listNamespaces,listNamespacesByLabels,getNamespace,createNamespace,deleteNamespace,listServices,listServicesByLabels,getService,createService,"
operator|+
literal|"deleteService,listReplicationControllers,listReplicationControllersByLabels,getReplicationController,createReplicationController,deleteReplicationController,scaleReplicationController,"
operator|+
literal|"listPods,listPodsByLabels,getPod,createPod,deletePod,listPersistentVolumes,listPersistentVolumesByLabels,getPersistentVolume,listPersistentVolumesClaims,listPersistentVolumesClaimsByLabels,"
operator|+
literal|"getPersistentVolumeClaim,createPersistentVolumeClaim,deletePersistentVolumeClaim,listSecrets,listSecretsByLabels,getSecret,createSecret,deleteSecret,listResourcesQuota,"
operator|+
literal|"listResourcesQuotaByLabels,getResourceQuota,createResourceQuota,deleteResourceQuota,listServiceAccounts,listServiceAccountsByLabels,getServiceAccount,createServiceAccount,"
operator|+
literal|"deleteServiceAccount,listNodes,listNodesByLabels,getNode,listConfigMaps,listConfigMapsByLabels,getConfigMap,createConfigMap,deleteConfigMap,listBuilds,listBuildsByLabels,"
operator|+
literal|"getBuild,listBuildConfigs,listBuildConfigsByLabels,getBuildConfig"
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
annotation|@
name|UriParam
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|caCertData
specifier|private
name|String
name|caCertData
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|caCertFile
specifier|private
name|String
name|caCertFile
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|clientCertData
specifier|private
name|String
name|clientCertData
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|clientCertFile
specifier|private
name|String
name|clientCertFile
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|clientKeyAlgo
specifier|private
name|String
name|clientKeyAlgo
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|clientKeyData
specifier|private
name|String
name|clientKeyData
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|clientKeyFile
specifier|private
name|String
name|clientKeyFile
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|clientKeyPassphrase
specifier|private
name|String
name|clientKeyPassphrase
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|oauthToken
specifier|private
name|String
name|oauthToken
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|trustCerts
specifier|private
name|Boolean
name|trustCerts
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
annotation|@
name|UriParam
DECL|field|portName
specifier|private
name|String
name|portName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|dnsDomain
specifier|private
name|String
name|dnsDomain
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|poolSize
specifier|private
name|int
name|poolSize
init|=
literal|1
decl_stmt|;
comment|/**      * Kubernetes Master url      */
DECL|method|getMasterUrl ()
specifier|public
name|String
name|getMasterUrl
parameter_list|()
block|{
return|return
name|masterUrl
return|;
block|}
DECL|method|setMasterUrl (String masterUrl)
specifier|public
name|void
name|setMasterUrl
parameter_list|(
name|String
name|masterUrl
parameter_list|)
block|{
name|this
operator|.
name|masterUrl
operator|=
name|masterUrl
expr_stmt|;
block|}
comment|/**      * Kubernetes Producer and Consumer category      */
DECL|method|getCategory ()
specifier|public
name|String
name|getCategory
parameter_list|()
block|{
return|return
name|category
return|;
block|}
DECL|method|setCategory (String category)
specifier|public
name|void
name|setCategory
parameter_list|(
name|String
name|category
parameter_list|)
block|{
name|this
operator|.
name|category
operator|=
name|category
expr_stmt|;
block|}
comment|/**      * Default KubernetesClient to use if provided      */
DECL|method|getKubernetesClient ()
specifier|public
name|DefaultKubernetesClient
name|getKubernetesClient
parameter_list|()
block|{
return|return
name|kubernetesClient
return|;
block|}
DECL|method|setKubernetesClient (DefaultKubernetesClient kubernetesClient)
specifier|public
name|void
name|setKubernetesClient
parameter_list|(
name|DefaultKubernetesClient
name|kubernetesClient
parameter_list|)
block|{
name|this
operator|.
name|kubernetesClient
operator|=
name|kubernetesClient
expr_stmt|;
block|}
comment|/**      * Username to connect to Kubernetes      */
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
comment|/**      * Password to connect to Kubernetes      */
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
comment|/**      * Producer operation to do on Kubernetes      */
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
comment|/**      * The Kubernetes API Version to use      */
DECL|method|getApiVersion ()
specifier|public
name|String
name|getApiVersion
parameter_list|()
block|{
return|return
name|apiVersion
return|;
block|}
DECL|method|setApiVersion (String apiVersion)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
block|}
comment|/**      * The CA Cert Data      */
DECL|method|getCaCertData ()
specifier|public
name|String
name|getCaCertData
parameter_list|()
block|{
return|return
name|caCertData
return|;
block|}
DECL|method|setCaCertData (String caCertData)
specifier|public
name|void
name|setCaCertData
parameter_list|(
name|String
name|caCertData
parameter_list|)
block|{
name|this
operator|.
name|caCertData
operator|=
name|caCertData
expr_stmt|;
block|}
comment|/**      * The CA Cert File      */
DECL|method|getCaCertFile ()
specifier|public
name|String
name|getCaCertFile
parameter_list|()
block|{
return|return
name|caCertFile
return|;
block|}
DECL|method|setCaCertFile (String caCertFile)
specifier|public
name|void
name|setCaCertFile
parameter_list|(
name|String
name|caCertFile
parameter_list|)
block|{
name|this
operator|.
name|caCertFile
operator|=
name|caCertFile
expr_stmt|;
block|}
comment|/**      * The Client Cert Data      */
DECL|method|getClientCertData ()
specifier|public
name|String
name|getClientCertData
parameter_list|()
block|{
return|return
name|clientCertData
return|;
block|}
DECL|method|setClientCertData (String clientCertData)
specifier|public
name|void
name|setClientCertData
parameter_list|(
name|String
name|clientCertData
parameter_list|)
block|{
name|this
operator|.
name|clientCertData
operator|=
name|clientCertData
expr_stmt|;
block|}
comment|/**      * The Client Cert File      */
DECL|method|getClientCertFile ()
specifier|public
name|String
name|getClientCertFile
parameter_list|()
block|{
return|return
name|clientCertFile
return|;
block|}
DECL|method|setClientCertFile (String clientCertFile)
specifier|public
name|void
name|setClientCertFile
parameter_list|(
name|String
name|clientCertFile
parameter_list|)
block|{
name|this
operator|.
name|clientCertFile
operator|=
name|clientCertFile
expr_stmt|;
block|}
comment|/**      * The Key Algorithm used by the client      */
DECL|method|getClientKeyAlgo ()
specifier|public
name|String
name|getClientKeyAlgo
parameter_list|()
block|{
return|return
name|clientKeyAlgo
return|;
block|}
DECL|method|setClientKeyAlgo (String clientKeyAlgo)
specifier|public
name|void
name|setClientKeyAlgo
parameter_list|(
name|String
name|clientKeyAlgo
parameter_list|)
block|{
name|this
operator|.
name|clientKeyAlgo
operator|=
name|clientKeyAlgo
expr_stmt|;
block|}
comment|/**      * The Client Key data      */
DECL|method|getClientKeyData ()
specifier|public
name|String
name|getClientKeyData
parameter_list|()
block|{
return|return
name|clientKeyData
return|;
block|}
DECL|method|setClientKeyData (String clientKeyData)
specifier|public
name|void
name|setClientKeyData
parameter_list|(
name|String
name|clientKeyData
parameter_list|)
block|{
name|this
operator|.
name|clientKeyData
operator|=
name|clientKeyData
expr_stmt|;
block|}
comment|/**      * The Client Key file      */
DECL|method|getClientKeyFile ()
specifier|public
name|String
name|getClientKeyFile
parameter_list|()
block|{
return|return
name|clientKeyFile
return|;
block|}
DECL|method|setClientKeyFile (String clientKeyFile)
specifier|public
name|void
name|setClientKeyFile
parameter_list|(
name|String
name|clientKeyFile
parameter_list|)
block|{
name|this
operator|.
name|clientKeyFile
operator|=
name|clientKeyFile
expr_stmt|;
block|}
comment|/**      * The Client Key Passphrase      */
DECL|method|getClientKeyPassphrase ()
specifier|public
name|String
name|getClientKeyPassphrase
parameter_list|()
block|{
return|return
name|clientKeyPassphrase
return|;
block|}
DECL|method|setClientKeyPassphrase (String clientKeyPassphrase)
specifier|public
name|void
name|setClientKeyPassphrase
parameter_list|(
name|String
name|clientKeyPassphrase
parameter_list|)
block|{
name|this
operator|.
name|clientKeyPassphrase
operator|=
name|clientKeyPassphrase
expr_stmt|;
block|}
comment|/**      * The Auth Token      */
DECL|method|getOauthToken ()
specifier|public
name|String
name|getOauthToken
parameter_list|()
block|{
return|return
name|oauthToken
return|;
block|}
DECL|method|setOauthToken (String oauthToken)
specifier|public
name|void
name|setOauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|this
operator|.
name|oauthToken
operator|=
name|oauthToken
expr_stmt|;
block|}
comment|/**      * Define if the certs we used are trusted anyway or not      */
DECL|method|getTrustCerts ()
specifier|public
name|Boolean
name|getTrustCerts
parameter_list|()
block|{
return|return
name|trustCerts
return|;
block|}
DECL|method|setTrustCerts (Boolean trustCerts)
specifier|public
name|void
name|setTrustCerts
parameter_list|(
name|Boolean
name|trustCerts
parameter_list|)
block|{
name|this
operator|.
name|trustCerts
operator|=
name|trustCerts
expr_stmt|;
block|}
comment|/**      * The namespace      */
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
DECL|method|getPortName ()
specifier|public
name|String
name|getPortName
parameter_list|()
block|{
return|return
name|portName
return|;
block|}
comment|/**      * The port name, used for ServiceCall EIP      */
DECL|method|setPortName (String portName)
specifier|public
name|void
name|setPortName
parameter_list|(
name|String
name|portName
parameter_list|)
block|{
name|this
operator|.
name|portName
operator|=
name|portName
expr_stmt|;
block|}
DECL|method|getDnsDomain ()
specifier|public
name|String
name|getDnsDomain
parameter_list|()
block|{
return|return
name|dnsDomain
return|;
block|}
comment|/**      * The dns domain, used for ServiceCall EIP      */
DECL|method|setDnsDomain (String dnsDomain)
specifier|public
name|void
name|setDnsDomain
parameter_list|(
name|String
name|dnsDomain
parameter_list|)
block|{
name|this
operator|.
name|dnsDomain
operator|=
name|dnsDomain
expr_stmt|;
block|}
comment|/**      * @deprecated use {@link #getNamespace()}      */
annotation|@
name|Deprecated
DECL|method|getNamespaceName ()
specifier|public
name|String
name|getNamespaceName
parameter_list|()
block|{
return|return
name|getNamespace
argument_list|()
return|;
block|}
comment|/**      * @deprecated use {@link #setNamespace(String)}      */
annotation|@
name|Deprecated
DECL|method|setNamespaceName (String namespace)
specifier|public
name|void
name|setNamespaceName
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
comment|/**      * The Consumer pool size      */
DECL|method|getPoolSize ()
specifier|public
name|int
name|getPoolSize
parameter_list|()
block|{
return|return
name|poolSize
return|;
block|}
DECL|method|setPoolSize (int poolSize)
specifier|public
name|void
name|setPoolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|this
operator|.
name|poolSize
operator|=
name|poolSize
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"KubernetesConfiguration [masterUrl="
operator|+
name|masterUrl
operator|+
literal|", category="
operator|+
name|category
operator|+
literal|", kubernetesClient="
operator|+
name|kubernetesClient
operator|+
literal|", username="
operator|+
name|username
operator|+
literal|", password="
operator|+
name|password
operator|+
literal|", operation="
operator|+
name|operation
operator|+
literal|", apiVersion="
operator|+
name|apiVersion
operator|+
literal|", caCertData="
operator|+
name|caCertData
operator|+
literal|", caCertFile="
operator|+
name|caCertFile
operator|+
literal|", clientCertData="
operator|+
name|clientCertData
operator|+
literal|", clientCertFile="
operator|+
name|clientCertFile
operator|+
literal|", clientKeyAlgo="
operator|+
name|clientKeyAlgo
operator|+
literal|", clientKeyData="
operator|+
name|clientKeyData
operator|+
literal|", clientKeyFile="
operator|+
name|clientKeyFile
operator|+
literal|", clientKeyPassphrase="
operator|+
name|clientKeyPassphrase
operator|+
literal|", oauthToken="
operator|+
name|oauthToken
operator|+
literal|", trustCerts="
operator|+
name|trustCerts
operator|+
literal|", namespaceName="
operator|+
name|namespace
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

