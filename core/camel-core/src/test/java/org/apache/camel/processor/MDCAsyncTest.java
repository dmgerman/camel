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
name|ExecutorService
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
name|Executors
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|RuntimeCamelException
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|AsyncCallbackToCompletableFutureAdapter
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|MDC
import|;
end_import

begin_class
DECL|class|MDCAsyncTest
specifier|public
class|class
name|MDCAsyncTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testMdcPreservedAfterAsyncEndpoint ()
specifier|public
name|void
name|testMdcPreservedAfterAsyncEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|mock
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
literal|"direct:a"
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
comment|// enable MDC and breadcrumb
name|context
operator|.
name|setUseMDCLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setUseBreadcrumb
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setMDCLoggingKeysPattern
argument_list|(
literal|"custom*,my*"
argument_list|)
expr_stmt|;
name|MdcCheckerProcessor
name|checker
init|=
operator|new
name|MdcCheckerProcessor
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route-async"
argument_list|)
operator|.
name|process
argument_list|(
name|e
lambda|->
block|{
comment|// custom is propagated
name|MDC
operator|.
name|put
argument_list|(
literal|"custom.hello"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
comment|// foo is not propagated
name|MDC
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"Bar"
argument_list|)
expr_stmt|;
comment|// myKey is propagated
name|MDC
operator|.
name|put
argument_list|(
literal|"myKey"
argument_list|,
literal|"Baz"
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|process
argument_list|(
name|checker
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyAsyncProcessor
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
name|checker
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyAsyncProcessor
specifier|private
specifier|static
class|class
name|MyAsyncProcessor
implements|implements
name|AsyncProcessor
block|{
DECL|field|EXECUTOR
specifier|private
specifier|static
specifier|final
name|ExecutorService
name|EXECUTOR
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|method|MyAsyncProcessor ()
name|MyAsyncProcessor
parameter_list|()
block|{
comment|// submit a Runnable that does nothing just to initialise the threads
name|EXECUTOR
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// do nothing
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"This processor does not support the sync pattern."
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|processAsync (Exchange exchange)
specifier|public
name|CompletableFuture
argument_list|<
name|Exchange
argument_list|>
name|processAsync
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|AsyncCallbackToCompletableFutureAdapter
argument_list|<
name|Exchange
argument_list|>
name|callback
init|=
operator|new
name|AsyncCallbackToCompletableFutureAdapter
argument_list|<>
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
return|return
name|callback
operator|.
name|getFuture
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|EXECUTOR
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Stores values from the first invocation to compare them with the second invocation later.      */
DECL|class|MdcCheckerProcessor
specifier|private
specifier|static
class|class
name|MdcCheckerProcessor
implements|implements
name|Processor
block|{
DECL|field|routeId
specifier|private
name|String
name|routeId
init|=
literal|"route-async"
decl_stmt|;
DECL|field|exchangeId
specifier|private
name|String
name|exchangeId
decl_stmt|;
DECL|field|messageId
specifier|private
name|String
name|messageId
decl_stmt|;
DECL|field|breadcrumbId
specifier|private
name|String
name|breadcrumbId
decl_stmt|;
DECL|field|contextId
specifier|private
name|String
name|contextId
decl_stmt|;
DECL|field|threadId
specifier|private
name|Long
name|threadId
decl_stmt|;
DECL|field|foo
specifier|private
name|String
name|foo
decl_stmt|;
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
comment|// custom is propagated as its pattern matches
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"custom.hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Baz"
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"myKey"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|foo
operator|!=
literal|null
condition|)
block|{
comment|// foo is not propagated
name|assertNotEquals
argument_list|(
name|foo
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|foo
operator|=
name|MDC
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threadId
operator|!=
literal|null
condition|)
block|{
name|Long
name|currId
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|assertNotEquals
argument_list|(
name|threadId
argument_list|,
name|currId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|threadId
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|routeId
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|routeId
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.routeId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchangeId
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|exchangeId
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.exchangeId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchangeId
operator|=
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.exchangeId"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exchangeId
operator|!=
literal|null
operator|&&
name|exchangeId
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageId
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|messageId
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.messageId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageId
operator|=
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.messageId"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|messageId
operator|!=
literal|null
operator|&&
name|messageId
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|breadcrumbId
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|breadcrumbId
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.breadcrumbId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|breadcrumbId
operator|=
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.breadcrumbId"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|breadcrumbId
operator|!=
literal|null
operator|&&
name|breadcrumbId
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contextId
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|contextId
argument_list|,
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.contextId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|contextId
operator|=
name|MDC
operator|.
name|get
argument_list|(
literal|"camel.contextId"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contextId
operator|!=
literal|null
operator|&&
name|contextId
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

