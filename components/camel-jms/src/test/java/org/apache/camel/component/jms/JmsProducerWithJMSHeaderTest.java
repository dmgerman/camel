begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
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
name|Destination
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
name|activemq
operator|.
name|command
operator|.
name|ActiveMQQueue
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
name|ContextTestSupport
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
name|mock
operator|.
name|MockEndpoint
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
name|jmsComponentClientAcknowledge
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsProducerWithJMSHeaderTest
specifier|public
class|class
name|JmsProducerWithJMSHeaderTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInOnlyJMSPrioritory ()
specifier|public
name|void
name|testInOnlyJMSPrioritory
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSPriority"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:foo?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"JMSPriority"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyJMSPrioritoryTheDeliveryModeIsDefault ()
specifier|public
name|void
name|testInOnlyJMSPrioritoryTheDeliveryModeIsDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSPriority"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// not provided as header but should use endpoint default then
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSDeliveryMode"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:foo?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"JMSPriority"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyJMSDeliveryMode ()
specifier|public
name|void
name|testInOnlyJMSDeliveryMode
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSDeliveryMode"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:foo?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"JMSDeliveryMode"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyJMSDeliveryModeThenPriorityIsDefault ()
specifier|public
name|void
name|testInOnlyJMSDeliveryModeThenPriorityIsDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSDeliveryMode"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// not provided as header but should use endpoint default then
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSPriority"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:foo?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"JMSDeliveryMode"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyJMSExpiration ()
specifier|public
name|void
name|testInOnlyJMSExpiration
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|long
name|ttl
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|5000
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:bar?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"JMSExpiration"
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
comment|// sleep just a little
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|Exchange
name|bar
init|=
name|consumer
operator|.
name|receiveNoWait
argument_list|(
literal|"activemq:queue:bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should be a message on queue"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"activemq:queue:foo"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyJMSExpirationNoMessage ()
specifier|public
name|void
name|testInOnlyJMSExpirationNoMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|long
name|ttl
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|2000
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:bar?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"JMSExpiration"
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
comment|// sleep more so the message is expired
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|Exchange
name|bar
init|=
name|consumer
operator|.
name|receiveNoWait
argument_list|(
literal|"activemq:queue:bar"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should NOT be a message on queue"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:queue:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyMultipleJMSHeaders ()
specifier|public
name|void
name|testInOnlyMultipleJMSHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSPriority"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSDeliveryMode"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Map
name|headers
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSPriority"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSDeliveryMode"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"activemq:queue:foo?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyMultipleJMSHeadersAndExpiration ()
specifier|public
name|void
name|testInOnlyMultipleJMSHeadersAndExpiration
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSPriority"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSDeliveryMode"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|long
name|ttl
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|2000
decl_stmt|;
name|Map
name|headers
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSPriority"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSDeliveryMode"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSExpiration"
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"activemq:queue:bar?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// sleep just a little
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|Exchange
name|bar
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|"activemq:queue:bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should be a message on queue"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"activemq:queue:foo?preserveMessageQos=true"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyMultipleJMSHeadersAndExpirationNoMessage ()
specifier|public
name|void
name|testInOnlyMultipleJMSHeadersAndExpirationNoMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|long
name|ttl
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|2000
decl_stmt|;
name|Map
name|headers
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSPriority"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSDeliveryMode"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSExpiration"
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"activemq:queue:bar?preserveMessageQos=true"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// sleep more so the message is expired
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|Exchange
name|bar
init|=
name|consumer
operator|.
name|receiveNoWait
argument_list|(
literal|"activemq:queue:bar"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should NOT be a message on queue"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:queue:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyJMSXGroupID ()
specifier|public
name|void
name|testInOnlyJMSXGroupID
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSXGroupID"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"atom"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:foo"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"JMSXGroupID"
argument_list|,
literal|"atom"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyJMSDestination ()
specifier|public
name|void
name|testInOnlyJMSDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|Destination
name|queue
init|=
operator|new
name|ActiveMQQueue
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSDestination"
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:bar"
argument_list|,
literal|"Hello World"
argument_list|,
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|,
name|queue
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"queue://foo"
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"JMSDestination"
argument_list|,
name|Destination
operator|.
name|class
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyJMSDestinationName ()
specifier|public
name|void
name|testInOnlyJMSDestinationName
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSDestination"
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:bar"
argument_list|,
literal|"Hello World"
argument_list|,
name|JmsConstants
operator|.
name|JMS_DESTINATION_NAME
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"queue://foo"
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"JMSDestination"
argument_list|,
name|Destination
operator|.
name|class
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutJMSDestination ()
specifier|public
name|void
name|testInOutJMSDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|Destination
name|queue
init|=
operator|new
name|ActiveMQQueue
argument_list|(
literal|"reply"
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"activemq:queue:bar"
argument_list|,
literal|"Hello World"
argument_list|,
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|,
name|queue
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutJMSDestinationName ()
specifier|public
name|void
name|testInOutJMSDestinationName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|reply
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"activemq:queue:bar"
argument_list|,
literal|"Hello World"
argument_list|,
name|JmsConstants
operator|.
name|JMS_DESTINATION_NAME
argument_list|,
literal|"reply"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyRouteJMSDestinationName ()
specifier|public
name|void
name|testInOnlyRouteJMSDestinationName
parameter_list|()
throws|throws
name|Exception
block|{
name|context
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"activemq:queue:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMSDestination"
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:bar"
argument_list|,
literal|"Hello World"
argument_list|,
name|JmsConstants
operator|.
name|JMS_DESTINATION_NAME
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"queue://b"
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"JMSDestination"
argument_list|,
name|Destination
operator|.
name|class
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
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
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
decl_stmt|;
name|camelContext
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
name|from
argument_list|(
literal|"activemq:queue:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:reply"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

