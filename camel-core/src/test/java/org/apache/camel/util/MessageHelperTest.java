begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|StringReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Message
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
name|StreamCache
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
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link MessageHelper}  */
end_comment

begin_class
DECL|class|MessageHelperTest
specifier|public
class|class
name|MessageHelperTest
extends|extends
name|TestCase
block|{
DECL|field|message
specifier|private
name|Message
name|message
decl_stmt|;
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
name|message
operator|=
operator|new
name|DefaultMessage
argument_list|()
expr_stmt|;
block|}
comment|/*      * Tests the {@link MessageHelper#resetStreamCache(Message)} method      */
DECL|method|testResetStreamCache ()
specifier|public
name|void
name|testResetStreamCache
parameter_list|()
throws|throws
name|Exception
block|{
comment|// should not throw exceptions when Message or message body is null
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// handle StreamCache
specifier|final
name|ValueHolder
argument_list|<
name|Boolean
argument_list|>
name|reset
init|=
operator|new
name|ValueHolder
argument_list|<
name|Boolean
argument_list|>
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
operator|new
name|StreamCache
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|reset
operator|.
name|set
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
comment|// noop
block|}
specifier|public
name|StreamCache
name|copy
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|inMemory
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|length
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have reset the stream cache"
argument_list|,
name|reset
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetContentType ()
specifier|public
name|void
name|testGetContentType
parameter_list|()
throws|throws
name|Exception
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/xml"
argument_list|,
name|MessageHelper
operator|.
name|getContentType
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetContentEncpding ()
specifier|public
name|void
name|testGetContentEncpding
parameter_list|()
throws|throws
name|Exception
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"iso-8859-1"
argument_list|,
name|MessageHelper
operator|.
name|getContentEncoding
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyHeaders ()
specifier|public
name|void
name|testCopyHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|source
init|=
name|message
decl_stmt|;
name|Message
name|target
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|source
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|source
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|target
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|source
argument_list|,
name|target
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|target
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yes"
argument_list|,
name|target
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyHeadersOverride ()
specifier|public
name|void
name|testCopyHeadersOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|source
init|=
name|message
decl_stmt|;
name|Message
name|target
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|source
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|source
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|target
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|source
argument_list|,
name|target
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|target
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|target
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyHeadersWithHeaderFilterStrategy ()
specifier|public
name|void
name|testCopyHeadersWithHeaderFilterStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|message
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|Message
name|source
init|=
name|message
decl_stmt|;
name|Message
name|target
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|DefaultHeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|headerFilterStrategy
operator|.
name|setInFilterPattern
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|source
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|source
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|target
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|source
argument_list|,
name|target
argument_list|,
name|headerFilterStrategy
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|target
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|target
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testDumpAsXmlPlainBody ()
specifier|public
name|void
name|testDumpAsXmlPlainBody
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|message
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
comment|// xml message body
name|message
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should contain body"
argument_list|,
name|out
operator|.
name|contains
argument_list|(
literal|"<body type=\"java.lang.String\">Hello World</body>"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testDumpAsXmlBody ()
specifier|public
name|void
name|testDumpAsXmlBody
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|message
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
comment|// xml message body
name|message
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><hi>Hello World</hi>"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should contain body"
argument_list|,
name|out
operator|.
name|contains
argument_list|(
literal|"<body type=\"java.lang.String\">&lt;?xml version=&quot;1.0&quot;?&gt;&lt;hi&gt;Hello World&lt;/hi&gt;</body>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should contain exchangeId"
argument_list|,
name|out
operator|.
name|contains
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testDumpAsXmlNoBody ()
specifier|public
name|void
name|testDumpAsXmlNoBody
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|message
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
comment|// xml message body
name|message
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|message
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<message exchangeId=\""
operator|+
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|"\">"
operator|+
literal|"\n<headers>\n<header key=\"foo\" type=\"java.lang.Integer\">123</header>\n</headers>\n</message>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testDumpAsXmlNoBodyIndent ()
specifier|public
name|void
name|testDumpAsXmlNoBodyIndent
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|message
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
comment|// xml message body
name|message
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<message exchangeId=\""
operator|+
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|"\">"
operator|+
literal|"\n<headers>\n<header key=\"foo\" type=\"java.lang.Integer\">123</header>\n</headers>\n</message>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testMessageDump ()
specifier|public
name|void
name|testMessageDump
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBContext
name|jaxb
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|MessageDump
operator|.
name|class
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|jaxb
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|message
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
comment|// xml message body
name|message
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|MessageDump
name|dump
init|=
operator|(
name|MessageDump
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|new
name|StringReader
argument_list|(
name|out
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dump
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|dump
operator|.
name|getBody
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|dump
operator|.
name|getBody
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dump
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|dump
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Integer"
argument_list|,
name|dump
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|dump
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

