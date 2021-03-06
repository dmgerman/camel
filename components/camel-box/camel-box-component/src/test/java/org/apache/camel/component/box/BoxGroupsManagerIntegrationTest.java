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
name|BoxGroup
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
name|BoxGroupMembership
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
name|BoxGroupsManager
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
name|BoxGroupsManagerApiMethod
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
name|Before
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
comment|/**  * Test class for {@link BoxGroupsManager} APIs.  */
end_comment

begin_class
DECL|class|BoxGroupsManagerIntegrationTest
specifier|public
class|class
name|BoxGroupsManagerIntegrationTest
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
name|BoxGroupsManagerIntegrationTest
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
name|BoxGroupsManagerApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|CAMEL_TEST_GROUP_DESCRIPTION
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_GROUP_DESCRIPTION
init|=
literal|"CamelTestGroupDescription"
decl_stmt|;
DECL|field|CAMEL_TEST_GROUP_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_GROUP_NAME
init|=
literal|"CamelTestGroup"
decl_stmt|;
DECL|field|CAMEL_TEST_CREATE_GROUP_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_CREATE_GROUP_NAME
init|=
literal|"CamelTestCreateGroup"
decl_stmt|;
DECL|field|testGroup
specifier|private
name|BoxGroup
name|testGroup
decl_stmt|;
DECL|field|testUser
specifier|private
name|BoxUser
name|testUser
decl_stmt|;
annotation|@
name|Test
DECL|method|testAddGroupMembership ()
specifier|public
name|void
name|testAddGroupMembership
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
literal|"CamelBox.groupId"
argument_list|,
name|testGroup
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
literal|"CamelBox.userId"
argument_list|,
name|testUser
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxGroupMembership.Role
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.role"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxGroupMembership
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://ADDGROUPMEMBERSHIP"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"addGroupMembership result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"addGroupMembership: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateGroup ()
specifier|public
name|void
name|testCreateGroup
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
name|BoxGroup
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// using String message body for single parameter "name"
name|result
operator|=
name|requestBody
argument_list|(
literal|"direct://CREATEGROUP"
argument_list|,
name|CAMEL_TEST_CREATE_GROUP_NAME
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"createGroup result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CAMEL_TEST_CREATE_GROUP_NAME
argument_list|,
name|result
operator|.
name|getInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"createGroup: "
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
argument_list|()
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
DECL|method|testDeleteGroup ()
specifier|public
name|void
name|testDeleteGroup
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "groupId"
name|requestBody
argument_list|(
literal|"direct://DELETEGROUP"
argument_list|,
name|testGroup
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|testGroup
operator|=
literal|null
expr_stmt|;
name|Iterable
argument_list|<
name|BoxGroup
operator|.
name|Info
argument_list|>
name|it
init|=
name|BoxGroup
operator|.
name|getAllGroups
argument_list|(
name|getConnection
argument_list|()
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
literal|"deleteGroup exists"
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
literal|"deleteGroup: exists? "
operator|+
name|exists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteGroupMembership ()
specifier|public
name|void
name|testDeleteGroupMembership
parameter_list|()
throws|throws
name|Exception
block|{
name|BoxGroupMembership
operator|.
name|Info
name|info
init|=
name|testGroup
operator|.
name|addMembership
argument_list|(
name|testUser
argument_list|,
name|BoxGroupMembership
operator|.
name|Role
operator|.
name|MEMBER
argument_list|)
decl_stmt|;
comment|// using String message body for single parameter "groupMembershipId"
name|requestBody
argument_list|(
literal|"direct://DELETEGROUPMEMBERSHIP"
argument_list|,
name|info
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|BoxGroupMembership
operator|.
name|Info
argument_list|>
name|memberships
init|=
name|testGroup
operator|.
name|getMemberships
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"deleteGroupMemberships memberships"
argument_list|,
name|memberships
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"deleteGroupMemberships memberships exists"
argument_list|,
literal|0
argument_list|,
name|memberships
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAllGroups ()
specifier|public
name|void
name|testGetAllGroups
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"direct://GETALLGROUPS"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getAllGroups result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getAllGroups: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetGroupInfo ()
specifier|public
name|void
name|testGetGroupInfo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "groupId"
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxGroup
operator|.
name|Info
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETGROUPINFO"
argument_list|,
name|testGroup
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getGroupInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getGroupInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdateGroupInfo ()
specifier|public
name|void
name|testUpdateGroupInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|BoxGroup
operator|.
name|Info
name|info
init|=
name|testGroup
operator|.
name|getInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
name|CAMEL_TEST_GROUP_DESCRIPTION
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
literal|"CamelBox.groupId"
argument_list|,
name|testGroup
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxGroup.Info
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.groupInfo"
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
name|BoxGroup
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATEGROUPINFO"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"updateGroupInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"updateGroupInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|info
operator|=
name|testGroup
operator|.
name|getInfo
argument_list|()
expr_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|testGroup
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
DECL|method|testGetGroupMembershipInfo ()
specifier|public
name|void
name|testGetGroupMembershipInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|BoxGroupMembership
operator|.
name|Info
name|info
init|=
name|testGroup
operator|.
name|addMembership
argument_list|(
name|testUser
argument_list|,
name|BoxGroupMembership
operator|.
name|Role
operator|.
name|MEMBER
argument_list|)
decl_stmt|;
comment|// using String message body for single parameter "groupMembershipId"
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxGroupMembership
operator|.
name|Info
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETGROUPMEMBERSHIPINFO"
argument_list|,
name|info
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getGroupMembershipInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getGroupMembershipInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetGroupMemberships ()
specifier|public
name|void
name|testGetGroupMemberships
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "groupId"
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
literal|"direct://GETGROUPMEMBERSHIPS"
argument_list|,
name|testGroup
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getGroupMemberships result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getGroupMemberships: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdateGroupMembershipInfo ()
specifier|public
name|void
name|testUpdateGroupMembershipInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|BoxGroupMembership
operator|.
name|Info
name|info
init|=
name|testGroup
operator|.
name|addMembership
argument_list|(
name|testUser
argument_list|,
name|BoxGroupMembership
operator|.
name|Role
operator|.
name|MEMBER
argument_list|)
decl_stmt|;
name|info
operator|.
name|setRole
argument_list|(
name|BoxGroupMembership
operator|.
name|Role
operator|.
name|ADMIN
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
literal|"CamelBox.groupMembershipId"
argument_list|,
name|info
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxGroupMembership.Info
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
name|BoxGroupMembership
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATEGROUPMEMBERSHIPINFO"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"updateGroupMembershipInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"updateGroupMembershipInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
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
comment|// test route for addGroupMembership
name|from
argument_list|(
literal|"direct://ADDGROUPMEMBERSHIP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addGroupMembership"
argument_list|)
expr_stmt|;
comment|// test route for createGroup
name|from
argument_list|(
literal|"direct://CREATEGROUP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/createGroup?inBody=name"
argument_list|)
expr_stmt|;
comment|// test route for deleteGroup
name|from
argument_list|(
literal|"direct://DELETEGROUP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/deleteGroup?inBody=groupId"
argument_list|)
expr_stmt|;
comment|// test route for deleteGroupMembership
name|from
argument_list|(
literal|"direct://DELETEGROUPMEMBERSHIP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/deleteGroupMembership?inBody=groupMembershipId"
argument_list|)
expr_stmt|;
comment|// test route for getAllGroups
name|from
argument_list|(
literal|"direct://GETALLGROUPS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getAllGroups"
argument_list|)
expr_stmt|;
comment|// test route for getGroupInfo
name|from
argument_list|(
literal|"direct://GETGROUPINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getGroupInfo?inBody=groupId"
argument_list|)
expr_stmt|;
comment|// test route for getGroupMembershipInfo
name|from
argument_list|(
literal|"direct://GETGROUPMEMBERSHIPINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getGroupMembershipInfo?inBody=groupMembershipId"
argument_list|)
expr_stmt|;
comment|// test route for getGroupMemberships
name|from
argument_list|(
literal|"direct://GETGROUPMEMBERSHIPS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getGroupMemberships?inBody=groupId"
argument_list|)
expr_stmt|;
comment|// test route for updateGroupInfo
name|from
argument_list|(
literal|"direct://UPDATEGROUPINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/updateGroupInfo"
argument_list|)
expr_stmt|;
comment|// test route for updateGroupMembershipInfo
name|from
argument_list|(
literal|"direct://UPDATEGROUPMEMBERSHIPINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/updateGroupMembershipInfo"
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
name|createTestGroup
argument_list|()
expr_stmt|;
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
name|deleteTestGroup
argument_list|()
expr_stmt|;
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
literal|"/addGroupMembership"
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|getBoxConnection
argument_list|()
return|;
block|}
DECL|method|createTestGroup ()
specifier|private
name|void
name|createTestGroup
parameter_list|()
block|{
name|testGroup
operator|=
name|BoxGroup
operator|.
name|createGroup
argument_list|(
name|getConnection
argument_list|()
argument_list|,
name|CAMEL_TEST_GROUP_NAME
argument_list|)
operator|.
name|getResource
argument_list|()
expr_stmt|;
block|}
DECL|method|deleteTestGroup ()
specifier|private
name|void
name|deleteTestGroup
parameter_list|()
block|{
if|if
condition|(
name|testGroup
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|testGroup
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{             }
name|testGroup
operator|=
literal|null
expr_stmt|;
block|}
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

