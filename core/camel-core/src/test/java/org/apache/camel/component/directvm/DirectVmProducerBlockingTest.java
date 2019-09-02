begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.directvm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|directvm
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
name|util
operator|.
name|StopWatch
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
DECL|class|DirectVmProducerBlockingTest
specifier|public
class|class
name|DirectVmProducerBlockingTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testProducerBlocksForSuspendedConsumer ()
specifier|public
name|void
name|testProducerBlocksForSuspendedConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|DirectVmEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct-vm:suspended"
argument_list|,
name|DirectVmEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getConsumer
argument_list|()
operator|.
name|suspend
argument_list|()
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:suspended?block=true&timeout=500&failIfNoConsumers=false"
argument_list|,
literal|"hello world"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected CamelExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|DirectVmConsumerNotAvailableException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|DirectVmConsumerNotAvailableException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CamelExchangeException
operator|.
name|class
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|watch
operator|.
name|taken
argument_list|()
operator|>
literal|490
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testProducerBlocksWithNoConsumers ()
specifier|public
name|void
name|testProducerBlocksWithNoConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|DirectVmEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct-vm:suspended"
argument_list|,
name|DirectVmEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getConsumer
argument_list|()
operator|.
name|suspend
argument_list|()
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:start?block=true&timeout=500&failIfNoConsumers=false"
argument_list|,
literal|"hello world"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected CamelExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|DirectVmConsumerNotAvailableException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|DirectVmConsumerNotAvailableException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CamelExchangeException
operator|.
name|class
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|watch
operator|.
name|taken
argument_list|()
operator|>
literal|490
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testProducerBlocksFailIfNoConsumerFalse ()
specifier|public
name|void
name|testProducerBlocksFailIfNoConsumerFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|DirectVmEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct-vm:suspended"
argument_list|,
name|DirectVmEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getConsumer
argument_list|()
operator|.
name|suspend
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:start?block=true&timeout=500&failIfNoConsumers=true"
argument_list|,
literal|"hello world"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected CamelExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|DirectVmConsumerNotAvailableException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|DirectVmConsumerNotAvailableException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CamelExchangeException
operator|.
name|class
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testProducerBlocksResumeTest ()
specifier|public
name|void
name|testProducerBlocksResumeTest
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|suspendRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
name|executor
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
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Resuming consumer"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|resumeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
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
literal|"direct-vm:suspended?block=true&timeout=1000&failIfNoConsumers=false"
argument_list|,
literal|"hello world"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|executor
operator|.
name|shutdownNow
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct-vm:suspended"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
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
block|}
end_class

end_unit

