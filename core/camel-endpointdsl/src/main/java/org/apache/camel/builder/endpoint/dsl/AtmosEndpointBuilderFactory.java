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
comment|/**  * The atmos component is used for integrating with EMC's Atomos Storage.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|AtmosEndpointBuilderFactory
specifier|public
interface|interface
name|AtmosEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Atmos component.      */
DECL|interface|AtmosEndpointConsumerBuilder
specifier|public
interface|interface
name|AtmosEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAtmosEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAtmosEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Atmos name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|name (String name)
specifier|default
name|AtmosEndpointConsumerBuilder
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
comment|/**          * Operation to perform.          * The option is a          *<code>org.apache.camel.component.atmos.util.AtmosOperation</code>          * type.          * @group common          */
DECL|method|operation (AtmosOperation operation)
specifier|default
name|AtmosEndpointConsumerBuilder
name|operation
parameter_list|(
name|AtmosOperation
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Operation to perform.          * The option will be converted to a          *<code>org.apache.camel.component.atmos.util.AtmosOperation</code>          * type.          * @group common          */
DECL|method|operation (String operation)
specifier|default
name|AtmosEndpointConsumerBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Atmos SSL validation.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|enableSslValidation ( boolean enableSslValidation)
specifier|default
name|AtmosEndpointConsumerBuilder
name|enableSslValidation
parameter_list|(
name|boolean
name|enableSslValidation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableSslValidation"
argument_list|,
name|enableSslValidation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos SSL validation.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|enableSslValidation ( String enableSslValidation)
specifier|default
name|AtmosEndpointConsumerBuilder
name|enableSslValidation
parameter_list|(
name|String
name|enableSslValidation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableSslValidation"
argument_list|,
name|enableSslValidation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos client fullTokenId.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|fullTokenId (String fullTokenId)
specifier|default
name|AtmosEndpointConsumerBuilder
name|fullTokenId
parameter_list|(
name|String
name|fullTokenId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"fullTokenId"
argument_list|,
name|fullTokenId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Local path to put files.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|localPath (String localPath)
specifier|default
name|AtmosEndpointConsumerBuilder
name|localPath
parameter_list|(
name|String
name|localPath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"localPath"
argument_list|,
name|localPath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * New path on Atmos when moving files.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|newRemotePath (String newRemotePath)
specifier|default
name|AtmosEndpointConsumerBuilder
name|newRemotePath
parameter_list|(
name|String
name|newRemotePath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"newRemotePath"
argument_list|,
name|newRemotePath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Search query on Atmos.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|query (String query)
specifier|default
name|AtmosEndpointConsumerBuilder
name|query
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"query"
argument_list|,
name|query
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Where to put files on Atmos.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|remotePath (String remotePath)
specifier|default
name|AtmosEndpointConsumerBuilder
name|remotePath
parameter_list|(
name|String
name|remotePath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"remotePath"
argument_list|,
name|remotePath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos shared secret.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|secretKey (String secretKey)
specifier|default
name|AtmosEndpointConsumerBuilder
name|secretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secretKey"
argument_list|,
name|secretKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atomos server uri.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|uri (String uri)
specifier|default
name|AtmosEndpointConsumerBuilder
name|uri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"uri"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|AtmosEndpointConsumerBuilder
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
name|AtmosEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the Atmos component.      */
DECL|interface|AdvancedAtmosEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedAtmosEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AtmosEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AtmosEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedAtmosEndpointConsumerBuilder
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
name|AdvancedAtmosEndpointConsumerBuilder
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
name|AdvancedAtmosEndpointConsumerBuilder
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
name|AdvancedAtmosEndpointConsumerBuilder
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
name|AdvancedAtmosEndpointConsumerBuilder
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
name|AdvancedAtmosEndpointConsumerBuilder
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
name|AdvancedAtmosEndpointConsumerBuilder
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
name|AdvancedAtmosEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Atmos component.      */
DECL|interface|AtmosEndpointProducerBuilder
specifier|public
specifier|static
interface|interface
name|AtmosEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAtmosEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAtmosEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Atmos name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|name (String name)
specifier|default
name|AtmosEndpointProducerBuilder
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
comment|/**          * Operation to perform.          * The option is a          *<code>org.apache.camel.component.atmos.util.AtmosOperation</code>          * type.          * @group common          */
DECL|method|operation (AtmosOperation operation)
specifier|default
name|AtmosEndpointProducerBuilder
name|operation
parameter_list|(
name|AtmosOperation
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Operation to perform.          * The option will be converted to a          *<code>org.apache.camel.component.atmos.util.AtmosOperation</code>          * type.          * @group common          */
DECL|method|operation (String operation)
specifier|default
name|AtmosEndpointProducerBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Atmos SSL validation.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|enableSslValidation ( boolean enableSslValidation)
specifier|default
name|AtmosEndpointProducerBuilder
name|enableSslValidation
parameter_list|(
name|boolean
name|enableSslValidation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableSslValidation"
argument_list|,
name|enableSslValidation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos SSL validation.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|enableSslValidation ( String enableSslValidation)
specifier|default
name|AtmosEndpointProducerBuilder
name|enableSslValidation
parameter_list|(
name|String
name|enableSslValidation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableSslValidation"
argument_list|,
name|enableSslValidation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos client fullTokenId.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|fullTokenId (String fullTokenId)
specifier|default
name|AtmosEndpointProducerBuilder
name|fullTokenId
parameter_list|(
name|String
name|fullTokenId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"fullTokenId"
argument_list|,
name|fullTokenId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Local path to put files.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|localPath (String localPath)
specifier|default
name|AtmosEndpointProducerBuilder
name|localPath
parameter_list|(
name|String
name|localPath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"localPath"
argument_list|,
name|localPath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * New path on Atmos when moving files.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|newRemotePath (String newRemotePath)
specifier|default
name|AtmosEndpointProducerBuilder
name|newRemotePath
parameter_list|(
name|String
name|newRemotePath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"newRemotePath"
argument_list|,
name|newRemotePath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Search query on Atmos.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|query (String query)
specifier|default
name|AtmosEndpointProducerBuilder
name|query
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"query"
argument_list|,
name|query
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Where to put files on Atmos.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|remotePath (String remotePath)
specifier|default
name|AtmosEndpointProducerBuilder
name|remotePath
parameter_list|(
name|String
name|remotePath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"remotePath"
argument_list|,
name|remotePath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos shared secret.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|secretKey (String secretKey)
specifier|default
name|AtmosEndpointProducerBuilder
name|secretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secretKey"
argument_list|,
name|secretKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atomos server uri.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|uri (String uri)
specifier|default
name|AtmosEndpointProducerBuilder
name|uri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"uri"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|AtmosEndpointProducerBuilder
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
name|AtmosEndpointProducerBuilder
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
comment|/**      * Advanced builder for endpoint producers for the Atmos component.      */
DECL|interface|AdvancedAtmosEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedAtmosEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AtmosEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AtmosEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedAtmosEndpointProducerBuilder
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
name|AdvancedAtmosEndpointProducerBuilder
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
name|AdvancedAtmosEndpointProducerBuilder
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
name|AdvancedAtmosEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Atmos component.      */
DECL|interface|AtmosEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AtmosEndpointBuilder
extends|extends
name|AtmosEndpointConsumerBuilder
extends|,
name|AtmosEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAtmosEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAtmosEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Atmos name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|name (String name)
specifier|default
name|AtmosEndpointBuilder
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
comment|/**          * Operation to perform.          * The option is a          *<code>org.apache.camel.component.atmos.util.AtmosOperation</code>          * type.          * @group common          */
DECL|method|operation (AtmosOperation operation)
specifier|default
name|AtmosEndpointBuilder
name|operation
parameter_list|(
name|AtmosOperation
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Operation to perform.          * The option will be converted to a          *<code>org.apache.camel.component.atmos.util.AtmosOperation</code>          * type.          * @group common          */
DECL|method|operation (String operation)
specifier|default
name|AtmosEndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Atmos SSL validation.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|enableSslValidation ( boolean enableSslValidation)
specifier|default
name|AtmosEndpointBuilder
name|enableSslValidation
parameter_list|(
name|boolean
name|enableSslValidation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableSslValidation"
argument_list|,
name|enableSslValidation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos SSL validation.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|enableSslValidation ( String enableSslValidation)
specifier|default
name|AtmosEndpointBuilder
name|enableSslValidation
parameter_list|(
name|String
name|enableSslValidation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableSslValidation"
argument_list|,
name|enableSslValidation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos client fullTokenId.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|fullTokenId (String fullTokenId)
specifier|default
name|AtmosEndpointBuilder
name|fullTokenId
parameter_list|(
name|String
name|fullTokenId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"fullTokenId"
argument_list|,
name|fullTokenId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Local path to put files.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|localPath (String localPath)
specifier|default
name|AtmosEndpointBuilder
name|localPath
parameter_list|(
name|String
name|localPath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"localPath"
argument_list|,
name|localPath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * New path on Atmos when moving files.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|newRemotePath (String newRemotePath)
specifier|default
name|AtmosEndpointBuilder
name|newRemotePath
parameter_list|(
name|String
name|newRemotePath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"newRemotePath"
argument_list|,
name|newRemotePath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Search query on Atmos.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|query (String query)
specifier|default
name|AtmosEndpointBuilder
name|query
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"query"
argument_list|,
name|query
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Where to put files on Atmos.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|remotePath (String remotePath)
specifier|default
name|AtmosEndpointBuilder
name|remotePath
parameter_list|(
name|String
name|remotePath
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"remotePath"
argument_list|,
name|remotePath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atmos shared secret.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|secretKey (String secretKey)
specifier|default
name|AtmosEndpointBuilder
name|secretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secretKey"
argument_list|,
name|secretKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Atomos server uri.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|uri (String uri)
specifier|default
name|AtmosEndpointBuilder
name|uri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"uri"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Atmos component.      */
DECL|interface|AdvancedAtmosEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedAtmosEndpointBuilder
extends|extends
name|AdvancedAtmosEndpointConsumerBuilder
extends|,
name|AdvancedAtmosEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AtmosEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AtmosEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedAtmosEndpointBuilder
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
name|AdvancedAtmosEndpointBuilder
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
name|AdvancedAtmosEndpointBuilder
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
name|AdvancedAtmosEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.atmos.util.AtmosOperation</code> enum.      */
DECL|enum|AtmosOperation
specifier|public
specifier|static
enum|enum
name|AtmosOperation
block|{
DECL|enumConstant|put
DECL|enumConstant|del
DECL|enumConstant|search
DECL|enumConstant|get
DECL|enumConstant|move
name|put
block|,
name|del
block|,
name|search
block|,
name|get
block|,
name|move
block|;     }
comment|/**      * The atmos component is used for integrating with EMC's Atomos Storage.      * Creates a builder to build endpoints for the Atmos component.      */
DECL|method|atmos (String path)
specifier|default
name|AtmosEndpointBuilder
name|atmos
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|AtmosEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|AtmosEndpointBuilder
implements|,
name|AdvancedAtmosEndpointBuilder
block|{
specifier|public
name|AtmosEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"atmos"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|AtmosEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

