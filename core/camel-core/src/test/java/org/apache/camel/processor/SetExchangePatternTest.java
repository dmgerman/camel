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

begin_class
DECL|class|SetExchangePatternTest
specifier|public
class|class
name|SetExchangePatternTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInOut ()
specifier|public
name|void
name|testInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageReceivedWithPattern
argument_list|(
literal|"direct:testInOut"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnly ()
specifier|public
name|void
name|testInOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageReceivedWithPattern
argument_list|(
literal|"direct:testInOnly"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetToInOnlyThenTo ()
specifier|public
name|void
name|testSetToInOnlyThenTo
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageReceivedWithPattern
argument_list|(
literal|"direct:testSetToInOnlyThenTo"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetToInOutThenTo ()
specifier|public
name|void
name|testSetToInOutThenTo
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageReceivedWithPattern
argument_list|(
literal|"direct:testSetToInOutThenTo"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToWithInOnlyParam ()
specifier|public
name|void
name|testToWithInOnlyParam
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageReceivedWithPattern
argument_list|(
literal|"direct:testToWithInOnlyParam"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToWithInOutParam ()
specifier|public
name|void
name|testToWithInOutParam
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageReceivedWithPattern
argument_list|(
literal|"direct:testToWithInOutParam"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetExchangePatternInOnly ()
specifier|public
name|void
name|testSetExchangePatternInOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageReceivedWithPattern
argument_list|(
literal|"direct:testSetExchangePatternInOnly"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPreserveOldMEPInOut ()
specifier|public
name|void
name|testPreserveOldMEPInOut
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the mock should get an InOut MEP
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
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangePattern
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
comment|// we send an InOnly
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:testInOut"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// the MEP should be preserved
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|out
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPreserveOldMEPInOnly ()
specifier|public
name|void
name|testPreserveOldMEPInOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the mock should get an InOnly MEP
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
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangePattern
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
comment|// we send an InOut
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:testInOnly"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// the MEP should be preserved
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|out
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|assertMessageReceivedWithPattern (String sendUri, ExchangePattern expectedPattern)
specifier|protected
name|void
name|assertMessageReceivedWithPattern
parameter_list|(
name|String
name|sendUri
parameter_list|,
name|ExchangePattern
name|expectedPattern
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|ExchangePattern
name|sendPattern
decl_stmt|;
switch|switch
condition|(
name|expectedPattern
condition|)
block|{
case|case
name|InOut
case|:
name|sendPattern
operator|=
name|ExchangePattern
operator|.
name|InOnly
expr_stmt|;
break|break;
case|case
name|InOnly
case|:
name|sendPattern
operator|=
name|ExchangePattern
operator|.
name|InOut
expr_stmt|;
break|break;
default|default:
name|sendPattern
operator|=
name|ExchangePattern
operator|.
name|InOnly
expr_stmt|;
block|}
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|String
name|expectedBody
init|=
literal|"InOnlyMessage"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sendUri
argument_list|,
name|sendPattern
argument_list|,
name|expectedBody
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|ExchangePattern
name|actualPattern
init|=
name|resultEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPattern
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"received exchange pattern"
argument_list|,
name|actualPattern
argument_list|,
name|expectedPattern
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
comment|// START SNIPPET: example
comment|// Send to an endpoint using InOut
name|from
argument_list|(
literal|"direct:testInOut"
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// Send to an endpoint using InOut
name|from
argument_list|(
literal|"direct:testInOnly"
argument_list|)
operator|.
name|inOnly
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// Set the exchange pattern to InOut, then send it from direct:inOnly to mock:result endpoint
name|from
argument_list|(
literal|"direct:testSetToInOnlyThenTo"
argument_list|)
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:testSetToInOutThenTo"
argument_list|)
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// Or we can pass the pattern as a parameter to the to() method
name|from
argument_list|(
literal|"direct:testToWithInOnlyParam"
argument_list|)
operator|.
name|to
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:testToWithInOutParam"
argument_list|)
operator|.
name|to
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// Set the exchange pattern to InOut, then send it on
name|from
argument_list|(
literal|"direct:testSetExchangePatternInOnly"
argument_list|)
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

