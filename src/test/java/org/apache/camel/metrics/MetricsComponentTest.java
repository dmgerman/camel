begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|metrics
operator|.
name|counter
operator|.
name|CounterEndpoint
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
name|metrics
operator|.
name|meter
operator|.
name|MeterEndpoint
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
name|runners
operator|.
name|MockitoJUnitRunner
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|MetricsComponentTest
specifier|public
class|class
name|MetricsComponentTest
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
DECL|field|camelRegistry
specifier|private
name|Registry
name|camelRegistry
decl_stmt|;
annotation|@
name|Mock
DECL|field|metricRegistry
specifier|private
name|MetricRegistry
name|metricRegistry
decl_stmt|;
DECL|field|inOrder
specifier|private
name|InOrder
name|inOrder
decl_stmt|;
DECL|field|component
specifier|private
name|MetricsComponent
name|component
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|=
operator|new
name|MetricsComponent
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
name|camelRegistry
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Long
name|value
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"mark"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|Endpoint
name|result
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"metrics:meter:long.meter"
argument_list|,
literal|"meter:long.meter"
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
name|MeterEndpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|MeterEndpoint
name|me
init|=
operator|(
name|MeterEndpoint
operator|)
name|result
decl_stmt|;
name|assertThat
argument_list|(
name|me
operator|.
name|getMark
argument_list|()
argument_list|,
name|is
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|me
operator|.
name|getMetricsName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"long.meter"
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
name|MetricsComponent
operator|.
name|METRIC_REGISTRY_NAME
argument_list|,
name|MetricRegistry
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
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateEndpoints ()
specifier|public
name|void
name|testCreateEndpoints
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
name|camelRegistry
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Long
name|value
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"mark"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|Endpoint
name|result
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"metrics:meter:long.meter"
argument_list|,
literal|"meter:long.meter"
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
name|MeterEndpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|MeterEndpoint
name|me
init|=
operator|(
name|MeterEndpoint
operator|)
name|result
decl_stmt|;
name|assertThat
argument_list|(
name|me
operator|.
name|getMark
argument_list|()
argument_list|,
name|is
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|me
operator|.
name|getMetricsName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"long.meter"
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
name|params
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"increment"
argument_list|,
name|value
operator|+
literal|1
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"decrement"
argument_list|,
name|value
operator|-
literal|1
argument_list|)
expr_stmt|;
name|result
operator|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"metrics:counter:long.counter"
argument_list|,
literal|"counter:long.counter"
argument_list|,
name|params
argument_list|)
expr_stmt|;
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
name|CounterEndpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|CounterEndpoint
name|ce
init|=
operator|(
name|CounterEndpoint
operator|)
name|result
decl_stmt|;
name|assertThat
argument_list|(
name|ce
operator|.
name|getIncrement
argument_list|()
argument_list|,
name|is
argument_list|(
name|value
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ce
operator|.
name|getDecrement
argument_list|()
argument_list|,
name|is
argument_list|(
name|value
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ce
operator|.
name|getMetricsName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"long.counter"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ce
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
name|MetricsComponent
operator|.
name|METRIC_REGISTRY_NAME
argument_list|,
name|MetricRegistry
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
literal|2
argument_list|)
argument_list|)
operator|.
name|getTypeConverter
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
DECL|method|testGetMetricsName ()
specifier|public
name|void
name|testGetMetricsName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|component
operator|.
name|getMetricsName
argument_list|(
literal|"meter:metric-a"
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"metric-a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getMetricsName
argument_list|(
literal|"meter:metric-a:sub-b"
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"metric-a:sub-b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getMetricsName
argument_list|(
literal|"metric-a"
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"metric-a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getMetricsName
argument_list|(
literal|"//metric-a"
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"//metric-a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getMetricsName
argument_list|(
literal|"meter://metric-a"
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"//metric-a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateNewEndpointForCounter ()
specifier|public
name|void
name|testCreateNewEndpointForCounter
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|component
operator|.
name|createNewEndpoint
argument_list|(
name|metricRegistry
argument_list|,
name|MetricsType
operator|.
name|COUNTER
argument_list|,
literal|"a name"
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
name|CounterEndpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateNewEndpointForMeter ()
specifier|public
name|void
name|testCreateNewEndpointForMeter
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|component
operator|.
name|createNewEndpoint
argument_list|(
name|metricRegistry
argument_list|,
name|MetricsType
operator|.
name|METER
argument_list|,
literal|"a name"
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
name|MeterEndpoint
operator|.
name|class
argument_list|)
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
DECL|method|testCreateNewEndpointForGauge ()
specifier|public
name|void
name|testCreateNewEndpointForGauge
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|createNewEndpoint
argument_list|(
name|metricRegistry
argument_list|,
name|MetricsType
operator|.
name|GAUGE
argument_list|,
literal|"a name"
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
DECL|method|testCreateNewEndpointForHistogram ()
specifier|public
name|void
name|testCreateNewEndpointForHistogram
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|createNewEndpoint
argument_list|(
name|metricRegistry
argument_list|,
name|MetricsType
operator|.
name|HISTOGRAM
argument_list|,
literal|"a name"
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
DECL|method|testCreateNewEndpointForTimer ()
specifier|public
name|void
name|testCreateNewEndpointForTimer
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|createNewEndpoint
argument_list|(
name|metricRegistry
argument_list|,
name|MetricsType
operator|.
name|TIMER
argument_list|,
literal|"a name"
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
throws|throws
name|Exception
block|{
for|for
control|(
name|MetricsType
name|type
range|:
name|EnumSet
operator|.
name|allOf
argument_list|(
name|MetricsType
operator|.
name|class
argument_list|)
control|)
block|{
name|assertThat
argument_list|(
name|component
operator|.
name|getMetricsType
argument_list|(
name|type
operator|.
name|toString
argument_list|()
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
throws|throws
name|Exception
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
name|MetricsComponent
operator|.
name|DEFAULT_METRICS_TYPE
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
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MetricRegistry
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
name|MetricRegistry
name|result
init|=
name|component
operator|.
name|getOrCreateMetricRegistry
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
name|MetricRegistry
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
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MetricRegistry
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
name|MetricRegistry
name|result
init|=
name|component
operator|.
name|getOrCreateMetricRegistry
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
name|MetricRegistry
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
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"name"
argument_list|,
name|MetricRegistry
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
name|MetricRegistry
name|result
init|=
name|component
operator|.
name|getMetricRegistryFromCamelRegistry
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
name|MetricRegistry
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
throws|throws
name|Exception
block|{
name|MetricRegistry
name|registry
init|=
name|component
operator|.
name|createMetricRegistry
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

