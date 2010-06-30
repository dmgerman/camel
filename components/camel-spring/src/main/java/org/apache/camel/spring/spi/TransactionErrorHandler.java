begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|spi
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
name|CountDownLatch
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
name|CamelContext
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
name|processor
operator|.
name|Logger
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
name|processor
operator|.
name|RedeliveryErrorHandler
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
name|processor
operator|.
name|RedeliveryPolicy
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
name|processor
operator|.
name|exceptionpolicy
operator|.
name|ExceptionPolicyStrategy
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
name|springframework
operator|.
name|transaction
operator|.
name|TransactionDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionCallbackWithoutResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/transactional-client.html">Transactional Client</a>  * EIP pattern.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|TransactionErrorHandler
specifier|public
class|class
name|TransactionErrorHandler
extends|extends
name|RedeliveryErrorHandler
block|{
DECL|field|transactionTemplate
specifier|private
specifier|final
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
comment|/**      * Creates the transaction error handler.      *      * @param camelContext            the camel context      * @param output                  outer processor that should use this default error handler      * @param logger                  logger to use for logging failures and redelivery attempts      * @param redeliveryProcessor     an optional processor to run before redelivery attempt      * @param redeliveryPolicy        policy for redelivery      * @param handledPolicy           policy for handling failed exception that are moved to the dead letter queue      * @param exceptionPolicyStrategy strategy for onException handling      * @param transactionTemplate     the transaction template      */
DECL|method|TransactionErrorHandler (CamelContext camelContext, Processor output, Logger logger, Processor redeliveryProcessor, RedeliveryPolicy redeliveryPolicy, Predicate handledPolicy, ExceptionPolicyStrategy exceptionPolicyStrategy, TransactionTemplate transactionTemplate)
specifier|public
name|TransactionErrorHandler
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|output
parameter_list|,
name|Logger
name|logger
parameter_list|,
name|Processor
name|redeliveryProcessor
parameter_list|,
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|,
name|Predicate
name|handledPolicy
parameter_list|,
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|output
argument_list|,
name|logger
argument_list|,
name|redeliveryProcessor
argument_list|,
name|redeliveryPolicy
argument_list|,
name|handledPolicy
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|setExceptionPolicy
argument_list|(
name|exceptionPolicyStrategy
argument_list|)
expr_stmt|;
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
block|}
DECL|method|supportTransacted ()
specifier|public
name|boolean
name|supportTransacted
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|output
operator|==
literal|null
condition|)
block|{
comment|// if no output then don't do any description
return|return
literal|""
return|;
block|}
return|return
literal|"TransactionErrorHandler:"
operator|+
name|propagationBehaviorToString
argument_list|(
name|transactionTemplate
operator|.
name|getPropagationBehavior
argument_list|()
argument_list|)
operator|+
literal|"["
operator|+
name|getOutput
argument_list|()
operator|+
literal|"]"
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
comment|// we have to run this synchronously as Spring Transaction does *not* support
comment|// using multiple threads to span a transaction
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|isTransactedBy
argument_list|(
name|transactionTemplate
argument_list|)
condition|)
block|{
comment|// already transacted by this transaction template
comment|// so lets just let the error handler process it
name|processByErrorHandler
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// not yet wrapped in transaction so lets do that
comment|// and then have it invoke the error handler from within that transaction
name|processInTransaction
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// invoke ths synchronous method as Spring Transaction does *not* support
comment|// using multiple threads to span a transaction
try|try
block|{
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// notify callback we are done synchronously
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|processInTransaction (final Exchange exchange)
specifier|protected
name|void
name|processInTransaction
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|transactionTemplate
argument_list|)
decl_stmt|;
try|try
block|{
comment|// mark the beginning of this transaction boundary
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|beginTransactedBy
argument_list|(
name|transactionTemplate
argument_list|)
expr_stmt|;
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
literal|"Transaction begin ("
operator|+
name|id
operator|+
literal|") for ExchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|doInTransactionTemplate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
literal|"Transaction commit ("
operator|+
name|id
operator|+
literal|") for ExchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|TransactionRollbackException
name|e
parameter_list|)
block|{
comment|// ignore as its just a dummy exception to force spring TX to rollback
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
literal|"Transaction rollback ("
operator|+
name|id
operator|+
literal|") for ExchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
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
name|warn
argument_list|(
literal|"Transaction rollback ("
operator|+
name|id
operator|+
literal|") for ExchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" due exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// mark the end of this transaction boundary
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|endTransactedBy
argument_list|(
name|transactionTemplate
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doInTransactionTemplate (final Exchange exchange)
specifier|protected
name|void
name|doInTransactionTemplate
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// spring transaction template is working best with rollback if you throw it a runtime exception
comment|// otherwise it may not rollback messages send to JMS queues etc.
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallbackWithoutResult
argument_list|()
block|{
specifier|protected
name|void
name|doInTransactionWithoutResult
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
comment|// wrapper exception to throw if the exchange failed
comment|// IMPORTANT: Must be a runtime exception to let Spring regard it as to do "rollback"
name|RuntimeException
name|rce
init|=
literal|null
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TRANSACTED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// and now let process the exchange by the error handler
name|processByErrorHandler
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// after handling and still an exception or marked as rollback only then rollback
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
operator|||
name|exchange
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
comment|// if it was a local rollback only then remove its marker so outer transaction
comment|// wont rollback as well (Note: isRollbackOnly() also returns true for ROLLBACK_ONLY_LAST)
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY_LAST
argument_list|)
expr_stmt|;
comment|// wrap exception in transacted exception
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
name|rce
operator|=
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
comment|// create dummy exception to force spring transaction manager to rollback
name|rce
operator|=
operator|new
name|TransactionRollbackException
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|status
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
name|status
operator|.
name|setRollbackOnly
argument_list|()
expr_stmt|;
block|}
comment|// rethrow if an exception occurred
if|if
condition|(
name|rce
operator|!=
literal|null
condition|)
block|{
throw|throw
name|rce
throw|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Processes the {@link Exchange} using the error handler.      *<p/>      * This implementation will invoke ensure this occurs synchronously, that means if the async routing engine      * did kick in, then this implementation will wait for the task to complete before it continues.      *      * @param exchange the exchange      */
DECL|method|processByErrorHandler (final Exchange exchange)
specifier|protected
name|void
name|processByErrorHandler
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|boolean
name|sync
init|=
name|super
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
name|doneSync
parameter_list|)
block|{
if|if
condition|(
operator|!
name|doneSync
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Asynchronous callback received for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Done TransactionErrorHandler"
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Waiting for asynchronous callback before continuing for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" -> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
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
literal|"Interrupted while waiting for asynchronous callback for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// we may be shutting down etc., so set exception
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Asynchronous callback received, will continue routing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" -> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|propagationBehaviorToString (int propagationBehavior)
specifier|private
specifier|static
name|String
name|propagationBehaviorToString
parameter_list|(
name|int
name|propagationBehavior
parameter_list|)
block|{
name|String
name|rc
decl_stmt|;
switch|switch
condition|(
name|propagationBehavior
condition|)
block|{
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_MANDATORY
case|:
name|rc
operator|=
literal|"PROPAGATION_MANDATORY"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NESTED
case|:
name|rc
operator|=
literal|"PROPAGATION_NESTED"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NEVER
case|:
name|rc
operator|=
literal|"PROPAGATION_NEVER"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NOT_SUPPORTED
case|:
name|rc
operator|=
literal|"PROPAGATION_NOT_SUPPORTED"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRED
case|:
name|rc
operator|=
literal|"PROPAGATION_REQUIRED"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRES_NEW
case|:
name|rc
operator|=
literal|"PROPAGATION_REQUIRES_NEW"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_SUPPORTS
case|:
name|rc
operator|=
literal|"PROPAGATION_SUPPORTS"
expr_stmt|;
break|break;
default|default:
name|rc
operator|=
literal|"UNKNOWN"
expr_stmt|;
block|}
return|return
name|rc
return|;
block|}
block|}
end_class

end_unit

