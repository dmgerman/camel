begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|io
operator|.
name|FileWriter
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
name|ArrayList
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
name|CamelContext
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
name|ChoiceType
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
name|ToType
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
name|ExpressionType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|GraphGeneratorSupport
specifier|public
specifier|abstract
class|class
name|GraphGeneratorSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RouteDotGenerator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dir
specifier|protected
name|String
name|dir
decl_stmt|;
DECL|field|imagePrefix
specifier|private
name|String
name|imagePrefix
init|=
literal|"http://www.enterpriseintegrationpatterns.com/img/"
decl_stmt|;
DECL|field|nodeMap
specifier|private
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
DECL|field|makeParentDirs
specifier|private
name|boolean
name|makeParentDirs
init|=
literal|true
decl_stmt|;
DECL|field|clusterCounter
specifier|protected
name|int
name|clusterCounter
decl_stmt|;
DECL|field|routeGroupMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteType
argument_list|>
argument_list|>
name|routeGroupMap
decl_stmt|;
DECL|field|extension
specifier|protected
name|String
name|extension
decl_stmt|;
DECL|method|GraphGeneratorSupport (String dir, String extension)
specifier|protected
name|GraphGeneratorSupport
parameter_list|(
name|String
name|dir
parameter_list|,
name|String
name|extension
parameter_list|)
block|{
name|this
operator|.
name|dir
operator|=
name|dir
expr_stmt|;
name|this
operator|.
name|extension
operator|=
name|extension
expr_stmt|;
block|}
DECL|method|getDir ()
specifier|public
name|String
name|getDir
parameter_list|()
block|{
return|return
name|dir
return|;
block|}
comment|/**      * Sets the destination directory in which to create the diagrams      */
DECL|method|setDir (String dir)
specifier|public
name|void
name|setDir
parameter_list|(
name|String
name|dir
parameter_list|)
block|{
name|this
operator|.
name|dir
operator|=
name|dir
expr_stmt|;
block|}
DECL|method|drawRoutes (CamelContext context)
specifier|public
name|void
name|drawRoutes
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|parent
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|)
decl_stmt|;
if|if
condition|(
name|makeParentDirs
condition|)
block|{
name|parent
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|RouteType
argument_list|>
name|routes
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
name|routeGroupMap
operator|=
name|createRouteGroupMap
argument_list|(
name|routes
argument_list|)
expr_stmt|;
comment|// generate the global file
name|generateFile
argument_list|(
name|parent
argument_list|,
literal|"routes"
operator|+
name|extension
argument_list|,
name|routeGroupMap
argument_list|)
expr_stmt|;
if|if
condition|(
name|routeGroupMap
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
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
name|routeGroupMap
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
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteType
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|group
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|group
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// generate the file containing just the routes in this group
name|generateFile
argument_list|(
name|parent
argument_list|,
name|group
operator|+
name|extension
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|generateFile (File parent, String fileName, Map<String, List<RouteType>> map)
specifier|private
name|void
name|generateFile
parameter_list|(
name|File
name|parent
parameter_list|,
name|String
name|fileName
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
throws|throws
name|IOException
block|{
name|nodeMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|clusterCounter
operator|=
literal|0
expr_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
operator|new
name|File
argument_list|(
name|parent
argument_list|,
name|fileName
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|generateFile
argument_list|(
name|writer
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|generateFile (PrintWriter writer, Map<String, List<RouteType>> map)
specifier|protected
specifier|abstract
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
function_decl|;
DECL|method|isMulticastNode (ProcessorType node)
specifier|protected
name|boolean
name|isMulticastNode
parameter_list|(
name|ProcessorType
name|node
parameter_list|)
block|{
return|return
name|node
operator|instanceof
name|MulticastType
operator|||
name|node
operator|instanceof
name|ChoiceType
return|;
block|}
DECL|method|getLabel (List<ExpressionType> expressions)
specifier|protected
name|String
name|getLabel
parameter_list|(
name|List
argument_list|<
name|ExpressionType
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
name|ExpressionType
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
DECL|method|getLabel (ExpressionType expression)
specifier|protected
name|String
name|getLabel
parameter_list|(
name|ExpressionType
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
name|FromType
condition|)
block|{
name|FromType
name|fromType
init|=
operator|(
name|FromType
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
name|ToType
condition|)
block|{
name|ToType
name|toType
init|=
operator|(
name|ToType
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
name|nodeMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
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
DECL|method|createRouteGroupMap (List<RouteType> routes)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteType
argument_list|>
argument_list|>
name|createRouteGroupMap
parameter_list|(
name|List
argument_list|<
name|RouteType
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
name|RouteType
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
name|RouteType
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteType
name|route
range|:
name|routes
control|)
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
name|RouteType
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
name|RouteType
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
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

