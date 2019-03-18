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
name|ExchangePattern
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
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|AbstractMessageListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|DefaultMessageListenerContainer
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
comment|/**  * Unit test inspired by user forum  */
end_comment

begin_class
DECL|class|JmsRouteWithCustomListenerContainerTest
specifier|public
class|class
name|JmsRouteWithCustomListenerContainerTest
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
DECL|method|testSendOrder ()
specifier|public
name|void
name|testSendOrder
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
name|MockEndpoint
name|order
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:topic"
argument_list|)
decl_stmt|;
name|order
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Camel in Action"
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"activemq:queue:inbox"
argument_list|,
literal|"Camel in Action"
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
comment|// assert MEP
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|inbox
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|order
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|JmsEndpoint
name|jmsEndpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"activemq:queue:inbox?messageListenerContainerFactory=#myListenerContainerFactory"
argument_list|,
name|JmsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|MyListenerContainerFactory
operator|.
name|class
argument_list|,
name|jmsEndpoint
operator|.
name|getMessageListenerContainerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ConsumerType
operator|.
name|Custom
argument_list|,
name|jmsEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConsumerType
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|MyListenerContainer
operator|.
name|class
argument_list|,
name|jmsEndpoint
operator|.
name|createMessageListenerContainer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"orderService"
argument_list|,
operator|new
name|MyOrderServiceBean
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myListenerContainerFactory"
argument_list|,
operator|new
name|MyListenerContainerFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
name|from
argument_list|(
literal|"activemq:queue:inbox?messageListenerContainerFactory=#myListenerContainerFactory"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:inbox"
argument_list|)
operator|.
name|inOnly
argument_list|(
literal|"activemq:topic:order"
argument_list|)
operator|.
name|bean
argument_list|(
literal|"orderService"
argument_list|,
literal|"handleOrder"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:topic:order"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:topic"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyListenerContainerFactory
specifier|public
specifier|static
class|class
name|MyListenerContainerFactory
implements|implements
name|MessageListenerContainerFactory
block|{
annotation|@
name|Override
DECL|method|createMessageListenerContainer ( JmsEndpoint endpoint)
specifier|public
name|AbstractMessageListenerContainer
name|createMessageListenerContainer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|)
block|{
return|return
operator|new
name|MyListenerContainer
argument_list|()
return|;
block|}
block|}
DECL|class|MyListenerContainer
specifier|public
specifier|static
class|class
name|MyListenerContainer
extends|extends
name|DefaultMessageListenerContainer
block|{      }
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
block|}
end_class

end_unit

