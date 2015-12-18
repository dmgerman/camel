begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
package|;
end_package

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
name|Message
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
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * Aggregate all {@link Message} into a single combined Exchange holding all the  * aggregated messages in a {@link List} of {@link Message} as the message body.  *   * This aggregation strategy can used in combination with @{link  * org.apache.camel.processor.Splitter} to batch messages  *   * @version  */
end_comment

begin_class
DECL|class|GroupedMessageAggregationStrategy
specifier|public
class|class
name|GroupedMessageAggregationStrategy
extends|extends
name|AbstractListAggregationStrategy
argument_list|<
name|Message
argument_list|>
block|{
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
comment|// for the first time we must create a new empty exchange as the
comment|// holder, as the outgoing exchange
comment|// must not be one of the grouped exchanges, as that causes a
comment|// endless circular reference
name|oldExchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|newExchange
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|aggregate
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (Exchange exchange)
specifier|public
name|Message
name|getValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
end_class

end_unit

