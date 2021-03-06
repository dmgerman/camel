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

begin_class
DECL|class|ConsumerCreationStrategyFactory
specifier|public
specifier|final
class|class
name|ConsumerCreationStrategyFactory
block|{
DECL|field|pulsarConsumer
specifier|private
specifier|final
name|PulsarConsumer
name|pulsarConsumer
decl_stmt|;
DECL|method|ConsumerCreationStrategyFactory (PulsarConsumer pulsarConsumer)
specifier|private
name|ConsumerCreationStrategyFactory
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
DECL|method|create (PulsarConsumer pulsarConsumer)
specifier|public
specifier|static
name|ConsumerCreationStrategyFactory
name|create
parameter_list|(
name|PulsarConsumer
name|pulsarConsumer
parameter_list|)
block|{
name|validate
argument_list|(
name|pulsarConsumer
argument_list|)
expr_stmt|;
return|return
operator|new
name|ConsumerCreationStrategyFactory
argument_list|(
name|pulsarConsumer
argument_list|)
return|;
block|}
DECL|method|validate (PulsarConsumer pulsarConsumer)
specifier|private
specifier|static
name|void
name|validate
parameter_list|(
name|PulsarConsumer
name|pulsarConsumer
parameter_list|)
block|{
if|if
condition|(
name|pulsarConsumer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Pulsar Consumer cannot be null"
argument_list|)
throw|;
block|}
block|}
DECL|method|getStrategy (final SubscriptionType subscriptionType)
specifier|public
name|ConsumerCreationStrategy
name|getStrategy
parameter_list|(
specifier|final
name|SubscriptionType
name|subscriptionType
parameter_list|)
block|{
specifier|final
name|SubscriptionType
name|type
init|=
name|subscriptionType
operator|==
literal|null
condition|?
name|SubscriptionType
operator|.
name|EXCLUSIVE
else|:
name|subscriptionType
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|SHARED
case|:
return|return
operator|new
name|SharedConsumerStrategy
argument_list|(
name|pulsarConsumer
argument_list|)
return|;
case|case
name|EXCLUSIVE
case|:
return|return
operator|new
name|ExclusiveConsumerStrategy
argument_list|(
name|pulsarConsumer
argument_list|)
return|;
case|case
name|FAILOVER
case|:
return|return
operator|new
name|FailoverConsumerStrategy
argument_list|(
name|pulsarConsumer
argument_list|)
return|;
default|default:
return|return
operator|new
name|ExclusiveConsumerStrategy
argument_list|(
name|pulsarConsumer
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

