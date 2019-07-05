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
comment|/**  * The asterisk component is used to interact with Asterisk PBX Server.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|AsteriskEndpointBuilderFactory
specifier|public
interface|interface
name|AsteriskEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Asterisk component.      */
DECL|interface|AsteriskEndpointConsumerBuilder
specifier|public
interface|interface
name|AsteriskEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAsteriskEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAsteriskEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Logical name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|name (String name)
specifier|default
name|AsteriskEndpointConsumerBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hostname of the asterisk server.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|hostname (String hostname)
specifier|default
name|AsteriskEndpointConsumerBuilder
name|hostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hostname"
argument_list|,
name|hostname
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Login password.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|password (String password)
specifier|default
name|AsteriskEndpointConsumerBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Login username.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|username (String username)
specifier|default
name|AsteriskEndpointConsumerBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|AsteriskEndpointConsumerBuilder
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
name|AsteriskEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the Asterisk component.      */
DECL|interface|AdvancedAsteriskEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedAsteriskEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AsteriskEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AsteriskEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedAsteriskEndpointConsumerBuilder
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
name|AdvancedAsteriskEndpointConsumerBuilder
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
name|AdvancedAsteriskEndpointConsumerBuilder
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
name|AdvancedAsteriskEndpointConsumerBuilder
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
name|AdvancedAsteriskEndpointConsumerBuilder
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
name|AdvancedAsteriskEndpointConsumerBuilder
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
name|AdvancedAsteriskEndpointConsumerBuilder
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
name|AdvancedAsteriskEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Asterisk component.      */
DECL|interface|AsteriskEndpointProducerBuilder
specifier|public
interface|interface
name|AsteriskEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAsteriskEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAsteriskEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Logical name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|name (String name)
specifier|default
name|AsteriskEndpointProducerBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hostname of the asterisk server.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|hostname (String hostname)
specifier|default
name|AsteriskEndpointProducerBuilder
name|hostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hostname"
argument_list|,
name|hostname
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Login password.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|password (String password)
specifier|default
name|AsteriskEndpointProducerBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Login username.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|username (String username)
specifier|default
name|AsteriskEndpointProducerBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What action to perform such as getting queue status, sip peers or          * extension state.          *           * The option is a:          *<code>org.apache.camel.component.asterisk.AsteriskAction</code> type.          *           * Group: producer          */
DECL|method|action (AsteriskAction action)
specifier|default
name|AsteriskEndpointProducerBuilder
name|action
parameter_list|(
name|AsteriskAction
name|action
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"action"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What action to perform such as getting queue status, sip peers or          * extension state.          *           * The option will be converted to a          *<code>org.apache.camel.component.asterisk.AsteriskAction</code> type.          *           * Group: producer          */
DECL|method|action (String action)
specifier|default
name|AsteriskEndpointProducerBuilder
name|action
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"action"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|AsteriskEndpointProducerBuilder
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
name|AsteriskEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Asterisk component.      */
DECL|interface|AdvancedAsteriskEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedAsteriskEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AsteriskEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AsteriskEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedAsteriskEndpointProducerBuilder
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
name|AdvancedAsteriskEndpointProducerBuilder
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
name|AdvancedAsteriskEndpointProducerBuilder
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
name|AdvancedAsteriskEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Asterisk component.      */
DECL|interface|AsteriskEndpointBuilder
specifier|public
interface|interface
name|AsteriskEndpointBuilder
extends|extends
name|AsteriskEndpointConsumerBuilder
extends|,
name|AsteriskEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAsteriskEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAsteriskEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Logical name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|name (String name)
specifier|default
name|AsteriskEndpointBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hostname of the asterisk server.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|hostname (String hostname)
specifier|default
name|AsteriskEndpointBuilder
name|hostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hostname"
argument_list|,
name|hostname
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Login password.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|password (String password)
specifier|default
name|AsteriskEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Login username.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|username (String username)
specifier|default
name|AsteriskEndpointBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Asterisk component.      */
DECL|interface|AdvancedAsteriskEndpointBuilder
specifier|public
interface|interface
name|AdvancedAsteriskEndpointBuilder
extends|extends
name|AdvancedAsteriskEndpointConsumerBuilder
extends|,
name|AdvancedAsteriskEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AsteriskEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AsteriskEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedAsteriskEndpointBuilder
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
name|AdvancedAsteriskEndpointBuilder
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
name|AdvancedAsteriskEndpointBuilder
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
name|AdvancedAsteriskEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.asterisk.AsteriskAction</code> enum.      */
DECL|enum|AsteriskAction
enum|enum
name|AsteriskAction
block|{
DECL|enumConstant|QUEUE_STATUS
name|QUEUE_STATUS
block|,
DECL|enumConstant|SIP_PEERS
name|SIP_PEERS
block|,
DECL|enumConstant|EXTENSION_STATE
name|EXTENSION_STATE
block|;     }
comment|/**      * The asterisk component is used to interact with Asterisk PBX Server.      * Maven coordinates: org.apache.camel:camel-asterisk      */
DECL|method|asterisk (String path)
specifier|default
name|AsteriskEndpointBuilder
name|asterisk
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|AsteriskEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|AsteriskEndpointBuilder
implements|,
name|AdvancedAsteriskEndpointBuilder
block|{
specifier|public
name|AsteriskEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"asterisk"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|AsteriskEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

