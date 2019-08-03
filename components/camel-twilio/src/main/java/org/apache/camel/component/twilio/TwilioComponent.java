begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twilio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twilio
package|;
end_package

begin_import
import|import
name|com
operator|.
name|twilio
operator|.
name|http
operator|.
name|TwilioRestClient
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
name|twilio
operator|.
name|internal
operator|.
name|TwilioApiCollection
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
name|twilio
operator|.
name|internal
operator|.
name|TwilioApiName
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link TwilioEndpoint}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"twilio"
argument_list|)
DECL|class|TwilioComponent
specifier|public
class|class
name|TwilioComponent
extends|extends
name|AbstractApiComponent
argument_list|<
name|TwilioApiName
argument_list|,
name|TwilioConfiguration
argument_list|,
name|TwilioApiCollection
argument_list|>
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|TwilioConfiguration
name|configuration
init|=
operator|new
name|TwilioConfiguration
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|restClient
specifier|private
name|TwilioRestClient
name|restClient
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"common,security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"common,security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"common,security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|accountSid
specifier|private
name|String
name|accountSid
decl_stmt|;
DECL|method|TwilioComponent ()
specifier|public
name|TwilioComponent
parameter_list|()
block|{
name|super
argument_list|(
name|TwilioEndpoint
operator|.
name|class
argument_list|,
name|TwilioApiName
operator|.
name|class
argument_list|,
name|TwilioApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|TwilioComponent (CamelContext context)
specifier|public
name|TwilioComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|TwilioEndpoint
operator|.
name|class
argument_list|,
name|TwilioApiName
operator|.
name|class
argument_list|,
name|TwilioApiCollection
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
name|TwilioApiName
name|getApiName
parameter_list|(
name|String
name|apiNameStr
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|TwilioApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String methodName, TwilioApiName apiName, TwilioConfiguration endpointConfiguration)
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
name|TwilioApiName
name|apiName
parameter_list|,
name|TwilioConfiguration
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
name|TwilioEndpoint
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
name|restClient
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|username
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|password
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to initialise Twilio, Twilio component configuration is missing"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|accountSid
argument_list|)
condition|)
block|{
name|accountSid
operator|=
name|username
expr_stmt|;
block|}
name|restClient
operator|=
operator|new
name|TwilioRestClient
operator|.
name|Builder
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
operator|.
name|accountSid
argument_list|(
name|accountSid
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
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
name|restClient
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|TwilioConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|setConfiguration (TwilioConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|TwilioConfiguration
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
DECL|method|getRestClient ()
specifier|public
name|TwilioRestClient
name|getRestClient
parameter_list|()
block|{
return|return
name|restClient
return|;
block|}
comment|/**      * To use the shared REST client      */
DECL|method|setRestClient (TwilioRestClient restClient)
specifier|public
name|void
name|setRestClient
parameter_list|(
name|TwilioRestClient
name|restClient
parameter_list|)
block|{
name|this
operator|.
name|restClient
operator|=
name|restClient
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * The account to use.      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * Auth token for the account.      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getAccountSid ()
specifier|public
name|String
name|getAccountSid
parameter_list|()
block|{
return|return
name|accountSid
return|;
block|}
comment|/**      * The account SID to use.      */
DECL|method|setAccountSid (String accountSid)
specifier|public
name|void
name|setAccountSid
parameter_list|(
name|String
name|accountSid
parameter_list|)
block|{
name|this
operator|.
name|accountSid
operator|=
name|accountSid
expr_stmt|;
block|}
block|}
end_class

end_unit

