begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.paho
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|paho
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|JndiRegistry
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
name|AvailablePortFinder
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
name|eclipse
operator|.
name|paho
operator|.
name|client
operator|.
name|mqttv3
operator|.
name|MqttConnectOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|paho
operator|.
name|client
operator|.
name|mqttv3
operator|.
name|MqttMessage
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
DECL|class|PahoComponentTest
specifier|public
class|class
name|PahoComponentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|connectOptions
name|MqttConnectOptions
name|connectOptions
init|=
operator|new
name|MqttConnectOptions
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:test"
argument_list|)
DECL|field|mock
name|MockEndpoint
name|mock
decl_stmt|;
DECL|field|broker
name|BrokerService
name|broker
decl_stmt|;
DECL|field|mqttPort
name|int
name|mqttPort
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|public
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
name|broker
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|broker
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|broker
operator|.
name|addConnector
argument_list|(
literal|"mqtt://localhost:"
operator|+
name|mqttPort
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|broker
operator|.
name|stop
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
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"paho:queue?brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"paho:queue?brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:test2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"paho:queue?brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"paho:persistenceTest?persistence=FILE&brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:persistenceTest"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:connectOptions"
argument_list|)
operator|.
name|to
argument_list|(
literal|"paho:registryConnectOptions?connectOptions=#connectOptions&brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"connectOptions"
argument_list|,
name|connectOptions
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
comment|// Tests
annotation|@
name|Test
DECL|method|checkOptions ()
specifier|public
name|void
name|checkOptions
parameter_list|()
block|{
name|String
name|uri
init|=
literal|"paho:/test/topic"
operator|+
literal|"?clientId=sampleClient"
operator|+
literal|"&brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
operator|+
literal|"&qos=2"
operator|+
literal|"&persistence=file"
decl_stmt|;
name|PahoEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|uri
argument_list|,
name|PahoEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Then
name|assertEquals
argument_list|(
literal|"/test/topic"
argument_list|,
name|endpoint
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sampleClient"
argument_list|,
name|endpoint
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tcp://localhost:"
operator|+
name|mqttPort
argument_list|,
name|endpoint
operator|.
name|getBrokerUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|endpoint
operator|.
name|getQos
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PahoPersistence
operator|.
name|FILE
argument_list|,
name|endpoint
operator|.
name|getPersistence
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldReadMessageFromMqtt ()
specifier|public
name|void
name|shouldReadMessageFromMqtt
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
name|String
name|msg
init|=
literal|"msg"
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|msg
argument_list|)
expr_stmt|;
comment|// When
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
name|msg
argument_list|)
expr_stmt|;
comment|// Then
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotReadMessageFromUnregisteredTopic ()
specifier|public
name|void
name|shouldNotReadMessageFromUnregisteredTopic
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// When
name|template
operator|.
name|sendBody
argument_list|(
literal|"paho:someRandomQueue?brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
argument_list|,
literal|"msg"
argument_list|)
expr_stmt|;
comment|// Then
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldUseConnectionOptionsFromRegistry ()
specifier|public
name|void
name|shouldUseConnectionOptionsFromRegistry
parameter_list|()
block|{
comment|// Given
name|PahoEndpoint
name|pahoWithConnectOptionsFromRegistry
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"paho:registryConnectOptions?connectOptions=#connectOptions&brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
argument_list|,
name|PahoEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Then
name|assertSame
argument_list|(
name|connectOptions
argument_list|,
name|pahoWithConnectOptionsFromRegistry
operator|.
name|resolveMqttConnectOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldAutomaticallyUseConnectionOptionsFromRegistry ()
specifier|public
name|void
name|shouldAutomaticallyUseConnectionOptionsFromRegistry
parameter_list|()
block|{
comment|// Given
name|PahoEndpoint
name|pahoWithConnectOptionsFromRegistry
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"paho:registryConnectOptions?brokerUrl=tcp://localhost:"
operator|+
name|mqttPort
argument_list|,
name|PahoEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Then
name|assertSame
argument_list|(
name|connectOptions
argument_list|,
name|pahoWithConnectOptionsFromRegistry
operator|.
name|resolveMqttConnectOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldKeepDefaultMessageInHeader ()
specifier|public
name|void
name|shouldKeepDefaultMessageInHeader
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|UnsupportedEncodingException
block|{
comment|// Given
specifier|final
name|String
name|msg
init|=
literal|"msg"
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|msg
argument_list|)
expr_stmt|;
comment|// When
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
name|msg
argument_list|)
expr_stmt|;
comment|// Then
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|payload
init|=
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"queue"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PahoConstants
operator|.
name|MQTT_TOPIC
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|msg
argument_list|,
name|payload
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldKeepOriginalMessageInHeader ()
specifier|public
name|void
name|shouldKeepOriginalMessageInHeader
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
specifier|final
name|String
name|msg
init|=
literal|"msg"
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|msg
argument_list|)
expr_stmt|;
comment|// When
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test2"
argument_list|,
name|msg
argument_list|)
expr_stmt|;
comment|// Then
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|MqttMessage
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|PahoMessage
operator|.
name|class
argument_list|)
operator|.
name|getMqttMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|msg
argument_list|,
operator|new
name|String
argument_list|(
name|message
operator|.
name|getPayload
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

