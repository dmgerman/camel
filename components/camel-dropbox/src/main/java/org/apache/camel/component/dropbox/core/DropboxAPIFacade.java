begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteArrayOutputStream
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
name|HashMap
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
name|DbxEntry
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
name|DbxWriteMode
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
name|DropboxResult
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
name|DROPBOX_FILE_SEPARATOR
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
specifier|transient
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
DECL|field|instance
specifier|private
specifier|static
name|DropboxAPIFacade
name|instance
decl_stmt|;
DECL|field|client
specifier|private
specifier|static
name|DbxClient
name|client
decl_stmt|;
DECL|method|DropboxAPIFacade ()
specifier|private
name|DropboxAPIFacade
parameter_list|()
block|{ }
comment|/**      * Return a singleton instance of this class      * @param client the DbxClient performing dropbox low level operations      * @return the singleton instance of this class      */
DECL|method|getInstance (DbxClient client)
specifier|public
specifier|static
name|DropboxAPIFacade
name|getInstance
parameter_list|(
name|DbxClient
name|client
parameter_list|)
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|DropboxAPIFacade
argument_list|()
expr_stmt|;
name|instance
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
comment|/**      * Put or upload a new file or an entire directory to dropbox      * @param localPath  the file path or the dir path on the local filesystem      * @param remotePath the remote path destination on dropbox      * @param mode how a file should be saved on dropbox;      *             in case of "add" the new file will be renamed in case      *             a file with the same name already exists on dropbox.      *             in case of "force" the file already existing with the same name will be overridden.      * @return a DropboxResult object reporting for each remote path the result of the operation.      * @throws DropboxException      */
DECL|method|put (String localPath, String remotePath, DropboxUploadMode mode)
specifier|public
name|DropboxResult
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
name|DropboxResult
name|result
init|=
operator|new
name|DropboxFileUploadResult
argument_list|()
decl_stmt|;
comment|//a map representing for each path the result of the put operation
name|Map
argument_list|<
name|String
argument_list|,
name|DropboxResultCode
argument_list|>
name|resultEntries
init|=
literal|null
decl_stmt|;
comment|//in case the remote path is not specified, the remotePath = localPath
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
name|DbxEntry
name|entry
init|=
literal|null
decl_stmt|;
try|try
block|{
name|entry
operator|=
name|instance
operator|.
name|client
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
throw|throw
operator|new
name|DropboxException
argument_list|(
name|dropboxPath
operator|+
literal|" does not exist or can't obtain metadata"
argument_list|)
throw|;
block|}
name|File
name|fileLocalPath
init|=
operator|new
name|File
argument_list|(
name|localPath
argument_list|)
decl_stmt|;
comment|//verify uploading of a single file
if|if
condition|(
name|fileLocalPath
operator|.
name|isFile
argument_list|()
condition|)
block|{
comment|//check if dropbox file exists
if|if
condition|(
name|entry
operator|!=
literal|null
operator|&&
operator|!
name|entry
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
name|dropboxPath
operator|+
literal|" exists on dropbox and is not a file!"
argument_list|)
throw|;
block|}
comment|//in case the entry not exists on dropbox check if the filename should be appended
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|dropboxPath
operator|.
name|endsWith
argument_list|(
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
name|resultEntries
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DropboxResultCode
argument_list|>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|DbxEntry
operator|.
name|File
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
name|resultEntries
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
name|resultEntries
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
name|resultEntries
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
finally|finally
block|{
name|result
operator|.
name|setResultEntries
argument_list|(
name|resultEntries
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
else|else
block|{
comment|//verify uploading of a list of files inside a dir
name|LOG
operator|.
name|info
argument_list|(
literal|"uploading a dir..."
argument_list|)
expr_stmt|;
comment|//check if dropbox folder exists
if|if
condition|(
name|entry
operator|!=
literal|null
operator|&&
operator|!
name|entry
operator|.
name|isFolder
argument_list|()
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
name|DROPBOX_FILE_SEPARATOR
argument_list|)
condition|)
block|{
name|dropboxPath
operator|=
name|dropboxPath
operator|+
name|DROPBOX_FILE_SEPARATOR
expr_stmt|;
block|}
comment|//revert to old path
name|String
name|oldDropboxPath
init|=
name|dropboxPath
decl_stmt|;
comment|//list all files in a dir
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
operator|==
literal|null
operator|||
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
literal|" doesn't contain any files"
argument_list|)
throw|;
block|}
name|resultEntries
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DropboxResultCode
argument_list|>
argument_list|(
name|listFiles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
name|info
argument_list|(
literal|"uploading:"
operator|+
name|fileLocalPath
operator|+
literal|","
operator|+
name|dropboxPath
argument_list|)
expr_stmt|;
name|DbxEntry
operator|.
name|File
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
name|resultEntries
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
name|resultEntries
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
name|resultEntries
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
name|result
operator|.
name|setResultEntries
argument_list|(
name|resultEntries
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
DECL|method|putSingleFile (File inputFile, String dropboxPath, DropboxUploadMode mode)
specifier|private
name|DbxEntry
operator|.
name|File
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
name|DbxEntry
operator|.
name|File
name|uploadedFile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DbxWriteMode
name|uploadMode
init|=
literal|null
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
name|DbxWriteMode
operator|.
name|force
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|uploadMode
operator|=
name|DbxWriteMode
operator|.
name|add
argument_list|()
expr_stmt|;
block|}
name|uploadedFile
operator|=
name|instance
operator|.
name|client
operator|.
name|uploadFile
argument_list|(
name|dropboxPath
argument_list|,
name|uploadMode
argument_list|,
name|inputFile
operator|.
name|length
argument_list|()
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
return|return
name|uploadedFile
return|;
block|}
finally|finally
block|{
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Search inside a remote path including its sub directories.      * The query param can be null.      * @param remotePath  the remote path where starting the search from      * @param query a space-separated list of substrings to search for. A file matches only if it contains all the substrings      * @return a DropboxResult object containing all the files found.      * @throws DropboxException      */
DECL|method|search (String remotePath, String query)
specifier|public
name|DropboxResult
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
name|DropboxResult
name|result
init|=
operator|new
name|DropboxSearchResult
argument_list|()
decl_stmt|;
name|DbxEntry
operator|.
name|WithChildren
name|listing
init|=
literal|null
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
name|info
argument_list|(
literal|"search no query"
argument_list|)
expr_stmt|;
try|try
block|{
name|listing
operator|=
name|instance
operator|.
name|client
operator|.
name|getMetadataWithChildren
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
literal|" does not exist or can't obtain metadata"
argument_list|)
throw|;
block|}
name|result
operator|.
name|setResultEntries
argument_list|(
name|listing
operator|.
name|children
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"search by query:"
operator|+
name|query
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DbxEntry
argument_list|>
name|entries
init|=
literal|null
decl_stmt|;
try|try
block|{
name|entries
operator|=
name|instance
operator|.
name|client
operator|.
name|searchFileAndFolderNames
argument_list|(
name|remotePath
argument_list|,
name|query
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
literal|" does not exist or can't obtain metadata"
argument_list|)
throw|;
block|}
name|result
operator|.
name|setResultEntries
argument_list|(
name|entries
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Delete every files and subdirectories inside the remote directory.      * In case the remotePath is a file, delete the file.      * @param remotePath  the remote location to delete      * @return a DropboxResult object with the result of the delete operation.      * @throws DropboxException      */
DECL|method|del (String remotePath)
specifier|public
name|DropboxResult
name|del
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
name|DropboxResult
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|instance
operator|.
name|client
operator|.
name|delete
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
literal|" does not exist or can't obtain metadata"
argument_list|)
throw|;
block|}
name|result
operator|=
operator|new
name|DropboxDelResult
argument_list|()
expr_stmt|;
name|result
operator|.
name|setResultEntries
argument_list|(
name|remotePath
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Rename a remote path with the new path location.      * @param remotePath the existing remote path to be renamed      * @param newRemotePath the new remote path substituting the old one      * @return a DropboxResult object with the result of the move operation.      * @throws DropboxException      */
DECL|method|move (String remotePath, String newRemotePath)
specifier|public
name|DropboxResult
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
name|DropboxResult
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|instance
operator|.
name|client
operator|.
name|move
argument_list|(
name|remotePath
argument_list|,
name|newRemotePath
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
literal|" does not exist or can't obtain metadata"
argument_list|)
throw|;
block|}
name|result
operator|=
operator|new
name|DropboxMoveResult
argument_list|()
expr_stmt|;
name|result
operator|.
name|setResultEntries
argument_list|(
name|remotePath
operator|+
literal|"-"
operator|+
name|newRemotePath
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Get the content of every file inside the remote path.      * @param remotePath the remote path where to download from      * @return a DropboxResult object with the content (ByteArrayOutputStream) of every files inside the remote path.      * @throws DropboxException      */
DECL|method|get (String remotePath)
specifier|public
name|DropboxResult
name|get
parameter_list|(
name|String
name|remotePath
parameter_list|)
throws|throws
name|DropboxException
block|{
name|DropboxResult
name|result
init|=
operator|new
name|DropboxFileDownloadResult
argument_list|()
decl_stmt|;
comment|//a map representing for each path the result of the baos
name|Map
argument_list|<
name|String
argument_list|,
name|ByteArrayOutputStream
argument_list|>
name|resultEntries
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ByteArrayOutputStream
argument_list|>
argument_list|()
decl_stmt|;
comment|//iterate from the remotePath
name|downloadFilesInFolder
argument_list|(
name|remotePath
argument_list|,
name|resultEntries
argument_list|)
expr_stmt|;
comment|//put the map of baos as result
name|result
operator|.
name|setResultEntries
argument_list|(
name|resultEntries
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|downloadFilesInFolder (String path, Map<String, ByteArrayOutputStream> resultEntries)
specifier|private
name|void
name|downloadFilesInFolder
parameter_list|(
name|String
name|path
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ByteArrayOutputStream
argument_list|>
name|resultEntries
parameter_list|)
throws|throws
name|DropboxException
block|{
name|DbxEntry
operator|.
name|WithChildren
name|listing
init|=
literal|null
decl_stmt|;
try|try
block|{
name|listing
operator|=
name|instance
operator|.
name|client
operator|.
name|getMetadataWithChildren
argument_list|(
name|path
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
name|path
operator|+
literal|" does not exist or can't obtain metadata"
argument_list|)
throw|;
block|}
if|if
condition|(
name|listing
operator|.
name|children
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"downloading a single file..."
argument_list|)
expr_stmt|;
name|downloadSingleFile
argument_list|(
name|path
argument_list|,
name|resultEntries
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|DbxEntry
name|entry
range|:
name|listing
operator|.
name|children
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|isFile
argument_list|()
condition|)
block|{
try|try
block|{
comment|//get the baos of the file
name|downloadSingleFile
argument_list|(
name|entry
operator|.
name|path
argument_list|,
name|resultEntries
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DropboxException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"can't download from "
operator|+
name|entry
operator|.
name|path
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//iterate on folder
name|downloadFilesInFolder
argument_list|(
name|entry
operator|.
name|path
argument_list|,
name|resultEntries
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|downloadSingleFile (String path, Map<String, ByteArrayOutputStream> resultEntries)
specifier|private
name|void
name|downloadSingleFile
parameter_list|(
name|String
name|path
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ByteArrayOutputStream
argument_list|>
name|resultEntries
parameter_list|)
throws|throws
name|DropboxException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DbxEntry
operator|.
name|File
name|downloadedFile
decl_stmt|;
try|try
block|{
name|downloadedFile
operator|=
name|instance
operator|.
name|client
operator|.
name|getFile
argument_list|(
name|path
argument_list|,
literal|null
argument_list|,
name|baos
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
name|path
operator|+
literal|" does not exist or can't obtain metadata"
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
literal|" can't obtain a stream"
argument_list|)
throw|;
block|}
if|if
condition|(
name|downloadedFile
operator|!=
literal|null
condition|)
block|{
name|resultEntries
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"downloaded path:"
operator|+
name|path
operator|+
literal|" - baos size:"
operator|+
name|baos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

