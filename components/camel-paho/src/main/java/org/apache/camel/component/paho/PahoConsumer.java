begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|Processor
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
name|SuspendableService
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
name|DefaultConsumer
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
name|IMqttDeliveryToken
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
name|MqttCallbackExtended
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
name|MqttClient
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
name|MqttException
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

begin_class
DECL|class|PahoConsumer
specifier|public
class|class
name|PahoConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|SuspendableService
block|{
DECL|field|client
specifier|private
specifier|volatile
name|MqttClient
name|client
decl_stmt|;
DECL|field|clientId
specifier|private
specifier|volatile
name|String
name|clientId
decl_stmt|;
DECL|field|stopClient
specifier|private
specifier|volatile
name|boolean
name|stopClient
decl_stmt|;
DECL|field|connectOptions
specifier|private
specifier|volatile
name|MqttConnectOptions
name|connectOptions
decl_stmt|;
DECL|method|PahoConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|PahoConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|MqttClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|setClient (MqttClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|MqttClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|connectOptions
operator|=
name|PahoEndpoint
operator|.
name|createMqttConnectOptions
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|clientId
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getClientId
argument_list|()
expr_stmt|;
if|if
condition|(
name|clientId
operator|==
literal|null
condition|)
block|{
name|clientId
operator|=
literal|"camel-"
operator|+
name|MqttClient
operator|.
name|generateClientId
argument_list|()
expr_stmt|;
block|}
name|stopClient
operator|=
literal|true
expr_stmt|;
name|client
operator|=
operator|new
name|MqttClient
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBrokerUrl
argument_list|()
argument_list|,
name|clientId
argument_list|,
name|PahoEndpoint
operator|.
name|createMqttClientPersistence
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Connecting client: {} to broker: {}"
argument_list|,
name|clientId
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBrokerUrl
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|connect
argument_list|(
name|connectOptions
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|setCallback
argument_list|(
operator|new
name|MqttCallbackExtended
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|connectComplete
parameter_list|(
name|boolean
name|reconnect
parameter_list|,
name|String
name|serverURI
parameter_list|)
block|{
if|if
condition|(
name|reconnect
condition|)
block|{
try|try
block|{
name|client
operator|.
name|subscribe
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getTopic
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQos
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MqttException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"MQTT resubscribe failed {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|connectionLost
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"MQTT broker connection lost due {}"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|messageArrived
parameter_list|(
name|String
name|topic
parameter_list|,
name|MqttMessage
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Message arrived on topic: {} -> {}"
argument_list|,
name|topic
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|message
argument_list|,
name|topic
argument_list|)
decl_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deliveryComplete
parameter_list|(
name|IMqttDeliveryToken
name|token
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Delivery complete. Token: {}"
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Subscribing client: {} to topic: {}"
argument_list|,
name|clientId
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|subscribe
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getTopic
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQos
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|stopClient
operator|&&
name|client
operator|!=
literal|null
operator|&&
name|client
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|String
name|topic
init|=
name|getEndpoint
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Un-unsubscribing client: {} from topic: {}"
argument_list|,
name|clientId
argument_list|,
name|topic
argument_list|)
expr_stmt|;
name|client
operator|.
name|unsubscribe
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Connecting client: {} from broker: {}"
argument_list|,
name|clientId
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBrokerUrl
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doSuspend
argument_list|()
expr_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|String
name|topic
init|=
name|getEndpoint
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Un-unsubscribing client: {} from topic: {}"
argument_list|,
name|clientId
argument_list|,
name|topic
argument_list|)
expr_stmt|;
name|client
operator|.
name|unsubscribe
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doResume
argument_list|()
expr_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|String
name|topic
init|=
name|getEndpoint
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Subscribing client: {} to topic: {}"
argument_list|,
name|clientId
argument_list|,
name|topic
argument_list|)
expr_stmt|;
name|client
operator|.
name|subscribe
argument_list|(
name|topic
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQos
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|PahoEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|PahoEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

