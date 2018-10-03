begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openshift.builds
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openshift
operator|.
name|builds
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
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watch
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
name|Watcher
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
name|dsl
operator|.
name|FilterWatchListMultiDeletable
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
name|dsl
operator|.
name|LogWatch
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
name|dsl
operator|.
name|NonNamespaceOperation
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|openshift
operator|.
name|api
operator|.
name|model
operator|.
name|Build
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|openshift
operator|.
name|api
operator|.
name|model
operator|.
name|BuildList
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|openshift
operator|.
name|api
operator|.
name|model
operator|.
name|DoneableBuild
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|openshift
operator|.
name|client
operator|.
name|OpenShiftClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|openshift
operator|.
name|client
operator|.
name|dsl
operator|.
name|BuildResource
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
name|KubernetesOperations
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
name|support
operator|.
name|DefaultProducer
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
name|support
operator|.
name|MessageHelper
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

begin_class
DECL|class|OpenshiftBuildsProducer
specifier|public
class|class
name|OpenshiftBuildsProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|OpenshiftBuildsProducer (AbstractKubernetesEndpoint endpoint)
specifier|public
name|OpenshiftBuildsProducer
parameter_list|(
name|AbstractKubernetesEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|AbstractKubernetesEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|AbstractKubernetesEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
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
name|String
name|operation
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
argument_list|)
condition|)
block|{
name|operation
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_OPERATION
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|operation
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
expr_stmt|;
block|}
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|KubernetesOperations
operator|.
name|LIST_BUILD
case|:
name|doList
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|)
expr_stmt|;
break|break;
case|case
name|KubernetesOperations
operator|.
name|LIST_BUILD_BY_LABELS_OPERATION
case|:
name|doListBuildByLabels
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|)
expr_stmt|;
break|break;
case|case
name|KubernetesOperations
operator|.
name|GET_BUILD_OPERATION
case|:
name|doGetBuild
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operation "
operator|+
name|operation
argument_list|)
throw|;
block|}
block|}
DECL|method|doList (Exchange exchange, String operation)
specifier|protected
name|void
name|doList
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|)
throws|throws
name|Exception
block|{
name|BuildList
name|buildList
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|adapt
argument_list|(
name|OpenShiftClient
operator|.
name|class
argument_list|)
operator|.
name|builds
argument_list|()
operator|.
name|inAnyNamespace
argument_list|()
operator|.
name|list
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|buildList
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doListBuildByLabels (Exchange exchange, String operation)
specifier|protected
name|void
name|doListBuildByLabels
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|)
throws|throws
name|Exception
block|{
name|BuildList
name|buildList
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|labels
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_BUILDS_LABELS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|namespaceName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_NAMESPACE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|namespaceName
argument_list|)
condition|)
block|{
name|NonNamespaceOperation
argument_list|<
name|Build
argument_list|,
name|BuildList
argument_list|,
name|DoneableBuild
argument_list|,
name|BuildResource
argument_list|<
name|Build
argument_list|,
name|DoneableBuild
argument_list|,
name|String
argument_list|,
name|LogWatch
argument_list|>
argument_list|>
name|builds
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|adapt
argument_list|(
name|OpenShiftClient
operator|.
name|class
argument_list|)
operator|.
name|builds
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|labels
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|builds
operator|.
name|withLabel
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buildList
operator|=
name|builds
operator|.
name|list
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|FilterWatchListMultiDeletable
argument_list|<
name|Build
argument_list|,
name|BuildList
argument_list|,
name|Boolean
argument_list|,
name|Watch
argument_list|,
name|Watcher
argument_list|<
name|Build
argument_list|>
argument_list|>
name|builds
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|adapt
argument_list|(
name|OpenShiftClient
operator|.
name|class
argument_list|)
operator|.
name|builds
argument_list|()
operator|.
name|inAnyNamespace
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|labels
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|builds
operator|.
name|withLabel
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buildList
operator|=
name|builds
operator|.
name|list
argument_list|()
expr_stmt|;
block|}
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|buildList
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doGetBuild (Exchange exchange, String operation)
specifier|protected
name|void
name|doGetBuild
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|)
throws|throws
name|Exception
block|{
name|Build
name|build
init|=
literal|null
decl_stmt|;
name|String
name|buildName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_BUILD_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|namespaceName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_NAMESPACE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|buildName
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Get a specific Build require specify a Build name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Get a specific Build require specify a Build name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|namespaceName
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Get a specific Build require specify a namespace name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Get a specific Build require specify a namespace name"
argument_list|)
throw|;
block|}
name|build
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|adapt
argument_list|(
name|OpenShiftClient
operator|.
name|class
argument_list|)
operator|.
name|builds
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|withName
argument_list|(
name|buildName
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|build
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

