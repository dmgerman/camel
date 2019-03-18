begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.springboot.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|springboot
operator|.
name|cluster
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|cluster
operator|.
name|CamelClusterService
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
name|zookeeper
operator|.
name|cluster
operator|.
name|ZooKeeperClusterService
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
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|test
operator|.
name|TestingServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|rules
operator|.
name|TemporaryFolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|EnableAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|runner
operator|.
name|ApplicationContextRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|ZooKeeperClusterServiceTest
specifier|public
class|class
name|ZooKeeperClusterServiceTest
block|{
DECL|field|SERVICE_PATH
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_PATH
init|=
literal|"/camel"
decl_stmt|;
annotation|@
name|Rule
DECL|field|testName
specifier|public
specifier|final
name|TestName
name|testName
init|=
operator|new
name|TestName
argument_list|()
decl_stmt|;
annotation|@
name|Rule
DECL|field|temporaryFolder
specifier|public
specifier|final
name|TemporaryFolder
name|temporaryFolder
init|=
operator|new
name|TemporaryFolder
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testClusterService ()
specifier|public
name|void
name|testClusterService
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|zkPort
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
specifier|final
name|File
name|zkDir
init|=
name|temporaryFolder
operator|.
name|newFolder
argument_list|()
decl_stmt|;
specifier|final
name|TestingServer
name|zkServer
init|=
operator|new
name|TestingServer
argument_list|(
name|zkPort
argument_list|,
name|zkDir
argument_list|)
decl_stmt|;
name|zkServer
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
operator|new
name|ApplicationContextRunner
argument_list|()
operator|.
name|withUserConfiguration
argument_list|(
name|TestConfiguration
operator|.
name|class
argument_list|)
operator|.
name|withPropertyValues
argument_list|(
literal|"debug=false"
argument_list|,
literal|"spring.main.banner-mode=OFF"
argument_list|,
literal|"spring.application.name="
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"camel.component.zookeeper.cluster.service.enabled=true"
argument_list|,
literal|"camel.component.zookeeper.cluster.service.nodes=localhost:"
operator|+
name|zkPort
argument_list|,
literal|"camel.component.zookeeper.cluster.service.id="
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"camel.component.zookeeper.cluster.service.base-path="
operator|+
name|SERVICE_PATH
argument_list|)
operator|.
name|run
argument_list|(
name|context
lambda|->
block|{
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|hasSingleBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|hasSingleBean
argument_list|(
name|CamelClusterService
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|CamelContext
name|camelContext
init|=
name|context
operator|.
name|getBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|CamelClusterService
name|clusterService
init|=
name|camelContext
operator|.
name|hasService
argument_list|(
name|CamelClusterService
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|clusterService
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|clusterService
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|ZooKeeperClusterService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|zkServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|// *************************************
comment|// Config
comment|// *************************************
annotation|@
name|EnableAutoConfiguration
annotation|@
name|Configuration
DECL|class|TestConfiguration
specifier|public
specifier|static
class|class
name|TestConfiguration
block|{     }
block|}
end_class

end_unit

