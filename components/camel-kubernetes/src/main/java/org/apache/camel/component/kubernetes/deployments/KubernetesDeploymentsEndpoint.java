begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.deployments
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
name|deployments
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The Kubernetes Nodes component provides a producer to execute kubernetes node operations  * and a consumer to consume node events.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.20.0"
argument_list|,
name|scheme
operator|=
literal|"kubernetes-deployments"
argument_list|,
name|title
operator|=
literal|"Kubernetes Deployments"
argument_list|,
name|syntax
operator|=
literal|"kubernetes-deployments:masterUrl"
argument_list|,
name|consumerClass
operator|=
name|KubernetesDeploymentsConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"container,cloud,paas"
argument_list|)
DECL|class|KubernetesDeploymentsEndpoint
specifier|public
class|class
name|KubernetesDeploymentsEndpoint
extends|extends
name|AbstractKubernetesEndpoint
block|{
DECL|method|KubernetesDeploymentsEndpoint (String uri, KubernetesDeploymentsComponent component, KubernetesConfiguration config)
specifier|public
name|KubernetesDeploymentsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|KubernetesDeploymentsComponent
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
name|KubernetesDeploymentsProducer
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
return|return
operator|new
name|KubernetesDeploymentsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

