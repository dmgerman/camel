begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ToEndpointPropertyTest
specifier|public
class|class
name|ToEndpointPropertyTest
extends|extends
name|ContextTestSupport
block|{
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
DECL|method|testSimpleToEndpoint ()
specifier|public
name|void
name|testSimpleToEndpoint
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://result"
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
block|}
DECL|method|testMediumToEndpoint ()
specifier|public
name|void
name|testMediumToEndpoint
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://result"
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
block|}
DECL|method|testRecipientListToEndpoint ()
specifier|public
name|void
name|testRecipientListToEndpoint
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"foo"
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://result"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoutingSlipToEndpoint ()
specifier|public
name|void
name|testRoutingSlipToEndpoint
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routingSlip
argument_list|(
name|header
argument_list|(
literal|"foo"
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://result"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|a
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
decl_stmt|;
name|a
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|a
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://a"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|b
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
decl_stmt|;
name|b
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|b
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://b"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"mock:a,mock:b,mock:result"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testWireTapToEndpoint ()
specifier|public
name|void
name|testWireTapToEndpoint
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"mock:tap"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://result"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|tap
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:tap"
argument_list|)
decl_stmt|;
name|tap
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|tap
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://tap"
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
block|}
DECL|method|testMulticastToEndpoint ()
specifier|public
name|void
name|testMulticastToEndpoint
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|multicast
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:a"
argument_list|,
literal|"direct:b"
argument_list|)
operator|.
name|end
argument_list|()
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
name|String
name|to
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"direct://b"
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"B"
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
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://result"
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
block|}
DECL|method|testDLCToEndpoint ()
specifier|public
name|void
name|testDLCToEndpoint
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
name|disableRedelivery
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
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
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
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|dead
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
decl_stmt|;
name|dead
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"direct://foo"
argument_list|)
expr_stmt|;
name|dead
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://dead"
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
block|}
DECL|method|testMediumDLCToEndpoint ()
specifier|public
name|void
name|testMediumDLCToEndpoint
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
literal|"direct:dead"
argument_list|)
operator|.
name|disableRedelivery
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
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
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
name|from
argument_list|(
literal|"direct:dead"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dead"
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
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|dead
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
decl_stmt|;
name|dead
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"direct://foo"
argument_list|)
expr_stmt|;
name|dead
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://dead"
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
block|}
DECL|method|testMulticastDLC ()
specifier|public
name|void
name|testMulticastDLC
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
name|disableRedelivery
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|multicast
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:a"
argument_list|,
literal|"direct:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
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
name|from
argument_list|(
literal|"direct:dead"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dead"
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
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|dead
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
decl_stmt|;
name|dead
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"direct://b"
argument_list|)
expr_stmt|;
name|dead
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"mock://dead"
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
block|}
block|}
end_class

end_unit

