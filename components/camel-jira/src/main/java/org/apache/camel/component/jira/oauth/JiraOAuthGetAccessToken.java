begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jira.oauth
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
operator|.
name|oauth
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|auth
operator|.
name|oauth
operator|.
name|OAuthGetAccessToken
import|;
end_import

begin_class
DECL|class|JiraOAuthGetAccessToken
specifier|public
class|class
name|JiraOAuthGetAccessToken
extends|extends
name|OAuthGetAccessToken
block|{
DECL|method|JiraOAuthGetAccessToken (String authorizationServerUrl)
specifier|public
name|JiraOAuthGetAccessToken
parameter_list|(
name|String
name|authorizationServerUrl
parameter_list|)
block|{
name|super
argument_list|(
name|authorizationServerUrl
argument_list|)
expr_stmt|;
comment|// use POST http method
name|this
operator|.
name|usePost
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

