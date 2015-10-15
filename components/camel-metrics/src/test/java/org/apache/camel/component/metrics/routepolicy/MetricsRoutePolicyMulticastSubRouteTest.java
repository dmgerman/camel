begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics.routepolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
operator|.
name|routepolicy
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
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Timer
import|;
end_import

begin_comment
comment|/**  * CAMEL-9226 - check metrics are counted correctly in multicast sub-routes  */
end_comment

begin_class
DECL|class|MetricsRoutePolicyMulticastSubRouteTest
specifier|public
class|class
name|MetricsRoutePolicyMulticastSubRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|registry
specifier|private
name|MetricRegistry
name|registry
init|=
operator|new
name|MetricRegistry
argument_list|()
decl_stmt|;
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
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|MetricsRoutePolicyFactory
name|factory
init|=
operator|new
name|MetricsRoutePolicyFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setUseJmx
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setMetricsRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutePolicyFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
name|context
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
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:multicast"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// there should be 3 names
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|registry
operator|.
name|getNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// there should be 3 Counters
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|registry
operator|.
name|getTimers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Timer
argument_list|>
name|timerEntry
range|:
name|registry
operator|.
name|getTimers
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|metricName
init|=
name|timerEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Timer
name|timer
init|=
name|timerEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// each count should be 1
name|assertEquals
argument_list|(
literal|"Count is wrong for "
operator|+
name|metricName
argument_list|,
literal|1
argument_list|,
name|timer
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:multicast"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"multicast"
argument_list|)
operator|.
name|multicast
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

