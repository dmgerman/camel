begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotations
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
name|support
operator|.
name|component
operator|.
name|AbstractApiComponent
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
name|IOHelper
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
comment|/**  * The Zendesk Component.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"zendesk"
argument_list|)
DECL|class|ZendeskComponent
specifier|public
class|class
name|ZendeskComponent
extends|extends
name|AbstractApiComponent
argument_list|<
name|ZendeskApiName
argument_list|,
name|ZendeskConfiguration
argument_list|,
name|ZendeskApiCollection
argument_list|>
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|zendesk
name|Zendesk
name|zendesk
decl_stmt|;
DECL|method|ZendeskComponent ()
specifier|public
name|ZendeskComponent
parameter_list|()
block|{
name|super
argument_list|(
name|ZendeskEndpoint
operator|.
name|class
argument_list|,
name|ZendeskApiName
operator|.
name|class
argument_list|,
name|ZendeskApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ZendeskComponent (CamelContext context)
specifier|public
name|ZendeskComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|ZendeskEndpoint
operator|.
name|class
argument_list|,
name|ZendeskApiName
operator|.
name|class
argument_list|,
name|ZendeskApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiName (String apiNameStr)
specifier|protected
name|ZendeskApiName
name|getApiName
parameter_list|(
name|String
name|apiNameStr
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|ZendeskApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
return|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|setConfiguration (ZendeskConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ZendeskConfiguration
name|configuration
parameter_list|)
block|{
name|super
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|ZendeskConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
comment|/**      * To use a shared {@link Zendesk} instance.      *       * @return the shared Zendesk instance      */
DECL|method|getZendesk ()
specifier|public
name|Zendesk
name|getZendesk
parameter_list|()
block|{
return|return
name|zendesk
return|;
block|}
DECL|method|setZendesk (Zendesk zendesk)
specifier|public
name|void
name|setZendesk
parameter_list|(
name|Zendesk
name|zendesk
parameter_list|)
block|{
name|this
operator|.
name|zendesk
operator|=
name|zendesk
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String methodName, ZendeskApiName apiName, ZendeskConfiguration endpointConfiguration)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|methodName
parameter_list|,
name|ZendeskApiName
name|apiName
parameter_list|,
name|ZendeskConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|endpointConfiguration
operator|.
name|setMethodName
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
return|return
operator|new
name|ZendeskEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|endpointConfiguration
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
name|zendesk
operator|==
literal|null
operator|&&
name|configuration
operator|!=
literal|null
condition|)
block|{
name|zendesk
operator|=
name|ZendeskHelper
operator|.
name|create
argument_list|(
name|configuration
argument_list|)
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
name|IOHelper
operator|.
name|close
argument_list|(
name|zendesk
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|public
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|zendesk
argument_list|)
expr_stmt|;
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

