begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar.stream
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
operator|.
name|stream
package|;
end_package

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
name|BatchGoogleCalendarClientFactory
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
name|GoogleCalendarClientFactory
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
name|DefaultComponent
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

begin_comment
comment|/**  * Represents the component that manages {@link GoogleCalendarStreamEndpoint}.  */
end_comment

begin_class
DECL|class|GoogleCalendarStreamComponent
specifier|public
class|class
name|GoogleCalendarStreamComponent
extends|extends
name|DefaultComponent
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
name|Calendar
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
name|GoogleCalendarClientFactory
name|clientFactory
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|GoogleCalendarStreamConfiguration
name|configuration
decl_stmt|;
DECL|method|GoogleCalendarStreamComponent ()
specifier|public
name|GoogleCalendarStreamComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|GoogleCalendarStreamComponent (CamelContext context)
specifier|public
name|GoogleCalendarStreamComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|GoogleCalendarStreamConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|getClient (GoogleCalendarStreamConfiguration endpointConfiguration)
specifier|public
name|Calendar
name|getClient
parameter_list|(
name|GoogleCalendarStreamConfiguration
name|endpointConfiguration
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
name|endpointConfiguration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|endpointConfiguration
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|endpointConfiguration
operator|.
name|getScopes
argument_list|()
argument_list|,
name|endpointConfiguration
operator|.
name|getApplicationName
argument_list|()
argument_list|,
name|endpointConfiguration
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|endpointConfiguration
operator|.
name|getAccessToken
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"me"
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
comment|/**      * The client Factory      */
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
DECL|method|getConfiguration ()
specifier|public
name|GoogleCalendarStreamConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * The configuration      */
DECL|method|setConfiguration (GoogleCalendarStreamConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GoogleCalendarStreamConfiguration
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
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|GoogleCalendarStreamConfiguration
name|configuration
init|=
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|GoogleCalendarStreamEndpoint
name|endpoint
init|=
operator|new
name|GoogleCalendarStreamEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

