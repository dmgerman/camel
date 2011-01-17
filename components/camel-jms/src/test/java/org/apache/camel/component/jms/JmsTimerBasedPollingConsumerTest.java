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
name|ConsumerTemplate
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsTimerBasedPollingConsumerTest
specifier|public
class|class
name|JmsTimerBasedPollingConsumerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testJmsTimerBasedPollingConsumer ()
specifier|public
name|void
name|testJmsTimerBasedPollingConsumer
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
name|expectsAscending
argument_list|(
name|header
argument_list|(
literal|"number"
argument_list|)
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:queue.inbox"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
block|}
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
literal|"activemq"
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
comment|// START SNIPPET: e1
name|MyCoolBean
name|cool
init|=
operator|new
name|MyCoolBean
argument_list|()
decl_stmt|;
name|cool
operator|.
name|setProducer
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|cool
operator|.
name|setConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer://foo?period=5000"
argument_list|)
operator|.
name|bean
argument_list|(
name|cool
argument_list|,
literal|"someBusinessLogic"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue.foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
DECL|class|MyCoolBean
specifier|public
specifier|static
class|class
name|MyCoolBean
block|{
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
DECL|field|consumer
specifier|private
name|ConsumerTemplate
name|consumer
decl_stmt|;
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
DECL|method|setConsumer (ConsumerTemplate consumer)
specifier|public
name|void
name|setConsumer
parameter_list|(
name|ConsumerTemplate
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
DECL|method|setProducer (ProducerTemplate producer)
specifier|public
name|void
name|setProducer
parameter_list|(
name|ProducerTemplate
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
DECL|method|someBusinessLogic ()
specifier|public
name|void
name|someBusinessLogic
parameter_list|()
block|{
comment|// loop to empty queue
while|while
condition|(
literal|true
condition|)
block|{
comment|// receive the message from the queue, wait at most 3 sec
name|String
name|msg
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"activemq:queue.inbox"
argument_list|,
literal|3000
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
comment|// no more messages in queue
break|break;
block|}
comment|// do something with body
name|msg
operator|=
literal|"Hello "
operator|+
name|msg
expr_stmt|;
comment|// send it to the next queue
name|producer
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue.foo"
argument_list|,
name|msg
argument_list|,
literal|"number"
argument_list|,
name|count
operator|++
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// END SNIPPET: e2
block|}
end_class

end_unit

