begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|language
package|;
end_package

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
name|XmlAttribute
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
name|XmlID
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
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
name|XmlValue
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
name|adapters
operator|.
name|CollapsedStringAdapter
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
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
name|Exchange
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
name|Expression
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
name|impl
operator|.
name|DefaultRouteContext
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
name|Language
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
name|IntrospectionSupport
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
comment|/**  * A useful base class for an expression  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"expressionType"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ExpressionType
specifier|public
class|class
name|ExpressionType
implements|implements
name|Expression
argument_list|<
name|Exchange
argument_list|>
implements|,
name|Predicate
argument_list|<
name|Exchange
argument_list|>
block|{
annotation|@
name|XmlAttribute
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|CollapsedStringAdapter
operator|.
name|class
argument_list|)
annotation|@
name|XmlID
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|XmlValue
DECL|field|expression
specifier|private
name|String
name|expression
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|predicate
specifier|private
name|Predicate
name|predicate
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|expressionValue
specifier|private
name|Expression
name|expressionValue
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|expressionType
specifier|private
name|ExpressionType
name|expressionType
decl_stmt|;
DECL|method|ExpressionType ()
specifier|public
name|ExpressionType
parameter_list|()
block|{     }
DECL|method|ExpressionType (String expression)
specifier|public
name|ExpressionType
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|ExpressionType (Predicate predicate)
specifier|public
name|ExpressionType
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|predicate
expr_stmt|;
block|}
DECL|method|ExpressionType (Expression expression)
specifier|public
name|ExpressionType
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expressionValue
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getLabel (List<ExpressionType> expressions)
specifier|public
specifier|static
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
name|expression
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
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
name|getLanguage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|getLanguage
argument_list|()
operator|+
literal|"{"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|getPredicate
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getExpressionValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|getExpressionValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getLanguage
argument_list|()
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
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|expressionValue
operator|==
literal|null
condition|)
block|{
name|RouteContext
name|routeContext
init|=
operator|new
name|DefaultRouteContext
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|expressionValue
operator|=
name|createExpression
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expressionValue
argument_list|,
literal|"expressionValue"
argument_list|)
expr_stmt|;
return|return
name|expressionValue
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|assertMatches (String text, Exchange exchange)
specifier|public
name|void
name|assertMatches
parameter_list|(
name|String
name|text
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|AssertionError
block|{
if|if
condition|(
operator|!
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|text
operator|+
name|getExpression
argument_list|()
operator|+
literal|" for exchange: "
operator|+
name|exchange
argument_list|)
throw|;
block|}
block|}
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|predicate
operator|==
literal|null
condition|)
block|{
name|RouteContext
name|routeContext
init|=
operator|new
name|DefaultRouteContext
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|predicate
operator|=
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|predicate
argument_list|,
literal|"predicate"
argument_list|)
expr_stmt|;
return|return
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
DECL|method|createPredicate (RouteContext routeContext)
specifier|public
name|Predicate
argument_list|<
name|Exchange
argument_list|>
name|createPredicate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|predicate
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|expressionType
operator|!=
literal|null
condition|)
block|{
name|predicate
operator|=
name|expressionType
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CamelContext
name|camelContext
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|Language
name|language
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|getLanguage
argument_list|()
argument_list|)
decl_stmt|;
name|predicate
operator|=
name|language
operator|.
name|createPredicate
argument_list|(
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|configurePredicate
argument_list|(
name|routeContext
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|predicate
return|;
block|}
DECL|method|createExpression (RouteContext routeContext)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|expressionValue
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|expressionType
operator|!=
literal|null
condition|)
block|{
name|expressionValue
operator|=
name|expressionType
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CamelContext
name|camelContext
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|Language
name|language
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|getLanguage
argument_list|()
argument_list|)
decl_stmt|;
name|expressionValue
operator|=
name|language
operator|.
name|createExpression
argument_list|(
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|configureExpression
argument_list|(
name|routeContext
argument_list|,
name|expressionValue
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|expressionValue
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|setExpression (String expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
comment|/**      * Gets the value of the id property.      */
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Sets the value of the id property.      */
DECL|method|setId (String value)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getPredicate ()
specifier|public
name|Predicate
name|getPredicate
parameter_list|()
block|{
return|return
name|predicate
return|;
block|}
DECL|method|getExpressionValue ()
specifier|public
name|Expression
name|getExpressionValue
parameter_list|()
block|{
return|return
name|expressionValue
return|;
block|}
DECL|method|setExpressionValue (Expression expressionValue)
specifier|protected
name|void
name|setExpressionValue
parameter_list|(
name|Expression
name|expressionValue
parameter_list|)
block|{
name|this
operator|.
name|expressionValue
operator|=
name|expressionValue
expr_stmt|;
block|}
comment|/**      * Returns some descriptive text to describe this node      */
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
name|String
name|language
init|=
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
name|Expression
name|expressionValue
init|=
name|getExpressionValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|expressionValue
operator|!=
literal|null
condition|)
block|{
return|return
name|expressionValue
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
return|return
literal|""
return|;
block|}
comment|/**      * Allows derived classes to set a lazily created expressionType instance      * such as if using the {@link ExpressionClause}      */
DECL|method|setExpressionType (ExpressionType expressionType)
specifier|protected
name|void
name|setExpressionType
parameter_list|(
name|ExpressionType
name|expressionType
parameter_list|)
block|{
name|this
operator|.
name|expressionType
operator|=
name|expressionType
expr_stmt|;
block|}
DECL|method|configurePredicate (RouteContext routeContext, Predicate predicate)
specifier|protected
name|void
name|configurePredicate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Predicate
name|predicate
parameter_list|)
block|{     }
DECL|method|configureExpression (RouteContext routeContext, Expression expression)
specifier|protected
name|void
name|configureExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{     }
comment|/**      * Sets a named property on the object instance using introspection      */
DECL|method|setProperty (Object bean, String name, Object value)
specifier|protected
name|void
name|setProperty
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to set property "
operator|+
name|name
operator|+
literal|" on "
operator|+
name|bean
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

