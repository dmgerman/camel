begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|api
operator|.
name|management
operator|.
name|ManagedCamelContext
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedAggregateProcessorMBean
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
name|AggregateController
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
name|DefaultAggregateController
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
DECL|class|ManagedAggregateControllerTest
specifier|public
class|class
name|ManagedAggregateControllerTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|controller
specifier|private
name|AggregateController
name|controller
init|=
operator|new
name|DefaultAggregateController
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testForceCompletionOfAll ()
specifier|public
name|void
name|testForceCompletionOfAll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"myAggregator\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test1"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test3"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test4"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"test1test3"
argument_list|,
literal|"test2test4"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"force"
argument_list|)
expr_stmt|;
name|Integer
name|pending
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"aggregationRepositoryGroups"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pending
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|groups
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"forceCompletionOfAllGroups"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|groups
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Long
name|completed
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|completed
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TotalCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|in
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TotalIn"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|in
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|byForced
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CompletedByForce"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|byForced
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|bySize
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CompletedBySize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|bySize
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|size
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CompletionSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|size
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|lan
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CorrelationExpressionLanguage"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"header"
argument_list|,
name|lan
argument_list|)
expr_stmt|;
name|String
name|cor
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CorrelationExpression"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|cor
argument_list|)
expr_stmt|;
name|Integer
name|inflight
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"InProgressCompleteExchanges"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|inflight
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|pending
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"aggregationRepositoryGroups"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pending
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testForceCompletionOfGroup ()
specifier|public
name|void
name|testForceCompletionOfGroup
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"myAggregator\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test1"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test3"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test4"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"test1test3"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"force"
argument_list|)
expr_stmt|;
name|Integer
name|pending
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"aggregationRepositoryGroups"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pending
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|groups
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"forceCompletionOfGroup"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"1"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|groups
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Long
name|completed
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|completed
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TotalCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|in
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TotalIn"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|in
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|byForced
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CompletedByForce"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|byForced
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|bySize
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CompletedBySize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|bySize
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|size
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CompletionSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|size
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|lan
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CorrelationExpressionLanguage"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"header"
argument_list|,
name|lan
argument_list|)
expr_stmt|;
name|String
name|cor
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CorrelationExpression"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|cor
argument_list|)
expr_stmt|;
name|Integer
name|inflight
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"InProgressCompleteExchanges"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|inflight
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|pending
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"aggregationRepositoryGroups"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pending
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// we can also use the client mbean
name|ManagedAggregateProcessorMBean
name|client
init|=
name|context
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getManagedProcessor
argument_list|(
literal|"myAggregator"
argument_list|,
name|ManagedAggregateProcessorMBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|client
operator|.
name|getCompletedByForce
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|client
operator|.
name|getTotalIn
argument_list|()
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
literal|"direct:start"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"id"
argument_list|)
argument_list|,
operator|new
name|MyAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|aggregateController
argument_list|(
name|controller
argument_list|)
operator|.
name|id
argument_list|(
literal|"myAggregator"
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|10
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:aggregated"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyAggregationStrategy
specifier|public
specifier|static
class|class
name|MyAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
name|String
name|body1
init|=
name|oldExchange
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
name|String
name|body2
init|=
name|newExchange
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
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body1
operator|+
name|body2
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
block|}
block|}
end_class

end_unit

