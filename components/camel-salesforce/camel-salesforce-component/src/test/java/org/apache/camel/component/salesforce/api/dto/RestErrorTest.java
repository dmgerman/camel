begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
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
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|JsonUtils
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
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|XStreamUtils
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

begin_class
DECL|class|RestErrorTest
specifier|public
class|class
name|RestErrorTest
block|{
DECL|field|error
name|RestError
name|error
init|=
operator|new
name|RestError
argument_list|(
literal|"errorCode"
argument_list|,
literal|"message"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"field1"
argument_list|,
literal|"field2"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldDeserializeFromJson ()
specifier|public
name|void
name|shouldDeserializeFromJson
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ObjectMapper
name|objectMapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
specifier|final
name|ObjectReader
name|reader
init|=
name|objectMapper
operator|.
name|readerFor
argument_list|(
name|RestError
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|RestError
name|gotWithErrorCode
init|=
name|reader
operator|.
expr|<
name|RestError
operator|>
name|readValue
argument_list|(
literal|"{\"errorCode\":\"errorCode\",\"message\":\"message\",\"fields\":[ \"field1\",\"field2\" ]}"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|gotWithErrorCode
argument_list|,
name|error
argument_list|)
expr_stmt|;
specifier|final
name|RestError
name|gotWithStatusCode
init|=
name|reader
operator|.
expr|<
name|RestError
operator|>
name|readValue
argument_list|(
literal|"{\"statusCode\":\"errorCode\",\"message\":\"message\",\"fields\":[ \"field1\",\"field2\" ]}"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|gotWithStatusCode
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDeserializeFromXml ()
specifier|public
name|void
name|shouldDeserializeFromXml
parameter_list|()
block|{
specifier|final
name|XStream
name|xStream
init|=
name|XStreamUtils
operator|.
name|createXStream
argument_list|(
name|RestError
operator|.
name|class
argument_list|)
decl_stmt|;
name|xStream
operator|.
name|alias
argument_list|(
literal|"errors"
argument_list|,
name|RestError
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|RestError
name|gotWithErrorCode
init|=
operator|(
name|RestError
operator|)
name|xStream
operator|.
name|fromXML
argument_list|(
literal|"<errors><fields>field1</fields><fields>field2</fields><message>message</message><errorCode>errorCode</errorCode></errors>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|gotWithErrorCode
argument_list|,
name|error
argument_list|)
expr_stmt|;
specifier|final
name|RestError
name|gotWithStatusCode
init|=
operator|(
name|RestError
operator|)
name|xStream
operator|.
name|fromXML
argument_list|(
literal|"<errors><fields>field1</fields><fields>field2</fields><message>message</message><statusCode>errorCode</statusCode></errors>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|gotWithStatusCode
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

