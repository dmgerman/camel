begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.corda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|contracts
operator|.
name|ContractState
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|crypto
operator|.
name|SecureHash
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|flows
operator|.
name|FlowLogic
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|identity
operator|.
name|AbstractParty
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|identity
operator|.
name|CordaX500Name
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|messaging
operator|.
name|CordaRPCOps
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|AttachmentQueryCriteria
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|AttachmentSort
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|PageSpecification
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|QueryCriteria
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|Sort
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
name|InvokeOnHeader
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
name|support
operator|.
name|HeaderSelectorProducer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
operator|.
name|CordaConstants
operator|.
name|ARGUMENTS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
operator|.
name|CordaConstants
operator|.
name|ATTACHMENT_QUERY_CRITERIA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
operator|.
name|CordaConstants
operator|.
name|DRAINING_MODE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
operator|.
name|CordaConstants
operator|.
name|EXACT_MATCH
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
operator|.
name|CordaConstants
operator|.
name|PAGE_SPECIFICATION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
operator|.
name|CordaConstants
operator|.
name|QUERY_CRITERIA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
operator|.
name|CordaConstants
operator|.
name|SECURE_HASH
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
operator|.
name|CordaConstants
operator|.
name|SORT
import|;
end_import

begin_comment
comment|/**  * The corda producer.  */
end_comment

