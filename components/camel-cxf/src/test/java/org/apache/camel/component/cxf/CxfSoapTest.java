begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|Processor
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
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|XMLUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|io
operator|.
name|CachedOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|StaxUtils
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
DECL|class|CxfSoapTest
specifier|public
class|class
name|CxfSoapTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|SOAP_STRING
specifier|private
specifier|static
specifier|final
name|String
name|SOAP_STRING
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><testMethod xmlns=\"http://camel.apache.org/testService\" />"
operator|+
literal|"</soap:Body></soap:Envelope>"
decl_stmt|;
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|//from("soap:direct:consumer?soap.wsdl=classpath:hello/HelloWorld-DOC.wsdl").to("mock:consumer");
name|from
argument_list|(
literal|"soap:direct:consumer?soap.wsdl=classpath:hello/HelloWorld-DOC.wsdl"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumer"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|e
parameter_list|)
block|{
name|Object
name|result
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:producer"
argument_list|)
operator|.
name|to
argument_list|(
literal|"soap:mock:producer?soap.wsdl=classpath:hello/HelloWorld-DOC.wsdl"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testSoapConsumer ()
specifier|public
name|void
name|testSoapConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// send out the request message
name|URL
name|request
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"SoapRequest.xml"
argument_list|)
decl_stmt|;
name|File
name|requestFile
init|=
operator|new
name|File
argument_list|(
name|request
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|FileInputStream
name|inputStream
init|=
operator|new
name|FileInputStream
argument_list|(
name|requestFile
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:consumer"
argument_list|,
name|inputStream
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"The result should not be changed"
argument_list|,
name|inputStream
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"result should be a inputstream"
argument_list|,
name|result
operator|instanceof
name|InputStream
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
operator|(
name|InputStream
operator|)
name|result
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|bos
operator|.
name|flush
argument_list|()
expr_stmt|;
operator|(
operator|(
name|InputStream
operator|)
name|result
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We should find the soap body string here"
argument_list|,
name|SOAP_STRING
argument_list|,
name|bos
operator|.
name|getOut
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSoapProducer ()
specifier|public
name|void
name|testSoapProducer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// set out the source message
name|URL
name|request
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"RequestBody.xml"
argument_list|)
decl_stmt|;
name|File
name|requestFile
init|=
operator|new
name|File
argument_list|(
name|request
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|FileInputStream
name|inputStream
init|=
operator|new
name|FileInputStream
argument_list|(
name|requestFile
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xmlReader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|StaxUtils
operator|.
name|read
argument_list|(
name|xmlReader
argument_list|)
argument_list|)
decl_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:producer"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:producer"
argument_list|,
name|source
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"The result should not be changed"
argument_list|,
name|source
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The result should be the instance of DOMSource"
argument_list|,
name|result
operator|instanceof
name|DOMSource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The DOMSource should be equal"
argument_list|,
name|XMLUtils
operator|.
name|toString
argument_list|(
name|source
argument_list|)
argument_list|,
name|XMLUtils
operator|.
name|toString
argument_list|(
operator|(
name|Source
operator|)
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

