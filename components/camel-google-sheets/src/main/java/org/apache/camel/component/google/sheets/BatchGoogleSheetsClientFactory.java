begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.sheets
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
name|sheets
package|;
end_package

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
name|sheets
operator|.
name|v4
operator|.
name|Sheets
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|BatchGoogleSheetsClientFactory
specifier|public
class|class
name|BatchGoogleSheetsClientFactory
implements|implements
name|GoogleSheetsClientFactory
block|{
DECL|field|transport
specifier|private
specifier|final
name|HttpTransport
name|transport
decl_stmt|;
DECL|field|jsonFactory
specifier|private
specifier|final
name|JacksonFactory
name|jsonFactory
decl_stmt|;
DECL|method|BatchGoogleSheetsClientFactory ()
specifier|public
name|BatchGoogleSheetsClientFactory
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|NetHttpTransport
argument_list|()
argument_list|,
operator|new
name|JacksonFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|BatchGoogleSheetsClientFactory (HttpTransport httpTransport)
specifier|public
name|BatchGoogleSheetsClientFactory
parameter_list|(
name|HttpTransport
name|httpTransport
parameter_list|)
block|{
name|this
argument_list|(
name|httpTransport
argument_list|,
operator|new
name|JacksonFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|BatchGoogleSheetsClientFactory (HttpTransport httpTransport, JacksonFactory jacksonFactory)
specifier|public
name|BatchGoogleSheetsClientFactory
parameter_list|(
name|HttpTransport
name|httpTransport
parameter_list|,
name|JacksonFactory
name|jacksonFactory
parameter_list|)
block|{
name|this
operator|.
name|transport
operator|=
name|httpTransport
expr_stmt|;
name|this
operator|.
name|jsonFactory
operator|=
name|jacksonFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|makeClient (String clientId, String clientSecret, String applicationName, String refreshToken, String accessToken)
specifier|public
name|Sheets
name|makeClient
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|String
name|applicationName
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|String
name|accessToken
parameter_list|)
block|{
if|if
condition|(
name|clientId
operator|==
literal|null
operator|||
name|clientSecret
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"clientId and clientSecret are required to create Google Sheets client."
argument_list|)
throw|;
block|}
try|try
block|{
name|Credential
name|credential
init|=
name|authorize
argument_list|(
name|clientId
argument_list|,
name|clientSecret
argument_list|,
name|refreshToken
argument_list|,
name|accessToken
argument_list|)
decl_stmt|;
name|Sheets
operator|.
name|Builder
name|clientBuilder
init|=
operator|new
name|Sheets
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
decl_stmt|;
name|configure
argument_list|(
name|clientBuilder
argument_list|)
expr_stmt|;
return|return
name|clientBuilder
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
literal|"Could not create Google Sheets client."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Subclasses may add customized configuration to client builder.      * @param clientBuilder      */
DECL|method|configure (Sheets.Builder clientBuilder)
specifier|protected
name|void
name|configure
parameter_list|(
name|Sheets
operator|.
name|Builder
name|clientBuilder
parameter_list|)
block|{
name|clientBuilder
operator|.
name|setRootUrl
argument_list|(
name|Sheets
operator|.
name|DEFAULT_ROOT_URL
argument_list|)
expr_stmt|;
block|}
comment|// Authorizes the installed application to access user's protected data.
DECL|method|authorize (String clientId, String clientSecret, String refreshToken, String accessToken)
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
name|String
name|refreshToken
parameter_list|,
name|String
name|accessToken
parameter_list|)
block|{
comment|// authorize
name|Credential
name|credential
init|=
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
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
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
name|ObjectHelper
operator|.
name|isNotEmpty
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
return|return
name|credential
return|;
block|}
block|}
end_class

end_unit

