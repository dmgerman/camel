begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Endpoint
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
name|Producer
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|filterchain
operator|.
name|IoFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|filterchain
operator|.
name|IoFilterAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
comment|/**  * For unit testing the<tt>filters</tt> option.  */
end_comment

begin_class
DECL|class|Mina2FiltersTest
specifier|public
class|class
name|Mina2FiltersTest
extends|extends
name|BaseMina2Test
block|{
annotation|@
name|Test
DECL|method|testFilterListRef ()
specifier|public
name|void
name|testFilterListRef
parameter_list|()
throws|throws
name|Exception
block|{
name|testFilter
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"mina2:tcp://localhost:%1$s?textline=true&minaLogger=true&sync=false&filters=#myFilters"
argument_list|,
name|getPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFilterElementRef ()
specifier|public
name|void
name|testFilterElementRef
parameter_list|()
throws|throws
name|Exception
block|{
name|testFilter
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"mina2:tcp://localhost:%1$s?textline=true&minaLogger=true&sync=false&filters=#myFilter"
argument_list|,
name|getPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|TestFilter
operator|.
name|called
operator|=
literal|0
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|testFilter (final String uri)
specifier|private
name|void
name|testFilter
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|)
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|uri
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
name|MockEndpoint
name|mock
init|=
name|this
operator|.
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// set input and execute it
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The filter should have been called twice (producer and consumer)"
argument_list|,
literal|2
argument_list|,
name|TestFilter
operator|.
name|called
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bindToRegistry (Registry registry)
specifier|protected
name|void
name|bindToRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|IoFilter
name|myFilter
init|=
operator|new
name|TestFilter
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IoFilter
argument_list|>
name|myFilters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|myFilters
operator|.
name|add
argument_list|(
name|myFilter
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myFilters"
argument_list|,
name|myFilters
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myFilter"
argument_list|,
name|myFilter
argument_list|)
expr_stmt|;
block|}
DECL|class|TestFilter
specifier|public
specifier|static
specifier|final
class|class
name|TestFilter
extends|extends
name|IoFilterAdapter
block|{
DECL|field|called
specifier|public
specifier|static
specifier|volatile
name|int
name|called
decl_stmt|;
annotation|@
name|Override
DECL|method|sessionCreated (NextFilter nextFilter, IoSession session)
specifier|public
name|void
name|sessionCreated
parameter_list|(
name|NextFilter
name|nextFilter
parameter_list|,
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|incCalled
argument_list|()
expr_stmt|;
name|nextFilter
operator|.
name|sessionCreated
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
DECL|method|incCalled ()
specifier|public
specifier|static
specifier|synchronized
name|void
name|incCalled
parameter_list|()
block|{
name|called
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

