begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_enum
DECL|enum|GitHubType
specifier|public
enum|enum
name|GitHubType
block|{
DECL|enumConstant|CLOSEPULLREQUEST
DECL|enumConstant|PULLREQUESTCOMMENT
DECL|enumConstant|COMMIT
DECL|enumConstant|PULLREQUEST
DECL|enumConstant|TAG
DECL|enumConstant|PULLREQUESTSTATE
name|CLOSEPULLREQUEST
block|,
name|PULLREQUESTCOMMENT
block|,
name|COMMIT
block|,
name|PULLREQUEST
block|,
name|TAG
block|,
name|PULLREQUESTSTATE
block|,
DECL|enumConstant|PULLREQUESTFILES
DECL|enumConstant|GETCOMMITFILE
DECL|enumConstant|CREATEISSUE
name|PULLREQUESTFILES
block|,
name|GETCOMMITFILE
block|,
name|CREATEISSUE
block|}
end_enum

end_unit

