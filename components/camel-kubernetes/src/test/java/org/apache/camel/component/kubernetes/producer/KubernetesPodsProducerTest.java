begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
import|;
end_import

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
name|Iterator
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
name|Container
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
name|ContainerPort
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
name|PodSpec
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
name|util
operator|.
name|ObjectHelper
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
name|Test
DECL|method|listTest ()
specifier|public
name|void
name|listTest
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|authToken
argument_list|)
condition|)
block|{
return|return;
block|}
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
name|boolean
name|defaultExists
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|Pod
argument_list|>
name|it
init|=
name|result
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Pod
name|pod
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|pod
operator|.
name|getMetadata
argument_list|()
operator|.
name|getName
argument_list|()
operator|)
operator|.
name|contains
argument_list|(
literal|"fabric8"
argument_list|)
condition|)
block|{
name|defaultExists
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|defaultExists
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|authToken
argument_list|)
condition|)
block|{
return|return;
block|}
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
literal|"default"
argument_list|)
expr_stmt|;
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|labels
operator|.
name|put
argument_list|(
literal|"component"
argument_list|,
literal|"elasticsearch"
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
name|boolean
name|podExists
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|Pod
argument_list|>
name|it
init|=
name|result
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Pod
name|pod
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pod
operator|.
name|getMetadata
argument_list|()
operator|.
name|getLabels
argument_list|()
operator|.
name|containsValue
argument_list|(
literal|"elasticsearch"
argument_list|)
condition|)
block|{
name|podExists
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertFalse
argument_list|(
name|podExists
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|authToken
argument_list|)
condition|)
block|{
return|return;
block|}
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
literal|"default"
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
literal|"elasticsearch-7015o"
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
name|assertNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAndDeletePod ()
specifier|public
name|void
name|createAndDeletePod
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|authToken
argument_list|)
condition|)
block|{
return|return;
block|}
name|Exchange
name|ex
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:createPod"
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
literal|"default"
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
literal|"test"
argument_list|)
expr_stmt|;
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|labels
operator|.
name|put
argument_list|(
literal|"this"
argument_list|,
literal|"rocks"
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
name|PodSpec
name|podSpec
init|=
operator|new
name|PodSpec
argument_list|()
decl_stmt|;
name|podSpec
operator|.
name|setHostname
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|Container
name|cont
init|=
operator|new
name|Container
argument_list|()
decl_stmt|;
name|cont
operator|.
name|setImage
argument_list|(
literal|"docker.io/jboss/wildfly:latest"
argument_list|)
expr_stmt|;
name|cont
operator|.
name|setName
argument_list|(
literal|"pippo"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ContainerPort
argument_list|>
name|containerPort
init|=
operator|new
name|ArrayList
argument_list|<
name|ContainerPort
argument_list|>
argument_list|()
decl_stmt|;
name|ContainerPort
name|port
init|=
operator|new
name|ContainerPort
argument_list|()
decl_stmt|;
name|port
operator|.
name|setHostIP
argument_list|(
literal|"0.0.0.0"
argument_list|)
expr_stmt|;
name|port
operator|.
name|setHostPort
argument_list|(
literal|8080
argument_list|)
expr_stmt|;
name|port
operator|.
name|setContainerPort
argument_list|(
literal|8080
argument_list|)
expr_stmt|;
name|containerPort
operator|.
name|add
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|cont
operator|.
name|setPorts
argument_list|(
name|containerPort
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Container
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Container
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|cont
argument_list|)
expr_stmt|;
name|podSpec
operator|.
name|setContainers
argument_list|(
name|list
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
name|KUBERNETES_POD_SPEC
argument_list|,
name|podSpec
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|ex
operator|=
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
literal|"default"
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
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|toF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=pods&operation=listPods"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:listByLabels"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=pods&operation=listPodsByLabels"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getPod"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=pods&operation=getPod"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:createPod"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=pods&operation=createPod"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deletePod"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=pods&operation=deletePod"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

