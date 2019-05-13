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

begin_enum
DECL|enum|JiraType
specifier|public
enum|enum
name|JiraType
block|{
comment|// add a new comment
DECL|enumConstant|ADDCOMMENT
name|ADDCOMMENT
block|,
comment|// add a new issue
DECL|enumConstant|ADDISSUE
name|ADDISSUE
block|,
comment|// add an attachment to an issue
DECL|enumConstant|ATTACH
name|ATTACH
block|,
comment|// delete an issue
DECL|enumConstant|DELETEISSUE
name|DELETEISSUE
block|,
comment|// retrieve recent issues
DECL|enumConstant|NEWISSUES
name|NEWISSUES
block|,
comment|// retrieve recent comments from any issues
DECL|enumConstant|NEWCOMMENTS
name|NEWCOMMENTS
block|,
comment|// update the fields of an issue
DECL|enumConstant|UPDATEISSUE
name|UPDATEISSUE
block|,
comment|// transition a status and resolution of an issue
DECL|enumConstant|TRANSITIONISSUE
name|TRANSITIONISSUE
block|,
comment|// add/remove watchers of an issue
DECL|enumConstant|WATCHERS
name|WATCHERS
block|}
end_enum

end_unit

