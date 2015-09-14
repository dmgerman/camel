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
name|DoneableResourceQuota
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
name|EditableResourceQuota
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
name|ResourceQuota
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
name|ResourceQuotaBuilder
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
name|ResourceQuotaList
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
name|ResourceQuotaSpec
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
name|dsl
operator|.
name|ClientNonNamespaceOperation
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
name|ClientOperation
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
name|ClientResource
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
name|KubernetesEndpoint
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
DECL|class|KubernetesResourcesQuotaProducer
specifier|public
class|class
name|KubernetesResourcesQuotaProducer
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
name|KubernetesResourcesQuotaProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|KubernetesResourcesQuotaProducer (KubernetesEndpoint endpoint)
specifier|public
name|KubernetesResourcesQuotaProducer
parameter_list|(
name|KubernetesEndpoint
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
name|KubernetesEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|KubernetesEndpoint
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
name|LIST_RESOURCES_QUOTA
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
name|LIST_SECRETS_BY_LABELS_OPERATION
case|:
name|doListResourceQuotasByLabels
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
name|GET_RESOURCE_QUOTA_OPERATION
case|:
name|doGetResourceQuota
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
name|CREATE_RESOURCE_QUOTA_OPERATION
case|:
name|doCreateResourceQuota
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
name|DELETE_RESOURCE_QUOTA_OPERATION
case|:
name|doDeleteResourceQuota
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
name|ResourceQuotaList
name|resList
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|resourceQuotas
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
name|resList
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doListResourceQuotasByLabels (Exchange exchange, String operation)
specifier|protected
name|void
name|doListResourceQuotasByLabels
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
name|ResourceQuotaList
name|resList
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
name|KUBERNETES_RESOURCES_QUOTA_LABELS
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
name|ClientNonNamespaceOperation
argument_list|<
name|KubernetesClient
argument_list|,
name|ResourceQuota
argument_list|,
name|ResourceQuotaList
argument_list|,
name|DoneableResourceQuota
argument_list|,
name|ClientResource
argument_list|<
name|ResourceQuota
argument_list|,
name|DoneableResourceQuota
argument_list|>
argument_list|>
name|resQuota
decl_stmt|;
name|resQuota
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|resourceQuotas
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
expr_stmt|;
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
name|resQuota
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
name|resList
operator|=
name|resQuota
operator|.
name|list
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ClientOperation
argument_list|<
name|KubernetesClient
argument_list|,
name|ResourceQuota
argument_list|,
name|ResourceQuotaList
argument_list|,
name|DoneableResourceQuota
argument_list|,
name|ClientResource
argument_list|<
name|ResourceQuota
argument_list|,
name|DoneableResourceQuota
argument_list|>
argument_list|>
name|resQuota
decl_stmt|;
name|resQuota
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|resourceQuotas
argument_list|()
expr_stmt|;
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
name|resQuota
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
name|resList
operator|=
name|resQuota
operator|.
name|list
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|resList
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doGetResourceQuota (Exchange exchange, String operation)
specifier|protected
name|void
name|doGetResourceQuota
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
name|ResourceQuota
name|rq
init|=
literal|null
decl_stmt|;
name|String
name|rqName
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
name|KUBERNETES_RESOURCES_QUOTA_NAME
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
name|rqName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Get a specific Resource Quota require specify a Resource Quota name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Get a specific Resource Quota require specify a Resource Quota name"
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
literal|"Get a specific Resource Quota require specify a namespace name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Get a specific Resource Quota require specify a namespace name"
argument_list|)
throw|;
block|}
name|rq
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|resourceQuotas
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|withName
argument_list|(
name|rqName
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|rq
argument_list|)
expr_stmt|;
block|}
DECL|method|doCreateResourceQuota (Exchange exchange, String operation)
specifier|protected
name|void
name|doCreateResourceQuota
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
name|ResourceQuota
name|rq
init|=
literal|null
decl_stmt|;
name|String
name|rqName
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
name|KUBERNETES_RESOURCES_QUOTA_NAME
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
name|ResourceQuotaSpec
name|rqSpec
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
name|KUBERNETES_RESOURCE_QUOTA_SPEC
argument_list|,
name|ResourceQuotaSpec
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
name|rqName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Create a specific resource quota require specify a resource quota name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Create a specific resource quota require specify a resource quota name"
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
literal|"Create a specific resource quota require specify a namespace name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Create a specific resource quota require specify a namespace name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|rqSpec
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Create a specific resource quota require specify a resource quota spec bean"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Create a specific resource quota require specify a resource quota spec bean"
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
name|KUBERNETES_RESOURCES_QUOTA_LABELS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|EditableResourceQuota
name|rqCreating
init|=
operator|new
name|ResourceQuotaBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
name|rqName
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
name|rqSpec
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|rq
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|resourceQuotas
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|create
argument_list|(
name|rqCreating
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|rq
argument_list|)
expr_stmt|;
block|}
DECL|method|doDeleteResourceQuota (Exchange exchange, String operation)
specifier|protected
name|void
name|doDeleteResourceQuota
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
name|rqName
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
name|KUBERNETES_RESOURCES_QUOTA_NAME
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
name|rqName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Delete a specific resource quota require specify a resource quota name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Delete a specific resource quota require specify a resource quota name"
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
literal|"Delete a specific resource quota require specify a namespace name"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Delete a specific resource quota require specify a namespace name"
argument_list|)
throw|;
block|}
name|boolean
name|rqDeleted
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|resourceQuotas
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|namespaceName
argument_list|)
operator|.
name|withName
argument_list|(
name|rqName
argument_list|)
operator|.
name|delete
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|rqDeleted
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

