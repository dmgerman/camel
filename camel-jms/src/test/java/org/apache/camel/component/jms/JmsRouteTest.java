begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
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
name|builder
operator|.
name|RouteBuilder
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
name|ProducerCache
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
name|ObjectMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
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
DECL|class|JmsRouteTest
specifier|public
class|class
name|JmsRouteTest
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
name|JmsRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|receivedExchange
specifier|protected
name|JmsExchange
name|receivedExchange
decl_stmt|;
DECL|field|container
specifier|protected
name|CamelContext
name|container
init|=
operator|new
name|DefaultCamelContext
argument_list|()
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
DECL|field|endpoint
specifier|protected
name|Endpoint
argument_list|<
name|JmsExchange
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|client
specifier|protected
name|ProducerCache
argument_list|<
name|JmsExchange
argument_list|>
name|client
init|=
operator|new
name|ProducerCache
argument_list|<
name|JmsExchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|testJmsRouteWithTextMessage ()
specifier|public
name|void
name|testJmsRouteWithTextMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedBody
init|=
literal|"Hello there!"
decl_stmt|;
name|sendExchange
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|assertReceivedValidExchange
argument_list|(
name|TextMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"body"
argument_list|,
name|expectedBody
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|testJmsRouteWithObjectMessage ()
specifier|public
name|void
name|testJmsRouteWithObjectMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|expectedBody
init|=
operator|new
name|PurchaseOrder
argument_list|(
literal|"Beer"
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|sendExchange
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|assertReceivedValidExchange
argument_list|(
name|ObjectMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"body"
argument_list|,
name|expectedBody
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|sendExchange (final Object expectedBody)
specifier|protected
name|void
name|sendExchange
parameter_list|(
specifier|final
name|Object
name|expectedBody
parameter_list|)
block|{
name|client
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
operator|new
name|Processor
argument_list|<
name|JmsExchange
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onExchange
parameter_list|(
name|JmsExchange
name|exchange
parameter_list|)
block|{
comment|// now lets fire in a message
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
name|expectedBody
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
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|assertReceivedValidExchange (Class type)
specifier|protected
name|Object
name|assertReceivedValidExchange
parameter_list|(
name|Class
name|type
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets wait on the message being received
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
name|JmsMessage
name|receivedMessage
init|=
name|receivedExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cheese header"
argument_list|,
literal|123
argument_list|,
name|receivedMessage
operator|.
name|getHeader
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|receivedMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|body
argument_list|)
expr_stmt|;
name|Message
name|jmsMessage
init|=
name|receivedMessage
operator|.
name|getJmsMessage
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Expected an instance of "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" but was "
operator|+
name|jmsMessage
argument_list|,
name|type
operator|.
name|isInstance
argument_list|(
name|jmsMessage
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received JMS message: "
operator|+
name|jmsMessage
argument_list|)
expr_stmt|;
return|return
name|body
return|;
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
comment|// lets configure some componnets
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
comment|// lets add some routes
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
literal|"jms:activemq:test.a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:activemq:test.b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:activemq:test.b"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|<
name|JmsExchange
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onExchange
parameter_list|(
name|JmsExchange
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
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
block|}
block|}
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|container
operator|.
name|resolveEndpoint
argument_list|(
literal|"jms:activemq:test.a"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No endpoint found!"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|container
operator|.
name|activateEndpoints
argument_list|()
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
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
name|container
operator|.
name|deactivateEndpoints
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

