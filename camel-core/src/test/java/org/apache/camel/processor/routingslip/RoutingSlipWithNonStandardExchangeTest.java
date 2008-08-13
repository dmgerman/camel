begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.routingslip
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|routingslip
package|;
end_package

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

begin_class
DECL|class|RoutingSlipWithNonStandardExchangeTest
specifier|public
class|class
name|RoutingSlipWithNonStandardExchangeTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|ANSWER
specifier|protected
specifier|static
specifier|final
name|String
name|ANSWER
init|=
literal|"answer"
decl_stmt|;
DECL|field|ROUTING_SLIP_HEADER
specifier|protected
specifier|static
specifier|final
name|String
name|ROUTING_SLIP_HEADER
init|=
literal|"routingSlipHeader"
decl_stmt|;
DECL|method|testRoutingSlipPreservesDifferentExchange ()
specifier|public
name|void
name|testRoutingSlipPreservesDifferentExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
decl_stmt|;
name|end
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
name|ROUTING_SLIP_HEADER
argument_list|,
literal|","
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|end
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|DummyExchange
operator|.
name|class
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|sendBody (String endpoint, String header, String delimiter)
specifier|protected
name|void
name|sendBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|String
name|header
parameter_list|,
name|String
name|delimiter
parameter_list|)
block|{
name|DummyExchange
name|exchange
init|=
operator|new
name|DummyExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
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
name|setHeader
argument_list|(
name|header
argument_list|,
literal|"mock:y"
operator|+
name|delimiter
operator|+
literal|"mock:z"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|routingSlip
argument_list|()
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

