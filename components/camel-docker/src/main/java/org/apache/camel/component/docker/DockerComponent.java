begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|spi
operator|.
name|Metadata
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
name|annotations
operator|.
name|Component
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link DockerEndpoint}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"docker"
argument_list|)
DECL|class|DockerComponent
specifier|public
class|class
name|DockerComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|DockerConfiguration
name|configuration
init|=
operator|new
name|DockerConfiguration
argument_list|()
decl_stmt|;
DECL|field|clients
specifier|private
name|Map
argument_list|<
name|DockerClientProfile
argument_list|,
name|DockerClient
argument_list|>
name|clients
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|DockerComponent ()
specifier|public
name|DockerComponent
parameter_list|()
block|{     }
DECL|method|DockerComponent (DockerConfiguration configuration)
specifier|public
name|DockerComponent
parameter_list|(
name|DockerConfiguration
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
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
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
comment|// Each endpoint can have its own configuration so make
comment|// a copy of the configuration
name|DockerConfiguration
name|configuration
init|=
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|String
name|normalizedRemaining
init|=
name|remaining
operator|.
name|replaceAll
argument_list|(
literal|"/"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|DockerOperation
name|operation
init|=
name|DockerOperation
operator|.
name|getDockerOperation
argument_list|(
name|normalizedRemaining
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DockerException
argument_list|(
name|remaining
operator|+
literal|" is not a valid operation"
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|setOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
operator|new
name|DockerEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// and store any left-over parameters on configuration
name|configuration
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|setConfiguration (DockerConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|DockerConfiguration
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
comment|/**      * To use the shared docker configuration      */
DECL|method|getConfiguration ()
specifier|protected
name|DockerConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getClient (DockerClientProfile clientProfile)
specifier|public
name|DockerClient
name|getClient
parameter_list|(
name|DockerClientProfile
name|clientProfile
parameter_list|)
throws|throws
name|DockerException
block|{
return|return
name|clients
operator|.
name|get
argument_list|(
name|clientProfile
argument_list|)
return|;
block|}
comment|/**      * To use the given docker client      */
DECL|method|setClient (DockerClientProfile clientProfile, DockerClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|DockerClientProfile
name|clientProfile
parameter_list|,
name|DockerClient
name|client
parameter_list|)
block|{
name|clients
operator|.
name|put
argument_list|(
name|clientProfile
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

