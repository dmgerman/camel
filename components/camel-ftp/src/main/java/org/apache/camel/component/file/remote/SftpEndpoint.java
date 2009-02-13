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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_comment
comment|/**  * Secure FTP endpoint  */
end_comment

begin_class
DECL|class|SftpEndpoint
specifier|public
class|class
name|SftpEndpoint
extends|extends
name|RemoteFileEndpoint
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
block|{
DECL|method|SftpEndpoint ()
specifier|public
name|SftpEndpoint
parameter_list|()
block|{
name|SftpOperations
name|operations
init|=
operator|new
name|SftpOperations
argument_list|()
decl_stmt|;
name|operations
operator|.
name|setEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|operations
operator|=
name|operations
expr_stmt|;
block|}
DECL|method|SftpEndpoint (String uri, SftpComponent component, RemoteFileOperations<ChannelSftp.LsEntry> operations, RemoteFileConfiguration configuration)
specifier|public
name|SftpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SftpComponent
name|component
parameter_list|,
name|RemoteFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|operations
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
name|operations
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|buildConsumer (Processor processor, RemoteFileOperations<ChannelSftp.LsEntry> operations)
specifier|protected
name|RemoteFileConsumer
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|buildConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|RemoteFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|operations
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
name|operations
argument_list|)
return|;
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

