begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.github.producer
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
name|producer
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
name|CommitStatus
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
name|CommitService
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
comment|/**  * Producer endpoint that sets the commit status.  *  * The endpoint requires the "GitHubPullRequest" header, identifying the pull request number (integer),  * and the "GitHubPullRequestHeadCommitSHA" header, identifying the commit SHA on which the state will be recorded.  */
end_comment

begin_class
DECL|class|PullRequestStateProducer
specifier|public
class|class
name|PullRequestStateProducer
extends|extends
name|AbstractGitHubProducer
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
name|PullRequestStateProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|commitService
specifier|private
name|CommitService
name|commitService
decl_stmt|;
DECL|field|state
specifier|private
name|String
name|state
decl_stmt|;
DECL|field|targetUrl
specifier|private
name|String
name|targetUrl
decl_stmt|;
DECL|method|PullRequestStateProducer (GitHubEndpoint endpoint)
specifier|public
name|PullRequestStateProducer
parameter_list|(
name|GitHubEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
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
literal|"githubCommitService"
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
literal|"Using CommitService found in registry "
operator|+
name|service
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|commitService
operator|=
operator|(
name|CommitService
operator|)
name|service
expr_stmt|;
block|}
else|else
block|{
name|commitService
operator|=
operator|new
name|CommitService
argument_list|()
expr_stmt|;
block|}
name|initService
argument_list|(
name|commitService
argument_list|)
expr_stmt|;
name|state
operator|=
name|endpoint
operator|.
name|getState
argument_list|()
expr_stmt|;
name|targetUrl
operator|=
name|endpoint
operator|.
name|getTargetUrl
argument_list|()
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|pullRequestNumberSHA
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"GitHubPullRequestHeadCommitSHA"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|text
init|=
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
decl_stmt|;
name|CommitStatus
name|status
init|=
operator|new
name|CommitStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|status
operator|.
name|setState
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|targetUrl
operator|!=
literal|null
condition|)
block|{
name|status
operator|.
name|setTargetUrl
argument_list|(
name|targetUrl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
name|status
operator|.
name|setDescription
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
name|CommitStatus
name|response
init|=
name|commitService
operator|.
name|createStatus
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|pullRequestNumberSHA
argument_list|,
name|status
argument_list|)
decl_stmt|;
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
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

