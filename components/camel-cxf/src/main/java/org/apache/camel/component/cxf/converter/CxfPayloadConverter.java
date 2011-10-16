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
operator|new
name|DOMSource
argument_list|(
name|element
argument_list|)
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
operator|new
name|DOMSource
argument_list|(
operator|(
name|Element
operator|)
name|node
argument_list|)
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
name|convertToElementList
argument_list|(
name|payload
operator|.
name|getBody
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|convertToElementList (List<Source> srcs)
specifier|private
specifier|static
name|List
argument_list|<
name|Element
argument_list|>
name|convertToElementList
parameter_list|(
name|List
argument_list|<
name|Source
argument_list|>
name|srcs
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|ret
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
name|x
init|=
literal|0
init|;
name|x
operator|<
name|srcs
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Source
name|src
init|=
name|srcs
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|src
operator|instanceof
name|DOMSource
condition|)
block|{
name|el
operator|=
call|(
name|Element
call|)
argument_list|(
operator|(
name|DOMSource
operator|)
name|src
argument_list|)
operator|.
name|getNode
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|src
argument_list|)
decl_stmt|;
name|el
operator|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|srcs
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|new
name|DOMSource
argument_list|(
name|el
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ret
operator|.
name|add
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
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
name|convertToElementList
argument_list|(
name|payload
operator|.
name|getBody
argument_list|()
argument_list|)
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
return|return
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
return|;
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

