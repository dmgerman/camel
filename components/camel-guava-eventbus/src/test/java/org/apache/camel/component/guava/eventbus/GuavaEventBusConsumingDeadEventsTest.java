begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DeadEvent
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
name|JndiRegistry
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
DECL|class|GuavaEventBusConsumingDeadEventsTest
specifier|public
class|class
name|GuavaEventBusConsumingDeadEventsTest
extends|extends
name|CamelTestSupport
block|{
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
literal|"guava-eventbus:eventBus?listenerInterface=org.apache.camel.component.guava.eventbus.DeadEventListener"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:deadEvents"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"eventBus"
argument_list|,
name|eventBus
argument_list|)
expr_stmt|;
return|return
name|registry
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
name|Date
name|message
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
name|message
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
literal|0
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:deadEvents"
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
literal|"mock:deadEvents"
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
argument_list|(
name|DeadEvent
operator|.
name|class
argument_list|)
operator|.
name|getEvent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

