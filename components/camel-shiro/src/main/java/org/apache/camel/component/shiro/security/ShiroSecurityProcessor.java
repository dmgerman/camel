begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.shiro.security
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|shiro
operator|.
name|security
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
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
name|AsyncCallback
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
name|CamelAuthorizationException
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
name|CamelExchangeException
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
name|Processor
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
name|processor
operator|.
name|DelegateAsyncProcessor
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
name|ExchangeHelper
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
name|IOHelper
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|SecurityUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|authc
operator|.
name|AuthenticationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|authc
operator|.
name|IncorrectCredentialsException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|authc
operator|.
name|LockedAccountException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|authc
operator|.
name|UnknownAccountException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|authc
operator|.
name|UsernamePasswordToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|authz
operator|.
name|Permission
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|codec
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|subject
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|util
operator|.
name|ByteSource
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
comment|/**  * {@link Processor} that executes the authentication and authorization of the {@link Subject} accordingly  * to the {@link ShiroSecurityPolicy}.  */
end_comment

begin_class
DECL|class|ShiroSecurityProcessor
specifier|public
class|class
name|ShiroSecurityProcessor
extends|extends
name|DelegateAsyncProcessor
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
name|ShiroSecurityProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|policy
specifier|private
specifier|final
name|ShiroSecurityPolicy
name|policy
decl_stmt|;
DECL|method|ShiroSecurityProcessor (Processor processor, ShiroSecurityPolicy policy)
specifier|public
name|ShiroSecurityProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|ShiroSecurityPolicy
name|policy
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|policy
operator|=
name|policy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
name|applySecurityPolicy
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// exception occurred so break out
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|applySecurityPolicy (Exchange exchange)
specifier|private
name|void
name|applySecurityPolicy
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteSource
name|encryptedToken
decl_stmt|;
comment|// if we have username and password as headers then use them to create a token
name|String
name|username
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_USERNAME
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
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_PASSWORD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|ShiroSecurityToken
name|token
init|=
operator|new
name|ShiroSecurityToken
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
comment|// store the token as header, either as base64 or as the object as-is
if|if
condition|(
name|policy
operator|.
name|isBase64
argument_list|()
condition|)
block|{
name|ByteSource
name|bytes
init|=
name|ShiroSecurityHelper
operator|.
name|encrypt
argument_list|(
name|token
argument_list|,
name|policy
operator|.
name|getPassPhrase
argument_list|()
argument_list|,
name|policy
operator|.
name|getCipherService
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|base64
init|=
name|bytes
operator|.
name|toBase64
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_TOKEN
argument_list|,
name|base64
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
comment|// and now remove the headers as we turned those into the token instead
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_USERNAME
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_PASSWORD
argument_list|)
expr_stmt|;
block|}
name|Object
name|token
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|exchange
argument_list|,
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_TOKEN
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we support the token in a number of ways
if|if
condition|(
name|token
operator|instanceof
name|ShiroSecurityToken
condition|)
block|{
name|ShiroSecurityToken
name|sst
init|=
operator|(
name|ShiroSecurityToken
operator|)
name|token
decl_stmt|;
name|encryptedToken
operator|=
name|ShiroSecurityHelper
operator|.
name|encrypt
argument_list|(
name|sst
argument_list|,
name|policy
operator|.
name|getPassPhrase
argument_list|()
argument_list|,
name|policy
operator|.
name|getCipherService
argument_list|()
argument_list|)
expr_stmt|;
comment|// Remove unencrypted token + replace with an encrypted token
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_TOKEN
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_TOKEN
argument_list|,
name|encryptedToken
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|String
condition|)
block|{
name|String
name|data
init|=
operator|(
name|String
operator|)
name|token
decl_stmt|;
if|if
condition|(
name|policy
operator|.
name|isBase64
argument_list|()
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
name|Base64
operator|.
name|decode
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|encryptedToken
operator|=
name|ByteSource
operator|.
name|Util
operator|.
name|bytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|encryptedToken
operator|=
name|ByteSource
operator|.
name|Util
operator|.
name|bytes
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|ByteSource
condition|)
block|{
name|encryptedToken
operator|=
operator|(
name|ByteSource
operator|)
name|token
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Shiro security header "
operator|+
name|ShiroSecurityConstants
operator|.
name|SHIRO_SECURITY_TOKEN
operator|+
literal|" is unsupported type: "
operator|+
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|token
argument_list|)
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|ByteSource
name|decryptedToken
init|=
name|policy
operator|.
name|getCipherService
argument_list|()
operator|.
name|decrypt
argument_list|(
name|encryptedToken
operator|.
name|getBytes
argument_list|()
argument_list|,
name|policy
operator|.
name|getPassPhrase
argument_list|()
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|byteArrayInputStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|decryptedToken
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectInputStream
name|objectInputStream
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|byteArrayInputStream
argument_list|)
decl_stmt|;
name|ShiroSecurityToken
name|securityToken
decl_stmt|;
try|try
block|{
name|securityToken
operator|=
operator|(
name|ShiroSecurityToken
operator|)
name|objectInputStream
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|objectInputStream
argument_list|,
name|byteArrayInputStream
argument_list|)
expr_stmt|;
block|}
name|Subject
name|currentUser
init|=
name|SecurityUtils
operator|.
name|getSubject
argument_list|()
decl_stmt|;
comment|// Authenticate user if not authenticated
try|try
block|{
name|authenticateUser
argument_list|(
name|currentUser
argument_list|,
name|securityToken
argument_list|)
expr_stmt|;
comment|// Test whether user's role is authorized to perform functions in the permissions list
name|authorizeUser
argument_list|(
name|currentUser
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|policy
operator|.
name|isAlwaysReauthenticate
argument_list|()
condition|)
block|{
name|currentUser
operator|.
name|logout
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|authenticateUser (Subject currentUser, ShiroSecurityToken securityToken)
specifier|private
name|void
name|authenticateUser
parameter_list|(
name|Subject
name|currentUser
parameter_list|,
name|ShiroSecurityToken
name|securityToken
parameter_list|)
block|{
name|boolean
name|authenticated
init|=
name|currentUser
operator|.
name|isAuthenticated
argument_list|()
decl_stmt|;
name|boolean
name|sameUser
init|=
name|securityToken
operator|.
name|getUsername
argument_list|()
operator|.
name|equals
argument_list|(
name|currentUser
operator|.
name|getPrincipal
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Authenticated: {}, same Username: {}"
argument_list|,
name|authenticated
argument_list|,
name|sameUser
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|authenticated
operator|||
operator|!
name|sameUser
condition|)
block|{
name|UsernamePasswordToken
name|token
init|=
operator|new
name|UsernamePasswordToken
argument_list|(
name|securityToken
operator|.
name|getUsername
argument_list|()
argument_list|,
name|securityToken
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|.
name|isAlwaysReauthenticate
argument_list|()
condition|)
block|{
name|token
operator|.
name|setRememberMe
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|token
operator|.
name|setRememberMe
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|currentUser
operator|.
name|login
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Current user {} successfully authenticated"
argument_list|,
name|currentUser
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownAccountException
name|uae
parameter_list|)
block|{
throw|throw
operator|new
name|UnknownAccountException
argument_list|(
literal|"Authentication Failed. There is no user with username of "
operator|+
name|token
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|uae
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IncorrectCredentialsException
name|ice
parameter_list|)
block|{
throw|throw
operator|new
name|IncorrectCredentialsException
argument_list|(
literal|"Authentication Failed. Password for account "
operator|+
name|token
operator|.
name|getPrincipal
argument_list|()
operator|+
literal|" was incorrect!"
argument_list|,
name|ice
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|LockedAccountException
name|lae
parameter_list|)
block|{
throw|throw
operator|new
name|LockedAccountException
argument_list|(
literal|"Authentication Failed. The account for username "
operator|+
name|token
operator|.
name|getPrincipal
argument_list|()
operator|+
literal|" is locked."
operator|+
literal|"Please contact your administrator to unlock it."
argument_list|,
name|lae
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|AuthenticationException
name|ae
parameter_list|)
block|{
throw|throw
operator|new
name|AuthenticationException
argument_list|(
literal|"Authentication Failed."
argument_list|,
name|ae
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|authorizeUser (Subject currentUser, Exchange exchange)
specifier|private
name|void
name|authorizeUser
parameter_list|(
name|Subject
name|currentUser
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelAuthorizationException
block|{
name|boolean
name|authorized
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|policy
operator|.
name|getPermissionsList
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|policy
operator|.
name|isAllPermissionsRequired
argument_list|()
condition|)
block|{
name|authorized
operator|=
name|currentUser
operator|.
name|isPermittedAll
argument_list|(
name|policy
operator|.
name|getPermissionsList
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|Permission
name|permission
range|:
name|policy
operator|.
name|getPermissionsList
argument_list|()
control|)
block|{
if|if
condition|(
name|currentUser
operator|.
name|isPermitted
argument_list|(
name|permission
argument_list|)
condition|)
block|{
name|authorized
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Valid Permissions List not specified for ShiroSecurityPolicy. No authorization checks will be performed for current user."
argument_list|)
expr_stmt|;
name|authorized
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|authorized
condition|)
block|{
throw|throw
operator|new
name|CamelAuthorizationException
argument_list|(
literal|"Authorization Failed. Subject's role set does not have the necessary permissions to perform further processing."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Current user {} is successfully authorized."
argument_list|,
name|currentUser
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

