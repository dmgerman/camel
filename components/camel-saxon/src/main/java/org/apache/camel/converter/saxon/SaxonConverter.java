begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.saxon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|saxon
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|dom
operator|.
name|DOMNodeList
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|dom
operator|.
name|NodeOverNodeInfo
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|om
operator|.
name|DocumentInfo
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|om
operator|.
name|NodeInfo
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|trans
operator|.
name|XPathException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|type
operator|.
name|Type
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
name|spi
operator|.
name|TypeConverterRegistry
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|SaxonConverter
specifier|public
specifier|final
class|class
name|SaxonConverter
block|{
DECL|method|SaxonConverter ()
specifier|private
name|SaxonConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toDOMDocument (NodeInfo node)
specifier|public
specifier|static
name|Document
name|toDOMDocument
parameter_list|(
name|NodeInfo
name|node
parameter_list|)
throws|throws
name|XPathException
block|{
switch|switch
condition|(
name|node
operator|.
name|getNodeKind
argument_list|()
condition|)
block|{
case|case
name|Type
operator|.
name|DOCUMENT
case|:
comment|// DOCUMENT type nodes can be wrapped directly
return|return
operator|(
name|Document
operator|)
name|NodeOverNodeInfo
operator|.
name|wrap
argument_list|(
name|node
argument_list|)
return|;
case|case
name|Type
operator|.
name|ELEMENT
case|:
comment|// ELEMENT nodes need to build a new DocumentInfo before wrapping
name|Configuration
name|config
init|=
name|node
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|DocumentInfo
name|documentInfo
init|=
name|config
operator|.
name|buildDocument
argument_list|(
name|node
argument_list|)
decl_stmt|;
return|return
operator|(
name|Document
operator|)
name|NodeOverNodeInfo
operator|.
name|wrap
argument_list|(
name|documentInfo
argument_list|)
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toDOMNode (NodeInfo node)
specifier|public
specifier|static
name|Node
name|toDOMNode
parameter_list|(
name|NodeInfo
name|node
parameter_list|)
block|{
return|return
name|NodeOverNodeInfo
operator|.
name|wrap
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toDOMSourceFromNodeInfo (NodeInfo nodeInfo)
specifier|public
specifier|static
name|DOMSource
name|toDOMSourceFromNodeInfo
parameter_list|(
name|NodeInfo
name|nodeInfo
parameter_list|)
block|{
return|return
operator|new
name|DOMSource
argument_list|(
name|toDOMNode
argument_list|(
name|nodeInfo
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toDOMNodeList (List<? extends NodeInfo> nodeList)
specifier|public
specifier|static
name|NodeList
name|toDOMNodeList
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|NodeInfo
argument_list|>
name|nodeList
parameter_list|)
block|{
name|List
argument_list|<
name|Node
argument_list|>
name|domNodeList
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|nodeList
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|NodeInfo
name|ni
range|:
name|nodeList
control|)
block|{
name|domNodeList
operator|.
name|add
argument_list|(
name|NodeOverNodeInfo
operator|.
name|wrap
argument_list|(
name|ni
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|DOMNodeList
argument_list|(
name|domNodeList
argument_list|)
return|;
block|}
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
if|if
condition|(
name|NodeInfo
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
comment|// use a fallback type converter so we can convert the embedded body if the value is NodeInfo
name|NodeInfo
name|ni
init|=
operator|(
name|NodeInfo
operator|)
name|value
decl_stmt|;
comment|// first try to find a Converter for Node
name|TypeConverter
name|tc
init|=
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
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|Node
name|node
init|=
name|NodeOverNodeInfo
operator|.
name|wrap
argument_list|(
name|ni
argument_list|)
decl_stmt|;
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|node
argument_list|)
return|;
block|}
comment|// if this does not exist we can also try NodeList (there are some type converters for that) as
comment|// the default Xerces Node implementation also implements NodeList.
name|tc
operator|=
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
expr_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|NodeInfo
argument_list|>
name|nil
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|nil
operator|.
name|add
argument_list|(
operator|(
name|NodeInfo
operator|)
name|value
argument_list|)
expr_stmt|;
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|toDOMNodeList
argument_list|(
name|nil
argument_list|)
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|List
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
name|List
argument_list|<
name|NodeInfo
argument_list|>
name|lion
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|value
control|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|NodeInfo
condition|)
block|{
name|lion
operator|.
name|add
argument_list|(
operator|(
name|NodeInfo
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|lion
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|NodeList
name|nl
init|=
name|toDOMNodeList
argument_list|(
name|lion
argument_list|)
decl_stmt|;
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|nl
argument_list|)
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|NodeOverNodeInfo
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
comment|// NodeOverNode info is a read-only Node implementation from Saxon. In contrast to the JDK
comment|// com.sun.org.apache.xerces.internal.dom.NodeImpl class it does not implement NodeList, but
comment|// many Camel type converters are based on that interface. Therefore we convert to NodeList and
comment|// try type conversion in the fallback type converter.
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
name|List
argument_list|<
name|Node
argument_list|>
name|domNodeList
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|domNodeList
operator|.
name|add
argument_list|(
operator|(
name|NodeOverNodeInfo
operator|)
name|value
argument_list|)
expr_stmt|;
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
operator|new
name|DOMNodeList
argument_list|(
name|domNodeList
argument_list|)
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

