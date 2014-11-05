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
name|io
operator|.
name|IOException
import|;
end_import

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
name|IRepositoryIdProvider
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

begin_class
DECL|class|MockIssueService
specifier|public
class|class
name|MockIssueService
extends|extends
name|IssueService
block|{
DECL|field|comments
specifier|private
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
operator|new
name|ArrayList
argument_list|<
name|Comment
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|mockPullRequestService
specifier|private
name|MockPullRequestService
name|mockPullRequestService
decl_stmt|;
DECL|method|MockIssueService (MockPullRequestService mockPullRequestService)
specifier|public
name|MockIssueService
parameter_list|(
name|MockPullRequestService
name|mockPullRequestService
parameter_list|)
block|{
name|this
operator|.
name|mockPullRequestService
operator|=
name|mockPullRequestService
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComments (IRepositoryIdProvider repository, int issueNumber)
specifier|public
name|List
argument_list|<
name|Comment
argument_list|>
name|getComments
parameter_list|(
name|IRepositoryIdProvider
name|repository
parameter_list|,
name|int
name|issueNumber
parameter_list|)
block|{
return|return
name|comments
return|;
block|}
annotation|@
name|Override
DECL|method|createComment (IRepositoryIdProvider repository, int issueNumber, String commentText)
specifier|public
name|Comment
name|createComment
parameter_list|(
name|IRepositoryIdProvider
name|repository
parameter_list|,
name|int
name|issueNumber
parameter_list|,
name|String
name|commentText
parameter_list|)
throws|throws
name|IOException
block|{
name|Comment
name|addedComment
init|=
name|mockPullRequestService
operator|.
name|addComment
argument_list|(
operator|(
name|long
operator|)
name|issueNumber
argument_list|,
name|commentText
argument_list|)
decl_stmt|;
return|return
name|addedComment
return|;
block|}
block|}
end_class

end_unit

