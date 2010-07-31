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
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|ProducerTemplate
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
name|impl
operator|.
name|DefaultCamelContext
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
name|DefaultExchange
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
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
operator|.
name|startServices
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
name|util
operator|.
name|ServiceHelper
operator|.
name|stopServices
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 931444 $  */
end_comment

begin_class
DECL|class|JpaUsePersistTest
specifier|public
class|class
name|JpaUsePersistTest
extends|extends
name|Assert
block|{
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|JpaEndpoint
name|endpoint
decl_stmt|;
DECL|field|transactionStrategy
specifier|protected
name|TransactionStrategy
name|transactionStrategy
decl_stmt|;
DECL|field|jpaTemplate
specifier|protected
name|JpaTemplate
name|jpaTemplate
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|startServices
argument_list|(
name|template
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
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
literal|"?usePersist=true"
argument_list|,
name|JpaEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|transactionStrategy
operator|=
name|endpoint
operator|.
name|createTransactionStrategy
argument_list|()
expr_stmt|;
name|jpaTemplate
operator|=
name|endpoint
operator|.
name|getTemplate
argument_list|()
expr_stmt|;
name|transactionStrategy
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
name|stopServices
argument_list|(
name|consumer
argument_list|,
name|template
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|Customer
name|customer
init|=
name|createDefaultCustomer
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|Exchange
name|returnedExchange
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|Customer
name|receivedCustomer
init|=
name|returnedExchange
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
name|results
init|=
name|jpaTemplate
operator|.
name|find
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|produceExistingEntityShouldThowAnException ()
specifier|public
name|void
name|produceExistingEntityShouldThowAnException
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Customer
name|customer
init|=
name|createDefaultCustomer
argument_list|()
decl_stmt|;
name|transactionStrategy
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
name|entityManager
operator|.
name|persist
argument_list|(
name|customer
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
name|customer
operator|.
name|setName
argument_list|(
literal|"Max Mustermann"
argument_list|)
expr_stmt|;
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|setAddressLine1
argument_list|(
literal|"Musterstr. 1"
argument_list|)
expr_stmt|;
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|setAddressLine2
argument_list|(
literal|"11111 Enterhausen"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|Exchange
name|returnedExchange
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|returnedExchange
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|returnedExchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
specifier|final
name|Customer
name|customer
init|=
name|createDefaultCustomer
argument_list|()
decl_stmt|;
name|transactionStrategy
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
name|entityManager
operator|.
name|persist
argument_list|(
name|customer
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
name|JPA_TEMPLATE
argument_list|,
name|JpaTemplate
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
name|boolean
name|received
init|=
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
decl_stmt|;
name|assertTrue
argument_list|(
name|received
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
comment|// give a bit tiem for consumer to delete after done
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|assertEntitiesInDatabase (int count, String entity)
specifier|private
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
name|results
init|=
name|jpaTemplate
operator|.
name|find
argument_list|(
literal|"select o from "
operator|+
name|entity
operator|+
literal|" o"
argument_list|)
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
specifier|private
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

