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
name|RuntimeCamelException
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

begin_class
DECL|class|FtpProducer
specifier|public
class|class
name|FtpProducer
extends|extends
name|RemoteFileProducer
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
DECL|method|FtpProducer (FtpEndpoint endpoint, FTPClient client)
specifier|public
name|FtpProducer
parameter_list|(
name|FtpEndpoint
name|endpoint
parameter_list|,
name|FTPClient
name|client
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
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
literal|"Processing "
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
comment|// exception will signify that we couldn't deliver
try|try
block|{
name|process
argument_list|(
name|endpoint
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
argument_list|)
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
literal|"Producer is stopping. Ignoring caught exception: "
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
literal|"Exception occured during processing: "
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
specifier|public
name|void
name|disconnect
parameter_list|()
throws|throws
name|IOException
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
name|FtpUtils
operator|.
name|disconnect
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
DECL|method|process (RemoteFileExchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|RemoteFileExchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|payload
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
name|String
name|fileName
init|=
name|createFileName
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|lastPathIndex
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
name|lastPathIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|directory
init|=
name|fileName
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
name|buildDirectory
argument_list|(
name|client
argument_list|,
name|directory
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Couldn't build directory: "
operator|+
name|directory
operator|+
literal|" (could be because of denied permissions)"
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|success
init|=
name|client
operator|.
name|storeFile
argument_list|(
name|fileName
argument_list|,
name|payload
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error sending file: "
operator|+
name|fileName
operator|+
literal|" to: "
operator|+
name|remoteServer
argument_list|()
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Sent: "
operator|+
name|fileName
operator|+
literal|" to: "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|payload
operator|!=
literal|null
condition|)
block|{
name|payload
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|buildDirectory (FTPClient ftpClient, String dirName)
specifier|protected
name|boolean
name|buildDirectory
parameter_list|(
name|FTPClient
name|ftpClient
parameter_list|,
name|String
name|dirName
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|originalDirectory
init|=
name|ftpClient
operator|.
name|printWorkingDirectory
argument_list|()
decl_stmt|;
name|boolean
name|success
init|=
literal|false
decl_stmt|;
try|try
block|{
comment|// maybe the full directory already exsits
name|success
operator|=
name|ftpClient
operator|.
name|changeWorkingDirectory
argument_list|(
name|dirName
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
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Trying to build remote directory: "
operator|+
name|dirName
argument_list|)
expr_stmt|;
block|}
name|success
operator|=
name|ftpClient
operator|.
name|makeDirectory
argument_list|(
name|dirName
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
comment|// we are here if the server side doesn't create intermediate folders
comment|// so create the folder one by one
name|buildDirectoryChunks
argument_list|(
name|ftpClient
argument_list|,
name|dirName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
comment|// change back to original directory
name|ftpClient
operator|.
name|changeWorkingDirectory
argument_list|(
name|originalDirectory
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
DECL|method|buildDirectoryChunks (FTPClient ftpClient, String dirName)
specifier|private
name|boolean
name|buildDirectoryChunks
parameter_list|(
name|FTPClient
name|ftpClient
parameter_list|,
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
literal|"\\/"
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
literal|"Trying to build remote directory: "
operator|+
name|directory
argument_list|)
expr_stmt|;
block|}
name|success
operator|=
name|ftpClient
operator|.
name|makeDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
block|}
end_class

end_unit

