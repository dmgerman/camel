begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|catalog
operator|.
name|CamelCatalog
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
name|catalog
operator|.
name|DefaultCamelCatalog
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
name|catalog
operator|.
name|JSonSchemaHelper
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
name|parser
operator|.
name|model
operator|.
name|CamelNodeDetails
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
name|parser
operator|.
name|model
operator|.
name|CamelNodeDetailsFactory
import|;
end_import

begin_class
DECL|class|CamelXmlTreeParserHelper
specifier|public
specifier|final
class|class
name|CamelXmlTreeParserHelper
block|{
DECL|field|camelCatalog
specifier|private
specifier|final
name|CamelCatalog
name|camelCatalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|(
literal|true
argument_list|)
decl_stmt|;
DECL|method|parseCamelRouteTree (Node xmlNode, String routeId, CamelNodeDetails route, String baseDir, String fullyQualifiedFileName)
specifier|public
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|parseCamelRouteTree
parameter_list|(
name|Node
name|xmlNode
parameter_list|,
name|String
name|routeId
parameter_list|,
name|CamelNodeDetails
name|route
parameter_list|,
name|String
name|baseDir
parameter_list|,
name|String
name|fullyQualifiedFileName
parameter_list|)
block|{
name|CamelNodeDetailsFactory
name|nodeFactory
init|=
name|CamelNodeDetailsFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|walkXmlTree
argument_list|(
name|nodeFactory
argument_list|,
name|xmlNode
argument_list|,
name|route
argument_list|)
expr_stmt|;
comment|// now parse the route node and build the correct model/tree structure of the EIPs
comment|// re-create factory as we rebuild the tree
name|nodeFactory
operator|=
name|CamelNodeDetailsFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|CamelNodeDetails
name|parent
init|=
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// we dont want the route element and only start with from
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|CamelNodeDetails
name|node
init|=
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|node
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"from"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|CamelNodeDetails
name|from
init|=
name|nodeFactory
operator|.
name|copyNode
argument_list|(
literal|null
argument_list|,
literal|"from"
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|from
operator|.
name|setFileName
argument_list|(
name|fullyQualifiedFileName
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|parent
operator|=
name|from
expr_stmt|;
block|}
else|else
block|{
comment|// add straight to parent
name|parent
operator|.
name|addOutput
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|node
operator|.
name|setFileName
argument_list|(
name|fullyQualifiedFileName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|walkXmlTree (CamelNodeDetailsFactory nodeFactory, Node node, CamelNodeDetails parent)
specifier|private
name|void
name|walkXmlTree
parameter_list|(
name|CamelNodeDetailsFactory
name|nodeFactory
parameter_list|,
name|Node
name|node
parameter_list|,
name|CamelNodeDetails
name|parent
parameter_list|)
block|{
name|CamelNodeDetails
name|newNode
init|=
literal|null
decl_stmt|;
name|String
name|name
init|=
name|node
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
name|boolean
name|isRoute
init|=
literal|"route"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"from"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// must be an eip model that has either input or output as we only want to track processors (also accept from)
name|boolean
name|isEip
init|=
name|camelCatalog
operator|.
name|findModelNames
argument_list|()
operator|.
name|contains
argument_list|(
name|name
argument_list|)
operator|&&
operator|(
name|hasInput
argument_list|(
name|name
argument_list|)
operator|||
name|hasOutput
argument_list|(
name|name
argument_list|)
operator|)
decl_stmt|;
comment|// skip when/otherwise (as we do this in Java DSL)
name|boolean
name|isWhenOrOtherwise
init|=
literal|"when"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"otherwise"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// only include if its a known Camel model (dont include languages)
if|if
condition|(
name|isRoute
operator|||
name|isEip
condition|)
block|{
comment|// skip route as we just keep from (and also skip when/otherwise)
if|if
condition|(
operator|!
literal|"route"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
name|isWhenOrOtherwise
condition|)
block|{
name|String
name|lineNumber
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
name|newNode
operator|=
name|nodeFactory
operator|.
name|newNode
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|newNode
operator|.
name|setRouteId
argument_list|(
name|parent
operator|.
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
name|newNode
operator|.
name|setFileName
argument_list|(
name|parent
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|newNode
operator|.
name|setLineNumber
argument_list|(
name|lineNumber
argument_list|)
expr_stmt|;
name|newNode
operator|.
name|setLineNumberEnd
argument_list|(
name|lineNumberEnd
argument_list|)
expr_stmt|;
name|parent
operator|.
name|addOutput
argument_list|(
name|newNode
argument_list|)
expr_stmt|;
block|}
block|}
name|NodeList
name|children
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
name|walkXmlTree
argument_list|(
name|nodeFactory
argument_list|,
name|child
argument_list|,
name|newNode
operator|!=
literal|null
condition|?
name|newNode
else|:
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|hasOutput (String name)
specifier|private
name|boolean
name|hasOutput
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|json
init|=
name|camelCatalog
operator|.
name|modelJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"model"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|isModelOutput
argument_list|(
name|rows
argument_list|)
return|;
block|}
DECL|method|isModelOutput (List<Map<String, String>> rows)
specifier|private
specifier|static
name|boolean
name|isModelOutput
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
parameter_list|)
block|{
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"output"
argument_list|)
condition|)
block|{
return|return
literal|"true"
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"output"
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|hasInput (String name)
specifier|private
name|boolean
name|hasInput
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|json
init|=
name|camelCatalog
operator|.
name|modelJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"model"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|isModelInput
argument_list|(
name|rows
argument_list|)
return|;
block|}
DECL|method|isModelInput (List<Map<String, String>> rows)
specifier|private
specifier|static
name|boolean
name|isModelInput
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
parameter_list|)
block|{
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"input"
argument_list|)
condition|)
block|{
return|return
literal|"true"
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"input"
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

