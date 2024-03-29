begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive
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
name|drive
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
name|client
operator|.
name|http
operator|.
name|javanet
operator|.
name|NetHttpTransport
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
name|client
operator|.
name|json
operator|.
name|jackson2
operator|.
name|JacksonFactory
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
name|drive
operator|.
name|Drive
import|;
end_import

begin_class
DECL|class|MyClientFactory
specifier|public
class|class
name|MyClientFactory
implements|implements
name|GoogleDriveClientFactory
block|{
DECL|method|MyClientFactory ()
specifier|public
name|MyClientFactory
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|makeClient (String clientId, String clientSecret, Collection<String> scopes, String applicationName, String refreshToken, String accessToken)
specifier|public
name|Drive
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
block|{
return|return
operator|new
name|Drive
argument_list|(
operator|new
name|NetHttpTransport
argument_list|()
argument_list|,
operator|new
name|JacksonFactory
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

