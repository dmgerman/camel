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
comment|/**  * The google-sheets component provides access to Google Sheets.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|GoogleSheetsEndpointBuilderFactory
specifier|public
interface|interface
name|GoogleSheetsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Google Sheets component.      */
DECL|interface|GoogleSheetsEndpointConsumerBuilder
specifier|public
interface|interface
name|GoogleSheetsEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGoogleSheetsEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGoogleSheetsEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * OAuth 2 access token. This typically expires after an hour so          * refreshToken is recommended for long term usage.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|accessToken ( String accessToken)
specifier|default
name|GoogleSheetsEndpointConsumerBuilder
name|accessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accessToken"
argument_list|,
name|accessToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Google Sheets application name. Example would be          * camel-google-sheets/1.0.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|applicationName ( String applicationName)
specifier|default
name|GoogleSheetsEndpointConsumerBuilder
name|applicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"applicationName"
argument_list|,
name|applicationName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Client ID of the sheets application.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clientId (String clientId)
specifier|default
name|GoogleSheetsEndpointConsumerBuilder
name|clientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientId"
argument_list|,
name|clientId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Client secret of the sheets application.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clientSecret ( String clientSecret)
specifier|default
name|GoogleSheetsEndpointConsumerBuilder
name|clientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientSecret"
argument_list|,
name|clientSecret
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the name of a parameter to be passed in the exchange In Body.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|inBody (String inBody)
specifier|default
name|GoogleSheetsEndpointConsumerBuilder
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
comment|/**          * OAuth 2 refresh token. Using this, the Google Sheets component can          * obtain a new accessToken whenever the current one expires - a          * necessity if the application is long-lived.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|refreshToken ( String refreshToken)
specifier|default
name|GoogleSheetsEndpointConsumerBuilder
name|refreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"refreshToken"
argument_list|,
name|refreshToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|GoogleSheetsEndpointConsumerBuilder
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
name|GoogleSheetsEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the Google Sheets component.      */
DECL|interface|AdvancedGoogleSheetsEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedGoogleSheetsEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GoogleSheetsEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GoogleSheetsEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedGoogleSheetsEndpointConsumerBuilder
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
name|AdvancedGoogleSheetsEndpointConsumerBuilder
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
name|AdvancedGoogleSheetsEndpointConsumerBuilder
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
name|AdvancedGoogleSheetsEndpointConsumerBuilder
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
name|AdvancedGoogleSheetsEndpointConsumerBuilder
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
name|AdvancedGoogleSheetsEndpointConsumerBuilder
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
name|AdvancedGoogleSheetsEndpointConsumerBuilder
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
name|AdvancedGoogleSheetsEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Google Sheets component.      */
DECL|interface|GoogleSheetsEndpointProducerBuilder
specifier|public
interface|interface
name|GoogleSheetsEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGoogleSheetsEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGoogleSheetsEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * OAuth 2 access token. This typically expires after an hour so          * refreshToken is recommended for long term usage.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|accessToken ( String accessToken)
specifier|default
name|GoogleSheetsEndpointProducerBuilder
name|accessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accessToken"
argument_list|,
name|accessToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Google Sheets application name. Example would be          * camel-google-sheets/1.0.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|applicationName ( String applicationName)
specifier|default
name|GoogleSheetsEndpointProducerBuilder
name|applicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"applicationName"
argument_list|,
name|applicationName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Client ID of the sheets application.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clientId (String clientId)
specifier|default
name|GoogleSheetsEndpointProducerBuilder
name|clientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientId"
argument_list|,
name|clientId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Client secret of the sheets application.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clientSecret ( String clientSecret)
specifier|default
name|GoogleSheetsEndpointProducerBuilder
name|clientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientSecret"
argument_list|,
name|clientSecret
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the name of a parameter to be passed in the exchange In Body.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|inBody (String inBody)
specifier|default
name|GoogleSheetsEndpointProducerBuilder
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
comment|/**          * OAuth 2 refresh token. Using this, the Google Sheets component can          * obtain a new accessToken whenever the current one expires - a          * necessity if the application is long-lived.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|refreshToken ( String refreshToken)
specifier|default
name|GoogleSheetsEndpointProducerBuilder
name|refreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"refreshToken"
argument_list|,
name|refreshToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|GoogleSheetsEndpointProducerBuilder
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
name|GoogleSheetsEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Google Sheets component.      */
DECL|interface|AdvancedGoogleSheetsEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedGoogleSheetsEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GoogleSheetsEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GoogleSheetsEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGoogleSheetsEndpointProducerBuilder
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
name|AdvancedGoogleSheetsEndpointProducerBuilder
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
name|AdvancedGoogleSheetsEndpointProducerBuilder
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
name|AdvancedGoogleSheetsEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Google Sheets component.      */
DECL|interface|GoogleSheetsEndpointBuilder
specifier|public
interface|interface
name|GoogleSheetsEndpointBuilder
extends|extends
name|GoogleSheetsEndpointConsumerBuilder
extends|,
name|GoogleSheetsEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGoogleSheetsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGoogleSheetsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * OAuth 2 access token. This typically expires after an hour so          * refreshToken is recommended for long term usage.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|accessToken (String accessToken)
specifier|default
name|GoogleSheetsEndpointBuilder
name|accessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accessToken"
argument_list|,
name|accessToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Google Sheets application name. Example would be          * camel-google-sheets/1.0.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|applicationName ( String applicationName)
specifier|default
name|GoogleSheetsEndpointBuilder
name|applicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"applicationName"
argument_list|,
name|applicationName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Client ID of the sheets application.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clientId (String clientId)
specifier|default
name|GoogleSheetsEndpointBuilder
name|clientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientId"
argument_list|,
name|clientId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Client secret of the sheets application.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clientSecret (String clientSecret)
specifier|default
name|GoogleSheetsEndpointBuilder
name|clientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientSecret"
argument_list|,
name|clientSecret
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the name of a parameter to be passed in the exchange In Body.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|inBody (String inBody)
specifier|default
name|GoogleSheetsEndpointBuilder
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
comment|/**          * OAuth 2 refresh token. Using this, the Google Sheets component can          * obtain a new accessToken whenever the current one expires - a          * necessity if the application is long-lived.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|refreshToken (String refreshToken)
specifier|default
name|GoogleSheetsEndpointBuilder
name|refreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"refreshToken"
argument_list|,
name|refreshToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Google Sheets component.      */
DECL|interface|AdvancedGoogleSheetsEndpointBuilder
specifier|public
interface|interface
name|AdvancedGoogleSheetsEndpointBuilder
extends|extends
name|AdvancedGoogleSheetsEndpointConsumerBuilder
extends|,
name|AdvancedGoogleSheetsEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GoogleSheetsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GoogleSheetsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGoogleSheetsEndpointBuilder
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
name|AdvancedGoogleSheetsEndpointBuilder
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
name|AdvancedGoogleSheetsEndpointBuilder
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
name|AdvancedGoogleSheetsEndpointBuilder
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
comment|/**      * Google Sheets (camel-google-sheets)      * The google-sheets component provides access to Google Sheets.      *       * Category: api,cloud,sheets      * Available as of version: 2.23      * Maven coordinates: org.apache.camel:camel-google-sheets      *       * Syntax:<code>google-sheets:apiName/methodName</code>      *       * Path parameter: apiName (required)      * What kind of operation to perform      * The value can be one of: SPREADSHEETS, DATA      *       * Path parameter: methodName (required)      * What sub operation to use for the selected operation      * The value can be one of: create, get, update, append, batchUpdate, clear      */
DECL|method|googleSheets (String path)
specifier|default
name|GoogleSheetsEndpointBuilder
name|googleSheets
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|GoogleSheetsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|GoogleSheetsEndpointBuilder
implements|,
name|AdvancedGoogleSheetsEndpointBuilder
block|{
specifier|public
name|GoogleSheetsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"google-sheets"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GoogleSheetsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

