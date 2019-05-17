begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.jira
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|jira
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
operator|.
name|JiraConstants
operator|.
name|ISSUE_PRIORITY_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
operator|.
name|JiraConstants
operator|.
name|ISSUE_PROJECT_KEY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
operator|.
name|JiraConstants
operator|.
name|ISSUE_SUMMARY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
operator|.
name|JiraConstants
operator|.
name|ISSUE_TYPE_NAME
import|;
end_import

begin_class
annotation|@
name|Component
DECL|class|AddIssueRoute
specifier|public
class|class
name|AddIssueRoute
extends|extends
name|RouteBuilder
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AddIssueRoute
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|">>>>>>>>>>>>>>>>>>>>> jira example - add new issue"
argument_list|)
expr_stmt|;
comment|// change the fields accordinly to your target jira server
name|from
argument_list|(
literal|"timer://foo?fixedRate=true&period=50000"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|ISSUE_PROJECT_KEY
argument_list|,
parameter_list|()
lambda|->
literal|"COM"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|ISSUE_TYPE_NAME
argument_list|,
parameter_list|()
lambda|->
literal|"Bug"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|ISSUE_SUMMARY
argument_list|,
parameter_list|()
lambda|->
literal|"Example Demo Bug jira "
operator|+
operator|(
operator|new
name|Date
argument_list|()
operator|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|ISSUE_PRIORITY_NAME
argument_list|,
parameter_list|()
lambda|->
literal|"Low"
argument_list|)
comment|// uncomment to add a component
comment|// .setHeader(ISSUE_COMPONENTS, () -> {
comment|//     List<String> comps = new ArrayList<>();
comment|//     comps.add("plugins");
comment|//     return comps;
comment|// })
operator|.
name|setBody
argument_list|(
parameter_list|()
lambda|->
literal|"A small description for a test issue. "
argument_list|)
operator|.
name|log
argument_list|(
literal|"  JIRA new issue: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jira://addIssue"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

