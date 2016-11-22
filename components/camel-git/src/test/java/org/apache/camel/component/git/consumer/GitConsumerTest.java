begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.git.consumer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|git
operator|.
name|consumer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|git
operator|.
name|GitTestSupport
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|api
operator|.
name|Git
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|api
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectIdRef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Ref
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevCommit
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

begin_class
DECL|class|GitConsumerTest
specifier|public
class|class
name|GitConsumerTest
extends|extends
name|GitTestSupport
block|{
annotation|@
name|Test
DECL|method|commitConsumerTest ()
specifier|public
name|void
name|commitConsumerTest
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Init
name|MockEndpoint
name|mockResultCommit
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result-commit"
argument_list|)
decl_stmt|;
name|mockResultCommit
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Git
name|git
init|=
name|getGitTestRepository
argument_list|()
decl_stmt|;
name|File
name|gitDir
init|=
operator|new
name|File
argument_list|(
name|gitLocalRepo
argument_list|,
literal|".git"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|gitDir
operator|.
name|exists
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|File
name|fileToAdd
init|=
operator|new
name|File
argument_list|(
name|gitLocalRepo
argument_list|,
name|filenameToAdd
argument_list|)
decl_stmt|;
name|fileToAdd
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|git
operator|.
name|add
argument_list|()
operator|.
name|addFilepattern
argument_list|(
name|filenameToAdd
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|Status
name|status
init|=
name|git
operator|.
name|status
argument_list|()
operator|.
name|call
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|status
operator|.
name|getAdded
argument_list|()
operator|.
name|contains
argument_list|(
name|filenameToAdd
argument_list|)
argument_list|)
expr_stmt|;
name|git
operator|.
name|commit
argument_list|()
operator|.
name|setMessage
argument_list|(
name|commitMessage
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|File
name|fileToAdd1
init|=
operator|new
name|File
argument_list|(
name|gitLocalRepo
argument_list|,
name|filenameBranchToAdd
argument_list|)
decl_stmt|;
name|fileToAdd1
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|git
operator|.
name|add
argument_list|()
operator|.
name|addFilepattern
argument_list|(
name|filenameBranchToAdd
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|status
operator|=
name|git
operator|.
name|status
argument_list|()
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|status
operator|.
name|getAdded
argument_list|()
operator|.
name|contains
argument_list|(
name|filenameBranchToAdd
argument_list|)
argument_list|)
expr_stmt|;
name|git
operator|.
name|commit
argument_list|()
operator|.
name|setMessage
argument_list|(
literal|"Test test Commit"
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|Iterable
argument_list|<
name|RevCommit
argument_list|>
name|logs
init|=
name|git
operator|.
name|log
argument_list|()
operator|.
name|call
argument_list|()
decl_stmt|;
name|validateGitLogs
argument_list|(
name|git
argument_list|,
literal|"Test test Commit"
argument_list|,
name|commitMessage
argument_list|)
expr_stmt|;
comment|// Test
name|mockResultCommit
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// Check
name|Exchange
name|ex1
init|=
name|mockResultCommit
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Exchange
name|ex2
init|=
name|mockResultCommit
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|commitMessage
argument_list|,
name|ex2
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|RevCommit
operator|.
name|class
argument_list|)
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test test Commit"
argument_list|,
name|ex1
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|RevCommit
operator|.
name|class
argument_list|)
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|tagConsumerTest ()
specifier|public
name|void
name|tagConsumerTest
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Init
name|MockEndpoint
name|mockResultTag
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result-tag"
argument_list|)
decl_stmt|;
name|mockResultTag
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Git
name|git
init|=
name|getGitTestRepository
argument_list|()
decl_stmt|;
name|File
name|fileToAdd
init|=
operator|new
name|File
argument_list|(
name|gitLocalRepo
argument_list|,
name|filenameToAdd
argument_list|)
decl_stmt|;
name|fileToAdd
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|git
operator|.
name|add
argument_list|()
operator|.
name|addFilepattern
argument_list|(
name|filenameToAdd
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|File
name|gitDir
init|=
operator|new
name|File
argument_list|(
name|gitLocalRepo
argument_list|,
literal|".git"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|gitDir
operator|.
name|exists
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Status
name|status
init|=
name|git
operator|.
name|status
argument_list|()
operator|.
name|call
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|status
operator|.
name|getAdded
argument_list|()
operator|.
name|contains
argument_list|(
name|filenameToAdd
argument_list|)
argument_list|)
expr_stmt|;
name|git
operator|.
name|commit
argument_list|()
operator|.
name|setMessage
argument_list|(
name|commitMessage
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|git
operator|.
name|tag
argument_list|()
operator|.
name|setName
argument_list|(
name|tagTest
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Ref
argument_list|>
name|ref
init|=
name|git
operator|.
name|tagList
argument_list|()
operator|.
name|call
argument_list|()
decl_stmt|;
name|boolean
name|tagCreated
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Ref
name|refInternal
range|:
name|ref
control|)
block|{
if|if
condition|(
name|refInternal
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"refs/tags/"
operator|+
name|tagTest
argument_list|)
condition|)
block|{
name|tagCreated
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
name|tagCreated
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Test
name|mockResultTag
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// Check
name|Exchange
name|exchange
init|=
name|mockResultTag
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"refs/tags/"
operator|+
name|tagTest
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|ObjectIdRef
operator|.
name|Unpeeled
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|branchConsumerTest ()
specifier|public
name|void
name|branchConsumerTest
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Init
name|MockEndpoint
name|mockResultBranch
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result-branch"
argument_list|)
decl_stmt|;
name|mockResultBranch
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Git
name|git
init|=
name|getGitTestRepository
argument_list|()
decl_stmt|;
name|File
name|fileToAdd
init|=
operator|new
name|File
argument_list|(
name|gitLocalRepo
argument_list|,
name|filenameToAdd
argument_list|)
decl_stmt|;
name|fileToAdd
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|git
operator|.
name|add
argument_list|()
operator|.
name|addFilepattern
argument_list|(
name|filenameToAdd
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|File
name|gitDir
init|=
operator|new
name|File
argument_list|(
name|gitLocalRepo
argument_list|,
literal|".git"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|gitDir
operator|.
name|exists
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Status
name|status
init|=
name|git
operator|.
name|status
argument_list|()
operator|.
name|call
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|status
operator|.
name|getAdded
argument_list|()
operator|.
name|contains
argument_list|(
name|filenameToAdd
argument_list|)
argument_list|)
expr_stmt|;
name|git
operator|.
name|commit
argument_list|()
operator|.
name|setMessage
argument_list|(
name|commitMessage
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|git
operator|.
name|branchCreate
argument_list|()
operator|.
name|setName
argument_list|(
name|branchTest
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Ref
argument_list|>
name|ref
init|=
name|git
operator|.
name|branchList
argument_list|()
operator|.
name|call
argument_list|()
decl_stmt|;
name|boolean
name|branchCreated
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Ref
name|refInternal
range|:
name|ref
control|)
block|{
if|if
condition|(
name|refInternal
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"refs/heads/"
operator|+
name|branchTest
argument_list|)
condition|)
block|{
name|branchCreated
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
name|branchCreated
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Test
name|mockResultBranch
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// Check
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|mockResultBranch
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|ObjectIdRef
operator|.
name|Unpeeled
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/"
operator|+
name|branchTest
argument_list|,
name|exchanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|ObjectIdRef
operator|.
name|Unpeeled
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|from
argument_list|(
literal|"direct:clone"
argument_list|)
operator|.
name|to
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?remotePath=https://github.com/oscerd/json-webserver-example.git&operation=clone"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:init"
argument_list|)
operator|.
name|to
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?operation=init"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:add"
argument_list|)
operator|.
name|to
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?operation=add"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:commit"
argument_list|)
operator|.
name|to
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?operation=commit"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:create-branch"
argument_list|)
operator|.
name|to
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?operation=createBranch&branchName="
operator|+
name|branchTest
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:create-tag"
argument_list|)
operator|.
name|to
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?operation=createTag&tagName="
operator|+
name|tagTest
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?type=commit"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result-commit"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?type=tag"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result-tag"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"git://"
operator|+
name|gitLocalRepo
operator|+
literal|"?type=branch"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result-branch"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

