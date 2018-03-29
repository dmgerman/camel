begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|ProducerTemplate
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
name|spring
operator|.
name|boot
operator|.
name|CamelAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
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
name|SpringBootApplication
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
name|SpringBootTest
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
name|Bean
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
name|context
operator|.
name|annotation
operator|.
name|Lazy
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
name|env
operator|.
name|ConfigurableEnvironment
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
name|env
operator|.
name|MutablePropertySources
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
name|env
operator|.
name|PropertiesPropertySource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_class
annotation|@
name|DirtiesContext
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootApplication
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
block|{
name|CamelAutoConfiguration
operator|.
name|class
block|,
name|CamelCloudServiceCallSimpleExpressionTest
operator|.
name|TestConfiguration
operator|.
name|class
block|,
name|CamelCloudServiceCallSimpleExpressionTest
operator|.
name|SpringBootPropertySourceConfig
operator|.
name|class
block|}
argument_list|)
DECL|class|CamelCloudServiceCallSimpleExpressionTest
specifier|public
class|class
name|CamelCloudServiceCallSimpleExpressionTest
block|{
annotation|@
name|Autowired
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testServiceCall ()
specifier|public
name|void
name|testServiceCall
parameter_list|()
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|SpringBootPropertyUtil
operator|.
name|PORT1
argument_list|)
argument_list|,
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|SpringBootPropertyUtil
operator|.
name|PORT3
argument_list|)
argument_list|,
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// *************************************
comment|// Config
comment|// *************************************
annotation|@
name|Configuration
DECL|class|TestConfiguration
specifier|public
specifier|static
class|class
name|TestConfiguration
block|{
annotation|@
name|Bean
DECL|method|myRouteBuilder ()
specifier|public
name|RouteBuilder
name|myRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|serviceCall
argument_list|(
literal|"{{service.name}}"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"jetty:http://localhost:%d/hello"
argument_list|,
name|SpringBootPropertyUtil
operator|.
name|PORT1
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
name|SpringBootPropertyUtil
operator|.
name|PORT1
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"jetty:http://localhost:%d/hello"
argument_list|,
name|SpringBootPropertyUtil
operator|.
name|PORT2
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
name|SpringBootPropertyUtil
operator|.
name|PORT2
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"jetty:http://localhost:%d/hello"
argument_list|,
name|SpringBootPropertyUtil
operator|.
name|PORT3
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
name|SpringBootPropertyUtil
operator|.
name|PORT3
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
DECL|method|getAllProperties ()
specifier|private
specifier|static
name|Properties
name|getAllProperties
parameter_list|()
block|{
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"service.name"
argument_list|,
literal|"custom-svc-list"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"camel.cloud.load-balancer.enabled"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"camel.cloud.service-call.component"
argument_list|,
literal|"jetty"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"camel.cloud.service-call.expression"
argument_list|,
literal|"$simple{header.CamelServiceCallScheme}:http://$simple{header.CamelServiceCallServiceHost}:$simple{header.CamelServiceCallServicePort}/hello"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"camel.cloud.service-call.expression-language"
argument_list|,
literal|"simple"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"camel.cloud.service-discovery.services[custom-svc-list]"
argument_list|,
name|SpringBootPropertyUtil
operator|.
name|getDiscoveryServices
argument_list|()
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"camel.cloud.service-filter.blacklist[custom-svc-list]"
argument_list|,
name|SpringBootPropertyUtil
operator|.
name|getServiceFilterBlacklist
argument_list|()
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"ribbon.enabled"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"debug"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|prop
return|;
block|}
comment|// *************************************
comment|// Config
comment|//
annotation|@
name|Configuration
DECL|class|SpringBootPropertySourceConfig
specifier|public
specifier|static
class|class
name|SpringBootPropertySourceConfig
block|{
annotation|@
name|Autowired
DECL|field|env
specifier|private
name|ConfigurableEnvironment
name|env
decl_stmt|;
annotation|@
name|Bean
annotation|@
name|Lazy
argument_list|(
literal|false
argument_list|)
DECL|method|springBootPropertySource ()
specifier|public
name|MutablePropertySources
name|springBootPropertySource
parameter_list|()
block|{
name|MutablePropertySources
name|sources
init|=
name|env
operator|.
name|getPropertySources
argument_list|()
decl_stmt|;
name|sources
operator|.
name|addFirst
argument_list|(
operator|new
name|PropertiesPropertySource
argument_list|(
literal|"boot-test-property"
argument_list|,
name|CamelCloudServiceCallSimpleExpressionTest
operator|.
name|getAllProperties
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sources
return|;
block|}
block|}
block|}
end_class

end_unit

