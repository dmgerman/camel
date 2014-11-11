begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmljson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xmljson
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|NodeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JsonToXmlAttributesTest
specifier|public
class|class
name|JsonToXmlAttributesTest
extends|extends
name|AbstractJsonTestSupport
block|{
annotation|@
name|Test
DECL|method|shouldCreateAttribute ()
specifier|public
name|void
name|shouldCreateAttribute
parameter_list|()
block|{
comment|// Given
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"jsonToXmlAttributesMessage.json"
argument_list|)
decl_stmt|;
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
comment|// When
name|String
name|xml
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|in
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Then
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|" b=\"2\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCreateOnlyOneAttribute ()
specifier|public
name|void
name|shouldCreateOnlyOneAttribute
parameter_list|()
block|{
comment|// Given
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"jsonToXmlAttributesMessage.json"
argument_list|)
decl_stmt|;
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
comment|// When
name|String
name|xml
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|in
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Then
name|assertFalse
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"a="
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCreateElementWithAttribute ()
specifier|public
name|void
name|shouldCreateElementWithAttribute
parameter_list|()
throws|throws
name|ParserConfigurationException
throws|,
name|IOException
throws|,
name|SAXException
block|{
comment|// Given
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"jsonToXmlElementWithAttributeMessage.json"
argument_list|)
decl_stmt|;
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
comment|// When
name|String
name|xml
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|in
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Then
name|Document
name|document
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|xml
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|NodeList
name|nodeList
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getElementsByTagName
argument_list|(
literal|"element"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nodeList
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|element
init|=
operator|(
name|Element
operator|)
name|nodeList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"elementContent"
argument_list|,
name|element
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attributeValue"
argument_list|,
name|element
operator|.
name|getAttribute
argument_list|(
literal|"attribute"
argument_list|)
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|xmljson
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:xml"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

