begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|Credentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|UsernamePasswordCredentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|auth
operator|.
name|AuthScope
import|;
end_import

begin_class
DECL|class|BasicAuthenticationHttpClientConfigurer
specifier|public
class|class
name|BasicAuthenticationHttpClientConfigurer
implements|implements
name|HttpClientConfigurer
block|{
DECL|field|proxy
specifier|private
specifier|final
name|boolean
name|proxy
decl_stmt|;
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
specifier|final
name|String
name|password
decl_stmt|;
DECL|method|BasicAuthenticationHttpClientConfigurer (boolean proxy, String user, String pwd)
specifier|public
name|BasicAuthenticationHttpClientConfigurer
parameter_list|(
name|boolean
name|proxy
parameter_list|,
name|String
name|user
parameter_list|,
name|String
name|pwd
parameter_list|)
block|{
name|this
operator|.
name|proxy
operator|=
name|proxy
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|pwd
expr_stmt|;
block|}
DECL|method|configureHttpClient (HttpClient client)
specifier|public
name|void
name|configureHttpClient
parameter_list|(
name|HttpClient
name|client
parameter_list|)
block|{
name|Credentials
name|credentials
init|=
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxy
condition|)
block|{
name|client
operator|.
name|getState
argument_list|()
operator|.
name|setProxyCredentials
argument_list|(
name|AuthScope
operator|.
name|ANY
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|client
operator|.
name|getState
argument_list|()
operator|.
name|setCredentials
argument_list|(
name|AuthScope
operator|.
name|ANY
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isProxy ()
specifier|public
name|boolean
name|isProxy
parameter_list|()
block|{
return|return
name|proxy
return|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
block|}
end_class

end_unit

