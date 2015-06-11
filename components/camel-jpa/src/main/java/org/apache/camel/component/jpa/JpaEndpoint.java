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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|InvalidPayloadException
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
name|InvalidPayloadRuntimeException
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
name|Producer
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
name|ScheduledPollEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|ExpressionAdapter
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
name|IntrospectionSupport
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
name|orm
operator|.
name|jpa
operator|.
name|LocalEntityManagerFactoryBean
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
name|SharedEntityManagerCreator
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
name|PlatformTransactionManager
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
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"jpa"
argument_list|,
name|title
operator|=
literal|"JPA"
argument_list|,
name|syntax
operator|=
literal|"jpa:entityType"
argument_list|,
name|consumerClass
operator|=
name|JpaConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"database,sql"
argument_list|)
DECL|class|JpaEndpoint
specifier|public
class|class
name|JpaEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
DECL|field|entityManagerFactory
specifier|private
name|EntityManagerFactory
name|entityManagerFactory
decl_stmt|;
DECL|field|transactionManager
specifier|private
name|PlatformTransactionManager
name|transactionManager
decl_stmt|;
DECL|field|producerExpression
specifier|private
name|Expression
name|producerExpression
decl_stmt|;
DECL|field|entityManagerProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entityManagerProperties
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Entity class name"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|entityType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"camel"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|persistenceUnit
specifier|private
name|String
name|persistenceUnit
init|=
literal|"camel"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|joinTransaction
specifier|private
name|boolean
name|joinTransaction
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sharedEntityManager
specifier|private
name|boolean
name|sharedEntityManager
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|maximumResults
specifier|private
name|int
name|maximumResults
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|consumeDelete
specifier|private
name|boolean
name|consumeDelete
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|consumeLockEntity
specifier|private
name|boolean
name|consumeLockEntity
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|flushOnSend
specifier|private
name|boolean
name|flushOnSend
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|usePersist
specifier|private
name|boolean
name|usePersist
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|usePassedInEntityManager
specifier|private
name|boolean
name|usePassedInEntityManager
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|isRemove
specifier|private
name|boolean
name|isRemove
decl_stmt|;
DECL|method|JpaEndpoint ()
specifier|public
name|JpaEndpoint
parameter_list|()
block|{     }
comment|/**      * @deprecated use {@link JpaEndpoint#JpaEndpoint(String, JpaComponent)} instead      */
annotation|@
name|Deprecated
DECL|method|JpaEndpoint (String endpointUri)
specifier|public
name|JpaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|JpaEndpoint (String uri, JpaComponent component)
specifier|public
name|JpaEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JpaComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|entityManagerFactory
operator|=
name|component
operator|.
name|getEntityManagerFactory
argument_list|()
expr_stmt|;
name|transactionManager
operator|=
name|component
operator|.
name|getTransactionManager
argument_list|()
expr_stmt|;
block|}
comment|/**      * @deprecated use {@link JpaEndpoint#JpaEndpoint(String, JpaComponent)} instead      */
annotation|@
name|Deprecated
DECL|method|JpaEndpoint (String endpointUri, EntityManagerFactory entityManagerFactory)
specifier|public
name|JpaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityManagerFactory
operator|=
name|entityManagerFactory
expr_stmt|;
block|}
comment|/**      * @deprecated use {@link JpaEndpoint#JpaEndpoint(String, JpaComponent)} instead      */
annotation|@
name|Deprecated
DECL|method|JpaEndpoint (String endpointUri, EntityManagerFactory entityManagerFactory, PlatformTransactionManager transactionManager)
specifier|public
name|JpaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|,
name|PlatformTransactionManager
name|transactionManager
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityManagerFactory
operator|=
name|entityManagerFactory
expr_stmt|;
name|this
operator|.
name|transactionManager
operator|=
name|transactionManager
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|validate
argument_list|()
expr_stmt|;
return|return
operator|new
name|JpaProducer
argument_list|(
name|this
argument_list|,
name|getProducerExpression
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|validate
argument_list|()
expr_stmt|;
name|JpaConsumer
name|consumer
init|=
operator|new
name|JpaConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|configureProperties (Map<String, Object> options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|super
operator|.
name|configureProperties
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|emProperties
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|options
argument_list|,
literal|"emf."
argument_list|)
decl_stmt|;
if|if
condition|(
name|emProperties
operator|!=
literal|null
condition|)
block|{
name|setEntityManagerProperties
argument_list|(
name|emProperties
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"jpa"
operator|+
operator|(
name|entityType
operator|!=
literal|null
condition|?
literal|"://"
operator|+
name|entityType
operator|.
name|getName
argument_list|()
else|:
literal|""
operator|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getProducerExpression ()
specifier|public
name|Expression
name|getProducerExpression
parameter_list|()
block|{
if|if
condition|(
name|producerExpression
operator|==
literal|null
condition|)
block|{
name|producerExpression
operator|=
name|createProducerExpression
argument_list|()
expr_stmt|;
block|}
return|return
name|producerExpression
return|;
block|}
DECL|method|setProducerExpression (Expression producerExpression)
specifier|public
name|void
name|setProducerExpression
parameter_list|(
name|Expression
name|producerExpression
parameter_list|)
block|{
name|this
operator|.
name|producerExpression
operator|=
name|producerExpression
expr_stmt|;
block|}
DECL|method|getMaximumResults ()
specifier|public
name|int
name|getMaximumResults
parameter_list|()
block|{
return|return
name|maximumResults
return|;
block|}
comment|/**      * Set the maximum number of results to retrieve on the Query.      */
DECL|method|setMaximumResults (int maximumResults)
specifier|public
name|void
name|setMaximumResults
parameter_list|(
name|int
name|maximumResults
parameter_list|)
block|{
name|this
operator|.
name|maximumResults
operator|=
name|maximumResults
expr_stmt|;
block|}
DECL|method|getEntityType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getEntityType
parameter_list|()
block|{
return|return
name|entityType
return|;
block|}
comment|/**      * The JPA annotated class to use as entity.      */
DECL|method|setEntityType (Class<?> entityType)
specifier|public
name|void
name|setEntityType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
parameter_list|)
block|{
name|this
operator|.
name|entityType
operator|=
name|entityType
expr_stmt|;
block|}
DECL|method|getEntityManagerFactory ()
specifier|public
name|EntityManagerFactory
name|getEntityManagerFactory
parameter_list|()
block|{
if|if
condition|(
name|entityManagerFactory
operator|==
literal|null
condition|)
block|{
name|entityManagerFactory
operator|=
name|createEntityManagerFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|entityManagerFactory
return|;
block|}
comment|/**      * The {@link EntityManagerFactory} to use.      */
DECL|method|setEntityManagerFactory (EntityManagerFactory entityManagerFactory)
specifier|public
name|void
name|setEntityManagerFactory
parameter_list|(
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|)
block|{
name|this
operator|.
name|entityManagerFactory
operator|=
name|entityManagerFactory
expr_stmt|;
block|}
DECL|method|getTransactionManager ()
specifier|public
name|PlatformTransactionManager
name|getTransactionManager
parameter_list|()
block|{
if|if
condition|(
name|transactionManager
operator|==
literal|null
condition|)
block|{
name|transactionManager
operator|=
name|createTransactionManager
argument_list|()
expr_stmt|;
block|}
return|return
name|transactionManager
return|;
block|}
comment|/**      * To use the {@link PlatformTransactionManager} for managing transactions.      */
DECL|method|setTransactionManager (PlatformTransactionManager transactionManager)
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|PlatformTransactionManager
name|transactionManager
parameter_list|)
block|{
name|this
operator|.
name|transactionManager
operator|=
name|transactionManager
expr_stmt|;
block|}
comment|/**      * Additional properties for the entity manager to use.      */
DECL|method|getEntityManagerProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getEntityManagerProperties
parameter_list|()
block|{
if|if
condition|(
name|entityManagerProperties
operator|==
literal|null
condition|)
block|{
name|entityManagerProperties
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|entityManagerProperties
return|;
block|}
DECL|method|setEntityManagerProperties (Map<String, Object> entityManagerProperties)
specifier|public
name|void
name|setEntityManagerProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entityManagerProperties
parameter_list|)
block|{
name|this
operator|.
name|entityManagerProperties
operator|=
name|entityManagerProperties
expr_stmt|;
block|}
DECL|method|getPersistenceUnit ()
specifier|public
name|String
name|getPersistenceUnit
parameter_list|()
block|{
return|return
name|persistenceUnit
return|;
block|}
comment|/**      * The JPA persistence unit used by default.      */
DECL|method|setPersistenceUnit (String persistenceUnit)
specifier|public
name|void
name|setPersistenceUnit
parameter_list|(
name|String
name|persistenceUnit
parameter_list|)
block|{
name|this
operator|.
name|persistenceUnit
operator|=
name|persistenceUnit
expr_stmt|;
block|}
DECL|method|isConsumeDelete ()
specifier|public
name|boolean
name|isConsumeDelete
parameter_list|()
block|{
return|return
name|consumeDelete
return|;
block|}
comment|/**      * If true, the entity is deleted after it is consumed; if false, the entity is not deleted.      */
DECL|method|setConsumeDelete (boolean consumeDelete)
specifier|public
name|void
name|setConsumeDelete
parameter_list|(
name|boolean
name|consumeDelete
parameter_list|)
block|{
name|this
operator|.
name|consumeDelete
operator|=
name|consumeDelete
expr_stmt|;
block|}
DECL|method|isConsumeLockEntity ()
specifier|public
name|boolean
name|isConsumeLockEntity
parameter_list|()
block|{
return|return
name|consumeLockEntity
return|;
block|}
comment|/**      * Specifies whether or not to set an exclusive lock on each entity bean while processing the results from polling.      */
DECL|method|setConsumeLockEntity (boolean consumeLockEntity)
specifier|public
name|void
name|setConsumeLockEntity
parameter_list|(
name|boolean
name|consumeLockEntity
parameter_list|)
block|{
name|this
operator|.
name|consumeLockEntity
operator|=
name|consumeLockEntity
expr_stmt|;
block|}
DECL|method|isFlushOnSend ()
specifier|public
name|boolean
name|isFlushOnSend
parameter_list|()
block|{
return|return
name|flushOnSend
return|;
block|}
comment|/**      * Flushes the EntityManager after the entity bean has been persisted.      */
DECL|method|setFlushOnSend (boolean flushOnSend)
specifier|public
name|void
name|setFlushOnSend
parameter_list|(
name|boolean
name|flushOnSend
parameter_list|)
block|{
name|this
operator|.
name|flushOnSend
operator|=
name|flushOnSend
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
comment|/**      * An integer value to define the maximum number of messages to gather per poll.      * By default, no maximum is set. Can be used to avoid polling many thousands of messages when starting up the server.      * Set a value of 0 or negative to disable.      */
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|isUsePersist ()
specifier|public
name|boolean
name|isUsePersist
parameter_list|()
block|{
return|return
name|usePersist
return|;
block|}
comment|/**      * Indicates to use entityManager.persist(entity) instead of entityManager.merge(entity).      * Note: entityManager.persist(entity) doesn't work for detached entities      * (where the EntityManager has to execute an UPDATE instead of an INSERT query)!      */
DECL|method|setUsePersist (boolean usePersist)
specifier|public
name|void
name|setUsePersist
parameter_list|(
name|boolean
name|usePersist
parameter_list|)
block|{
name|this
operator|.
name|usePersist
operator|=
name|usePersist
expr_stmt|;
block|}
DECL|method|isRemove ()
specifier|public
name|boolean
name|isRemove
parameter_list|()
block|{
return|return
name|isRemove
return|;
block|}
DECL|method|setRemove (boolean isRemove)
specifier|public
name|void
name|setRemove
parameter_list|(
name|boolean
name|isRemove
parameter_list|)
block|{
name|this
operator|.
name|isRemove
operator|=
name|isRemove
expr_stmt|;
block|}
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
comment|/**      * The camel-jpa component will join transaction by default.      * You can use this option to turn this off, for example if you use LOCAL_RESOURCE and join transaction      * doesn't work with your JPA provider. This option can also be set globally on the JpaComponent,      * instead of having to set it on all endpoints.      */
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
DECL|method|isUsePassedInEntityManager ()
specifier|public
name|boolean
name|isUsePassedInEntityManager
parameter_list|()
block|{
return|return
name|this
operator|.
name|usePassedInEntityManager
return|;
block|}
comment|/**      * If set to true, then Camel will use the EntityManager from the header      * JpaConstants.ENTITYMANAGER instead of the configured entity manager on the component/endpoint.      * This allows end users to control which entity manager will be in use.      */
DECL|method|setUsePassedInEntityManager (boolean usePassedIn)
specifier|public
name|void
name|setUsePassedInEntityManager
parameter_list|(
name|boolean
name|usePassedIn
parameter_list|)
block|{
name|this
operator|.
name|usePassedInEntityManager
operator|=
name|usePassedIn
expr_stmt|;
block|}
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
comment|/**      * Whether to use Spring's SharedEntityManager for the consumer/producer.      * Note in most cases joinTransaction should be set to false as this is not an EXTENDED EntityManager.      */
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
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|validate ()
specifier|protected
name|void
name|validate
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getEntityManagerFactory
argument_list|()
argument_list|,
literal|"entityManagerFactory"
argument_list|)
expr_stmt|;
block|}
DECL|method|createEntityManagerFactory ()
specifier|protected
name|EntityManagerFactory
name|createEntityManagerFactory
parameter_list|()
block|{
name|LocalEntityManagerFactoryBean
name|emfBean
init|=
operator|new
name|LocalEntityManagerFactoryBean
argument_list|()
decl_stmt|;
name|emfBean
operator|.
name|setPersistenceUnitName
argument_list|(
name|persistenceUnit
argument_list|)
expr_stmt|;
name|emfBean
operator|.
name|setJpaPropertyMap
argument_list|(
name|getEntityManagerProperties
argument_list|()
argument_list|)
expr_stmt|;
name|emfBean
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
return|return
name|emfBean
operator|.
name|getObject
argument_list|()
return|;
block|}
DECL|method|createTransactionManager ()
specifier|protected
name|PlatformTransactionManager
name|createTransactionManager
parameter_list|()
block|{
name|JpaTransactionManager
name|tm
init|=
operator|new
name|JpaTransactionManager
argument_list|(
name|getEntityManagerFactory
argument_list|()
argument_list|)
decl_stmt|;
name|tm
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
return|return
name|tm
return|;
block|}
comment|/**      * @deprecated use {@link #getEntityManagerFactory()} to get hold of factory and create an entity manager using the factory.      */
annotation|@
name|Deprecated
DECL|method|createEntityManager ()
specifier|protected
name|EntityManager
name|createEntityManager
parameter_list|()
block|{
if|if
condition|(
name|sharedEntityManager
condition|)
block|{
return|return
name|SharedEntityManagerCreator
operator|.
name|createSharedEntityManager
argument_list|(
name|getEntityManagerFactory
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getEntityManagerFactory
argument_list|()
operator|.
name|createEntityManager
argument_list|()
return|;
block|}
block|}
DECL|method|createTransactionTemplate ()
specifier|protected
name|TransactionTemplate
name|createTransactionTemplate
parameter_list|()
block|{
name|TransactionTemplate
name|transactionTemplate
init|=
operator|new
name|TransactionTemplate
argument_list|(
name|getTransactionManager
argument_list|()
argument_list|)
decl_stmt|;
name|transactionTemplate
operator|.
name|setPropagationBehavior
argument_list|(
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRED
argument_list|)
expr_stmt|;
name|transactionTemplate
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
return|return
name|transactionTemplate
return|;
block|}
DECL|method|createProducerExpression ()
specifier|protected
name|Expression
name|createProducerExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|answer
decl_stmt|;
comment|// must have a body
try|try
block|{
if|if
condition|(
name|getEntityType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|getEntityType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidPayloadException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidPayloadRuntimeException
argument_list|(
name|exchange
argument_list|,
name|getEntityType
argument_list|()
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
comment|// is never null
return|return
name|answer
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

