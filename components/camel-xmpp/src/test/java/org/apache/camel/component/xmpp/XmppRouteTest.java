begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmpp
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|textui
operator|.
name|TestRunner
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
name|Endpoint
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|ProducerCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Message
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
DECL|class|XmppRouteTest
specifier|public
class|class
name|XmppRouteTest
extends|extends
name|TestCase
block|{
DECL|field|enabled
specifier|protected
specifier|static
name|boolean
name|enabled
decl_stmt|;
DECL|field|xmppUrl
specifier|protected
specifier|static
name|String
name|xmppUrl
decl_stmt|;
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
name|XmppRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|receivedExchange
specifier|protected
name|Exchange
name|receivedExchange
decl_stmt|;
DECL|field|container
specifier|protected
name|CamelContext
name|container
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|latch
specifier|protected
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|client
specifier|protected
name|ProducerCache
name|client
decl_stmt|;
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|enabled
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|xmppUrl
operator|=
name|args
index|[
literal|0
index|]
expr_stmt|;
block|}
name|TestRunner
operator|.
name|run
argument_list|(
name|XmppRouteTest
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testXmppRouteWithTextMessage ()
specifier|public
name|void
name|testXmppRouteWithTextMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedBody
init|=
literal|"Hello there!"
decl_stmt|;
name|sendExchange
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|assertReceivedValidExchange
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"body"
argument_list|,
name|expectedBody
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|sendExchange (final Object expectedBody)
specifier|protected
name|void
name|sendExchange
parameter_list|(
specifier|final
name|Object
name|expectedBody
parameter_list|)
block|{
name|client
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
block|{
comment|// now lets fire in a message
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|assertReceivedValidExchange ()
specifier|protected
name|Object
name|assertReceivedValidExchange
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets wait on the message being received
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedExchange
argument_list|)
expr_stmt|;
name|XmppMessage
name|receivedMessage
init|=
operator|(
name|XmppMessage
operator|)
name|receivedExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cheese header"
argument_list|,
literal|123
argument_list|,
name|receivedMessage
operator|.
name|getHeader
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|receivedMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|XmppRouteTest
operator|.
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|body
argument_list|)
expr_stmt|;
name|Message
name|xmppMessage
init|=
name|receivedMessage
operator|.
name|getXmppMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|xmppMessage
argument_list|)
expr_stmt|;
name|XmppRouteTest
operator|.
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received XMPP message: "
operator|+
name|xmppMessage
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|body
return|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|container
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|String
name|uriPrefix
init|=
name|getUriPrefix
argument_list|()
decl_stmt|;
specifier|final
name|String
name|uri1
init|=
name|uriPrefix
operator|+
literal|"&resource=camel-test-from&nickname=came-test-from"
decl_stmt|;
specifier|final
name|String
name|uri2
init|=
name|uriPrefix
operator|+
literal|"&resource=camel-test-to&nickname=came-test-to"
decl_stmt|;
specifier|final
name|String
name|uri3
init|=
name|uriPrefix
operator|+
literal|"&resource=camel-test-from-processor&nickname=came-test-from-processor"
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Using URI "
operator|+
name|uri1
operator|+
literal|" and "
operator|+
name|uri2
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|container
operator|.
name|getEndpoint
argument_list|(
name|uri1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No endpoint found!"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|// lets add some routes
name|container
operator|.
name|addRoutes
argument_list|(
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
name|uri1
argument_list|)
operator|.
name|to
argument_list|(
name|uri2
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|uri3
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
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Received exchange: "
operator|+
name|e
argument_list|)
expr_stmt|;
name|receivedExchange
operator|=
name|e
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|getUriPrefix ()
specifier|protected
name|String
name|getUriPrefix
parameter_list|()
block|{
return|return
literal|"xmpp://localhost:"
operator|+
name|EmbeddedXmppTestServer
operator|.
name|instance
argument_list|()
operator|.
name|getXmppPort
argument_list|()
operator|+
literal|"/camel?login=false&room=camel-anon"
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
name|container
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

