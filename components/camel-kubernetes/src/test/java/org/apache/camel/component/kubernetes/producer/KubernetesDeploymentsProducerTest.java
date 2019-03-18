begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.producer
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
name|producer
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
name|List
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
name|api
operator|.
name|model
operator|.
name|apps
operator|.
name|Deployment
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
name|api
operator|.
name|model
operator|.
name|apps
operator|.
name|DeploymentBuilder
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
name|api
operator|.
name|model
operator|.
name|apps
operator|.
name|DeploymentListBuilder
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
name|server
operator|.
name|mock
operator|.
name|KubernetesServer
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
name|Exchange
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
name|builder
operator|.
name|RouteBuilder
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
name|KubernetesConstants
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
name|KubernetesTestSupport
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|KubernetesDeploymentsProducerTest
specifier|public
class|class
name|KubernetesDeploymentsProducerTest
extends|extends
name|KubernetesTestSupport
block|{
annotation|@
name|Rule
DECL|field|server
specifier|public
name|KubernetesServer
name|server
init|=
operator|new
name|KubernetesServer
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"kubernetesClient"
argument_list|,
name|server
operator|.
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|listTest ()
specifier|public
name|void
name|listTest
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|expect
argument_list|()
operator|.
name|withPath
argument_list|(
literal|"/apis/extensions/v1beta1/namespaces/test/deployments"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
operator|new
name|DeploymentListBuilder
argument_list|()
operator|.
name|addNewItem
argument_list|()
operator|.
name|and
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|once
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Deployment
argument_list|>
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:list"
argument_list|,
literal|""
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listByLabelsTest ()
specifier|public
name|void
name|listByLabelsTest
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|expect
argument_list|()
operator|.
name|withPath
argument_list|(
literal|"/apis/extensions/v1beta1/namespaces/test/deployments?labelSelector="
operator|+
name|toUrlEncoded
argument_list|(
literal|"key1=value1,key2=value2"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
operator|new
name|DeploymentListBuilder
argument_list|()
operator|.
name|addNewItem
argument_list|()
operator|.
name|and
argument_list|()
operator|.
name|addNewItem
argument_list|()
operator|.
name|and
argument_list|()
operator|.
name|addNewItem
argument_list|()
operator|.
name|and
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|once
argument_list|()
expr_stmt|;
name|Exchange
name|ex
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:listByLabels"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|labels
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|labels
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|labels
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_DEPLOYMENTS_LABELS
argument_list|,
name|labels
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Deployment
argument_list|>
name|result
init|=
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAndDeleteDeployment ()
specifier|public
name|void
name|createAndDeleteDeployment
parameter_list|()
throws|throws
name|Exception
block|{
name|Deployment
name|de1
init|=
operator|new
name|DeploymentBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withNamespace
argument_list|(
literal|"test"
argument_list|)
operator|.
name|withName
argument_list|(
literal|"de1"
argument_list|)
operator|.
name|withResourceVersion
argument_list|(
literal|"1"
argument_list|)
operator|.
name|withGeneration
argument_list|(
literal|2L
argument_list|)
operator|.
name|endMetadata
argument_list|()
operator|.
name|withNewSpec
argument_list|()
operator|.
name|withReplicas
argument_list|(
literal|0
argument_list|)
operator|.
name|endSpec
argument_list|()
operator|.
name|withNewStatus
argument_list|()
operator|.
name|withReplicas
argument_list|(
literal|1
argument_list|)
operator|.
name|withObservedGeneration
argument_list|(
literal|1L
argument_list|)
operator|.
name|endStatus
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|server
operator|.
name|expect
argument_list|()
operator|.
name|withPath
argument_list|(
literal|"/apis/extensions/v1beta1/namespaces/test/deployments/de1"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
name|de1
argument_list|)
operator|.
name|once
argument_list|()
expr_stmt|;
name|server
operator|.
name|expect
argument_list|()
operator|.
name|withPath
argument_list|(
literal|"/apis/extensions/v1beta1/namespaces/test/deployments/de1"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
operator|new
name|DeploymentBuilder
argument_list|(
name|de1
argument_list|)
operator|.
name|editStatus
argument_list|()
operator|.
name|withReplicas
argument_list|(
literal|0
argument_list|)
operator|.
name|withObservedGeneration
argument_list|(
literal|2L
argument_list|)
operator|.
name|endStatus
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|times
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:deleteDeployment"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_DEPLOYMENT_NAME
argument_list|,
literal|"de1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_NAMESPACE_NAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|boolean
name|deDeleted
init|=
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|deDeleted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createScaleAndDeleteDeployment ()
specifier|public
name|void
name|createScaleAndDeleteDeployment
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|expect
argument_list|()
operator|.
name|withPath
argument_list|(
literal|"/apis/extensions/v1beta1/namespaces/test/deployments/de1"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
operator|new
name|DeploymentBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
literal|"de1"
argument_list|)
operator|.
name|withResourceVersion
argument_list|(
literal|"1"
argument_list|)
operator|.
name|endMetadata
argument_list|()
operator|.
name|withNewSpec
argument_list|()
operator|.
name|withReplicas
argument_list|(
literal|5
argument_list|)
operator|.
name|endSpec
argument_list|()
operator|.
name|withNewStatus
argument_list|()
operator|.
name|withReplicas
argument_list|(
literal|1
argument_list|)
operator|.
name|endStatus
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|once
argument_list|()
expr_stmt|;
name|server
operator|.
name|expect
argument_list|()
operator|.
name|withPath
argument_list|(
literal|"/apis/extensions/v1beta1/namespaces/test/deployments/de1"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
operator|new
name|DeploymentBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
literal|"de1"
argument_list|)
operator|.
name|withResourceVersion
argument_list|(
literal|"1"
argument_list|)
operator|.
name|endMetadata
argument_list|()
operator|.
name|withNewSpec
argument_list|()
operator|.
name|withReplicas
argument_list|(
literal|5
argument_list|)
operator|.
name|endSpec
argument_list|()
operator|.
name|withNewStatus
argument_list|()
operator|.
name|withReplicas
argument_list|(
literal|5
argument_list|)
operator|.
name|endStatus
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|always
argument_list|()
expr_stmt|;
name|Exchange
name|ex
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:scaleDeployment"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_NAMESPACE_NAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_DEPLOYMENT_NAME
argument_list|,
literal|"de1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_DEPLOYMENT_REPLICAS
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|//Thread.sleep(3000);
name|int
name|replicas
init|=
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|replicas
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:list"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes-deployments:///?kubernetesClient=#kubernetesClient&operation=listDeployments"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:listByLabels"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes-deployments:///?kubernetesClient=#kubernetesClient&operation=listDeploymentsByLabels"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deleteDeployment"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes-deployments:///?kubernetesClient=#kubernetesClient&operation=deleteDeployment"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:createDeployment"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes-deployments:///?kubernetesClient=#kubernetesClient&operation=createDeployment"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:scaleDeployment"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes-deployments:///?kubernetesClient=#kubernetesClient&operation=scaleDeployment"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

