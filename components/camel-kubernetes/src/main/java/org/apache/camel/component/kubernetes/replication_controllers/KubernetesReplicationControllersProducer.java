begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.replication_controllers
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
name|replication_controllers
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
name|api
operator|.
name|model
operator|.
name|DoneableReplicationController
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
name|ReplicationControllerBuilder
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
name|ReplicationControllerList
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
name|MixedOperation
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
name|kubernetes
operator|.
name|client
operator|.
name|dsl
operator|.
name|RollableScalableResource
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
name|impl
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
name|util
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

begin_class
DECL|class|KubernetesReplicationControllersProducer
specifier|public
class|class
name|KubernetesReplicationControllersProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KubernetesReplicationControllersProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|KubernetesReplicationControllersProducer (AbstractKubernetesEndpoint endpoint)
specifier|public
name|KubernetesReplicationControllersProducer
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
name|LIST_REPLICATION_CONTROLLERS_OPERATION
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
name|LIST_REPLICATION_CONTROLLERS_BY_LABELS_OPERATION
case|:
name|doListReplicationControllersByLabels
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
name|GET_REPLICATION_CONTROLLER_OPERATION
case|:
name|doGetReplicationController
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
name|CREATE_REPLICATION_CONTROLLER_OPERATION
case|:
name|doCreateReplicationController
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
name|DELETE_REPLICATION_CONTROLLER_OPERATION
case|:
name|doDeleteReplicationController
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
name|SCALE_REPLICATION_CONTROLLER_OPERATION
case|:
name|doScaleReplicationController
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
name|ReplicationControllerList
name|rcList
init|=
literal|null
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
name|rcList
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|replicationControllers
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|list
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|rcList
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|replicationControllers
argument_list|()
operator|.
name|inAnyNamespace
argument_list|()
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
name|rcList
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doListReplicationControllersByLabels (Exchange exchange, String operation)
specifier|protected
name|void
name|doListReplicationControllersByLabels
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
name|ReplicationControllerList
name|rcList
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
name|KUBERNETES_REPLICATION_CONTROLLERS_LABELS
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
name|ReplicationController
argument_list|,
name|ReplicationControllerList
argument_list|,
name|DoneableReplicationController
argument_list|,
name|RollableScalableResource
argument_list|<
name|ReplicationController
argument_list|,
name|DoneableReplicationController
argument_list|>
argument_list|>
name|replicationControllers
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|replicationControllers
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
name|replicationControllers
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
name|rcList
operator|=
name|replicationControllers
operator|.
name|list
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|FilterWatchListMultiDeletable
argument_list|<
name|ReplicationController
argument_list|,
name|ReplicationControllerList
argument_list|,
name|Boolean
argument_list|,
name|Watch
argument_list|,
name|Watcher
argument_list|<
name|ReplicationController
argument_list|>
argument_list|>
name|replicationControllers
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|replicationControllers
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
name|replicationControllers
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
name|rcList
operator|=
name|replicationControllers
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
name|rcList
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doGetReplicationController (Exchange exchange, String operation)
specifier|protected
name|void
name|doGetReplicationController
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
name|ReplicationController
name|rc
init|=
literal|null
decl_stmt|;
name|String
name|rcName
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
name|KUBERNETES_REPLICATION_CONTROLLER_NAME
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
name|rcName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Get a specific replication controller require specify a replication controller name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Get a specific replication controller require specify a replication controller name"
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
name|LOG
operator|.
name|error
argument_list|(
literal|"Get a specific replication controller require specify a namespace name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Get a specific replication controller require specify a namespace name"
argument_list|)
throw|;
block|}
name|rc
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|replicationControllers
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|withName
argument_list|(
name|rcName
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
name|rc
argument_list|)
expr_stmt|;
block|}
DECL|method|doCreateReplicationController (Exchange exchange, String operation)
specifier|protected
name|void
name|doCreateReplicationController
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
name|ReplicationController
name|rc
init|=
literal|null
decl_stmt|;
name|String
name|rcName
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
name|KUBERNETES_REPLICATION_CONTROLLER_NAME
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
name|ReplicationControllerSpec
name|rcSpec
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
name|KUBERNETES_REPLICATION_CONTROLLER_SPEC
argument_list|,
name|ReplicationControllerSpec
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
name|rcName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Create a specific replication controller require specify a replication controller name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Create a specific replication controller require specify a replication controller name"
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
name|LOG
operator|.
name|error
argument_list|(
literal|"Create a specific replication controller require specify a namespace name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Create a specific replication controller require specify a namespace name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|rcSpec
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Create a specific replication controller require specify a replication controller spec bean"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Create a specific replication controller require specify a replication controller spec bean"
argument_list|)
throw|;
block|}
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
name|KUBERNETES_REPLICATION_CONTROLLERS_LABELS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|ReplicationController
name|rcCreating
init|=
operator|new
name|ReplicationControllerBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
name|rcName
argument_list|)
operator|.
name|withLabels
argument_list|(
name|labels
argument_list|)
operator|.
name|endMetadata
argument_list|()
operator|.
name|withSpec
argument_list|(
name|rcSpec
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|rc
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|replicationControllers
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|create
argument_list|(
name|rcCreating
argument_list|)
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
name|rc
argument_list|)
expr_stmt|;
block|}
DECL|method|doDeleteReplicationController (Exchange exchange, String operation)
specifier|protected
name|void
name|doDeleteReplicationController
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
name|String
name|rcName
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
name|KUBERNETES_REPLICATION_CONTROLLER_NAME
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
name|rcName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Delete a specific replication controller require specify a replication controller name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Delete a specific replication controller require specify a replication controller name"
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
name|LOG
operator|.
name|error
argument_list|(
literal|"Delete a specific replication controller require specify a namespace name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Delete a specific replication controller require specify a namespace name"
argument_list|)
throw|;
block|}
name|boolean
name|rcDeleted
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|replicationControllers
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|withName
argument_list|(
name|rcName
argument_list|)
operator|.
name|delete
argument_list|()
decl_stmt|;
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
name|rcDeleted
argument_list|)
expr_stmt|;
block|}
DECL|method|doScaleReplicationController (Exchange exchange, String operation)
specifier|protected
name|void
name|doScaleReplicationController
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
name|String
name|rcName
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
name|KUBERNETES_REPLICATION_CONTROLLER_NAME
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
name|Integer
name|replicasNumber
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
name|KUBERNETES_REPLICATION_CONTROLLER_REPLICAS
argument_list|,
name|Integer
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
name|rcName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Scale a specific replication controller require specify a replication controller name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Scale a specific replication controller require specify a replication controller name"
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
name|LOG
operator|.
name|error
argument_list|(
literal|"Scale a specific replication controller require specify a namespace name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Scale a specific replication controller require specify a namespace name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|replicasNumber
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Scale a specific replication controller require specify a replicas number"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Scale a specific replication controller require specify a replicas number"
argument_list|)
throw|;
block|}
name|ReplicationController
name|rcScaled
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|replicationControllers
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|withName
argument_list|(
name|rcName
argument_list|)
operator|.
name|scale
argument_list|(
name|replicasNumber
argument_list|,
literal|true
argument_list|)
decl_stmt|;
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
name|rcScaled
operator|.
name|getStatus
argument_list|()
operator|.
name|getReplicas
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

