begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar.configuration
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
name|configuration
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
name|utils
operator|.
name|consumers
operator|.
name|SubscriptionType
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
name|pulsar
operator|.
name|utils
operator|.
name|consumers
operator|.
name|SubscriptionType
operator|.
name|EXCLUSIVE
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|PulsarConfiguration
specifier|public
class|class
name|PulsarConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"subs"
argument_list|)
DECL|field|subscriptionName
specifier|private
name|String
name|subscriptionName
init|=
literal|"subs"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"EXCLUSIVE"
argument_list|)
DECL|field|subscriptionType
specifier|private
name|SubscriptionType
name|subscriptionType
init|=
name|EXCLUSIVE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|numberOfConsumers
specifier|private
name|int
name|numberOfConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|consumerQueueSize
specifier|private
name|int
name|consumerQueueSize
init|=
literal|10
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"sole-consumer"
argument_list|)
DECL|field|consumerName
specifier|private
name|String
name|consumerName
init|=
literal|"sole-consumer"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"default-producer"
argument_list|)
DECL|field|producerName
specifier|private
name|String
name|producerName
init|=
literal|"default-producer"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"cons"
argument_list|)
DECL|field|consumerNamePrefix
specifier|private
name|String
name|consumerNamePrefix
init|=
literal|"cons"
decl_stmt|;
DECL|method|getSubscriptionName ()
specifier|public
name|String
name|getSubscriptionName
parameter_list|()
block|{
return|return
name|subscriptionName
return|;
block|}
comment|/**      * Name of the subscription to use      */
DECL|method|setSubscriptionName (String subscriptionName)
specifier|public
name|void
name|setSubscriptionName
parameter_list|(
name|String
name|subscriptionName
parameter_list|)
block|{
name|this
operator|.
name|subscriptionName
operator|=
name|subscriptionName
expr_stmt|;
block|}
DECL|method|getSubscriptionType ()
specifier|public
name|SubscriptionType
name|getSubscriptionType
parameter_list|()
block|{
return|return
name|subscriptionType
return|;
block|}
comment|/**      * Type of the subscription [EXCLUSIVE|SHARED|FAILOVER], defaults to EXCLUSIVE      */
DECL|method|setSubscriptionType (SubscriptionType subscriptionType)
specifier|public
name|void
name|setSubscriptionType
parameter_list|(
name|SubscriptionType
name|subscriptionType
parameter_list|)
block|{
name|this
operator|.
name|subscriptionType
operator|=
name|subscriptionType
expr_stmt|;
block|}
DECL|method|getNumberOfConsumers ()
specifier|public
name|int
name|getNumberOfConsumers
parameter_list|()
block|{
return|return
name|numberOfConsumers
return|;
block|}
comment|/**      * Number of consumers - defaults to 1      */
DECL|method|setNumberOfConsumers (int numberOfConsumers)
specifier|public
name|void
name|setNumberOfConsumers
parameter_list|(
name|int
name|numberOfConsumers
parameter_list|)
block|{
name|this
operator|.
name|numberOfConsumers
operator|=
name|numberOfConsumers
expr_stmt|;
block|}
DECL|method|getConsumerQueueSize ()
specifier|public
name|int
name|getConsumerQueueSize
parameter_list|()
block|{
return|return
name|consumerQueueSize
return|;
block|}
comment|/**      * Size of the consumer queue - defaults to 10      */
DECL|method|setConsumerQueueSize (int consumerQueueSize)
specifier|public
name|void
name|setConsumerQueueSize
parameter_list|(
name|int
name|consumerQueueSize
parameter_list|)
block|{
name|this
operator|.
name|consumerQueueSize
operator|=
name|consumerQueueSize
expr_stmt|;
block|}
DECL|method|getConsumerName ()
specifier|public
name|String
name|getConsumerName
parameter_list|()
block|{
return|return
name|consumerName
return|;
block|}
comment|/**      * Name of the consumer when subscription is EXCLUSIVE      */
DECL|method|setConsumerName (String consumerName)
specifier|public
name|void
name|setConsumerName
parameter_list|(
name|String
name|consumerName
parameter_list|)
block|{
name|this
operator|.
name|consumerName
operator|=
name|consumerName
expr_stmt|;
block|}
DECL|method|getProducerName ()
specifier|public
name|String
name|getProducerName
parameter_list|()
block|{
return|return
name|producerName
return|;
block|}
comment|/**      * Name of the producer      */
DECL|method|setProducerName (String producerName)
specifier|public
name|void
name|setProducerName
parameter_list|(
name|String
name|producerName
parameter_list|)
block|{
name|this
operator|.
name|producerName
operator|=
name|producerName
expr_stmt|;
block|}
DECL|method|getConsumerNamePrefix ()
specifier|public
name|String
name|getConsumerNamePrefix
parameter_list|()
block|{
return|return
name|consumerNamePrefix
return|;
block|}
comment|/**      * Prefix to add to consumer names when a SHARED or FAILOVER subscription is used      */
DECL|method|setConsumerNamePrefix (String consumerNamePrefix)
specifier|public
name|void
name|setConsumerNamePrefix
parameter_list|(
name|String
name|consumerNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|consumerNamePrefix
operator|=
name|consumerNamePrefix
expr_stmt|;
block|}
block|}
end_class

end_unit

