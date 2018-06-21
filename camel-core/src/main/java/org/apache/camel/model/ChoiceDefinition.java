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
name|AbstractList
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
name|XmlElement
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
name|XmlElementRef
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
name|processor
operator|.
name|ChoiceProcessor
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
name|FilterProcessor
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
name|AsPredicate
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
name|Metadata
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

begin_comment
comment|/**  * Routes messages based on a series of predicates  *  * @version  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"choice"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ChoiceDefinition
specifier|public
class|class
name|ChoiceDefinition
extends|extends
name|ProcessorDefinition
argument_list|<
name|ChoiceDefinition
argument_list|>
block|{
annotation|@
name|XmlElementRef
annotation|@
name|AsPredicate
DECL|field|whenClauses
specifier|private
name|List
argument_list|<
name|WhenDefinition
argument_list|>
name|whenClauses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
DECL|field|otherwise
specifier|private
name|OtherwiseDefinition
name|otherwise
decl_stmt|;
DECL|field|onlyWhenOrOtherwise
specifier|private
specifier|transient
name|boolean
name|onlyWhenOrOtherwise
init|=
literal|true
decl_stmt|;
DECL|method|ChoiceDefinition ()
specifier|public
name|ChoiceDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
comment|// wrap the outputs into a list where we can on the inside control the when/otherwise
comment|// but make it appear as a list on the outside
return|return
operator|new
name|AbstractList
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|index
operator|<
name|whenClauses
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|whenClauses
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
if|if
condition|(
name|index
operator|==
name|whenClauses
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|otherwise
return|;
block|}
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index "
operator|+
name|index
operator|+
literal|" is out of bounds with size "
operator|+
name|size
argument_list|()
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|add
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|def
parameter_list|)
block|{
if|if
condition|(
name|def
operator|instanceof
name|WhenDefinition
condition|)
block|{
return|return
name|whenClauses
operator|.
name|add
argument_list|(
operator|(
name|WhenDefinition
operator|)
name|def
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|def
operator|instanceof
name|OtherwiseDefinition
condition|)
block|{
name|otherwise
operator|=
operator|(
name|OtherwiseDefinition
operator|)
name|def
expr_stmt|;
return|return
literal|true
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected either a WhenDefinition or OtherwiseDefinition but was "
operator|+
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|def
argument_list|)
argument_list|)
throw|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|whenClauses
operator|.
name|size
argument_list|()
operator|+
operator|(
name|otherwise
operator|==
literal|null
condition|?
literal|0
else|:
literal|1
operator|)
return|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|whenClauses
operator|.
name|clear
argument_list|()
expr_stmt|;
name|otherwise
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|element
parameter_list|)
block|{
if|if
condition|(
name|index
operator|<
name|whenClauses
operator|.
name|size
argument_list|()
condition|)
block|{
if|if
condition|(
name|element
operator|instanceof
name|WhenDefinition
condition|)
block|{
return|return
name|whenClauses
operator|.
name|set
argument_list|(
name|index
argument_list|,
operator|(
name|WhenDefinition
operator|)
name|element
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected WhenDefinition but was "
operator|+
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|element
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|index
operator|==
name|whenClauses
operator|.
name|size
argument_list|()
condition|)
block|{
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|old
init|=
name|otherwise
decl_stmt|;
name|otherwise
operator|=
operator|(
name|OtherwiseDefinition
operator|)
name|element
expr_stmt|;
return|return
name|old
return|;
block|}
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index "
operator|+
name|index
operator|+
literal|" is out of bounds with size "
operator|+
name|size
argument_list|()
argument_list|)
throw|;
block|}
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|remove
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|index
operator|<
name|whenClauses
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|whenClauses
operator|.
name|remove
argument_list|(
name|index
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|index
operator|==
name|whenClauses
operator|.
name|size
argument_list|()
condition|)
block|{
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|old
init|=
name|otherwise
decl_stmt|;
name|otherwise
operator|=
literal|null
expr_stmt|;
return|return
name|old
return|;
block|}
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index "
operator|+
name|index
operator|+
literal|" is out of bounds with size "
operator|+
name|size
argument_list|()
argument_list|)
throw|;
block|}
block|}
empty_stmt|;
block|}
annotation|@
name|Override
DECL|method|isOutputSupported ()
specifier|public
name|boolean
name|isOutputSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Choice["
operator|+
name|getWhenClauses
argument_list|()
operator|+
operator|(
name|getOtherwise
argument_list|()
operator|!=
literal|null
condition|?
literal|" "
operator|+
name|getOtherwise
argument_list|()
else|:
literal|""
operator|)
operator|+
literal|"]"
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
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|filters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|WhenDefinition
name|whenClause
range|:
name|whenClauses
control|)
block|{
comment|// also resolve properties and constant fields on embedded expressions in the when clauses
name|ExpressionNode
name|exp
init|=
name|whenClause
decl_stmt|;
name|ExpressionDefinition
name|expressionDefinition
init|=
name|exp
operator|.
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|expressionDefinition
operator|!=
literal|null
condition|)
block|{
comment|// resolve properties before we create the processor
name|ProcessorDefinitionHelper
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|expressionDefinition
argument_list|)
expr_stmt|;
comment|// resolve constant fields (eg Exchange.FILE_NAME)
name|ProcessorDefinitionHelper
operator|.
name|resolveKnownConstantFields
argument_list|(
name|expressionDefinition
argument_list|)
expr_stmt|;
block|}
name|FilterProcessor
name|filter
init|=
operator|(
name|FilterProcessor
operator|)
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|whenClause
argument_list|)
decl_stmt|;
name|filters
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|Processor
name|otherwiseProcessor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
name|otherwiseProcessor
operator|=
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|otherwise
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ChoiceProcessor
argument_list|(
name|filters
argument_list|,
name|otherwiseProcessor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addOutput (ProcessorDefinition<?> output)
specifier|public
name|void
name|addOutput
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
parameter_list|)
block|{
if|if
condition|(
name|onlyWhenOrOtherwise
condition|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|WhenDefinition
operator|||
name|output
operator|instanceof
name|OtherwiseDefinition
condition|)
block|{
comment|// okay we are adding a when or otherwise so allow any kind of output after this again
name|onlyWhenOrOtherwise
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A new choice clause should start with a when() or otherwise(). "
operator|+
literal|"If you intend to end the entire choice and are using endChoice() then use end() instead."
argument_list|)
throw|;
block|}
block|}
name|super
operator|.
name|addOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|end ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|end
parameter_list|()
block|{
comment|// we end a block so only when or otherwise is supported
name|onlyWhenOrOtherwise
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|end
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|endChoice ()
specifier|public
name|ChoiceDefinition
name|endChoice
parameter_list|()
block|{
comment|// we end a block so only when or otherwise is supported
name|onlyWhenOrOtherwise
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|endChoice
argument_list|()
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the predicate for the when node      *      * @param predicate the predicate      * @return the builder      */
DECL|method|when (@sPredicate Predicate predicate)
specifier|public
name|ChoiceDefinition
name|when
parameter_list|(
annotation|@
name|AsPredicate
name|Predicate
name|predicate
parameter_list|)
block|{
name|addClause
argument_list|(
operator|new
name|WhenDefinition
argument_list|(
name|predicate
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates an expression for the when node      *      * @return expression to be used as builder to configure the when node      */
annotation|@
name|AsPredicate
DECL|method|when ()
specifier|public
name|ExpressionClause
argument_list|<
name|ChoiceDefinition
argument_list|>
name|when
parameter_list|()
block|{
name|ExpressionClause
argument_list|<
name|ChoiceDefinition
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|addClause
argument_list|(
operator|new
name|WhenDefinition
argument_list|(
name|clause
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
DECL|method|addClause (ProcessorDefinition<?> when)
specifier|private
name|void
name|addClause
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|when
parameter_list|)
block|{
name|onlyWhenOrOtherwise
operator|=
literal|true
expr_stmt|;
name|popBlock
argument_list|()
expr_stmt|;
name|addOutput
argument_list|(
name|when
argument_list|)
expr_stmt|;
name|pushBlock
argument_list|(
name|when
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the otherwise node      *      * @return the builder      */
DECL|method|otherwise ()
specifier|public
name|ChoiceDefinition
name|otherwise
parameter_list|()
block|{
name|OtherwiseDefinition
name|answer
init|=
operator|new
name|OtherwiseDefinition
argument_list|()
decl_stmt|;
name|addClause
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
DECL|method|setId (String value)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// when setting id, we should set it on the fine grained element, if possible
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
name|otherwise
operator|.
name|setId
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|getWhenClauses
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|int
name|size
init|=
name|getWhenClauses
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|getWhenClauses
argument_list|()
operator|.
name|get
argument_list|(
name|size
operator|-
literal|1
argument_list|)
operator|.
name|setId
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setId
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"choice"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
name|CollectionStringBuffer
name|buffer
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|"choice["
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WhenDefinition
argument_list|>
name|list
init|=
name|getWhenClauses
argument_list|()
decl_stmt|;
for|for
control|(
name|WhenDefinition
name|whenType
range|:
name|list
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|whenType
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getWhenClauses ()
specifier|public
name|List
argument_list|<
name|WhenDefinition
argument_list|>
name|getWhenClauses
parameter_list|()
block|{
return|return
name|whenClauses
return|;
block|}
comment|/**      * Sets the when clauses      */
DECL|method|setWhenClauses (List<WhenDefinition> whenClauses)
specifier|public
name|void
name|setWhenClauses
parameter_list|(
name|List
argument_list|<
name|WhenDefinition
argument_list|>
name|whenClauses
parameter_list|)
block|{
name|this
operator|.
name|whenClauses
operator|=
name|whenClauses
expr_stmt|;
block|}
DECL|method|getOtherwise ()
specifier|public
name|OtherwiseDefinition
name|getOtherwise
parameter_list|()
block|{
return|return
name|otherwise
return|;
block|}
DECL|method|setOtherwise (OtherwiseDefinition otherwise)
specifier|public
name|void
name|setOtherwise
parameter_list|(
name|OtherwiseDefinition
name|otherwise
parameter_list|)
block|{
name|this
operator|.
name|otherwise
operator|=
name|otherwise
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureChild (ProcessorDefinition<?> output)
specifier|public
name|void
name|configureChild
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
parameter_list|)
block|{
if|if
condition|(
name|whenClauses
operator|==
literal|null
operator|||
name|whenClauses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|WhenDefinition
name|when
range|:
name|whenClauses
control|)
block|{
if|if
condition|(
name|when
operator|.
name|getExpression
argument_list|()
operator|instanceof
name|ExpressionClause
condition|)
block|{
name|ExpressionClause
argument_list|<
name|?
argument_list|>
name|clause
init|=
operator|(
name|ExpressionClause
argument_list|<
name|?
argument_list|>
operator|)
name|when
operator|.
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|clause
operator|.
name|getExpressionType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// if using the Java DSL then the expression may have been set using the
comment|// ExpressionClause which is a fancy builder to define expressions and predicates
comment|// using fluent builders in the DSL. However we need afterwards a callback to
comment|// reset the expression to the expression type the ExpressionClause did build for us
name|when
operator|.
name|setExpression
argument_list|(
name|clause
operator|.
name|getExpressionType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

