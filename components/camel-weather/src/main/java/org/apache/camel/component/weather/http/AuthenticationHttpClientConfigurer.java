begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.weather.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|weather
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
name|NTCredentials
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
DECL|class|AuthenticationHttpClientConfigurer
specifier|public
specifier|final
class|class
name|AuthenticationHttpClientConfigurer
implements|implements
name|HttpClientConfigurer
block|{
DECL|field|proxy
specifier|private
specifier|final
name|boolean
name|proxy
decl_stmt|;
DECL|field|credentials
specifier|private
specifier|final
name|Credentials
name|credentials
decl_stmt|;
DECL|method|AuthenticationHttpClientConfigurer (boolean proxy, Credentials credentials)
specifier|private
name|AuthenticationHttpClientConfigurer
parameter_list|(
name|boolean
name|proxy
parameter_list|,
name|Credentials
name|credentials
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
name|credentials
operator|=
name|credentials
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureHttpClient (HttpClient client)
specifier|public
name|HttpClient
name|configureHttpClient
parameter_list|(
name|HttpClient
name|client
parameter_list|)
block|{
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
name|this
operator|.
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
name|this
operator|.
name|credentials
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|basicAutenticationConfigurer (boolean proxy, String user, String pwd)
specifier|public
specifier|static
name|HttpClientConfigurer
name|basicAutenticationConfigurer
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
return|return
operator|new
name|AuthenticationHttpClientConfigurer
argument_list|(
name|proxy
argument_list|,
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|user
argument_list|,
name|pwd
argument_list|)
argument_list|)
return|;
block|}
DECL|method|ntlmAutenticationConfigurer (boolean proxy, String user, String pwd, String domain, String host)
specifier|public
specifier|static
name|HttpClientConfigurer
name|ntlmAutenticationConfigurer
parameter_list|(
name|boolean
name|proxy
parameter_list|,
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
return|return
operator|new
name|AuthenticationHttpClientConfigurer
argument_list|(
name|proxy
argument_list|,
operator|new
name|NTCredentials
argument_list|(
name|user
argument_list|,
name|pwd
argument_list|,
name|host
argument_list|,
name|domain
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

