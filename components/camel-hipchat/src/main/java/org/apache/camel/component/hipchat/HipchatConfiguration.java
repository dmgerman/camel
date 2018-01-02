begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hipchat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hipchat
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriPath
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
name|CloseableHttpClient
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
name|HttpClients
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|HipchatConfiguration
specifier|public
class|class
name|HipchatConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
init|=
name|HipchatConstants
operator|.
name|DEFAULT_HOST
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|HipchatConstants
operator|.
name|DEFAULT_PORT
argument_list|)
DECL|field|port
specifier|private
name|Integer
name|port
init|=
name|HipchatConstants
operator|.
name|DEFAULT_PORT
decl_stmt|;
annotation|@
name|UriParam
DECL|field|authToken
specifier|private
name|String
name|authToken
decl_stmt|;
annotation|@
name|UriParam
DECL|field|consumeUsers
specifier|private
name|String
name|consumeUsers
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The CloseableHttpClient reference from registry to be used during API HTTP requests."
argument_list|,
name|defaultValue
operator|=
literal|"CloseableHttpClient default from HttpClient library"
argument_list|)
DECL|field|httpClient
specifier|private
name|CloseableHttpClient
name|httpClient
init|=
name|HttpClients
operator|.
name|createDefault
argument_list|()
decl_stmt|;
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * The host for the hipchat server, such as api.hipchat.com      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * The port for the hipchat server. Is by default 80.      */
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
comment|/**      * The protocol for the hipchat server, such as http.      */
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getAuthToken ()
specifier|public
name|String
name|getAuthToken
parameter_list|()
block|{
return|return
name|authToken
return|;
block|}
comment|/**      * OAuth 2 auth token      */
DECL|method|setAuthToken (String authToken)
specifier|public
name|void
name|setAuthToken
parameter_list|(
name|String
name|authToken
parameter_list|)
block|{
name|this
operator|.
name|authToken
operator|=
name|authToken
expr_stmt|;
block|}
DECL|method|getConsumeUsers ()
specifier|public
name|String
name|getConsumeUsers
parameter_list|()
block|{
return|return
name|consumeUsers
return|;
block|}
comment|/**      * Username(s) when consuming messages from the hiptchat server.      *<p/>      * Multiple user names can be separated by comma.      */
DECL|method|setConsumeUsers (String consumeUsers)
specifier|public
name|void
name|setConsumeUsers
parameter_list|(
name|String
name|consumeUsers
parameter_list|)
block|{
name|this
operator|.
name|consumeUsers
operator|=
name|consumeUsers
expr_stmt|;
block|}
DECL|method|hipChatUrl ()
specifier|public
name|String
name|hipChatUrl
parameter_list|()
block|{
return|return
name|getProtocol
argument_list|()
operator|+
literal|"://"
operator|+
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
return|;
block|}
DECL|method|consumableUsers ()
specifier|public
name|String
index|[]
name|consumableUsers
parameter_list|()
block|{
return|return
name|consumeUsers
operator|!=
literal|null
condition|?
name|consumeUsers
operator|.
name|split
argument_list|(
literal|","
argument_list|)
else|:
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
DECL|method|withAuthToken (String urlPath)
specifier|public
name|String
name|withAuthToken
parameter_list|(
name|String
name|urlPath
parameter_list|)
block|{
return|return
name|urlPath
operator|+
name|HipchatApiConstants
operator|.
name|AUTH_TOKEN_PREFIX
operator|+
name|getAuthToken
argument_list|()
return|;
block|}
DECL|method|getHttpClient ()
specifier|public
name|CloseableHttpClient
name|getHttpClient
parameter_list|()
block|{
return|return
name|httpClient
return|;
block|}
DECL|method|setHttpClient (CloseableHttpClient httpClient)
specifier|public
name|void
name|setHttpClient
parameter_list|(
name|CloseableHttpClient
name|httpClient
parameter_list|)
block|{
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
block|}
block|}
end_class

end_unit

