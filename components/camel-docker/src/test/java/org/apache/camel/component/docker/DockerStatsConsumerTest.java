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
name|concurrent
operator|.
name|TimeUnit
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
name|StatsCallback
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
name|StatsCmd
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
name|Statistics
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
name|builder
operator|.
name|RouteBuilder
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
name|util
operator|.
name|DockerTestUtils
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
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
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

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|powermock
operator|.
name|modules
operator|.
name|junit4
operator|.
name|PowerMockRunner
import|;
end_import

begin_comment
comment|/**  * Consumer test for statistics on Docker Platform  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|PowerMockRunner
operator|.
name|class
argument_list|)
DECL|class|DockerStatsConsumerTest
specifier|public
class|class
name|DockerStatsConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|host
specifier|private
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|2375
decl_stmt|;
DECL|field|containerId
specifier|private
name|String
name|containerId
init|=
literal|"470b9b823e6c"
decl_stmt|;
DECL|field|callback
specifier|private
name|StatsCallback
name|callback
decl_stmt|;
DECL|field|dockerConfiguration
specifier|private
name|DockerConfiguration
name|dockerConfiguration
decl_stmt|;
annotation|@
name|Mock
DECL|field|statsCmd
specifier|private
name|StatsCmd
name|statsCmd
decl_stmt|;
annotation|@
name|Mock
DECL|field|dockerClient
specifier|private
name|DockerClient
name|dockerClient
decl_stmt|;
DECL|method|setupMocks ()
specifier|public
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
name|statsCmd
argument_list|(
name|Matchers
operator|.
name|any
argument_list|(
name|StatsCallback
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|StatsCmd
argument_list|>
argument_list|()
block|{
specifier|public
name|StatsCmd
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
block|{
name|Object
index|[]
name|args
init|=
name|invocation
operator|.
name|getArguments
argument_list|()
decl_stmt|;
name|callback
operator|=
operator|(
name|StatsCallback
operator|)
name|args
index|[
literal|0
index|]
expr_stmt|;
return|return
name|statsCmd
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStats ()
specifier|public
name|void
name|testStats
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onStats
argument_list|(
operator|new
name|Statistics
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"docker://stats?host="
operator|+
name|host
operator|+
literal|"&port="
operator|+
name|port
operator|+
literal|"&containerId="
operator|+
name|containerId
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|dockerConfiguration
operator|=
operator|new
name|DockerConfiguration
argument_list|()
expr_stmt|;
name|dockerConfiguration
operator|.
name|setParameters
argument_list|(
name|DockerTestUtils
operator|.
name|getDefaultParameters
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|dockerConfiguration
argument_list|)
argument_list|)
expr_stmt|;
name|DockerComponent
name|dockerComponent
init|=
operator|new
name|DockerComponent
argument_list|(
name|dockerConfiguration
argument_list|)
decl_stmt|;
name|dockerComponent
operator|.
name|setClient
argument_list|(
name|DockerTestUtils
operator|.
name|getClientProfile
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|dockerConfiguration
argument_list|)
argument_list|,
name|dockerClient
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"docker"
argument_list|,
name|dockerComponent
argument_list|)
expr_stmt|;
name|setupMocks
argument_list|()
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

