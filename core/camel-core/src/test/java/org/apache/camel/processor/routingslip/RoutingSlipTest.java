begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ExpressionBuilder
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

begin_class
DECL|class|RoutingSlipTest
specifier|public
class|class
name|RoutingSlipTest
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
literal|"myHeader"
decl_stmt|;
annotation|@
name|Test
DECL|method|testUpdatingOfRoutingSlipAllDefaults ()
specifier|public
name|void
name|testUpdatingOfRoutingSlipAllDefaults
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|x
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|y
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|z
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdatingOfRoutingSlipHeaderSet ()
specifier|public
name|void
name|testUpdatingOfRoutingSlipHeaderSet
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|x
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|y
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|z
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
literal|"aRoutingSlipHeader"
argument_list|,
literal|","
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdatingOfRoutingSlipHeaderAndDelimiterSet ()
specifier|public
name|void
name|testUpdatingOfRoutingSlipHeaderAndDelimiterSet
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|x
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|y
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|z
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:c"
argument_list|,
literal|"aRoutingSlipHeader"
argument_list|,
literal|"#"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBodyExpression ()
specifier|public
name|void
name|testBodyExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|x
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|y
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|z
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"mock:x, mock:y,mock:z"
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"mock:x, mock:y,mock:z"
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"mock:x, mock:y,mock:z"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:d"
argument_list|,
literal|"mock:x, mock:y,mock:z"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessagePassingThrough ()
specifier|public
name|void
name|testMessagePassingThrough
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEmptyRoutingSlip ()
specifier|public
name|void
name|testEmptyRoutingSlip
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|end
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBodyWithEmptyRoutingSlip
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoRoutingSlip ()
specifier|public
name|void
name|testNoRoutingSlip
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|end
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBodyWithNoRoutingSlip
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|endpoint
argument_list|,
name|ANSWER
argument_list|,
name|header
argument_list|,
literal|"mock:x"
operator|+
name|delimiter
operator|+
literal|"mock:y"
operator|+
name|delimiter
operator|+
literal|"mock:z"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendBodyWithEmptyRoutingSlip ()
specifier|protected
name|void
name|sendBodyWithEmptyRoutingSlip
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
name|ANSWER
argument_list|,
name|ROUTING_SLIP_HEADER
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|sendBodyWithNoRoutingSlip ()
specifier|protected
name|void
name|sendBodyWithNoRoutingSlip
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
name|ANSWER
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
argument_list|(
name|header
argument_list|(
literal|"myHeader"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
comment|// START SNIPPET: e2
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|routingSlip
argument_list|(
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
literal|"aRoutingSlipHeader"
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
comment|// START SNIPPET: e3
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|routingSlip
argument_list|(
name|header
argument_list|(
literal|"aRoutingSlipHeader"
argument_list|)
argument_list|,
literal|"#"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e3
comment|// START SNIPPET: e4
name|from
argument_list|(
literal|"direct:d"
argument_list|)
operator|.
name|routingSlip
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e4
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

