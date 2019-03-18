begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DbxRequestConfig
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
name|v2
operator|.
name|DbxClientV2
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

begin_class
annotation|@
name|UriParams
DECL|class|DropboxConfiguration
specifier|public
class|class
name|DropboxConfiguration
block|{
comment|//specific dropbox operation for the component
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|operation
specifier|private
name|DropboxOperation
name|operation
decl_stmt|;
comment|//dropbox auth options
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
comment|//local path to put files
annotation|@
name|UriParam
DECL|field|localPath
specifier|private
name|String
name|localPath
decl_stmt|;
comment|//where to put files on dropbox
annotation|@
name|UriParam
DECL|field|remotePath
specifier|private
name|String
name|remotePath
decl_stmt|;
comment|//new path on dropbox when moving files
annotation|@
name|UriParam
DECL|field|newRemotePath
specifier|private
name|String
name|newRemotePath
decl_stmt|;
comment|//search query on dropbox
annotation|@
name|UriParam
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
comment|//in case of uploading if force or add existing file
annotation|@
name|UriParam
DECL|field|uploadMode
specifier|private
name|DropboxUploadMode
name|uploadMode
decl_stmt|;
comment|//id of the app
annotation|@
name|UriParam
DECL|field|clientIdentifier
specifier|private
name|String
name|clientIdentifier
decl_stmt|;
comment|//reference to dropbox client
annotation|@
name|UriParam
DECL|field|client
specifier|private
name|DbxClientV2
name|client
decl_stmt|;
comment|/**      * To use an existing DbxClient instance as DropBox client.      */
DECL|method|setClient (DbxClientV2 client)
specifier|public
name|void
name|setClient
parameter_list|(
name|DbxClientV2
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
name|DbxClientV2
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
comment|/**      * Obtain a new instance of DbxClient and store it in configuration.      */
DECL|method|createClient ()
specifier|public
name|void
name|createClient
parameter_list|()
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
name|this
operator|.
name|client
operator|=
operator|new
name|DbxClientV2
argument_list|(
name|config
argument_list|,
name|accessToken
argument_list|)
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
comment|/**      * The access token to make API requests for a specific Dropbox user      */
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
comment|/**      * Optional folder or file to upload on Dropbox from the local filesystem.      * If this option has not been configured then the message body is used as the content to upload.      */
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
comment|/**      * Original file or folder to move      */
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
comment|/**      * Destination file or folder      */
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
comment|/**      * A space-separated list of sub-strings to search for. A file matches only if it contains all the sub-strings. If this option is not set, all files will be matched.      */
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
comment|/**      * Name of the app registered to make API requests      */
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
comment|/**      * The specific action (typically is a CRUD action) to perform on Dropbox remote folder.      */
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
comment|/**      * Which mode to upload.      * in case of "add" the new file will be renamed if a file with the same name already exists on dropbox.      * in case of "force" if a file with the same name already exists on dropbox, this will be overwritten.      */
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

