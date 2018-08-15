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
name|component
operator|.
name|file
operator|.
name|GenericFileEndpoint
import|;
end_import

begin_comment
comment|/**  * Secure FTP Component  */
end_comment

begin_class
DECL|class|SftpComponent
specifier|public
class|class
name|SftpComponent
extends|extends
name|RemoteFileComponent
argument_list|<
name|SftpRemoteFile
argument_list|>
block|{
DECL|method|SftpComponent ()
specifier|public
name|SftpComponent
parameter_list|()
block|{
name|setEndpointClass
argument_list|(
name|SftpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|SftpComponent (CamelContext context)
specifier|public
name|SftpComponent
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
name|setEndpointClass
argument_list|(
name|SftpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|buildFileEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|GenericFileEndpoint
argument_list|<
name|SftpRemoteFile
argument_list|>
name|buildFileEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// get the base uri part before the options as they can be non URI valid such as the expression using $ chars
comment|// and the URI constructor will regard $ as an illegal character and we dont want to enforce end users to
comment|// to escape the $ for the expression (file language)
name|String
name|baseUri
init|=
name|uri
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|baseUri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|indexOf
argument_list|(
literal|"?"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// lets make sure we create a new configuration as each endpoint can
comment|// customize its own version
name|SftpConfiguration
name|config
init|=
operator|new
name|SftpConfiguration
argument_list|(
operator|new
name|URI
argument_list|(
name|baseUri
argument_list|)
argument_list|)
decl_stmt|;
name|FtpUtils
operator|.
name|ensureRelativeFtpDirectory
argument_list|(
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
return|return
operator|new
name|SftpEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|afterPropertiesSet (GenericFileEndpoint<SftpRemoteFile> endpoint)
specifier|protected
name|void
name|afterPropertiesSet
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|SftpRemoteFile
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

