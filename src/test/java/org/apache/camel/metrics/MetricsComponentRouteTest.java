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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_class
DECL|class|MetricsComponentRouteTest
specifier|public
class|class
name|MetricsComponentRouteTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testMetrics ()
specifier|public
name|void
name|testMetrics
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// .to("metrics")
comment|// .to("metrics:")
operator|.
name|to
argument_list|(
literal|"metrics:A"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:counter://B"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:counter:C?increment=19291"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:counter:C?decrement=19292"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:counter:C"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:D"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:D?mark=90001"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:histogram:E"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:histogram:E?value=12000000031"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

