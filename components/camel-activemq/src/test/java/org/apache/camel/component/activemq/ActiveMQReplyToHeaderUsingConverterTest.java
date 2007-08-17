begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.activemq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|activemq
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|activemq
operator|.
name|ActiveMQComponent
operator|.
name|activeMQComponent
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
name|HashMap
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

begin_comment
comment|/**  * @version $Revision: 538973 $  */
end_comment

begin_class
DECL|class|ActiveMQReplyToHeaderUsingConverterTest
specifier|public
class|class
name|ActiveMQReplyToHeaderUsingConverterTest
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
name|ActiveMQReplyToHeaderUsingConverterTest
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|field|replyQueueName
specifier|protected
name|String
name|replyQueueName
init|=
literal|"queue://test.my.reply.queue"
decl_stmt|;
DECL|field|correlationID
specifier|protected
name|String
name|correlationID
init|=
literal|"ABC-123"
decl_stmt|;
DECL|field|groupID
specifier|protected
name|String
name|groupID
init|=
literal|"GROUP-XYZ"
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
DECL|method|testSendingAMessageFromCamelSetsCustomJmsHeaders ()
specifier|public
name|void
name|testSendingAMessageFromCamelSetsCustomJmsHeaders
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
name|firstMessage
init|=
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|firstMessage
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
name|firstMessage
operator|.
name|header
argument_list|(
literal|"JMSCorrelationID"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
name|firstMessage
operator|.
name|header
argument_list|(
literal|"JMSReplyTo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ActiveMQConverter
operator|.
name|toDestination
argument_list|(
name|replyQueueName
argument_list|)
argument_list|)
expr_stmt|;
name|firstMessage
operator|.
name|header
argument_list|(
literal|"JMSType"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|messageType
argument_list|)
expr_stmt|;
name|firstMessage
operator|.
name|header
argument_list|(
literal|"JMSXGroupID"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|groupID
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSReplyTo"
argument_list|,
name|replyQueueName
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|correlationID
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSType"
argument_list|,
name|messageType
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSXGroupID"
argument_list|,
name|groupID
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"activemq:test.a"
argument_list|,
name|expectedBody
argument_list|,
name|headers
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|replyTo
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"JMSReplyTo"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Reply to is: "
operator|+
name|replyTo
argument_list|)
expr_stmt|;
name|Destination
name|destination
init|=
name|assertIsInstanceOf
argument_list|(
name|Destination
operator|.
name|class
argument_list|,
name|replyTo
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ReplyTo"
argument_list|,
name|replyQueueName
argument_list|,
name|destination
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
literal|"JMSCorrelationID"
argument_list|,
name|correlationID
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
literal|"JMSType"
argument_list|,
name|messageType
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
literal|"JMSXGroupID"
argument_list|,
name|groupID
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
comment|// START SNIPPET: example
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|activeMQComponent
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
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
literal|"activemq:test.a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:test.b"
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

