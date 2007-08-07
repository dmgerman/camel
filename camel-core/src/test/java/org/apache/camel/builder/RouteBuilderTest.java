begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Route
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
name|TestSupport
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
name|EventDrivenConsumerRoute
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
name|processor
operator|.
name|ChoiceProcessor
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
name|processor
operator|.
name|DeadLetterChannel
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
name|processor
operator|.
name|DelegateProcessor
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
name|processor
operator|.
name|FilterProcessor
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
name|processor
operator|.
name|MulticastProcessor
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
name|processor
operator|.
name|RecipientList
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
name|processor
operator|.
name|SendProcessor
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
name|processor
operator|.
name|Splitter
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
name|processor
operator|.
name|idempotent
operator|.
name|IdempotentConsumer
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
name|processor
operator|.
name|idempotent
operator|.
name|MemoryMessageIdRepository
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|MemoryMessageIdRepository
operator|.
name|memoryMessageIdRepository
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteBuilderTest
specifier|public
class|class
name|RouteBuilderTest
extends|extends
name|TestSupport
block|{
DECL|field|myProcessor
specifier|protected
name|Processor
name|myProcessor
init|=
operator|new
name|MyProcessor
argument_list|()
decl_stmt|;
DECL|field|interceptor1
specifier|protected
name|DelegateProcessor
name|interceptor1
decl_stmt|;
DECL|field|interceptor2
specifier|protected
name|DelegateProcessor
name|interceptor2
decl_stmt|;
DECL|method|buildSimpleRoute ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildSimpleRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e1
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testSimpleRoute ()
specifier|public
name|void
name|testSimpleRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildSimpleRoute
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
argument_list|<
name|Exchange
argument_list|>
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|SendProcessor
name|sendProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Endpoint URI"
argument_list|,
literal|"seda:b"
argument_list|,
name|sendProcessor
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildSimpleRouteWithHeaderPredicate ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildSimpleRouteWithHeaderPredicate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e2
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e2
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testSimpleRouteWithHeaderPredicate ()
specifier|public
name|void
name|testSimpleRouteWithHeaderPredicate
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildSimpleRouteWithHeaderPredicate
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|FilterProcessor
name|filterProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|FilterProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|SendProcessor
name|sendProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|unwrapErrorHandler
argument_list|(
name|filterProcessor
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Endpoint URI"
argument_list|,
literal|"seda:b"
argument_list|,
name|sendProcessor
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildSimpleRouteWithChoice ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildSimpleRouteWithChoice
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e3
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:c"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"seda:d"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e3
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testSimpleRouteWithChoice ()
specifier|public
name|void
name|testSimpleRouteWithChoice
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildSimpleRouteWithChoice
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|ChoiceProcessor
name|choiceProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|ChoiceProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|filters
init|=
name|choiceProcessor
operator|.
name|getFilters
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be two when clauses"
argument_list|,
literal|2
argument_list|,
name|filters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FilterProcessor
name|filter1
init|=
name|filters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSendTo
argument_list|(
name|filter1
operator|.
name|getProcessor
argument_list|()
argument_list|,
literal|"seda:b"
argument_list|)
expr_stmt|;
name|FilterProcessor
name|filter2
init|=
name|filters
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertSendTo
argument_list|(
name|filter2
operator|.
name|getProcessor
argument_list|()
argument_list|,
literal|"seda:c"
argument_list|)
expr_stmt|;
name|assertSendTo
argument_list|(
name|choiceProcessor
operator|.
name|getOtherwise
argument_list|()
argument_list|,
literal|"seda:d"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildCustomProcessor ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildCustomProcessor
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e4
name|myProcessor
operator|=
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
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Called with exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|process
argument_list|(
name|myProcessor
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e4
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testCustomProcessor ()
specifier|public
name|void
name|testCustomProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildCustomProcessor
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be called with my processor"
argument_list|,
name|myProcessor
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildCustomProcessorWithFilter ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildCustomProcessorWithFilter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e5
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|myProcessor
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e5
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testCustomProcessorWithFilter ()
specifier|public
name|void
name|testCustomProcessorWithFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildCustomProcessorWithFilter
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|FilterProcessor
name|filterProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|FilterProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be called with my processor"
argument_list|,
name|myProcessor
argument_list|,
name|unwrapErrorHandler
argument_list|(
name|filterProcessor
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildWireTap ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildWireTap
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e6
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:tap"
argument_list|,
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e6
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testWireTap ()
specifier|public
name|void
name|testWireTap
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildWireTap
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|MulticastProcessor
name|multicastProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|MulticastProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Processor
argument_list|>
name|endpoints
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
name|multicastProcessor
operator|.
name|getProcessors
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should have 2 endpoints"
argument_list|,
literal|2
argument_list|,
name|endpoints
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSendToProcessor
argument_list|(
name|endpoints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"seda:tap"
argument_list|)
expr_stmt|;
name|assertSendToProcessor
argument_list|(
name|endpoints
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildRouteWithInterceptor ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildRouteWithInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|interceptor1
operator|=
operator|new
name|DelegateProcessor
argument_list|()
block|{         }
expr_stmt|;
comment|// START SNIPPET: e7
name|interceptor2
operator|=
operator|new
name|MyInterceptorProcessor
argument_list|()
expr_stmt|;
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|intercept
argument_list|(
name|interceptor1
argument_list|)
operator|.
name|intercept
argument_list|(
name|interceptor2
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:d"
argument_list|)
expr_stmt|;
comment|/*                  *                   * TODO keep old DSL? .intercept() .add(interceptor1)                  * .add(interceptor2) .target().to("seda:d");                  */
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e7
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testRouteWithInterceptor ()
specifier|public
name|void
name|testRouteWithInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildRouteWithInterceptor
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|DelegateProcessor
name|p1
init|=
name|assertIsInstanceOf
argument_list|(
name|DelegateProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|processor
operator|=
name|p1
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
name|DelegateProcessor
name|p2
init|=
name|assertIsInstanceOf
argument_list|(
name|DelegateProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|assertSendTo
argument_list|(
name|p2
operator|.
name|getProcessor
argument_list|()
argument_list|,
literal|"seda:d"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testComplexExpressions ()
specifier|public
name|void
name|testComplexExpressions
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e7
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
literal|45
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e7
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|builder
operator|.
name|getRouteList
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|2
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"processor: "
operator|+
name|processor
argument_list|)
expr_stmt|;
comment|/*              * TODO FilterProcessor filterProcessor =              * assertIsInstanceOf(FilterProcessor.class, processor);              *               * SendProcessor sendProcessor =              * assertIsInstanceOf(SendProcessor.class,              * filterProcessor.getProcessor()); assertEquals("Endpoint URI",              * "seda:b", sendProcessor.getDestination().getEndpointUri());              */
block|}
block|}
DECL|method|buildStaticRecipientList ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildStaticRecipientList
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e8
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|,
literal|"seda:c"
argument_list|,
literal|"seda:d"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e8
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|buildDynamicRecipientList ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildDynamicRecipientList
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e9
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e9
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testRouteDynamicReceipentList ()
specifier|public
name|void
name|testRouteDynamicReceipentList
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildDynamicRecipientList
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|RecipientList
name|p1
init|=
name|assertIsInstanceOf
argument_list|(
name|RecipientList
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
block|}
block|}
DECL|method|buildSplitter ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildSplitter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: splitter
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|splitter
argument_list|(
name|bodyAs
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|tokenize
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: splitter
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testSplitter ()
specifier|public
name|void
name|testSplitter
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildSplitter
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|Splitter
name|p1
init|=
name|assertIsInstanceOf
argument_list|(
name|Splitter
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
block|}
block|}
DECL|method|buildIdempotentConsumer ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildIdempotentConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: idempotent
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|header
argument_list|(
literal|"myMessageId"
argument_list|)
argument_list|,
name|memoryMessageIdRepository
argument_list|(
literal|200
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: idempotent
return|return
name|builder
operator|.
name|getRouteList
argument_list|()
return|;
block|}
DECL|method|testIdempotentConsumer ()
specifier|public
name|void
name|testIdempotentConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|buildIdempotentConsumer
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|getProcessorWithoutErrorHandler
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|IdempotentConsumer
name|idempotentConsumer
init|=
name|assertIsInstanceOf
argument_list|(
name|IdempotentConsumer
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"messageIdExpression"
argument_list|,
literal|"header(myMessageId)"
argument_list|,
name|idempotentConsumer
operator|.
name|getMessageIdExpression
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|MemoryMessageIdRepository
operator|.
name|class
argument_list|,
name|idempotentConsumer
operator|.
name|getMessageIdRepository
argument_list|()
argument_list|)
expr_stmt|;
name|SendProcessor
name|sendProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|unwrapErrorHandler
argument_list|(
name|idempotentConsumer
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Endpoint URI"
argument_list|,
literal|"seda:b"
argument_list|,
name|sendProcessor
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertSendTo (Processor processor, String uri)
specifier|protected
name|void
name|assertSendTo
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|processor
operator|instanceof
name|SendProcessor
operator|)
condition|)
block|{
name|processor
operator|=
name|unwrapErrorHandler
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
name|SendProcessor
name|sendProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Endpoint URI"
argument_list|,
name|uri
argument_list|,
name|sendProcessor
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertSendToProcessor (Processor processor, String uri)
specifier|protected
name|void
name|assertSendToProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|processor
operator|instanceof
name|Producer
operator|)
condition|)
block|{
name|processor
operator|=
name|unwrapErrorHandler
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|processor
operator|instanceof
name|SendProcessor
condition|)
block|{
name|assertSendTo
argument_list|(
name|processor
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Producer
name|producer
init|=
name|assertIsInstanceOf
argument_list|(
name|Producer
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Endpoint URI"
argument_list|,
name|uri
argument_list|,
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * By default routes should be wrapped in the {@link DeadLetterChannel} so      * lets unwrap that and return the actual processor      */
DECL|method|getProcessorWithoutErrorHandler (Route route)
specifier|protected
name|Processor
name|getProcessorWithoutErrorHandler
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
return|return
name|unwrapErrorHandler
argument_list|(
name|processor
argument_list|)
return|;
block|}
DECL|method|unwrapErrorHandler (Processor processor)
specifier|protected
name|Processor
name|unwrapErrorHandler
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|DeadLetterChannel
condition|)
block|{
name|DeadLetterChannel
name|deadLetter
init|=
operator|(
name|DeadLetterChannel
operator|)
name|processor
decl_stmt|;
return|return
name|deadLetter
operator|.
name|getOutput
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|processor
return|;
block|}
block|}
block|}
end_class

end_unit

