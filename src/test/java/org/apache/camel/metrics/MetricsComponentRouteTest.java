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
name|Date
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
name|Test
DECL|method|testMessageContentDelivery ()
specifier|public
name|void
name|testMessageContentDelivery
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
name|String
name|body
init|=
literal|"Message Body"
decl_stmt|;
name|String
name|header1
init|=
literal|"Header 1"
decl_stmt|;
name|String
name|header2
init|=
literal|"Header 2"
decl_stmt|;
name|Object
name|value1
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Object
name|value2
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|header1
argument_list|,
name|value1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|header2
argument_list|,
name|value2
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
name|headers
operator|.
name|put
argument_list|(
name|header1
argument_list|,
name|value1
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|header2
argument_list|,
name|value2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
name|body
argument_list|,
name|headers
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
operator|.
name|to
argument_list|(
literal|"metrics:timer:T?action=start"
argument_list|)
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
literal|"metrics:timer:T"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:histogram:E?value=12000000031"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:timer:T?action=stop"
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

