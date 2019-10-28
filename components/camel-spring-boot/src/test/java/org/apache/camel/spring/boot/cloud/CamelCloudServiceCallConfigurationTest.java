begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceChooser
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
name|cloud
operator|.
name|ServiceDiscovery
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
name|cloud
operator|.
name|ServiceFilter
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
name|cloud
operator|.
name|ServiceLoadBalancer
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
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigurations
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
name|core
operator|.
name|env
operator|.
name|Environment
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
name|assertFalse
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
annotation|@
name|Ignore
argument_list|(
literal|"TODO: Fix me later"
argument_list|)
DECL|class|CamelCloudServiceCallConfigurationTest
specifier|public
class|class
name|CamelCloudServiceCallConfigurationTest
block|{
annotation|@
name|Test
DECL|method|testConfiguration ()
specifier|public
name|void
name|testConfiguration
parameter_list|()
block|{
operator|new
name|ApplicationContextRunner
argument_list|()
operator|.
name|withConfiguration
argument_list|(
name|AutoConfigurations
operator|.
name|of
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelCloudAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelCloudServiceChooserAutoConfiguration
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|withPropertyValues
argument_list|(
literal|"camel.cloud.enabled=false"
argument_list|,
literal|"camel.cloud.service-discovery.enabled=false"
argument_list|,
literal|"camel.cloud.service-filter.enabled=false"
argument_list|,
literal|"camel.cloud.service-chooser.enabled=true"
argument_list|,
literal|"camel.cloud.load-balancer.enabled=false"
argument_list|,
literal|"debug=false"
argument_list|)
operator|.
name|run
argument_list|(
parameter_list|(
name|context
parameter_list|)
lambda|->
block|{
name|Environment
name|env
init|=
name|context
operator|.
name|getEnvironment
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|env
operator|.
name|getProperty
argument_list|(
literal|"camel.cloud.enabled"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|env
operator|.
name|getProperty
argument_list|(
literal|"camel.cloud.service-discovery.enabled"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|env
operator|.
name|getProperty
argument_list|(
literal|"camel.cloud.service-filter.enabled"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|env
operator|.
name|getProperty
argument_list|(
literal|"camel.cloud.service-chooser.enabled"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|env
operator|.
name|getProperty
argument_list|(
literal|"camel.cloud.load-balancer.enabled"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getBeansOfType
argument_list|(
name|ServiceDiscovery
operator|.
name|class
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getBeansOfType
argument_list|(
name|ServiceFilter
operator|.
name|class
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getBeansOfType
argument_list|(
name|ServiceChooser
operator|.
name|class
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getBeansOfType
argument_list|(
name|ServiceLoadBalancer
operator|.
name|class
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

