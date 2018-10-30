begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.nsq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|NSQProducer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|exceptions
operator|.
name|NSQException
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
name|EndpointInject
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
name|junit
operator|.
name|Test
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
name|TimeoutException
import|;
end_import

begin_class
DECL|class|NsqConsumerTest
specifier|public
class|class
name|NsqConsumerTest
extends|extends
name|NsqTestSupport
block|{
DECL|field|NUMBER_OF_MESSAGES
specifier|private
specifier|static
specifier|final
name|int
name|NUMBER_OF_MESSAGES
init|=
literal|10000
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mockResultEndpoint
specifier|protected
name|MockEndpoint
name|mockResultEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testConsumer ()
specifier|public
name|void
name|testConsumer
parameter_list|()
throws|throws
name|NSQException
throws|,
name|TimeoutException
throws|,
name|InterruptedException
block|{
name|mockResultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockResultEndpoint
operator|.
name|setAssertPeriod
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|NSQProducer
name|producer
init|=
operator|new
name|NSQProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|addAddress
argument_list|(
literal|"localhost"
argument_list|,
literal|4150
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|produce
argument_list|(
literal|"test"
argument_list|,
operator|(
literal|"Hello NSQ!"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|mockResultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello NSQ!"
argument_list|,
name|mockResultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLoadConsumer ()
specifier|public
name|void
name|testLoadConsumer
parameter_list|()
throws|throws
name|NSQException
throws|,
name|TimeoutException
throws|,
name|InterruptedException
block|{
name|mockResultEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
name|NUMBER_OF_MESSAGES
argument_list|)
expr_stmt|;
name|mockResultEndpoint
operator|.
name|setAssertPeriod
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|NSQProducer
name|producer
init|=
operator|new
name|NSQProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|addAddress
argument_list|(
literal|"localhost"
argument_list|,
literal|4150
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|NUMBER_OF_MESSAGES
condition|;
name|i
operator|++
control|)
block|{
name|producer
operator|.
name|produce
argument_list|(
literal|"test"
argument_list|,
operator|(
literal|"test"
operator|+
name|i
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mockResultEndpoint
operator|.
name|assertIsSatisfied
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
throws|throws
name|Exception
block|{
name|fromF
argument_list|(
literal|"nsq://%s?topic=%s&lookupInterval=5s"
argument_list|,
name|getNsqConsumerUrl
argument_list|()
argument_list|,
literal|"test"
argument_list|)
operator|.
name|to
argument_list|(
name|mockResultEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

