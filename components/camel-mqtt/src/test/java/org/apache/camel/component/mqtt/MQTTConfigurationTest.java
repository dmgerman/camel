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
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|QoS
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
DECL|class|MQTTConfigurationTest
specifier|public
class|class
name|MQTTConfigurationTest
extends|extends
name|MQTTBaseTest
block|{
annotation|@
name|Test
DECL|method|testBasicConfiguration ()
specifier|public
name|void
name|testBasicConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mqtt:todo?byDefaultRetain=true&qualityOfService=exactlyOnce&publishTopicName="
operator|+
name|TEST_TOPIC
operator|+
literal|"&subscribeTopicName="
operator|+
name|TEST_TOPIC
operator|+
literal|"&host="
operator|+
name|MQTTTestSupport
operator|.
name|getHostForMQTTEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not a MQTTEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|MQTTEndpoint
argument_list|)
expr_stmt|;
name|MQTTEndpoint
name|mqttEndpoint
init|=
operator|(
name|MQTTEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQoS
argument_list|()
argument_list|,
name|QoS
operator|.
name|EXACTLY_ONCE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPublishTopicName
argument_list|()
argument_list|,
name|TEST_TOPIC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubscribeTopicName
argument_list|()
argument_list|,
name|TEST_TOPIC
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isByDefaultRetain
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipleSubscribeTopicsConfiguration ()
specifier|public
name|void
name|testMultipleSubscribeTopicsConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mqtt:todo?byDefaultRetain=true&qualityOfService=exactlyOnce&publishTopicName="
operator|+
name|TEST_TOPIC
operator|+
literal|"&subscribeTopicNames="
operator|+
name|TEST_TOPICS
operator|+
literal|"&host="
operator|+
name|MQTTTestSupport
operator|.
name|getHostForMQTTEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not a MQTTEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|MQTTEndpoint
argument_list|)
expr_stmt|;
name|MQTTEndpoint
name|mqttEndpoint
init|=
operator|(
name|MQTTEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQoS
argument_list|()
argument_list|,
name|QoS
operator|.
name|EXACTLY_ONCE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPublishTopicName
argument_list|()
argument_list|,
name|TEST_TOPIC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubscribeTopicNames
argument_list|()
argument_list|,
name|TEST_TOPICS
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isByDefaultRetain
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWildcardSubscribeTopicsConfiguration ()
specifier|public
name|void
name|testWildcardSubscribeTopicsConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mqtt:todo?byDefaultRetain=true&qualityOfService=exactlyOnce&publishTopicName="
operator|+
name|TEST_TOPIC
operator|+
literal|"&subscribeTopicNames="
operator|+
name|TEST_TOPICS_WITH_WILDCARDS
operator|+
literal|"&host="
operator|+
name|MQTTTestSupport
operator|.
name|getHostForMQTTEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not a MQTTEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|MQTTEndpoint
argument_list|)
expr_stmt|;
name|MQTTEndpoint
name|mqttEndpoint
init|=
operator|(
name|MQTTEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQoS
argument_list|()
argument_list|,
name|QoS
operator|.
name|EXACTLY_ONCE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPublishTopicName
argument_list|()
argument_list|,
name|TEST_TOPIC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubscribeTopicNames
argument_list|()
argument_list|,
name|TEST_TOPICS_WITH_WILDCARDS
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isByDefaultRetain
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

