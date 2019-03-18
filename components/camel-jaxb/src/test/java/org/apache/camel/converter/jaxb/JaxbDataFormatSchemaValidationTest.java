begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|CamelExecutionException
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
name|converter
operator|.
name|jaxb
operator|.
name|address
operator|.
name|Address
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
name|converter
operator|.
name|jaxb
operator|.
name|person
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
DECL|class|JaxbDataFormatSchemaValidationTest
specifier|public
class|class
name|JaxbDataFormatSchemaValidationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:marshall"
argument_list|)
DECL|field|mockMarshall
specifier|private
name|MockEndpoint
name|mockMarshall
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:unmarshall"
argument_list|)
DECL|field|mockUnmarshall
specifier|private
name|MockEndpoint
name|mockUnmarshall
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshallSuccess ()
specifier|public
name|void
name|testMarshallSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|mockMarshall
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Address
name|address
init|=
operator|new
name|Address
argument_list|()
decl_stmt|;
name|address
operator|.
name|setAddressLine1
argument_list|(
literal|"Hauptstr. 1; 01129 Entenhausen"
argument_list|)
expr_stmt|;
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
name|person
operator|.
name|setFirstName
argument_list|(
literal|"Christian"
argument_list|)
expr_stmt|;
name|person
operator|.
name|setLastName
argument_list|(
literal|"Mueller"
argument_list|)
expr_stmt|;
name|person
operator|.
name|setAge
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|36
argument_list|)
argument_list|)
expr_stmt|;
name|person
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshall"
argument_list|,
name|person
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|payload
init|=
name|mockMarshall
operator|.
name|getExchanges
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
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|startsWith
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<person xmlns=\"person.jaxb.converter.camel.apache.org\" xmlns:ns2=\"address.jaxb.converter.camel.apache.org\">"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<firstName>Christian</firstName>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<lastName>Mueller</lastName>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<age>36</age>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<address>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<ns2:addressLine1>Hauptstr. 1; 01129 Entenhausen</ns2:addressLine1>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"</address>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"</person>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshallWithValidationException ()
specifier|public
name|void
name|testMarshallWithValidationException
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshall"
argument_list|,
operator|new
name|Person
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"CamelExecutionException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|IOException
operator|.
name|class
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"javax.xml.bind.MarshalException"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.xml.sax.SAXParseException"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"cvc-complex-type.2.4.a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testUnmarshallSuccess ()
specifier|public
name|void
name|testUnmarshallSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|mockUnmarshall
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"<person xmlns=\"person.jaxb.converter.camel.apache.org\" xmlns:ns2=\"address.jaxb.converter.camel.apache.org\">"
argument_list|)
operator|.
name|append
argument_list|(
literal|"<firstName>Christian</firstName>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"<lastName>Mueller</lastName>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"<age>36</age>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"<address>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"<ns2:addressLine1>Hauptstr. 1; 01129 Entenhausen</ns2:addressLine1>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"</address>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"</person>"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshall"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Person
name|person
init|=
name|mockUnmarshall
operator|.
name|getExchanges
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
literal|"Christian"
argument_list|,
name|person
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Mueller"
argument_list|,
name|person
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|36
argument_list|)
argument_list|,
name|person
operator|.
name|getAge
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshallWithValidationException ()
specifier|public
name|void
name|testUnmarshallWithValidationException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"<person xmlns=\"person.jaxb.converter.camel.apache.org\" />"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshall"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"CamelExecutionException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|IOException
operator|.
name|class
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"javax.xml.bind.UnmarshalException"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.xml.sax.SAXParseException"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"cvc-complex-type.2.4.b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|JaxbDataFormat
name|jaxbDataFormat
init|=
operator|new
name|JaxbDataFormat
argument_list|()
decl_stmt|;
name|jaxbDataFormat
operator|.
name|setContextPath
argument_list|(
name|Person
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jaxbDataFormat
operator|.
name|setSchema
argument_list|(
literal|"classpath:person.xsd,classpath:address.xsd"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshall"
argument_list|)
operator|.
name|marshal
argument_list|(
name|jaxbDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:marshall"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshall"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|jaxbDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unmarshall"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

