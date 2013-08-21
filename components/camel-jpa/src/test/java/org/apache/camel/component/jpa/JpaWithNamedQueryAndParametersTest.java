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
name|HashMap
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
name|Endpoint
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
name|SimpleRegistry
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
name|ServiceHelper
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
name|Test
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
name|util
operator|.
name|ServiceHelper
operator|.
name|startServices
import|;
end_import

begin_class
DECL|class|JpaWithNamedQueryAndParametersTest
specifier|public
class|class
name|JpaWithNamedQueryAndParametersTest
extends|extends
name|Assert
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JpaWithNamedQueryAndParametersTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|DefaultCamelContext
name|camelContext
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
DECL|field|latch
specifier|protected
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|entityName
specifier|protected
name|String
name|entityName
init|=
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|queryText
specifier|protected
name|String
name|queryText
init|=
literal|"select o from "
operator|+
name|entityName
operator|+
literal|" o where o.name like 'Willem'"
decl_stmt|;
annotation|@
name|Test
DECL|method|testProducerInsertsIntoDatabaseThenConsumerFiresMessageExchange ()
specifier|public
name|void
name|testProducerInsertsIntoDatabaseThenConsumerFiresMessageExchange
parameter_list|()
throws|throws
name|Exception
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
comment|// lets delete any exiting records before the test
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"delete from "
operator|+
name|entityName
argument_list|)
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
comment|// now lets create a dummy entry
name|Customer
name|dummy
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|dummy
operator|.
name|setName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|persist
argument_list|(
name|dummy
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
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
name|queryText
argument_list|)
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should have no results: "
operator|+
name|results
argument_list|,
literal|0
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// lets produce some objects
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
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
literal|"Willem"
argument_list|)
expr_stmt|;
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
block|}
block|}
argument_list|)
expr_stmt|;
comment|// now lets assert that there is a result
name|results
operator|=
name|entityManager
operator|.
name|createQuery
argument_list|(
name|queryText
argument_list|)
operator|.
name|getResultList
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have results: "
operator|+
name|results
argument_list|,
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Customer
name|customer
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
literal|"name property"
argument_list|,
literal|"Willem"
argument_list|,
name|customer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// now lets create a consumer to consume it
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Received exchange: "
operator|+
name|e
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|receivedExchange
operator|=
name|e
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
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertReceivedResult
argument_list|(
name|receivedExchange
argument_list|)
expr_stmt|;
name|JpaConsumer
name|jpaConsumer
init|=
operator|(
name|JpaConsumer
operator|)
name|consumer
decl_stmt|;
name|assertURIQueryOption
argument_list|(
name|jpaConsumer
argument_list|)
expr_stmt|;
block|}
DECL|method|assertReceivedResult (Exchange exchange)
specifier|protected
name|void
name|assertReceivedResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Customer
name|result
init|=
name|exchange
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
name|assertNotNull
argument_list|(
literal|"Received a POJO"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name property"
argument_list|,
literal|"Willem"
argument_list|,
name|result
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertURIQueryOption (JpaConsumer jpaConsumer)
specifier|protected
name|void
name|assertURIQueryOption
parameter_list|(
name|JpaConsumer
name|jpaConsumer
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"findAllCustomersWithName"
argument_list|,
name|jpaConsumer
operator|.
name|getNamedQuery
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"custName"
argument_list|,
literal|"Willem"
argument_list|)
expr_stmt|;
comment|// bind the params
name|registry
operator|.
name|put
argument_list|(
literal|"params"
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|template
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|template
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|Endpoint
name|value
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find endpoint!"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a JPA endpoint but was: "
operator|+
name|value
argument_list|,
name|value
operator|instanceof
name|JpaEndpoint
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|(
name|JpaEndpoint
operator|)
name|value
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
name|getEntityManager
argument_list|()
expr_stmt|;
block|}
DECL|method|getEndpointUri ()
specifier|protected
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?consumer.namedQuery=findAllCustomersWithName&consumer.parameters=#params"
return|;
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
name|ServiceHelper
operator|.
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
block|}
end_class

end_unit

