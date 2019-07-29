begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|BindToRegistry
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
name|JmsRouteWithCustomListenerContainerTest
operator|.
name|MyOrderServiceBean
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
name|mock
operator|.
name|MockEndpoint
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
name|JndiRegistry
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
name|Test
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
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_class
DECL|class|JmsRouteWithInOnlyAndMultipleAcksTest
specifier|public
class|class
name|JmsRouteWithInOnlyAndMultipleAcksTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|componentName
specifier|protected
name|String
name|componentName
init|=
literal|"amq"
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"orderService"
argument_list|)
DECL|field|serviceBean
specifier|private
name|MyOrderServiceBean
name|serviceBean
init|=
operator|new
name|MyOrderServiceBean
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"orderServiceNotificationWithAck-1"
argument_list|)
DECL|field|orderNotificationAckBean
specifier|private
name|MyOrderServiceNotificationWithAckBean
name|orderNotificationAckBean
init|=
operator|new
name|MyOrderServiceNotificationWithAckBean
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"orderServiceNotificationWithAck-2"
argument_list|)
DECL|field|orderNotificationAckBean2
specifier|private
name|MyOrderServiceNotificationWithAckBean
name|orderNotificationAckBean2
init|=
operator|new
name|MyOrderServiceNotificationWithAckBean
argument_list|(
literal|"2"
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendOrderWithMultipleAcks ()
specifier|public
name|void
name|testSendOrderWithMultipleAcks
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|inbox
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:inbox"
argument_list|)
decl_stmt|;
name|inbox
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Camel in Action"
argument_list|)
expr_stmt|;
name|String
name|orderId
init|=
literal|"1"
decl_stmt|;
name|MockEndpoint
name|notifCollector
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:orderNotificationAckCollector"
argument_list|)
decl_stmt|;
name|notifCollector
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|notifCollector
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|orderId
argument_list|)
expr_stmt|;
name|notifCollector
operator|.
name|setResultWaitTime
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"amq:queue:inbox"
argument_list|,
literal|"Camel in Action"
argument_list|,
literal|"JMSCorrelationID"
argument_list|,
name|orderId
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"OK: Camel in Action"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
name|componentName
argument_list|,
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this route picks up an order request
comment|// send out a one way notification to multiple
comment|// topic subscribers, lets a bean handle
comment|// the order and then delivers a reply back to
comment|// the original order request initiator
name|from
argument_list|(
literal|"amq:queue:inbox"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:inbox"
argument_list|)
operator|.
name|inOnly
argument_list|(
literal|"amq:topic:orderServiceNotification"
argument_list|)
operator|.
name|bean
argument_list|(
literal|"orderService"
argument_list|,
literal|"handleOrder"
argument_list|)
expr_stmt|;
comment|// this route collects an order request notification
comment|// and sends back an acknowledgment back to a queue
name|from
argument_list|(
literal|"amq:topic:orderServiceNotification"
argument_list|)
operator|.
name|bean
argument_list|(
literal|"orderServiceNotificationWithAck-1"
argument_list|,
literal|"handleOrderNotificationWithAck"
argument_list|)
operator|.
name|to
argument_list|(
literal|"amq:queue:orderServiceNotificationAck"
argument_list|)
expr_stmt|;
comment|// this route collects an order request notification
comment|// and sends back an acknowledgment back to a queue
name|from
argument_list|(
literal|"amq:topic:orderServiceNotification"
argument_list|)
operator|.
name|bean
argument_list|(
literal|"orderServiceNotificationWithAck-2"
argument_list|,
literal|"handleOrderNotificationWithAck"
argument_list|)
operator|.
name|to
argument_list|(
literal|"amq:queue:orderServiceNotificationAck"
argument_list|)
expr_stmt|;
comment|// this route collects all order notifications acknowledgments
name|from
argument_list|(
literal|"amq:queue:orderServiceNotificationAck"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:orderNotificationAckCollector"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyOrderServiceBean
specifier|public
specifier|static
class|class
name|MyOrderServiceBean
block|{
DECL|method|handleOrder (String body)
specifier|public
name|String
name|handleOrder
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
literal|"OK: "
operator|+
name|body
return|;
block|}
block|}
DECL|class|MyOrderServiceNotificationWithAckBean
specifier|public
specifier|static
class|class
name|MyOrderServiceNotificationWithAckBean
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|method|MyOrderServiceNotificationWithAckBean (String id)
specifier|public
name|MyOrderServiceNotificationWithAckBean
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|handleOrderNotificationWithAck (String body)
specifier|public
name|String
name|handleOrderNotificationWithAck
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
literal|"Ack-"
operator|+
name|id
operator|+
literal|":"
operator|+
name|body
return|;
block|}
block|}
block|}
end_class

end_unit

