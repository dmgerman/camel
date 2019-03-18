begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.github.consumer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|github
operator|.
name|consumer
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
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
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
name|Processor
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
name|github
operator|.
name|GitHubConstants
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
name|github
operator|.
name|GitHubEndpoint
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|Comment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|CommitComment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|PullRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|service
operator|.
name|IssueService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|service
operator|.
name|PullRequestService
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

begin_class
DECL|class|PullRequestCommentConsumer
specifier|public
class|class
name|PullRequestCommentConsumer
extends|extends
name|AbstractGitHubConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PullRequestCommentConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|pullRequestService
specifier|private
name|PullRequestService
name|pullRequestService
decl_stmt|;
DECL|field|issueService
specifier|private
name|IssueService
name|issueService
decl_stmt|;
DECL|field|commentIds
specifier|private
name|List
argument_list|<
name|Long
argument_list|>
name|commentIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|PullRequestCommentConsumer (GitHubEndpoint endpoint, Processor processor)
specifier|public
name|PullRequestCommentConsumer
parameter_list|(
name|GitHubEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|Registry
name|registry
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|Object
name|service
init|=
name|registry
operator|.
name|lookupByName
argument_list|(
name|GitHubConstants
operator|.
name|GITHUB_PULL_REQUEST_SERVICE
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using PullRequestService found in registry {}"
argument_list|,
name|service
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|pullRequestService
operator|=
operator|(
name|PullRequestService
operator|)
name|service
expr_stmt|;
block|}
else|else
block|{
name|pullRequestService
operator|=
operator|new
name|PullRequestService
argument_list|()
expr_stmt|;
block|}
name|initService
argument_list|(
name|pullRequestService
argument_list|)
expr_stmt|;
name|service
operator|=
name|registry
operator|.
name|lookupByName
argument_list|(
name|GitHubConstants
operator|.
name|GITHUB_ISSUE_SERVICE
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
name|issueService
operator|=
operator|(
name|IssueService
operator|)
name|service
expr_stmt|;
block|}
else|else
block|{
name|issueService
operator|=
operator|new
name|IssueService
argument_list|()
expr_stmt|;
block|}
name|initService
argument_list|(
name|issueService
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"GitHub PullRequestCommentConsumer: Indexing current pull request comments..."
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PullRequest
argument_list|>
name|pullRequests
init|=
name|pullRequestService
operator|.
name|getPullRequests
argument_list|(
name|getRepository
argument_list|()
argument_list|,
literal|"open"
argument_list|)
decl_stmt|;
for|for
control|(
name|PullRequest
name|pullRequest
range|:
name|pullRequests
control|)
block|{
name|List
argument_list|<
name|CommitComment
argument_list|>
name|commitComments
init|=
name|pullRequestService
operator|.
name|getComments
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|pullRequest
operator|.
name|getNumber
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Comment
name|comment
range|:
name|commitComments
control|)
block|{
name|commentIds
operator|.
name|add
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|issueService
operator|.
name|getComments
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|pullRequest
operator|.
name|getNumber
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Comment
name|comment
range|:
name|comments
control|)
block|{
name|commentIds
operator|.
name|add
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Do this here, rather than at the class level.  We only care about it for setting the Exchange header, so
comment|// there's no point growing memory over time.
name|Map
argument_list|<
name|Long
argument_list|,
name|PullRequest
argument_list|>
name|commentIdToPullRequest
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PullRequest
argument_list|>
name|pullRequests
init|=
name|pullRequestService
operator|.
name|getPullRequests
argument_list|(
name|getRepository
argument_list|()
argument_list|,
literal|"open"
argument_list|)
decl_stmt|;
comment|// In the end, we want comments oldest to newest.
name|Stack
argument_list|<
name|Comment
argument_list|>
name|newComments
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PullRequest
name|pullRequest
range|:
name|pullRequests
control|)
block|{
name|List
argument_list|<
name|CommitComment
argument_list|>
name|commitComments
init|=
name|pullRequestService
operator|.
name|getComments
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|pullRequest
operator|.
name|getNumber
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Comment
name|comment
range|:
name|commitComments
control|)
block|{
if|if
condition|(
operator|!
name|commentIds
operator|.
name|contains
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|newComments
operator|.
name|add
argument_list|(
name|comment
argument_list|)
expr_stmt|;
name|commentIds
operator|.
name|add
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|commentIdToPullRequest
operator|.
name|put
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|,
name|pullRequest
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|issueService
operator|.
name|getComments
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|pullRequest
operator|.
name|getNumber
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Comment
name|comment
range|:
name|comments
control|)
block|{
if|if
condition|(
operator|!
name|commentIds
operator|.
name|contains
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|newComments
operator|.
name|add
argument_list|(
name|comment
argument_list|)
expr_stmt|;
name|commentIds
operator|.
name|add
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|commentIdToPullRequest
operator|.
name|put
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|,
name|pullRequest
argument_list|)
expr_stmt|;
block|}
block|}
block|}
while|while
condition|(
operator|!
name|newComments
operator|.
name|empty
argument_list|()
condition|)
block|{
name|Comment
name|newComment
init|=
name|newComments
operator|.
name|pop
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|newComment
argument_list|)
expr_stmt|;
comment|// Required by the producers.  Set it here for convenience.
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GitHubConstants
operator|.
name|GITHUB_PULLREQUEST
argument_list|,
name|commentIdToPullRequest
operator|.
name|get
argument_list|(
name|newComment
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|newComments
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

