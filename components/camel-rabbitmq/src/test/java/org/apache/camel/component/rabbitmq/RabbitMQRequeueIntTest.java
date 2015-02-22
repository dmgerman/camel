begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
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

begin_comment
comment|/**  * Integration test to confirm REQUEUE header causes message to be re-queued instead of sent to DLQ.  *  * Created by Andrew Austin on 2/21/15.  */
end_comment

begin_class
DECL|class|RabbitMQRequeueIntTest
specifier|public
class|class
name|RabbitMQRequeueIntTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|ROUTING_KEY
specifier|public
specifier|static
specifier|final
name|String
name|ROUTING_KEY
init|=
literal|"rk4"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:rabbitMQ"
argument_list|)
DECL|field|directProducer
specifier|protected
name|ProducerTemplate
name|directProducer
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"rabbitmq:localhost:5672/ex4?username=cameltest&password=cameltest"
operator|+
literal|"&autoAck=false&queue=q4&routingKey="
operator|+
name|ROUTING_KEY
argument_list|)
DECL|field|rabbitMQEndpoint
specifier|private
name|Endpoint
name|rabbitMQEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:producing"
argument_list|)
DECL|field|producingMockEndpoint
specifier|private
name|MockEndpoint
name|producingMockEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:consuming"
argument_list|)
DECL|field|consumingMockEndpoint
specifier|private
name|MockEndpoint
name|consumingMockEndpoint
decl_stmt|;
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
name|from
argument_list|(
literal|"direct:rabbitMQ"
argument_list|)
operator|.
name|id
argument_list|(
literal|"producingRoute"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Sending message"
argument_list|)
operator|.
name|inOnly
argument_list|(
name|rabbitMQEndpoint
argument_list|)
operator|.
name|to
argument_list|(
name|producingMockEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|rabbitMQEndpoint
argument_list|)
operator|.
name|id
argument_list|(
literal|"consumingRoute"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Receiving message"
argument_list|)
operator|.
name|inOnly
argument_list|(
name|consumingMockEndpoint
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"requeue header false"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"Setting REQUEUE flag to false"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REQUEUE
argument_list|,
name|constant
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"requeue header true"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"Setting REQUEUE flag to true"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REQUEUE
argument_list|,
name|constant
argument_list|(
literal|true
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"non-boolean header"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"Setting REQUEUE flag to non-boolean"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REQUEUE
argument_list|,
name|constant
argument_list|(
literal|4l
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|throwException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"Simulated exception"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testNoRequeueHeaderCausesReject ()
specifier|public
name|void
name|testNoRequeueHeaderCausesReject
parameter_list|()
throws|throws
name|Exception
block|{
name|producingMockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|consumingMockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|directProducer
operator|.
name|sendBody
argument_list|(
literal|"no requeue header"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|producingMockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|consumingMockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNonBooleanRequeueHeaderCausesReject ()
specifier|public
name|void
name|testNonBooleanRequeueHeaderCausesReject
parameter_list|()
throws|throws
name|Exception
block|{
name|producingMockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|consumingMockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|directProducer
operator|.
name|sendBody
argument_list|(
literal|"non-boolean header"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|producingMockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|consumingMockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFalseRequeueHeaderCausesReject ()
specifier|public
name|void
name|testFalseRequeueHeaderCausesReject
parameter_list|()
throws|throws
name|Exception
block|{
name|producingMockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|consumingMockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|directProducer
operator|.
name|sendBody
argument_list|(
literal|"non-boolean header"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|producingMockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|consumingMockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrueRequeueHeaderCausesRequeue ()
specifier|public
name|void
name|testTrueRequeueHeaderCausesRequeue
parameter_list|()
throws|throws
name|Exception
block|{
name|producingMockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|consumingMockEndpoint
operator|.
name|setMinimumExpectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|directProducer
operator|.
name|sendBody
argument_list|(
literal|"requeue header true"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|producingMockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|consumingMockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

