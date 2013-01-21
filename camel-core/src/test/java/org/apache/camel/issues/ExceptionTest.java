begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
DECL|class|ExceptionTest
specifier|public
class|class
name|ExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testExceptionWithoutHandler ()
specifier|public
name|void
name|testExceptionWithoutHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|errorEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|exceptionEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|errorEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<exception/>"
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// we don't expect any thrown exception here as there's no onException clause defined for this test
comment|// so that the general purpose dead letter channel will come into the play and then when all the attempts
comment|// to redelivery fails the exchange will be moved to "mock:error" and then from the client point of
comment|// view the exchange is completed.
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<body/>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptionWithHandler ()
specifier|public
name|void
name|testExceptionWithHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|errorEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|exceptionEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|errorEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<exception/>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<body/>"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptionWithLongHandler ()
specifier|public
name|void
name|testExceptionWithLongHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|errorEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|exceptionEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|errorEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<not-handled/>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<body/>"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testLongRouteWithHandler ()
specifier|public
name|void
name|testLongRouteWithHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|errorEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|exceptionEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|errorEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<exception/>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"<body/>"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
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
specifier|final
name|Processor
name|exceptionThrower
init|=
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<exception/>"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Exception thrown intentionally."
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"WithLongHandler"
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using long exception handler"
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"<not-handled/>"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exception"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"WithHandler"
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using exception handler"
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exception"
argument_list|)
expr_stmt|;
block|}
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
name|exceptionThrower
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"<some-value/>"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|exceptionThrower
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

