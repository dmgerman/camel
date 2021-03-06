begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics
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
name|Meter
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
name|CamelSpringRunner
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
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
operator|.
name|MetricsComponent
operator|.
name|METRIC_REGISTRY_NAME
import|;
end_import

begin_import
import|import static
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
name|MetricsConstants
operator|.
name|HEADER_METER_MARK
import|;
end_import

begin_import
import|import static
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
name|MetricsConstants
operator|.
name|HEADER_METRIC_NAME
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
name|reset
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
name|CamelSpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
block|{
name|MeterRouteTest
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
DECL|class|MeterRouteTest
specifier|public
class|class
name|MeterRouteTest
block|{
annotation|@
name|EndpointInject
argument_list|(
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
literal|"direct:in-1"
argument_list|)
DECL|field|producer1
specifier|private
name|ProducerTemplate
name|producer1
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:in-2"
argument_list|)
DECL|field|producer2
specifier|private
name|ProducerTemplate
name|producer2
decl_stmt|;
DECL|field|mockRegistry
specifier|private
name|MetricRegistry
name|mockRegistry
decl_stmt|;
DECL|field|mockMeter
specifier|private
name|Meter
name|mockMeter
decl_stmt|;
DECL|field|inOrder
specifier|private
name|InOrder
name|inOrder
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
literal|"direct:in-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:A?mark=3179"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:in-2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:A"
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
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
comment|// TODO - 12.05.2014, Lauri - is there any better way to set this up?
name|mockRegistry
operator|=
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
name|METRIC_REGISTRY_NAME
argument_list|,
name|MetricRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockMeter
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Meter
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|=
name|Mockito
operator|.
name|inOrder
argument_list|(
name|mockRegistry
argument_list|,
name|mockMeter
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockRegistry
operator|.
name|meter
argument_list|(
literal|"A"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockMeter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|endpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|mockRegistry
argument_list|,
name|mockMeter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testValueSetInUri ()
specifier|public
name|void
name|testValueSetInUri
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|producer1
operator|.
name|sendBody
argument_list|(
name|body
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
name|meter
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|mockMeter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|mark
argument_list|(
literal|3179L
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testValueNoSetInUri ()
specifier|public
name|void
name|testValueNoSetInUri
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|producer2
operator|.
name|sendBody
argument_list|(
name|body
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
name|meter
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|mockMeter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|mark
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOverrideMetricsName ()
specifier|public
name|void
name|testOverrideMetricsName
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockRegistry
operator|.
name|meter
argument_list|(
literal|"B"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockMeter
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|producer1
operator|.
name|sendBodyAndHeader
argument_list|(
name|body
argument_list|,
name|HEADER_METRIC_NAME
argument_list|,
literal|"B"
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
name|meter
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|mockMeter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|mark
argument_list|(
literal|3179L
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOverrideValueWithHeader ()
specifier|public
name|void
name|testOverrideValueWithHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|producer1
operator|.
name|sendBodyAndHeader
argument_list|(
name|body
argument_list|,
name|HEADER_METER_MARK
argument_list|,
literal|9926L
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
name|meter
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|mockMeter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|mark
argument_list|(
literal|9926L
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testValueNoSetInUriOverrideWithHeader ()
specifier|public
name|void
name|testValueNoSetInUriOverrideWithHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|producer2
operator|.
name|sendBodyAndHeader
argument_list|(
name|body
argument_list|,
name|HEADER_METER_MARK
argument_list|,
literal|7707370L
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
name|meter
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|mockMeter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|mark
argument_list|(
literal|7707370L
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

