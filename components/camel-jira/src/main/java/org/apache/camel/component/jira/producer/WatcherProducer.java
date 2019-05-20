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
name|Issue
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
name|ISSUE_KEY
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
name|ISSUE_WATCHERS_ADD
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
name|ISSUE_WATCHERS_REMOVE
import|;
end_import

begin_class
DECL|class|WatcherProducer
specifier|public
class|class
name|WatcherProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|WatcherProducer (JiraEndpoint endpoint)
specifier|public
name|WatcherProducer
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
name|String
name|issueKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|watchersAdd
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
name|List
argument_list|<
name|String
argument_list|>
name|watchersRemove
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ISSUE_WATCHERS_REMOVE
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|issueKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Missing exchange input header named \'IssueKey\', it should specify the issue key to add/remove watchers to."
argument_list|)
throw|;
block|}
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
name|boolean
name|hasWatchersToAdd
init|=
name|watchersAdd
operator|!=
literal|null
operator|&&
name|watchersAdd
operator|.
name|size
argument_list|()
operator|>
literal|0
decl_stmt|;
name|boolean
name|hasWatchersToRemove
init|=
name|watchersRemove
operator|!=
literal|null
operator|&&
name|watchersRemove
operator|.
name|size
argument_list|()
operator|>
literal|0
decl_stmt|;
if|if
condition|(
name|hasWatchersToAdd
operator|||
name|hasWatchersToRemove
condition|)
block|{
name|IssueRestClient
name|issueClient
init|=
name|client
operator|.
name|getIssueClient
argument_list|()
decl_stmt|;
name|Issue
name|issue
init|=
name|issueClient
operator|.
name|getIssue
argument_list|(
name|issueKey
argument_list|)
operator|.
name|claim
argument_list|()
decl_stmt|;
if|if
condition|(
name|hasWatchersToAdd
condition|)
block|{
for|for
control|(
name|String
name|watcher
range|:
name|watchersAdd
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
if|if
condition|(
name|hasWatchersToRemove
condition|)
block|{
for|for
control|(
name|String
name|watcher
range|:
name|watchersRemove
control|)
block|{
name|issueClient
operator|.
name|removeWatcher
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
block|}
block|}
block|}
end_class

end_unit
