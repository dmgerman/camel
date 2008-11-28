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
name|ArrayBlockingQueue
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
name|ThreadPoolExecutor
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
name|Header
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
name|Message
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
import|;
end_import

begin_class
DECL|class|MultiCastAggregatorTest
specifier|public
class|class
name|MultiCastAggregatorTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testMulticastReceivesItsOwnExchangeParallelly ()
specifier|public
name|void
name|testMulticastReceivesItsOwnExchangeParallelly
parameter_list|()
throws|throws
name|Exception
block|{
name|sendingAMessageUsingMulticastReceivesItsOwnExchange
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testMulticastReceivesItsOwnExchangeSequentially ()
specifier|public
name|void
name|testMulticastReceivesItsOwnExchangeSequentially
parameter_list|()
throws|throws
name|Exception
block|{
name|sendingAMessageUsingMulticastReceivesItsOwnExchange
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|sendingAMessageUsingMulticastReceivesItsOwnExchange (boolean isParallel)
specifier|private
name|void
name|sendingAMessageUsingMulticastReceivesItsOwnExchange
parameter_list|(
name|boolean
name|isParallel
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"inputx+inputy+inputz"
argument_list|)
expr_stmt|;
name|String
name|url
decl_stmt|;
if|if
condition|(
name|isParallel
condition|)
block|{
name|url
operator|=
literal|"direct:parallel"
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
literal|"direct:sequential"
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|url
argument_list|,
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"input"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get result here"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't get the right result"
argument_list|,
literal|"inputx+inputy+inputz"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|ThreadPoolExecutor
name|tpExecutor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|,
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
comment|// START SNIPPET: example
comment|// The message will be sent parallelly to the endpoints
name|from
argument_list|(
literal|"direct:parallel"
argument_list|)
operator|.
name|multicast
argument_list|(
operator|new
name|BodyOutAggregatingStrategy
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|setThreadPoolExecutor
argument_list|(
name|tpExecutor
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:x"
argument_list|,
literal|"direct:y"
argument_list|,
literal|"direct:z"
argument_list|)
expr_stmt|;
comment|// Multicast the message in a sequential way
name|from
argument_list|(
literal|"direct:sequential"
argument_list|)
operator|.
name|multicast
argument_list|(
operator|new
name|BodyOutAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:x"
argument_list|,
literal|"direct:y"
argument_list|,
literal|"direct:z"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:x"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|AppendingProcessor
argument_list|(
literal|"x"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:aggregator"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:y"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|AppendingProcessor
argument_list|(
literal|"y"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:aggregator"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:z"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|AppendingProcessor
argument_list|(
literal|"z"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:aggregator"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:aggregator"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"cheese"
argument_list|)
argument_list|,
operator|new
name|BodyInAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|completedPredicate
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COUNT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

