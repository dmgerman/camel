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
name|MDC
import|;
end_import

begin_class
DECL|class|MDCTest
specifier|public
class|class
name|MDCTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testMDC ()
specifier|public
name|void
name|testMDC
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMDCTwoMessages ()
specifier|public
name|void
name|testMDCTwoMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
comment|// enable MDC
name|context
operator|.
name|setUseMDCLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route-a"
argument_list|)
operator|.
name|step
argument_list|(
literal|"step-a"
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
name|assertEquals
argument_list|(
literal|"route-a"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.routeId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.exchangeId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.messageId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"step-a"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.stepId"
argument_list|)
argument_list|)
expr_stmt|;
name|MDC
operator|.
name|put
argument_list|(
literal|"custom.id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:b"
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
name|assertEquals
argument_list|(
literal|"route-a"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.routeId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.exchangeId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.messageId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"step-a"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.stepId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route-b"
argument_list|)
operator|.
name|step
argument_list|(
literal|"step-b"
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
name|assertEquals
argument_list|(
literal|"route-b"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.routeId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.exchangeId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.messageId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"step-b"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.stepId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"custom.id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
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

