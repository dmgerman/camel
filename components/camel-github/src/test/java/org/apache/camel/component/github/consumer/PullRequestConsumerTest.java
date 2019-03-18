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
name|Message
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
name|builder
operator|.
name|RouteBuilder
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
name|GitHubComponent
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
name|GitHubComponentTestBase
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
name|User
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
DECL|class|PullRequestConsumerTest
specifier|public
class|class
name|PullRequestConsumerTest
extends|extends
name|GitHubComponentTestBase
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PullRequestConsumerTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addComponent
argument_list|(
literal|"github"
argument_list|,
operator|new
name|GitHubComponent
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"github://pullRequest?username=someguy&password=apassword&repoOwner=anotherguy&repoName=somerepo"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MockPullRequestProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|mockResultEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|pullRequestTest ()
specifier|public
name|void
name|pullRequestTest
parameter_list|()
throws|throws
name|Exception
block|{
name|PullRequest
name|pr1
init|=
name|pullRequestService
operator|.
name|addPullRequest
argument_list|(
literal|"First add"
argument_list|)
decl_stmt|;
name|PullRequest
name|pr2
init|=
name|pullRequestService
operator|.
name|addPullRequest
argument_list|(
literal|"Second"
argument_list|)
decl_stmt|;
name|mockResultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mockResultEndpoint
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
name|pr1
argument_list|,
name|pr2
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|mockResultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MockPullRequestProcessor
specifier|public
class|class
name|MockPullRequestProcessor
implements|implements
name|Processor
block|{
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
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|PullRequest
name|pullRequest
init|=
operator|(
name|PullRequest
operator|)
name|in
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|User
name|pullRequestUser
init|=
name|pullRequest
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|pullRequest
operator|.
name|getTitle
argument_list|()
expr_stmt|;
name|pullRequest
operator|.
name|getHtmlUrl
argument_list|()
expr_stmt|;
name|pullRequest
operator|.
name|getUser
argument_list|()
operator|.
name|getLogin
argument_list|()
expr_stmt|;
name|pullRequest
operator|.
name|getUser
argument_list|()
operator|.
name|getHtmlUrl
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Got PullRequest "
operator|+
name|pullRequest
operator|.
name|getHtmlUrl
argument_list|()
operator|+
literal|" ["
operator|+
name|pullRequest
operator|.
name|getTitle
argument_list|()
operator|+
literal|"] From "
operator|+
name|pullRequestUser
operator|.
name|getLogin
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

