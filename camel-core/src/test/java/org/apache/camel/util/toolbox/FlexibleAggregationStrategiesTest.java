begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.toolbox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|toolbox
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|CountDownLatch
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|LoggingLevel
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
name|NotifyBuilder
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
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
name|toolbox
operator|.
name|FlexibleAggregationStrategy
operator|.
name|CompletionAwareMixin
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
name|toolbox
operator|.
name|FlexibleAggregationStrategy
operator|.
name|TimeoutAwareMixin
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
comment|/**  * Unit tests for the {@link FlexibleAggregationStrategy}.  * @since 2.12  */
end_comment

begin_class
DECL|class|FlexibleAggregationStrategiesTest
specifier|public
class|class
name|FlexibleAggregationStrategiesTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|completionLatch
specifier|private
specifier|final
name|CountDownLatch
name|completionLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|timeoutLatch
specifier|private
specifier|final
name|CountDownLatch
name|timeoutLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testFlexibleAggregationStrategyNoCondition ()
specifier|public
name|void
name|testFlexibleAggregationStrategyNoCondition
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"AGGREGATE1"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"AGGREGATE2"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"AGGREGATE3"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"AGGREGATE4"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"AGGREGATE5"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|resultList
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"AGGREGATE"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|,
name|resultList
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testFlexibleAggregationStrategyCondition ()
specifier|public
name|void
name|testFlexibleAggregationStrategyCondition
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"AGGREGATE1"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"DISCARD"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"AGGREGATE2"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"DISCARD"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"AGGREGATE3"
argument_list|,
literal|"id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|resultList
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"AGGREGATE"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|,
name|resultList
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testFlexibleAggregationStrategyStoreInPropertyHashSet ()
specifier|public
name|void
name|testFlexibleAggregationStrategyStoreInPropertyHashSet
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result2"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
literal|"AggregationResult"
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|HashSet
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"ignored body"
argument_list|,
literal|"input"
argument_list|,
literal|"AGGREGATE1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"ignored body"
argument_list|,
literal|"input"
argument_list|,
literal|"DISCARD"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"ignored body"
argument_list|,
literal|"input"
argument_list|,
literal|"AGGREGATE2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"ignored body"
argument_list|,
literal|"input"
argument_list|,
literal|"DISCARD"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"ignored body"
argument_list|,
literal|"input"
argument_list|,
literal|"AGGREGATE3"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|resultSet
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result2"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
literal|"AggregationResult"
argument_list|,
name|HashSet
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|resultSet
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultSet
operator|.
name|contains
argument_list|(
literal|"AGGREGATE1"
argument_list|)
operator|&&
name|resultSet
operator|.
name|contains
argument_list|(
literal|"AGGREGATE2"
argument_list|)
operator|&&
name|resultSet
operator|.
name|contains
argument_list|(
literal|"AGGREGATE3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFlexibleAggregationStrategyStoreInHeaderSingleValue ()
specifier|public
name|void
name|testFlexibleAggregationStrategyStoreInHeaderSingleValue
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result3"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result3"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"AggregationResult"
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result3"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"AggregationResult"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"AGGREGATE3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start3"
argument_list|,
literal|"AGGREGATE1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start3"
argument_list|,
literal|"AGGREGATE2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start3"
argument_list|,
literal|"AGGREGATE3"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|testFlexibleAggregationStrategyGenericArrayListWithoutNulls ()
specifier|public
name|void
name|testFlexibleAggregationStrategyGenericArrayListWithoutNulls
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result4"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result4"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start4"
argument_list|,
literal|"AGGREGATE1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start4"
argument_list|,
literal|123d
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start4"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|ArrayList
name|list
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result4"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|contains
argument_list|(
literal|"AGGREGATE1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|contains
argument_list|(
literal|123d
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFlexibleAggregationStrategyFailWithInvalidCast ()
specifier|public
name|void
name|testFlexibleAggregationStrategyFailWithInvalidCast
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result5"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start5"
argument_list|,
literal|"AGGREGATE1"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"Type Conversion exception expected, as we are not ignoring invalid casts"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|testFlexibleAggregationStrategyFailOnInvalidCast ()
specifier|public
name|void
name|testFlexibleAggregationStrategyFailOnInvalidCast
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result6"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result6"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start6"
argument_list|,
literal|"AGGREGATE1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start6"
argument_list|,
literal|"AGGREGATE2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start6"
argument_list|,
literal|"AGGREGATE3"
argument_list|)
expr_stmt|;
name|ArrayList
name|list
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result6"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|list
control|)
block|{
name|assertNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFlexibleAggregationStrategyTimeoutCompletionMixins ()
specifier|public
name|void
name|testFlexibleAggregationStrategyTimeoutCompletionMixins
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result.timeoutAndCompletionAware"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.timeoutAndCompletionAware"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"AGGREGATE1"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.timeoutAndCompletionAware"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
literal|"Timeout"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.timeoutAndCompletionAware"
argument_list|)
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"AGGREGATE3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start.timeoutAndCompletionAware"
argument_list|,
literal|"AGGREGATE1"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|timeoutLatch
operator|.
name|await
argument_list|(
literal|2500
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start.timeoutAndCompletionAware"
argument_list|,
literal|"AGGREGATE2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start.timeoutAndCompletionAware"
argument_list|,
literal|"AGGREGATE3"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|completionLatch
operator|.
name|await
argument_list|(
literal|2500
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.timeoutAndCompletionAware"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testFlexibleAggregationStrategyPickXPath ()
specifier|public
name|void
name|testFlexibleAggregationStrategyPickXPath
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result.xpath1"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.xpath1"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start.xpath1"
argument_list|,
literal|"<envelope><result>ok</result></envelope>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start.xpath1"
argument_list|,
literal|"<envelope><result>error</result></envelope>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start.xpath1"
argument_list|,
literal|"<envelope>no result</envelope>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|ArrayList
argument_list|<
name|Node
argument_list|>
name|list
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result.xpath1"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"error"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLinkedList ()
specifier|public
name|void
name|testLinkedList
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|and
argument_list|()
operator|.
name|whenExactlyFailed
argument_list|(
literal|0
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:linkedlist"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"FIRST"
argument_list|,
literal|"SECOND"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|notify
operator|.
name|matches
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHashSet ()
specifier|public
name|void
name|testHashSet
parameter_list|()
throws|throws
name|Exception
block|{
name|HashSet
argument_list|<
name|String
argument_list|>
name|r
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|r
operator|.
name|add
argument_list|(
literal|"FIRST"
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
literal|"SECOND"
argument_list|)
expr_stmt|;
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|and
argument_list|()
operator|.
name|whenExactlyFailed
argument_list|(
literal|0
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|Set
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:hashset"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"FIRST"
argument_list|,
literal|"SECOND"
argument_list|)
argument_list|,
name|Set
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|notify
operator|.
name|matches
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r
argument_list|,
name|result
argument_list|)
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
literal|"direct:start1"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|AggregationStrategies
operator|.
name|flexible
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|accumulateInCollection
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
operator|.
name|pick
argument_list|(
name|simple
argument_list|(
literal|"${body}"
argument_list|)
argument_list|)
operator|.
name|condition
argument_list|(
name|simple
argument_list|(
literal|"${body} contains 'AGGREGATE'"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|header
argument_list|(
literal|"id"
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|5
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|AggregationStrategies
operator|.
name|flexible
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|accumulateInCollection
argument_list|(
name|HashSet
operator|.
name|class
argument_list|)
operator|.
name|pick
argument_list|(
name|simple
argument_list|(
literal|"${header.input}"
argument_list|)
argument_list|)
operator|.
name|condition
argument_list|(
name|simple
argument_list|(
literal|"${header.input} contains 'AGGREGATE'"
argument_list|)
argument_list|)
operator|.
name|storeInProperty
argument_list|(
literal|"AggregationResult"
argument_list|)
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|5
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start3"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|AggregationStrategies
operator|.
name|flexible
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|storeInHeader
argument_list|(
literal|"AggregationResult"
argument_list|)
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start4"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|AggregationStrategies
operator|.
name|flexible
argument_list|()
operator|.
name|accumulateInCollection
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result4"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start5"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|AggregationStrategies
operator|.
name|flexible
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|accumulateInCollection
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result5"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start6"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|AggregationStrategies
operator|.
name|flexible
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|ignoreInvalidCasts
argument_list|()
operator|.
name|storeNulls
argument_list|()
operator|.
name|accumulateInCollection
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result6"
argument_list|)
expr_stmt|;
name|AggregationStrategy
name|timeoutCompletionStrategy
init|=
name|AggregationStrategies
operator|.
name|flexible
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|condition
argument_list|(
name|simple
argument_list|(
literal|"${body} contains 'AGGREGATE'"
argument_list|)
argument_list|)
operator|.
name|timeoutAware
argument_list|(
operator|new
name|TimeoutAwareMixin
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|timeout
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|total
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"Timeout"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|timeoutLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|completionAware
argument_list|(
operator|new
name|CompletionAwareMixin
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onCompletion
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|completionLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:start.timeoutAndCompletionAware"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|timeoutCompletionStrategy
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|500
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result.timeoutAndCompletionAware"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start.xpath1"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|AggregationStrategies
operator|.
name|flexible
argument_list|(
name|Node
operator|.
name|class
argument_list|)
operator|.
name|pick
argument_list|(
name|xpath
argument_list|(
literal|"//result[1]"
argument_list|)
operator|.
name|nodeResult
argument_list|()
argument_list|)
operator|.
name|accumulateInCollection
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result.xpath1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:linkedlist"
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"Before the first split the body is ${body} and has class ${body.getClass()}"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|,
name|AggregationStrategies
operator|.
name|flexible
argument_list|()
operator|.
name|pick
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|accumulateInCollection
argument_list|(
name|LinkedList
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"During the first split the body is ${body} and has class ${body.getClass()}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"Before the second split the body is ${body} and has class ${body.getClass()}"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|,
name|AggregationStrategies
operator|.
name|flexible
argument_list|()
operator|.
name|pick
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|accumulateInCollection
argument_list|(
name|LinkedList
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"During the second split the body is ${body} and has class ${body.getClass()}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"After the second split the body is ${body} and has class ${body.getClass()}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hashset"
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"Before the first split the body is ${body} and has class ${body.getClass()}"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|,
name|AggregationStrategies
operator|.
name|flexible
argument_list|()
operator|.
name|pick
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|accumulateInCollection
argument_list|(
name|HashSet
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"During the first split the body is ${body} and has class ${body.getClass()}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"Before the second split the body is ${body} and has class ${body.getClass()}"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|,
name|AggregationStrategies
operator|.
name|flexible
argument_list|()
operator|.
name|pick
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|accumulateInCollection
argument_list|(
name|HashSet
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"During the second split the body is ${body} and has class ${body.getClass()}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
literal|"After the second split the body is ${body} and has class ${body.getClass()}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

