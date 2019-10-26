begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.protobuf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|protobuf
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
name|dataformat
operator|.
name|protobuf
operator|.
name|generated
operator|.
name|AddressBookProtos
operator|.
name|Person
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
DECL|class|ProtobufMarshalAndUnmarshalJsonTest
specifier|public
class|class
name|ProtobufMarshalAndUnmarshalJsonTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|PERSON_TEST_NAME
specifier|private
specifier|static
specifier|final
name|String
name|PERSON_TEST_NAME
init|=
literal|"Martin"
decl_stmt|;
DECL|field|PERSON_TEST_JSON
specifier|private
specifier|static
specifier|final
name|String
name|PERSON_TEST_JSON
init|=
literal|"{\"name\": \"Martin\",\"id\": 1234}"
decl_stmt|;
DECL|field|PERSON_TEST_ID
specifier|private
specifier|static
specifier|final
name|int
name|PERSON_TEST_ID
init|=
literal|1234
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshal ()
specifier|public
name|void
name|testMarshalAndUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|marshalAndUnmarshal
argument_list|(
literal|"direct:in"
argument_list|,
literal|"direct:back"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalWithDSL ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithDSL
parameter_list|()
throws|throws
name|Exception
block|{
name|marshalAndUnmarshal
argument_list|(
literal|"direct:marshal"
argument_list|,
literal|"direct:unmarshalA"
argument_list|)
expr_stmt|;
block|}
DECL|method|marshalAndUnmarshal (String inURI, String outURI)
specifier|private
name|void
name|marshalAndUnmarshal
parameter_list|(
name|String
name|inURI
parameter_list|,
name|String
name|outURI
parameter_list|)
throws|throws
name|Exception
block|{
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
name|Person
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|inURI
argument_list|,
name|PERSON_TEST_JSON
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|outURI
argument_list|,
name|marshalled
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Person
name|output
init|=
name|mock
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
name|Person
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PERSON_TEST_NAME
argument_list|,
name|output
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PERSON_TEST_ID
argument_list|,
name|output
operator|.
name|getId
argument_list|()
argument_list|)
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
name|ProtobufDataFormat
name|format
init|=
operator|new
name|ProtobufDataFormat
argument_list|(
name|Person
operator|.
name|getDefaultInstance
argument_list|()
argument_list|,
name|ProtobufDataFormat
operator|.
name|CONTENT_TYPE_FORMAT_JSON
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:in"
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
name|from
argument_list|(
literal|"direct:back"
argument_list|)
operator|.
name|marshal
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|protobuf
argument_list|(
literal|"org.apache.camel.dataformat.protobuf.generated.AddressBookProtos$Person"
argument_list|,
literal|"json"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalA"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|protobuf
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

