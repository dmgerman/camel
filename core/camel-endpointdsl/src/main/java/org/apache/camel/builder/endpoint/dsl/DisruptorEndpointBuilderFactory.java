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
name|WaitForTaskToComplete
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
comment|/**  * The disruptor component provides asynchronous SEDA behavior using LMAX  * Disruptor.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|DisruptorEndpointBuilderFactory
specifier|public
interface|interface
name|DisruptorEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Disruptor component.      */
DECL|interface|DisruptorEndpointConsumerBuilder
specifier|public
interface|interface
name|DisruptorEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDisruptorEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The maximum capacity of the Disruptors ringbuffer Will be effectively          * increased to the nearest power of two. Notice: Mind if you use this          * option, then its the first endpoint being created with the queue          * name, that determines the size. To make sure all endpoints use same          * size, then configure the size option on all of them, or the first          * endpoint being created.          *           * The option is a:<code>int</code> type.          *           * Group: common          */
DECL|method|size (int size)
specifier|default
name|DisruptorEndpointConsumerBuilder
name|size
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"size"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The maximum capacity of the Disruptors ringbuffer Will be effectively          * increased to the nearest power of two. Notice: Mind if you use this          * option, then its the first endpoint being created with the queue          * name, that determines the size. To make sure all endpoints use same          * size, then configure the size option on all of them, or the first          * endpoint being created.          *           * The option will be converted to a<code>int</code> type.          *           * Group: common          */
DECL|method|size (String size)
specifier|default
name|DisruptorEndpointConsumerBuilder
name|size
parameter_list|(
name|String
name|size
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"size"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|DisruptorEndpointConsumerBuilder
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
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( String bridgeErrorHandler)
specifier|default
name|DisruptorEndpointConsumerBuilder
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
comment|/**          * Number of concurrent threads processing exchanges.          *           * The option is a:<code>int</code> type.          *           * Group: consumer          */
DECL|method|concurrentConsumers ( int concurrentConsumers)
specifier|default
name|DisruptorEndpointConsumerBuilder
name|concurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"concurrentConsumers"
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Number of concurrent threads processing exchanges.          *           * The option will be converted to a<code>int</code> type.          *           * Group: consumer          */
DECL|method|concurrentConsumers ( String concurrentConsumers)
specifier|default
name|DisruptorEndpointConsumerBuilder
name|concurrentConsumers
parameter_list|(
name|String
name|concurrentConsumers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"concurrentConsumers"
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies whether multiple consumers are allowed. If enabled, you can          * use Disruptor for Publish-Subscribe messaging. That is, you can send          * a message to the queue and have each consumer receive a copy of the          * message. When enabled, this option should be specified on every          * consumer endpoint.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|multipleConsumers ( boolean multipleConsumers)
specifier|default
name|DisruptorEndpointConsumerBuilder
name|multipleConsumers
parameter_list|(
name|boolean
name|multipleConsumers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"multipleConsumers"
argument_list|,
name|multipleConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies whether multiple consumers are allowed. If enabled, you can          * use Disruptor for Publish-Subscribe messaging. That is, you can send          * a message to the queue and have each consumer receive a copy of the          * message. When enabled, this option should be specified on every          * consumer endpoint.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|multipleConsumers ( String multipleConsumers)
specifier|default
name|DisruptorEndpointConsumerBuilder
name|multipleConsumers
parameter_list|(
name|String
name|multipleConsumers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"multipleConsumers"
argument_list|,
name|multipleConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Defines the strategy used by consumer threads to wait on new          * exchanges to be published. The options allowed are:Blocking,          * Sleeping, BusySpin and Yielding.          *           * The option is a:          *<code>org.apache.camel.component.disruptor.DisruptorWaitStrategy</code> type.          *           * Group: consumer          */
DECL|method|waitStrategy ( DisruptorWaitStrategy waitStrategy)
specifier|default
name|DisruptorEndpointConsumerBuilder
name|waitStrategy
parameter_list|(
name|DisruptorWaitStrategy
name|waitStrategy
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"waitStrategy"
argument_list|,
name|waitStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Defines the strategy used by consumer threads to wait on new          * exchanges to be published. The options allowed are:Blocking,          * Sleeping, BusySpin and Yielding.          *           * The option will be converted to a          *<code>org.apache.camel.component.disruptor.DisruptorWaitStrategy</code> type.          *           * Group: consumer          */
DECL|method|waitStrategy ( String waitStrategy)
specifier|default
name|DisruptorEndpointConsumerBuilder
name|waitStrategy
parameter_list|(
name|String
name|waitStrategy
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"waitStrategy"
argument_list|,
name|waitStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the Disruptor component.      */
DECL|interface|AdvancedDisruptorEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedDisruptorEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DisruptorEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DisruptorEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
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
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option will be converted to a          *<code>org.apache.camel.spi.ExceptionHandler</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( String exceptionHandler)
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
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
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option is a:<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( ExchangePattern exchangePattern)
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
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
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option will be converted to a          *<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( String exchangePattern)
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedDisruptorEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Disruptor component.      */
DECL|interface|DisruptorEndpointProducerBuilder
specifier|public
interface|interface
name|DisruptorEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDisruptorEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDisruptorEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The maximum capacity of the Disruptors ringbuffer Will be effectively          * increased to the nearest power of two. Notice: Mind if you use this          * option, then its the first endpoint being created with the queue          * name, that determines the size. To make sure all endpoints use same          * size, then configure the size option on all of them, or the first          * endpoint being created.          *           * The option is a:<code>int</code> type.          *           * Group: common          */
DECL|method|size (int size)
specifier|default
name|DisruptorEndpointProducerBuilder
name|size
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"size"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The maximum capacity of the Disruptors ringbuffer Will be effectively          * increased to the nearest power of two. Notice: Mind if you use this          * option, then its the first endpoint being created with the queue          * name, that determines the size. To make sure all endpoints use same          * size, then configure the size option on all of them, or the first          * endpoint being created.          *           * The option will be converted to a<code>int</code> type.          *           * Group: common          */
DECL|method|size (String size)
specifier|default
name|DisruptorEndpointProducerBuilder
name|size
parameter_list|(
name|String
name|size
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"size"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether a thread that sends messages to a full Disruptor will block          * until the ringbuffer's capacity is no longer exhausted. By default,          * the calling thread will block and wait until the message can be          * accepted. By disabling this option, an exception will be thrown          * stating that the queue is full.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|blockWhenFull ( boolean blockWhenFull)
specifier|default
name|DisruptorEndpointProducerBuilder
name|blockWhenFull
parameter_list|(
name|boolean
name|blockWhenFull
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"blockWhenFull"
argument_list|,
name|blockWhenFull
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether a thread that sends messages to a full Disruptor will block          * until the ringbuffer's capacity is no longer exhausted. By default,          * the calling thread will block and wait until the message can be          * accepted. By disabling this option, an exception will be thrown          * stating that the queue is full.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|blockWhenFull ( String blockWhenFull)
specifier|default
name|DisruptorEndpointProducerBuilder
name|blockWhenFull
parameter_list|(
name|String
name|blockWhenFull
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"blockWhenFull"
argument_list|,
name|blockWhenFull
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|DisruptorEndpointProducerBuilder
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
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|default
name|DisruptorEndpointProducerBuilder
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
comment|/**          * Defines the producers allowed on the Disruptor. The options allowed          * are: Multi to allow multiple producers and Single to enable certain          * optimizations only allowed when one concurrent producer (on one          * thread or otherwise synchronized) is active.          *           * The option is a:          *<code>org.apache.camel.component.disruptor.DisruptorProducerType</code> type.          *           * Group: producer          */
DECL|method|producerType ( DisruptorProducerType producerType)
specifier|default
name|DisruptorEndpointProducerBuilder
name|producerType
parameter_list|(
name|DisruptorProducerType
name|producerType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"producerType"
argument_list|,
name|producerType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Defines the producers allowed on the Disruptor. The options allowed          * are: Multi to allow multiple producers and Single to enable certain          * optimizations only allowed when one concurrent producer (on one          * thread or otherwise synchronized) is active.          *           * The option will be converted to a          *<code>org.apache.camel.component.disruptor.DisruptorProducerType</code> type.          *           * Group: producer          */
DECL|method|producerType ( String producerType)
specifier|default
name|DisruptorEndpointProducerBuilder
name|producerType
parameter_list|(
name|String
name|producerType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"producerType"
argument_list|,
name|producerType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Timeout (in milliseconds) before a producer will stop waiting for an          * asynchronous task to complete. You can disable timeout by using 0 or          * a negative value.          *           * The option is a:<code>long</code> type.          *           * Group: producer          */
DECL|method|timeout (long timeout)
specifier|default
name|DisruptorEndpointProducerBuilder
name|timeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Timeout (in milliseconds) before a producer will stop waiting for an          * asynchronous task to complete. You can disable timeout by using 0 or          * a negative value.          *           * The option will be converted to a<code>long</code> type.          *           * Group: producer          */
DECL|method|timeout (String timeout)
specifier|default
name|DisruptorEndpointProducerBuilder
name|timeout
parameter_list|(
name|String
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Option to specify whether the caller should wait for the async task          * to complete or not before continuing. The following three options are          * supported: Always, Never or IfReplyExpected. The first two values are          * self-explanatory. The last value, IfReplyExpected, will only wait if          * the message is Request Reply based.          *           * The option is a:<code>org.apache.camel.WaitForTaskToComplete</code>          * type.          *           * Group: producer          */
DECL|method|waitForTaskToComplete ( WaitForTaskToComplete waitForTaskToComplete)
specifier|default
name|DisruptorEndpointProducerBuilder
name|waitForTaskToComplete
parameter_list|(
name|WaitForTaskToComplete
name|waitForTaskToComplete
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"waitForTaskToComplete"
argument_list|,
name|waitForTaskToComplete
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Option to specify whether the caller should wait for the async task          * to complete or not before continuing. The following three options are          * supported: Always, Never or IfReplyExpected. The first two values are          * self-explanatory. The last value, IfReplyExpected, will only wait if          * the message is Request Reply based.          *           * The option will be converted to a          *<code>org.apache.camel.WaitForTaskToComplete</code> type.          *           * Group: producer          */
DECL|method|waitForTaskToComplete ( String waitForTaskToComplete)
specifier|default
name|DisruptorEndpointProducerBuilder
name|waitForTaskToComplete
parameter_list|(
name|String
name|waitForTaskToComplete
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"waitForTaskToComplete"
argument_list|,
name|waitForTaskToComplete
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the Disruptor component.      */
DECL|interface|AdvancedDisruptorEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedDisruptorEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DisruptorEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DisruptorEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedDisruptorEndpointProducerBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedDisruptorEndpointProducerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedDisruptorEndpointProducerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedDisruptorEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Disruptor component.      */
DECL|interface|DisruptorEndpointBuilder
specifier|public
interface|interface
name|DisruptorEndpointBuilder
extends|extends
name|DisruptorEndpointConsumerBuilder
extends|,
name|DisruptorEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDisruptorEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDisruptorEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The maximum capacity of the Disruptors ringbuffer Will be effectively          * increased to the nearest power of two. Notice: Mind if you use this          * option, then its the first endpoint being created with the queue          * name, that determines the size. To make sure all endpoints use same          * size, then configure the size option on all of them, or the first          * endpoint being created.          *           * The option is a:<code>int</code> type.          *           * Group: common          */
DECL|method|size (int size)
specifier|default
name|DisruptorEndpointBuilder
name|size
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"size"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The maximum capacity of the Disruptors ringbuffer Will be effectively          * increased to the nearest power of two. Notice: Mind if you use this          * option, then its the first endpoint being created with the queue          * name, that determines the size. To make sure all endpoints use same          * size, then configure the size option on all of them, or the first          * endpoint being created.          *           * The option will be converted to a<code>int</code> type.          *           * Group: common          */
DECL|method|size (String size)
specifier|default
name|DisruptorEndpointBuilder
name|size
parameter_list|(
name|String
name|size
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"size"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Disruptor component.      */
DECL|interface|AdvancedDisruptorEndpointBuilder
specifier|public
interface|interface
name|AdvancedDisruptorEndpointBuilder
extends|extends
name|AdvancedDisruptorEndpointConsumerBuilder
extends|,
name|AdvancedDisruptorEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DisruptorEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DisruptorEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedDisruptorEndpointBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedDisruptorEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedDisruptorEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedDisruptorEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.disruptor.DisruptorWaitStrategy</code>      * enum.      */
DECL|enum|DisruptorWaitStrategy
enum|enum
name|DisruptorWaitStrategy
block|{
DECL|enumConstant|Blocking
name|Blocking
block|,
DECL|enumConstant|Sleeping
name|Sleeping
block|,
DECL|enumConstant|BusySpin
name|BusySpin
block|,
DECL|enumConstant|Yielding
name|Yielding
block|;     }
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.disruptor.DisruptorProducerType</code>      * enum.      */
DECL|enum|DisruptorProducerType
enum|enum
name|DisruptorProducerType
block|{
DECL|enumConstant|Single
name|Single
block|,
DECL|enumConstant|Multi
name|Multi
block|;     }
comment|/**      * Disruptor (camel-disruptor)      * The disruptor component provides asynchronous SEDA behavior using LMAX      * Disruptor.      *       * Syntax:<code>disruptor:name</code>      * Category: endpoint      * Available as of version: 2.12      * Maven coordinates: org.apache.camel:camel-disruptor      */
DECL|method|disruptor (String path)
specifier|default
name|DisruptorEndpointBuilder
name|disruptor
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|DisruptorEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|DisruptorEndpointBuilder
implements|,
name|AdvancedDisruptorEndpointBuilder
block|{
specifier|public
name|DisruptorEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"disruptor"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|DisruptorEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

