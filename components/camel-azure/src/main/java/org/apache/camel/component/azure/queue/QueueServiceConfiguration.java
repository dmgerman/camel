begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.queue
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
package|;
end_package

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
name|common
operator|.
name|AbstractConfiguration
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
name|UriParam
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
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|QueueServiceConfiguration
specifier|public
class|class
name|QueueServiceConfiguration
extends|extends
name|AbstractConfiguration
block|{
DECL|field|queueName
specifier|private
name|String
name|queueName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|azureQueueClient
specifier|private
name|CloudQueue
name|azureQueueClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"listQueues"
argument_list|)
DECL|field|operation
specifier|private
name|QueueServiceOperations
name|operation
init|=
name|QueueServiceOperations
operator|.
name|listQueues
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|messageTimeToLive
specifier|private
name|int
name|messageTimeToLive
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|messageVisibilityDelay
specifier|private
name|int
name|messageVisibilityDelay
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|queuePrefix
specifier|private
name|String
name|queuePrefix
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
comment|/**      * The queue resource name      */
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
comment|/**      * The queue service client      */
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
comment|/**      * Queue service operation hint to the producer      */
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
name|int
name|getMessageTimeToLive
parameter_list|()
block|{
return|return
name|messageTimeToLive
return|;
block|}
comment|/**      * Message Time To Live in seconds      */
DECL|method|setMessageTimeToLive (int messageTimeToLive)
specifier|public
name|void
name|setMessageTimeToLive
parameter_list|(
name|int
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
name|int
name|getMessageVisibilityDelay
parameter_list|()
block|{
return|return
name|messageVisibilityDelay
return|;
block|}
comment|/**      * Message Visibility Delay in seconds      */
DECL|method|setMessageVisibilityDelay (int messageVisibilityDelay)
specifier|public
name|void
name|setMessageVisibilityDelay
parameter_list|(
name|int
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
comment|/**      * Set a prefix which can be used for listing the queues      */
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
block|}
end_class

end_unit

