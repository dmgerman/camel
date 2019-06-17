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
comment|/**  * The github component is used for integrating Camel with github.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|GitHubEndpointBuilderFactory
specifier|public
interface|interface
name|GitHubEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the GitHub component.      */
DECL|interface|GitHubEndpointConsumerBuilder
specifier|public
interface|interface
name|GitHubEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGitHubEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGitHubEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What git operation to execute.          * The option is a          *<code>org.apache.camel.component.github.GitHubType</code> type.          * @group common          */
DECL|method|type (GitHubType type)
specifier|default
name|GitHubEndpointConsumerBuilder
name|type
parameter_list|(
name|GitHubType
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What git operation to execute.          * The option will be converted to a          *<code>org.apache.camel.component.github.GitHubType</code> type.          * @group common          */
DECL|method|type (String type)
specifier|default
name|GitHubEndpointConsumerBuilder
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of branch.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|branchName (String branchName)
specifier|default
name|GitHubEndpointConsumerBuilder
name|branchName
parameter_list|(
name|String
name|branchName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"branchName"
argument_list|,
name|branchName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub OAuth token, required unless username& password are provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|oauthToken (String oauthToken)
specifier|default
name|GitHubEndpointConsumerBuilder
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
comment|/**          * GitHub password, required unless oauthToken is provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|password (String password)
specifier|default
name|GitHubEndpointConsumerBuilder
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
comment|/**          * GitHub repository name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|repoName (String repoName)
specifier|default
name|GitHubEndpointConsumerBuilder
name|repoName
parameter_list|(
name|String
name|repoName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"repoName"
argument_list|,
name|repoName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub repository owner (organization).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|repoOwner (String repoOwner)
specifier|default
name|GitHubEndpointConsumerBuilder
name|repoOwner
parameter_list|(
name|String
name|repoOwner
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"repoOwner"
argument_list|,
name|repoOwner
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub username, required unless oauthToken is provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|username (String username)
specifier|default
name|GitHubEndpointConsumerBuilder
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
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|GitHubEndpointConsumerBuilder
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
name|GitHubEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the GitHub component.      */
DECL|interface|AdvancedGitHubEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedGitHubEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GitHubEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GitHubEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedGitHubEndpointConsumerBuilder
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
name|AdvancedGitHubEndpointConsumerBuilder
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
name|AdvancedGitHubEndpointConsumerBuilder
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
name|AdvancedGitHubEndpointConsumerBuilder
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
name|AdvancedGitHubEndpointConsumerBuilder
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
name|AdvancedGitHubEndpointConsumerBuilder
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
name|AdvancedGitHubEndpointConsumerBuilder
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
name|AdvancedGitHubEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the GitHub component.      */
DECL|interface|GitHubEndpointProducerBuilder
specifier|public
interface|interface
name|GitHubEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGitHubEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGitHubEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What git operation to execute.          * The option is a          *<code>org.apache.camel.component.github.GitHubType</code> type.          * @group common          */
DECL|method|type (GitHubType type)
specifier|default
name|GitHubEndpointProducerBuilder
name|type
parameter_list|(
name|GitHubType
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What git operation to execute.          * The option will be converted to a          *<code>org.apache.camel.component.github.GitHubType</code> type.          * @group common          */
DECL|method|type (String type)
specifier|default
name|GitHubEndpointProducerBuilder
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub OAuth token, required unless username& password are provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|oauthToken (String oauthToken)
specifier|default
name|GitHubEndpointProducerBuilder
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
comment|/**          * GitHub password, required unless oauthToken is provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|password (String password)
specifier|default
name|GitHubEndpointProducerBuilder
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
comment|/**          * GitHub repository name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|repoName (String repoName)
specifier|default
name|GitHubEndpointProducerBuilder
name|repoName
parameter_list|(
name|String
name|repoName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"repoName"
argument_list|,
name|repoName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub repository owner (organization).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|repoOwner (String repoOwner)
specifier|default
name|GitHubEndpointProducerBuilder
name|repoOwner
parameter_list|(
name|String
name|repoOwner
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"repoOwner"
argument_list|,
name|repoOwner
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub username, required unless oauthToken is provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|username (String username)
specifier|default
name|GitHubEndpointProducerBuilder
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
comment|/**          * To use the given encoding when getting a git commit file.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|encoding (String encoding)
specifier|default
name|GitHubEndpointProducerBuilder
name|encoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"encoding"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|GitHubEndpointProducerBuilder
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
name|GitHubEndpointProducerBuilder
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
comment|/**          * To set git commit status state.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|state (String state)
specifier|default
name|GitHubEndpointProducerBuilder
name|state
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"state"
argument_list|,
name|state
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To set git commit status target url.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|targetUrl (String targetUrl)
specifier|default
name|GitHubEndpointProducerBuilder
name|targetUrl
parameter_list|(
name|String
name|targetUrl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"targetUrl"
argument_list|,
name|targetUrl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the GitHub component.      */
DECL|interface|AdvancedGitHubEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedGitHubEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GitHubEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GitHubEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGitHubEndpointProducerBuilder
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
name|AdvancedGitHubEndpointProducerBuilder
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
name|AdvancedGitHubEndpointProducerBuilder
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
name|AdvancedGitHubEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the GitHub component.      */
DECL|interface|GitHubEndpointBuilder
specifier|public
interface|interface
name|GitHubEndpointBuilder
extends|extends
name|GitHubEndpointConsumerBuilder
extends|,
name|GitHubEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGitHubEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGitHubEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What git operation to execute.          * The option is a          *<code>org.apache.camel.component.github.GitHubType</code> type.          * @group common          */
DECL|method|type (GitHubType type)
specifier|default
name|GitHubEndpointBuilder
name|type
parameter_list|(
name|GitHubType
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What git operation to execute.          * The option will be converted to a          *<code>org.apache.camel.component.github.GitHubType</code> type.          * @group common          */
DECL|method|type (String type)
specifier|default
name|GitHubEndpointBuilder
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub OAuth token, required unless username& password are provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|oauthToken (String oauthToken)
specifier|default
name|GitHubEndpointBuilder
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
comment|/**          * GitHub password, required unless oauthToken is provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|password (String password)
specifier|default
name|GitHubEndpointBuilder
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
comment|/**          * GitHub repository name.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|repoName (String repoName)
specifier|default
name|GitHubEndpointBuilder
name|repoName
parameter_list|(
name|String
name|repoName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"repoName"
argument_list|,
name|repoName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub repository owner (organization).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|repoOwner (String repoOwner)
specifier|default
name|GitHubEndpointBuilder
name|repoOwner
parameter_list|(
name|String
name|repoOwner
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"repoOwner"
argument_list|,
name|repoOwner
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * GitHub username, required unless oauthToken is provided.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|username (String username)
specifier|default
name|GitHubEndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the GitHub component.      */
DECL|interface|AdvancedGitHubEndpointBuilder
specifier|public
interface|interface
name|AdvancedGitHubEndpointBuilder
extends|extends
name|AdvancedGitHubEndpointConsumerBuilder
extends|,
name|AdvancedGitHubEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GitHubEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GitHubEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGitHubEndpointBuilder
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
name|AdvancedGitHubEndpointBuilder
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
name|AdvancedGitHubEndpointBuilder
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
name|AdvancedGitHubEndpointBuilder
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
comment|/**      * Proxy enum for<code>org.apache.camel.component.github.GitHubType</code>      * enum.      */
DECL|enum|GitHubType
specifier|public
specifier|static
enum|enum
name|GitHubType
block|{
DECL|enumConstant|CLOSEPULLREQUEST
DECL|enumConstant|PULLREQUESTCOMMENT
DECL|enumConstant|COMMIT
DECL|enumConstant|PULLREQUEST
DECL|enumConstant|TAG
DECL|enumConstant|PULLREQUESTSTATE
DECL|enumConstant|PULLREQUESTFILES
DECL|enumConstant|GETCOMMITFILE
DECL|enumConstant|CREATEISSUE
name|CLOSEPULLREQUEST
block|,
name|PULLREQUESTCOMMENT
block|,
name|COMMIT
block|,
name|PULLREQUEST
block|,
name|TAG
block|,
name|PULLREQUESTSTATE
block|,
name|PULLREQUESTFILES
block|,
name|GETCOMMITFILE
block|,
name|CREATEISSUE
block|;     }
comment|/**      * The github component is used for integrating Camel with github. Creates a      * builder to build endpoints for the GitHub component.      */
DECL|method|gitHub (String path)
specifier|default
name|GitHubEndpointBuilder
name|gitHub
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|GitHubEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|GitHubEndpointBuilder
implements|,
name|AdvancedGitHubEndpointBuilder
block|{
specifier|public
name|GitHubEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"github"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GitHubEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

