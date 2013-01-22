begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.mtom
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
operator|.
name|mtom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
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
name|component
operator|.
name|cxf
operator|.
name|CXFTestSupport
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
name|cxf
operator|.
name|CxfPayload
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
name|jaxp
operator|.
name|XmlConverter
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
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|DOMUtils
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
name|XPathUtils
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
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

begin_comment
comment|/** * Unit test for exercising MTOM feature of a CxfConsumer in PAYLOAD mode *  * @version  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CxfMtomConsumerPayloadModeTest
specifier|public
class|class
name|CxfMtomConsumerPayloadModeTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|port
specifier|static
name|int
name|port
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testConsumer ()
specifier|public
name|void
name|testConsumer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|MtomTestHelper
operator|.
name|isAwtHeadless
argument_list|(
name|logger
argument_list|,
literal|null
argument_list|)
condition|)
block|{
return|return;
block|}
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|send
argument_list|(
literal|"cxf:bean:consumerEndpoint"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong Content-Type header"
argument_list|,
literal|"application/xop+xml"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Source
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<
name|Source
argument_list|>
argument_list|()
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|DOMUtils
operator|.
name|readXml
argument_list|(
operator|new
name|StringReader
argument_list|(
name|getRequestMessage
argument_list|()
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|body
init|=
operator|new
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|SoapHeader
argument_list|>
argument_list|()
argument_list|,
name|elements
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|addAttachment
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_PHOTO_CID
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_PHOTO_DATA
argument_list|,
literal|"application/octet-stream"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|addAttachment
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_IMAGE_CID
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
name|MtomTestHelper
operator|.
name|requestJpeg
argument_list|,
literal|"image/jpeg"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// START SNIPPET: consumer
DECL|class|MyProcessor
specifier|public
specifier|static
class|class
name|MyProcessor
implements|implements
name|Processor
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// verify request
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"ns"
argument_list|,
name|MtomTestHelper
operator|.
name|SERVICE_TYPES_NS
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"xop"
argument_list|,
name|MtomTestHelper
operator|.
name|XOP_NS
argument_list|)
expr_stmt|;
name|XPathUtils
name|xu
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|Element
name|body
init|=
operator|new
name|XmlConverter
argument_list|()
operator|.
name|toDOMElement
argument_list|(
name|in
operator|.
name|getBody
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|Element
name|ele
init|=
operator|(
name|Element
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//ns:Detail/ns:photo/xop:Include"
argument_list|,
name|body
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
name|String
name|photoId
init|=
name|ele
operator|.
name|getAttribute
argument_list|(
literal|"href"
argument_list|)
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
decl_stmt|;
comment|// skip "cid:"
name|assertEquals
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_PHOTO_CID
argument_list|,
name|photoId
argument_list|)
expr_stmt|;
name|ele
operator|=
operator|(
name|Element
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//ns:Detail/ns:image/xop:Include"
argument_list|,
name|body
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
expr_stmt|;
name|String
name|imageId
init|=
name|ele
operator|.
name|getAttribute
argument_list|(
literal|"href"
argument_list|)
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
decl_stmt|;
comment|// skip "cid:"
name|assertEquals
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_IMAGE_CID
argument_list|,
name|imageId
argument_list|)
expr_stmt|;
name|DataHandler
name|dr
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachment
argument_list|(
name|photoId
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"application/octet-stream"
argument_list|,
name|dr
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|MtomTestHelper
operator|.
name|assertEquals
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_PHOTO_DATA
argument_list|,
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|dr
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|dr
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachment
argument_list|(
name|imageId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"image/jpeg"
argument_list|,
name|dr
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|MtomTestHelper
operator|.
name|assertEquals
argument_list|(
name|MtomTestHelper
operator|.
name|requestJpeg
argument_list|,
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|dr
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// create response
name|List
argument_list|<
name|Source
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<
name|Source
argument_list|>
argument_list|()
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|DOMUtils
operator|.
name|readXml
argument_list|(
operator|new
name|StringReader
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_MESSAGE
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|sbody
init|=
operator|new
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|SoapHeader
argument_list|>
argument_list|()
argument_list|,
name|elements
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|sbody
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|addAttachment
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_PHOTO_CID
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_PHOTO_DATA
argument_list|,
literal|"application/octet-stream"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|addAttachment
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_IMAGE_CID
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
name|MtomTestHelper
operator|.
name|responseJpeg
argument_list|,
literal|"image/jpeg"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: consumer
DECL|method|getRequestMessage ()
specifier|protected
name|String
name|getRequestMessage
parameter_list|()
block|{
return|return
name|MtomTestHelper
operator|.
name|REQ_MESSAGE
return|;
block|}
block|}
end_class

end_unit

