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
name|CountDownLatch
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
name|examples
operator|.
name|Address
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
name|examples
operator|.
name|Customer
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_class
DECL|class|AbstractJpaMethodTest
specifier|public
specifier|abstract
class|class
name|AbstractJpaMethodTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|endpoint
specifier|protected
name|JpaEndpoint
name|endpoint
decl_stmt|;
DECL|field|entityManager
specifier|protected
name|EntityManager
name|entityManager
decl_stmt|;
DECL|field|transactionTemplate
specifier|protected
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|field|consumer
specifier|protected
name|Consumer
name|consumer
decl_stmt|;
DECL|field|receivedExchange
specifier|protected
name|Exchange
name|receivedExchange
decl_stmt|;
DECL|method|usePersist ()
specifier|abstract
name|boolean
name|usePersist
parameter_list|()
function_decl|;
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|entityManager
operator|!=
literal|null
condition|)
block|{
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|produceNewEntity ()
specifier|public
name|void
name|produceNewEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|setUp
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?usePersist="
operator|+
operator|(
name|usePersist
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
operator|)
argument_list|)
expr_stmt|;
name|Customer
name|customer
init|=
name|createDefaultCustomer
argument_list|()
decl_stmt|;
name|Customer
name|receivedCustomer
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|customer
argument_list|,
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getName
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedCustomer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"select o from "
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" o"
argument_list|)
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Customer
name|persistedCustomer
init|=
operator|(
name|Customer
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getName
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getId
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|produceNewEntitiesFromList ()
specifier|public
name|void
name|produceNewEntitiesFromList
parameter_list|()
throws|throws
name|Exception
block|{
name|setUp
argument_list|(
literal|"jpa://"
operator|+
name|List
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?usePersist="
operator|+
operator|(
name|usePersist
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
operator|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Customer
argument_list|>
name|customers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|customers
operator|.
name|add
argument_list|(
name|createDefaultCustomer
argument_list|()
argument_list|)
expr_stmt|;
name|customers
operator|.
name|add
argument_list|(
name|createDefaultCustomer
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|returnedCustomers
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|customers
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|returnedCustomers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|2
argument_list|,
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|2
argument_list|,
name|Address
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|produceNewEntitiesFromArray ()
specifier|public
name|void
name|produceNewEntitiesFromArray
parameter_list|()
throws|throws
name|Exception
block|{
name|setUp
argument_list|(
literal|"jpa://"
operator|+
name|Customer
index|[]
operator|.
expr|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?usePersist="
operator|+
operator|(
name|usePersist
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
operator|)
argument_list|)
expr_stmt|;
name|Customer
index|[]
name|customers
init|=
operator|new
name|Customer
index|[]
block|{
name|createDefaultCustomer
argument_list|()
block|,
name|createDefaultCustomer
argument_list|()
block|}
decl_stmt|;
name|Object
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|customers
argument_list|)
decl_stmt|;
name|Customer
index|[]
name|returnedCustomers
init|=
operator|(
name|Customer
index|[]
operator|)
name|reply
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|returnedCustomers
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|2
argument_list|,
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|2
argument_list|,
name|Address
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|consumeEntity ()
specifier|public
name|void
name|consumeEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|setUp
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?usePersist="
operator|+
operator|(
name|usePersist
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
operator|)
argument_list|)
expr_stmt|;
specifier|final
name|Customer
name|customer
init|=
name|createDefaultCustomer
argument_list|()
decl_stmt|;
name|save
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|1
argument_list|,
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|1
argument_list|,
name|Address
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|consumer
operator|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|e
parameter_list|)
block|{
name|receivedExchange
operator|=
name|e
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JpaConstants
operator|.
name|ENTITY_MANAGER
argument_list|,
name|EntityManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|50
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedExchange
argument_list|)
expr_stmt|;
name|Customer
name|receivedCustomer
init|=
name|receivedExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getName
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getId
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// give a bit time for consumer to delete after done
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|0
argument_list|,
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|0
argument_list|,
name|Address
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setUp (String endpointUri)
specifier|protected
name|void
name|setUp
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|Exception
block|{
name|endpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|JpaEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|transactionTemplate
operator|=
name|endpoint
operator|.
name|createTransactionTemplate
argument_list|()
expr_stmt|;
name|entityManager
operator|=
name|endpoint
operator|.
name|getEntityManagerFactory
argument_list|()
operator|.
name|createEntityManager
argument_list|()
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
name|entityManager
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"delete from "
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|0
argument_list|,
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|0
argument_list|,
name|Address
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|save (final Object persistable)
specifier|protected
name|void
name|save
parameter_list|(
specifier|final
name|Object
name|persistable
parameter_list|)
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
name|entityManager
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|persist
argument_list|(
name|persistable
argument_list|)
expr_stmt|;
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
DECL|method|assertEntitiesInDatabase (int count, String entity)
specifier|protected
name|void
name|assertEntitiesInDatabase
parameter_list|(
name|int
name|count
parameter_list|,
name|String
name|entity
parameter_list|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"select o from "
operator|+
name|entity
operator|+
literal|" o"
argument_list|)
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|count
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createDefaultCustomer ()
specifier|protected
name|Customer
name|createDefaultCustomer
parameter_list|()
block|{
name|Customer
name|customer
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Christian Mueller"
argument_list|)
expr_stmt|;
name|Address
name|address
init|=
operator|new
name|Address
argument_list|()
decl_stmt|;
name|address
operator|.
name|setAddressLine1
argument_list|(
literal|"Hahnstr. 1"
argument_list|)
expr_stmt|;
name|address
operator|.
name|setAddressLine2
argument_list|(
literal|"60313 Frankfurt am Main"
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
return|return
name|customer
return|;
block|}
block|}
end_class

end_unit

