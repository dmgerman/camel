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

begin_comment
comment|/**  * Container for login request and response data.   */
end_comment

begin_class
DECL|class|GLoginData
specifier|public
class|class
name|GLoginData
block|{
DECL|field|hostName
specifier|private
name|String
name|hostName
decl_stmt|;
DECL|field|clientName
specifier|private
name|String
name|clientName
decl_stmt|;
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|devPort
specifier|private
name|int
name|devPort
decl_stmt|;
DECL|field|devAdmin
specifier|private
name|boolean
name|devAdmin
decl_stmt|;
DECL|field|devMode
specifier|private
name|boolean
name|devMode
decl_stmt|;
DECL|field|authenticationToken
specifier|private
name|String
name|authenticationToken
decl_stmt|;
DECL|field|authorizationCookie
specifier|private
name|String
name|authorizationCookie
decl_stmt|;
comment|/**      * @see GLoginEndpoint#getHostName()      * @see GLoginBinding#GLOGIN_HOST_NAME      */
DECL|method|getHostName ()
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
DECL|method|setHostName (String hostName)
specifier|public
name|void
name|setHostName
parameter_list|(
name|String
name|hostName
parameter_list|)
block|{
name|this
operator|.
name|hostName
operator|=
name|hostName
expr_stmt|;
block|}
DECL|method|getClientName ()
specifier|public
name|String
name|getClientName
parameter_list|()
block|{
return|return
name|clientName
return|;
block|}
DECL|method|setClientName (String clientName)
specifier|public
name|void
name|setClientName
parameter_list|(
name|String
name|clientName
parameter_list|)
block|{
name|this
operator|.
name|clientName
operator|=
name|clientName
expr_stmt|;
block|}
comment|/**      * @see GLoginBinding#GLOGIN_USER_NAME      */
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
comment|/**      * @see GLoginBinding#GLOGIN_PASSWORD      */
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
comment|/**      * @see GLoginEndpoint#getDevPort()      */
DECL|method|getDevPort ()
specifier|public
name|int
name|getDevPort
parameter_list|()
block|{
return|return
name|devPort
return|;
block|}
DECL|method|setDevPort (int devPort)
specifier|public
name|void
name|setDevPort
parameter_list|(
name|int
name|devPort
parameter_list|)
block|{
name|this
operator|.
name|devPort
operator|=
name|devPort
expr_stmt|;
block|}
DECL|method|isDevAdmin ()
specifier|public
name|boolean
name|isDevAdmin
parameter_list|()
block|{
return|return
name|devAdmin
return|;
block|}
DECL|method|setDevAdmin (boolean devAdmin)
specifier|public
name|void
name|setDevAdmin
parameter_list|(
name|boolean
name|devAdmin
parameter_list|)
block|{
name|this
operator|.
name|devAdmin
operator|=
name|devAdmin
expr_stmt|;
block|}
DECL|method|isDevMode ()
specifier|public
name|boolean
name|isDevMode
parameter_list|()
block|{
return|return
name|devMode
return|;
block|}
DECL|method|setDevMode (boolean devMode)
specifier|public
name|void
name|setDevMode
parameter_list|(
name|boolean
name|devMode
parameter_list|)
block|{
name|this
operator|.
name|devMode
operator|=
name|devMode
expr_stmt|;
block|}
DECL|method|getAuthenticationToken ()
specifier|public
name|String
name|getAuthenticationToken
parameter_list|()
block|{
return|return
name|authenticationToken
return|;
block|}
comment|/**      * @see GLoginBinding#GLOGIN_TOKEN      */
DECL|method|setAuthenticationToken (String authenticationToken)
specifier|public
name|void
name|setAuthenticationToken
parameter_list|(
name|String
name|authenticationToken
parameter_list|)
block|{
name|this
operator|.
name|authenticationToken
operator|=
name|authenticationToken
expr_stmt|;
block|}
DECL|method|getAuthorizationCookie ()
specifier|public
name|String
name|getAuthorizationCookie
parameter_list|()
block|{
return|return
name|authorizationCookie
return|;
block|}
comment|/**      * @see GLoginBinding#GLOGIN_COOKIE      */
DECL|method|setAuthorizationCookie (String authorizationCookie)
specifier|public
name|void
name|setAuthorizationCookie
parameter_list|(
name|String
name|authorizationCookie
parameter_list|)
block|{
name|this
operator|.
name|authorizationCookie
operator|=
name|authorizationCookie
expr_stmt|;
block|}
block|}
end_class

end_unit

