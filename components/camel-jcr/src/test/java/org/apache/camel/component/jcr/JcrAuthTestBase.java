begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|SimpleCredentials
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|api
operator|.
name|jsr283
operator|.
name|security
operator|.
name|AccessControlManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|api
operator|.
name|jsr283
operator|.
name|security
operator|.
name|AccessControlPolicyIterator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|api
operator|.
name|security
operator|.
name|user
operator|.
name|User
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|api
operator|.
name|security
operator|.
name|user
operator|.
name|UserManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|core
operator|.
name|SessionImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|core
operator|.
name|TransientRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|core
operator|.
name|security
operator|.
name|authorization
operator|.
name|JackrabbitAccessControlList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_comment
comment|/**  * Base class for tests that use authentication/authorization in the repository.  * Ensures that the transient repo is set up properly for each test.  */
end_comment

begin_class
DECL|class|JcrAuthTestBase
specifier|public
specifier|abstract
class|class
name|JcrAuthTestBase
extends|extends
name|CamelTestSupport
block|{
DECL|field|BASE_REPO_PATH
specifier|protected
specifier|static
specifier|final
name|String
name|BASE_REPO_PATH
init|=
literal|"/home/test"
decl_stmt|;
DECL|field|CONFIG_FILE
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG_FILE
init|=
literal|"target/test-classes/repository_with_auth.xml"
decl_stmt|;
DECL|field|repository
specifier|private
name|Repository
name|repository
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/repository"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|File
name|config
init|=
operator|new
name|File
argument_list|(
name|CONFIG_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|config
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"missing config file: "
operator|+
name|config
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
name|repository
operator|=
operator|new
name|TransientRepository
argument_list|(
name|CONFIG_FILE
argument_list|,
literal|"target/repository_with_auth"
argument_list|)
expr_stmt|;
comment|// set up a user to authenticate
name|SessionImpl
name|session
init|=
operator|(
name|SessionImpl
operator|)
name|repository
operator|.
name|login
argument_list|(
operator|new
name|SimpleCredentials
argument_list|(
literal|"admin"
argument_list|,
literal|"admin"
operator|.
name|toCharArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|UserManager
name|userManager
init|=
name|session
operator|.
name|getUserManager
argument_list|()
decl_stmt|;
name|User
name|user
init|=
operator|(
name|User
operator|)
name|userManager
operator|.
name|getAuthorizable
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
name|user
operator|=
name|userManager
operator|.
name|createUser
argument_list|(
literal|"test"
argument_list|,
literal|"quatloos"
argument_list|)
expr_stmt|;
block|}
comment|// set up permissions
name|String
name|permissionsPath
init|=
name|session
operator|.
name|getRootNode
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|AccessControlManager
name|accessControlManager
init|=
name|session
operator|.
name|getAccessControlManager
argument_list|()
decl_stmt|;
name|AccessControlPolicyIterator
name|acls
init|=
name|accessControlManager
operator|.
name|getApplicablePolicies
argument_list|(
name|permissionsPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|acls
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|JackrabbitAccessControlList
name|acl
init|=
operator|(
name|JackrabbitAccessControlList
operator|)
name|acls
operator|.
name|nextAccessControlPolicy
argument_list|()
decl_stmt|;
name|acl
operator|.
name|addEntry
argument_list|(
name|user
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|accessControlManager
operator|.
name|getSupportedPrivileges
argument_list|(
name|permissionsPath
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|accessControlManager
operator|.
name|setPolicy
argument_list|(
name|permissionsPath
argument_list|,
name|acl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"could not set access control for path "
operator|+
name|permissionsPath
argument_list|)
throw|;
block|}
name|session
operator|.
name|save
argument_list|()
expr_stmt|;
name|session
operator|.
name|logout
argument_list|()
expr_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"repository"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getRepository ()
specifier|protected
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
block|}
end_class

end_unit

