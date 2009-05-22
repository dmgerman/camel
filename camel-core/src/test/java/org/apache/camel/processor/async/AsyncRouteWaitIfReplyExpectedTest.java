begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.async
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|async
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
name|WaitForTaskToComplete
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|AsyncRouteWaitIfReplyExpectedTest
specifier|public
class|class
name|AsyncRouteWaitIfReplyExpectedTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|route
specifier|private
specifier|static
name|String
name|route
init|=
literal|""
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
name|route
operator|=
literal|""
expr_stmt|;
block|}
DECL|method|testAsyncReplyExpected ()
specifier|public
name|void
name|testAsyncReplyExpected
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// we should not run before the async processor that sets B
name|route
operator|+=
literal|"A"
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BA"
argument_list|,
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsyncNoReplyExpected ()
specifier|public
name|void
name|testAsyncNoReplyExpected
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
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
literal|"Hello"
argument_list|)
expr_stmt|;
comment|// we should run before the async processor that sets B
name|route
operator|+=
literal|"A"
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AB"
argument_list|,
name|route
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
comment|// we start this route async
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// we play a bit with the message
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|append
argument_list|(
literal|" World"
argument_list|)
argument_list|)
comment|// now turn the route into async from this point forward
comment|// the caller will have a Future<Exchange> returned as response in OUT
comment|// to be used to grape the async response when he fell like it
comment|// we only want to wait for tasks to complete if we expect a reply
comment|// otherwise not
operator|.
name|async
argument_list|()
operator|.
name|waitForTaskToComplete
argument_list|(
name|WaitForTaskToComplete
operator|.
name|IfReplyExpected
argument_list|)
comment|// from this point forward this is the async route doing its work
comment|// so we do a bit of delay to simulate heavy work that takes time
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
comment|// wait a litter longer for the slow box
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
comment|// and we also work with the message so we can prepare a response
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
argument_list|)
comment|// and we use mocks for unit testing
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
name|route
operator|+=
literal|"B"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
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

