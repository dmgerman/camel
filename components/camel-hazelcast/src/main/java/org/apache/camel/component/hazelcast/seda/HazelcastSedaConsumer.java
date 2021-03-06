begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|seda
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|BaseQueue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|transaction
operator|.
name|TransactionContext
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|Consumer
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
name|Endpoint
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
name|AsyncProcessorConverterHelper
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultExchangeHolder
import|;
end_import

begin_comment
comment|/**  * Implementation of Hazelcast SEDA {@link Consumer} component.  */
end_comment

begin_class
DECL|class|HazelcastSedaConsumer
specifier|public
class|class
name|HazelcastSedaConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|Runnable
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|HazelcastSedaEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|method|HazelcastSedaConsumer (final Endpoint endpoint, final Processor processor)
specifier|public
name|HazelcastSedaConsumer
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|Processor
name|processor
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
name|endpoint
operator|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
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
name|int
name|concurrentConsumers
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConcurrentConsumers
argument_list|()
decl_stmt|;
name|executor
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|concurrentConsumers
condition|;
name|i
operator|++
control|)
block|{
name|executor
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
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
name|executor
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|executor
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|BaseQueue
argument_list|<
name|?
argument_list|>
name|queue
init|=
name|endpoint
operator|.
name|getHazelcastInstance
argument_list|()
operator|.
name|getQueue
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|queue
operator|!=
literal|null
operator|&&
name|isRunAllowed
argument_list|()
condition|)
block|{
specifier|final
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
name|TransactionContext
name|transactionCtx
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTransacted
argument_list|()
condition|)
block|{
comment|// Get and begin transaction if exist
name|transactionCtx
operator|=
name|endpoint
operator|.
name|getHazelcastInstance
argument_list|()
operator|.
name|newTransactionContext
argument_list|()
expr_stmt|;
if|if
condition|(
name|transactionCtx
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Begin transaction: {}"
argument_list|,
name|transactionCtx
operator|.
name|getTxnId
argument_list|()
argument_list|)
expr_stmt|;
name|transactionCtx
operator|.
name|beginTransaction
argument_list|()
expr_stmt|;
name|queue
operator|=
name|transactionCtx
operator|.
name|getQueue
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|Object
name|body
init|=
name|queue
operator|.
name|poll
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|body
operator|instanceof
name|DefaultExchangeHolder
condition|)
block|{
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
operator|(
name|DefaultExchangeHolder
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// process using the asynchronous routing engine
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|asyncDone
parameter_list|)
block|{
comment|// noop
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Rollback
if|if
condition|(
name|transactionCtx
operator|!=
literal|null
condition|)
block|{
name|transactionCtx
operator|.
name|rollbackTransaction
argument_list|()
expr_stmt|;
block|}
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Hzlq Exception caught: {}"
argument_list|,
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// Rollback
if|if
condition|(
name|transactionCtx
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Rollback transaction: {}"
argument_list|,
name|transactionCtx
operator|.
name|getTxnId
argument_list|()
argument_list|)
expr_stmt|;
name|transactionCtx
operator|.
name|rollbackTransaction
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// It's OK, I commit
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
operator|&&
name|transactionCtx
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Commit transaction: {}"
argument_list|,
name|transactionCtx
operator|.
name|getTxnId
argument_list|()
argument_list|)
expr_stmt|;
name|transactionCtx
operator|.
name|commitTransaction
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Hzlq Consumer Interrupted: {}"
argument_list|,
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// Rollback
if|if
condition|(
name|transactionCtx
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Rollback transaction: {}"
argument_list|,
name|transactionCtx
operator|.
name|getTxnId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|transactionCtx
operator|.
name|rollbackTransaction
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ignore
parameter_list|)
block|{                     }
block|}
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOnErrorDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ignore
parameter_list|)
block|{                 }
block|}
block|}
block|}
block|}
end_class

end_unit

