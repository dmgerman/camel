begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zendesk.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The zendesk endpoint interacts with the Zendesk server.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.zendesk"
argument_list|)
DECL|class|ZendeskComponentConfiguration
specifier|public
class|class
name|ZendeskComponentConfiguration
block|{
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|ZendeskConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|ZendeskConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( ZendeskConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ZendeskConfigurationNestedConfiguration
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
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|ZendeskConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|ZendeskConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
name|ZendeskConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * What operation to use          *           * @param methodNamethe          *            methodName to set          */
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**          * The server URL to connect.          *           * @param url          *            server URL          */
DECL|field|serverUrl
specifier|private
name|String
name|serverUrl
decl_stmt|;
comment|/**          * The user name.          *           * @param user          *            user name          */
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
comment|/**          * The security token.          *           * @param token          *            security token          */
DECL|field|token
specifier|private
name|String
name|token
decl_stmt|;
comment|/**          * The OAuth token.          *           * @param token          *            OAuth token          */
DECL|field|oauthToken
specifier|private
name|String
name|oauthToken
decl_stmt|;
comment|/**          * The password.          *           * @param password          *            password          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|getServerUrl ()
specifier|public
name|String
name|getServerUrl
parameter_list|()
block|{
return|return
name|serverUrl
return|;
block|}
DECL|method|setServerUrl (String serverUrl)
specifier|public
name|void
name|setServerUrl
parameter_list|(
name|String
name|serverUrl
parameter_list|)
block|{
name|this
operator|.
name|serverUrl
operator|=
name|serverUrl
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
DECL|method|getToken ()
specifier|public
name|String
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
DECL|method|setToken (String token)
specifier|public
name|void
name|setToken
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
DECL|method|getOauthToken ()
specifier|public
name|String
name|getOauthToken
parameter_list|()
block|{
return|return
name|oauthToken
return|;
block|}
DECL|method|setOauthToken (String oauthToken)
specifier|public
name|void
name|setOauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|this
operator|.
name|oauthToken
operator|=
name|oauthToken
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
block|}
block|}
end_class

end_unit

