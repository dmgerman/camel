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
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|binary
specifier|private
name|boolean
name|binary
decl_stmt|;
DECL|field|passiveMode
specifier|private
name|boolean
name|passiveMode
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
name|setUsername
argument_list|(
name|uri
operator|.
name|getUserInfo
argument_list|()
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|setDirectory (String directory)
specifier|public
name|void
name|setDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
comment|// let super do its work first
name|super
operator|.
name|setDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
comment|// for FTP we must not start with a / root, so skip it if its there
if|if
condition|(
name|getDirectory
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|setDirectory
argument_list|(
name|getDirectory
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

