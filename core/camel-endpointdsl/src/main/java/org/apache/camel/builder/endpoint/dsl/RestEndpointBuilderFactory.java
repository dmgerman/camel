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
comment|/**  * The rest component is used for either hosting REST services (consumer) or  * calling external REST services (producer).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|RestEndpointBuilderFactory
specifier|public
interface|interface
name|RestEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the REST component.      */
DECL|interface|RestEndpointConsumerBuilder
specifier|public
interface|interface
name|RestEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedRestEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedRestEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Media type such as: 'text/xml', or 'application/json' this REST          * service accepts. By default we accept all kinds of types.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|consumes (String consumes)
specifier|default
name|RestEndpointConsumerBuilder
name|consumes
parameter_list|(
name|String
name|consumes
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"consumes"
argument_list|,
name|consumes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To declare the incoming POJO binding type as a FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|inType (String inType)
specifier|default
name|RestEndpointConsumerBuilder
name|inType
parameter_list|(
name|String
name|inType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"inType"
argument_list|,
name|inType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To declare the outgoing POJO binding type as a FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|outType (String outType)
specifier|default
name|RestEndpointConsumerBuilder
name|outType
parameter_list|(
name|String
name|outType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"outType"
argument_list|,
name|outType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Media type such as: 'text/xml', or 'application/json' this REST          * service returns.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|produces (String produces)
specifier|default
name|RestEndpointConsumerBuilder
name|produces
parameter_list|(
name|String
name|produces
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"produces"
argument_list|,
name|produces
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of the route this REST services creates.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|routeId (String routeId)
specifier|default
name|RestEndpointConsumerBuilder
name|routeId
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"routeId"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|RestEndpointConsumerBuilder
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
name|RestEndpointConsumerBuilder
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
comment|/**          * The Camel Rest component to use for (consumer) the REST transport,          * such as jetty, servlet, undertow. If no component has been explicit          * configured, then Camel will lookup if there is a Camel component that          * integrates with the Rest DSL, or if a          * org.apache.camel.spi.RestConsumerFactory is registered in the          * registry. If either one is found, then that is being used.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|consumerComponentName ( String consumerComponentName)
specifier|default
name|RestEndpointConsumerBuilder
name|consumerComponentName
parameter_list|(
name|String
name|consumerComponentName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"consumerComponentName"
argument_list|,
name|consumerComponentName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Human description to document this REST service.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|description (String description)
specifier|default
name|RestEndpointConsumerBuilder
name|description
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the REST component.      */
DECL|interface|AdvancedRestEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedRestEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|RestEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|RestEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedRestEndpointConsumerBuilder
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
name|AdvancedRestEndpointConsumerBuilder
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
name|AdvancedRestEndpointConsumerBuilder
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
name|AdvancedRestEndpointConsumerBuilder
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
name|AdvancedRestEndpointConsumerBuilder
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
name|AdvancedRestEndpointConsumerBuilder
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
name|AdvancedRestEndpointConsumerBuilder
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
name|AdvancedRestEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the REST component.      */
DECL|interface|RestEndpointProducerBuilder
specifier|public
interface|interface
name|RestEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedRestEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedRestEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Media type such as: 'text/xml', or 'application/json' this REST          * service accepts. By default we accept all kinds of types.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|consumes (String consumes)
specifier|default
name|RestEndpointProducerBuilder
name|consumes
parameter_list|(
name|String
name|consumes
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"consumes"
argument_list|,
name|consumes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To declare the incoming POJO binding type as a FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|inType (String inType)
specifier|default
name|RestEndpointProducerBuilder
name|inType
parameter_list|(
name|String
name|inType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"inType"
argument_list|,
name|inType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To declare the outgoing POJO binding type as a FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|outType (String outType)
specifier|default
name|RestEndpointProducerBuilder
name|outType
parameter_list|(
name|String
name|outType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"outType"
argument_list|,
name|outType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Media type such as: 'text/xml', or 'application/json' this REST          * service returns.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|produces (String produces)
specifier|default
name|RestEndpointProducerBuilder
name|produces
parameter_list|(
name|String
name|produces
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"produces"
argument_list|,
name|produces
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of the route this REST services creates.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|routeId (String routeId)
specifier|default
name|RestEndpointProducerBuilder
name|routeId
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"routeId"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The swagger api doc resource to use. The resource is loaded from          * classpath by default and must be in JSon format.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|apiDoc (String apiDoc)
specifier|default
name|RestEndpointProducerBuilder
name|apiDoc
parameter_list|(
name|String
name|apiDoc
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"apiDoc"
argument_list|,
name|apiDoc
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Configures the binding mode for the producer. If set to anything          * other than 'off' the producer will try to convert the body of the          * incoming message from inType to the json or xml, and the response          * from json or xml to outType.          *           * The option is a:          *<code>org.apache.camel.spi.RestConfiguration$RestBindingMode</code>          * type.          *           * Group: producer          */
DECL|method|bindingMode ( RestBindingMode bindingMode)
specifier|default
name|RestEndpointProducerBuilder
name|bindingMode
parameter_list|(
name|RestBindingMode
name|bindingMode
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"bindingMode"
argument_list|,
name|bindingMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Configures the binding mode for the producer. If set to anything          * other than 'off' the producer will try to convert the body of the          * incoming message from inType to the json or xml, and the response          * from json or xml to outType.          *           * The option will be converted to a          *<code>org.apache.camel.spi.RestConfiguration$RestBindingMode</code>          * type.          *           * Group: producer          */
DECL|method|bindingMode (String bindingMode)
specifier|default
name|RestEndpointProducerBuilder
name|bindingMode
parameter_list|(
name|String
name|bindingMode
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"bindingMode"
argument_list|,
name|bindingMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Host and port of HTTP service to use (override host in swagger          * schema).          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|host (String host)
specifier|default
name|RestEndpointProducerBuilder
name|host
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"host"
argument_list|,
name|host
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|RestEndpointProducerBuilder
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
name|RestEndpointProducerBuilder
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
comment|/**          * The Camel Rest component to use for (producer) the REST transport,          * such as http, undertow. If no component has been explicit configured,          * then Camel will lookup if there is a Camel component that integrates          * with the Rest DSL, or if a org.apache.camel.spi.RestProducerFactory          * is registered in the registry. If either one is found, then that is          * being used.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|producerComponentName ( String producerComponentName)
specifier|default
name|RestEndpointProducerBuilder
name|producerComponentName
parameter_list|(
name|String
name|producerComponentName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"producerComponentName"
argument_list|,
name|producerComponentName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Query parameters for the HTTP service to call.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|queryParameters ( String queryParameters)
specifier|default
name|RestEndpointProducerBuilder
name|queryParameters
parameter_list|(
name|String
name|queryParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"queryParameters"
argument_list|,
name|queryParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the REST component.      */
DECL|interface|AdvancedRestEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedRestEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|RestEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|RestEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedRestEndpointProducerBuilder
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
name|AdvancedRestEndpointProducerBuilder
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
name|AdvancedRestEndpointProducerBuilder
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
name|AdvancedRestEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the REST component.      */
DECL|interface|RestEndpointBuilder
specifier|public
interface|interface
name|RestEndpointBuilder
extends|extends
name|RestEndpointConsumerBuilder
extends|,
name|RestEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedRestEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedRestEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Media type such as: 'text/xml', or 'application/json' this REST          * service accepts. By default we accept all kinds of types.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|consumes (String consumes)
specifier|default
name|RestEndpointBuilder
name|consumes
parameter_list|(
name|String
name|consumes
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"consumes"
argument_list|,
name|consumes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To declare the incoming POJO binding type as a FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|inType (String inType)
specifier|default
name|RestEndpointBuilder
name|inType
parameter_list|(
name|String
name|inType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"inType"
argument_list|,
name|inType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To declare the outgoing POJO binding type as a FQN class name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|outType (String outType)
specifier|default
name|RestEndpointBuilder
name|outType
parameter_list|(
name|String
name|outType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"outType"
argument_list|,
name|outType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Media type such as: 'text/xml', or 'application/json' this REST          * service returns.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|produces (String produces)
specifier|default
name|RestEndpointBuilder
name|produces
parameter_list|(
name|String
name|produces
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"produces"
argument_list|,
name|produces
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of the route this REST services creates.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|routeId (String routeId)
specifier|default
name|RestEndpointBuilder
name|routeId
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"routeId"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the REST component.      */
DECL|interface|AdvancedRestEndpointBuilder
specifier|public
interface|interface
name|AdvancedRestEndpointBuilder
extends|extends
name|AdvancedRestEndpointConsumerBuilder
extends|,
name|AdvancedRestEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|RestEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|RestEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedRestEndpointBuilder
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
name|AdvancedRestEndpointBuilder
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
name|AdvancedRestEndpointBuilder
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
name|AdvancedRestEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.spi.RestConfiguration$RestBindingMode</code> enum.      */
DECL|enum|RestBindingMode
enum|enum
name|RestBindingMode
block|{
DECL|enumConstant|auto
name|auto
block|,
DECL|enumConstant|off
name|off
block|,
DECL|enumConstant|json
name|json
block|,
DECL|enumConstant|xml
name|xml
block|,
DECL|enumConstant|json_xml
name|json_xml
block|;     }
comment|/**      * REST (camel-rest)      * The rest component is used for either hosting REST services (consumer) or      * calling external REST services (producer).      *       * Category: core,rest      * Since: 2.14      * Maven coordinates: org.apache.camel:camel-rest      *       * Syntax:<code>rest:method:path:uriTemplate</code>      *       * Path parameter: method (required)      * HTTP method to use.      * The value can be one of: get, post, put, delete, patch, head, trace,      * connect, options      *       * Path parameter: path (required)      * The base path      *       * Path parameter: uriTemplate      * The uri template      */
DECL|method|restEndpoint (String path)
specifier|default
name|RestEndpointBuilder
name|restEndpoint
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|RestEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|RestEndpointBuilder
implements|,
name|AdvancedRestEndpointBuilder
block|{
specifier|public
name|RestEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"rest"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|RestEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

