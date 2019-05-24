begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.transaction
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|transaction
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|concurrent
operator|.
name|CompletableFuture
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
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|TransactionRolledbackException
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
name|LoggingLevel
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
name|Navigate
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
name|RuntimeCamelException
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
name|errorhandler
operator|.
name|ErrorHandlerSupport
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
name|errorhandler
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
name|spi
operator|.
name|ShutdownPrepared
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
name|AsyncCallbackToCompletableFutureAdapter
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
name|ExchangeHelper
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
name|service
operator|.
name|ServiceHelper
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

begin_comment
comment|/**  * Does transactional execution according given policy. This class is based on  * {@link org.apache.camel.spring.spi.TransactionErrorHandler} excluding  * redelivery functionality. In the Spring implementation redelivering is done  * within the transaction which is not appropriate in JTA since every error  * breaks the current transaction.  */
end_comment

begin_class
DECL|class|TransactionErrorHandler
specifier|public
class|class
name|TransactionErrorHandler
extends|extends
name|ErrorHandlerSupport
implements|implements
name|AsyncProcessor
implements|,
name|ShutdownPrepared
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
block|{
DECL|field|output
specifier|protected
specifier|final
name|Processor
name|output
decl_stmt|;
DECL|field|preparingShutdown
specifier|protected
specifier|volatile
name|boolean
name|preparingShutdown
decl_stmt|;
DECL|field|exceptionPolicy
specifier|private
name|ExceptionPolicyStrategy
name|exceptionPolicy
decl_stmt|;
DECL|field|transactionPolicy
specifier|private
name|JtaTransactionPolicy
name|transactionPolicy
decl_stmt|;
DECL|field|transactionKey
specifier|private
specifier|final
name|String
name|transactionKey
decl_stmt|;
DECL|field|rollbackLoggingLevel
specifier|private
specifier|final
name|LoggingLevel
name|rollbackLoggingLevel
decl_stmt|;
comment|/**      * Creates the transaction error handler.      *      * @param camelContext      *            the camel context      * @param output      *            outer processor that should use this default error handler      * @param exceptionPolicyStrategy      *            strategy for onException handling      * @param transactionPolicy      *            the transaction policy      * @param executorService      *            the {@link java.util.concurrent.ScheduledExecutorService} to      *            be used for redelivery thread pool. Can be<tt>null</tt>.      * @param rollbackLoggingLevel      *            logging level to use for logging transaction rollback occurred      */
DECL|method|TransactionErrorHandler (CamelContext camelContext, Processor output, ExceptionPolicyStrategy exceptionPolicyStrategy, JtaTransactionPolicy transactionPolicy, ScheduledExecutorService executorService, LoggingLevel rollbackLoggingLevel)
specifier|public
name|TransactionErrorHandler
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|output
parameter_list|,
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
parameter_list|,
name|JtaTransactionPolicy
name|transactionPolicy
parameter_list|,
name|ScheduledExecutorService
name|executorService
parameter_list|,
name|LoggingLevel
name|rollbackLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
name|this
operator|.
name|transactionPolicy
operator|=
name|transactionPolicy
expr_stmt|;
name|this
operator|.
name|rollbackLoggingLevel
operator|=
name|rollbackLoggingLevel
expr_stmt|;
name|this
operator|.
name|transactionKey
operator|=
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|transactionPolicy
argument_list|)
expr_stmt|;
name|setExceptionPolicy
argument_list|(
name|exceptionPolicyStrategy
argument_list|)
expr_stmt|;
block|}
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
comment|// we have to run this synchronously as a JTA Transaction does *not*
comment|// support using multiple threads to span a transaction
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|isTransactedBy
argument_list|(
name|transactionKey
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
comment|// and then have it invoke the error handler from within that
comment|// transaction
name|processInTransaction
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|// invoke this synchronous method as JTA Transaction does *not*
comment|// support using multiple threads to span a transaction
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
annotation|@
name|Override
DECL|method|processAsync (Exchange exchange)
specifier|public
name|CompletableFuture
argument_list|<
name|Exchange
argument_list|>
name|processAsync
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|AsyncCallbackToCompletableFutureAdapter
argument_list|<
name|Exchange
argument_list|>
name|callback
init|=
operator|new
name|AsyncCallbackToCompletableFutureAdapter
argument_list|<>
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
return|return
name|callback
operator|.
name|getFuture
argument_list|()
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
comment|// is the exchange redelivered, for example JMS brokers support such
comment|// details
name|Boolean
name|externalRedelivered
init|=
name|exchange
operator|.
name|isExternalRedelivered
argument_list|()
decl_stmt|;
specifier|final
name|String
name|redelivered
init|=
name|externalRedelivered
operator|!=
literal|null
condition|?
name|externalRedelivered
operator|.
name|toString
argument_list|()
else|:
literal|"unknown"
decl_stmt|;
specifier|final
name|String
name|ids
init|=
name|ExchangeHelper
operator|.
name|logIds
argument_list|(
name|exchange
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
name|transactionKey
argument_list|)
expr_stmt|;
comment|// do in transaction
name|logTransactionBegin
argument_list|(
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
name|doInTransactionTemplate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|logTransactionCommit
argument_list|(
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransactionRolledbackException
name|e
parameter_list|)
block|{
comment|// do not set as exception, as its just a dummy exception to force
comment|// spring TX to rollback
name|logTransactionRollback
argument_list|(
name|redelivered
argument_list|,
name|ids
argument_list|,
literal|null
argument_list|,
literal|true
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
name|logTransactionRollback
argument_list|(
name|redelivered
argument_list|,
name|ids
argument_list|,
name|e
argument_list|,
literal|false
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
name|transactionKey
argument_list|)
expr_stmt|;
block|}
comment|// if it was a local rollback only then remove its marker so outer
comment|// transaction wont see the marker
name|Boolean
name|onlyLast
init|=
operator|(
name|Boolean
operator|)
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY_LAST
argument_list|)
decl_stmt|;
if|if
condition|(
name|onlyLast
operator|!=
literal|null
operator|&&
name|onlyLast
condition|)
block|{
comment|// we only want this logged at debug level
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
comment|// log exception if there was a cause exception so we have the
comment|// stack trace
name|Exception
name|cause
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} "
operator|+
literal|"due exchange was marked for rollbackOnlyLast and caught: "
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} "
operator|+
literal|"due exchange was marked for rollbackOnlyLast"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove caused exception due we was marked as rollback only last
comment|// so by removing the exception, any outer transaction will not be
comment|// affected
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setTransactionPolicy (JtaTransactionPolicy transactionPolicy)
specifier|public
name|void
name|setTransactionPolicy
parameter_list|(
name|JtaTransactionPolicy
name|transactionPolicy
parameter_list|)
block|{
name|this
operator|.
name|transactionPolicy
operator|=
name|transactionPolicy
expr_stmt|;
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
throws|throws
name|Throwable
block|{
comment|// spring transaction template is working best with rollback if you
comment|// throw it a runtime exception
comment|// otherwise it may not rollback messages send to JMS queues etc.
name|transactionPolicy
operator|.
name|run
argument_list|(
operator|new
name|JtaTransactionPolicy
operator|.
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Throwable
block|{
comment|// wrapper exception to throw if the exchange failed
comment|// IMPORTANT: Must be a runtime exception to let Spring regard
comment|// it as to do "rollback"
name|Throwable
name|rce
decl_stmt|;
comment|// and now let process the exchange by the error handler
name|processByErrorHandler
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// after handling and still an exception or marked as rollback
comment|// only then rollback
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
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// create dummy exception to force spring transaction
comment|// manager to rollback
name|rce
operator|=
operator|new
name|TransactionRolledbackException
argument_list|()
expr_stmt|;
block|}
comment|// throw runtime exception to force rollback (which works
comment|// best to rollback with Spring transaction manager)
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
literal|"Throwing runtime exception to force transaction to rollback on {}"
argument_list|,
name|transactionPolicy
argument_list|)
expr_stmt|;
block|}
throw|throw
name|rce
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Processes the {@link Exchange} using the error handler.      *<p/>      * This implementation will invoke ensure this occurs synchronously, that      * means if the async routing engine did kick in, then this implementation      * will wait for the task to complete before it continues.      *      * @param exchange      *            the exchange      */
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
try|try
block|{
name|output
operator|.
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
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Logs the transaction begin      */
DECL|method|logTransactionBegin (String redelivered, String ids)
specifier|private
name|void
name|logTransactionBegin
parameter_list|(
name|String
name|redelivered
parameter_list|,
name|String
name|ids
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
literal|"Transaction begin ({}) redelivered({}) for {})"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Logs the transaction commit      */
DECL|method|logTransactionCommit (String redelivered, String ids)
specifier|private
name|void
name|logTransactionCommit
parameter_list|(
name|String
name|redelivered
parameter_list|,
name|String
name|ids
parameter_list|)
block|{
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|redelivered
argument_list|)
condition|)
block|{
comment|// okay its a redelivered message so log at INFO level if
comment|// rollbackLoggingLevel is INFO or higher
comment|// this allows people to know that the redelivered message was
comment|// committed this time
if|if
condition|(
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|INFO
operator|||
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|WARN
operator|||
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|ERROR
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Transaction commit ({}) redelivered({}) for {})"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
comment|// return after we have logged
return|return;
block|}
block|}
comment|// log non redelivered by default at DEBUG level
name|log
operator|.
name|debug
argument_list|(
literal|"Transaction commit ({}) redelivered({}) for {})"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
comment|/**      * Logs the transaction rollback.      */
DECL|method|logTransactionRollback (String redelivered, String ids, Throwable e, boolean rollbackOnly)
specifier|private
name|void
name|logTransactionRollback
parameter_list|(
name|String
name|redelivered
parameter_list|,
name|String
name|ids
parameter_list|,
name|Throwable
name|e
parameter_list|,
name|boolean
name|rollbackOnly
parameter_list|)
block|{
if|if
condition|(
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|OFF
condition|)
block|{
return|return;
block|}
elseif|else
if|if
condition|(
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|ERROR
operator|&&
name|log
operator|.
name|isErrorEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|rollbackOnly
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} due exchange was marked for rollbackOnly"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} caught: {}"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|WARN
operator|&&
name|log
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|rollbackOnly
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} due exchange was marked for rollbackOnly"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} caught: {}"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|INFO
operator|&&
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|rollbackOnly
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} due exchange was marked for rollbackOnly"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} caught: {}"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|DEBUG
operator|&&
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|rollbackOnly
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} due exchange was marked for rollbackOnly"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} caught: {}"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|rollbackLoggingLevel
operator|==
name|LoggingLevel
operator|.
name|TRACE
operator|&&
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|rollbackOnly
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} due exchange was marked for rollbackOnly"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Transaction rollback ({}) redelivered({}) for {} caught: {}"
argument_list|,
name|transactionKey
argument_list|,
name|redelivered
argument_list|,
name|ids
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|setExceptionPolicy (ExceptionPolicyStrategy exceptionPolicy)
specifier|public
name|void
name|setExceptionPolicy
parameter_list|(
name|ExceptionPolicyStrategy
name|exceptionPolicy
parameter_list|)
block|{
name|this
operator|.
name|exceptionPolicy
operator|=
name|exceptionPolicy
expr_stmt|;
block|}
DECL|method|getExceptionPolicy ()
specifier|public
name|ExceptionPolicyStrategy
name|getExceptionPolicy
parameter_list|()
block|{
return|return
name|exceptionPolicy
return|;
block|}
annotation|@
name|Override
DECL|method|getOutput ()
specifier|public
name|Processor
name|getOutput
parameter_list|()
block|{
return|return
name|output
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|preparingShutdown
operator|=
literal|false
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
comment|// noop, do not stop any services which we only do when shutting down
comment|// as the error handler can be context scoped, and should not stop in
comment|// case a route stops
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|output
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|prepareShutdown (boolean suspendOnly, boolean forced)
specifier|public
name|void
name|prepareShutdown
parameter_list|(
name|boolean
name|suspendOnly
parameter_list|,
name|boolean
name|forced
parameter_list|)
block|{
comment|// prepare for shutdown, eg do not allow redelivery if configured
name|log
operator|.
name|trace
argument_list|(
literal|"Prepare shutdown on error handler {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|preparingShutdown
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

