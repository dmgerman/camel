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
name|RoutesBuilder
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
name|support
operator|.
name|DefaultExchange
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
name|Publisher
import|;
end_import

begin_class
DECL|class|ExchangeRequestTest
specifier|public
class|class
name|ExchangeRequestTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testStreamRequest ()
specifier|public
name|void
name|testStreamRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelReactiveStreamsService
name|camel
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|string
init|=
name|camel
operator|.
name|toStream
argument_list|(
literal|"data"
argument_list|,
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|res
init|=
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|string
argument_list|)
operator|.
name|blockingFirst
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|String
name|content
init|=
name|res
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
name|assertNotNull
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInteraction ()
specifier|public
name|void
name|testInteraction
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelReactiveStreamsService
name|camel
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Integer
name|res
init|=
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|camel
operator|.
name|toStream
argument_list|(
literal|"plusOne"
argument_list|,
literal|1L
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|blockingFirst
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|res
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipleInteractions ()
specifier|public
name|void
name|testMultipleInteractions
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelReactiveStreamsService
name|camel
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Integer
name|sum
init|=
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|e
lambda|->
name|camel
operator|.
name|toStream
argument_list|(
literal|"plusOne"
argument_list|,
name|e
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|reduce
argument_list|(
parameter_list|(
name|i
parameter_list|,
name|j
parameter_list|)
lambda|->
name|i
operator|+
name|j
argument_list|)
operator|.
name|blockingGet
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|sum
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|sum
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"reactive-streams:data"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:plusOne"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|body
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|b
lambda|->
name|b
operator|+
literal|1
argument_list|)
operator|.
name|log
argument_list|(
literal|"Hello ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

