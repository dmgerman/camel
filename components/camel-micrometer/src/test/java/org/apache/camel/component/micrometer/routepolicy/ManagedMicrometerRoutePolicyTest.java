begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.routepolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|routepolicy
package|;
end_package

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Meter
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
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
annotation|@
name|Ignore
DECL|class|ManagedMicrometerRoutePolicyTest
specifier|public
class|class
name|ManagedMicrometerRoutePolicyTest
extends|extends
name|AbstractMicrometerRoutePolicyTest
block|{
DECL|method|getMBeanServer ()
specifier|protected
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testMetricsRoutePolicy ()
specifier|public
name|void
name|testMetricsRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:bar"
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// there should be 3 names
name|List
argument_list|<
name|Meter
argument_list|>
name|meters
init|=
name|registry
operator|.
name|getMeters
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|meters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// there should be 3 mbeans
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|set
init|=
name|getMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.camel.micrometer:*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|String
operator|.
name|format
argument_list|(
literal|"org.apache.camel:context=%s,type=services,name=MicrometerRegistryService"
argument_list|,
name|context
operator|.
name|getManagementName
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
name|json
init|=
operator|(
name|String
operator|)
name|getMBeanServer
argument_list|()
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"dumpStatisticsAsJson"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"bar.responses"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"foo.responses"
argument_list|)
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
literal|"seda:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"micrometer:counter:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
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
block|}
end_class

end_unit

