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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|FailedToCreateConsumerException
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
name|FailedToCreateProducerException
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
name|LoggingLevel
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
name|component
operator|.
name|file
operator|.
name|GenericFileProducer
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
operator|.
name|PathSeparator
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
name|strategy
operator|.
name|FileMoveExistingStrategy
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
name|ClassResolver
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
name|UriEndpoint
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
name|support
operator|.
name|PlatformHelper
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
name|FTPClientConfig
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
comment|/**  * The ftp component is used for uploading or downloading files from FTP servers.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.1.0"
argument_list|,
name|scheme
operator|=
literal|"ftp"
argument_list|,
name|extendsScheme
operator|=
literal|"file"
argument_list|,
name|title
operator|=
literal|"FTP"
argument_list|,
name|syntax
operator|=
literal|"ftp:host:port/directoryName"
argument_list|,
name|alternativeSyntax
operator|=
literal|"ftp:username:password@host:port/directoryName"
argument_list|,
name|label
operator|=
literal|"file"
argument_list|,
name|excludeProperties
operator|=
literal|"readLockIdempotentReleaseAsync,readLockIdempotentReleaseAsyncPoolSize,readLockIdempotentReleaseDelay,readLockIdempotentReleaseExecutorService"
argument_list|)
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed FtpEndpoint"
argument_list|)
DECL|class|FtpEndpoint
specifier|public
class|class
name|FtpEndpoint
parameter_list|<
name|T
extends|extends
name|FTPFile
parameter_list|>
extends|extends
name|RemoteFileEndpoint
argument_list|<
name|FTPFile
argument_list|>
block|{
DECL|field|soTimeout
specifier|protected
name|int
name|soTimeout
decl_stmt|;
DECL|field|dataTimeout
specifier|protected
name|int
name|dataTimeout
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|protected
name|FtpConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|ftpClientConfig
specifier|protected
name|FTPClientConfig
name|ftpClientConfig
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|prefix
operator|=
literal|"ftpClientConfig."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|ftpClientConfigParameters
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientConfigParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|prefix
operator|=
literal|"ftpClient."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|ftpClientParameters
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|ftpClient
specifier|protected
name|FTPClient
name|ftpClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"DEBUG"
argument_list|)
DECL|field|transferLoggingLevel
specifier|protected
name|LoggingLevel
name|transferLoggingLevel
init|=
name|LoggingLevel
operator|.
name|DEBUG
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"5"
argument_list|)
DECL|field|transferLoggingIntervalSeconds
specifier|protected
name|int
name|transferLoggingIntervalSeconds
init|=
literal|5
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|transferLoggingVerbose
specifier|protected
name|boolean
name|transferLoggingVerbose
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|resumeDownload
specifier|protected
name|boolean
name|resumeDownload
decl_stmt|;
DECL|method|FtpEndpoint ()
specifier|public
name|FtpEndpoint
parameter_list|()
block|{     }
DECL|method|FtpEndpoint (String uri, RemoteFileComponent<FTPFile> component, FtpConfiguration configuration)
specifier|public
name|FtpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|RemoteFileComponent
argument_list|<
name|FTPFile
argument_list|>
name|component
parameter_list|,
name|FtpConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
literal|"ftp"
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|RemoteFileConsumer
argument_list|<
name|FTPFile
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|isResumeDownload
argument_list|()
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getLocalWorkDirectory
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The option localWorkDirectory must be configured when resumeDownload=true"
argument_list|)
throw|;
block|}
if|if
condition|(
name|isResumeDownload
argument_list|()
operator|&&
operator|!
name|getConfiguration
argument_list|()
operator|.
name|isBinary
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The option binary must be enabled when resumeDownload=true"
argument_list|)
throw|;
block|}
return|return
name|super
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildConsumer (Processor processor)
specifier|protected
name|RemoteFileConsumer
argument_list|<
name|FTPFile
argument_list|>
name|buildConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|FtpConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|createRemoteFileOperations
argument_list|()
argument_list|,
name|processStrategy
operator|!=
literal|null
condition|?
name|processStrategy
else|:
name|createGenericFileStrategy
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateConsumerException
argument_list|(
name|this
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|buildProducer ()
specifier|protected
name|GenericFileProducer
argument_list|<
name|FTPFile
argument_list|>
name|buildProducer
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|this
operator|.
name|getMoveExistingFileStrategy
argument_list|()
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|setMoveExistingFileStrategy
argument_list|(
name|createDefaultFtpMoveExistingFileStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|RemoteFileProducer
argument_list|<>
argument_list|(
name|this
argument_list|,
name|createRemoteFileOperations
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateProducerException
argument_list|(
name|this
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Default Existing File Move Strategy      * @return the default implementation for ftp components      */
DECL|method|createDefaultFtpMoveExistingFileStrategy ()
specifier|private
name|FileMoveExistingStrategy
name|createDefaultFtpMoveExistingFileStrategy
parameter_list|()
block|{
return|return
operator|new
name|FtpDefaultMoveExistingFileStrategy
argument_list|()
return|;
block|}
DECL|method|createRemoteFileOperations ()
specifier|public
name|RemoteFileOperations
argument_list|<
name|FTPFile
argument_list|>
name|createRemoteFileOperations
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure ftp client
name|FTPClient
name|client
init|=
name|ftpClient
decl_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
comment|// must use a new client if not explicit configured to use a custom client
name|client
operator|=
name|createFtpClient
argument_list|()
expr_stmt|;
block|}
comment|// use configured buffer size which is larger and therefore faster (as the default is no buffer)
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveBufferSize
argument_list|()
operator|>
literal|0
condition|)
block|{
name|client
operator|.
name|setBufferSize
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveBufferSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// set any endpoint configured timeouts
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getConnectTimeout
argument_list|()
operator|>
operator|-
literal|1
condition|)
block|{
name|client
operator|.
name|setConnectTimeout
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getConnectTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getSoTimeout
argument_list|()
operator|>
operator|-
literal|1
condition|)
block|{
name|soTimeout
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getSoTimeout
argument_list|()
expr_stmt|;
block|}
name|dataTimeout
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getTimeout
argument_list|()
expr_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getActivePortRange
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// parse it as min-max
name|String
index|[]
name|parts
init|=
name|getConfiguration
argument_list|()
operator|.
name|getActivePortRange
argument_list|()
operator|.
name|split
argument_list|(
literal|"-"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The option activePortRange should have syntax: min-max"
argument_list|)
throw|;
block|}
name|int
name|min
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|parts
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|int
name|max
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using active port range: {}-{}"
argument_list|,
name|min
argument_list|,
name|max
argument_list|)
expr_stmt|;
name|client
operator|.
name|setActivePortRange
argument_list|(
name|min
argument_list|,
name|max
argument_list|)
expr_stmt|;
block|}
comment|// then lookup ftp client parameters and set those
if|if
condition|(
name|ftpClientParameters
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|localParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|ftpClientParameters
argument_list|)
decl_stmt|;
comment|// setting soTimeout has to be done later on FTPClient (after it has connected)
name|Object
name|timeout
init|=
name|localParameters
operator|.
name|remove
argument_list|(
literal|"soTimeout"
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
name|soTimeout
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
comment|// and we want to keep data timeout so we can log it later
name|timeout
operator|=
name|localParameters
operator|.
name|remove
argument_list|(
literal|"dataTimeout"
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
name|dataTimeout
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|client
argument_list|,
name|localParameters
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ftpClientConfigParameters
operator|!=
literal|null
condition|)
block|{
comment|// client config is optional so create a new one if we have parameter for it
if|if
condition|(
name|ftpClientConfig
operator|==
literal|null
condition|)
block|{
name|ftpClientConfig
operator|=
operator|new
name|FTPClientConfig
argument_list|()
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|localConfigParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|ftpClientConfigParameters
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|ftpClientConfig
argument_list|,
name|localConfigParameters
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dataTimeout
operator|>
literal|0
condition|)
block|{
name|client
operator|.
name|setDataTimeout
argument_list|(
name|dataTimeout
argument_list|)
expr_stmt|;
block|}
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
literal|"Created FTPClient [connectTimeout: {}, soTimeout: {}, dataTimeout: {}, bufferSize: {}"
operator|+
literal|", receiveDataSocketBufferSize: {}, sendDataSocketBufferSize: {}]: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|client
operator|.
name|getConnectTimeout
argument_list|()
block|,
name|getSoTimeout
argument_list|()
block|,
name|dataTimeout
block|,
name|client
operator|.
name|getBufferSize
argument_list|()
block|,
name|client
operator|.
name|getReceiveDataSocketBufferSize
argument_list|()
block|,
name|client
operator|.
name|getSendDataSocketBufferSize
argument_list|()
block|,
name|client
block|}
argument_list|)
expr_stmt|;
block|}
name|FtpOperations
name|operations
init|=
operator|new
name|FtpOperations
argument_list|(
name|client
argument_list|,
name|getFtpClientConfig
argument_list|()
argument_list|)
decl_stmt|;
name|operations
operator|.
name|setEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|operations
return|;
block|}
DECL|method|createFtpClient ()
specifier|protected
name|FTPClient
name|createFtpClient
parameter_list|()
throws|throws
name|Exception
block|{
name|FTPClient
name|client
init|=
operator|new
name|FTPClient
argument_list|()
decl_stmt|;
comment|// If we're in an OSGI environment, set the parser factory to
comment|// OsgiParserFactory, because commons-net uses Class.forName in their
comment|// default ParserFactory
if|if
condition|(
name|isOsgi
argument_list|()
condition|)
block|{
name|ClassResolver
name|cr
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
decl_stmt|;
name|OsgiParserFactory
name|opf
init|=
operator|new
name|OsgiParserFactory
argument_list|(
name|cr
argument_list|)
decl_stmt|;
name|client
operator|.
name|setParserFactory
argument_list|(
name|opf
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|isOsgi ()
specifier|private
name|boolean
name|isOsgi
parameter_list|()
block|{
return|return
name|PlatformHelper
operator|.
name|isOsgiContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|FtpConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|FtpConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|setConfiguration (GenericFileConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GenericFileConfiguration
name|configuration
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"FtpConfiguration expected"
argument_list|)
throw|;
block|}
comment|// need to set on both
name|this
operator|.
name|configuration
operator|=
operator|(
name|FtpConfiguration
operator|)
name|configuration
expr_stmt|;
name|super
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
DECL|method|getFtpClient ()
specifier|public
name|FTPClient
name|getFtpClient
parameter_list|()
block|{
return|return
name|ftpClient
return|;
block|}
comment|/**      * To use a custom instance of FTPClient      */
DECL|method|setFtpClient (FTPClient ftpClient)
specifier|public
name|void
name|setFtpClient
parameter_list|(
name|FTPClient
name|ftpClient
parameter_list|)
block|{
name|this
operator|.
name|ftpClient
operator|=
name|ftpClient
expr_stmt|;
block|}
DECL|method|getFtpClientConfig ()
specifier|public
name|FTPClientConfig
name|getFtpClientConfig
parameter_list|()
block|{
return|return
name|ftpClientConfig
return|;
block|}
comment|/**      * To use a custom instance of FTPClientConfig to configure the FTP client the endpoint should use.      */
DECL|method|setFtpClientConfig (FTPClientConfig ftpClientConfig)
specifier|public
name|void
name|setFtpClientConfig
parameter_list|(
name|FTPClientConfig
name|ftpClientConfig
parameter_list|)
block|{
name|this
operator|.
name|ftpClientConfig
operator|=
name|ftpClientConfig
expr_stmt|;
block|}
comment|/**      * Used by FtpComponent to provide additional parameters for the FTPClient      */
DECL|method|setFtpClientParameters (Map<String, Object> ftpClientParameters)
name|void
name|setFtpClientParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientParameters
parameter_list|)
block|{
name|this
operator|.
name|ftpClientParameters
operator|=
name|ftpClientParameters
expr_stmt|;
block|}
comment|/**      * Used by FtpComponent to provide additional parameters for the FTPClientConfig      */
DECL|method|setFtpClientConfigParameters (Map<String, Object> ftpClientConfigParameters)
name|void
name|setFtpClientConfigParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientConfigParameters
parameter_list|)
block|{
name|this
operator|.
name|ftpClientConfigParameters
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|ftpClientConfigParameters
argument_list|)
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
comment|/**      * Sets the soTimeout on the FTP client.      */
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
DECL|method|getDataTimeout ()
specifier|public
name|int
name|getDataTimeout
parameter_list|()
block|{
return|return
name|dataTimeout
return|;
block|}
comment|/**      * Sets the data timeout on the FTP client.      */
DECL|method|setDataTimeout (int dataTimeout)
specifier|public
name|void
name|setDataTimeout
parameter_list|(
name|int
name|dataTimeout
parameter_list|)
block|{
name|this
operator|.
name|dataTimeout
operator|=
name|dataTimeout
expr_stmt|;
block|}
DECL|method|getTransferLoggingLevel ()
specifier|public
name|LoggingLevel
name|getTransferLoggingLevel
parameter_list|()
block|{
return|return
name|transferLoggingLevel
return|;
block|}
comment|/**      * Configure the logging level to use when logging the progress of upload and download operations.      */
DECL|method|setTransferLoggingLevel (LoggingLevel transferLoggingLevel)
specifier|public
name|void
name|setTransferLoggingLevel
parameter_list|(
name|LoggingLevel
name|transferLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|transferLoggingLevel
operator|=
name|transferLoggingLevel
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging level to use when logging the progress of upload and download operations"
argument_list|)
DECL|method|setTransferLoggingLevelName (String transferLoggingLevel)
specifier|public
name|void
name|setTransferLoggingLevelName
parameter_list|(
name|String
name|transferLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|transferLoggingLevel
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
name|transferLoggingLevel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getTransferLoggingLevelName ()
specifier|public
name|String
name|getTransferLoggingLevelName
parameter_list|()
block|{
return|return
name|transferLoggingLevel
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getTransferLoggingIntervalSeconds ()
specifier|public
name|int
name|getTransferLoggingIntervalSeconds
parameter_list|()
block|{
return|return
name|transferLoggingIntervalSeconds
return|;
block|}
comment|/**      * Configures the interval in seconds to use when logging the progress of upload and download operations that are in-flight.      * This is used for logging progress when operations takes longer time.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Interval in seconds to use when logging the progress of upload and download operations that are in-flight"
argument_list|)
DECL|method|setTransferLoggingIntervalSeconds (int transferLoggingIntervalSeconds)
specifier|public
name|void
name|setTransferLoggingIntervalSeconds
parameter_list|(
name|int
name|transferLoggingIntervalSeconds
parameter_list|)
block|{
name|this
operator|.
name|transferLoggingIntervalSeconds
operator|=
name|transferLoggingIntervalSeconds
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isTransferLoggingVerbose ()
specifier|public
name|boolean
name|isTransferLoggingVerbose
parameter_list|()
block|{
return|return
name|transferLoggingVerbose
return|;
block|}
comment|/**      * Configures whether the perform verbose (fine grained) logging of the progress of upload and download operations.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether the perform verbose (fine grained) logging of the progress of upload and download operations"
argument_list|)
DECL|method|setTransferLoggingVerbose (boolean transferLoggingVerbose)
specifier|public
name|void
name|setTransferLoggingVerbose
parameter_list|(
name|boolean
name|transferLoggingVerbose
parameter_list|)
block|{
name|this
operator|.
name|transferLoggingVerbose
operator|=
name|transferLoggingVerbose
expr_stmt|;
block|}
DECL|method|isResumeDownload ()
specifier|public
name|boolean
name|isResumeDownload
parameter_list|()
block|{
return|return
name|resumeDownload
return|;
block|}
comment|/**      * Configures whether resume download is enabled. This must be supported by the FTP server (almost all FTP servers support it).      * In addition the options<tt>localWorkDirectory</tt> must be configured so downloaded files are stored in a local directory,      * and the option<tt>binary</tt> must be enabled, which is required to support resuming of downloads.      */
DECL|method|setResumeDownload (boolean resumeDownload)
specifier|public
name|void
name|setResumeDownload
parameter_list|(
name|boolean
name|resumeDownload
parameter_list|)
block|{
name|this
operator|.
name|resumeDownload
operator|=
name|resumeDownload
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getFileSeparator ()
specifier|public
name|char
name|getFileSeparator
parameter_list|()
block|{
comment|// the regular ftp component should use the configured separator
comment|// as FTP servers may require you to use windows or unix style
comment|// and therefore you need to be able to control that
name|PathSeparator
name|pathSeparator
init|=
name|getConfiguration
argument_list|()
operator|.
name|getSeparator
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|pathSeparator
condition|)
block|{
case|case
name|Windows
case|:
return|return
literal|'\\'
return|;
case|case
name|UNIX
case|:
return|return
literal|'/'
return|;
default|default:
return|return
name|super
operator|.
name|getFileSeparator
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

