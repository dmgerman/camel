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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
comment|/**  * The properties component is used for using property placeholders in endpoint  * uris.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|PropertiesEndpointBuilderFactory
specifier|public
interface|interface
name|PropertiesEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Properties component.      */
DECL|interface|PropertiesEndpointConsumerBuilder
specifier|public
interface|interface
name|PropertiesEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedPropertiesEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedPropertiesEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Property key to use as placeholder.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|key (String key)
specifier|default
name|PropertiesEndpointConsumerBuilder
name|key
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to silently ignore if a location cannot be located, such as a          * properties file not found.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|ignoreMissingLocation ( boolean ignoreMissingLocation)
specifier|default
name|PropertiesEndpointConsumerBuilder
name|ignoreMissingLocation
parameter_list|(
name|boolean
name|ignoreMissingLocation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ignoreMissingLocation"
argument_list|,
name|ignoreMissingLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to silently ignore if a location cannot be located, such as a          * properties file not found.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|ignoreMissingLocation ( String ignoreMissingLocation)
specifier|default
name|PropertiesEndpointConsumerBuilder
name|ignoreMissingLocation
parameter_list|(
name|String
name|ignoreMissingLocation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ignoreMissingLocation"
argument_list|,
name|ignoreMissingLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A list of locations to load properties. You can use comma to separate          * multiple locations. This option will override any default locations          * and only use the locations from this option.          * The option is a          *<code>java.util.List&lt;org.apache.camel.component.properties.PropertiesLocation&gt;</code> type.          * @group common          */
DECL|method|locations ( List<Object> locations)
specifier|default
name|PropertiesEndpointConsumerBuilder
name|locations
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|locations
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"locations"
argument_list|,
name|locations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A list of locations to load properties. You can use comma to separate          * multiple locations. This option will override any default locations          * and only use the locations from this option.          * The option will be converted to a          *<code>java.util.List&lt;org.apache.camel.component.properties.PropertiesLocation&gt;</code> type.          * @group common          */
DECL|method|locations (String locations)
specifier|default
name|PropertiesEndpointConsumerBuilder
name|locations
parameter_list|(
name|String
name|locations
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"locations"
argument_list|,
name|locations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|PropertiesEndpointConsumerBuilder
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
name|PropertiesEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the Properties component.      */
DECL|interface|AdvancedPropertiesEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedPropertiesEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|PropertiesEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|PropertiesEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedPropertiesEndpointConsumerBuilder
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
name|AdvancedPropertiesEndpointConsumerBuilder
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
name|AdvancedPropertiesEndpointConsumerBuilder
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
name|AdvancedPropertiesEndpointConsumerBuilder
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
name|AdvancedPropertiesEndpointConsumerBuilder
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
name|AdvancedPropertiesEndpointConsumerBuilder
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
name|AdvancedPropertiesEndpointConsumerBuilder
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
name|AdvancedPropertiesEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Properties component.      */
DECL|interface|PropertiesEndpointProducerBuilder
specifier|public
interface|interface
name|PropertiesEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedPropertiesEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedPropertiesEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Property key to use as placeholder.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|key (String key)
specifier|default
name|PropertiesEndpointProducerBuilder
name|key
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to silently ignore if a location cannot be located, such as a          * properties file not found.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|ignoreMissingLocation ( boolean ignoreMissingLocation)
specifier|default
name|PropertiesEndpointProducerBuilder
name|ignoreMissingLocation
parameter_list|(
name|boolean
name|ignoreMissingLocation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ignoreMissingLocation"
argument_list|,
name|ignoreMissingLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to silently ignore if a location cannot be located, such as a          * properties file not found.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|ignoreMissingLocation ( String ignoreMissingLocation)
specifier|default
name|PropertiesEndpointProducerBuilder
name|ignoreMissingLocation
parameter_list|(
name|String
name|ignoreMissingLocation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ignoreMissingLocation"
argument_list|,
name|ignoreMissingLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A list of locations to load properties. You can use comma to separate          * multiple locations. This option will override any default locations          * and only use the locations from this option.          * The option is a          *<code>java.util.List&lt;org.apache.camel.component.properties.PropertiesLocation&gt;</code> type.          * @group common          */
DECL|method|locations ( List<Object> locations)
specifier|default
name|PropertiesEndpointProducerBuilder
name|locations
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|locations
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"locations"
argument_list|,
name|locations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A list of locations to load properties. You can use comma to separate          * multiple locations. This option will override any default locations          * and only use the locations from this option.          * The option will be converted to a          *<code>java.util.List&lt;org.apache.camel.component.properties.PropertiesLocation&gt;</code> type.          * @group common          */
DECL|method|locations (String locations)
specifier|default
name|PropertiesEndpointProducerBuilder
name|locations
parameter_list|(
name|String
name|locations
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"locations"
argument_list|,
name|locations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|PropertiesEndpointProducerBuilder
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
name|PropertiesEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Properties component.      */
DECL|interface|AdvancedPropertiesEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedPropertiesEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|PropertiesEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|PropertiesEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedPropertiesEndpointProducerBuilder
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
name|AdvancedPropertiesEndpointProducerBuilder
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
name|AdvancedPropertiesEndpointProducerBuilder
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
name|AdvancedPropertiesEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Properties component.      */
DECL|interface|PropertiesEndpointBuilder
specifier|public
interface|interface
name|PropertiesEndpointBuilder
extends|extends
name|PropertiesEndpointConsumerBuilder
extends|,
name|PropertiesEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedPropertiesEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedPropertiesEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Property key to use as placeholder.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|key (String key)
specifier|default
name|PropertiesEndpointBuilder
name|key
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to silently ignore if a location cannot be located, such as a          * properties file not found.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|ignoreMissingLocation ( boolean ignoreMissingLocation)
specifier|default
name|PropertiesEndpointBuilder
name|ignoreMissingLocation
parameter_list|(
name|boolean
name|ignoreMissingLocation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ignoreMissingLocation"
argument_list|,
name|ignoreMissingLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to silently ignore if a location cannot be located, such as a          * properties file not found.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|ignoreMissingLocation ( String ignoreMissingLocation)
specifier|default
name|PropertiesEndpointBuilder
name|ignoreMissingLocation
parameter_list|(
name|String
name|ignoreMissingLocation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ignoreMissingLocation"
argument_list|,
name|ignoreMissingLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A list of locations to load properties. You can use comma to separate          * multiple locations. This option will override any default locations          * and only use the locations from this option.          * The option is a          *<code>java.util.List&lt;org.apache.camel.component.properties.PropertiesLocation&gt;</code> type.          * @group common          */
DECL|method|locations (List<Object> locations)
specifier|default
name|PropertiesEndpointBuilder
name|locations
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|locations
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"locations"
argument_list|,
name|locations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A list of locations to load properties. You can use comma to separate          * multiple locations. This option will override any default locations          * and only use the locations from this option.          * The option will be converted to a          *<code>java.util.List&lt;org.apache.camel.component.properties.PropertiesLocation&gt;</code> type.          * @group common          */
DECL|method|locations (String locations)
specifier|default
name|PropertiesEndpointBuilder
name|locations
parameter_list|(
name|String
name|locations
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"locations"
argument_list|,
name|locations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Properties component.      */
DECL|interface|AdvancedPropertiesEndpointBuilder
specifier|public
interface|interface
name|AdvancedPropertiesEndpointBuilder
extends|extends
name|AdvancedPropertiesEndpointConsumerBuilder
extends|,
name|AdvancedPropertiesEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|PropertiesEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|PropertiesEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedPropertiesEndpointBuilder
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
name|AdvancedPropertiesEndpointBuilder
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
name|AdvancedPropertiesEndpointBuilder
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
name|AdvancedPropertiesEndpointBuilder
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
comment|/**      * The properties component is used for using property placeholders in      * endpoint uris.      * Maven coordinates: org.apache.camel:camel-properties      */
DECL|method|properties (String path)
specifier|default
name|PropertiesEndpointBuilder
name|properties
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|PropertiesEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|PropertiesEndpointBuilder
implements|,
name|AdvancedPropertiesEndpointBuilder
block|{
specifier|public
name|PropertiesEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"properties"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|PropertiesEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

