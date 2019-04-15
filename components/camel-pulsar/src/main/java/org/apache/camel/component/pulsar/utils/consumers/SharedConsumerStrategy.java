begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Consumer
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
name|PulsarClientException
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
name|SubscriptionType
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
DECL|class|SharedConsumerStrategy
specifier|public
class|class
name|SharedConsumerStrategy
implements|implements
name|ConsumerCreationStrategy
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SharedConsumerStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|pulsarConsumer
specifier|private
specifier|final
name|PulsarConsumer
name|pulsarConsumer
decl_stmt|;
DECL|method|SharedConsumerStrategy (PulsarConsumer pulsarConsumer)
name|SharedConsumerStrategy
parameter_list|(
name|PulsarConsumer
name|pulsarConsumer
parameter_list|)
block|{
name|this
operator|.
name|pulsarConsumer
operator|=
name|pulsarConsumer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (final PulsarEndpoint pulsarEndpoint)
specifier|public
name|Collection
argument_list|<
name|Consumer
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|>
name|create
parameter_list|(
specifier|final
name|PulsarEndpoint
name|pulsarEndpoint
parameter_list|)
block|{
return|return
name|createMultipleConsumers
argument_list|(
name|pulsarEndpoint
argument_list|)
return|;
block|}
DECL|method|createMultipleConsumers (final PulsarEndpoint pulsarEndpoint)
specifier|private
name|Collection
argument_list|<
name|Consumer
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|>
name|createMultipleConsumers
parameter_list|(
specifier|final
name|PulsarEndpoint
name|pulsarEndpoint
parameter_list|)
block|{
specifier|final
name|Collection
argument_list|<
name|Consumer
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|>
name|consumers
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|PulsarConfiguration
name|configuration
init|=
name|pulsarEndpoint
operator|.
name|getPulsarConfiguration
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|configuration
operator|.
name|getNumberOfConsumers
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|consumerName
init|=
name|configuration
operator|.
name|getConsumerNamePrefix
argument_list|()
operator|+
name|i
decl_stmt|;
try|try
block|{
name|ConsumerBuilder
argument_list|<
name|byte
index|[]
argument_list|>
name|builder
init|=
name|CommonCreationStrategyImpl
operator|.
name|create
argument_list|(
name|consumerName
argument_list|,
name|pulsarEndpoint
argument_list|,
name|pulsarConsumer
argument_list|)
decl_stmt|;
name|consumers
operator|.
name|add
argument_list|(
name|builder
operator|.
name|subscriptionType
argument_list|(
name|SubscriptionType
operator|.
name|Shared
argument_list|)
operator|.
name|subscribe
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PulsarClientException
name|exception
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"A PulsarClientException occurred when creating Consumer {}, {}"
argument_list|,
name|consumerName
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|consumers
return|;
block|}
block|}
end_class

end_unit
