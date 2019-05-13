begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jira.producer
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
operator|.
name|producer
package|;
end_package

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
name|IssueRestClient
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
name|JiraRestClient
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
name|BasicIssue
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
name|Priority
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
name|input
operator|.
name|IssueInputBuilder
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
name|component
operator|.
name|jira
operator|.
name|JiraEndpoint
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
name|support
operator|.
name|DefaultProducer
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
name|JiraConstants
operator|.
name|*
import|;
end_import

begin_class
DECL|class|AddIssueProducer
specifier|public
class|class
name|AddIssueProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|AddIssueProducer (JiraEndpoint endpoint)
specifier|public
name|AddIssueProducer
parameter_list|(
name|JiraEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|JiraRestClient
name|client
init|=
operator|(
operator|(
name|JiraEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getClient
argument_list|()
decl_stmt|;
comment|// required fields
name|String
name|projectKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_PROJECT_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Long
name|issueTypeId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_TYPE_ID
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|issueTypeName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_TYPE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|summary
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_SUMMARY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// optional fields
name|String
name|assigneeName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_ASSIGNEE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|priorityName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_PRIORITY_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Long
name|priorityId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_PRIORITY_ID
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|components
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_COMPONENTS
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|watchers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_WATCHERS_ADD
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// search for issueTypeId from an issueTypeName
if|if
condition|(
name|issueTypeId
operator|==
literal|null
operator|&&
name|issueTypeName
operator|!=
literal|null
condition|)
block|{
name|Iterable
argument_list|<
name|IssueType
argument_list|>
name|issueTypes
init|=
name|client
operator|.
name|getMetadataClient
argument_list|()
operator|.
name|getIssueTypes
argument_list|()
operator|.
name|claim
argument_list|()
decl_stmt|;
for|for
control|(
name|IssueType
name|type
range|:
name|issueTypes
control|)
block|{
if|if
condition|(
name|issueTypeName
operator|.
name|equals
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|issueTypeId
operator|=
name|type
operator|.
name|getId
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
comment|// search for priorityId from an priorityName
if|if
condition|(
name|priorityId
operator|==
literal|null
operator|&&
name|priorityName
operator|!=
literal|null
condition|)
block|{
name|Iterable
argument_list|<
name|Priority
argument_list|>
name|priorities
init|=
name|client
operator|.
name|getMetadataClient
argument_list|()
operator|.
name|getPriorities
argument_list|()
operator|.
name|claim
argument_list|()
decl_stmt|;
for|for
control|(
name|Priority
name|pri
range|:
name|priorities
control|)
block|{
if|if
condition|(
name|priorityName
operator|.
name|equals
argument_list|(
name|pri
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|priorityId
operator|=
name|pri
operator|.
name|getId
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|projectKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A valid project key is required."
argument_list|)
throw|;
block|}
if|if
condition|(
name|issueTypeId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A valid issue type id is required, actual: id("
operator|+
name|issueTypeId
operator|+
literal|"), name("
operator|+
name|issueTypeName
operator|+
literal|")"
argument_list|)
throw|;
block|}
if|if
condition|(
name|summary
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A summary field is required, actual value: "
operator|+
name|summary
argument_list|)
throw|;
block|}
name|IssueInputBuilder
name|builder
init|=
operator|new
name|IssueInputBuilder
argument_list|(
name|projectKey
argument_list|,
name|issueTypeId
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDescription
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setSummary
argument_list|(
name|summary
argument_list|)
expr_stmt|;
if|if
condition|(
name|components
operator|!=
literal|null
operator|&&
name|components
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|setComponentsNames
argument_list|(
name|components
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|priorityId
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setPriorityId
argument_list|(
name|priorityId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|assigneeName
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setAssigneeName
argument_list|(
name|assigneeName
argument_list|)
expr_stmt|;
block|}
name|IssueRestClient
name|issueClient
init|=
name|client
operator|.
name|getIssueClient
argument_list|()
decl_stmt|;
name|BasicIssue
name|issueCreated
init|=
name|issueClient
operator|.
name|createIssue
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|claim
argument_list|()
decl_stmt|;
name|Issue
name|issue
init|=
name|issueClient
operator|.
name|getIssue
argument_list|(
name|issueCreated
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|claim
argument_list|()
decl_stmt|;
if|if
condition|(
name|watchers
operator|!=
literal|null
operator|&&
name|watchers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|watcher
range|:
name|watchers
control|)
block|{
name|issueClient
operator|.
name|addWatcher
argument_list|(
name|issue
operator|.
name|getWatchers
argument_list|()
operator|.
name|getSelf
argument_list|()
argument_list|,
name|watcher
argument_list|)
expr_stmt|;
block|}
block|}
comment|// support InOut
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
comment|// copy the header of in message to the out message
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|issue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|issue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

