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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|AsyncProcessor
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
name|impl
operator|.
name|converter
operator|.
name|AsyncProcessorTypeConverter
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
name|model
operator|.
name|ProcessorDefinition
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
name|AuthorizationPolicy
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
name|RouteContext
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
name|AsyncProcessorHelper
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
name|config
operator|.
name|Ini
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
name|config
operator|.
name|IniSecurityManagerFactory
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
name|crypto
operator|.
name|AesCipherService
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
name|crypto
operator|.
name|CipherService
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
name|mgt
operator|.
name|SecurityManager
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
name|apache
operator|.
name|shiro
operator|.
name|util
operator|.
name|Factory
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

begin_class
DECL|class|ShiroSecurityPolicy
specifier|public
class|class
name|ShiroSecurityPolicy
implements|implements
name|AuthorizationPolicy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ShiroSecurityPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bits128
specifier|private
specifier|final
name|byte
index|[]
name|bits128
init|=
block|{
operator|(
name|byte
operator|)
literal|0x08
block|,
operator|(
name|byte
operator|)
literal|0x09
block|,
operator|(
name|byte
operator|)
literal|0x0A
block|,
operator|(
name|byte
operator|)
literal|0x0B
block|,
operator|(
name|byte
operator|)
literal|0x0C
block|,
operator|(
name|byte
operator|)
literal|0x0D
block|,
operator|(
name|byte
operator|)
literal|0x0E
block|,
operator|(
name|byte
operator|)
literal|0x0F
block|,
operator|(
name|byte
operator|)
literal|0x10
block|,
operator|(
name|byte
operator|)
literal|0x11
block|,
operator|(
name|byte
operator|)
literal|0x12
block|,
operator|(
name|byte
operator|)
literal|0x13
block|,
operator|(
name|byte
operator|)
literal|0x14
block|,
operator|(
name|byte
operator|)
literal|0x15
block|,
operator|(
name|byte
operator|)
literal|0x16
block|,
operator|(
name|byte
operator|)
literal|0x17
block|}
decl_stmt|;
DECL|field|cipherService
specifier|private
name|CipherService
name|cipherService
decl_stmt|;
DECL|field|passPhrase
specifier|private
name|byte
index|[]
name|passPhrase
decl_stmt|;
DECL|field|securityManager
specifier|private
name|SecurityManager
name|securityManager
decl_stmt|;
DECL|field|permissionsList
specifier|private
name|List
argument_list|<
name|Permission
argument_list|>
name|permissionsList
decl_stmt|;
DECL|field|alwaysReauthenticate
specifier|private
name|boolean
name|alwaysReauthenticate
decl_stmt|;
DECL|method|ShiroSecurityPolicy ()
specifier|public
name|ShiroSecurityPolicy
parameter_list|()
block|{
name|this
operator|.
name|passPhrase
operator|=
name|bits128
expr_stmt|;
comment|// Set up AES encryption based cipher service, by default
name|cipherService
operator|=
operator|new
name|AesCipherService
argument_list|()
expr_stmt|;
name|permissionsList
operator|=
operator|new
name|ArrayList
argument_list|<
name|Permission
argument_list|>
argument_list|()
expr_stmt|;
name|alwaysReauthenticate
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|ShiroSecurityPolicy (String iniResourcePath)
specifier|public
name|ShiroSecurityPolicy
parameter_list|(
name|String
name|iniResourcePath
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|Factory
argument_list|<
name|SecurityManager
argument_list|>
name|factory
init|=
operator|new
name|IniSecurityManagerFactory
argument_list|(
name|iniResourcePath
argument_list|)
decl_stmt|;
name|securityManager
operator|=
operator|(
name|SecurityManager
operator|)
name|factory
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|SecurityUtils
operator|.
name|setSecurityManager
argument_list|(
name|securityManager
argument_list|)
expr_stmt|;
block|}
DECL|method|ShiroSecurityPolicy (Ini ini)
specifier|public
name|ShiroSecurityPolicy
parameter_list|(
name|Ini
name|ini
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|Factory
argument_list|<
name|SecurityManager
argument_list|>
name|factory
init|=
operator|new
name|IniSecurityManagerFactory
argument_list|(
name|ini
argument_list|)
decl_stmt|;
name|securityManager
operator|=
operator|(
name|SecurityManager
operator|)
name|factory
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|SecurityUtils
operator|.
name|setSecurityManager
argument_list|(
name|securityManager
argument_list|)
expr_stmt|;
block|}
DECL|method|ShiroSecurityPolicy (String iniResourcePath, byte[] passPhrase)
specifier|public
name|ShiroSecurityPolicy
parameter_list|(
name|String
name|iniResourcePath
parameter_list|,
name|byte
index|[]
name|passPhrase
parameter_list|)
block|{
name|this
argument_list|(
name|iniResourcePath
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPassPhrase
argument_list|(
name|passPhrase
argument_list|)
expr_stmt|;
block|}
DECL|method|ShiroSecurityPolicy (Ini ini, byte[] passPhrase)
specifier|public
name|ShiroSecurityPolicy
parameter_list|(
name|Ini
name|ini
parameter_list|,
name|byte
index|[]
name|passPhrase
parameter_list|)
block|{
name|this
argument_list|(
name|ini
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPassPhrase
argument_list|(
name|passPhrase
argument_list|)
expr_stmt|;
block|}
DECL|method|ShiroSecurityPolicy (String iniResourcePath, byte[] passPhrase, boolean alwaysReauthenticate)
specifier|public
name|ShiroSecurityPolicy
parameter_list|(
name|String
name|iniResourcePath
parameter_list|,
name|byte
index|[]
name|passPhrase
parameter_list|,
name|boolean
name|alwaysReauthenticate
parameter_list|)
block|{
name|this
argument_list|(
name|iniResourcePath
argument_list|,
name|passPhrase
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAlwaysReauthenticate
argument_list|(
name|alwaysReauthenticate
argument_list|)
expr_stmt|;
block|}
DECL|method|ShiroSecurityPolicy (Ini ini, byte[] passPhrase, boolean alwaysReauthenticate)
specifier|public
name|ShiroSecurityPolicy
parameter_list|(
name|Ini
name|ini
parameter_list|,
name|byte
index|[]
name|passPhrase
parameter_list|,
name|boolean
name|alwaysReauthenticate
parameter_list|)
block|{
name|this
argument_list|(
name|ini
argument_list|,
name|passPhrase
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAlwaysReauthenticate
argument_list|(
name|alwaysReauthenticate
argument_list|)
expr_stmt|;
block|}
DECL|method|ShiroSecurityPolicy (String iniResourcePath, byte[] passPhrase, boolean alwaysReauthenticate, List<Permission> permissionsList)
specifier|public
name|ShiroSecurityPolicy
parameter_list|(
name|String
name|iniResourcePath
parameter_list|,
name|byte
index|[]
name|passPhrase
parameter_list|,
name|boolean
name|alwaysReauthenticate
parameter_list|,
name|List
argument_list|<
name|Permission
argument_list|>
name|permissionsList
parameter_list|)
block|{
name|this
argument_list|(
name|iniResourcePath
argument_list|,
name|passPhrase
argument_list|,
name|alwaysReauthenticate
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPermissionsList
argument_list|(
name|permissionsList
argument_list|)
expr_stmt|;
block|}
DECL|method|ShiroSecurityPolicy (Ini ini, byte[] passPhrase, boolean alwaysReauthenticate, List<Permission> permissionsList)
specifier|public
name|ShiroSecurityPolicy
parameter_list|(
name|Ini
name|ini
parameter_list|,
name|byte
index|[]
name|passPhrase
parameter_list|,
name|boolean
name|alwaysReauthenticate
parameter_list|,
name|List
argument_list|<
name|Permission
argument_list|>
name|permissionsList
parameter_list|)
block|{
name|this
argument_list|(
name|ini
argument_list|,
name|passPhrase
argument_list|,
name|alwaysReauthenticate
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPermissionsList
argument_list|(
name|permissionsList
argument_list|)
expr_stmt|;
block|}
DECL|method|beforeWrap (RouteContext routeContext, ProcessorDefinition<?> definition)
specifier|public
name|void
name|beforeWrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
comment|//Not implemented
block|}
DECL|method|wrap (RouteContext routeContext, final Processor processor)
specifier|public
name|Processor
name|wrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|)
block|{
return|return
operator|new
name|AsyncProcessor
argument_list|()
block|{
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|boolean
name|sync
decl_stmt|;
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
comment|// If here, then user is authenticated and authorized
comment|// Now let the original processor continue routing supporting the async routing engine
name|AsyncProcessor
name|ap
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|sync
operator|=
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|ap
argument_list|,
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// we only have to handle async completion of this policy
if|if
condition|(
name|doneSync
condition|)
block|{
return|return;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
comment|// if async, continue routing async
return|return
literal|false
return|;
block|}
comment|// we are done synchronously, so do our after work and invoke the callback
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
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|applySecurityPolicy
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
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
init|=
operator|(
name|ByteSource
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"SHIRO_SECURITY_TOKEN"
argument_list|)
decl_stmt|;
name|ByteSource
name|decryptedToken
init|=
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
init|=
operator|(
name|ShiroSecurityToken
operator|)
name|objectInputStream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|objectInputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|byteArrayInputStream
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|alwaysReauthenticate
condition|)
block|{
name|currentUser
operator|.
name|logout
argument_list|()
expr_stmt|;
name|currentUser
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
block|}
return|;
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
if|if
condition|(
operator|!
name|currentUser
operator|.
name|isAuthenticated
argument_list|()
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
name|alwaysReauthenticate
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Current User "
operator|+
name|currentUser
operator|.
name|getPrincipal
argument_list|()
operator|+
literal|" successfully authenticated"
argument_list|)
expr_stmt|;
block|}
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
name|permissionsList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Permission
name|permission
range|:
name|permissionsList
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
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Valid Permissions List not specified for ShiroSecurityPolicy. No authorization checks will be performed for current user"
argument_list|)
expr_stmt|;
block|}
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
literal|"Authorization Failed. Subject's role set does not have the necessary permissions to perform further processing"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Current User "
operator|+
name|currentUser
operator|.
name|getPrincipal
argument_list|()
operator|+
literal|" is successfully authorized. The exchange will be allowed to proceed"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getCipherService ()
specifier|public
name|CipherService
name|getCipherService
parameter_list|()
block|{
return|return
name|cipherService
return|;
block|}
DECL|method|setCipherService (CipherService cipherService)
specifier|public
name|void
name|setCipherService
parameter_list|(
name|CipherService
name|cipherService
parameter_list|)
block|{
name|this
operator|.
name|cipherService
operator|=
name|cipherService
expr_stmt|;
block|}
DECL|method|getSecurityManager ()
specifier|public
name|SecurityManager
name|getSecurityManager
parameter_list|()
block|{
return|return
name|securityManager
return|;
block|}
DECL|method|setSecurityManager (SecurityManager securityManager)
specifier|public
name|void
name|setSecurityManager
parameter_list|(
name|SecurityManager
name|securityManager
parameter_list|)
block|{
name|this
operator|.
name|securityManager
operator|=
name|securityManager
expr_stmt|;
block|}
DECL|method|getPassPhrase ()
specifier|public
name|byte
index|[]
name|getPassPhrase
parameter_list|()
block|{
return|return
name|passPhrase
return|;
block|}
DECL|method|setPassPhrase (byte[] passPhrase)
specifier|public
name|void
name|setPassPhrase
parameter_list|(
name|byte
index|[]
name|passPhrase
parameter_list|)
block|{
name|this
operator|.
name|passPhrase
operator|=
name|passPhrase
expr_stmt|;
block|}
DECL|method|getPermissionsList ()
specifier|public
name|List
argument_list|<
name|Permission
argument_list|>
name|getPermissionsList
parameter_list|()
block|{
return|return
name|permissionsList
return|;
block|}
DECL|method|setPermissionsList (List<Permission> permissionsList)
specifier|public
name|void
name|setPermissionsList
parameter_list|(
name|List
argument_list|<
name|Permission
argument_list|>
name|permissionsList
parameter_list|)
block|{
name|this
operator|.
name|permissionsList
operator|=
name|permissionsList
expr_stmt|;
block|}
DECL|method|isAlwaysReauthenticate ()
specifier|public
name|boolean
name|isAlwaysReauthenticate
parameter_list|()
block|{
return|return
name|alwaysReauthenticate
return|;
block|}
DECL|method|setAlwaysReauthenticate (boolean alwaysReauthenticate)
specifier|public
name|void
name|setAlwaysReauthenticate
parameter_list|(
name|boolean
name|alwaysReauthenticate
parameter_list|)
block|{
name|this
operator|.
name|alwaysReauthenticate
operator|=
name|alwaysReauthenticate
expr_stmt|;
block|}
block|}
end_class

end_unit

