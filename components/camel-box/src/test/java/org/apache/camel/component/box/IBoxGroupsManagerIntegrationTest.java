begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/*  * Camel Api Route test generated by camel-component-util-maven-plugin  * Generated on: Tue Jun 24 22:42:08 PDT 2014  */
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
name|boxjavalibv2
operator|.
name|dao
operator|.
name|BoxCollection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|dao
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
name|boxjavalibv2
operator|.
name|dao
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
name|boxjavalibv2
operator|.
name|requests
operator|.
name|requestobjects
operator|.
name|BoxGroupMembershipRequestObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|requests
operator|.
name|requestobjects
operator|.
name|BoxGroupRequestObject
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
name|IBoxGroupsManagerApiMethod
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
comment|/**  * Test class for com.box.boxjavalibv2.resourcemanagers.IBoxGroupsManager APIs.  */
end_comment

begin_class
DECL|class|IBoxGroupsManagerIntegrationTest
specifier|public
class|class
name|IBoxGroupsManagerIntegrationTest
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
name|IBoxGroupsManagerIntegrationTest
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
name|IBoxGroupsManagerApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|CAMEL_TEST_GROUP
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_GROUP
init|=
literal|"CamelTestGroup"
decl_stmt|;
DECL|method|createGroup ()
specifier|public
name|BoxGroup
name|createGroup
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using com.box.boxjavalibv2.requests.requestobjects.BoxGroupRequestObject message body for single parameter "groupRequest"
specifier|final
name|BoxGroupRequestObject
name|requestObject
init|=
name|BoxGroupRequestObject
operator|.
name|createGroupRequestObject
argument_list|(
name|CAMEL_TEST_GROUP
argument_list|)
decl_stmt|;
name|BoxGroup
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://CREATEGROUP"
argument_list|,
name|requestObject
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"createGroup result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Test
DECL|method|testCreateGroup_1 ()
specifier|public
name|void
name|testCreateGroup_1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "name"
name|BoxGroup
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://CREATEGROUP_1"
argument_list|,
name|CAMEL_TEST_GROUP
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"createGroup result"
argument_list|,
name|result
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
name|deleteGroup
argument_list|(
name|result
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createMembership (String groupId)
specifier|public
name|BoxGroupMembership
name|createMembership
parameter_list|(
name|String
name|groupId
parameter_list|)
throws|throws
name|Exception
block|{
comment|// using com.box.boxjavalibv2.requests.requestobjects.BoxGroupMembershipRequestObject message body for single parameter "groupMembershipRequest"
specifier|final
name|BoxGroupMembershipRequestObject
name|requestObject
init|=
name|BoxGroupMembershipRequestObject
operator|.
name|addMembershipRequestObject
argument_list|(
name|groupId
argument_list|,
name|testUserId
argument_list|,
name|BoxGroupMembership
operator|.
name|ROLE_MEMBER
argument_list|)
decl_stmt|;
name|BoxGroupMembership
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://CREATEMEMBERSHIP"
argument_list|,
name|requestObject
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"createMembership result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Test
DECL|method|testCreateMembership_1 ()
specifier|public
name|void
name|testCreateMembership_1
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxGroup
name|group
init|=
name|createGroup
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.groupId"
argument_list|,
name|group
operator|.
name|getId
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
name|testUserId
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.role"
argument_list|,
name|BoxGroupMembership
operator|.
name|ROLE_MEMBER
argument_list|)
expr_stmt|;
name|BoxGroupMembership
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://CREATEMEMBERSHIP_1"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"createMembership result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"createMembership: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deleteGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|deleteGroup (String groupId)
specifier|public
name|void
name|deleteGroup
parameter_list|(
name|String
name|groupId
parameter_list|)
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.restclientv2.requestsbase.BoxDefaultRequestObject
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.defaultRequest"
argument_list|,
name|BOX_DEFAULT_REQUEST_OBJECT
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://DELETEGROUP"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteMembership ()
specifier|public
name|void
name|testDeleteMembership
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxGroup
name|group
init|=
name|createGroup
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|BoxGroupMembership
name|membership
init|=
name|createMembership
argument_list|(
name|group
operator|.
name|getId
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.membershipId"
argument_list|,
name|membership
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.restclientv2.requestsbase.BoxDefaultRequestObject
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.defaultRequest"
argument_list|,
name|BOX_DEFAULT_REQUEST_OBJECT
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://DELETEMEMBERSHIP"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deleteGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testGetAllCollaborations ()
specifier|public
name|void
name|testGetAllCollaborations
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxGroup
name|group
init|=
name|createGroup
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.groupId"
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.restclientv2.requestsbase.BoxDefaultRequestObject
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.defaultRequest"
argument_list|,
name|BOX_DEFAULT_REQUEST_OBJECT
argument_list|)
expr_stmt|;
name|BoxCollection
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETALLCOLLABORATIONS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getAllCollaborations result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getAllCollaborations: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deleteGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|// using com.box.restclientv2.requestsbase.BoxDefaultRequestObject message body for single parameter "defaultRequest"
name|BoxCollection
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETALLGROUPS"
argument_list|,
name|BOX_DEFAULT_REQUEST_OBJECT
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
DECL|method|testGetMembership ()
specifier|public
name|void
name|testGetMembership
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxGroup
name|group
init|=
name|createGroup
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|BoxGroupMembership
name|membership
init|=
name|createMembership
argument_list|(
name|group
operator|.
name|getId
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.membershipId"
argument_list|,
name|membership
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.restclientv2.requestsbase.BoxDefaultRequestObject
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.defaultRequest"
argument_list|,
name|BOX_DEFAULT_REQUEST_OBJECT
argument_list|)
expr_stmt|;
name|BoxGroupMembership
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETMEMBERSHIP"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getMembership result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getMembership: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deleteGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testGetMemberships ()
specifier|public
name|void
name|testGetMemberships
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxGroup
name|group
init|=
name|createGroup
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.groupId"
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.restclientv2.requestsbase.BoxDefaultRequestObject
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.defaultRequest"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|BoxCollection
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETMEMBERSHIPS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getMemberships result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getMemberships: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deleteGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testUpdateGroup ()
specifier|public
name|void
name|testUpdateGroup
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxGroup
name|group
init|=
name|createGroup
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.groupId"
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.boxjavalibv2.requests.requestobjects.BoxGroupRequestObject
specifier|final
name|BoxGroupRequestObject
name|requestObject
init|=
name|BoxGroupRequestObject
operator|.
name|updateGroupRequestObject
argument_list|(
name|CAMEL_TEST_GROUP
operator|+
literal|"_Updated"
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.groupRequest"
argument_list|,
name|requestObject
argument_list|)
expr_stmt|;
name|BoxGroup
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATEGROUP"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"updateGroup result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"updateGroup: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deleteGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testUpdateMembership ()
specifier|public
name|void
name|testUpdateMembership
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxGroup
name|group
init|=
name|createGroup
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|BoxGroupMembership
name|membership
init|=
name|createMembership
argument_list|(
name|group
operator|.
name|getId
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.membershipId"
argument_list|,
name|membership
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.boxjavalibv2.requests.requestobjects.BoxGroupMembershipRequestObject
specifier|final
name|BoxGroupMembershipRequestObject
name|requestObject
init|=
name|BoxGroupMembershipRequestObject
operator|.
name|updateMembershipRequestObject
argument_list|(
name|BoxGroupMembership
operator|.
name|ROLE_ADMIN
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.groupMembershipRequest"
argument_list|,
name|requestObject
argument_list|)
expr_stmt|;
name|BoxGroupMembership
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATEMEMBERSHIP"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"updateMembership result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"updateMembership: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deleteGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testUpdateMembership_1 ()
specifier|public
name|void
name|testUpdateMembership_1
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxGroup
name|group
init|=
name|createGroup
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|BoxGroupMembership
name|membership
init|=
name|createMembership
argument_list|(
name|group
operator|.
name|getId
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.membershipId"
argument_list|,
name|membership
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.role"
argument_list|,
name|BoxGroupMembership
operator|.
name|ROLE_ADMIN
argument_list|)
expr_stmt|;
name|BoxGroupMembership
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATEMEMBERSHIP_1"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"updateMembership result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"updateMembership: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deleteGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"/createGroup?inBody=groupRequest"
argument_list|)
expr_stmt|;
comment|// test route for createGroup
name|from
argument_list|(
literal|"direct://CREATEGROUP_1"
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
comment|// test route for createMembership
name|from
argument_list|(
literal|"direct://CREATEMEMBERSHIP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/createMembership?inBody=groupMembershipRequest"
argument_list|)
expr_stmt|;
comment|// test route for createMembership
name|from
argument_list|(
literal|"direct://CREATEMEMBERSHIP_1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/createMembership"
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
literal|"/deleteGroup"
argument_list|)
expr_stmt|;
comment|// test route for deleteMembership
name|from
argument_list|(
literal|"direct://DELETEMEMBERSHIP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/deleteMembership"
argument_list|)
expr_stmt|;
comment|// test route for getAllCollaborations
name|from
argument_list|(
literal|"direct://GETALLCOLLABORATIONS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getAllCollaborations"
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
literal|"/getAllGroups?inBody=defaultRequest"
argument_list|)
expr_stmt|;
comment|// test route for getMembership
name|from
argument_list|(
literal|"direct://GETMEMBERSHIP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getMembership"
argument_list|)
expr_stmt|;
comment|// test route for getMemberships
name|from
argument_list|(
literal|"direct://GETMEMBERSHIPS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getMemberships"
argument_list|)
expr_stmt|;
comment|// test route for updateGroup
name|from
argument_list|(
literal|"direct://UPDATEGROUP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/updateGroup"
argument_list|)
expr_stmt|;
comment|// test route for updateMembership
name|from
argument_list|(
literal|"direct://UPDATEMEMBERSHIP"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/updateMembership"
argument_list|)
expr_stmt|;
comment|// test route for updateMembership
name|from
argument_list|(
literal|"direct://UPDATEMEMBERSHIP_1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/updateMembership"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

