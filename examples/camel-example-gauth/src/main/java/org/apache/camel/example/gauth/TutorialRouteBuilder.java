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
name|java
operator|.
name|net
operator|.
name|URLEncoder
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * Builds the OAuth-specific routes (implements the OAuth integration layer) of the demo application.  */
end_comment

begin_class
DECL|class|TutorialRouteBuilder
specifier|public
class|class
name|TutorialRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|application
specifier|private
name|String
name|application
decl_stmt|;
comment|/**      * Sets the name of the GAE application.      *      * @param application a GAE application name.      */
DECL|method|setApplication (String application)
specifier|public
name|void
name|setApplication
parameter_list|(
name|String
name|application
parameter_list|)
block|{
name|this
operator|.
name|application
operator|=
name|application
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Callback URL for sending back an authorized access token.
name|String
name|encodedCallback
init|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"https://%s.appspot.com/camel/handler"
argument_list|,
name|application
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
comment|// Google should issue an access token that is scoped to calendar feeds.
name|String
name|encodedScope
init|=
name|URLEncoder
operator|.
name|encode
argument_list|(
literal|"http://www.google.com/calendar/feeds/"
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
comment|// Route for obtaining an unauthorized request token from Google Accounts. The
comment|// response redirects the browser to an authorization page provided by Google.
name|from
argument_list|(
literal|"ghttp:///authorize"
argument_list|)
operator|.
name|to
argument_list|(
literal|"gauth:authorize?callback="
operator|+
name|encodedCallback
operator|+
literal|"&scope="
operator|+
name|encodedScope
argument_list|)
expr_stmt|;
comment|// Handles callbacks from Google Accounts which contain an authorized request token.
comment|// The authorized request token is upgraded to an access token which is stored in
comment|// the response message header. The TutorialTokenProcessor is application-specific
comment|// and stores the access token (plus access token secret) is cookies. It further
comment|// redirects the user to the application's main location (/oauth/calendar).
name|from
argument_list|(
literal|"ghttp:///handler"
argument_list|)
operator|.
name|to
argument_list|(
literal|"gauth:upgrade"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|TutorialTokenProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

