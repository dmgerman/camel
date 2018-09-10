begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|TimeUnit
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DirectVmNoConsumerTest
specifier|public
class|class
name|DirectVmNoConsumerTest
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
DECL|method|testInOnly ()
specifier|public
name|void
name|testInOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct-vm"
argument_list|,
name|DirectVmComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct-vm:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct-vm:foo"
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
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
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
expr_stmt|;
block|}
block|}
DECL|method|testInOut ()
specifier|public
name|void
name|testInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct-vm"
argument_list|,
name|DirectVmComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct-vm:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct-vm:foo"
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
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct-vm:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
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
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFailIfNoConsumerFalse ()
specifier|public
name|void
name|testFailIfNoConsumerFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct-vm"
argument_list|,
name|DirectVmComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct-vm:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct-vm:foo?failIfNoConsumers=false"
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
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
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
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFailIfNoConsumersAfterConsumersLeave ()
specifier|public
name|void
name|testFailIfNoConsumersAfterConsumersLeave
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct-vm"
argument_list|,
name|DirectVmComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct-vm:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"stopThisRoute"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
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
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"stopThisRoute"
argument_list|)
expr_stmt|;
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
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
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFailIfNoConsumersWithValidConsumer ()
specifier|public
name|void
name|testFailIfNoConsumersWithValidConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct-vm"
argument_list|,
name|DirectVmComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct-vm:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct-vm:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct-vm:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
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
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:in"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailIfNoConsumersFalseWithPipeline ()
specifier|public
name|void
name|testFailIfNoConsumersFalseWithPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct-vm"
argument_list|,
name|DirectVmComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct-vm:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct-vm:foo?failIfNoConsumers=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct-vm:bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct-vm:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
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
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:in"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigOnAConsumer ()
specifier|public
name|void
name|testConfigOnAConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct-vm"
argument_list|,
name|DirectVmComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct-vm:foo?failIfNoConsumers=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test"
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
block|}
block|}
end_class

end_unit

