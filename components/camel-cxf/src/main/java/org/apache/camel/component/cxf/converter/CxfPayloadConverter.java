begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.converter
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
name|converter
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
name|io
operator|.
name|Reader
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
name|transform
operator|.
name|stax
operator|.
name|StAXSource
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
name|stream
operator|.
name|StreamSource
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
name|Converter
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
name|FallbackConverter
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
name|TypeConverter
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
name|camel
operator|.
name|spi
operator|.
name|TypeConverterRegistry
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
name|StaxSource
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

begin_class
annotation|@
name|Converter
DECL|class|CxfPayloadConverter
specifier|public
specifier|final
class|class
name|CxfPayloadConverter
block|{
DECL|field|xml
specifier|private
specifier|static
name|XmlConverter
name|xml
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
DECL|method|CxfPayloadConverter ()
specifier|private
name|CxfPayloadConverter
parameter_list|()
block|{
comment|// Helper class
block|}
annotation|@
name|Converter
DECL|method|documentToCxfPayload (Document doc, Exchange exchange)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|documentToCxfPayload
parameter_list|(
name|Document
name|doc
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|elementToCxfPayload
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|elementToCxfPayload (Element element, Exchange exchange)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|elementToCxfPayload
parameter_list|(
name|Element
name|element
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
operator|new
name|CxfPayload
argument_list|<
name|T
argument_list|>
argument_list|(
name|headers
argument_list|,
name|body
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|nodeListToCxfPayload (NodeList nodeList, Exchange exchange)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|nodeListToCxfPayload
parameter_list|(
name|NodeList
name|nodeList
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
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
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// add all nodes to the body that are elements
if|if
condition|(
name|Element
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|node
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|body
operator|.
name|add
argument_list|(
operator|(
name|Element
operator|)
name|node
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|CxfPayload
argument_list|<
name|T
argument_list|>
argument_list|(
name|headers
argument_list|,
name|body
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|sourceToCxfPayload (Source src, Exchange exchange)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|sourceToCxfPayload
parameter_list|(
name|Source
name|src
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Source
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<
name|Source
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
name|src
argument_list|)
expr_stmt|;
return|return
operator|new
name|CxfPayload
argument_list|<
name|T
argument_list|>
argument_list|(
name|headers
argument_list|,
name|body
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|cxfPayloadToNodeList (CxfPayload<T> payload, Exchange exchange)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|NodeList
name|cxfPayloadToNodeList
parameter_list|(
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|payload
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|NodeListWrapper
argument_list|(
name|payload
operator|.
name|getBody
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|cxfPayLoadToNode (CxfPayload<T> payload, Exchange exchange)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Node
name|cxfPayLoadToNode
parameter_list|(
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|payload
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|payloadBodyElements
init|=
name|payload
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|payloadBodyElements
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|payloadBodyElements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|cxfPayLoadToSource (CxfPayload<T> payload, Exchange exchange)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Source
name|cxfPayLoadToSource
parameter_list|(
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|payload
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|Source
argument_list|>
name|payloadBody
init|=
name|payload
operator|.
name|getBodySources
argument_list|()
decl_stmt|;
if|if
condition|(
name|payloadBody
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|payloadBody
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|cxfPayLoadToStreamCache (CxfPayload<T> payload, Exchange exchange)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|StreamCache
name|cxfPayLoadToStreamCache
parameter_list|(
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|payload
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|CachedCxfPayload
argument_list|<
name|T
argument_list|>
argument_list|(
name|payload
argument_list|,
name|exchange
argument_list|,
name|xml
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|FallbackConverter
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
comment|// use fallback type converter, so we can probably convert into
comment|// CxfPayloads from other types
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|Source
name|src
init|=
literal|null
decl_stmt|;
comment|// many of the common format that can have a Source created directly
if|if
condition|(
name|value
operator|instanceof
name|InputStream
condition|)
block|{
name|src
operator|=
operator|new
name|StreamSource
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Reader
condition|)
block|{
name|src
operator|=
operator|new
name|StreamSource
argument_list|(
operator|(
name|Reader
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|src
operator|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Node
condition|)
block|{
name|src
operator|=
operator|new
name|DOMSource
argument_list|(
operator|(
name|Node
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Source
condition|)
block|{
name|src
operator|=
operator|(
name|Source
operator|)
name|value
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
comment|// assuming staxsource is preferred, otherwise use the one preferred
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stax
operator|.
name|StAXSource
operator|.
name|class
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|==
literal|null
condition|)
block|{
name|tc
operator|=
name|registry
operator|.
name|lookup
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|src
operator|=
name|tc
operator|.
name|convertTo
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|src
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|sourceToCxfPayload
argument_list|(
name|src
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|NodeList
operator|.
name|class
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|NodeList
name|nodeList
init|=
name|tc
operator|.
name|convertTo
argument_list|(
name|NodeList
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
return|return
operator|(
name|T
operator|)
name|nodeListToCxfPayload
argument_list|(
name|nodeList
argument_list|,
name|exchange
argument_list|)
return|;
block|}
name|tc
operator|=
name|registry
operator|.
name|lookup
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|Document
name|document
init|=
name|tc
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
return|return
operator|(
name|T
operator|)
name|documentToCxfPayload
argument_list|(
name|document
argument_list|,
name|exchange
argument_list|)
return|;
block|}
comment|// maybe we can convert via an InputStream
name|CxfPayload
argument_list|<
name|?
argument_list|>
name|p
decl_stmt|;
name|p
operator|=
name|convertVia
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|,
name|registry
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|p
return|;
block|}
comment|// String is the converter of last resort
name|p
operator|=
name|convertVia
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|,
name|registry
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|p
return|;
block|}
comment|// no we could not do it currently
return|return
operator|(
name|T
operator|)
name|Void
operator|.
name|TYPE
return|;
block|}
comment|// Convert a CxfPayload into something else
if|if
condition|(
name|CxfPayload
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|CxfPayload
argument_list|<
name|?
argument_list|>
name|payload
init|=
operator|(
name|CxfPayload
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
name|int
name|size
init|=
name|payload
operator|.
name|getBodySources
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|1
condition|)
block|{
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|Document
operator|.
name|class
argument_list|)
condition|)
block|{
name|Source
name|s
init|=
name|payload
operator|.
name|getBodySources
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Document
name|d
decl_stmt|;
try|try
block|{
name|d
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|d
argument_list|)
return|;
block|}
comment|// CAMEL-8410 Just make sure we get the Source object directly from the payload body source
name|Source
name|s
init|=
name|payload
operator|.
name|getBodySources
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|s
argument_list|)
return|;
block|}
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|Source
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|XMLStreamReader
name|r
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|payload
operator|.
name|getNsMap
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|s
operator|instanceof
name|StaxSource
condition|)
block|{
name|r
operator|=
operator|(
operator|(
name|StaxSource
operator|)
name|s
operator|)
operator|.
name|getXMLStreamReader
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|s
operator|instanceof
name|StAXSource
condition|)
block|{
name|r
operator|=
operator|(
operator|(
name|StAXSource
operator|)
name|s
operator|)
operator|.
name|getXMLStreamReader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|s
operator|=
operator|new
name|StAXSource
argument_list|(
operator|new
name|DelegatingXMLStreamReader
argument_list|(
name|r
argument_list|,
name|payload
operator|.
name|getNsMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|T
name|t
init|=
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|s
argument_list|)
decl_stmt|;
return|return
name|t
return|;
block|}
block|}
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|NodeList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|Object
name|result
init|=
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|cxfPayloadToNodeList
argument_list|(
operator|(
name|CxfPayload
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// no we could not do it currently, and we just abort the convert here
return|return
operator|(
name|T
operator|)
name|Void
operator|.
name|TYPE
return|;
block|}
else|else
block|{
return|return
operator|(
name|T
operator|)
name|result
return|;
block|}
block|}
comment|// we cannot convert a node list, so we try the first item from the
comment|// node list
name|tc
operator|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|Node
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|NodeList
name|nodeList
init|=
name|cxfPayloadToNodeList
argument_list|(
operator|(
name|CxfPayload
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodeList
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|nodeList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// no we could not do it currently
return|return
operator|(
name|T
operator|)
name|Void
operator|.
name|TYPE
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
comment|// empty size so we cannot convert
return|return
operator|(
name|T
operator|)
name|Void
operator|.
name|TYPE
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|convertVia (Class<V> via, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|,
name|V
parameter_list|>
name|CxfPayload
argument_list|<
name|T
argument_list|>
name|convertVia
parameter_list|(
name|Class
argument_list|<
name|V
argument_list|>
name|via
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|via
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|TypeConverter
name|tc1
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|via
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc1
operator|!=
literal|null
condition|)
block|{
name|V
name|is
init|=
name|tc
operator|.
name|convertTo
argument_list|(
name|via
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|Document
name|document
init|=
name|tc1
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
decl_stmt|;
return|return
name|documentToCxfPayload
argument_list|(
name|document
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

