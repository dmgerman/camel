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
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|AMQP
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|ConnectionFactory
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * @author Stephen Samuel  */
end_comment

begin_class
DECL|class|RabbitMQConsumerIntTest
specifier|public
class|class
name|RabbitMQConsumerIntTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RabbitMQConsumerIntTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|EXCHANGE
specifier|private
specifier|static
specifier|final
name|String
name|EXCHANGE
init|=
literal|"ex1"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"rabbitmq:localhost:5672/"
operator|+
name|EXCHANGE
operator|+
literal|"?username=cameltest&password=cameltest"
argument_list|)
DECL|field|from
specifier|private
name|Endpoint
name|from
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|to
specifier|private
name|MockEndpoint
name|to
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
name|from
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|sentMessageIsReceived ()
specifier|public
name|void
name|sentMessageIsReceived
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|to
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|ConnectionFactory
name|factory
init|=
operator|new
name|ConnectionFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setPort
argument_list|(
literal|5672
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setUsername
argument_list|(
literal|"cameltest"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setPassword
argument_list|(
literal|"cameltest"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setVirtualHost
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|Connection
name|conn
init|=
name|factory
operator|.
name|newConnection
argument_list|()
decl_stmt|;
name|AMQP
operator|.
name|BasicProperties
operator|.
name|Builder
name|properties
init|=
operator|new
name|AMQP
operator|.
name|BasicProperties
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|Channel
name|channel
init|=
name|conn
operator|.
name|createChannel
argument_list|()
decl_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|EXCHANGE
argument_list|,
literal|""
argument_list|,
name|properties
operator|.
name|build
argument_list|()
argument_list|,
literal|"hello world"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|to
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

