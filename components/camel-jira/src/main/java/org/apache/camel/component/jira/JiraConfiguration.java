begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jira
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
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
name|RuntimeCamelException
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
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|JiraConfiguration
specifier|public
class|class
name|JiraConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|verificationCode
specifier|private
name|String
name|verificationCode
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|privateKey
specifier|private
name|String
name|privateKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|jiraUrl
specifier|private
name|String
name|jiraUrl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
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
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
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
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"6000"
argument_list|)
DECL|field|delay
specifier|private
name|Integer
name|delay
init|=
literal|6000
decl_stmt|;
DECL|method|getVerificationCode ()
specifier|public
name|String
name|getVerificationCode
parameter_list|()
block|{
return|return
name|verificationCode
return|;
block|}
comment|/**      * (OAuth only) The verification code from Jira generated in the first step of the authorization proccess.      */
DECL|method|setVerificationCode (String verificationCode)
specifier|public
name|void
name|setVerificationCode
parameter_list|(
name|String
name|verificationCode
parameter_list|)
block|{
name|this
operator|.
name|verificationCode
operator|=
name|verificationCode
expr_stmt|;
block|}
DECL|method|getConsumerKey ()
specifier|public
name|String
name|getConsumerKey
parameter_list|()
block|{
return|return
name|consumerKey
return|;
block|}
comment|/**      * (OAuth only) The consumer key from Jira settings.      */
DECL|method|setConsumerKey (String consumerKey)
specifier|public
name|void
name|setConsumerKey
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
name|this
operator|.
name|consumerKey
operator|=
name|consumerKey
expr_stmt|;
block|}
DECL|method|getPrivateKey ()
specifier|public
name|String
name|getPrivateKey
parameter_list|()
block|{
return|return
name|privateKey
return|;
block|}
comment|/**      * (OAuth only) The private key generated by the client to encrypt the conversation to the server.      */
DECL|method|setPrivateKey (String privateKey)
specifier|public
name|void
name|setPrivateKey
parameter_list|(
name|String
name|privateKey
parameter_list|)
block|{
name|this
operator|.
name|privateKey
operator|=
name|privateKey
expr_stmt|;
block|}
DECL|method|getAccessToken ()
specifier|public
name|String
name|getAccessToken
parameter_list|()
block|{
return|return
name|accessToken
return|;
block|}
comment|/**      * (OAuth only) The access token generated by the Jira server.      */
DECL|method|setAccessToken (String accessToken)
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
block|}
DECL|method|getJiraUrl ()
specifier|public
name|String
name|getJiraUrl
parameter_list|()
block|{
return|return
name|jiraUrl
return|;
block|}
comment|/**      * The Jira server url, example: http://my_jira.com:8081      */
DECL|method|setJiraUrl (String jiraUrl)
specifier|public
name|void
name|setJiraUrl
parameter_list|(
name|String
name|jiraUrl
parameter_list|)
block|{
name|this
operator|.
name|jiraUrl
operator|=
name|jiraUrl
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|Integer
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
comment|/**      * Time in milliseconds to elapse for the next poll.      * @param delay Integer time in milliseconds      */
DECL|method|setDelay (Integer delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|Integer
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
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
comment|/**      * (Basic authentication only) The username to authenticate to the Jira server. Use only if OAuth is not enabled on the Jira server.      * Do not set the username and OAuth token parameter, if they are both set, the username basic authentication takes precedence.      */
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
comment|/**      * (Basic authentication only) The password to authenticate to the Jira server. Use only if username basic authentication is used.      */
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
DECL|method|copy ()
specifier|public
name|JiraConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|JiraConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

