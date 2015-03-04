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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * test sending recieving from an MQTT broker  */
end_comment

begin_class
DECL|class|MQTTBaseTest
specifier|public
specifier|abstract
class|class
name|MQTTBaseTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MQTTBaseTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|TEST_TOPIC
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_TOPIC
init|=
literal|"ComponentTestTopic"
decl_stmt|;
DECL|field|TEST_TOPIC_2
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_TOPIC_2
init|=
literal|"AnotherTestTopic"
decl_stmt|;
DECL|field|TEST_WILDCARD_TOPIC
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_WILDCARD_TOPIC
init|=
literal|"base/+/#"
decl_stmt|;
DECL|field|TEST_TOPICS
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_TOPICS
init|=
name|TEST_TOPIC
operator|+
literal|","
operator|+
name|TEST_TOPIC_2
decl_stmt|;
DECL|field|TEST_TOPICS_WITH_WILDCARDS
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_TOPICS_WITH_WILDCARDS
init|=
name|TEST_TOPICS
operator|+
literal|","
operator|+
name|TEST_WILDCARD_TOPIC
decl_stmt|;
DECL|field|brokerService
specifier|protected
name|BrokerService
name|brokerService
decl_stmt|;
DECL|field|numberOfMessages
specifier|protected
name|int
name|numberOfMessages
init|=
literal|10
decl_stmt|;
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"mqtt://127.0.0.1:1883"
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|brokerService
operator|!=
literal|null
condition|)
block|{
name|brokerService
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

