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
comment|/**  * The azure-queue component is used for storing and retrieving messages from  * Azure Storage Queue Service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|QueueServiceEndpointBuilderFactory
specifier|public
interface|interface
name|QueueServiceEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the Azure Storage Queue Service      * component.      */
DECL|interface|QueueServiceEndpointConsumerBuilder
specifier|public
interface|interface
name|QueueServiceEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedQueueServiceEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedQueueServiceEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Container Queue compact Uri.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|containerAndQueueUri ( String containerAndQueueUri)
specifier|default
name|QueueServiceEndpointConsumerBuilder
name|containerAndQueueUri
parameter_list|(
name|String
name|containerAndQueueUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"containerAndQueueUri"
argument_list|,
name|containerAndQueueUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The queue service client.          * The option is a          *<code>com.microsoft.azure.storage.queue.CloudQueue</code> type.          * @group common          */
DECL|method|azureQueueClient ( Object azureQueueClient)
specifier|default
name|QueueServiceEndpointConsumerBuilder
name|azureQueueClient
parameter_list|(
name|Object
name|azureQueueClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"azureQueueClient"
argument_list|,
name|azureQueueClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The queue service client.          * The option will be converted to a          *<code>com.microsoft.azure.storage.queue.CloudQueue</code> type.          * @group common          */
DECL|method|azureQueueClient ( String azureQueueClient)
specifier|default
name|QueueServiceEndpointConsumerBuilder
name|azureQueueClient
parameter_list|(
name|String
name|azureQueueClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"azureQueueClient"
argument_list|,
name|azureQueueClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the storage credentials, required in most cases.          * The option is a          *<code>com.microsoft.azure.storage.StorageCredentials</code> type.          * @group common          */
DECL|method|credentials ( Object credentials)
specifier|default
name|QueueServiceEndpointConsumerBuilder
name|credentials
parameter_list|(
name|Object
name|credentials
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"credentials"
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the storage credentials, required in most cases.          * The option will be converted to a          *<code>com.microsoft.azure.storage.StorageCredentials</code> type.          * @group common          */
DECL|method|credentials ( String credentials)
specifier|default
name|QueueServiceEndpointConsumerBuilder
name|credentials
parameter_list|(
name|String
name|credentials
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"credentials"
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|QueueServiceEndpointConsumerBuilder
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
name|QueueServiceEndpointConsumerBuilder
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
comment|/**      * Advanced builder for endpoint consumers for the Azure Storage Queue      * Service component.      */
DECL|interface|AdvancedQueueServiceEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedQueueServiceEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|QueueServiceEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|QueueServiceEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedQueueServiceEndpointConsumerBuilder
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
name|AdvancedQueueServiceEndpointConsumerBuilder
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
name|AdvancedQueueServiceEndpointConsumerBuilder
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
name|AdvancedQueueServiceEndpointConsumerBuilder
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
name|AdvancedQueueServiceEndpointConsumerBuilder
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
name|AdvancedQueueServiceEndpointConsumerBuilder
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
name|AdvancedQueueServiceEndpointConsumerBuilder
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
name|AdvancedQueueServiceEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the Azure Storage Queue Service      * component.      */
DECL|interface|QueueServiceEndpointProducerBuilder
specifier|public
specifier|static
interface|interface
name|QueueServiceEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedQueueServiceEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedQueueServiceEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Container Queue compact Uri.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|containerAndQueueUri ( String containerAndQueueUri)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|containerAndQueueUri
parameter_list|(
name|String
name|containerAndQueueUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"containerAndQueueUri"
argument_list|,
name|containerAndQueueUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The queue service client.          * The option is a          *<code>com.microsoft.azure.storage.queue.CloudQueue</code> type.          * @group common          */
DECL|method|azureQueueClient ( Object azureQueueClient)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|azureQueueClient
parameter_list|(
name|Object
name|azureQueueClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"azureQueueClient"
argument_list|,
name|azureQueueClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The queue service client.          * The option will be converted to a          *<code>com.microsoft.azure.storage.queue.CloudQueue</code> type.          * @group common          */
DECL|method|azureQueueClient ( String azureQueueClient)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|azureQueueClient
parameter_list|(
name|String
name|azureQueueClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"azureQueueClient"
argument_list|,
name|azureQueueClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the storage credentials, required in most cases.          * The option is a          *<code>com.microsoft.azure.storage.StorageCredentials</code> type.          * @group common          */
DECL|method|credentials ( Object credentials)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|credentials
parameter_list|(
name|Object
name|credentials
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"credentials"
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the storage credentials, required in most cases.          * The option will be converted to a          *<code>com.microsoft.azure.storage.StorageCredentials</code> type.          * @group common          */
DECL|method|credentials ( String credentials)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|credentials
parameter_list|(
name|String
name|credentials
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"credentials"
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|QueueServiceEndpointProducerBuilder
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
name|QueueServiceEndpointProducerBuilder
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
comment|/**          * Message Time To Live in seconds.          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|messageTimeToLive ( int messageTimeToLive)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|messageTimeToLive
parameter_list|(
name|int
name|messageTimeToLive
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"messageTimeToLive"
argument_list|,
name|messageTimeToLive
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Message Time To Live in seconds.          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|messageTimeToLive ( String messageTimeToLive)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|messageTimeToLive
parameter_list|(
name|String
name|messageTimeToLive
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"messageTimeToLive"
argument_list|,
name|messageTimeToLive
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Message Visibility Delay in seconds.          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|messageVisibilityDelay ( int messageVisibilityDelay)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|messageVisibilityDelay
parameter_list|(
name|int
name|messageVisibilityDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"messageVisibilityDelay"
argument_list|,
name|messageVisibilityDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Message Visibility Delay in seconds.          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|messageVisibilityDelay ( String messageVisibilityDelay)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|messageVisibilityDelay
parameter_list|(
name|String
name|messageVisibilityDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"messageVisibilityDelay"
argument_list|,
name|messageVisibilityDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Queue service operation hint to the producer.          * The option is a          *<code>org.apache.camel.component.azure.queue.QueueServiceOperations</code> type.          * @group producer          */
DECL|method|operation ( QueueServiceOperations operation)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|operation
parameter_list|(
name|QueueServiceOperations
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
comment|/**          * Queue service operation hint to the producer.          * The option will be converted to a          *<code>org.apache.camel.component.azure.queue.QueueServiceOperations</code> type.          * @group producer          */
DECL|method|operation (String operation)
specifier|default
name|QueueServiceEndpointProducerBuilder
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
comment|/**          * Set a prefix which can be used for listing the queues.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|queuePrefix ( String queuePrefix)
specifier|default
name|QueueServiceEndpointProducerBuilder
name|queuePrefix
parameter_list|(
name|String
name|queuePrefix
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"queuePrefix"
argument_list|,
name|queuePrefix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the Azure Storage Queue      * Service component.      */
DECL|interface|AdvancedQueueServiceEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedQueueServiceEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|QueueServiceEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|QueueServiceEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedQueueServiceEndpointProducerBuilder
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
name|AdvancedQueueServiceEndpointProducerBuilder
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
name|AdvancedQueueServiceEndpointProducerBuilder
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
name|AdvancedQueueServiceEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the Azure Storage Queue Service component.      */
DECL|interface|QueueServiceEndpointBuilder
specifier|public
specifier|static
interface|interface
name|QueueServiceEndpointBuilder
extends|extends
name|QueueServiceEndpointConsumerBuilder
extends|,
name|QueueServiceEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedQueueServiceEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedQueueServiceEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Container Queue compact Uri.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|containerAndQueueUri ( String containerAndQueueUri)
specifier|default
name|QueueServiceEndpointBuilder
name|containerAndQueueUri
parameter_list|(
name|String
name|containerAndQueueUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"containerAndQueueUri"
argument_list|,
name|containerAndQueueUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The queue service client.          * The option is a          *<code>com.microsoft.azure.storage.queue.CloudQueue</code> type.          * @group common          */
DECL|method|azureQueueClient ( Object azureQueueClient)
specifier|default
name|QueueServiceEndpointBuilder
name|azureQueueClient
parameter_list|(
name|Object
name|azureQueueClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"azureQueueClient"
argument_list|,
name|azureQueueClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The queue service client.          * The option will be converted to a          *<code>com.microsoft.azure.storage.queue.CloudQueue</code> type.          * @group common          */
DECL|method|azureQueueClient ( String azureQueueClient)
specifier|default
name|QueueServiceEndpointBuilder
name|azureQueueClient
parameter_list|(
name|String
name|azureQueueClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"azureQueueClient"
argument_list|,
name|azureQueueClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the storage credentials, required in most cases.          * The option is a          *<code>com.microsoft.azure.storage.StorageCredentials</code> type.          * @group common          */
DECL|method|credentials (Object credentials)
specifier|default
name|QueueServiceEndpointBuilder
name|credentials
parameter_list|(
name|Object
name|credentials
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"credentials"
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the storage credentials, required in most cases.          * The option will be converted to a          *<code>com.microsoft.azure.storage.StorageCredentials</code> type.          * @group common          */
DECL|method|credentials (String credentials)
specifier|default
name|QueueServiceEndpointBuilder
name|credentials
parameter_list|(
name|String
name|credentials
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"credentials"
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Azure Storage Queue Service      * component.      */
DECL|interface|AdvancedQueueServiceEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedQueueServiceEndpointBuilder
extends|extends
name|AdvancedQueueServiceEndpointConsumerBuilder
extends|,
name|AdvancedQueueServiceEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|QueueServiceEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|QueueServiceEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedQueueServiceEndpointBuilder
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
name|AdvancedQueueServiceEndpointBuilder
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
name|AdvancedQueueServiceEndpointBuilder
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
name|AdvancedQueueServiceEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.azure.queue.QueueServiceOperations</code> enum.      */
DECL|enum|QueueServiceOperations
specifier|public
specifier|static
enum|enum
name|QueueServiceOperations
block|{
DECL|enumConstant|listQueues
DECL|enumConstant|createQueue
DECL|enumConstant|deleteQueue
DECL|enumConstant|addMessage
DECL|enumConstant|retrieveMessage
DECL|enumConstant|peekMessage
DECL|enumConstant|updateMessage
DECL|enumConstant|deleteMessage
name|listQueues
block|,
name|createQueue
block|,
name|deleteQueue
block|,
name|addMessage
block|,
name|retrieveMessage
block|,
name|peekMessage
block|,
name|updateMessage
block|,
name|deleteMessage
block|;     }
comment|/**      * The azure-queue component is used for storing and retrieving messages      * from Azure Storage Queue Service. Creates a builder to build endpoints      * for the Azure Storage Queue Service component.      */
DECL|method|queueService (String path)
specifier|default
name|QueueServiceEndpointBuilder
name|queueService
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|QueueServiceEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|QueueServiceEndpointBuilder
implements|,
name|AdvancedQueueServiceEndpointBuilder
block|{
specifier|public
name|QueueServiceEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"azure-queue"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|QueueServiceEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

