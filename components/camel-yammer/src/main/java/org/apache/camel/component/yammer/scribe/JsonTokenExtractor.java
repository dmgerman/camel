begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.scribe
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
operator|.
name|scribe
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scribe
operator|.
name|exceptions
operator|.
name|OAuthException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scribe
operator|.
name|extractors
operator|.
name|AccessTokenExtractor
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
name|Token
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scribe
operator|.
name|utils
operator|.
name|Preconditions
import|;
end_import

begin_class
DECL|class|JsonTokenExtractor
specifier|public
class|class
name|JsonTokenExtractor
implements|implements
name|AccessTokenExtractor
block|{
DECL|field|DEFAULT_ACCESS_TOKEN_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|DEFAULT_ACCESS_TOKEN_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"access_token\":\\s*\"(\\S*?)\""
argument_list|)
decl_stmt|;
DECL|field|accessTokenPattern
specifier|private
name|Pattern
name|accessTokenPattern
decl_stmt|;
DECL|method|JsonTokenExtractor ()
specifier|public
name|JsonTokenExtractor
parameter_list|()
block|{
name|accessTokenPattern
operator|=
name|DEFAULT_ACCESS_TOKEN_PATTERN
expr_stmt|;
block|}
DECL|method|JsonTokenExtractor (String tokenRegex)
specifier|public
name|JsonTokenExtractor
parameter_list|(
name|String
name|tokenRegex
parameter_list|)
block|{
name|accessTokenPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|tokenRegex
argument_list|)
expr_stmt|;
block|}
DECL|method|extract (String response)
specifier|public
name|Token
name|extract
parameter_list|(
name|String
name|response
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkEmptyString
argument_list|(
name|response
argument_list|,
literal|"Cannot extract a token from a null or empty String"
argument_list|)
expr_stmt|;
name|Matcher
name|matcher
init|=
name|accessTokenPattern
operator|.
name|matcher
argument_list|(
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
operator|new
name|Token
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|""
argument_list|,
name|response
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|OAuthException
argument_list|(
literal|"Cannot extract an acces token. Response was: "
operator|+
name|response
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

