begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|camel
operator|.
name|util
operator|.
name|CamelClient
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
name|EntityTransaction
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
name|Properties
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
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
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
DECL|field|client
specifier|protected
name|CamelClient
name|client
init|=
operator|new
name|CamelClient
argument_list|(
name|camelContext
argument_list|)
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
DECL|field|queryText
specifier|protected
name|String
name|queryText
init|=
literal|"select o from "
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" o"
decl_stmt|;
DECL|field|transaction
specifier|protected
name|EntityTransaction
name|transaction
decl_stmt|;
DECL|method|testProducerInsertsIntoDatabaseThenConsumerFiresMessageExchange ()
specifier|public
name|void
name|testProducerInsertsIntoDatabaseThenConsumerFiresMessageExchange
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets assert that there are no existing send mail tasks
name|transaction
operator|=
name|entityManager
operator|.
name|getTransaction
argument_list|()
expr_stmt|;
name|transaction
operator|.
name|begin
argument_list|()
expr_stmt|;
name|List
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
name|client
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
operator|new
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onExchange
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
name|transaction
operator|.
name|commit
argument_list|()
expr_stmt|;
comment|// now lets assert that there is a result
name|transaction
operator|.
name|begin
argument_list|()
expr_stmt|;
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
literal|"Should have no results: "
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
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onExchange
parameter_list|(
name|Exchange
name|e
parameter_list|)
block|{
name|log
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
name|boolean
name|received
init|=
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Did not recieve the message!"
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
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"foo"
argument_list|,
name|createJpaComponent
argument_list|()
argument_list|)
expr_stmt|;
name|startServices
argument_list|(
name|client
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|Endpoint
name|value
init|=
name|camelContext
operator|.
name|resolveEndpoint
argument_list|(
literal|"jpa:"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
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
name|entityManager
operator|=
name|endpoint
operator|.
name|createEntityManager
argument_list|()
expr_stmt|;
block|}
DECL|method|createJpaComponent ()
specifier|protected
name|JpaComponent
name|createJpaComponent
parameter_list|()
block|{
name|JpaComponent
name|answer
init|=
operator|new
name|JpaComponent
argument_list|()
decl_stmt|;
comment|/*         Properties properties = new Properties();         properties.setProperty("openjpa.ConnectionDriverName", "org.apache.derby.jdbc.EmbeddedDriver");         properties.setProperty("openjpa.ConnectionURL", "jdbc:derby:target/derby;create=true");         properties.setProperty("openjpa.jdbc.SynchronizeMappings", "buildSchema");         properties.setProperty("openjpa.Log", "DefaultLevel=WARN,SQL=TRACE");         answer.setEntityManagerProperties(properties); */
return|return
name|answer
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
if|if
condition|(
name|transaction
operator|!=
literal|null
condition|)
block|{
name|transaction
operator|.
name|rollback
argument_list|()
expr_stmt|;
name|transaction
operator|=
literal|null
expr_stmt|;
block|}
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
name|stopServices
argument_list|(
name|consumer
argument_list|,
name|client
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

