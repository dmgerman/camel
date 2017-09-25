begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.springboot.ha
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
operator|.
name|springboot
operator|.
name|ha
package|;
end_package

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
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.kubernetes.cluster.service"
argument_list|)
DECL|class|KubernetesClusterServiceConfiguration
specifier|public
class|class
name|KubernetesClusterServiceConfiguration
block|{
comment|/**      * Sets if the Kubernetes cluster service should be enabled or not, default is false.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
comment|/**      * Cluster Service ID      */
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * Set the URL of the Kubernetes master (read from Kubernetes client properties by default).      */
DECL|field|masterUrl
specifier|private
name|String
name|masterUrl
decl_stmt|;
comment|/**      * Connection timeout in milliseconds to use when making requests to the Kubernetes API server.      */
DECL|field|connectionTimeoutMillis
specifier|private
name|Integer
name|connectionTimeoutMillis
decl_stmt|;
comment|/**      * Set the name of the Kubernetes namespace containing the pods and the configmap (autodetected by default)      */
DECL|field|kubernetesNamespace
specifier|private
name|String
name|kubernetesNamespace
decl_stmt|;
comment|/**      * Set the name of the ConfigMap used to do optimistic locking (defaults to 'leaders').      */
DECL|field|configMapName
specifier|private
name|String
name|configMapName
decl_stmt|;
comment|/**      * Set the name of the current pod (autodetected from container host name by default).      */
DECL|field|podName
specifier|private
name|String
name|podName
decl_stmt|;
comment|/**      * Set the labels used to identify the pods composing the cluster.      */
DECL|field|clusterLabels
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|clusterLabels
decl_stmt|;
comment|/**      * A jitter factor to apply in order to prevent all pods to call Kubernetes APIs in the same instant.      */
DECL|field|jitterFactor
specifier|private
name|Double
name|jitterFactor
decl_stmt|;
comment|/**      * The default duration of the lease for the current leader.      */
DECL|field|leaseDurationMillis
specifier|private
name|Long
name|leaseDurationMillis
decl_stmt|;
comment|/**      * The deadline after which the leader must stop its services because it may have lost the leadership.      */
DECL|field|renewDeadlineMillis
specifier|private
name|Long
name|renewDeadlineMillis
decl_stmt|;
comment|/**      * The time between two subsequent attempts to check and acquire the leadership.      * It is randomized using the jitter factor.      */
DECL|field|retryPeriodMillis
specifier|private
name|Long
name|retryPeriodMillis
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
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
DECL|method|getConnectionTimeoutMillis ()
specifier|public
name|Integer
name|getConnectionTimeoutMillis
parameter_list|()
block|{
return|return
name|connectionTimeoutMillis
return|;
block|}
DECL|method|setConnectionTimeoutMillis (Integer connectionTimeoutMillis)
specifier|public
name|void
name|setConnectionTimeoutMillis
parameter_list|(
name|Integer
name|connectionTimeoutMillis
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeoutMillis
operator|=
name|connectionTimeoutMillis
expr_stmt|;
block|}
DECL|method|getKubernetesNamespace ()
specifier|public
name|String
name|getKubernetesNamespace
parameter_list|()
block|{
return|return
name|kubernetesNamespace
return|;
block|}
DECL|method|setKubernetesNamespace (String kubernetesNamespace)
specifier|public
name|void
name|setKubernetesNamespace
parameter_list|(
name|String
name|kubernetesNamespace
parameter_list|)
block|{
name|this
operator|.
name|kubernetesNamespace
operator|=
name|kubernetesNamespace
expr_stmt|;
block|}
DECL|method|getConfigMapName ()
specifier|public
name|String
name|getConfigMapName
parameter_list|()
block|{
return|return
name|configMapName
return|;
block|}
DECL|method|setConfigMapName (String configMapName)
specifier|public
name|void
name|setConfigMapName
parameter_list|(
name|String
name|configMapName
parameter_list|)
block|{
name|this
operator|.
name|configMapName
operator|=
name|configMapName
expr_stmt|;
block|}
DECL|method|getPodName ()
specifier|public
name|String
name|getPodName
parameter_list|()
block|{
return|return
name|podName
return|;
block|}
DECL|method|setPodName (String podName)
specifier|public
name|void
name|setPodName
parameter_list|(
name|String
name|podName
parameter_list|)
block|{
name|this
operator|.
name|podName
operator|=
name|podName
expr_stmt|;
block|}
DECL|method|getClusterLabels ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getClusterLabels
parameter_list|()
block|{
return|return
name|clusterLabels
return|;
block|}
DECL|method|setClusterLabels (Map<String, String> clusterLabels)
specifier|public
name|void
name|setClusterLabels
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|clusterLabels
parameter_list|)
block|{
name|this
operator|.
name|clusterLabels
operator|=
name|clusterLabels
expr_stmt|;
block|}
DECL|method|getJitterFactor ()
specifier|public
name|Double
name|getJitterFactor
parameter_list|()
block|{
return|return
name|jitterFactor
return|;
block|}
DECL|method|setJitterFactor (Double jitterFactor)
specifier|public
name|void
name|setJitterFactor
parameter_list|(
name|Double
name|jitterFactor
parameter_list|)
block|{
name|this
operator|.
name|jitterFactor
operator|=
name|jitterFactor
expr_stmt|;
block|}
DECL|method|getLeaseDurationMillis ()
specifier|public
name|Long
name|getLeaseDurationMillis
parameter_list|()
block|{
return|return
name|leaseDurationMillis
return|;
block|}
DECL|method|setLeaseDurationMillis (Long leaseDurationMillis)
specifier|public
name|void
name|setLeaseDurationMillis
parameter_list|(
name|Long
name|leaseDurationMillis
parameter_list|)
block|{
name|this
operator|.
name|leaseDurationMillis
operator|=
name|leaseDurationMillis
expr_stmt|;
block|}
DECL|method|getRenewDeadlineMillis ()
specifier|public
name|Long
name|getRenewDeadlineMillis
parameter_list|()
block|{
return|return
name|renewDeadlineMillis
return|;
block|}
DECL|method|setRenewDeadlineMillis (Long renewDeadlineMillis)
specifier|public
name|void
name|setRenewDeadlineMillis
parameter_list|(
name|Long
name|renewDeadlineMillis
parameter_list|)
block|{
name|this
operator|.
name|renewDeadlineMillis
operator|=
name|renewDeadlineMillis
expr_stmt|;
block|}
DECL|method|getRetryPeriodMillis ()
specifier|public
name|Long
name|getRetryPeriodMillis
parameter_list|()
block|{
return|return
name|retryPeriodMillis
return|;
block|}
DECL|method|setRetryPeriodMillis (Long retryPeriodMillis)
specifier|public
name|void
name|setRetryPeriodMillis
parameter_list|(
name|Long
name|retryPeriodMillis
parameter_list|)
block|{
name|this
operator|.
name|retryPeriodMillis
operator|=
name|retryPeriodMillis
expr_stmt|;
block|}
block|}
end_class

end_unit

