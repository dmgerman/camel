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
name|http
operator|.
name|auth
operator|.
name|AuthScope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
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
name|http
operator|.
name|auth
operator|.
name|NTCredentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
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
name|http
operator|.
name|client
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
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|DefaultHttpClient
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
DECL|field|domain
specifier|private
specifier|final
name|String
name|domain
decl_stmt|;
DECL|field|host
specifier|private
specifier|final
name|String
name|host
decl_stmt|;
DECL|method|BasicAuthenticationHttpClientConfigurer (String user, String pwd, String domain, String host)
specifier|public
name|BasicAuthenticationHttpClientConfigurer
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|pwd
parameter_list|,
name|String
name|domain
parameter_list|,
name|String
name|host
parameter_list|)
block|{
name|username
operator|=
name|user
expr_stmt|;
name|password
operator|=
name|pwd
expr_stmt|;
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|host
operator|=
name|host
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
name|defaultcreds
decl_stmt|;
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
name|defaultcreds
operator|=
operator|new
name|NTCredentials
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
name|host
argument_list|,
name|domain
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|defaultcreds
operator|=
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|DefaultHttpClient
operator|)
name|client
operator|)
operator|.
name|getCredentialsProvider
argument_list|()
operator|.
name|setCredentials
argument_list|(
name|AuthScope
operator|.
name|ANY
argument_list|,
name|defaultcreds
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

