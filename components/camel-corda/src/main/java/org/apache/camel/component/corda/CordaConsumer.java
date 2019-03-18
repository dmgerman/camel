begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|List
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
name|messaging
operator|.
name|DataFeed
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
name|FlowProgressHandle
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
name|StateMachineInfo
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
name|StateMachineTransactionMapping
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
name|StateMachineUpdate
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
name|NodeInfo
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
name|NetworkMapCache
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
name|Vault
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
name|support
operator|.
name|DefaultConsumer
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

begin_import
import|import
name|rx
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Subscription
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
name|NETWORK_MAP_FEED
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
name|START_TRACKED_FLOW_DYNAMIC
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
name|STATE_MACHINE_FEED
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
name|STATE_MACHINE_RECORDED_TRANSACTION_MAPPING_FEED
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
name|VAULT_TRACK
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
name|VAULT_TRACK_BY
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
name|VAULT_TRACK_BY_CRITERIA
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
name|VAULT_TRACK_BY_WITH_PAGING_SPEC
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
name|VAULT_TRACK_BY_WITH_SORTING
import|;
end_import

begin_comment
comment|/**  * The corda consumer.  */
end_comment

begin_class
DECL|class|CordaConsumer
specifier|public
class|class
name|CordaConsumer
extends|extends
name|DefaultConsumer
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
name|CordaConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|CordaConfiguration
name|configuration
decl_stmt|;
DECL|field|cordaRPCOps
specifier|private
name|CordaRPCOps
name|cordaRPCOps
decl_stmt|;
DECL|field|subscription
specifier|private
name|Subscription
name|subscription
decl_stmt|;
DECL|method|CordaConsumer (CordaEndpoint endpoint, Processor processor, CordaConfiguration configuration, CordaRPCOps cordaRPCOps)
specifier|public
name|CordaConsumer
parameter_list|(
name|CordaEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
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
name|processor
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
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|ContractState
argument_list|>
name|contractStateClass
init|=
name|configuration
operator|.
name|getContractStateClass
argument_list|()
decl_stmt|;
name|QueryCriteria
name|criteria
init|=
name|configuration
operator|.
name|getQueryCriteria
argument_list|()
decl_stmt|;
name|PageSpecification
name|pageSpec
init|=
name|configuration
operator|.
name|getPageSpecification
argument_list|()
decl_stmt|;
name|Sort
name|sorting
init|=
name|configuration
operator|.
name|getSort
argument_list|()
decl_stmt|;
name|DataFeed
argument_list|<
name|Vault
operator|.
name|Page
argument_list|<
name|ContractState
argument_list|>
argument_list|,
name|Vault
operator|.
name|Update
argument_list|<
name|ContractState
argument_list|>
argument_list|>
name|pageUpdateDataFeed
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|configuration
operator|.
name|getOperation
argument_list|()
condition|)
block|{
case|case
name|VAULT_TRACK
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|VAULT_TRACK
argument_list|)
expr_stmt|;
name|pageUpdateDataFeed
operator|=
name|cordaRPCOps
operator|.
name|vaultTrack
argument_list|(
name|contractStateClass
argument_list|)
expr_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|pageUpdateDataFeed
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|pageUpdateDataFeed
operator|.
name|getUpdates
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processContractStateUpdate
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|VAULT_TRACK
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|VAULT_TRACK
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|VAULT_TRACK_BY
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|VAULT_TRACK_BY
argument_list|)
expr_stmt|;
name|pageUpdateDataFeed
operator|=
name|cordaRPCOps
operator|.
name|vaultTrackBy
argument_list|(
name|criteria
argument_list|,
name|pageSpec
argument_list|,
name|sorting
argument_list|,
name|contractStateClass
argument_list|)
expr_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|pageUpdateDataFeed
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|pageUpdateDataFeed
operator|.
name|getUpdates
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processContractStateUpdate
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|VAULT_TRACK_BY
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|VAULT_TRACK_BY
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|VAULT_TRACK_BY_CRITERIA
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|VAULT_TRACK_BY_CRITERIA
argument_list|)
expr_stmt|;
name|pageUpdateDataFeed
operator|=
name|cordaRPCOps
operator|.
name|vaultTrackByCriteria
argument_list|(
name|contractStateClass
argument_list|,
name|criteria
argument_list|)
expr_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|pageUpdateDataFeed
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|pageUpdateDataFeed
operator|.
name|getUpdates
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processContractStateUpdate
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|VAULT_TRACK_BY_CRITERIA
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|VAULT_TRACK_BY_CRITERIA
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|VAULT_TRACK_BY_WITH_PAGING_SPEC
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|VAULT_TRACK_BY_WITH_PAGING_SPEC
argument_list|)
expr_stmt|;
name|pageUpdateDataFeed
operator|=
name|cordaRPCOps
operator|.
name|vaultTrackByWithPagingSpec
argument_list|(
name|contractStateClass
argument_list|,
name|criteria
argument_list|,
name|pageSpec
argument_list|)
expr_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|pageUpdateDataFeed
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|pageUpdateDataFeed
operator|.
name|getUpdates
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processContractStateUpdate
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|VAULT_TRACK_BY_WITH_PAGING_SPEC
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|VAULT_TRACK_BY_WITH_PAGING_SPEC
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|VAULT_TRACK_BY_WITH_SORTING
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|VAULT_TRACK_BY_WITH_SORTING
argument_list|)
expr_stmt|;
name|pageUpdateDataFeed
operator|=
name|cordaRPCOps
operator|.
name|vaultTrackByWithSorting
argument_list|(
name|contractStateClass
argument_list|,
name|criteria
argument_list|,
name|sorting
argument_list|)
expr_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|pageUpdateDataFeed
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|pageUpdateDataFeed
operator|.
name|getUpdates
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processContractStateUpdate
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|VAULT_TRACK_BY_WITH_SORTING
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|VAULT_TRACK_BY_WITH_SORTING
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|STATE_MACHINE_FEED
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|STATE_MACHINE_FEED
argument_list|)
expr_stmt|;
name|DataFeed
argument_list|<
name|List
argument_list|<
name|StateMachineInfo
argument_list|>
argument_list|,
name|StateMachineUpdate
argument_list|>
name|stateFeed
init|=
name|cordaRPCOps
operator|.
name|stateMachinesFeed
argument_list|()
decl_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|stateFeed
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|stateFeed
operator|.
name|getUpdates
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processStateMachineUpdate
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|STATE_MACHINE_FEED
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|STATE_MACHINE_FEED
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|NETWORK_MAP_FEED
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|NETWORK_MAP_FEED
argument_list|)
expr_stmt|;
name|DataFeed
argument_list|<
name|List
argument_list|<
name|NodeInfo
argument_list|>
argument_list|,
name|NetworkMapCache
operator|.
name|MapChange
argument_list|>
name|networkMapFeed
init|=
name|cordaRPCOps
operator|.
name|networkMapFeed
argument_list|()
decl_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|networkMapFeed
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|networkMapFeed
operator|.
name|getUpdates
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|proceedNetworkMapFeed
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|NETWORK_MAP_FEED
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|NETWORK_MAP_FEED
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|STATE_MACHINE_RECORDED_TRANSACTION_MAPPING_FEED
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|STATE_MACHINE_RECORDED_TRANSACTION_MAPPING_FEED
argument_list|)
expr_stmt|;
name|DataFeed
argument_list|<
name|List
argument_list|<
name|StateMachineTransactionMapping
argument_list|>
argument_list|,
name|StateMachineTransactionMapping
argument_list|>
name|transactionFeed
init|=
name|cordaRPCOps
operator|.
name|stateMachineRecordedTransactionMappingFeed
argument_list|()
decl_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|transactionFeed
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|transactionFeed
operator|.
name|getUpdates
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processTransactionMappingFeed
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|STATE_MACHINE_RECORDED_TRANSACTION_MAPPING_FEED
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|STATE_MACHINE_RECORDED_TRANSACTION_MAPPING_FEED
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|START_TRACKED_FLOW_DYNAMIC
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"subscribing for operation: "
operator|+
name|START_TRACKED_FLOW_DYNAMIC
argument_list|)
expr_stmt|;
name|FlowProgressHandle
argument_list|<
name|Object
argument_list|>
name|objectFlowProgressHandle
init|=
name|cordaRPCOps
operator|.
name|startTrackedFlowDynamic
argument_list|(
name|configuration
operator|.
name|getFlowLociClass
argument_list|()
argument_list|,
name|configuration
operator|.
name|getArguments
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|objectFlowProgressHandle
operator|.
name|getReturnValue
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|Observable
argument_list|<
name|String
argument_list|>
name|progress
init|=
name|objectFlowProgressHandle
operator|.
name|getProgress
argument_list|()
decl_stmt|;
name|processSnapshot
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|subscription
operator|=
name|progress
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processFlowProcess
argument_list|(
name|x
argument_list|)
argument_list|,
name|t
lambda|->
name|processError
argument_list|(
name|t
argument_list|,
name|CordaConstants
operator|.
name|START_TRACKED_FLOW_DYNAMIC
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|CordaConstants
operator|.
name|START_TRACKED_FLOW_DYNAMIC
argument_list|)
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
name|configuration
operator|.
name|getOperation
argument_list|()
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Subscribed: {}"
argument_list|,
name|this
operator|.
name|configuration
argument_list|)
expr_stmt|;
block|}
DECL|method|processSnapshot (Exchange exchange, Object page)
specifier|private
name|void
name|processSnapshot
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|page
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isProcessSnapshot
argument_list|()
condition|)
block|{
try|try
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error processing snapshot"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|processFlowProcess (String x)
specifier|private
name|void
name|processFlowProcess
parameter_list|(
name|String
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processFlowProcess {}"
argument_list|,
name|x
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|x
argument_list|)
expr_stmt|;
name|processEvent
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|processTransactionMappingFeed (StateMachineTransactionMapping x)
specifier|private
name|void
name|processTransactionMappingFeed
parameter_list|(
name|StateMachineTransactionMapping
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processTransactionMappingFeed {}"
argument_list|,
name|x
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|x
argument_list|)
expr_stmt|;
name|processEvent
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|proceedNetworkMapFeed (NetworkMapCache.MapChange x)
specifier|private
name|void
name|proceedNetworkMapFeed
parameter_list|(
name|NetworkMapCache
operator|.
name|MapChange
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"proceedNetworkMapFeed {}"
argument_list|,
name|x
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|x
argument_list|)
expr_stmt|;
name|processEvent
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|processStateMachineUpdate (StateMachineUpdate x)
specifier|private
name|void
name|processStateMachineUpdate
parameter_list|(
name|StateMachineUpdate
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processStateMachineUpdate {}"
argument_list|,
name|x
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|x
argument_list|)
expr_stmt|;
name|processEvent
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|processContractStateUpdate (Vault.Update<ContractState> x)
specifier|private
name|void
name|processContractStateUpdate
parameter_list|(
name|Vault
operator|.
name|Update
argument_list|<
name|ContractState
argument_list|>
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processContractStateUpdate {}"
argument_list|,
name|x
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|x
argument_list|)
expr_stmt|;
name|processEvent
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|processError (Throwable throwable, String operation)
specifier|private
name|void
name|processError
parameter_list|(
name|Throwable
name|throwable
parameter_list|,
name|String
name|operation
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processError for operation: "
operator|+
name|operation
operator|+
literal|" "
operator|+
name|throwable
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
name|processEvent
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|processEvent (Exchange exchange)
specifier|public
name|void
name|processEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processEvent {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error processing event "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processDone (String operation)
specifier|private
name|void
name|processDone
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processDone for operation: {}"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|subscription
operator|!=
literal|null
condition|)
block|{
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

