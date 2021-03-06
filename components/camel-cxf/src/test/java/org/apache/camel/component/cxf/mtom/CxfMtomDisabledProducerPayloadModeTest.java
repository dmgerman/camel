begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

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
name|IOException
import|;
end_import

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
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|ws
operator|.
name|Holder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|attachment
operator|.
name|AttachmentMessage
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
name|cxf
operator|.
name|mtom_feature
operator|.
name|Hello
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
name|attachment
operator|.
name|AttachmentDataSource
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
name|CastUtils
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
name|Assert
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
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_comment
comment|/**  *   * Unit test for exercising SOAP with Attachment (SwA) feature of a CxfProducer in PAYLOAD mode.    * That is, testing attachment with MTOM optimization off.  *    */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CxfMtomDisabledProducerPayloadModeTest
specifier|public
class|class
name|CxfMtomDisabledProducerPayloadModeTest
extends|extends
name|CxfMtomProducerPayloadModeTest
block|{
annotation|@
name|Override
DECL|method|isMtomEnabled ()
specifier|protected
name|boolean
name|isMtomEnabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getServiceImpl ()
specifier|protected
name|Object
name|getServiceImpl
parameter_list|()
block|{
return|return
operator|new
name|MyHelloImpl
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testProducer ()
specifier|public
name|void
name|testProducer
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
name|Exchange
name|exchange
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|send
argument_list|(
literal|"direct:testEndpoint"
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
name|List
argument_list|<
name|Source
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|StringReader
argument_list|(
name|MtomTestHelper
operator|.
name|MTOM_DISABLED_REQ_MESSAGE
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
argument_list|<>
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
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
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
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
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
decl_stmt|;
comment|// process response - verify response attachments
name|CxfPayload
argument_list|<
name|?
argument_list|>
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|getBody
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataHandler
name|dr
init|=
name|exchange
operator|.
name|getOut
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|getAttachment
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_PHOTO_CID
argument_list|)
decl_stmt|;
name|Assert
operator|.
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
name|RESP_PHOTO_DATA
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
name|getOut
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|getAttachment
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_IMAGE_CID
argument_list|)
expr_stmt|;
name|Assert
operator|.
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
name|BufferedImage
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|dr
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|560
argument_list|,
name|image
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|300
argument_list|,
name|image
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyHelloImpl
specifier|public
specifier|static
class|class
name|MyHelloImpl
extends|extends
name|HelloImpl
implements|implements
name|Hello
block|{
annotation|@
name|Resource
DECL|field|ctx
name|WebServiceContext
name|ctx
decl_stmt|;
annotation|@
name|Override
DECL|method|detail (Holder<byte[]> photo, Holder<Image> image)
specifier|public
name|void
name|detail
parameter_list|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
parameter_list|,
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
parameter_list|)
block|{
comment|// verify request attachments
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|ctx
operator|.
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataHandler
name|dh
init|=
name|map
operator|.
name|get
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_PHOTO_CID
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"application/octet-stream"
argument_list|,
name|dh
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bytes
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|dh
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|MtomTestHelper
operator|.
name|assertEquals
argument_list|(
name|bytes
argument_list|,
name|MtomTestHelper
operator|.
name|REQ_PHOTO_DATA
argument_list|)
expr_stmt|;
name|dh
operator|=
name|map
operator|.
name|get
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_IMAGE_CID
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"image/jpeg"
argument_list|,
name|dh
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedImage
name|bufferedImage
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bufferedImage
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
name|dh
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|bufferedImage
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|41
argument_list|,
name|bufferedImage
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|39
argument_list|,
name|bufferedImage
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
comment|// add output attachments
name|map
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|ctx
operator|.
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|OUTBOUND_MESSAGE_ATTACHMENTS
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|DataSource
name|ds
init|=
operator|new
name|AttachmentDataSource
argument_list|(
literal|"image/jpeg"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/Splash.jpg"
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_IMAGE_CID
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|DataSource
name|ds
init|=
operator|new
name|AttachmentDataSource
argument_list|(
literal|"application/octet-stream"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_PHOTO_DATA
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_PHOTO_CID
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

