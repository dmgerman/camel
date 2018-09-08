begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|helper
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
name|ArrayDeque
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
name|LinkedHashMap
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
name|Queue
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
name|NamedNodeMap
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
comment|/**  * An XML parser that uses SAX to include line and column number for each XML element in the parsed Document.  *<p/>  * The line number and column number can be obtained from a Node/Element using  *<pre>  *   String lineNumber = (String) node.getUserData(XmlLineNumberParser.LINE_NUMBER);  *   String lineNumberEnd = (String) node.getUserData(XmlLineNumberParser.LINE_NUMBER_END);  *   String columnNumber = (String) node.getUserData(XmlLineNumberParser.COLUMN_NUMBER);  *   String columnNumberEnd = (String) node.getUserData(XmlLineNumberParser.COLUMN_NUMBER_END);  *</pre>  * Mind that start and end numbers are the same for single-level XML tags.  */
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
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Parses the XML.      *      * @param is the XML content as an input stream      * @param rootNames one or more root names that is used as baseline for beginning the parsing, for example camelContext to start parsing      *                  when Camel is discovered. Multiple names can be defined separated by comma      * @param forceNamespace an optional namespace to force assign to each node. This may be needed for JAXB unmarshalling from XML -> POJO.      * @return the DOM model      * @throws Exception is thrown if error parsing      */
DECL|method|parseXml (final InputStream is, final String rootNames, final String forceNamespace)
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
argument_list|<>
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
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|localNs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|anonymousNs
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
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
name|String
name|ns
init|=
literal|null
decl_stmt|;
comment|// are we using namespace prefixes
name|int
name|pos
init|=
name|qName
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
name|String
name|prefix
init|=
name|qName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|ns
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"xmlns:"
operator|+
name|prefix
argument_list|)
expr_stmt|;
if|if
condition|(
name|ns
operator|!=
literal|null
condition|)
block|{
name|localNs
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ns
operator|=
name|localNs
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// maybe there is an anonymous namespace (xmlns)
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
name|ns
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"xmlns"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ns
operator|!=
literal|null
condition|)
block|{
name|anonymousNs
operator|.
name|put
argument_list|(
name|qName
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|anonymousNs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// grab latest anonymous namespace to use as the namespace as
comment|// this child tag should use the parents+ namespace
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|anonymousNs
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|ns
operator|=
name|values
operator|.
name|get
argument_list|(
name|values
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|ns
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
name|ns
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
block|}
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
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
block|}
name|String
name|ln
init|=
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
decl_stmt|;
name|String
name|cn
init|=
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
decl_stmt|;
name|el
operator|.
name|setUserData
argument_list|(
name|LINE_NUMBER
argument_list|,
name|ln
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
name|cn
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
name|String
name|ln
init|=
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
decl_stmt|;
name|String
name|cn
init|=
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
decl_stmt|;
name|closedEl
operator|.
name|setUserData
argument_list|(
name|LINE_NUMBER_END
argument_list|,
name|ln
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
name|cn
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|anonymousNs
operator|.
name|remove
argument_list|(
name|qName
argument_list|)
expr_stmt|;
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
name|textBuffer
operator|.
name|append
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
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
block|}
end_class

end_unit

