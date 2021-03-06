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
name|HeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  * The direct-vm component provides direct, synchronous call to another endpoint  * from any CamelContext in the same JVM.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|DirectVmEndpointBuilderFactory
specifier|public
interface|interface
name|DirectVmEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Direct VM component.      */
DECL|interface|DirectVmEndpointConsumerBuilder
specifier|public
interface|interface
name|DirectVmEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDirectVmEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDirectVmEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|DirectVmEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|doSetProperty
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
name|DirectVmEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|String
name|bridgeErrorHandler
parameter_list|)
block|{
name|doSetProperty
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
block|}
comment|/**      * Advanced builder for endpoint consumers for the Direct VM component.      */
DECL|interface|AdvancedDirectVmEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedDirectVmEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DirectVmEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DirectVmEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedDirectVmEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|String
name|exceptionHandler
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointConsumerBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointConsumerBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Whether to propagate or not properties from the producer side to the          * consumer side, and vice versa. Default value: true.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|propagateProperties ( boolean propagateProperties)
specifier|default
name|AdvancedDirectVmEndpointConsumerBuilder
name|propagateProperties
parameter_list|(
name|boolean
name|propagateProperties
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"propagateProperties"
argument_list|,
name|propagateProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to propagate or not properties from the producer side to the          * consumer side, and vice versa. Default value: true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|propagateProperties ( String propagateProperties)
specifier|default
name|AdvancedDirectVmEndpointConsumerBuilder
name|propagateProperties
parameter_list|(
name|String
name|propagateProperties
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"propagateProperties"
argument_list|,
name|propagateProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedDirectVmEndpointConsumerBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointConsumerBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Builder for endpoint producers for the Direct VM component.      */
DECL|interface|DirectVmEndpointProducerBuilder
specifier|public
interface|interface
name|DirectVmEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDirectVmEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDirectVmEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * If sending a message to a direct endpoint which has no active          * consumer, then we can tell the producer to block and wait for the          * consumer to become active.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|block (boolean block)
specifier|default
name|DirectVmEndpointProducerBuilder
name|block
parameter_list|(
name|boolean
name|block
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"block"
argument_list|,
name|block
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If sending a message to a direct endpoint which has no active          * consumer, then we can tell the producer to block and wait for the          * consumer to become active.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|block (String block)
specifier|default
name|DirectVmEndpointProducerBuilder
name|block
parameter_list|(
name|String
name|block
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"block"
argument_list|,
name|block
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should fail by throwing an exception, when          * sending to a Direct-VM endpoint with no active consumers.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failIfNoConsumers ( boolean failIfNoConsumers)
specifier|default
name|DirectVmEndpointProducerBuilder
name|failIfNoConsumers
parameter_list|(
name|boolean
name|failIfNoConsumers
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"failIfNoConsumers"
argument_list|,
name|failIfNoConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should fail by throwing an exception, when          * sending to a Direct-VM endpoint with no active consumers.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failIfNoConsumers ( String failIfNoConsumers)
specifier|default
name|DirectVmEndpointProducerBuilder
name|failIfNoConsumers
parameter_list|(
name|String
name|failIfNoConsumers
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"failIfNoConsumers"
argument_list|,
name|failIfNoConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|DirectVmEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
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
name|DirectVmEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The timeout value to use if block is enabled.          *           * The option is a:<code>long</code> type.          *           * Group: producer          */
DECL|method|timeout (long timeout)
specifier|default
name|DirectVmEndpointProducerBuilder
name|timeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The timeout value to use if block is enabled.          *           * The option will be converted to a<code>long</code> type.          *           * Group: producer          */
DECL|method|timeout (String timeout)
specifier|default
name|DirectVmEndpointProducerBuilder
name|timeout
parameter_list|(
name|String
name|timeout
parameter_list|)
block|{
name|doSetProperty
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
block|}
comment|/**      * Advanced builder for endpoint producers for the Direct VM component.      */
DECL|interface|AdvancedDirectVmEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedDirectVmEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DirectVmEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DirectVmEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Sets a HeaderFilterStrategy that will only be applied on producer          * endpoints (on both directions: request and response). Default value:          * none.          *           * The option is a:          *<code>org.apache.camel.spi.HeaderFilterStrategy</code> type.          *           * Group: producer (advanced)          */
DECL|method|headerFilterStrategy ( HeaderFilterStrategy headerFilterStrategy)
specifier|default
name|AdvancedDirectVmEndpointProducerBuilder
name|headerFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"headerFilterStrategy"
argument_list|,
name|headerFilterStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets a HeaderFilterStrategy that will only be applied on producer          * endpoints (on both directions: request and response). Default value:          * none.          *           * The option will be converted to a          *<code>org.apache.camel.spi.HeaderFilterStrategy</code> type.          *           * Group: producer (advanced)          */
DECL|method|headerFilterStrategy ( String headerFilterStrategy)
specifier|default
name|AdvancedDirectVmEndpointProducerBuilder
name|headerFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"headerFilterStrategy"
argument_list|,
name|headerFilterStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedDirectVmEndpointProducerBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointProducerBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Whether to propagate or not properties from the producer side to the          * consumer side, and vice versa. Default value: true.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|propagateProperties ( boolean propagateProperties)
specifier|default
name|AdvancedDirectVmEndpointProducerBuilder
name|propagateProperties
parameter_list|(
name|boolean
name|propagateProperties
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"propagateProperties"
argument_list|,
name|propagateProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to propagate or not properties from the producer side to the          * consumer side, and vice versa. Default value: true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|propagateProperties ( String propagateProperties)
specifier|default
name|AdvancedDirectVmEndpointProducerBuilder
name|propagateProperties
parameter_list|(
name|String
name|propagateProperties
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"propagateProperties"
argument_list|,
name|propagateProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedDirectVmEndpointProducerBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointProducerBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Builder for endpoint for the Direct VM component.      */
DECL|interface|DirectVmEndpointBuilder
specifier|public
interface|interface
name|DirectVmEndpointBuilder
extends|extends
name|DirectVmEndpointConsumerBuilder
extends|,
name|DirectVmEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDirectVmEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDirectVmEndpointBuilder
operator|)
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Direct VM component.      */
DECL|interface|AdvancedDirectVmEndpointBuilder
specifier|public
interface|interface
name|AdvancedDirectVmEndpointBuilder
extends|extends
name|AdvancedDirectVmEndpointConsumerBuilder
extends|,
name|AdvancedDirectVmEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DirectVmEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DirectVmEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedDirectVmEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Whether to propagate or not properties from the producer side to the          * consumer side, and vice versa. Default value: true.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|propagateProperties ( boolean propagateProperties)
specifier|default
name|AdvancedDirectVmEndpointBuilder
name|propagateProperties
parameter_list|(
name|boolean
name|propagateProperties
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"propagateProperties"
argument_list|,
name|propagateProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to propagate or not properties from the producer side to the          * consumer side, and vice versa. Default value: true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|propagateProperties ( String propagateProperties)
specifier|default
name|AdvancedDirectVmEndpointBuilder
name|propagateProperties
parameter_list|(
name|String
name|propagateProperties
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"propagateProperties"
argument_list|,
name|propagateProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedDirectVmEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedDirectVmEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Direct VM (camel-directvm)      * The direct-vm component provides direct, synchronous call to another      * endpoint from any CamelContext in the same JVM.      *       * Category: core,endpoint      * Since: 2.10      * Maven coordinates: org.apache.camel:camel-directvm      *       * Syntax:<code>direct-vm:name</code>      *       * Path parameter: name (required)      * Name of direct-vm endpoint      */
DECL|method|directVm (String path)
specifier|default
name|DirectVmEndpointBuilder
name|directVm
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|DirectVmEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|DirectVmEndpointBuilder
implements|,
name|AdvancedDirectVmEndpointBuilder
block|{
specifier|public
name|DirectVmEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"direct-vm"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|DirectVmEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

