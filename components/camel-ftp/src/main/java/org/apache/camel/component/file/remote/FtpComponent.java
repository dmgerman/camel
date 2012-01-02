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
name|FTPFile
import|;
end_import

begin_comment
comment|/**  * FTP Component  */
end_comment

begin_class
DECL|class|FtpComponent
specifier|public
class|class
name|FtpComponent
extends|extends
name|RemoteFileComponent
argument_list|<
name|FTPFile
argument_list|>
block|{
DECL|method|FtpComponent ()
specifier|public
name|FtpComponent
parameter_list|()
block|{     }
DECL|method|FtpComponent (CamelContext context)
specifier|public
name|FtpComponent
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
block|}
annotation|@
name|Override
DECL|method|buildFileEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|GenericFileEndpoint
argument_list|<
name|FTPFile
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
name|String
name|baseUri
init|=
name|getBaseUri
argument_list|(
name|uri
argument_list|)
decl_stmt|;
comment|// lets make sure we create a new configuration as each endpoint can customize its own version
comment|// must pass on baseUri to the configuration (see above)
name|FtpConfiguration
name|config
init|=
operator|new
name|FtpConfiguration
argument_list|(
operator|new
name|URI
argument_list|(
name|baseUri
argument_list|)
argument_list|)
decl_stmt|;
name|FtpEndpoint
argument_list|<
name|FTPFile
argument_list|>
name|answer
init|=
operator|new
name|FtpEndpoint
argument_list|<
name|FTPFile
argument_list|>
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|extractAndSetFtpClientConfigParameters
argument_list|(
name|parameters
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|extractAndSetFtpClientParameters
argument_list|(
name|parameters
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Get the base uri part before the options as they can be non URI valid such as the expression using $ chars      * and the URI constructor will regard $ as an illegal character and we don't want to enforce end users to      * to escape the $ for the expression (file language)      */
DECL|method|getBaseUri (String uri)
specifier|protected
name|String
name|getBaseUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|String
name|baseUri
init|=
name|uri
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|indexOf
argument_list|(
literal|"?"
argument_list|)
operator|!=
operator|-
literal|1
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
return|return
name|baseUri
return|;
block|}
comment|/**      * Extract additional ftp client configuration options from the parameters map (parameters starting with       * 'ftpClientConfig.'). To remember these parameters, we set them in the endpoint and we can use them       * when creating a client.      */
DECL|method|extractAndSetFtpClientConfigParameters (Map<String, Object> parameters, FtpEndpoint<FTPFile> answer)
specifier|protected
name|void
name|extractAndSetFtpClientConfigParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|FtpEndpoint
argument_list|<
name|FTPFile
argument_list|>
name|answer
parameter_list|)
block|{
if|if
condition|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|parameters
argument_list|,
literal|"ftpClientConfig."
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"ftpClientConfig."
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setFtpClientConfigParameters
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Extract additional ftp client options from the parameters map (parameters starting with       * 'ftpClient.'). To remember these parameters, we set them in the endpoint and we can use them       * when creating a client.      */
DECL|method|extractAndSetFtpClientParameters (Map<String, Object> parameters, FtpEndpoint<FTPFile> answer)
specifier|protected
name|void
name|extractAndSetFtpClientParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|FtpEndpoint
argument_list|<
name|FTPFile
argument_list|>
name|answer
parameter_list|)
block|{
if|if
condition|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|parameters
argument_list|,
literal|"ftpClient."
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"ftpClient."
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setFtpClientParameters
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|afterPropertiesSet (GenericFileEndpoint<FTPFile> endpoint)
specifier|protected
name|void
name|afterPropertiesSet
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|FTPFile
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

