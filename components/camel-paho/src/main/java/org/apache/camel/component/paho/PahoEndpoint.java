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
name|Consumer
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
name|Producer
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|DefaultEndpoint
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
name|util
operator|.
name|ObjectHelper
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
name|MqttClientPersistence
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
name|eclipse
operator|.
name|paho
operator|.
name|client
operator|.
name|mqttv3
operator|.
name|persist
operator|.
name|MemoryPersistence
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
name|persist
operator|.
name|MqttDefaultFilePersistence
import|;
end_import

begin_comment
comment|/**  * Component for communicating with MQTT message brokers using Eclipse Paho MQTT Client.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"paho"
argument_list|,
name|title
operator|=
literal|"Paho"
argument_list|,
name|label
operator|=
literal|"messaging,iot"
argument_list|,
name|syntax
operator|=
literal|"paho:topic"
argument_list|)
DECL|class|PahoEndpoint
specifier|public
class|class
name|PahoEndpoint
extends|extends
name|DefaultEndpoint
block|{
comment|// Configuration members
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of the topic"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|topic
specifier|private
specifier|final
name|String
name|topic
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|PahoConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|client
specifier|private
name|MqttClient
name|client
decl_stmt|;
DECL|field|stopClient
specifier|private
specifier|transient
name|boolean
name|stopClient
decl_stmt|;
DECL|method|PahoEndpoint (String uri, String topic, PahoComponent component, PahoConfiguration configuration)
specifier|public
name|PahoEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|topic
parameter_list|,
name|PahoComponent
name|component
parameter_list|,
name|PahoConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
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
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|stopClient
operator|=
literal|true
expr_stmt|;
name|client
operator|=
operator|new
name|MqttClient
argument_list|(
name|configuration
operator|.
name|getBrokerUrl
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|resolvePersistence
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|connect
argument_list|(
name|createMqttConnectOptions
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|stopClient
operator|&&
name|client
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|createMqttConnectOptions (PahoConfiguration config)
specifier|private
specifier|static
name|MqttConnectOptions
name|createMqttConnectOptions
parameter_list|(
name|PahoConfiguration
name|config
parameter_list|)
block|{
name|MqttConnectOptions
name|mq
init|=
operator|new
name|MqttConnectOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getUserName
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
condition|)
block|{
name|mq
operator|.
name|setUserName
argument_list|(
name|config
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setPassword
argument_list|(
name|config
operator|.
name|getPassword
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mq
operator|.
name|setAutomaticReconnect
argument_list|(
name|config
operator|.
name|isAutomaticReconnect
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setCleanSession
argument_list|(
name|config
operator|.
name|isCleanSession
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setConnectionTimeout
argument_list|(
name|config
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setExecutorServiceTimeout
argument_list|(
name|config
operator|.
name|getExecutorServiceTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setCustomWebSocketHeaders
argument_list|(
name|config
operator|.
name|getCustomWebSocketHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setHttpsHostnameVerificationEnabled
argument_list|(
name|config
operator|.
name|isHttpsHostnameVerificationEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setKeepAliveInterval
argument_list|(
name|config
operator|.
name|getKeepAliveInterval
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setMaxInflight
argument_list|(
name|config
operator|.
name|getMaxInflight
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setMaxReconnectDelay
argument_list|(
name|config
operator|.
name|getMaxReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setMqttVersion
argument_list|(
name|config
operator|.
name|getMqttVersion
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setSocketFactory
argument_list|(
name|config
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setSSLHostnameVerifier
argument_list|(
name|config
operator|.
name|getSslHostnameVerifier
argument_list|()
argument_list|)
expr_stmt|;
name|mq
operator|.
name|setSSLProperties
argument_list|(
name|config
operator|.
name|getSslClientProps
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getWillTopic
argument_list|()
operator|!=
literal|null
operator|&&
name|config
operator|.
name|getWillPayload
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mq
operator|.
name|setWill
argument_list|(
name|config
operator|.
name|getWillTopic
argument_list|()
argument_list|,
name|config
operator|.
name|getWillPayload
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|,
name|config
operator|.
name|getWillQos
argument_list|()
argument_list|,
name|config
operator|.
name|isWillRetained
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getServerURIs
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mq
operator|.
name|setServerURIs
argument_list|(
name|config
operator|.
name|getServerURIs
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|mq
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|PahoProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|PahoConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|PahoComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|PahoComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getTopic ()
specifier|public
name|String
name|getTopic
parameter_list|()
block|{
return|return
name|topic
return|;
block|}
comment|// Resolvers
DECL|method|resolvePersistence ()
specifier|protected
name|MqttClientPersistence
name|resolvePersistence
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|.
name|getPersistence
argument_list|()
operator|==
name|PahoPersistence
operator|.
name|MEMORY
condition|)
block|{
return|return
operator|new
name|MemoryPersistence
argument_list|()
return|;
block|}
else|else
block|{
if|if
condition|(
name|configuration
operator|.
name|getFilePersistenceDirectory
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|MqttDefaultFilePersistence
argument_list|(
name|configuration
operator|.
name|getFilePersistenceDirectory
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|MqttDefaultFilePersistence
argument_list|()
return|;
block|}
block|}
block|}
DECL|method|createExchange (MqttMessage mqttMessage, String topic)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|MqttMessage
name|mqttMessage
parameter_list|,
name|String
name|topic
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|PahoMessage
name|paho
init|=
operator|new
name|PahoMessage
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|mqttMessage
argument_list|)
decl_stmt|;
name|paho
operator|.
name|setBody
argument_list|(
name|mqttMessage
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
name|paho
operator|.
name|setHeader
argument_list|(
name|PahoConstants
operator|.
name|MQTT_TOPIC
argument_list|,
name|topic
argument_list|)
expr_stmt|;
name|paho
operator|.
name|setHeader
argument_list|(
name|PahoConstants
operator|.
name|MQTT_QOS
argument_list|,
name|mqttMessage
operator|.
name|getQos
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|paho
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|PahoConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
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
comment|/**      * To use an exiting mqtt client      */
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
block|}
end_class

end_unit

