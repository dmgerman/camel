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
name|component
operator|.
name|docker
operator|.
name|exception
operator|.
name|DockerException
import|;
end_import

begin_class
DECL|class|DockerConfiguration
specifier|public
class|class
name|DockerConfiguration
block|{
DECL|field|DEFAULT_DOCKER_HOST
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_DOCKER_HOST
init|=
literal|"localhost"
decl_stmt|;
DECL|field|DEFAULT_DOCKER_PORT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_DOCKER_PORT
init|=
literal|5000
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<
name|DockerClientProfile
argument_list|,
name|DockerClient
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|operation
specifier|private
name|DockerOperation
name|operation
decl_stmt|;
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
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|DockerOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (DockerOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|DockerOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getDefaultHost ()
specifier|public
name|String
name|getDefaultHost
parameter_list|()
block|{
return|return
name|DEFAULT_DOCKER_HOST
return|;
block|}
DECL|method|getDefaultPort ()
specifier|public
name|Integer
name|getDefaultPort
parameter_list|()
block|{
return|return
name|DEFAULT_DOCKER_PORT
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
block|}
end_class

end_unit