begin_class
DECL|class|CordaProducer
specifier|public
class|class
name|CordaProducer
extends|extends
name|HeaderSelectorProducer
block|{
DECL|field|configuration
specifier|private
name|CordaConfiguration
name|configuration
decl_stmt|;
DECL|field|cordaRPCOps
specifier|private
name|CordaRPCOps
name|cordaRPCOps
decl_stmt|;
DECL|method|CordaProducer (CordaEndpoint endpoint, final CordaConfiguration configuration, CordaRPCOps cordaRPCOps)
specifier|public
name|CordaProducer
parameter_list|(
name|CordaEndpoint
name|endpoint
parameter_list|,
specifier|final
name|CordaConfiguration
name|configuration
parameter_list|,
name|CordaRPCOps
name|cordaRPCOps
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|CordaConstants
operator|.
name|OPERATION
argument_list|,
parameter_list|()
lambda|->
name|configuration
operator|.
name|getOperation
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|cordaRPCOps
operator|=
name|cordaRPCOps
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|CordaEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|CordaEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|CURRENT_NODE_TIME
argument_list|)
DECL|method|currentNodeTime (Message message)
name|void
name|currentNodeTime
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|currentNodeTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|GET_PROTOCOL_VERSION
argument_list|)
DECL|method|getProtocolVersion (Message message)
name|void
name|getProtocolVersion
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|getProtocolVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|NETWORK_MAP_SNAPSHOT
argument_list|)
DECL|method|networkMapSnapshot (Message message)
name|void
name|networkMapSnapshot
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|networkMapSnapshot
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|STATE_MACHINE_SNAPSHOT
argument_list|)
DECL|method|stateMachinesSnapshot (Message message)
name|void
name|stateMachinesSnapshot
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|stateMachinesSnapshot
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|STATE_MACHINE_RECORDED_TRANSACTION_MAPPING_SNAPSHOT
argument_list|)
DECL|method|stateMachineRecordedTransactionMappingSnapshot (Message message)
name|void
name|stateMachineRecordedTransactionMappingSnapshot
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|stateMachineRecordedTransactionMappingSnapshot
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|REGISTERED_FLOWS
argument_list|)
DECL|method|registeredFlows (Message message)
name|void
name|registeredFlows
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|registeredFlows
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|CLEAR_NETWORK_MAP_CACHE
argument_list|)
DECL|method|clearNetworkMapCache (Message message)
name|void
name|clearNetworkMapCache
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|cordaRPCOps
operator|.
name|clearNetworkMapCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|IS_FLOWS_DRAINING_MODE_ENABLED
argument_list|)
DECL|method|isFlowsDrainingModeEnabled (Message message)
name|void
name|isFlowsDrainingModeEnabled
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|isFlowsDrainingModeEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|SET_FLOWS_DRAINING_MODE_ENABLED
argument_list|)
DECL|method|setFlowsDrainingModeEnabled (Message message)
name|void
name|setFlowsDrainingModeEnabled
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Boolean
name|mode
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|DRAINING_MODE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|cordaRPCOps
operator|.
name|setFlowsDrainingModeEnabled
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|NOTARY_IDENTITIES
argument_list|)
DECL|method|notaryIdentities (Message message)
name|void
name|notaryIdentities
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|notaryIdentities
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|NODE_INFO
argument_list|)
DECL|method|nodeInfo (Message message)
name|void
name|nodeInfo
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|nodeInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|ADD_VAULT_TRANSACTION_NOTE
argument_list|)
DECL|method|addVaultTransactionNote (Message message)
name|void
name|addVaultTransactionNote
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|SecureHash
name|secureHash
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SECURE_HASH
argument_list|,
name|SecureHash
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|note
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|cordaRPCOps
operator|.
name|addVaultTransactionNote
argument_list|(
name|secureHash
argument_list|,
name|note
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|GET_VAULT_TRANSACTION_NOTES
argument_list|)
DECL|method|getVaultTransactionNotes (Message message)
name|void
name|getVaultTransactionNotes
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|SecureHash
name|secureHash
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SECURE_HASH
argument_list|,
name|SecureHash
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|getVaultTransactionNotes
argument_list|(
name|secureHash
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|UPLOAD_ATTACHMENT
argument_list|)
DECL|method|uploadAttachment (Message message)
name|void
name|uploadAttachment
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|inputStream
init|=
name|message
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|SecureHash
name|secureHash
init|=
name|cordaRPCOps
operator|.
name|uploadAttachment
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SECURE_HASH
argument_list|,
name|secureHash
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|ATTACHMENT_EXISTS
argument_list|)
DECL|method|attachmentExists (Message message)
name|void
name|attachmentExists
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|SecureHash
name|secureHash
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SECURE_HASH
argument_list|,
name|SecureHash
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|attachmentExists
argument_list|(
name|secureHash
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|OPEN_ATTACHMENT
argument_list|)
DECL|method|openAttachment (Message message)
name|void
name|openAttachment
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|SecureHash
name|secureHash
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SECURE_HASH
argument_list|,
name|SecureHash
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|openAttachment
argument_list|(
name|secureHash
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|QUERY_ATTACHMENTS
argument_list|)
DECL|method|queryAttachments (Message message)
name|void
name|queryAttachments
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|AttachmentQueryCriteria
name|queryCriteria
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ATTACHMENT_QUERY_CRITERIA
argument_list|,
name|AttachmentQueryCriteria
operator|.
name|class
argument_list|)
decl_stmt|;
name|AttachmentSort
name|attachmentSort
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SORT
argument_list|,
name|AttachmentSort
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|queryAttachments
argument_list|(
name|queryCriteria
argument_list|,
name|attachmentSort
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|NODE_INFO_FROM_PARTY
argument_list|)
DECL|method|nodeInfoFromParty (Message message)
name|void
name|nodeInfoFromParty
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|AbstractParty
name|party
init|=
name|message
operator|.
name|getBody
argument_list|(
name|AbstractParty
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|nodeInfoFromParty
argument_list|(
name|party
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|NOTARY_PARTY_FROM_X500_NAME
argument_list|)
DECL|method|notaryPartyFromX500Name (Message message)
name|void
name|notaryPartyFromX500Name
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|CordaX500Name
name|x500Name
init|=
name|message
operator|.
name|getBody
argument_list|(
name|CordaX500Name
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|notaryPartyFromX500Name
argument_list|(
name|x500Name
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|PARTIES_FROM_NAME
argument_list|)
DECL|method|partiesFromName (Message message)
name|void
name|partiesFromName
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|query
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|exactMatch
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|EXACT_MATCH
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|partiesFromName
argument_list|(
name|query
argument_list|,
name|exactMatch
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|PARTIES_FROM_KEY
argument_list|)
DECL|method|partyFromKey (Message message)
name|void
name|partyFromKey
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|PublicKey
name|key
init|=
name|message
operator|.
name|getBody
argument_list|(
name|PublicKey
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|partyFromKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|WELL_KNOWN_PARTY_FROM_X500_NAME
argument_list|)
DECL|method|wellKnownPartyFromX500Name (Message message)
name|void
name|wellKnownPartyFromX500Name
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|CordaX500Name
name|x500Name
init|=
name|message
operator|.
name|getBody
argument_list|(
name|CordaX500Name
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|wellKnownPartyFromX500Name
argument_list|(
name|x500Name
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|WELL_KNOWN_PARTY_FROM_ANONYMOUS
argument_list|)
DECL|method|wellKnownPartyFromAnonymous (Message message)
name|void
name|wellKnownPartyFromAnonymous
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|AbstractParty
name|party
init|=
name|message
operator|.
name|getBody
argument_list|(
name|AbstractParty
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|wellKnownPartyFromAnonymous
argument_list|(
name|party
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|START_FLOW_DYNAMIC
argument_list|)
DECL|method|startFlowDynamic (Message message)
name|void
name|startFlowDynamic
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
index|[]
name|args
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ARGUMENTS
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|FlowLogic
argument_list|<
name|?
argument_list|>
argument_list|>
name|aClass
init|=
name|message
operator|.
name|getBody
argument_list|(
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|startFlowDynamic
argument_list|(
name|aClass
argument_list|,
name|args
argument_list|)
operator|.
name|getReturnValue
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|VAULT_QUERY
argument_list|)
DECL|method|vaultQuery (Message message)
name|void
name|vaultQuery
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|ContractState
argument_list|>
name|contractStateClass
init|=
operator|(
name|Class
argument_list|<
name|ContractState
argument_list|>
operator|)
name|message
operator|.
name|getBody
argument_list|(
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|vaultQuery
argument_list|(
name|contractStateClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|VAULT_QUERY_BY
argument_list|)
DECL|method|vaultQueryBy (Message message)
name|void
name|vaultQueryBy
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|ContractState
argument_list|>
name|contractStateClass
init|=
operator|(
name|Class
argument_list|<
name|ContractState
argument_list|>
operator|)
name|message
operator|.
name|getBody
argument_list|(
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|QueryCriteria
name|criteria
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|QUERY_CRITERIA
argument_list|,
name|QueryCriteria
operator|.
name|class
argument_list|)
decl_stmt|;
name|PageSpecification
name|pageSpec
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|PAGE_SPECIFICATION
argument_list|,
name|PageSpecification
operator|.
name|class
argument_list|)
decl_stmt|;
name|Sort
name|sorting
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SORT
argument_list|,
name|Sort
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|vaultQueryBy
argument_list|(
name|criteria
argument_list|,
name|pageSpec
argument_list|,
name|sorting
argument_list|,
name|contractStateClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|VAULT_QUERY_BY_CRITERIA
argument_list|)
DECL|method|vaultQueryByCriteria (Message message)
name|void
name|vaultQueryByCriteria
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|ContractState
argument_list|>
name|contractStateClass
init|=
operator|(
name|Class
argument_list|<
name|ContractState
argument_list|>
operator|)
name|message
operator|.
name|getBody
argument_list|(
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|QueryCriteria
name|criteria
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|QUERY_CRITERIA
argument_list|,
name|QueryCriteria
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|vaultQueryByCriteria
argument_list|(
name|criteria
argument_list|,
name|contractStateClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|VAULT_QUERY_BY_WITH_PAGING_SPEC
argument_list|)
DECL|method|vaultQueryByWithPagingSpec (Message message)
name|void
name|vaultQueryByWithPagingSpec
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|ContractState
argument_list|>
name|contractStateClass
init|=
operator|(
name|Class
argument_list|<
name|ContractState
argument_list|>
operator|)
name|message
operator|.
name|getBody
argument_list|(
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|QueryCriteria
name|criteria
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|QUERY_CRITERIA
argument_list|,
name|QueryCriteria
operator|.
name|class
argument_list|)
decl_stmt|;
name|PageSpecification
name|pageSpec
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|PAGE_SPECIFICATION
argument_list|,
name|PageSpecification
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|vaultQueryByWithPagingSpec
argument_list|(
name|contractStateClass
argument_list|,
name|criteria
argument_list|,
name|pageSpec
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CordaConstants
operator|.
name|VAULT_QUERY_BY_WITH_SORTING
argument_list|)
DECL|method|vaultQueryByWithSorting (Message message)
name|void
name|vaultQueryByWithSorting
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|ContractState
argument_list|>
name|contractStateClass
init|=
operator|(
name|Class
argument_list|<
name|ContractState
argument_list|>
operator|)
name|message
operator|.
name|getBody
argument_list|(
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|QueryCriteria
name|criteria
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|QUERY_CRITERIA
argument_list|,
name|QueryCriteria
operator|.
name|class
argument_list|)
decl_stmt|;
name|Sort
name|sorting
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SORT
argument_list|,
name|Sort
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|cordaRPCOps
operator|.
name|vaultQueryByWithSorting
argument_list|(
name|contractStateClass
argument_list|,
name|criteria
argument_list|,
name|sorting
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

