begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
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
name|Headers
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
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|hasEntry
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|hasKey
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|InvokeRequestReplyUsingJmsReplyToHeaderTest
specifier|public
class|class
name|InvokeRequestReplyUsingJmsReplyToHeaderTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|InvokeRequestReplyUsingJmsReplyToHeaderTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|replyQueueName
specifier|protected
name|String
name|replyQueueName
init|=
literal|"queue://test.reply"
decl_stmt|;
DECL|field|correlationID
specifier|protected
name|Object
name|correlationID
init|=
literal|"ABC-123"
decl_stmt|;
DECL|field|groupID
specifier|protected
name|Object
name|groupID
init|=
literal|"GROUP-XYZ"
decl_stmt|;
DECL|field|myBean
specifier|private
name|MyServer
name|myBean
init|=
operator|new
name|MyServer
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testPerformRequestReplyOverJms ()
specifier|public
name|void
name|testPerformRequestReplyOverJms
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"JMSXGroupID"
argument_list|,
name|groupID
argument_list|)
expr_stmt|;
name|Exchange
name|reply
init|=
name|template
operator|.
name|request
argument_list|(
literal|"activemq:test.server?replyTo=queue:test.reply"
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
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"James"
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
literal|"JMSXGroupID"
argument_list|,
name|groupID
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|reply
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Received headers: "
operator|+
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Received body: "
operator|+
name|in
operator|.
name|getBody
argument_list|()
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|receivedHeaders
init|=
name|myBean
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|receivedHeaders
argument_list|,
name|hasKey
argument_list|(
literal|"JMSReplyTo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|receivedHeaders
argument_list|,
name|hasEntry
argument_list|(
literal|"JMSXGroupID"
argument_list|,
name|groupID
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|receivedHeaders
argument_list|,
name|hasEntry
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|correlationID
argument_list|)
argument_list|)
expr_stmt|;
name|replyTo
operator|=
name|receivedHeaders
operator|.
name|get
argument_list|(
literal|"JMSReplyTo"
argument_list|)
expr_stmt|;
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
literal|"activemq:test.server"
argument_list|)
operator|.
name|bean
argument_list|(
name|myBean
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyServer
specifier|protected
specifier|static
class|class
name|MyServer
block|{
DECL|field|headers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
decl_stmt|;
DECL|method|process (@eaders Map<String, Object> headers, String body)
specifier|public
name|String
name|process
parameter_list|(
annotation|@
name|Headers
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|String
name|body
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"process() invoked with headers: "
operator|+
name|headers
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|body
return|;
block|}
DECL|method|getHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
block|}
block|}
end_class

end_unit

