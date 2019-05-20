begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jira
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Collection
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|Attachment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|BasicComponent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|BasicPriority
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|BasicWatchers
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|Comment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|Issue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|IssueType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|Resolution
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|User
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|User
operator|.
name|S48_48
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
operator|.
name|JiraTestConstants
operator|.
name|KEY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
operator|.
name|JiraTestConstants
operator|.
name|TEST_JIRA_URL
import|;
end_import

begin_class
DECL|class|Utils
specifier|public
specifier|final
class|class
name|Utils
block|{
DECL|field|userAssignee
specifier|public
specifier|static
name|User
name|userAssignee
decl_stmt|;
static|static
block|{
try|try
block|{
name|userAssignee
operator|=
operator|new
name|User
argument_list|(
literal|null
argument_list|,
literal|"user-test"
argument_list|,
literal|"User Test"
argument_list|,
literal|"user@test"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
name|buildUserAvatarUris
argument_list|(
literal|"user-test"
argument_list|,
literal|10082L
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
DECL|field|issueType
specifier|private
specifier|static
name|IssueType
name|issueType
init|=
operator|new
name|IssueType
argument_list|(
literal|null
argument_list|,
literal|1L
argument_list|,
literal|"Bug"
argument_list|,
literal|false
argument_list|,
literal|"Bug"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
DECL|method|Utils ()
specifier|private
name|Utils
parameter_list|()
block|{     }
DECL|method|createIssue (long id)
specifier|public
specifier|static
name|Issue
name|createIssue
parameter_list|(
name|long
name|id
parameter_list|)
block|{
return|return
name|createIssueWithComments
argument_list|(
name|id
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|createIssue (long id, String summary, String key, IssueType issueType, String description, BasicPriority priority, User assignee, Collection<BasicComponent> components, BasicWatchers watchers)
specifier|public
specifier|static
name|Issue
name|createIssue
parameter_list|(
name|long
name|id
parameter_list|,
name|String
name|summary
parameter_list|,
name|String
name|key
parameter_list|,
name|IssueType
name|issueType
parameter_list|,
name|String
name|description
parameter_list|,
name|BasicPriority
name|priority
parameter_list|,
name|User
name|assignee
parameter_list|,
name|Collection
argument_list|<
name|BasicComponent
argument_list|>
name|components
parameter_list|,
name|BasicWatchers
name|watchers
parameter_list|)
block|{
name|URI
name|selfUri
init|=
name|URI
operator|.
name|create
argument_list|(
name|TEST_JIRA_URL
operator|+
literal|"/rest/api/latest/issue/"
operator|+
name|id
argument_list|)
decl_stmt|;
return|return
operator|new
name|Issue
argument_list|(
name|summary
argument_list|,
name|selfUri
argument_list|,
name|KEY
operator|+
literal|"-"
operator|+
name|id
argument_list|,
name|id
argument_list|,
literal|null
argument_list|,
name|issueType
argument_list|,
literal|null
argument_list|,
name|description
argument_list|,
name|priority
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|assignee
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|components
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|watchers
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|transitionIssueDone (Issue issue, Status status, Resolution resolution)
specifier|public
specifier|static
name|Issue
name|transitionIssueDone
parameter_list|(
name|Issue
name|issue
parameter_list|,
name|Status
name|status
parameter_list|,
name|Resolution
name|resolution
parameter_list|)
block|{
return|return
operator|new
name|Issue
argument_list|(
name|issue
operator|.
name|getSummary
argument_list|()
argument_list|,
name|issue
operator|.
name|getSelf
argument_list|()
argument_list|,
name|issue
operator|.
name|getKey
argument_list|()
argument_list|,
name|issue
operator|.
name|getId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|issue
operator|.
name|getIssueType
argument_list|()
argument_list|,
name|status
argument_list|,
name|issue
operator|.
name|getDescription
argument_list|()
argument_list|,
name|issue
operator|.
name|getPriority
argument_list|()
argument_list|,
name|resolution
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|issue
operator|.
name|getAssignee
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createIssueWithAttachment (long id, String summary, String key, IssueType issueType, String description, BasicPriority priority, User assignee, Collection<Attachment> attachments)
specifier|public
specifier|static
name|Issue
name|createIssueWithAttachment
parameter_list|(
name|long
name|id
parameter_list|,
name|String
name|summary
parameter_list|,
name|String
name|key
parameter_list|,
name|IssueType
name|issueType
parameter_list|,
name|String
name|description
parameter_list|,
name|BasicPriority
name|priority
parameter_list|,
name|User
name|assignee
parameter_list|,
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
name|URI
name|selfUri
init|=
name|URI
operator|.
name|create
argument_list|(
name|TEST_JIRA_URL
operator|+
literal|"/rest/api/latest/issue/"
operator|+
name|id
argument_list|)
decl_stmt|;
return|return
operator|new
name|Issue
argument_list|(
name|summary
argument_list|,
name|selfUri
argument_list|,
name|KEY
operator|+
literal|"-"
operator|+
name|id
argument_list|,
name|id
argument_list|,
literal|null
argument_list|,
name|issueType
argument_list|,
literal|null
argument_list|,
name|description
argument_list|,
name|priority
argument_list|,
literal|null
argument_list|,
name|attachments
argument_list|,
literal|null
argument_list|,
name|assignee
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createIssueWithComments (long id, int numComments)
specifier|public
specifier|static
name|Issue
name|createIssueWithComments
parameter_list|(
name|long
name|id
parameter_list|,
name|int
name|numComments
parameter_list|)
block|{
name|Collection
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|numComments
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|idx
init|=
literal|1
init|;
name|idx
operator|<
name|numComments
operator|+
literal|1
condition|;
name|idx
operator|++
control|)
block|{
name|Comment
name|c
init|=
name|newComment
argument_list|(
name|id
argument_list|,
name|idx
argument_list|,
literal|"A test comment "
operator|+
name|idx
operator|+
literal|" for "
operator|+
name|KEY
operator|+
literal|"-"
operator|+
name|id
argument_list|)
decl_stmt|;
name|comments
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|createIssueWithComments
argument_list|(
name|id
argument_list|,
name|comments
argument_list|)
return|;
block|}
DECL|method|createIssueWithComments (long id, Collection<Comment> comments)
specifier|public
specifier|static
name|Issue
name|createIssueWithComments
parameter_list|(
name|long
name|id
parameter_list|,
name|Collection
argument_list|<
name|Comment
argument_list|>
name|comments
parameter_list|)
block|{
name|URI
name|selfUri
init|=
name|URI
operator|.
name|create
argument_list|(
name|TEST_JIRA_URL
operator|+
literal|"/rest/api/latest/issue/"
operator|+
name|id
argument_list|)
decl_stmt|;
return|return
operator|new
name|Issue
argument_list|(
literal|"jira summary test "
operator|+
name|id
argument_list|,
name|selfUri
argument_list|,
name|KEY
operator|+
literal|"-"
operator|+
name|id
argument_list|,
name|id
argument_list|,
literal|null
argument_list|,
name|issueType
argument_list|,
literal|null
argument_list|,
literal|"Description "
operator|+
name|id
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|userAssignee
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|comments
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|newComment (long issueId, int newCommentId, String comment)
specifier|public
specifier|static
name|Comment
name|newComment
parameter_list|(
name|long
name|issueId
parameter_list|,
name|int
name|newCommentId
parameter_list|,
name|String
name|comment
parameter_list|)
block|{
name|DateTime
name|now
init|=
name|DateTime
operator|.
name|now
argument_list|()
decl_stmt|;
name|Long
name|id
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|issueId
operator|+
literal|"0"
operator|+
name|newCommentId
argument_list|)
decl_stmt|;
name|URI
name|selfUri
init|=
name|URI
operator|.
name|create
argument_list|(
name|TEST_JIRA_URL
operator|+
literal|"/rest/api/latest/issue/"
operator|+
name|issueId
operator|+
literal|"/comment"
argument_list|)
decl_stmt|;
return|return
operator|new
name|Comment
argument_list|(
name|selfUri
argument_list|,
name|comment
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|now
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|buildUserAvatarUris (@ullable String user, Long avatarId)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|URI
argument_list|>
name|buildUserAvatarUris
parameter_list|(
annotation|@
name|Nullable
name|String
name|user
parameter_list|,
name|Long
name|avatarId
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|URI
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|S48_48
argument_list|,
name|buildUserAvatarUri
argument_list|(
name|user
argument_list|,
name|avatarId
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|buildUserAvatarUri (@ullable String userName, Long avatarId)
specifier|private
specifier|static
name|URI
name|buildUserAvatarUri
parameter_list|(
annotation|@
name|Nullable
name|String
name|userName
parameter_list|,
name|Long
name|avatarId
parameter_list|)
throws|throws
name|Exception
block|{
comment|// secure/useravatar?size=small&ownerId=admin&avatarId=10054
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"secure/useravatar?"
argument_list|)
decl_stmt|;
comment|// Optional user name
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|userName
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"ownerId="
argument_list|)
operator|.
name|append
argument_list|(
name|userName
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
block|}
comment|// avatar Id
name|sb
operator|.
name|append
argument_list|(
literal|"avatarId="
argument_list|)
operator|.
name|append
argument_list|(
name|avatarId
argument_list|)
expr_stmt|;
name|String
name|relativeAvatarUrl
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|URI
name|avatarUrl
init|=
operator|new
name|URL
argument_list|(
name|TEST_JIRA_URL
operator|+
literal|"/"
operator|+
name|relativeAvatarUrl
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
return|return
name|avatarUrl
return|;
block|}
block|}
end_class

end_unit
