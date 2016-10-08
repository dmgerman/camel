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
name|impl
operator|.
name|DefaultAsyncProducer
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
name|Callback
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

begin_class
DECL|class|MQTTProducer
specifier|public
class|class
name|MQTTProducer
extends|extends
name|DefaultAsyncProducer
implements|implements
name|Processor
block|{
DECL|field|mqttEndpoint
specifier|private
specifier|final
name|MQTTEndpoint
name|mqttEndpoint
decl_stmt|;
DECL|method|MQTTProducer (MQTTEndpoint mqttEndpoint)
specifier|public
name|MQTTProducer
parameter_list|(
name|MQTTEndpoint
name|mqttEndpoint
parameter_list|)
block|{
name|super
argument_list|(
name|mqttEndpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|mqttEndpoint
operator|=
name|mqttEndpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
operator|!
name|mqttEndpoint
operator|.
name|isConnected
argument_list|()
condition|)
block|{
try|try
block|{
name|ensureConnected
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
name|byte
index|[]
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|MQTTConfiguration
name|configuration
init|=
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|boolean
name|retain
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|configuration
operator|.
name|getMqttRetainPropertyName
argument_list|()
argument_list|,
name|configuration
operator|.
name|isByDefaultRetain
argument_list|()
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|QoS
name|qoS
init|=
name|configuration
operator|.
name|getQoS
argument_list|()
decl_stmt|;
name|Object
name|qoSValue
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|configuration
operator|.
name|getMqttQosPropertyName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|qoSValue
operator|!=
literal|null
condition|)
block|{
name|qoS
operator|=
name|MQTTConfiguration
operator|.
name|getQoS
argument_list|(
name|qoSValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// where should we publish to
name|String
name|topicName
init|=
name|configuration
operator|.
name|getPublishTopicName
argument_list|()
decl_stmt|;
comment|// get the topic name by using the header of MQTT_PUBLISH_TOPIC
name|Object
name|topicValue
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQTTConfiguration
operator|.
name|MQTT_PUBLISH_TOPIC
argument_list|)
decl_stmt|;
if|if
condition|(
name|topicValue
operator|!=
literal|null
condition|)
block|{
name|topicName
operator|=
name|topicValue
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
specifier|final
name|String
name|name
init|=
name|topicName
decl_stmt|;
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Publishing to {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|mqttEndpoint
operator|.
name|publish
argument_list|(
name|name
argument_list|,
name|body
argument_list|,
name|qoS
argument_list|,
name|retain
argument_list|,
operator|new
name|Callback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|aVoid
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"onSuccess from {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"onFailure from {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// we continue async, as the mqtt endpoint will invoke the callback when its done
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// no data to send so we are done
name|log
operator|.
name|trace
argument_list|(
literal|"No data to publish"
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|mqttEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isLazySessionCreation
argument_list|()
condition|)
block|{
name|ensureConnected
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|ensureConnected ()
specifier|protected
specifier|synchronized
name|void
name|ensureConnected
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|mqttEndpoint
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|mqttEndpoint
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

