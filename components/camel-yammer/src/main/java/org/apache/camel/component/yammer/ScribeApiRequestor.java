begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
package|;
end_package

begin_import
import|import
name|org
operator|.
name|scribe
operator|.
name|model
operator|.
name|OAuthConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scribe
operator|.
name|model
operator|.
name|OAuthRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scribe
operator|.
name|model
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scribe
operator|.
name|model
operator|.
name|Verb
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ScribeApiRequestor
specifier|public
class|class
name|ScribeApiRequestor
implements|implements
name|ApiRequestor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ScribeApiRequestor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|apiUrl
name|String
name|apiUrl
decl_stmt|;
DECL|field|apiAccessToken
name|String
name|apiAccessToken
decl_stmt|;
DECL|method|ScribeApiRequestor (String apiUrl, String apiAccessToken)
specifier|public
name|ScribeApiRequestor
parameter_list|(
name|String
name|apiUrl
parameter_list|,
name|String
name|apiAccessToken
parameter_list|)
block|{
name|this
operator|.
name|apiUrl
operator|=
name|apiUrl
expr_stmt|;
name|this
operator|.
name|apiAccessToken
operator|=
name|apiAccessToken
expr_stmt|;
block|}
DECL|method|send (Verb verb, String params)
specifier|private
name|String
name|send
parameter_list|(
name|Verb
name|verb
parameter_list|,
name|String
name|params
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|url
init|=
name|apiUrl
operator|+
operator|(
operator|(
name|params
operator|!=
literal|null
operator|)
condition|?
name|params
else|:
literal|""
operator|)
decl_stmt|;
name|OAuthRequest
name|request
init|=
operator|new
name|OAuthRequest
argument_list|(
name|verb
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|request
operator|.
name|addQuerystringParameter
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|,
name|apiAccessToken
argument_list|)
expr_stmt|;
comment|// For more details on the âBearerâ token refer to http://tools.ietf.org/html/draft-ietf-oauth-v2-bearer-23
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Bearer "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|apiAccessToken
argument_list|)
expr_stmt|;
name|request
operator|.
name|addHeader
argument_list|(
literal|"Authorization"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Yammer request url: %s"
argument_list|,
name|request
operator|.
name|getCompleteUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Response
name|response
init|=
name|request
operator|.
name|send
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|isSuccessful
argument_list|()
condition|)
block|{
return|return
name|response
operator|.
name|getBody
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to poll %s. Got response code %s and body: %s"
argument_list|,
name|getApiUrl
argument_list|()
argument_list|,
name|response
operator|.
name|getCode
argument_list|()
argument_list|,
name|response
operator|.
name|getBody
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|getApiUrl ()
specifier|public
name|String
name|getApiUrl
parameter_list|()
block|{
return|return
name|apiUrl
return|;
block|}
DECL|method|setApiUrl (String apiUrl)
specifier|public
name|void
name|setApiUrl
parameter_list|(
name|String
name|apiUrl
parameter_list|)
block|{
name|this
operator|.
name|apiUrl
operator|=
name|apiUrl
expr_stmt|;
block|}
DECL|method|getApiAccessToken ()
specifier|public
name|String
name|getApiAccessToken
parameter_list|()
block|{
return|return
name|apiAccessToken
return|;
block|}
DECL|method|setApiAccessToken (String apiAccessToken)
specifier|public
name|void
name|setApiAccessToken
parameter_list|(
name|String
name|apiAccessToken
parameter_list|)
block|{
name|this
operator|.
name|apiAccessToken
operator|=
name|apiAccessToken
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|send
argument_list|(
name|Verb
operator|.
name|GET
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|post (String params)
specifier|public
name|String
name|post
parameter_list|(
name|String
name|params
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|send
argument_list|(
name|Verb
operator|.
name|POST
argument_list|,
name|params
argument_list|)
return|;
block|}
block|}
end_class

end_unit

