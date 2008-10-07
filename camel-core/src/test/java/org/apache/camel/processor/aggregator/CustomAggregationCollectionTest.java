begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.processor.aggregator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregator
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|AbstractCollection
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
name|ContextTestSupport
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
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
name|AggregationCollection
import|;
end_import

begin_comment
comment|/**  * Unit test for using our own aggregation collection.  */
end_comment

begin_class
DECL|class|CustomAggregationCollectionTest
specifier|public
class|class
name|CustomAggregationCollectionTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testCustomAggregationCollection ()
specifier|public
name|void
name|testCustomAggregationCollection
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e2
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
comment|// we expect 5 messages since our custom aggregation collection just gets it all
comment|// but returns them in reverse order
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"190"
argument_list|,
literal|"200"
argument_list|,
literal|"130"
argument_list|,
literal|"150"
argument_list|,
literal|"100"
argument_list|)
expr_stmt|;
comment|// then we sent all the message at once
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"100"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"150"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"130"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"200"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"190"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// END SNIPPET: e2
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// our route is aggregating from the direct queue and sending the response to the mock
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// use our own collection for aggregation
operator|.
name|aggregator
argument_list|(
operator|new
name|MyReverseAggregationCollection
argument_list|()
argument_list|)
comment|// wait for 0.5 seconds to aggregate
operator|.
name|batchTimeout
argument_list|(
literal|500L
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e3
DECL|class|MyReverseAggregationCollection
specifier|private
specifier|static
class|class
name|MyReverseAggregationCollection
extends|extends
name|AbstractCollection
argument_list|<
name|Exchange
argument_list|>
implements|implements
name|AggregationCollection
block|{
DECL|field|collection
specifier|private
name|List
argument_list|<
name|Exchange
argument_list|>
name|collection
init|=
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|correlation
specifier|private
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlation
decl_stmt|;
DECL|field|strategy
specifier|private
name|AggregationStrategy
name|strategy
decl_stmt|;
DECL|method|getCorrelationExpression ()
specifier|public
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|getCorrelationExpression
parameter_list|()
block|{
return|return
name|correlation
return|;
block|}
DECL|method|setCorrelationExpression (Expression<Exchange> correlationExpression)
specifier|public
name|void
name|setCorrelationExpression
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlationExpression
parameter_list|)
block|{
name|this
operator|.
name|correlation
operator|=
name|correlationExpression
expr_stmt|;
block|}
DECL|method|getAggregationStrategy ()
specifier|public
name|AggregationStrategy
name|getAggregationStrategy
parameter_list|()
block|{
return|return
name|strategy
return|;
block|}
DECL|method|setAggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|void
name|setAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|aggregationStrategy
expr_stmt|;
block|}
DECL|method|add (Exchange exchange)
specifier|public
name|boolean
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|collection
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|Exchange
argument_list|>
name|iterator
parameter_list|()
block|{
comment|// demonstrate the we can do something with this collection, so we reverse it
name|Collections
operator|.
name|reverse
argument_list|(
name|collection
argument_list|)
expr_stmt|;
return|return
name|collection
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|collection
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|onAggregation (Object correlationKey, Exchange newExchange)
specifier|public
name|void
name|onAggregation
parameter_list|(
name|Object
name|correlationKey
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
name|add
argument_list|(
name|newExchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e3
block|}
end_class

end_unit

