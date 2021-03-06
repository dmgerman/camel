begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.webhook.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|webhook
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|webhook
operator|.
name|WebhookCapableEndpoint
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
name|webhook
operator|.
name|WebhookConfiguration
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * A test endpoint for testing webhook capabilities  */
end_comment

begin_class
DECL|class|TestEndpoint
specifier|public
class|class
name|TestEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|WebhookCapableEndpoint
block|{
DECL|field|DEFAULT_METHOD
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_METHOD
init|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"POST"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|webhookHandler
specifier|private
name|Function
argument_list|<
name|Processor
argument_list|,
name|Processor
argument_list|>
name|webhookHandler
decl_stmt|;
DECL|field|register
specifier|private
name|Runnable
name|register
decl_stmt|;
DECL|field|unregister
specifier|private
name|Runnable
name|unregister
decl_stmt|;
DECL|field|methods
specifier|private
name|Supplier
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|methods
decl_stmt|;
DECL|field|producer
specifier|private
name|Supplier
argument_list|<
name|Producer
argument_list|>
name|producer
decl_stmt|;
DECL|field|consumer
specifier|private
name|Function
argument_list|<
name|Processor
argument_list|,
name|Consumer
argument_list|>
name|consumer
decl_stmt|;
DECL|field|webhookConfiguration
specifier|private
name|WebhookConfiguration
name|webhookConfiguration
decl_stmt|;
DECL|field|singleton
specifier|private
name|boolean
name|singleton
decl_stmt|;
DECL|method|TestEndpoint (String endpointUri, Component component)
specifier|public
name|TestEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createWebhookHandler (Processor next)
specifier|public
name|Processor
name|createWebhookHandler
parameter_list|(
name|Processor
name|next
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|webhookHandler
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|webhookHandler
operator|.
name|apply
argument_list|(
name|next
argument_list|)
return|;
block|}
return|return
name|next
return|;
block|}
annotation|@
name|Override
DECL|method|registerWebhook ()
specifier|public
name|void
name|registerWebhook
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|register
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|register
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|unregisterWebhook ()
specifier|public
name|void
name|unregisterWebhook
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|unregister
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|unregister
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setWebhookConfiguration (WebhookConfiguration webhookConfiguration)
specifier|public
name|void
name|setWebhookConfiguration
parameter_list|(
name|WebhookConfiguration
name|webhookConfiguration
parameter_list|)
block|{
name|this
operator|.
name|webhookConfiguration
operator|=
name|webhookConfiguration
expr_stmt|;
block|}
DECL|method|getWebhookConfiguration ()
specifier|public
name|WebhookConfiguration
name|getWebhookConfiguration
parameter_list|()
block|{
return|return
name|webhookConfiguration
return|;
block|}
annotation|@
name|Override
DECL|method|getWebhookMethods ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getWebhookMethods
parameter_list|()
block|{
return|return
name|this
operator|.
name|methods
operator|!=
literal|null
condition|?
name|this
operator|.
name|methods
operator|.
name|get
argument_list|()
else|:
name|DEFAULT_METHOD
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|producer
operator|!=
literal|null
condition|?
name|this
operator|.
name|producer
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|consumer
operator|!=
literal|null
condition|?
name|this
operator|.
name|consumer
operator|.
name|apply
argument_list|(
name|processor
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|singleton
return|;
block|}
DECL|method|setSingleton (boolean singleton)
specifier|public
name|void
name|setSingleton
parameter_list|(
name|boolean
name|singleton
parameter_list|)
block|{
name|this
operator|.
name|singleton
operator|=
name|singleton
expr_stmt|;
block|}
DECL|method|setWebhookHandler (Function<Processor, Processor> webhookHandler)
specifier|public
name|void
name|setWebhookHandler
parameter_list|(
name|Function
argument_list|<
name|Processor
argument_list|,
name|Processor
argument_list|>
name|webhookHandler
parameter_list|)
block|{
name|this
operator|.
name|webhookHandler
operator|=
name|webhookHandler
expr_stmt|;
block|}
DECL|method|setRegisterWebhook (Runnable register)
specifier|public
name|void
name|setRegisterWebhook
parameter_list|(
name|Runnable
name|register
parameter_list|)
block|{
name|this
operator|.
name|register
operator|=
name|register
expr_stmt|;
block|}
DECL|method|setUnregisterWebhook (Runnable unregister)
specifier|public
name|void
name|setUnregisterWebhook
parameter_list|(
name|Runnable
name|unregister
parameter_list|)
block|{
name|this
operator|.
name|unregister
operator|=
name|unregister
expr_stmt|;
block|}
DECL|method|setWebhookMethods (Supplier<List<String>> methods)
specifier|public
name|void
name|setWebhookMethods
parameter_list|(
name|Supplier
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|methods
parameter_list|)
block|{
name|this
operator|.
name|methods
operator|=
name|methods
expr_stmt|;
block|}
DECL|method|setWebhookProducer (Supplier<Producer> producer)
specifier|public
name|void
name|setWebhookProducer
parameter_list|(
name|Supplier
argument_list|<
name|Producer
argument_list|>
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
DECL|method|setConsumer (Function<Processor, Consumer> consumer)
specifier|public
name|void
name|setConsumer
parameter_list|(
name|Function
argument_list|<
name|Processor
argument_list|,
name|Consumer
argument_list|>
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
block|}
end_class

end_unit

