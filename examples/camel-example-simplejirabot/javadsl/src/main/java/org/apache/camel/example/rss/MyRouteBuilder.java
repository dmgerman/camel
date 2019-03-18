begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.rss
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|rss
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * A simple example router demonstrating the camel-rss component.  */
end_comment

begin_class
DECL|class|MyRouteBuilder
specifier|public
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|String
name|rssURL
init|=
literal|"https://issues.apache.org/jira/sr/jira.issueviews:searchrequest-rss/temp/SearchRequest.xml"
operator|+
literal|"?pid=12311211&sorter/field=issuekey&sorter/order=DESC&tempMax=1000&delay=10s"
decl_stmt|;
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"rss:"
operator|+
name|rssURL
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|rss
argument_list|()
operator|.
name|setBody
argument_list|(
name|xpath
argument_list|(
literal|"/rss/channel/item/title/text()"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Jira: "
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:jirabot?showHeaders=false&showExchangePattern=false&showBodyType=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"irc:JiraBot@irc.freenode.net/#jirabottest"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
end_class

end_unit

