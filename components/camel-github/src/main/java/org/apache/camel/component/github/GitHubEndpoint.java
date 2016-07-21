begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.github
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
name|Consumer
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
name|Producer
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
name|consumer
operator|.
name|CommitConsumer
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
name|consumer
operator|.
name|PullRequestCommentConsumer
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
name|consumer
operator|.
name|PullRequestConsumer
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
name|consumer
operator|.
name|TagConsumer
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
name|producer
operator|.
name|ClosePullRequestProducer
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
name|producer
operator|.
name|CreateIssueProducer
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
name|producer
operator|.
name|GetCommitFileProducer
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
name|producer
operator|.
name|PullRequestCommentProducer
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
name|producer
operator|.
name|PullRequestFilesProducer
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
name|producer
operator|.
name|PullRequestStateProducer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The github component is used for integrating Camel with github.  *  * The endpoint encapsulates portions of the GitHub API, relying on the org.eclipse.egit.github.core Java SDK.  * Available endpoint URIs include:  *  * CONSUMERS  * github://pullRequest (new pull requests)  * github://pullRequestComment (new pull request comments)  * github://commit/[branch] (new commits)  * github://tag (new tags)  *  * PRODUCERS  * github://pullRequestComment (create a new pull request comment; see PullRequestCommentProducer for header requirements)  *  * The endpoints will respond with org.eclipse.egit.github.core-provided POJOs (PullRequest, CommitComment,  * RepositoryTag, RepositoryCommit, etc.)  *  * Note: Rather than webhooks, this endpoint relies on simple polling.  Reasons include:  * - concerned about reliability/stability if this somehow relied on an exposed, embedded server (Jetty?)  * - the types of payloads we're polling aren't typically large (plus, paging is available in the API)  * - need to support apps running somewhere not publicly accessible where a webhook would fail  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"github"
argument_list|,
name|title
operator|=
literal|"GitHub"
argument_list|,
name|syntax
operator|=
literal|"github:type/branchName"
argument_list|,
name|label
operator|=
literal|"api,file"
argument_list|)
DECL|class|GitHubEndpoint
specifier|public
class|class
name|GitHubEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|type
specifier|private
name|GitHubType
name|type
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|branchName
specifier|private
name|String
name|branchName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|oauthToken
specifier|private
name|String
name|oauthToken
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|repoOwner
specifier|private
name|String
name|repoOwner
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|repoName
specifier|private
name|String
name|repoName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"error,failure,pending,success"
argument_list|)
DECL|field|state
specifier|private
name|String
name|state
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|targetUrl
specifier|private
name|String
name|targetUrl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|method|GitHubEndpoint (String uri, GitHubComponent component)
specifier|public
name|GitHubEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GitHubComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|CLOSEPULLREQUEST
condition|)
block|{
return|return
operator|new
name|ClosePullRequestProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|PULLREQUESTCOMMENT
condition|)
block|{
return|return
operator|new
name|PullRequestCommentProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|PULLREQUESTSTATE
condition|)
block|{
return|return
operator|new
name|PullRequestStateProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|PULLREQUESTFILES
condition|)
block|{
return|return
operator|new
name|PullRequestFilesProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|GETCOMMITFILE
condition|)
block|{
return|return
operator|new
name|GetCommitFileProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|CREATEISSUE
condition|)
block|{
return|return
operator|new
name|CreateIssueProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create producer with type "
operator|+
name|type
argument_list|)
throw|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|COMMIT
condition|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|branchName
argument_list|,
literal|"branchName"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|CommitConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|branchName
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|PULLREQUEST
condition|)
block|{
return|return
operator|new
name|PullRequestConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|PULLREQUESTCOMMENT
condition|)
block|{
return|return
operator|new
name|PullRequestCommentConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitHubType
operator|.
name|TAG
condition|)
block|{
return|return
operator|new
name|TagConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create consumer with type "
operator|+
name|type
argument_list|)
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getType ()
specifier|public
name|GitHubType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * What git operation to execute      */
DECL|method|setType (GitHubType type)
specifier|public
name|void
name|setType
parameter_list|(
name|GitHubType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getBranchName ()
specifier|public
name|String
name|getBranchName
parameter_list|()
block|{
return|return
name|branchName
return|;
block|}
comment|/**      * Name of branch      */
DECL|method|setBranchName (String branchName)
specifier|public
name|void
name|setBranchName
parameter_list|(
name|String
name|branchName
parameter_list|)
block|{
name|this
operator|.
name|branchName
operator|=
name|branchName
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * GitHub username, required unless oauthToken is provided      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * GitHub password, required unless oauthToken is provided      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getOauthToken ()
specifier|public
name|String
name|getOauthToken
parameter_list|()
block|{
return|return
name|oauthToken
return|;
block|}
comment|/**      * GitHub OAuth token, required unless username& password are provided      */
DECL|method|setOauthToken (String oauthToken)
specifier|public
name|void
name|setOauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|this
operator|.
name|oauthToken
operator|=
name|oauthToken
expr_stmt|;
block|}
DECL|method|hasOauth ()
specifier|public
name|boolean
name|hasOauth
parameter_list|()
block|{
return|return
name|oauthToken
operator|!=
literal|null
operator|&&
name|oauthToken
operator|.
name|length
argument_list|()
operator|>
literal|0
return|;
block|}
DECL|method|getRepoOwner ()
specifier|public
name|String
name|getRepoOwner
parameter_list|()
block|{
return|return
name|repoOwner
return|;
block|}
comment|/**      * GitHub repository owner (organization)      */
DECL|method|setRepoOwner (String repoOwner)
specifier|public
name|void
name|setRepoOwner
parameter_list|(
name|String
name|repoOwner
parameter_list|)
block|{
name|this
operator|.
name|repoOwner
operator|=
name|repoOwner
expr_stmt|;
block|}
DECL|method|getRepoName ()
specifier|public
name|String
name|getRepoName
parameter_list|()
block|{
return|return
name|repoName
return|;
block|}
comment|/**      * GitHub repository name      */
DECL|method|setRepoName (String repoName)
specifier|public
name|void
name|setRepoName
parameter_list|(
name|String
name|repoName
parameter_list|)
block|{
name|this
operator|.
name|repoName
operator|=
name|repoName
expr_stmt|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
comment|/**      * To set git commit status state      */
DECL|method|setState (String state)
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|getTargetUrl ()
specifier|public
name|String
name|getTargetUrl
parameter_list|()
block|{
return|return
name|targetUrl
return|;
block|}
comment|/**      * To set git commit status target url      */
DECL|method|setTargetUrl (String targetUrl)
specifier|public
name|void
name|setTargetUrl
parameter_list|(
name|String
name|targetUrl
parameter_list|)
block|{
name|this
operator|.
name|targetUrl
operator|=
name|targetUrl
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * To use the given encoding when getting a git commit file      */
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
block|}
end_class

end_unit

