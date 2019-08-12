begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|XmlAnyAttribute
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
name|namespace
operator|.
name|QName
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
name|AfterPropertiesConfigured
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
name|CamelContextAware
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
name|ExpressionFactory
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
name|NoSuchLanguageException
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
name|DefinitionPropertyPlaceholderConfigurable
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
name|OtherAttributesAware
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
name|support
operator|.
name|ExpressionToPredicateAdapter
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
name|support
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
name|support
operator|.
name|ScriptHelper
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
comment|/**  * A useful base class for an expression  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"language"
argument_list|,
name|title
operator|=
literal|"Expression"
argument_list|)
annotation|@
name|XmlRootElement
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"expression"
argument_list|)
comment|// must be named expression
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ExpressionDefinition
specifier|public
class|class
name|ExpressionDefinition
implements|implements
name|Expression
implements|,
name|Predicate
implements|,
name|OtherAttributesAware
implements|,
name|ExpressionFactory
implements|,
name|DefinitionPropertyPlaceholderConfigurable
block|{
annotation|@
name|XmlAttribute
annotation|@
name|XmlID
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|XmlValue
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|expression
specifier|private
name|String
name|expression
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|trim
specifier|private
name|Boolean
name|trim
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
name|ExpressionDefinition
name|expressionType
decl_stmt|;
comment|// use xs:any to support optional property placeholders
annotation|@
name|XmlAnyAttribute
DECL|field|otherAttributes
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|otherAttributes
decl_stmt|;
DECL|method|ExpressionDefinition ()
specifier|public
name|ExpressionDefinition
parameter_list|()
block|{     }
DECL|method|ExpressionDefinition (String expression)
specifier|public
name|ExpressionDefinition
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
DECL|method|ExpressionDefinition (Predicate predicate)
specifier|public
name|ExpressionDefinition
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
DECL|method|ExpressionDefinition (Expression expression)
specifier|public
name|ExpressionDefinition
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
DECL|method|getLabel (List<ExpressionDefinition> expressions)
specifier|public
specifier|static
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
comment|// favour using the output from expression value
if|if
condition|(
name|getExpressionValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getExpressionValue
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
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
argument_list|)
operator|.
name|append
argument_list|(
literal|"{"
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
elseif|else
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
return|return
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|expressionValue
operator|==
literal|null
condition|)
block|{
name|expressionValue
operator|=
name|createExpression
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
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
argument_list|,
name|type
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
annotation|@
name|Override
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
name|predicate
operator|=
name|createPredicate
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
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
specifier|final
name|Predicate
name|createPredicate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
name|createPredicate
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createPredicate (CamelContext camelContext)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|CamelContext
name|camelContext
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
name|getExpressionType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|predicate
operator|=
name|getExpressionType
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getExpressionValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|predicate
operator|=
operator|new
name|ExpressionToPredicateAdapter
argument_list|(
name|getExpressionValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
literal|"language"
argument_list|,
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|language
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchLanguageException
argument_list|(
name|getLanguage
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|exp
init|=
name|getExpression
argument_list|()
decl_stmt|;
comment|// should be true by default
name|boolean
name|isTrim
init|=
name|getTrim
argument_list|()
operator|==
literal|null
operator|||
name|getTrim
argument_list|()
decl_stmt|;
comment|// trim if configured to trim
if|if
condition|(
name|exp
operator|!=
literal|null
operator|&&
name|isTrim
condition|)
block|{
name|exp
operator|=
name|exp
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
comment|// resolve the expression as it may be an external script from the classpath/file etc
name|exp
operator|=
name|ScriptHelper
operator|.
name|resolveOptionalExternalScript
argument_list|(
name|camelContext
argument_list|,
name|exp
argument_list|)
expr_stmt|;
name|predicate
operator|=
name|language
operator|.
name|createPredicate
argument_list|(
name|exp
argument_list|)
expr_stmt|;
name|configurePredicate
argument_list|(
name|camelContext
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
block|}
comment|// inject CamelContext if its aware
if|if
condition|(
name|predicate
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|predicate
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
return|return
name|predicate
return|;
block|}
DECL|method|createExpression (RouteContext routeContext)
specifier|public
specifier|final
name|Expression
name|createExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
name|createExpression
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExpression (CamelContext camelContext)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|getExpressionValue
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getExpressionType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setExpressionValue
argument_list|(
name|getExpressionType
argument_list|()
operator|.
name|createExpression
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
literal|"language"
argument_list|,
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|language
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchLanguageException
argument_list|(
name|getLanguage
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|exp
init|=
name|getExpression
argument_list|()
decl_stmt|;
comment|// should be true by default
name|boolean
name|isTrim
init|=
name|getTrim
argument_list|()
operator|==
literal|null
operator|||
name|getTrim
argument_list|()
decl_stmt|;
comment|// trim if configured to trim
if|if
condition|(
name|exp
operator|!=
literal|null
operator|&&
name|isTrim
condition|)
block|{
name|exp
operator|=
name|exp
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
comment|// resolve the expression as it may be an external script from the classpath/file etc
name|exp
operator|=
name|ScriptHelper
operator|.
name|resolveOptionalExternalScript
argument_list|(
name|camelContext
argument_list|,
name|exp
argument_list|)
expr_stmt|;
name|setExpressionValue
argument_list|(
name|language
operator|.
name|createExpression
argument_list|(
name|exp
argument_list|)
argument_list|)
expr_stmt|;
name|configureExpression
argument_list|(
name|camelContext
argument_list|,
name|getExpressionValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// inject CamelContext if its aware
if|if
condition|(
name|getExpressionValue
argument_list|()
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|getExpressionValue
argument_list|()
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
return|return
name|getExpressionValue
argument_list|()
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
comment|/**      * The expression value in your chosen language syntax      */
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
comment|/**      * Sets the id of this node      */
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
DECL|method|getExpressionType ()
specifier|public
name|ExpressionDefinition
name|getExpressionType
parameter_list|()
block|{
return|return
name|expressionType
return|;
block|}
DECL|method|getTrim ()
specifier|public
name|Boolean
name|getTrim
parameter_list|()
block|{
return|return
name|trim
return|;
block|}
comment|/**      * Whether to trim the value to remove leading and trailing whitespaces and line breaks      */
DECL|method|setTrim (Boolean trim)
specifier|public
name|void
name|setTrim
parameter_list|(
name|Boolean
name|trim
parameter_list|)
block|{
name|this
operator|.
name|trim
operator|=
name|trim
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOtherAttributes ()
specifier|public
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|getOtherAttributes
parameter_list|()
block|{
return|return
name|otherAttributes
return|;
block|}
annotation|@
name|Override
DECL|method|setOtherAttributes (Map<QName, Object> otherAttributes)
specifier|public
name|void
name|setOtherAttributes
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|otherAttributes
parameter_list|)
block|{
name|this
operator|.
name|otherAttributes
operator|=
name|otherAttributes
expr_stmt|;
block|}
comment|/**      * Returns some descriptive text to describe this node      */
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
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
name|String
name|exp
init|=
name|getExpression
argument_list|()
decl_stmt|;
return|return
name|exp
operator|!=
literal|null
condition|?
name|exp
else|:
literal|""
return|;
block|}
comment|/**      * Allows derived classes to set a lazily created expressionType instance      * such as if using the {@link org.apache.camel.builder.ExpressionClause}      */
DECL|method|setExpressionType (ExpressionDefinition expressionType)
specifier|protected
name|void
name|setExpressionType
parameter_list|(
name|ExpressionDefinition
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
DECL|method|configurePredicate (CamelContext camelContext, Predicate predicate)
specifier|protected
name|void
name|configurePredicate
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Predicate
name|predicate
parameter_list|)
block|{
comment|// allows to perform additional logic after the properties has been configured which may be needed
comment|// in the various camel components outside camel-core
if|if
condition|(
name|predicate
operator|instanceof
name|AfterPropertiesConfigured
condition|)
block|{
operator|(
operator|(
name|AfterPropertiesConfigured
operator|)
name|predicate
operator|)
operator|.
name|afterPropertiesConfigured
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureExpression (CamelContext camelContext, Expression expression)
specifier|protected
name|void
name|configureExpression
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
comment|// allows to perform additional logic after the properties has been configured which may be needed
comment|// in the various camel components outside camel-core
if|if
condition|(
name|expression
operator|instanceof
name|AfterPropertiesConfigured
condition|)
block|{
operator|(
operator|(
name|AfterPropertiesConfigured
operator|)
name|expression
operator|)
operator|.
name|afterPropertiesConfigured
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets a named property on the object instance using introspection      */
DECL|method|setProperty (CamelContext camelContext, Object bean, String name, Object value)
specifier|protected
name|void
name|setProperty
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
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
name|camelContext
argument_list|,
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

