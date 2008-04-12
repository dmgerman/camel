begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|model
operator|.
name|language
operator|.
name|ExpressionType
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
name|model
operator|.
name|loadbalancer
operator|.
name|RoundRobinLoadBalanceStrategy
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
name|model
operator|.
name|loadbalancer
operator|.
name|StickyLoadBalanceStrategy
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|XmlParseTest
specifier|public
class|class
name|XmlParseTest
extends|extends
name|XmlTestSupport
block|{
DECL|method|testParseSimpleRouteXml ()
specifier|public
name|void
name|testParseSimpleRouteXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"simpleRoute.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
literal|"to"
argument_list|,
name|route
argument_list|,
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseProcessorXml ()
specifier|public
name|void
name|testParseProcessorXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"processor.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|assertProcessor
argument_list|(
name|route
argument_list|,
literal|"myProcessor"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseProcessorWithFilterXml ()
specifier|public
name|void
name|testParseProcessorWithFilterXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"processorWithFilter.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|FilterType
name|filter
init|=
name|assertFilter
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|filter
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|"juel"
argument_list|,
literal|"in.header.foo == 'bar'"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseProcessorWithHeaderFilterXml ()
specifier|public
name|void
name|testParseProcessorWithHeaderFilterXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"processorWithHeaderFilter.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|FilterType
name|filter
init|=
name|assertFilter
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|filter
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|"header"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseProcessorWithElFilterXml ()
specifier|public
name|void
name|testParseProcessorWithElFilterXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"processorWithElFilter.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|FilterType
name|filter
init|=
name|assertFilter
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|filter
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|"el"
argument_list|,
literal|"$in.header.foo == 'bar'"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseProcessorWithGroovyFilterXml ()
specifier|public
name|void
name|testParseProcessorWithGroovyFilterXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"processorWithGroovyFilter.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|FilterType
name|filter
init|=
name|assertFilter
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|filter
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|"groovy"
argument_list|,
literal|"in.headers.any { h -> h.startsWith('foo')}"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseRecipientListXml ()
specifier|public
name|void
name|testParseRecipientListXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"dynamicRecipientList.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|RecipientListType
name|node
init|=
name|assertRecipientList
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|node
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|"header"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseStaticRecipientListXml ()
specifier|public
name|void
name|testParseStaticRecipientListXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"staticRecipientList.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
name|route
argument_list|,
literal|"seda:b"
argument_list|,
literal|"seda:c"
argument_list|,
literal|"seda:d"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseSetHeaderXml ()
specifier|public
name|void
name|testParseSetHeaderXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"setHeader.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|SetHeaderType
name|node
init|=
name|assertSetHeader
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"oldBodyValue"
argument_list|,
name|node
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
name|node
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|"simple"
argument_list|,
literal|"body"
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
name|route
argument_list|,
literal|"mock:b"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseSetHeaderWithChildProcessorXml ()
specifier|public
name|void
name|testParseSetHeaderWithChildProcessorXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"setHeaderWithChildProcessor.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|SetHeaderType
name|node
init|=
name|assertSetHeader
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"oldBodyValue"
argument_list|,
name|node
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
name|node
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|"simple"
argument_list|,
literal|"body"
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
name|node
argument_list|,
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseSetHeaderToConstantXml ()
specifier|public
name|void
name|testParseSetHeaderToConstantXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"setHeaderToConstant.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|SetHeaderType
name|node
init|=
name|assertSetHeader
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"theHeader"
argument_list|,
name|node
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a value"
argument_list|,
name|node
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|node
operator|.
name|getExpression
argument_list|()
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
name|route
argument_list|,
literal|"mock:b"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseConvertBodyXml ()
specifier|public
name|void
name|testParseConvertBodyXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"convertBody.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|ConvertBodyType
name|node
init|=
name|assertConvertBody
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Integer"
argument_list|,
name|node
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|node
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseRoutingSlipXml ()
specifier|public
name|void
name|testParseRoutingSlipXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"routingSlip.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|RoutingSlipType
name|node
init|=
name|assertRoutingSlip
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|RoutingSlipType
operator|.
name|ROUTING_SLIP_HEADER
argument_list|,
name|node
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RoutingSlipType
operator|.
name|DEFAULT_DELIMITER
argument_list|,
name|node
operator|.
name|getUriDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseRoutingSlipWithHeaderSetXml ()
specifier|public
name|void
name|testParseRoutingSlipWithHeaderSetXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"routingSlipHeaderSet.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|RoutingSlipType
name|node
init|=
name|assertRoutingSlip
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"theRoutingSlipHeader"
argument_list|,
name|node
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RoutingSlipType
operator|.
name|DEFAULT_DELIMITER
argument_list|,
name|node
operator|.
name|getUriDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseRoutingSlipWithHeaderAndDelimiterSetXml ()
specifier|public
name|void
name|testParseRoutingSlipWithHeaderAndDelimiterSetXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"routingSlipHeaderAndDelimiterSet.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|RoutingSlipType
name|node
init|=
name|assertRoutingSlip
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"theRoutingSlipHeader"
argument_list|,
name|node
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#"
argument_list|,
name|node
operator|.
name|getUriDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//TODO get the test fixed
DECL|method|xtestParseRouteWithInterceptorXml ()
specifier|public
name|void
name|xtestParseRouteWithInterceptorXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"routeWithInterceptor.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
literal|"to"
argument_list|,
name|route
argument_list|,
literal|"seda:d"
argument_list|)
expr_stmt|;
name|assertInterceptorRefs
argument_list|(
name|route
argument_list|,
literal|"interceptor1"
argument_list|,
literal|"interceptor2"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseRouteWithChoiceXml ()
specifier|public
name|void
name|testParseRouteWithChoiceXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"routeWithChoice.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|ChoiceType
name|choice
init|=
name|assertChoice
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WhenType
argument_list|>
name|whens
init|=
name|assertListSize
argument_list|(
name|choice
operator|.
name|getWhenClauses
argument_list|()
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertChildTo
argument_list|(
literal|"when(0)"
argument_list|,
name|whens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"seda:b"
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
literal|"when(1)"
argument_list|,
name|whens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"seda:c"
argument_list|)
expr_stmt|;
name|OtherwiseType
name|otherwise
init|=
name|choice
operator|.
name|getOtherwise
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Otherwise is null"
argument_list|,
name|otherwise
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
literal|"otherwise"
argument_list|,
name|otherwise
argument_list|,
literal|"seda:d"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseSplitterXml ()
specifier|public
name|void
name|testParseSplitterXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"splitter.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|SplitterType
name|splitter
init|=
name|assertSplitter
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|splitter
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|"xpath"
argument_list|,
literal|"/foo/bar"
argument_list|)
expr_stmt|;
name|assertChildTo
argument_list|(
literal|"to"
argument_list|,
name|splitter
argument_list|,
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseLoadBalance ()
specifier|public
name|void
name|testParseLoadBalance
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"routeWithLoadBalance.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|LoadBalanceType
name|loadBalance
init|=
name|assertLoadBalancer
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Here should have 3 output here"
argument_list|,
literal|3
argument_list|,
name|loadBalance
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The loadBalancer shoud be RoundRobinLoadBalanceStrategy"
argument_list|,
name|loadBalance
operator|.
name|getLoadBalancerType
argument_list|()
operator|instanceof
name|RoundRobinLoadBalanceStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseStickyLoadBalance ()
specifier|public
name|void
name|testParseStickyLoadBalance
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"routeWithStickyLoadBalance.xml"
argument_list|)
decl_stmt|;
name|assertFrom
argument_list|(
name|route
argument_list|,
literal|"seda:a"
argument_list|)
expr_stmt|;
name|LoadBalanceType
name|loadBalance
init|=
name|assertLoadBalancer
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Here should have 3 output here"
argument_list|,
literal|3
argument_list|,
name|loadBalance
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The loadBalancer shoud be StickyLoadBalanceStrategy"
argument_list|,
name|loadBalance
operator|.
name|getLoadBalancerType
argument_list|()
operator|instanceof
name|StickyLoadBalanceStrategy
argument_list|)
expr_stmt|;
name|StickyLoadBalanceStrategy
name|strategy
init|=
operator|(
name|StickyLoadBalanceStrategy
operator|)
name|loadBalance
operator|.
name|getLoadBalancerType
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"the expression should not be null "
argument_list|,
name|strategy
operator|.
name|getExpressionType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseBatchResequencerXml ()
specifier|public
name|void
name|testParseBatchResequencerXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"resequencerBatch.xml"
argument_list|)
decl_stmt|;
name|ResequencerType
name|resequencer
init|=
name|assertResequencer
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|resequencer
operator|.
name|getStreamConfig
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resequencer
operator|.
name|getBatchConfig
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|resequencer
operator|.
name|getBatchConfig
argument_list|()
operator|.
name|getBatchSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2000L
argument_list|,
name|resequencer
operator|.
name|getBatchConfig
argument_list|()
operator|.
name|getBatchTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseStreamResequencerXml ()
specifier|public
name|void
name|testParseStreamResequencerXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteType
name|route
init|=
name|assertOneRoute
argument_list|(
literal|"resequencerStream.xml"
argument_list|)
decl_stmt|;
name|ResequencerType
name|resequencer
init|=
name|assertResequencer
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resequencer
operator|.
name|getStreamConfig
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resequencer
operator|.
name|getBatchConfig
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|resequencer
operator|.
name|getStreamConfig
argument_list|()
operator|.
name|getCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2000L
argument_list|,
name|resequencer
operator|.
name|getStreamConfig
argument_list|()
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|assertOneRoute (String uri)
specifier|protected
name|RouteType
name|assertOneRoute
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|JAXBException
block|{
name|RouteContainer
name|context
init|=
name|assertParseAsJaxb
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|RouteType
name|route
init|=
name|assertOneElement
argument_list|(
name|context
operator|.
name|getRoutes
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|route
return|;
block|}
DECL|method|assertFrom (RouteType route, String uri)
specifier|protected
name|void
name|assertFrom
parameter_list|(
name|RouteType
name|route
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|FromType
name|from
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getInputs
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From URI"
argument_list|,
name|uri
argument_list|,
name|from
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertChildTo (String message, ProcessorType<?> route, String uri)
specifier|protected
name|void
name|assertChildTo
parameter_list|(
name|String
name|message
parameter_list|,
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
name|ToType
name|value
init|=
name|assertIsInstanceOf
argument_list|(
name|ToType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|message
operator|+
literal|"To URI"
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Testing: "
operator|+
name|text
operator|+
literal|" is equal to: "
operator|+
name|uri
operator|+
literal|" for processor: "
operator|+
name|processor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
name|uri
argument_list|,
name|value
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertTo (String message, ProcessorType processor, String uri)
specifier|protected
name|void
name|assertTo
parameter_list|(
name|String
name|message
parameter_list|,
name|ProcessorType
name|processor
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|ToType
name|value
init|=
name|assertIsInstanceOf
argument_list|(
name|ToType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|message
operator|+
literal|"To URI"
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Testing: "
operator|+
name|text
operator|+
literal|" is equal to: "
operator|+
name|uri
operator|+
literal|" for processor: "
operator|+
name|processor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
name|uri
argument_list|,
name|value
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertChildTo (ProcessorType route, String... uris)
specifier|protected
name|void
name|assertChildTo
parameter_list|(
name|ProcessorType
name|route
parameter_list|,
name|String
modifier|...
name|uris
parameter_list|)
block|{
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|list
init|=
name|assertListSize
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|uris
operator|.
name|length
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|uris
control|)
block|{
name|assertTo
argument_list|(
literal|"output["
operator|+
name|idx
operator|+
literal|"] "
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|idx
operator|++
argument_list|)
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertChildTo (ProcessorType route, String uri, int toIdx)
specifier|protected
name|void
name|assertChildTo
parameter_list|(
name|ProcessorType
name|route
parameter_list|,
name|String
name|uri
parameter_list|,
name|int
name|toIdx
parameter_list|)
block|{
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|list
init|=
name|route
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|assertTo
argument_list|(
literal|"to and idx="
operator|+
name|toIdx
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|toIdx
argument_list|)
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|assertProcessor (ProcessorType<?> route, String processorRef)
specifier|protected
name|void
name|assertProcessor
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|,
name|String
name|processorRef
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
name|ProcessorRef
name|to
init|=
name|assertIsInstanceOf
argument_list|(
name|ProcessorRef
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Processor ref"
argument_list|,
name|processorRef
argument_list|,
name|to
operator|.
name|getRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertFilter (ProcessorType<?> route)
specifier|protected
name|FilterType
name|assertFilter
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|FilterType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertRecipientList (ProcessorType<?> route)
specifier|protected
name|RecipientListType
name|assertRecipientList
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|RecipientListType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertConvertBody (ProcessorType<?> route)
specifier|protected
name|ConvertBodyType
name|assertConvertBody
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|ConvertBodyType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertRoutingSlip (ProcessorType<?> route)
specifier|protected
name|RoutingSlipType
name|assertRoutingSlip
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|RoutingSlipType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertSetHeader (ProcessorType<?> route)
specifier|protected
name|SetHeaderType
name|assertSetHeader
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|SetHeaderType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertChoice (ProcessorType<?> route)
specifier|protected
name|ChoiceType
name|assertChoice
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|ChoiceType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertSplitter (ProcessorType<?> route)
specifier|protected
name|SplitterType
name|assertSplitter
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|SplitterType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertLoadBalancer (ProcessorType<?> route)
specifier|protected
name|LoadBalanceType
name|assertLoadBalancer
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|LoadBalanceType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertResequencer (ProcessorType<?> route)
specifier|protected
name|ResequencerType
name|assertResequencer
parameter_list|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|route
parameter_list|)
block|{
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|ResequencerType
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|assertExpression (ExpressionType expression, String language, String languageExpression)
specifier|protected
name|void
name|assertExpression
parameter_list|(
name|ExpressionType
name|expression
parameter_list|,
name|String
name|language
parameter_list|,
name|String
name|languageExpression
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Expression should not be null!"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expression language"
argument_list|,
name|language
argument_list|,
name|expression
operator|.
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expression"
argument_list|,
name|languageExpression
argument_list|,
name|expression
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertInterceptorRefs (ProcessorType route, String... names)
specifier|protected
name|void
name|assertInterceptorRefs
parameter_list|(
name|ProcessorType
name|route
parameter_list|,
name|String
modifier|...
name|names
parameter_list|)
block|{
comment|/*         TODO         int idx = 0;         List<InterceptorType> interceptors = route.getInterceptors();         for (String name : names) {             int nextIdx = idx + 1;             assertTrue("Not enough interceptors! Expected: " + nextIdx + " but have: " + interceptors,                     nextIdx<= interceptors.size());              InterceptorRef interceptor = assertIsInstanceOf(InterceptorRef.class, interceptors.get(idx++));             assertEquals("Interceptor: " + idx, name, interceptor.getRef());         } */
block|}
block|}
end_class

end_unit

