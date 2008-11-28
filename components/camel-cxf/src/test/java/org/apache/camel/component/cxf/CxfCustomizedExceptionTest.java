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
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
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
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Text
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
name|CamelContext
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
name|ContextTestSupport
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
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
name|Bus
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
name|BusFactory
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|frontend
operator|.
name|ClientFactoryBean
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
name|frontend
operator|.
name|ClientProxyFactoryBean
import|;
end_import

begin_class
DECL|class|CxfCustomizedExceptionTest
specifier|public
class|class
name|CxfCustomizedExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|ROUTER_ADDRESS
specifier|protected
specifier|static
specifier|final
name|String
name|ROUTER_ADDRESS
init|=
literal|"http://localhost:9002/router"
decl_stmt|;
DECL|field|SERVICE_CLASS
specifier|protected
specifier|static
specifier|final
name|String
name|SERVICE_CLASS
init|=
literal|"serviceClass=org.apache.camel.component.cxf.HelloService"
decl_stmt|;
DECL|field|routerEndpointURI
specifier|protected
specifier|static
name|String
name|routerEndpointURI
init|=
literal|"cxf://"
operator|+
name|ROUTER_ADDRESS
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
decl_stmt|;
DECL|field|EXCEPTION_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|EXCEPTION_MESSAGE
init|=
literal|"This is an exception test message"
decl_stmt|;
DECL|field|DETAIL_TEXT
specifier|private
specifier|static
specifier|final
name|String
name|DETAIL_TEXT
init|=
literal|"This is a detail text node"
decl_stmt|;
DECL|field|SOAP_FAULT
specifier|private
specifier|static
specifier|final
name|SoapFault
name|SOAP_FAULT
decl_stmt|;
DECL|field|bus
specifier|private
name|Bus
name|bus
decl_stmt|;
static|static
block|{
comment|// START SNIPPET: FaultDefine
name|SOAP_FAULT
operator|=
operator|new
name|SoapFault
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|SoapFault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
expr_stmt|;
name|Element
name|detail
init|=
name|SOAP_FAULT
operator|.
name|getOrCreateDetail
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|detail
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|Text
name|tn
init|=
name|doc
operator|.
name|createTextNode
argument_list|(
name|DETAIL_TEXT
argument_list|)
decl_stmt|;
name|detail
operator|.
name|appendChild
argument_list|(
name|tn
argument_list|)
expr_stmt|;
comment|// END SNIPPET: FaultDefine
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|//TODO need to shutdown the server
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
comment|//bus.shutdown(true);
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
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
comment|// START SNIPPET: ThrowFault
name|from
argument_list|(
name|routerEndpointURI
argument_list|)
operator|.
name|throwFault
argument_list|(
name|SOAP_FAULT
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ThrowFault
block|}
block|}
return|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
DECL|method|testInvokingServiceFromCXFClient ()
specifier|public
name|void
name|testInvokingServiceFromCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|ClientFactoryBean
name|clientBean
init|=
name|proxyFactory
operator|.
name|getClientFactoryBean
argument_list|()
decl_stmt|;
name|clientBean
operator|.
name|setAddress
argument_list|(
name|ROUTER_ADDRESS
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|HelloService
name|client
init|=
operator|(
name|HelloService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|client
operator|.
name|echo
argument_list|(
literal|"hello world"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect to get an exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Expect to get right exception message"
argument_list|,
name|EXCEPTION_MESSAGE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Exception is not instance of SoapFault"
argument_list|,
name|e
operator|instanceof
name|SoapFault
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expect to get right detail message"
argument_list|,
name|DETAIL_TEXT
argument_list|,
operator|(
operator|(
name|SoapFault
operator|)
name|e
operator|)
operator|.
name|getDetail
argument_list|()
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
comment|//In CXF 2.1.2 , the fault code is per spec , the below fault-code is for SOAP 1.1
name|assertEquals
argument_list|(
literal|"Expect to get right fault-code"
argument_list|,
literal|"{http://schemas.xmlsoap.org/soap/envelope/}Client"
argument_list|,
operator|(
operator|(
name|SoapFault
operator|)
name|e
operator|)
operator|.
name|getFaultCode
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testInvokingServiceFromHTTPURL ()
specifier|public
name|void
name|testInvokingServiceFromHTTPURL
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|ROUTER_ADDRESS
argument_list|)
decl_stmt|;
name|URLConnection
name|urlConnection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|urlConnection
operator|.
name|setDoInput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|urlConnection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|urlConnection
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|urlConnection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
comment|// Send POST data
name|OutputStream
name|out
init|=
name|urlConnection
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
comment|// copy the message out
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"SimpleSoapRequest.xml"
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// check the response code
try|try
block|{
name|urlConnection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"We except an IOException here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"500"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

