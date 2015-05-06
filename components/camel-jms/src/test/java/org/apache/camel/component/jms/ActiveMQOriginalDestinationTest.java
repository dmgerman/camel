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
name|Session
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
name|ActiveMQDestination
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
name|ActiveMQMessage
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ActiveMQOriginalDestinationTest
specifier|public
class|class
name|ActiveMQOriginalDestinationTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|componentName
specifier|protected
name|String
name|componentName
init|=
literal|"activemq"
decl_stmt|;
annotation|@
name|Test
DECL|method|testActiveMQOriginalDestination ()
specifier|public
name|void
name|testActiveMQOriginalDestination
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
comment|// consume from bar
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|"activemq:queue:bar"
argument_list|,
literal|5000
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// and we should have foo as the original destination
name|JmsMessage
name|msg
init|=
name|out
operator|.
name|getIn
argument_list|(
name|JmsMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|jms
init|=
name|msg
operator|.
name|getJmsMessage
argument_list|()
decl_stmt|;
name|ActiveMQMessage
name|amq
init|=
name|assertIsInstanceOf
argument_list|(
name|ActiveMQMessage
operator|.
name|class
argument_list|,
name|jms
argument_list|)
decl_stmt|;
name|ActiveMQDestination
name|original
init|=
name|amq
operator|.
name|getOriginalDestination
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|original
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|original
operator|.
name|getPhysicalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Queue"
argument_list|,
name|original
operator|.
name|getDestinationTypeAsString
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
name|JmsComponent
name|jms
init|=
name|camelContext
operator|.
name|getComponent
argument_list|(
name|componentName
argument_list|,
name|JmsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|jms
operator|.
name|setMessageCreatedStrategy
argument_list|(
operator|new
name|OriginalDestinationPropagateStrategy
argument_list|()
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
literal|"activemq:queue:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * A strategy to enrich JMS message with their original destination if the Camel      * route originates from a JMS destination.      *<p/>      * This implementation uses ActiveMQ specific code which can be moved to activemq-camel      * when it supports Camel 2.16      */
DECL|class|OriginalDestinationPropagateStrategy
specifier|private
class|class
name|OriginalDestinationPropagateStrategy
implements|implements
name|MessageCreatedStrategy
block|{
annotation|@
name|Override
DECL|method|onMessageCreated (Message message, Session session, Exchange exchange, Throwable cause)
specifier|public
name|void
name|onMessageCreated
parameter_list|(
name|Message
name|message
parameter_list|,
name|Session
name|session
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|instanceof
name|JmsMessage
condition|)
block|{
name|JmsMessage
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|JmsMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|jms
init|=
name|msg
operator|.
name|getJmsMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|instanceof
name|ActiveMQMessage
condition|)
block|{
name|ActiveMQMessage
name|amq
init|=
operator|(
name|ActiveMQMessage
operator|)
name|jms
decl_stmt|;
name|ActiveMQDestination
name|from
init|=
name|amq
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|from
operator|!=
literal|null
operator|&&
name|message
operator|instanceof
name|ActiveMQMessage
condition|)
block|{
operator|(
operator|(
name|ActiveMQMessage
operator|)
name|message
operator|)
operator|.
name|setOriginalDestination
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

