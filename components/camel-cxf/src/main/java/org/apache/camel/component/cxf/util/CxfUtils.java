begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.util
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
name|util
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|XMLStreamWriter
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|staxutils
operator|.
name|StaxUtils
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
name|W3CDOMStreamWriter
import|;
end_import

begin_class
DECL|class|CxfUtils
specifier|public
specifier|final
class|class
name|CxfUtils
block|{
DECL|method|CxfUtils ()
specifier|private
name|CxfUtils
parameter_list|()
block|{
comment|// helper class
block|}
DECL|method|getStringFromInputStream (InputStream in)
specifier|public
specifier|static
name|String
name|getStringFromInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|getOut
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|elementToString (Element element)
specifier|public
specifier|static
name|String
name|elementToString
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
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
name|visitNodesForNameSpace
argument_list|(
name|element
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|writeElement
argument_list|(
name|element
argument_list|,
name|writer
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
return|return
name|converter
operator|.
name|toString
argument_list|(
name|converter
operator|.
name|toDOMSource
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|copyHttpHeadersFromCxfToCamel (org.apache.cxf.message.Message cxfMessage, org.apache.camel.Message camelMessage)
specifier|public
specifier|static
name|void
name|copyHttpHeadersFromCxfToCamel
parameter_list|(
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
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
block|{
name|copyMessageHeader
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
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
name|REQUEST_URI
argument_list|,
name|Exchange
operator|.
name|HTTP_URI
argument_list|)
expr_stmt|;
name|copyMessageHeader
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
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
name|HTTP_REQUEST_METHOD
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|)
expr_stmt|;
comment|// We need remove the BASE_PATH from the PATH_INFO
name|String
name|pathInfo
init|=
operator|(
name|String
operator|)
name|cxfMessage
operator|.
name|get
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
name|PATH_INFO
argument_list|)
decl_stmt|;
name|String
name|basePath
init|=
operator|(
name|String
operator|)
name|cxfMessage
operator|.
name|get
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
name|BASE_PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|pathInfo
operator|!=
literal|null
operator|&&
name|basePath
operator|!=
literal|null
operator|&&
name|pathInfo
operator|.
name|startsWith
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|pathInfo
operator|=
name|pathInfo
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pathInfo
operator|!=
literal|null
condition|)
block|{
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|pathInfo
argument_list|)
expr_stmt|;
block|}
name|copyMessageHeader
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
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
name|CONTENT_TYPE
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|copyMessageHeader
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
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
name|ENCODING
argument_list|,
name|Exchange
operator|.
name|HTTP_CHARACTER_ENCODING
argument_list|)
expr_stmt|;
name|copyMessageHeader
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
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
name|QUERY_STRING
argument_list|,
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|)
expr_stmt|;
name|copyMessageHeader
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
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
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|Exchange
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
DECL|method|copyMessageHeader (org.apache.cxf.message.Message cxfMessage, Message camelMessage, String cxfKey, String camelKey)
specifier|private
specifier|static
name|void
name|copyMessageHeader
parameter_list|(
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
parameter_list|,
name|Message
name|camelMessage
parameter_list|,
name|String
name|cxfKey
parameter_list|,
name|String
name|camelKey
parameter_list|)
block|{
if|if
condition|(
name|cxfMessage
operator|.
name|get
argument_list|(
name|cxfKey
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|camelKey
argument_list|,
name|cxfMessage
operator|.
name|get
argument_list|(
name|cxfKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|writeElement (Element e, XMLStreamWriter writer, Map<String, String> namespaces)
specifier|private
specifier|static
name|void
name|writeElement
parameter_list|(
name|Element
name|e
parameter_list|,
name|XMLStreamWriter
name|writer
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|prefix
init|=
name|e
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
name|e
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|localName
init|=
name|e
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|localName
operator|==
literal|null
condition|)
block|{
name|localName
operator|=
name|e
operator|.
name|getNodeName
argument_list|()
expr_stmt|;
if|if
condition|(
name|localName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Element's local name cannot be null!"
argument_list|)
throw|;
block|}
block|}
name|String
name|decUri
init|=
name|writer
operator|.
name|getNamespaceContext
argument_list|()
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
name|boolean
name|declareNamespace
init|=
name|decUri
operator|==
literal|null
operator|||
operator|!
name|decUri
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|==
literal|null
operator|||
name|ns
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|localName
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|decUri
argument_list|)
condition|)
block|{
name|declareNamespace
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|localName
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
name|NamedNodeMap
name|attrs
init|=
name|e
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attrs
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|attr
init|=
name|attrs
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|attr
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|attrPrefix
init|=
name|attr
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|attrPrefix
operator|==
literal|null
condition|)
block|{
name|attrPrefix
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|attr
operator|.
name|getNodeName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|attrPrefix
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeNamespace
argument_list|(
name|name
argument_list|,
name|attr
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
operator|&&
name|attr
operator|.
name|getNodeValue
argument_list|()
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|declareNamespace
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
literal|""
operator|.
name|equals
argument_list|(
name|attrPrefix
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeNamespace
argument_list|(
literal|""
argument_list|,
name|attr
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|attr
operator|.
name|getNodeValue
argument_list|()
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|declareNamespace
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|attr
operator|.
name|getNodeValue
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|declareNamespace
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|attns
init|=
name|attr
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|attr
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|attns
operator|==
literal|null
operator|||
name|attns
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|writer
operator|.
name|writeAttribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|attrPrefix
operator|==
literal|null
operator|||
name|attrPrefix
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|writer
operator|.
name|writeAttribute
argument_list|(
name|attns
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|writeAttribute
argument_list|(
name|attrPrefix
argument_list|,
name|attns
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|declareNamespace
condition|)
block|{
if|if
condition|(
name|ns
operator|==
literal|null
condition|)
block|{
name|writer
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|namespaces
operator|!=
literal|null
operator|&&
name|namespaces
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|namespaces
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|namespaceURI
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
block|}
name|Node
name|nd
init|=
name|e
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
name|StaxUtils
operator|.
name|writeNode
argument_list|(
name|nd
argument_list|,
name|writer
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
DECL|method|visitNodesForNameSpace (Node node, Map<String, String> namespaces)
specifier|private
specifier|static
name|void
name|visitNodesForNameSpace
parameter_list|(
name|Node
name|node
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|element
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|getPrefix
argument_list|()
operator|!=
literal|null
operator|&&
name|element
operator|.
name|getNamespaceURI
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|namespaces
operator|.
name|put
argument_list|(
name|element
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|.
name|getChildNodes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|NodeList
name|nodelist
init|=
name|node
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nodelist
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|visitNodesForNameSpace
argument_list|(
name|nodelist
operator|.
name|item
argument_list|(
name|i
argument_list|)
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

