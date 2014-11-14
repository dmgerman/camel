begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|calendar
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|Calendar
import|;
end_import

begin_interface
DECL|interface|GoogleCalendarClientFactory
specifier|public
interface|interface
name|GoogleCalendarClientFactory
block|{
DECL|method|makeClient (String clientId, String clientSecret, Collection<String> scopes, String applicationName, String refreshToken, String accessToken)
name|Calendar
name|makeClient
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|scopes
parameter_list|,
name|String
name|applicationName
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|String
name|accessToken
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

