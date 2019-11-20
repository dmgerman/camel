begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.amqp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|amqp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
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
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
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
name|broker
operator|.
name|BrokerService
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
name|EndpointInject
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
name|AvailablePortFinder
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
name|apache
operator|.
name|qpid
operator|.
name|jms
operator|.
name|message
operator|.
name|JmsMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|qpid
operator|.
name|jms
operator|.
name|provider
operator|.
name|amqp
operator|.
name|message
operator|.
name|AmqpJmsMessageFacade
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|amqp
operator|.
name|AMQPComponent
operator|.
name|amqpComponent
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
name|amqp
operator|.
name|AMQPConnectionDetails
operator|.
name|AMQP_PORT
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
name|amqp
operator|.
name|AMQPConnectionDetails
operator|.
name|discoverAMQP
import|;
end_import

begin_class
DECL|class|AMQPRouteTest
specifier|public
class|class
name|AMQPRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|amqpPort
specifier|static
name|int
name|amqpPort
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|broker
specifier|static
name|BrokerService
name|broker
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|field|expectedBody
name|String
name|expectedBody
init|=
literal|"Hello there!"
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|broker
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|broker
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|broker
operator|.
name|addConnector
argument_list|(
literal|"amqp://0.0.0.0:"
operator|+
name|amqpPort
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|AMQP_PORT
argument_list|,
name|amqpPort
operator|+
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|afterClass ()
specifier|public
specifier|static
name|void
name|afterClass
parameter_list|()
throws|throws
name|Exception
block|{
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJmsQueue ()
specifier|public
name|void
name|testJmsQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"amqp-customized:queue:ping"
argument_list|,
name|expectedBody
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRequestReply ()
specifier|public
name|void
name|testRequestReply
parameter_list|()
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"amqp-customized:queue:inOut"
argument_list|,
name|expectedBody
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJmsTopic ()
specifier|public
name|void
name|testJmsTopic
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"amqp-customized:topic:ping"
argument_list|,
name|expectedBody
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPrefixWildcard ()
specifier|public
name|void
name|testPrefixWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
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
literal|"amqp-customized:wildcard.foo.bar"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIncludeDestination ()
specifier|public
name|void
name|testIncludeDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
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
name|isEqualTo
argument_list|(
literal|"ping"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"amqp-customized:queue:ping"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoAmqpAnnotations ()
specifier|public
name|void
name|testNoAmqpAnnotations
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
expr_stmt|;
comment|// default doesn't map annotations to headers
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMS_AMQP_MA_cheese"
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|sendAmqpMessage
argument_list|(
name|context
operator|.
name|getComponent
argument_list|(
literal|"amqp-customized"
argument_list|,
name|AMQPComponent
operator|.
name|class
argument_list|)
argument_list|,
literal|"ping"
argument_list|,
name|expectedBody
argument_list|,
name|facade
lambda|->
block|{
try|try
block|{
name|facade
operator|.
name|setApplicationProperty
argument_list|(
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|facade
operator|.
name|setTracingAnnotation
argument_list|(
literal|"cheese"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAmqpAnnotations ()
specifier|public
name|void
name|testAmqpAnnotations
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"JMS_AMQP_MA_cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|456
argument_list|)
expr_stmt|;
name|sendAmqpMessage
argument_list|(
name|context
operator|.
name|getComponent
argument_list|(
literal|"amqp-customized2"
argument_list|,
name|AMQPComponent
operator|.
name|class
argument_list|)
argument_list|,
literal|"ping2"
argument_list|,
name|expectedBody
argument_list|,
name|facade
lambda|->
block|{
try|try
block|{
name|facade
operator|.
name|setApplicationProperty
argument_list|(
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|facade
operator|.
name|setTracingAnnotation
argument_list|(
literal|"cheese"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendAmqpMessage (AMQPComponent component, String queue, String body, Consumer<AmqpJmsMessageFacade> messageCustomizer)
specifier|private
name|void
name|sendAmqpMessage
parameter_list|(
name|AMQPComponent
name|component
parameter_list|,
name|String
name|queue
parameter_list|,
name|String
name|body
parameter_list|,
name|Consumer
argument_list|<
name|AmqpJmsMessageFacade
argument_list|>
name|messageCustomizer
parameter_list|)
throws|throws
name|JMSException
block|{
name|ConnectionFactory
name|factory
init|=
name|component
operator|.
name|getConnectionFactory
argument_list|()
decl_stmt|;
try|try
init|(
name|Connection
name|connection
init|=
name|factory
operator|.
name|createConnection
argument_list|()
init|;
name|Session
name|session
operator|=
name|connection
operator|.
name|createSession
argument_list|()
init|;
name|MessageProducer
name|producer
operator|=
name|session
operator|.
name|createProducer
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|queue
argument_list|)
argument_list|)
init|)
block|{
name|TextMessage
name|message
init|=
name|session
operator|.
name|createTextMessage
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|messageCustomizer
operator|.
name|accept
argument_list|(
call|(
name|AmqpJmsMessageFacade
call|)
argument_list|(
operator|(
name|JmsMessage
operator|)
name|message
argument_list|)
operator|.
name|getFacade
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Routes fixtures
annotation|@
name|Override
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
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amqpConnection"
argument_list|,
name|discoverAMQP
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"amqp-customized"
argument_list|,
name|amqpComponent
argument_list|(
literal|"amqp://localhost:"
operator|+
name|amqpPort
argument_list|)
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"amqp-customized2"
argument_list|,
name|amqpComponent
argument_list|(
literal|"amqp://localhost:"
operator|+
name|amqpPort
argument_list|)
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getComponent
argument_list|(
literal|"amqp-customized2"
argument_list|,
name|AMQPComponent
operator|.
name|class
argument_list|)
operator|.
name|setIncludeAmqpAnnotations
argument_list|(
literal|true
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"amqp-customized:queue:ping"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:routing"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"amqp-customized2:queue:ping2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:routing"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"amqp-customized:queue:inOut"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"response"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"amqp-customized:topic:ping"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:routing"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"amqp-customized:topic:ping"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:routing"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"amqp-customized:queue:wildcard.>"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:routing"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"amqp:queue:uriEndpoint"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:routing"
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
block|}
end_class

end_unit

