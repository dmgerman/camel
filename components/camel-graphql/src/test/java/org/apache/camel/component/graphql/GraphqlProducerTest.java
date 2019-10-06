begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.graphql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|graphql
package|;
end_package

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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|json
operator|.
name|JsonObject
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
DECL|class|GraphqlProducerTest
specifier|public
class|class
name|GraphqlProducerTest
block|{
annotation|@
name|Test
DECL|method|shouldBuildRequestBodyWithQuery ()
specifier|public
name|void
name|shouldBuildRequestBodyWithQuery
parameter_list|()
block|{
name|String
name|query
init|=
literal|"queryText"
decl_stmt|;
name|String
name|body
init|=
name|GraphqlProducer
operator|.
name|buildRequestBody
argument_list|(
name|query
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|expectedBody
init|=
literal|"{"
operator|+
literal|"\"query\":\"queryText\","
operator|+
literal|"\"operationName\":null,"
operator|+
literal|"\"variables\":{}"
operator|+
literal|"}"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedBody
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldBuildRequestBodyWithQueryOperationNameAndVariables ()
specifier|public
name|void
name|shouldBuildRequestBodyWithQueryOperationNameAndVariables
parameter_list|()
block|{
name|String
name|query
init|=
literal|"queryText"
decl_stmt|;
name|String
name|operationName
init|=
literal|"queryName"
decl_stmt|;
name|JsonObject
name|variables
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|variables
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|variables
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|GraphqlProducer
operator|.
name|buildRequestBody
argument_list|(
name|query
argument_list|,
name|operationName
argument_list|,
name|variables
argument_list|)
decl_stmt|;
name|String
name|expectedBody
init|=
literal|"{"
operator|+
literal|"\"query\":\"queryText\","
operator|+
literal|"\"operationName\":\"queryName\","
operator|+
literal|"\"variables\":{\"key1\":\"value1\",\"key2\":\"value2\"}"
operator|+
literal|"}"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedBody
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

