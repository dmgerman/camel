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
name|FromType
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
name|InterceptorRef
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
name|MulticastType
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
name|PipelineType
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
name|ProcessorType
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
name|RouteType
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
name|ToType
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
name|isNotEmpty
import|;
end_import

begin_comment
comment|/**  * A<a href="http://www.graphviz.org/">DOT</a> file creator plugin which  * creates a DOT file showing the current routes  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteDotGenerator
specifier|public
class|class
name|RouteDotGenerator
extends|extends
name|GraphGeneratorSupport
block|{
DECL|method|RouteDotGenerator (String dir)
specifier|public
name|RouteDotGenerator
parameter_list|(
name|String
name|dir
parameter_list|)
block|{
name|super
argument_list|(
name|dir
argument_list|,
literal|".dot"
argument_list|)
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|printRoutes (PrintWriter writer, Map<String, List<RouteType>> map)
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
name|RouteType
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
name|RouteType
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
name|RouteType
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
DECL|method|printRoutes (PrintWriter writer, String group, List<RouteType> routes)
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
name|RouteType
argument_list|>
name|routes
parameter_list|)
block|{
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
literal|"subgraph cluster_"
operator|+
operator|(
name|clusterCounter
operator|++
operator|)
operator|+
literal|" {"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"label = \""
operator|+
name|group
operator|+
literal|"\";"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"color = grey;"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"style = \"dashed\";"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"URL = \""
operator|+
name|group
operator|+
literal|".html\";"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|RouteType
name|route
range|:
name|routes
control|)
block|{
name|List
argument_list|<
name|FromType
argument_list|>
name|inputs
init|=
name|route
operator|.
name|getInputs
argument_list|()
decl_stmt|;
for|for
control|(
name|FromType
name|input
range|:
name|inputs
control|)
block|{
name|printRoute
argument_list|(
name|writer
argument_list|,
name|route
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
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
literal|"}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|escapeNodeId (String text)
specifier|protected
name|String
name|escapeNodeId
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|text
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'_'
argument_list|)
operator|.
name|replace
argument_list|(
literal|"$"
argument_list|,
literal|"_"
argument_list|)
return|;
block|}
DECL|method|printRoute (PrintWriter writer, final RouteType route, FromType input)
specifier|protected
name|void
name|printRoute
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
specifier|final
name|RouteType
name|route
parameter_list|,
name|FromType
name|input
parameter_list|)
block|{
name|NodeData
name|nodeData
init|=
name|getNodeData
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|printNode
argument_list|(
name|writer
argument_list|,
name|nodeData
argument_list|)
expr_stmt|;
comment|// TODO we should add a transactional client / event driven consumer / polling client
name|NodeData
name|from
init|=
name|nodeData
decl_stmt|;
for|for
control|(
name|ProcessorType
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
DECL|method|printNode (PrintWriter writer, NodeData fromData, ProcessorType node)
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
name|ProcessorType
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|MulticastType
operator|||
name|node
operator|instanceof
name|InterceptorRef
condition|)
block|{
comment|// no need for a multicast or interceptor node
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
init|=
name|node
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|boolean
name|isPipeline
init|=
name|isPipeline
argument_list|(
name|node
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcessorType
name|output
range|:
name|outputs
control|)
block|{
name|NodeData
name|out
init|=
name|printNode
argument_list|(
name|writer
argument_list|,
name|fromData
argument_list|,
name|output
argument_list|)
decl_stmt|;
comment|// if in pipeline then we should move the from node to the next in the pipeline
if|if
condition|(
name|isPipeline
condition|)
block|{
name|fromData
operator|=
name|out
expr_stmt|;
block|}
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
name|fromData
operator|.
name|id
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
literal|" -> "
argument_list|)
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|toData
operator|.
name|id
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|" ["
argument_list|)
expr_stmt|;
name|String
name|label
init|=
name|fromData
operator|.
name|edgeLabel
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|label
argument_list|)
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"label = \""
operator|+
name|label
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"];"
argument_list|)
expr_stmt|;
block|}
comment|// now lets write any children
comment|//List<ProcessorType> outputs = node.getOutputs();
name|List
argument_list|<
name|ProcessorType
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
name|ProcessorType
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
name|data
operator|.
name|id
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|" ["
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"label = \""
operator|+
name|data
operator|.
name|label
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"tooltip = \""
operator|+
name|data
operator|.
name|tooltop
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|data
operator|.
name|url
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"URL = \""
operator|+
name|data
operator|.
name|url
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|String
name|image
init|=
name|data
operator|.
name|image
decl_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"shapefile = \""
operator|+
name|image
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"peripheries=0"
argument_list|)
expr_stmt|;
block|}
name|String
name|shape
init|=
name|data
operator|.
name|shape
decl_stmt|;
if|if
condition|(
name|shape
operator|==
literal|null
operator|&&
name|image
operator|!=
literal|null
condition|)
block|{
name|shape
operator|=
literal|"custom"
expr_stmt|;
block|}
if|if
condition|(
name|shape
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"shape = \""
operator|+
name|shape
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"];"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|generateFile (PrintWriter writer, Map<String, List<RouteType>> map)
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
name|RouteType
argument_list|>
argument_list|>
name|map
parameter_list|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"digraph CamelRoutes {"
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
literal|"node [style = \"rounded,filled\", fillcolor = yellow, "
operator|+
literal|"fontname=\"Helvetica-Oblique\"];"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
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
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Is the given node a pipeline      */
DECL|method|isPipeline (ProcessorType node)
specifier|private
specifier|static
name|boolean
name|isPipeline
parameter_list|(
name|ProcessorType
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|MulticastType
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|node
operator|instanceof
name|PipelineType
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|node
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// is pipeline if there is more than 1 output and they are all To types
for|for
control|(
name|Object
name|type
range|:
name|node
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|type
operator|instanceof
name|ToType
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

