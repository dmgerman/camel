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
name|builder
operator|.
name|ExchangeBuilder
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
name|MqttCallback
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|PahoConsumer
specifier|public
class|class
name|PahoConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PahoConsumer
operator|.
name|class
argument_list|)
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
name|String
name|topic
init|=
name|getEndpoint
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|subscribe
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setCallback
argument_list|(
operator|new
name|MqttCallback
argument_list|()
block|{
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"MQTT broker connection lost:"
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
name|Exchange
name|exchange
init|=
name|ExchangeBuilder
operator|.
name|anExchange
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|withBody
argument_list|(
name|message
operator|.
name|getPayload
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
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
block|{                      }
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Delivery complete. Token: {}."
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
block|}
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
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
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
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
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

