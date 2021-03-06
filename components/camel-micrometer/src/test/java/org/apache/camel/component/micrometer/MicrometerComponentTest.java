begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|MeterRegistry
import|;
end_import

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
name|Tags
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
name|Endpoint
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
name|RuntimeCamelException
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
name|TypeConverter
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
name|spi
operator|.
name|Registry
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
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|instanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|not
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|notNullValue
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
name|assertThat
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
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|MicrometerComponentTest
specifier|public
class|class
name|MicrometerComponentTest
block|{
annotation|@
name|Mock
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Mock
DECL|field|typeConverter
specifier|private
name|TypeConverter
name|typeConverter
decl_stmt|;
annotation|@
name|Mock
DECL|field|camelRegistry
specifier|private
name|Registry
name|camelRegistry
decl_stmt|;
annotation|@
name|Mock
DECL|field|metricRegistry
specifier|private
name|MeterRegistry
name|metricRegistry
decl_stmt|;
DECL|field|inOrder
specifier|private
name|InOrder
name|inOrder
decl_stmt|;
DECL|field|component
specifier|private
name|MicrometerComponent
name|component
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|component
operator|=
operator|new
name|MicrometerComponent
argument_list|()
expr_stmt|;
name|inOrder
operator|=
name|Mockito
operator|.
name|inOrder
argument_list|(
name|camelContext
argument_list|,
name|camelRegistry
argument_list|,
name|metricRegistry
argument_list|,
name|typeConverter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateEndpoint ()
specifier|public
name|void
name|testCreateEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|camelRegistry
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|typeConverter
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"key=value"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"key=value"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
name|MicrometerConstants
operator|.
name|METRICS_REGISTRY_NAME
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metricRegistry
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"tags"
argument_list|,
literal|"key=value"
argument_list|)
expr_stmt|;
name|Endpoint
name|result
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"micrometer:counter:counter"
argument_list|,
literal|"counter:counter"
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|MicrometerEndpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|MicrometerEndpoint
name|me
init|=
operator|(
name|MicrometerEndpoint
operator|)
name|result
decl_stmt|;
name|assertThat
argument_list|(
name|me
operator|.
name|getMetricsName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"counter"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|me
operator|.
name|getRegistry
argument_list|()
argument_list|,
name|is
argument_list|(
name|metricRegistry
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getRegistry
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelRegistry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|lookupByNameAndType
argument_list|(
name|MicrometerConstants
operator|.
name|METRICS_REGISTRY_NAME
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getTypeConverter
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|typeConverter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"key=value"
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
DECL|method|testCreateNewEndpointForCounter ()
specifier|public
name|void
name|testCreateNewEndpointForCounter
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
operator|new
name|MicrometerEndpoint
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|metricRegistry
argument_list|,
name|Meter
operator|.
name|Type
operator|.
name|COUNTER
argument_list|,
literal|"a name"
argument_list|,
name|Tags
operator|.
name|empty
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoint
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|MicrometerEndpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateNewEndpointForHistogram ()
specifier|public
name|void
name|testCreateNewEndpointForHistogram
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
operator|new
name|MicrometerEndpoint
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|metricRegistry
argument_list|,
name|Meter
operator|.
name|Type
operator|.
name|DISTRIBUTION_SUMMARY
argument_list|,
literal|"a name"
argument_list|,
name|Tags
operator|.
name|empty
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoint
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|MicrometerEndpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateNewEndpointForTimer ()
specifier|public
name|void
name|testCreateNewEndpointForTimer
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
operator|new
name|MicrometerEndpoint
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|metricRegistry
argument_list|,
name|Meter
operator|.
name|Type
operator|.
name|TIMER
argument_list|,
literal|"a name"
argument_list|,
name|Tags
operator|.
name|empty
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoint
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|MicrometerEndpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetMetricsType ()
specifier|public
name|void
name|testGetMetricsType
parameter_list|()
block|{
name|Meter
operator|.
name|Type
index|[]
name|supportedTypes
init|=
block|{
name|Meter
operator|.
name|Type
operator|.
name|COUNTER
block|,
name|Meter
operator|.
name|Type
operator|.
name|DISTRIBUTION_SUMMARY
block|,
name|Meter
operator|.
name|Type
operator|.
name|TIMER
block|}
decl_stmt|;
for|for
control|(
name|Meter
operator|.
name|Type
name|type
range|:
name|supportedTypes
control|)
block|{
name|assertThat
argument_list|(
name|component
operator|.
name|getMetricsType
argument_list|(
name|MicrometerUtils
operator|.
name|getName
argument_list|(
name|type
argument_list|)
operator|+
literal|":metrics-name"
argument_list|)
argument_list|,
name|is
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testGetMetricsTypeNotSet ()
specifier|public
name|void
name|testGetMetricsTypeNotSet
parameter_list|()
block|{
name|assertThat
argument_list|(
name|component
operator|.
name|getMetricsType
argument_list|(
literal|"no-metrics-type"
argument_list|)
argument_list|,
name|is
argument_list|(
name|MicrometerComponent
operator|.
name|DEFAULT_METER_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RuntimeCamelException
operator|.
name|class
argument_list|)
DECL|method|testGetMetricsTypeNotFound ()
specifier|public
name|void
name|testGetMetricsTypeNotFound
parameter_list|()
block|{
name|component
operator|.
name|getMetricsType
argument_list|(
literal|"unknown-metrics:metrics-name"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetOrCreateMetricRegistryFoundInCamelRegistry ()
specifier|public
name|void
name|testGetOrCreateMetricRegistryFoundInCamelRegistry
parameter_list|()
block|{
name|when
argument_list|(
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metricRegistry
argument_list|)
expr_stmt|;
name|MeterRegistry
name|result
init|=
name|MicrometerUtils
operator|.
name|getOrCreateMeterRegistry
argument_list|(
name|camelRegistry
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|metricRegistry
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelRegistry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MeterRegistry
operator|.
name|class
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
DECL|method|testGetOrCreateMetricRegistryFoundInCamelRegistryByType ()
specifier|public
name|void
name|testGetOrCreateMetricRegistryFoundInCamelRegistryByType
parameter_list|()
block|{
name|when
argument_list|(
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelRegistry
operator|.
name|findByType
argument_list|(
name|MeterRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|metricRegistry
argument_list|)
argument_list|)
expr_stmt|;
name|MeterRegistry
name|result
init|=
name|MicrometerUtils
operator|.
name|getOrCreateMeterRegistry
argument_list|(
name|camelRegistry
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|metricRegistry
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelRegistry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelRegistry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|findByType
argument_list|(
name|MeterRegistry
operator|.
name|class
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
DECL|method|testGetOrCreateMetricRegistryNotFoundInCamelRegistry ()
specifier|public
name|void
name|testGetOrCreateMetricRegistryNotFoundInCamelRegistry
parameter_list|()
block|{
name|when
argument_list|(
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelRegistry
operator|.
name|findByType
argument_list|(
name|MeterRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|MeterRegistry
name|result
init|=
name|MicrometerUtils
operator|.
name|getOrCreateMeterRegistry
argument_list|(
name|camelRegistry
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|not
argument_list|(
name|metricRegistry
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelRegistry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelRegistry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|findByType
argument_list|(
name|MeterRegistry
operator|.
name|class
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
DECL|method|testGetMetricRegistryFromCamelRegistry ()
specifier|public
name|void
name|testGetMetricRegistryFromCamelRegistry
parameter_list|()
block|{
name|when
argument_list|(
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metricRegistry
argument_list|)
expr_stmt|;
name|MeterRegistry
name|result
init|=
name|MicrometerUtils
operator|.
name|getMeterRegistryFromCamelRegistry
argument_list|(
name|camelRegistry
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|metricRegistry
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|camelRegistry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MeterRegistry
operator|.
name|class
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
DECL|method|testCreateMetricRegistry ()
specifier|public
name|void
name|testCreateMetricRegistry
parameter_list|()
block|{
name|MeterRegistry
name|registry
init|=
name|MicrometerUtils
operator|.
name|createMeterRegistry
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|registry
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

