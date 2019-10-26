begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.persistent_volumes_claims
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
name|persistent_volumes_claims
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Processor
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
name|Producer
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
name|component
operator|.
name|kubernetes
operator|.
name|AbstractKubernetesEndpoint
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
name|component
operator|.
name|kubernetes
operator|.
name|KubernetesConfiguration
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
name|UriEndpoint
import|;
end_import

begin_comment
comment|/**  * The Kubernetes Persistent Volumes Claims component provides a producer to  * execute kubernetes persistent volume claim operations.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|scheme
operator|=
literal|"kubernetes-persistent-volumes-claims"
argument_list|,
name|title
operator|=
literal|"Kubernetes Persistent Volume Claim"
argument_list|,
name|syntax
operator|=
literal|"kubernetes-persistent-volumes-claims:masterUrl"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"container,cloud,paas"
argument_list|)
DECL|class|KubernetesPersistentVolumesClaimsEndpoint
specifier|public
class|class
name|KubernetesPersistentVolumesClaimsEndpoint
extends|extends
name|AbstractKubernetesEndpoint
block|{
DECL|method|KubernetesPersistentVolumesClaimsEndpoint (String uri, KubernetesPersistentVolumesClaimsComponent component, KubernetesConfiguration config)
specifier|public
name|KubernetesPersistentVolumesClaimsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|KubernetesPersistentVolumesClaimsComponent
name|component
parameter_list|,
name|KubernetesConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|KubernetesPersistentVolumesClaimsProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The kubernetes-persistent-volumes-claims doesn't exist"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

