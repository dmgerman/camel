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
name|SendEmail
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
name|support
operator|.
name|service
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

begin_class
DECL|class|JpaTest
specifier|public
class|class
name|JpaTest
extends|extends
name|Assert
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
name|JpaTest
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
name|SendEmail
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
literal|" o"
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
name|SendEmail
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
name|SendEmail
name|mail
init|=
operator|(
name|SendEmail
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
comment|// should have a EntityManager
name|EntityManager
name|entityManager
init|=
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
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a EntityManager as header"
argument_list|,
name|entityManager
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
name|assertNotNull
argument_list|(
name|receivedExchange
argument_list|)
expr_stmt|;
name|SendEmail
name|result
init|=
name|receivedExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SendEmail
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
name|startService
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
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|stopService
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

