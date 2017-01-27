begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CamelReactiveStreamsServiceImpl
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
name|JndiRegistry
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

begin_class
DECL|class|CamelReactiveStreamsTest
specifier|public
class|class
name|CamelReactiveStreamsTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testDefaultService ()
specifier|public
name|void
name|testDefaultService
parameter_list|()
block|{
name|CamelReactiveStreamsService
name|service1
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"default-service"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|CamelReactiveStreamsServiceImpl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSameDefaultServiceReturned ()
specifier|public
name|void
name|testSameDefaultServiceReturned
parameter_list|()
block|{
name|CamelReactiveStreamsService
name|service1
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"default-service"
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
argument_list|,
literal|"default-service"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|CamelReactiveStreamsServiceImpl
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|service2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSameServiceReturnedFromRegistry ()
specifier|public
name|void
name|testSameServiceReturnedFromRegistry
parameter_list|()
block|{
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
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|service2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|ReactiveStreamsTestService
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"from-registry"
argument_list|,
operator|(
operator|(
name|ReactiveStreamsTestService
operator|)
name|service1
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSameNamedServiceReturnedFromRegistry ()
specifier|public
name|void
name|testSameNamedServiceReturnedFromRegistry
parameter_list|()
block|{
name|CamelReactiveStreamsService
name|service1
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"dummy"
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
argument_list|,
literal|"dummy"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|service2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|ReactiveStreamsTestService
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"from-registry"
argument_list|,
operator|(
operator|(
name|ReactiveStreamsTestService
operator|)
name|service1
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testOnlyOneService ()
specifier|public
name|void
name|testOnlyOneService
parameter_list|()
block|{
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"dummy"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testOnlyOneNamedService ()
specifier|public
name|void
name|testOnlyOneNamedService
parameter_list|()
block|{
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"dummy"
argument_list|)
expr_stmt|;
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"dummy2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNamedServiceResolvedUsingFactory ()
specifier|public
name|void
name|testNamedServiceResolvedUsingFactory
parameter_list|()
block|{
name|CamelReactiveStreamsService
name|service1
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"test-service"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|service1
operator|instanceof
name|ReactiveStreamsTestService
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
operator|(
operator|(
name|ReactiveStreamsTestService
operator|)
name|service1
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
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
return|return
name|registry
return|;
block|}
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
block|}
end_class

end_unit

