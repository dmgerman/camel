begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar
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
name|calendar
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|auth
operator|.
name|oauth2
operator|.
name|Credential
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
name|googleapis
operator|.
name|auth
operator|.
name|oauth2
operator|.
name|GoogleCredential
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
name|googleapis
operator|.
name|javanet
operator|.
name|GoogleNetHttpTransport
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
name|HttpTransport
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
name|calendar
operator|.
name|Calendar
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
name|RuntimeCamelException
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
DECL|class|BatchGoogleCalendarClientFactory
specifier|public
class|class
name|BatchGoogleCalendarClientFactory
implements|implements
name|GoogleCalendarClientFactory
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
name|BatchGoogleCalendarClientFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|transport
specifier|private
name|NetHttpTransport
name|transport
decl_stmt|;
DECL|field|jsonFactory
specifier|private
name|JacksonFactory
name|jsonFactory
decl_stmt|;
DECL|method|BatchGoogleCalendarClientFactory ()
specifier|public
name|BatchGoogleCalendarClientFactory
parameter_list|()
block|{
name|this
operator|.
name|transport
operator|=
operator|new
name|NetHttpTransport
argument_list|()
expr_stmt|;
name|this
operator|.
name|jsonFactory
operator|=
operator|new
name|JacksonFactory
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|makeClient (String clientId, String clientSecret, Collection<String> scopes, String applicationName, String refreshToken, String accessToken, String emailAddress, String p12FileName, String user)
specifier|public
name|Calendar
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
parameter_list|,
name|String
name|emailAddress
parameter_list|,
name|String
name|p12FileName
parameter_list|,
name|String
name|user
parameter_list|)
block|{
name|boolean
name|serviceAccount
init|=
literal|false
decl_stmt|;
comment|// if emailAddress and p12FileName values are present, assume Google Service Account
if|if
condition|(
literal|null
operator|!=
name|emailAddress
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|emailAddress
argument_list|)
operator|&&
literal|null
operator|!=
name|p12FileName
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|p12FileName
argument_list|)
condition|)
block|{
name|serviceAccount
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|serviceAccount
operator|&&
operator|(
name|clientId
operator|==
literal|null
operator|||
name|clientSecret
operator|==
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"clientId and clientSecret are required to create Google Calendar client."
argument_list|)
throw|;
block|}
try|try
block|{
name|Credential
name|credential
decl_stmt|;
if|if
condition|(
name|serviceAccount
condition|)
block|{
name|credential
operator|=
name|authorizeServiceAccount
argument_list|(
name|emailAddress
argument_list|,
name|p12FileName
argument_list|,
name|scopes
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|credential
operator|=
name|authorize
argument_list|(
name|clientId
argument_list|,
name|clientSecret
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
if|if
condition|(
name|refreshToken
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|refreshToken
argument_list|)
condition|)
block|{
name|credential
operator|.
name|setRefreshToken
argument_list|(
name|refreshToken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|accessToken
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|accessToken
argument_list|)
condition|)
block|{
name|credential
operator|.
name|setAccessToken
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|Calendar
operator|.
name|Builder
argument_list|(
name|transport
argument_list|,
name|jsonFactory
argument_list|,
name|credential
argument_list|)
operator|.
name|setApplicationName
argument_list|(
name|applicationName
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Could not create Google Calendar client."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// Authorizes the installed application to access user's protected data.
DECL|method|authorize (String clientId, String clientSecret, Collection<String> scopes)
specifier|private
name|Credential
name|authorize
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
parameter_list|)
throws|throws
name|Exception
block|{
comment|// authorize
return|return
operator|new
name|GoogleCredential
operator|.
name|Builder
argument_list|()
operator|.
name|setJsonFactory
argument_list|(
name|jsonFactory
argument_list|)
operator|.
name|setTransport
argument_list|(
name|transport
argument_list|)
operator|.
name|setClientSecrets
argument_list|(
name|clientId
argument_list|,
name|clientSecret
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|authorizeServiceAccount (String emailAddress, String p12FileName, Collection<String> scopes, String user)
specifier|private
name|Credential
name|authorizeServiceAccount
parameter_list|(
name|String
name|emailAddress
parameter_list|,
name|String
name|p12FileName
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|scopes
parameter_list|,
name|String
name|user
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpTransport
name|httpTransport
init|=
name|GoogleNetHttpTransport
operator|.
name|newTrustedTransport
argument_list|()
decl_stmt|;
comment|// set the service account user when provided
name|GoogleCredential
name|credential
init|=
operator|new
name|GoogleCredential
operator|.
name|Builder
argument_list|()
operator|.
name|setTransport
argument_list|(
name|httpTransport
argument_list|)
operator|.
name|setJsonFactory
argument_list|(
name|jsonFactory
argument_list|)
operator|.
name|setServiceAccountId
argument_list|(
name|emailAddress
argument_list|)
operator|.
name|setServiceAccountPrivateKeyFromP12File
argument_list|(
operator|new
name|File
argument_list|(
name|p12FileName
argument_list|)
argument_list|)
operator|.
name|setServiceAccountScopes
argument_list|(
name|scopes
argument_list|)
operator|.
name|setServiceAccountUser
argument_list|(
name|user
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|credential
return|;
block|}
block|}
end_class

end_unit

