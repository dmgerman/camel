begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.etl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|etl
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
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
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
name|Converter
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
name|component
operator|.
name|jpa
operator|.
name|JpaConstants
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
comment|/**  * A Message Transformer of an XML document to a Customer entity bean  *   * @version   */
end_comment

begin_comment
comment|// START SNIPPET: example
end_comment

begin_class
annotation|@
name|Converter
DECL|class|CustomerTransformer
specifier|public
specifier|final
class|class
name|CustomerTransformer
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
name|CustomerTransformer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|CustomerTransformer ()
specifier|private
name|CustomerTransformer
parameter_list|()
block|{     }
comment|/**      * A transformation method to convert a person document into a customer      * entity      */
annotation|@
name|Converter
DECL|method|toCustomer (PersonDocument doc, Exchange exchange)
specifier|public
specifier|static
name|CustomerEntity
name|toCustomer
parameter_list|(
name|PersonDocument
name|doc
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|EntityManager
name|entityManager
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JpaConstants
operator|.
name|ENTITYMANAGER
argument_list|,
name|EntityManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|TransactionTemplate
name|transactionTemplate
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"transactionTemplate"
argument_list|,
name|TransactionTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|doc
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|CustomerEntity
name|customer
init|=
name|findCustomerByName
argument_list|(
name|transactionTemplate
argument_list|,
name|entityManager
argument_list|,
name|user
argument_list|)
decl_stmt|;
comment|// let's convert information from the document into the entity bean
name|customer
operator|.
name|setUserName
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setFirstName
argument_list|(
name|doc
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setSurname
argument_list|(
name|doc
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setCity
argument_list|(
name|doc
operator|.
name|getCity
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Created object customer: {}"
argument_list|,
name|customer
argument_list|)
expr_stmt|;
return|return
name|customer
return|;
block|}
comment|/**      * Finds a customer for the given username      */
DECL|method|findCustomerByName (TransactionTemplate transactionTemplate, final EntityManager entityManager, final String userName)
specifier|private
specifier|static
name|CustomerEntity
name|findCustomerByName
parameter_list|(
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
specifier|final
name|EntityManager
name|entityManager
parameter_list|,
specifier|final
name|String
name|userName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|<
name|CustomerEntity
argument_list|>
argument_list|()
block|{
specifier|public
name|CustomerEntity
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
name|entityManager
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|CustomerEntity
argument_list|>
name|list
init|=
name|entityManager
operator|.
name|createNamedQuery
argument_list|(
literal|"findCustomerByUsername"
argument_list|,
name|CustomerEntity
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"userName"
argument_list|,
name|userName
argument_list|)
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|CustomerEntity
name|answer
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|answer
operator|=
operator|new
name|CustomerEntity
argument_list|()
expr_stmt|;
name|answer
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Created a new CustomerEntity {} as no matching persisted entity found."
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found a matching CustomerEntity {} having the userName {}."
argument_list|,
name|answer
argument_list|,
name|userName
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

