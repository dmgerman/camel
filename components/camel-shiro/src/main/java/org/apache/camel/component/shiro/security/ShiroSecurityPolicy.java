begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|NamedNode
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
DECL|field|rolesList
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|rolesList
decl_stmt|;
DECL|field|alwaysReauthenticate
specifier|private
name|boolean
name|alwaysReauthenticate
decl_stmt|;
DECL|field|base64
specifier|private
name|boolean
name|base64
decl_stmt|;
DECL|field|allPermissionsRequired
specifier|private
name|boolean
name|allPermissionsRequired
decl_stmt|;
DECL|field|allRolesRequired
specifier|private
name|boolean
name|allRolesRequired
decl_stmt|;
DECL|method|ShiroSecurityPolicy ()
specifier|public
name|ShiroSecurityPolicy
parameter_list|()
block|{
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
argument_list|<>
argument_list|()
expr_stmt|;
name|rolesList
operator|=
operator|new
name|ArrayList
argument_list|<>
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
annotation|@
name|Override
DECL|method|beforeWrap (RouteContext routeContext, NamedNode definition)
specifier|public
name|void
name|beforeWrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
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
literal|"Securing route {} using Shiro policy {}"
argument_list|,
name|routeContext
operator|.
name|getRouteId
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ShiroSecurityProcessor
argument_list|(
name|processor
argument_list|,
name|this
argument_list|)
return|;
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
DECL|method|isBase64 ()
specifier|public
name|boolean
name|isBase64
parameter_list|()
block|{
return|return
name|base64
return|;
block|}
DECL|method|setBase64 (boolean base64)
specifier|public
name|void
name|setBase64
parameter_list|(
name|boolean
name|base64
parameter_list|)
block|{
name|this
operator|.
name|base64
operator|=
name|base64
expr_stmt|;
block|}
DECL|method|isAllPermissionsRequired ()
specifier|public
name|boolean
name|isAllPermissionsRequired
parameter_list|()
block|{
return|return
name|allPermissionsRequired
return|;
block|}
DECL|method|setAllPermissionsRequired (boolean allPermissionsRequired)
specifier|public
name|void
name|setAllPermissionsRequired
parameter_list|(
name|boolean
name|allPermissionsRequired
parameter_list|)
block|{
name|this
operator|.
name|allPermissionsRequired
operator|=
name|allPermissionsRequired
expr_stmt|;
block|}
DECL|method|getRolesList ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRolesList
parameter_list|()
block|{
return|return
name|rolesList
return|;
block|}
DECL|method|setRolesList (List<String> rolesList)
specifier|public
name|void
name|setRolesList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|rolesList
parameter_list|)
block|{
name|this
operator|.
name|rolesList
operator|=
name|rolesList
expr_stmt|;
block|}
DECL|method|isAllRolesRequired ()
specifier|public
name|boolean
name|isAllRolesRequired
parameter_list|()
block|{
return|return
name|allRolesRequired
return|;
block|}
DECL|method|setAllRolesRequired (boolean allRolesRequired)
specifier|public
name|void
name|setAllRolesRequired
parameter_list|(
name|boolean
name|allRolesRequired
parameter_list|)
block|{
name|this
operator|.
name|allRolesRequired
operator|=
name|allRolesRequired
expr_stmt|;
block|}
block|}
end_class

end_unit

