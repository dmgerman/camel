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
name|Collections
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
comment|/**  * Represents an XML&lt;choice/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
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
DECL|class|ChoiceType
specifier|public
class|class
name|ChoiceType
extends|extends
name|ProcessorType
argument_list|<
name|ChoiceType
argument_list|>
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
name|ChoiceType
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|whenClauses
specifier|private
name|List
argument_list|<
name|WhenType
argument_list|>
name|whenClauses
init|=
operator|new
name|ArrayList
argument_list|<
name|WhenType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|otherwise
specifier|private
name|OtherwiseType
name|otherwise
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|getOtherwise
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Choice["
operator|+
name|getWhenClauses
argument_list|()
operator|+
literal|" "
operator|+
name|getOtherwise
argument_list|()
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"Choice["
operator|+
name|getWhenClauses
argument_list|()
operator|+
literal|"]"
return|;
block|}
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
literal|"choice"
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
argument_list|<
name|FilterProcessor
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|WhenType
name|whenClaus
range|:
name|whenClauses
control|)
block|{
name|filters
operator|.
name|add
argument_list|(
name|whenClaus
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
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
name|otherwise
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No otherwise clause was specified for a choice block, any unmatched exchanges will be dropped"
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
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the predicate for the when node      *      * @param predicate  the predicate      * @return the builder      */
DECL|method|when (Predicate predicate)
specifier|public
name|ChoiceType
name|when
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|getWhenClauses
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WhenType
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
DECL|method|when ()
specifier|public
name|ExpressionClause
argument_list|<
name|ChoiceType
argument_list|>
name|when
parameter_list|()
block|{
name|WhenType
name|when
init|=
operator|new
name|WhenType
argument_list|()
decl_stmt|;
name|getWhenClauses
argument_list|()
operator|.
name|add
argument_list|(
name|when
argument_list|)
expr_stmt|;
name|ExpressionClause
argument_list|<
name|ChoiceType
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|ChoiceType
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|when
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
comment|/**      * Sets the otherwise node      *       * @return the builder      */
DECL|method|otherwise ()
specifier|public
name|ChoiceType
name|otherwise
parameter_list|()
block|{
name|OtherwiseType
name|answer
init|=
operator|new
name|OtherwiseType
argument_list|()
decl_stmt|;
name|setOtherwise
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
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
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WhenType
argument_list|>
name|list
init|=
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
name|whenType
operator|.
name|getLabel
argument_list|()
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
DECL|method|getWhenClauses ()
specifier|public
name|List
argument_list|<
name|WhenType
argument_list|>
name|getWhenClauses
parameter_list|()
block|{
return|return
name|whenClauses
return|;
block|}
DECL|method|setWhenClauses (List<WhenType> whenClauses)
specifier|public
name|void
name|setWhenClauses
parameter_list|(
name|List
argument_list|<
name|WhenType
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
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
return|return
name|otherwise
operator|.
name|getOutputs
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|whenClauses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
else|else
block|{
name|WhenType
name|when
init|=
name|whenClauses
operator|.
name|get
argument_list|(
name|whenClauses
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|when
operator|.
name|getOutputs
argument_list|()
return|;
block|}
block|}
DECL|method|getOtherwise ()
specifier|public
name|OtherwiseType
name|getOtherwise
parameter_list|()
block|{
return|return
name|otherwise
return|;
block|}
DECL|method|setOtherwise (OtherwiseType otherwise)
specifier|public
name|void
name|setOtherwise
parameter_list|(
name|OtherwiseType
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
block|}
end_class

end_unit

