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
name|Vector
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelSftp
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|SftpException
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

begin_class
DECL|class|SftpConsumer
specifier|public
class|class
name|SftpConsumer
extends|extends
name|RemoteFileConsumer
argument_list|<
name|RemoteFileExchange
argument_list|>
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|SftpEndpoint
name|endpoint
decl_stmt|;
DECL|field|channel
specifier|private
name|ChannelSftp
name|channel
decl_stmt|;
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|method|SftpConsumer (SftpEndpoint endpoint, Processor processor, Session session)
specifier|public
name|SftpConsumer
parameter_list|(
name|SftpEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Session
name|session
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
name|session
operator|=
name|session
expr_stmt|;
block|}
DECL|method|SftpConsumer (SftpEndpoint endpoint, Processor processor, Session session, ScheduledExecutorService executor)
specifier|public
name|SftpConsumer
parameter_list|(
name|SftpEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Session
name|session
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
name|session
operator|=
name|session
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
name|JSchException
block|{
if|if
condition|(
name|channel
operator|==
literal|null
operator|||
operator|!
name|channel
operator|.
name|isConnected
argument_list|()
condition|)
block|{
if|if
condition|(
name|session
operator|==
literal|null
operator|||
operator|!
name|session
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Session isn't connected, trying to recreate and connect."
argument_list|)
expr_stmt|;
name|session
operator|=
name|endpoint
operator|.
name|createSession
argument_list|()
expr_stmt|;
name|session
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Channel isn't connected, trying to recreate and connect."
argument_list|)
expr_stmt|;
name|channel
operator|=
name|endpoint
operator|.
name|createChannelSftp
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|channel
operator|.
name|connect
argument_list|()
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
name|JSchException
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
literal|"Disconnecting from "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|channel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
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
name|channel
operator|.
name|cd
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
name|Vector
name|files
init|=
name|channel
operator|.
name|ls
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
specifier|final
name|ChannelSftp
operator|.
name|LsEntry
name|file
init|=
operator|(
name|ChannelSftp
operator|.
name|LsEntry
operator|)
name|files
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|pollFile
argument_list|(
name|file
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
name|channel
operator|.
name|pwd
argument_list|()
decl_stmt|;
name|channel
operator|.
name|cd
argument_list|(
name|dir
argument_list|)
expr_stmt|;
for|for
control|(
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
range|:
operator|(
name|ChannelSftp
operator|.
name|LsEntry
index|[]
operator|)
name|channel
operator|.
name|ls
argument_list|(
literal|"."
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|ChannelSftp
operator|.
name|LsEntry
index|[]
block|{}
argument_list|)
control|)
block|{
if|if
condition|(
name|sftpFile
operator|.
name|getFilename
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
comment|// skip
block|}
elseif|else
if|if
condition|(
name|sftpFile
operator|.
name|getAttrs
argument_list|()
operator|.
name|isDir
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
name|sftpFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|pollFile
argument_list|(
name|sftpFile
argument_list|)
expr_stmt|;
block|}
block|}
comment|// change back to original current dir
name|channel
operator|.
name|cd
argument_list|(
name|currentDir
argument_list|)
expr_stmt|;
block|}
DECL|method|getFullFileName (ChannelSftp.LsEntry sftpFile)
specifier|protected
name|String
name|getFullFileName
parameter_list|(
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
parameter_list|)
throws|throws
name|IOException
throws|,
name|SftpException
block|{
return|return
name|channel
operator|.
name|pwd
argument_list|()
operator|+
literal|"/"
operator|+
name|sftpFile
operator|.
name|getFilename
argument_list|()
return|;
block|}
DECL|method|pollFile (ChannelSftp.LsEntry sftpFile)
specifier|private
name|void
name|pollFile
parameter_list|(
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
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
literal|"Polling file: "
operator|+
name|sftpFile
argument_list|)
expr_stmt|;
block|}
name|long
name|ts
init|=
name|sftpFile
operator|.
name|getAttrs
argument_list|()
operator|.
name|getMTime
argument_list|()
operator|*
literal|1000L
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
name|sftpFile
argument_list|)
condition|)
block|{
name|String
name|fullFileName
init|=
name|getFullFileName
argument_list|(
name|sftpFile
argument_list|)
decl_stmt|;
comment|// is we use excluse read then acquire the exclusive read (waiting until we got it)
if|if
condition|(
name|exclusiveReadLock
condition|)
block|{
name|acquireExclusiveReadLock
argument_list|(
name|sftpFile
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
name|channel
operator|.
name|get
argument_list|(
name|sftpFile
operator|.
name|getFilename
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
name|sftpFile
operator|.
name|getFilename
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
name|getFullFileName
argument_list|(
name|sftpFile
argument_list|)
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
name|sftpFile
operator|.
name|getFilename
argument_list|()
operator|+
literal|" from: "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|deleteFile
argument_list|(
name|sftpFile
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isMoveFile
argument_list|()
condition|)
block|{
name|String
name|fromName
init|=
name|sftpFile
operator|.
name|getFilename
argument_list|()
decl_stmt|;
name|String
name|toName
init|=
name|getMoveFileName
argument_list|(
name|fromName
argument_list|)
decl_stmt|;
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
literal|"Moving file: "
operator|+
name|fromName
operator|+
literal|" to: "
operator|+
name|toName
argument_list|)
expr_stmt|;
block|}
comment|// delete any existing file
name|boolean
name|deleted
init|=
name|deleteFile
argument_list|(
name|toName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|deleted
condition|)
block|{
comment|// if we could not delete any existing file then maybe the folder is missing
comment|// build folder if needed
name|int
name|lastPathIndex
init|=
name|toName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastPathIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|directory
init|=
name|toName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastPathIndex
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|SftpUtils
operator|.
name|buildDirectory
argument_list|(
name|channel
argument_list|,
name|directory
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can not build directory: "
operator|+
name|directory
operator|+
literal|" (maybe because of denied permissions)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// try to rename
try|try
block|{
name|channel
operator|.
name|rename
argument_list|(
name|fromName
argument_list|,
name|toName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
comment|// ignore just log a warning
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can not move file: "
operator|+
name|fromName
operator|+
literal|" to: "
operator|+
name|toName
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
DECL|method|deleteFile (String filename)
specifier|private
name|boolean
name|deleteFile
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
try|try
block|{
name|channel
operator|.
name|rm
argument_list|(
name|filename
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
comment|// ignore just log a warning
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not delete file: "
operator|+
name|filename
operator|+
literal|" from: "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
DECL|method|acquireExclusiveReadLock (ChannelSftp.LsEntry sftpFile)
specifier|protected
name|void
name|acquireExclusiveReadLock
parameter_list|(
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
parameter_list|)
throws|throws
name|SftpException
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
literal|"Waiting for exclusive read lock to file: "
operator|+
name|sftpFile
argument_list|)
expr_stmt|;
block|}
comment|// the trick is to try to rename the file, if we can rename then we have exclusive read
comment|// since its a remote file we can not use java.nio to get a RW access
name|String
name|originalName
init|=
name|sftpFile
operator|.
name|getFilename
argument_list|()
decl_stmt|;
name|String
name|newName
init|=
name|originalName
operator|+
literal|".camelExclusiveReadLock"
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
try|try
block|{
name|channel
operator|.
name|rename
argument_list|(
name|originalName
argument_list|,
name|newName
argument_list|)
expr_stmt|;
name|exclusive
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
comment|// ignore we can not rename it
block|}
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
literal|"Acquired exclusive read lock to file: "
operator|+
name|originalName
argument_list|)
expr_stmt|;
block|}
comment|// rename it back so we can read it
name|channel
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
literal|"Exclusive read lock not granted. Sleeping for 1000 millis"
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
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
init|=
operator|(
name|ChannelSftp
operator|.
name|LsEntry
operator|)
name|file
decl_stmt|;
return|return
name|sftpFile
operator|.
name|getFilename
argument_list|()
return|;
block|}
block|}
end_class

end_unit

