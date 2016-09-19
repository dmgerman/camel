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
name|ExecStartCmd
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
name|command
operator|.
name|ExecStartResultCallback
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
comment|/**  * Validates Exec Start Request headers are parsed properly  */
end_comment

begin_class
DECL|class|ExecStartCmdHeaderTest
specifier|public
class|class
name|ExecStartCmdHeaderTest
extends|extends
name|BaseDockerHeaderTest
argument_list|<
name|ExecStartCmd
argument_list|>
block|{
annotation|@
name|Mock
DECL|field|mockObject
specifier|private
name|ExecStartCmd
name|mockObject
decl_stmt|;
annotation|@
name|Mock
DECL|field|callback
specifier|private
name|ExecStartResultCallback
name|callback
decl_stmt|;
annotation|@
name|Test
DECL|method|execCreateHeaderTest ()
specifier|public
name|void
name|execCreateHeaderTest
parameter_list|()
block|{
name|String
name|id
init|=
literal|"1"
decl_stmt|;
name|boolean
name|tty
init|=
literal|true
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
name|DOCKER_EXEC_ID
argument_list|,
name|id
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
name|execStartCmd
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|id
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
name|withTty
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
name|tty
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
name|execStartCmd
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
name|Mockito
operator|.
name|when
argument_list|(
name|mockObject
operator|.
name|exec
argument_list|(
name|Mockito
operator|.
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|callback
argument_list|)
expr_stmt|;
try|try
block|{
name|Mockito
operator|.
name|when
argument_list|(
name|callback
operator|.
name|awaitCompletion
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
name|EXEC_START
return|;
block|}
block|}
end_class

end_unit

