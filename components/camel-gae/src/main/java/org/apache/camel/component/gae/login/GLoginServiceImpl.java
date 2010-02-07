begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.login
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|login
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLEncoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|client
operator|.
name|GoogleAuthTokenFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|util
operator|.
name|AuthenticationException
import|;
end_import

begin_comment
comment|/**  * Implements the interactions with Google's authentication and authorization  * services. If the endpoint is configured to run in development mode the  * authentication and authorization services of the development server are used.  */
end_comment

begin_class
DECL|class|GLoginServiceImpl
specifier|public
class|class
name|GLoginServiceImpl
implements|implements
name|GLoginService
block|{
comment|/**      * Authenticates a user and stores the authentication token to      * {@link GLoginData#setAuthenticationToken(String)}. If the endpoint is      * configured to run in development mode this method simply returns without      * any further action.       */
DECL|method|authenticate (GLoginData data)
specifier|public
name|void
name|authenticate
parameter_list|(
name|GLoginData
name|data
parameter_list|)
throws|throws
name|AuthenticationException
block|{
if|if
condition|(
name|data
operator|.
name|isDevMode
argument_list|()
condition|)
block|{
return|return;
block|}
name|GoogleAuthTokenFactory
name|factory
init|=
operator|new
name|GoogleAuthTokenFactory
argument_list|(
literal|"ah"
argument_list|,
name|data
operator|.
name|getClientName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|token
init|=
name|factory
operator|.
name|getAuthToken
argument_list|(
name|data
operator|.
name|getUserName
argument_list|()
argument_list|,
name|data
operator|.
name|getPassword
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"ah"
argument_list|,
name|data
operator|.
name|getClientName
argument_list|()
argument_list|)
decl_stmt|;
name|data
operator|.
name|setAuthenticationToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
comment|/**      * Dispatches authorization to {@link #authorizeDev(GLoginData)} if the      * endpoint is configured to run in development mode, otherwise to      * {@link #authorizeStd(GLoginData)}.      */
DECL|method|authorize (GLoginData data)
specifier|public
name|void
name|authorize
parameter_list|(
name|GLoginData
name|data
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|data
operator|.
name|isDevMode
argument_list|()
condition|)
block|{
name|authorizeDev
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|authorizeStd
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Authorizes access to a development server and stores the resulting      * authorization cookie to {@link GLoginData#setAuthorizationCookie(String)}      * . Authorization in development mode doesn't require an authentication      * token.      */
DECL|method|authorizeDev (GLoginData data)
specifier|protected
name|void
name|authorizeDev
parameter_list|(
name|GLoginData
name|data
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|homeLocation
init|=
name|String
operator|.
name|format
argument_list|(
literal|"http://%s:%d"
argument_list|,
name|data
operator|.
name|getHostName
argument_list|()
argument_list|,
name|data
operator|.
name|getDevPort
argument_list|()
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|connection
init|=
name|createURLConnection
argument_list|(
name|homeLocation
operator|+
literal|"/_ah/login"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|connection
operator|.
name|getOutputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"email=%s&isAdmin=%s&continue=%s"
argument_list|,
name|URLEncoder
operator|.
name|encode
argument_list|(
name|data
operator|.
name|getUserName
argument_list|()
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|data
operator|.
name|isDevAdmin
argument_list|()
condition|?
literal|"on"
else|:
literal|"off"
argument_list|,
name|URLEncoder
operator|.
name|encode
argument_list|(
name|homeLocation
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|data
operator|.
name|setAuthorizationCookie
argument_list|(
name|connection
operator|.
name|getHeaderField
argument_list|(
literal|"Set-Cookie"
argument_list|)
argument_list|)
expr_stmt|;
name|connection
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
comment|/**      * Authorizes access to a Google App Engine application and stores the      * resulting authorization cookie to      * {@link GLoginData#setAuthorizationCookie(String)}. This method requires      * an authentication token from      * {@link GLoginData#getAuthenticationToken()}.      */
DECL|method|authorizeStd (GLoginData data)
specifier|protected
name|void
name|authorizeStd
parameter_list|(
name|GLoginData
name|data
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|url
init|=
name|String
operator|.
name|format
argument_list|(
literal|"https://%s/_ah/login?auth=%s"
argument_list|,
name|data
operator|.
name|getHostName
argument_list|()
argument_list|,
name|data
operator|.
name|getAuthenticationToken
argument_list|()
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|connection
init|=
name|createURLConnection
argument_list|(
name|url
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|data
operator|.
name|setAuthorizationCookie
argument_list|(
name|connection
operator|.
name|getHeaderField
argument_list|(
literal|"Set-Cookie"
argument_list|)
argument_list|)
expr_stmt|;
name|connection
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
DECL|method|createURLConnection (String url, boolean dev)
specifier|private
specifier|static
name|HttpURLConnection
name|createURLConnection
parameter_list|(
name|String
name|url
parameter_list|,
name|boolean
name|dev
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO: support usage of proxy (via endpoint parameters or GLoginData object)
name|HttpURLConnection
name|connection
init|=
operator|(
name|HttpURLConnection
operator|)
operator|new
name|URL
argument_list|(
name|url
argument_list|)
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|setInstanceFollowRedirects
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|dev
condition|)
block|{
name|connection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/x-www-form-urlencoded"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setRequestMethod
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|connection
return|;
block|}
block|}
end_class

end_unit

