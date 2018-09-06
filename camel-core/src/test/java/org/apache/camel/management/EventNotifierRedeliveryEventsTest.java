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
name|ExchangeCompletedEvent
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
name|ExchangeCreatedEvent
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
name|ExchangeFailureHandledEvent
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
name|ExchangeFailureHandlingEvent
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
name|ExchangeRedeliveryEvent
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|EventNotifierRedeliveryEventsTest
specifier|public
class|class
name|EventNotifierRedeliveryEventsTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|events
specifier|private
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
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
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
name|setIgnoreCamelContextEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreRouteEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreServiceEvents
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
DECL|method|testExchangeRedeliverySync ()
specifier|public
name|void
name|testExchangeRedeliverySync
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|4
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
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
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|ExchangeCreatedEvent
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
expr_stmt|;
name|ExchangeRedeliveryEvent
name|e
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeRedeliveryEvent
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e
operator|.
name|getAttempt
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeRedeliveryEvent
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
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e
operator|.
name|getAttempt
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeRedeliveryEvent
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
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|e
operator|.
name|getAttempt
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeRedeliveryEvent
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
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|e
operator|.
name|getAttempt
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|ExchangeFailureHandlingEvent
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
expr_stmt|;
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
literal|7
argument_list|)
argument_list|)
expr_stmt|;
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
literal|8
argument_list|)
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|ExchangeFailureHandledEvent
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
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|ExchangeCompletedEvent
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
expr_stmt|;
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
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeRedeliveryAsync ()
specifier|public
name|void
name|testExchangeRedeliveryAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|4
argument_list|)
operator|.
name|asyncDelayedRedelivery
argument_list|()
operator|.
name|redeliveryDelay
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
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
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|ExchangeCreatedEvent
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
expr_stmt|;
name|ExchangeRedeliveryEvent
name|e
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeRedeliveryEvent
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e
operator|.
name|getAttempt
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeRedeliveryEvent
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
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e
operator|.
name|getAttempt
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeRedeliveryEvent
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
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|e
operator|.
name|getAttempt
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeRedeliveryEvent
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
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|e
operator|.
name|getAttempt
argument_list|()
argument_list|)
expr_stmt|;
comment|// since its async the ordering of the rest can be different depending per OS and timing
block|}
block|}
end_class

end_unit

