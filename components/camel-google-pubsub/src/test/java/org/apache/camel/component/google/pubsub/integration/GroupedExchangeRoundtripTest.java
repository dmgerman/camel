begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.pubsub.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|pubsub
operator|.
name|integration
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|google
operator|.
name|pubsub
operator|.
name|PubsubTestSupport
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
name|GroupedExchangeAggregationStrategy
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
name|junit
operator|.
name|BeforeClass
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
DECL|class|GroupedExchangeRoundtripTest
specifier|public
class|class
name|GroupedExchangeRoundtripTest
extends|extends
name|PubsubTestSupport
block|{
DECL|field|TOPIC_NAME
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC_NAME
init|=
literal|"groupTopic"
decl_stmt|;
DECL|field|SUBSCRIPTION_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SUBSCRIPTION_NAME
init|=
literal|"groupSubscription"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:aggregator"
argument_list|)
DECL|field|aggregator
specifier|private
name|Endpoint
name|aggregator
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"google-pubsub:{{project.id}}:"
operator|+
name|TOPIC_NAME
argument_list|)
DECL|field|topic
specifier|private
name|Endpoint
name|topic
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:sendResult"
argument_list|)
DECL|field|sendResult
specifier|private
name|MockEndpoint
name|sendResult
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"google-pubsub:{{project.id}}:"
operator|+
name|SUBSCRIPTION_NAME
argument_list|)
DECL|field|pubsubSubscription
specifier|private
name|Endpoint
name|pubsubSubscription
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:receiveResult"
argument_list|)
DECL|field|receiveResult
specifier|private
name|MockEndpoint
name|receiveResult
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:aggregator"
argument_list|)
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|createTopicSubscription ()
specifier|public
specifier|static
name|void
name|createTopicSubscription
parameter_list|()
throws|throws
name|Exception
block|{
name|createTopicSubscriptionPair
argument_list|(
name|TOPIC_NAME
argument_list|,
name|SUBSCRIPTION_NAME
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|aggregator
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"Group_Send"
argument_list|)
operator|.
name|aggregate
argument_list|(
operator|new
name|GroupedExchangeAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|2
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|5000L
argument_list|)
operator|.
name|to
argument_list|(
name|topic
argument_list|)
operator|.
name|to
argument_list|(
name|sendResult
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|pubsubSubscription
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"Group_Receive"
argument_list|)
operator|.
name|to
argument_list|(
name|receiveResult
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Tests that a grouped exhcange is successfully received      *      * @throws Exception      */
annotation|@
name|Test
DECL|method|sendGrouped ()
specifier|public
name|void
name|sendGrouped
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange1
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Exchange
name|exchange2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|body1
init|=
literal|"Group 1.1 : "
operator|+
name|exchange1
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|String
name|body2
init|=
literal|"Group 1.2 : "
operator|+
name|exchange2
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|receiveResult
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|receiveResult
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
name|body1
argument_list|,
name|body2
argument_list|)
expr_stmt|;
name|exchange1
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body1
argument_list|)
expr_stmt|;
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body2
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|exchange1
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|exchange2
argument_list|)
expr_stmt|;
name|receiveResult
operator|.
name|assertIsSatisfied
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// Send result section
name|List
argument_list|<
name|Exchange
argument_list|>
name|results
init|=
name|sendResult
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Received exchanges"
argument_list|,
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|exchangeGrouped
init|=
operator|(
name|List
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|GROUPED_EXCHANGE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Received messages within the exchange"
argument_list|,
literal|2
argument_list|,
name|exchangeGrouped
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

