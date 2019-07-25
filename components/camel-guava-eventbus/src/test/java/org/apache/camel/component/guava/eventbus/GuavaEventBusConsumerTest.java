begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.guava.eventbus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|guava
operator|.
name|eventbus
package|;
end_package

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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
operator|.
name|EventBus
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
name|BindToRegistry
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

begin_class
DECL|class|GuavaEventBusConsumerTest
specifier|public
class|class
name|GuavaEventBusConsumerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"eventBus"
argument_list|)
DECL|field|eventBus
name|EventBus
name|eventBus
init|=
operator|new
name|EventBus
argument_list|()
decl_stmt|;
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
literal|"guava-eventbus:eventBus"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:allEvents"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"guava-eventbus:eventBus"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:multipliedConsumer"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"guava-eventbus:eventBus?eventClass=org.apache.camel.component.guava.eventbus.MessageWrapper"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:wrapperEvents"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"guava-eventbus:eventBus?listenerInterface=org.apache.camel.component.guava.eventbus.CustomListener"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:customListenerEvents"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"guava-eventbus:eventBus?listenerInterface=org.apache.camel.component.guava.eventbus.CustomMultiEventListener"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:customMultiEventListenerEvents"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|shouldForwardMessageToCamel ()
specifier|public
name|void
name|shouldForwardMessageToCamel
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
name|String
name|message
init|=
literal|"message"
decl_stmt|;
comment|// When
name|eventBus
operator|.
name|post
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// Then
name|getMockEndpoint
argument_list|(
literal|"mock:allEvents"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|message
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:allEvents"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldForwardMessageToMultipleConsumers ()
specifier|public
name|void
name|shouldForwardMessageToMultipleConsumers
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
name|String
name|message
init|=
literal|"message"
decl_stmt|;
comment|// When
name|eventBus
operator|.
name|post
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// Then
name|getMockEndpoint
argument_list|(
literal|"mock:allEvents"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:multipliedConsumer"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|message
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:allEvents"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:multipliedConsumer"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFilterForwardedMessages ()
specifier|public
name|void
name|shouldFilterForwardedMessages
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
name|MessageWrapper
name|wrappedMessage
init|=
operator|new
name|MessageWrapper
argument_list|(
literal|"message"
argument_list|)
decl_stmt|;
comment|// When
name|eventBus
operator|.
name|post
argument_list|(
name|wrappedMessage
argument_list|)
expr_stmt|;
name|eventBus
operator|.
name|post
argument_list|(
literal|"String message."
argument_list|)
expr_stmt|;
comment|// Then
name|getMockEndpoint
argument_list|(
literal|"mock:wrapperEvents"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|wrappedMessage
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:wrapperEvents"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldUseCustomListener ()
specifier|public
name|void
name|shouldUseCustomListener
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
name|MessageWrapper
name|wrappedMessage
init|=
operator|new
name|MessageWrapper
argument_list|(
literal|"message"
argument_list|)
decl_stmt|;
comment|// When
name|eventBus
operator|.
name|post
argument_list|(
name|wrappedMessage
argument_list|)
expr_stmt|;
name|eventBus
operator|.
name|post
argument_list|(
literal|"String message."
argument_list|)
expr_stmt|;
comment|// Then
name|getMockEndpoint
argument_list|(
literal|"mock:customListenerEvents"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|wrappedMessage
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:customListenerEvents"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportMultiEventCustomListener ()
specifier|public
name|void
name|shouldSupportMultiEventCustomListener
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
name|String
name|stringEvent
init|=
literal|"stringEvent"
decl_stmt|;
name|Date
name|dateEvent
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
comment|// When
name|eventBus
operator|.
name|post
argument_list|(
name|stringEvent
argument_list|)
expr_stmt|;
name|eventBus
operator|.
name|post
argument_list|(
name|dateEvent
argument_list|)
expr_stmt|;
comment|// Then
name|getMockEndpoint
argument_list|(
literal|"mock:customMultiEventListenerEvents"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|stringEvent
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:customMultiEventListenerEvents"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dateEvent
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:customMultiEventListenerEvents"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

