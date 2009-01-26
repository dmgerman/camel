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
name|List
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
name|JSch
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|UserInfo
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
name|GenericFileExchange
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
import|;
end_import

begin_comment
comment|/**  * SFTP remote file operations  */
end_comment

begin_class
DECL|class|SftpRemoteFileOperations
specifier|public
class|class
name|SftpRemoteFileOperations
implements|implements
name|RemoteFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|SftpRemoteFileOperations
operator|.
name|class
argument_list|)
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
try|try
block|{
if|if
condition|(
name|isConnected
argument_list|()
condition|)
block|{
comment|// already connected
return|return
literal|true
return|;
block|}
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
name|trace
argument_list|(
literal|"Session isn't connected, trying to recreate and connect."
argument_list|)
expr_stmt|;
name|session
operator|=
name|createSession
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|session
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Channel isn't connected, trying to recreate and connect."
argument_list|)
expr_stmt|;
name|channel
operator|=
operator|(
name|ChannelSftp
operator|)
name|session
operator|.
name|openChannel
argument_list|(
literal|"sftp"
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
name|configuration
operator|.
name|remoteServerInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Cannot connect to "
operator|+
name|configuration
operator|.
name|remoteServerInformation
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createSession (final RemoteFileConfiguration configuration)
specifier|protected
name|Session
name|createSession
parameter_list|(
specifier|final
name|RemoteFileConfiguration
name|configuration
parameter_list|)
throws|throws
name|JSchException
block|{
specifier|final
name|JSch
name|jsch
init|=
operator|new
name|JSch
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using private keyfile: "
operator|+
name|configuration
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getPrivateKeyFilePassphrase
argument_list|()
argument_list|)
condition|)
block|{
name|jsch
operator|.
name|addIdentity
argument_list|(
name|configuration
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPrivateKeyFilePassphrase
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jsch
operator|.
name|addIdentity
argument_list|(
name|configuration
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getKnownHostsFile
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using knownhosts file: "
operator|+
name|configuration
operator|.
name|getKnownHostsFile
argument_list|()
argument_list|)
expr_stmt|;
name|jsch
operator|.
name|setKnownHosts
argument_list|(
name|configuration
operator|.
name|getKnownHostsFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Session
name|session
init|=
name|jsch
operator|.
name|getSession
argument_list|(
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|session
operator|.
name|setUserInfo
argument_list|(
operator|new
name|UserInfo
argument_list|()
block|{
specifier|public
name|String
name|getPassphrase
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getPassword
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|promptPassword
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|promptPassphrase
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|promptYesNo
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|s
argument_list|)
expr_stmt|;
comment|// Return 'false' indicating modification of the hosts file is disabled.
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|showMessage
parameter_list|(
name|String
name|s
parameter_list|)
block|{             }
block|}
argument_list|)
expr_stmt|;
return|return
name|session
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
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|isConnected
argument_list|()
operator|&&
name|channel
operator|!=
literal|null
operator|&&
name|channel
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
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|isConnected
argument_list|()
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
operator|&&
name|channel
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|channel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
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
name|channel
operator|.
name|rm
argument_list|(
name|name
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
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Cannot delete file: "
operator|+
name|name
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
name|channel
operator|.
name|rename
argument_list|(
name|from
argument_list|,
name|to
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
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Cannot rename file from: "
operator|+
name|from
operator|+
literal|" to: "
operator|+
name|to
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|buildDirectory (String dirName)
specifier|public
name|boolean
name|buildDirectory
parameter_list|(
name|String
name|dirName
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
name|String
name|originalDirectory
init|=
name|getCurrentDirectory
argument_list|()
decl_stmt|;
try|try
block|{
comment|// maybe the full directory already exsits
try|try
block|{
name|channel
operator|.
name|cd
argument_list|(
name|dirName
argument_list|)
expr_stmt|;
name|success
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
comment|// ignore, we could not change directory so try to create it instead
block|}
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
try|try
block|{
name|channel
operator|.
name|mkdir
argument_list|(
name|dirName
argument_list|)
expr_stmt|;
name|success
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
comment|// we are here if the server side doesn't create intermediate folders
comment|// so create the folder one by one
name|success
operator|=
name|buildDirectoryChunks
argument_list|(
name|dirName
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
name|RemoteFileOperationFailedException
argument_list|(
literal|"Cannot build directory "
operator|+
name|dirName
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Cannot build directory "
operator|+
name|dirName
argument_list|,
name|e
argument_list|)
throw|;
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
name|changeCurrentDirectory
argument_list|(
name|originalDirectory
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|success
return|;
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
throws|,
name|SftpException
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
try|try
block|{
name|channel
operator|.
name|mkdir
argument_list|(
name|directory
argument_list|)
expr_stmt|;
name|success
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
comment|// ignore keep trying to create the rest of the path
block|}
block|}
return|return
name|success
return|;
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
name|channel
operator|.
name|pwd
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Cannot get current directory"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|changeCurrentDirectory (String path)
specifier|public
name|void
name|changeCurrentDirectory
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
try|try
block|{
name|channel
operator|.
name|cd
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Cannot change current directory to: "
operator|+
name|path
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|listFiles ()
specifier|public
name|List
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
name|listFiles
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
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
comment|// list current dirctory if file path is not given
name|path
operator|=
literal|"."
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Vector
name|files
init|=
name|channel
operator|.
name|ls
argument_list|(
name|path
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|file
range|:
name|files
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Cannot list directory: "
operator|+
name|path
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|retrieveFile (String name, GenericFileExchange<ChannelSftp.LsEntry> exchange)
specifier|public
name|boolean
name|retrieveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|GenericFileExchange
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
try|try
block|{
name|GenericFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|target
init|=
name|exchange
operator|.
name|getGenericFile
argument_list|()
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|target
operator|.
name|setBody
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|channel
operator|.
name|get
argument_list|(
name|name
argument_list|,
name|os
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
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Could not retrieve the file ["
operator|+
name|name
operator|+
literal|"]"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|storeFile (String name, GenericFileExchange<ChannelSftp.LsEntry> exchange)
specifier|public
name|boolean
name|storeFile
parameter_list|(
name|String
name|name
parameter_list|,
name|GenericFileExchange
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
try|try
block|{
name|channel
operator|.
name|put
argument_list|(
operator|(
name|InputStream
operator|)
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
argument_list|,
name|name
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
throw|throw
operator|new
name|RemoteFileOperationFailedException
argument_list|(
literal|"Could not write the file ["
operator|+
name|name
operator|+
literal|"]"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

