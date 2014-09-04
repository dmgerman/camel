begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive
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
name|drive
package|;
end_package

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
name|extensions
operator|.
name|java6
operator|.
name|auth
operator|.
name|oauth2
operator|.
name|AuthorizationCodeInstalledApp
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
name|extensions
operator|.
name|jetty
operator|.
name|auth
operator|.
name|oauth2
operator|.
name|LocalServerReceiver
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
name|GoogleAuthorizationCodeFlow
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
name|client
operator|.
name|util
operator|.
name|store
operator|.
name|FileDataStoreFactory
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
name|drive
operator|.
name|Drive
import|;
end_import

begin_class
DECL|class|DefaultGoogleDriveClientFactory
specifier|public
class|class
name|DefaultGoogleDriveClientFactory
implements|implements
name|GoogleDriveClientFactory
block|{
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
DECL|field|dataStoreFactory
specifier|private
name|FileDataStoreFactory
name|dataStoreFactory
decl_stmt|;
comment|// TODO Directory to store user credentials
DECL|field|DATA_STORE_DIR
specifier|private
specifier|static
specifier|final
name|java
operator|.
name|io
operator|.
name|File
name|DATA_STORE_DIR
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
argument_list|,
literal|".store/drive_sample"
argument_list|)
decl_stmt|;
DECL|method|DefaultGoogleDriveClientFactory ()
specifier|public
name|DefaultGoogleDriveClientFactory
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
comment|/*      * (non-Javadoc)      *       * @see      * org.apache.camel.component.google.drive.GoogleDriveClientFactory#makeClient      * (java.lang.String, java.lang.String, java.util.Collection)      */
annotation|@
name|Override
DECL|method|makeClient (String clientId, String clientSecret, Collection<String> scopes)
specifier|public
name|Drive
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
parameter_list|)
block|{
name|Credential
name|credential
decl_stmt|;
try|try
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
return|return
operator|new
name|Drive
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
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
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
name|dataStoreFactory
operator|=
operator|new
name|FileDataStoreFactory
argument_list|(
name|DATA_STORE_DIR
argument_list|)
expr_stmt|;
comment|// set up authorization code flow
comment|// TODO refresh token support too
name|GoogleAuthorizationCodeFlow
name|flow
init|=
operator|new
name|GoogleAuthorizationCodeFlow
operator|.
name|Builder
argument_list|(
name|transport
argument_list|,
name|jsonFactory
argument_list|,
name|clientId
argument_list|,
name|clientSecret
argument_list|,
name|scopes
argument_list|)
operator|.
name|setDataStoreFactory
argument_list|(
name|dataStoreFactory
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// authorize
return|return
operator|new
name|AuthorizationCodeInstalledApp
argument_list|(
name|flow
argument_list|,
operator|new
name|LocalServerReceiver
argument_list|()
argument_list|)
operator|.
name|authorize
argument_list|(
literal|"user"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

