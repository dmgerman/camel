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
name|Processor
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

begin_class
DECL|class|SftpEndpoint
specifier|public
class|class
name|SftpEndpoint
extends|extends
name|RemoteFileEndpoint
argument_list|<
name|RemoteFileExchange
argument_list|>
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|SftpEndpoint (String uri, RemoteFileComponent remoteFileComponent, RemoteFileConfiguration configuration)
specifier|public
name|SftpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|RemoteFileComponent
name|remoteFileComponent
parameter_list|,
name|RemoteFileConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|remoteFileComponent
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
DECL|method|SftpEndpoint (String endpointUri)
specifier|public
name|SftpEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|SftpProducer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SftpProducer
argument_list|(
name|this
argument_list|,
name|createSession
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|SftpConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|SftpConsumer
name|consumer
init|=
operator|new
name|SftpConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|createSession
argument_list|()
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createSession ()
specifier|protected
name|Session
name|createSession
parameter_list|()
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
name|String
name|privateKeyFile
init|=
name|getConfiguration
argument_list|()
operator|.
name|getPrivateKeyFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|privateKeyFile
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using private keyfile: "
operator|+
name|privateKeyFile
argument_list|)
expr_stmt|;
name|String
name|privateKeyFilePassphrase
init|=
name|getConfiguration
argument_list|()
operator|.
name|getPrivateKeyFilePassphrase
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|privateKeyFilePassphrase
argument_list|)
condition|)
block|{
name|jsch
operator|.
name|addIdentity
argument_list|(
name|privateKeyFile
argument_list|,
name|privateKeyFilePassphrase
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jsch
operator|.
name|addIdentity
argument_list|(
name|privateKeyFile
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|knownHostsFile
init|=
name|getConfiguration
argument_list|()
operator|.
name|getKnownHosts
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|knownHostsFile
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using knownHosts: "
operator|+
name|knownHostsFile
argument_list|)
expr_stmt|;
name|jsch
operator|.
name|setKnownHosts
argument_list|(
name|knownHostsFile
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
name|getConfiguration
argument_list|()
operator|.
name|getUsername
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
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
name|getConfiguration
argument_list|()
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
name|string
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
name|string
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
name|string
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|string
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
name|string
parameter_list|)
block|{             }
block|}
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
DECL|method|createChannelSftp (Session session)
specifier|public
name|ChannelSftp
name|createChannelSftp
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JSchException
block|{
specifier|final
name|ChannelSftp
name|channel
init|=
operator|(
name|ChannelSftp
operator|)
name|session
operator|.
name|openChannel
argument_list|(
literal|"sftp"
argument_list|)
decl_stmt|;
return|return
name|channel
return|;
block|}
block|}
end_class

end_unit

