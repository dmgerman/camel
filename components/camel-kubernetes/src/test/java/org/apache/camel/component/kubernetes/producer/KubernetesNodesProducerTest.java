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
name|Node
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
DECL|class|KubernetesNodesProducerTest
specifier|public
class|class
name|KubernetesNodesProducerTest
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
name|Node
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
name|assertTrue
argument_list|(
name|result
operator|.
name|size
argument_list|()
operator|==
literal|1
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
literal|"kubernetes.io/hostname"
argument_list|,
literal|"172.28.128.4"
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
name|KUBERNETES_NODES_LABELS
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
name|Node
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
name|Node
name|node
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|node
operator|.
name|getStatus
argument_list|()
operator|.
name|getCapacity
argument_list|()
operator|.
name|get
argument_list|(
literal|"pods"
argument_list|)
operator|.
name|getAmount
argument_list|()
operator|.
name|equals
argument_list|(
literal|"110"
argument_list|)
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
literal|"kubernetes-nodes://%s?oauthToken=%s&operation=listNodes"
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
literal|"kubernetes-nodes://%s?oauthToken=%s&operation=listNodesByLabels"
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

