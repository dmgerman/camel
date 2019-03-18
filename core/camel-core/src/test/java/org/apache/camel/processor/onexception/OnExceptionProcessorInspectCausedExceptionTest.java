begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.onexception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|onexception
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
comment|/**  * Unit test for using a processor to peek the caused exception  */
end_comment

begin_class
DECL|class|OnExceptionProcessorInspectCausedExceptionTest
specifier|public
class|class
name|OnExceptionProcessorInspectCausedExceptionTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInspectExceptionByProcessor ()
specifier|public
name|void
name|testInspectExceptionByProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:myerror"
argument_list|)
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
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ok
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e1
comment|// here we register exception cause for MyFunctionException
comment|// when this exception occur we want it to be processed by our processor
name|onException
argument_list|(
name|MyFunctionalException
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyFunctionFailureHandler
argument_list|()
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
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
throw|throw
operator|new
name|MyFunctionalException
argument_list|(
literal|"Sorry you cannot do this"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
DECL|class|MyFunctionFailureHandler
specifier|public
specifier|static
class|class
name|MyFunctionFailureHandler
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
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
comment|// the caused by exception is stored in a property on the exchange
name|Throwable
name|caused
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|caused
argument_list|)
expr_stmt|;
comment|// here you can do what you want, but Camel regard this exception as handled, and
comment|// this processor as a failurehandler, so it wont do redeliveries. So this is the
comment|// end of this route. But if we want to route it somewhere we can just get a
comment|// producer template and send it.
comment|// send it to our mock endpoint
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|send
argument_list|(
literal|"mock:myerror"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e2
block|}
end_class

end_unit

