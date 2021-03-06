begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox.core
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
name|core
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractMap
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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|DbxDownloader
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
name|DbxException
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
name|com
operator|.
name|dropbox
operator|.
name|core
operator|.
name|v2
operator|.
name|files
operator|.
name|FileMetadata
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
name|files
operator|.
name|ListFolderErrorException
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
name|files
operator|.
name|ListFolderResult
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
name|files
operator|.
name|Metadata
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
name|files
operator|.
name|SearchMatch
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
name|files
operator|.
name|SearchResult
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
name|files
operator|.
name|WriteMode
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
name|component
operator|.
name|dropbox
operator|.
name|dto
operator|.
name|DropboxDelResult
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
name|dto
operator|.
name|DropboxFileDownloadResult
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
name|dto
operator|.
name|DropboxFileUploadResult
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
name|dto
operator|.
name|DropboxMoveResult
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
name|dto
operator|.
name|DropboxSearchResult
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
name|DropboxResultCode
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
name|support
operator|.
name|builder
operator|.
name|OutputStreamBuilder
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxConstants
operator|.
name|HEADER_PUT_FILE_NAME
import|;
end_import

begin_class
DECL|class|DropboxAPIFacade
specifier|public
specifier|final
class|class
name|DropboxAPIFacade
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
name|DropboxAPIFacade
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
specifier|final
name|DbxClientV2
name|client
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
comment|/**      * @param client the DbxClient performing dropbox low level operations      * @param exchange the current Exchange      */
DECL|method|DropboxAPIFacade (DbxClientV2 client, Exchange exchange)
specifier|public
name|DropboxAPIFacade
parameter_list|(
name|DbxClientV2
name|client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
comment|/**      * Put or upload a new file or an entire directory to dropbox      *      * @param localPath the file path or the dir path on the local filesystem      * @param remotePath the remote path destination on dropbox      * @param mode how a file should be saved on dropbox; in case of "add" the      *            new file will be renamed in case a file with the same name      *            already exists on dropbox. in case of "force" the file already      *            existing with the same name will be overridden.      * @return a result object reporting for each remote path the result of the      *         operation.      * @throws DropboxException      */
DECL|method|put (String localPath, String remotePath, DropboxUploadMode mode)
specifier|public
name|DropboxFileUploadResult
name|put
parameter_list|(
name|String
name|localPath
parameter_list|,
name|String
name|remotePath
parameter_list|,
name|DropboxUploadMode
name|mode
parameter_list|)
throws|throws
name|DropboxException
block|{
comment|// in case the remote path is not specified, the remotePath = localPath
name|String
name|dropboxPath
init|=
name|remotePath
operator|==
literal|null
condition|?
name|localPath
else|:
name|remotePath
decl_stmt|;
name|boolean
name|isPresent
init|=
literal|true
decl_stmt|;
try|try
block|{
name|client
operator|.
name|files
argument_list|()
operator|.
name|getMetadata
argument_list|(
name|dropboxPath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DbxException
name|e
parameter_list|)
block|{
name|isPresent
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|localPath
operator|!=
literal|null
condition|)
block|{
return|return
name|putFile
argument_list|(
name|localPath
argument_list|,
name|mode
argument_list|,
name|dropboxPath
argument_list|,
name|isPresent
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|putBody
argument_list|(
name|exchange
argument_list|,
name|mode
argument_list|,
name|dropboxPath
argument_list|,
name|isPresent
argument_list|)
return|;
block|}
block|}
DECL|method|putFile (String localPath, DropboxUploadMode mode, String dropboxPath, boolean isPresent)
specifier|private
name|DropboxFileUploadResult
name|putFile
parameter_list|(
name|String
name|localPath
parameter_list|,
name|DropboxUploadMode
name|mode
parameter_list|,
name|String
name|dropboxPath
parameter_list|,
name|boolean
name|isPresent
parameter_list|)
throws|throws
name|DropboxException
block|{
name|File
name|fileLocalPath
init|=
operator|new
name|File
argument_list|(
name|localPath
argument_list|)
decl_stmt|;
comment|// verify uploading of a single file
if|if
condition|(
name|fileLocalPath
operator|.
name|isFile
argument_list|()
condition|)
block|{
comment|// check if dropbox file exists
if|if
condition|(
name|isPresent
operator|&&
operator|!
name|DropboxUploadMode
operator|.
name|force
operator|.
name|equals
argument_list|(
name|mode
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|dropboxPath
operator|+
literal|" exists on dropbox. Use force upload mode to override"
argument_list|)
throw|;
block|}
comment|// in case the entry not exists on dropbox check if the filename
comment|// should be appended
if|if
condition|(
operator|!
name|isPresent
condition|)
block|{
if|if
condition|(
name|dropboxPath
operator|.
name|endsWith
argument_list|(
name|DropboxConstants
operator|.
name|DROPBOX_FILE_SEPARATOR
argument_list|)
condition|)
block|{
name|dropboxPath
operator|=
name|dropboxPath
operator|+
name|fileLocalPath
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Uploading: {},{}"
argument_list|,
name|fileLocalPath
argument_list|,
name|dropboxPath
argument_list|)
expr_stmt|;
name|DropboxFileUploadResult
name|result
decl_stmt|;
try|try
block|{
name|FileMetadata
name|uploadedFile
init|=
name|putSingleFile
argument_list|(
name|fileLocalPath
argument_list|,
name|dropboxPath
argument_list|,
name|mode
argument_list|)
decl_stmt|;
if|if
condition|(
name|uploadedFile
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|DropboxFileUploadResult
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|KO
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
operator|new
name|DropboxFileUploadResult
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|result
operator|=
operator|new
name|DropboxFileUploadResult
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|KO
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
elseif|else
if|if
condition|(
name|fileLocalPath
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// verify uploading of a list of files inside a dir
name|LOG
operator|.
name|debug
argument_list|(
literal|"Uploading a dir..."
argument_list|)
expr_stmt|;
comment|// check if dropbox folder exists
if|if
condition|(
name|isPresent
operator|&&
operator|!
name|DropboxUploadMode
operator|.
name|force
operator|.
name|equals
argument_list|(
name|mode
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|dropboxPath
operator|+
literal|" exists on dropbox and is not a folder!"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|dropboxPath
operator|.
name|endsWith
argument_list|(
name|DropboxConstants
operator|.
name|DROPBOX_FILE_SEPARATOR
argument_list|)
condition|)
block|{
name|dropboxPath
operator|=
name|dropboxPath
operator|+
name|DropboxConstants
operator|.
name|DROPBOX_FILE_SEPARATOR
expr_stmt|;
block|}
comment|// revert to old path
name|String
name|oldDropboxPath
init|=
name|dropboxPath
decl_stmt|;
comment|// list all files in a dir
name|Collection
argument_list|<
name|File
argument_list|>
name|listFiles
init|=
name|FileUtils
operator|.
name|listFiles
argument_list|(
name|fileLocalPath
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|listFiles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|localPath
operator|+
literal|" does not contain any files"
argument_list|)
throw|;
block|}
name|HashMap
argument_list|<
name|String
argument_list|,
name|DropboxResultCode
argument_list|>
name|resultMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|listFiles
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|listFiles
control|)
block|{
name|String
name|absPath
init|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|int
name|indexRemainingPath
init|=
name|localPath
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|localPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|indexRemainingPath
operator|+=
literal|1
expr_stmt|;
block|}
name|String
name|remainingPath
init|=
name|absPath
operator|.
name|substring
argument_list|(
name|indexRemainingPath
argument_list|)
decl_stmt|;
name|dropboxPath
operator|=
name|dropboxPath
operator|+
name|remainingPath
expr_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Uploading: {},{}"
argument_list|,
name|fileLocalPath
argument_list|,
name|dropboxPath
argument_list|)
expr_stmt|;
name|FileMetadata
name|uploadedFile
init|=
name|putSingleFile
argument_list|(
name|file
argument_list|,
name|dropboxPath
argument_list|,
name|mode
argument_list|)
decl_stmt|;
if|if
condition|(
name|uploadedFile
operator|==
literal|null
condition|)
block|{
name|resultMap
operator|.
name|put
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|KO
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resultMap
operator|.
name|put
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|resultMap
operator|.
name|put
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|KO
argument_list|)
expr_stmt|;
block|}
name|dropboxPath
operator|=
name|oldDropboxPath
expr_stmt|;
block|}
return|return
operator|new
name|DropboxFileUploadResult
argument_list|(
name|resultMap
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|putBody (Exchange exchange, DropboxUploadMode mode, String dropboxPath, boolean isPresent)
specifier|private
name|DropboxFileUploadResult
name|putBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DropboxUploadMode
name|mode
parameter_list|,
name|String
name|dropboxPath
parameter_list|,
name|boolean
name|isPresent
parameter_list|)
throws|throws
name|DropboxException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HEADER_PUT_FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
comment|// fallback to use CamelFileName
name|name
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
comment|// use message id as file name
name|name
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
expr_stmt|;
block|}
comment|// in case the entry not exists on dropbox check if the filename should
comment|// be appended
if|if
condition|(
operator|!
name|isPresent
condition|)
block|{
if|if
condition|(
name|dropboxPath
operator|.
name|endsWith
argument_list|(
name|DropboxConstants
operator|.
name|DROPBOX_FILE_SEPARATOR
argument_list|)
condition|)
block|{
name|dropboxPath
operator|=
name|dropboxPath
operator|+
name|name
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Uploading message body: {}"
argument_list|,
name|dropboxPath
argument_list|)
expr_stmt|;
name|DropboxFileUploadResult
name|result
decl_stmt|;
try|try
block|{
name|FileMetadata
name|uploadedFile
init|=
name|putSingleBody
argument_list|(
name|exchange
argument_list|,
name|dropboxPath
argument_list|,
name|mode
argument_list|)
decl_stmt|;
if|if
condition|(
name|uploadedFile
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|DropboxFileUploadResult
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|KO
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
operator|new
name|DropboxFileUploadResult
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|result
operator|=
operator|new
name|DropboxFileUploadResult
argument_list|(
name|dropboxPath
argument_list|,
name|DropboxResultCode
operator|.
name|KO
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|putSingleFile (File inputFile, String dropboxPath, DropboxUploadMode mode)
specifier|private
name|FileMetadata
name|putSingleFile
parameter_list|(
name|File
name|inputFile
parameter_list|,
name|String
name|dropboxPath
parameter_list|,
name|DropboxUploadMode
name|mode
parameter_list|)
throws|throws
name|Exception
block|{
name|FileInputStream
name|inputStream
init|=
operator|new
name|FileInputStream
argument_list|(
name|inputFile
argument_list|)
decl_stmt|;
name|FileMetadata
name|uploadedFile
decl_stmt|;
try|try
block|{
name|WriteMode
name|uploadMode
decl_stmt|;
if|if
condition|(
name|mode
operator|==
name|DropboxUploadMode
operator|.
name|force
condition|)
block|{
name|uploadMode
operator|=
name|WriteMode
operator|.
name|OVERWRITE
expr_stmt|;
block|}
else|else
block|{
name|uploadMode
operator|=
name|WriteMode
operator|.
name|ADD
expr_stmt|;
block|}
name|uploadedFile
operator|=
name|client
operator|.
name|files
argument_list|()
operator|.
name|uploadBuilder
argument_list|(
name|dropboxPath
argument_list|)
operator|.
name|withMode
argument_list|(
name|uploadMode
argument_list|)
operator|.
name|uploadAndFinish
argument_list|(
name|inputStream
argument_list|,
name|inputFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|uploadedFile
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|putSingleBody (Exchange exchange, String dropboxPath, DropboxUploadMode mode)
specifier|private
name|FileMetadata
name|putSingleBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|dropboxPath
parameter_list|,
name|DropboxUploadMode
name|mode
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|data
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
decl_stmt|;
try|try
block|{
name|FileMetadata
name|uploadedFile
decl_stmt|;
name|WriteMode
name|uploadMode
decl_stmt|;
if|if
condition|(
name|mode
operator|==
name|DropboxUploadMode
operator|.
name|force
condition|)
block|{
name|uploadMode
operator|=
name|WriteMode
operator|.
name|OVERWRITE
expr_stmt|;
block|}
else|else
block|{
name|uploadMode
operator|=
name|WriteMode
operator|.
name|ADD
expr_stmt|;
block|}
name|uploadedFile
operator|=
name|client
operator|.
name|files
argument_list|()
operator|.
name|uploadBuilder
argument_list|(
name|dropboxPath
argument_list|)
operator|.
name|withMode
argument_list|(
name|uploadMode
argument_list|)
operator|.
name|uploadAndFinish
argument_list|(
name|is
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|uploadedFile
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Search inside a remote path including its sub directories. The query      * param can be null.      *      * @param remotePath the remote path where starting the search from      * @param query a space-separated list of substrings to search for. A file      *            matches only if it contains all the substrings      * @return a result object containing all the files found.      * @throws DropboxException      */
DECL|method|search (String remotePath, String query)
specifier|public
name|DropboxSearchResult
name|search
parameter_list|(
name|String
name|remotePath
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|DropboxException
block|{
name|SearchResult
name|listing
decl_stmt|;
name|List
argument_list|<
name|SearchMatch
argument_list|>
name|searchMatches
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Search no query"
argument_list|)
expr_stmt|;
try|try
block|{
name|listing
operator|=
name|client
operator|.
name|files
argument_list|()
operator|.
name|search
argument_list|(
name|remotePath
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|searchMatches
operator|=
name|listing
operator|.
name|getMatches
argument_list|()
expr_stmt|;
return|return
operator|new
name|DropboxSearchResult
argument_list|(
name|searchMatches
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|DbxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|remotePath
operator|+
literal|" does not exist or cannot obtain metadata"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Search by query: {}"
argument_list|,
name|query
argument_list|)
expr_stmt|;
try|try
block|{
name|listing
operator|=
name|client
operator|.
name|files
argument_list|()
operator|.
name|search
argument_list|(
name|remotePath
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|searchMatches
operator|=
name|listing
operator|.
name|getMatches
argument_list|()
expr_stmt|;
return|return
operator|new
name|DropboxSearchResult
argument_list|(
name|searchMatches
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|DbxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|remotePath
operator|+
literal|" does not exist or cannot obtain metadata"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Delete every files and subdirectories inside the remote directory. In      * case the remotePath is a file, delete the file.      *      * @param remotePath the remote location to delete      * @return a result object with the result of the delete operation.      * @throws DropboxException      */
DECL|method|del (String remotePath)
specifier|public
name|DropboxDelResult
name|del
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
try|try
block|{
name|client
operator|.
name|files
argument_list|()
operator|.
name|deleteV2
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DbxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|remotePath
operator|+
literal|" does not exist or cannot obtain metadata"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|DropboxDelResult
argument_list|(
name|remotePath
argument_list|)
return|;
block|}
comment|/**      * Rename a remote path with the new path location.      *      * @param remotePath the existing remote path to be renamed      * @param newRemotePath the new remote path substituting the old one      * @return a result object with the result of the move operation.      * @throws DropboxException      */
DECL|method|move (String remotePath, String newRemotePath)
specifier|public
name|DropboxMoveResult
name|move
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
try|try
block|{
name|client
operator|.
name|files
argument_list|()
operator|.
name|moveV2
argument_list|(
name|remotePath
argument_list|,
name|newRemotePath
argument_list|)
expr_stmt|;
return|return
operator|new
name|DropboxMoveResult
argument_list|(
name|remotePath
argument_list|,
name|newRemotePath
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|DbxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|remotePath
operator|+
literal|" does not exist or cannot obtain metadata"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Get the content of every file inside the remote path.      *      * @param remotePath the remote path where to download from      * @return a result object with the content (ByteArrayOutputStream) of every      *         files inside the remote path.      * @throws DropboxException      */
DECL|method|get (String remotePath)
specifier|public
name|DropboxFileDownloadResult
name|get
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
return|return
operator|new
name|DropboxFileDownloadResult
argument_list|(
name|downloadFilesInFolder
argument_list|(
name|remotePath
argument_list|)
argument_list|)
return|;
block|}
DECL|method|downloadFilesInFolder (String path)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|downloadFilesInFolder
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|DropboxException
block|{
try|try
block|{
name|ListFolderResult
name|folderResult
init|=
name|client
operator|.
name|files
argument_list|()
operator|.
name|listFolder
argument_list|(
name|path
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|?
literal|""
else|:
name|path
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|returnMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Metadata
name|entry
range|:
name|folderResult
operator|.
name|getEntries
argument_list|()
control|)
block|{
name|returnMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getPathDisplay
argument_list|()
argument_list|,
name|downloadSingleFile
argument_list|(
name|entry
operator|.
name|getPathDisplay
argument_list|()
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|returnMap
return|;
block|}
catch|catch
parameter_list|(
name|ListFolderErrorException
name|e
parameter_list|)
block|{
try|try
block|{
name|DbxDownloader
argument_list|<
name|FileMetadata
argument_list|>
name|listing
init|=
name|client
operator|.
name|files
argument_list|()
operator|.
name|download
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|listing
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"downloading a single file..."
argument_list|)
expr_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|downloadSingleFile
argument_list|(
name|path
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|DbxException
name|dbxException
parameter_list|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|dbxException
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|DbxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|downloadSingleFile (String path)
specifier|private
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|downloadSingleFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|DropboxException
block|{
try|try
block|{
name|OutputStreamBuilder
name|target
init|=
name|OutputStreamBuilder
operator|.
name|withExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|DbxDownloader
argument_list|<
name|FileMetadata
argument_list|>
name|downloadedFile
init|=
name|client
operator|.
name|files
argument_list|()
operator|.
name|download
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|downloadedFile
operator|!=
literal|null
condition|)
block|{
name|downloadedFile
operator|.
name|download
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"downloaded path={}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
operator|new
name|AbstractMap
operator|.
name|SimpleEntry
argument_list|<>
argument_list|(
name|path
argument_list|,
name|target
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|DbxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|path
operator|+
literal|" does not exist or cannot obtain metadata"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|path
operator|+
literal|" cannot obtain a stream"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

