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
comment|/**  * The git component is used for working with git repositories.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|GitEndpointBuilderFactory
specifier|public
interface|interface
name|GitEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Git component.      */
DECL|interface|GitEndpointConsumerBuilder
specifier|public
interface|interface
name|GitEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGitEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGitEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Local repository path.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|localPath (String localPath)
specifier|default
name|GitEndpointConsumerBuilder
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
comment|/**          * The branch name to work on.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|branchName (String branchName)
specifier|default
name|GitEndpointConsumerBuilder
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
comment|/**          * Remote repository password.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|password (String password)
specifier|default
name|GitEndpointConsumerBuilder
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
comment|/**          * The remote repository name to use in particular operation like pull.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|remoteName (String remoteName)
specifier|default
name|GitEndpointConsumerBuilder
name|remoteName
parameter_list|(
name|String
name|remoteName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"remoteName"
argument_list|,
name|remoteName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The remote repository path.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|remotePath (String remotePath)
specifier|default
name|GitEndpointConsumerBuilder
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
comment|/**          * The tag name to work on.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|tagName (String tagName)
specifier|default
name|GitEndpointConsumerBuilder
name|tagName
parameter_list|(
name|String
name|tagName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tagName"
argument_list|,
name|tagName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Remote repository username.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|username (String username)
specifier|default
name|GitEndpointConsumerBuilder
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
name|GitEndpointConsumerBuilder
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
name|GitEndpointConsumerBuilder
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
comment|/**          * The consumer type.          *           * The option is a:          *<code>org.apache.camel.component.git.consumer.GitType</code> type.          *           * Group: consumer          */
DECL|method|type (GitType type)
specifier|default
name|GitEndpointConsumerBuilder
name|type
parameter_list|(
name|GitType
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
comment|/**          * The consumer type.          *           * The option will be converted to a          *<code>org.apache.camel.component.git.consumer.GitType</code> type.          *           * Group: consumer          */
DECL|method|type (String type)
specifier|default
name|GitEndpointConsumerBuilder
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
block|}
comment|/**      * Advanced builder for endpoint consumers for the Git component.      */
DECL|interface|AdvancedGitEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedGitEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GitEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GitEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedGitEndpointConsumerBuilder
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
name|AdvancedGitEndpointConsumerBuilder
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
name|AdvancedGitEndpointConsumerBuilder
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
name|AdvancedGitEndpointConsumerBuilder
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
name|AdvancedGitEndpointConsumerBuilder
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
name|AdvancedGitEndpointConsumerBuilder
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
name|AdvancedGitEndpointConsumerBuilder
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
name|AdvancedGitEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Git component.      */
DECL|interface|GitEndpointProducerBuilder
specifier|public
interface|interface
name|GitEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGitEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGitEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Local repository path.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|localPath (String localPath)
specifier|default
name|GitEndpointProducerBuilder
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
comment|/**          * The branch name to work on.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|branchName (String branchName)
specifier|default
name|GitEndpointProducerBuilder
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
comment|/**          * Remote repository password.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|password (String password)
specifier|default
name|GitEndpointProducerBuilder
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
comment|/**          * The remote repository name to use in particular operation like pull.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|remoteName (String remoteName)
specifier|default
name|GitEndpointProducerBuilder
name|remoteName
parameter_list|(
name|String
name|remoteName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"remoteName"
argument_list|,
name|remoteName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The remote repository path.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|remotePath (String remotePath)
specifier|default
name|GitEndpointProducerBuilder
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
comment|/**          * The tag name to work on.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|tagName (String tagName)
specifier|default
name|GitEndpointProducerBuilder
name|tagName
parameter_list|(
name|String
name|tagName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tagName"
argument_list|,
name|tagName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Remote repository username.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|username (String username)
specifier|default
name|GitEndpointProducerBuilder
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
comment|/**          * The flag to manage empty git commits.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|allowEmpty (boolean allowEmpty)
specifier|default
name|GitEndpointProducerBuilder
name|allowEmpty
parameter_list|(
name|boolean
name|allowEmpty
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"allowEmpty"
argument_list|,
name|allowEmpty
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The flag to manage empty git commits.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|allowEmpty (String allowEmpty)
specifier|default
name|GitEndpointProducerBuilder
name|allowEmpty
parameter_list|(
name|String
name|allowEmpty
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"allowEmpty"
argument_list|,
name|allowEmpty
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|GitEndpointProducerBuilder
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
name|GitEndpointProducerBuilder
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
comment|/**          * The operation to do on the repository.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|operation (String operation)
specifier|default
name|GitEndpointProducerBuilder
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
block|}
comment|/**      * Advanced builder for endpoint producers for the Git component.      */
DECL|interface|AdvancedGitEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedGitEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GitEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GitEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGitEndpointProducerBuilder
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
name|AdvancedGitEndpointProducerBuilder
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
name|AdvancedGitEndpointProducerBuilder
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
name|AdvancedGitEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Git component.      */
DECL|interface|GitEndpointBuilder
specifier|public
interface|interface
name|GitEndpointBuilder
extends|extends
name|GitEndpointConsumerBuilder
extends|,
name|GitEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGitEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGitEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Local repository path.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|localPath (String localPath)
specifier|default
name|GitEndpointBuilder
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
comment|/**          * The branch name to work on.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|branchName (String branchName)
specifier|default
name|GitEndpointBuilder
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
comment|/**          * Remote repository password.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|password (String password)
specifier|default
name|GitEndpointBuilder
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
comment|/**          * The remote repository name to use in particular operation like pull.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|remoteName (String remoteName)
specifier|default
name|GitEndpointBuilder
name|remoteName
parameter_list|(
name|String
name|remoteName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"remoteName"
argument_list|,
name|remoteName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The remote repository path.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|remotePath (String remotePath)
specifier|default
name|GitEndpointBuilder
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
comment|/**          * The tag name to work on.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|tagName (String tagName)
specifier|default
name|GitEndpointBuilder
name|tagName
parameter_list|(
name|String
name|tagName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tagName"
argument_list|,
name|tagName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Remote repository username.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|username (String username)
specifier|default
name|GitEndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the Git component.      */
DECL|interface|AdvancedGitEndpointBuilder
specifier|public
interface|interface
name|AdvancedGitEndpointBuilder
extends|extends
name|AdvancedGitEndpointConsumerBuilder
extends|,
name|AdvancedGitEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GitEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GitEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGitEndpointBuilder
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
name|AdvancedGitEndpointBuilder
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
name|AdvancedGitEndpointBuilder
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
name|AdvancedGitEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.git.consumer.GitType</code> enum.      */
DECL|enum|GitType
enum|enum
name|GitType
block|{
DECL|enumConstant|COMMIT
name|COMMIT
block|,
DECL|enumConstant|TAG
name|TAG
block|,
DECL|enumConstant|BRANCH
name|BRANCH
block|;     }
comment|/**      * The git component is used for working with git repositories.      * Maven coordinates: org.apache.camel:camel-git      */
DECL|method|git (String path)
specifier|default
name|GitEndpointBuilder
name|git
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|GitEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|GitEndpointBuilder
implements|,
name|AdvancedGitEndpointBuilder
block|{
specifier|public
name|GitEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"git"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GitEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

