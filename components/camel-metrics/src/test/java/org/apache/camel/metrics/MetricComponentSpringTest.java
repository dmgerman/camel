begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.metrics
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|metrics
package|;
end_package

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Counter
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|EndpointInject
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
name|Produce
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
name|spring
operator|.
name|javaconfig
operator|.
name|SingleRouteCamelConfiguration
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
name|spring
operator|.
name|CamelSpringDelegatingTestContextLoader
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
name|spring
operator|.
name|CamelSpringJUnit4ClassRunner
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
name|spring
operator|.
name|MockEndpoints
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
name|InOrder
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
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
block|{
name|MetricComponentSpringTest
operator|.
name|TestConfig
operator|.
name|class
block|}
argument_list|,
name|loader
operator|=
name|CamelSpringDelegatingTestContextLoader
operator|.
name|class
argument_list|)
annotation|@
name|MockEndpoints
DECL|class|MetricComponentSpringTest
specifier|public
class|class
name|MetricComponentSpringTest
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:out"
argument_list|)
DECL|field|endpoint
specifier|private
name|MockEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:in"
argument_list|)
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|Configuration
DECL|class|TestConfig
specifier|public
specifier|static
class|class
name|TestConfig
extends|extends
name|SingleRouteCamelConfiguration
block|{
annotation|@
name|Bean
annotation|@
name|Override
DECL|method|route ()
specifier|public
name|RouteBuilder
name|route
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
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:counter:A?increment=512"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Bean
argument_list|(
name|name
operator|=
name|MetricsComponent
operator|.
name|METRIC_REGISTRY_NAME
argument_list|)
DECL|method|getMetricRegistry ()
specifier|public
name|MetricRegistry
name|getMetricRegistry
parameter_list|()
block|{
return|return
name|Mockito
operator|.
name|mock
argument_list|(
name|MetricRegistry
operator|.
name|class
argument_list|)
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMetricsRegistryFromCamelRegistry ()
specifier|public
name|void
name|testMetricsRegistryFromCamelRegistry
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO - 12.05.2014, Lauri - is there any better way to set this up?
name|MetricRegistry
name|mockRegistry
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|MetricsComponent
operator|.
name|METRIC_REGISTRY_NAME
argument_list|,
name|MetricRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|Counter
name|mockCounter
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Counter
operator|.
name|class
argument_list|)
decl_stmt|;
name|InOrder
name|inOrder
init|=
name|Mockito
operator|.
name|inOrder
argument_list|(
name|mockRegistry
argument_list|,
name|mockCounter
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockRegistry
operator|.
name|counter
argument_list|(
literal|"A"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCounter
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|mockRegistry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|counter
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|mockCounter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|inc
argument_list|(
literal|512L
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

