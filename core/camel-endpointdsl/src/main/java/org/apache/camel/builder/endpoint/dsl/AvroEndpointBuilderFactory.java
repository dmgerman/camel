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
comment|/**  * Working with Apache Avro for data serialization.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|AvroEndpointBuilderFactory
specifier|public
interface|interface
name|AvroEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Avro component.      */
DECL|interface|AvroEndpointConsumerBuilder
specifier|public
interface|interface
name|AvroEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAvroEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAvroEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Avro protocol to use.          *           * The option is a:<code>org.apache.avro.Protocol</code> type.          *           * Group: common          */
DECL|method|protocol (Object protocol)
specifier|default
name|AvroEndpointConsumerBuilder
name|protocol
parameter_list|(
name|Object
name|protocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol to use.          *           * The option will be converted to a          *<code>org.apache.avro.Protocol</code> type.          *           * Group: common          */
DECL|method|protocol (String protocol)
specifier|default
name|AvroEndpointConsumerBuilder
name|protocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol to use defined by the FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|protocolClassName ( String protocolClassName)
specifier|default
name|AvroEndpointConsumerBuilder
name|protocolClassName
parameter_list|(
name|String
name|protocolClassName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocolClassName"
argument_list|,
name|protocolClassName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol location.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|protocolLocation ( String protocolLocation)
specifier|default
name|AvroEndpointConsumerBuilder
name|protocolLocation
parameter_list|(
name|String
name|protocolLocation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocolLocation"
argument_list|,
name|protocolLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If protocol object provided is reflection protocol. Should be used          * only with protocol parameter because for protocolClassName protocol          * type will be auto detected.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|reflectionProtocol ( boolean reflectionProtocol)
specifier|default
name|AvroEndpointConsumerBuilder
name|reflectionProtocol
parameter_list|(
name|boolean
name|reflectionProtocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"reflectionProtocol"
argument_list|,
name|reflectionProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If protocol object provided is reflection protocol. Should be used          * only with protocol parameter because for protocolClassName protocol          * type will be auto detected.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|reflectionProtocol ( String reflectionProtocol)
specifier|default
name|AvroEndpointConsumerBuilder
name|reflectionProtocol
parameter_list|(
name|String
name|reflectionProtocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"reflectionProtocol"
argument_list|,
name|reflectionProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, consumer parameter won't be wrapped into array. Will fail if          * protocol specifies more then 1 parameter for the message.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|singleParameter ( boolean singleParameter)
specifier|default
name|AvroEndpointConsumerBuilder
name|singleParameter
parameter_list|(
name|boolean
name|singleParameter
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"singleParameter"
argument_list|,
name|singleParameter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, consumer parameter won't be wrapped into array. Will fail if          * protocol specifies more then 1 parameter for the message.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|singleParameter ( String singleParameter)
specifier|default
name|AvroEndpointConsumerBuilder
name|singleParameter
parameter_list|(
name|String
name|singleParameter
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"singleParameter"
argument_list|,
name|singleParameter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Authority to use (username and password).          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|uriAuthority (String uriAuthority)
specifier|default
name|AvroEndpointConsumerBuilder
name|uriAuthority
parameter_list|(
name|String
name|uriAuthority
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"uriAuthority"
argument_list|,
name|uriAuthority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|AvroEndpointConsumerBuilder
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
name|AvroEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the Avro component.      */
DECL|interface|AdvancedAvroEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedAvroEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AvroEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AvroEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedAvroEndpointConsumerBuilder
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
name|AdvancedAvroEndpointConsumerBuilder
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
name|AdvancedAvroEndpointConsumerBuilder
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
name|AdvancedAvroEndpointConsumerBuilder
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
name|AdvancedAvroEndpointConsumerBuilder
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
name|AdvancedAvroEndpointConsumerBuilder
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
name|AdvancedAvroEndpointConsumerBuilder
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
name|AdvancedAvroEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Avro component.      */
DECL|interface|AvroEndpointProducerBuilder
specifier|public
interface|interface
name|AvroEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAvroEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAvroEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Avro protocol to use.          *           * The option is a:<code>org.apache.avro.Protocol</code> type.          *           * Group: common          */
DECL|method|protocol (Object protocol)
specifier|default
name|AvroEndpointProducerBuilder
name|protocol
parameter_list|(
name|Object
name|protocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol to use.          *           * The option will be converted to a          *<code>org.apache.avro.Protocol</code> type.          *           * Group: common          */
DECL|method|protocol (String protocol)
specifier|default
name|AvroEndpointProducerBuilder
name|protocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol to use defined by the FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|protocolClassName ( String protocolClassName)
specifier|default
name|AvroEndpointProducerBuilder
name|protocolClassName
parameter_list|(
name|String
name|protocolClassName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocolClassName"
argument_list|,
name|protocolClassName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol location.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|protocolLocation ( String protocolLocation)
specifier|default
name|AvroEndpointProducerBuilder
name|protocolLocation
parameter_list|(
name|String
name|protocolLocation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocolLocation"
argument_list|,
name|protocolLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If protocol object provided is reflection protocol. Should be used          * only with protocol parameter because for protocolClassName protocol          * type will be auto detected.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|reflectionProtocol ( boolean reflectionProtocol)
specifier|default
name|AvroEndpointProducerBuilder
name|reflectionProtocol
parameter_list|(
name|boolean
name|reflectionProtocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"reflectionProtocol"
argument_list|,
name|reflectionProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If protocol object provided is reflection protocol. Should be used          * only with protocol parameter because for protocolClassName protocol          * type will be auto detected.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|reflectionProtocol ( String reflectionProtocol)
specifier|default
name|AvroEndpointProducerBuilder
name|reflectionProtocol
parameter_list|(
name|String
name|reflectionProtocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"reflectionProtocol"
argument_list|,
name|reflectionProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, consumer parameter won't be wrapped into array. Will fail if          * protocol specifies more then 1 parameter for the message.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|singleParameter ( boolean singleParameter)
specifier|default
name|AvroEndpointProducerBuilder
name|singleParameter
parameter_list|(
name|boolean
name|singleParameter
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"singleParameter"
argument_list|,
name|singleParameter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, consumer parameter won't be wrapped into array. Will fail if          * protocol specifies more then 1 parameter for the message.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|singleParameter ( String singleParameter)
specifier|default
name|AvroEndpointProducerBuilder
name|singleParameter
parameter_list|(
name|String
name|singleParameter
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"singleParameter"
argument_list|,
name|singleParameter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Authority to use (username and password).          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|uriAuthority (String uriAuthority)
specifier|default
name|AvroEndpointProducerBuilder
name|uriAuthority
parameter_list|(
name|String
name|uriAuthority
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"uriAuthority"
argument_list|,
name|uriAuthority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|AvroEndpointProducerBuilder
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
name|AvroEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Avro component.      */
DECL|interface|AdvancedAvroEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedAvroEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AvroEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AvroEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedAvroEndpointProducerBuilder
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
name|AdvancedAvroEndpointProducerBuilder
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
name|AdvancedAvroEndpointProducerBuilder
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
name|AdvancedAvroEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Avro component.      */
DECL|interface|AvroEndpointBuilder
specifier|public
interface|interface
name|AvroEndpointBuilder
extends|extends
name|AvroEndpointConsumerBuilder
extends|,
name|AvroEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAvroEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAvroEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Avro protocol to use.          *           * The option is a:<code>org.apache.avro.Protocol</code> type.          *           * Group: common          */
DECL|method|protocol (Object protocol)
specifier|default
name|AvroEndpointBuilder
name|protocol
parameter_list|(
name|Object
name|protocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol to use.          *           * The option will be converted to a          *<code>org.apache.avro.Protocol</code> type.          *           * Group: common          */
DECL|method|protocol (String protocol)
specifier|default
name|AvroEndpointBuilder
name|protocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol to use defined by the FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|protocolClassName (String protocolClassName)
specifier|default
name|AvroEndpointBuilder
name|protocolClassName
parameter_list|(
name|String
name|protocolClassName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocolClassName"
argument_list|,
name|protocolClassName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Avro protocol location.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|protocolLocation (String protocolLocation)
specifier|default
name|AvroEndpointBuilder
name|protocolLocation
parameter_list|(
name|String
name|protocolLocation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"protocolLocation"
argument_list|,
name|protocolLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If protocol object provided is reflection protocol. Should be used          * only with protocol parameter because for protocolClassName protocol          * type will be auto detected.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|reflectionProtocol ( boolean reflectionProtocol)
specifier|default
name|AvroEndpointBuilder
name|reflectionProtocol
parameter_list|(
name|boolean
name|reflectionProtocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"reflectionProtocol"
argument_list|,
name|reflectionProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If protocol object provided is reflection protocol. Should be used          * only with protocol parameter because for protocolClassName protocol          * type will be auto detected.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|reflectionProtocol (String reflectionProtocol)
specifier|default
name|AvroEndpointBuilder
name|reflectionProtocol
parameter_list|(
name|String
name|reflectionProtocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"reflectionProtocol"
argument_list|,
name|reflectionProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, consumer parameter won't be wrapped into array. Will fail if          * protocol specifies more then 1 parameter for the message.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|singleParameter (boolean singleParameter)
specifier|default
name|AvroEndpointBuilder
name|singleParameter
parameter_list|(
name|boolean
name|singleParameter
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"singleParameter"
argument_list|,
name|singleParameter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, consumer parameter won't be wrapped into array. Will fail if          * protocol specifies more then 1 parameter for the message.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|singleParameter (String singleParameter)
specifier|default
name|AvroEndpointBuilder
name|singleParameter
parameter_list|(
name|String
name|singleParameter
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"singleParameter"
argument_list|,
name|singleParameter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Authority to use (username and password).          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|uriAuthority (String uriAuthority)
specifier|default
name|AvroEndpointBuilder
name|uriAuthority
parameter_list|(
name|String
name|uriAuthority
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"uriAuthority"
argument_list|,
name|uriAuthority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Avro component.      */
DECL|interface|AdvancedAvroEndpointBuilder
specifier|public
interface|interface
name|AdvancedAvroEndpointBuilder
extends|extends
name|AdvancedAvroEndpointConsumerBuilder
extends|,
name|AdvancedAvroEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AvroEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AvroEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedAvroEndpointBuilder
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
name|AdvancedAvroEndpointBuilder
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
name|AdvancedAvroEndpointBuilder
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
name|AdvancedAvroEndpointBuilder
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
comment|/**      * Avro (camel-avro)      * Working with Apache Avro for data serialization.      *       * Category: messaging,transformation      * Since: 2.10      * Maven coordinates: org.apache.camel:camel-avro      *       * Syntax:<code>avro:transport:host:port/messageName</code>      *       * Path parameter: transport (required)      * Transport to use, can be either http or netty      * The value can be one of: http, netty      *       * Path parameter: port (required)      * Port number to use      *       * Path parameter: host (required)      * Hostname to use      *       * Path parameter: messageName      * The name of the message to send.      */
DECL|method|avro (String path)
specifier|default
name|AvroEndpointBuilder
name|avro
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|AvroEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|AvroEndpointBuilder
implements|,
name|AdvancedAvroEndpointBuilder
block|{
specifier|public
name|AvroEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"avro"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|AvroEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

