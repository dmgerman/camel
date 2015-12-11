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
comment|/**  * Constants to use when working with Paho component.  */
end_comment

begin_class
DECL|class|PahoConstants
specifier|public
specifier|final
class|class
name|PahoConstants
block|{
comment|/**      * Header indicating a topic of a MQTT message.      */
DECL|field|MQTT_TOPIC
specifier|public
specifier|static
specifier|final
name|String
name|MQTT_TOPIC
init|=
literal|"CamelMqttTopic"
decl_stmt|;
DECL|field|DEFAULT_BROKER_URL
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_BROKER_URL
init|=
literal|"tcp://localhost:1883"
decl_stmt|;
DECL|field|DEFAULT_QOS
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_QOS
init|=
literal|2
decl_stmt|;
DECL|field|DEFAULT_QOS_STRING
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_QOS_STRING
init|=
name|DEFAULT_QOS
operator|+
literal|""
decl_stmt|;
DECL|field|DEFAULT_RETAINED_STRING
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_RETAINED_STRING
init|=
literal|"false"
decl_stmt|;
annotation|@
name|Deprecated
DECL|field|HEADER_ORIGINAL_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_ORIGINAL_MESSAGE
init|=
literal|"PahoOriginalMessage"
decl_stmt|;
DECL|method|PahoConstants ()
specifier|private
name|PahoConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

