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
name|java
operator|.
name|util
operator|.
name|Set
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
name|CamelContext
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
name|NoSuchBeanException
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

begin_comment
comment|/**  * Some utilities for the webhook component.  */
end_comment

begin_class
DECL|class|WebhookUtils
specifier|public
specifier|final
class|class
name|WebhookUtils
block|{
DECL|field|DEFAULT_REST_CONSUMER_COMPONENTS
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|DEFAULT_REST_CONSUMER_COMPONENTS
init|=
operator|new
name|String
index|[]
block|{
literal|"coap"
block|,
literal|"netty-http"
block|,
literal|"jetty"
block|,
literal|"servlet"
block|,
literal|"undertow"
block|}
decl_stmt|;
DECL|method|WebhookUtils ()
specifier|private
name|WebhookUtils
parameter_list|()
block|{     }
comment|/**      * Used to locate the most suitable {@code RestConsumerFactory}.      */
DECL|method|locateRestConsumerFactory (CamelContext context, WebhookConfiguration configuration)
specifier|public
specifier|static
name|RestConsumerFactory
name|locateRestConsumerFactory
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|WebhookConfiguration
name|configuration
parameter_list|)
block|{
name|RestConsumerFactory
name|factory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getWebhookComponentName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|comp
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|configuration
operator|.
name|getWebhookComponentName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|instanceof
name|RestConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestConsumerFactory
operator|)
name|comp
expr_stmt|;
block|}
else|else
block|{
name|comp
operator|=
name|context
operator|.
name|getComponent
argument_list|(
name|configuration
operator|.
name|getWebhookComponentName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|comp
operator|instanceof
name|RestConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestConsumerFactory
operator|)
name|comp
expr_stmt|;
block|}
block|}
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|comp
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Component "
operator|+
name|configuration
operator|.
name|getWebhookComponentName
argument_list|()
operator|+
literal|" is not a RestConsumerFactory"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|configuration
operator|.
name|getWebhookComponentName
argument_list|()
argument_list|,
name|RestConsumerFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
comment|// try all components
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|context
operator|.
name|getComponentNames
argument_list|()
control|)
block|{
name|Component
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|instanceof
name|RestConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestConsumerFactory
operator|)
name|comp
expr_stmt|;
break|break;
block|}
block|}
block|}
comment|// lookup in registry
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|Set
argument_list|<
name|RestConsumerFactory
argument_list|>
name|factories
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|RestConsumerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|factories
operator|!=
literal|null
operator|&&
name|factories
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|factory
operator|=
name|factories
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
comment|// no explicit factory found then try to see if we can find any of the default rest consumer components
comment|// and there must only be exactly one so we safely can pick this one
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|RestConsumerFactory
name|found
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|DEFAULT_REST_CONSUMER_COMPONENTS
control|)
block|{
name|Object
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
name|name
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|instanceof
name|RestConsumerFactory
condition|)
block|{
if|if
condition|(
name|found
operator|==
literal|null
condition|)
block|{
name|found
operator|=
operator|(
name|RestConsumerFactory
operator|)
name|comp
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple RestConsumerFactory found on classpath. Configure explicit which component to use"
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
name|factory
operator|=
name|found
expr_stmt|;
block|}
block|}
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find RestConsumerFactory in Registry or as a Component to use"
argument_list|)
throw|;
block|}
return|return
name|factory
return|;
block|}
block|}
end_class

end_unit

