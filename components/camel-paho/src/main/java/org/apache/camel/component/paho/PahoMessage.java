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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultMessage
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
DECL|class|PahoMessage
specifier|public
class|class
name|PahoMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|mqttMessage
specifier|private
specifier|transient
name|MqttMessage
name|mqttMessage
decl_stmt|;
DECL|method|PahoMessage ()
specifier|public
name|PahoMessage
parameter_list|()
block|{     }
DECL|method|PahoMessage (MqttMessage mqttMessage)
specifier|public
name|PahoMessage
parameter_list|(
name|MqttMessage
name|mqttMessage
parameter_list|)
block|{
name|this
operator|.
name|mqttMessage
operator|=
name|mqttMessage
expr_stmt|;
block|}
DECL|method|getMqttMessage ()
specifier|public
name|MqttMessage
name|getMqttMessage
parameter_list|()
block|{
return|return
name|mqttMessage
return|;
block|}
DECL|method|setMqttMessage (MqttMessage mqttMessage)
specifier|public
name|void
name|setMqttMessage
parameter_list|(
name|MqttMessage
name|mqttMessage
parameter_list|)
block|{
name|this
operator|.
name|mqttMessage
operator|=
name|mqttMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|PahoMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|PahoMessage
argument_list|(
name|mqttMessage
argument_list|)
return|;
block|}
block|}
end_class

end_unit

