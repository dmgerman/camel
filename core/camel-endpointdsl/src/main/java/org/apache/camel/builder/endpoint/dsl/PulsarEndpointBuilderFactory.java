begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|ExchangePattern
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
name|EndpointConsumerBuilder
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
name|EndpointProducerBuilder
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
name|endpoint
operator|.
name|AbstractEndpointBuilder
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
name|ExceptionHandler
import|;
end_import

begin_comment
comment|/**  * Camel Apache Pulsar Component  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|PulsarEndpointBuilderFactory
specifier|public
interface|interface
name|PulsarEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Apache Pulsar component.      */
DECL|interface|PulsarEndpointConsumerBuilder
specifier|public
interface|interface
name|PulsarEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedPulsarEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the topic is persistent or non-persistent.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|persistence (String persistence)
specifier|default
name|PulsarEndpointConsumerBuilder
name|persistence
parameter_list|(
name|String
name|persistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"persistence"
argument_list|,
name|persistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The tenant.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|tenant (String tenant)
specifier|default
name|PulsarEndpointConsumerBuilder
name|tenant
parameter_list|(
name|String
name|tenant
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tenant"
argument_list|,
name|tenant
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The namespace.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|namespace (String namespace)
specifier|default
name|PulsarEndpointConsumerBuilder
name|namespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"namespace"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The topic.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|topic (String topic)
specifier|default
name|PulsarEndpointConsumerBuilder
name|topic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"topic"
argument_list|,
name|topic
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|PulsarEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( String bridgeErrorHandler)
specifier|default
name|PulsarEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|String
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of the consumer when subscription is EXCLUSIVE.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|consumerName (String consumerName)
specifier|default
name|PulsarEndpointConsumerBuilder
name|consumerName
parameter_list|(
name|String
name|consumerName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consumerName"
argument_list|,
name|consumerName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Prefix to add to consumer names when a SHARED or FAILOVER          * subscription is used.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|consumerNamePrefix ( String consumerNamePrefix)
specifier|default
name|PulsarEndpointConsumerBuilder
name|consumerNamePrefix
parameter_list|(
name|String
name|consumerNamePrefix
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consumerNamePrefix"
argument_list|,
name|consumerNamePrefix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Size of the consumer queue - defaults to 10.          * The option is a<code>int</code> type.          * @group consumer          */
DECL|method|consumerQueueSize ( int consumerQueueSize)
specifier|default
name|PulsarEndpointConsumerBuilder
name|consumerQueueSize
parameter_list|(
name|int
name|consumerQueueSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consumerQueueSize"
argument_list|,
name|consumerQueueSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Size of the consumer queue - defaults to 10.          * The option will be converted to a<code>int</code> type.          * @group consumer          */
DECL|method|consumerQueueSize ( String consumerQueueSize)
specifier|default
name|PulsarEndpointConsumerBuilder
name|consumerQueueSize
parameter_list|(
name|String
name|consumerQueueSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consumerQueueSize"
argument_list|,
name|consumerQueueSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Number of consumers - defaults to 1.          * The option is a<code>int</code> type.          * @group consumer          */
DECL|method|numberOfConsumers ( int numberOfConsumers)
specifier|default
name|PulsarEndpointConsumerBuilder
name|numberOfConsumers
parameter_list|(
name|int
name|numberOfConsumers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"numberOfConsumers"
argument_list|,
name|numberOfConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Number of consumers - defaults to 1.          * The option will be converted to a<code>int</code> type.          * @group consumer          */
DECL|method|numberOfConsumers ( String numberOfConsumers)
specifier|default
name|PulsarEndpointConsumerBuilder
name|numberOfConsumers
parameter_list|(
name|String
name|numberOfConsumers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"numberOfConsumers"
argument_list|,
name|numberOfConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of the subscription to use.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|subscriptionName ( String subscriptionName)
specifier|default
name|PulsarEndpointConsumerBuilder
name|subscriptionName
parameter_list|(
name|String
name|subscriptionName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"subscriptionName"
argument_list|,
name|subscriptionName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Type of the subscription EXCLUSIVESHAREDFAILOVER, defaults to          * EXCLUSIVE.          * The option is a          *<code>org.apache.camel.component.pulsar.utils.consumers.SubscriptionType</code> type.          * @group consumer          */
DECL|method|subscriptionType ( SubscriptionType subscriptionType)
specifier|default
name|PulsarEndpointConsumerBuilder
name|subscriptionType
parameter_list|(
name|SubscriptionType
name|subscriptionType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"subscriptionType"
argument_list|,
name|subscriptionType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Type of the subscription EXCLUSIVESHAREDFAILOVER, defaults to          * EXCLUSIVE.          * The option will be converted to a          *<code>org.apache.camel.component.pulsar.utils.consumers.SubscriptionType</code> type.          * @group consumer          */
DECL|method|subscriptionType ( String subscriptionType)
specifier|default
name|PulsarEndpointConsumerBuilder
name|subscriptionType
parameter_list|(
name|String
name|subscriptionType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"subscriptionType"
argument_list|,
name|subscriptionType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the Apache Pulsar component.      */
DECL|interface|AdvancedPulsarEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedPulsarEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|PulsarEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|PulsarEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option will be converted to a          *<code>org.apache.camel.spi.ExceptionHandler</code> type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( String exceptionHandler)
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|String
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          * The option is a<code>org.apache.camel.ExchangePattern</code> type.          * @group consumer (advanced)          */
DECL|method|exchangePattern ( ExchangePattern exchangePattern)
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          * The option will be converted to a          *<code>org.apache.camel.ExchangePattern</code> type.          * @group consumer (advanced)          */
DECL|method|exchangePattern ( String exchangePattern)
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedPulsarEndpointConsumerBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Builder for endpoint producers for the Apache Pulsar component.      */
DECL|interface|PulsarEndpointProducerBuilder
specifier|public
interface|interface
name|PulsarEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedPulsarEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedPulsarEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the topic is persistent or non-persistent.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|persistence (String persistence)
specifier|default
name|PulsarEndpointProducerBuilder
name|persistence
parameter_list|(
name|String
name|persistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"persistence"
argument_list|,
name|persistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The tenant.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|tenant (String tenant)
specifier|default
name|PulsarEndpointProducerBuilder
name|tenant
parameter_list|(
name|String
name|tenant
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tenant"
argument_list|,
name|tenant
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The namespace.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|namespace (String namespace)
specifier|default
name|PulsarEndpointProducerBuilder
name|namespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"namespace"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The topic.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|topic (String topic)
specifier|default
name|PulsarEndpointProducerBuilder
name|topic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"topic"
argument_list|,
name|topic
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|PulsarEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|default
name|PulsarEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of the producer.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|producerName (String producerName)
specifier|default
name|PulsarEndpointProducerBuilder
name|producerName
parameter_list|(
name|String
name|producerName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"producerName"
argument_list|,
name|producerName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the Apache Pulsar component.      */
DECL|interface|AdvancedPulsarEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedPulsarEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|PulsarEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|PulsarEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedPulsarEndpointProducerBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedPulsarEndpointProducerBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedPulsarEndpointProducerBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedPulsarEndpointProducerBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Builder for endpoint for the Apache Pulsar component.      */
DECL|interface|PulsarEndpointBuilder
specifier|public
interface|interface
name|PulsarEndpointBuilder
extends|extends
name|PulsarEndpointConsumerBuilder
extends|,
name|PulsarEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedPulsarEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedPulsarEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the topic is persistent or non-persistent.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|persistence (String persistence)
specifier|default
name|PulsarEndpointBuilder
name|persistence
parameter_list|(
name|String
name|persistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"persistence"
argument_list|,
name|persistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The tenant.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|tenant (String tenant)
specifier|default
name|PulsarEndpointBuilder
name|tenant
parameter_list|(
name|String
name|tenant
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tenant"
argument_list|,
name|tenant
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The namespace.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|namespace (String namespace)
specifier|default
name|PulsarEndpointBuilder
name|namespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"namespace"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The topic.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|topic (String topic)
specifier|default
name|PulsarEndpointBuilder
name|topic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"topic"
argument_list|,
name|topic
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Apache Pulsar component.      */
DECL|interface|AdvancedPulsarEndpointBuilder
specifier|public
interface|interface
name|AdvancedPulsarEndpointBuilder
extends|extends
name|AdvancedPulsarEndpointConsumerBuilder
extends|,
name|AdvancedPulsarEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|PulsarEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|PulsarEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedPulsarEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedPulsarEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedPulsarEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedPulsarEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.pulsar.utils.consumers.SubscriptionType</code> enum.      */
DECL|enum|SubscriptionType
enum|enum
name|SubscriptionType
block|{
DECL|enumConstant|EXCLUSIVE
name|EXCLUSIVE
block|,
DECL|enumConstant|SHARED
name|SHARED
block|,
DECL|enumConstant|FAILOVER
name|FAILOVER
block|;     }
comment|/**      * Camel Apache Pulsar Component      * Maven coordinates: org.apache.camel:camel-pulsar      */
DECL|method|pulsar (String path)
specifier|default
name|PulsarEndpointBuilder
name|pulsar
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|PulsarEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|PulsarEndpointBuilder
implements|,
name|AdvancedPulsarEndpointBuilder
block|{
specifier|public
name|PulsarEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"pulsar"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|PulsarEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

