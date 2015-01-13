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
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests bridging between two mqtt topic using Camel  *   * @version  */
end_comment

begin_class
DECL|class|MQTTBrigeTest
specifier|public
class|class
name|MQTTBrigeTest
extends|extends
name|MQTTBaseTest
block|{
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
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:startWorkaround"
argument_list|)
DECL|field|workaroundTemplate
specifier|protected
name|ProducerTemplate
name|workaroundTemplate
decl_stmt|;
annotation|@
name|Test
DECL|method|testMqttBridge ()
specifier|public
name|void
name|testMqttBridge
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedBody
init|=
literal|"Dummy!"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMqttBridgeWorkAround ()
specifier|public
name|void
name|testMqttBridgeWorkAround
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedBody
init|=
literal|"Dummy!"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|workaroundTemplate
operator|.
name|sendBody
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
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
comment|// Bridge message over two MQTT topics
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mqtt:foo?publishTopicName=test/topic1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"mqtt:foo?subscribeTopicName=test/topic1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:testlogger?showAll=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mqtt:foo?publishTopicName=test/resulttopic"
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
literal|"Message processed"
argument_list|)
expr_stmt|;
comment|// Bridge message over two MQTT topics with a seda in between
name|from
argument_list|(
literal|"direct:startWorkaround"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mqtt:foo?publishTopicName=test/topic2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"mqtt:foo?subscribeTopicName=test/topic2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:testlogger?showAll=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mqtt:foo?publishTopicName=test/resulttopic"
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
literal|"Message processed"
argument_list|)
expr_stmt|;
comment|// Forward the result to a mock endpoint to test
name|from
argument_list|(
literal|"mqtt:foo?subscribeTopicName=test/resulttopic"
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

