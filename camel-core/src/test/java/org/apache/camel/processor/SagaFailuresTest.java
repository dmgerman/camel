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
name|atomic
operator|.
name|AtomicInteger
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|saga
operator|.
name|InMemorySagaService
import|;
end_import

begin_class
DECL|class|SagaFailuresTest
specifier|public
class|class
name|SagaFailuresTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|maxFailures
specifier|private
name|AtomicInteger
name|maxFailures
decl_stmt|;
DECL|method|testCompensationAfterFailures ()
specifier|public
name|void
name|testCompensationAfterFailures
parameter_list|()
throws|throws
name|Exception
block|{
name|maxFailures
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|MockEndpoint
name|compensate
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:compensate"
argument_list|)
decl_stmt|;
name|compensate
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:saga-compensate"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|compensate
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testNoCompensationAfterMaxFailures ()
specifier|public
name|void
name|testNoCompensationAfterMaxFailures
parameter_list|()
throws|throws
name|Exception
block|{
name|maxFailures
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|MockEndpoint
name|compensate
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:compensate"
argument_list|)
decl_stmt|;
name|compensate
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|compensate
operator|.
name|setResultWaitTime
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:saga-compensate"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|compensate
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testCompletionAfterFailures ()
specifier|public
name|void
name|testCompletionAfterFailures
parameter_list|()
throws|throws
name|Exception
block|{
name|maxFailures
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|MockEndpoint
name|complete
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:complete"
argument_list|)
decl_stmt|;
name|complete
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|end
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:saga-complete"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|complete
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|end
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testNoCompletionAfterMaxFailures ()
specifier|public
name|void
name|testNoCompletionAfterMaxFailures
parameter_list|()
throws|throws
name|Exception
block|{
name|maxFailures
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|MockEndpoint
name|complete
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:complete"
argument_list|)
decl_stmt|;
name|complete
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|complete
operator|.
name|setResultWaitTime
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|end
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:saga-complete"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|complete
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
name|end
operator|.
name|assertIsSatisfied
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
name|InMemorySagaService
name|sagaService
init|=
operator|new
name|InMemorySagaService
argument_list|()
decl_stmt|;
name|sagaService
operator|.
name|setMaxRetryAttempts
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|sagaService
operator|.
name|setRetryDelayInMilliseconds
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|sagaService
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:saga-compensate"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|compensation
argument_list|(
literal|"direct:compensate"
argument_list|)
operator|.
name|process
argument_list|(
name|x
lambda|->
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"fail"
argument_list|)
throw|;
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:saga-complete"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|completion
argument_list|(
literal|"direct:complete"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:compensate"
argument_list|)
operator|.
name|process
argument_list|(
name|x
lambda|->
block|{
name|int
name|current
init|=
name|maxFailures
operator|.
name|decrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|>=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"compensation failure"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:compensate"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:complete"
argument_list|)
operator|.
name|process
argument_list|(
name|x
lambda|->
block|{
name|int
name|current
init|=
name|maxFailures
operator|.
name|decrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|>=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"completion failure"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:complete"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

