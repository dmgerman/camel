begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mqtt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mqtt
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
name|HashSet
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
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|broker
operator|.
name|BrokerService
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
comment|/**  * Tests duplicate delivery via mqtt consumer.  *   * @version  */
end_comment

begin_class
DECL|class|MQTTDuplicatesTest
specifier|public
class|class
name|MQTTDuplicatesTest
extends|extends
name|MQTTBaseTest
block|{
DECL|field|MESSAGE_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|MESSAGE_COUNT
init|=
literal|50
decl_stmt|;
DECL|field|WAIT_MILLIS
specifier|private
specifier|static
specifier|final
name|int
name|WAIT_MILLIS
init|=
literal|100
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:withClientID"
argument_list|)
DECL|field|templateWithClientID
specifier|protected
name|ProducerTemplate
name|templateWithClientID
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:withoutClientID"
argument_list|)
DECL|field|templateWithoutClientID
specifier|protected
name|ProducerTemplate
name|templateWithoutClientID
decl_stmt|;
annotation|@
name|Test
DECL|method|testMqttDuplicates ()
specifier|public
name|void
name|testMqttDuplicates
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|MESSAGE_COUNT
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|": Dummy! "
operator|+
name|i
decl_stmt|;
name|templateWithClientID
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:withClientID"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|WAIT_MILLIS
argument_list|)
expr_stmt|;
block|}
name|assertNoDuplicates
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMqttDuplicatesAfterBrokerRestartWithoutClientID ()
specifier|public
name|void
name|testMqttDuplicatesAfterBrokerRestartWithoutClientID
parameter_list|()
throws|throws
name|Exception
block|{
name|brokerService
operator|.
name|stop
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|waitUntilStopped
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|">>>>>>>>>> Restarting broker..."
argument_list|)
expr_stmt|;
name|brokerService
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setAdvisorySupport
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|addConnector
argument_list|(
name|MQTTTestSupport
operator|.
name|getConnection
argument_list|()
operator|+
literal|"?trace=true"
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|start
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|waitUntilStarted
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|">>>>>>>>>> Broker restarted"
argument_list|)
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
name|MESSAGE_COUNT
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|": Dummy-restart-without-clientID! "
operator|+
name|i
decl_stmt|;
name|templateWithoutClientID
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:withoutClientID"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|WAIT_MILLIS
argument_list|)
expr_stmt|;
block|}
name|assertNoDuplicates
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMqttDuplicatesAfterBrokerRestartWithClientID ()
specifier|public
name|void
name|testMqttDuplicatesAfterBrokerRestartWithClientID
parameter_list|()
throws|throws
name|Exception
block|{
name|brokerService
operator|.
name|stop
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|waitUntilStopped
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|">>>>>>>>>> Restarting broker..."
argument_list|)
expr_stmt|;
name|brokerService
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setAdvisorySupport
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|addConnector
argument_list|(
name|MQTTTestSupport
operator|.
name|getConnection
argument_list|()
operator|+
literal|"?trace=true"
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|start
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|waitUntilStarted
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|">>>>>>>>>> Broker restarted"
argument_list|)
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
name|MESSAGE_COUNT
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|": Dummy-restart-with-clientID! "
operator|+
name|i
decl_stmt|;
name|templateWithClientID
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:withClientID"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|WAIT_MILLIS
argument_list|)
expr_stmt|;
block|}
name|assertNoDuplicates
argument_list|()
expr_stmt|;
block|}
DECL|method|assertNoDuplicates ()
specifier|private
name|void
name|assertNoDuplicates
parameter_list|()
block|{
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|resultEndpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"No message was delivered - something wrong happened"
argument_list|,
name|exchanges
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|duplicates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|e
range|:
name|exchanges
control|)
block|{
name|String
name|body
init|=
name|e
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
if|if
condition|(
name|values
operator|.
name|contains
argument_list|(
name|body
argument_list|)
condition|)
block|{
name|duplicates
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
name|values
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Duplicate messages are detected: "
operator|+
name|duplicates
operator|.
name|toString
argument_list|()
argument_list|,
name|duplicates
operator|.
name|isEmpty
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
comment|// --------------------
comment|//  Without client ID:
comment|// --------------------
name|from
argument_list|(
literal|"direct:withoutClientID"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"SenderWithoutClientID"
argument_list|)
operator|.
name|log
argument_list|(
literal|"$$$$$ Sending message: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mqtt:sender?publishTopicName=test/topic1&qualityOfService=ExactlyOnce&host="
operator|+
name|MQTTTestSupport
operator|.
name|getHostForMQTTEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"mqtt:reader?subscribeTopicName=test/topic1&qualityOfService=ExactlyOnce&host="
operator|+
name|MQTTTestSupport
operator|.
name|getHostForMQTTEndpoint
argument_list|()
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"ReceiverWithoutClientID"
argument_list|)
operator|.
name|log
argument_list|(
literal|"$$$$$ Received message: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// --------------------
comment|//  With client ID:
comment|// --------------------
name|from
argument_list|(
literal|"direct:withClientID"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"SenderWithClientID"
argument_list|)
operator|.
name|log
argument_list|(
literal|"$$$$$ Sending message: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mqtt:sender?publishTopicName=test/topic2&clientId=sender&qualityOfService=ExactlyOnce&host="
operator|+
name|MQTTTestSupport
operator|.
name|getHostForMQTTEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"mqtt:reader?subscribeTopicName=test/topic2&clientId=receiver&qualityOfService=ExactlyOnce&host="
operator|+
name|MQTTTestSupport
operator|.
name|getHostForMQTTEndpoint
argument_list|()
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"ReceiverWithClientID"
argument_list|)
operator|.
name|log
argument_list|(
literal|"$$$$$ Received message: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

