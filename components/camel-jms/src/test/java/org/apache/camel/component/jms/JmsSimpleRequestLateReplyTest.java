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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_comment
comment|/**  * A simple request / late reply test.  */
end_comment

begin_class
DECL|class|JmsSimpleRequestLateReplyTest
specifier|public
class|class
name|JmsSimpleRequestLateReplyTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JmsSimpleRequestLateReplyTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|replyDestination
specifier|private
specifier|static
name|Destination
name|replyDestination
decl_stmt|;
DECL|field|cid
specifier|private
specifier|static
name|String
name|cid
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|String
name|expectedBody
init|=
literal|"Late Reply"
decl_stmt|;
DECL|field|activeMQComponent
specifier|protected
name|JmsComponent
name|activeMQComponent
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testRequestLateReplyUsingCustomDestinationHeaderForReply ()
specifier|public
name|void
name|testRequestLateReplyUsingCustomDestinationHeaderForReply
parameter_list|()
throws|throws
name|Exception
block|{
name|doTest
argument_list|(
operator|new
name|SendLateReply
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doTest (Runnable runnable)
specifier|protected
name|void
name|doTest
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|// use another thread to send the late reply to simulate that we do it later, not
comment|// from the original route anyway
operator|new
name|Thread
argument_list|(
name|runnable
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|getQueueEndpointName
argument_list|()
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedBody
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|class|SendLateReply
specifier|private
class|class
name|SendLateReply
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Waiting for latch"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
comment|// wait 1 sec after latch before sending he late replay
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Sending late reply"
argument_list|)
expr_stmt|;
comment|// use some dummy queue as we override this with the property: JmsConstants.JMS_DESTINATION
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
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|,
name|replyDestination
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|cid
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"activemq:dummy"
argument_list|,
name|expectedBody
argument_list|,
name|headers
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
name|activeMQComponent
operator|=
name|camelContext
operator|.
name|getComponent
argument_list|(
literal|"activemq"
argument_list|,
name|JmsComponent
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// as this is a unit test I dont want to wait 20 sec before timeout occurs, so we use 10
name|activeMQComponent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setRequestTimeout
argument_list|(
literal|10000
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
name|getQueueEndpointName
argument_list|()
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
comment|// set the MEP to InOnly as we are not able to send a reply right now but will do it later
comment|// from that other thread
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|replyDestination
operator|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"JMSReplyTo"
argument_list|,
name|Destination
operator|.
name|class
argument_list|)
expr_stmt|;
name|cid
operator|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"ReplyDestination: "
operator|+
name|replyDestination
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"JMSCorrelationID: "
operator|+
name|cid
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Ahh I cannot send a reply. Someone else must do it."
argument_list|)
expr_stmt|;
comment|// signal to the other thread to send back the reply message
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
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
DECL|method|getQueueEndpointName ()
specifier|protected
specifier|static
name|String
name|getQueueEndpointName
parameter_list|()
block|{
comment|// need to use a fixed queue for reply as a temp queue may be deleted
return|return
literal|"activemq:queue:hello.queue?replyTo=myReplyQueue"
return|;
block|}
block|}
end_class

end_unit

