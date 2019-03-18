begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.soap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|soap
operator|.
name|name
operator|.
name|TypeNameStrategy
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
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|SoapToSoapSingleDataFormatterTest
specifier|public
class|class
name|SoapToSoapSingleDataFormatterTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|soapjaxbModel
specifier|private
specifier|static
name|SoapJaxbDataFormat
name|soapjaxbModel
decl_stmt|;
DECL|field|namespacePrefixMap
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePrefixMap
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setup ()
specifier|public
specifier|static
name|void
name|setup
parameter_list|()
block|{
name|namespacePrefixMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|namespacePrefixMap
operator|.
name|put
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/envelope/"
argument_list|,
literal|"soap"
argument_list|)
expr_stmt|;
name|namespacePrefixMap
operator|.
name|put
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|,
literal|"xsd"
argument_list|)
expr_stmt|;
name|namespacePrefixMap
operator|.
name|put
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema-instance"
argument_list|,
literal|"xsi"
argument_list|)
expr_stmt|;
name|namespacePrefixMap
operator|.
name|put
argument_list|(
literal|"http://www.example.com/contact"
argument_list|,
literal|"cont"
argument_list|)
expr_stmt|;
name|namespacePrefixMap
operator|.
name|put
argument_list|(
literal|"http://www.example.com/soapheaders"
argument_list|,
literal|"custom"
argument_list|)
expr_stmt|;
name|soapjaxbModel
operator|=
operator|new
name|SoapJaxbDataFormat
argument_list|(
literal|"com.example.contact:com.example.soapheaders"
argument_list|)
expr_stmt|;
name|soapjaxbModel
operator|.
name|setNamespacePrefix
argument_list|(
name|namespacePrefixMap
argument_list|)
expr_stmt|;
name|soapjaxbModel
operator|.
name|setPrettyPrint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|soapjaxbModel
operator|.
name|setIgnoreUnmarshalledHeaders
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|soapjaxbModel
operator|.
name|setIgnoreJAXBElement
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|soapjaxbModel
operator|.
name|setElementNameStrategy
argument_list|(
operator|new
name|TypeNameStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|teardown ()
specifier|public
specifier|static
name|void
name|teardown
parameter_list|()
block|{
name|soapjaxbModel
operator|=
literal|null
expr_stmt|;
name|namespacePrefixMap
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSoapMarshal ()
specifier|public
name|void
name|testSoapMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|createRequest
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|result
init|=
name|endpoint
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|byte
index|[]
name|body
init|=
operator|(
name|byte
index|[]
operator|)
name|result
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|SOAPMessage
name|request
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|stream
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Expected headers"
argument_list|,
literal|null
operator|!=
name|request
operator|.
name|getSOAPHeader
argument_list|()
operator|&&
name|request
operator|.
name|getSOAPHeader
argument_list|()
operator|.
name|extractAllHeaderElements
argument_list|()
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRequest ()
specifier|private
name|InputStream
name|createRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|stream
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"SoapMarshalHeadersTest.xml"
argument_list|)
decl_stmt|;
return|return
name|stream
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
name|context
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|LOG_DEBUG_BODY_MAX_CHARS
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|soapjaxbModel
argument_list|)
operator|.
name|marshal
argument_list|(
name|soapjaxbModel
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

