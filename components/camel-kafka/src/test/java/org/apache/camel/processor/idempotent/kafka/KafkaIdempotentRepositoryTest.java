begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.processor.idempotent.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|kafka
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
name|RoutesBuilder
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
name|kafka
operator|.
name|embedded
operator|.
name|EmbeddedKafkaBroker
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
name|kafka
operator|.
name|embedded
operator|.
name|EmbeddedZookeeper
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
name|AvailablePortFinder
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
name|Rule
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
name|Properties
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
name|*
import|;
end_import

begin_comment
comment|/**  * @author jkorab  */
end_comment

begin_class
DECL|class|KafkaIdempotentRepositoryTest
specifier|public
class|class
name|KafkaIdempotentRepositoryTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Rule
DECL|field|zookeeper
specifier|public
name|EmbeddedZookeeper
name|zookeeper
init|=
operator|new
name|EmbeddedZookeeper
argument_list|()
decl_stmt|;
annotation|@
name|Rule
DECL|field|kafkaBroker
specifier|public
name|EmbeddedKafkaBroker
name|kafkaBroker
init|=
operator|new
name|EmbeddedKafkaBroker
argument_list|(
literal|0
argument_list|,
name|zookeeper
operator|.
name|getConnection
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|KafkaIdempotentRepository
name|kafkaIdempotentRepository
init|=
operator|new
name|KafkaIdempotentRepository
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|header
argument_list|(
literal|"id"
argument_list|)
argument_list|,
name|kafkaIdempotentRepository
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testRemovesDuplicate ()
specifier|public
name|void
name|testRemovesDuplicate
parameter_list|()
block|{              }
block|}
end_class

end_unit

