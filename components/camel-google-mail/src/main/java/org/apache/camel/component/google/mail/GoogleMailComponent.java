begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|Gmail
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
name|google
operator|.
name|mail
operator|.
name|internal
operator|.
name|GoogleMailApiCollection
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
name|google
operator|.
name|mail
operator|.
name|internal
operator|.
name|GoogleMailApiName
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
name|support
operator|.
name|component
operator|.
name|AbstractApiComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link GoogleMailEndpoint}.  */
end_comment

begin_class
DECL|class|GoogleMailComponent
specifier|public
class|class
name|GoogleMailComponent
extends|extends
name|AbstractApiComponent
argument_list|<
name|GoogleMailApiName
argument_list|,
name|GoogleMailConfiguration
argument_list|,
name|GoogleMailApiCollection
argument_list|>
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|client
specifier|private
name|Gmail
name|client
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|clientFactory
specifier|private
name|GoogleMailClientFactory
name|clientFactory
decl_stmt|;
DECL|method|GoogleMailComponent ()
specifier|public
name|GoogleMailComponent
parameter_list|()
block|{
name|super
argument_list|(
name|GoogleMailEndpoint
operator|.
name|class
argument_list|,
name|GoogleMailApiName
operator|.
name|class
argument_list|,
name|GoogleMailApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
name|registerExtension
argument_list|(
operator|new
name|GoogleMailComponentVerifierExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|GoogleMailComponent (CamelContext context)
specifier|public
name|GoogleMailComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|GoogleMailEndpoint
operator|.
name|class
argument_list|,
name|GoogleMailApiName
operator|.
name|class
argument_list|,
name|GoogleMailApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
name|registerExtension
argument_list|(
operator|new
name|GoogleMailComponentVerifierExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiName (String apiNameStr)
specifier|protected
name|GoogleMailApiName
name|getApiName
parameter_list|(
name|String
name|apiNameStr
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|GoogleMailApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
return|;
block|}
DECL|method|getClient (GoogleMailConfiguration googleMailConfiguration)
specifier|public
name|Gmail
name|getClient
parameter_list|(
name|GoogleMailConfiguration
name|googleMailConfiguration
parameter_list|)
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|getClientFactory
argument_list|()
operator|.
name|makeClient
argument_list|(
name|googleMailConfiguration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|googleMailConfiguration
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|googleMailConfiguration
operator|.
name|getApplicationName
argument_list|()
argument_list|,
name|googleMailConfiguration
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|googleMailConfiguration
operator|.
name|getAccessToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getClientFactory ()
specifier|public
name|GoogleMailClientFactory
name|getClientFactory
parameter_list|()
block|{
if|if
condition|(
name|clientFactory
operator|==
literal|null
condition|)
block|{
name|clientFactory
operator|=
operator|new
name|BatchGoogleMailClientFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|clientFactory
return|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|setConfiguration (GoogleMailConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GoogleMailConfiguration
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
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|GoogleMailConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|GoogleMailConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
comment|/**      * To use the GoogleCalendarClientFactory as factory for creating the client.      * Will by default use {@link BatchGoogleMailClientFactory}      */
DECL|method|setClientFactory (GoogleMailClientFactory clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|GoogleMailClientFactory
name|clientFactory
parameter_list|)
block|{
name|this
operator|.
name|clientFactory
operator|=
name|clientFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String methodName, GoogleMailApiName apiName, GoogleMailConfiguration endpointConfiguration)
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
name|GoogleMailApiName
name|apiName
parameter_list|,
name|GoogleMailConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|endpointConfiguration
operator|.
name|setApiName
argument_list|(
name|apiName
argument_list|)
expr_stmt|;
name|endpointConfiguration
operator|.
name|setMethodName
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
return|return
operator|new
name|GoogleMailEndpoint
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
block|}
end_class

end_unit

