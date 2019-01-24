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
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

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
name|jms
operator|.
name|CamelJmsTestHelper
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
name|AssertionClause
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
name|Before
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

begin_class
DECL|class|ActiveMQPropagateSerializableHeadersTest
specifier|public
class|class
name|ActiveMQPropagateSerializableHeadersTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|expectedBody
specifier|protected
name|Object
name|expectedBody
init|=
literal|"<time>"
operator|+
operator|new
name|Date
argument_list|()
operator|+
literal|"</time>"
decl_stmt|;
DECL|field|replyQueue
specifier|protected
name|ActiveMQQueue
name|replyQueue
init|=
operator|new
name|ActiveMQQueue
argument_list|(
literal|"test.reply.queue"
argument_list|)
decl_stmt|;
DECL|field|correlationID
specifier|protected
name|String
name|correlationID
init|=
literal|"ABC-123"
decl_stmt|;
DECL|field|messageType
specifier|protected
name|String
name|messageType
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|calValue
specifier|private
name|Calendar
name|calValue
decl_stmt|;
DECL|field|mapValue
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mapValue
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|calValue
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|mapValue
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|mapValue
operator|.
name|put
argument_list|(
literal|"myStringEntry"
argument_list|,
literal|"stringValue"
argument_list|)
expr_stmt|;
name|mapValue
operator|.
name|put
argument_list|(
literal|"myCalEntry"
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|mapValue
operator|.
name|put
argument_list|(
literal|"myIntEntry"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testForwardingAMessageAcrossJMSKeepingCustomJMSHeaders ()
specifier|public
name|void
name|testForwardingAMessageAcrossJMSKeepingCustomJMSHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|AssertionClause
name|firstMessageExpectations
init|=
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|firstMessageExpectations
operator|.
name|header
argument_list|(
literal|"myCal"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|calValue
argument_list|)
expr_stmt|;
name|firstMessageExpectations
operator|.
name|header
argument_list|(
literal|"myMap"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|mapValue
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:test.a"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
block|{
name|String
name|headerValue
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"myString"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myString"
argument_list|,
literal|"stringValue"
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|{
name|Calendar
name|headerValue
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"myCal"
argument_list|,
name|Calendar
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myCal"
argument_list|,
name|calValue
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerValue
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"myMap"
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myMap"
argument_list|,
name|mapValue
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|// START SNIPPET: example
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
comment|// END SNIPPET: example
comment|// prevent java.io.NotSerializableException: org.apache.camel.support.DefaultMessageHistory
name|camelContext
operator|.
name|setMessageHistory
argument_list|(
literal|false
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
literal|"activemq:test.a"
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
comment|// set the JMS headers
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"myString"
argument_list|,
literal|"stringValue"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"myMap"
argument_list|,
name|mapValue
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"myCal"
argument_list|,
name|calValue
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:test.b?transferExchange=true&allowSerializedHeaders=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:test.b"
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

