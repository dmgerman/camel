begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pubnub.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
operator|.
name|example
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|pubsub
operator|.
name|PNMessageResult
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
name|pubnub
operator|.
name|PubNubConstants
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
name|main
operator|.
name|Main
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
operator|.
name|PubNubConstants
operator|.
name|OPERATION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
operator|.
name|PubNubConstants
operator|.
name|UUID
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
operator|.
name|example
operator|.
name|PubNubExampleConstants
operator|.
name|PUBNUB_PUBLISH_KEY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
operator|.
name|example
operator|.
name|PubNubExampleConstants
operator|.
name|PUBNUB_SUBSCRIBE_KEY
import|;
end_import

begin_class
DECL|class|PubNubSensor2Example
specifier|public
specifier|final
class|class
name|PubNubSensor2Example
block|{
DECL|method|PubNubSensor2Example ()
specifier|private
name|PubNubSensor2Example
parameter_list|()
block|{     }
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|PubsubRoute
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|SimulatedDeviceEventGeneratorRoute
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|class|SimulatedDeviceEventGeneratorRoute
specifier|static
class|class
name|SimulatedDeviceEventGeneratorRoute
extends|extends
name|RouteBuilder
block|{
DECL|field|deviceEP
specifier|private
specifier|final
name|String
name|deviceEP
init|=
literal|"pubnub:iot?uuid=device2&publishKey="
operator|+
name|PUBNUB_PUBLISH_KEY
operator|+
literal|"&subscribeKey="
operator|+
name|PUBNUB_SUBSCRIBE_KEY
decl_stmt|;
DECL|field|devicePrivateEP
specifier|private
specifier|final
name|String
name|devicePrivateEP
init|=
literal|"pubnub:device2private?uuid=device2&publishKey="
operator|+
name|PUBNUB_PUBLISH_KEY
operator|+
literal|"&subscribeKey="
operator|+
name|PUBNUB_SUBSCRIBE_KEY
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"timer:device2"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"device-event-route"
argument_list|)
operator|.
name|bean
argument_list|(
name|PubNubSensor2Example
operator|.
name|EventGeneratorBean
operator|.
name|class
argument_list|,
literal|"getRandomEvent('device2')"
argument_list|)
operator|.
name|to
argument_list|(
name|deviceEP
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|devicePrivateEP
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"device-unicast-route"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Message from master to device2 : ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|PubsubRoute
specifier|static
class|class
name|PubsubRoute
extends|extends
name|RouteBuilder
block|{
DECL|field|masterEP
specifier|private
specifier|static
name|String
name|masterEP
init|=
literal|"pubnub:iot?uuid=master&subscribeKey="
operator|+
name|PUBNUB_SUBSCRIBE_KEY
operator|+
literal|"&publishKey="
operator|+
name|PUBNUB_PUBLISH_KEY
decl_stmt|;
DECL|field|devices
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|devices
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|masterEP
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"master-route"
argument_list|)
operator|.
name|bean
argument_list|(
name|PubNubSensor2Example
operator|.
name|PubsubRoute
operator|.
name|DataProcessorBean
operator|.
name|class
argument_list|,
literal|"doSomethingInteresting(${body})"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body} headers : ${headers}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|//TODO Could remote control device to turn on/off sensor measurement
name|from
argument_list|(
literal|"timer:master?delay=15s&period=5s"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"unicast2device-route"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|PubNubConstants
operator|.
name|CHANNEL
argument_list|,
name|method
argument_list|(
name|PubNubSensor2Example
operator|.
name|PubsubRoute
operator|.
name|DataProcessorBean
operator|.
name|class
argument_list|,
literal|"getUnicastChannelOfDevice()"
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Hello device"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|masterEP
argument_list|)
expr_stmt|;
block|}
DECL|class|DataProcessorBean
specifier|public
specifier|static
class|class
name|DataProcessorBean
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"pubnub:iot?uuid=master&subscribeKey="
operator|+
name|PUBNUB_SUBSCRIBE_KEY
argument_list|)
DECL|field|template
specifier|private
specifier|static
name|ProducerTemplate
name|template
decl_stmt|;
DECL|method|getUnicastChannelOfDevice ()
specifier|public
specifier|static
name|String
name|getUnicastChannelOfDevice
parameter_list|()
block|{
comment|// just get the first channel
return|return
name|devices
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|doSomethingInteresting (PNMessageResult message)
specifier|public
specifier|static
name|void
name|doSomethingInteresting
parameter_list|(
name|PNMessageResult
name|message
parameter_list|)
block|{
name|String
name|deviceUUID
decl_stmt|;
name|deviceUUID
operator|=
name|message
operator|.
name|getPublisher
argument_list|()
expr_stmt|;
if|if
condition|(
name|devices
operator|.
name|get
argument_list|(
name|deviceUUID
argument_list|)
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|OPERATION
argument_list|,
literal|"WHERENOW"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|UUID
argument_list|,
name|deviceUUID
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|channels
init|=
operator|(
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|devices
operator|.
name|put
argument_list|(
name|deviceUUID
argument_list|,
name|channels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|class|DeviceWeatherInfo
specifier|static
class|class
name|DeviceWeatherInfo
block|{
DECL|field|device
specifier|private
name|String
name|device
decl_stmt|;
DECL|field|humidity
specifier|private
name|int
name|humidity
decl_stmt|;
DECL|field|temperature
specifier|private
name|int
name|temperature
decl_stmt|;
DECL|method|DeviceWeatherInfo (String device)
name|DeviceWeatherInfo
parameter_list|(
name|String
name|device
parameter_list|)
block|{
name|Random
name|rand
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|this
operator|.
name|device
operator|=
name|device
expr_stmt|;
name|this
operator|.
name|humidity
operator|=
name|rand
operator|.
name|nextInt
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|this
operator|.
name|temperature
operator|=
name|rand
operator|.
name|nextInt
argument_list|(
literal|40
argument_list|)
expr_stmt|;
block|}
DECL|method|getDevice ()
specifier|public
name|String
name|getDevice
parameter_list|()
block|{
return|return
name|device
return|;
block|}
DECL|method|getHumidity ()
specifier|public
name|int
name|getHumidity
parameter_list|()
block|{
return|return
name|humidity
return|;
block|}
DECL|method|getTemperature ()
specifier|public
name|int
name|getTemperature
parameter_list|()
block|{
return|return
name|temperature
return|;
block|}
block|}
DECL|class|EventGeneratorBean
specifier|public
specifier|static
class|class
name|EventGeneratorBean
block|{
DECL|method|getRandomEvent (String device)
specifier|public
specifier|static
name|DeviceWeatherInfo
name|getRandomEvent
parameter_list|(
name|String
name|device
parameter_list|)
block|{
return|return
operator|new
name|DeviceWeatherInfo
argument_list|(
name|device
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

