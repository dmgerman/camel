begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|Processor
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
name|builder
operator|.
name|ExpressionClause
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
name|processor
operator|.
name|CatchProcessor
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
name|processor
operator|.
name|TryProcessor
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
name|RouteContext
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
name|ProcessorDefinitionHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;try/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"doTry"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|TryDefinition
specifier|public
class|class
name|TryDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|TryDefinition
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|catchClauses
specifier|private
name|List
argument_list|<
name|CatchDefinition
argument_list|>
name|catchClauses
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|finallyClause
specifier|private
name|FinallyDefinition
name|finallyClause
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|initialized
specifier|private
name|boolean
name|initialized
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|outputsWithoutCatches
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputsWithoutCatches
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DoTry["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"doTry"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|tryProcessor
init|=
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|,
name|getOutputsWithoutCatches
argument_list|()
argument_list|)
decl_stmt|;
name|Processor
name|finallyProcessor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|finallyClause
operator|!=
literal|null
condition|)
block|{
name|finallyProcessor
operator|=
name|finallyClause
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|CatchProcessor
argument_list|>
name|catchProcessors
init|=
operator|new
name|ArrayList
argument_list|<
name|CatchProcessor
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|catchClauses
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|CatchDefinition
name|catchClause
range|:
name|catchClauses
control|)
block|{
name|catchProcessors
operator|.
name|add
argument_list|(
name|catchClause
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|TryProcessor
argument_list|(
name|tryProcessor
argument_list|,
name|catchProcessors
argument_list|,
name|finallyProcessor
argument_list|)
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Handles the given exception(s)      *      * @param exceptionType  the exception(s)      * @return the try builder      */
DECL|method|doCatch (Class... exceptionType)
specifier|public
name|TryDefinition
name|doCatch
parameter_list|(
name|Class
modifier|...
name|exceptionType
parameter_list|)
block|{
name|popBlock
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|exceptionType
argument_list|)
decl_stmt|;
name|CatchDefinition
name|answer
init|=
operator|new
name|CatchDefinition
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|addOutput
argument_list|(
name|answer
argument_list|)
expr_stmt|;
name|pushBlock
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets an additional predicate that should be true before the onCatch is triggered.      *<p/>      * To be used for fine grained controlling whether a thrown exception should be intercepted      * by this exception type or not.      *      * @param predicate  predicate that determines true or false      * @return the builder      */
DECL|method|onWhen (Predicate predicate)
specifier|public
name|TryDefinition
name|onWhen
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
comment|// set the onWhen predicate on all the catch definitions
name|Iterator
argument_list|<
name|CatchDefinition
argument_list|>
name|it
init|=
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
argument_list|(
name|getOutputs
argument_list|()
argument_list|,
name|CatchDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CatchDefinition
name|doCatch
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|doCatch
operator|.
name|setOnWhen
argument_list|(
operator|new
name|WhenDefinition
argument_list|(
name|predicate
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Creates an expression to configure an additional predicate that should be true before the      * onCatch is triggered.      *<p/>      * To be used for fine grained controlling whether a thrown exception should be intercepted      * by this exception type or not.      *      * @return the expression clause to configure      */
DECL|method|onWhen ()
specifier|public
name|ExpressionClause
argument_list|<
name|TryDefinition
argument_list|>
name|onWhen
parameter_list|()
block|{
name|WhenDefinition
name|answer
init|=
operator|new
name|WhenDefinition
argument_list|()
decl_stmt|;
comment|// set the onWhen definition on all the catch definitions
name|Iterator
argument_list|<
name|CatchDefinition
argument_list|>
name|it
init|=
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
argument_list|(
name|getOutputs
argument_list|()
argument_list|,
name|CatchDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CatchDefinition
name|doCatch
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|doCatch
operator|.
name|setOnWhen
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
comment|// return a expression clause as builder to set the predicate on the onWhen definition
name|ExpressionClause
argument_list|<
name|TryDefinition
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|TryDefinition
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExpression
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
comment|/**      * The finally block for a given handle      *      * @return  the try builder      */
DECL|method|doFinally ()
specifier|public
name|TryDefinition
name|doFinally
parameter_list|()
block|{
name|popBlock
argument_list|()
expr_stmt|;
name|FinallyDefinition
name|answer
init|=
operator|new
name|FinallyDefinition
argument_list|()
decl_stmt|;
name|addOutput
argument_list|(
name|answer
argument_list|)
expr_stmt|;
name|pushBlock
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|end ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
extends|extends
name|ProcessorDefinition
argument_list|>
name|end
parameter_list|()
block|{
name|popBlock
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|end
argument_list|()
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getCatchClauses ()
specifier|public
name|List
argument_list|<
name|CatchDefinition
argument_list|>
name|getCatchClauses
parameter_list|()
block|{
if|if
condition|(
name|catchClauses
operator|==
literal|null
condition|)
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
block|}
return|return
name|catchClauses
return|;
block|}
DECL|method|getFinallyClause ()
specifier|public
name|FinallyDefinition
name|getFinallyClause
parameter_list|()
block|{
if|if
condition|(
name|finallyClause
operator|==
literal|null
condition|)
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
block|}
return|return
name|finallyClause
return|;
block|}
DECL|method|getOutputsWithoutCatches ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getOutputsWithoutCatches
parameter_list|()
block|{
if|if
condition|(
name|outputsWithoutCatches
operator|==
literal|null
condition|)
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
block|}
return|return
name|outputsWithoutCatches
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
parameter_list|)
block|{
name|initialized
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|setOutputs
argument_list|(
name|outputs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addOutput (ProcessorDefinition output)
specifier|public
name|void
name|addOutput
parameter_list|(
name|ProcessorDefinition
name|output
parameter_list|)
block|{
name|initialized
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|addOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks whether or not this object has been initialized      */
DECL|method|checkInitialized ()
specifier|protected
name|void
name|checkInitialized
parameter_list|()
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
name|initialized
operator|=
literal|true
expr_stmt|;
name|outputsWithoutCatches
operator|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
expr_stmt|;
name|catchClauses
operator|=
operator|new
name|ArrayList
argument_list|<
name|CatchDefinition
argument_list|>
argument_list|()
expr_stmt|;
name|finallyClause
operator|=
literal|null
expr_stmt|;
for|for
control|(
name|ProcessorDefinition
name|output
range|:
name|outputs
control|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|CatchDefinition
condition|)
block|{
name|catchClauses
operator|.
name|add
argument_list|(
operator|(
name|CatchDefinition
operator|)
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|output
operator|instanceof
name|FinallyDefinition
condition|)
block|{
if|if
condition|(
name|finallyClause
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple finally clauses added: "
operator|+
name|finallyClause
operator|+
literal|" and "
operator|+
name|output
argument_list|)
throw|;
block|}
else|else
block|{
name|finallyClause
operator|=
operator|(
name|FinallyDefinition
operator|)
name|output
expr_stmt|;
block|}
block|}
else|else
block|{
name|outputsWithoutCatches
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

