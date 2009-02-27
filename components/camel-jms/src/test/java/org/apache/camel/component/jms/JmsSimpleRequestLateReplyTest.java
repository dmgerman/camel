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
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TemporaryQueue
import|;
end_import

begin_comment
comment|/**  * A simple requesr / late reply test using InOptionalOut.  */
end_comment

begin_class
DECL|class|JmsSimpleRequestLateReplyTest
specifier|public
class|class
name|JmsSimpleRequestLateReplyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JmsSimpleRequestLateReplyTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|String
name|expectedBody
init|=
literal|"Late Reply"
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
DECL|field|activeMQComponent
specifier|protected
name|ActiveMQComponent
name|activeMQComponent
decl_stmt|;
DECL|method|testRequestLateReplyUsingCustomDestinationHeaderForReply ()
specifier|public
name|void
name|testRequestLateReplyUsingCustomDestinationHeaderForReply
parameter_list|()
throws|throws
name|Exception
block|{
name|Runnable
name|runnable
init|=
operator|new
name|SendLateReply
argument_list|()
decl_stmt|;
name|doTest
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
DECL|method|testRequestLateReplyUsingDestinationEndpointForReply ()
specifier|public
name|void
name|testRequestLateReplyUsingDestinationEndpointForReply
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use another thread to send the late reply to simulate that we do it later, not
comment|// from the origianl route anyway
name|doTest
argument_list|(
operator|new
name|SendLateReplyUsingTemporaryEndpoint
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
comment|// from the origianl route anyway
name|Thread
name|sender
init|=
operator|new
name|Thread
argument_list|(
name|runnable
argument_list|)
decl_stmt|;
name|sender
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
name|getQueueEndpointName
argument_list|()
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
comment|// we expect a response so InOut
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
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
block|}
block|}
argument_list|)
decl_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedBody
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
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
name|debug
argument_list|(
literal|"Wating for latch"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
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
name|debug
argument_list|(
literal|"Sending late reply"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"activemq:dummy"
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|,
name|replyDestination
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
name|in
operator|.
name|setBody
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|cid
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|SendLateReplyUsingTemporaryEndpoint
specifier|private
class|class
name|SendLateReplyUsingTemporaryEndpoint
implements|implements
name|Runnable
block|{
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
name|debug
argument_list|(
literal|"Wating for latch"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
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
name|debug
argument_list|(
literal|"Sending late reply"
argument_list|)
expr_stmt|;
try|try
block|{
name|JmsEndpoint
name|endpoint
init|=
name|JmsEndpoint
operator|.
name|newInstance
argument_list|(
name|replyDestination
argument_list|,
name|activeMQComponent
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|endpoint
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
name|in
operator|.
name|setBody
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|cid
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed to create the endpoint for "
operator|+
name|replyDestination
argument_list|)
expr_stmt|;
block|}
block|}
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
name|activeMQComponent
operator|=
name|ActiveMQComponent
operator|.
name|activeMQComponent
argument_list|(
literal|"vm://localhost?broker.persistent=false"
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
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|activeMQComponent
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
comment|// set the MEP to InOptionalOut as we might not be able to send a reply
name|from
argument_list|(
name|getQueueEndpointName
argument_list|()
argument_list|)
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOptionalOut
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
name|JmsConstants
operator|.
name|JMS_REPLY_DESTINATION
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
name|debug
argument_list|(
literal|"ReplyDestination: "
operator|+
name|replyDestination
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"JMSCorrelationID: "
operator|+
name|cid
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ahh I cannot send a reply. Someone else must do it."
argument_list|)
expr_stmt|;
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
name|String
name|getQueueEndpointName
parameter_list|()
block|{
comment|// lets use a different queue name for each test
return|return
literal|"activemq:queue:hello."
operator|+
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

