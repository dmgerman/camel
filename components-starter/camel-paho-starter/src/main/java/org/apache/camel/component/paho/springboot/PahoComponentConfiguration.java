begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.paho.springboot
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
operator|.
name|springboot
package|;
end_package

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
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * Component for communicating with MQTT M2M message brokers using Eclipse Paho  * MQTT Client.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.paho"
argument_list|)
DECL|class|PahoComponentConfiguration
specifier|public
class|class
name|PahoComponentConfiguration
block|{
comment|/**      * The URL of the MQTT broker.      */
DECL|field|brokerUrl
specifier|private
name|String
name|brokerUrl
decl_stmt|;
comment|/**      * MQTT client identifier.      */
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**      * Client connection options      */
annotation|@
name|NestedConfigurationProperty
DECL|field|connectOptions
specifier|private
name|MqttConnectOptions
name|connectOptions
decl_stmt|;
DECL|method|getBrokerUrl ()
specifier|public
name|String
name|getBrokerUrl
parameter_list|()
block|{
return|return
name|brokerUrl
return|;
block|}
DECL|method|setBrokerUrl (String brokerUrl)
specifier|public
name|void
name|setBrokerUrl
parameter_list|(
name|String
name|brokerUrl
parameter_list|)
block|{
name|this
operator|.
name|brokerUrl
operator|=
name|brokerUrl
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getConnectOptions ()
specifier|public
name|MqttConnectOptions
name|getConnectOptions
parameter_list|()
block|{
return|return
name|connectOptions
return|;
block|}
DECL|method|setConnectOptions (MqttConnectOptions connectOptions)
specifier|public
name|void
name|setConnectOptions
parameter_list|(
name|MqttConnectOptions
name|connectOptions
parameter_list|)
block|{
name|this
operator|.
name|connectOptions
operator|=
name|connectOptions
expr_stmt|;
block|}
block|}
end_class

end_unit

