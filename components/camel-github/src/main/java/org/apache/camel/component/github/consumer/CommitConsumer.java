begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|RepositoryCommit
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

begin_class
DECL|class|CommitConsumer
specifier|public
class|class
name|CommitConsumer
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
name|CommitConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|commitService
specifier|private
name|CommitService
name|commitService
decl_stmt|;
DECL|field|commitHashes
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|commitHashes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|CommitConsumer (GitHubEndpoint endpoint, Processor processor, String branchName)
specifier|public
name|CommitConsumer
parameter_list|(
name|GitHubEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|branchName
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
name|GITHUB_COMMIT_SERVICE
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
name|LOG
operator|.
name|info
argument_list|(
literal|"GitHub CommitConsumer: Indexing current commits..."
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RepositoryCommit
argument_list|>
name|commits
init|=
name|commitService
operator|.
name|getCommits
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|branchName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|RepositoryCommit
name|commit
range|:
name|commits
control|)
block|{
name|commitHashes
operator|.
name|add
argument_list|(
name|commit
operator|.
name|getSha
argument_list|()
argument_list|)
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
name|RepositoryCommit
argument_list|>
name|commits
init|=
name|commitService
operator|.
name|getCommits
argument_list|(
name|getRepository
argument_list|()
argument_list|)
decl_stmt|;
comment|// In the end, we want tags oldest to newest.
name|Stack
argument_list|<
name|RepositoryCommit
argument_list|>
name|newCommits
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|RepositoryCommit
name|commit
range|:
name|commits
control|)
block|{
if|if
condition|(
operator|!
name|commitHashes
operator|.
name|contains
argument_list|(
name|commit
operator|.
name|getSha
argument_list|()
argument_list|)
condition|)
block|{
name|newCommits
operator|.
name|push
argument_list|(
name|commit
argument_list|)
expr_stmt|;
name|commitHashes
operator|.
name|add
argument_list|(
name|commit
operator|.
name|getSha
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
while|while
condition|(
operator|!
name|newCommits
operator|.
name|empty
argument_list|()
condition|)
block|{
name|RepositoryCommit
name|newCommit
init|=
name|newCommits
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
name|newCommit
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
name|newCommits
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

