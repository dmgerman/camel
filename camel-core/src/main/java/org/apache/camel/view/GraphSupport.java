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
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ChoiceDefinition
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
name|PipelineDefinition
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ToDefinition
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
name|language
operator|.
name|ExpressionDefinition
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
name|util
operator|.
name|CollectionStringBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A base class for Graph processing code of Camel EIPs containing a number of helper methods  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|GraphSupport
specifier|public
class|class
name|GraphSupport
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|nodeMap
specifier|protected
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|NodeData
argument_list|>
name|nodeMap
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|NodeData
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|imagePrefix
specifier|private
name|String
name|imagePrefix
init|=
literal|"http://camel.apache.org/images/eip/"
decl_stmt|;
DECL|method|getLabel (List<ExpressionDefinition> expressions)
specifier|protected
name|String
name|getLabel
parameter_list|(
name|List
argument_list|<
name|ExpressionDefinition
argument_list|>
name|expressions
parameter_list|)
block|{
name|CollectionStringBuffer
name|buffer
init|=
operator|new
name|CollectionStringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|ExpressionDefinition
name|expression
range|:
name|expressions
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|getLabel
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getLabel (ExpressionDefinition expression)
specifier|protected
name|String
name|getLabel
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
return|return
name|expression
operator|.
name|getLabel
argument_list|()
return|;
block|}
return|return
literal|""
return|;
block|}
DECL|method|getNodeData (Object node)
specifier|protected
name|NodeData
name|getNodeData
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|Object
name|key
init|=
name|node
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|FromDefinition
condition|)
block|{
name|FromDefinition
name|fromType
init|=
operator|(
name|FromDefinition
operator|)
name|node
decl_stmt|;
name|key
operator|=
name|fromType
operator|.
name|getUriOrRef
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|ToDefinition
condition|)
block|{
name|ToDefinition
name|toType
init|=
operator|(
name|ToDefinition
operator|)
name|node
decl_stmt|;
name|key
operator|=
name|toType
operator|.
name|getUriOrRef
argument_list|()
expr_stmt|;
block|}
name|NodeData
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|nodeMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|String
name|id
init|=
literal|"node"
operator|+
operator|(
name|nodeMap
operator|.
name|size
argument_list|()
operator|+
literal|1
operator|)
decl_stmt|;
name|answer
operator|=
operator|new
name|NodeData
argument_list|(
name|id
argument_list|,
name|node
argument_list|,
name|imagePrefix
argument_list|)
expr_stmt|;
name|nodeMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createRouteGroupMap (List<RouteDefinition> routes)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|>
name|createRouteGroupMap
parameter_list|(
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
parameter_list|)
block|{
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
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routes
control|)
block|{
name|addRouteToMap
argument_list|(
name|map
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
DECL|method|addRouteToMap (Map<String, List<RouteDefinition>> map, RouteDefinition route)
specifier|protected
name|void
name|addRouteToMap
parameter_list|(
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
parameter_list|,
name|RouteDefinition
name|route
parameter_list|)
block|{
name|String
name|group
init|=
name|route
operator|.
name|getGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
name|group
operator|=
literal|"Camel Routes"
expr_stmt|;
block|}
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|list
init|=
name|map
operator|.
name|get
argument_list|(
name|group
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|group
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
DECL|method|isMulticastNode (ProcessorDefinition node)
specifier|protected
name|boolean
name|isMulticastNode
parameter_list|(
name|ProcessorDefinition
name|node
parameter_list|)
block|{
return|return
name|node
operator|instanceof
name|MulticastDefinition
operator|||
name|node
operator|instanceof
name|ChoiceDefinition
return|;
block|}
comment|/**      * Is the given node a pipeline      */
DECL|method|isPipeline (ProcessorDefinition node)
specifier|protected
name|boolean
name|isPipeline
parameter_list|(
name|ProcessorDefinition
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
return|return
literal|false
return|;
block|}
if|if
condition|(
name|node
operator|instanceof
name|PipelineDefinition
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
name|ToDefinition
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
DECL|method|getImagePrefix ()
specifier|public
name|String
name|getImagePrefix
parameter_list|()
block|{
return|return
name|imagePrefix
return|;
block|}
DECL|method|setImagePrefix (String imagePrefix)
specifier|public
name|void
name|setImagePrefix
parameter_list|(
name|String
name|imagePrefix
parameter_list|)
block|{
name|this
operator|.
name|imagePrefix
operator|=
name|imagePrefix
expr_stmt|;
block|}
block|}
end_class

end_unit

