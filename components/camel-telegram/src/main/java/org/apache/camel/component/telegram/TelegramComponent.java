begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"telegram"
argument_list|)
DECL|class|TelegramComponent
specifier|public
class|class
name|TelegramComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|authorizationToken
specifier|private
name|String
name|authorizationToken
decl_stmt|;
DECL|method|TelegramComponent ()
specifier|public
name|TelegramComponent
parameter_list|()
block|{     }
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
name|TelegramConfiguration
name|configuration
init|=
operator|new
name|TelegramConfiguration
argument_list|()
decl_stmt|;
comment|// ignore trailing slash
if|if
condition|(
name|remaining
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|remaining
operator|=
name|remaining
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|remaining
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|.
name|setType
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getAuthorizationToken
argument_list|()
operator|==
literal|null
condition|)
block|{
name|configuration
operator|.
name|setAuthorizationToken
argument_list|(
name|authorizationToken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getAuthorizationToken
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"AuthorizationToken must be configured on either component or endpoint for telegram: "
operator|+
name|uri
argument_list|)
throw|;
block|}
if|if
condition|(
name|TelegramConfiguration
operator|.
name|ENDPOINT_TYPE_BOTS
operator|.
name|equals
argument_list|(
name|configuration
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|TelegramEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported endpoint type for uri "
operator|+
name|uri
operator|+
literal|", remaining "
operator|+
name|remaining
argument_list|)
throw|;
block|}
DECL|method|getAuthorizationToken ()
specifier|public
name|String
name|getAuthorizationToken
parameter_list|()
block|{
return|return
name|authorizationToken
return|;
block|}
comment|/**      * The default Telegram authorization token to be used when the information is not provided in the endpoints.      */
DECL|method|setAuthorizationToken (String authorizationToken)
specifier|public
name|void
name|setAuthorizationToken
parameter_list|(
name|String
name|authorizationToken
parameter_list|)
block|{
name|this
operator|.
name|authorizationToken
operator|=
name|authorizationToken
expr_stmt|;
block|}
block|}
end_class

end_unit

