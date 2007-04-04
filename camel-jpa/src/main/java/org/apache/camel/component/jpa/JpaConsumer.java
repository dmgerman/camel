begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ScheduledExecutorService
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
name|PollingConsumer
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
name|orm
operator|.
name|jpa
operator|.
name|JpaCallback
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JpaConsumer
specifier|public
class|class
name|JpaConsumer
extends|extends
name|PollingConsumer
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JpaConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|JpaEndpoint
name|endpoint
decl_stmt|;
DECL|field|template
specifier|private
specifier|final
name|TransactionStrategy
name|template
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
DECL|method|JpaConsumer (JpaEndpoint endpoint, Processor<Exchange> processor, ScheduledExecutorService executor)
specifier|public
name|JpaConsumer
parameter_list|(
name|JpaEndpoint
name|endpoint
parameter_list|,
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|template
operator|=
name|endpoint
operator|.
name|createTransactionStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|execute
argument_list|(
operator|new
name|JpaCallback
argument_list|()
block|{
specifier|public
name|Object
name|doInJpa
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|)
throws|throws
name|PersistenceException
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
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|query
operator|.
name|getResultList
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|result
range|:
name|results
control|)
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
literal|"Processing new entity: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
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
comment|// lets turn the result into an exchange and fire it into the processor
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getEndpoint ()
specifier|public
name|JpaEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
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
DECL|method|setDeleteHandler (DeleteHandler deleteHandler)
specifier|public
name|void
name|setDeleteHandler
parameter_list|(
name|DeleteHandler
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
comment|/**      * A strategy method to lock an object with an exclusive lock so that it can be processed      *      * @param entity        the entity to be locked      * @param entityManager      * @return true if the entity was locked      */
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
try|try
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
literal|"Acquiring exclusive lock on entity: "
operator|+
name|entity
argument_list|)
expr_stmt|;
block|}
name|entityManager
operator|.
name|lock
argument_list|(
name|entity
argument_list|,
name|LockModeType
operator|.
name|WRITE
argument_list|)
expr_stmt|;
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
return|return
name|QueryBuilder
operator|.
name|nativeQuery
argument_list|(
name|nativeQuery
argument_list|)
return|;
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
init|=
name|endpoint
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
return|return
name|QueryBuilder
operator|.
name|query
argument_list|(
literal|"select x from "
operator|+
name|entityType
operator|.
name|getName
argument_list|()
operator|+
literal|" x"
argument_list|)
return|;
block|}
block|}
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
comment|// TODO auto-discover an annotation in the entity bean to indicate the process completed method call?
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
name|endpoint
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
name|endpoint
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
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

