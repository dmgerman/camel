begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|AbstractJdbcGeneratedKeysTest
specifier|public
specifier|abstract
class|class
name|AbstractJdbcGeneratedKeysTest
extends|extends
name|AbstractJdbcTestSupport
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testRetrieveGeneratedKeys (String query, Map<String, Object> parameters)
specifier|protected
name|void
name|testRetrieveGeneratedKeys
parameter_list|(
name|String
name|query
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// first we create our exchange using the endpoint
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:hello"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// then we set the SQL on the in body and add possible parameters
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_RETRIEVE_GENERATED_KEYS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|setHeaders
argument_list|(
name|exchange
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response from Camel
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// assertions of the response
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_DATA
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_ROW_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|generatedKeys
init|=
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_DATA
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"out body could not be converted to an ArrayList - was: "
operator|+
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|generatedKeys
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|generatedKeys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|generatedKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"auto increment value should be 2"
argument_list|,
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"generated keys row count should be one"
argument_list|,
literal|1
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_ROW_COUNT
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRetrieveGeneratedKeys (String query)
specifier|protected
name|void
name|testRetrieveGeneratedKeys
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|Exception
block|{
name|testRetrieveGeneratedKeys
argument_list|(
name|query
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testRetrieveGeneratedKeysWithStringGeneratedColumns (String query, Map<String, Object> parameters)
specifier|protected
name|void
name|testRetrieveGeneratedKeysWithStringGeneratedColumns
parameter_list|(
name|String
name|query
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// first we create our exchange using the endpoint
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:hello"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// then we set the SQL on the in body and add possible parameters
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_RETRIEVE_GENERATED_KEYS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_COLUMNS
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID"
block|}
argument_list|)
expr_stmt|;
name|setHeaders
argument_list|(
name|exchange
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response from Camel
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// assertions of the response
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_DATA
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_ROW_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|generatedKeys
init|=
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_DATA
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"out body could not be converted to an ArrayList - was: "
operator|+
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|generatedKeys
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|generatedKeys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|generatedKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"auto increment value should be 2"
argument_list|,
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"generated keys row count should be one"
argument_list|,
literal|1
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_ROW_COUNT
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRetrieveGeneratedKeysWithStringGeneratedColumns (String query)
specifier|protected
name|void
name|testRetrieveGeneratedKeysWithStringGeneratedColumns
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|Exception
block|{
name|testRetrieveGeneratedKeysWithStringGeneratedColumns
argument_list|(
name|query
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testRetrieveGeneratedKeysWithIntGeneratedColumns (String query, Map<String, Object> parameters)
specifier|protected
name|void
name|testRetrieveGeneratedKeysWithIntGeneratedColumns
parameter_list|(
name|String
name|query
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// first we create our exchange using the endpoint
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:hello"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// then we set the SQL on the in body and add possible parameters
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_RETRIEVE_GENERATED_KEYS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_COLUMNS
argument_list|,
operator|new
name|int
index|[]
block|{
literal|1
block|}
argument_list|)
expr_stmt|;
name|setHeaders
argument_list|(
name|exchange
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response from Camel
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// assertions of the response
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_DATA
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_ROW_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|generatedKeys
init|=
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_DATA
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"out body could not be converted to an ArrayList - was: "
operator|+
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|generatedKeys
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|generatedKeys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|generatedKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"auto increment value should be 2"
argument_list|,
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"generated keys row count should be one"
argument_list|,
literal|1
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_ROW_COUNT
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRetrieveGeneratedKeysWithIntGeneratedColumns (String query)
specifier|protected
name|void
name|testRetrieveGeneratedKeysWithIntGeneratedColumns
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|Exception
block|{
name|testRetrieveGeneratedKeysWithIntGeneratedColumns
argument_list|(
name|query
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown (String query, Map<String, Object> parameters)
specifier|protected
name|void
name|testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown
parameter_list|(
name|String
name|query
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// first we create our exchange using the endpoint
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:hello"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// then we set the SQL on the in body and add possible parameters
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_RETRIEVE_GENERATED_KEYS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|setHeaders
argument_list|(
name|exchange
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// set wrong data type for generated columns
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_COLUMNS
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response from Camel
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown (String query)
specifier|protected
name|void
name|testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|Exception
block|{
name|testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown
argument_list|(
name|query
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|setHeaders (Exchange exchange, Map<String, Object> parameters)
specifier|private
name|void
name|setHeaders
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameter
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|parameter
operator|.
name|getKey
argument_list|()
argument_list|,
name|parameter
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
specifier|abstract
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

