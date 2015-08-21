begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker.headers
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
operator|.
name|headers
package|;
end_package

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
name|command
operator|.
name|CommitCmd
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
name|model
operator|.
name|ExposedPort
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
name|model
operator|.
name|ExposedPorts
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
name|model
operator|.
name|Volume
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
name|model
operator|.
name|Volumes
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
name|DockerConstants
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
name|DockerOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Matchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_comment
comment|/**  * Validates Commit Container Request headers are parsed properly  */
end_comment

begin_class
DECL|class|CommitContainerCmdHeaderTest
specifier|public
class|class
name|CommitContainerCmdHeaderTest
extends|extends
name|BaseDockerHeaderTest
argument_list|<
name|CommitCmd
argument_list|>
block|{
annotation|@
name|Mock
DECL|field|mockObject
specifier|private
name|CommitCmd
name|mockObject
decl_stmt|;
annotation|@
name|Test
DECL|method|commitContainerHeaderTest ()
specifier|public
name|void
name|commitContainerHeaderTest
parameter_list|()
block|{
name|String
name|containerId
init|=
literal|"9c09acd48a25"
decl_stmt|;
name|String
name|env
init|=
literal|"FOO=bar"
decl_stmt|;
name|boolean
name|attachStdErr
init|=
literal|true
decl_stmt|;
name|boolean
name|attachStdOut
init|=
literal|true
decl_stmt|;
name|boolean
name|attachStdIn
init|=
literal|false
decl_stmt|;
name|boolean
name|disableNetwork
init|=
literal|false
decl_stmt|;
name|boolean
name|openStdIn
init|=
literal|false
decl_stmt|;
name|String
name|portSpecs
init|=
literal|"80"
decl_stmt|;
name|boolean
name|stdInOnce
init|=
literal|false
decl_stmt|;
name|String
name|tag
init|=
literal|"1.0"
decl_stmt|;
name|String
name|repository
init|=
literal|"docker/empty"
decl_stmt|;
name|String
name|cmd
init|=
literal|"whoami"
decl_stmt|;
name|String
name|author
init|=
literal|"cameluser"
decl_stmt|;
name|String
name|message
init|=
literal|"Camel Docker Container Commit"
decl_stmt|;
name|boolean
name|pause
init|=
literal|false
decl_stmt|;
name|ExposedPorts
name|exposedPorts
init|=
operator|new
name|ExposedPorts
argument_list|(
name|ExposedPort
operator|.
name|tcp
argument_list|(
literal|22
argument_list|)
argument_list|)
decl_stmt|;
name|Integer
name|memory
init|=
literal|2048
decl_stmt|;
name|Integer
name|swapMemory
init|=
literal|512
decl_stmt|;
name|String
name|workingDir
init|=
literal|"/opt"
decl_stmt|;
name|String
name|user
init|=
literal|"docker"
decl_stmt|;
name|Volumes
name|volumes
init|=
operator|new
name|Volumes
argument_list|(
operator|new
name|Volume
argument_list|(
literal|"/example"
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|tty
init|=
literal|true
decl_stmt|;
name|String
name|hostname
init|=
literal|"dockerhostname"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|getDefaultParameters
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|containerId
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_ENV
argument_list|,
name|env
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_IN
argument_list|,
name|attachStdIn
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_ERR
argument_list|,
name|attachStdErr
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_OUT
argument_list|,
name|attachStdOut
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_DISABLE_NETWORK
argument_list|,
name|disableNetwork
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_TAG
argument_list|,
name|tag
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_REPOSITORY
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_CMD
argument_list|,
name|cmd
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_AUTHOR
argument_list|,
name|author
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_MESSAGE
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PAUSE
argument_list|,
name|pause
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_EXPOSED_PORTS
argument_list|,
name|exposedPorts
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_MEMORY
argument_list|,
name|memory
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_MEMORY_SWAP
argument_list|,
name|swapMemory
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_WORKING_DIR
argument_list|,
name|workingDir
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_USER
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_VOLUMES
argument_list|,
name|volumes
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_TTY
argument_list|,
name|tty
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_HOSTNAME
argument_list|,
name|hostname
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_OPEN_STD_IN
argument_list|,
name|openStdIn
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_STD_IN_ONCE
argument_list|,
name|stdInOnce
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PORT_SPECS
argument_list|,
name|portSpecs
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:in"
argument_list|,
literal|""
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|dockerClient
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|commitCmd
argument_list|(
name|containerId
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withEnv
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withAttachStderr
argument_list|(
name|attachStdErr
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withAttachStdin
argument_list|(
name|attachStdIn
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withAttachStdout
argument_list|(
name|attachStdOut
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withDisableNetwork
argument_list|(
name|disableNetwork
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withTag
argument_list|(
name|tag
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withCmd
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withPause
argument_list|(
name|pause
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withExposedPorts
argument_list|(
name|exposedPorts
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withMemory
argument_list|(
name|memory
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withMemorySwap
argument_list|(
name|swapMemory
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withWorkingDir
argument_list|(
name|workingDir
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withVolumes
argument_list|(
name|volumes
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withTty
argument_list|(
name|tty
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withHostname
argument_list|(
name|hostname
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withOpenStdin
argument_list|(
name|openStdIn
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withStdinOnce
argument_list|(
name|stdInOnce
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockObject
argument_list|,
name|Mockito
operator|.
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|withPortSpecs
argument_list|(
name|portSpecs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setupMocks ()
specifier|protected
name|void
name|setupMocks
parameter_list|()
block|{
name|Mockito
operator|.
name|when
argument_list|(
name|dockerClient
operator|.
name|commitCmd
argument_list|(
name|Matchers
operator|.
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockObject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOperation ()
specifier|protected
name|DockerOperation
name|getOperation
parameter_list|()
block|{
return|return
name|DockerOperation
operator|.
name|COMMIT_CONTAINER
return|;
block|}
block|}
end_class

end_unit

