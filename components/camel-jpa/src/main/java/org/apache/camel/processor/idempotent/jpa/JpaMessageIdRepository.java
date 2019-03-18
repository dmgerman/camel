begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|jpa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Persistence
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PersistenceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Query
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|IdempotentRepository
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
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTransactionManager
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
name|TransactionCallback
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
name|jpa
operator|.
name|JpaHelper
operator|.
name|getTargetEntityManager
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"JPA based message id repository"
argument_list|)
DECL|class|JpaMessageIdRepository
specifier|public
class|class
name|JpaMessageIdRepository
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
block|{
DECL|field|QUERY_STRING
specifier|protected
specifier|static
specifier|final
name|String
name|QUERY_STRING
init|=
literal|"select x from "
operator|+
name|MessageProcessed
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x where x.processorName = ?1 and x.messageId = ?2"
decl_stmt|;
DECL|field|QUERY_CLEAR_STRING
specifier|protected
specifier|static
specifier|final
name|String
name|QUERY_CLEAR_STRING
init|=
literal|"select x from "
operator|+
name|MessageProcessed
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x where x.processorName = ?1"
decl_stmt|;
DECL|field|processorName
specifier|private
specifier|final
name|String
name|processorName
decl_stmt|;
DECL|field|entityManagerFactory
specifier|private
specifier|final
name|EntityManagerFactory
name|entityManagerFactory
decl_stmt|;
DECL|field|transactionTemplate
specifier|private
specifier|final
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|field|joinTransaction
specifier|private
name|boolean
name|joinTransaction
init|=
literal|true
decl_stmt|;
DECL|field|sharedEntityManager
specifier|private
name|boolean
name|sharedEntityManager
decl_stmt|;
DECL|method|JpaMessageIdRepository (EntityManagerFactory entityManagerFactory, String processorName)
specifier|public
name|JpaMessageIdRepository
parameter_list|(
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|,
name|String
name|processorName
parameter_list|)
block|{
name|this
argument_list|(
name|entityManagerFactory
argument_list|,
name|createTransactionTemplate
argument_list|(
name|entityManagerFactory
argument_list|)
argument_list|,
name|processorName
argument_list|)
expr_stmt|;
block|}
DECL|method|JpaMessageIdRepository (EntityManagerFactory entityManagerFactory, TransactionTemplate transactionTemplate, String processorName)
specifier|public
name|JpaMessageIdRepository
parameter_list|(
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|String
name|processorName
parameter_list|)
block|{
name|this
operator|.
name|entityManagerFactory
operator|=
name|entityManagerFactory
expr_stmt|;
name|this
operator|.
name|processorName
operator|=
name|processorName
expr_stmt|;
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
block|}
DECL|method|jpaMessageIdRepository (String persistenceUnit, String processorName)
specifier|public
specifier|static
name|JpaMessageIdRepository
name|jpaMessageIdRepository
parameter_list|(
name|String
name|persistenceUnit
parameter_list|,
name|String
name|processorName
parameter_list|)
block|{
return|return
name|jpaMessageIdRepository
argument_list|(
name|Persistence
operator|.
name|createEntityManagerFactory
argument_list|(
name|persistenceUnit
argument_list|)
argument_list|,
name|processorName
argument_list|)
return|;
block|}
DECL|method|jpaMessageIdRepository (EntityManagerFactory entityManagerFactory, String processorName)
specifier|public
specifier|static
name|JpaMessageIdRepository
name|jpaMessageIdRepository
parameter_list|(
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|,
name|String
name|processorName
parameter_list|)
block|{
return|return
operator|new
name|JpaMessageIdRepository
argument_list|(
name|entityManagerFactory
argument_list|,
name|processorName
argument_list|)
return|;
block|}
DECL|method|createTransactionTemplate (EntityManagerFactory entityManagerFactory)
specifier|private
specifier|static
name|TransactionTemplate
name|createTransactionTemplate
parameter_list|(
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|)
block|{
name|TransactionTemplate
name|transactionTemplate
init|=
operator|new
name|TransactionTemplate
argument_list|()
decl_stmt|;
name|transactionTemplate
operator|.
name|setTransactionManager
argument_list|(
operator|new
name|JpaTransactionManager
argument_list|(
name|entityManagerFactory
argument_list|)
argument_list|)
expr_stmt|;
name|transactionTemplate
operator|.
name|setPropagationBehavior
argument_list|(
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRED
argument_list|)
expr_stmt|;
return|return
name|transactionTemplate
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds the key to the store"
argument_list|)
DECL|method|add (String messageId)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
return|return
name|add
argument_list|(
literal|null
argument_list|,
name|messageId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|add (final Exchange exchange, final String messageId)
specifier|public
name|boolean
name|add
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|String
name|messageId
parameter_list|)
block|{
specifier|final
name|EntityManager
name|entityManager
init|=
name|getTargetEntityManager
argument_list|(
name|exchange
argument_list|,
name|entityManagerFactory
argument_list|,
literal|true
argument_list|,
name|sharedEntityManager
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Run this in single transaction.
name|Boolean
name|rc
init|=
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
if|if
condition|(
name|isJoinTransaction
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|query
argument_list|(
name|entityManager
argument_list|,
name|messageId
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|MessageProcessed
name|processed
init|=
operator|new
name|MessageProcessed
argument_list|()
decl_stmt|;
name|processed
operator|.
name|setProcessorName
argument_list|(
name|processorName
argument_list|)
expr_stmt|;
name|processed
operator|.
name|setMessageId
argument_list|(
name|messageId
argument_list|)
expr_stmt|;
name|processed
operator|.
name|setCreatedAt
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|persist
argument_list|(
name|processed
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
else|else
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Something went wrong trying to add message to repository {}"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|PersistenceException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|entityManager
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"add {} -> {}"
argument_list|,
name|messageId
argument_list|,
name|rc
argument_list|)
expr_stmt|;
return|return
name|rc
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Does the store contain the given key"
argument_list|)
DECL|method|contains (String messageId)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
return|return
name|contains
argument_list|(
literal|null
argument_list|,
name|messageId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|contains (final Exchange exchange, final String messageId)
specifier|public
name|boolean
name|contains
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|String
name|messageId
parameter_list|)
block|{
specifier|final
name|EntityManager
name|entityManager
init|=
name|getTargetEntityManager
argument_list|(
name|exchange
argument_list|,
name|entityManagerFactory
argument_list|,
literal|true
argument_list|,
name|sharedEntityManager
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Run this in single transaction.
name|Boolean
name|rc
init|=
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
if|if
condition|(
name|isJoinTransaction
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|query
argument_list|(
name|entityManager
argument_list|,
name|messageId
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
else|else
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"contains {} -> {}"
argument_list|,
name|messageId
argument_list|,
name|rc
argument_list|)
expr_stmt|;
return|return
name|rc
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Remove the key from the store"
argument_list|)
DECL|method|remove (String messageId)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
return|return
name|remove
argument_list|(
literal|null
argument_list|,
name|messageId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (final Exchange exchange, final String messageId)
specifier|public
name|boolean
name|remove
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|String
name|messageId
parameter_list|)
block|{
specifier|final
name|EntityManager
name|entityManager
init|=
name|getTargetEntityManager
argument_list|(
name|exchange
argument_list|,
name|entityManagerFactory
argument_list|,
literal|true
argument_list|,
name|sharedEntityManager
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Boolean
name|rc
init|=
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
if|if
condition|(
name|isJoinTransaction
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|query
argument_list|(
name|entityManager
argument_list|,
name|messageId
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
else|else
block|{
name|MessageProcessed
name|processed
init|=
operator|(
name|MessageProcessed
operator|)
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|entityManager
operator|.
name|remove
argument_list|(
name|processed
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Something went wrong trying to remove message to repository {}"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|PersistenceException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|entityManager
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"remove {}"
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
return|return
name|rc
return|;
block|}
annotation|@
name|Override
DECL|method|confirm (String messageId)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
return|return
name|confirm
argument_list|(
literal|null
argument_list|,
name|messageId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|confirm (final Exchange exchange, String messageId)
specifier|public
name|boolean
name|confirm
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
name|String
name|messageId
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"confirm {} -> true"
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clear the store"
argument_list|)
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
specifier|final
name|EntityManager
name|entityManager
init|=
name|getTargetEntityManager
argument_list|(
literal|null
argument_list|,
name|entityManagerFactory
argument_list|,
literal|true
argument_list|,
name|sharedEntityManager
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
if|if
condition|(
name|isJoinTransaction
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|queryClear
argument_list|(
name|entityManager
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|item
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|entityManager
operator|.
name|remove
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Something went wrong trying to clear the repository {}"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|PersistenceException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|entityManager
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"clear the store {}"
argument_list|,
name|MessageProcessed
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|query (final EntityManager entityManager, final String messageId)
specifier|private
name|List
argument_list|<
name|?
argument_list|>
name|query
parameter_list|(
specifier|final
name|EntityManager
name|entityManager
parameter_list|,
specifier|final
name|String
name|messageId
parameter_list|)
block|{
name|Query
name|query
init|=
name|entityManager
operator|.
name|createQuery
argument_list|(
name|QUERY_STRING
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|1
argument_list|,
name|processorName
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|2
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
return|return
name|query
operator|.
name|getResultList
argument_list|()
return|;
block|}
DECL|method|queryClear (final EntityManager entityManager)
specifier|private
name|List
argument_list|<
name|?
argument_list|>
name|queryClear
parameter_list|(
specifier|final
name|EntityManager
name|entityManager
parameter_list|)
block|{
name|Query
name|query
init|=
name|entityManager
operator|.
name|createQuery
argument_list|(
name|QUERY_CLEAR_STRING
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|1
argument_list|,
name|processorName
argument_list|)
expr_stmt|;
return|return
name|query
operator|.
name|getResultList
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The processor name"
argument_list|)
DECL|method|getProcessorName ()
specifier|public
name|String
name|getProcessorName
parameter_list|()
block|{
return|return
name|processorName
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to join existing transaction"
argument_list|)
DECL|method|isJoinTransaction ()
specifier|public
name|boolean
name|isJoinTransaction
parameter_list|()
block|{
return|return
name|joinTransaction
return|;
block|}
DECL|method|setJoinTransaction (boolean joinTransaction)
specifier|public
name|void
name|setJoinTransaction
parameter_list|(
name|boolean
name|joinTransaction
parameter_list|)
block|{
name|this
operator|.
name|joinTransaction
operator|=
name|joinTransaction
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to use shared EntityManager"
argument_list|)
DECL|method|isSharedEntityManager ()
specifier|public
name|boolean
name|isSharedEntityManager
parameter_list|()
block|{
return|return
name|sharedEntityManager
return|;
block|}
DECL|method|setSharedEntityManager (boolean sharedEntityManager)
specifier|public
name|void
name|setSharedEntityManager
parameter_list|(
name|boolean
name|sharedEntityManager
parameter_list|)
block|{
name|this
operator|.
name|sharedEntityManager
operator|=
name|sharedEntityManager
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
comment|// noop
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
comment|// noop
block|}
block|}
end_class

end_unit

