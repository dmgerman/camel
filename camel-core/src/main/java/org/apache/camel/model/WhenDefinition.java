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
name|Label
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

begin_comment
comment|/**  * Triggers a route when an expression evaluates to<tt>true</tt>  *   * @version   */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"eip,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"when"
argument_list|)
DECL|class|WhenDefinition
specifier|public
class|class
name|WhenDefinition
extends|extends
name|ExpressionNode
block|{
DECL|method|WhenDefinition ()
specifier|public
name|WhenDefinition
parameter_list|()
block|{     }
DECL|method|WhenDefinition (Predicate predicate)
specifier|public
name|WhenDefinition
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|super
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|WhenDefinition (ExpressionDefinition expression)
specifier|public
name|WhenDefinition
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
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
literal|"When["
operator|+
name|description
argument_list|()
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|description ()
specifier|protected
name|String
name|description
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|getExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|language
init|=
name|getExpression
argument_list|()
operator|.
name|getLanguage
argument_list|()
decl_stmt|;
if|if
condition|(
name|language
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|language
argument_list|)
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|getExpression
argument_list|()
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|language
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
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
return|return
literal|"when["
operator|+
name|description
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|FilterProcessor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createFilterProcessor
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|endParent ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|endParent
parameter_list|()
block|{
comment|// when using when in the DSL we don't want to end back to this when, but instead
comment|// the parent of this, so return the parent
return|return
name|this
operator|.
name|getParent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

