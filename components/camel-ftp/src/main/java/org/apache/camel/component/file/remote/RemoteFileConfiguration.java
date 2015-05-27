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
name|net
operator|.
name|URI
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
name|GenericFileConfiguration
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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

begin_comment
comment|/**  * Configuration of the FTP server  */
end_comment

begin_class
DECL|class|RemoteFileConfiguration
specifier|public
specifier|abstract
class|class
name|RemoteFileConfiguration
extends|extends
name|GenericFileConfiguration
block|{
comment|/**      * Path separator as either unix or windows style.      *<p/>      * UNIX = Path separator / is used      * Windows = Path separator \ is used      * Auto = Use existing path separator in file name      */
DECL|enum|PathSeparator
DECL|enumConstant|UNIX
DECL|enumConstant|Windows
DECL|enumConstant|Auto
specifier|public
enum|enum
name|PathSeparator
block|{
name|UNIX
block|,
name|Windows
block|,
name|Auto
block|}
comment|// component name is implied as the protocol, eg ftp/ftps etc
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"directoryName"
argument_list|)
DECL|field|directoryName
specifier|private
name|String
name|directoryName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|binary
specifier|private
name|boolean
name|binary
decl_stmt|;
annotation|@
name|UriParam
DECL|field|passiveMode
specifier|private
name|boolean
name|passiveMode
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|connectTimeout
specifier|private
name|int
name|connectTimeout
init|=
literal|10000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|soTimeout
specifier|private
name|int
name|soTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"32768"
argument_list|)
DECL|field|receiveBufferSize
specifier|private
name|int
name|receiveBufferSize
init|=
literal|32
operator|*
literal|1024
decl_stmt|;
annotation|@
name|UriParam
DECL|field|throwExceptionOnConnectFailed
specifier|private
name|boolean
name|throwExceptionOnConnectFailed
decl_stmt|;
annotation|@
name|UriParam
DECL|field|siteCommand
specifier|private
name|String
name|siteCommand
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|stepwise
specifier|private
name|boolean
name|stepwise
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"UNIX"
argument_list|)
DECL|field|separator
specifier|private
name|PathSeparator
name|separator
init|=
name|PathSeparator
operator|.
name|UNIX
decl_stmt|;
annotation|@
name|UriParam
DECL|field|streamDownload
specifier|private
name|boolean
name|streamDownload
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|useList
specifier|private
name|boolean
name|useList
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ignoreFileNotFoundOrPermissionError
specifier|private
name|boolean
name|ignoreFileNotFoundOrPermissionError
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|sendNoop
specifier|private
name|boolean
name|sendNoop
init|=
literal|true
decl_stmt|;
DECL|method|RemoteFileConfiguration ()
specifier|public
name|RemoteFileConfiguration
parameter_list|()
block|{     }
DECL|method|RemoteFileConfiguration (URI uri)
specifier|public
name|RemoteFileConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|configure
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|needToNormalize ()
specifier|public
name|boolean
name|needToNormalize
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|configure (URI uri)
specifier|public
name|void
name|configure
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|super
operator|.
name|configure
argument_list|(
name|uri
argument_list|)
expr_stmt|;
comment|// after configure the directory has been resolved, so we can use it for directoryName
comment|// (directoryName is the name we use in the other file components, to use consistent name)
name|setDirectoryName
argument_list|(
name|getDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|setProtocol
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|setDefaultPort
argument_list|()
expr_stmt|;
comment|// UserInfo can contain both username and password as: user:pwd@ftpserver
comment|// see: http://en.wikipedia.org/wiki/URI_scheme
name|String
name|username
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
decl_stmt|;
name|String
name|pw
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|username
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|pw
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|username
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
name|username
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|username
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pw
operator|!=
literal|null
condition|)
block|{
name|setPassword
argument_list|(
name|pw
argument_list|)
expr_stmt|;
block|}
name|setHost
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|setPort
argument_list|(
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns human readable server information for logging purpose      */
DECL|method|remoteServerInformation ()
specifier|public
name|String
name|remoteServerInformation
parameter_list|()
block|{
return|return
name|protocol
operator|+
literal|"://"
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
literal|"@"
operator|+
name|host
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
return|;
block|}
DECL|method|setDefaultPort ()
specifier|protected
specifier|abstract
name|void
name|setDefaultPort
parameter_list|()
function_decl|;
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * Hostname of the FTP server      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Port of the FTP server      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
comment|// only set port if provided with a positive number
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * Password to use for login      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
comment|/**      * The ftp protocol to use      */
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * Username to use for login      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getDirectoryName ()
specifier|public
name|String
name|getDirectoryName
parameter_list|()
block|{
return|return
name|directoryName
return|;
block|}
comment|/**      * The starting directory      */
DECL|method|setDirectoryName (String directoryName)
specifier|public
name|void
name|setDirectoryName
parameter_list|(
name|String
name|directoryName
parameter_list|)
block|{
name|this
operator|.
name|directoryName
operator|=
name|directoryName
expr_stmt|;
block|}
DECL|method|isBinary ()
specifier|public
name|boolean
name|isBinary
parameter_list|()
block|{
return|return
name|binary
return|;
block|}
DECL|method|setBinary (boolean binary)
specifier|public
name|void
name|setBinary
parameter_list|(
name|boolean
name|binary
parameter_list|)
block|{
name|this
operator|.
name|binary
operator|=
name|binary
expr_stmt|;
block|}
DECL|method|isPassiveMode ()
specifier|public
name|boolean
name|isPassiveMode
parameter_list|()
block|{
return|return
name|passiveMode
return|;
block|}
comment|/**      * Sets passive mode connections.      *<br/>      * Default is active mode connections.      */
DECL|method|setPassiveMode (boolean passiveMode)
specifier|public
name|void
name|setPassiveMode
parameter_list|(
name|boolean
name|passiveMode
parameter_list|)
block|{
name|this
operator|.
name|passiveMode
operator|=
name|passiveMode
expr_stmt|;
block|}
DECL|method|getConnectTimeout ()
specifier|public
name|int
name|getConnectTimeout
parameter_list|()
block|{
return|return
name|connectTimeout
return|;
block|}
comment|/**      * Sets the connect timeout for waiting for a connection to be established      *<p/>      * Used by both FTPClient and JSCH      */
DECL|method|setConnectTimeout (int connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|int
name|connectTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectTimeout
operator|=
name|connectTimeout
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Sets the data timeout for waiting for reply      *<p/>      * Used only by FTPClient      */
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getSoTimeout ()
specifier|public
name|int
name|getSoTimeout
parameter_list|()
block|{
return|return
name|soTimeout
return|;
block|}
comment|/**      * Sets the so timeout      *<p/>      * Used only by FTPClient      */
DECL|method|setSoTimeout (int soTimeout)
specifier|public
name|void
name|setSoTimeout
parameter_list|(
name|int
name|soTimeout
parameter_list|)
block|{
name|this
operator|.
name|soTimeout
operator|=
name|soTimeout
expr_stmt|;
block|}
DECL|method|getReceiveBufferSize ()
specifier|public
name|int
name|getReceiveBufferSize
parameter_list|()
block|{
return|return
name|receiveBufferSize
return|;
block|}
comment|/**      * The receive (download) buffer size      *<p/>      * Used only by FTPClient      */
DECL|method|setReceiveBufferSize (int receiveBufferSize)
specifier|public
name|void
name|setReceiveBufferSize
parameter_list|(
name|int
name|receiveBufferSize
parameter_list|)
block|{
name|this
operator|.
name|receiveBufferSize
operator|=
name|receiveBufferSize
expr_stmt|;
block|}
DECL|method|isThrowExceptionOnConnectFailed ()
specifier|public
name|boolean
name|isThrowExceptionOnConnectFailed
parameter_list|()
block|{
return|return
name|throwExceptionOnConnectFailed
return|;
block|}
comment|/**      * Should an exception be thrown if connection failed (exhausted)      *<p/>      * By default exception is not thrown and a<tt>WARN</tt> is logged.      * You can use this to enable exception being thrown and handle the thrown exception      * from the {@link org.apache.camel.spi.PollingConsumerPollStrategy} rollback method.      */
DECL|method|setThrowExceptionOnConnectFailed (boolean throwExceptionOnConnectFailed)
specifier|public
name|void
name|setThrowExceptionOnConnectFailed
parameter_list|(
name|boolean
name|throwExceptionOnConnectFailed
parameter_list|)
block|{
name|this
operator|.
name|throwExceptionOnConnectFailed
operator|=
name|throwExceptionOnConnectFailed
expr_stmt|;
block|}
DECL|method|getSiteCommand ()
specifier|public
name|String
name|getSiteCommand
parameter_list|()
block|{
return|return
name|siteCommand
return|;
block|}
comment|/**      * Sets optional site command(s) to be executed after successful login.      *<p/>      * Multiple site commands can be separated using a new line character (\n).      *      * @param siteCommand the site command(s).      */
DECL|method|setSiteCommand (String siteCommand)
specifier|public
name|void
name|setSiteCommand
parameter_list|(
name|String
name|siteCommand
parameter_list|)
block|{
name|this
operator|.
name|siteCommand
operator|=
name|siteCommand
expr_stmt|;
block|}
DECL|method|isStepwise ()
specifier|public
name|boolean
name|isStepwise
parameter_list|()
block|{
return|return
name|stepwise
return|;
block|}
comment|/**      * Sets whether we should stepwise change directories while traversing file structures      * when downloading files, or as well when uploading a file to a directory.      *<p/>      * You can disable this if you for example are in a situation where you cannot change directory      * on the FTP server due security reasons.      *      * @param stepwise whether to use change directory or not      */
DECL|method|setStepwise (boolean stepwise)
specifier|public
name|void
name|setStepwise
parameter_list|(
name|boolean
name|stepwise
parameter_list|)
block|{
name|this
operator|.
name|stepwise
operator|=
name|stepwise
expr_stmt|;
block|}
DECL|method|getSeparator ()
specifier|public
name|PathSeparator
name|getSeparator
parameter_list|()
block|{
return|return
name|separator
return|;
block|}
comment|/**      * Sets the path separator to be used.      *<p/>      * UNIX = Uses unix style path separator      * Windows = Uses windows style path separator      * Auto = (is default) Use existing path separator in file name      */
DECL|method|setSeparator (PathSeparator separator)
specifier|public
name|void
name|setSeparator
parameter_list|(
name|PathSeparator
name|separator
parameter_list|)
block|{
name|this
operator|.
name|separator
operator|=
name|separator
expr_stmt|;
block|}
DECL|method|isStreamDownload ()
specifier|public
name|boolean
name|isStreamDownload
parameter_list|()
block|{
return|return
name|streamDownload
return|;
block|}
comment|/**      * Sets the download method to use when not using a local working directory.  If set to true,      * the remote files are streamed to the route as they are read.  When set to false, the remote files      * are loaded into memory before being sent into the route.      */
DECL|method|setStreamDownload (boolean streamDownload)
specifier|public
name|void
name|setStreamDownload
parameter_list|(
name|boolean
name|streamDownload
parameter_list|)
block|{
name|this
operator|.
name|streamDownload
operator|=
name|streamDownload
expr_stmt|;
block|}
DECL|method|isUseList ()
specifier|public
name|boolean
name|isUseList
parameter_list|()
block|{
return|return
name|useList
return|;
block|}
comment|/**      * Whether to allow using LIST command when downloading a file.      *<p/>      * Default is<tt>true</tt>. In some use cases you may want to download      * a specific file and are not allowed to use the LIST command, and therefore      * you can set this option to<tt>false</tt>.      */
DECL|method|setUseList (boolean useList)
specifier|public
name|void
name|setUseList
parameter_list|(
name|boolean
name|useList
parameter_list|)
block|{
name|this
operator|.
name|useList
operator|=
name|useList
expr_stmt|;
block|}
DECL|method|isIgnoreFileNotFoundOrPermissionError ()
specifier|public
name|boolean
name|isIgnoreFileNotFoundOrPermissionError
parameter_list|()
block|{
return|return
name|ignoreFileNotFoundOrPermissionError
return|;
block|}
comment|/**      * Whether to ignore when trying to download a file which does not exist or due to permission error.      *<p/>      * By default when a file does not exists or insufficient permission, then an exception is thrown.      * Setting this option to<tt>true</tt> allows to ignore that instead.      */
DECL|method|setIgnoreFileNotFoundOrPermissionError (boolean ignoreFileNotFoundOrPermissionError)
specifier|public
name|void
name|setIgnoreFileNotFoundOrPermissionError
parameter_list|(
name|boolean
name|ignoreFileNotFoundOrPermissionError
parameter_list|)
block|{
name|this
operator|.
name|ignoreFileNotFoundOrPermissionError
operator|=
name|ignoreFileNotFoundOrPermissionError
expr_stmt|;
block|}
DECL|method|isSendNoop ()
specifier|public
name|boolean
name|isSendNoop
parameter_list|()
block|{
return|return
name|sendNoop
return|;
block|}
comment|/**      * Whether to send a noop command as a pre-write check before uploading files to the FTP server.      *<p/>      * This is enabled by default as a validation of the connection is still valid, which allows to silently      * re-connect to be able to upload the file. However if this causes problems, you can turn this option off.      */
DECL|method|setSendNoop (boolean sendNoop)
specifier|public
name|void
name|setSendNoop
parameter_list|(
name|boolean
name|sendNoop
parameter_list|)
block|{
name|this
operator|.
name|sendNoop
operator|=
name|sendNoop
expr_stmt|;
block|}
comment|/**      * Normalizes the given path according to the configured path separator.      *      * @param path  the given path      * @return the normalized path      */
DECL|method|normalizePath (String path)
specifier|public
name|String
name|normalizePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|path
argument_list|)
operator|||
name|separator
operator|==
name|PathSeparator
operator|.
name|Auto
condition|)
block|{
return|return
name|path
return|;
block|}
if|if
condition|(
name|separator
operator|==
name|PathSeparator
operator|.
name|UNIX
condition|)
block|{
comment|// unix style
return|return
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
else|else
block|{
comment|// windows style
return|return
name|path
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'\\'
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

