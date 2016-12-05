begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
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
name|HashMap
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
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|util
operator|.
name|Strings
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
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_class
DECL|class|CamelXmlHelper
specifier|public
class|class
name|CamelXmlHelper
block|{
DECL|method|getSafeAttribute (Node node, String key)
specifier|public
specifier|static
name|String
name|getSafeAttribute
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|Node
name|attr
init|=
name|node
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|!=
literal|null
condition|)
block|{
return|return
name|attr
operator|.
name|getNodeValue
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|findAllEndpoints (Document dom)
specifier|public
specifier|static
name|List
argument_list|<
name|Node
argument_list|>
name|findAllEndpoints
parameter_list|(
name|Document
name|dom
parameter_list|)
block|{
name|List
argument_list|<
name|Node
argument_list|>
name|nodes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|NodeList
name|list
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"endpoint"
argument_list|)
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
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"endpoint"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
comment|// it may not be a camel namespace, so skip those
name|String
name|ns
init|=
name|child
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|ns
operator|==
literal|null
condition|)
block|{
name|NamedNodeMap
name|attrs
init|=
name|child
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|attrs
operator|!=
literal|null
condition|)
block|{
name|Node
name|node
init|=
name|attrs
operator|.
name|getNamedItem
argument_list|(
literal|"xmlns"
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|ns
operator|=
name|node
operator|.
name|getNodeValue
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// assume no namespace its for camel
if|if
condition|(
name|ns
operator|==
literal|null
operator|||
name|ns
operator|.
name|contains
argument_list|(
literal|"camel"
argument_list|)
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|list
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"onException"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|findAllUrisRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
name|list
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"onCompletion"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|findAllUrisRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
name|list
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"intercept"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|findAllUrisRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
name|list
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"interceptFrom"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|findAllUrisRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
name|list
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"interceptSendToEndpoint"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|findAllUrisRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
name|list
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"rest"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"route"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNodeName
argument_list|()
argument_list|)
operator|||
literal|"to"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|findAllUrisRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
block|}
name|list
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"route"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"route"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|findAllUrisRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|nodes
return|;
block|}
DECL|method|findAllUrisRecursive (Node node, List<Node> nodes)
specifier|private
specifier|static
name|void
name|findAllUrisRecursive
parameter_list|(
name|Node
name|node
parameter_list|,
name|List
argument_list|<
name|Node
argument_list|>
name|nodes
parameter_list|)
block|{
comment|// okay its a route so grab all uri attributes we can find
name|String
name|url
init|=
name|getSafeAttribute
argument_list|(
name|node
argument_list|,
literal|"uri"
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
name|NodeList
name|children
init|=
name|node
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|children
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
name|findAllUrisRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|findAllSimpleExpressions (Document dom)
specifier|public
specifier|static
name|List
argument_list|<
name|Node
argument_list|>
name|findAllSimpleExpressions
parameter_list|(
name|Document
name|dom
parameter_list|)
block|{
name|List
argument_list|<
name|Node
argument_list|>
name|nodes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|NodeList
name|list
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"route"
argument_list|)
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
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"route"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|findAllSimpleExpressionsRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|nodes
return|;
block|}
DECL|method|findAllSimpleExpressionsRecursive (Node node, List<Node> nodes)
specifier|private
specifier|static
name|void
name|findAllSimpleExpressionsRecursive
parameter_list|(
name|Node
name|node
parameter_list|,
name|List
argument_list|<
name|Node
argument_list|>
name|nodes
parameter_list|)
block|{
comment|// okay its a route so grab if its<simple>
if|if
condition|(
literal|"simple"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
name|NodeList
name|children
init|=
name|node
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|children
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
name|findAllSimpleExpressionsRecursive
argument_list|(
name|child
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getSelectedCamelElementNode (String key, InputStream resourceInputStream)
specifier|public
specifier|static
name|Element
name|getSelectedCamelElementNode
parameter_list|(
name|String
name|key
parameter_list|,
name|InputStream
name|resourceInputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|root
init|=
name|loadCamelXmlFileAsDom
argument_list|(
name|resourceInputStream
argument_list|)
decl_stmt|;
name|Element
name|selectedElement
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|Node
name|selectedNode
init|=
name|findCamelNodeInDocument
argument_list|(
name|root
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|selectedNode
operator|instanceof
name|Element
condition|)
block|{
name|selectedElement
operator|=
operator|(
name|Element
operator|)
name|selectedNode
expr_stmt|;
block|}
block|}
return|return
name|selectedElement
return|;
block|}
DECL|method|loadCamelXmlFileAsDom (InputStream resourceInputStream)
specifier|private
specifier|static
name|Document
name|loadCamelXmlFileAsDom
parameter_list|(
name|InputStream
name|resourceInputStream
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO:
name|Document
name|root
init|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|resourceInputStream
argument_list|,
literal|"camelContext,routes,rests"
argument_list|,
literal|"http://camel.apache.org/schema/spring"
argument_list|)
decl_stmt|;
return|return
name|root
return|;
block|}
DECL|method|findCamelNodeInDocument (Document root, String key)
specifier|private
specifier|static
name|Node
name|findCamelNodeInDocument
parameter_list|(
name|Document
name|root
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Node
name|selectedNode
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
operator|&&
operator|!
name|Strings
operator|.
name|isBlank
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|String
index|[]
name|paths
init|=
name|key
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|NodeList
name|camels
init|=
name|getCamelContextElements
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|camels
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|rootNodeCounts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|size
init|=
name|camels
operator|.
name|getLength
argument_list|()
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|camels
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|path
range|:
name|paths
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
name|String
name|actual
init|=
name|getIdOrIndex
argument_list|(
name|node
argument_list|,
name|rootNodeCounts
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|equal
argument_list|(
name|actual
argument_list|,
name|path
argument_list|)
condition|)
block|{
name|node
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
name|node
operator|=
name|findCamelNodeForPath
argument_list|(
name|node
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
return|return
name|node
return|;
block|}
block|}
block|}
block|}
return|return
name|selectedNode
return|;
block|}
DECL|method|findCamelNodeForPath (Node node, String path)
specifier|private
specifier|static
name|Node
name|findCamelNodeForPath
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|NodeList
name|childNodes
init|=
name|node
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|childNodes
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|nodeCounts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|size
init|=
name|childNodes
operator|.
name|getLength
argument_list|()
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|childNodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Element
condition|)
block|{
name|String
name|actual
init|=
name|getIdOrIndex
argument_list|(
name|child
argument_list|,
name|nodeCounts
argument_list|)
decl_stmt|;
if|if
condition|(
name|equal
argument_list|(
name|actual
argument_list|,
name|path
argument_list|)
condition|)
block|{
return|return
name|child
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getIdOrIndex (Node node, Map<String, Integer> nodeCounts)
specifier|private
specifier|static
name|String
name|getIdOrIndex
parameter_list|(
name|Node
name|node
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|nodeCounts
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
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
name|String
name|elementName
init|=
name|element
operator|.
name|getTagName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"routes"
operator|.
name|equals
argument_list|(
name|elementName
argument_list|)
condition|)
block|{
name|elementName
operator|=
literal|"camelContext"
expr_stmt|;
block|}
name|Integer
name|countObject
init|=
name|nodeCounts
operator|.
name|get
argument_list|(
name|elementName
argument_list|)
decl_stmt|;
name|int
name|count
init|=
name|countObject
operator|!=
literal|null
condition|?
name|countObject
operator|.
name|intValue
argument_list|()
else|:
literal|0
decl_stmt|;
name|nodeCounts
operator|.
name|put
argument_list|(
name|elementName
argument_list|,
operator|++
name|count
argument_list|)
expr_stmt|;
name|answer
operator|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isBlank
argument_list|(
name|answer
argument_list|)
condition|)
block|{
name|answer
operator|=
literal|"_"
operator|+
name|elementName
operator|+
name|count
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getCamelContextElements (Document dom)
specifier|private
specifier|static
name|NodeList
name|getCamelContextElements
parameter_list|(
name|Document
name|dom
parameter_list|)
block|{
name|NodeList
name|camels
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"camelContext"
argument_list|)
decl_stmt|;
if|if
condition|(
name|camels
operator|==
literal|null
operator|||
name|camels
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
name|camels
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"routes"
argument_list|)
expr_stmt|;
block|}
return|return
name|camels
return|;
block|}
DECL|method|equal (Object a, Object b)
specifier|private
specifier|static
name|boolean
name|equal
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
name|a
operator|==
name|b
condition|?
literal|true
else|:
name|a
operator|!=
literal|null
operator|&&
name|b
operator|!=
literal|null
operator|&&
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
return|;
block|}
block|}
end_class

end_unit

