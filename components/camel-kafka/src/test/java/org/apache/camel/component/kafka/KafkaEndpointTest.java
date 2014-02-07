begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|ThreadPoolExecutor
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|message
operator|.
name|MessageAndMetadata
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * @author Stephen Samuel  */
end_comment

begin_class
DECL|class|KafkaEndpointTest
specifier|public
class|class
name|KafkaEndpointTest
block|{
annotation|@
name|Test
DECL|method|testCreatingKafkaExchangeSetsHeaders ()
specifier|public
name|void
name|testCreatingKafkaExchangeSetsHeaders
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|KafkaEndpoint
name|endpoint
init|=
operator|new
name|KafkaEndpoint
argument_list|(
literal|"kafka:localhost"
argument_list|,
literal|"localhost"
argument_list|,
operator|new
name|KafkaComponent
argument_list|()
argument_list|)
decl_stmt|;
name|MessageAndMetadata
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|mm
init|=
operator|new
name|MessageAndMetadata
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
argument_list|(
literal|"somekey"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"mymessage"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"topic"
argument_list|,
literal|4
argument_list|,
literal|56
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createKafkaExchange
argument_list|(
name|mm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"somekey"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"topic"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|creatingExecutorUsesThreadPoolSettings ()
specifier|public
name|void
name|creatingExecutorUsesThreadPoolSettings
parameter_list|()
throws|throws
name|Exception
block|{
name|KafkaEndpoint
name|endpoint
init|=
operator|new
name|KafkaEndpoint
argument_list|(
literal|"kafka:localhost"
argument_list|,
literal|"kafka:localhost"
argument_list|,
operator|new
name|KafkaComponent
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setConsumerStreams
argument_list|(
literal|44
argument_list|)
expr_stmt|;
name|ThreadPoolExecutor
name|executor
init|=
name|endpoint
operator|.
name|createExecutor
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|44
argument_list|,
name|executor
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|assertSingleton ()
specifier|public
name|void
name|assertSingleton
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|KafkaEndpoint
name|endpoint
init|=
operator|new
name|KafkaEndpoint
argument_list|(
literal|"kafka:localhost"
argument_list|,
literal|"localhost"
argument_list|,
operator|new
name|KafkaComponent
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

