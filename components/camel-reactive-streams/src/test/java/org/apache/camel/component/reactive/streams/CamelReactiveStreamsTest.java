begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|reactive
operator|.
name|streams
operator|.
name|engine
operator|.
name|DefaultCamelReactiveStreamsService
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
name|reactive
operator|.
name|streams
operator|.
name|support
operator|.
name|ReactiveStreamsTestService
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
name|DefaultCamelContext
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
name|SimpleRegistry
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

begin_class
DECL|class|CamelReactiveStreamsTest
specifier|public
class|class
name|CamelReactiveStreamsTest
block|{
annotation|@
name|Test
DECL|method|testDefaultService ()
specifier|public
name|void
name|testDefaultService
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
try|try
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelReactiveStreamsService
name|service1
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|DefaultCamelReactiveStreamsService
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSameDefaultServiceReturned ()
specifier|public
name|void
name|testSameDefaultServiceReturned
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
try|try
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelReactiveStreamsService
name|service1
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CamelReactiveStreamsService
name|service2
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|DefaultCamelReactiveStreamsService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|service2
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSameServiceReturnedFromRegistry ()
specifier|public
name|void
name|testSameServiceReturnedFromRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|ReactiveStreamsComponent
name|component
init|=
operator|new
name|ReactiveStreamsComponent
argument_list|()
decl_stmt|;
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"dummy"
argument_list|,
operator|new
name|ReactiveStreamsTestService
argument_list|(
literal|"from-registry"
argument_list|)
argument_list|)
expr_stmt|;
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|SCHEME
argument_list|,
name|component
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelReactiveStreamsService
name|service1
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CamelReactiveStreamsService
name|service2
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|service2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|ReactiveStreamsTestService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"from-registry"
argument_list|,
name|service1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testNamedServiceResolvedUsingFactory ()
specifier|public
name|void
name|testNamedServiceResolvedUsingFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|ReactiveStreamsComponent
name|component
init|=
operator|new
name|ReactiveStreamsComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setServiceType
argument_list|(
literal|"test-service"
argument_list|)
expr_stmt|;
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|SCHEME
argument_list|,
name|component
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelReactiveStreamsService
name|service1
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CamelReactiveStreamsService
name|service2
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|service2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|ReactiveStreamsTestService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"test-service"
argument_list|,
name|service1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

