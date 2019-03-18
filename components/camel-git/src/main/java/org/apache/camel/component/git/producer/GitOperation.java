begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.git.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|git
operator|.
name|producer
package|;
end_package

begin_interface
DECL|interface|GitOperation
specifier|public
interface|interface
name|GitOperation
block|{
DECL|field|CLONE_OPERATION
name|String
name|CLONE_OPERATION
init|=
literal|"clone"
decl_stmt|;
DECL|field|CHECKOUT_OPERATION
name|String
name|CHECKOUT_OPERATION
init|=
literal|"checkout"
decl_stmt|;
DECL|field|INIT_OPERATION
name|String
name|INIT_OPERATION
init|=
literal|"init"
decl_stmt|;
DECL|field|ADD_OPERATION
name|String
name|ADD_OPERATION
init|=
literal|"add"
decl_stmt|;
DECL|field|REMOVE_OPERATION
name|String
name|REMOVE_OPERATION
init|=
literal|"remove"
decl_stmt|;
DECL|field|COMMIT_OPERATION
name|String
name|COMMIT_OPERATION
init|=
literal|"commit"
decl_stmt|;
DECL|field|COMMIT_ALL_OPERATION
name|String
name|COMMIT_ALL_OPERATION
init|=
literal|"commitAll"
decl_stmt|;
DECL|field|CREATE_BRANCH_OPERATION
name|String
name|CREATE_BRANCH_OPERATION
init|=
literal|"createBranch"
decl_stmt|;
DECL|field|DELETE_BRANCH_OPERATION
name|String
name|DELETE_BRANCH_OPERATION
init|=
literal|"deleteBranch"
decl_stmt|;
DECL|field|CREATE_TAG_OPERATION
name|String
name|CREATE_TAG_OPERATION
init|=
literal|"createTag"
decl_stmt|;
DECL|field|DELETE_TAG_OPERATION
name|String
name|DELETE_TAG_OPERATION
init|=
literal|"deleteTag"
decl_stmt|;
DECL|field|STATUS_OPERATION
name|String
name|STATUS_OPERATION
init|=
literal|"status"
decl_stmt|;
DECL|field|LOG_OPERATION
name|String
name|LOG_OPERATION
init|=
literal|"log"
decl_stmt|;
DECL|field|PUSH_OPERATION
name|String
name|PUSH_OPERATION
init|=
literal|"push"
decl_stmt|;
DECL|field|PUSH_TAG_OPERATION
name|String
name|PUSH_TAG_OPERATION
init|=
literal|"pushTag"
decl_stmt|;
DECL|field|PULL_OPERATION
name|String
name|PULL_OPERATION
init|=
literal|"pull"
decl_stmt|;
DECL|field|MERGE_OPERATION
name|String
name|MERGE_OPERATION
init|=
literal|"merge"
decl_stmt|;
DECL|field|SHOW_BRANCHES_OPERATION
name|String
name|SHOW_BRANCHES_OPERATION
init|=
literal|"showBranches"
decl_stmt|;
DECL|field|SHOW_TAGS_OPERATION
name|String
name|SHOW_TAGS_OPERATION
init|=
literal|"showTags"
decl_stmt|;
DECL|field|CHERRYPICK_OPERATION
name|String
name|CHERRYPICK_OPERATION
init|=
literal|"cherryPick"
decl_stmt|;
DECL|field|REMOTE_ADD_OPERATION
name|String
name|REMOTE_ADD_OPERATION
init|=
literal|"remoteAdd"
decl_stmt|;
DECL|field|REMOTE_LIST_OPERATION
name|String
name|REMOTE_LIST_OPERATION
init|=
literal|"remoteList"
decl_stmt|;
DECL|field|CLEAN_OPERATION
name|String
name|CLEAN_OPERATION
init|=
literal|"clean"
decl_stmt|;
DECL|field|GC_OPERATION
name|String
name|GC_OPERATION
init|=
literal|"gc"
decl_stmt|;
block|}
end_interface

end_unit

