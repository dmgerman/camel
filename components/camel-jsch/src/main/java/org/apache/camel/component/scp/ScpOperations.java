begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.scp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|scp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

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
name|Hashtable
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelExec
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
name|UIKeyboardInteractive
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
name|InvalidPayloadException
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
name|remote
operator|.
name|RemoteFileConfiguration
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
name|remote
operator|.
name|RemoteFileOperations
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

begin_comment
comment|/**  * SCP remote file operations  */
end_comment

begin_class
DECL|class|ScpOperations
specifier|public
class|class
name|ScpOperations
implements|implements
name|RemoteFileOperations
argument_list|<
name|ScpFile
argument_list|>
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
name|ScpOperations
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|ScpEndpoint
name|endpoint
decl_stmt|;
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|field|channel
specifier|private
name|ChannelExec
name|channel
decl_stmt|;
DECL|field|userKnownHostFile
specifier|private
name|String
name|userKnownHostFile
decl_stmt|;
annotation|@
name|Override
DECL|method|setEndpoint (GenericFileEndpoint<ScpFile> endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|ScpFile
argument_list|>
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
operator|(
name|ScpEndpoint
operator|)
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Operation 'delete' not supported by the scp: protocol"
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
comment|// maybe... cannot determine using the scp: protocol
return|return
literal|false
return|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Operation 'rename' not supported by the scp: protocol"
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
comment|// done by the server
return|return
literal|true
return|;
block|}
annotation|@
name|Override
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
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|releaseRetreivedFileResources (Exchange exchange)
specifier|public
name|void
name|releaseRetreivedFileResources
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// No-op
block|}
annotation|@
name|Override
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|session
argument_list|,
literal|"session"
argument_list|)
expr_stmt|;
name|ScpConfiguration
name|cfg
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|int
name|timeout
init|=
name|cfg
operator|.
name|getConnectTimeout
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
literal|"Opening channel to {} with {} timeout..."
argument_list|,
name|cfg
operator|.
name|remoteServerInformation
argument_list|()
argument_list|,
name|timeout
operator|>
literal|0
condition|?
operator|(
name|Integer
operator|.
name|toString
argument_list|(
name|timeout
argument_list|)
operator|+
literal|" ms"
operator|)
else|:
literal|"no"
argument_list|)
expr_stmt|;
block|}
name|String
name|file
init|=
name|getRemoteFile
argument_list|(
name|name
argument_list|,
name|cfg
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// Do an explicit test for a null body and decide what to do
if|if
condition|(
name|endpoint
operator|.
name|isAllowNullBody
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Writing empty file."
argument_list|)
expr_stmt|;
name|is
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot write null body to file: "
operator|+
name|name
argument_list|)
throw|;
block|}
block|}
try|try
block|{
name|channel
operator|=
operator|(
name|ChannelExec
operator|)
name|session
operator|.
name|openChannel
argument_list|(
literal|"exec"
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setCommand
argument_list|(
name|getScpCommand
argument_list|(
name|cfg
argument_list|,
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|channel
operator|.
name|connect
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Channel connected to {}"
argument_list|,
name|cfg
operator|.
name|remoteServerInformation
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|is
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|write
argument_list|(
name|channel
argument_list|,
name|file
argument_list|,
name|is
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPayloadException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot store file: "
operator|+
name|name
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
name|GenericFileOperationFailedException
argument_list|(
literal|"Failed to write file "
operator|+
name|file
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
comment|// must close stream after usage
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Failed to write file "
operator|+
name|file
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Disconnecting 'exec' scp channel"
argument_list|)
expr_stmt|;
name|channel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|channel
operator|=
literal|null
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Channel disconnected from {}"
argument_list|,
name|cfg
operator|.
name|remoteServerInformation
argument_list|()
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
DECL|method|getCurrentDirectory ()
specifier|public
name|String
name|getCurrentDirectory
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
return|return
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
return|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Operation 'cd "
operator|+
name|path
operator|+
literal|"' not supported by the scp: protocol"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|changeToParentDirectory ()
specifier|public
name|void
name|changeToParentDirectory
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Operation 'cd ..' not supported by the scp: protocol"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|listFiles ()
specifier|public
name|List
argument_list|<
name|ScpFile
argument_list|>
name|listFiles
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Operation 'ls' not supported by the scp: protocol"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|listFiles (String path)
specifier|public
name|List
argument_list|<
name|ScpFile
argument_list|>
name|listFiles
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Operation 'ls "
operator|+
name|path
operator|+
literal|"' not supported by the scp: protocol"
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
operator|!
name|isConnected
argument_list|()
condition|)
block|{
name|session
operator|=
name|createSession
argument_list|(
name|configuration
operator|instanceof
name|ScpConfiguration
condition|?
operator|(
name|ScpConfiguration
operator|)
name|configuration
else|:
literal|null
argument_list|)
expr_stmt|;
comment|// TODO: deal with reconnection attempts
if|if
condition|(
operator|!
name|isConnected
argument_list|()
condition|)
block|{
name|session
operator|=
literal|null
expr_stmt|;
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Failed to connect to "
operator|+
name|configuration
operator|.
name|remoteServerInformation
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
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
return|;
block|}
annotation|@
name|Override
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
name|session
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sendNoop ()
specifier|public
name|boolean
name|sendNoop
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// not supported by scp:
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|sendSiteCommand (String command)
specifier|public
name|boolean
name|sendSiteCommand
parameter_list|(
name|String
name|command
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
return|return
literal|true
return|;
block|}
DECL|method|createSession (ScpConfiguration config)
specifier|private
name|Session
name|createSession
parameter_list|(
name|ScpConfiguration
name|config
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|config
argument_list|,
literal|"ScpConfiguration"
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|JSch
name|jsch
init|=
operator|new
name|JSch
argument_list|()
decl_stmt|;
comment|// get from configuration
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getCiphers
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using ciphers: {}"
argument_list|,
name|config
operator|.
name|getCiphers
argument_list|()
argument_list|)
expr_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ciphers
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ciphers
operator|.
name|put
argument_list|(
literal|"cipher.s2c"
argument_list|,
name|config
operator|.
name|getCiphers
argument_list|()
argument_list|)
expr_stmt|;
name|ciphers
operator|.
name|put
argument_list|(
literal|"cipher.c2s"
argument_list|,
name|config
operator|.
name|getCiphers
argument_list|()
argument_list|)
expr_stmt|;
name|JSch
operator|.
name|setConfig
argument_list|(
name|ciphers
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using private keyfile: {}"
argument_list|,
name|config
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|pkfp
init|=
name|config
operator|.
name|getPrivateKeyFilePassphrase
argument_list|()
decl_stmt|;
name|jsch
operator|.
name|addIdentity
argument_list|(
name|config
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|,
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|pkfp
argument_list|)
condition|?
name|pkfp
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
name|String
name|knownHostsFile
init|=
name|config
operator|.
name|getKnownHostsFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|knownHostsFile
operator|==
literal|null
operator|&&
name|config
operator|.
name|isUseUserKnownHostsFile
argument_list|()
condition|)
block|{
if|if
condition|(
name|userKnownHostFile
operator|==
literal|null
condition|)
block|{
name|userKnownHostFile
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.ssh/known_hosts"
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Known host file not configured, using user known host file: "
operator|+
name|userKnownHostFile
argument_list|)
expr_stmt|;
block|}
name|knownHostsFile
operator|=
name|userKnownHostFile
expr_stmt|;
block|}
name|jsch
operator|.
name|setKnownHosts
argument_list|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|knownHostsFile
argument_list|)
condition|?
literal|null
else|:
name|knownHostsFile
argument_list|)
expr_stmt|;
name|session
operator|=
name|jsch
operator|.
name|getSession
argument_list|(
name|config
operator|.
name|getUsername
argument_list|()
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|setTimeout
argument_list|(
name|config
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|setUserInfo
argument_list|(
operator|new
name|SessionUserInfo
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getStrictHostKeyChecking
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using StrickHostKeyChecking: {}"
argument_list|,
name|config
operator|.
name|getStrictHostKeyChecking
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|setConfig
argument_list|(
literal|"StrictHostKeyChecking"
argument_list|,
name|config
operator|.
name|getStrictHostKeyChecking
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getPreferredAuthentications
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using preferredAuthentications: {}"
argument_list|,
name|config
operator|.
name|getPreferredAuthentications
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|setConfig
argument_list|(
literal|"PreferredAuthentications"
argument_list|,
name|config
operator|.
name|getPreferredAuthentications
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|timeout
init|=
name|config
operator|.
name|getConnectTimeout
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connecting to {} with {} timeout..."
argument_list|,
name|config
operator|.
name|remoteServerInformation
argument_list|()
argument_list|,
name|timeout
operator|>
literal|0
condition|?
operator|(
name|Integer
operator|.
name|toString
argument_list|(
name|timeout
argument_list|)
operator|+
literal|" ms"
operator|)
else|:
literal|"no"
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|session
operator|.
name|connect
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
name|session
operator|=
literal|null
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not create ssh session for "
operator|+
name|config
operator|.
name|remoteServerInformation
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|session
return|;
block|}
DECL|method|write (ChannelExec c, String name, InputStream data, ScpConfiguration cfg)
specifier|private
name|void
name|write
parameter_list|(
name|ChannelExec
name|c
parameter_list|,
name|String
name|name
parameter_list|,
name|InputStream
name|data
parameter_list|,
name|ScpConfiguration
name|cfg
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|os
init|=
name|c
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|c
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|writeFile
argument_list|(
name|name
argument_list|,
name|data
argument_list|,
name|os
argument_list|,
name|is
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|writeFile (String filename, InputStream data, OutputStream os, InputStream is, ScpConfiguration cfg)
specifier|private
name|void
name|writeFile
parameter_list|(
name|String
name|filename
parameter_list|,
name|InputStream
name|data
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|ScpConfiguration
name|cfg
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|int
name|lineFeed
init|=
literal|'\n'
decl_stmt|;
name|String
name|bytes
decl_stmt|;
name|int
name|pos
init|=
name|filename
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>=
literal|0
condition|)
block|{
comment|// write to child directory
name|String
name|dir
init|=
name|filename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|bytes
operator|=
literal|"D0775 0 "
operator|+
name|dir
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"[scp:sink] {}"
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|bytes
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|lineFeed
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|readAck
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|writeFile
argument_list|(
name|filename
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
argument_list|,
name|data
argument_list|,
name|os
argument_list|,
name|is
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|bytes
operator|=
literal|"E"
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"[scp:sink] {}"
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|bytes
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|lineFeed
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|readAck
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|read
decl_stmt|;
name|int
name|size
init|=
name|endpoint
operator|.
name|getBufferSize
argument_list|()
decl_stmt|;
name|byte
index|[]
name|reply
init|=
operator|new
name|byte
index|[
name|size
index|]
decl_stmt|;
comment|// figure out the stream size as we need to pass it in the header
name|BufferedInputStream
name|buffer
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|data
argument_list|,
name|size
argument_list|)
decl_stmt|;
try|try
block|{
name|buffer
operator|.
name|mark
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|buffer
operator|.
name|read
argument_list|(
name|reply
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|count
operator|+=
name|read
expr_stmt|;
block|}
comment|// send the header
name|bytes
operator|=
literal|"C0"
operator|+
name|cfg
operator|.
name|getChmod
argument_list|()
operator|+
literal|" "
operator|+
name|count
operator|+
literal|" "
operator|+
name|filename
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"[scp:sink] {}"
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|bytes
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|lineFeed
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|readAck
argument_list|(
name|is
argument_list|)
expr_stmt|;
comment|// now send the stream
name|buffer
operator|.
name|reset
argument_list|()
expr_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|buffer
operator|.
name|read
argument_list|(
name|reply
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|reply
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|writeAck
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|readAck
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|writeAck (OutputStream os)
specifier|private
name|void
name|writeAck
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|os
operator|.
name|write
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|method|readAck (InputStream is)
specifier|private
name|int
name|readAck
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|message
decl_stmt|;
name|int
name|answer
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|answer
condition|)
block|{
case|case
literal|0
case|:
break|break;
default|default:
name|message
operator|=
literal|"[scp] Return Code ["
operator|+
name|answer
operator|+
literal|"] "
operator|+
name|readLine
argument_list|(
name|is
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|message
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|readLine (InputStream is)
specifier|private
name|String
name|readLine
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|bytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|int
name|c
decl_stmt|;
do|do
block|{
name|c
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
return|return
name|bytes
operator|.
name|toString
argument_list|()
return|;
block|}
name|bytes
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|c
operator|!=
operator|-
literal|1
condition|)
do|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
name|String
name|message
init|=
literal|"[scp] Unexpected end of stream"
decl_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|message
argument_list|)
throw|;
block|}
DECL|method|getRemoteTarget (ScpConfiguration config)
specifier|private
specifier|static
name|String
name|getRemoteTarget
parameter_list|(
name|ScpConfiguration
name|config
parameter_list|)
block|{
comment|// use current dir (".") if target directory not specified in uri
return|return
name|config
operator|.
name|getDirectory
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|?
literal|"."
else|:
name|config
operator|.
name|getDirectory
argument_list|()
return|;
block|}
DECL|method|getRemoteFile (String name, ScpConfiguration config)
specifier|private
specifier|static
name|String
name|getRemoteFile
parameter_list|(
name|String
name|name
parameter_list|,
name|ScpConfiguration
name|config
parameter_list|)
block|{
name|String
name|dir
init|=
name|config
operator|.
name|getDirectory
argument_list|()
decl_stmt|;
name|dir
operator|=
name|dir
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|dir
else|:
name|dir
operator|+
literal|"/"
expr_stmt|;
return|return
name|name
operator|.
name|startsWith
argument_list|(
name|dir
argument_list|)
condition|?
name|name
operator|.
name|substring
argument_list|(
name|dir
operator|.
name|length
argument_list|()
argument_list|)
else|:
name|name
return|;
block|}
DECL|method|isRecursiveScp (String name)
specifier|private
specifier|static
name|boolean
name|isRecursiveScp
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|>
literal|0
return|;
block|}
DECL|method|getScpCommand (ScpConfiguration config, String name)
specifier|private
specifier|static
name|String
name|getScpCommand
parameter_list|(
name|ScpConfiguration
name|config
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|StringBuilder
name|cmd
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|cmd
operator|.
name|append
argument_list|(
literal|"scp "
argument_list|)
expr_stmt|;
comment|// TODO: need config for scp *-p* (preserves modification times, access times, and modes from the original file)
comment|// String command="scp " + (ptimestamp ? "-p " : "") + "-t " + configuration.getDirectory();
comment|// TODO: refactor to use generic command
name|cmd
operator|.
name|append
argument_list|(
name|isRecursiveScp
argument_list|(
name|name
argument_list|)
condition|?
literal|"-r "
else|:
literal|""
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|append
argument_list|(
literal|"-t "
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|append
argument_list|(
name|getRemoteTarget
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cmd
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|class|SessionUserInfo
specifier|protected
specifier|static
specifier|final
class|class
name|SessionUserInfo
implements|implements
name|UserInfo
implements|,
name|UIKeyboardInteractive
block|{
DECL|field|config
specifier|private
specifier|final
name|ScpConfiguration
name|config
decl_stmt|;
DECL|method|SessionUserInfo (ScpConfiguration config)
specifier|public
name|SessionUserInfo
parameter_list|(
name|ScpConfiguration
name|config
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|config
argument_list|,
literal|"config"
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPassphrase ()
specifier|public
name|String
name|getPassphrase
parameter_list|()
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Private Key authentication not supported"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Providing password for ssh authentication of user '{}'"
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|config
operator|.
name|getPassword
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|promptPassword (String message)
specifier|public
name|boolean
name|promptPassword
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|promptPassphrase (String message)
specifier|public
name|boolean
name|promptPassphrase
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|promptYesNo (String message)
specifier|public
name|boolean
name|promptYesNo
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|showMessage (String message)
specifier|public
name|void
name|showMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|promptKeyboardInteractive (String destination, String name, String instruction, String[] prompt, boolean[] echo)
specifier|public
name|String
index|[]
name|promptKeyboardInteractive
parameter_list|(
name|String
name|destination
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|instruction
parameter_list|,
name|String
index|[]
name|prompt
parameter_list|,
name|boolean
index|[]
name|echo
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|instruction
argument_list|)
expr_stmt|;
comment|// Called for either SSH_MSG_USERAUTH_INFO_REQUEST or SSH_MSG_USERAUTH_PASSWD_CHANGEREQ
comment|// The most secure choice (especially for the second case) is to return null
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

