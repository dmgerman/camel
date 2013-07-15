begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_comment
comment|/**  * Security configuration for the {@link NettyHttpConsumer}.  */
end_comment

begin_class
DECL|class|NettyHttpSecurityConfiguration
specifier|public
class|class
name|NettyHttpSecurityConfiguration
block|{
DECL|field|authenticate
specifier|private
name|boolean
name|authenticate
init|=
literal|true
decl_stmt|;
DECL|field|constraint
specifier|private
name|String
name|constraint
init|=
literal|"Basic"
decl_stmt|;
DECL|field|realm
specifier|private
name|String
name|realm
decl_stmt|;
DECL|field|contextPathMatcher
specifier|private
name|ContextPathMatcher
name|contextPathMatcher
decl_stmt|;
DECL|field|securityAuthenticator
specifier|private
name|SecurityAuthenticator
name|securityAuthenticator
decl_stmt|;
DECL|method|isAuthenticate ()
specifier|public
name|boolean
name|isAuthenticate
parameter_list|()
block|{
return|return
name|authenticate
return|;
block|}
comment|/**      * Whether to enable authentication      *<p/>      * This is by default enabled.      */
DECL|method|setAuthenticate (boolean authenticate)
specifier|public
name|void
name|setAuthenticate
parameter_list|(
name|boolean
name|authenticate
parameter_list|)
block|{
name|this
operator|.
name|authenticate
operator|=
name|authenticate
expr_stmt|;
block|}
DECL|method|getConstraint ()
specifier|public
name|String
name|getConstraint
parameter_list|()
block|{
return|return
name|constraint
return|;
block|}
comment|/**      * The supported constraint.      *<p/>      * Currently only Basic is supported.      */
DECL|method|setConstraint (String constraint)
specifier|public
name|void
name|setConstraint
parameter_list|(
name|String
name|constraint
parameter_list|)
block|{
name|this
operator|.
name|constraint
operator|=
name|constraint
expr_stmt|;
block|}
DECL|method|getRealm ()
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
return|return
name|realm
return|;
block|}
comment|/**      * Sets the name of the realm to use.      */
DECL|method|setRealm (String realm)
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
block|}
DECL|method|getContextPathMatcher ()
specifier|public
name|ContextPathMatcher
name|getContextPathMatcher
parameter_list|()
block|{
return|return
name|contextPathMatcher
return|;
block|}
comment|/**      * Sets a {@link ContextPathMatcher} to use for matching if a url is restricted or not.      *<p/>      * By default this is<tt>null</tt>, which means all resources is restricted.      */
DECL|method|setContextPathMatcher (ContextPathMatcher contextPathMatcher)
specifier|public
name|void
name|setContextPathMatcher
parameter_list|(
name|ContextPathMatcher
name|contextPathMatcher
parameter_list|)
block|{
name|this
operator|.
name|contextPathMatcher
operator|=
name|contextPathMatcher
expr_stmt|;
block|}
DECL|method|getSecurityAuthenticator ()
specifier|public
name|SecurityAuthenticator
name|getSecurityAuthenticator
parameter_list|()
block|{
return|return
name|securityAuthenticator
return|;
block|}
comment|/**      * Sets the {@link SecurityAuthenticator} to use for authenticating the {@link HttpPrincipal}.      */
DECL|method|setSecurityAuthenticator (SecurityAuthenticator securityAuthenticator)
specifier|public
name|void
name|setSecurityAuthenticator
parameter_list|(
name|SecurityAuthenticator
name|securityAuthenticator
parameter_list|)
block|{
name|this
operator|.
name|securityAuthenticator
operator|=
name|securityAuthenticator
expr_stmt|;
block|}
block|}
end_class

end_unit

