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
comment|/**  * Bridges Camel with Spring Integration.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SpringIntegrationEndpointBuilderFactory
specifier|public
interface|interface
name|SpringIntegrationEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Spring Integration component.      */
DECL|interface|SpringIntegrationEndpointConsumerBuilder
specifier|public
interface|interface
name|SpringIntegrationEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSpringIntegrationEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSpringIntegrationEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The default channel name which is used by the Spring Integration          * Spring context. It will equal to the inputChannel name for the Spring          * Integration consumer and the outputChannel name for the Spring          * Integration provider.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|defaultChannel ( String defaultChannel)
specifier|default
name|SpringIntegrationEndpointConsumerBuilder
name|defaultChannel
parameter_list|(
name|String
name|defaultChannel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultChannel"
argument_list|,
name|defaultChannel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The exchange pattern that the Spring integration endpoint should use.          * If inOut=true then a reply channel is expected, either from the          * Spring Integration Message header or configured on the endpoint.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|inOut (boolean inOut)
specifier|default
name|SpringIntegrationEndpointConsumerBuilder
name|inOut
parameter_list|(
name|boolean
name|inOut
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inOut"
argument_list|,
name|inOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The exchange pattern that the Spring integration endpoint should use.          * If inOut=true then a reply channel is expected, either from the          * Spring Integration Message header or configured on the endpoint.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|inOut (String inOut)
specifier|default
name|SpringIntegrationEndpointConsumerBuilder
name|inOut
parameter_list|(
name|String
name|inOut
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inOut"
argument_list|,
name|inOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|SpringIntegrationEndpointConsumerBuilder
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
name|SpringIntegrationEndpointConsumerBuilder
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
comment|/**          * The Spring integration input channel name that this endpoint wants to          * consume from Spring integration.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|inputChannel ( String inputChannel)
specifier|default
name|SpringIntegrationEndpointConsumerBuilder
name|inputChannel
parameter_list|(
name|String
name|inputChannel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inputChannel"
argument_list|,
name|inputChannel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the Spring Integration      * component.      */
DECL|interface|AdvancedSpringIntegrationEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedSpringIntegrationEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SpringIntegrationEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SpringIntegrationEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedSpringIntegrationEndpointConsumerBuilder
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
name|AdvancedSpringIntegrationEndpointConsumerBuilder
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
name|AdvancedSpringIntegrationEndpointConsumerBuilder
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
name|AdvancedSpringIntegrationEndpointConsumerBuilder
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
name|AdvancedSpringIntegrationEndpointConsumerBuilder
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
name|AdvancedSpringIntegrationEndpointConsumerBuilder
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
name|AdvancedSpringIntegrationEndpointConsumerBuilder
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
name|AdvancedSpringIntegrationEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Spring Integration component.      */
DECL|interface|SpringIntegrationEndpointProducerBuilder
specifier|public
interface|interface
name|SpringIntegrationEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSpringIntegrationEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSpringIntegrationEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The default channel name which is used by the Spring Integration          * Spring context. It will equal to the inputChannel name for the Spring          * Integration consumer and the outputChannel name for the Spring          * Integration provider.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|defaultChannel ( String defaultChannel)
specifier|default
name|SpringIntegrationEndpointProducerBuilder
name|defaultChannel
parameter_list|(
name|String
name|defaultChannel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultChannel"
argument_list|,
name|defaultChannel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The exchange pattern that the Spring integration endpoint should use.          * If inOut=true then a reply channel is expected, either from the          * Spring Integration Message header or configured on the endpoint.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|inOut (boolean inOut)
specifier|default
name|SpringIntegrationEndpointProducerBuilder
name|inOut
parameter_list|(
name|boolean
name|inOut
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inOut"
argument_list|,
name|inOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The exchange pattern that the Spring integration endpoint should use.          * If inOut=true then a reply channel is expected, either from the          * Spring Integration Message header or configured on the endpoint.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|inOut (String inOut)
specifier|default
name|SpringIntegrationEndpointProducerBuilder
name|inOut
parameter_list|(
name|String
name|inOut
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inOut"
argument_list|,
name|inOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|SpringIntegrationEndpointProducerBuilder
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
name|SpringIntegrationEndpointProducerBuilder
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
comment|/**          * The Spring integration output channel name that is used to send          * messages to Spring integration.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|outputChannel ( String outputChannel)
specifier|default
name|SpringIntegrationEndpointProducerBuilder
name|outputChannel
parameter_list|(
name|String
name|outputChannel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"outputChannel"
argument_list|,
name|outputChannel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the Spring Integration      * component.      */
DECL|interface|AdvancedSpringIntegrationEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedSpringIntegrationEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SpringIntegrationEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SpringIntegrationEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSpringIntegrationEndpointProducerBuilder
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
name|AdvancedSpringIntegrationEndpointProducerBuilder
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
name|AdvancedSpringIntegrationEndpointProducerBuilder
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
name|AdvancedSpringIntegrationEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Spring Integration component.      */
DECL|interface|SpringIntegrationEndpointBuilder
specifier|public
interface|interface
name|SpringIntegrationEndpointBuilder
extends|extends
name|SpringIntegrationEndpointConsumerBuilder
extends|,
name|SpringIntegrationEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSpringIntegrationEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSpringIntegrationEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The default channel name which is used by the Spring Integration          * Spring context. It will equal to the inputChannel name for the Spring          * Integration consumer and the outputChannel name for the Spring          * Integration provider.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|defaultChannel ( String defaultChannel)
specifier|default
name|SpringIntegrationEndpointBuilder
name|defaultChannel
parameter_list|(
name|String
name|defaultChannel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultChannel"
argument_list|,
name|defaultChannel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The exchange pattern that the Spring integration endpoint should use.          * If inOut=true then a reply channel is expected, either from the          * Spring Integration Message header or configured on the endpoint.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|inOut (boolean inOut)
specifier|default
name|SpringIntegrationEndpointBuilder
name|inOut
parameter_list|(
name|boolean
name|inOut
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inOut"
argument_list|,
name|inOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The exchange pattern that the Spring integration endpoint should use.          * If inOut=true then a reply channel is expected, either from the          * Spring Integration Message header or configured on the endpoint.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|inOut (String inOut)
specifier|default
name|SpringIntegrationEndpointBuilder
name|inOut
parameter_list|(
name|String
name|inOut
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inOut"
argument_list|,
name|inOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Spring Integration component.      */
DECL|interface|AdvancedSpringIntegrationEndpointBuilder
specifier|public
interface|interface
name|AdvancedSpringIntegrationEndpointBuilder
extends|extends
name|AdvancedSpringIntegrationEndpointConsumerBuilder
extends|,
name|AdvancedSpringIntegrationEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SpringIntegrationEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SpringIntegrationEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSpringIntegrationEndpointBuilder
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
name|AdvancedSpringIntegrationEndpointBuilder
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
name|AdvancedSpringIntegrationEndpointBuilder
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
name|AdvancedSpringIntegrationEndpointBuilder
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
comment|/**      * Bridges Camel with Spring Integration. Creates a builder to build      * endpoints for the Spring Integration component.      */
DECL|method|springIntegration (String path)
specifier|default
name|SpringIntegrationEndpointBuilder
name|springIntegration
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SpringIntegrationEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SpringIntegrationEndpointBuilder
implements|,
name|AdvancedSpringIntegrationEndpointBuilder
block|{
specifier|public
name|SpringIntegrationEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"spring-integration"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SpringIntegrationEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

