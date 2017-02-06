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
name|io
operator|.
name|reactivex
operator|.
name|Flowable
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

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_class
DECL|class|EventTypeTest
specifier|public
class|class
name|EventTypeTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testOnCompleteHeaderForwarded ()
specifier|public
name|void
name|testOnCompleteHeaderForwarded
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"reactive-streams:numbers?forwardOnComplete=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|numbers
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|streamSubscriber
argument_list|(
literal|"numbers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Flowable
operator|.
expr|<
name|Integer
operator|>
name|empty
argument_list|()
operator|.
name|subscribe
argument_list|(
name|numbers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedHeaderReceived
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_EVENT_TYPE
argument_list|,
literal|"onComplete"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|null
block|}
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnCompleteHeaderNotForwarded ()
specifier|public
name|void
name|testOnCompleteHeaderNotForwarded
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"reactive-streams:numbers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|numbers
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|streamSubscriber
argument_list|(
literal|"numbers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Flowable
operator|.
expr|<
name|Integer
operator|>
name|empty
argument_list|()
operator|.
name|subscribe
argument_list|(
name|numbers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnNextHeaderForwarded ()
specifier|public
name|void
name|testOnNextHeaderForwarded
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"reactive-streams:numbers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|numbers
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|streamSubscriber
argument_list|(
literal|"numbers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|)
operator|.
name|subscribe
argument_list|(
name|numbers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedHeaderReceived
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_EVENT_TYPE
argument_list|,
literal|"onNext"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|ex
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnErrorHeaderForwarded ()
specifier|public
name|void
name|testOnErrorHeaderForwarded
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"reactive-streams:numbers?forwardOnError=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|numbers
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|streamSubscriber
argument_list|(
literal|"numbers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|RuntimeException
name|ex
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|)
operator|.
name|map
argument_list|(
name|n
lambda|->
block|{
if|if
condition|(
name|n
operator|==
literal|1
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
return|return
name|n
return|;
block|}
argument_list|)
operator|.
name|subscribe
argument_list|(
name|numbers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedHeaderReceived
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_EVENT_TYPE
argument_list|,
literal|"onError"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exch
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ex
argument_list|,
name|exch
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnErrorHeaderNotForwarded ()
specifier|public
name|void
name|testOnErrorHeaderNotForwarded
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"reactive-streams:numbers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|numbers
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|streamSubscriber
argument_list|(
literal|"numbers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|RuntimeException
name|ex
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|)
operator|.
name|map
argument_list|(
name|n
lambda|->
block|{
if|if
condition|(
name|n
operator|==
literal|1
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
return|return
name|n
return|;
block|}
argument_list|)
operator|.
name|subscribe
argument_list|(
name|numbers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|200
argument_list|)
expr_stmt|;
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

