begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|processor
operator|.
name|aggregate
operator|.
name|AbstractListAggregationStrategy
import|;
end_import

begin_comment
comment|/**  * Aggregate body of input {@link Message} into a single combined Exchange holding all the  * aggregated bodies in a {@link List} of type {@link Object} as the message body.  *  * This aggregation strategy can used in combination with {@link org.apache.camel.processor.Splitter} to batch messages  *  * @version  */
end_comment

begin_class
DECL|class|BatchAggregationStrategy
specifier|public
class|class
name|BatchAggregationStrategy
extends|extends
name|AbstractListAggregationStrategy
argument_list|<
name|Object
argument_list|>
block|{
DECL|method|getValue (Exchange exchange)
specifier|public
name|Object
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
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
end_class

end_unit

