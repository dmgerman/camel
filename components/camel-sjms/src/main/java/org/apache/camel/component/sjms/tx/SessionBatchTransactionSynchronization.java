begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|tx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
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
name|locks
operator|.
name|ReadWriteLock
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
name|locks
operator|.
name|ReentrantReadWriteLock
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|sjms
operator|.
name|TransactionCommitStrategy
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
name|sjms
operator|.
name|taskmanager
operator|.
name|TimedTaskManager
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
name|Synchronization
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

begin_comment
comment|/**  * SessionTransactionSynchronization is called at the completion of each  * {@link org.apache.camel.Exchange}.  */
end_comment

begin_class
DECL|class|SessionBatchTransactionSynchronization
specifier|public
class|class
name|SessionBatchTransactionSynchronization
implements|implements
name|Synchronization
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
name|SessionBatchTransactionSynchronization
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|field|commitStrategy
specifier|private
specifier|final
name|TransactionCommitStrategy
name|commitStrategy
decl_stmt|;
DECL|field|batchTransactionTimeout
specifier|private
name|long
name|batchTransactionTimeout
init|=
literal|5000
decl_stmt|;
DECL|field|currentTask
specifier|private
name|TimeoutTask
name|currentTask
decl_stmt|;
DECL|field|lock
specifier|private
name|ReadWriteLock
name|lock
init|=
operator|new
name|ReentrantReadWriteLock
argument_list|()
decl_stmt|;
DECL|field|timedTaskManager
specifier|private
specifier|final
name|TimedTaskManager
name|timedTaskManager
decl_stmt|;
DECL|method|SessionBatchTransactionSynchronization (TimedTaskManager timedTaskManager, Session session, TransactionCommitStrategy commitStrategy, long batchTransactionTimeout)
specifier|public
name|SessionBatchTransactionSynchronization
parameter_list|(
name|TimedTaskManager
name|timedTaskManager
parameter_list|,
name|Session
name|session
parameter_list|,
name|TransactionCommitStrategy
name|commitStrategy
parameter_list|,
name|long
name|batchTransactionTimeout
parameter_list|)
block|{
name|this
operator|.
name|timedTaskManager
operator|=
name|timedTaskManager
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
if|if
condition|(
name|commitStrategy
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|commitStrategy
operator|=
operator|new
name|DefaultTransactionCommitStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|commitStrategy
operator|=
name|commitStrategy
expr_stmt|;
block|}
if|if
condition|(
name|batchTransactionTimeout
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|batchTransactionTimeout
operator|=
name|batchTransactionTimeout
expr_stmt|;
name|createTask
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
if|if
condition|(
name|commitStrategy
operator|.
name|rollback
argument_list|(
name|exchange
argument_list|)
condition|)
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
literal|"Processing failure of Exchange id:{}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
name|session
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to rollback the session: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
if|if
condition|(
name|commitStrategy
operator|.
name|commit
argument_list|(
name|exchange
argument_list|)
condition|)
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
literal|"Processing completion of Exchange id:{}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
name|session
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to commit the session: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
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
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
name|resetTask
argument_list|()
expr_stmt|;
block|}
DECL|method|createTask ()
specifier|private
name|void
name|createTask
parameter_list|()
block|{
try|try
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
name|currentTask
operator|=
operator|new
name|TimeoutTask
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|resetTask ()
specifier|private
name|void
name|resetTask
parameter_list|()
block|{
try|try
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
name|currentTask
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|currentTask
operator|=
operator|new
name|TimeoutTask
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
name|timedTaskManager
operator|.
name|addTask
argument_list|(
name|currentTask
argument_list|,
name|batchTransactionTimeout
argument_list|)
expr_stmt|;
block|}
DECL|class|TimeoutTask
specifier|public
specifier|final
class|class
name|TimeoutTask
extends|extends
name|TimerTask
block|{
DECL|method|TimeoutTask ()
specifier|private
name|TimeoutTask
parameter_list|()
block|{         }
comment|/**          * When the timer executes, either commits or rolls back the session          * transaction.          */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Batch Transaction Timer expired"
argument_list|)
expr_stmt|;
try|try
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Committing the current transactions"
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
name|session
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
operator|(
operator|(
name|BatchTransactionCommitStrategy
operator|)
name|commitStrategy
operator|)
operator|.
name|reset
argument_list|()
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
name|warn
argument_list|(
literal|"Failed to commit the session during timeout: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|cancel ()
specifier|public
name|boolean
name|cancel
parameter_list|()
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cancelling the TimeoutTask"
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|cancel
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

