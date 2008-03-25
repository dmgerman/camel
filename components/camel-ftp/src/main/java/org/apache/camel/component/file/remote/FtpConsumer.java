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
name|RuntimeCamelException
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
name|FTPConnectionClosedException
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
name|FtpConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|FtpEndpoint
name|endpoint
decl_stmt|;
DECL|field|recursive
specifier|private
name|boolean
name|recursive
init|=
literal|true
decl_stmt|;
DECL|field|regexPattern
specifier|private
name|String
name|regexPattern
init|=
literal|""
decl_stmt|;
DECL|field|lastPollTime
specifier|private
name|long
name|lastPollTime
decl_stmt|;
DECL|field|client
specifier|private
name|FTPClient
name|client
decl_stmt|;
DECL|field|setNames
specifier|private
name|boolean
name|setNames
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
comment|// TODO: is there a way to avoid copy-pasting the reconnect logic?
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"FtpConsumer's client isn't connected, trying to reconnect..."
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|connect
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Connected to "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO: is there a way to avoid copy-pasting the reconnect logic?
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
name|info
argument_list|(
literal|"FtpConsumer's client is being explicitly disconnected"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|disconnect
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
comment|// TODO: is there a way to avoid copy-pasting the reconnect logic?
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
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
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
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
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
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
name|FTPConnectionClosedException
name|e
parameter_list|)
block|{
comment|// If the server disconnected us, then we must manually disconnect
comment|// the client before attempting to reconnect
name|LOG
operator|.
name|warn
argument_list|(
literal|"Disconnecting due to exception: "
operator|+
name|e
operator|.
name|toString
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
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Caught RuntimeCamelException: "
operator|+
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Hoping an explicit disconnect/reconnect will solve the problem"
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|""
argument_list|)
throw|;
block|}
block|}
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
operator|.
name|getTimestamp
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
operator|>
name|lastPollTime
condition|)
block|{
comment|// TODO
comment|// do we
comment|// need
comment|// to
comment|// adjust
comment|// the
comment|// TZ?
comment|// can
comment|// we?
if|if
condition|(
name|isMatched
argument_list|(
name|ftpFile
argument_list|)
condition|)
block|{
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
name|RemoteFileExchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|getFullFileName
argument_list|(
name|ftpFile
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
name|relativePath
init|=
name|getFullFileName
argument_list|(
name|ftpFile
argument_list|)
operator|.
name|substring
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|relativePath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|relativePath
operator|=
name|relativePath
operator|.
name|substring
argument_list|(
literal|1
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
block|}
DECL|method|isMatched (FTPFile file)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|FTPFile
name|file
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|regexPattern
operator|!=
literal|null
operator|&&
name|regexPattern
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|result
operator|=
name|file
operator|.
name|getName
argument_list|()
operator|.
name|matches
argument_list|(
name|getRegexPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|isRecursive ()
specifier|public
name|boolean
name|isRecursive
parameter_list|()
block|{
return|return
name|recursive
return|;
block|}
DECL|method|setRecursive (boolean recursive)
specifier|public
name|void
name|setRecursive
parameter_list|(
name|boolean
name|recursive
parameter_list|)
block|{
name|this
operator|.
name|recursive
operator|=
name|recursive
expr_stmt|;
block|}
DECL|method|getLastPollTime ()
specifier|public
name|long
name|getLastPollTime
parameter_list|()
block|{
return|return
name|lastPollTime
return|;
block|}
DECL|method|setLastPollTime (long lastPollTime)
specifier|public
name|void
name|setLastPollTime
parameter_list|(
name|long
name|lastPollTime
parameter_list|)
block|{
name|this
operator|.
name|lastPollTime
operator|=
name|lastPollTime
expr_stmt|;
block|}
DECL|method|getRegexPattern ()
specifier|public
name|String
name|getRegexPattern
parameter_list|()
block|{
return|return
name|regexPattern
return|;
block|}
DECL|method|setRegexPattern (String regexPattern)
specifier|public
name|void
name|setRegexPattern
parameter_list|(
name|String
name|regexPattern
parameter_list|)
block|{
name|this
operator|.
name|regexPattern
operator|=
name|regexPattern
expr_stmt|;
block|}
DECL|method|isSetNames ()
specifier|public
name|boolean
name|isSetNames
parameter_list|()
block|{
return|return
name|setNames
return|;
block|}
DECL|method|setSetNames (boolean setNames)
specifier|public
name|void
name|setSetNames
parameter_list|(
name|boolean
name|setNames
parameter_list|)
block|{
name|this
operator|.
name|setNames
operator|=
name|setNames
expr_stmt|;
block|}
block|}
end_class

end_unit

