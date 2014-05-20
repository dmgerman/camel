begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.metrics.histogram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|metrics
operator|.
name|histogram
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
name|Exchange
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
name|DefaultProducer
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
name|Histogram
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
DECL|class|HistogramProducer
specifier|public
class|class
name|HistogramProducer
extends|extends
name|DefaultProducer
block|{
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
name|HistogramProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|HistogramProducer (Endpoint endpoint)
specifier|public
name|HistogramProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|HistogramEndpoint
name|endpoint
init|=
operator|(
name|HistogramEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
name|MetricRegistry
name|registry
init|=
name|endpoint
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|String
name|metricsName
init|=
name|endpoint
operator|.
name|getMetricsName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Histogram
name|histogram
init|=
name|registry
operator|.
name|histogram
argument_list|(
name|metricsName
argument_list|)
decl_stmt|;
name|Long
name|value
init|=
name|endpoint
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|histogram
operator|.
name|update
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot update histogram \"{}\" with null value"
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

