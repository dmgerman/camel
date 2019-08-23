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
comment|/**  * The hazelcast-queue component is used to access Hazelcast distributed queue.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|HazelcastQueueEndpointBuilderFactory
specifier|public
interface|interface
name|HazelcastQueueEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Hazelcast Queue component.      */
DECL|interface|HazelcastQueueEndpointConsumerBuilder
specifier|public
interface|interface
name|HazelcastQueueEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedHazelcastQueueEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedHazelcastQueueEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To specify a default operation to use, if no operation header has          * been provided.          *           * The option is a:          *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>          * type.          *           * Group: common          */
DECL|method|defaultOperation ( HazelcastOperation defaultOperation)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|defaultOperation
parameter_list|(
name|HazelcastOperation
name|defaultOperation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultOperation"
argument_list|,
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To specify a default operation to use, if no operation header has          * been provided.          *           * The option will be converted to a          *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>          * type.          *           * Group: common          */
DECL|method|defaultOperation ( String defaultOperation)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|defaultOperation
parameter_list|(
name|String
name|defaultOperation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultOperation"
argument_list|,
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference which can be used for hazelcast          * endpoint.          *           * The option is a:<code>com.hazelcast.core.HazelcastInstance</code>          * type.          *           * Group: common          */
DECL|method|hazelcastInstance ( Object hazelcastInstance)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|hazelcastInstance
parameter_list|(
name|Object
name|hazelcastInstance
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstance"
argument_list|,
name|hazelcastInstance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference which can be used for hazelcast          * endpoint.          *           * The option will be converted to a          *<code>com.hazelcast.core.HazelcastInstance</code> type.          *           * Group: common          */
DECL|method|hazelcastInstance ( String hazelcastInstance)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|hazelcastInstance
parameter_list|(
name|String
name|hazelcastInstance
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstance"
argument_list|,
name|hazelcastInstance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference name which can be used for hazelcast          * endpoint. If you don't specify the instance reference, camel use the          * default hazelcast instance from the camel-hazelcast instance.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|hazelcastInstanceName ( String hazelcastInstanceName)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|hazelcastInstanceName
parameter_list|(
name|String
name|hazelcastInstanceName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstanceName"
argument_list|,
name|hazelcastInstanceName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
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
name|HazelcastQueueEndpointConsumerBuilder
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
comment|/**          * Define the polling timeout of the Queue consumer in Poll mode.          *           * The option is a:<code>long</code> type.          *           * Group: consumer          */
DECL|method|pollingTimeout ( long pollingTimeout)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|pollingTimeout
parameter_list|(
name|long
name|pollingTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"pollingTimeout"
argument_list|,
name|pollingTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the polling timeout of the Queue consumer in Poll mode.          *           * The option will be converted to a<code>long</code> type.          *           * Group: consumer          */
DECL|method|pollingTimeout ( String pollingTimeout)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|pollingTimeout
parameter_list|(
name|String
name|pollingTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"pollingTimeout"
argument_list|,
name|pollingTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the Pool size for Queue Consumer Executor.          *           * The option is a:<code>int</code> type.          *           * Group: consumer          */
DECL|method|poolSize (int poolSize)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|poolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"poolSize"
argument_list|,
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the Pool size for Queue Consumer Executor.          *           * The option will be converted to a<code>int</code> type.          *           * Group: consumer          */
DECL|method|poolSize (String poolSize)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|poolSize
parameter_list|(
name|String
name|poolSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"poolSize"
argument_list|,
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the Queue Consumer mode: Listen or Poll.          *           * The option is a:          *<code>org.apache.camel.component.hazelcast.queue.HazelcastQueueConsumerMode</code> type.          *           * Group: consumer          */
DECL|method|queueConsumerMode ( HazelcastQueueConsumerMode queueConsumerMode)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|queueConsumerMode
parameter_list|(
name|HazelcastQueueConsumerMode
name|queueConsumerMode
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"queueConsumerMode"
argument_list|,
name|queueConsumerMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the Queue Consumer mode: Listen or Poll.          *           * The option will be converted to a          *<code>org.apache.camel.component.hazelcast.queue.HazelcastQueueConsumerMode</code> type.          *           * Group: consumer          */
DECL|method|queueConsumerMode ( String queueConsumerMode)
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|queueConsumerMode
parameter_list|(
name|String
name|queueConsumerMode
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"queueConsumerMode"
argument_list|,
name|queueConsumerMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the Hazelcast Queue      * component.      */
DECL|interface|AdvancedHazelcastQueueEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedHazelcastQueueEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|HazelcastQueueEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|HazelcastQueueEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedHazelcastQueueEndpointConsumerBuilder
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
name|AdvancedHazelcastQueueEndpointConsumerBuilder
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
name|AdvancedHazelcastQueueEndpointConsumerBuilder
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
name|AdvancedHazelcastQueueEndpointConsumerBuilder
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
name|AdvancedHazelcastQueueEndpointConsumerBuilder
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
name|AdvancedHazelcastQueueEndpointConsumerBuilder
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
name|AdvancedHazelcastQueueEndpointConsumerBuilder
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
name|AdvancedHazelcastQueueEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Hazelcast Queue component.      */
DECL|interface|HazelcastQueueEndpointProducerBuilder
specifier|public
interface|interface
name|HazelcastQueueEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedHazelcastQueueEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedHazelcastQueueEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To specify a default operation to use, if no operation header has          * been provided.          *           * The option is a:          *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>          * type.          *           * Group: common          */
DECL|method|defaultOperation ( HazelcastOperation defaultOperation)
specifier|default
name|HazelcastQueueEndpointProducerBuilder
name|defaultOperation
parameter_list|(
name|HazelcastOperation
name|defaultOperation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultOperation"
argument_list|,
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To specify a default operation to use, if no operation header has          * been provided.          *           * The option will be converted to a          *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>          * type.          *           * Group: common          */
DECL|method|defaultOperation ( String defaultOperation)
specifier|default
name|HazelcastQueueEndpointProducerBuilder
name|defaultOperation
parameter_list|(
name|String
name|defaultOperation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultOperation"
argument_list|,
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference which can be used for hazelcast          * endpoint.          *           * The option is a:<code>com.hazelcast.core.HazelcastInstance</code>          * type.          *           * Group: common          */
DECL|method|hazelcastInstance ( Object hazelcastInstance)
specifier|default
name|HazelcastQueueEndpointProducerBuilder
name|hazelcastInstance
parameter_list|(
name|Object
name|hazelcastInstance
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstance"
argument_list|,
name|hazelcastInstance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference which can be used for hazelcast          * endpoint.          *           * The option will be converted to a          *<code>com.hazelcast.core.HazelcastInstance</code> type.          *           * Group: common          */
DECL|method|hazelcastInstance ( String hazelcastInstance)
specifier|default
name|HazelcastQueueEndpointProducerBuilder
name|hazelcastInstance
parameter_list|(
name|String
name|hazelcastInstance
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstance"
argument_list|,
name|hazelcastInstance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference name which can be used for hazelcast          * endpoint. If you don't specify the instance reference, camel use the          * default hazelcast instance from the camel-hazelcast instance.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|hazelcastInstanceName ( String hazelcastInstanceName)
specifier|default
name|HazelcastQueueEndpointProducerBuilder
name|hazelcastInstanceName
parameter_list|(
name|String
name|hazelcastInstanceName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstanceName"
argument_list|,
name|hazelcastInstanceName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|HazelcastQueueEndpointProducerBuilder
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
name|HazelcastQueueEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Hazelcast Queue      * component.      */
DECL|interface|AdvancedHazelcastQueueEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedHazelcastQueueEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|HazelcastQueueEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|HazelcastQueueEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedHazelcastQueueEndpointProducerBuilder
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
name|AdvancedHazelcastQueueEndpointProducerBuilder
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
name|AdvancedHazelcastQueueEndpointProducerBuilder
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
name|AdvancedHazelcastQueueEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Hazelcast Queue component.      */
DECL|interface|HazelcastQueueEndpointBuilder
specifier|public
interface|interface
name|HazelcastQueueEndpointBuilder
extends|extends
name|HazelcastQueueEndpointConsumerBuilder
extends|,
name|HazelcastQueueEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedHazelcastQueueEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedHazelcastQueueEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To specify a default operation to use, if no operation header has          * been provided.          *           * The option is a:          *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>          * type.          *           * Group: common          */
DECL|method|defaultOperation ( HazelcastOperation defaultOperation)
specifier|default
name|HazelcastQueueEndpointBuilder
name|defaultOperation
parameter_list|(
name|HazelcastOperation
name|defaultOperation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultOperation"
argument_list|,
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To specify a default operation to use, if no operation header has          * been provided.          *           * The option will be converted to a          *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>          * type.          *           * Group: common          */
DECL|method|defaultOperation ( String defaultOperation)
specifier|default
name|HazelcastQueueEndpointBuilder
name|defaultOperation
parameter_list|(
name|String
name|defaultOperation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultOperation"
argument_list|,
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference which can be used for hazelcast          * endpoint.          *           * The option is a:<code>com.hazelcast.core.HazelcastInstance</code>          * type.          *           * Group: common          */
DECL|method|hazelcastInstance ( Object hazelcastInstance)
specifier|default
name|HazelcastQueueEndpointBuilder
name|hazelcastInstance
parameter_list|(
name|Object
name|hazelcastInstance
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstance"
argument_list|,
name|hazelcastInstance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference which can be used for hazelcast          * endpoint.          *           * The option will be converted to a          *<code>com.hazelcast.core.HazelcastInstance</code> type.          *           * Group: common          */
DECL|method|hazelcastInstance ( String hazelcastInstance)
specifier|default
name|HazelcastQueueEndpointBuilder
name|hazelcastInstance
parameter_list|(
name|String
name|hazelcastInstance
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstance"
argument_list|,
name|hazelcastInstance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference name which can be used for hazelcast          * endpoint. If you don't specify the instance reference, camel use the          * default hazelcast instance from the camel-hazelcast instance.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|hazelcastInstanceName ( String hazelcastInstanceName)
specifier|default
name|HazelcastQueueEndpointBuilder
name|hazelcastInstanceName
parameter_list|(
name|String
name|hazelcastInstanceName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstanceName"
argument_list|,
name|hazelcastInstanceName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Hazelcast Queue component.      */
DECL|interface|AdvancedHazelcastQueueEndpointBuilder
specifier|public
interface|interface
name|AdvancedHazelcastQueueEndpointBuilder
extends|extends
name|AdvancedHazelcastQueueEndpointConsumerBuilder
extends|,
name|AdvancedHazelcastQueueEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|HazelcastQueueEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|HazelcastQueueEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedHazelcastQueueEndpointBuilder
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
name|AdvancedHazelcastQueueEndpointBuilder
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
name|AdvancedHazelcastQueueEndpointBuilder
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
name|AdvancedHazelcastQueueEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>      * enum.      */
DECL|enum|HazelcastOperation
enum|enum
name|HazelcastOperation
block|{
DECL|enumConstant|put
name|put
block|,
DECL|enumConstant|delete
name|delete
block|,
DECL|enumConstant|get
name|get
block|,
DECL|enumConstant|update
name|update
block|,
DECL|enumConstant|query
name|query
block|,
DECL|enumConstant|getAll
name|getAll
block|,
DECL|enumConstant|clear
name|clear
block|,
DECL|enumConstant|putIfAbsent
name|putIfAbsent
block|,
DECL|enumConstant|allAll
name|allAll
block|,
DECL|enumConstant|removeAll
name|removeAll
block|,
DECL|enumConstant|retainAll
name|retainAll
block|,
DECL|enumConstant|evict
name|evict
block|,
DECL|enumConstant|evictAll
name|evictAll
block|,
DECL|enumConstant|valueCount
name|valueCount
block|,
DECL|enumConstant|containsKey
name|containsKey
block|,
DECL|enumConstant|containsValue
name|containsValue
block|,
DECL|enumConstant|keySet
name|keySet
block|,
DECL|enumConstant|removevalue
name|removevalue
block|,
DECL|enumConstant|increment
name|increment
block|,
DECL|enumConstant|decrement
name|decrement
block|,
DECL|enumConstant|setvalue
name|setvalue
block|,
DECL|enumConstant|destroy
name|destroy
block|,
DECL|enumConstant|compareAndSet
name|compareAndSet
block|,
DECL|enumConstant|getAndAdd
name|getAndAdd
block|,
DECL|enumConstant|add
name|add
block|,
DECL|enumConstant|offer
name|offer
block|,
DECL|enumConstant|peek
name|peek
block|,
DECL|enumConstant|poll
name|poll
block|,
DECL|enumConstant|remainingCapacity
name|remainingCapacity
block|,
DECL|enumConstant|drainTo
name|drainTo
block|,
DECL|enumConstant|removeIf
name|removeIf
block|,
DECL|enumConstant|take
name|take
block|,
DECL|enumConstant|publish
name|publish
block|,
DECL|enumConstant|readOnceHeal
name|readOnceHeal
block|,
DECL|enumConstant|readOnceTail
name|readOnceTail
block|,
DECL|enumConstant|capacity
name|capacity
block|;     }
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.hazelcast.queue.HazelcastQueueConsumerMode</code> enum.      */
DECL|enum|HazelcastQueueConsumerMode
enum|enum
name|HazelcastQueueConsumerMode
block|{
DECL|enumConstant|listen
name|listen
block|,
DECL|enumConstant|poll
name|poll
block|;     }
comment|/**      * Hazelcast Queue (camel-hazelcast)      * The hazelcast-queue component is used to access Hazelcast distributed      * queue.      *       * Category: cache,datagrid      * Available as of version: 2.7      * Maven coordinates: org.apache.camel:camel-hazelcast      *       * Syntax:<code>hazelcast-queue:cacheName</code>      *       * Path parameter: cacheName (required)      * The name of the cache      */
DECL|method|hazelcastQueue (String path)
specifier|default
name|HazelcastQueueEndpointBuilder
name|hazelcastQueue
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|HazelcastQueueEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|HazelcastQueueEndpointBuilder
implements|,
name|AdvancedHazelcastQueueEndpointBuilder
block|{
specifier|public
name|HazelcastQueueEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"hazelcast-queue"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|HazelcastQueueEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

