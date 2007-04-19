begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|MemoryMessageIdRepository
operator|.
name|memoryMessageIdRepository
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
name|util
operator|.
name|ProducerCache
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|IdempotentConsumerTest
specifier|public
class|class
name|IdempotentConsumerTest
extends|extends
name|TestCase
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|IdempotentConsumerTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|latch
specifier|protected
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|3
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|client
specifier|protected
name|ProducerCache
argument_list|<
name|Exchange
argument_list|>
name|client
init|=
operator|new
name|ProducerCache
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|receivedBodies
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|receivedBodies
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|testDuplicateMessagesAreFilteredOut ()
specifier|public
name|void
name|testDuplicateMessagesAreFilteredOut
parameter_list|()
throws|throws
name|Exception
block|{
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"2"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"2"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"3"
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
comment|// lets wait on the message being received
name|boolean
name|received
init|=
name|latch
operator|.
name|await
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Did not receive the message!"
argument_list|,
name|received
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have received 3 responses: "
operator|+
name|receivedBodies
argument_list|,
literal|3
argument_list|,
name|receivedBodies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"received bodies"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"one"
block|,
literal|"two"
block|,
literal|"three"
block|}
argument_list|)
argument_list|,
name|receivedBodies
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received bodies: "
operator|+
name|receivedBodies
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (final Object messageId, final Object body)
specifier|protected
name|void
name|sendMessage
parameter_list|(
specifier|final
name|Object
name|messageId
parameter_list|,
specifier|final
name|Object
name|body
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
argument_list|<
name|Exchange
argument_list|>
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
name|body
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"messageId"
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|context
operator|=
name|createContext
argument_list|()
expr_stmt|;
specifier|final
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
init|=
operator|new
name|Processor
argument_list|<
name|Exchange
argument_list|>
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
name|Message
name|in
init|=
name|e
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|body
operator|+
literal|" on exchange: "
operator|+
name|e
argument_list|)
expr_stmt|;
name|receivedBodies
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|final
name|String
name|endpointUri
init|=
literal|"queue:test.a"
decl_stmt|;
comment|// lets add some routes
name|context
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|(
name|endpointUri
argument_list|,
name|processor
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|context
operator|.
name|resolveEndpoint
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No endpoint found for URI: "
operator|+
name|endpointUri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|createContext ()
specifier|protected
name|CamelContext
name|createContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
DECL|method|createRouteBuilder (final String endpointUri, final Processor<Exchange> processor)
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|(
specifier|final
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
parameter_list|)
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
name|endpointUri
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|header
argument_list|(
literal|"messageId"
argument_list|)
argument_list|,
name|memoryMessageIdRepository
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
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
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

