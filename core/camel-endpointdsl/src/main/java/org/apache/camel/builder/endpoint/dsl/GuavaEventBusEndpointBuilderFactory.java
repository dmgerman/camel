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
comment|/**  * The guava-eventbus component provides integration bridge between Camel and  * Google Guava EventBus.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|GuavaEventBusEndpointBuilderFactory
specifier|public
interface|interface
name|GuavaEventBusEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Guava EventBus component.      */
DECL|interface|GuavaEventBusEndpointConsumerBuilder
specifier|public
interface|interface
name|GuavaEventBusEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGuavaEventBusEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGuavaEventBusEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To lookup the Guava EventBus from the registry with the given name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|eventBusRef ( String eventBusRef)
specifier|default
name|GuavaEventBusEndpointConsumerBuilder
name|eventBusRef
parameter_list|(
name|String
name|eventBusRef
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventBusRef"
argument_list|,
name|eventBusRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If used on the consumer side of the route, will filter events          * received from the EventBus to the instances of the class and          * superclasses of eventClass. Null value of this option is equal to          * setting it to the java.lang.Object i.e. the consumer will capture all          * messages incoming to the event bus. This option cannot be used          * together with listenerInterface option.          * The option is a<code>java.lang.Class&lt;java.lang.Object&gt;</code>          * type.          * @group common          */
DECL|method|eventClass ( Class<Object> eventClass)
specifier|default
name|GuavaEventBusEndpointConsumerBuilder
name|eventClass
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|eventClass
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventClass"
argument_list|,
name|eventClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If used on the consumer side of the route, will filter events          * received from the EventBus to the instances of the class and          * superclasses of eventClass. Null value of this option is equal to          * setting it to the java.lang.Object i.e. the consumer will capture all          * messages incoming to the event bus. This option cannot be used          * together with listenerInterface option.          * The option will be converted to a          *<code>java.lang.Class&lt;java.lang.Object&gt;</code> type.          * @group common          */
DECL|method|eventClass ( String eventClass)
specifier|default
name|GuavaEventBusEndpointConsumerBuilder
name|eventClass
parameter_list|(
name|String
name|eventClass
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventClass"
argument_list|,
name|eventClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The interface with method(s) marked with the Subscribe annotation.          * Dynamic proxy will be created over the interface so it could be          * registered as the EventBus listener. Particularly useful when          * creating multi-event listeners and for handling DeadEvent properly.          * This option cannot be used together with eventClass option.          * The option is a<code>java.lang.Class&lt;java.lang.Object&gt;</code>          * type.          * @group common          */
DECL|method|listenerInterface ( Class<Object> listenerInterface)
specifier|default
name|GuavaEventBusEndpointConsumerBuilder
name|listenerInterface
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|listenerInterface
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listenerInterface"
argument_list|,
name|listenerInterface
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The interface with method(s) marked with the Subscribe annotation.          * Dynamic proxy will be created over the interface so it could be          * registered as the EventBus listener. Particularly useful when          * creating multi-event listeners and for handling DeadEvent properly.          * This option cannot be used together with eventClass option.          * The option will be converted to a          *<code>java.lang.Class&lt;java.lang.Object&gt;</code> type.          * @group common          */
DECL|method|listenerInterface ( String listenerInterface)
specifier|default
name|GuavaEventBusEndpointConsumerBuilder
name|listenerInterface
parameter_list|(
name|String
name|listenerInterface
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listenerInterface"
argument_list|,
name|listenerInterface
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|GuavaEventBusEndpointConsumerBuilder
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
name|GuavaEventBusEndpointConsumerBuilder
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
block|}
comment|/**      * Advanced builder for endpoint consumers for the Guava EventBus component.      */
DECL|interface|AdvancedGuavaEventBusEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedGuavaEventBusEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GuavaEventBusEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GuavaEventBusEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedGuavaEventBusEndpointConsumerBuilder
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
name|AdvancedGuavaEventBusEndpointConsumerBuilder
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
name|AdvancedGuavaEventBusEndpointConsumerBuilder
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
name|AdvancedGuavaEventBusEndpointConsumerBuilder
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
name|AdvancedGuavaEventBusEndpointConsumerBuilder
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
name|AdvancedGuavaEventBusEndpointConsumerBuilder
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
name|AdvancedGuavaEventBusEndpointConsumerBuilder
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
name|AdvancedGuavaEventBusEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Guava EventBus component.      */
DECL|interface|GuavaEventBusEndpointProducerBuilder
specifier|public
specifier|static
interface|interface
name|GuavaEventBusEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGuavaEventBusEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGuavaEventBusEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To lookup the Guava EventBus from the registry with the given name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|eventBusRef ( String eventBusRef)
specifier|default
name|GuavaEventBusEndpointProducerBuilder
name|eventBusRef
parameter_list|(
name|String
name|eventBusRef
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventBusRef"
argument_list|,
name|eventBusRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If used on the consumer side of the route, will filter events          * received from the EventBus to the instances of the class and          * superclasses of eventClass. Null value of this option is equal to          * setting it to the java.lang.Object i.e. the consumer will capture all          * messages incoming to the event bus. This option cannot be used          * together with listenerInterface option.          * The option is a<code>java.lang.Class&lt;java.lang.Object&gt;</code>          * type.          * @group common          */
DECL|method|eventClass ( Class<Object> eventClass)
specifier|default
name|GuavaEventBusEndpointProducerBuilder
name|eventClass
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|eventClass
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventClass"
argument_list|,
name|eventClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If used on the consumer side of the route, will filter events          * received from the EventBus to the instances of the class and          * superclasses of eventClass. Null value of this option is equal to          * setting it to the java.lang.Object i.e. the consumer will capture all          * messages incoming to the event bus. This option cannot be used          * together with listenerInterface option.          * The option will be converted to a          *<code>java.lang.Class&lt;java.lang.Object&gt;</code> type.          * @group common          */
DECL|method|eventClass ( String eventClass)
specifier|default
name|GuavaEventBusEndpointProducerBuilder
name|eventClass
parameter_list|(
name|String
name|eventClass
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventClass"
argument_list|,
name|eventClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The interface with method(s) marked with the Subscribe annotation.          * Dynamic proxy will be created over the interface so it could be          * registered as the EventBus listener. Particularly useful when          * creating multi-event listeners and for handling DeadEvent properly.          * This option cannot be used together with eventClass option.          * The option is a<code>java.lang.Class&lt;java.lang.Object&gt;</code>          * type.          * @group common          */
DECL|method|listenerInterface ( Class<Object> listenerInterface)
specifier|default
name|GuavaEventBusEndpointProducerBuilder
name|listenerInterface
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|listenerInterface
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listenerInterface"
argument_list|,
name|listenerInterface
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The interface with method(s) marked with the Subscribe annotation.          * Dynamic proxy will be created over the interface so it could be          * registered as the EventBus listener. Particularly useful when          * creating multi-event listeners and for handling DeadEvent properly.          * This option cannot be used together with eventClass option.          * The option will be converted to a          *<code>java.lang.Class&lt;java.lang.Object&gt;</code> type.          * @group common          */
DECL|method|listenerInterface ( String listenerInterface)
specifier|default
name|GuavaEventBusEndpointProducerBuilder
name|listenerInterface
parameter_list|(
name|String
name|listenerInterface
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listenerInterface"
argument_list|,
name|listenerInterface
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|GuavaEventBusEndpointProducerBuilder
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
name|GuavaEventBusEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Guava EventBus component.      */
DECL|interface|AdvancedGuavaEventBusEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedGuavaEventBusEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GuavaEventBusEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GuavaEventBusEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGuavaEventBusEndpointProducerBuilder
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
name|AdvancedGuavaEventBusEndpointProducerBuilder
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
name|AdvancedGuavaEventBusEndpointProducerBuilder
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
name|AdvancedGuavaEventBusEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Guava EventBus component.      */
DECL|interface|GuavaEventBusEndpointBuilder
specifier|public
specifier|static
interface|interface
name|GuavaEventBusEndpointBuilder
extends|extends
name|GuavaEventBusEndpointConsumerBuilder
extends|,
name|GuavaEventBusEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGuavaEventBusEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGuavaEventBusEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To lookup the Guava EventBus from the registry with the given name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|eventBusRef (String eventBusRef)
specifier|default
name|GuavaEventBusEndpointBuilder
name|eventBusRef
parameter_list|(
name|String
name|eventBusRef
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventBusRef"
argument_list|,
name|eventBusRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If used on the consumer side of the route, will filter events          * received from the EventBus to the instances of the class and          * superclasses of eventClass. Null value of this option is equal to          * setting it to the java.lang.Object i.e. the consumer will capture all          * messages incoming to the event bus. This option cannot be used          * together with listenerInterface option.          * The option is a<code>java.lang.Class&lt;java.lang.Object&gt;</code>          * type.          * @group common          */
DECL|method|eventClass (Class<Object> eventClass)
specifier|default
name|GuavaEventBusEndpointBuilder
name|eventClass
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|eventClass
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventClass"
argument_list|,
name|eventClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If used on the consumer side of the route, will filter events          * received from the EventBus to the instances of the class and          * superclasses of eventClass. Null value of this option is equal to          * setting it to the java.lang.Object i.e. the consumer will capture all          * messages incoming to the event bus. This option cannot be used          * together with listenerInterface option.          * The option will be converted to a          *<code>java.lang.Class&lt;java.lang.Object&gt;</code> type.          * @group common          */
DECL|method|eventClass (String eventClass)
specifier|default
name|GuavaEventBusEndpointBuilder
name|eventClass
parameter_list|(
name|String
name|eventClass
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"eventClass"
argument_list|,
name|eventClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The interface with method(s) marked with the Subscribe annotation.          * Dynamic proxy will be created over the interface so it could be          * registered as the EventBus listener. Particularly useful when          * creating multi-event listeners and for handling DeadEvent properly.          * This option cannot be used together with eventClass option.          * The option is a<code>java.lang.Class&lt;java.lang.Object&gt;</code>          * type.          * @group common          */
DECL|method|listenerInterface ( Class<Object> listenerInterface)
specifier|default
name|GuavaEventBusEndpointBuilder
name|listenerInterface
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|listenerInterface
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listenerInterface"
argument_list|,
name|listenerInterface
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The interface with method(s) marked with the Subscribe annotation.          * Dynamic proxy will be created over the interface so it could be          * registered as the EventBus listener. Particularly useful when          * creating multi-event listeners and for handling DeadEvent properly.          * This option cannot be used together with eventClass option.          * The option will be converted to a          *<code>java.lang.Class&lt;java.lang.Object&gt;</code> type.          * @group common          */
DECL|method|listenerInterface ( String listenerInterface)
specifier|default
name|GuavaEventBusEndpointBuilder
name|listenerInterface
parameter_list|(
name|String
name|listenerInterface
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listenerInterface"
argument_list|,
name|listenerInterface
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Guava EventBus component.      */
DECL|interface|AdvancedGuavaEventBusEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedGuavaEventBusEndpointBuilder
extends|extends
name|AdvancedGuavaEventBusEndpointConsumerBuilder
extends|,
name|AdvancedGuavaEventBusEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GuavaEventBusEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GuavaEventBusEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGuavaEventBusEndpointBuilder
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
name|AdvancedGuavaEventBusEndpointBuilder
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
name|AdvancedGuavaEventBusEndpointBuilder
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
name|AdvancedGuavaEventBusEndpointBuilder
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
comment|/**      * The guava-eventbus component provides integration bridge between Camel      * and Google Guava EventBus. Creates a builder to build endpoints for the      * Guava EventBus component.      */
DECL|method|guavaEventBus (String path)
specifier|default
name|GuavaEventBusEndpointBuilder
name|guavaEventBus
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|GuavaEventBusEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|GuavaEventBusEndpointBuilder
implements|,
name|AdvancedGuavaEventBusEndpointBuilder
block|{
specifier|public
name|GuavaEventBusEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"guava-eventbus"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GuavaEventBusEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

