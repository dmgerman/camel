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
DECL|class|OnCompletionAsyncTest
specifier|public
class|class
name|OnCompletionAsyncTest
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
annotation|@
name|Test
DECL|method|testAsyncComplete ()
specifier|public
name|void
name|testAsyncComplete
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
name|onCompletion
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|delay
argument_list|(
literal|250
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"OnComplete:${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:after"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
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
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|ON_COMPLETION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"OnComplete:Bye World"
argument_list|)
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
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncFailure ()
specifier|public
name|void
name|testAsyncFailure
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
name|onCompletion
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|delay
argument_list|(
literal|250
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"OnComplete:${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:after"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
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
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Kabom"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|ON_COMPLETION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"OnComplete:Kabom"
argument_list|)
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
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Kabom"
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
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Kabom"
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
block|}
annotation|@
name|Test
DECL|method|testAsyncCompleteUseOriginalBody ()
specifier|public
name|void
name|testAsyncCompleteUseOriginalBody
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
name|onCompletion
argument_list|()
operator|.
name|useOriginalBody
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|delay
argument_list|(
literal|250
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"OnComplete:${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:after"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
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
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|ON_COMPLETION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"OnComplete:Hello World"
argument_list|)
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
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncFailureUseOriginalBody ()
specifier|public
name|void
name|testAsyncFailureUseOriginalBody
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
name|onCompletion
argument_list|()
operator|.
name|useOriginalBody
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|delay
argument_list|(
literal|250
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"OnComplete:${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:after"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Before:${body}"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
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
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Kabom"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|ON_COMPLETION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"OnComplete:Kabom"
argument_list|)
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
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Kabom"
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
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Kabom"
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
block|}
annotation|@
name|Test
DECL|method|testAsyncCompleteOnCompleteFail ()
specifier|public
name|void
name|testAsyncCompleteOnCompleteFail
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
name|onCompletion
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|delay
argument_list|(
literal|250
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"OnComplete:${body}"
argument_list|)
argument_list|)
comment|// this exception does not cause any side effect as we are
comment|// in async mode
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalAccessException
argument_list|(
literal|"From onComplete"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:after"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
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
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|ON_COMPLETION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
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
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyProcessor
specifier|public
specifier|static
class|class
name|MyProcessor
implements|implements
name|Processor
block|{
DECL|method|MyProcessor ()
specifier|public
name|MyProcessor
parameter_list|()
block|{         }
annotation|@
name|Override
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
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Kabom"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Kabom"
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

