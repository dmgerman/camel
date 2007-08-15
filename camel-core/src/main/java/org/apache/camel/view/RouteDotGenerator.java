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
name|Predicate
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
name|*
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
name|util
operator|.
name|ObjectHelper
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
name|isNullOrBlank
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

begin_comment
comment|/**  * A<a href="http://www.graphviz.org/">DOT</a> file creator plugin which  * creates a DOT file showing the current routes  *  * @version $Revision: 523881 $  */
end_comment

begin_class
DECL|class|RouteDotGenerator
specifier|public
class|class
name|RouteDotGenerator
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
DECL|field|file
specifier|private
name|String
name|file
init|=
literal|"CamelRoutes.dot"
decl_stmt|;
DECL|field|imagePrefix
specifier|private
name|String
name|imagePrefix
init|=
literal|"http://www.enterpriseintegrationpatterns.com/img/"
decl_stmt|;
DECL|field|idMap
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|idMap
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getFile ()
specifier|public
name|String
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
comment|/**      * Sets the destination file name to create the destination diagram      */
DECL|method|setFile (String file)
specifier|public
name|void
name|setFile
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
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
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|generateFile
argument_list|(
name|writer
argument_list|,
name|context
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
DECL|method|generateFile (PrintWriter writer, CamelContext context)
specifier|protected
name|void
name|generateFile
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"digraph \"Camel Routes\" {"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
comment|/*writer.println("label=\"Camel Context: " + context + "\"];");         writer.println();         */
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
name|context
operator|.
name|getRouteDefinitions
argument_list|()
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
DECL|method|printRoutes (PrintWriter writer, List<RouteType> routes)
specifier|protected
name|void
name|printRoutes
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|List
argument_list|<
name|RouteType
argument_list|>
name|routes
parameter_list|)
block|{
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
block|}
DECL|method|printRoute (PrintWriter writer, RouteType route, FromType input)
specifier|protected
name|void
name|printRoute
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|RouteType
name|route
parameter_list|,
name|FromType
name|input
parameter_list|)
block|{
name|String
name|ref
init|=
name|input
operator|.
name|getRef
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNullOrBlank
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|ref
operator|=
name|input
operator|.
name|getUri
argument_list|()
expr_stmt|;
block|}
name|String
name|fromID
init|=
name|getID
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|fromID
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
name|ref
operator|+
literal|"\""
argument_list|)
expr_stmt|;
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
comment|// TODO we should add a transactional client / event driven consumer / polling client
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
init|=
name|route
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorType
name|output
range|:
name|outputs
control|)
block|{
name|printNode
argument_list|(
name|writer
argument_list|,
name|fromID
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|printNode (PrintWriter writer, String fromID, ProcessorType node)
specifier|protected
name|String
name|printNode
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|fromID
parameter_list|,
name|ProcessorType
name|node
parameter_list|)
block|{
name|String
name|toID
init|=
name|getID
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
name|writer
operator|.
name|print
argument_list|(
name|toID
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|" ["
argument_list|)
expr_stmt|;
name|printNodeAttributes
argument_list|(
name|writer
argument_list|,
name|node
argument_list|,
name|fromID
argument_list|)
expr_stmt|;
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
name|writer
operator|.
name|print
argument_list|(
name|fromID
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
name|toID
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|" ["
argument_list|)
expr_stmt|;
comment|// TODO customize the line!
name|writer
operator|.
name|println
argument_list|(
literal|"];"
argument_list|)
expr_stmt|;
comment|// now lets write any children
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
for|for
control|(
name|ProcessorType
name|output
range|:
name|outputs
control|)
block|{
name|printNode
argument_list|(
name|writer
argument_list|,
name|toID
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
return|return
name|toID
return|;
block|}
DECL|class|NodeData
specifier|protected
class|class
name|NodeData
block|{
DECL|field|image
specifier|public
name|String
name|image
decl_stmt|;
DECL|field|label
specifier|public
name|String
name|label
decl_stmt|;
block|}
DECL|method|printNodeAttributes (PrintWriter writer, ProcessorType node, String id)
specifier|protected
name|void
name|printNodeAttributes
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|ProcessorType
name|node
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|NodeData
name|nodeData
init|=
operator|new
name|NodeData
argument_list|()
decl_stmt|;
name|configureNodeData
argument_list|(
name|node
argument_list|,
name|nodeData
argument_list|)
expr_stmt|;
name|String
name|label
init|=
name|nodeData
operator|.
name|label
decl_stmt|;
if|if
condition|(
name|label
operator|==
literal|null
condition|)
block|{
name|label
operator|=
name|node
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
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
name|String
name|image
init|=
name|nodeData
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
literal|"shape = custom"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureNodeData (ProcessorType node, NodeData nodeData)
specifier|protected
name|void
name|configureNodeData
parameter_list|(
name|ProcessorType
name|node
parameter_list|,
name|NodeData
name|nodeData
parameter_list|)
block|{
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
name|String
name|ref
init|=
name|toType
operator|.
name|getRef
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNullOrBlank
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|ref
operator|=
name|toType
operator|.
name|getUri
argument_list|()
expr_stmt|;
block|}
name|nodeData
operator|.
name|label
operator|=
name|ref
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|FilterType
condition|)
block|{
name|FilterType
name|filterType
init|=
operator|(
name|FilterType
operator|)
name|node
decl_stmt|;
name|nodeData
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"MessageFilterIcon.gif"
expr_stmt|;
name|nodeData
operator|.
name|label
operator|=
name|getLabel
argument_list|(
name|filterType
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|ChoiceType
condition|)
block|{
name|ChoiceType
name|choiceType
init|=
operator|(
name|ChoiceType
operator|)
name|node
decl_stmt|;
name|nodeData
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"ContentBasedRouterIcon.gif"
expr_stmt|;
name|CollectionStringBuffer
name|buffer
init|=
operator|new
name|CollectionStringBuffer
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WhenType
argument_list|>
name|list
init|=
name|choiceType
operator|.
name|getWhenClauses
argument_list|()
decl_stmt|;
for|for
control|(
name|WhenType
name|whenType
range|:
name|list
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|getLabel
argument_list|(
name|whenType
operator|.
name|getExpression
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|nodeData
operator|.
name|label
operator|=
name|buffer
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|RecipientListType
condition|)
block|{
name|RecipientListType
name|recipientListType
init|=
operator|(
name|RecipientListType
operator|)
name|node
decl_stmt|;
name|nodeData
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"RecipientListIcon.gif"
expr_stmt|;
name|nodeData
operator|.
name|label
operator|=
name|getLabel
argument_list|(
name|recipientListType
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|SplitterType
condition|)
block|{
name|SplitterType
name|splitterType
init|=
operator|(
name|SplitterType
operator|)
name|node
decl_stmt|;
name|nodeData
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"SplitterIcon.gif"
expr_stmt|;
name|nodeData
operator|.
name|label
operator|=
name|getLabel
argument_list|(
name|splitterType
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|AggregatorType
condition|)
block|{
name|AggregatorType
name|aggregatorType
init|=
operator|(
name|AggregatorType
operator|)
name|node
decl_stmt|;
name|nodeData
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"AggregatorIcon.gif"
expr_stmt|;
name|nodeData
operator|.
name|label
operator|=
name|getLabel
argument_list|(
name|aggregatorType
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|ResequencerType
condition|)
block|{
name|ResequencerType
name|resequencerType
init|=
operator|(
name|ResequencerType
operator|)
name|node
decl_stmt|;
name|nodeData
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"ResequencerIcon.gif"
expr_stmt|;
name|nodeData
operator|.
name|label
operator|=
name|getLabel
argument_list|(
name|resequencerType
operator|.
name|getExpressions
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|String
name|language
init|=
name|expression
operator|.
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|language
argument_list|)
condition|)
block|{
name|Predicate
name|predicate
init|=
name|expression
operator|.
name|getPredicate
argument_list|()
decl_stmt|;
if|if
condition|(
name|predicate
operator|!=
literal|null
condition|)
block|{
return|return
name|predicate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
else|else
block|{
return|return
name|language
return|;
block|}
block|}
return|return
literal|""
return|;
block|}
DECL|method|getID (Object node)
specifier|protected
name|String
name|getID
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|String
name|answer
init|=
name|idMap
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
literal|"node"
operator|+
operator|(
name|idMap
operator|.
name|size
argument_list|()
operator|+
literal|1
operator|)
expr_stmt|;
name|idMap
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

