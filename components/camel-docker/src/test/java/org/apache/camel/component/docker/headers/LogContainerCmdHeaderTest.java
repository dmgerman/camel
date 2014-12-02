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
name|LogContainerCmd
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
comment|/**  * Validates Log Container Request headers are applied properly  */
end_comment

begin_class
DECL|class|LogContainerCmdHeaderTest
specifier|public
class|class
name|LogContainerCmdHeaderTest
extends|extends
name|BaseDockerHeaderTest
argument_list|<
name|LogContainerCmd
argument_list|>
block|{
annotation|@
name|Mock
DECL|field|mockObject
specifier|private
name|LogContainerCmd
name|mockObject
decl_stmt|;
annotation|@
name|Test
DECL|method|logContainerHeaderTest ()
specifier|public
name|void
name|logContainerHeaderTest
parameter_list|()
block|{
name|String
name|containerId
init|=
literal|"9c09acd48a25"
decl_stmt|;
name|boolean
name|stdOut
init|=
literal|true
decl_stmt|;
name|boolean
name|stdErr
init|=
literal|true
decl_stmt|;
name|boolean
name|followStream
init|=
literal|true
decl_stmt|;
name|boolean
name|timestamps
init|=
literal|true
decl_stmt|;
name|boolean
name|tailAll
init|=
literal|true
decl_stmt|;
name|int
name|tail
init|=
literal|5
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
name|DOCKER_FOLLOW_STREAM
argument_list|,
name|followStream
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_STD_OUT
argument_list|,
name|stdOut
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_STD_ERR
argument_list|,
name|stdErr
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_TIMESTAMPS
argument_list|,
name|timestamps
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_TAIL
argument_list|,
name|tail
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_TAIL_ALL
argument_list|,
name|tailAll
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
name|logContainerCmd
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
name|withFollowStream
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|followStream
argument_list|)
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
name|withTail
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|tail
argument_list|)
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
name|withTailAll
argument_list|()
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
name|withStdErr
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|stdErr
argument_list|)
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
name|withStdOut
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|stdOut
argument_list|)
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
name|withTimestamps
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|timestamps
argument_list|)
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
name|logContainerCmd
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
name|LOG_CONTAINER
return|;
block|}
block|}
end_class

end_unit

