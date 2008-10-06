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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Expression
import|;
end_import

begin_comment
comment|/**  * A {@link Collection} which aggregates exchanges together,  * using a correlation {@link Expression} and a {@link AggregationStrategy}.  *<p/>  * The Default Implementation will group messages based on the correlation expression.  * Other implementations could for instance just add all exchanges as a batch.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|AggregationCollection
specifier|public
interface|interface
name|AggregationCollection
extends|extends
name|Collection
argument_list|<
name|Exchange
argument_list|>
block|{
comment|/**      * Gets the correlation expression      */
DECL|method|getCorrelationExpression ()
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|getCorrelationExpression
parameter_list|()
function_decl|;
comment|/**      * Sets the correlation expression to be used      */
DECL|method|setCorrelationExpression (Expression<Exchange> correlationExpression)
name|void
name|setCorrelationExpression
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlationExpression
parameter_list|)
function_decl|;
comment|/**      * Gets the aggregation strategy      */
DECL|method|getAggregationStrategy ()
name|AggregationStrategy
name|getAggregationStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets the aggregation strategy to be used      */
DECL|method|setAggregationStrategy (AggregationStrategy aggregationStrategy)
name|void
name|setAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
function_decl|;
comment|/**      * Adds the given exchange to this collection      */
DECL|method|add (Exchange exchange)
name|boolean
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Gets the iterator to iterate this collection.      */
DECL|method|iterator ()
name|Iterator
argument_list|<
name|Exchange
argument_list|>
name|iterator
parameter_list|()
function_decl|;
comment|/**      * Gets the size of this collection      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Clears this colleciton      */
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

