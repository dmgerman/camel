begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox.validator
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
operator|.
name|validator
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dropbox
operator|.
name|DropboxConfiguration
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
name|DropboxConstants
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|DropboxConfigurationValidator
specifier|public
specifier|final
class|class
name|DropboxConfigurationValidator
block|{
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"/*?(\\S+)/*?"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
DECL|method|DropboxConfigurationValidator ()
specifier|private
name|DropboxConfigurationValidator
parameter_list|()
block|{ }
DECL|method|validateCommonProperties (DropboxConfiguration configuration)
specifier|public
specifier|static
name|void
name|validateCommonProperties
parameter_list|(
name|DropboxConfiguration
name|configuration
parameter_list|)
throws|throws
name|DropboxException
block|{
if|if
condition|(
name|configuration
operator|.
name|getAccessToken
argument_list|()
operator|==
literal|null
operator|||
name|configuration
operator|.
name|getAccessToken
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"option<accessToken> is not present or not valid!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getClientIdentifier
argument_list|()
operator|==
literal|null
operator|||
name|configuration
operator|.
name|getClientIdentifier
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"option<clientIdentifier> is not present or not valid!"
argument_list|)
throw|;
block|}
block|}
DECL|method|validateGetOp (String remotePath)
specifier|public
specifier|static
name|void
name|validateGetOp
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
name|validateRemotePath
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
block|}
DECL|method|validatePutOp (String localPath, String remotePath, DropboxUploadMode uploadMode)
specifier|public
specifier|static
name|void
name|validatePutOp
parameter_list|(
name|String
name|localPath
parameter_list|,
name|String
name|remotePath
parameter_list|,
name|DropboxUploadMode
name|uploadMode
parameter_list|)
throws|throws
name|DropboxException
block|{
name|validateLocalPath
argument_list|(
name|localPath
argument_list|)
expr_stmt|;
comment|//remote path is optional
if|if
condition|(
name|remotePath
operator|!=
literal|null
condition|)
block|{
name|validateRemotePathForPut
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//in case remote path is not set, local path is even the remote path so it must be validated as UNIX
name|validatePathInUnix
argument_list|(
name|localPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uploadMode
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"option<uploadMode> is not present or not valid!"
argument_list|)
throw|;
block|}
block|}
DECL|method|validateSearchOp (String remotePath)
specifier|public
specifier|static
name|void
name|validateSearchOp
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
name|validateRemotePath
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
block|}
DECL|method|validateDelOp (String remotePath)
specifier|public
specifier|static
name|void
name|validateDelOp
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
name|validateRemotePath
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
block|}
DECL|method|validateMoveOp (String remotePath, String newRemotePath)
specifier|public
specifier|static
name|void
name|validateMoveOp
parameter_list|(
name|String
name|remotePath
parameter_list|,
name|String
name|newRemotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
name|validateRemotePath
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
name|validateRemotePath
argument_list|(
name|newRemotePath
argument_list|)
expr_stmt|;
block|}
DECL|method|validateLocalPath (String localPath)
specifier|private
specifier|static
name|void
name|validateLocalPath
parameter_list|(
name|String
name|localPath
parameter_list|)
throws|throws
name|DropboxException
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|localPath
argument_list|)
condition|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|localPath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"option<localPath> is not an existing file or directory!"
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|validateRemotePath (String remotePath)
specifier|private
specifier|static
name|void
name|validateRemotePath
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
if|if
condition|(
name|remotePath
operator|==
literal|null
operator|||
operator|!
name|remotePath
operator|.
name|startsWith
argument_list|(
name|DropboxConstants
operator|.
name|DROPBOX_FILE_SEPARATOR
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"option<remotePath> is not valid!"
argument_list|)
throw|;
block|}
name|validatePathInUnix
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
block|}
DECL|method|validateRemotePathForPut (String remotePath)
specifier|private
specifier|static
name|void
name|validateRemotePathForPut
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
if|if
condition|(
operator|!
name|remotePath
operator|.
name|startsWith
argument_list|(
name|DropboxConstants
operator|.
name|DROPBOX_FILE_SEPARATOR
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"option<remotePath> is not valid!"
argument_list|)
throw|;
block|}
name|validatePathInUnix
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
block|}
DECL|method|validatePathInUnix (String path)
specifier|private
specifier|static
name|void
name|validatePathInUnix
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|DropboxException
block|{
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|path
operator|+
literal|" is not a valid path, must be in UNIX form!"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

