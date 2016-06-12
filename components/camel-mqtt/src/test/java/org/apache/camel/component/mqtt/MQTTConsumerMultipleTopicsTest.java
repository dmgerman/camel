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
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|BlockingConnection
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
name|MQTT
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
name|fusesource
operator|.
name|mqtt
operator|.
name|client
operator|.
name|Topic
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
DECL|class|MQTTConsumerMultipleTopicsTest
specifier|public
class|class
name|MQTTConsumerMultipleTopicsTest
extends|extends
name|MQTTBaseTest
block|{
annotation|@
name|Test
DECL|method|testConsumeMultipleTopics ()
specifier|public
name|void
name|testConsumeMultipleTopics
parameter_list|()
throws|throws
name|Exception
block|{
name|MQTT
name|mqtt
init|=
operator|new
name|MQTT
argument_list|()
decl_stmt|;
name|mqtt
operator|.
name|setHost
argument_list|(
name|MQTTTestSupport
operator|.
name|getHostForMQTTEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|BlockingConnection
name|publisherConnection
init|=
name|mqtt
operator|.
name|blockingConnection
argument_list|()
decl_stmt|;
name|Topic
name|topic1
init|=
operator|new
name|Topic
argument_list|(
name|TEST_TOPIC
argument_list|,
name|QoS
operator|.
name|AT_MOST_ONCE
argument_list|)
decl_stmt|;
name|Topic
name|topic2
init|=
operator|new
name|Topic
argument_list|(
name|TEST_TOPIC_2
argument_list|,
name|QoS
operator|.
name|AT_MOST_ONCE
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|numberOfMessages
operator|*
literal|2
argument_list|)
expr_stmt|;
name|publisherConnection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|String
name|payload
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
name|numberOfMessages
condition|;
name|i
operator|++
control|)
block|{
name|payload
operator|=
literal|"Topic 1, Message "
operator|+
name|i
expr_stmt|;
name|publisherConnection
operator|.
name|publish
argument_list|(
name|topic1
operator|.
name|name
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|,
name|QoS
operator|.
name|AT_LEAST_ONCE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|payload
operator|=
literal|"Topic 2, Message "
operator|+
name|i
expr_stmt|;
name|publisherConnection
operator|.
name|publish
argument_list|(
name|topic2
operator|.
name|name
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|,
name|QoS
operator|.
name|AT_LEAST_ONCE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
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
name|from
argument_list|(
literal|"mqtt:bar?subscribeTopicNames="
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
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|convertToString
argument_list|()
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

