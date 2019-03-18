begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamConstants
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
name|XMLStreamException
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
name|sax
operator|.
name|SAXSource
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
name|ContentHandler
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
name|DTDHandler
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
name|EntityResolver
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
name|ErrorHandler
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
name|xml
operator|.
name|sax
operator|.
name|SAXNotRecognizedException
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
name|SAXNotSupportedException
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
name|SAXParseException
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
name|XMLReader
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
name|ext
operator|.
name|LexicalHandler
import|;
end_import

begin_comment
comment|/**  * Adapter to turn a StAX {@link XMLStreamReader} into a {@link SAXSource}.  */
end_comment

begin_class
DECL|class|StAX2SAXSource
specifier|public
class|class
name|StAX2SAXSource
extends|extends
name|SAXSource
implements|implements
name|XMLReader
block|{
DECL|field|streamReader
specifier|private
name|XMLStreamReader
name|streamReader
decl_stmt|;
DECL|field|contentHandler
specifier|private
name|ContentHandler
name|contentHandler
decl_stmt|;
DECL|field|lexicalHandler
specifier|private
name|LexicalHandler
name|lexicalHandler
decl_stmt|;
DECL|method|StAX2SAXSource (XMLStreamReader streamReader)
specifier|public
name|StAX2SAXSource
parameter_list|(
name|XMLStreamReader
name|streamReader
parameter_list|)
block|{
name|this
operator|.
name|streamReader
operator|=
name|streamReader
expr_stmt|;
name|setInputSource
argument_list|(
operator|new
name|InputSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getXMLReader ()
specifier|public
name|XMLReader
name|getXMLReader
parameter_list|()
block|{
return|return
name|this
return|;
block|}
DECL|method|getXMLStreamReader ()
specifier|public
name|XMLStreamReader
name|getXMLStreamReader
parameter_list|()
block|{
return|return
name|streamReader
return|;
block|}
DECL|method|parse ()
specifier|protected
name|void
name|parse
parameter_list|()
throws|throws
name|SAXException
block|{
specifier|final
name|StAX2SAXAttributes
name|attributes
init|=
operator|new
name|StAX2SAXAttributes
argument_list|()
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
switch|switch
condition|(
name|streamReader
operator|.
name|getEventType
argument_list|()
condition|)
block|{
comment|// Attributes are handled in START_ELEMENT
case|case
name|XMLStreamConstants
operator|.
name|ATTRIBUTE
case|:
break|break;
case|case
name|XMLStreamConstants
operator|.
name|CDATA
case|:
block|{
if|if
condition|(
name|lexicalHandler
operator|!=
literal|null
condition|)
block|{
name|lexicalHandler
operator|.
name|startCDATA
argument_list|()
expr_stmt|;
block|}
name|int
name|length
init|=
name|streamReader
operator|.
name|getTextLength
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|streamReader
operator|.
name|getTextStart
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|streamReader
operator|.
name|getTextCharacters
argument_list|()
decl_stmt|;
name|contentHandler
operator|.
name|characters
argument_list|(
name|chars
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|lexicalHandler
operator|!=
literal|null
condition|)
block|{
name|lexicalHandler
operator|.
name|endCDATA
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
case|case
name|XMLStreamConstants
operator|.
name|CHARACTERS
case|:
block|{
name|int
name|length
init|=
name|streamReader
operator|.
name|getTextLength
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|streamReader
operator|.
name|getTextStart
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|streamReader
operator|.
name|getTextCharacters
argument_list|()
decl_stmt|;
name|contentHandler
operator|.
name|characters
argument_list|(
name|chars
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|XMLStreamConstants
operator|.
name|SPACE
case|:
block|{
name|int
name|length
init|=
name|streamReader
operator|.
name|getTextLength
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|streamReader
operator|.
name|getTextStart
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|streamReader
operator|.
name|getTextCharacters
argument_list|()
decl_stmt|;
name|contentHandler
operator|.
name|ignorableWhitespace
argument_list|(
name|chars
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|XMLStreamConstants
operator|.
name|COMMENT
case|:
if|if
condition|(
name|lexicalHandler
operator|!=
literal|null
condition|)
block|{
name|int
name|length
init|=
name|streamReader
operator|.
name|getTextLength
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|streamReader
operator|.
name|getTextStart
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|streamReader
operator|.
name|getTextCharacters
argument_list|()
decl_stmt|;
name|lexicalHandler
operator|.
name|comment
argument_list|(
name|chars
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|XMLStreamConstants
operator|.
name|DTD
case|:
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_DOCUMENT
case|:
name|contentHandler
operator|.
name|endDocument
argument_list|()
expr_stmt|;
return|return;
case|case
name|XMLStreamConstants
operator|.
name|END_ELEMENT
case|:
block|{
name|String
name|uri
init|=
name|nullToEmpty
argument_list|(
name|streamReader
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|localName
init|=
name|streamReader
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|qname
init|=
name|getPrefixedName
argument_list|(
name|streamReader
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|localName
argument_list|)
decl_stmt|;
name|contentHandler
operator|.
name|endElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|qname
argument_list|)
expr_stmt|;
comment|// namespaces
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|streamReader
operator|.
name|getNamespaceCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|nsPrefix
init|=
name|streamReader
operator|.
name|getNamespacePrefix
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|contentHandler
operator|.
name|endPrefixMapping
argument_list|(
name|nsPrefix
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
name|XMLStreamConstants
operator|.
name|ENTITY_DECLARATION
case|:
case|case
name|XMLStreamConstants
operator|.
name|ENTITY_REFERENCE
case|:
case|case
name|XMLStreamConstants
operator|.
name|NAMESPACE
case|:
case|case
name|XMLStreamConstants
operator|.
name|NOTATION_DECLARATION
case|:
break|break;
case|case
name|XMLStreamConstants
operator|.
name|PROCESSING_INSTRUCTION
case|:
break|break;
case|case
name|XMLStreamConstants
operator|.
name|START_DOCUMENT
case|:
name|contentHandler
operator|.
name|startDocument
argument_list|()
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|START_ELEMENT
case|:
block|{
comment|// namespaces
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|streamReader
operator|.
name|getNamespaceCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|nsPrefix
init|=
name|nullToEmpty
argument_list|(
name|streamReader
operator|.
name|getNamespacePrefix
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|nsUri
init|=
name|nullToEmpty
argument_list|(
name|streamReader
operator|.
name|getNamespaceURI
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|contentHandler
operator|.
name|startPrefixMapping
argument_list|(
name|nsPrefix
argument_list|,
name|nsUri
argument_list|)
expr_stmt|;
block|}
name|String
name|uri
init|=
name|nullToEmpty
argument_list|(
name|streamReader
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|localName
init|=
name|streamReader
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|qname
init|=
name|getPrefixedName
argument_list|(
name|streamReader
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|localName
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|init
argument_list|()
expr_stmt|;
name|contentHandler
operator|.
name|startElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|qname
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|reset
argument_list|()
expr_stmt|;
break|break;
block|}
default|default:
break|break;
block|}
if|if
condition|(
operator|!
name|streamReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return;
block|}
name|streamReader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|SAXParseException
name|spe
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|spe
operator|=
operator|new
name|SAXParseException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
operator|.
name|getLocation
argument_list|()
operator|.
name|getLineNumber
argument_list|()
argument_list|,
name|e
operator|.
name|getLocation
argument_list|()
operator|.
name|getColumnNumber
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|spe
operator|=
operator|new
name|SAXParseException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|spe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|spe
throw|;
block|}
block|}
DECL|method|getPrefixedName (String prefix, String localName)
specifier|private
name|String
name|getPrefixedName
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|localName
parameter_list|)
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
operator|||
name|prefix
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|localName
return|;
block|}
return|return
name|prefix
operator|+
literal|":"
operator|+
name|localName
return|;
block|}
DECL|method|nullToEmpty (String s)
specifier|private
name|String
name|nullToEmpty
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|==
literal|null
condition|?
literal|""
else|:
name|s
return|;
block|}
DECL|class|StAX2SAXAttributes
class|class
name|StAX2SAXAttributes
implements|implements
name|Attributes
block|{
DECL|field|attributeCount
specifier|private
name|int
name|attributeCount
decl_stmt|;
DECL|method|init ()
name|void
name|init
parameter_list|()
block|{
name|attributeCount
operator|=
name|streamReader
operator|.
name|getAttributeCount
argument_list|()
expr_stmt|;
block|}
DECL|method|reset ()
name|void
name|reset
parameter_list|()
block|{
name|attributeCount
operator|=
literal|0
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLength ()
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|attributeCount
return|;
block|}
DECL|method|checkIndex (int index)
specifier|private
name|boolean
name|checkIndex
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|index
operator|>=
literal|0
operator|&&
name|index
operator|<
name|attributeCount
return|;
block|}
annotation|@
name|Override
DECL|method|getURI (int index)
specifier|public
name|String
name|getURI
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
operator|!
name|checkIndex
argument_list|(
name|index
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|nullToEmpty
argument_list|(
name|streamReader
operator|.
name|getAttributeNamespace
argument_list|(
name|index
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getLocalName (int index)
specifier|public
name|String
name|getLocalName
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
operator|!
name|checkIndex
argument_list|(
name|index
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|streamReader
operator|.
name|getAttributeLocalName
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getQName (int index)
specifier|public
name|String
name|getQName
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
operator|!
name|checkIndex
argument_list|(
name|index
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|localName
init|=
name|streamReader
operator|.
name|getAttributeLocalName
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
name|streamReader
operator|.
name|getAttributePrefix
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|getPrefixedName
argument_list|(
name|prefix
argument_list|,
name|localName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getType (int index)
specifier|public
name|String
name|getType
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
operator|!
name|checkIndex
argument_list|(
name|index
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|streamReader
operator|.
name|getAttributeType
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (int index)
specifier|public
name|String
name|getValue
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
operator|!
name|checkIndex
argument_list|(
name|index
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|nullToEmpty
argument_list|(
name|streamReader
operator|.
name|getAttributeValue
argument_list|(
name|index
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getIndex (String searchUri, String searchLocalName)
specifier|public
name|int
name|getIndex
parameter_list|(
name|String
name|searchUri
parameter_list|,
name|String
name|searchLocalName
parameter_list|)
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
name|attributeCount
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|getURI
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|searchUri
argument_list|)
operator|&&
name|getLocalName
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|searchLocalName
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|getIndex (String searchQName)
specifier|public
name|int
name|getIndex
parameter_list|(
name|String
name|searchQName
parameter_list|)
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
name|attributeCount
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|getQName
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|searchQName
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|getType (String uri, String localName)
specifier|public
name|String
name|getType
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|)
block|{
return|return
name|getType
argument_list|(
name|getIndex
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getType (String qName)
specifier|public
name|String
name|getType
parameter_list|(
name|String
name|qName
parameter_list|)
block|{
return|return
name|getType
argument_list|(
name|getIndex
argument_list|(
name|qName
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (String uri, String localName)
specifier|public
name|String
name|getValue
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|)
block|{
return|return
name|getValue
argument_list|(
name|getIndex
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (String qName)
specifier|public
name|String
name|getValue
parameter_list|(
name|String
name|qName
parameter_list|)
block|{
return|return
name|getValue
argument_list|(
name|getIndex
argument_list|(
name|qName
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|getFeature (String name)
specifier|public
name|boolean
name|getFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
return|return
literal|false
return|;
block|}
DECL|method|setFeature (String name, boolean value)
specifier|public
name|void
name|setFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{     }
DECL|method|getProperty (String name)
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
return|return
literal|null
return|;
block|}
DECL|method|setProperty (String name, Object value)
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
if|if
condition|(
literal|"http://xml.org/sax/properties/lexical-handler"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|lexicalHandler
operator|=
operator|(
name|LexicalHandler
operator|)
name|value
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|SAXNotRecognizedException
argument_list|(
name|name
argument_list|)
throw|;
block|}
block|}
DECL|method|setEntityResolver (EntityResolver resolver)
specifier|public
name|void
name|setEntityResolver
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{     }
DECL|method|getEntityResolver ()
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|setDTDHandler (DTDHandler handler)
specifier|public
name|void
name|setDTDHandler
parameter_list|(
name|DTDHandler
name|handler
parameter_list|)
block|{     }
DECL|method|getDTDHandler ()
specifier|public
name|DTDHandler
name|getDTDHandler
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|setContentHandler (ContentHandler handler)
specifier|public
name|void
name|setContentHandler
parameter_list|(
name|ContentHandler
name|handler
parameter_list|)
block|{
name|this
operator|.
name|contentHandler
operator|=
name|handler
expr_stmt|;
if|if
condition|(
name|handler
operator|instanceof
name|LexicalHandler
operator|&&
name|lexicalHandler
operator|==
literal|null
condition|)
block|{
name|lexicalHandler
operator|=
operator|(
name|LexicalHandler
operator|)
name|handler
expr_stmt|;
block|}
block|}
DECL|method|getContentHandler ()
specifier|public
name|ContentHandler
name|getContentHandler
parameter_list|()
block|{
return|return
name|this
operator|.
name|contentHandler
return|;
block|}
DECL|method|setErrorHandler (ErrorHandler handler)
specifier|public
name|void
name|setErrorHandler
parameter_list|(
name|ErrorHandler
name|handler
parameter_list|)
block|{     }
DECL|method|getErrorHandler ()
specifier|public
name|ErrorHandler
name|getErrorHandler
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|parse (InputSource input)
specifier|public
name|void
name|parse
parameter_list|(
name|InputSource
name|input
parameter_list|)
throws|throws
name|SAXException
block|{
name|StAX2SAXSource
operator|.
name|this
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
DECL|method|parse (String systemId)
specifier|public
name|void
name|parse
parameter_list|(
name|String
name|systemId
parameter_list|)
throws|throws
name|SAXException
block|{
name|StAX2SAXSource
operator|.
name|this
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

