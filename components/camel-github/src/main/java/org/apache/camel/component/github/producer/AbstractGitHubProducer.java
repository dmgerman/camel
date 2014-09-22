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
name|impl
operator|.
name|DefaultProducer
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
name|Repository
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
name|GitHubService
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
name|RepositoryService
import|;
end_import

begin_class
DECL|class|AbstractGitHubProducer
specifier|public
specifier|abstract
class|class
name|AbstractGitHubProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|GitHubEndpoint
name|endpoint
decl_stmt|;
DECL|field|repositoryService
specifier|private
name|RepositoryService
name|repositoryService
decl_stmt|;
DECL|field|repository
specifier|private
name|Repository
name|repository
decl_stmt|;
DECL|method|AbstractGitHubProducer (GitHubEndpoint endpoint)
specifier|public
name|AbstractGitHubProducer
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
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|repositoryService
operator|=
operator|new
name|RepositoryService
argument_list|()
expr_stmt|;
name|initService
argument_list|(
name|repositoryService
argument_list|)
expr_stmt|;
name|repository
operator|=
name|repositoryService
operator|.
name|getRepository
argument_list|(
name|endpoint
operator|.
name|getRepoOwner
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getRepoName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|initService (GitHubService service)
specifier|protected
name|void
name|initService
parameter_list|(
name|GitHubService
name|service
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|hasOauth
argument_list|()
condition|)
block|{
name|service
operator|.
name|getClient
argument_list|()
operator|.
name|setOAuth2Token
argument_list|(
name|endpoint
operator|.
name|getOauthToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|service
operator|.
name|getClient
argument_list|()
operator|.
name|setCredentials
argument_list|(
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getRepositoryService ()
specifier|protected
name|RepositoryService
name|getRepositoryService
parameter_list|()
block|{
return|return
name|repositoryService
return|;
block|}
DECL|method|getRepository ()
specifier|protected
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
specifier|abstract
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

