begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dropbox
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dropbox
operator|.
name|core
operator|.
name|DbxClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dropbox
operator|.
name|core
operator|.
name|DbxRequestConfig
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
name|component
operator|.
name|dropbox
operator|.
name|util
operator|.
name|DropboxException
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
name|component
operator|.
name|dropbox
operator|.
name|util
operator|.
name|DropboxOperation
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
name|component
operator|.
name|dropbox
operator|.
name|util
operator|.
name|DropboxUploadMode
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
DECL|class|DropboxConfiguration
specifier|public
class|class
name|DropboxConfiguration
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DropboxConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//dropbox auth options
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
comment|//local path to put files
DECL|field|localPath
specifier|private
name|String
name|localPath
decl_stmt|;
comment|//where to put files on dropbox
DECL|field|remotePath
specifier|private
name|String
name|remotePath
decl_stmt|;
comment|//new path on dropbox when moving files
DECL|field|newRemotePath
specifier|private
name|String
name|newRemotePath
decl_stmt|;
comment|//search query on dropbox
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
comment|//in case of uploading if force or add existing file
DECL|field|uploadMode
specifier|private
name|DropboxUploadMode
name|uploadMode
decl_stmt|;
comment|//id of the app
DECL|field|clientIdentifier
specifier|private
name|String
name|clientIdentifier
decl_stmt|;
comment|//specific dropbox operation for the component
DECL|field|operation
specifier|private
name|DropboxOperation
name|operation
decl_stmt|;
comment|//reference to dropboxclient
DECL|field|client
specifier|private
name|DbxClient
name|client
decl_stmt|;
DECL|method|setClient (DbxClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|DbxClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|DbxClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
comment|/**      * Obtain a new instance of DbxClient and store it in configuration.      * @throws DropboxException      */
DECL|method|createClient ()
specifier|public
name|void
name|createClient
parameter_list|()
throws|throws
name|DropboxException
block|{
name|DbxRequestConfig
name|config
init|=
operator|new
name|DbxRequestConfig
argument_list|(
name|clientIdentifier
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|DbxClient
name|client
init|=
operator|new
name|DbxClient
argument_list|(
name|config
argument_list|,
name|accessToken
argument_list|)
decl_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"can't establish a Dropbox conenction!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getAccessToken ()
specifier|public
name|String
name|getAccessToken
parameter_list|()
block|{
return|return
name|accessToken
return|;
block|}
DECL|method|setAccessToken (String accessToken)
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
block|}
DECL|method|getLocalPath ()
specifier|public
name|String
name|getLocalPath
parameter_list|()
block|{
return|return
name|localPath
return|;
block|}
DECL|method|setLocalPath (String localPath)
specifier|public
name|void
name|setLocalPath
parameter_list|(
name|String
name|localPath
parameter_list|)
block|{
name|this
operator|.
name|localPath
operator|=
name|localPath
expr_stmt|;
block|}
DECL|method|getRemotePath ()
specifier|public
name|String
name|getRemotePath
parameter_list|()
block|{
return|return
name|remotePath
return|;
block|}
DECL|method|setRemotePath (String remotePath)
specifier|public
name|void
name|setRemotePath
parameter_list|(
name|String
name|remotePath
parameter_list|)
block|{
name|this
operator|.
name|remotePath
operator|=
name|remotePath
expr_stmt|;
block|}
DECL|method|getNewRemotePath ()
specifier|public
name|String
name|getNewRemotePath
parameter_list|()
block|{
return|return
name|newRemotePath
return|;
block|}
DECL|method|setNewRemotePath (String newRemotePath)
specifier|public
name|void
name|setNewRemotePath
parameter_list|(
name|String
name|newRemotePath
parameter_list|)
block|{
name|this
operator|.
name|newRemotePath
operator|=
name|newRemotePath
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getClientIdentifier ()
specifier|public
name|String
name|getClientIdentifier
parameter_list|()
block|{
return|return
name|clientIdentifier
return|;
block|}
DECL|method|setClientIdentifier (String clientIdentifier)
specifier|public
name|void
name|setClientIdentifier
parameter_list|(
name|String
name|clientIdentifier
parameter_list|)
block|{
name|this
operator|.
name|clientIdentifier
operator|=
name|clientIdentifier
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|DropboxOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (DropboxOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|DropboxOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getUploadMode ()
specifier|public
name|DropboxUploadMode
name|getUploadMode
parameter_list|()
block|{
return|return
name|uploadMode
return|;
block|}
DECL|method|setUploadMode (DropboxUploadMode uploadMode)
specifier|public
name|void
name|setUploadMode
parameter_list|(
name|DropboxUploadMode
name|uploadMode
parameter_list|)
block|{
name|this
operator|.
name|uploadMode
operator|=
name|uploadMode
expr_stmt|;
block|}
block|}
end_class

end_unit

