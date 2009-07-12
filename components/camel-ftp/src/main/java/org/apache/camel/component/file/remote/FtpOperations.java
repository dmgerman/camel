begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FileOutputStream
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
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|Arrays
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
name|component
operator|.
name|file
operator|.
name|FileComponent
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
name|GenericFileEndpoint
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
name|GenericFileExist
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
name|commons
operator|.
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
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
comment|/**  * FTP remote file operations  */
end_comment

begin_class
DECL|class|FtpOperations
specifier|public
class|class
name|FtpOperations
implements|implements
name|RemoteFileOperations
argument_list|<
name|FTPFile
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FtpOperations
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
specifier|final
name|FTPClient
name|client
decl_stmt|;
DECL|field|endpoint
specifier|private
name|RemoteFileEndpoint
name|endpoint
decl_stmt|;
DECL|method|FtpOperations ()
specifier|public
name|FtpOperations
parameter_list|()
block|{
name|this
operator|.
name|client
operator|=
operator|new
name|FTPClient
argument_list|()
expr_stmt|;
block|}
DECL|method|FtpOperations (FTPClient client)
specifier|public
name|FtpOperations
parameter_list|(
name|FTPClient
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
DECL|method|setEndpoint (GenericFileEndpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|GenericFileEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
operator|(
name|RemoteFileEndpoint
operator|)
name|endpoint
expr_stmt|;
block|}
DECL|method|connect (RemoteFileConfiguration configuration)
specifier|public
name|boolean
name|connect
parameter_list|(
name|RemoteFileConfiguration
name|configuration
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Connecting using FTPClient: "
operator|+
name|client
argument_list|)
expr_stmt|;
block|}
name|String
name|host
init|=
name|configuration
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|configuration
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|String
name|username
init|=
name|configuration
operator|.
name|getUsername
argument_list|()
decl_stmt|;
name|FtpConfiguration
name|ftpConfig
init|=
operator|(
name|FtpConfiguration
operator|)
name|configuration
decl_stmt|;
if|if
condition|(
name|ftpConfig
operator|.
name|getFtpClientConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Configuring FTPClient with config: "
operator|+
name|ftpConfig
operator|.
name|getFtpClientConfig
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|configure
argument_list|(
name|ftpConfig
operator|.
name|getFtpClientConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Connecting to "
operator|+
name|configuration
operator|.
name|remoteServerInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|connected
init|=
literal|false
decl_stmt|;
name|int
name|attempt
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|connected
condition|)
block|{
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
operator|&&
name|attempt
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Reconnect attempt #"
operator|+
name|attempt
operator|+
literal|" connecting to + "
operator|+
name|configuration
operator|.
name|remoteServerInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|connect
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
comment|// yes we could connect
name|connected
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|GenericFileOperationFailedException
name|failed
init|=
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Could not connect due: "
operator|+
name|failed
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|attempt
operator|++
expr_stmt|;
if|if
condition|(
name|attempt
operator|>
name|endpoint
operator|.
name|getMaximumReconnectAttempts
argument_list|()
condition|)
block|{
throw|throw
name|failed
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getReconnectDelay
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|endpoint
operator|.
name|getReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e1
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
comment|// must enter passive mode directly after connect
if|if
condition|(
name|configuration
operator|.
name|isPassiveMode
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using passive mode connections"
argument_list|)
expr_stmt|;
name|client
operator|.
name|enterLocalPassiveMode
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|boolean
name|login
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Attempting to login user: "
operator|+
name|username
operator|+
literal|" using password: "
operator|+
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|login
operator|=
name|client
operator|.
name|login
argument_list|(
name|username
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Attempting to login anonymous"
argument_list|)
expr_stmt|;
name|login
operator|=
name|client
operator|.
name|login
argument_list|(
literal|"anonymous"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"User "
operator|+
operator|(
name|username
operator|!=
literal|null
condition|?
name|username
else|:
literal|"anonymous"
operator|)
operator|+
literal|" logged in: "
operator|+
name|login
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|login
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|)
throw|;
block|}
name|client
operator|.
name|setFileType
argument_list|(
name|configuration
operator|.
name|isBinary
argument_list|()
condition|?
name|FTPClient
operator|.
name|BINARY_FILE_TYPE
else|:
name|FTPClient
operator|.
name|ASCII_FILE_TYPE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|isConnected ()
specifier|public
name|boolean
name|isConnected
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
return|return
name|client
operator|.
name|isConnected
argument_list|()
return|;
block|}
DECL|method|disconnect ()
specifier|public
name|void
name|disconnect
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
try|try
block|{
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|deleteFile (String name)
specifier|public
name|boolean
name|deleteFile
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleteing file: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|this
operator|.
name|client
operator|.
name|deleteFile
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|renameFile (String from, String to)
specifier|public
name|boolean
name|renameFile
parameter_list|(
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Renaming file: "
operator|+
name|from
operator|+
literal|" to: "
operator|+
name|to
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|client
operator|.
name|rename
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|buildDirectory (String directory, boolean absolute)
specifier|public
name|boolean
name|buildDirectory
parameter_list|(
name|String
name|directory
parameter_list|,
name|boolean
name|absolute
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Building directory: "
operator|+
name|directory
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|String
name|originalDirectory
init|=
name|client
operator|.
name|printWorkingDirectory
argument_list|()
decl_stmt|;
name|boolean
name|success
decl_stmt|;
try|try
block|{
comment|// maybe the full directory already exsits
name|success
operator|=
name|client
operator|.
name|changeWorkingDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to build remote directory: "
operator|+
name|directory
argument_list|)
expr_stmt|;
block|}
name|success
operator|=
name|client
operator|.
name|makeDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
comment|// we are here if the server side doesn't create intermediate folders so create the folder one by one
name|success
operator|=
name|buildDirectoryChunks
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|success
return|;
block|}
finally|finally
block|{
comment|// change back to original directory
if|if
condition|(
name|originalDirectory
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|changeWorkingDirectory
argument_list|(
name|originalDirectory
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|retrieveFile (String name, Exchange exchange)
specifier|public
name|boolean
name|retrieveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getLocalWorkDirectory
argument_list|()
argument_list|)
condition|)
block|{
comment|// local work directory is configured so we should store file content as files in this local directory
return|return
name|retrieveFileToFileInLocalWorkDirectory
argument_list|(
name|name
argument_list|,
name|exchange
argument_list|)
return|;
block|}
else|else
block|{
comment|// store file content directory as stream on the body
return|return
name|retrieveFileToStreamInBody
argument_list|(
name|name
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
DECL|method|retrieveFileToStreamInBody (String name, Exchange exchange)
specifier|private
name|boolean
name|retrieveFileToStreamInBody
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
name|OutputStream
name|os
init|=
literal|null
decl_stmt|;
try|try
block|{
name|os
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
name|target
init|=
operator|(
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"Exchange should have the "
operator|+
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
operator|+
literal|" set"
argument_list|)
expr_stmt|;
name|target
operator|.
name|setBody
argument_list|(
name|os
argument_list|)
expr_stmt|;
return|return
name|client
operator|.
name|retrieveFile
argument_list|(
name|name
argument_list|,
name|os
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|os
argument_list|,
literal|"retrieve: "
operator|+
name|name
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|retrieveFileToFileInLocalWorkDirectory (String name, Exchange exchange)
specifier|private
name|boolean
name|retrieveFileToFileInLocalWorkDirectory
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
name|File
name|temp
decl_stmt|;
name|File
name|local
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|endpoint
operator|.
name|getLocalWorkDirectory
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|OutputStream
name|os
decl_stmt|;
try|try
block|{
comment|// use relative filename in local work directory
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
name|target
init|=
operator|(
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"Exchange should have the "
operator|+
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
operator|+
literal|" set"
argument_list|)
expr_stmt|;
name|String
name|relativeName
init|=
name|target
operator|.
name|getRelativeFilePath
argument_list|()
decl_stmt|;
name|temp
operator|=
operator|new
name|File
argument_list|(
name|local
argument_list|,
name|relativeName
operator|+
literal|".inprogress"
argument_list|)
expr_stmt|;
name|local
operator|=
operator|new
name|File
argument_list|(
name|local
argument_list|,
name|relativeName
argument_list|)
expr_stmt|;
comment|// create directory to local work file
name|local
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
comment|// delete any existing files
if|if
condition|(
name|temp
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|temp
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot delete existing local work file: "
operator|+
name|temp
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|local
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|local
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot delete existing local work file: "
operator|+
name|local
argument_list|)
throw|;
block|}
block|}
comment|// create new temp local work file
if|if
condition|(
operator|!
name|temp
operator|.
name|createNewFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot create new local work file: "
operator|+
name|temp
argument_list|)
throw|;
block|}
comment|// store content as a file in the local work directory in the temp handle
name|os
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|temp
argument_list|)
expr_stmt|;
comment|// set header with the path to the local work file
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LOCAL_WORK_PATH
argument_list|,
name|local
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot create new local work file: "
operator|+
name|local
argument_list|)
throw|;
block|}
name|boolean
name|result
decl_stmt|;
try|try
block|{
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
name|target
init|=
operator|(
name|GenericFile
argument_list|<
name|FTPFile
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
decl_stmt|;
comment|// store the java.io.File handle as the body
name|target
operator|.
name|setBody
argument_list|(
name|local
argument_list|)
expr_stmt|;
name|result
operator|=
name|client
operator|.
name|retrieveFile
argument_list|(
name|name
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
comment|// need to close the stream before rename it
name|ObjectHelper
operator|.
name|close
argument_list|(
name|os
argument_list|,
literal|"retrieve: "
operator|+
name|name
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
comment|// rename temp to local after we have retrieved the data
if|if
condition|(
operator|!
name|temp
operator|.
name|renameTo
argument_list|(
name|local
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot rename local work file from: "
operator|+
name|temp
operator|+
literal|" to: "
operator|+
name|local
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
DECL|method|storeFile (String name, Exchange exchange)
specifier|public
name|boolean
name|storeFile
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// if an existing file already exists what should we do?
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Ignore
operator|||
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Fail
condition|)
block|{
name|boolean
name|existFile
init|=
name|existsFile
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|existFile
operator|&&
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Ignore
condition|)
block|{
comment|// ignore but indicate that the file was written
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"An existing file already exists: "
operator|+
name|name
operator|+
literal|". Ignore and do not override it."
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|existFile
operator|&&
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Fail
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"File already exist: "
operator|+
name|name
operator|+
literal|". Cannot write new file."
argument_list|)
throw|;
block|}
block|}
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Append
condition|)
block|{
return|return
name|client
operator|.
name|appendFile
argument_list|(
name|name
argument_list|,
name|is
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|client
operator|.
name|storeFile
argument_list|(
name|name
argument_list|,
name|is
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
literal|"store: "
operator|+
name|name
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|existsFile (String name)
specifier|public
name|boolean
name|existsFile
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// check whether a file already exists
name|String
name|directory
init|=
name|FileUtil
operator|.
name|onlyPath
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|directory
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|onlyName
init|=
name|FileUtil
operator|.
name|stripPath
argument_list|(
name|name
argument_list|)
decl_stmt|;
try|try
block|{
name|String
index|[]
name|names
init|=
name|client
operator|.
name|listNames
argument_list|(
name|directory
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|existing
range|:
name|names
control|)
block|{
if|if
condition|(
name|existing
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
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getCurrentDirectory ()
specifier|public
name|String
name|getCurrentDirectory
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
try|try
block|{
return|return
name|client
operator|.
name|printWorkingDirectory
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|changeCurrentDirectory (String newDirectory)
specifier|public
name|void
name|changeCurrentDirectory
parameter_list|(
name|String
name|newDirectory
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
try|try
block|{
name|client
operator|.
name|changeWorkingDirectory
argument_list|(
name|newDirectory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|listFiles ()
specifier|public
name|List
argument_list|<
name|FTPFile
argument_list|>
name|listFiles
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
return|return
name|listFiles
argument_list|(
literal|"."
argument_list|)
return|;
block|}
DECL|method|listFiles (String path)
specifier|public
name|List
argument_list|<
name|FTPFile
argument_list|>
name|listFiles
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// use current directory if path not given
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|path
operator|=
literal|"."
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|List
argument_list|<
name|FTPFile
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|FTPFile
argument_list|>
argument_list|()
decl_stmt|;
name|FTPFile
index|[]
name|files
init|=
name|client
operator|.
name|listFiles
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|list
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|files
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|client
operator|.
name|getReplyCode
argument_list|()
argument_list|,
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|buildDirectoryChunks (String dirName)
specifier|private
name|boolean
name|buildDirectoryChunks
parameter_list|(
name|String
name|dirName
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|dirName
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|dirs
init|=
name|dirName
operator|.
name|split
argument_list|(
literal|"/|\\\\"
argument_list|)
decl_stmt|;
name|boolean
name|success
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|dir
range|:
name|dirs
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|dir
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|String
name|directory
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// do not try to build root / folder
if|if
condition|(
operator|!
name|directory
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to build remote directory by chunk: "
operator|+
name|directory
argument_list|)
expr_stmt|;
block|}
name|success
operator|=
name|client
operator|.
name|makeDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|success
return|;
block|}
block|}
end_class

end_unit

