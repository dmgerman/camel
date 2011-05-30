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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|FileDataSource
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|AttachmentImpl
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
name|Binding
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
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|Endpoint
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
name|message
operator|.
name|Attachment
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
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|DefaultCxfBindingTest
specifier|public
class|class
name|DefaultCxfBindingTest
extends|extends
name|Assert
block|{
DECL|field|context
specifier|private
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testSetGetHeaderFilterStrategy ()
specifier|public
name|void
name|testSetGetHeaderFilterStrategy
parameter_list|()
block|{
name|DefaultCxfBinding
name|cxfBinding
init|=
operator|new
name|DefaultCxfBinding
argument_list|()
decl_stmt|;
name|HeaderFilterStrategy
name|hfs
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|cxfBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|hfs
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"The header filter strategy is set"
argument_list|,
name|hfs
argument_list|,
name|cxfBinding
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPopulateCxfRequestFromExchange ()
specifier|public
name|void
name|testPopulateCxfRequestFromExchange
parameter_list|()
block|{
name|DefaultCxfBinding
name|cxfBinding
init|=
operator|new
name|DefaultCxfBinding
argument_list|()
decl_stmt|;
name|cxfBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|CXF_EXCHANGE
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|DATA_FORMAT_PROPERTY
argument_list|,
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"soapAction"
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"MyFruitHeader"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|addAttachment
argument_list|(
literal|"att-1"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"pom.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cxfBinding
operator|.
name|populateCxfRequestFromExchange
argument_list|(
name|cxfExchange
argument_list|,
name|exchange
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
comment|// check the protocol headers
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|requestContext
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|headers
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"soapaction"
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"SoapAction"
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"SOAPAction"
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"myfruitheader"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"myFruitHeader"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"MYFRUITHEADER"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Set
operator|)
name|requestContext
operator|.
name|get
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_ATTACHMENTS
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|attachments
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Attachment
name|att
init|=
name|attachments
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"att-1"
argument_list|,
name|att
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPopupalteExchangeFromCxfResponse ()
specifier|public
name|void
name|testPopupalteExchangeFromCxfResponse
parameter_list|()
block|{
name|DefaultCxfBinding
name|cxfBinding
init|=
operator|new
name|DefaultCxfBinding
argument_list|()
decl_stmt|;
name|cxfBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|CXF_EXCHANGE
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|DATA_FORMAT_PROPERTY
argument_list|,
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|responseContext
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"content-type"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"text/xml;charset=UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Length"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"241"
argument_list|)
argument_list|)
expr_stmt|;
name|responseContext
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
argument_list|()
decl_stmt|;
name|cxfExchange
operator|.
name|setInMessage
argument_list|(
name|cxfMessage
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
operator|new
name|HashSet
argument_list|<
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
name|attachments
operator|.
name|add
argument_list|(
operator|new
name|AttachmentImpl
argument_list|(
literal|"att-1"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"pom.xml"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cxfMessage
operator|.
name|setAttachments
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
name|cxfBinding
operator|.
name|populateExchangeFromCxfResponse
argument_list|(
name|exchange
argument_list|,
name|cxfExchange
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|camelHeaders
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelHeaders
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|responseContext
argument_list|,
name|camelHeaders
operator|.
name|get
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|camelAttachments
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelAttachments
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|camelAttachments
operator|.
name|get
argument_list|(
literal|"att-1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPopupalteCxfResponseFromExchange ()
specifier|public
name|void
name|testPopupalteCxfResponseFromExchange
parameter_list|()
block|{
name|DefaultCxfBinding
name|cxfBinding
init|=
operator|new
name|DefaultCxfBinding
argument_list|()
decl_stmt|;
name|cxfBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|DATA_FORMAT_PROPERTY
argument_list|,
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"soapAction"
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"MyFruitHeader"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|addAttachment
argument_list|(
literal|"att-1"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"pom.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Binding
name|binding
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Binding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|binding
operator|.
name|createMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|cxfMessage
argument_list|)
expr_stmt|;
name|cxfExchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|cxfBinding
operator|.
name|populateCxfResponseFromExchange
argument_list|(
name|exchange
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
name|cxfMessage
operator|=
name|cxfExchange
operator|.
name|getOutMessage
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|cxfMessage
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|cxfMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|headers
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"soapaction"
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"SoapAction"
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"SOAPAction"
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"myfruitheader"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"myFruitHeader"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|headers
argument_list|,
literal|"MYFRUITHEADER"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
name|cxfMessage
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|attachments
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Attachment
name|att
init|=
name|attachments
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"att-1"
argument_list|,
name|att
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPopupalteExchangeFromCxfRequest ()
specifier|public
name|void
name|testPopupalteExchangeFromCxfRequest
parameter_list|()
block|{
name|DefaultCxfBinding
name|cxfBinding
init|=
operator|new
name|DefaultCxfBinding
argument_list|()
decl_stmt|;
name|cxfBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|DATA_FORMAT_PROPERTY
argument_list|,
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"content-type"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"text/xml;charset=UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Length"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"241"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"soapAction"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"urn:hello:world"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"myfruitheader"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"peach"
argument_list|)
argument_list|)
expr_stmt|;
name|cxfMessage
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
operator|new
name|HashSet
argument_list|<
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
name|attachments
operator|.
name|add
argument_list|(
operator|new
name|AttachmentImpl
argument_list|(
literal|"att-1"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"pom.xml"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cxfMessage
operator|.
name|setAttachments
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
name|cxfExchange
operator|.
name|setInMessage
argument_list|(
name|cxfMessage
argument_list|)
expr_stmt|;
name|cxfBinding
operator|.
name|populateExchangeFromCxfRequest
argument_list|(
name|cxfExchange
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|camelHeaders
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelHeaders
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|camelHeaders
operator|.
name|get
argument_list|(
literal|"soapaction"
argument_list|)
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|camelHeaders
operator|.
name|get
argument_list|(
literal|"SoapAction"
argument_list|)
argument_list|,
literal|"urn:hello:world"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|camelHeaders
operator|.
name|get
argument_list|(
literal|"content-type"
argument_list|)
argument_list|,
literal|"text/xml;charset=UTF-8"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|camelHeaders
operator|.
name|get
argument_list|(
literal|"content-length"
argument_list|)
argument_list|,
literal|"241"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|camelHeaders
operator|.
name|get
argument_list|(
literal|"MyFruitHeader"
argument_list|)
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|camelAttachments
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelAttachments
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|camelAttachments
operator|.
name|get
argument_list|(
literal|"att-1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyHeader (Map<String, List<String>> headers, String name, String value)
specifier|private
name|void
name|verifyHeader
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The entry must be available"
argument_list|,
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The value must match"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

