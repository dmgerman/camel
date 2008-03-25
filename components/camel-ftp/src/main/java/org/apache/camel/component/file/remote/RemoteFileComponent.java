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
name|CamelContext
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
name|RuntimeCamelException
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
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_class
DECL|class|RemoteFileComponent
specifier|public
class|class
name|RemoteFileComponent
extends|extends
name|DefaultComponent
argument_list|<
name|RemoteFileExchange
argument_list|>
block|{
DECL|field|configuration
specifier|private
name|RemoteFileConfiguration
name|configuration
decl_stmt|;
DECL|method|RemoteFileComponent ()
specifier|public
name|RemoteFileComponent
parameter_list|()
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|RemoteFileConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|RemoteFileComponent (RemoteFileConfiguration configuration)
specifier|public
name|RemoteFileComponent
parameter_list|(
name|RemoteFileConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|RemoteFileComponent (CamelContext context)
specifier|public
name|RemoteFileComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|RemoteFileConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RemoteFileComponent"
return|;
block|}
DECL|method|remoteFileComponent ()
specifier|public
specifier|static
name|RemoteFileComponent
name|remoteFileComponent
parameter_list|()
block|{
return|return
operator|new
name|RemoteFileComponent
argument_list|()
return|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|RemoteFileEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|RemoteFileConfiguration
name|config
init|=
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|config
operator|.
name|configure
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
comment|// lets make sure we copy the configuration as each endpoint can
comment|// customize its own version
specifier|final
name|RemoteFileEndpoint
name|endpoint
decl_stmt|;
if|if
condition|(
literal|"ftp"
operator|.
name|equals
argument_list|(
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|endpoint
operator|=
operator|new
name|FtpEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"sftp"
operator|.
name|equals
argument_list|(
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|endpoint
operator|=
operator|new
name|SftpEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unsupported protocol: "
operator|+
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
throw|;
block|}
name|setProperties
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|RemoteFileConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (RemoteFileConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|RemoteFileConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
block|}
end_class

end_unit

