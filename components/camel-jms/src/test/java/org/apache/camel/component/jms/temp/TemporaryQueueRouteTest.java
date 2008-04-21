begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.temp
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
name|temp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ActiveMQTempQueue
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
name|Message
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
name|component
operator|.
name|jms
operator|.
name|JmsQueueEndpoint
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
name|BrowsableQueueTest
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|TemporaryQueueRouteTest
specifier|public
class|class
name|TemporaryQueueRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TemporaryQueueRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpointUri
specifier|protected
name|String
name|endpointUri
init|=
literal|"activemq:temp:queue:cheese"
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|Object
name|expectedBody
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|field|myBean
specifier|protected
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
DECL|method|testSendMessage ()
specifier|public
name|void
name|testSendMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Result"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|endpointUri
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Message
name|message
init|=
name|myBean
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"should have received a message"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Received: "
operator|+
name|message
argument_list|)
expr_stmt|;
name|Object
name|header
init|=
name|message
operator|.
name|getHeader
argument_list|(
literal|"JMSDestination"
argument_list|)
decl_stmt|;
name|isValidDestination
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
DECL|method|isValidDestination (Object header)
specifier|protected
name|void
name|isValidDestination
parameter_list|(
name|Object
name|header
parameter_list|)
block|{
name|ActiveMQTempQueue
name|destination
init|=
name|assertIsInstanceOf
argument_list|(
name|ActiveMQTempQueue
operator|.
name|class
argument_list|,
name|header
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Received message has a temporary queue: "
operator|+
name|destination
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
name|endpointUri
argument_list|)
operator|.
name|bean
argument_list|(
name|myBean
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
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|field|message
specifier|private
name|Message
name|message
decl_stmt|;
DECL|method|onMessage (Message message)
specifier|public
name|String
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Invoked bean with: "
operator|+
name|message
argument_list|)
expr_stmt|;
return|return
literal|"Result"
return|;
block|}
DECL|method|getMessage ()
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
block|}
block|}
end_class

end_unit

