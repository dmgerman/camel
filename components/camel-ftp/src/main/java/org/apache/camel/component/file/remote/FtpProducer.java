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
DECL|field|loggedIn
specifier|private
name|boolean
name|loggedIn
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
literal|"Processing "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|connectIfNecessary
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|loggedIn
condition|)
block|{
name|String
name|message
init|=
literal|"Could not connect/login to "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|FtpOperationFailedException
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
name|message
argument_list|)
throw|;
block|}
name|process
argument_list|(
operator|(
name|RemoteFileExchange
operator|)
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
name|loggedIn
operator|=
literal|false
expr_stmt|;
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
name|log
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
name|log
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
operator|||
operator|!
name|loggedIn
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Not connected/logged in, connecting to "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|loggedIn
operator|=
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
if|if
condition|(
operator|!
name|loggedIn
condition|)
block|{
return|return;
block|}
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Connected and logged in to "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect ()
specifier|public
name|void
name|disconnect
parameter_list|()
throws|throws
name|IOException
block|{
name|loggedIn
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
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
name|FtpUtils
operator|.
name|buildDirectory
argument_list|(
name|client
argument_list|,
name|directory
argument_list|)
condition|)
block|{
name|log
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
name|String
name|message
init|=
literal|"Error sending file: "
operator|+
name|fileName
operator|+
literal|" to: "
operator|+
name|remoteServer
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|FtpOperationFailedException
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
name|message
argument_list|)
throw|;
block|}
name|log
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
block|}
end_class

end_unit

