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
name|util
operator|.
name|IntrospectionSupport
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
comment|/**  * FTP endpoint  */
end_comment

begin_class
DECL|class|FtpEndpoint
specifier|public
class|class
name|FtpEndpoint
extends|extends
name|RemoteFileEndpoint
argument_list|<
name|FTPFile
argument_list|>
block|{
DECL|field|ftpClient
specifier|private
name|FTPClient
name|ftpClient
decl_stmt|;
DECL|field|ftpClientConfig
specifier|private
name|FTPClientConfig
name|ftpClientConfig
decl_stmt|;
DECL|field|ftpClientParameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientParameters
decl_stmt|;
DECL|field|ftpClientConfigParameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientConfigParameters
decl_stmt|;
DECL|method|FtpEndpoint ()
specifier|public
name|FtpEndpoint
parameter_list|()
block|{     }
DECL|method|FtpEndpoint (String uri, FtpComponent component, RemoteFileConfiguration configuration)
specifier|public
name|FtpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|FtpComponent
name|component
parameter_list|,
name|RemoteFileConfiguration
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
return|return
operator|new
name|RemoteFileProducer
argument_list|<
name|FTPFile
argument_list|>
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
DECL|method|createRemoteFileOperations ()
specifier|protected
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
operator|new
name|FTPClient
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ftpClientParameters
operator|!=
literal|null
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|client
argument_list|,
name|ftpClientParameters
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
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|ftpClientConfig
argument_list|,
name|ftpClientConfigParameters
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
name|ftpClientConfigParameters
expr_stmt|;
block|}
block|}
end_class

end_unit

