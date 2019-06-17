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
comment|/**  * Allows producing messages to manage Zendesk ticket, user, organization, etc.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ZendeskEndpointBuilderFactory
specifier|public
interface|interface
name|ZendeskEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Zendesk component.      */
DECL|interface|ZendeskEndpointConsumerBuilder
specifier|public
interface|interface
name|ZendeskEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedZendeskEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedZendeskEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What operation to use.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|methodName (String methodName)
specifier|default
name|ZendeskEndpointConsumerBuilder
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
name|ZendeskEndpointConsumerBuilder
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
comment|/**          * The server URL to connect.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|serverUrl (String serverUrl)
specifier|default
name|ZendeskEndpointConsumerBuilder
name|serverUrl
parameter_list|(
name|String
name|serverUrl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"serverUrl"
argument_list|,
name|serverUrl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|ZendeskEndpointConsumerBuilder
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
name|ZendeskEndpointConsumerBuilder
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
comment|/**          * The OAuth token.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|oauthToken (String oauthToken)
specifier|default
name|ZendeskEndpointConsumerBuilder
name|oauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"oauthToken"
argument_list|,
name|oauthToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The password.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|password (String password)
specifier|default
name|ZendeskEndpointConsumerBuilder
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
comment|/**          * The security token.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|token (String token)
specifier|default
name|ZendeskEndpointConsumerBuilder
name|token
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"token"
argument_list|,
name|token
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The user name.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|username (String username)
specifier|default
name|ZendeskEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the Zendesk component.      */
DECL|interface|AdvancedZendeskEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedZendeskEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ZendeskEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ZendeskEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedZendeskEndpointConsumerBuilder
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
name|AdvancedZendeskEndpointConsumerBuilder
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
name|AdvancedZendeskEndpointConsumerBuilder
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
name|AdvancedZendeskEndpointConsumerBuilder
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
name|AdvancedZendeskEndpointConsumerBuilder
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
name|AdvancedZendeskEndpointConsumerBuilder
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
name|AdvancedZendeskEndpointConsumerBuilder
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
name|AdvancedZendeskEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Zendesk component.      */
DECL|interface|ZendeskEndpointProducerBuilder
specifier|public
interface|interface
name|ZendeskEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedZendeskEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedZendeskEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What operation to use.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|methodName (String methodName)
specifier|default
name|ZendeskEndpointProducerBuilder
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
name|ZendeskEndpointProducerBuilder
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
comment|/**          * The server URL to connect.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|serverUrl (String serverUrl)
specifier|default
name|ZendeskEndpointProducerBuilder
name|serverUrl
parameter_list|(
name|String
name|serverUrl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"serverUrl"
argument_list|,
name|serverUrl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|ZendeskEndpointProducerBuilder
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
name|ZendeskEndpointProducerBuilder
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
comment|/**          * The OAuth token.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|oauthToken (String oauthToken)
specifier|default
name|ZendeskEndpointProducerBuilder
name|oauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"oauthToken"
argument_list|,
name|oauthToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The password.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|password (String password)
specifier|default
name|ZendeskEndpointProducerBuilder
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
comment|/**          * The security token.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|token (String token)
specifier|default
name|ZendeskEndpointProducerBuilder
name|token
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"token"
argument_list|,
name|token
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The user name.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|username (String username)
specifier|default
name|ZendeskEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Zendesk component.      */
DECL|interface|AdvancedZendeskEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedZendeskEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ZendeskEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ZendeskEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedZendeskEndpointProducerBuilder
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
name|AdvancedZendeskEndpointProducerBuilder
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
name|AdvancedZendeskEndpointProducerBuilder
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
name|AdvancedZendeskEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Zendesk component.      */
DECL|interface|ZendeskEndpointBuilder
specifier|public
interface|interface
name|ZendeskEndpointBuilder
extends|extends
name|ZendeskEndpointConsumerBuilder
extends|,
name|ZendeskEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedZendeskEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedZendeskEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What operation to use.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|methodName (String methodName)
specifier|default
name|ZendeskEndpointBuilder
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
name|ZendeskEndpointBuilder
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
comment|/**          * The server URL to connect.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|serverUrl (String serverUrl)
specifier|default
name|ZendeskEndpointBuilder
name|serverUrl
parameter_list|(
name|String
name|serverUrl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"serverUrl"
argument_list|,
name|serverUrl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The OAuth token.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|oauthToken (String oauthToken)
specifier|default
name|ZendeskEndpointBuilder
name|oauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"oauthToken"
argument_list|,
name|oauthToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The password.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|password (String password)
specifier|default
name|ZendeskEndpointBuilder
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
comment|/**          * The security token.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|token (String token)
specifier|default
name|ZendeskEndpointBuilder
name|token
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"token"
argument_list|,
name|token
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The user name.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|username (String username)
specifier|default
name|ZendeskEndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the Zendesk component.      */
DECL|interface|AdvancedZendeskEndpointBuilder
specifier|public
interface|interface
name|AdvancedZendeskEndpointBuilder
extends|extends
name|AdvancedZendeskEndpointConsumerBuilder
extends|,
name|AdvancedZendeskEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ZendeskEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ZendeskEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedZendeskEndpointBuilder
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
name|AdvancedZendeskEndpointBuilder
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
name|AdvancedZendeskEndpointBuilder
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
name|AdvancedZendeskEndpointBuilder
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
comment|/**      * Allows producing messages to manage Zendesk ticket, user, organization,      * etc. Creates a builder to build endpoints for the Zendesk component.      */
DECL|method|zendesk (String path)
specifier|default
name|ZendeskEndpointBuilder
name|zendesk
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ZendeskEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ZendeskEndpointBuilder
implements|,
name|AdvancedZendeskEndpointBuilder
block|{
specifier|public
name|ZendeskEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"zendesk"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ZendeskEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

