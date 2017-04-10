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
name|SSLContextParametersAware
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
comment|/**  * FTP Secure (FTP over SSL/TLS) Component.  *<p/>  * If desired, the JVM property<tt>-Djavax.net.debug=all</tt> can be used to see wire-level SSL details.  *   * @version   */
end_comment

begin_class
DECL|class|FtpsComponent
specifier|public
class|class
name|FtpsComponent
extends|extends
name|FtpComponent
implements|implements
name|SSLContextParametersAware
block|{
DECL|method|FtpsComponent ()
specifier|public
name|FtpsComponent
parameter_list|()
block|{
name|setEndpointClass
argument_list|(
name|FtpsEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|FtpsComponent (CamelContext context)
specifier|public
name|FtpsComponent
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
name|FtpsEndpoint
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
name|FtpsConfiguration
name|config
init|=
operator|new
name|FtpsConfiguration
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
name|FtpsEndpoint
name|endpoint
init|=
operator|new
name|FtpsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|extractAndSetFtpClientKeyStoreParameters
argument_list|(
name|parameters
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|extractAndSetFtpClientTrustStoreParameters
argument_list|(
name|parameters
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|extractAndSetFtpClientConfigParameters
argument_list|(
name|parameters
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|extractAndSetFtpClientParameters
argument_list|(
name|parameters
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setSslContextParameters
argument_list|(
name|getGlobalSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
comment|/**      * Extract additional ftp client key store options from the parameters map (parameters starting with       * 'ftpClient.keyStore.'). To remember these parameters, we set them in the endpoint and we can use       * them when creating a client.      */
DECL|method|extractAndSetFtpClientKeyStoreParameters (Map<String, Object> parameters, FtpsEndpoint endpoint)
specifier|protected
name|void
name|extractAndSetFtpClientKeyStoreParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|FtpsEndpoint
name|endpoint
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
literal|"ftpClient.keyStore."
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
literal|"ftpClient.keyStore."
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setFtpClientKeyStoreParameters
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Extract additional ftp client trust store options from the parameters map (parameters starting with       * 'ftpClient.trustStore.'). To remember these parameters, we set them in the endpoint and we can use       * them when creating a client.      */
DECL|method|extractAndSetFtpClientTrustStoreParameters (Map<String, Object> parameters, FtpsEndpoint endpoint)
specifier|protected
name|void
name|extractAndSetFtpClientTrustStoreParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|FtpsEndpoint
name|endpoint
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
literal|"ftpClient.trustStore."
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
literal|"ftpClient.trustStore."
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setFtpClientTrustStoreParameters
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

