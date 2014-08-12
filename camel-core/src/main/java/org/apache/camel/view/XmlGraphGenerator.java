begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.view
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|view
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|Set
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
name|model
operator|.
name|FromDefinition
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
name|model
operator|.
name|MulticastDefinition
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|RouteDefinition
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isEmpty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
operator|.
name|xmlEncode
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|XmlGraphGenerator
specifier|public
class|class
name|XmlGraphGenerator
extends|extends
name|GraphGeneratorSupport
block|{
DECL|field|addUrl
specifier|private
name|boolean
name|addUrl
init|=
literal|true
decl_stmt|;
DECL|method|XmlGraphGenerator (String dir)
specifier|public
name|XmlGraphGenerator
parameter_list|(
name|String
name|dir
parameter_list|)
block|{
name|super
argument_list|(
name|dir
argument_list|,
literal|".xml"
argument_list|)
expr_stmt|;
block|}
DECL|method|generateFile (PrintWriter writer, Map<String, List<RouteDefinition>> map)
specifier|protected
name|void
name|generateFile
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|>
name|map
parameter_list|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<Graph>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<Node id='root' name='Camel Routes' description='Collection of Camel Routes' nodeType='root'/>"
argument_list|)
expr_stmt|;
block|}
name|printRoutes
argument_list|(
name|writer
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</Graph>"
argument_list|)
expr_stmt|;
block|}
DECL|method|printRoutes (PrintWriter writer, Map<String, List<RouteDefinition>> map)
specifier|protected
name|void
name|printRoutes
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|>
name|map
parameter_list|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|group
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|printRoutes
argument_list|(
name|writer
argument_list|,
name|group
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|printRoutes (PrintWriter writer, String group, List<RouteDefinition> routes)
specifier|protected
name|void
name|printRoutes
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|group
parameter_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
parameter_list|)
block|{
name|group
operator|=
name|xmlEncode
argument_list|(
name|group
argument_list|)
expr_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|int
name|idx
init|=
name|group
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|group
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
operator|&&
name|idx
operator|<
name|group
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|group
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"<Node id='"
operator|+
name|group
operator|+
literal|"' name='"
operator|+
name|name
operator|+
literal|"' description='"
operator|+
name|group
operator|+
literal|"' nodeType='group'/>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<Edge fromID='root' toID='"
operator|+
name|group
operator|+
literal|"'/>"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routes
control|)
block|{
name|List
argument_list|<
name|FromDefinition
argument_list|>
name|inputs
init|=
name|route
operator|.
name|getInputs
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|FromDefinition
name|input
range|:
name|inputs
control|)
block|{
name|NodeData
name|nodeData
init|=
name|getNodeData
argument_list|(
name|input
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<Edge fromID='"
operator|+
name|group
operator|+
literal|"' toID='"
operator|+
name|xmlEncode
argument_list|(
name|nodeData
operator|.
name|id
argument_list|)
operator|+
literal|"'/>"
argument_list|)
expr_stmt|;
block|}
block|}
name|printRoute
argument_list|(
name|writer
argument_list|,
name|route
argument_list|,
name|nodeData
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|printRoute (PrintWriter writer, final RouteDefinition route, NodeData nodeData)
specifier|protected
name|void
name|printRoute
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
specifier|final
name|RouteDefinition
name|route
parameter_list|,
name|NodeData
name|nodeData
parameter_list|)
block|{
name|printNode
argument_list|(
name|writer
argument_list|,
name|nodeData
argument_list|)
expr_stmt|;
name|NodeData
name|from
init|=
name|nodeData
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|route
operator|.
name|getOutputs
argument_list|()
control|)
block|{
name|NodeData
name|newData
init|=
name|printNode
argument_list|(
name|writer
argument_list|,
name|from
argument_list|,
name|output
argument_list|)
decl_stmt|;
name|from
operator|=
name|newData
expr_stmt|;
block|}
block|}
DECL|method|printNode (PrintWriter writer, NodeData fromData, ProcessorDefinition<?> node)
specifier|protected
name|NodeData
name|printNode
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|NodeData
name|fromData
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|MulticastDefinition
condition|)
block|{
comment|// no need for a multicast node
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
name|node
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|outputs
control|)
block|{
name|printNode
argument_list|(
name|writer
argument_list|,
name|fromData
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
return|return
name|fromData
return|;
block|}
name|NodeData
name|toData
init|=
name|getNodeData
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|printNode
argument_list|(
name|writer
argument_list|,
name|toData
argument_list|)
expr_stmt|;
if|if
condition|(
name|fromData
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|print
argument_list|(
literal|"<Edge fromID=\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|xmlEncode
argument_list|(
name|fromData
operator|.
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
literal|"\" toID=\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|xmlEncode
argument_list|(
name|toData
operator|.
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|association
init|=
name|toData
operator|.
name|edgeLabel
decl_stmt|;
if|if
condition|(
name|isEmpty
argument_list|(
name|association
argument_list|)
condition|)
block|{
name|writer
operator|.
name|print
argument_list|(
literal|"\" association=\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|xmlEncode
argument_list|(
name|association
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"\"/>"
argument_list|)
expr_stmt|;
block|}
comment|// now lets write any children
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
name|toData
operator|.
name|outputs
decl_stmt|;
if|if
condition|(
name|outputs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|outputs
control|)
block|{
name|NodeData
name|newData
init|=
name|printNode
argument_list|(
name|writer
argument_list|,
name|toData
argument_list|,
name|output
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isMulticastNode
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|toData
operator|=
name|newData
expr_stmt|;
block|}
block|}
block|}
return|return
name|toData
return|;
block|}
DECL|method|printNode (PrintWriter writer, NodeData data)
specifier|protected
name|void
name|printNode
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|NodeData
name|data
parameter_list|)
block|{
if|if
condition|(
operator|!
name|data
operator|.
name|nodeWritten
condition|)
block|{
name|data
operator|.
name|nodeWritten
operator|=
literal|true
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
literal|"<Node id=\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|xmlEncode
argument_list|(
name|data
operator|.
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
literal|"\" name=\""
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|data
operator|.
name|label
decl_stmt|;
if|if
condition|(
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|data
operator|.
name|tooltop
expr_stmt|;
block|}
name|writer
operator|.
name|print
argument_list|(
name|xmlEncode
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
literal|"\" nodeType=\""
argument_list|)
expr_stmt|;
name|String
name|nodeType
init|=
name|data
operator|.
name|image
decl_stmt|;
if|if
condition|(
name|isEmpty
argument_list|(
name|nodeType
argument_list|)
condition|)
block|{
name|nodeType
operator|=
name|data
operator|.
name|shape
expr_stmt|;
if|if
condition|(
name|isEmpty
argument_list|(
name|nodeType
argument_list|)
condition|)
block|{
name|nodeType
operator|=
literal|"node"
expr_stmt|;
block|}
block|}
name|writer
operator|.
name|print
argument_list|(
name|xmlEncode
argument_list|(
name|nodeType
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
literal|"\" description=\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|xmlEncode
argument_list|(
name|data
operator|.
name|tooltop
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|addUrl
condition|)
block|{
name|writer
operator|.
name|print
argument_list|(
literal|"\" url=\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|xmlEncode
argument_list|(
name|data
operator|.
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"\"/>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

