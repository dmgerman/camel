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
name|ListContainersCmd
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
comment|/**  * Validates List Containers Request headers are applied properly  */
end_comment

begin_class
DECL|class|ListContainersCmdHeaderTest
specifier|public
class|class
name|ListContainersCmdHeaderTest
extends|extends
name|BaseDockerHeaderTest
argument_list|<
name|ListContainersCmd
argument_list|>
block|{
annotation|@
name|Mock
DECL|field|mockObject
specifier|private
name|ListContainersCmd
name|mockObject
decl_stmt|;
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|listContainerHeaderTest ()
specifier|public
name|void
name|listContainerHeaderTest
parameter_list|()
block|{
name|boolean
name|showSize
init|=
literal|true
decl_stmt|;
name|boolean
name|showAll
init|=
literal|false
decl_stmt|;
name|int
name|limit
init|=
literal|2
decl_stmt|;
name|String
name|since
init|=
literal|"id1"
decl_stmt|;
name|String
name|before
init|=
literal|"id2"
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
name|DOCKER_LIMIT
argument_list|,
name|limit
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_SHOW_ALL
argument_list|,
name|showAll
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_SHOW_SIZE
argument_list|,
name|showSize
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_SINCE
argument_list|,
name|since
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_BEFORE
argument_list|,
name|before
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
name|listContainersCmd
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
name|withShowAll
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|showAll
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
name|withShowSize
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|showSize
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
name|withLimit
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|limit
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
name|withSince
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|since
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
name|withBefore
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|before
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
name|listContainersCmd
argument_list|()
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
name|LIST_CONTAINERS
return|;
block|}
block|}
end_class

end_unit

