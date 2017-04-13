begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|transaction
operator|.
name|HeuristicMixedException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|HeuristicRollbackException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|RollbackException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|SystemException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|Transaction
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
comment|/**  * Helper methods for transaction handling  */
end_comment

begin_class
DECL|class|TransactionalJtaTransactionPolicy
specifier|public
specifier|abstract
class|class
name|TransactionalJtaTransactionPolicy
extends|extends
name|JtaTransactionPolicy
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
name|TransactionalJtaTransactionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|runWithTransaction (final Runnable runnable, final boolean isNew)
specifier|protected
name|void
name|runWithTransaction
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|,
specifier|final
name|boolean
name|isNew
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|isNew
condition|)
block|{
name|begin
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|rollback
argument_list|(
name|isNew
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|rollback
argument_list|(
name|isNew
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|rollback
argument_list|(
name|isNew
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
if|if
condition|(
name|isNew
condition|)
block|{
name|commit
argument_list|()
expr_stmt|;
block|}
return|return;
block|}
DECL|method|begin ()
specifier|private
name|void
name|begin
parameter_list|()
throws|throws
name|Exception
block|{
name|transactionManager
operator|.
name|begin
argument_list|()
expr_stmt|;
block|}
DECL|method|commit ()
specifier|private
name|void
name|commit
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|transactionManager
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HeuristicMixedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Unable to commit transaction"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|HeuristicRollbackException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Unable to commit transaction"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RollbackException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Unable to commit transaction"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SystemException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Unable to commit transaction"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|rollback
argument_list|(
literal|true
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|rollback
argument_list|(
literal|true
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|rollback
argument_list|(
literal|true
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|rollback (boolean isNew)
specifier|protected
name|void
name|rollback
parameter_list|(
name|boolean
name|isNew
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
if|if
condition|(
name|isNew
condition|)
block|{
name|transactionManager
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|transactionManager
operator|.
name|setRollbackOnly
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not rollback transaction!"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|suspendTransaction ()
specifier|protected
name|Transaction
name|suspendTransaction
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|transactionManager
operator|.
name|suspend
argument_list|()
return|;
block|}
DECL|method|resumeTransaction (final Transaction suspendedTransaction)
specifier|protected
name|void
name|resumeTransaction
parameter_list|(
specifier|final
name|Transaction
name|suspendedTransaction
parameter_list|)
block|{
if|if
condition|(
name|suspendedTransaction
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|transactionManager
operator|.
name|resume
argument_list|(
name|suspendedTransaction
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not resume transaction!"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|hasActiveTransaction ()
specifier|protected
name|boolean
name|hasActiveTransaction
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|transactionManager
operator|.
name|getStatus
argument_list|()
operator|!=
name|Status
operator|.
name|STATUS_MARKED_ROLLBACK
operator|&&
name|transactionManager
operator|.
name|getStatus
argument_list|()
operator|!=
name|Status
operator|.
name|STATUS_NO_TRANSACTION
return|;
block|}
block|}
end_class

end_unit

