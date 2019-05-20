begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jira
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
package|;
end_package

begin_interface
DECL|interface|JiraTestConstants
specifier|public
interface|interface
name|JiraTestConstants
block|{
DECL|field|KEY
name|String
name|KEY
init|=
literal|"TST"
decl_stmt|;
DECL|field|TEST_JIRA_URL
name|String
name|TEST_JIRA_URL
init|=
literal|"https://somerepo.atlassian.net"
decl_stmt|;
DECL|field|PROJECT
name|String
name|PROJECT
init|=
literal|"TST"
decl_stmt|;
DECL|field|USERNAME
name|String
name|USERNAME
init|=
literal|"someguy"
decl_stmt|;
DECL|field|PASSWORD
name|String
name|PASSWORD
init|=
literal|"my_password"
decl_stmt|;
DECL|field|JIRA_CREDENTIALS
name|String
name|JIRA_CREDENTIALS
init|=
name|TEST_JIRA_URL
operator|+
literal|"&username="
operator|+
name|USERNAME
operator|+
literal|"&password="
operator|+
name|PASSWORD
decl_stmt|;
block|}
end_interface

end_unit
