begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|IOException
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

begin_class
DECL|class|OnExceptionComplexWithNestedErrorHandlerRouteTest
specifier|public
class|class
name|OnExceptionComplexWithNestedErrorHandlerRouteTest
extends|extends
name|OnExceptionComplexRouteTest
block|{
DECL|method|testNoError3 ()
specifier|public
name|void
name|testNoError3
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
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start3"
argument_list|,
literal|"<order><type>myType</type><user>James</user></order>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testFunctionalError3 ()
specifier|public
name|void
name|testFunctionalError3
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
literal|"mock:error3"
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
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start3"
argument_list|,
literal|"<order><type>myType</type><user>Func</user></order>"
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
comment|// global error handler
comment|// as its based on a unit test we do not have any delays between and do not log the stack trace
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliverDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|logStackTrace
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// shared for both routes
name|onException
argument_list|(
name|MyTechnicalException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:tech.error"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// route specific on exception for MyFunctionalException
comment|// we MUST use .end() to indicate that this sub block is ended
operator|.
name|onException
argument_list|(
name|MyFunctionalException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"bean:myServiceBean"
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
comment|// route specific on exception for MyFunctionalException that is different than the previous route
comment|// here we marked it as handled and send it to a different destination mock:handled
comment|// we MUST use .end() to indicate that this sub block is ended
operator|.
name|onException
argument_list|(
name|MyFunctionalException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:handled"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"bean:myServiceBean"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:start3"
argument_list|)
comment|// route specific error handler that is different than the global error handler
comment|// here we do not redeliver and send errors to mock:error3 instead of the global endpoint
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error3"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
argument_list|)
comment|// route specific on exception to mark MyFunctionalException as being handled
operator|.
name|onException
argument_list|(
name|MyFunctionalException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|end
argument_list|()
comment|// however we want the IO exceptions to redeliver at most 3 times
operator|.
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"bean:myServiceBean"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

