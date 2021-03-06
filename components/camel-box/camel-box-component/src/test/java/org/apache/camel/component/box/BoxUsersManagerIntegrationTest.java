begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxUser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|CreateUserParams
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|EmailAlias
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
name|builder
operator|.
name|RouteBuilder
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
name|box
operator|.
name|api
operator|.
name|BoxUsersManager
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
name|box
operator|.
name|internal
operator|.
name|BoxApiCollection
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
name|box
operator|.
name|internal
operator|.
name|BoxUsersManagerApiMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assume
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
comment|/**  * Test class for {@link BoxUsersManager}  * APIs.  */
end_comment

begin_class
DECL|class|BoxUsersManagerIntegrationTest
specifier|public
class|class
name|BoxUsersManagerIntegrationTest
extends|extends
name|AbstractBoxTestSupport
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
name|BoxUsersManagerIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|BoxApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|BoxUsersManagerApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|CAMEL_TEST_USER_EMAIL_ALIAS
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_USER_EMAIL_ALIAS
init|=
literal|"camel@example.com"
decl_stmt|;
DECL|field|CAMEL_TEST_USER_JOB_TITLE
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_USER_JOB_TITLE
init|=
literal|"Camel Tester"
decl_stmt|;
DECL|field|CAMEL_TEST_CREATE_APP_USER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_CREATE_APP_USER_NAME
init|=
literal|"Wilma"
decl_stmt|;
DECL|field|CAMEL_TEST_CREATE_ENTERPRISE_USER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_CREATE_ENTERPRISE_USER_NAME
init|=
literal|"fred"
decl_stmt|;
DECL|field|CAMEL_TEST_CREATE_ENTERPRISE_USER2_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_CREATE_ENTERPRISE_USER2_NAME
init|=
literal|"gregory"
decl_stmt|;
DECL|field|CAMEL_TEST_ENTERPRISE_USER_LOGIN_KEY
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_ENTERPRISE_USER_LOGIN_KEY
init|=
literal|"enterpriseUser1Login"
decl_stmt|;
DECL|field|CAMEL_TEST_ENTERPRISE_USER2_LOGIN_KEY
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_ENTERPRISE_USER2_LOGIN_KEY
init|=
literal|"enterpriseUser2Login"
decl_stmt|;
DECL|field|testUser
specifier|private
name|BoxUser
name|testUser
decl_stmt|;
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testAddUserEmailAlias ()
specifier|public
name|void
name|testAddUserEmailAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|EmailAlias
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.userId"
argument_list|,
name|testUser
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.email"
argument_list|,
name|CAMEL_TEST_USER_EMAIL_ALIAS
argument_list|)
expr_stmt|;
name|result
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://ADDUSEREMAILALIAS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"addUserEmailAlias result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"addUserEmailAlias: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|testUser
operator|.
name|deleteEmailAlias
argument_list|(
name|result
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{                 }
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testCreateAppUser ()
specifier|public
name|void
name|testCreateAppUser
parameter_list|()
throws|throws
name|Exception
block|{
comment|//This test makes sense only with JWT authentication. With standard (OAuth) it will always fail.
name|Assume
operator|.
name|assumeTrue
argument_list|(
literal|"Test has to be executed with standard authentication."
argument_list|,
name|jwtAuthentoication
argument_list|)
expr_stmt|;
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxUser
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|CreateUserParams
name|params
init|=
operator|new
name|CreateUserParams
argument_list|()
decl_stmt|;
name|params
operator|.
name|setSpaceAmount
argument_list|(
literal|1073741824
argument_list|)
expr_stmt|;
comment|// 1 GB
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.name"
argument_list|,
name|CAMEL_TEST_CREATE_APP_USER_NAME
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.CreateUserParams
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.params"
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|result
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://CREATEAPPUSER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"createAppUser result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"createAppUser: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|result
operator|.
name|delete
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{                 }
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testCreateEnterpriseUser ()
specifier|public
name|void
name|testCreateEnterpriseUser
parameter_list|()
throws|throws
name|Exception
block|{
comment|//This test makes sense only with standard (OAuth) authentication, with JWT it will always fail with return code 403
name|Assume
operator|.
name|assumeFalse
argument_list|(
literal|"Test has to be executed with standard authentication."
argument_list|,
name|jwtAuthentoication
argument_list|)
expr_stmt|;
name|String
name|enterpriseUser1Login
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|CAMEL_TEST_ENTERPRISE_USER_LOGIN_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|enterpriseUser1Login
operator|!=
literal|null
operator|&&
literal|""
operator|.
name|equals
argument_list|(
name|enterpriseUser1Login
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|enterpriseUser1Login
operator|=
literal|null
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"Email for enterprise user has to be defined in test-options.properties for this test to succeed."
argument_list|,
name|enterpriseUser1Login
argument_list|)
expr_stmt|;
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxUser
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|CreateUserParams
name|params
init|=
operator|new
name|CreateUserParams
argument_list|()
decl_stmt|;
name|params
operator|.
name|setSpaceAmount
argument_list|(
literal|1073741824
argument_list|)
expr_stmt|;
comment|// 1 GB
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.login"
argument_list|,
name|enterpriseUser1Login
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.name"
argument_list|,
name|CAMEL_TEST_CREATE_ENTERPRISE_USER_NAME
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.CreateUserParams
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.params"
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|result
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://CREATEENTERPRISEUSER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"createEnterpriseUser result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"createEnterpriseUser: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|result
operator|.
name|delete
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{                 }
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testDeleteUser ()
specifier|public
name|void
name|testDeleteUser
parameter_list|()
throws|throws
name|Exception
block|{
comment|//This test makes sense only with JWT authentication. With standard (OAuth) it will always fail.
name|Assume
operator|.
name|assumeTrue
argument_list|(
literal|"Test has to be executed with standard authentication."
argument_list|,
name|jwtAuthentoication
argument_list|)
expr_stmt|;
name|BoxUser
operator|.
name|Info
name|info
init|=
name|BoxUser
operator|.
name|createAppUser
argument_list|(
name|getConnection
argument_list|()
argument_list|,
name|CAMEL_TEST_CREATE_APP_USER_NAME
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.userId"
argument_list|,
name|info
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.notifyUser"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.force"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://DELETEUSER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|//give some time to delete task to be finished
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|Iterable
argument_list|<
name|BoxUser
operator|.
name|Info
argument_list|>
name|it
init|=
name|BoxUser
operator|.
name|getAllEnterpriseUsers
argument_list|(
name|getConnection
argument_list|()
argument_list|,
name|CAMEL_TEST_CREATE_APP_USER_NAME
argument_list|)
decl_stmt|;
name|int
name|searchResults
init|=
name|sizeOfIterable
argument_list|(
name|it
argument_list|)
decl_stmt|;
name|boolean
name|exists
init|=
name|searchResults
operator|>
literal|0
condition|?
literal|true
else|:
literal|false
decl_stmt|;
name|assertEquals
argument_list|(
literal|"deleteUser exists"
argument_list|,
literal|false
argument_list|,
name|exists
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"deleteUser: exists? "
operator|+
name|exists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testDeleteUserEmailAlias ()
specifier|public
name|void
name|testDeleteUserEmailAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|EmailAlias
name|emailAlias
init|=
literal|null
decl_stmt|;
try|try
block|{
name|emailAlias
operator|=
name|testUser
operator|.
name|addEmailAlias
argument_list|(
name|CAMEL_TEST_USER_EMAIL_ALIAS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.userId"
argument_list|,
name|testUser
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.emailAliasId"
argument_list|,
name|emailAlias
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://DELETEUSEREMAILALIAS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"deleteUserEmailAlias email aliases"
argument_list|,
name|testUser
operator|.
name|getEmailAliases
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"deleteUserEmailAlias email aliases"
argument_list|,
literal|0
argument_list|,
name|testUser
operator|.
name|getEmailAliases
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAllEnterpriseOrExternalUsers ()
specifier|public
name|void
name|testGetAllEnterpriseOrExternalUsers
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.filterTerm"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is String[]
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.fields"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|final
name|java
operator|.
name|util
operator|.
name|List
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETALLENTERPRISEOREXTERNALUSERS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getAllEnterpriseOrExternalUsers result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getAllEnterpriseOrExternalUsers: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetCurrentUser ()
specifier|public
name|void
name|testGetCurrentUser
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxUser
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETCURRENTUSER"
argument_list|,
name|testUser
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getCurrentUser result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getCurrentUser: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetUserEmailAlias ()
specifier|public
name|void
name|testGetUserEmailAlias
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "userId"
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|final
name|java
operator|.
name|util
operator|.
name|Collection
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETUSEREMAILALIAS"
argument_list|,
name|testUser
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getUserEmailAlias result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getUserEmailAlias: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetUserInfo ()
specifier|public
name|void
name|testGetUserInfo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "userId"
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxUser
operator|.
name|Info
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETUSERINFO"
argument_list|,
name|testUser
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getUserInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getUserInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdateUserInfo ()
specifier|public
name|void
name|testUpdateUserInfo
parameter_list|()
throws|throws
name|Exception
block|{
comment|//This test makes sense only with standard (OAuth) authentication, with JWT it will always fail with return code 403
name|Assume
operator|.
name|assumeFalse
argument_list|(
literal|"Test has to be executed with standard authentication."
argument_list|,
name|jwtAuthentoication
argument_list|)
expr_stmt|;
name|BoxUser
operator|.
name|Info
name|info
init|=
name|testUser
operator|.
name|getInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|setJobTitle
argument_list|(
name|CAMEL_TEST_USER_JOB_TITLE
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.userId"
argument_list|,
name|testUser
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxUser.Info
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.info"
argument_list|,
name|info
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxUser
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATEUSERINFO"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"updateUserInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"updateUserInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|info
operator|=
name|testUser
operator|.
name|getInfo
argument_list|()
expr_stmt|;
name|info
operator|.
name|setJobTitle
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|testUser
operator|.
name|updateInfo
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testmMoveFolderToUser ()
specifier|public
name|void
name|testmMoveFolderToUser
parameter_list|()
throws|throws
name|Exception
block|{
comment|//This test makes sense only with standard (OAuth) authentication, with JWT it will always fail with return code 403
name|Assume
operator|.
name|assumeFalse
argument_list|(
literal|"Test has to be executed with standard authentication."
argument_list|,
name|jwtAuthentoication
argument_list|)
expr_stmt|;
name|String
name|enterpriseUser1Login
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|CAMEL_TEST_ENTERPRISE_USER_LOGIN_KEY
argument_list|)
decl_stmt|;
name|String
name|enterpriseUser2Login
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|CAMEL_TEST_ENTERPRISE_USER2_LOGIN_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|enterpriseUser1Login
operator|!=
literal|null
operator|&&
literal|""
operator|.
name|equals
argument_list|(
name|enterpriseUser1Login
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|enterpriseUser1Login
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|enterpriseUser2Login
operator|!=
literal|null
operator|&&
literal|""
operator|.
name|equals
argument_list|(
name|enterpriseUser2Login
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|enterpriseUser2Login
operator|=
literal|null
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"Email for enterprise user has to be defined in test-options.properties for this test to succeed."
argument_list|,
name|enterpriseUser1Login
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Email for enterprise user2 has to be defined in test-options.properties for this test to succeed."
argument_list|,
name|enterpriseUser2Login
argument_list|)
expr_stmt|;
name|BoxUser
operator|.
name|Info
name|user1
init|=
literal|null
decl_stmt|;
name|BoxUser
operator|.
name|Info
name|user2
init|=
literal|null
decl_stmt|;
try|try
block|{
name|user1
operator|=
name|BoxUser
operator|.
name|createEnterpriseUser
argument_list|(
name|getConnection
argument_list|()
argument_list|,
name|enterpriseUser1Login
argument_list|,
name|CAMEL_TEST_CREATE_ENTERPRISE_USER_NAME
argument_list|)
expr_stmt|;
name|user2
operator|=
name|BoxUser
operator|.
name|createEnterpriseUser
argument_list|(
name|getConnection
argument_list|()
argument_list|,
name|enterpriseUser2Login
argument_list|,
name|CAMEL_TEST_CREATE_ENTERPRISE_USER2_NAME
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.userId"
argument_list|,
name|user1
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.sourceUserId"
argument_list|,
name|user2
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
operator|.
name|Info
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://MOVEFOLDERTOUSER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"moveFolderToUser result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|user1
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|user1
operator|.
name|getResource
argument_list|()
operator|.
name|delete
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{                 }
block|}
if|if
condition|(
name|user2
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|user2
operator|.
name|getResource
argument_list|()
operator|.
name|delete
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{                 }
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for addUserEmailAlias
name|from
argument_list|(
literal|"direct://ADDUSEREMAILALIAS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addUserEmailAlias"
argument_list|)
expr_stmt|;
comment|// test route for createAppUser
name|from
argument_list|(
literal|"direct://CREATEAPPUSER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/createAppUser"
argument_list|)
expr_stmt|;
comment|// test route for createEnterpriseUser
name|from
argument_list|(
literal|"direct://CREATEENTERPRISEUSER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/createEnterpriseUser"
argument_list|)
expr_stmt|;
comment|// test route for deleteUser
name|from
argument_list|(
literal|"direct://DELETEUSER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/deleteUser"
argument_list|)
expr_stmt|;
comment|// test route for deleteUserEmailAlias
name|from
argument_list|(
literal|"direct://DELETEUSEREMAILALIAS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/deleteUserEmailAlias"
argument_list|)
expr_stmt|;
comment|// test route for getAllEnterpriseOrExternalUsers
name|from
argument_list|(
literal|"direct://GETALLENTERPRISEOREXTERNALUSERS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getAllEnterpriseOrExternalUsers"
argument_list|)
expr_stmt|;
comment|// test route for getCurrentUser
name|from
argument_list|(
literal|"direct://GETCURRENTUSER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getCurrentUser"
argument_list|)
expr_stmt|;
comment|// test route for getUserEmailAlias
name|from
argument_list|(
literal|"direct://GETUSEREMAILALIAS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getUserEmailAlias?inBody=userId"
argument_list|)
expr_stmt|;
comment|// test route for getUserInfo
name|from
argument_list|(
literal|"direct://GETUSERINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getUserInfo?inBody=userId"
argument_list|)
expr_stmt|;
comment|// test route for updateUserInfo
name|from
argument_list|(
literal|"direct://UPDATEUSERINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/updateUserInfo"
argument_list|)
expr_stmt|;
comment|// test route for moveFolderToUser
name|from
argument_list|(
literal|"direct://MOVEFOLDERTOUSER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/moveFolderToUser"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Before
DECL|method|setupTest ()
specifier|public
name|void
name|setupTest
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestUser
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|teardownTest ()
specifier|public
name|void
name|teardownTest
parameter_list|()
block|{
name|deleteTestUser
argument_list|()
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|BoxAPIConnection
name|getConnection
parameter_list|()
block|{
name|BoxEndpoint
name|endpoint
init|=
operator|(
name|BoxEndpoint
operator|)
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addUserEmailAlias"
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|getBoxConnection
argument_list|()
return|;
block|}
DECL|method|createTestUser ()
specifier|private
name|void
name|createTestUser
parameter_list|()
block|{
name|testUser
operator|=
name|getCurrentUser
argument_list|()
expr_stmt|;
block|}
DECL|method|deleteTestUser ()
specifier|private
name|void
name|deleteTestUser
parameter_list|()
block|{
if|if
condition|(
name|testUser
operator|!=
literal|null
condition|)
block|{
name|testUser
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getCurrentUser ()
specifier|private
name|BoxUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|BoxUser
operator|.
name|getCurrentUser
argument_list|(
name|getConnection
argument_list|()
argument_list|)
return|;
block|}
DECL|method|sizeOfIterable (Iterable<?> it)
specifier|private
name|int
name|sizeOfIterable
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|it
parameter_list|)
block|{
if|if
condition|(
name|it
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|it
operator|)
operator|.
name|size
argument_list|()
return|;
block|}
else|else
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Object
name|obj
range|:
name|it
control|)
block|{
name|i
operator|++
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
block|}
block|}
end_class

end_unit

