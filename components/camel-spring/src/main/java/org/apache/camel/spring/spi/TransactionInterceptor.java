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
name|ExchangeProperty
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
name|DelayPolicy
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
name|DelegateProcessor
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|DefaultTransactionStatus
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
name|TransactionSynchronizationManager
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
comment|/**  * The<a href="http://activemq.apache.org/camel/transactional-client.html">Transactional Client</a>  * EIP pattern.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|TransactionInterceptor
specifier|public
class|class
name|TransactionInterceptor
extends|extends
name|DelegateProcessor
block|{
DECL|field|TRANSACTED
specifier|public
specifier|static
specifier|final
name|ExchangeProperty
argument_list|<
name|Boolean
argument_list|>
name|TRANSACTED
init|=
operator|new
name|ExchangeProperty
argument_list|<
name|Boolean
argument_list|>
argument_list|(
literal|"transacted"
argument_list|,
literal|"org.apache.camel.transacted"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TransactionInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|transactionTemplate
specifier|private
specifier|final
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|field|redeliveryPolicy
specifier|private
name|RedeliveryPolicy
name|redeliveryPolicy
decl_stmt|;
DECL|field|delayPolicy
specifier|private
name|DelayPolicy
name|delayPolicy
decl_stmt|;
DECL|method|TransactionInterceptor (TransactionTemplate transactionTemplate)
specifier|public
name|TransactionInterceptor
parameter_list|(
name|TransactionTemplate
name|transactionTemplate
parameter_list|)
block|{
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
block|}
DECL|method|TransactionInterceptor (Processor processor, TransactionTemplate transactionTemplate)
specifier|public
name|TransactionInterceptor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
block|}
comment|/**      * @deprecated use DelayPolicy. Will be removed in Camel 2.0      */
DECL|method|TransactionInterceptor (Processor processor, TransactionTemplate transactionTemplate, RedeliveryPolicy redeliveryPolicy)
specifier|public
name|TransactionInterceptor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|)
block|{
name|this
argument_list|(
name|processor
argument_list|,
name|transactionTemplate
argument_list|)
expr_stmt|;
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
name|this
operator|.
name|delayPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
block|}
DECL|method|TransactionInterceptor (Processor processor, TransactionTemplate transactionTemplate, DelayPolicy delayPolicy)
specifier|public
name|TransactionInterceptor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|DelayPolicy
name|delayPolicy
parameter_list|)
block|{
name|this
argument_list|(
name|processor
argument_list|,
name|transactionTemplate
argument_list|)
expr_stmt|;
name|this
operator|.
name|delayPolicy
operator|=
name|delayPolicy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"TransactionInterceptor:"
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
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
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
name|RuntimeCamelException
name|rce
init|=
literal|null
decl_stmt|;
name|boolean
name|activeTx
init|=
literal|false
decl_stmt|;
try|try
block|{
comment|// find out if there is an actual transaction alive, and thus we are in transacted mode
name|activeTx
operator|=
name|TransactionSynchronizationManager
operator|.
name|isActualTransactionActive
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|activeTx
condition|)
block|{
name|activeTx
operator|=
name|status
operator|.
name|isNewTransaction
argument_list|()
operator|&&
operator|!
name|status
operator|.
name|isCompleted
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|activeTx
condition|)
block|{
if|if
condition|(
name|DefaultTransactionStatus
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|status
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|DefaultTransactionStatus
name|defStatus
init|=
name|DefaultTransactionStatus
operator|.
name|class
operator|.
name|cast
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|activeTx
operator|=
name|defStatus
operator|.
name|hasTransaction
argument_list|()
operator|&&
operator|!
name|status
operator|.
name|isCompleted
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Is actual transaction active: "
operator|+
name|activeTx
argument_list|)
expr_stmt|;
block|}
comment|// okay mark the exchange as transacted, then the DeadLetterChannel or others know
comment|// its a transacted exchange
if|if
condition|(
name|activeTx
condition|)
block|{
name|TRANSACTED
operator|.
name|set
argument_list|(
name|exchange
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|// process the exchange
name|processNext
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// wrap if the exchange failed with an exception
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
name|wrapRuntimeCamelException
argument_list|(
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
name|rce
operator|=
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// rethrow exception if the exchange failed
if|if
condition|(
name|rce
operator|!=
literal|null
condition|)
block|{
comment|// an exception occured so please sleep before we rethrow the exception
name|delayBeforeRedelivery
argument_list|()
expr_stmt|;
if|if
condition|(
name|activeTx
condition|)
block|{
name|status
operator|.
name|setRollbackOnly
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Setting transaction to rollbackOnly due to exception being thrown: "
operator|+
name|rce
operator|.
name|getMessage
argument_list|()
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
comment|/**      * Wraps the caused exception in a RuntimeCamelException if its not already such an exception      */
DECL|method|wrapRuntimeCamelException (Throwable e)
specifier|private
specifier|static
name|RuntimeCamelException
name|wrapRuntimeCamelException
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|RuntimeCamelException
condition|)
block|{
comment|// dont double wrap if already a RuntimeCamelException
return|return
operator|(
name|RuntimeCamelException
operator|)
name|e
return|;
block|}
else|else
block|{
comment|// wrap if the exchange threw an exception
return|return
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
comment|/**      * Sleeps before the transaction is set as rollback and the caused exception is rethrown to let the      * Spring TransactionManager handle the rollback.      */
DECL|method|delayBeforeRedelivery ()
specifier|protected
name|void
name|delayBeforeRedelivery
parameter_list|()
block|{
name|long
name|delay
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|redeliveryPolicy
operator|!=
literal|null
condition|)
block|{
name|delay
operator|=
name|redeliveryPolicy
operator|.
name|getDelay
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|delayPolicy
operator|!=
literal|null
condition|)
block|{
name|delay
operator|=
name|delayPolicy
operator|.
name|getDelay
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|delay
operator|>
literal|0
condition|)
block|{
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sleeping for: "
operator|+
name|delay
operator|+
literal|" millis until attempting redelivery"
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
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
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Thread interrupted: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * @deprecated use DelayPolicy. Will be removed in Camel 2.0      */
DECL|method|getRedeliveryPolicy ()
specifier|public
name|RedeliveryPolicy
name|getRedeliveryPolicy
parameter_list|()
block|{
return|return
name|redeliveryPolicy
return|;
block|}
comment|/**      * @deprecated use DelayPolicy. Will be removed in Camel 2.0      */
DECL|method|setRedeliveryPolicy (RedeliveryPolicy redeliveryPolicy)
specifier|public
name|void
name|setRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|)
block|{
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
block|}
DECL|method|getDelayPolicy ()
specifier|public
name|DelayPolicy
name|getDelayPolicy
parameter_list|()
block|{
return|return
name|delayPolicy
return|;
block|}
DECL|method|setDelayPolicy (DelayPolicy delayPolicy)
specifier|public
name|void
name|setDelayPolicy
parameter_list|(
name|DelayPolicy
name|delayPolicy
parameter_list|)
block|{
name|this
operator|.
name|delayPolicy
operator|=
name|delayPolicy
expr_stmt|;
block|}
DECL|method|propagationBehaviorToString (int propagationBehavior)
specifier|protected
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

