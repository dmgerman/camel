begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.async
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|async
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
name|spi
operator|.
name|CamelEvent
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
name|spi
operator|.
name|CamelEvent
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
name|Test
import|;
end_import

begin_class
DECL|class|AsyncEndpointEventNotifierSendingTest
specifier|public
class|class
name|AsyncEndpointEventNotifierSendingTest
extends|extends
name|ContextTestSupport
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
annotation|@
name|Test
DECL|method|testAsyncEndpointEventNotifier ()
specifier|public
name|void
name|testAsyncEndpointEventNotifier
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Camel"
argument_list|,
name|reply
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
literal|2
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
literal|3
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
literal|4
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
literal|5
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
literal|6
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
literal|7
argument_list|)
argument_list|)
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
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|ExchangeSendingEvent
operator|||
name|event
operator|instanceof
name|ExchangeSentEvent
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
block|{             }
block|}
argument_list|)
expr_stmt|;
return|return
name|context
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addComponent
argument_list|(
literal|"async"
argument_list|,
operator|new
name|MyAsyncComponent
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|to
argument_list|(
literal|"async:bye:camel?delay=250"
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

