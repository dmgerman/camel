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
name|List
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
DECL|class|PullRequestConsumer
specifier|public
class|class
name|PullRequestConsumer
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
name|PullRequestConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|pullRequestService
specifier|private
name|PullRequestService
name|pullRequestService
decl_stmt|;
DECL|field|lastOpenPullRequest
specifier|private
name|int
name|lastOpenPullRequest
decl_stmt|;
DECL|method|PullRequestConsumer (GitHubEndpoint endpoint, Processor processor)
specifier|public
name|PullRequestConsumer
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
name|LOG
operator|.
name|info
argument_list|(
literal|"GitHub PullRequestConsumer: Indexing current pull requests..."
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
if|if
condition|(
name|pullRequests
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|lastOpenPullRequest
operator|=
name|pullRequests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getNumber
argument_list|()
expr_stmt|;
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
name|List
argument_list|<
name|PullRequest
argument_list|>
name|openPullRequests
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
comment|// In the end, we want PRs oldest to newest.
name|Stack
argument_list|<
name|PullRequest
argument_list|>
name|newPullRequests
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
name|openPullRequests
control|)
block|{
if|if
condition|(
name|pullRequest
operator|.
name|getNumber
argument_list|()
operator|>
name|lastOpenPullRequest
condition|)
block|{
name|newPullRequests
operator|.
name|push
argument_list|(
name|pullRequest
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
if|if
condition|(
name|newPullRequests
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|lastOpenPullRequest
operator|=
name|openPullRequests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getNumber
argument_list|()
expr_stmt|;
block|}
while|while
condition|(
operator|!
name|newPullRequests
operator|.
name|empty
argument_list|()
condition|)
block|{
name|PullRequest
name|newPullRequest
init|=
name|newPullRequests
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
name|newPullRequest
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
name|newPullRequest
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|newPullRequest
operator|.
name|getHead
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GitHubConstants
operator|.
name|GITHUB_PULLREQUEST_HEAD_COMMIT_SHA
argument_list|,
name|newPullRequest
operator|.
name|getHead
argument_list|()
operator|.
name|getSha
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|newPullRequests
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

