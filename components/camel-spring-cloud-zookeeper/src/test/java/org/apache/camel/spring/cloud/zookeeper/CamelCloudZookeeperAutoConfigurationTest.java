begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.cloud.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|cloud
operator|.
name|zookeeper
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
name|spring
operator|.
name|cloud
operator|.
name|zookeeper
operator|.
name|support
operator|.
name|ZookeeperServer
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
name|WebApplicationType
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
name|builder
operator|.
name|SpringApplicationBuilder
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
name|ConfigurableApplicationContext
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
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|converter
operator|.
name|Converter
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
DECL|class|CamelCloudZookeeperAutoConfigurationTest
specifier|public
class|class
name|CamelCloudZookeeperAutoConfigurationTest
block|{
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
DECL|method|testServiceDefinitionToConsulRegistration ()
specifier|public
name|void
name|testServiceDefinitionToConsulRegistration
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ZookeeperServer
name|server
init|=
operator|new
name|ZookeeperServer
argument_list|(
name|temporaryFolder
operator|.
name|newFolder
argument_list|(
name|testName
operator|.
name|getMethodName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ConfigurableApplicationContext
name|context
init|=
operator|new
name|SpringApplicationBuilder
argument_list|(
name|TestConfiguration
operator|.
name|class
argument_list|)
operator|.
name|web
argument_list|(
name|WebApplicationType
operator|.
name|NONE
argument_list|)
operator|.
name|run
argument_list|(
literal|"--debug=false"
argument_list|,
literal|"--spring.main.banner-mode=OFF"
argument_list|,
literal|"--spring.application.name="
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"--ribbon.enabled=false"
argument_list|,
literal|"--ribbon.eureka.enabled=false"
argument_list|,
literal|"--management.endpoint.enabled=false"
argument_list|,
literal|"--spring.cloud.zookeeper.enabled=true"
argument_list|,
literal|"--spring.cloud.zookeeper.connect-string="
operator|+
name|server
operator|.
name|connectString
argument_list|()
argument_list|,
literal|"--spring.cloud.zookeeper.config.enabled=false"
argument_list|,
literal|"--spring.cloud.zookeeper.discovery.enabled=true"
argument_list|,
literal|"--spring.cloud.service-registry.auto-registration.enabled=false"
argument_list|)
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Converter
argument_list|>
name|converters
init|=
name|context
operator|.
name|getBeansOfType
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|converters
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|converters
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|ServiceDefinitionToZookeeperRegistration
operator|.
name|class
operator|::
name|isInstance
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// shutdown spring context
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// shutdown zookeeper
name|server
operator|.
name|shutdown
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

