begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.common.message
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
name|common
operator|.
name|message
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
name|net
operator|.
name|URL
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|header
operator|.
name|CxfHeaderFilterStrategy
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
name|support
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
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|message
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

begin_class
DECL|class|CxfMessageHelperTest
specifier|public
class|class
name|CxfMessageHelperTest
extends|extends
name|Assert
block|{
DECL|field|REQUEST_STRING
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_STRING
init|=
literal|"<testMethod xmlns=\"http://camel.apache.org/testService\"/>"
decl_stmt|;
DECL|field|context
specifier|private
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// setup the default context for testing
annotation|@
name|Test
DECL|method|testGetCxfInMessage ()
specifier|public
name|void
name|testGetCxfInMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|CxfHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
comment|// String
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"hello world"
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
name|message
init|=
name|CxfMessageHelper
operator|.
name|getCxfInMessage
argument_list|(
name|headerFilterStrategy
argument_list|,
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// test message
name|InputStream
name|is
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The input stream should not be null"
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Don't get the right message"
argument_list|,
name|toString
argument_list|(
name|is
argument_list|)
argument_list|,
literal|"hello world"
argument_list|)
expr_stmt|;
comment|// DOMSource
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|message
operator|=
name|CxfMessageHelper
operator|.
name|getCxfInMessage
argument_list|(
name|headerFilterStrategy
argument_list|,
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|is
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The input stream should not be null"
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Don't get the right message"
argument_list|,
name|toString
argument_list|(
name|is
argument_list|)
argument_list|,
name|REQUEST_STRING
argument_list|)
expr_stmt|;
comment|// File
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|requestFile
argument_list|)
expr_stmt|;
name|message
operator|=
name|CxfMessageHelper
operator|.
name|getCxfInMessage
argument_list|(
name|headerFilterStrategy
argument_list|,
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|is
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The input stream should not be null"
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Don't get the right message"
argument_list|,
name|toString
argument_list|(
name|is
argument_list|)
argument_list|,
name|REQUEST_STRING
argument_list|)
expr_stmt|;
comment|// transport header's case insensitiveness
comment|// String
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"hello world"
argument_list|)
expr_stmt|;
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
name|message
operator|=
name|CxfMessageHelper
operator|.
name|getCxfInMessage
argument_list|(
name|headerFilterStrategy
argument_list|,
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// test message
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
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
comment|// verify there is no duplicate
name|assertNotNull
argument_list|(
literal|"The headers must be present"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"There must be one header entry"
argument_list|,
name|headers
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
comment|// verify the soapaction can be retrieved in case-insensitive ways
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
DECL|method|toString (InputStream is)
specifier|private
name|String
name|toString
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|out
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|CachedOutputStream
name|os
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|writeCacheTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|toString
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"(?s)<\\?.*\\?>"
argument_list|,
literal|""
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"(?s)<!--.*-->"
argument_list|,
literal|""
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"(\\r)?\\n"
argument_list|,
literal|""
argument_list|)
return|;
block|}
block|}
end_class

end_unit

