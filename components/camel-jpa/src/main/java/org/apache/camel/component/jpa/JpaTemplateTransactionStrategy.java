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
name|PersistenceException
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
comment|/**  * Delegates the strategy to the {@link JpaTemplate} and {@link TransactionTemplate} for transaction handling  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JpaTemplateTransactionStrategy
specifier|public
class|class
name|JpaTemplateTransactionStrategy
implements|implements
name|TransactionStrategy
block|{
DECL|field|jpaTemplate
specifier|private
specifier|final
name|JpaTemplate
name|jpaTemplate
decl_stmt|;
DECL|field|transactionTemplate
specifier|private
specifier|final
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|method|JpaTemplateTransactionStrategy (JpaTemplate jpaTemplate, TransactionTemplate transactionTemplate)
specifier|public
name|JpaTemplateTransactionStrategy
parameter_list|(
name|JpaTemplate
name|jpaTemplate
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|)
block|{
name|this
operator|.
name|jpaTemplate
operator|=
name|jpaTemplate
expr_stmt|;
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
block|}
comment|/**      * Creates a new implementation from the given JPA factory      */
DECL|method|newInstance (EntityManagerFactory emf)
specifier|public
specifier|static
name|JpaTemplateTransactionStrategy
name|newInstance
parameter_list|(
name|EntityManagerFactory
name|emf
parameter_list|)
block|{
name|JpaTemplate
name|template
init|=
operator|new
name|JpaTemplate
argument_list|(
name|emf
argument_list|)
decl_stmt|;
return|return
name|newInstance
argument_list|(
name|emf
argument_list|,
name|template
argument_list|)
return|;
block|}
comment|/**      * Creates a new implementation from the given JPA factory and JPA template      */
DECL|method|newInstance (EntityManagerFactory emf, JpaTemplate template)
specifier|public
specifier|static
name|JpaTemplateTransactionStrategy
name|newInstance
parameter_list|(
name|EntityManagerFactory
name|emf
parameter_list|,
name|JpaTemplate
name|template
parameter_list|)
block|{
name|JpaTransactionManager
name|transactionManager
init|=
operator|new
name|JpaTransactionManager
argument_list|(
name|emf
argument_list|)
decl_stmt|;
name|transactionManager
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|TransactionTemplate
name|tranasctionTemplate
init|=
operator|new
name|TransactionTemplate
argument_list|(
name|transactionManager
argument_list|)
decl_stmt|;
name|tranasctionTemplate
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
return|return
operator|new
name|JpaTemplateTransactionStrategy
argument_list|(
name|template
argument_list|,
name|tranasctionTemplate
argument_list|)
return|;
block|}
comment|/**      * Creates a new implementation from the given Transaction Manager and JPA template      */
DECL|method|newInstance (PlatformTransactionManager transactionManager, JpaTemplate template)
specifier|public
specifier|static
name|JpaTemplateTransactionStrategy
name|newInstance
parameter_list|(
name|PlatformTransactionManager
name|transactionManager
parameter_list|,
name|JpaTemplate
name|template
parameter_list|)
block|{
name|TransactionTemplate
name|tranasctionTemplate
init|=
operator|new
name|TransactionTemplate
argument_list|(
name|transactionManager
argument_list|)
decl_stmt|;
name|tranasctionTemplate
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
return|return
operator|new
name|JpaTemplateTransactionStrategy
argument_list|(
name|template
argument_list|,
name|tranasctionTemplate
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|execute (final JpaCallback callback)
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|JpaCallback
name|callback
parameter_list|)
block|{
return|return
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
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
return|return
name|jpaTemplate
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
return|return
name|callback
operator|.
name|doInJpa
argument_list|(
name|entityManager
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

