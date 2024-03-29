begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Date
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
name|Message
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
name|Processor
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|file
operator|.
name|GenericFile
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
name|file
operator|.
name|GenericFileOperationFailedException
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
name|file
operator|.
name|GenericFileProcessStrategy
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
name|FileUtil
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
name|StopWatch
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
name|StringHelper
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
name|TimeUtils
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
name|URISupport
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
name|net
operator|.
name|ftp
operator|.
name|FTPClient
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
name|net
operator|.
name|ftp
operator|.
name|FTPFile
import|;
end_import

begin_comment
comment|/**  * FTP consumer  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed FtpConsumer"
argument_list|)
DECL|class|FtpConsumer
specifier|public
class|class
name|FtpConsumer
extends|extends
name|RemoteFileConsumer
argument_list|<
name|FTPFile
argument_list|>
block|{
DECL|field|endpointPath
specifier|protected
name|String
name|endpointPath
decl_stmt|;
DECL|field|ftpConsumerToString
specifier|private
specifier|transient
name|String
name|ftpConsumerToString
decl_stmt|;
DECL|method|FtpConsumer (RemoteFileEndpoint<FTPFile> endpoint, Processor processor, RemoteFileOperations<FTPFile> fileOperations, GenericFileProcessStrategy processStrategy)
specifier|public
name|FtpConsumer
parameter_list|(
name|RemoteFileEndpoint
argument_list|<
name|FTPFile
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|RemoteFileOperations
argument_list|<
name|FTPFile
argument_list|>
name|fileOperations
parameter_list|,
name|GenericFileProcessStrategy
name|processStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|fileOperations
argument_list|,
name|processStrategy
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpointPath
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOperations ()
specifier|protected
name|FtpOperations
name|getOperations
parameter_list|()
block|{
return|return
operator|(
name|FtpOperations
operator|)
name|super
operator|.
name|getOperations
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// turn off scheduler first, so autoCreate is handled before scheduler starts
name|boolean
name|startScheduler
init|=
name|isStartScheduler
argument_list|()
decl_stmt|;
name|setStartScheduler
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isAutoCreate
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Auto creating directory: {}"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|connectIfNecessary
argument_list|()
expr_stmt|;
name|operations
operator|.
name|buildDirectory
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GenericFileOperationFailedException
name|e
parameter_list|)
block|{
comment|// log a WARN as we want to start the consumer.
name|log
operator|.
name|warn
argument_list|(
literal|"Error auto creating directory: "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|startScheduler
condition|)
block|{
name|setStartScheduler
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|startScheduler
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|pollDirectory (String fileName, List<GenericFile<FTPFile>> fileList, int depth)
specifier|protected
name|boolean
name|pollDirectory
parameter_list|(
name|String
name|fileName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
argument_list|>
name|fileList
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|String
name|currentDir
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isStepwise
argument_list|()
condition|)
block|{
comment|// must remember current dir so we stay in that directory after the poll
name|currentDir
operator|=
name|operations
operator|.
name|getCurrentDirectory
argument_list|()
expr_stmt|;
block|}
comment|// strip trailing slash
name|fileName
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|boolean
name|answer
init|=
name|doPollDirectory
argument_list|(
name|fileName
argument_list|,
literal|null
argument_list|,
name|fileList
argument_list|,
name|depth
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentDir
operator|!=
literal|null
condition|)
block|{
name|operations
operator|.
name|changeCurrentDirectory
argument_list|(
name|currentDir
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|pollSubDirectory (String absolutePath, String dirName, List<GenericFile<FTPFile>> fileList, int depth)
specifier|protected
name|boolean
name|pollSubDirectory
parameter_list|(
name|String
name|absolutePath
parameter_list|,
name|String
name|dirName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
argument_list|>
name|fileList
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|boolean
name|answer
init|=
name|doSafePollSubDirectory
argument_list|(
name|absolutePath
argument_list|,
name|dirName
argument_list|,
name|fileList
argument_list|,
name|depth
argument_list|)
decl_stmt|;
comment|// change back to parent directory when finished polling sub directory
if|if
condition|(
name|isStepwise
argument_list|()
condition|)
block|{
name|operations
operator|.
name|changeToParentDirectory
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|doPollDirectory (String absolutePath, String dirName, List<GenericFile<FTPFile>> fileList, int depth)
specifier|protected
name|boolean
name|doPollDirectory
parameter_list|(
name|String
name|absolutePath
parameter_list|,
name|String
name|dirName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
argument_list|>
name|fileList
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"doPollDirectory from absolutePath: {}, dirName: {}"
argument_list|,
name|absolutePath
argument_list|,
name|dirName
argument_list|)
expr_stmt|;
name|depth
operator|++
expr_stmt|;
comment|// remove trailing /
name|dirName
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|dirName
argument_list|)
expr_stmt|;
comment|// compute dir depending on stepwise is enabled or not
name|String
name|dir
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|FTPFile
argument_list|>
name|files
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|isStepwise
argument_list|()
condition|)
block|{
name|dir
operator|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|dirName
argument_list|)
condition|?
name|dirName
else|:
name|absolutePath
expr_stmt|;
name|operations
operator|.
name|changeCurrentDirectory
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dir
operator|=
name|absolutePath
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Polling directory: {}"
argument_list|,
name|dir
argument_list|)
expr_stmt|;
if|if
condition|(
name|isUseList
argument_list|()
condition|)
block|{
if|if
condition|(
name|isStepwise
argument_list|()
condition|)
block|{
name|files
operator|=
name|operations
operator|.
name|listFiles
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|files
operator|=
name|operations
operator|.
name|listFiles
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// we cannot use the LIST command(s) so we can only poll a named file
comment|// so created a pseudo file with that name
name|FTPFile
name|file
init|=
operator|new
name|FTPFile
argument_list|()
decl_stmt|;
name|file
operator|.
name|setType
argument_list|(
name|FTPFile
operator|.
name|FILE_TYPE
argument_list|)
expr_stmt|;
name|fileExpressionResult
operator|=
name|evaluateFileExpression
argument_list|()
expr_stmt|;
if|if
condition|(
name|fileExpressionResult
operator|!=
literal|null
condition|)
block|{
name|file
operator|.
name|setName
argument_list|(
name|fileExpressionResult
argument_list|)
expr_stmt|;
name|files
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|files
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|GenericFileOperationFailedException
name|e
parameter_list|)
block|{
if|if
condition|(
name|ignoreCannotRetrieveFile
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot list files in directory {} due directory does not exists or file permission error."
argument_list|,
name|dir
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
if|if
condition|(
name|files
operator|==
literal|null
operator|||
name|files
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// no files in this directory to poll
name|log
operator|.
name|trace
argument_list|(
literal|"No files found in directory: {}"
argument_list|,
name|dir
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
comment|// we found some files
name|log
operator|.
name|trace
argument_list|(
literal|"Found {} in directory: {}"
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|,
name|dir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isPreSort
argument_list|()
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|files
argument_list|,
parameter_list|(
name|a
parameter_list|,
name|b
parameter_list|)
lambda|->
name|a
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|FTPFile
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"FtpFile[name={}, dir={}, file={}]"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|file
operator|.
name|isDirectory
argument_list|()
argument_list|,
name|file
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// check if we can continue polling in files
if|if
condition|(
operator|!
name|canPollMoreFiles
argument_list|(
name|fileList
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|RemoteFile
argument_list|<
name|FTPFile
argument_list|>
name|remote
init|=
name|asRemoteFile
argument_list|(
name|absolutePath
argument_list|,
name|file
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCharset
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isRecursive
argument_list|()
operator|&&
name|depth
operator|<
name|endpoint
operator|.
name|getMaxDepth
argument_list|()
operator|&&
name|isValidFile
argument_list|(
name|remote
argument_list|,
literal|true
argument_list|,
name|files
argument_list|)
condition|)
block|{
comment|// recursive scan and add the sub files and folders
name|String
name|subDirectory
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|absolutePath
argument_list|)
condition|?
name|absolutePath
operator|+
literal|"/"
operator|+
name|subDirectory
else|:
name|subDirectory
decl_stmt|;
name|boolean
name|canPollMore
init|=
name|pollSubDirectory
argument_list|(
name|path
argument_list|,
name|subDirectory
argument_list|,
name|fileList
argument_list|,
name|depth
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|canPollMore
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|RemoteFile
argument_list|<
name|FTPFile
argument_list|>
name|remote
init|=
name|asRemoteFile
argument_list|(
name|absolutePath
argument_list|,
name|file
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCharset
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|depth
operator|>=
name|endpoint
operator|.
name|getMinDepth
argument_list|()
operator|&&
name|isValidFile
argument_list|(
name|remote
argument_list|,
literal|false
argument_list|,
name|files
argument_list|)
condition|)
block|{
comment|// matched file so add
name|fileList
operator|.
name|add
argument_list|(
name|remote
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Ignoring unsupported remote file type: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isMatched (GenericFile<FTPFile> file, String doneFileName, List<FTPFile> files)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
name|file
parameter_list|,
name|String
name|doneFileName
parameter_list|,
name|List
argument_list|<
name|FTPFile
argument_list|>
name|files
parameter_list|)
block|{
name|String
name|onlyName
init|=
name|FileUtil
operator|.
name|stripPath
argument_list|(
name|doneFileName
argument_list|)
decl_stmt|;
for|for
control|(
name|FTPFile
name|f
range|:
name|files
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|onlyName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Done file: {} does not exist"
argument_list|,
name|doneFileName
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|ignoreCannotRetrieveFile (String name, Exchange exchange, Exception cause)
specifier|protected
name|boolean
name|ignoreCannotRetrieveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Exception
name|cause
parameter_list|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isIgnoreFileNotFoundOrPermissionError
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|// error code 550 is file not found
name|int
name|code
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FtpConstants
operator|.
name|FTP_REPLY_CODE
argument_list|,
literal|0
argument_list|,
name|int
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|code
operator|==
literal|550
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
name|cause
operator|instanceof
name|GenericFileOperationFailedException
condition|)
block|{
name|GenericFileOperationFailedException
name|generic
init|=
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|GenericFileOperationFailedException
operator|.
name|class
argument_list|,
name|cause
argument_list|)
decl_stmt|;
comment|//exchange is null and cause has the reason for failure to read directories
if|if
condition|(
name|generic
operator|.
name|getCode
argument_list|()
operator|==
literal|550
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
name|super
operator|.
name|ignoreCannotRetrieveFile
argument_list|(
name|name
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
return|;
block|}
DECL|method|asRemoteFile (String absolutePath, FTPFile file, String charset)
specifier|private
name|RemoteFile
argument_list|<
name|FTPFile
argument_list|>
name|asRemoteFile
parameter_list|(
name|String
name|absolutePath
parameter_list|,
name|FTPFile
name|file
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|RemoteFile
argument_list|<
name|FTPFile
argument_list|>
name|answer
init|=
operator|new
name|RemoteFile
argument_list|<>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setCharset
argument_list|(
name|charset
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setEndpointPath
argument_list|(
name|endpointPath
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setFileNameOnly
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setFileLength
argument_list|(
name|file
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setDirectory
argument_list|(
name|file
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|file
operator|.
name|getTimestamp
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLastModified
argument_list|(
name|file
operator|.
name|getTimestamp
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setHostname
argument_list|(
operator|(
operator|(
name|RemoteFileConfiguration
operator|)
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|)
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
comment|// absolute or relative path
name|boolean
name|absolute
init|=
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
name|absolutePath
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setAbsolute
argument_list|(
name|absolute
argument_list|)
expr_stmt|;
comment|// create a pseudo absolute name
name|String
name|dir
init|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|absolutePath
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
operator|(
name|FtpConfiguration
operator|)
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|)
operator|.
name|isHandleDirectoryParserAbsoluteResult
argument_list|()
condition|)
block|{
name|fileName
operator|=
name|FtpUtils
operator|.
name|extractDirNameFromAbsolutePath
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|absoluteFileName
init|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|dir
operator|+
literal|"/"
operator|+
name|fileName
argument_list|)
decl_stmt|;
comment|// if absolute start with a leading separator otherwise let it be relative
if|if
condition|(
name|absolute
condition|)
block|{
name|absoluteFileName
operator|=
literal|"/"
operator|+
name|absoluteFileName
expr_stmt|;
block|}
name|answer
operator|.
name|setAbsoluteFilePath
argument_list|(
name|absoluteFileName
argument_list|)
expr_stmt|;
comment|// the relative filename, skip the leading endpoint configured path
name|String
name|relativePath
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|absoluteFileName
argument_list|,
name|endpointPath
argument_list|)
decl_stmt|;
comment|// skip leading /
name|relativePath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|relativePath
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setRelativeFilePath
argument_list|(
name|relativePath
argument_list|)
expr_stmt|;
comment|// the file name should be the relative path
name|answer
operator|.
name|setFileName
argument_list|(
name|answer
operator|.
name|getRelativeFilePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|updateFileHeaders (GenericFile<FTPFile> file, Message message)
specifier|protected
name|void
name|updateFileHeaders
parameter_list|(
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
name|file
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|long
name|length
init|=
name|file
operator|.
name|getFile
argument_list|()
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|long
name|modified
init|=
name|file
operator|.
name|getFile
argument_list|()
operator|.
name|getTimestamp
argument_list|()
operator|!=
literal|null
condition|?
name|file
operator|.
name|getFile
argument_list|()
operator|.
name|getTimestamp
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
else|:
operator|-
literal|1
decl_stmt|;
name|file
operator|.
name|setFileLength
argument_list|(
name|length
argument_list|)
expr_stmt|;
name|file
operator|.
name|setLastModified
argument_list|(
name|modified
argument_list|)
expr_stmt|;
if|if
condition|(
name|length
operator|>=
literal|0
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LENGTH
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|modified
operator|>=
literal|0
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LAST_MODIFIED
argument_list|,
name|modified
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isStepwise ()
specifier|private
name|boolean
name|isStepwise
parameter_list|()
block|{
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
return|return
name|config
operator|.
name|isStepwise
argument_list|()
return|;
block|}
DECL|method|isUseList ()
specifier|private
name|boolean
name|isUseList
parameter_list|()
block|{
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
return|return
name|config
operator|.
name|isUseList
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Summary of last FTP activity (download only)"
argument_list|)
DECL|method|getLastFtpActivity ()
specifier|public
name|String
name|getLastFtpActivity
parameter_list|()
block|{
name|FTPClient
name|client
init|=
name|getOperations
argument_list|()
operator|.
name|getFtpClient
argument_list|()
decl_stmt|;
name|FtpClientActivityListener
name|listener
init|=
operator|(
name|FtpClientActivityListener
operator|)
name|client
operator|.
name|getCopyStreamListener
argument_list|()
decl_stmt|;
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|String
name|log
init|=
name|listener
operator|.
name|getLastLogActivity
argument_list|()
decl_stmt|;
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|long
name|since
init|=
name|listener
operator|.
name|getLastLogActivityTimestamp
argument_list|()
decl_stmt|;
if|if
condition|(
name|since
operator|>
literal|0
condition|)
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|(
operator|new
name|Date
argument_list|(
name|since
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|delta
init|=
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|String
name|human
init|=
name|TimeUtils
operator|.
name|printDuration
argument_list|(
name|delta
argument_list|)
decl_stmt|;
return|return
name|log
operator|+
literal|" "
operator|+
name|human
operator|+
literal|" ago"
return|;
block|}
else|else
block|{
return|return
name|log
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Summary of last FTP activity (all)"
argument_list|)
DECL|method|getLastFtpActivityVerbose ()
specifier|public
name|String
name|getLastFtpActivityVerbose
parameter_list|()
block|{
name|FTPClient
name|client
init|=
name|getOperations
argument_list|()
operator|.
name|getFtpClient
argument_list|()
decl_stmt|;
name|FtpClientActivityListener
name|listener
init|=
operator|(
name|FtpClientActivityListener
operator|)
name|client
operator|.
name|getCopyStreamListener
argument_list|()
decl_stmt|;
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|String
name|log
init|=
name|listener
operator|.
name|getLastVerboseLogActivity
argument_list|()
decl_stmt|;
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|long
name|since
init|=
name|listener
operator|.
name|getLastVerboseLogActivityTimestamp
argument_list|()
decl_stmt|;
if|if
condition|(
name|since
operator|>
literal|0
condition|)
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|(
operator|new
name|Date
argument_list|(
name|since
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|delta
init|=
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|String
name|human
init|=
name|TimeUtils
operator|.
name|printDuration
argument_list|(
name|delta
argument_list|)
decl_stmt|;
return|return
name|log
operator|+
literal|" "
operator|+
name|human
operator|+
literal|" ago"
return|;
block|}
else|else
block|{
return|return
name|log
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|ftpConsumerToString
operator|==
literal|null
condition|)
block|{
name|ftpConsumerToString
operator|=
literal|"FtpConsumer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|ftpConsumerToString
return|;
block|}
block|}
end_class

end_unit

