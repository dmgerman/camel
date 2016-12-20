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
name|InputStream
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
name|Stack
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
name|SAXParser
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
name|SAXParserFactory
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
name|Node
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
name|Attributes
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
name|Locator
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
name|xml
operator|.
name|sax
operator|.
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_comment
comment|/**  * An XML parser that uses SAX to include line and column number for each XML element in the parsed Document.  *<p>  * The line number and column number can be obtained from a Node/Element using  *<pre>  *   String lineNumber = (String) node.getUserData(XmlLineNumberParser.LINE_NUMBER);  *   String lineNumberEnd = (String) node.getUserData(XmlLineNumberParser.LINE_NUMBER_END);  *   String columnNumber = (String) node.getUserData(XmlLineNumberParser.COLUMN_NUMBER);  *   String columnNumberEnd = (String) node.getUserData(XmlLineNumberParser.COLUMN_NUMBER_END);  *</pre>  */
end_comment

begin_class
DECL|class|XmlLineNumberParser
specifier|public
specifier|final
class|class
name|XmlLineNumberParser
block|{
DECL|field|LINE_NUMBER
specifier|public
specifier|static
specifier|final
name|String
name|LINE_NUMBER
init|=
literal|"lineNumber"
decl_stmt|;
DECL|field|COLUMN_NUMBER
specifier|public
specifier|static
specifier|final
name|String
name|COLUMN_NUMBER
init|=
literal|"colNumber"
decl_stmt|;
DECL|field|LINE_NUMBER_END
specifier|public
specifier|static
specifier|final
name|String
name|LINE_NUMBER_END
init|=
literal|"lineNumberEnd"
decl_stmt|;
DECL|field|COLUMN_NUMBER_END
specifier|public
specifier|static
specifier|final
name|String
name|COLUMN_NUMBER_END
init|=
literal|"colNumberEnd"
decl_stmt|;
comment|/**      * Allows to plugin a custom text transformer in the parser, that can transform all the text content      */
DECL|interface|XmlTextTransformer
specifier|public
interface|interface
name|XmlTextTransformer
block|{
DECL|method|transform (String text)
name|String
name|transform
parameter_list|(
name|String
name|text
parameter_list|)
function_decl|;
block|}
DECL|method|XmlLineNumberParser ()
specifier|private
name|XmlLineNumberParser
parameter_list|()
block|{     }
comment|/**      * Parses the XML.      *      * @param is the XML content as an input stream      * @return the DOM model      * @throws Exception is thrown if error parsing      */
DECL|method|parseXml (final InputStream is)
specifier|public
specifier|static
name|Document
name|parseXml
parameter_list|(
specifier|final
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|parseXml
argument_list|(
name|is
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Parses the XML.      *      * @param is             the XML content as an input stream      * @param xmlTransformer the XML transformer      * @return the DOM model      * @throws Exception is thrown if error parsing      */
DECL|method|parseXml (final InputStream is, final XmlTextTransformer xmlTransformer)
specifier|public
specifier|static
name|Document
name|parseXml
parameter_list|(
specifier|final
name|InputStream
name|is
parameter_list|,
specifier|final
name|XmlTextTransformer
name|xmlTransformer
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|parseXml
argument_list|(
name|is
argument_list|,
name|xmlTransformer
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Parses the XML.      *      * @param is              the XML content as an input stream      * @param xmlTransformer  the XML transformer      * @param rootNames       one or more root names that is used as baseline for beginning the parsing, for example camelContext to start parsing      *                        when Camel is discovered. Multiple names can be defined separated by comma      * @param forceNamespace  an optional namespaces to force assign to each node. This may be needed for JAXB unmarshalling from XML -> POJO.      * @return the DOM model      * @throws Exception is thrown if error parsing      */
DECL|method|parseXml (final InputStream is, XmlTextTransformer xmlTransformer, String rootNames, final String forceNamespace)
specifier|public
specifier|static
name|Document
name|parseXml
parameter_list|(
specifier|final
name|InputStream
name|is
parameter_list|,
name|XmlTextTransformer
name|xmlTransformer
parameter_list|,
name|String
name|rootNames
parameter_list|,
specifier|final
name|String
name|forceNamespace
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|is
argument_list|,
literal|"is"
argument_list|)
expr_stmt|;
specifier|final
name|XmlTextTransformer
name|transformer
init|=
name|xmlTransformer
operator|==
literal|null
condition|?
operator|new
name|NoopTransformer
argument_list|()
else|:
name|xmlTransformer
decl_stmt|;
specifier|final
name|Document
name|doc
decl_stmt|;
name|SAXParser
name|parser
decl_stmt|;
specifier|final
name|SAXParserFactory
name|factory
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|parser
operator|=
name|factory
operator|.
name|newSAXParser
argument_list|()
expr_stmt|;
specifier|final
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
comment|// turn off validator and loading external dtd
name|dbf
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/namespaces"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/validation"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://apache.org/xml/features/nonvalidating/load-dtd-grammar"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://apache.org/xml/features/nonvalidating/load-external-dtd"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/external-parameter-entities"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/external-general-entities"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|DocumentBuilder
name|docBuilder
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|doc
operator|=
name|docBuilder
operator|.
name|newDocument
argument_list|()
expr_stmt|;
specifier|final
name|Stack
argument_list|<
name|Element
argument_list|>
name|elementStack
init|=
operator|new
name|Stack
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|StringBuilder
name|textBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|DefaultHandler
name|handler
init|=
operator|new
name|DefaultHandler
argument_list|()
block|{
specifier|private
name|Locator
name|locator
decl_stmt|;
specifier|private
name|boolean
name|found
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|setDocumentLocator
parameter_list|(
specifier|final
name|Locator
name|locator
parameter_list|)
block|{
name|this
operator|.
name|locator
operator|=
name|locator
expr_stmt|;
comment|// Save the locator, so that it can be used later for line tracking when traversing nodes.
name|this
operator|.
name|found
operator|=
name|rootNames
operator|==
literal|null
expr_stmt|;
block|}
specifier|private
name|boolean
name|isRootName
parameter_list|(
name|String
name|qName
parameter_list|)
block|{
for|for
control|(
name|String
name|root
range|:
name|rootNames
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
if|if
condition|(
name|qName
operator|.
name|equals
argument_list|(
name|root
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|startElement
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|localName
parameter_list|,
specifier|final
name|String
name|qName
parameter_list|,
specifier|final
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
name|addTextIfNeeded
argument_list|()
expr_stmt|;
if|if
condition|(
name|rootNames
operator|!=
literal|null
operator|&&
operator|!
name|found
condition|)
block|{
if|if
condition|(
name|isRootName
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|found
condition|)
block|{
name|Element
name|el
decl_stmt|;
if|if
condition|(
name|forceNamespace
operator|!=
literal|null
condition|)
block|{
name|el
operator|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|forceNamespace
argument_list|,
name|qName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|el
operator|=
name|doc
operator|.
name|createElement
argument_list|(
name|qName
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|el
operator|.
name|setAttribute
argument_list|(
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|el
operator|.
name|setUserData
argument_list|(
name|LINE_NUMBER
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|locator
operator|.
name|getLineNumber
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|el
operator|.
name|setUserData
argument_list|(
name|COLUMN_NUMBER
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|locator
operator|.
name|getColumnNumber
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|elementStack
operator|.
name|push
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|endElement
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|localName
parameter_list|,
specifier|final
name|String
name|qName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|found
condition|)
block|{
return|return;
block|}
name|addTextIfNeeded
argument_list|()
expr_stmt|;
specifier|final
name|Element
name|closedEl
init|=
name|elementStack
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|elementStack
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
name|closedEl
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|elementStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Is this the root element?
name|doc
operator|.
name|appendChild
argument_list|(
name|closedEl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Element
name|parentEl
init|=
name|elementStack
operator|.
name|peek
argument_list|()
decl_stmt|;
name|parentEl
operator|.
name|appendChild
argument_list|(
name|closedEl
argument_list|)
expr_stmt|;
block|}
name|closedEl
operator|.
name|setUserData
argument_list|(
name|LINE_NUMBER_END
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|locator
operator|.
name|getLineNumber
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|closedEl
operator|.
name|setUserData
argument_list|(
name|COLUMN_NUMBER_END
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|locator
operator|.
name|getColumnNumber
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|characters
parameter_list|(
specifier|final
name|char
name|ch
index|[]
parameter_list|,
specifier|final
name|int
name|start
parameter_list|,
specifier|final
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
name|char
index|[]
name|chars
init|=
operator|new
name|char
index|[
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|chars
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|chars
argument_list|)
decl_stmt|;
name|s
operator|=
name|transformer
operator|.
name|transform
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|textBuffer
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InputSource
name|resolveEntity
parameter_list|(
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
comment|// do not resolve external dtd
return|return
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|""
argument_list|)
argument_list|)
return|;
block|}
comment|// Outputs text accumulated under the current node
specifier|private
name|void
name|addTextIfNeeded
parameter_list|()
block|{
if|if
condition|(
name|textBuffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
specifier|final
name|Element
name|el
init|=
name|elementStack
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|elementStack
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Node
name|textNode
init|=
name|doc
operator|.
name|createTextNode
argument_list|(
name|textBuffer
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|el
operator|.
name|appendChild
argument_list|(
name|textNode
argument_list|)
expr_stmt|;
name|textBuffer
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|textBuffer
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|is
argument_list|,
name|handler
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
DECL|class|NoopTransformer
specifier|private
specifier|static
specifier|final
class|class
name|NoopTransformer
implements|implements
name|XmlTextTransformer
block|{
annotation|@
name|Override
DECL|method|transform (String text)
specifier|public
name|String
name|transform
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|text
return|;
block|}
block|}
block|}
end_class

end_unit

