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
name|jms
operator|.
name|JmsConstants
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
name|jmsComponentClientAcknowledge
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|TempReplyToIssueTest
specifier|public
class|class
name|TempReplyToIssueTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testReplyToIssue ()
specifier|public
name|void
name|testReplyToIssue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"activemq:queue:test.queue"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we should receive that fixed reply
name|assertEquals
argument_list|(
literal|"Hello Moon"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|handleMessage (@eaderR) final Destination jmsReplyTo, @Header(R) final String id, @Body String body, Exchange exchange)
specifier|public
name|String
name|handleMessage
parameter_list|(
annotation|@
name|Header
argument_list|(
literal|"JMSReplyTo"
argument_list|)
specifier|final
name|Destination
name|jmsReplyTo
parameter_list|,
annotation|@
name|Header
argument_list|(
literal|"JMSCorrelationID"
argument_list|)
specifier|final
name|String
name|id
parameter_list|,
annotation|@
name|Body
name|String
name|body
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|jmsReplyTo
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a temp queue"
argument_list|,
name|jmsReplyTo
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"temp-queue"
argument_list|)
argument_list|)
expr_stmt|;
comment|// we send the reply manually (notice we just use a bogus endpoint uri)
name|ProducerTemplate
name|producer
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|producer
operator|.
name|send
argument_list|(
literal|"activemq:queue:xxx"
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
literal|"Hello Moon"
argument_list|)
expr_stmt|;
comment|// remember to set correlation id
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|id
argument_list|)
expr_stmt|;
comment|// this is the real destination we send the reply to
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|,
name|jmsReplyTo
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// stop it after use
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// sleep a bit so Camel will send the reply a bit later
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// this will later cause a problem as the temp queue has been deleted
comment|// and exceptions will be logged etc
return|return
literal|"Hello "
operator|+
name|body
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
literal|"activemq:queue:test.queue"
argument_list|)
operator|.
name|bean
argument_list|(
name|TempReplyToIssueTest
operator|.
name|class
argument_list|,
literal|"handleMessage"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

