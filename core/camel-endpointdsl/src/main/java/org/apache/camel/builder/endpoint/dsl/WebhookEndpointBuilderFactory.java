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
comment|/**  * The webhook component allows other Camel components that can receive push  * notifications to expose webhook endpoints and automatically register them  * with their own webhook provider.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|WebhookEndpointBuilderFactory
specifier|public
interface|interface
name|WebhookEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Webhook component.      */
DECL|interface|WebhookEndpointBuilder
specifier|public
interface|interface
name|WebhookEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedWebhookEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedWebhookEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Automatically register the webhook at startup and unregister it on          * shutdown.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|webhookAutoRegister ( boolean webhookAutoRegister)
specifier|default
name|WebhookEndpointBuilder
name|webhookAutoRegister
parameter_list|(
name|boolean
name|webhookAutoRegister
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"webhookAutoRegister"
argument_list|,
name|webhookAutoRegister
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Automatically register the webhook at startup and unregister it on          * shutdown.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|webhookAutoRegister ( String webhookAutoRegister)
specifier|default
name|WebhookEndpointBuilder
name|webhookAutoRegister
parameter_list|(
name|String
name|webhookAutoRegister
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"webhookAutoRegister"
argument_list|,
name|webhookAutoRegister
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The first (base) path element where the webhook will be exposed. It's          * a good practice to set it to a random string, so that it cannot be          * guessed by unauthorized parties.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|webhookBasePath (String webhookBasePath)
specifier|default
name|WebhookEndpointBuilder
name|webhookBasePath
parameter_list|(
name|String
name|webhookBasePath
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"webhookBasePath"
argument_list|,
name|webhookBasePath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Camel Rest component to use for the REST transport, such as          * netty-http.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|webhookComponentName ( String webhookComponentName)
specifier|default
name|WebhookEndpointBuilder
name|webhookComponentName
parameter_list|(
name|String
name|webhookComponentName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"webhookComponentName"
argument_list|,
name|webhookComponentName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The URL of the current service as seen by the webhook provider.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|webhookExternalUrl ( String webhookExternalUrl)
specifier|default
name|WebhookEndpointBuilder
name|webhookExternalUrl
parameter_list|(
name|String
name|webhookExternalUrl
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"webhookExternalUrl"
argument_list|,
name|webhookExternalUrl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The path where the webhook endpoint will be exposed (relative to          * basePath, if any).          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|webhookPath (String webhookPath)
specifier|default
name|WebhookEndpointBuilder
name|webhookPath
parameter_list|(
name|String
name|webhookPath
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"webhookPath"
argument_list|,
name|webhookPath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|WebhookEndpointBuilder
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
name|WebhookEndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the Webhook component.      */
DECL|interface|AdvancedWebhookEndpointBuilder
specifier|public
interface|interface
name|AdvancedWebhookEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|WebhookEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|WebhookEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedWebhookEndpointBuilder
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
name|AdvancedWebhookEndpointBuilder
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
name|AdvancedWebhookEndpointBuilder
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
name|AdvancedWebhookEndpointBuilder
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
name|AdvancedWebhookEndpointBuilder
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
name|AdvancedWebhookEndpointBuilder
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
name|AdvancedWebhookEndpointBuilder
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
name|AdvancedWebhookEndpointBuilder
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
comment|/**      * Webhook (camel-webhook)      * The webhook component allows other Camel components that can receive push      * notifications to expose webhook endpoints and automatically register them      * with their own webhook provider.      *       * Category: cloud      * Available as of version: 3.0      * Maven coordinates: org.apache.camel:camel-webhook      *       * Syntax:<code>webhook:endpointUri</code>      *       * Path parameter: endpointUri      * The delegate uri. Must belong to a component that supports webhooks.      */
DECL|method|webhook (String path)
specifier|default
name|WebhookEndpointBuilder
name|webhook
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|WebhookEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|WebhookEndpointBuilder
implements|,
name|AdvancedWebhookEndpointBuilder
block|{
specifier|public
name|WebhookEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"webhook"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|WebhookEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

