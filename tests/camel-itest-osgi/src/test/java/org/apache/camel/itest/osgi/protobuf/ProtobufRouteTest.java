begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.protobuf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
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
name|CamelException
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
name|FailedToCreateRouteException
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
name|dataformat
operator|.
name|protobuf
operator|.
name|ProtobufDataFormat
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationTestSupport
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|JUnit4TestRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|ProtobufRouteTest
specifier|public
class|class
name|ProtobufRouteTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalWithDataFormat ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithDataFormat
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
DECL|method|testMarshalAndUnmarshalWithDSL1 ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithDSL1
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
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalWithDSL2 ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithDSL2
parameter_list|()
throws|throws
name|Exception
block|{
name|marshalAndUnmarshal
argument_list|(
literal|"direct:marshal"
argument_list|,
literal|"direct:unmarshalB"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmashalWithDSL3 ()
specifier|public
name|void
name|testMarshalAndUnmashalWithDSL3
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:unmarshalC"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|protobuf
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"wrong instance"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect the exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Expect FailedToCreateRouteException"
argument_list|,
name|ex
operator|instanceof
name|FailedToCreateRouteException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong reason"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
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
name|AddressBookProtos
operator|.
name|Person
name|input
init|=
name|AddressBookProtos
operator|.
name|Person
operator|.
name|newBuilder
argument_list|()
operator|.
name|setName
argument_list|(
literal|"Martin"
argument_list|)
operator|.
name|setId
argument_list|(
literal|1234
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
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
name|equals
argument_list|(
name|input
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
name|input
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
name|AddressBookProtos
operator|.
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
name|AddressBookProtos
operator|.
name|Person
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Martin"
argument_list|,
name|output
operator|.
name|getName
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
name|AddressBookProtos
operator|.
name|Person
operator|.
name|getDefaultInstance
argument_list|()
argument_list|)
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
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|protobuf
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalA"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|protobuf
argument_list|(
literal|"org.apache.camel.dataformat.protobuf.generated.AddressBookProtos$Person"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalB"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|protobuf
argument_list|(
name|AddressBookProtos
operator|.
name|Person
operator|.
name|getDefaultInstance
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
comment|// using the features to install the other camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-protobuf"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

