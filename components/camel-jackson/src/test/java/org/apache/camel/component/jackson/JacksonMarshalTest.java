begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
package|;
end_package

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
DECL|class|JacksonMarshalTest
specifier|public
class|class
name|JacksonMarshalTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalMap ()
specifier|public
name|void
name|testMarshalAndUnmarshalMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|in
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|in
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reverse"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:in"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|String
name|marshalledAsString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"name\":\"Camel\"}"
argument_list|,
name|marshalledAsString
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:back"
argument_list|,
name|marshalled
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalMapWithPrettyPrint ()
specifier|public
name|void
name|testMarshalAndUnmarshalMapWithPrettyPrint
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|in
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|in
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reverse"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:inPretty"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|String
name|marshalledAsString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|String
operator|.
name|format
argument_list|(
literal|"{%s  \"name\" : \"Camel\"%s}"
argument_list|,
name|LS
argument_list|,
name|LS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|marshalledAsString
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:backPretty"
argument_list|,
name|marshalled
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalPojo ()
specifier|public
name|void
name|testMarshalAndUnmarshalPojo
parameter_list|()
throws|throws
name|Exception
block|{
name|TestPojo
name|in
init|=
operator|new
name|TestPojo
argument_list|()
decl_stmt|;
name|in
operator|.
name|setName
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reversePojo"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|TestPojo
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:inPojo"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|String
name|marshalledAsString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"name\":\"Camel\"}"
argument_list|,
name|marshalledAsString
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:backPojo"
argument_list|,
name|marshalled
argument_list|)
expr_stmt|;
name|mock
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
name|JacksonDataFormat
name|format
init|=
operator|new
name|JacksonDataFormat
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|marshal
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:back"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
name|JacksonDataFormat
name|prettyPrintDataFormat
init|=
operator|new
name|JacksonDataFormat
argument_list|()
decl_stmt|;
name|prettyPrintDataFormat
operator|.
name|setPrettyPrint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:inPretty"
argument_list|)
operator|.
name|marshal
argument_list|(
name|prettyPrintDataFormat
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:backPretty"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|prettyPrintDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
name|JacksonDataFormat
name|formatPojo
init|=
operator|new
name|JacksonDataFormat
argument_list|(
name|TestPojo
operator|.
name|class
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:inPojo"
argument_list|)
operator|.
name|marshal
argument_list|(
name|formatPojo
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:backPojo"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|formatPojo
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reversePojo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

