begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.tagsoup
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|tagsoup
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_class
DECL|class|TidyMarkupTestSupport
specifier|public
specifier|final
class|class
name|TidyMarkupTestSupport
block|{
DECL|method|TidyMarkupTestSupport ()
specifier|private
name|TidyMarkupTestSupport
parameter_list|()
block|{
comment|// Utility class
block|}
DECL|method|loadFileAsString (File file)
specifier|public
specifier|static
name|String
name|loadFileAsString
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|StringBuilder
name|fileContent
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|BufferedReader
name|input
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|input
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|fileContent
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|fileContent
operator|.
name|append
argument_list|(
name|System
operator|.
name|lineSeparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|fileContent
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Convert XML String to a Document.      *       * @param xmlString      * @return document Document      * @throws IOException      * @throws SAXException      * @throws ParserConfigurationException      */
DECL|method|stringToXml (String xmlString)
specifier|public
specifier|static
name|Document
name|stringToXml
parameter_list|(
name|String
name|xmlString
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
return|return
name|createDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xmlString
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Static to generate a documentBuilder      *       * @return      * @throws ParserConfigurationException      */
DECL|method|createDocumentBuilder ()
specifier|public
specifier|static
name|DocumentBuilder
name|createDocumentBuilder
parameter_list|()
throws|throws
name|ParserConfigurationException
block|{
name|DocumentBuilderFactory
name|docBuilderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|docBuilderFactory
operator|.
name|setIgnoringComments
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|docBuilderFactory
operator|.
name|setIgnoringElementContentWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|docBuilderFactory
operator|.
name|setCoalescing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|docBuilderFactory
operator|.
name|setExpandEntityReferences
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|docBuilderFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|docBuilderFactory
operator|.
name|newDocumentBuilder
argument_list|()
return|;
block|}
block|}
end_class

end_unit

