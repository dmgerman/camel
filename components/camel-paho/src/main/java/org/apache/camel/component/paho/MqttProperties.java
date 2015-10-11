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

begin_comment
comment|/**  * MQTT message properties.  */
end_comment

begin_class
DECL|class|MqttProperties
specifier|public
class|class
name|MqttProperties
block|{
DECL|field|topic
specifier|private
name|String
name|topic
decl_stmt|;
DECL|field|qos
specifier|private
name|int
name|qos
decl_stmt|;
DECL|field|retain
specifier|private
name|boolean
name|retain
decl_stmt|;
DECL|field|duplicate
specifier|private
name|boolean
name|duplicate
decl_stmt|;
DECL|method|MqttProperties ()
specifier|public
name|MqttProperties
parameter_list|()
block|{}
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
DECL|method|setTopic (String topic)
specifier|public
name|void
name|setTopic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
block|}
DECL|method|getQos ()
specifier|public
name|int
name|getQos
parameter_list|()
block|{
return|return
name|qos
return|;
block|}
DECL|method|setQos (int qos)
specifier|public
name|void
name|setQos
parameter_list|(
name|int
name|qos
parameter_list|)
block|{
name|this
operator|.
name|qos
operator|=
name|qos
expr_stmt|;
block|}
DECL|method|isRetain ()
specifier|public
name|boolean
name|isRetain
parameter_list|()
block|{
return|return
name|retain
return|;
block|}
DECL|method|setRetain (boolean retain)
specifier|public
name|void
name|setRetain
parameter_list|(
name|boolean
name|retain
parameter_list|)
block|{
name|this
operator|.
name|retain
operator|=
name|retain
expr_stmt|;
block|}
DECL|method|isDuplicate ()
specifier|public
name|boolean
name|isDuplicate
parameter_list|()
block|{
return|return
name|duplicate
return|;
block|}
DECL|method|setDuplicate (boolean duplicate)
specifier|public
name|void
name|setDuplicate
parameter_list|(
name|boolean
name|duplicate
parameter_list|)
block|{
name|this
operator|.
name|duplicate
operator|=
name|duplicate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"PahoMqttProperties [topic="
operator|+
name|topic
operator|+
literal|", qos="
operator|+
name|qos
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

