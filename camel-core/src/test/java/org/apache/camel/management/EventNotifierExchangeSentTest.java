begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

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
name|EventObject
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
name|concurrent
operator|.
name|TimeUnit
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
name|management
operator|.
name|event
operator|.
name|ExchangeSendingEvent
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
name|management
operator|.
name|event
operator|.
name|ExchangeSentEvent
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
name|support
operator|.
name|EventNotifierSupport
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
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|EventNotifierExchangeSentTest
specifier|public
class|class
name|EventNotifierExchangeSentTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|events
specifier|protected
specifier|static
name|List
argument_list|<
name|EventObject
argument_list|>
name|events
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|events
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
operator|new
name|EventNotifierSupport
argument_list|()
block|{
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|events
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// filter out unwanted events
name|setIgnoreCamelContextEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreServiceEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreRouteEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreExchangeCreatedEvent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreExchangeCompletedEvent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreExchangeFailedEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreExchangeRedeliveryEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{             }
block|}
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testExchangeSent ()
specifier|public
name|void
name|testExchangeSent
parameter_list|()
throws|throws
name|Exception
block|{
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ExchangeSendingEvent
name|e0
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSendingEvent
name|e1
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e2
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSendingEvent
name|e3
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e4
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSendingEvent
name|e5
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e6
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e7
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|7
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|e0
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://foo"
argument_list|,
name|e1
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://foo"
argument_list|,
name|e2
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://bar"
argument_list|,
name|e3
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://bar"
argument_list|,
name|e4
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|time
init|=
name|e4
operator|.
name|getTimeTaken
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take about 0.5 sec, was: "
operator|+
name|time
argument_list|,
name|time
operator|>
literal|400
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://result"
argument_list|,
name|e5
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://result"
argument_list|,
name|e6
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|e7
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|time
operator|=
name|e7
operator|.
name|getTimeTaken
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take about 0.5 sec, was: "
operator|+
name|time
argument_list|,
name|time
operator|>
literal|400
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeSentRecipient ()
specifier|public
name|void
name|testExchangeSentRecipient
parameter_list|()
throws|throws
name|Exception
block|{
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
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"direct:cool,direct:start"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ExchangeSendingEvent
name|e0
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSendingEvent
name|e1
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e2
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSendingEvent
name|e3
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSendingEvent
name|e4
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e5
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSendingEvent
name|e6
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e7
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|7
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSendingEvent
name|e8
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSendingEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e9
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|9
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e10
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|ExchangeSentEvent
name|e11
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeSentEvent
operator|.
name|class
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|11
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"direct://foo"
argument_list|,
name|e0
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://cool"
argument_list|,
name|e1
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://cool"
argument_list|,
name|e2
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|e3
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://foo"
argument_list|,
name|e4
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://foo"
argument_list|,
name|e5
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://bar"
argument_list|,
name|e6
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://bar"
argument_list|,
name|e7
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://result"
argument_list|,
name|e8
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://result"
argument_list|,
name|e9
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|e10
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://foo"
argument_list|,
name|e11
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeWireTap ()
specifier|public
name|void
name|testExchangeWireTap
parameter_list|()
throws|throws
name|Exception
block|{
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:tap"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// give it time to complete
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// we should find log:foo which we tapped
comment|// which runs async so they can be in random order
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|boolean
name|found2
init|=
literal|false
decl_stmt|;
for|for
control|(
name|EventObject
name|event
range|:
name|events
control|)
block|{
if|if
condition|(
name|event
operator|instanceof
name|ExchangeSendingEvent
condition|)
block|{
name|ExchangeSendingEvent
name|sending
init|=
operator|(
name|ExchangeSendingEvent
operator|)
name|event
decl_stmt|;
name|String
name|uri
init|=
name|sending
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"log://foo"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|ExchangeSentEvent
condition|)
block|{
name|ExchangeSentEvent
name|sent
init|=
operator|(
name|ExchangeSentEvent
operator|)
name|event
decl_stmt|;
name|String
name|uri
init|=
name|sent
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"log://foo"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|found2
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
name|assertTrue
argument_list|(
literal|"We should find log:foo being wire tapped"
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should find log:foo being wire tapped"
argument_list|,
name|found2
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|delay
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|recipientList
argument_list|()
operator|.
name|header
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:cool"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:tap"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"log:foo"
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

