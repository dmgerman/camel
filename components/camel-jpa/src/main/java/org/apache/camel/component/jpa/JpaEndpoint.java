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
name|javax
operator|.
name|persistence
operator|.
name|Persistence
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
name|NoTypeConversionAvailableException
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
name|builder
operator|.
name|ExpressionBuilder
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
name|JpaTemplate
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JpaEndpoint
specifier|public
class|class
name|JpaEndpoint
extends|extends
name|ScheduledPollEndpoint
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|entityManagerFactory
specifier|private
name|EntityManagerFactory
name|entityManagerFactory
decl_stmt|;
DECL|field|persistenceUnit
specifier|private
name|String
name|persistenceUnit
init|=
literal|"camel"
decl_stmt|;
DECL|field|template
specifier|private
name|JpaTemplate
name|template
decl_stmt|;
DECL|field|producerExpression
specifier|private
name|Expression
name|producerExpression
decl_stmt|;
DECL|field|maximumResults
specifier|private
name|int
name|maximumResults
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|entityType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
decl_stmt|;
DECL|field|entityManagerProperties
specifier|private
name|Map
name|entityManagerProperties
decl_stmt|;
DECL|field|consumeDelete
specifier|private
name|boolean
name|consumeDelete
init|=
literal|true
decl_stmt|;
DECL|field|consumeLockEntity
specifier|private
name|boolean
name|consumeLockEntity
init|=
literal|true
decl_stmt|;
DECL|field|flushOnSend
specifier|private
name|boolean
name|flushOnSend
init|=
literal|true
decl_stmt|;
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
block|}
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
DECL|method|configureProperties (Map options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
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
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getTemplate ()
specifier|public
name|JpaTemplate
name|getTemplate
parameter_list|()
block|{
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|template
operator|=
name|createTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|template
return|;
block|}
DECL|method|setTemplate (JpaTemplate template)
specifier|public
name|void
name|setTemplate
parameter_list|(
name|JpaTemplate
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
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
DECL|method|getEntityManagerProperties ()
specifier|public
name|Map
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
name|System
operator|.
name|getProperties
argument_list|()
expr_stmt|;
block|}
return|return
name|entityManagerProperties
return|;
block|}
DECL|method|setEntityManagerProperties (Map entityManagerProperties)
specifier|public
name|void
name|setEntityManagerProperties
parameter_list|(
name|Map
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
literal|"entityManagerFactory property"
argument_list|)
expr_stmt|;
block|}
DECL|method|createTemplate ()
specifier|protected
name|JpaTemplate
name|createTemplate
parameter_list|()
block|{
return|return
operator|new
name|JpaTemplate
argument_list|(
name|getEntityManagerFactory
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createEntityManagerFactory ()
specifier|protected
name|EntityManagerFactory
name|createEntityManagerFactory
parameter_list|()
block|{
return|return
name|Persistence
operator|.
name|createEntityManagerFactory
argument_list|(
name|persistenceUnit
argument_list|,
name|getEntityManagerProperties
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createEntityManager ()
specifier|protected
name|EntityManager
name|createEntityManager
parameter_list|()
block|{
return|return
name|getEntityManagerFactory
argument_list|()
operator|.
name|createEntityManager
argument_list|()
return|;
block|}
DECL|method|createTransactionStrategy ()
specifier|protected
name|TransactionStrategy
name|createTransactionStrategy
parameter_list|()
block|{
name|EntityManagerFactory
name|emf
init|=
name|getEntityManagerFactory
argument_list|()
decl_stmt|;
return|return
name|JpaTemplateTransactionStrategy
operator|.
name|newInstance
argument_list|(
name|emf
argument_list|,
name|getTemplate
argument_list|()
argument_list|)
return|;
comment|// return new DefaultTransactionStrategy(emf);
block|}
DECL|method|createProducerExpression ()
specifier|protected
name|Expression
name|createProducerExpression
parameter_list|()
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|getEntityType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|Expression
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
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|Object
name|defaultValue
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoTypeConversionAvailableException
argument_list|(
name|defaultValue
argument_list|,
name|type
argument_list|)
throw|;
block|}
comment|// if we don't have a body then
comment|// lets instantiate and inject a new instance
name|answer
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

