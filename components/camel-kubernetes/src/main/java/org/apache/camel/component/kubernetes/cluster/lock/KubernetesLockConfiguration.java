begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.cluster.lock
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
name|cluster
operator|.
name|lock
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|KubernetesClient
import|;
end_import

begin_comment
comment|/**  * Configuration for Kubernetes Lock.  */
end_comment

begin_class
DECL|class|KubernetesLockConfiguration
specifier|public
class|class
name|KubernetesLockConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|DEFAULT_CONFIGMAP_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CONFIGMAP_NAME
init|=
literal|"leaders"
decl_stmt|;
DECL|field|DEFAULT_JITTER_FACTOR
specifier|public
specifier|static
specifier|final
name|double
name|DEFAULT_JITTER_FACTOR
init|=
literal|1.2
decl_stmt|;
DECL|field|DEFAULT_LEASE_DURATION_MILLIS
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_LEASE_DURATION_MILLIS
init|=
literal|30000
decl_stmt|;
DECL|field|DEFAULT_RENEW_DEADLINE_MILLIS
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_RENEW_DEADLINE_MILLIS
init|=
literal|20000
decl_stmt|;
DECL|field|DEFAULT_RETRY_PERIOD_MILLIS
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_RETRY_PERIOD_MILLIS
init|=
literal|5000
decl_stmt|;
comment|/**      * Kubernetes namespace containing the pods and the ConfigMap used for      * locking.      */
DECL|field|kubernetesResourcesNamespace
specifier|private
name|String
name|kubernetesResourcesNamespace
decl_stmt|;
comment|/**      * Name of the ConfigMap used for locking.      */
DECL|field|configMapName
specifier|private
name|String
name|configMapName
init|=
name|DEFAULT_CONFIGMAP_NAME
decl_stmt|;
comment|/**      * Name of the lock group (or namespace according to the Camel cluster      * convention) within the chosen ConfigMap.      */
DECL|field|groupName
specifier|private
name|String
name|groupName
decl_stmt|;
comment|/**      * Name of the current pod (defaults to host name).      */
DECL|field|podName
specifier|private
name|String
name|podName
decl_stmt|;
comment|/**      * Labels used to identify the members of the cluster.      */
DECL|field|clusterLabels
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|clusterLabels
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * A jitter factor to apply in order to prevent all pods to call Kubernetes      * APIs in the same instant.      */
DECL|field|jitterFactor
specifier|private
name|double
name|jitterFactor
init|=
name|DEFAULT_JITTER_FACTOR
decl_stmt|;
comment|/**      * The default duration of the lease for the current leader.      */
DECL|field|leaseDurationMillis
specifier|private
name|long
name|leaseDurationMillis
init|=
name|DEFAULT_LEASE_DURATION_MILLIS
decl_stmt|;
comment|/**      * The deadline after which the leader must stop its services because it may      * have lost the leadership.      */
DECL|field|renewDeadlineMillis
specifier|private
name|long
name|renewDeadlineMillis
init|=
name|DEFAULT_RENEW_DEADLINE_MILLIS
decl_stmt|;
comment|/**      * The time between two subsequent attempts to check and acquire the      * leadership. It is randomized using the jitter factor.      */
DECL|field|retryPeriodMillis
specifier|private
name|long
name|retryPeriodMillis
init|=
name|DEFAULT_RETRY_PERIOD_MILLIS
decl_stmt|;
DECL|method|KubernetesLockConfiguration ()
specifier|public
name|KubernetesLockConfiguration
parameter_list|()
block|{     }
DECL|method|getKubernetesResourcesNamespaceOrDefault (KubernetesClient kubernetesClient)
specifier|public
name|String
name|getKubernetesResourcesNamespaceOrDefault
parameter_list|(
name|KubernetesClient
name|kubernetesClient
parameter_list|)
block|{
if|if
condition|(
name|kubernetesResourcesNamespace
operator|!=
literal|null
condition|)
block|{
return|return
name|kubernetesResourcesNamespace
return|;
block|}
return|return
name|kubernetesClient
operator|.
name|getNamespace
argument_list|()
return|;
block|}
DECL|method|getKubernetesResourcesNamespace ()
specifier|public
name|String
name|getKubernetesResourcesNamespace
parameter_list|()
block|{
return|return
name|kubernetesResourcesNamespace
return|;
block|}
DECL|method|setKubernetesResourcesNamespace (String kubernetesResourcesNamespace)
specifier|public
name|void
name|setKubernetesResourcesNamespace
parameter_list|(
name|String
name|kubernetesResourcesNamespace
parameter_list|)
block|{
name|this
operator|.
name|kubernetesResourcesNamespace
operator|=
name|kubernetesResourcesNamespace
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
DECL|method|getGroupName ()
specifier|public
name|String
name|getGroupName
parameter_list|()
block|{
return|return
name|groupName
return|;
block|}
DECL|method|setGroupName (String groupName)
specifier|public
name|void
name|setGroupName
parameter_list|(
name|String
name|groupName
parameter_list|)
block|{
name|this
operator|.
name|groupName
operator|=
name|groupName
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
DECL|method|addToClusterLabels (String key, String value)
specifier|public
name|void
name|addToClusterLabels
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|clusterLabels
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
name|double
name|getJitterFactor
parameter_list|()
block|{
return|return
name|jitterFactor
return|;
block|}
DECL|method|setJitterFactor (double jitterFactor)
specifier|public
name|void
name|setJitterFactor
parameter_list|(
name|double
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
name|long
name|getLeaseDurationMillis
parameter_list|()
block|{
return|return
name|leaseDurationMillis
return|;
block|}
DECL|method|setLeaseDurationMillis (long leaseDurationMillis)
specifier|public
name|void
name|setLeaseDurationMillis
parameter_list|(
name|long
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
name|long
name|getRenewDeadlineMillis
parameter_list|()
block|{
return|return
name|renewDeadlineMillis
return|;
block|}
DECL|method|setRenewDeadlineMillis (long renewDeadlineMillis)
specifier|public
name|void
name|setRenewDeadlineMillis
parameter_list|(
name|long
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
name|long
name|getRetryPeriodMillis
parameter_list|()
block|{
return|return
name|retryPeriodMillis
return|;
block|}
DECL|method|setRetryPeriodMillis (long retryPeriodMillis)
specifier|public
name|void
name|setRetryPeriodMillis
parameter_list|(
name|long
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
DECL|method|copy ()
specifier|public
name|KubernetesLockConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
name|KubernetesLockConfiguration
name|copy
init|=
operator|(
name|KubernetesLockConfiguration
operator|)
name|this
operator|.
name|clone
argument_list|()
decl_stmt|;
return|return
name|copy
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot clone"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"KubernetesLockConfiguration{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"kubernetesResourcesNamespace='"
argument_list|)
operator|.
name|append
argument_list|(
name|kubernetesResourcesNamespace
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", configMapName='"
argument_list|)
operator|.
name|append
argument_list|(
name|configMapName
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", groupName='"
argument_list|)
operator|.
name|append
argument_list|(
name|groupName
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", podName='"
argument_list|)
operator|.
name|append
argument_list|(
name|podName
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", clusterLabels="
argument_list|)
operator|.
name|append
argument_list|(
name|clusterLabels
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", jitterFactor="
argument_list|)
operator|.
name|append
argument_list|(
name|jitterFactor
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", leaseDurationMillis="
argument_list|)
operator|.
name|append
argument_list|(
name|leaseDurationMillis
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", renewDeadlineMillis="
argument_list|)
operator|.
name|append
argument_list|(
name|renewDeadlineMillis
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", retryPeriodMillis="
argument_list|)
operator|.
name|append
argument_list|(
name|retryPeriodMillis
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

