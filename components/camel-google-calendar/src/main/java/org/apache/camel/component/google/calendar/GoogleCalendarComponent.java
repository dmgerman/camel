begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar
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
name|calendar
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
name|calendar
operator|.
name|Calendar
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
name|calendar
operator|.
name|internal
operator|.
name|GoogleCalendarApiCollection
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
name|calendar
operator|.
name|internal
operator|.
name|GoogleCalendarApiName
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
name|AbstractApiComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link GoogleCalendarEndpoint}.  */
end_comment

begin_class
DECL|class|GoogleCalendarComponent
specifier|public
class|class
name|GoogleCalendarComponent
extends|extends
name|AbstractApiComponent
argument_list|<
name|GoogleCalendarApiName
argument_list|,
name|GoogleCalendarConfiguration
argument_list|,
name|GoogleCalendarApiCollection
argument_list|>
block|{
DECL|field|client
specifier|private
name|Calendar
name|client
decl_stmt|;
DECL|field|clientFactory
specifier|private
name|GoogleCalendarClientFactory
name|clientFactory
decl_stmt|;
DECL|method|GoogleCalendarComponent ()
specifier|public
name|GoogleCalendarComponent
parameter_list|()
block|{
name|super
argument_list|(
name|GoogleCalendarEndpoint
operator|.
name|class
argument_list|,
name|GoogleCalendarApiName
operator|.
name|class
argument_list|,
name|GoogleCalendarApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|GoogleCalendarComponent (CamelContext context)
specifier|public
name|GoogleCalendarComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|GoogleCalendarEndpoint
operator|.
name|class
argument_list|,
name|GoogleCalendarApiName
operator|.
name|class
argument_list|,
name|GoogleCalendarApiCollection
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
name|GoogleCalendarApiName
name|getApiName
parameter_list|(
name|String
name|apiNameStr
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|GoogleCalendarApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
return|;
block|}
DECL|method|getClient ()
specifier|public
name|Calendar
name|getClient
parameter_list|()
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
name|configuration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|configuration
operator|.
name|getScopes
argument_list|()
argument_list|,
name|configuration
operator|.
name|getApplicationName
argument_list|()
argument_list|,
name|configuration
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|configuration
operator|.
name|getAccessToken
argument_list|()
argument_list|,
name|configuration
operator|.
name|getEmailAddress
argument_list|()
argument_list|,
name|configuration
operator|.
name|getP12FileName
argument_list|()
argument_list|,
name|configuration
operator|.
name|getUser
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
name|GoogleCalendarClientFactory
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
name|BatchGoogleCalendarClientFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|clientFactory
return|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|GoogleCalendarConfiguration
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
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|setConfiguration (GoogleCalendarConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GoogleCalendarConfiguration
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
comment|/**      * To use the GoogleCalendarClientFactory as factory for creating the client.      * Will by default use {@link BatchGoogleCalendarClientFactory}      */
DECL|method|setClientFactory (GoogleCalendarClientFactory clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|GoogleCalendarClientFactory
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
DECL|method|createEndpoint (String uri, String methodName, GoogleCalendarApiName apiName, GoogleCalendarConfiguration endpointConfiguration)
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
name|GoogleCalendarApiName
name|apiName
parameter_list|,
name|GoogleCalendarConfiguration
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
name|GoogleCalendarEndpoint
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

