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

begin_interface
DECL|interface|GitHubConstants
specifier|public
interface|interface
name|GitHubConstants
block|{
DECL|field|GITHUB_REPOSITORY_SERVICE
name|String
name|GITHUB_REPOSITORY_SERVICE
init|=
literal|"githubRepositoryService"
decl_stmt|;
DECL|field|GITHUB_COMMIT_SERVICE
name|String
name|GITHUB_COMMIT_SERVICE
init|=
literal|"githubCommitService"
decl_stmt|;
DECL|field|GITHUB_DATA_SERVICE
name|String
name|GITHUB_DATA_SERVICE
init|=
literal|"githubDataService"
decl_stmt|;
DECL|field|GITHUB_PULL_REQUEST_SERVICE
name|String
name|GITHUB_PULL_REQUEST_SERVICE
init|=
literal|"githubPullRequestService"
decl_stmt|;
DECL|field|GITHUB_ISSUE_SERVICE
name|String
name|GITHUB_ISSUE_SERVICE
init|=
literal|"githbIssueService"
decl_stmt|;
DECL|field|GITHUB_PULLREQUEST
name|String
name|GITHUB_PULLREQUEST
init|=
literal|"GitHubPullRequest"
decl_stmt|;
DECL|field|GITHUB_INRESPONSETO
name|String
name|GITHUB_INRESPONSETO
init|=
literal|"GitHubInResponseTo"
decl_stmt|;
DECL|field|GITHUB_PULLREQUEST_HEAD_COMMIT_SHA
name|String
name|GITHUB_PULLREQUEST_HEAD_COMMIT_SHA
init|=
literal|"GitHubPullRequestHeadCommitSHA"
decl_stmt|;
DECL|field|GITHUB_ISSUE_TITLE
name|String
name|GITHUB_ISSUE_TITLE
init|=
literal|"GitHubIssueTitle"
decl_stmt|;
block|}
end_interface

end_unit

