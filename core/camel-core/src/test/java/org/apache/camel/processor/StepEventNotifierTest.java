begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spi
operator|.
name|CamelEvent
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
name|Test
import|;
end_import

begin_class
DECL|class|StepEventNotifierTest
specifier|public
class|class
name|StepEventNotifierTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|notifier
specifier|private
name|MyEventNotifier
name|notifier
init|=
operator|new
name|MyEventNotifier
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testStepEventNotifier ()
specifier|public
name|void
name|testStepEventNotifier
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addService
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
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
literal|4
argument_list|,
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CamelEvent
operator|.
name|StepStartedEvent
operator|.
name|class
argument_list|,
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CamelEvent
operator|.
name|StepCompletedEvent
operator|.
name|class
argument_list|,
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CamelEvent
operator|.
name|StepStartedEvent
operator|.
name|class
argument_list|,
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CamelEvent
operator|.
name|StepCompletedEvent
operator|.
name|class
argument_list|,
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
operator|(
operator|(
name|CamelEvent
operator|.
name|StepEvent
operator|)
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getStepId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
operator|(
operator|(
name|CamelEvent
operator|.
name|StepEvent
operator|)
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getStepId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
operator|(
operator|(
name|CamelEvent
operator|.
name|StepEvent
operator|)
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|getStepId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
operator|(
operator|(
name|CamelEvent
operator|.
name|StepEvent
operator|)
name|notifier
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|getStepId
argument_list|()
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
name|step
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|step
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|end
argument_list|()
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
DECL|class|MyEventNotifier
specifier|private
class|class
name|MyEventNotifier
extends|extends
name|EventNotifierSupport
block|{
DECL|field|events
specifier|private
specifier|final
name|List
argument_list|<
name|CamelEvent
argument_list|>
name|events
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|MyEventNotifier ()
specifier|public
name|MyEventNotifier
parameter_list|()
block|{
name|setIgnoreCamelContextEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreExchangeEvents
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
name|setIgnoreStepEvents
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|notify (CamelEvent event)
specifier|public
name|void
name|notify
parameter_list|(
name|CamelEvent
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
DECL|method|getEvents ()
specifier|public
name|List
argument_list|<
name|CamelEvent
argument_list|>
name|getEvents
parameter_list|()
block|{
return|return
name|events
return|;
block|}
block|}
block|}
end_class

end_unit

