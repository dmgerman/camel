begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.queue.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|azure
operator|.
name|queue
operator|.
name|springboot
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
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|StorageCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|queue
operator|.
name|CloudQueue
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
name|component
operator|.
name|azure
operator|.
name|queue
operator|.
name|QueueServiceOperations
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The azure-queue component is used for storing and retrieving messages from  * Azure Storage Queue Service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.azure-queue"
argument_list|)
DECL|class|QueueServiceComponentConfiguration
specifier|public
class|class
name|QueueServiceComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the azure-queue component. This      * is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The Queue Service configuration      */
DECL|field|configuration
specifier|private
name|QueueServiceConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the producer should be started lazy (on the first message). By      * starting lazy you can use this to allow CamelContext and routes to      * startup in situations where a producer may otherwise fail during starting      * and cause the route to fail being started. By deferring this startup to      * be lazy then the startup failure can be handled during routing messages      * via Camel's routing error handlers. Beware that when the first message is      * processed then creating and starting the producer may take a little time      * and prolong the total processing time of the processing.      */
DECL|field|lazyStartProducer
specifier|private
name|Boolean
name|lazyStartProducer
init|=
literal|false
decl_stmt|;
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler,      * which mean any exceptions occurred while the consumer is trying to pickup      * incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler. By default the consumer will use      * the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that      * will be logged at WARN or ERROR level and ignored.      */
DECL|field|bridgeErrorHandler
specifier|private
name|Boolean
name|bridgeErrorHandler
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|QueueServiceConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( QueueServiceConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|QueueServiceConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|method|getLazyStartProducer ()
specifier|public
name|Boolean
name|getLazyStartProducer
parameter_list|()
block|{
return|return
name|lazyStartProducer
return|;
block|}
DECL|method|setLazyStartProducer (Boolean lazyStartProducer)
specifier|public
name|void
name|setLazyStartProducer
parameter_list|(
name|Boolean
name|lazyStartProducer
parameter_list|)
block|{
name|this
operator|.
name|lazyStartProducer
operator|=
name|lazyStartProducer
expr_stmt|;
block|}
DECL|method|getBridgeErrorHandler ()
specifier|public
name|Boolean
name|getBridgeErrorHandler
parameter_list|()
block|{
return|return
name|bridgeErrorHandler
return|;
block|}
DECL|method|setBridgeErrorHandler (Boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|Boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|bridgeErrorHandler
operator|=
name|bridgeErrorHandler
expr_stmt|;
block|}
DECL|class|QueueServiceConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|QueueServiceConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|azure
operator|.
name|queue
operator|.
name|QueueServiceConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * The queue resource name          */
DECL|field|queueName
specifier|private
name|String
name|queueName
decl_stmt|;
comment|/**          * The queue service client          */
DECL|field|azureQueueClient
specifier|private
name|CloudQueue
name|azureQueueClient
decl_stmt|;
comment|/**          * Queue service operation hint to the producer          */
DECL|field|operation
specifier|private
name|QueueServiceOperations
name|operation
init|=
name|QueueServiceOperations
operator|.
name|listQueues
decl_stmt|;
comment|/**          * Message Time To Live in seconds          */
DECL|field|messageTimeToLive
specifier|private
name|Integer
name|messageTimeToLive
decl_stmt|;
comment|/**          * Message Visibility Delay in seconds          */
DECL|field|messageVisibilityDelay
specifier|private
name|Integer
name|messageVisibilityDelay
decl_stmt|;
comment|/**          * Set a prefix which can be used for listing the queues          */
DECL|field|queuePrefix
specifier|private
name|String
name|queuePrefix
decl_stmt|;
comment|/**          * Set the Azure account name          */
DECL|field|accountName
specifier|private
name|String
name|accountName
decl_stmt|;
comment|/**          * Set the storage credentials, required in most cases          */
DECL|field|credentials
specifier|private
name|StorageCredentials
name|credentials
decl_stmt|;
DECL|method|getQueueName ()
specifier|public
name|String
name|getQueueName
parameter_list|()
block|{
return|return
name|queueName
return|;
block|}
DECL|method|setQueueName (String queueName)
specifier|public
name|void
name|setQueueName
parameter_list|(
name|String
name|queueName
parameter_list|)
block|{
name|this
operator|.
name|queueName
operator|=
name|queueName
expr_stmt|;
block|}
DECL|method|getAzureQueueClient ()
specifier|public
name|CloudQueue
name|getAzureQueueClient
parameter_list|()
block|{
return|return
name|azureQueueClient
return|;
block|}
DECL|method|setAzureQueueClient (CloudQueue azureQueueClient)
specifier|public
name|void
name|setAzureQueueClient
parameter_list|(
name|CloudQueue
name|azureQueueClient
parameter_list|)
block|{
name|this
operator|.
name|azureQueueClient
operator|=
name|azureQueueClient
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|QueueServiceOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (QueueServiceOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|QueueServiceOperations
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getMessageTimeToLive ()
specifier|public
name|Integer
name|getMessageTimeToLive
parameter_list|()
block|{
return|return
name|messageTimeToLive
return|;
block|}
DECL|method|setMessageTimeToLive (Integer messageTimeToLive)
specifier|public
name|void
name|setMessageTimeToLive
parameter_list|(
name|Integer
name|messageTimeToLive
parameter_list|)
block|{
name|this
operator|.
name|messageTimeToLive
operator|=
name|messageTimeToLive
expr_stmt|;
block|}
DECL|method|getMessageVisibilityDelay ()
specifier|public
name|Integer
name|getMessageVisibilityDelay
parameter_list|()
block|{
return|return
name|messageVisibilityDelay
return|;
block|}
DECL|method|setMessageVisibilityDelay (Integer messageVisibilityDelay)
specifier|public
name|void
name|setMessageVisibilityDelay
parameter_list|(
name|Integer
name|messageVisibilityDelay
parameter_list|)
block|{
name|this
operator|.
name|messageVisibilityDelay
operator|=
name|messageVisibilityDelay
expr_stmt|;
block|}
DECL|method|getQueuePrefix ()
specifier|public
name|String
name|getQueuePrefix
parameter_list|()
block|{
return|return
name|queuePrefix
return|;
block|}
DECL|method|setQueuePrefix (String queuePrefix)
specifier|public
name|void
name|setQueuePrefix
parameter_list|(
name|String
name|queuePrefix
parameter_list|)
block|{
name|this
operator|.
name|queuePrefix
operator|=
name|queuePrefix
expr_stmt|;
block|}
DECL|method|getAccountName ()
specifier|public
name|String
name|getAccountName
parameter_list|()
block|{
return|return
name|accountName
return|;
block|}
DECL|method|setAccountName (String accountName)
specifier|public
name|void
name|setAccountName
parameter_list|(
name|String
name|accountName
parameter_list|)
block|{
name|this
operator|.
name|accountName
operator|=
name|accountName
expr_stmt|;
block|}
DECL|method|getCredentials ()
specifier|public
name|StorageCredentials
name|getCredentials
parameter_list|()
block|{
return|return
name|credentials
return|;
block|}
DECL|method|setCredentials (StorageCredentials credentials)
specifier|public
name|void
name|setCredentials
parameter_list|(
name|StorageCredentials
name|credentials
parameter_list|)
block|{
name|this
operator|.
name|credentials
operator|=
name|credentials
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

