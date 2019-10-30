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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Expression
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
name|Message
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
name|language
operator|.
name|simple
operator|.
name|SimpleLanguage
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
name|DefaultProducer
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
DECL|class|JpaProducer
specifier|public
class|class
name|JpaProducer
extends|extends
name|DefaultProducer
block|{
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
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
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
DECL|field|findEntity
specifier|private
name|boolean
name|findEntity
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
DECL|field|queryFactory
specifier|private
name|QueryFactory
name|queryFactory
decl_stmt|;
DECL|field|useExecuteUpdate
specifier|private
name|Boolean
name|useExecuteUpdate
decl_stmt|;
DECL|method|JpaProducer (JpaEndpoint endpoint, Expression expression)
specifier|public
name|JpaProducer
parameter_list|(
name|JpaEndpoint
name|endpoint
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
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
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|queryFactory
operator|=
name|QueryBuilder
operator|.
name|query
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|namedQuery
operator|!=
literal|null
condition|)
block|{
name|queryFactory
operator|=
name|QueryBuilder
operator|.
name|namedQuery
argument_list|(
name|namedQuery
argument_list|)
expr_stmt|;
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
name|queryFactory
operator|=
name|QueryBuilder
operator|.
name|nativeQuery
argument_list|(
name|nativeQuery
argument_list|,
name|resultClass
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|queryFactory
operator|=
name|QueryBuilder
operator|.
name|nativeQuery
argument_list|(
name|nativeQuery
argument_list|)
expr_stmt|;
block|}
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
DECL|method|isFindEntity ()
specifier|public
name|boolean
name|isFindEntity
parameter_list|()
block|{
return|return
name|findEntity
return|;
block|}
DECL|method|setFindEntity (boolean findEntity)
specifier|public
name|void
name|setFindEntity
parameter_list|(
name|boolean
name|findEntity
parameter_list|)
block|{
name|this
operator|.
name|findEntity
operator|=
name|findEntity
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
DECL|method|setUseExecuteUpdate (Boolean executeUpdate)
specifier|public
name|void
name|setUseExecuteUpdate
parameter_list|(
name|Boolean
name|executeUpdate
parameter_list|)
block|{
name|this
operator|.
name|useExecuteUpdate
operator|=
name|executeUpdate
expr_stmt|;
block|}
DECL|method|isUseExecuteUpdate ()
specifier|public
name|boolean
name|isUseExecuteUpdate
parameter_list|()
block|{
if|if
condition|(
name|useExecuteUpdate
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|query
operator|.
name|regionMatches
argument_list|(
literal|true
argument_list|,
literal|0
argument_list|,
literal|"select"
argument_list|,
literal|0
argument_list|,
literal|6
argument_list|)
condition|)
block|{
name|useExecuteUpdate
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|useExecuteUpdate
operator|=
literal|true
expr_stmt|;
block|}
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
name|nativeQuery
operator|.
name|regionMatches
argument_list|(
literal|true
argument_list|,
literal|0
argument_list|,
literal|"select"
argument_list|,
literal|0
argument_list|,
literal|6
argument_list|)
condition|)
block|{
name|useExecuteUpdate
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|useExecuteUpdate
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|useExecuteUpdate
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|useExecuteUpdate
return|;
block|}
annotation|@
name|Override
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
comment|// resolve the entity manager before evaluating the expression
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
if|if
condition|(
name|findEntity
condition|)
block|{
name|processFind
argument_list|(
name|exchange
argument_list|,
name|entityManager
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getQueryFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processQuery
argument_list|(
name|exchange
argument_list|,
name|entityManager
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processEntity
argument_list|(
name|exchange
argument_list|,
name|entityManager
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processQuery (Exchange exchange, EntityManager entityManager)
specifier|protected
name|void
name|processQuery
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EntityManager
name|entityManager
parameter_list|)
block|{
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
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
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
name|Object
name|answer
init|=
name|isUseExecuteUpdate
argument_list|()
condition|?
name|query
operator|.
name|executeUpdate
argument_list|()
else|:
name|query
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|Message
name|target
init|=
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|target
operator|.
name|setBody
argument_list|(
name|answer
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isFlushOnSend
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|configureParameters (Query query, Exchange exchange)
specifier|private
name|void
name|configureParameters
parameter_list|(
name|Query
name|query
parameter_list|,
name|Exchange
name|exchange
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
comment|// setup the parameters
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|params
decl_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
name|params
operator|=
name|parameters
expr_stmt|;
block|}
else|else
block|{
name|params
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JpaConstants
operator|.
name|JPA_PARAMETERS_HEADER
argument_list|,
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|forEach
argument_list|(
parameter_list|(
name|key
parameter_list|,
name|value
parameter_list|)
lambda|->
block|{
name|Object
name|resolvedValue
init|=
name|value
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|resolvedValue
operator|=
name|SimpleLanguage
operator|.
name|expression
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|setParameter
argument_list|(
name|key
argument_list|,
name|resolvedValue
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processFind (Exchange exchange, EntityManager entityManager)
specifier|protected
name|void
name|processFind
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EntityManager
name|entityManager
parameter_list|)
block|{
specifier|final
name|Object
name|key
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
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
name|Object
name|answer
init|=
name|entityManager
operator|.
name|find
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEntityType
argument_list|()
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Find: {} -> {}"
argument_list|,
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|Message
name|target
init|=
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|target
operator|.
name|setBody
argument_list|(
name|answer
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isFlushOnSend
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processEntity (Exchange exchange, EntityManager entityManager)
specifier|protected
name|void
name|processEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EntityManager
name|entityManager
parameter_list|)
block|{
specifier|final
name|Object
name|values
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|!=
literal|null
condition|)
block|{
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
if|if
condition|(
name|values
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|Object
index|[]
name|array
init|=
operator|(
name|Object
index|[]
operator|)
name|values
decl_stmt|;
comment|// need to create an array to store returned values as they can be updated
comment|// by JPA such as setting auto assigned ids
name|Object
index|[]
name|managedArray
init|=
operator|new
name|Object
index|[
name|array
operator|.
name|length
index|]
decl_stmt|;
name|Object
name|managedEntity
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|element
init|=
name|array
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isRemove
argument_list|()
condition|)
block|{
name|managedEntity
operator|=
name|save
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|managedEntity
operator|=
name|remove
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
name|managedArray
index|[
name|i
index|]
operator|=
name|managedEntity
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isUsePersist
argument_list|()
condition|)
block|{
comment|// and copy back to original array
name|System
operator|.
name|arraycopy
argument_list|(
name|managedArray
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
literal|0
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|values
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|values
decl_stmt|;
comment|// need to create a list to store returned values as they can be updated
comment|// by JPA such as setting auto assigned ids
name|Collection
name|managedCollection
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|collection
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|managedEntity
decl_stmt|;
for|for
control|(
name|Object
name|entity
range|:
name|collection
control|)
block|{
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isRemove
argument_list|()
condition|)
block|{
name|managedEntity
operator|=
name|save
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|managedEntity
operator|=
name|remove
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
name|managedCollection
operator|.
name|add
argument_list|(
name|managedEntity
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isUsePersist
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|managedCollection
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Object
name|managedEntity
decl_stmt|;
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isRemove
argument_list|()
condition|)
block|{
name|managedEntity
operator|=
name|save
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|managedEntity
operator|=
name|remove
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isUsePersist
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|managedEntity
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isFlushOnSend
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**                  * Save the given entity and return the managed entity                  *                  * @return the managed entity                  */
specifier|private
name|Object
name|save
parameter_list|(
specifier|final
name|Object
name|entity
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"save: {}"
argument_list|,
name|entity
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isUsePersist
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|persist
argument_list|(
name|entity
argument_list|)
expr_stmt|;
return|return
name|entity
return|;
block|}
else|else
block|{
return|return
name|entityManager
operator|.
name|merge
argument_list|(
name|entity
argument_list|)
return|;
block|}
block|}
comment|/**                  * Remove the given entity and return the managed entity                  *                  * @return the managed entity                  */
specifier|private
name|Object
name|remove
parameter_list|(
specifier|final
name|Object
name|entity
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"remove: {}"
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|Object
name|managedEntity
decl_stmt|;
comment|// First check if entity is attached to the persistence context
if|if
condition|(
name|entityManager
operator|.
name|contains
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|managedEntity
operator|=
name|entity
expr_stmt|;
block|}
else|else
block|{
comment|// If not, merge entity state into context before removing it
name|managedEntity
operator|=
name|entityManager
operator|.
name|merge
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
name|entityManager
operator|.
name|remove
argument_list|(
name|managedEntity
argument_list|)
expr_stmt|;
return|return
name|managedEntity
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

