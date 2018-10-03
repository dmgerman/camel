begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.web3j
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|web3j
package|;
end_package

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
name|org
operator|.
name|web3j
operator|.
name|protocol
operator|.
name|Web3j
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|protocol
operator|.
name|core
operator|.
name|methods
operator|.
name|request
operator|.
name|EthFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|protocol
operator|.
name|core
operator|.
name|methods
operator|.
name|response
operator|.
name|EthBlock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|protocol
operator|.
name|core
operator|.
name|methods
operator|.
name|response
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|protocol
operator|.
name|core
operator|.
name|methods
operator|.
name|response
operator|.
name|Transaction
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Subscription
import|;
end_import

begin_comment
comment|/**  * The web3j consumer.  */
end_comment

begin_class
DECL|class|Web3jConsumer
specifier|public
class|class
name|Web3jConsumer
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
name|Web3jConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|web3j
specifier|private
specifier|final
name|Web3j
name|web3j
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|Web3jConfiguration
name|configuration
decl_stmt|;
DECL|field|subscription
specifier|private
name|Subscription
name|subscription
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Web3jEndpoint
name|endpoint
decl_stmt|;
DECL|method|Web3jConsumer (Web3jEndpoint endpoint, Processor processor, Web3jConfiguration configuration)
specifier|public
name|Web3jConsumer
parameter_list|(
name|Web3jEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Web3jConfiguration
name|configuration
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
name|web3j
operator|=
name|endpoint
operator|.
name|getWeb3j
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|Web3jEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|Web3jEndpoint
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Subscribing to: {}"
argument_list|,
name|endpoint
operator|.
name|getNodeAddress
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|configuration
operator|.
name|getOperation
argument_list|()
condition|)
block|{
case|case
name|Web3jConstants
operator|.
name|ETH_LOG_OBSERVABLE
case|:
name|EthFilter
name|ethFilter
init|=
name|Web3jEndpoint
operator|.
name|buildEthFilter
argument_list|(
name|configuration
operator|.
name|getFromBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|getToBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|getAddresses
argument_list|()
argument_list|,
name|configuration
operator|.
name|getTopics
argument_list|()
argument_list|)
decl_stmt|;
name|subscription
operator|=
name|web3j
operator|.
name|ethLogObservable
argument_list|(
name|ethFilter
argument_list|)
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|ethLogObservable
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
name|Web3jConstants
operator|.
name|ETH_LOG_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|ETH_LOG_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|ETH_BLOCK_HASH_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|ethBlockHashObservable
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|ethBlockHashObservable
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
name|Web3jConstants
operator|.
name|ETH_BLOCK_HASH_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|ETH_BLOCK_HASH_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|ETH_PENDING_TRANSACTION_HASH_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|ethPendingTransactionHashObservable
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|ethPendingTransactionHashObservable
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
name|Web3jConstants
operator|.
name|ETH_PENDING_TRANSACTION_HASH_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|ETH_PENDING_TRANSACTION_HASH_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|TRANSACTION_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|transactionObservable
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processTransaction
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
name|Web3jConstants
operator|.
name|TRANSACTION_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|TRANSACTION_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|PENDING_TRANSACTION_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|pendingTransactionObservable
argument_list|()
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processTransaction
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
name|Web3jConstants
operator|.
name|PENDING_TRANSACTION_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|PENDING_TRANSACTION_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|BLOCK_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|blockObservable
argument_list|(
name|configuration
operator|.
name|isFullTransactionObjects
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|blockObservable
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
name|Web3jConstants
operator|.
name|BLOCK_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|BLOCK_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|REPLAY_BLOCKS_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|replayBlocksObservable
argument_list|(
name|configuration
operator|.
name|getFromBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|getToBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|isFullTransactionObjects
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|blockObservable
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
name|Web3jConstants
operator|.
name|REPLAY_BLOCKS_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|REPLAY_BLOCKS_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|REPLAY_TRANSACTIONS_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|replayTransactionsObservable
argument_list|(
name|configuration
operator|.
name|getFromBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|getToBlock
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processTransaction
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
name|Web3jConstants
operator|.
name|REPLAY_TRANSACTIONS_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|REPLAY_TRANSACTIONS_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_BLOCK_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|catchUpToLatestBlockObservable
argument_list|(
name|configuration
operator|.
name|getFromBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|isFullTransactionObjects
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|blockObservable
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
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_BLOCK_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_BLOCK_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_TRANSACTION_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|catchUpToLatestTransactionObservable
argument_list|(
name|configuration
operator|.
name|getFromBlock
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processTransaction
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
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_TRANSACTION_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_TRANSACTION_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_AND_SUBSCRIBE_TO_NEW_BLOCKS_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|catchUpToLatestAndSubscribeToNewBlocksObservable
argument_list|(
name|configuration
operator|.
name|getFromBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|isFullTransactionObjects
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|blockObservable
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
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_AND_SUBSCRIBE_TO_NEW_BLOCKS_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_AND_SUBSCRIBE_TO_NEW_BLOCKS_OBSERVABLE
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_AND_SUBSCRIBE_TO_NEW_TRANSACTIONS_OBSERVABLE
case|:
name|subscription
operator|=
name|web3j
operator|.
name|catchUpToLatestAndSubscribeToNewTransactionsObservable
argument_list|(
name|configuration
operator|.
name|getFromBlock
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
name|x
lambda|->
name|processTransaction
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
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_AND_SUBSCRIBE_TO_NEW_TRANSACTIONS_OBSERVABLE
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|processDone
argument_list|(
name|Web3jConstants
operator|.
name|CATCH_UP_TO_LATEST_AND_SUBSCRIBE_TO_NEW_TRANSACTIONS_OBSERVABLE
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
DECL|method|buildEthFilter ()
specifier|private
name|EthFilter
name|buildEthFilter
parameter_list|()
block|{
name|EthFilter
name|ethFilter
init|=
operator|new
name|EthFilter
argument_list|(
name|configuration
operator|.
name|getFromBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|getToBlock
argument_list|()
argument_list|,
name|configuration
operator|.
name|getAddresses
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getTopics
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|topic
range|:
name|configuration
operator|.
name|getTopics
argument_list|()
control|)
block|{
if|if
condition|(
name|topic
operator|!=
literal|null
operator|&&
name|topic
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ethFilter
operator|.
name|addSingleTopic
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ethFilter
operator|.
name|addNullTopic
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|ethFilter
return|;
block|}
DECL|method|ethBlockHashObservable (String x)
specifier|private
name|void
name|ethBlockHashObservable
parameter_list|(
name|String
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processEthBlock {}"
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
DECL|method|ethPendingTransactionHashObservable (String x)
specifier|private
name|void
name|ethPendingTransactionHashObservable
parameter_list|(
name|String
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processEthBlock {}"
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
DECL|method|blockObservable (EthBlock x)
specifier|private
name|void
name|blockObservable
parameter_list|(
name|EthBlock
name|x
parameter_list|)
block|{
name|EthBlock
operator|.
name|Block
name|block
init|=
name|x
operator|.
name|getBlock
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"processEthBlock {}"
argument_list|,
name|block
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
name|block
argument_list|)
expr_stmt|;
name|processEvent
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|processTransaction (Transaction x)
specifier|private
name|void
name|processTransaction
parameter_list|(
name|Transaction
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processTransaction {}"
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
DECL|method|ethLogObservable (Log x)
specifier|private
name|void
name|ethLogObservable
parameter_list|(
name|Log
name|x
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processLogObservable {}"
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
name|setHeader
argument_list|(
literal|"status"
argument_list|,
literal|"done"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"operation"
argument_list|,
name|operation
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

