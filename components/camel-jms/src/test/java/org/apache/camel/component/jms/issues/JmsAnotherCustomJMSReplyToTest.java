begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.issues
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
operator|.
name|issues
package|;
end_package

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
name|javax
operator|.
name|jms
operator|.
name|TextMessage
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
name|camel
operator|.
name|component
operator|.
name|ActiveMQComponent
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
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsTemplate
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|camel
operator|.
name|component
operator|.
name|ActiveMQComponent
operator|.
name|activeMQComponent
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsAnotherCustomJMSReplyToTest
specifier|public
class|class
name|JmsAnotherCustomJMSReplyToTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|MQURI
specifier|private
specifier|static
specifier|final
name|String
name|MQURI
init|=
literal|"vm://localhost?broker.persistent=false&broker.useJmx=false"
decl_stmt|;
DECL|field|amq
specifier|private
name|ActiveMQComponent
name|amq
decl_stmt|;
annotation|@
name|Test
DECL|method|testCustomJMSReplyToInOnly ()
specifier|public
name|void
name|testCustomJMSReplyToInOnly
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
name|expectedBodiesReceived
argument_list|(
literal|"My name is Arnio"
argument_list|)
expr_stmt|;
comment|// start a inOnly route
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:queue:hello"
argument_list|,
literal|"Hello, I'm here"
argument_list|)
expr_stmt|;
comment|// now consume using something that is not Camel
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|JmsTemplate
name|jms
init|=
operator|new
name|JmsTemplate
argument_list|(
name|amq
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
decl_stmt|;
name|TextMessage
name|msg
init|=
operator|(
name|TextMessage
operator|)
name|jms
operator|.
name|receive
argument_list|(
literal|"nameRequestor"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"What's your name"
argument_list|,
name|msg
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
comment|// there should be a JMSReplyTo so we know where to send the reply
name|Destination
name|replyTo
init|=
name|msg
operator|.
name|getJMSReplyTo
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"queue://nameReplyQueue"
argument_list|,
name|replyTo
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// send reply
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:"
operator|+
name|replyTo
operator|.
name|toString
argument_list|()
argument_list|,
literal|"My name is Arnio"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"activemq:queue:hello"
argument_list|)
operator|.
name|inOnly
argument_list|()
operator|.
name|process
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"What's your name"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"JMSReplyTo"
argument_list|,
literal|"nameReplyQueue"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:nameRequestor?preserveMessageQos=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:nameReplyQueue"
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
name|amq
operator|=
name|activeMQComponent
argument_list|(
name|MQURI
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|amq
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

