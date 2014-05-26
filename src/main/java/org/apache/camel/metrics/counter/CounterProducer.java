begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.metrics.counter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|metrics
operator|.
name|counter
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|metrics
operator|.
name|MetricsComponent
operator|.
name|HEADER_COUNTER_DECREMENT
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
name|metrics
operator|.
name|MetricsComponent
operator|.
name|HEADER_COUNTER_INCREMENT
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
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Counter
import|;
end_import

begin_class
DECL|class|CounterProducer
specifier|public
class|class
name|CounterProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|CounterProducer (Endpoint endpoint)
specifier|public
name|CounterProducer
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
name|CounterEndpoint
name|endpoint
init|=
operator|(
name|CounterEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|metricName
init|=
name|endpoint
operator|.
name|getMetricsName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Counter
name|counter
init|=
name|endpoint
operator|.
name|getRegistry
argument_list|()
operator|.
name|counter
argument_list|(
name|metricName
argument_list|)
decl_stmt|;
name|Long
name|increment
init|=
name|endpoint
operator|.
name|getIncrement
argument_list|()
decl_stmt|;
name|Long
name|decrement
init|=
name|endpoint
operator|.
name|getDecrement
argument_list|()
decl_stmt|;
name|Long
name|finalIncrement
init|=
name|endpoint
operator|.
name|getLongHeader
argument_list|(
name|exchange
argument_list|,
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|increment
argument_list|)
decl_stmt|;
name|Long
name|finalDecrement
init|=
name|endpoint
operator|.
name|getLongHeader
argument_list|(
name|exchange
argument_list|,
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|decrement
argument_list|)
decl_stmt|;
if|if
condition|(
name|finalIncrement
operator|!=
literal|null
condition|)
block|{
name|counter
operator|.
name|inc
argument_list|(
name|finalIncrement
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|finalDecrement
operator|!=
literal|null
condition|)
block|{
name|counter
operator|.
name|dec
argument_list|(
name|finalDecrement
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|counter
operator|.
name|inc
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

