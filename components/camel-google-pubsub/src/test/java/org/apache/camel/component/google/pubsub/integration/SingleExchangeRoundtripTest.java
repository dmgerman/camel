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
name|HashMap
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
name|Map
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
name|GooglePubsubConstants
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
DECL|class|SingleExchangeRoundtripTest
specifier|public
class|class
name|SingleExchangeRoundtripTest
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
literal|"singleSend"
decl_stmt|;
DECL|field|SUBSCRIPTION_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SUBSCRIPTION_NAME
init|=
literal|"singleReceive"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:from"
argument_list|)
DECL|field|directIn
specifier|private
name|Endpoint
name|directIn
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"google-pubsub:{{project.id}}:"
operator|+
name|TOPIC_NAME
argument_list|)
DECL|field|pubsubTopic
specifier|private
name|Endpoint
name|pubsubTopic
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
literal|"direct:from"
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
name|directIn
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"Single_Send"
argument_list|)
operator|.
name|to
argument_list|(
name|pubsubTopic
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
literal|"Single_Receive"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:one"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:one"
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
annotation|@
name|Test
DECL|method|testSingleMessageSend ()
specifier|public
name|void
name|testSingleMessageSend
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|attributeKey
init|=
literal|"ATTRIBUTE-TEST-KEY"
decl_stmt|;
name|String
name|attributeValue
init|=
literal|"ATTRIBUTE-TEST-VALUE"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|attributeKey
argument_list|,
name|attributeValue
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Single  : "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|ATTRIBUTES
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
name|receiveResult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|receiveResult
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|sentExchanges
init|=
name|sendResult
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Sent exchanges"
argument_list|,
literal|1
argument_list|,
name|sentExchanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|sentExchange
init|=
name|sentExchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Sent ID"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|,
name|sentExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|receiveResult
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|receivedExchanges
init|=
name|receiveResult
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Received exchanges"
argument_list|,
name|receivedExchanges
argument_list|)
expr_stmt|;
name|Exchange
name|receivedExchange
init|=
name|receivedExchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"PUBSUB Message ID Property"
argument_list|,
name|receivedExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"PUBSUB Ack ID Property"
argument_list|,
name|receivedExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|ACK_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"PUBSUB Published Time"
argument_list|,
name|receivedExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|PUBLISH_TIME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PUBSUB Header Attribute"
argument_list|,
name|attributeValue
argument_list|,
operator|(
operator|(
name|Map
operator|)
name|receivedExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|ATTRIBUTES
argument_list|)
operator|)
operator|.
name|get
argument_list|(
name|attributeKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sentExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|,
name|receivedExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GooglePubsubConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

