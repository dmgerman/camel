begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mock
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
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
name|support
operator|.
name|builder
operator|.
name|ExpressionBuilder
import|;
end_import

begin_comment
comment|/**  * A support class for building expression clauses.  *<p/>  * This implementation is a derived copy of the<tt>org.apache.camel.builder.ExpressionClauseSupport</tt> from camel-core,  * that are specialized for being used with the mock component and separated from camel-core.  */
end_comment

begin_class
DECL|class|MockExpressionClauseSupport
specifier|public
class|class
name|MockExpressionClauseSupport
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|result
specifier|private
name|T
name|result
decl_stmt|;
DECL|field|expressionValue
specifier|private
name|Expression
name|expressionValue
decl_stmt|;
DECL|field|expressionType
specifier|private
name|ExpressionFactory
name|expressionType
decl_stmt|;
DECL|method|MockExpressionClauseSupport (T result)
specifier|public
name|MockExpressionClauseSupport
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
comment|// Helper expressions
comment|// -------------------------------------------------------------------------
comment|/**      * Specify an {@link org.apache.camel.Expression} instance      */
DECL|method|expression (Expression expression)
specifier|public
name|T
name|expression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|setExpressionValue
argument_list|(
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|expression
operator|instanceof
name|ExpressionFactory
condition|)
block|{
name|setExpressionType
argument_list|(
operator|(
name|ExpressionFactory
operator|)
name|expression
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Specify an {@link ExpressionFactory} instance      */
DECL|method|language (ExpressionFactory expression)
specifier|public
name|T
name|language
parameter_list|(
name|ExpressionFactory
name|expression
parameter_list|)
block|{
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Specify the constant expression value.      *      *<b>Important:</b> this is a fixed constant value that is only set once during starting up the route,      * do not use this if you want dynamic values during routing.      */
DECL|method|constant (Object value)
specifier|public
name|T
name|constant
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * An expression of the exchange      */
DECL|method|exchange ()
specifier|public
name|T
name|exchange
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|exchangeExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * An expression of an inbound message      */
DECL|method|inMessage ()
specifier|public
name|T
name|inMessage
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|inMessageExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * An expression of an inbound message      */
DECL|method|outMessage ()
specifier|public
name|T
name|outMessage
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|outMessageExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * An expression of an inbound message body      */
DECL|method|body ()
specifier|public
name|T
name|body
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * An expression of an inbound message body converted to the expected type      */
DECL|method|body (Class<?> expectedType)
specifier|public
name|T
name|body
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|expectedType
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|expectedType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * An expression of an outbound message body      */
DECL|method|outBody ()
specifier|public
name|T
name|outBody
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|outBodyExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * An expression of an outbound message body converted to the expected type      */
DECL|method|outBody (Class<?> expectedType)
specifier|public
name|T
name|outBody
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|expectedType
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|outBodyExpression
argument_list|(
name|expectedType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * An expression of an inbound message header of the given name      */
DECL|method|header (String name)
specifier|public
name|T
name|header
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * An expression of the inbound headers      */
DECL|method|headers ()
specifier|public
name|T
name|headers
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|headersExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * An expression of an outbound message header of the given name      */
DECL|method|outHeader (String name)
specifier|public
name|T
name|outHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|outHeaderExpression
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * An expression of the outbound headers      */
DECL|method|outHeaders ()
specifier|public
name|T
name|outHeaders
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|outHeadersExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * An expression of the exchange pattern      */
DECL|method|exchangePattern ()
specifier|public
name|T
name|exchangePattern
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|exchangePatternExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * An expression of an exchange property of the given name      */
DECL|method|exchangeProperty (String name)
specifier|public
name|T
name|exchangeProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|exchangePropertyExpression
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * An expression of the exchange properties      */
DECL|method|exchangeProperties ()
specifier|public
name|T
name|exchangeProperties
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|exchangePropertiesExpression
argument_list|()
argument_list|)
return|;
block|}
comment|// Languages
comment|// -------------------------------------------------------------------------
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *      * @param bean the name of the bean looked up the registry      * @return the builder to continue processing the DSL      */
DECL|method|method (String bean)
specifier|public
name|T
name|method
parameter_list|(
name|String
name|bean
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|beanExpression
argument_list|(
name|bean
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *      * @param bean the name of the bean looked up the registry      * @param method the name of the method to invoke on the bean      * @return the builder to continue processing the DSL      */
DECL|method|method (String bean, String method)
specifier|public
name|T
name|method
parameter_list|(
name|String
name|bean
parameter_list|,
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"bean"
argument_list|,
name|bean
operator|+
literal|"?method="
operator|+
name|method
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|method
argument_list|(
name|bean
argument_list|)
return|;
block|}
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/groovy.html">Groovy      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|groovy (String text)
specifier|public
name|T
name|groovy
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"groovy"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/jsonpath.html">Json Path      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|jsonpath (String text)
specifier|public
name|T
name|jsonpath
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"jsonpath"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/ognl.html">OGNL      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|ognl (String text)
specifier|public
name|T
name|ognl
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"ognl"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/mvel.html">MVEL      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|mvel (String text)
specifier|public
name|T
name|mvel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"mvel"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates a {@link Expression} by looking up existing {@link Expression}      * from the {@link org.apache.camel.spi.Registry}      *      * @param ref refers to the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|ref (String ref)
specifier|public
name|T
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"ref"
argument_list|,
name|ref
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/spel.html">SpEL      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|spel (String text)
specifier|public
name|T
name|spel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"spel"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/simple.html">Simple      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|simple (String text)
specifier|public
name|T
name|simple
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"simple"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/hl7.html">HL7 Terser      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|hl7terser (String text)
specifier|public
name|T
name|hl7terser
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"hl7terser"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"xpath"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
literal|"xquery"
argument_list|,
name|text
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Evaluates a given language name with the expression text      *      * @param language the name of the language      * @param expression the expression in the given language      * @return the builder to continue processing the DSL      */
DECL|method|language (String language, String expression)
specifier|public
name|T
name|language
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
name|language
argument_list|,
name|expression
argument_list|)
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
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
specifier|public
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
name|ExpressionFactory
name|getExpressionType
parameter_list|()
block|{
return|return
name|expressionType
return|;
block|}
DECL|method|setExpressionType (ExpressionFactory expressionType)
specifier|public
name|void
name|setExpressionType
parameter_list|(
name|ExpressionFactory
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
DECL|method|createExpression (CamelContext camelContext)
specifier|protected
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
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No expression value configured"
argument_list|)
throw|;
block|}
block|}
return|return
name|getExpressionValue
argument_list|()
return|;
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
comment|// noop
block|}
block|}
end_class

end_unit

