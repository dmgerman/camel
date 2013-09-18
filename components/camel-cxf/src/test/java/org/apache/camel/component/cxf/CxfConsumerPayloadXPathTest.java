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
name|ExchangePattern
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
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
name|DefaultExchange
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
name|AvailablePortFinder
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|SoapHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|ComparisonFailure
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
DECL|class|CxfConsumerPayloadXPathTest
specifier|public
class|class
name|CxfConsumerPayloadXPathTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|HEADER_SIZE
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_SIZE
init|=
literal|"tstsize"
decl_stmt|;
annotation|@
name|Test
DECL|method|size1XPathStringResultTest ()
specifier|public
name|void
name|size1XPathStringResultTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|1
argument_list|,
operator|new
name|TestRouteWithXPathStringResultBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|size100XPathStringResultTest ()
specifier|public
name|void
name|size100XPathStringResultTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|100
argument_list|,
operator|new
name|TestRouteWithXPathStringResultBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|size1000XPathStringResultTest ()
specifier|public
name|void
name|size1000XPathStringResultTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|1000
argument_list|,
operator|new
name|TestRouteWithXPathStringResultBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|size10000XPathStringResultTest ()
specifier|public
name|void
name|size10000XPathStringResultTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|10000
argument_list|,
operator|new
name|TestRouteWithXPathStringResultBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|size1XPathTest ()
specifier|public
name|void
name|size1XPathTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|1
argument_list|,
operator|new
name|TestRouteWithXPathBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|size100XPathTest ()
specifier|public
name|void
name|size100XPathTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|100
argument_list|,
operator|new
name|TestRouteWithXPathBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|size1000XPathTest ()
specifier|public
name|void
name|size1000XPathTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|1000
argument_list|,
operator|new
name|TestRouteWithXPathBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|size10000XPathTest ()
specifier|public
name|void
name|size10000XPathTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|10000
argument_list|,
operator|new
name|TestRouteWithXPathBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//the textnode appears to have siblings!
annotation|@
name|Test
DECL|method|size10000DomTest ()
specifier|public
name|void
name|size10000DomTest
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleTest
argument_list|(
literal|10000
argument_list|,
operator|new
name|TestRouteWithDomBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|TestRouteWithXPathBuilder
specifier|private
class|class
name|TestRouteWithXPathBuilder
extends|extends
name|BaseRouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"cxf://"
operator|+
name|testAddress
operator|+
literal|"?dataFormat=PAYLOAD"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|XPathProcessor
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ResponseProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TestRouteWithXPathStringResultBuilder
specifier|private
class|class
name|TestRouteWithXPathStringResultBuilder
extends|extends
name|BaseRouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"cxf://"
operator|+
name|testAddress
operator|+
literal|"?dataFormat=PAYLOAD"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|XPathStringResultProcessor
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ResponseProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TestRouteWithDomFirstOneOnlyBuilder
specifier|private
class|class
name|TestRouteWithDomFirstOneOnlyBuilder
extends|extends
name|BaseRouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"cxf://"
operator|+
name|testAddress
operator|+
literal|"?dataFormat=PAYLOAD"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|DomFirstOneOnlyProcessor
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ResponseProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TestRouteWithDomBuilder
specifier|private
class|class
name|TestRouteWithDomBuilder
extends|extends
name|BaseRouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"cxf://"
operator|+
name|testAddress
operator|+
literal|"?dataFormat=PAYLOAD"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|DomProcessor
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ResponseProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|//implementation simular to xpath() in route: no data loss
DECL|class|XPathStringResultProcessor
specifier|private
class|class
name|XPathStringResultProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|//xpath expression directly results in a: String
name|String
name|content
init|=
operator|(
name|String
operator|)
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"//xml/text()"
argument_list|)
operator|.
name|stringResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
name|obj
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|//this version leads to data loss
DECL|class|XPathProcessor
specifier|private
class|class
name|XPathProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|//xpath expression results in a: net.sf.saxon.dom.DOMNodeList
comment|//after which it is converted to a String
name|String
name|content
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"//xml/text()"
argument_list|)
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
name|obj
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|//this version leads to data loss
DECL|class|DomFirstOneOnlyProcessor
specifier|private
class|class
name|DomFirstOneOnlyProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|payload
init|=
operator|(
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
operator|)
name|obj
decl_stmt|;
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|payload
operator|.
name|getBody
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Text
name|textnode
init|=
operator|(
name|Text
operator|)
name|el
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|textnode
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|DomProcessor
specifier|private
class|class
name|DomProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|payload
init|=
operator|(
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
operator|)
name|obj
decl_stmt|;
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|payload
operator|.
name|getBody
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Text
name|textnode
init|=
operator|(
name|Text
operator|)
name|el
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|textnode
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|textnode
operator|=
operator|(
name|Text
operator|)
name|textnode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
while|while
condition|(
name|textnode
operator|!=
literal|null
condition|)
block|{
comment|//the textnode appears to have siblings!
name|b
operator|.
name|append
argument_list|(
name|textnode
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|textnode
operator|=
operator|(
name|Text
operator|)
name|textnode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ResponseProcessor
specifier|private
class|class
name|ResponseProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|String
name|content
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
name|String
name|msgOut
init|=
name|constructSoapMessage
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|msgOut
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_SIZE
argument_list|,
literal|""
operator|+
name|content
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|simpleTest (int repeat, BaseRouteBuilder builder)
specifier|private
name|void
name|simpleTest
parameter_list|(
name|int
name|repeat
parameter_list|,
name|BaseRouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
name|setUseRouteBuilder
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|startCamelContext
argument_list|()
expr_stmt|;
name|String
name|content
init|=
name|StringUtils
operator|.
name|repeat
argument_list|(
literal|"x"
argument_list|,
name|repeat
argument_list|)
decl_stmt|;
name|String
name|msgIn
init|=
name|constructSoapMessage
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|Exchange
name|exchgIn
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchgIn
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|exchgIn
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|msgIn
argument_list|)
expr_stmt|;
comment|//Execute
name|Exchange
name|exchgOut
init|=
name|template
operator|.
name|send
argument_list|(
name|builder
operator|.
name|getTestAddress
argument_list|()
argument_list|,
name|exchgIn
argument_list|)
decl_stmt|;
comment|//Verify
name|String
name|result
init|=
name|exchgOut
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response on http call"
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|//check for data loss in received input (after xpath)
name|String
name|headerSize
init|=
name|exchgOut
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HEADER_SIZE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
operator|+
name|repeat
argument_list|,
name|headerSize
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"dataloss in output occurred"
argument_list|,
name|result
operator|.
name|length
argument_list|()
operator|>
name|repeat
argument_list|)
expr_stmt|;
name|stopCamelContext
argument_list|()
expr_stmt|;
block|}
DECL|class|BaseRouteBuilder
specifier|private
specifier|abstract
class|class
name|BaseRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|testAddress
specifier|protected
specifier|final
name|String
name|testAddress
init|=
name|getAvailableUrl
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
DECL|method|getTestAddress ()
specifier|public
name|String
name|getTestAddress
parameter_list|()
block|{
return|return
name|testAddress
return|;
block|}
block|}
DECL|method|getAvailableUrl (String pathEnd)
specifier|private
name|String
name|getAvailableUrl
parameter_list|(
name|String
name|pathEnd
parameter_list|)
block|{
name|int
name|availablePort
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|String
name|url
init|=
literal|"http://localhost:"
operator|+
name|availablePort
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
return|return
name|url
operator|+
literal|"/"
operator|+
name|pathEnd
return|;
block|}
DECL|method|constructSoapMessage (String content)
specifier|private
name|String
name|constructSoapMessage
parameter_list|(
name|String
name|content
parameter_list|)
block|{
return|return
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
literal|"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soapenv:Body><xml>"
operator|+
name|content
operator|+
literal|"</xml></soapenv:Body>"
operator|+
literal|"</soapenv:Envelope>"
return|;
block|}
block|}
end_class

end_unit

