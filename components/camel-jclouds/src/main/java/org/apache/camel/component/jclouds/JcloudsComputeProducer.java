begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jclouds
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jclouds
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Predicate
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
name|CamelException
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
name|CamelExchangeException
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
name|jclouds
operator|.
name|compute
operator|.
name|ComputeService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|RunNodesException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|domain
operator|.
name|ComputeMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|domain
operator|.
name|ExecResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|domain
operator|.
name|Hardware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|domain
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|domain
operator|.
name|NodeMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|domain
operator|.
name|TemplateBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|domain
operator|.
name|internal
operator|.
name|NodeMetadataImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|options
operator|.
name|RunScriptOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|domain
operator|.
name|LoginCredentials
import|;
end_import

begin_class
DECL|class|JcloudsComputeProducer
specifier|public
class|class
name|JcloudsComputeProducer
extends|extends
name|JcloudsProducer
block|{
DECL|field|computeService
specifier|private
specifier|final
name|ComputeService
name|computeService
decl_stmt|;
DECL|method|JcloudsComputeProducer (JcloudsEndpoint endpoint, ComputeService computeService)
specifier|public
name|JcloudsComputeProducer
parameter_list|(
name|JcloudsEndpoint
name|endpoint
parameter_list|,
name|ComputeService
name|computeService
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|computeService
operator|=
name|computeService
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|JcloudsComputeEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|JcloudsComputeEndpoint
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
init|=
name|getOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Operation must be specified in the endpoint URI or as a property on the exchange."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
if|if
condition|(
name|JcloudsConstants
operator|.
name|LIST_NODES
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|listNodes
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JcloudsConstants
operator|.
name|LIST_IMAGES
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|listImages
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JcloudsConstants
operator|.
name|LIST_HARDWARE
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|listHardware
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JcloudsConstants
operator|.
name|RUN_SCRIPT
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|runScriptOnNode
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JcloudsConstants
operator|.
name|CREATE_NODE
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|createNode
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JcloudsConstants
operator|.
name|DESTROY_NODE
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|destroyNode
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a node with the specified group.      */
DECL|method|createNode (Exchange exchange)
specifier|protected
name|void
name|createNode
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelException
block|{
name|String
name|group
init|=
name|getGroup
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|imageId
init|=
name|getImageId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|locationId
init|=
name|getLocationId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|hardwareId
init|=
name|getHardwareId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Group must be specific in the URI or as exchange property for the destroy node operation."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|TemplateBuilder
name|builder
init|=
name|computeService
operator|.
name|templateBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|any
argument_list|()
expr_stmt|;
if|if
condition|(
name|locationId
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|locationId
argument_list|(
name|locationId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|imageId
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|imageId
argument_list|(
name|imageId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hardwareId
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|hardwareId
argument_list|(
name|hardwareId
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Set
argument_list|<
name|?
extends|extends
name|NodeMetadata
argument_list|>
name|nodeMetadatas
init|=
name|computeService
operator|.
name|createNodesInGroup
argument_list|(
name|group
argument_list|,
literal|1
argument_list|,
name|builder
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|nodeMetadatas
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RunNodesException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error creating jclouds node."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Runs a script on the target node.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|runScriptOnNode (Exchange exchange)
specifier|protected
name|void
name|runScriptOnNode
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelException
block|{
name|String
name|script
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|nodeId
init|=
name|getNodeId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|getUser
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LoginCredentials
name|credentials
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|credentials
operator|=
name|LoginCredentials
operator|.
name|builder
argument_list|()
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|ExecResponse
name|execResponse
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|credentials
operator|==
literal|null
condition|)
block|{
name|execResponse
operator|=
name|computeService
operator|.
name|runScriptOnNode
argument_list|(
name|nodeId
argument_list|,
name|script
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|execResponse
operator|=
name|computeService
operator|.
name|runScriptOnNode
argument_list|(
name|nodeId
argument_list|,
name|script
argument_list|,
name|RunScriptOptions
operator|.
name|Builder
operator|.
name|overrideLoginCredentials
argument_list|(
name|credentials
argument_list|)
operator|.
name|runAsRoot
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|execResponse
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Failed to receive response for run script operation on node: "
operator|+
name|nodeId
operator|+
literal|" using script: "
operator|+
name|script
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|JcloudsConstants
operator|.
name|RUN_SCRIPT_ERROR
argument_list|,
name|execResponse
operator|.
name|getError
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|JcloudsConstants
operator|.
name|RUN_SCRIPT_EXIT_CODE
argument_list|,
name|execResponse
operator|.
name|getExitStatus
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|execResponse
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Destroys the node with the specified nodeId.      */
DECL|method|destroyNode (Exchange exchange)
specifier|protected
name|void
name|destroyNode
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Predicate
argument_list|<
name|NodeMetadata
argument_list|>
name|predicate
init|=
name|getNodePredicate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|computeService
operator|.
name|destroyNodesMatching
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the metadata of the available nodes to the out message.      */
DECL|method|listNodes (Exchange exchange)
specifier|protected
name|void
name|listNodes
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Predicate
argument_list|<
name|ComputeMetadata
argument_list|>
name|predicate
init|=
name|getComputePredicate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|?
extends|extends
name|ComputeMetadata
argument_list|>
name|computeMetadatas
init|=
name|computeService
operator|.
name|listNodesDetailsMatching
argument_list|(
name|predicate
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|computeMetadatas
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the available images to the out message.      */
DECL|method|listImages (Exchange exchange)
specifier|protected
name|void
name|listImages
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Set
argument_list|<
name|?
extends|extends
name|Image
argument_list|>
name|images
init|=
name|computeService
operator|.
name|listImages
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|images
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the available hardware profiles to the out message.      */
DECL|method|listHardware (Exchange exchange)
specifier|protected
name|void
name|listHardware
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Set
argument_list|<
name|?
extends|extends
name|Hardware
argument_list|>
name|hardwareProfiles
init|=
name|computeService
operator|.
name|listHardwareProfiles
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|hardwareProfiles
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the required {@ComputeMetadata} {@link Predicate} for the Exhcnage.      * The predicate can be used for filtering.      */
DECL|method|getComputePredicate (final Exchange exchange)
specifier|public
name|Predicate
argument_list|<
name|ComputeMetadata
argument_list|>
name|getComputePredicate
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|String
name|nodeId
init|=
name|getNodeId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Predicate
argument_list|<
name|ComputeMetadata
argument_list|>
name|predicate
init|=
operator|new
name|Predicate
argument_list|<
name|ComputeMetadata
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|apply
parameter_list|(
name|ComputeMetadata
name|metadata
parameter_list|)
block|{
if|if
condition|(
name|nodeId
operator|!=
literal|null
operator|&&
operator|!
name|nodeId
operator|.
name|equals
argument_list|(
name|metadata
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|//If NodeMetadata also delegate to Node predicate.
if|if
condition|(
name|metadata
operator|instanceof
name|NodeMetadataImpl
condition|)
block|{
name|Predicate
argument_list|<
name|NodeMetadata
argument_list|>
name|nodeMetadataPredicate
init|=
name|getNodePredicate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|nodeMetadataPredicate
operator|.
name|apply
argument_list|(
operator|(
name|NodeMetadataImpl
operator|)
name|metadata
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
return|return
name|predicate
return|;
block|}
comment|/**      * Returns the required {@ComputeMetadata} {@link Predicate} for the Exhcnage.      * The predicate can be used for filtering.      */
DECL|method|getNodePredicate (Exchange exchange)
specifier|public
name|Predicate
argument_list|<
name|NodeMetadata
argument_list|>
name|getNodePredicate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|String
name|nodeId
init|=
name|getNodeId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
specifier|final
name|String
name|imageId
init|=
name|getImageId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
specifier|final
name|String
name|group
init|=
name|getGroup
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
specifier|final
name|NodeMetadata
operator|.
name|Status
name|queryState
init|=
name|getNodeState
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Predicate
argument_list|<
name|NodeMetadata
argument_list|>
name|predicate
init|=
operator|new
name|Predicate
argument_list|<
name|NodeMetadata
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|apply
parameter_list|(
name|NodeMetadata
name|metadata
parameter_list|)
block|{
if|if
condition|(
name|nodeId
operator|!=
literal|null
operator|&&
operator|!
name|nodeId
operator|.
name|equals
argument_list|(
name|metadata
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|imageId
operator|!=
literal|null
operator|&&
operator|!
name|imageId
operator|.
name|equals
argument_list|(
name|metadata
operator|.
name|getImageId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|queryState
operator|!=
literal|null
operator|&&
operator|!
name|queryState
operator|.
name|equals
argument_list|(
name|metadata
operator|.
name|getStatus
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|group
operator|!=
literal|null
operator|&&
operator|!
name|group
operator|.
name|equals
argument_list|(
name|metadata
operator|.
name|getGroup
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
return|return
name|predicate
return|;
block|}
comment|/**      * Retrieves the operation from the URI or from the exchange headers. The header will take precedence over the URI.      */
DECL|method|getOperation (Exchange exchange)
specifier|public
name|String
name|getOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|operation
init|=
name|getEndpoint
argument_list|()
operator|.
name|getOperation
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|OPERATION
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|operation
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|OPERATION
argument_list|)
expr_stmt|;
block|}
return|return
name|operation
return|;
block|}
comment|/**      * Retrieves the node state from the URI or from the exchange headers. The header will take precedence over the URI.      */
DECL|method|getNodeState (Exchange exchange)
specifier|public
name|NodeMetadata
operator|.
name|Status
name|getNodeState
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|NodeMetadata
operator|.
name|Status
name|nodeState
init|=
literal|null
decl_stmt|;
name|String
name|state
init|=
name|getEndpoint
argument_list|()
operator|.
name|getNodeState
argument_list|()
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|nodeState
operator|=
name|NodeMetadata
operator|.
name|Status
operator|.
name|valueOf
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|NODE_STATE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Object
name|stateHeader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|NODE_STATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|stateHeader
operator|==
literal|null
condition|)
block|{
name|nodeState
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|stateHeader
operator|instanceof
name|NodeMetadata
operator|.
name|Status
condition|)
block|{
name|nodeState
operator|=
operator|(
name|NodeMetadata
operator|.
name|Status
operator|)
name|stateHeader
expr_stmt|;
block|}
else|else
block|{
name|nodeState
operator|=
name|NodeMetadata
operator|.
name|Status
operator|.
name|valueOf
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|stateHeader
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|nodeState
return|;
block|}
comment|/**      * Retrieves the image id from the URI or from the exchange properties. The property will take precedence over the URI.      */
DECL|method|getImageId (Exchange exchange)
specifier|protected
name|String
name|getImageId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|imageId
init|=
name|getEndpoint
argument_list|()
operator|.
name|getImageId
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|IMAGE_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|imageId
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|IMAGE_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|imageId
return|;
block|}
comment|/**      * Retrieves the hardware id from the URI or from the exchange headers. The header will take precedence over the URI.      */
DECL|method|getHardwareId (Exchange exchange)
specifier|protected
name|String
name|getHardwareId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|hardwareId
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHardwareId
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|HARDWARE_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|hardwareId
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|HARDWARE_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|hardwareId
return|;
block|}
comment|/**      * Retrieves the location id from the URI or from the exchange headers. The header will take precedence over the URI.      */
DECL|method|getLocationId (Exchange exchange)
specifier|protected
name|String
name|getLocationId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|locationId
init|=
name|getEndpoint
argument_list|()
operator|.
name|getLocationId
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|LOCATION_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|locationId
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|LOCATION_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|locationId
return|;
block|}
comment|/**      * Retrieves the node id from the URI or from the exchange headers. The header will take precedence over the URI.      */
DECL|method|getNodeId (Exchange exchange)
specifier|protected
name|String
name|getNodeId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|nodeId
init|=
name|getEndpoint
argument_list|()
operator|.
name|getNodeId
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|NODE_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|nodeId
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|NODE_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|nodeId
return|;
block|}
comment|/**      * Retrieves the group from the URI or from the exchange headers. The header will take precedence over the URI.      */
DECL|method|getGroup (Exchange exchange)
specifier|protected
name|String
name|getGroup
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|group
init|=
name|getEndpoint
argument_list|()
operator|.
name|getGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|GROUP
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|group
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|GROUP
argument_list|)
expr_stmt|;
block|}
return|return
name|group
return|;
block|}
comment|/**      * Retrieves the user from the URI or from the exchange headers. The header will take precedence over the URI.      */
DECL|method|getUser (Exchange exchange)
specifier|protected
name|String
name|getUser
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|user
init|=
name|getEndpoint
argument_list|()
operator|.
name|getUser
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|USER
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|user
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|USER
argument_list|)
expr_stmt|;
block|}
return|return
name|user
return|;
block|}
block|}
end_class

end_unit

