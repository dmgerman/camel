begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.cloud
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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceCallConstants
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
name|model
operator|.
name|cloud
operator|.
name|BlacklistServiceCallServiceFilterConfiguration
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
name|model
operator|.
name|cloud
operator|.
name|CombinedServiceCallServiceDiscoveryConfiguration
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
name|model
operator|.
name|cloud
operator|.
name|CombinedServiceCallServiceFilterConfiguration
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
name|model
operator|.
name|cloud
operator|.
name|DefaultServiceCallServiceLoadBalancerConfiguration
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
name|model
operator|.
name|cloud
operator|.
name|HealthyServiceCallServiceFilterConfiguration
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
name|model
operator|.
name|cloud
operator|.
name|ServiceCallConfigurationDefinition
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
name|model
operator|.
name|cloud
operator|.
name|ServiceCallExpressionConfiguration
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
name|model
operator|.
name|cloud
operator|.
name|StaticServiceCallServiceDiscoveryConfiguration
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
name|SpringCamelContext
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|ServiceCallConfigurationTest
specifier|public
class|class
name|ServiceCallConfigurationTest
block|{
annotation|@
name|Test
DECL|method|testServiceDiscoveryConfiguration ()
specifier|public
name|void
name|testServiceDiscoveryConfiguration
parameter_list|()
block|{
name|SpringCamelContext
name|context
init|=
name|createContext
argument_list|(
literal|"org/apache/camel/spring/cloud/ServiceCallConfigurationTest.xml"
argument_list|)
decl_stmt|;
name|testConfiguration1
argument_list|(
name|context
operator|.
name|getServiceCallConfiguration
argument_list|(
literal|"conf1"
argument_list|)
argument_list|)
expr_stmt|;
name|testConfiguration2
argument_list|(
name|context
operator|.
name|getServiceCallConfiguration
argument_list|(
literal|"conf2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConfiguration1 (ServiceCallConfigurationDefinition conf)
specifier|protected
name|void
name|testConfiguration1
parameter_list|(
name|ServiceCallConfigurationDefinition
name|conf
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"conf1"
argument_list|,
name|conf
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No ServiceCallConfiguration (1)"
argument_list|,
name|conf
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No ServiceDiscoveryConfiguration (1)"
argument_list|,
name|conf
operator|.
name|getServiceDiscoveryConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No ServiceCallLoadBalancerConfiguration (1)"
argument_list|,
name|conf
operator|.
name|getLoadBalancerConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|getLoadBalancerConfiguration
argument_list|()
operator|instanceof
name|DefaultServiceCallServiceLoadBalancerConfiguration
argument_list|)
expr_stmt|;
name|ServiceCallExpressionConfiguration
name|expConf1
init|=
name|conf
operator|.
name|getExpressionConfiguration
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|expConf1
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No ServiceCallExpressionConfiguration (1)"
argument_list|,
name|expConf1
operator|.
name|getExpressionType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_HOST
argument_list|,
name|expConf1
operator|.
name|getHostHeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_PORT
argument_list|,
name|expConf1
operator|.
name|getPortHeader
argument_list|()
argument_list|)
expr_stmt|;
name|StaticServiceCallServiceDiscoveryConfiguration
name|discovery1
init|=
operator|(
name|StaticServiceCallServiceDiscoveryConfiguration
operator|)
name|conf
operator|.
name|getServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|discovery1
operator|.
name|getServers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost:9091"
argument_list|,
name|discovery1
operator|.
name|getServers
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConfiguration2 (ServiceCallConfigurationDefinition conf)
specifier|protected
name|void
name|testConfiguration2
parameter_list|(
name|ServiceCallConfigurationDefinition
name|conf
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"conf2"
argument_list|,
name|conf
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No ServiceCallConfiguration (2)"
argument_list|,
name|conf
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No ServiceDiscoveryConfiguration (2)"
argument_list|,
name|conf
operator|.
name|getServiceDiscoveryConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|conf
operator|.
name|getLoadBalancerConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|CombinedServiceCallServiceDiscoveryConfiguration
name|discovery2
init|=
operator|(
name|CombinedServiceCallServiceDiscoveryConfiguration
operator|)
name|conf
operator|.
name|getServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|discovery2
operator|.
name|getServiceDiscoveryConfigurations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|discovery2
operator|.
name|getServiceDiscoveryConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|StaticServiceCallServiceDiscoveryConfiguration
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|discovery2
operator|.
name|getServiceDiscoveryConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|StaticServiceCallServiceDiscoveryConfiguration
argument_list|)
expr_stmt|;
name|ServiceCallExpressionConfiguration
name|expconf
init|=
name|conf
operator|.
name|getExpressionConfiguration
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|expconf
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|expconf
operator|.
name|getExpressionType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MyHostHeader"
argument_list|,
name|expconf
operator|.
name|getHostHeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MyPortHeader"
argument_list|,
name|expconf
operator|.
name|getPortHeader
argument_list|()
argument_list|)
expr_stmt|;
name|StaticServiceCallServiceDiscoveryConfiguration
name|sconf1
init|=
operator|(
name|StaticServiceCallServiceDiscoveryConfiguration
operator|)
name|discovery2
operator|.
name|getServiceDiscoveryConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sconf1
operator|.
name|getServers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost:9092"
argument_list|,
name|sconf1
operator|.
name|getServers
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|StaticServiceCallServiceDiscoveryConfiguration
name|sconf
init|=
operator|(
name|StaticServiceCallServiceDiscoveryConfiguration
operator|)
name|discovery2
operator|.
name|getServiceDiscoveryConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sconf
operator|.
name|getServers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost:9093,localhost:9094,localhost:9095,localhost:9096"
argument_list|,
name|sconf
operator|.
name|getServers
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|CombinedServiceCallServiceFilterConfiguration
name|filter
init|=
operator|(
name|CombinedServiceCallServiceFilterConfiguration
operator|)
name|conf
operator|.
name|getServiceFilterConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|filter
operator|.
name|getServiceFilterConfigurations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|getServiceFilterConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|HealthyServiceCallServiceFilterConfiguration
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|getServiceFilterConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|BlacklistServiceCallServiceFilterConfiguration
argument_list|)
expr_stmt|;
block|}
DECL|method|createContext (String classpathConfigFile)
specifier|protected
name|SpringCamelContext
name|createContext
parameter_list|(
name|String
name|classpathConfigFile
parameter_list|)
block|{
name|ClassPathXmlApplicationContext
name|appContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|classpathConfigFile
argument_list|)
decl_stmt|;
name|SpringCamelContext
name|camelContext
init|=
name|appContext
operator|.
name|getBean
argument_list|(
name|SpringCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No Camel Context in file: "
operator|+
name|classpathConfigFile
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

