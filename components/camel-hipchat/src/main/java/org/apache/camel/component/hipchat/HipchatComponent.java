begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hipchat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hipchat
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|impl
operator|.
name|UriEndpointComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link HipchatEndpoint}. Hipchat is an Atlassian software for team chat.  *  * The hipchat component uses the OAuth2 Hipchat API to produce/consume messages. For more details about Hipchat API  * @see<a href="https://www.hipchat.com/docs/apiv2/auth">Hipchat API</a>. You can get the Oauth2 auth token  * at @see<a href="https://www.hipchat.com/account/api">Hipchat Auth Token</a>. The messages produced and consumed  * would be from/to owner of the provided auth token.  */
end_comment

begin_class
DECL|class|HipchatComponent
specifier|public
class|class
name|HipchatComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|HipchatComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|HipchatComponent ()
specifier|public
name|HipchatComponent
parameter_list|()
block|{
name|super
argument_list|(
name|HipchatEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|HipchatComponent (CamelContext context)
specifier|public
name|HipchatComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|HipchatEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|HipchatEndpoint
name|endpoint
init|=
name|getHipchatEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAuthToken
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|HipchatException
argument_list|(
literal|"OAuth 2 auth token must be specified"
argument_list|)
throw|;
block|}
name|parseUri
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using Hipchat API URL: {}"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|hipChatUrl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|parseUri (String uri, HipchatEndpoint endpoint)
specifier|private
name|void
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|HipchatEndpoint
name|endpoint
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|URI
name|hipChatUri
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|hipChatUri
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setHost
argument_list|(
name|hipChatUri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|hipChatUri
operator|.
name|getPort
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setPort
argument_list|(
name|hipChatUri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getHipchatEndpoint (String uri)
specifier|protected
name|HipchatEndpoint
name|getHipchatEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
operator|new
name|HipchatEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

