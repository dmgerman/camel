begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.login
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|login
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
name|Exchange
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
import|;
end_import

begin_comment
comment|/**  * Binds {@link GLoginData} to a Camel {@link Exchange}.  */
end_comment

begin_class
DECL|class|GLoginBinding
specifier|public
class|class
name|GLoginBinding
implements|implements
name|OutboundBinding
argument_list|<
name|GLoginEndpoint
argument_list|,
name|GLoginData
argument_list|,
name|GLoginData
argument_list|>
block|{
comment|/**      * Name of the Camel header defining the host name. Overrides      * {@link GLoginEndpoint#getHostName()}.      */
DECL|field|GLOGIN_HOST_NAME
specifier|public
specifier|static
specifier|final
name|String
name|GLOGIN_HOST_NAME
init|=
literal|"CamelGloginHostName"
decl_stmt|;
comment|/**      * Name of the Camel header defining the login username. Overrides      * {@link GLoginEndpoint#getUserName()}.      */
DECL|field|GLOGIN_USER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|GLOGIN_USER_NAME
init|=
literal|"CamelGloginUserName"
decl_stmt|;
comment|/**      * Name of the Camel header defining the login password. Overrides      * {@link GLoginEndpoint#getPassword()}.      */
DECL|field|GLOGIN_PASSWORD
specifier|public
specifier|static
specifier|final
name|String
name|GLOGIN_PASSWORD
init|=
literal|"CamelGloginPassword"
decl_stmt|;
comment|/**      * Name of the Camel header containing the resulting authentication token.       */
DECL|field|GLOGIN_TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|GLOGIN_TOKEN
init|=
literal|"CamelGloginToken"
decl_stmt|;
comment|/**      * Name of the Camel header containing the resulting authorization cookie.       */
DECL|field|GLOGIN_COOKIE
specifier|public
specifier|static
specifier|final
name|String
name|GLOGIN_COOKIE
init|=
literal|"CamelGloginCookie"
decl_stmt|;
comment|/**      * Creates a {@link GLoginData} object from endpoint and      *<code>exchange.getIn()</code> header data. The created object is used to      * obtain an authentication token and an authorization cookie.      *       * @param endpoint      * @param exchange      * @param request      *            ignored.      * @return      */
DECL|method|writeRequest (GLoginEndpoint endpoint, Exchange exchange, GLoginData request)
specifier|public
name|GLoginData
name|writeRequest
parameter_list|(
name|GLoginEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GLoginData
name|request
parameter_list|)
block|{
name|String
name|hostName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GLOGIN_HOST_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|userName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GLOGIN_USER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GLOGIN_PASSWORD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|=
operator|new
name|GLoginData
argument_list|()
expr_stmt|;
if|if
condition|(
name|hostName
operator|==
literal|null
condition|)
block|{
name|hostName
operator|=
name|endpoint
operator|.
name|getHostName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|userName
operator|==
literal|null
condition|)
block|{
name|userName
operator|=
name|endpoint
operator|.
name|getUserName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|password
operator|=
name|endpoint
operator|.
name|getPassword
argument_list|()
expr_stmt|;
block|}
name|request
operator|.
name|setClientName
argument_list|(
name|endpoint
operator|.
name|getClientName
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setDevAdmin
argument_list|(
name|endpoint
operator|.
name|isDevAdmin
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setDevPort
argument_list|(
name|endpoint
operator|.
name|getDevPort
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setDevMode
argument_list|(
name|endpoint
operator|.
name|isDevMode
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setHostName
argument_list|(
name|hostName
argument_list|)
expr_stmt|;
name|request
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
name|request
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
return|return
name|request
return|;
block|}
comment|/**      * Creates an<code>exchange.getOut()</code> message with a      * {@link #GLOGIN_TOKEN} header containing an authentication token and a      * {@link #GLOGIN_COOKIE} header containing an authorization cookie. If the      * endpoint is configured to run in development mode, no authentication      * token will be set, only an authorization cookie.      *       * @param endpoint      * @param exchange      * @param response      * @return      */
DECL|method|readResponse (GLoginEndpoint endpoint, Exchange exchange, GLoginData response)
specifier|public
name|Exchange
name|readResponse
parameter_list|(
name|GLoginEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GLoginData
name|response
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|response
operator|.
name|getAuthenticationToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GLOGIN_TOKEN
argument_list|,
name|response
operator|.
name|getAuthenticationToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|response
operator|.
name|getAuthorizationCookie
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GLOGIN_COOKIE
argument_list|,
name|response
operator|.
name|getAuthorizationCookie
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

