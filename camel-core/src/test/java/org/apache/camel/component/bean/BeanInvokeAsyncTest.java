begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletableFuture
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
name|Assert
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
comment|/**  * Unit test for Java 8 {@link CompletableFuture} as return type on a bean being called from a Camel route.  */
end_comment

begin_class
DECL|class|BeanInvokeAsyncTest
specifier|public
class|class
name|BeanInvokeAsyncTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|callFuture
specifier|private
specifier|volatile
name|CompletableFuture
argument_list|<
name|Object
argument_list|>
name|callFuture
decl_stmt|;
DECL|field|receivedBody
specifier|private
specifier|volatile
name|String
name|receivedBody
decl_stmt|;
DECL|field|methodInvoked
specifier|private
specifier|volatile
name|CountDownLatch
name|methodInvoked
decl_stmt|;
DECL|field|sendFuture
specifier|private
name|Future
argument_list|<
name|Object
argument_list|>
name|sendFuture
decl_stmt|;
annotation|@
name|Test
DECL|method|testDoSomething ()
specifier|public
name|void
name|testDoSomething
parameter_list|()
throws|throws
name|Exception
block|{
name|runTestSendBody
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello World"
argument_list|,
name|this
operator|::
name|doSomething
argument_list|)
expr_stmt|;
name|runTestSendBody
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|this
operator|::
name|doSomething
argument_list|)
expr_stmt|;
name|runTestSendBody
argument_list|(
name|this
operator|::
name|expectNullBody
argument_list|,
literal|null
argument_list|,
name|this
operator|::
name|doSomething
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChangeSomething ()
specifier|public
name|void
name|testChangeSomething
parameter_list|()
throws|throws
name|Exception
block|{
name|runTestSendBody
argument_list|(
literal|"Bye World"
argument_list|,
literal|"Hello World"
argument_list|,
name|this
operator|::
name|changeSomething
argument_list|)
expr_stmt|;
name|runTestSendBody
argument_list|(
literal|"Bye All"
argument_list|,
literal|null
argument_list|,
name|this
operator|::
name|changeSomething
argument_list|)
expr_stmt|;
name|runTestSendBody
argument_list|(
literal|"Bye All"
argument_list|,
literal|""
argument_list|,
name|this
operator|::
name|changeSomething
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoNothing ()
specifier|public
name|void
name|testDoNothing
parameter_list|()
throws|throws
name|Exception
block|{
name|runTestSendBody
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello World"
argument_list|,
name|this
operator|::
name|doNothing
argument_list|)
expr_stmt|;
name|runTestSendBody
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|this
operator|::
name|doNothing
argument_list|)
expr_stmt|;
name|runTestSendBody
argument_list|(
name|this
operator|::
name|expectNullBody
argument_list|,
literal|null
argument_list|,
name|this
operator|::
name|doNothing
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThrowSomething ()
specifier|public
name|void
name|testThrowSomething
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|runTestSendBody
argument_list|(
name|m
lambda|->
name|m
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"SomeProblem"
argument_list|,
name|this
operator|::
name|throwSomething
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|CamelExecutionException
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalStateException
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"SomeProblem"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|runTestSendBody (String expectedBody, String sentBody, Function<String, String> processor)
specifier|private
name|void
name|runTestSendBody
parameter_list|(
name|String
name|expectedBody
parameter_list|,
name|String
name|sentBody
parameter_list|,
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|runTestSendBody
argument_list|(
name|m
lambda|->
name|m
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
argument_list|,
name|sentBody
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|runTestSendBody (Consumer<MockEndpoint> mockPreparer, String sentBody, Function<String, String> processor)
specifier|private
name|void
name|runTestSendBody
parameter_list|(
name|Consumer
argument_list|<
name|MockEndpoint
argument_list|>
name|mockPreparer
parameter_list|,
name|String
name|sentBody
parameter_list|,
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
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
name|reset
argument_list|()
expr_stmt|;
name|mockPreparer
operator|.
name|accept
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|callFuture
operator|=
operator|new
name|CompletableFuture
argument_list|<>
argument_list|()
expr_stmt|;
name|methodInvoked
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendFuture
operator|=
name|template
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:entry"
argument_list|,
name|sentBody
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|methodInvoked
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|sendFuture
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|callFuture
operator|.
name|complete
argument_list|(
name|processor
operator|.
name|apply
argument_list|(
name|receivedBody
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|callFuture
operator|.
name|completeExceptionally
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|sendFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|expectNullBody (MockEndpoint mock)
specifier|private
name|void
name|expectNullBody
parameter_list|(
name|MockEndpoint
name|mock
parameter_list|)
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isNull
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
literal|"direct:entry"
argument_list|)
operator|.
name|bean
argument_list|(
name|BeanInvokeAsyncTest
operator|.
name|this
argument_list|,
literal|"asyncMethod"
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
comment|// java 8 async return type
DECL|method|asyncMethod (String body)
specifier|public
name|CompletableFuture
argument_list|<
name|?
argument_list|>
name|asyncMethod
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|this
operator|.
name|receivedBody
operator|=
name|body
expr_stmt|;
name|methodInvoked
operator|.
name|countDown
argument_list|()
expr_stmt|;
return|return
name|callFuture
return|;
block|}
DECL|method|doSomething (String s)
specifier|public
name|String
name|doSomething
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
return|;
block|}
DECL|method|changeSomething (String s)
specifier|public
name|String
name|changeSomething
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
literal|"Hello World"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
literal|"Bye World"
return|;
block|}
return|return
literal|"Bye All"
return|;
block|}
DECL|method|doNothing (String s)
specifier|public
name|String
name|doNothing
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
DECL|method|throwSomething (String s)
specifier|public
name|String
name|throwSomething
parameter_list|(
name|String
name|s
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|s
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

