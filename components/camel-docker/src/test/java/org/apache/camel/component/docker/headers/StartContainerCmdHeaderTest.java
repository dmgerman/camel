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
name|StartContainerCmd
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
name|Bind
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
name|Capability
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
name|Device
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
name|Link
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
name|LxcConf
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
name|PortBinding
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
name|Ports
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
name|RestartPolicy
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
name|Ignore
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
comment|/**  * Validates Start Container Request headers are applied properly  */
end_comment

begin_class
DECL|class|StartContainerCmdHeaderTest
specifier|public
class|class
name|StartContainerCmdHeaderTest
extends|extends
name|BaseDockerHeaderTest
argument_list|<
name|StartContainerCmd
argument_list|>
block|{
annotation|@
name|Mock
DECL|field|exposedPort
name|ExposedPort
name|exposedPort
decl_stmt|;
annotation|@
name|Mock
DECL|field|mockObject
specifier|private
name|StartContainerCmd
name|mockObject
decl_stmt|;
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|startContainerHeaderTest ()
specifier|public
name|void
name|startContainerHeaderTest
parameter_list|()
block|{
name|String
name|containerId
init|=
literal|"be29975e0098"
decl_stmt|;
name|Volume
name|vol
init|=
operator|new
name|Volume
argument_list|(
literal|"/opt/webapp1"
argument_list|)
decl_stmt|;
name|Bind
index|[]
name|binds
init|=
operator|new
name|Bind
index|[]
block|{
operator|new
name|Bind
argument_list|(
literal|"/opt/webapp1"
argument_list|,
name|vol
argument_list|)
block|}
decl_stmt|;
name|boolean
name|publishAllPorts
init|=
literal|false
decl_stmt|;
name|boolean
name|privileged
init|=
literal|false
decl_stmt|;
name|String
index|[]
name|dns
init|=
operator|new
name|String
index|[]
block|{
literal|"8.8.8.8"
block|}
decl_stmt|;
name|Link
index|[]
name|links
init|=
operator|new
name|Link
index|[]
block|{
operator|new
name|Link
argument_list|(
literal|"container1"
argument_list|,
literal|"container1Link"
argument_list|)
block|}
decl_stmt|;
name|String
name|networkMode
init|=
literal|"host"
decl_stmt|;
name|LxcConf
index|[]
name|lxcConf
init|=
operator|new
name|LxcConf
index|[]
block|{
operator|new
name|LxcConf
argument_list|()
block|}
decl_stmt|;
name|String
name|volumesFromContainer
init|=
literal|"container2"
decl_stmt|;
name|Capability
name|capAdd
init|=
name|Capability
operator|.
name|NET_BROADCAST
decl_stmt|;
name|Capability
name|capDrop
init|=
name|Capability
operator|.
name|BLOCK_SUSPEND
decl_stmt|;
name|Device
index|[]
name|devices
init|=
operator|new
name|Device
index|[]
block|{
operator|new
name|Device
argument_list|(
literal|"rwm"
argument_list|,
literal|"/dev/nulo"
argument_list|,
literal|"/dev/zero"
argument_list|)
block|}
decl_stmt|;
name|RestartPolicy
name|restartPolicy
init|=
name|RestartPolicy
operator|.
name|noRestart
argument_list|()
decl_stmt|;
name|PortBinding
index|[]
name|portBindings
init|=
operator|new
name|PortBinding
index|[]
block|{
operator|new
name|PortBinding
argument_list|(
name|Ports
operator|.
name|Binding
argument_list|(
literal|28768
argument_list|)
argument_list|,
name|ExposedPort
operator|.
name|tcp
argument_list|(
literal|22
argument_list|)
argument_list|)
block|}
decl_stmt|;
name|Ports
name|ports
init|=
operator|new
name|Ports
argument_list|(
name|ExposedPort
operator|.
name|tcp
argument_list|(
literal|22
argument_list|)
argument_list|,
name|Ports
operator|.
name|Binding
argument_list|(
literal|11022
argument_list|)
argument_list|)
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
name|DOCKER_BINDS
argument_list|,
name|binds
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PUBLISH_ALL_PORTS
argument_list|,
name|publishAllPorts
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PRIVILEGED
argument_list|,
name|privileged
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_DNS
argument_list|,
name|dns
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_DNS_SEARCH
argument_list|,
name|dns
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_LINKS
argument_list|,
name|links
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_NETWORK_MODE
argument_list|,
name|networkMode
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_LXC_CONF
argument_list|,
name|lxcConf
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_VOLUMES_FROM
argument_list|,
name|volumesFromContainer
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_CAP_ADD
argument_list|,
name|capAdd
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_CAP_DROP
argument_list|,
name|capDrop
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_DEVICES
argument_list|,
name|devices
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_RESTART_POLICY
argument_list|,
name|restartPolicy
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PORT_BINDINGS
argument_list|,
name|portBindings
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PORTS
argument_list|,
name|ports
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
name|startContainerCmd
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
name|withBinds
argument_list|(
name|binds
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
name|withPublishAllPorts
argument_list|(
name|publishAllPorts
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
name|withPrivileged
argument_list|(
name|privileged
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
name|withDns
argument_list|(
name|dns
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
name|withDnsSearch
argument_list|(
name|dns
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
name|withLinks
argument_list|(
name|links
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
name|withNetworkMode
argument_list|(
name|networkMode
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
name|withLxcConf
argument_list|(
name|lxcConf
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
name|withVolumesFrom
argument_list|(
name|volumesFromContainer
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
name|withCapAdd
argument_list|(
name|capAdd
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
name|withCapDrop
argument_list|(
name|capDrop
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
name|withDevices
argument_list|(
name|devices
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
name|withRestartPolicy
argument_list|(
name|restartPolicy
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
name|withPortBindings
argument_list|(
name|portBindings
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
name|withPortBindings
argument_list|(
name|ports
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
name|startContainerCmd
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
name|START_CONTAINER
return|;
block|}
block|}
end_class

end_unit

