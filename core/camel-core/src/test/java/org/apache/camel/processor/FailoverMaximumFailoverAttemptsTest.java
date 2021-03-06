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

begin_class
DECL|class|FailoverMaximumFailoverAttemptsTest
specifier|public
class|class
name|FailoverMaximumFailoverAttemptsTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testFailoverMaximumFailoverAttempts ()
specifier|public
name|void
name|testFailoverMaximumFailoverAttempts
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:bad"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bad2"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bad3"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:good"
argument_list|)
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
name|assertEquals
argument_list|(
literal|"Damn Again Again"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// as its round robin based it remembers that last good endpoint
comment|// and will invoke the next
name|resetMocks
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bad"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bad2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bad3"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:good"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
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
literal|"direct:start"
argument_list|)
operator|.
name|loadBalance
argument_list|()
operator|.
name|failover
argument_list|(
literal|2
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:bad"
argument_list|,
literal|"direct:bad2"
argument_list|,
literal|"direct:bad3"
argument_list|,
literal|"direct:good"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bad"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bad"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bad"
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
literal|"direct:bad2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bad2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bad2"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn Again"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bad3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bad3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bad3"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn Again Again"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:good"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:good"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:good"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

