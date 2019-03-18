begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|net
operator|.
name|ConnectException
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
name|CamelExecutionException
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
name|LoggingLevel
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
name|model
operator|.
name|RouteDefinition
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
name|reifier
operator|.
name|RouteReifier
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
comment|/**  * Based on user forum issue  */
end_comment

begin_class
DECL|class|RouteScopedOnExceptionWithInterceptSendToEndpointIssueTest
specifier|public
class|class
name|RouteScopedOnExceptionWithInterceptSendToEndpointIssueTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testIssue ()
specifier|public
name|void
name|testIssue
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|RouteReifier
operator|.
name|adviceWith
argument_list|(
name|route
argument_list|,
name|context
argument_list|,
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
name|interceptSendToEndpoint
argument_list|(
literal|"seda:*"
argument_list|)
operator|.
name|skipSendToOriginalEndpoint
argument_list|()
operator|.
name|throwException
argument_list|(
operator|new
name|ConnectException
argument_list|(
literal|"Forced"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:global"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:seda"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// we fail all redeliveries so after that we send to mock:exhausted
name|getMockEndpoint
argument_list|(
literal|"mock:exhausted"
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
literal|"Should thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|ConnectException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ConnectException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"mock:global"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|5000
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// no redelivery delay for faster unit tests
operator|.
name|onException
argument_list|(
name|ConnectException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|5
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|logRetryAttempted
argument_list|(
literal|true
argument_list|)
operator|.
name|retryAttemptedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|)
comment|// send to mock when we are exhausted
operator|.
name|to
argument_list|(
literal|"mock:exhausted"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

