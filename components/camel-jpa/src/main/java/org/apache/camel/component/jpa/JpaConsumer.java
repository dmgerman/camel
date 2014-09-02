begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|OptimisticLockException
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
name|PessimisticLockException
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
name|impl
operator|.
name|ScheduledBatchPollingConsumer
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
name|CastUtils
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JpaConsumer
specifier|public
class|class
name|JpaConsumer
extends|extends
name|ScheduledBatchPollingConsumer
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
name|JpaConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NOWAIT
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|NOWAIT
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
DECL|field|entityManager
specifier|private
name|EntityManager
name|entityManager
decl_stmt|;
DECL|field|queryFactory
specifier|private
name|QueryFactory
name|queryFactory
decl_stmt|;
DECL|field|deleteHandler
specifier|private
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
name|deleteHandler
decl_stmt|;
DECL|field|preDeleteHandler
specifier|private
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
name|preDeleteHandler
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
DECL|field|resultClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultClass
decl_stmt|;
DECL|field|transacted
specifier|private
name|boolean
name|transacted
decl_stmt|;
DECL|field|skipLockedEntity
specifier|private
name|boolean
name|skipLockedEntity
decl_stmt|;
static|static
block|{
name|NOWAIT
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|NOWAIT
operator|.
name|put
argument_list|(
literal|"javax.persistence.lock.timeout"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
block|}
DECL|class|DataHolder
specifier|private
specifier|static
specifier|final
class|class
name|DataHolder
block|{
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|result
specifier|private
name|Object
name|result
decl_stmt|;
DECL|field|manager
specifier|private
name|EntityManager
name|manager
decl_stmt|;
DECL|method|DataHolder ()
specifier|private
name|DataHolder
parameter_list|()
block|{         }
block|}
DECL|method|JpaConsumer (JpaEndpoint endpoint, Processor processor)
specifier|public
name|JpaConsumer
parameter_list|(
name|JpaEndpoint
name|endpoint
parameter_list|,
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
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must reset for each poll
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
name|pendingExchanges
operator|=
literal|0
expr_stmt|;
name|Object
name|messagePolled
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
name|Queue
argument_list|<
name|DataHolder
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<
name|DataHolder
argument_list|>
argument_list|()
decl_stmt|;
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Created query {}"
argument_list|,
name|query
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Got result list from query {}"
argument_list|,
name|results
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|result
range|:
name|results
control|)
block|{
name|DataHolder
name|holder
init|=
operator|new
name|DataHolder
argument_list|()
decl_stmt|;
name|holder
operator|.
name|manager
operator|=
name|entityManager
expr_stmt|;
name|holder
operator|.
name|result
operator|=
name|result
expr_stmt|;
name|holder
operator|.
name|exchange
operator|=
name|createExchange
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|holder
argument_list|)
expr_stmt|;
block|}
name|PersistenceException
name|cause
init|=
literal|null
decl_stmt|;
name|int
name|messagePolled
init|=
literal|0
decl_stmt|;
try|try
block|{
name|messagePolled
operator|=
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|answer
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|PersistenceException
condition|)
block|{
name|cause
operator|=
operator|(
name|PersistenceException
operator|)
name|e
expr_stmt|;
block|}
else|else
block|{
name|cause
operator|=
operator|new
name|PersistenceException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isTransacted
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error processing last message due: {}. Will commit all previous successful processed message, and ignore this last failure."
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// rollback all by throwning exception
throw|throw
name|cause
throw|;
block|}
block|}
comment|// commit
name|LOG
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
return|return
name|messagePolled
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|messagePolled
argument_list|)
return|;
block|}
DECL|method|processBatch (Queue<Object> exchanges)
specifier|public
name|int
name|processBatch
parameter_list|(
name|Queue
argument_list|<
name|Object
argument_list|>
name|exchanges
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// limit if needed
if|if
condition|(
name|maxMessagesPerPoll
operator|>
literal|0
operator|&&
name|total
operator|>
name|maxMessagesPerPoll
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Limiting to maximum messages to poll "
operator|+
name|maxMessagesPerPoll
operator|+
literal|" as there was "
operator|+
name|total
operator|+
literal|" messages in this poll."
argument_list|)
expr_stmt|;
name|total
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|total
operator|&&
name|isBatchAllowed
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
comment|// only loop if we are started (allowed to run)
name|DataHolder
name|holder
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|DataHolder
operator|.
name|class
argument_list|,
name|exchanges
operator|.
name|poll
argument_list|()
argument_list|)
decl_stmt|;
name|EntityManager
name|entityManager
init|=
name|holder
operator|.
name|manager
decl_stmt|;
name|Exchange
name|exchange
init|=
name|holder
operator|.
name|exchange
decl_stmt|;
name|Object
name|result
init|=
name|holder
operator|.
name|result
decl_stmt|;
comment|// add current index and total as properties
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
name|index
operator|==
name|total
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// update pending number of exchanges
name|pendingExchanges
operator|=
name|total
operator|-
name|index
operator|-
literal|1
expr_stmt|;
if|if
condition|(
name|lockEntity
argument_list|(
name|result
argument_list|,
name|entityManager
argument_list|)
condition|)
block|{
comment|// Run the @PreConsumed callback
name|createPreDeleteHandler
argument_list|()
operator|.
name|deleteObject
argument_list|(
name|entityManager
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// process the current exchange
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing exchange: {}"
argument_list|,
name|exchange
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
comment|// if we failed then throw exception
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
comment|// Run the @Consumed callback
name|getDeleteHandler
argument_list|()
operator|.
name|deleteObject
argument_list|(
name|entityManager
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|total
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
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
DECL|method|getQueryFactory ()
specifier|public
name|QueryFactory
name|getQueryFactory
parameter_list|()
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
DECL|method|getDeleteHandler ()
specifier|public
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
name|getDeleteHandler
parameter_list|()
block|{
if|if
condition|(
name|deleteHandler
operator|==
literal|null
condition|)
block|{
name|deleteHandler
operator|=
name|createDeleteHandler
argument_list|()
expr_stmt|;
block|}
return|return
name|deleteHandler
return|;
block|}
DECL|method|setDeleteHandler (DeleteHandler<Object> deleteHandler)
specifier|public
name|void
name|setDeleteHandler
parameter_list|(
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
name|deleteHandler
parameter_list|)
block|{
name|this
operator|.
name|deleteHandler
operator|=
name|deleteHandler
expr_stmt|;
block|}
DECL|method|getPreDeleteHandler ()
specifier|public
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
name|getPreDeleteHandler
parameter_list|()
block|{
if|if
condition|(
name|preDeleteHandler
operator|==
literal|null
condition|)
block|{
name|preDeleteHandler
operator|=
name|createPreDeleteHandler
argument_list|()
expr_stmt|;
block|}
return|return
name|preDeleteHandler
return|;
block|}
DECL|method|setPreDeleteHandler (DeleteHandler<Object> preDeleteHandler)
specifier|public
name|void
name|setPreDeleteHandler
parameter_list|(
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
name|preDeleteHandler
parameter_list|)
block|{
name|this
operator|.
name|preDeleteHandler
operator|=
name|preDeleteHandler
expr_stmt|;
block|}
DECL|method|setParameters (Map<String, Object> params)
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
name|params
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|params
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
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|transacted
return|;
block|}
comment|/**      * Sets whether to run in transacted mode or not.      *<p/>      * This option is default<tt>false</tt>. When<tt>false</tt> then all the good messages      * will commit, and the first failed message will rollback.      * However when<tt>true</tt>, then all messages will rollback, if just one message failed.      */
DECL|method|setTransacted (boolean transacted)
specifier|public
name|void
name|setTransacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|this
operator|.
name|transacted
operator|=
name|transacted
expr_stmt|;
block|}
comment|/**      * Sets whether to use NOWAIT on lock and silently skip the entity. This      * allows different instances to process entities at the same time but not      * processing the same entity.      *       * @param skipLockedEntity      */
DECL|method|setSkipLockedEntity (boolean skipLockedEntity)
specifier|public
name|void
name|setSkipLockedEntity
parameter_list|(
name|boolean
name|skipLockedEntity
parameter_list|)
block|{
name|this
operator|.
name|skipLockedEntity
operator|=
name|skipLockedEntity
expr_stmt|;
block|}
DECL|method|isSkipLockedEntity ()
specifier|public
name|boolean
name|isSkipLockedEntity
parameter_list|()
block|{
return|return
name|skipLockedEntity
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * A strategy method to lock an object with an exclusive lock so that it can      * be processed      *       * @param entity the entity to be locked      * @param entityManager entity manager      * @return true if the entity was locked      */
DECL|method|lockEntity (Object entity, EntityManager entityManager)
specifier|protected
name|boolean
name|lockEntity
parameter_list|(
name|Object
name|entity
parameter_list|,
name|EntityManager
name|entityManager
parameter_list|)
block|{
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isConsumeDelete
argument_list|()
operator|||
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isConsumeLockEntity
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Acquiring exclusive lock on entity: {}"
argument_list|,
name|entity
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSkipLockedEntity
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|lock
argument_list|(
name|entity
argument_list|,
name|lockModeType
argument_list|,
name|NOWAIT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|entityManager
operator|.
name|lock
argument_list|(
name|entity
argument_list|,
name|lockModeType
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Failed to achieve lock on entity: "
operator|+
name|entity
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|instanceof
name|PessimisticLockException
operator|||
name|e
operator|instanceof
name|OptimisticLockException
condition|)
block|{
comment|//transaction marked as rollback can't continue gracefully
throw|throw
operator|(
name|PersistenceException
operator|)
name|e
throw|;
block|}
comment|//TODO: Find if possible an alternative way to handle results of native queries.
comment|//Result of native queries are Arrays and cannot be locked by all JPA Providers.
if|if
condition|(
name|entity
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
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
DECL|method|createPreDeleteHandler ()
specifier|protected
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
name|createPreDeleteHandler
parameter_list|()
block|{
comment|// Look for @PreConsumed to allow custom callback before the Entity has been consumed
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
operator|!=
literal|null
condition|)
block|{
comment|// Inspect the method(s) annotated with @PreConsumed
name|List
argument_list|<
name|Method
argument_list|>
name|methods
init|=
name|ObjectHelper
operator|.
name|findMethodsWithAnnotation
argument_list|(
name|entityType
argument_list|,
name|PreConsumed
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|methods
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Only one method can be annotated with the @PreConsumed annotation but found: "
operator|+
name|methods
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|methods
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// Inspect the parameters of the @PreConsumed method
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameters
init|=
name|methods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"@PreConsumed annotated method cannot have parameters!"
argument_list|)
throw|;
block|}
specifier|final
name|Method
name|method
init|=
name|methods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
operator|new
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|deleteObject
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|,
name|Object
name|entityBean
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|entityBean
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
comment|// else do nothing
return|return
operator|new
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|deleteObject
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|,
name|Object
name|entityBean
parameter_list|)
block|{
comment|// Do nothing
block|}
block|}
return|;
block|}
DECL|method|createDeleteHandler ()
specifier|protected
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
name|createDeleteHandler
parameter_list|()
block|{
comment|// look for @Consumed to allow custom callback when the Entity has been consumed
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
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Method
argument_list|>
name|methods
init|=
name|ObjectHelper
operator|.
name|findMethodsWithAnnotation
argument_list|(
name|entityType
argument_list|,
name|Consumed
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|methods
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only one method can be annotated with the @Consumed annotation but found: "
operator|+
name|methods
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|methods
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
specifier|final
name|Method
name|method
init|=
name|methods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
operator|new
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|deleteObject
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|,
name|Object
name|entityBean
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|entityBean
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isConsumeDelete
argument_list|()
condition|)
block|{
return|return
operator|new
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|deleteObject
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|,
name|Object
name|entityBean
parameter_list|)
block|{
name|entityManager
operator|.
name|remove
argument_list|(
name|entityBean
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|DeleteHandler
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|deleteObject
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|,
name|Object
name|entityBean
parameter_list|)
block|{
comment|// do nothing
block|}
block|}
return|;
block|}
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
DECL|method|createExchange (Object result)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|result
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
name|ENTITYMANAGER
argument_list|,
name|entityManager
argument_list|)
expr_stmt|;
return|return
name|exchange
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
name|this
operator|.
name|entityManager
operator|=
name|entityManagerFactory
operator|.
name|createEntityManager
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Created EntityManager {} on {}"
argument_list|,
name|entityManager
argument_list|,
name|this
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
comment|// noop
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
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
name|this
operator|.
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Closed EntityManager {} on {}"
argument_list|,
name|entityManager
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

