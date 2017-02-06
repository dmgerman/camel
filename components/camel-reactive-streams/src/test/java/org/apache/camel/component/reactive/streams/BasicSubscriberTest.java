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
name|Publisher
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
DECL|class|BasicSubscriberTest
specifier|public
class|class
name|BasicSubscriberTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testWorking ()
specifier|public
name|void
name|testWorking
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|count
init|=
literal|2
decl_stmt|;
name|MockEndpoint
name|e1
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:sub1"
argument_list|)
decl_stmt|;
name|e1
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|e1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|MockEndpoint
name|e2
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:sub2"
argument_list|)
decl_stmt|;
name|e2
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|e2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|MockEndpoint
name|e3
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:sub3"
argument_list|)
decl_stmt|;
name|e3
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|e3
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|Exchange
name|ex1
init|=
name|e1
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Exchange
name|ex2
init|=
name|e2
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Exchange
name|ex3
init|=
name|e3
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ex1
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|ex2
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ex1
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|ex3
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doPostSetup ()
specifier|protected
name|void
name|doPostSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|sub
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|()
argument_list|)
operator|.
name|streamSubscriber
argument_list|(
literal|"sub"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|sub2
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|()
argument_list|)
operator|.
name|streamSubscriber
argument_list|(
literal|"sub2"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Publisher
argument_list|<
name|Integer
argument_list|>
name|pub
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|()
argument_list|)
operator|.
name|fromStream
argument_list|(
literal|"pub"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|pub
operator|.
name|subscribe
argument_list|(
name|sub
argument_list|)
expr_stmt|;
name|pub
operator|.
name|subscribe
argument_list|(
name|sub2
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
literal|"reactive-streams:sub"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:sub1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:sub2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:sub2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:tick?period=50"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"random(500)"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"mock:sub3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:pub"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

