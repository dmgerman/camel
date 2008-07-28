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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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

begin_class
DECL|class|FtpConsumer
specifier|public
class|class
name|FtpConsumer
extends|extends
name|RemoteFileConsumer
argument_list|<
name|RemoteFileExchange
argument_list|>
block|{
DECL|field|endpoint
specifier|private
name|FtpEndpoint
name|endpoint
decl_stmt|;
DECL|field|client
specifier|private
name|FTPClient
name|client
decl_stmt|;
DECL|method|FtpConsumer (FtpEndpoint endpoint, Processor processor, FTPClient client)
specifier|public
name|FtpConsumer
parameter_list|(
name|FtpEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|FTPClient
name|client
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|FtpConsumer (FtpEndpoint endpoint, Processor processor, FTPClient client, ScheduledExecutorService executor)
specifier|public
name|FtpConsumer
parameter_list|(
name|FtpEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|FTPClient
name|client
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting"
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping"
argument_list|)
expr_stmt|;
comment|// disconnect when stopping
try|try
block|{
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore just log a warning
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occured during disconecting from "
operator|+
name|remoteServer
argument_list|()
operator|+
literal|". "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|connectIfNecessary ()
specifier|protected
name|void
name|connectIfNecessary
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|client
operator|.
name|isConnected
argument_list|()
condition|)
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
literal|"Not connected, connecting to "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|FtpUtils
operator|.
name|connect
argument_list|(
name|client
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Connected to "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|disconnect ()
specifier|protected
name|void
name|disconnect
parameter_list|()
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Disconnecting from "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
name|FtpUtils
operator|.
name|disconnect
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
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
literal|"Polling "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connectIfNecessary
argument_list|()
expr_stmt|;
comment|// If the attempt to connect isn't successful, then the thrown
comment|// exception will signify that we couldn't poll
try|try
block|{
specifier|final
name|String
name|fileName
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|pollDirectory
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|index
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
operator|-
literal|1
condition|)
block|{
comment|// cd to the folder of the filename
name|client
operator|.
name|changeWorkingDirectory
argument_list|(
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// list the files in the fold and poll the first file
specifier|final
name|FTPFile
index|[]
name|files
init|=
name|client
operator|.
name|listFiles
argument_list|(
name|fileName
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|pollFile
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|lastPollTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
condition|)
block|{
comment|// if we are stopping then ignore any exception during a poll
name|LOG
operator|.
name|warn
argument_list|(
literal|"Consumer is stopping. Ignoring caught exception: "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occured during polling: "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|disconnect
argument_list|()
expr_stmt|;
comment|// Rethrow to signify that we didn't poll
throw|throw
name|e
throw|;
block|}
block|}
block|}
DECL|method|pollDirectory (String dir)
specifier|protected
name|void
name|pollDirectory
parameter_list|(
name|String
name|dir
parameter_list|)
throws|throws
name|Exception
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
literal|"Polling directory: "
operator|+
name|dir
argument_list|)
expr_stmt|;
block|}
name|String
name|currentDir
init|=
name|client
operator|.
name|printWorkingDirectory
argument_list|()
decl_stmt|;
name|client
operator|.
name|changeWorkingDirectory
argument_list|(
name|dir
argument_list|)
expr_stmt|;
for|for
control|(
name|FTPFile
name|ftpFile
range|:
name|client
operator|.
name|listFiles
argument_list|()
control|)
block|{
if|if
condition|(
name|ftpFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|pollFile
argument_list|(
name|ftpFile
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ftpFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|isRecursive
argument_list|()
condition|)
block|{
name|pollDirectory
argument_list|(
name|getFullFileName
argument_list|(
name|ftpFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unsupported type of FTPFile: "
operator|+
name|ftpFile
operator|+
literal|" (not a file or directory). Is skipped."
argument_list|)
expr_stmt|;
block|}
block|}
comment|// change back to original current dir
name|client
operator|.
name|changeWorkingDirectory
argument_list|(
name|currentDir
argument_list|)
expr_stmt|;
block|}
DECL|method|getFullFileName (FTPFile ftpFile)
specifier|protected
name|String
name|getFullFileName
parameter_list|(
name|FTPFile
name|ftpFile
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|client
operator|.
name|printWorkingDirectory
argument_list|()
operator|+
literal|"/"
operator|+
name|ftpFile
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|pollFile (FTPFile ftpFile)
specifier|private
name|void
name|pollFile
parameter_list|(
name|FTPFile
name|ftpFile
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ftpFile
operator|==
literal|null
condition|)
block|{
return|return;
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
literal|"Polling file: "
operator|+
name|ftpFile
argument_list|)
expr_stmt|;
block|}
name|long
name|ts
init|=
name|ftpFile
operator|.
name|getTimestamp
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
decl_stmt|;
comment|// TODO do we need to adjust the TZ? can we?
if|if
condition|(
name|ts
operator|>
name|lastPollTime
operator|&&
name|isMatched
argument_list|(
name|ftpFile
argument_list|)
condition|)
block|{
name|String
name|fullFileName
init|=
name|getFullFileName
argument_list|(
name|ftpFile
argument_list|)
decl_stmt|;
comment|// is we use excluse read then acquire the exclusive read (waiting until we got it)
if|if
condition|(
name|exclusiveRead
condition|)
block|{
name|acquireExclusiveRead
argument_list|(
name|client
argument_list|,
name|ftpFile
argument_list|)
expr_stmt|;
block|}
comment|// retrieve the file
specifier|final
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|client
operator|.
name|retrieveFile
argument_list|(
name|ftpFile
operator|.
name|getName
argument_list|()
argument_list|,
name|byteArrayOutputStream
argument_list|)
expr_stmt|;
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
literal|"Retrieved file: "
operator|+
name|ftpFile
operator|.
name|getName
argument_list|()
operator|+
literal|" from: "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|RemoteFileExchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|fullFileName
argument_list|,
name|byteArrayOutputStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSetNames
argument_list|()
condition|)
block|{
comment|// set the filename in the special header filename marker to the ftp filename
name|String
name|ftpBasePath
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|String
name|relativePath
init|=
name|fullFileName
operator|.
name|substring
argument_list|(
name|ftpBasePath
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|relativePath
operator|=
name|relativePath
operator|.
name|replaceFirst
argument_list|(
literal|"/"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
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
literal|"Setting exchange filename to "
operator|+
name|relativePath
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
name|relativePath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deleteFile
condition|)
block|{
comment|// delete file after consuming
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
name|ftpFile
operator|.
name|getName
argument_list|()
operator|+
literal|" from: "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|deleted
init|=
name|client
operator|.
name|deleteFile
argument_list|(
name|ftpFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|deleted
condition|)
block|{
comment|// ignore just log a warning
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not delete file: "
operator|+
name|ftpFile
operator|.
name|getName
argument_list|()
operator|+
literal|" from: "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|acquireExclusiveRead (FTPClient client, FTPFile ftpFile)
specifier|protected
name|void
name|acquireExclusiveRead
parameter_list|(
name|FTPClient
name|client
parameter_list|,
name|FTPFile
name|ftpFile
parameter_list|)
throws|throws
name|IOException
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
literal|"Waiting for exclusive lock to file: "
operator|+
name|ftpFile
argument_list|)
expr_stmt|;
block|}
comment|// the trick is to try to rename the file, if we can rename then we have exclusive read
comment|// since its a remote file we can not use java.nio to get a RW lock
name|String
name|originalName
init|=
name|ftpFile
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|newName
init|=
name|originalName
operator|+
literal|".camelExclusiveRead"
decl_stmt|;
name|boolean
name|exclusive
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|exclusive
condition|)
block|{
name|exclusive
operator|=
name|client
operator|.
name|rename
argument_list|(
name|originalName
argument_list|,
name|newName
argument_list|)
expr_stmt|;
if|if
condition|(
name|exclusive
condition|)
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
literal|"Acquired exclusive lock to file: "
operator|+
name|originalName
argument_list|)
expr_stmt|;
block|}
comment|// rename it back so we can read it
name|client
operator|.
name|rename
argument_list|(
name|newName
argument_list|,
name|originalName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Exclusive lock not granted. Sleeping for 1000 millis."
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
DECL|method|getFileName (Object file)
specifier|protected
name|String
name|getFileName
parameter_list|(
name|Object
name|file
parameter_list|)
block|{
name|FTPFile
name|ftpFile
init|=
operator|(
name|FTPFile
operator|)
name|file
decl_stmt|;
return|return
name|ftpFile
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

