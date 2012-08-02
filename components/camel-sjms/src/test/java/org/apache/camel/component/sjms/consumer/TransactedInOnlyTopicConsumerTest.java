begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.consumer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|consumer
package|;
end_package

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
name|RuntimeCamelException
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
name|sjms
operator|.
name|SjmsComponent
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
name|sjms
operator|.
name|jms
operator|.
name|JmsMessageHeaderType
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|TransactedInOnlyTopicConsumerTest
specifier|public
class|class
name|TransactedInOnlyTopicConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_DESTINATION_1
specifier|private
specifier|static
specifier|final
name|String
name|TEST_DESTINATION_1
init|=
literal|"sjms:topic:transacted.in.only.topic.consumer.test.1?transacted=true"
decl_stmt|;
DECL|field|TEST_DESTINATION_2
specifier|private
specifier|static
specifier|final
name|String
name|TEST_DESTINATION_2
init|=
literal|"sjms:topic:transacted.in.only.topic.consumer.test.2?transacted=true"
decl_stmt|;
DECL|field|logger
specifier|protected
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testTransactedInOnlyTopicConsumerExchangeFailure ()
specifier|public
name|void
name|testTransactedInOnlyTopicConsumerExchangeFailure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// We should see the World message twice, once for the exception
name|getMockEndpoint
argument_list|(
literal|"mock:test1.topic.mock.before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"World"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test1.topic.mock.after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|TEST_DESTINATION_1
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test1.topic.mock.before"
argument_list|)
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test1.topic.mock.after"
argument_list|)
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTransactedInOnlyTopicConsumerRuntimeException ()
specifier|public
name|void
name|testTransactedInOnlyTopicConsumerRuntimeException
parameter_list|()
throws|throws
name|Exception
block|{
comment|// We should see the World message twice, once for the exception
name|getMockEndpoint
argument_list|(
literal|"mock:test2.topic.mock.before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"World"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test2.topic.mock.after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|TEST_DESTINATION_2
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test2.topic.mock.before"
argument_list|)
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test2.topic.mock.after"
argument_list|)
operator|.
name|assertIsSatisfied
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
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://broker?broker.persistent=false&broker.useJmx=true"
argument_list|)
decl_stmt|;
name|SjmsComponent
name|component
init|=
operator|new
name|SjmsComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"sjms"
argument_list|,
name|component
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
block|{
name|from
argument_list|(
name|TEST_DESTINATION_1
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test1.before"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test1.topic.mock.before"
argument_list|)
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
name|logger
operator|.
name|info
argument_list|(
literal|"Begin processing Exchange ID: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JmsMessageHeaderType
operator|.
name|JMSRedelivered
operator|.
name|toString
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"true"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Exchange does not have a retry message.  Set the exception and allow the retry."
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Creating Failure"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Exchange has retry header.  Continue processing the message."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Hello "
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test1.after?showAll=true"
argument_list|,
literal|"mock:test1.topic.mock.after"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|TEST_DESTINATION_2
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test2.before"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test2.topic.mock.before"
argument_list|)
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
name|logger
operator|.
name|info
argument_list|(
literal|"Begin processing Exchange ID: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JmsMessageHeaderType
operator|.
name|JMSRedelivered
operator|.
name|toString
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"true"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Exchange does not have a retry message.  Throw the exception to verify we handle the retry."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Creating Failure"
argument_list|)
throw|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Exchange has retry header.  Continue processing the message."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Hello "
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test2.after?showAll=true"
argument_list|,
literal|"mock:test2.topic.mock.after"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

