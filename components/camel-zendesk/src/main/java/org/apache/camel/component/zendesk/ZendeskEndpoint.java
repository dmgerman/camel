begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zendesk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zendesk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|zendesk
operator|.
name|internal
operator|.
name|ZendeskApiCollection
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
name|zendesk
operator|.
name|internal
operator|.
name|ZendeskApiName
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
name|zendesk
operator|.
name|internal
operator|.
name|ZendeskConstants
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
name|zendesk
operator|.
name|internal
operator|.
name|ZendeskHelper
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
name|zendesk
operator|.
name|internal
operator|.
name|ZendeskPropertiesHelper
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
name|impl
operator|.
name|DefaultEndpoint
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
name|Metadata
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
name|spi
operator|.
name|UriPath
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|component
operator|.
name|AbstractApiEndpoint
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
name|util
operator|.
name|component
operator|.
name|ApiMethod
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
name|util
operator|.
name|component
operator|.
name|ApiMethodPropertiesHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|zendesk
operator|.
name|client
operator|.
name|v2
operator|.
name|Zendesk
import|;
end_import

begin_comment
comment|/**  * The zendesk endpoint interacts with the Zendesk server.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.19.0"
argument_list|,
name|scheme
operator|=
literal|"zendesk"
argument_list|,
name|title
operator|=
literal|"Zendesk"
argument_list|,
name|syntax
operator|=
literal|"zendesk:methodName"
argument_list|,
name|consumerClass
operator|=
name|ZendeskConsumer
operator|.
name|class
argument_list|,
name|consumerPrefix
operator|=
literal|"consumer"
argument_list|,
name|label
operator|=
literal|"api,support,cloud"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|ZendeskEndpoint
specifier|public
class|class
name|ZendeskEndpoint
extends|extends
name|AbstractApiEndpoint
argument_list|<
name|ZendeskApiName
argument_list|,
name|ZendeskConfiguration
argument_list|>
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|ZendeskConfiguration
name|configuration
decl_stmt|;
DECL|field|apiProxy
specifier|private
name|Zendesk
name|apiProxy
decl_stmt|;
DECL|method|ZendeskEndpoint (String uri, ZendeskComponent component, ZendeskApiName apiName, String methodName, ZendeskConfiguration endpointConfiguration)
specifier|public
name|ZendeskEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|ZendeskComponent
name|component
parameter_list|,
name|ZendeskApiName
name|apiName
parameter_list|,
name|String
name|methodName
parameter_list|,
name|ZendeskConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|ZendeskApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getHelper
argument_list|(
name|apiName
argument_list|)
argument_list|,
name|endpointConfiguration
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpointConfiguration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|ZendeskComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|ZendeskComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ZendeskProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
comment|// make sure inBody is not set for consumers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option inBody is not supported for consumer endpoint"
argument_list|)
throw|;
block|}
specifier|final
name|ZendeskConsumer
name|consumer
init|=
operator|new
name|ZendeskConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
comment|// also set consumer.* properties
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|apiProxy
operator|!=
literal|null
operator|&&
operator|!
name|apiProxy
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|apiProxy
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPropertiesHelper ()
specifier|protected
name|ApiMethodPropertiesHelper
argument_list|<
name|ZendeskConfiguration
argument_list|>
name|getPropertiesHelper
parameter_list|()
block|{
return|return
name|ZendeskPropertiesHelper
operator|.
name|getHelper
argument_list|()
return|;
block|}
DECL|method|getThreadProfileName ()
specifier|protected
name|String
name|getThreadProfileName
parameter_list|()
block|{
return|return
name|ZendeskConstants
operator|.
name|THREAD_PROFILE_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfigureProperties ()
specifier|protected
name|void
name|afterConfigureProperties
parameter_list|()
block|{
comment|// create connection eagerly, a good way to validate configuration
name|getZendesk
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiProxy (ApiMethod method, Map<String, Object> args)
specifier|public
name|Object
name|getApiProxy
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
block|{
return|return
name|getZendesk
argument_list|()
return|;
block|}
DECL|method|getZendesk ()
specifier|private
name|Zendesk
name|getZendesk
parameter_list|()
block|{
if|if
condition|(
name|apiProxy
operator|==
literal|null
condition|)
block|{
name|apiProxy
operator|=
name|ZendeskHelper
operator|.
name|create
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
return|return
name|apiProxy
return|;
block|}
block|}
end_class

end_unit

