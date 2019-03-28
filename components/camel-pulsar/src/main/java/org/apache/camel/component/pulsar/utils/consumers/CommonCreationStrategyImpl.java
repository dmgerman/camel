begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar.utils.consumers
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
operator|.
name|utils
operator|.
name|consumers
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
name|component
operator|.
name|pulsar
operator|.
name|PulsarConsumer
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
name|pulsar
operator|.
name|PulsarEndpoint
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
name|pulsar
operator|.
name|PulsarMessageListener
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
name|pulsar
operator|.
name|configuration
operator|.
name|PulsarConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|ConsumerBuilder
import|;
end_import

begin_class
DECL|class|CommonCreationStrategyImpl
specifier|public
specifier|final
class|class
name|CommonCreationStrategyImpl
block|{
DECL|method|create (final String name, final PulsarEndpoint pulsarEndpoint, final PulsarConsumer pulsarConsumer)
specifier|public
specifier|static
name|ConsumerBuilder
argument_list|<
name|byte
index|[]
argument_list|>
name|create
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|PulsarEndpoint
name|pulsarEndpoint
parameter_list|,
specifier|final
name|PulsarConsumer
name|pulsarConsumer
parameter_list|)
block|{
specifier|final
name|PulsarConfiguration
name|endpointConfiguration
init|=
name|pulsarEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
return|return
name|pulsarEndpoint
operator|.
name|getPulsarClient
argument_list|()
operator|.
name|newConsumer
argument_list|()
operator|.
name|topic
argument_list|(
name|pulsarEndpoint
operator|.
name|getTopic
argument_list|()
argument_list|)
operator|.
name|subscriptionName
argument_list|(
name|endpointConfiguration
operator|.
name|getSubscriptionName
argument_list|()
argument_list|)
operator|.
name|receiverQueueSize
argument_list|(
name|endpointConfiguration
operator|.
name|getConsumerQueueSize
argument_list|()
argument_list|)
operator|.
name|consumerName
argument_list|(
name|name
argument_list|)
operator|.
name|messageListener
argument_list|(
operator|new
name|PulsarMessageListener
argument_list|(
name|pulsarEndpoint
argument_list|,
name|pulsarConsumer
operator|.
name|getExceptionHandler
argument_list|()
argument_list|,
name|pulsarConsumer
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

