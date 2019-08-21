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
name|RuntimeCamelException
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
name|ValidationException
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

begin_class
DECL|class|ValidationTest
specifier|public
class|class
name|ValidationTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|validator
specifier|protected
name|Processor
name|validator
init|=
operator|new
name|MyValidator
argument_list|()
decl_stmt|;
DECL|field|validEndpoint
specifier|protected
name|MockEndpoint
name|validEndpoint
decl_stmt|;
DECL|field|invalidEndpoint
specifier|protected
name|MockEndpoint
name|invalidEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testValidMessage ()
specifier|public
name|void
name|testValidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|invalidEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<valid/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"validResult"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidMessage ()
specifier|public
name|void
name|testInvalidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|invalidEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<invalid/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"notMatchedHeaderValue"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
comment|// the expected empty catch block here is not intended for this
comment|// class itself but the subclasses
comment|// e.g. ValidationWithErrorInHandleAndFinallyBlockTest where
comment|// noErrorHandler() is being installed.
comment|// this's also why there's no fail("Should have thrown an
comment|// exception") call here right after
comment|// template.sendBodyAndHeader() call as RuntimeCamelException will
comment|// be not thrown by *all* subclasses
comment|// but only by some of them.
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testinvalidThenValidMessage ()
specifier|public
name|void
name|testinvalidThenValidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|invalidEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<invalid/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"notMatchedHeaderValue"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
comment|// the same as above
block|}
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<valid/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"validResult"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<valid/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"validResult"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|validEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:valid"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|invalidEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:invalid"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|validEndpoint
operator|.
name|whenAnyExchangeReceived
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"validResult"
argument_list|)
argument_list|)
expr_stmt|;
name|invalidEndpoint
operator|.
name|whenAnyExchangeReceived
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"invalidResult"
argument_list|)
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|process
argument_list|(
name|validator
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:invalid"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

