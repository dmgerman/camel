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
name|Collection
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
name|impl
operator|.
name|DefaultProducer
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JpaProducer
specifier|public
class|class
name|JpaProducer
extends|extends
name|DefaultProducer
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
name|JpaProducer
operator|.
name|class
argument_list|)
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
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
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
argument_list|)
decl_stmt|;
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
for|for
control|(
name|Object
name|element
range|:
name|array
control|)
block|{
name|save
argument_list|(
name|element
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
for|for
control|(
name|Object
name|entity
range|:
name|collection
control|)
block|{
name|save
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Object
name|managedEntity
init|=
name|save
argument_list|(
name|values
argument_list|)
decl_stmt|;
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
comment|/**                  * Save the given entity end return the managed entity                  *                  * @return the managed entity                  */
specifier|private
name|Object
name|save
parameter_list|(
specifier|final
name|Object
name|entity
parameter_list|)
block|{
name|LOG
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
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

