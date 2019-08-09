begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.webhook
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
package|;
end_package

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
name|DelegateEndpoint
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
name|Endpoint
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
name|spi
operator|.
name|RestConsumerFactory
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
name|UriEndpoint
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The webhook component allows other Camel components that can receive push notifications to expose  * webhook endpoints and automatically register them with their own webhook provider.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"3.0.0"
argument_list|,
name|scheme
operator|=
literal|"webhook"
argument_list|,
name|title
operator|=
literal|"Webhook"
argument_list|,
name|syntax
operator|=
literal|"webhook:endpointUri"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"cloud"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|WebhookEndpoint
specifier|public
class|class
name|WebhookEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|DelegateEndpoint
block|{
DECL|field|delegateEndpoint
specifier|private
name|WebhookCapableEndpoint
name|delegateEndpoint
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|WebhookConfiguration
name|configuration
decl_stmt|;
DECL|method|WebhookEndpoint (String uri, WebhookComponent component, WebhookConfiguration configuration, String delegateUri)
specifier|public
name|WebhookEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|WebhookComponent
name|component
parameter_list|,
name|WebhookConfiguration
name|configuration
parameter_list|,
name|String
name|delegateUri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|Endpoint
name|delegate
init|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|delegateUri
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|delegate
operator|instanceof
name|WebhookCapableEndpoint
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The provided endpoint is not capable of being used in webhook mode: "
operator|+
name|delegateUri
argument_list|)
throw|;
block|}
name|delegateEndpoint
operator|=
operator|(
name|WebhookCapableEndpoint
operator|)
name|delegate
expr_stmt|;
name|delegateEndpoint
operator|.
name|setWebhookConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot create a producer with the webhook endpoint."
argument_list|)
throw|;
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
name|RestConsumerFactory
name|factory
init|=
name|WebhookUtils
operator|.
name|locateRestConsumerFactory
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|configuration
operator|.
name|computeFullPath
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|String
name|serverUrl
init|=
name|configuration
operator|.
name|computeServerUriPrefix
argument_list|()
decl_stmt|;
name|String
name|url
init|=
name|serverUrl
operator|+
name|path
decl_stmt|;
name|Processor
name|handler
init|=
name|delegateEndpoint
operator|.
name|createWebhookHandler
argument_list|(
name|processor
argument_list|)
decl_stmt|;
return|return
operator|new
name|MultiRestConsumer
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|factory
argument_list|,
name|this
argument_list|,
name|handler
argument_list|,
name|delegateEndpoint
operator|.
name|getWebhookMethods
argument_list|()
argument_list|,
name|url
argument_list|,
name|path
argument_list|,
name|configuration
operator|.
name|getRestConfiguration
argument_list|()
argument_list|,
name|this
operator|::
name|configureConsumer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isWebhookAutoRegister
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Registering webhook for endpoint {}"
argument_list|,
name|delegateEndpoint
argument_list|)
expr_stmt|;
name|delegateEndpoint
operator|.
name|registerWebhook
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isWebhookAutoRegister
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Unregistering webhook for endpoint {}"
argument_list|,
name|delegateEndpoint
argument_list|)
expr_stmt|;
name|delegateEndpoint
operator|.
name|unregisterWebhook
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getConfiguration ()
specifier|public
name|WebhookConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|WebhookCapableEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|delegateEndpoint
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

