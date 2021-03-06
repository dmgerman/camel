begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
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
name|Endpoint
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
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|CommitFile
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
DECL|class|PullRequestFilesProducerTest
specifier|public
class|class
name|PullRequestFilesProducerTest
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
name|PullRequestFilesProducerTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|latestPullRequestNumber
specifier|private
name|int
name|latestPullRequestNumber
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
literal|"direct:validPullRequest"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MockPullFilesProducerProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"github://pullRequestFiles?username=someguy&password=apassword&repoOwner=anotherguy&repoName=somerepo"
argument_list|)
expr_stmt|;
block|}
comment|// end of configure
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testPullRequestFilesProducer ()
specifier|public
name|void
name|testPullRequestFilesProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|PullRequest
name|pullRequest
init|=
name|pullRequestService
operator|.
name|addPullRequest
argument_list|(
literal|"testPullRequestFilesProducer"
argument_list|)
decl_stmt|;
name|latestPullRequestNumber
operator|=
name|pullRequest
operator|.
name|getNumber
argument_list|()
expr_stmt|;
name|CommitFile
name|file
init|=
operator|new
name|CommitFile
argument_list|()
decl_stmt|;
name|file
operator|.
name|setFilename
argument_list|(
literal|"testfile"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|CommitFile
argument_list|>
name|commitFiles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|commitFiles
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|pullRequestService
operator|.
name|setFiles
argument_list|(
name|latestPullRequestNumber
argument_list|,
name|commitFiles
argument_list|)
expr_stmt|;
name|Endpoint
name|filesProducerEndpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:validPullRequest"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|filesProducerEndpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Exchange
name|resp
init|=
name|template
operator|.
name|send
argument_list|(
name|filesProducerEndpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|resp
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|commitFiles
argument_list|)
expr_stmt|;
block|}
DECL|class|MockPullFilesProducerProcessor
specifier|public
class|class
name|MockPullFilesProducerProcessor
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|in
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|GitHubConstants
operator|.
name|GITHUB_PULLREQUEST
argument_list|,
name|latestPullRequestNumber
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

