begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Callable
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
name|ExecutionException
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
name|Future
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
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
name|LockModeType
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
name|support
operator|.
name|PollingConsumerSupport
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
DECL|class|JpaPollingConsumer
specifier|public
class|class
name|JpaPollingConsumer
extends|extends
name|PollingConsumerSupport
block|{
DECL|field|executorService
specifier|private
specifier|transient
name|ExecutorService
name|executorService
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
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|namedQuery
specifier|private
name|String
name|namedQuery
decl_stmt|;
DECL|field|nativeQuery
specifier|private
name|String
name|nativeQuery
decl_stmt|;
DECL|field|lockModeType
specifier|private
name|LockModeType
name|lockModeType
init|=
name|LockModeType
operator|.
name|PESSIMISTIC_WRITE
decl_stmt|;
DECL|field|resultClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultClass
decl_stmt|;
DECL|field|queryFactory
specifier|private
name|QueryFactory
name|queryFactory
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
DECL|method|JpaPollingConsumer (JpaEndpoint endpoint)
specifier|public
name|JpaPollingConsumer
parameter_list|(
name|JpaEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityManagerFactory
operator|=
name|endpoint
operator|.
name|getEntityManagerFactory
argument_list|()
expr_stmt|;
name|this
operator|.
name|transactionTemplate
operator|=
name|endpoint
operator|.
name|createTransactionTemplate
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|JpaEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|JpaEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getNamedQuery ()
specifier|public
name|String
name|getNamedQuery
parameter_list|()
block|{
return|return
name|namedQuery
return|;
block|}
DECL|method|setNamedQuery (String namedQuery)
specifier|public
name|void
name|setNamedQuery
parameter_list|(
name|String
name|namedQuery
parameter_list|)
block|{
name|this
operator|.
name|namedQuery
operator|=
name|namedQuery
expr_stmt|;
block|}
DECL|method|getNativeQuery ()
specifier|public
name|String
name|getNativeQuery
parameter_list|()
block|{
return|return
name|nativeQuery
return|;
block|}
DECL|method|setNativeQuery (String nativeQuery)
specifier|public
name|void
name|setNativeQuery
parameter_list|(
name|String
name|nativeQuery
parameter_list|)
block|{
name|this
operator|.
name|nativeQuery
operator|=
name|nativeQuery
expr_stmt|;
block|}
DECL|method|getLockModeType ()
specifier|public
name|LockModeType
name|getLockModeType
parameter_list|()
block|{
return|return
name|lockModeType
return|;
block|}
DECL|method|setLockModeType (LockModeType lockModeType)
specifier|public
name|void
name|setLockModeType
parameter_list|(
name|LockModeType
name|lockModeType
parameter_list|)
block|{
name|this
operator|.
name|lockModeType
operator|=
name|lockModeType
expr_stmt|;
block|}
DECL|method|getResultClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultClass
parameter_list|()
block|{
return|return
name|resultClass
return|;
block|}
DECL|method|setResultClass (Class<?> resultClass)
specifier|public
name|void
name|setResultClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultClass
parameter_list|)
block|{
name|this
operator|.
name|resultClass
operator|=
name|resultClass
expr_stmt|;
block|}
DECL|method|getQueryFactory ()
specifier|public
name|QueryFactory
name|getQueryFactory
parameter_list|()
block|{
return|return
name|queryFactory
return|;
block|}
DECL|method|setQueryFactory (QueryFactory queryFactory)
specifier|public
name|void
name|setQueryFactory
parameter_list|(
name|QueryFactory
name|queryFactory
parameter_list|)
block|{
name|this
operator|.
name|queryFactory
operator|=
name|queryFactory
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|receive ()
specifier|public
name|Exchange
name|receive
parameter_list|()
block|{
comment|// resolve the entity manager before evaluating the expression
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
name|getEndpoint
argument_list|()
operator|.
name|isUsePassedInEntityManager
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isSharedEntityManager
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Object
name|out
init|=
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
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
name|Query
name|query
init|=
name|getQueryFactory
argument_list|()
operator|.
name|createQuery
argument_list|(
name|entityManager
argument_list|)
decl_stmt|;
name|configureParameters
argument_list|(
name|query
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isConsumeLockEntity
argument_list|()
condition|)
block|{
name|query
operator|.
name|setLockMode
argument_list|(
name|getLockModeType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Created query {}"
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|Object
name|answer
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|query
operator|.
name|getResultList
argument_list|()
decl_stmt|;
if|if
condition|(
name|results
operator|!=
literal|null
operator|&&
name|results
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// we only have 1 entity so return that
name|answer
operator|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we have more data so return a list
name|answer
operator|=
name|results
expr_stmt|;
block|}
comment|// commit
name|log
operator|.
name|debug
argument_list|(
literal|"Flushing EntityManager"
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// must clear after flush
name|entityManager
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PersistenceException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Disposing EntityManager {} on {} due to coming transaction rollback"
argument_list|,
name|entityManager
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
return|return
name|answer
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|out
argument_list|,
name|entityManager
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|out
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|receiveNoWait ()
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|()
block|{
comment|// call receive as-is
return|return
name|receive
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
comment|// need to use a thread pool to perform the task so we can support timeout
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getOrCreatePollingConsumerExecutorService
argument_list|()
expr_stmt|;
block|}
comment|// the task is the receive method
name|Future
argument_list|<
name|Exchange
argument_list|>
name|future
init|=
name|executorService
operator|.
name|submit
argument_list|(
operator|(
name|Callable
argument_list|<
name|Exchange
argument_list|>
operator|)
name|this
operator|::
name|receive
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
decl||
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
comment|// ignore as we hit timeout then return null
block|}
return|return
literal|null
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
if|if
condition|(
name|queryFactory
operator|==
literal|null
condition|)
block|{
name|queryFactory
operator|=
name|createQueryFactory
argument_list|()
expr_stmt|;
if|if
condition|(
name|queryFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No queryType property configured on this consumer, nor an entityType configured on the endpoint so cannot consume"
argument_list|)
throw|;
block|}
block|}
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
DECL|method|configureParameters (Query query)
specifier|protected
name|void
name|configureParameters
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|int
name|maxResults
init|=
name|getEndpoint
argument_list|()
operator|.
name|getMaximumResults
argument_list|()
decl_stmt|;
if|if
condition|(
name|maxResults
operator|>
literal|0
condition|)
block|{
name|query
operator|.
name|setMaxResults
argument_list|(
name|maxResults
argument_list|)
expr_stmt|;
block|}
comment|// setup the parameter
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|query
operator|.
name|setParameter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createExchange (Object result, EntityManager entityManager)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|result
parameter_list|,
name|EntityManager
name|entityManager
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
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
name|result
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JpaConstants
operator|.
name|ENTITY_MANAGER
argument_list|,
name|entityManager
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createQueryFactory ()
specifier|protected
name|QueryFactory
name|createQueryFactory
parameter_list|()
block|{
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
return|return
name|QueryBuilder
operator|.
name|query
argument_list|(
name|query
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|namedQuery
operator|!=
literal|null
condition|)
block|{
return|return
name|QueryBuilder
operator|.
name|namedQuery
argument_list|(
name|namedQuery
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|nativeQuery
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|resultClass
operator|!=
literal|null
condition|)
block|{
return|return
name|QueryBuilder
operator|.
name|nativeQuery
argument_list|(
name|nativeQuery
argument_list|,
name|resultClass
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|QueryBuilder
operator|.
name|nativeQuery
argument_list|(
name|nativeQuery
argument_list|)
return|;
block|}
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
init|=
name|getEndpoint
argument_list|()
operator|.
name|getEntityType
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityType
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
comment|// Check if we have a property name on the @Entity annotation
name|String
name|name
init|=
name|getEntityName
argument_list|(
name|entityType
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|QueryBuilder
operator|.
name|query
argument_list|(
literal|"select x from "
operator|+
name|name
operator|+
literal|" x"
argument_list|)
return|;
block|}
else|else
block|{
comment|// Remove package name of the entity to be conform with JPA 1.0 spec
return|return
name|QueryBuilder
operator|.
name|query
argument_list|(
literal|"select x from "
operator|+
name|entityType
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" x"
argument_list|)
return|;
block|}
block|}
block|}
block|}
DECL|method|getEntityName (Class<?> clazz)
specifier|protected
name|String
name|getEntityName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|Entity
name|entity
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|Entity
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Check if the property name has been defined for Entity annotation
if|if
condition|(
name|entity
operator|!=
literal|null
operator|&&
operator|!
name|entity
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
return|return
name|entity
operator|.
name|name
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

