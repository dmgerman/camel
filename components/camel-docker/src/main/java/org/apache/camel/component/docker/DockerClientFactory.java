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
name|DockerClientConfig
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
class|class
name|DockerClientFactory
block|{
comment|/**      * Produces a {@link DockerClient} to communicate with Docker      *       * @param dockerConfiguration      * @param message the Camel message      * @return a DockerClient      * @throws DockerException      */
DECL|method|getDockerClient (DockerConfiguration dockerConfiguration, Message message)
specifier|public
specifier|static
name|DockerClient
name|getDockerClient
parameter_list|(
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
name|Integer
name|port
init|=
literal|null
decl_stmt|;
name|String
name|host
init|=
literal|null
decl_stmt|;
name|DockerClient
name|client
init|=
literal|null
decl_stmt|;
name|port
operator|=
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
argument_list|)
expr_stmt|;
name|host
operator|=
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
argument_list|)
expr_stmt|;
name|int
name|uriPort
init|=
name|port
operator|!=
literal|null
condition|?
name|port
else|:
name|dockerConfiguration
operator|.
name|getDefaultPort
argument_list|()
decl_stmt|;
name|String
name|uriHost
init|=
name|host
operator|!=
literal|null
condition|?
name|host
else|:
name|dockerConfiguration
operator|.
name|getDefaultHost
argument_list|()
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
argument_list|)
decl_stmt|;
name|DockerClientProfile
name|clientProfile
init|=
operator|new
name|DockerClientProfile
argument_list|()
decl_stmt|;
name|clientProfile
operator|.
name|setHost
argument_list|(
name|uriHost
argument_list|)
expr_stmt|;
name|clientProfile
operator|.
name|setPort
argument_list|(
name|uriPort
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
if|if
condition|(
name|secure
operator|!=
literal|null
operator|&&
name|secure
condition|)
block|{
name|clientProfile
operator|.
name|setSecure
argument_list|(
name|secure
argument_list|)
expr_stmt|;
block|}
name|client
operator|=
name|dockerConfiguration
operator|.
name|getClient
argument_list|(
name|clientProfile
argument_list|)
expr_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
return|return
name|client
return|;
block|}
name|DockerClientConfig
operator|.
name|DockerClientConfigBuilder
name|configBuilder
init|=
operator|new
name|DockerClientConfig
operator|.
name|DockerClientConfigBuilder
argument_list|()
operator|.
name|withUsername
argument_list|(
name|username
argument_list|)
operator|.
name|withPassword
argument_list|(
name|password
argument_list|)
operator|.
name|withEmail
argument_list|(
name|email
argument_list|)
operator|.
name|withReadTimeout
argument_list|(
name|requestTimeout
argument_list|)
operator|.
name|withUri
argument_list|(
name|clientProfile
operator|.
name|toUrl
argument_list|()
argument_list|)
decl_stmt|;
name|DockerClientConfig
name|config
init|=
name|configBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|DockerClientBuilder
operator|.
name|getInstance
argument_list|(
name|config
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

