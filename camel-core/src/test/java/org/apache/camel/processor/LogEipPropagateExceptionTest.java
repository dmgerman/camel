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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Testing CAMEL-4388  */
end_comment

begin_class
DECL|class|LogEipPropagateExceptionTest
specifier|public
class|class
name|LogEipPropagateExceptionTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testFailure ()
specifier|public
name|void
name|testFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:handleFailure"
argument_list|)
operator|.
name|whenAnyExchangeReceived
argument_list|(
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"TEST EXCEPTION"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:exceptionFailure"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:startFailure"
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
DECL|method|testSuccess ()
specifier|public
name|void
name|testSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:handleSuccess"
argument_list|)
operator|.
name|whenAnyExchangeReceived
argument_list|(
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"TEST EXCEPTION"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:exceptionSuccess"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:startSuccess"
argument_list|,
literal|"Hello World"
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
name|from
argument_list|(
literal|"direct:startFailure"
argument_list|)
operator|.
name|onException
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exceptionFailure"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:handleFailure"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultFailure"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:handleFailure"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
literal|"FAULTY LOG"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:handleFailure"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:startSuccess"
argument_list|)
operator|.
name|onException
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exceptionSuccess"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:handleSuccess"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultSuccess"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:handleSuccess"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:handleSuccess"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

