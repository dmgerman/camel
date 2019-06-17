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
comment|/**  * The Twilio component allows you to interact with the Twilio REST APIs using  * Twilio Java SDK.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|TwilioEndpointBuilderFactory
specifier|public
interface|interface
name|TwilioEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Twilio component.      */
DECL|interface|TwilioEndpointConsumerBuilder
specifier|public
interface|interface
name|TwilioEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedTwilioEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedTwilioEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What kind of operation to perform.          * The option is a          *<code>org.apache.camel.component.twilio.internal.TwilioApiName</code>          * type.          * @group common          */
DECL|method|apiName (TwilioApiName apiName)
specifier|default
name|TwilioEndpointConsumerBuilder
name|apiName
parameter_list|(
name|TwilioApiName
name|apiName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"apiName"
argument_list|,
name|apiName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What kind of operation to perform.          * The option will be converted to a          *<code>org.apache.camel.component.twilio.internal.TwilioApiName</code>          * type.          * @group common          */
DECL|method|apiName (String apiName)
specifier|default
name|TwilioEndpointConsumerBuilder
name|apiName
parameter_list|(
name|String
name|apiName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"apiName"
argument_list|,
name|apiName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What sub operation to use for the selected operation.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|methodName (String methodName)
specifier|default
name|TwilioEndpointConsumerBuilder
name|methodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"methodName"
argument_list|,
name|methodName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the name of a parameter to be passed in the exchange In Body.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|inBody (String inBody)
specifier|default
name|TwilioEndpointConsumerBuilder
name|inBody
parameter_list|(
name|String
name|inBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inBody"
argument_list|,
name|inBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|TwilioEndpointConsumerBuilder
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
name|TwilioEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the Twilio component.      */
DECL|interface|AdvancedTwilioEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedTwilioEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|TwilioEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|TwilioEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedTwilioEndpointConsumerBuilder
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
name|AdvancedTwilioEndpointConsumerBuilder
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
name|AdvancedTwilioEndpointConsumerBuilder
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
name|AdvancedTwilioEndpointConsumerBuilder
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
name|AdvancedTwilioEndpointConsumerBuilder
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
name|AdvancedTwilioEndpointConsumerBuilder
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
name|AdvancedTwilioEndpointConsumerBuilder
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
name|AdvancedTwilioEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Twilio component.      */
DECL|interface|TwilioEndpointProducerBuilder
specifier|public
interface|interface
name|TwilioEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedTwilioEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedTwilioEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What kind of operation to perform.          * The option is a          *<code>org.apache.camel.component.twilio.internal.TwilioApiName</code>          * type.          * @group common          */
DECL|method|apiName (TwilioApiName apiName)
specifier|default
name|TwilioEndpointProducerBuilder
name|apiName
parameter_list|(
name|TwilioApiName
name|apiName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"apiName"
argument_list|,
name|apiName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What kind of operation to perform.          * The option will be converted to a          *<code>org.apache.camel.component.twilio.internal.TwilioApiName</code>          * type.          * @group common          */
DECL|method|apiName (String apiName)
specifier|default
name|TwilioEndpointProducerBuilder
name|apiName
parameter_list|(
name|String
name|apiName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"apiName"
argument_list|,
name|apiName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What sub operation to use for the selected operation.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|methodName (String methodName)
specifier|default
name|TwilioEndpointProducerBuilder
name|methodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"methodName"
argument_list|,
name|methodName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the name of a parameter to be passed in the exchange In Body.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|inBody (String inBody)
specifier|default
name|TwilioEndpointProducerBuilder
name|inBody
parameter_list|(
name|String
name|inBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inBody"
argument_list|,
name|inBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|TwilioEndpointProducerBuilder
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
name|TwilioEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Twilio component.      */
DECL|interface|AdvancedTwilioEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedTwilioEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|TwilioEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|TwilioEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedTwilioEndpointProducerBuilder
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
name|AdvancedTwilioEndpointProducerBuilder
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
name|AdvancedTwilioEndpointProducerBuilder
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
name|AdvancedTwilioEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Twilio component.      */
DECL|interface|TwilioEndpointBuilder
specifier|public
interface|interface
name|TwilioEndpointBuilder
extends|extends
name|TwilioEndpointConsumerBuilder
extends|,
name|TwilioEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedTwilioEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedTwilioEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What kind of operation to perform.          * The option is a          *<code>org.apache.camel.component.twilio.internal.TwilioApiName</code>          * type.          * @group common          */
DECL|method|apiName (TwilioApiName apiName)
specifier|default
name|TwilioEndpointBuilder
name|apiName
parameter_list|(
name|TwilioApiName
name|apiName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"apiName"
argument_list|,
name|apiName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What kind of operation to perform.          * The option will be converted to a          *<code>org.apache.camel.component.twilio.internal.TwilioApiName</code>          * type.          * @group common          */
DECL|method|apiName (String apiName)
specifier|default
name|TwilioEndpointBuilder
name|apiName
parameter_list|(
name|String
name|apiName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"apiName"
argument_list|,
name|apiName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What sub operation to use for the selected operation.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|methodName (String methodName)
specifier|default
name|TwilioEndpointBuilder
name|methodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"methodName"
argument_list|,
name|methodName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the name of a parameter to be passed in the exchange In Body.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|inBody (String inBody)
specifier|default
name|TwilioEndpointBuilder
name|inBody
parameter_list|(
name|String
name|inBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inBody"
argument_list|,
name|inBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Twilio component.      */
DECL|interface|AdvancedTwilioEndpointBuilder
specifier|public
interface|interface
name|AdvancedTwilioEndpointBuilder
extends|extends
name|AdvancedTwilioEndpointConsumerBuilder
extends|,
name|AdvancedTwilioEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|TwilioEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|TwilioEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedTwilioEndpointBuilder
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
name|AdvancedTwilioEndpointBuilder
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
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedTwilioEndpointBuilder
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
name|AdvancedTwilioEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.twilio.internal.TwilioApiName</code>      * enum.      */
DECL|enum|TwilioApiName
specifier|static
enum|enum
name|TwilioApiName
block|{
DECL|enumConstant|ACCOUNT
DECL|enumConstant|ADDRESS
DECL|enumConstant|APPLICATION
DECL|enumConstant|AVAILABLE_PHONE_NUMBER_COUNTRY
DECL|enumConstant|CALL
DECL|enumConstant|CONFERENCE
DECL|enumConstant|CONNECT_APP
DECL|enumConstant|INCOMING_PHONE_NUMBER
DECL|enumConstant|KEY
DECL|enumConstant|MESSAGE
DECL|enumConstant|NEW_KEY
DECL|enumConstant|NEW_SIGNING_KEY
DECL|enumConstant|NOTIFICATION
DECL|enumConstant|OUTGOING_CALLER_ID
DECL|enumConstant|QUEUE
DECL|enumConstant|RECORDING
DECL|enumConstant|SHORT_CODE
DECL|enumConstant|SIGNING_KEY
DECL|enumConstant|TOKEN
DECL|enumConstant|TRANSCRIPTION
DECL|enumConstant|VALIDATION_REQUEST
DECL|enumConstant|ADDRESS_DEPENDENT_PHONE_NUMBER
DECL|enumConstant|AVAILABLE_PHONE_NUMBER_COUNTRY_LOCAL
DECL|enumConstant|AVAILABLE_PHONE_NUMBER_COUNTRY_MOBILE
DECL|enumConstant|AVAILABLE_PHONE_NUMBER_COUNTRY_TOLL_FREE
DECL|enumConstant|CALL_FEEDBACK
DECL|enumConstant|CALL_FEEDBACK_SUMMARY
DECL|enumConstant|CALL_NOTIFICATION
DECL|enumConstant|CALL_RECORDING
DECL|enumConstant|CONFERENCE_PARTICIPANT
DECL|enumConstant|INCOMING_PHONE_NUMBER_LOCAL
DECL|enumConstant|INCOMING_PHONE_NUMBER_MOBILE
DECL|enumConstant|INCOMING_PHONE_NUMBER_TOLL_FREE
DECL|enumConstant|MESSAGE_FEEDBACK
DECL|enumConstant|MESSAGE_MEDIA
DECL|enumConstant|QUEUE_MEMBER
DECL|enumConstant|RECORDING_ADD_ON_RESULT
DECL|enumConstant|RECORDING_TRANSCRIPTION
DECL|enumConstant|RECORDING_ADD_ON_RESULT_PAYLOAD
DECL|enumConstant|SIP_CREDENTIAL_LIST
DECL|enumConstant|SIP_DOMAIN
DECL|enumConstant|SIP_IP_ACCESS_CONTROL_LIST
DECL|enumConstant|SIP_CREDENTIAL_LIST_CREDENTIAL
DECL|enumConstant|SIP_DOMAIN_CREDENTIAL_LIST_MAPPING
DECL|enumConstant|SIP_DOMAIN_IP_ACCESS_CONTROL_LIST_MAPPING
DECL|enumConstant|SIP_IP_ACCESS_CONTROL_LIST_IP_ADDRESS
DECL|enumConstant|USAGE_RECORD
DECL|enumConstant|USAGE_TRIGGER
DECL|enumConstant|USAGE_RECORD_ALL_TIME
DECL|enumConstant|USAGE_RECORD_DAILY
DECL|enumConstant|USAGE_RECORD_LAST_MONTH
DECL|enumConstant|USAGE_RECORD_MONTHLY
DECL|enumConstant|USAGE_RECORD_THIS_MONTH
DECL|enumConstant|USAGE_RECORD_TODAY
DECL|enumConstant|USAGE_RECORD_YEARLY
DECL|enumConstant|USAGE_RECORD_YESTERDAY
name|ACCOUNT
block|,
name|ADDRESS
block|,
name|APPLICATION
block|,
name|AVAILABLE_PHONE_NUMBER_COUNTRY
block|,
name|CALL
block|,
name|CONFERENCE
block|,
name|CONNECT_APP
block|,
name|INCOMING_PHONE_NUMBER
block|,
name|KEY
block|,
name|MESSAGE
block|,
name|NEW_KEY
block|,
name|NEW_SIGNING_KEY
block|,
name|NOTIFICATION
block|,
name|OUTGOING_CALLER_ID
block|,
name|QUEUE
block|,
name|RECORDING
block|,
name|SHORT_CODE
block|,
name|SIGNING_KEY
block|,
name|TOKEN
block|,
name|TRANSCRIPTION
block|,
name|VALIDATION_REQUEST
block|,
name|ADDRESS_DEPENDENT_PHONE_NUMBER
block|,
name|AVAILABLE_PHONE_NUMBER_COUNTRY_LOCAL
block|,
name|AVAILABLE_PHONE_NUMBER_COUNTRY_MOBILE
block|,
name|AVAILABLE_PHONE_NUMBER_COUNTRY_TOLL_FREE
block|,
name|CALL_FEEDBACK
block|,
name|CALL_FEEDBACK_SUMMARY
block|,
name|CALL_NOTIFICATION
block|,
name|CALL_RECORDING
block|,
name|CONFERENCE_PARTICIPANT
block|,
name|INCOMING_PHONE_NUMBER_LOCAL
block|,
name|INCOMING_PHONE_NUMBER_MOBILE
block|,
name|INCOMING_PHONE_NUMBER_TOLL_FREE
block|,
name|MESSAGE_FEEDBACK
block|,
name|MESSAGE_MEDIA
block|,
name|QUEUE_MEMBER
block|,
name|RECORDING_ADD_ON_RESULT
block|,
name|RECORDING_TRANSCRIPTION
block|,
name|RECORDING_ADD_ON_RESULT_PAYLOAD
block|,
name|SIP_CREDENTIAL_LIST
block|,
name|SIP_DOMAIN
block|,
name|SIP_IP_ACCESS_CONTROL_LIST
block|,
name|SIP_CREDENTIAL_LIST_CREDENTIAL
block|,
name|SIP_DOMAIN_CREDENTIAL_LIST_MAPPING
block|,
name|SIP_DOMAIN_IP_ACCESS_CONTROL_LIST_MAPPING
block|,
name|SIP_IP_ACCESS_CONTROL_LIST_IP_ADDRESS
block|,
name|USAGE_RECORD
block|,
name|USAGE_TRIGGER
block|,
name|USAGE_RECORD_ALL_TIME
block|,
name|USAGE_RECORD_DAILY
block|,
name|USAGE_RECORD_LAST_MONTH
block|,
name|USAGE_RECORD_MONTHLY
block|,
name|USAGE_RECORD_THIS_MONTH
block|,
name|USAGE_RECORD_TODAY
block|,
name|USAGE_RECORD_YEARLY
block|,
name|USAGE_RECORD_YESTERDAY
block|;     }
comment|/**      * The Twilio component allows you to interact with the Twilio REST APIs      * using Twilio Java SDK. Creates a builder to build endpoints for the      * Twilio component.      */
DECL|method|twilio (String path)
specifier|default
name|TwilioEndpointBuilder
name|twilio
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|TwilioEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|TwilioEndpointBuilder
implements|,
name|AdvancedTwilioEndpointBuilder
block|{
specifier|public
name|TwilioEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"twilio"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|TwilioEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

