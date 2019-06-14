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
comment|/**  * Reactive Camel using reactive streams  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ReactiveStreamsEndpointBuilderFactory
specifier|public
interface|interface
name|ReactiveStreamsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Reactive Streams component.      */
DECL|interface|ReactiveStreamsEndpointConsumerBuilder
specifier|public
interface|interface
name|ReactiveStreamsEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedReactiveStreamsEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Name of the stream channel used by the endpoint to exchange messages.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|stream ( String stream)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|stream
parameter_list|(
name|String
name|stream
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"stream"
argument_list|,
name|stream
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
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
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
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
comment|/**          * Number of threads used to process exchanges in the Camel route.          * The option is a<code>int</code> type.          * @group consumer          */
DECL|method|concurrentConsumers ( int concurrentConsumers)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
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
comment|/**          * Number of threads used to process exchanges in the Camel route.          * The option will be converted to a<code>int</code> type.          * @group consumer          */
DECL|method|concurrentConsumers ( String concurrentConsumers)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
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
comment|/**          * Set the low watermark of requested exchanges to the active          * subscription as percentage of the maxInflightExchanges. When the          * number of pending items from the upstream source is lower than the          * watermark, new items can be requested to the subscription. If set to          * 0, the subscriber will request items in batches of          * maxInflightExchanges, only after all items of the previous batch have          * been processed. If set to 1, the subscriber can request a new item          * each time an exchange is processed (chatty). Any intermediate value          * can be used.          * The option is a<code>double</code> type.          * @group consumer          */
DECL|method|exchangesRefillLowWatermark ( double exchangesRefillLowWatermark)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|exchangesRefillLowWatermark
parameter_list|(
name|double
name|exchangesRefillLowWatermark
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangesRefillLowWatermark"
argument_list|,
name|exchangesRefillLowWatermark
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the low watermark of requested exchanges to the active          * subscription as percentage of the maxInflightExchanges. When the          * number of pending items from the upstream source is lower than the          * watermark, new items can be requested to the subscription. If set to          * 0, the subscriber will request items in batches of          * maxInflightExchanges, only after all items of the previous batch have          * been processed. If set to 1, the subscriber can request a new item          * each time an exchange is processed (chatty). Any intermediate value          * can be used.          * The option will be converted to a<code>double</code> type.          * @group consumer          */
DECL|method|exchangesRefillLowWatermark ( String exchangesRefillLowWatermark)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|exchangesRefillLowWatermark
parameter_list|(
name|String
name|exchangesRefillLowWatermark
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangesRefillLowWatermark"
argument_list|,
name|exchangesRefillLowWatermark
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines if onComplete events should be pushed to the Camel route.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|forwardOnComplete ( boolean forwardOnComplete)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|forwardOnComplete
parameter_list|(
name|boolean
name|forwardOnComplete
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"forwardOnComplete"
argument_list|,
name|forwardOnComplete
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines if onComplete events should be pushed to the Camel route.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|forwardOnComplete ( String forwardOnComplete)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|forwardOnComplete
parameter_list|(
name|String
name|forwardOnComplete
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"forwardOnComplete"
argument_list|,
name|forwardOnComplete
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines if onError events should be pushed to the Camel route.          * Exceptions will be set as message body.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|forwardOnError ( boolean forwardOnError)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|forwardOnError
parameter_list|(
name|boolean
name|forwardOnError
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"forwardOnError"
argument_list|,
name|forwardOnError
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines if onError events should be pushed to the Camel route.          * Exceptions will be set as message body.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|forwardOnError ( String forwardOnError)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|forwardOnError
parameter_list|(
name|String
name|forwardOnError
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"forwardOnError"
argument_list|,
name|forwardOnError
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Maximum number of exchanges concurrently being processed by Camel.          * This parameter controls backpressure on the stream. Setting a          * non-positive value will disable backpressure.          * The option is a<code>java.lang.Integer</code> type.          * @group consumer          */
DECL|method|maxInflightExchanges ( Integer maxInflightExchanges)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|maxInflightExchanges
parameter_list|(
name|Integer
name|maxInflightExchanges
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxInflightExchanges"
argument_list|,
name|maxInflightExchanges
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Maximum number of exchanges concurrently being processed by Camel.          * This parameter controls backpressure on the stream. Setting a          * non-positive value will disable backpressure.          * The option will be converted to a<code>java.lang.Integer</code>          * type.          * @group consumer          */
DECL|method|maxInflightExchanges ( String maxInflightExchanges)
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|maxInflightExchanges
parameter_list|(
name|String
name|maxInflightExchanges
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxInflightExchanges"
argument_list|,
name|maxInflightExchanges
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the Reactive Streams      * component.      */
DECL|interface|AdvancedReactiveStreamsEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedReactiveStreamsEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|ReactiveStreamsEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ReactiveStreamsEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Reactive Streams component.      */
DECL|interface|ReactiveStreamsEndpointProducerBuilder
specifier|public
specifier|static
interface|interface
name|ReactiveStreamsEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedReactiveStreamsEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Name of the stream channel used by the endpoint to exchange messages.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|stream ( String stream)
specifier|public
specifier|default
name|ReactiveStreamsEndpointProducerBuilder
name|stream
parameter_list|(
name|String
name|stream
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"stream"
argument_list|,
name|stream
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The backpressure strategy to use when pushing events to a slow          * subscriber.          * The option is a          *<code>org.apache.camel.component.reactive.streams.ReactiveStreamsBackpressureStrategy</code> type.          * @group producer          */
DECL|method|backpressureStrategy ( ReactiveStreamsBackpressureStrategy backpressureStrategy)
specifier|public
specifier|default
name|ReactiveStreamsEndpointProducerBuilder
name|backpressureStrategy
parameter_list|(
name|ReactiveStreamsBackpressureStrategy
name|backpressureStrategy
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"backpressureStrategy"
argument_list|,
name|backpressureStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The backpressure strategy to use when pushing events to a slow          * subscriber.          * The option will be converted to a          *<code>org.apache.camel.component.reactive.streams.ReactiveStreamsBackpressureStrategy</code> type.          * @group producer          */
DECL|method|backpressureStrategy ( String backpressureStrategy)
specifier|public
specifier|default
name|ReactiveStreamsEndpointProducerBuilder
name|backpressureStrategy
parameter_list|(
name|String
name|backpressureStrategy
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"backpressureStrategy"
argument_list|,
name|backpressureStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|public
specifier|default
name|ReactiveStreamsEndpointProducerBuilder
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
specifier|public
specifier|default
name|ReactiveStreamsEndpointProducerBuilder
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
block|}
comment|/**      * Advanced builder for endpoint producers for the Reactive Streams      * component.      */
DECL|interface|AdvancedReactiveStreamsEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedReactiveStreamsEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|ReactiveStreamsEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ReactiveStreamsEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointProducerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointProducerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointProducerBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Reactive Streams component.      */
DECL|interface|ReactiveStreamsEndpointBuilder
specifier|public
specifier|static
interface|interface
name|ReactiveStreamsEndpointBuilder
extends|extends
name|ReactiveStreamsEndpointConsumerBuilder
extends|,
name|ReactiveStreamsEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedReactiveStreamsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Name of the stream channel used by the endpoint to exchange messages.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|stream (String stream)
specifier|public
specifier|default
name|ReactiveStreamsEndpointBuilder
name|stream
parameter_list|(
name|String
name|stream
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"stream"
argument_list|,
name|stream
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Reactive Streams component.      */
DECL|interface|AdvancedReactiveStreamsEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedReactiveStreamsEndpointBuilder
extends|extends
name|AdvancedReactiveStreamsEndpointConsumerBuilder
extends|,
name|AdvancedReactiveStreamsEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|ReactiveStreamsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ReactiveStreamsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointBuilder
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
specifier|public
specifier|default
name|AdvancedReactiveStreamsEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.reactive.streams.ReactiveStreamsBackpressureStrategy</code> enum.      */
DECL|enum|ReactiveStreamsBackpressureStrategy
specifier|public
specifier|static
enum|enum
name|ReactiveStreamsBackpressureStrategy
block|{
DECL|enumConstant|BUFFER
DECL|enumConstant|OLDEST
DECL|enumConstant|LATEST
name|BUFFER
block|,
name|OLDEST
block|,
name|LATEST
block|;     }
comment|/**      * Reactive Camel using reactive streams Creates a builder to build      * endpoints for the Reactive Streams component.      */
DECL|method|reactiveStreams (String path)
specifier|public
specifier|default
name|ReactiveStreamsEndpointBuilder
name|reactiveStreams
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ReactiveStreamsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ReactiveStreamsEndpointBuilder
implements|,
name|AdvancedReactiveStreamsEndpointBuilder
block|{
specifier|public
name|ReactiveStreamsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"reactive-streams"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ReactiveStreamsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

