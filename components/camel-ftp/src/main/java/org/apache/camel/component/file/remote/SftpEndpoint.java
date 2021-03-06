begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Proxy
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

begin_comment
comment|/**  *  The sftp (FTP over SSH) component is used for uploading or downloading files from SFTP servers.  */
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
literal|"sftp"
argument_list|,
name|extendsScheme
operator|=
literal|"file"
argument_list|,
name|title
operator|=
literal|"SFTP"
argument_list|,
name|syntax
operator|=
literal|"sftp:host:port/directoryName"
argument_list|,
name|label
operator|=
literal|"file"
argument_list|,
name|excludeProperties
operator|=
literal|"appendChars,binary,passiveMode,bufferSize,siteCommand"
argument_list|)
DECL|class|SftpEndpoint
specifier|public
class|class
name|SftpEndpoint
extends|extends
name|RemoteFileEndpoint
argument_list|<
name|SftpRemoteFile
argument_list|>
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|protected
name|SftpConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|proxy
specifier|protected
name|Proxy
name|proxy
decl_stmt|;
DECL|method|SftpEndpoint ()
specifier|public
name|SftpEndpoint
parameter_list|()
block|{     }
DECL|method|SftpEndpoint (String uri, SftpComponent component, SftpConfiguration configuration)
specifier|public
name|SftpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SftpComponent
name|component
parameter_list|,
name|SftpConfiguration
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
DECL|method|getConfiguration ()
specifier|public
name|SftpConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
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
literal|"SftpConfiguration expected"
argument_list|)
throw|;
block|}
comment|// need to set on both
name|this
operator|.
name|configuration
operator|=
operator|(
name|SftpConfiguration
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
annotation|@
name|Override
DECL|method|buildConsumer (Processor processor)
specifier|protected
name|RemoteFileConsumer
argument_list|<
name|SftpRemoteFile
argument_list|>
name|buildConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
operator|new
name|SftpConsumer
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
annotation|@
name|Override
DECL|method|buildProducer ()
specifier|protected
name|GenericFileProducer
argument_list|<
name|SftpRemoteFile
argument_list|>
name|buildProducer
parameter_list|()
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
name|createDefaultSftpMoveExistingFileStrategy
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
comment|/**      * Default Existing File Move Strategy      * @return the default implementation for sftp component      */
DECL|method|createDefaultSftpMoveExistingFileStrategy ()
specifier|private
name|FileMoveExistingStrategy
name|createDefaultSftpMoveExistingFileStrategy
parameter_list|()
block|{
return|return
operator|new
name|SftpDefaultMoveExistingFileStrategy
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createRemoteFileOperations ()
specifier|public
name|RemoteFileOperations
argument_list|<
name|SftpRemoteFile
argument_list|>
name|createRemoteFileOperations
parameter_list|()
block|{
name|SftpOperations
name|operations
init|=
operator|new
name|SftpOperations
argument_list|(
name|proxy
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
DECL|method|getProxy ()
specifier|public
name|Proxy
name|getProxy
parameter_list|()
block|{
return|return
name|proxy
return|;
block|}
comment|/**      * To use a custom configured com.jcraft.jsch.Proxy.      * This proxy is used to consume/send messages from the target SFTP host.      */
DECL|method|setProxy (Proxy proxy)
specifier|public
name|void
name|setProxy
parameter_list|(
name|Proxy
name|proxy
parameter_list|)
block|{
name|this
operator|.
name|proxy
operator|=
name|proxy
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
literal|"sftp"
return|;
block|}
block|}
end_class

end_unit

