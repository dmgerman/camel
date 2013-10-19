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
name|MultiSteps
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JpaWithNamedQueryTest
specifier|public
class|class
name|JpaWithNamedQueryTest
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
name|JpaWithNamedQueryTest
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|MultiSteps
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
literal|" o where o.step = 1"
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
name|MultiSteps
name|dummy
init|=
operator|new
name|MultiSteps
argument_list|(
literal|"cheese"
argument_list|)
decl_stmt|;
name|dummy
operator|.
name|setStep
argument_list|(
literal|4
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|MultiSteps
argument_list|(
literal|"foo@bar.com"
argument_list|)
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
name|MultiSteps
name|mail
init|=
operator|(
name|MultiSteps
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
literal|"address property"
argument_list|,
literal|"foo@bar.com"
argument_list|,
name|mail
operator|.
name|getAddress
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
literal|50
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
comment|// lets now test that the database is updated
comment|// we need to sleep as we will be invoked from inside the transaction!
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
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
comment|// make use of the EntityManager having the relevant persistence-context
name|EntityManager
name|entityManager2
init|=
name|receivedExchange
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
name|entityManager2
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
comment|// now lets assert that there are still 2 entities left
name|List
argument_list|<
name|?
argument_list|>
name|rows
init|=
name|entityManager2
operator|.
name|createQuery
argument_list|(
literal|"select x from MultiSteps x"
argument_list|)
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of entities: "
operator|+
name|rows
argument_list|,
literal|2
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|counter
init|=
literal|1
decl_stmt|;
for|for
control|(
name|Object
name|rowObj
range|:
name|rows
control|)
block|{
name|assertTrue
argument_list|(
literal|"Rows are not instances of MultiSteps"
argument_list|,
name|rowObj
operator|instanceof
name|MultiSteps
argument_list|)
expr_stmt|;
specifier|final
name|MultiSteps
name|row
init|=
operator|(
name|MultiSteps
operator|)
name|rowObj
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"entity: "
operator|+
name|counter
operator|++
operator|+
literal|" = "
operator|+
name|row
argument_list|)
expr_stmt|;
if|if
condition|(
name|row
operator|.
name|getAddress
argument_list|()
operator|.
name|equals
argument_list|(
literal|"foo@bar.com"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Found updated row: "
operator|+
name|row
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Updated row step for: "
operator|+
name|row
argument_list|,
name|getUpdatedStepValue
argument_list|()
argument_list|,
name|row
operator|.
name|getStep
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// dummy row
name|assertEquals
argument_list|(
literal|"dummy row step for: "
operator|+
name|row
argument_list|,
literal|4
argument_list|,
name|row
operator|.
name|getStep
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
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
name|MultiSteps
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|MultiSteps
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
literal|"address property"
argument_list|,
literal|"foo@bar.com"
argument_list|,
name|result
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getUpdatedStepValue ()
specifier|protected
name|int
name|getUpdatedStepValue
parameter_list|()
block|{
return|return
literal|2
return|;
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
literal|"step1"
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
name|createEntityManager
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
name|MultiSteps
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?consumer.namedQuery=step1"
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

