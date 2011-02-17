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
name|File
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|GraphGeneratorSupport
specifier|public
specifier|abstract
class|class
name|GraphGeneratorSupport
extends|extends
name|GraphSupport
block|{
DECL|field|dir
specifier|protected
name|String
name|dir
decl_stmt|;
DECL|field|clusterCounter
specifier|protected
name|int
name|clusterCounter
decl_stmt|;
DECL|field|extension
specifier|protected
name|String
name|extension
decl_stmt|;
DECL|field|makeParentDirs
specifier|private
specifier|final
name|boolean
name|makeParentDirs
init|=
literal|true
decl_stmt|;
DECL|field|routeGroupMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|>
name|routeGroupMap
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
DECL|method|getRoutesText (CamelContext context)
specifier|public
name|String
name|getRoutesText
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
comment|// used by web console
name|List
argument_list|<
name|RouteDefinition
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
return|return
name|createRouteMapText
argument_list|()
return|;
block|}
DECL|method|createRouteMapText ()
specifier|private
name|String
name|createRouteMapText
parameter_list|()
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|generateFile
argument_list|(
name|writer
argument_list|,
name|routeGroupMap
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
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
name|RouteDefinition
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
name|RouteDefinition
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
name|RouteDefinition
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
DECL|method|generateFile (File parent, String fileName, Map<String, List<RouteDefinition>> map)
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
name|RouteDefinition
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
DECL|method|generateFile (PrintWriter writer, Map<String, List<RouteDefinition>> map)
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
name|RouteDefinition
argument_list|>
argument_list|>
name|map
parameter_list|)
function_decl|;
block|}
end_class

end_unit

