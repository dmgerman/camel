begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.gauth
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|gauth
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|Cookie
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|Exchange
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
name|Processor
import|;
end_import

begin_import
import|import static
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
name|auth
operator|.
name|GAuthUpgradeBinding
operator|.
name|GAUTH_ACCESS_TOKEN
import|;
end_import

begin_import
import|import static
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
name|auth
operator|.
name|GAuthUpgradeBinding
operator|.
name|GAUTH_ACCESS_TOKEN_SECRET
import|;
end_import

begin_comment
comment|/**  * Reads an OAuth access token plus access token secret from a Camel message and stores them in  * cookies. These cookies are needed by {@link org.apache.camel.example.gauth.TutorialController}  * for accessing a user's calendar via the Google Calendar API. The cookies are valid for one  * hour. Finally, it generates an HTTP 302 response that redirects the user to the application's  * main location (/oauth/calendar).  *<p>  * In production systems it is<em>not</em> recommended to store access tokens in cookies. The   * recommended approach is to store them in a database. The demo application is only doing that  * to keep the example as simple as possible. However, an attacker could not use an access token  * alone to get access to a user's calendar data because the application's consumer secret is  * necessary for that as well. The consumer secret never leaves the demo application.  */
end_comment

begin_class
DECL|class|TutorialTokenProcessor
specifier|public
class|class
name|TutorialTokenProcessor
implements|implements
name|Processor
block|{
DECL|field|ONE_HOUR
specifier|private
specifier|static
specifier|final
name|int
name|ONE_HOUR
init|=
literal|3600
decl_stmt|;
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|accessToken
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GAUTH_ACCESS_TOKEN
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|accessTokenSecret
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GAUTH_ACCESS_TOKEN_SECRET
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessToken
operator|!=
literal|null
condition|)
block|{
name|HttpServletResponse
name|servletResponse
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_SERVLET_RESPONSE
argument_list|,
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|Cookie
name|accessTokenCookie
init|=
operator|new
name|Cookie
argument_list|(
literal|"TUTORIAL-ACCESS-TOKEN"
argument_list|,
name|accessToken
argument_list|)
decl_stmt|;
name|Cookie
name|accessTokenSecretCookie
init|=
operator|new
name|Cookie
argument_list|(
literal|"TUTORIAL-ACCESS-TOKEN-SECRET"
argument_list|,
name|accessTokenSecret
argument_list|)
decl_stmt|;
name|accessTokenCookie
operator|.
name|setPath
argument_list|(
literal|"/oauth/"
argument_list|)
expr_stmt|;
name|accessTokenCookie
operator|.
name|setMaxAge
argument_list|(
name|ONE_HOUR
argument_list|)
expr_stmt|;
name|accessTokenSecretCookie
operator|.
name|setPath
argument_list|(
literal|"/oauth/"
argument_list|)
expr_stmt|;
name|accessTokenSecretCookie
operator|.
name|setMaxAge
argument_list|(
name|ONE_HOUR
argument_list|)
expr_stmt|;
name|servletResponse
operator|.
name|addCookie
argument_list|(
name|accessTokenCookie
argument_list|)
expr_stmt|;
name|servletResponse
operator|.
name|addCookie
argument_list|(
name|accessTokenSecretCookie
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|302
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Location"
argument_list|,
literal|"/oauth/calendar"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

