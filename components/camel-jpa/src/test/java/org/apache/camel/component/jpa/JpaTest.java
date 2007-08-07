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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CamelTemplate
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JpaTest
specifier|public
class|class
name|JpaTest
extends|extends
name|TestCase
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
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
name|CamelTemplate
name|template
init|=
operator|new
name|CamelTemplate
argument_list|(
name|camelContext
argument_list|)
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
argument_list|<
name|Exchange
argument_list|>
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
DECL|method|testProducerInsertsIntoDatabaseThenConsumerFiresMessageExchange ()
specifier|public
name|void
name|testProducerInsertsIntoDatabaseThenConsumerFiresMessageExchange
parameter_list|()
throws|throws
name|Exception
block|{
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
name|results
init|=
name|jpaTemplate
operator|.
name|find
argument_list|(
name|queryText
argument_list|)
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
name|jpaTemplate
operator|.
name|find
argument_list|(
name|queryText
argument_list|)
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
literal|"Did not receive the message!"
argument_list|,
name|received
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
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
name|Override
DECL|method|tearDown ()
specifier|protected
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

