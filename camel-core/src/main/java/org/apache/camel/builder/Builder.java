begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Expression
import|;
end_import

begin_comment
comment|/**  * A helper class for including portions of the<a  * href="http://camel.apache.org/expression.html">expression</a> and  *<a href="http://camel.apache.org/predicate.html">predicate</a><a  * href="http://camel.apache.org/dsl.html">Java DSL</a>  *  * @version   */
end_comment

begin_class
DECL|class|Builder
specifier|public
specifier|final
class|class
name|Builder
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|Builder ()
specifier|private
name|Builder
parameter_list|()
block|{     }
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">bean expression</a>      * value builder.      *<p/>      * This method accepts dual parameters. Either an bean instance or a reference to a bean (String).      *      * @param beanOrBeanRef  either an instanceof a bean or a reference to bean to lookup in the Registry      * @return the builder      */
DECL|method|bean (final Object beanOrBeanRef)
specifier|public
specifier|static
name|ValueBuilder
name|bean
parameter_list|(
specifier|final
name|Object
name|beanOrBeanRef
parameter_list|)
block|{
return|return
name|bean
argument_list|(
name|beanOrBeanRef
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">bean expression</a>      * value builder.      *<p/>      * This method accepts dual parameters. Either an bean instance or a reference to a bean (String).      *      * @param beanOrBeanRef  either an instanceof a bean or a reference to bean to lookup in the Registry      * @param method the method name      * @return the builder      */
DECL|method|bean (Object beanOrBeanRef, String method)
specifier|public
specifier|static
name|ValueBuilder
name|bean
parameter_list|(
name|Object
name|beanOrBeanRef
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|Expression
name|expression
decl_stmt|;
if|if
condition|(
name|beanOrBeanRef
operator|instanceof
name|String
condition|)
block|{
name|expression
operator|=
name|ExpressionBuilder
operator|.
name|beanExpression
argument_list|(
operator|(
name|String
operator|)
name|beanOrBeanRef
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|expression
operator|=
name|ExpressionBuilder
operator|.
name|beanExpression
argument_list|(
name|beanOrBeanRef
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">bean expression</a>      * value builder      *      * @param beanType the bean class which will be invoked      * @param method   name of method to invoke      * @return the builder      */
DECL|method|bean (Class<?> beanType, String method)
specifier|public
specifier|static
name|ValueBuilder
name|bean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|beanExpression
argument_list|(
name|beanType
argument_list|,
name|method
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a constant expression      */
DECL|method|constant (Object value)
specifier|public
specifier|static
name|ValueBuilder
name|constant
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a constant expression      */
DECL|method|language (String language, String expression)
specifier|public
specifier|static
name|ValueBuilder
name|language
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
name|Expression
name|exp
init|=
name|ExpressionBuilder
operator|.
name|languageExpression
argument_list|(
name|language
argument_list|,
name|expression
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * Returns a simple expression        */
DECL|method|simple (String value)
specifier|public
specifier|static
name|ValueBuilder
name|simple
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|simpleExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a simple expression      */
DECL|method|simple (String value, Class<?> resultType)
specifier|public
specifier|static
name|ValueBuilder
name|simple
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|simpleExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|expression
operator|=
name|ExpressionBuilder
operator|.
name|convertToExpression
argument_list|(
name|expression
argument_list|,
name|resultType
argument_list|)
expr_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for headers on an exchange      */
DECL|method|header (String name)
specifier|public
specifier|static
name|ValueBuilder
name|header
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for properties on an exchange      *      * @deprecated use {@link #exchangeProperty(String)} instead      */
annotation|@
name|Deprecated
DECL|method|property (String name)
specifier|public
specifier|static
name|ValueBuilder
name|property
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|exchangeProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for properties on an exchange      */
DECL|method|exchangeProperty (String name)
specifier|public
specifier|static
name|ValueBuilder
name|exchangeProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|exchangePropertyExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound body on an exchange      */
DECL|method|body ()
specifier|public
specifier|static
name|ValueBuilder
name|body
parameter_list|()
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound message body as a      * specific type      */
DECL|method|bodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|bodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound body on an      * exchange      */
DECL|method|outBody ()
specifier|public
specifier|static
name|ValueBuilder
name|outBody
parameter_list|()
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|outBodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound message body as a      * specific type      */
DECL|method|outBodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|outBodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|outBodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the fault body on an      * exchange      */
DECL|method|faultBody ()
specifier|public
specifier|static
name|ValueBuilder
name|faultBody
parameter_list|()
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|faultBodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the fault message body as a      * specific type      */
DECL|method|faultBodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|faultBodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|faultBodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns an expression for the given system property      */
DECL|method|systemProperty (final String name)
specifier|public
specifier|static
name|ValueBuilder
name|systemProperty
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|systemProperty
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns an expression for the given system property      */
DECL|method|systemProperty (final String name, final String defaultValue)
specifier|public
specifier|static
name|ValueBuilder
name|systemProperty
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|defaultValue
parameter_list|)
block|{
return|return
operator|new
name|ValueBuilder
argument_list|(
name|ExpressionBuilder
operator|.
name|systemPropertyExpression
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the exception message on an exchange      */
DECL|method|exceptionMessage ()
specifier|public
specifier|static
name|ValueBuilder
name|exceptionMessage
parameter_list|()
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|exchangeExceptionMessageExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the exception stacktrace on an exchange      */
DECL|method|exceptionStackTrace ()
specifier|public
specifier|static
name|ValueBuilder
name|exceptionStackTrace
parameter_list|()
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|exchangeExceptionStackTraceExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns an expression that replaces all occurrences of the regular       * expression with the given replacement      */
DECL|method|regexReplaceAll (Expression content, String regex, String replacement)
specifier|public
specifier|static
name|ValueBuilder
name|regexReplaceAll
parameter_list|(
name|Expression
name|content
parameter_list|,
name|String
name|regex
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|regexReplaceAll
argument_list|(
name|content
argument_list|,
name|regex
argument_list|,
name|replacement
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Returns an expression that replaces all occurrences of the regular       * expression with the given replacement      */
DECL|method|regexReplaceAll (Expression content, String regex, Expression replacement)
specifier|public
specifier|static
name|ValueBuilder
name|regexReplaceAll
parameter_list|(
name|Expression
name|content
parameter_list|,
name|String
name|regex
parameter_list|,
name|Expression
name|replacement
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|regexReplaceAll
argument_list|(
name|content
argument_list|,
name|regex
argument_list|,
name|replacement
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Returns an expression processing the exchange to the given endpoint uri.      *      * @param uri   endpoint uri      * @return the builder      */
DECL|method|sendTo (String uri)
specifier|public
specifier|static
name|ValueBuilder
name|sendTo
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|toExpression
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
block|}
end_class

end_unit

