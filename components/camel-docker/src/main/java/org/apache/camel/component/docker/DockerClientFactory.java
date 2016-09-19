begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|DockerClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|DockerCmdExecFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|core
operator|.
name|DefaultDockerClientConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|core
operator|.
name|DockerClientBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|core
operator|.
name|LocalDirectorySSLConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|core
operator|.
name|SSLConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|jaxrs
operator|.
name|JerseyDockerCmdExecFactory
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
name|Message
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
name|docker
operator|.
name|exception
operator|.
name|DockerException
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
name|docker
operator|.
name|ssl
operator|.
name|NoImplSslConfig
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

begin_comment
comment|/**  * Methods for communicating with Docker  */
end_comment

begin_class
DECL|class|DockerClientFactory
specifier|public
specifier|final
class|class
name|DockerClientFactory
block|{
DECL|method|DockerClientFactory ()
specifier|private
name|DockerClientFactory
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * Produces a {@link DockerClient} to communicate with Docker      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"resource"
argument_list|)
DECL|method|getDockerClient (DockerComponent dockerComponent, DockerConfiguration dockerConfiguration, Message message)
specifier|public
specifier|static
name|DockerClient
name|getDockerClient
parameter_list|(
name|DockerComponent
name|dockerComponent
parameter_list|,
name|DockerConfiguration
name|dockerConfiguration
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|DockerException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|dockerConfiguration
argument_list|,
literal|"dockerConfiguration"
argument_list|)
expr_stmt|;
name|DockerClientProfile
name|clientProfile
init|=
operator|new
name|DockerClientProfile
argument_list|()
decl_stmt|;
name|Integer
name|port
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PORT
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_HOST
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getHost
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|maxTotalConnections
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_MAX_TOTAL_CONNECTIONS
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getMaxTotalConnections
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|maxPerRouteConnections
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_MAX_PER_ROUTE_CONNECTIONS
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getMaxPerRouteConnections
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|username
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_USERNAME
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getUsername
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PASSWORD
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|email
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_EMAIL
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getEmail
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|requestTimeout
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_API_REQUEST_TIMEOUT
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getRequestTimeout
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|serverAddress
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_SERVER_ADDRESS
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getServerAddress
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|certPath
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_CERT_PATH
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|getCertPath
argument_list|()
argument_list|)
decl_stmt|;
name|Boolean
name|secure
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_SECURE
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|isSecure
argument_list|()
argument_list|)
decl_stmt|;
name|Boolean
name|tlsVerify
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_TLSVERIFY
argument_list|,
name|dockerConfiguration
argument_list|,
name|message
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|dockerConfiguration
operator|.
name|isTlsVerify
argument_list|()
argument_list|)
decl_stmt|;
name|clientProfile
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setEmail
argument_list|(
name|email
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setRequestTimeout
argument_list|(
name|requestTimeout
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setServerAddress
argument_list|(
name|serverAddress
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setCertPath
argument_list|(
name|certPath
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setMaxTotalConnections
argument_list|(
name|maxTotalConnections
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setMaxPerRouteConnections
argument_list|(
name|maxPerRouteConnections
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setSecure
argument_list|(
name|secure
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setTlsVerify
argument_list|(
name|tlsVerify
argument_list|)
expr_stmt|;
name|DockerClient
name|dockerClient
init|=
name|dockerComponent
operator|.
name|getClient
argument_list|(
name|clientProfile
argument_list|)
decl_stmt|;
if|if
condition|(
name|dockerClient
operator|!=
literal|null
condition|)
block|{
return|return
name|dockerClient
return|;
block|}
name|SSLConfig
name|sslConfig
decl_stmt|;
if|if
condition|(
name|clientProfile
operator|.
name|isSecure
argument_list|()
operator|!=
literal|null
operator|&&
name|clientProfile
operator|.
name|isSecure
argument_list|()
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clientProfile
operator|.
name|getCertPath
argument_list|()
argument_list|,
literal|"certPath must be specified in secure mode"
argument_list|)
expr_stmt|;
name|sslConfig
operator|=
operator|new
name|LocalDirectorySSLConfig
argument_list|(
name|clientProfile
operator|.
name|getCertPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// docker-java requires an implementation of SslConfig interface
comment|// to be available for DockerCmdExecFactoryImpl
name|sslConfig
operator|=
operator|new
name|NoImplSslConfig
argument_list|()
expr_stmt|;
block|}
name|DefaultDockerClientConfig
operator|.
name|Builder
name|configBuilder
init|=
name|DefaultDockerClientConfig
operator|.
name|createDefaultConfigBuilder
argument_list|()
operator|.
name|withDockerHost
argument_list|(
name|clientProfile
operator|.
name|toUrl
argument_list|()
argument_list|)
operator|.
name|withDockerTlsVerify
argument_list|(
name|clientProfile
operator|.
name|isTlsVerify
argument_list|()
argument_list|)
operator|.
name|withRegistryUsername
argument_list|(
name|clientProfile
operator|.
name|getUsername
argument_list|()
argument_list|)
operator|.
name|withRegistryPassword
argument_list|(
name|clientProfile
operator|.
name|getPassword
argument_list|()
argument_list|)
operator|.
name|withRegistryEmail
argument_list|(
name|clientProfile
operator|.
name|getEmail
argument_list|()
argument_list|)
operator|.
name|withRegistryUrl
argument_list|(
name|clientProfile
operator|.
name|getServerAddress
argument_list|()
argument_list|)
operator|.
name|withCustomSslConfig
argument_list|(
name|sslConfig
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientProfile
operator|.
name|getCertPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|configBuilder
operator|.
name|withDockerCertPath
argument_list|(
name|clientProfile
operator|.
name|getCertPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// @Deprecated: isFollowRedirectFilterEnabled, isLoggingFilterEnabled
name|DockerCmdExecFactory
name|dockerCmdExecFactory
init|=
operator|new
name|JerseyDockerCmdExecFactory
argument_list|()
operator|.
name|withReadTimeout
argument_list|(
name|clientProfile
operator|.
name|getRequestTimeout
argument_list|()
argument_list|)
operator|.
name|withConnectTimeout
argument_list|(
name|clientProfile
operator|.
name|getRequestTimeout
argument_list|()
argument_list|)
operator|.
name|withMaxTotalConnections
argument_list|(
name|clientProfile
operator|.
name|getMaxTotalConnections
argument_list|()
argument_list|)
operator|.
name|withMaxPerRouteConnections
argument_list|(
name|clientProfile
operator|.
name|getMaxPerRouteConnections
argument_list|()
argument_list|)
decl_stmt|;
name|dockerClient
operator|=
name|DockerClientBuilder
operator|.
name|getInstance
argument_list|(
name|configBuilder
argument_list|)
operator|.
name|withDockerCmdExecFactory
argument_list|(
name|dockerCmdExecFactory
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|dockerComponent
operator|.
name|setClient
argument_list|(
name|clientProfile
argument_list|,
name|dockerClient
argument_list|)
expr_stmt|;
return|return
name|dockerClient
return|;
block|}
block|}
end_class

end_unit

