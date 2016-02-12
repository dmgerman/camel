begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.consumer
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
name|consumer
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
name|Iterator
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
name|EditablePodTemplateSpec
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
name|PodTemplateSpecBuilder
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
name|ReplicationController
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
name|ReplicationControllerSpec
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
name|EndpointInject
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
name|Message
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
DECL|class|KubernetesReplicationControllersConsumerTest
specifier|public
class|class
name|KubernetesReplicationControllersConsumerTest
extends|extends
name|KubernetesTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mockResultEndpoint
specifier|protected
name|MockEndpoint
name|mockResultEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|createAndDeleteReplicationController ()
specifier|public
name|void
name|createAndDeleteReplicationController
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
name|mockResultEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_EVENT_ACTION
argument_list|,
literal|"ADDED"
argument_list|,
literal|"DELETED"
argument_list|,
literal|"MODIFIED"
argument_list|,
literal|"MODIFIED"
argument_list|,
literal|"MODIFIED"
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:createReplicationController"
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
name|KUBERNETES_REPLICATION_CONTROLLER_NAME
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
name|KUBERNETES_REPLICATION_CONTROLLERS_LABELS
argument_list|,
name|labels
argument_list|)
expr_stmt|;
name|ReplicationControllerSpec
name|rcSpec
init|=
operator|new
name|ReplicationControllerSpec
argument_list|()
decl_stmt|;
name|rcSpec
operator|.
name|setReplicas
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|PodTemplateSpecBuilder
name|builder
init|=
operator|new
name|PodTemplateSpecBuilder
argument_list|()
decl_stmt|;
name|EditablePodTemplateSpec
name|t
init|=
name|builder
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
literal|"nginx-template"
argument_list|)
operator|.
name|addToLabels
argument_list|(
literal|"server"
argument_list|,
literal|"nginx"
argument_list|)
operator|.
name|endMetadata
argument_list|()
operator|.
name|withNewSpec
argument_list|()
operator|.
name|addNewContainer
argument_list|()
operator|.
name|withName
argument_list|(
literal|"wildfly"
argument_list|)
operator|.
name|withImage
argument_list|(
literal|"jboss/wildfly"
argument_list|)
operator|.
name|addNewPort
argument_list|()
operator|.
name|withContainerPort
argument_list|(
literal|80
argument_list|)
operator|.
name|endPort
argument_list|()
operator|.
name|endContainer
argument_list|()
operator|.
name|endSpec
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|rcSpec
operator|.
name|setTemplate
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|selectorMap
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
name|selectorMap
operator|.
name|put
argument_list|(
literal|"server"
argument_list|,
literal|"nginx"
argument_list|)
expr_stmt|;
name|rcSpec
operator|.
name|setSelector
argument_list|(
name|selectorMap
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
name|KUBERNETES_REPLICATION_CONTROLLER_SPEC
argument_list|,
name|rcSpec
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|ReplicationController
name|rc
init|=
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|ReplicationController
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|rc
operator|.
name|getMetadata
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|ex
operator|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:deleteReplicationController"
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
name|KUBERNETES_REPLICATION_CONTROLLER_NAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|boolean
name|rcDeleted
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
name|rcDeleted
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|mockResultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
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
literal|"kubernetes://%s?oauthToken=%s&category=replicationControllers&operation=listReplicationControllers"
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
literal|"kubernetes://%s?oauthToken=%s&category=replicationControllers&operation=listReplicationControllersByLabels"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getReplicationController"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=replicationControllers&operation=getReplicationController"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:createReplicationController"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=replicationControllers&operation=createReplicationController"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deleteReplicationController"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=replicationControllers&operation=deleteReplicationController"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"kubernetes://%s?oauthToken=%s&category=replicationControllers"
argument_list|,
name|host
argument_list|,
name|authToken
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|KubernertesProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|mockResultEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|KubernertesProcessor
specifier|public
class|class
name|KubernertesProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Got event with body: "
operator|+
name|in
operator|.
name|getBody
argument_list|()
operator|+
literal|" and action "
operator|+
name|in
operator|.
name|getHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_EVENT_ACTION
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

