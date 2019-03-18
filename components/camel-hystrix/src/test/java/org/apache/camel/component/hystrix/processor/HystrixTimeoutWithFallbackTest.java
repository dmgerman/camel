begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hystrix.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hystrix
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
name|RoutesBuilder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
comment|/**  * Hystrix using timeout and fallback with Java DSL  */
end_comment

begin_class
DECL|class|HystrixTimeoutWithFallbackTest
specifier|public
class|class
name|HystrixTimeoutWithFallbackTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testFast ()
specifier|public
name|void
name|testFast
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this calls the fast route and therefore we get a response
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"fast"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fast response"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSlow ()
specifier|public
name|void
name|testSlow
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this calls the slow route and therefore causes a timeout which triggers the fallback
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"slow"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fallback response"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|hystrix
argument_list|()
comment|// use 2 second timeout
operator|.
name|hystrixConfiguration
argument_list|()
operator|.
name|executionTimeoutInMilliseconds
argument_list|(
literal|2000
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"Hystrix processing start: ${threadName}"
argument_list|)
operator|.
name|toD
argument_list|(
literal|"direct:${body}"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Hystrix processing end: ${threadName}"
argument_list|)
operator|.
name|onFallback
argument_list|()
comment|// use fallback if there was an exception or timeout
operator|.
name|log
argument_list|(
literal|"Hystrix fallback start: ${threadName}"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Fallback response"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Hystrix fallback end: ${threadName}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"After Hystrix ${body}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:fast"
argument_list|)
comment|// this is a fast route and takes 1 second to respond
operator|.
name|log
argument_list|(
literal|"Fast processing start: ${threadName}"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Fast response"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Fast processing end: ${threadName}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:slow"
argument_list|)
comment|// this is a slow route and takes 3 second to respond
operator|.
name|log
argument_list|(
literal|"Slow processing start: ${threadName}"
argument_list|)
operator|.
name|delay
argument_list|(
literal|3000
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Slow response"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Slow processing end: ${threadName}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

