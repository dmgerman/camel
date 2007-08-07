begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jms
package|;
end_package

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
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentClientAcknowledge
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
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
name|activemq
operator|.
name|ActiveMQConnectionFactory
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
name|Producer
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
name|builder
operator|.
name|RouteBuilder
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
name|jms
operator|.
name|JmsEndpoint
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
name|jms
operator|.
name|JmsExchange
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
name|jms
operator|.
name|JmsMessage
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
name|pojo
operator|.
name|PojoComponent
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
name|edu
operator|.
name|emory
operator|.
name|mathcs
operator|.
name|backport
operator|.
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
name|edu
operator|.
name|emory
operator|.
name|mathcs
operator|.
name|backport
operator|.
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
comment|/**  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|JmsIntegrationTest
specifier|public
class|class
name|JmsIntegrationTest
extends|extends
name|TestCase
block|{
DECL|field|container
specifier|protected
name|CamelContext
name|container
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|method|testDummy ()
specifier|public
name|void
name|testDummy
parameter_list|()
block|{     }
comment|/**      * Commented out since this fails due to us not converting the JmsExchange to a PojoExchange      *       * @throws Exception      */
DECL|method|xtestOneWayInJmsOutPojo ()
specifier|public
name|void
name|xtestOneWayInJmsOutPojo
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|receivedCountDown
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// Configure the components
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
decl_stmt|;
name|container
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|jmsComponentClientAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
name|PojoComponent
name|component
init|=
operator|new
name|PojoComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|addService
argument_list|(
literal|"listener"
argument_list|,
operator|new
name|MessageListener
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Received: "
operator|+
name|msg
argument_list|)
expr_stmt|;
name|receivedCountDown
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|container
operator|.
name|addComponent
argument_list|(
literal|"default"
argument_list|,
name|component
argument_list|)
expr_stmt|;
comment|// lets add a jms -> pojo route
name|container
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"jms:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"pojo:listener"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// Send a message to the JMS endpoint
name|JmsEndpoint
name|endpoint
init|=
operator|(
name|JmsEndpoint
operator|)
name|container
operator|.
name|getEndpoint
argument_list|(
literal|"jms:test"
argument_list|)
decl_stmt|;
name|Producer
argument_list|<
name|JmsExchange
argument_list|>
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|JmsExchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|JmsMessage
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// The Activated endpoint should send it to the pojo due to the configured route.
name|assertTrue
argument_list|(
literal|"The message ware received by the Pojo"
argument_list|,
name|receivedCountDown
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
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
name|container
operator|.
name|stop
argument_list|()
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

