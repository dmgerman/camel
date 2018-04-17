begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box.api
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
operator|.
name|api
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
name|Collection
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
comment|/**  * Box Groups Manager  *   *<p>  * Provides operations to manage Box groups.  *   *   *  */
end_comment

begin_class
DECL|class|BoxGroupsManager
specifier|public
class|class
name|BoxGroupsManager
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
name|BoxGroupsManager
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Box connection to authenticated user account.      */
DECL|field|boxConnection
specifier|private
name|BoxAPIConnection
name|boxConnection
decl_stmt|;
comment|/**      * Create groups manager to manage the users of Box connection's      * authenticated user.      *       * @param boxConnection      *            - Box connection to authenticated user account.      */
DECL|method|BoxGroupsManager (BoxAPIConnection boxConnection)
specifier|public
name|BoxGroupsManager
parameter_list|(
name|BoxAPIConnection
name|boxConnection
parameter_list|)
block|{
name|this
operator|.
name|boxConnection
operator|=
name|boxConnection
expr_stmt|;
block|}
comment|/**      * Get all the groups in the enterprise.      *       * @return Collection containing all the enterprise's groups.      */
DECL|method|getAllGroups ()
specifier|public
name|Collection
argument_list|<
name|BoxGroup
argument_list|>
name|getAllGroups
parameter_list|()
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting all groups"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|BoxGroup
argument_list|>
name|groups
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|BoxGroup
operator|.
name|Info
name|groupInfo
range|:
name|BoxGroup
operator|.
name|getAllGroups
argument_list|(
name|boxConnection
argument_list|)
control|)
block|{
name|groups
operator|.
name|add
argument_list|(
name|groupInfo
operator|.
name|getResource
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|groups
return|;
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
block|}
comment|/**      * Create a new group with a specified name and optional additional parameters.      * Optional parameters may be null.      *       * @param name      *            - the name of the new group.      * @param provenance      *            - the provenance of the new group.      * @param externalSyncIdentifier      *            - the external_sync_identifier of the new group.      * @param description      *            - the description of the new group.      * @param invitabilityLevel      *            - the invitibility_level of the new group.      * @param memberViewabilityLevel      *            - the member_viewability_level of the new group.      * @return The newly created group.      */
DECL|method|createGroup (String name, String provenance, String externalSyncIdentifier, String description, String invitabilityLevel, String memberViewabilityLevel)
specifier|public
name|BoxGroup
name|createGroup
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|provenance
parameter_list|,
name|String
name|externalSyncIdentifier
parameter_list|,
name|String
name|description
parameter_list|,
name|String
name|invitabilityLevel
parameter_list|,
name|String
name|memberViewabilityLevel
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating group name="
operator|+
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'name' can not be null"
argument_list|)
throw|;
block|}
return|return
name|BoxGroup
operator|.
name|createGroup
argument_list|(
name|boxConnection
argument_list|,
name|name
argument_list|,
name|provenance
argument_list|,
name|externalSyncIdentifier
argument_list|,
name|description
argument_list|,
name|invitabilityLevel
argument_list|,
name|memberViewabilityLevel
argument_list|)
operator|.
name|getResource
argument_list|()
return|;
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
block|}
comment|/**      * Delete group.      *       * @param groupId      *            - the id of group to delete.      */
DECL|method|deleteGroup (String groupId)
specifier|public
name|void
name|deleteGroup
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleting group("
operator|+
name|groupId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupId' can not be null"
argument_list|)
throw|;
block|}
name|BoxGroup
name|group
init|=
operator|new
name|BoxGroup
argument_list|(
name|boxConnection
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
name|group
operator|.
name|delete
argument_list|()
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
block|}
comment|/**      * Get group information.      *       * @param groupId      *            - the id of group.      * @return The group information.      */
DECL|method|getGroupInfo (String groupId)
specifier|public
name|BoxGroup
operator|.
name|Info
name|getGroupInfo
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting info for group(id="
operator|+
name|groupId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupId' can not be null"
argument_list|)
throw|;
block|}
name|BoxGroup
name|group
init|=
operator|new
name|BoxGroup
argument_list|(
name|boxConnection
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
return|return
name|group
operator|.
name|getInfo
argument_list|()
return|;
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
block|}
comment|/**      * Update group information.      *      * @param groupId      *            - the id of group to update.      * @param groupInfo      *            - the updated information      * @return The updated group.      */
DECL|method|updateGroupInfo (String groupId, BoxGroup.Info groupInfo)
specifier|public
name|BoxGroup
name|updateGroupInfo
parameter_list|(
name|String
name|groupId
parameter_list|,
name|BoxGroup
operator|.
name|Info
name|groupInfo
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Updating info for group(id="
operator|+
name|groupId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupId' can not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|groupInfo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupInfo' can not be null"
argument_list|)
throw|;
block|}
name|BoxGroup
name|group
init|=
operator|new
name|BoxGroup
argument_list|(
name|boxConnection
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
name|group
operator|.
name|updateInfo
argument_list|(
name|groupInfo
argument_list|)
expr_stmt|;
return|return
name|group
return|;
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
block|}
comment|/**      * Get information about all of the group memberships for this group.      *       * @param groupId      *            - the id of group.      * @return The group information.      */
DECL|method|getGroupMemberships (String groupId)
specifier|public
name|Collection
argument_list|<
name|BoxGroupMembership
operator|.
name|Info
argument_list|>
name|getGroupMemberships
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting information about all memberships for group(id="
operator|+
name|groupId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupId' can not be null"
argument_list|)
throw|;
block|}
name|BoxGroup
name|group
init|=
operator|new
name|BoxGroup
argument_list|(
name|boxConnection
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
return|return
name|group
operator|.
name|getMemberships
argument_list|()
return|;
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
block|}
comment|/**      * Add a member to group with the specified role.      *       * @param groupId      *            - the id of group.      * @param userId      *            - the id of user to be added to group.      * @param role      *            - the role of the user in this group. Can be<code>null</code>      *            to assign the default role.      * @return The group information.      */
DECL|method|addGroupMembership (String groupId, String userId, BoxGroupMembership.Role role)
specifier|public
name|BoxGroupMembership
name|addGroupMembership
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|userId
parameter_list|,
name|BoxGroupMembership
operator|.
name|Role
name|role
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding user(id="
operator|+
name|userId
operator|+
literal|") as member to group(id="
operator|+
name|groupId
operator|+
operator|(
name|role
operator|==
literal|null
condition|?
literal|")"
else|:
literal|") with role="
operator|+
name|role
operator|.
name|name
argument_list|()
operator|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupId' can not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|userId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'userId' can not be null"
argument_list|)
throw|;
block|}
name|BoxGroup
name|group
init|=
operator|new
name|BoxGroup
argument_list|(
name|boxConnection
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
name|BoxUser
name|user
init|=
operator|new
name|BoxUser
argument_list|(
name|boxConnection
argument_list|,
name|userId
argument_list|)
decl_stmt|;
return|return
name|group
operator|.
name|addMembership
argument_list|(
name|user
argument_list|,
name|role
argument_list|)
operator|.
name|getResource
argument_list|()
return|;
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
block|}
comment|/**      * Delete group membership.      *       * @param groupMembershipId      *            - the id of group membership to delete.      */
DECL|method|deleteGroupMembership (String groupMembershipId)
specifier|public
name|void
name|deleteGroupMembership
parameter_list|(
name|String
name|groupMembershipId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleting groupMembership(id="
operator|+
name|groupMembershipId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupMembershipId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupMemebershipId' can not be null"
argument_list|)
throw|;
block|}
name|BoxGroupMembership
name|groupMembership
init|=
operator|new
name|BoxGroupMembership
argument_list|(
name|boxConnection
argument_list|,
name|groupMembershipId
argument_list|)
decl_stmt|;
name|groupMembership
operator|.
name|delete
argument_list|()
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
block|}
comment|/**      * Get group membership information.      *       * @param groupMemebershipId      *            - the id of group membership.      * @return The group information.      */
DECL|method|getGroupMembershipInfo (String groupMemebershipId)
specifier|public
name|BoxGroupMembership
operator|.
name|Info
name|getGroupMembershipInfo
parameter_list|(
name|String
name|groupMemebershipId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting info for groupMemebership(id="
operator|+
name|groupMemebershipId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupMemebershipId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupMemebershipId' can not be null"
argument_list|)
throw|;
block|}
name|BoxGroupMembership
name|group
init|=
operator|new
name|BoxGroupMembership
argument_list|(
name|boxConnection
argument_list|,
name|groupMemebershipId
argument_list|)
decl_stmt|;
return|return
name|group
operator|.
name|getInfo
argument_list|()
return|;
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
block|}
comment|/**      * Update group membership information.      *       * @param groupMembershipId      *            - the id of group membership to update.      * @param info      *            - the updated information.      * @return The group information.      */
DECL|method|updateGroupMembershipInfo (String groupMemebershipId, BoxGroupMembership.Info info)
specifier|public
name|BoxGroupMembership
name|updateGroupMembershipInfo
parameter_list|(
name|String
name|groupMemebershipId
parameter_list|,
name|BoxGroupMembership
operator|.
name|Info
name|info
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Updating info for groupMembership(id="
operator|+
name|groupMemebershipId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupMemebershipId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'groupMemebershipId' can not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'info' can not be null"
argument_list|)
throw|;
block|}
name|BoxGroupMembership
name|groupMembership
init|=
operator|new
name|BoxGroupMembership
argument_list|(
name|boxConnection
argument_list|,
name|groupMemebershipId
argument_list|)
decl_stmt|;
name|groupMembership
operator|.
name|updateInfo
argument_list|(
name|info
argument_list|)
expr_stmt|;
return|return
name|groupMembership
return|;
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
block|}
block|}
end_class

end_unit

