begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zendesk.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zendesk
operator|.
name|internal
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
name|component
operator|.
name|zendesk
operator|.
name|ZendeskConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|zendesk
operator|.
name|client
operator|.
name|v2
operator|.
name|Zendesk
import|;
end_import

begin_comment
comment|/**  * ZendeskHelper.  *   *<p>  * Utility class for creating Zendesk API Connections  */
end_comment

begin_class
DECL|class|ZendeskHelper
specifier|public
specifier|final
class|class
name|ZendeskHelper
block|{
DECL|method|ZendeskHelper ()
specifier|private
name|ZendeskHelper
parameter_list|()
block|{
comment|// hide utility class constructor
block|}
DECL|method|create (ZendeskConfiguration configuration)
specifier|public
specifier|static
name|Zendesk
name|create
parameter_list|(
name|ZendeskConfiguration
name|configuration
parameter_list|)
block|{
return|return
operator|new
name|Zendesk
operator|.
name|Builder
argument_list|(
name|configuration
operator|.
name|getServerUrl
argument_list|()
argument_list|)
operator|.
name|setUsername
argument_list|(
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|)
operator|.
name|setToken
argument_list|(
name|configuration
operator|.
name|getToken
argument_list|()
argument_list|)
operator|.
name|setOauthToken
argument_list|(
name|configuration
operator|.
name|getOauthToken
argument_list|()
argument_list|)
operator|.
name|setPassword
argument_list|(
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

