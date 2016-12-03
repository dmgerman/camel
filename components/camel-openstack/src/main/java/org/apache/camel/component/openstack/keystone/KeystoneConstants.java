begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.keystone
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openstack
operator|.
name|keystone
package|;
end_package

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
name|openstack
operator|.
name|common
operator|.
name|OpenstackConstants
import|;
end_import

begin_class
DECL|class|KeystoneConstants
specifier|public
specifier|final
class|class
name|KeystoneConstants
extends|extends
name|OpenstackConstants
block|{
DECL|field|REGIONS
specifier|public
specifier|static
specifier|final
name|String
name|REGIONS
init|=
literal|"regions"
decl_stmt|;
DECL|field|DOMAINS
specifier|public
specifier|static
specifier|final
name|String
name|DOMAINS
init|=
literal|"domains"
decl_stmt|;
DECL|field|PROJECTS
specifier|public
specifier|static
specifier|final
name|String
name|PROJECTS
init|=
literal|"projects"
decl_stmt|;
DECL|field|USERS
specifier|public
specifier|static
specifier|final
name|String
name|USERS
init|=
literal|"users"
decl_stmt|;
DECL|field|GROUPS
specifier|public
specifier|static
specifier|final
name|String
name|GROUPS
init|=
literal|"groups"
decl_stmt|;
DECL|field|DESCRIPTION
specifier|public
specifier|static
specifier|final
name|String
name|DESCRIPTION
init|=
literal|"description"
decl_stmt|;
DECL|field|DOMAIN_ID
specifier|public
specifier|static
specifier|final
name|String
name|DOMAIN_ID
init|=
literal|"domainId"
decl_stmt|;
DECL|field|PARENT_ID
specifier|public
specifier|static
specifier|final
name|String
name|PARENT_ID
init|=
literal|"parentId"
decl_stmt|;
DECL|field|PASSWORD
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"password"
decl_stmt|;
DECL|field|EMAIL
specifier|public
specifier|static
specifier|final
name|String
name|EMAIL
init|=
literal|"email"
decl_stmt|;
DECL|field|USER_ID
specifier|public
specifier|static
specifier|final
name|String
name|USER_ID
init|=
literal|"userId"
decl_stmt|;
DECL|field|GROUP_ID
specifier|public
specifier|static
specifier|final
name|String
name|GROUP_ID
init|=
literal|"groupId"
decl_stmt|;
DECL|field|ADD_USER_TO_GROUP
specifier|public
specifier|static
specifier|final
name|String
name|ADD_USER_TO_GROUP
init|=
literal|"addUserToGroup"
decl_stmt|;
DECL|field|CHECK_GROUP_USER
specifier|public
specifier|static
specifier|final
name|String
name|CHECK_GROUP_USER
init|=
literal|"checkUserGroup"
decl_stmt|;
DECL|field|REMOVE_USER_FROM_GROUP
specifier|public
specifier|static
specifier|final
name|String
name|REMOVE_USER_FROM_GROUP
init|=
literal|"removeUserFromGroup"
decl_stmt|;
block|}
end_class

end_unit

