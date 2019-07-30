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
name|Pod
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
name|PodBuilder
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
name|PodListBuilder
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
name|BindToRegistry
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
DECL|class|KubernetesPodsProducerTest
specifier|public
class|class
name|KubernetesPodsProducerTest
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
name|BindToRegistry
argument_list|(
literal|"kubernetesClient"
argument_list|)
DECL|method|getClient ()
specifier|public
name|KubernetesClient
name|getClient
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|server
operator|.
name|getClient
argument_list|()
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
literal|"/api/v1/pods"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
operator|new
name|PodListBuilder
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
name|List
argument_list|<
name|Pod
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
literal|"/api/v1/pods?labelSelector="
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
name|PodListBuilder
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
name|KUBERNETES_PODS_LABELS
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
name|Pod
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
DECL|method|getPodTest ()
specifier|public
name|void
name|getPodTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Pod
name|pod1
init|=
operator|new
name|PodBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
literal|"pod1"
argument_list|)
operator|.
name|withNamespace
argument_list|(
literal|"test"
argument_list|)
operator|.
name|and
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|Pod
name|pod2
init|=
operator|new
name|PodBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
literal|"pod2"
argument_list|)
operator|.
name|withNamespace
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|and
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
literal|"/api/v1/namespaces/test/pods/pod1"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
name|pod1
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
literal|"/api/v1/namespaces/ns1/pods/pod2"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
name|pod2
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
literal|"direct:getPod"
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
name|KUBERNETES_POD_NAME
argument_list|,
literal|"pod1"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Pod
name|result
init|=
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|Pod
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"pod1"
argument_list|,
name|result
operator|.
name|getMetadata
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deletePod ()
specifier|public
name|void
name|deletePod
parameter_list|()
throws|throws
name|Exception
block|{
name|Pod
name|pod1
init|=
operator|new
name|PodBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
literal|"pod1"
argument_list|)
operator|.
name|withNamespace
argument_list|(
literal|"test"
argument_list|)
operator|.
name|and
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
literal|"/api/v1/namespaces/test/pods/pod1"
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|200
argument_list|,
name|pod1
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
literal|"direct:deletePod"
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
name|KUBERNETES_POD_NAME
argument_list|,
literal|"pod1"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|boolean
name|podDeleted
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
name|podDeleted
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
name|to
argument_list|(
literal|"kubernetes-pods:///?kubernetesClient=#kubernetesClient&operation=listPods"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:listByLabels"
argument_list|)
operator|.
name|to
argument_list|(
literal|"kubernetes-pods:///?kubernetesClient=#kubernetesClient&operation=listPodsByLabels"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getPod"
argument_list|)
operator|.
name|to
argument_list|(
literal|"kubernetes-pods:///?kubernetesClient=#kubernetesClient&operation=getPod"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deletePod"
argument_list|)
operator|.
name|to
argument_list|(
literal|"kubernetes-pods:///?kubernetesClient=#kubernetesClient&operation=deletePod"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

