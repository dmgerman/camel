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
name|concurrent
operator|.
name|TimeUnit
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
name|impl
operator|.
name|DefaultComponent
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
name|histogram
operator|.
name|HistogramEndpoint
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
name|metrics
operator|.
name|timer
operator|.
name|TimerEndpoint
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Slf4jReporter
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link MetricsEndpoint}.  */
end_comment

begin_class
DECL|class|MetricsComponent
specifier|public
class|class
name|MetricsComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|METRIC_REGISTRY_NAME
specifier|public
specifier|static
specifier|final
name|String
name|METRIC_REGISTRY_NAME
init|=
literal|"metricRegistry"
decl_stmt|;
DECL|field|DEFAULT_METRICS_TYPE
specifier|public
specifier|static
specifier|final
name|MetricsType
name|DEFAULT_METRICS_TYPE
init|=
name|MetricsType
operator|.
name|METER
decl_stmt|;
DECL|field|DEFAULT_REPORTING_INTERVAL_SECONDS
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_REPORTING_INTERVAL_SECONDS
init|=
literal|60L
decl_stmt|;
DECL|field|HEADER_PERFIX
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_PERFIX
init|=
literal|"CamelMetrics"
decl_stmt|;
DECL|field|HEADER_METRIC_NAME
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_METRIC_NAME
init|=
name|HEADER_PERFIX
operator|+
literal|"Name"
decl_stmt|;
DECL|field|HEADER_COUNTER_INCREMENT
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_COUNTER_INCREMENT
init|=
name|HEADER_PERFIX
operator|+
literal|"CounterIncrement"
decl_stmt|;
DECL|field|HEADER_COUNTER_DECREMENT
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_COUNTER_DECREMENT
init|=
name|HEADER_PERFIX
operator|+
literal|"CounterDecrement"
decl_stmt|;
DECL|field|HEADER_HISTOGRAM_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_HISTOGRAM_VALUE
init|=
name|HEADER_PERFIX
operator|+
literal|"HistogramValue"
decl_stmt|;
DECL|field|HEADER_METER_MARK
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_METER_MARK
init|=
name|HEADER_PERFIX
operator|+
literal|"MeterMark"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MetricsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|metricRegistry
specifier|private
name|MetricRegistry
name|metricRegistry
decl_stmt|;
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|metricRegistry
operator|==
literal|null
condition|)
block|{
name|Registry
name|camelRegistry
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|metricRegistry
operator|=
name|getOrCreateMetricRegistry
argument_list|(
name|camelRegistry
argument_list|,
name|METRIC_REGISTRY_NAME
argument_list|)
expr_stmt|;
block|}
name|String
name|metricsName
init|=
name|getMetricsName
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|MetricsType
name|metricsType
init|=
name|getMetricsType
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Metrics type: {}; name: {}"
argument_list|,
name|metricsType
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|createNewEndpoint
argument_list|(
name|metricRegistry
argument_list|,
name|metricsType
argument_list|,
name|metricsName
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getMetricsName (String remaining)
name|String
name|getMetricsName
parameter_list|(
name|String
name|remaining
parameter_list|)
block|{
name|String
name|name
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
return|return
name|name
operator|==
literal|null
condition|?
name|remaining
else|:
name|name
return|;
block|}
DECL|method|createNewEndpoint (MetricRegistry registry, MetricsType type, String metricsName)
name|Endpoint
name|createNewEndpoint
parameter_list|(
name|MetricRegistry
name|registry
parameter_list|,
name|MetricsType
name|type
parameter_list|,
name|String
name|metricsName
parameter_list|)
block|{
name|Endpoint
name|endpoint
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|COUNTER
case|:
name|endpoint
operator|=
operator|new
name|CounterEndpoint
argument_list|(
name|registry
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
break|break;
case|case
name|METER
case|:
name|endpoint
operator|=
operator|new
name|MeterEndpoint
argument_list|(
name|registry
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
break|break;
case|case
name|HISTOGRAM
case|:
name|endpoint
operator|=
operator|new
name|HistogramEndpoint
argument_list|(
name|registry
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
break|break;
case|case
name|TIMER
case|:
name|endpoint
operator|=
operator|new
name|TimerEndpoint
argument_list|(
name|registry
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Metrics type \""
operator|+
name|type
operator|.
name|toString
argument_list|()
operator|+
literal|"\" not supported"
argument_list|)
throw|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getMetricsType (String remaining)
name|MetricsType
name|getMetricsType
parameter_list|(
name|String
name|remaining
parameter_list|)
block|{
name|String
name|name
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|MetricsType
name|type
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|DEFAULT_METRICS_TYPE
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|MetricsType
operator|.
name|getByName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unknow metrics type \""
operator|+
name|name
operator|+
literal|"\""
argument_list|)
throw|;
block|}
return|return
name|type
return|;
block|}
DECL|method|getOrCreateMetricRegistry (Registry camelRegistry, String registryName)
name|MetricRegistry
name|getOrCreateMetricRegistry
parameter_list|(
name|Registry
name|camelRegistry
parameter_list|,
name|String
name|registryName
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Looking up MetricRegistry from Camel Registry for name \"{}\""
argument_list|,
name|registryName
argument_list|)
expr_stmt|;
name|MetricRegistry
name|result
init|=
name|getMetricRegistryFromCamelRegistry
argument_list|(
name|camelRegistry
argument_list|,
name|registryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"MetricRegistry not found from Camel Registry for name \"{}\""
argument_list|,
name|registryName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating new default MetricRegistry"
argument_list|)
expr_stmt|;
name|result
operator|=
name|createMetricRegistry
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getMetricRegistryFromCamelRegistry (Registry camelRegistry, String registryName)
name|MetricRegistry
name|getMetricRegistryFromCamelRegistry
parameter_list|(
name|Registry
name|camelRegistry
parameter_list|,
name|String
name|registryName
parameter_list|)
block|{
return|return
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
name|registryName
argument_list|,
name|MetricRegistry
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|createMetricRegistry ()
name|MetricRegistry
name|createMetricRegistry
parameter_list|()
block|{
name|MetricRegistry
name|registry
init|=
operator|new
name|MetricRegistry
argument_list|()
decl_stmt|;
specifier|final
name|Slf4jReporter
name|reporter
init|=
name|Slf4jReporter
operator|.
name|forRegistry
argument_list|(
name|registry
argument_list|)
operator|.
name|outputTo
argument_list|(
name|LOG
argument_list|)
operator|.
name|convertRatesTo
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|convertDurationsTo
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|reporter
operator|.
name|start
argument_list|(
name|DEFAULT_REPORTING_INTERVAL_SECONDS
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
block|}
end_class

end_unit

