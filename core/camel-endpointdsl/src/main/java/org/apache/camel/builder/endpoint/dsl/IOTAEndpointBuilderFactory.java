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
comment|/**  * Component for integrate IOTA DLT  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|IOTAEndpointBuilderFactory
specifier|public
interface|interface
name|IOTAEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the IOTA component.      */
DECL|interface|IOTAEndpointConsumerBuilder
specifier|public
interface|interface
name|IOTAEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedIOTAEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedIOTAEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The depth determines how deep the tangle is analysed for getting          * Tips.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|depth (Integer depth)
specifier|default
name|IOTAEndpointConsumerBuilder
name|depth
parameter_list|(
name|Integer
name|depth
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"depth"
argument_list|,
name|depth
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The depth determines how deep the tangle is analysed for getting          * Tips.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|depth (String depth)
specifier|default
name|IOTAEndpointConsumerBuilder
name|depth
parameter_list|(
name|String
name|depth
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"depth"
argument_list|,
name|depth
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The minWeightMagnitude is the minimum number of zeroes that a          * proof-of-work output/transaction hash must end with to be considered          * valid by full nodes.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|minWeightMagnitude ( Integer minWeightMagnitude)
specifier|default
name|IOTAEndpointConsumerBuilder
name|minWeightMagnitude
parameter_list|(
name|Integer
name|minWeightMagnitude
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"minWeightMagnitude"
argument_list|,
name|minWeightMagnitude
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The minWeightMagnitude is the minimum number of zeroes that a          * proof-of-work output/transaction hash must end with to be considered          * valid by full nodes.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|minWeightMagnitude ( String minWeightMagnitude)
specifier|default
name|IOTAEndpointConsumerBuilder
name|minWeightMagnitude
parameter_list|(
name|String
name|minWeightMagnitude
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"minWeightMagnitude"
argument_list|,
name|minWeightMagnitude
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Supported operations are 'sendTransfer', 'getNewAddress'.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|operation (String operation)
specifier|default
name|IOTAEndpointConsumerBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Address security level.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|securityLevel (Integer securityLevel)
specifier|default
name|IOTAEndpointConsumerBuilder
name|securityLevel
parameter_list|(
name|Integer
name|securityLevel
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"securityLevel"
argument_list|,
name|securityLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Address security level.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|securityLevel (String securityLevel)
specifier|default
name|IOTAEndpointConsumerBuilder
name|securityLevel
parameter_list|(
name|String
name|securityLevel
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"securityLevel"
argument_list|,
name|securityLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * TAG.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|tag (String tag)
specifier|default
name|IOTAEndpointConsumerBuilder
name|tag
parameter_list|(
name|String
name|tag
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"tag"
argument_list|,
name|tag
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Node url.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|url (String url)
specifier|default
name|IOTAEndpointConsumerBuilder
name|url
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|IOTAEndpointConsumerBuilder
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
name|IOTAEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the IOTA component.      */
DECL|interface|AdvancedIOTAEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedIOTAEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|IOTAEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|IOTAEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedIOTAEndpointConsumerBuilder
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
name|AdvancedIOTAEndpointConsumerBuilder
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
name|AdvancedIOTAEndpointConsumerBuilder
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
name|AdvancedIOTAEndpointConsumerBuilder
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
name|AdvancedIOTAEndpointConsumerBuilder
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
name|AdvancedIOTAEndpointConsumerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedIOTAEndpointConsumerBuilder
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
name|AdvancedIOTAEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the IOTA component.      */
DECL|interface|IOTAEndpointProducerBuilder
specifier|public
interface|interface
name|IOTAEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedIOTAEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedIOTAEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The depth determines how deep the tangle is analysed for getting          * Tips.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|depth (Integer depth)
specifier|default
name|IOTAEndpointProducerBuilder
name|depth
parameter_list|(
name|Integer
name|depth
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"depth"
argument_list|,
name|depth
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The depth determines how deep the tangle is analysed for getting          * Tips.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|depth (String depth)
specifier|default
name|IOTAEndpointProducerBuilder
name|depth
parameter_list|(
name|String
name|depth
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"depth"
argument_list|,
name|depth
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The minWeightMagnitude is the minimum number of zeroes that a          * proof-of-work output/transaction hash must end with to be considered          * valid by full nodes.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|minWeightMagnitude ( Integer minWeightMagnitude)
specifier|default
name|IOTAEndpointProducerBuilder
name|minWeightMagnitude
parameter_list|(
name|Integer
name|minWeightMagnitude
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"minWeightMagnitude"
argument_list|,
name|minWeightMagnitude
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The minWeightMagnitude is the minimum number of zeroes that a          * proof-of-work output/transaction hash must end with to be considered          * valid by full nodes.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|minWeightMagnitude ( String minWeightMagnitude)
specifier|default
name|IOTAEndpointProducerBuilder
name|minWeightMagnitude
parameter_list|(
name|String
name|minWeightMagnitude
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"minWeightMagnitude"
argument_list|,
name|minWeightMagnitude
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Supported operations are 'sendTransfer', 'getNewAddress'.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|operation (String operation)
specifier|default
name|IOTAEndpointProducerBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Address security level.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|securityLevel (Integer securityLevel)
specifier|default
name|IOTAEndpointProducerBuilder
name|securityLevel
parameter_list|(
name|Integer
name|securityLevel
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"securityLevel"
argument_list|,
name|securityLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Address security level.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|securityLevel (String securityLevel)
specifier|default
name|IOTAEndpointProducerBuilder
name|securityLevel
parameter_list|(
name|String
name|securityLevel
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"securityLevel"
argument_list|,
name|securityLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * TAG.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|tag (String tag)
specifier|default
name|IOTAEndpointProducerBuilder
name|tag
parameter_list|(
name|String
name|tag
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"tag"
argument_list|,
name|tag
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Node url.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|url (String url)
specifier|default
name|IOTAEndpointProducerBuilder
name|url
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|IOTAEndpointProducerBuilder
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
name|IOTAEndpointProducerBuilder
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
block|}
comment|/**      * Advanced builder for endpoint producers for the IOTA component.      */
DECL|interface|AdvancedIOTAEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedIOTAEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|IOTAEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|IOTAEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedIOTAEndpointProducerBuilder
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
name|AdvancedIOTAEndpointProducerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedIOTAEndpointProducerBuilder
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
name|AdvancedIOTAEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the IOTA component.      */
DECL|interface|IOTAEndpointBuilder
specifier|public
interface|interface
name|IOTAEndpointBuilder
extends|extends
name|IOTAEndpointConsumerBuilder
extends|,
name|IOTAEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedIOTAEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedIOTAEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The depth determines how deep the tangle is analysed for getting          * Tips.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|depth (Integer depth)
specifier|default
name|IOTAEndpointBuilder
name|depth
parameter_list|(
name|Integer
name|depth
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"depth"
argument_list|,
name|depth
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The depth determines how deep the tangle is analysed for getting          * Tips.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|depth (String depth)
specifier|default
name|IOTAEndpointBuilder
name|depth
parameter_list|(
name|String
name|depth
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"depth"
argument_list|,
name|depth
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The minWeightMagnitude is the minimum number of zeroes that a          * proof-of-work output/transaction hash must end with to be considered          * valid by full nodes.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|minWeightMagnitude ( Integer minWeightMagnitude)
specifier|default
name|IOTAEndpointBuilder
name|minWeightMagnitude
parameter_list|(
name|Integer
name|minWeightMagnitude
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"minWeightMagnitude"
argument_list|,
name|minWeightMagnitude
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The minWeightMagnitude is the minimum number of zeroes that a          * proof-of-work output/transaction hash must end with to be considered          * valid by full nodes.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|minWeightMagnitude (String minWeightMagnitude)
specifier|default
name|IOTAEndpointBuilder
name|minWeightMagnitude
parameter_list|(
name|String
name|minWeightMagnitude
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"minWeightMagnitude"
argument_list|,
name|minWeightMagnitude
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Supported operations are 'sendTransfer', 'getNewAddress'.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|operation (String operation)
specifier|default
name|IOTAEndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Address security level.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: common          */
DECL|method|securityLevel (Integer securityLevel)
specifier|default
name|IOTAEndpointBuilder
name|securityLevel
parameter_list|(
name|Integer
name|securityLevel
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"securityLevel"
argument_list|,
name|securityLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Address security level.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: common          */
DECL|method|securityLevel (String securityLevel)
specifier|default
name|IOTAEndpointBuilder
name|securityLevel
parameter_list|(
name|String
name|securityLevel
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"securityLevel"
argument_list|,
name|securityLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * TAG.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|tag (String tag)
specifier|default
name|IOTAEndpointBuilder
name|tag
parameter_list|(
name|String
name|tag
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"tag"
argument_list|,
name|tag
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Node url.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|url (String url)
specifier|default
name|IOTAEndpointBuilder
name|url
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the IOTA component.      */
DECL|interface|AdvancedIOTAEndpointBuilder
specifier|public
interface|interface
name|AdvancedIOTAEndpointBuilder
extends|extends
name|AdvancedIOTAEndpointConsumerBuilder
extends|,
name|AdvancedIOTAEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|IOTAEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|IOTAEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedIOTAEndpointBuilder
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
name|AdvancedIOTAEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedIOTAEndpointBuilder
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
name|AdvancedIOTAEndpointBuilder
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
comment|/**      * IOTA (camel-iota)      * Component for integrate IOTA DLT      *       * Category: dlt      * Available as of version: 2.23      * Maven coordinates: org.apache.camel:camel-iota      *       * Syntax:<code>iota:name</code>      *       * Path parameter: name (required)      * Component name      */
DECL|method|iota (String path)
specifier|default
name|IOTAEndpointBuilder
name|iota
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|IOTAEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|IOTAEndpointBuilder
implements|,
name|AdvancedIOTAEndpointBuilder
block|{
specifier|public
name|IOTAEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"iota"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|IOTAEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

