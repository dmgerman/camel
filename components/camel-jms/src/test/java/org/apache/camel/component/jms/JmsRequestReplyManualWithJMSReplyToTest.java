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
name|camel
operator|.
name|Body
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
name|Consume
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
name|Header
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
DECL|class|JmsRequestReplyManualWithJMSReplyToTest
specifier|public
class|class
name|JmsRequestReplyManualWithJMSReplyToTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Consume
argument_list|(
name|uri
operator|=
literal|"activemq:queue:foo"
argument_list|)
DECL|method|doSomething (@eaderR) Destination jmsReplyTo, @Body String body)
specifier|public
name|void
name|doSomething
parameter_list|(
annotation|@
name|Header
argument_list|(
literal|"JMSReplyTo"
argument_list|)
name|Destination
name|jmsReplyTo
parameter_list|,
annotation|@
name|Body
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|String
name|endpointName
init|=
literal|"activemq:"
operator|+
name|jmsReplyTo
operator|.
name|toString
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|endpointName
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testManualRequestReply ()
specifier|public
name|void
name|testManualRequestReply
parameter_list|()
throws|throws
name|Exception
block|{
comment|// send an InOnly but force Camel to pass JMSReplyTo
name|template
operator|.
name|send
argument_list|(
literal|"activemq:queue:foo?preserveMessageQos=true"
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
literal|"Hello World"
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
literal|"bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"activemq:queue:bar"
argument_list|,
literal|5000
argument_list|,
name|String
operator|.
name|class
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
literal|"vm://localhost?broker.persistent=false&broker.useJmx=false"
argument_list|)
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
block|}
end_class

end_unit

