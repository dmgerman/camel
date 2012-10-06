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
name|CamelExchangeException
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SplitterParallelStopOnExceptionTest
specifier|public
class|class
name|SplitterParallelStopOnExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|service
specifier|private
name|ExecutorService
name|service
decl_stmt|;
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use a pool with 2 concurrent tasks so we cannot run too fast
name|service
operator|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|service
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|method|testSplitParallelStopOnExceptionOk ()
specifier|public
name|void
name|testSplitParallelStopOnExceptionOk
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World,Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testSplitParallelStopOnExceptionStop ()
specifier|public
name|void
name|testSplitParallelStopOnExceptionStop
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|isNotEqualTo
argument_list|(
literal|"Kaboom"
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
literal|"Hello World,Goodday World,Kaboom,Bye World"
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
name|CamelExchangeException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|CamelExchangeException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Parallel processing failed for number "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Exchange[Message: Kaboom]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|cause
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
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|stopOnException
argument_list|()
operator|.
name|executorService
argument_list|(
name|service
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
name|String
name|body
init|=
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
decl_stmt|;
if|if
condition|(
literal|"Kaboom"
operator|.
name|equals
argument_list|(
name|body
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced"
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

