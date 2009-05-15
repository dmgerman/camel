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
name|java
operator|.
name|util
operator|.
name|Map
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
name|builder
operator|.
name|xml
operator|.
name|Namespaces
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
name|ExpressionNode
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
name|model
operator|.
name|language
operator|.
name|MethodCallExpression
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
name|XPathExpression
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
name|XQueryExpression
import|;
end_import

begin_comment
comment|/**  * Represents an expression clause within the DSL which when the expression is  * complete the clause continues to another part of the DSL  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|ExpressionClause
specifier|public
class|class
name|ExpressionClause
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ExpressionDefinition
block|{
DECL|field|result
specifier|private
name|T
name|result
decl_stmt|;
DECL|field|language
specifier|private
name|String
name|language
decl_stmt|;
DECL|method|ExpressionClause (T result)
specifier|public
name|ExpressionClause
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
DECL|method|createAndSetExpression (T result)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|ExpressionNode
parameter_list|>
name|ExpressionClause
argument_list|<
name|T
argument_list|>
name|createAndSetExpression
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|ExpressionClause
argument_list|<
name|T
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|T
argument_list|>
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|result
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
comment|// Helper expressions
comment|// -------------------------------------------------------------------------
comment|/**      * Specify an {@link Expression} instance      */
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
return|return
name|result
return|;
block|}
comment|/**      * Specify the constant expression value      */
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
DECL|method|body (Class expectedType)
specifier|public
name|T
name|body
parameter_list|(
name|Class
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|outBody (Class expectedType)
specifier|public
name|T
name|outBody
parameter_list|(
name|Class
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
comment|/**      * An expression of an exchange property of the given name      */
DECL|method|property (String name)
specifier|public
name|T
name|property
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
name|propertyExpression
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * An expression of the exchange properties      */
DECL|method|properties ()
specifier|public
name|T
name|properties
parameter_list|()
block|{
return|return
name|expression
argument_list|(
name|ExpressionBuilder
operator|.
name|propertiesExpression
argument_list|()
argument_list|)
return|;
block|}
comment|// Languages
comment|// -------------------------------------------------------------------------
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *       * @param bean the name of the bean looked up the registry      * @return the builder to continue processing the DSL      */
DECL|method|method (String bean)
specifier|public
name|T
name|method
parameter_list|(
name|String
name|bean
parameter_list|)
block|{
name|MethodCallExpression
name|expression
init|=
operator|new
name|MethodCallExpression
argument_list|(
name|bean
argument_list|)
decl_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *       * @param beanType the Class of the bean which we want to invoke      * @return the builder to continue processing the DSL      */
DECL|method|method (Class beanType)
specifier|public
name|T
name|method
parameter_list|(
name|Class
name|beanType
parameter_list|)
block|{
name|MethodCallExpression
name|expression
init|=
operator|new
name|MethodCallExpression
argument_list|(
name|beanType
argument_list|)
decl_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *       * @param bean the name of the bean looked up the registry      * @param method the name of the method to invoke on the bean      * @return the builder to continue processing the DSL      */
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
name|MethodCallExpression
name|expression
init|=
operator|new
name|MethodCallExpression
argument_list|(
name|bean
argument_list|,
name|method
argument_list|)
decl_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *       * @param beanType the Class of the bean which we want to invoke      * @param method the name of the method to invoke on the bean      * @return the builder to continue processing the DSL      */
DECL|method|method (Class beanType, String method)
specifier|public
name|T
name|method
parameter_list|(
name|Class
name|beanType
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|MethodCallExpression
name|expression
init|=
operator|new
name|MethodCallExpression
argument_list|(
name|beanType
argument_list|,
name|method
argument_list|)
decl_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates the<a href="http://camel.apache.org/el.html">EL      * Language from JSP and JSF</a> using the<a      * href="http://camel.apache.org/juel.html">JUEL library</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|el (String text)
specifier|public
name|T
name|el
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"el"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/groovy.html">Groovy      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|language
argument_list|(
literal|"groovy"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a      * href="http://camel.apache.org/java-script.html">JavaScript      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|javaScript (String text)
specifier|public
name|T
name|javaScript
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"js"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://commons.apache.org/jxpath/">JXPath expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|jxpath (String text)
specifier|public
name|T
name|jxpath
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"jxpath"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/ognl.html">OGNL      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|language
argument_list|(
literal|"ognl"
argument_list|,
name|text
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
name|language
argument_list|(
literal|"mvel"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/php.html">PHP      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|php (String text)
specifier|public
name|T
name|php
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"php"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/python.html">Python      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|python (String text)
specifier|public
name|T
name|python
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"python"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/ruby.html">Ruby      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|ruby (String text)
specifier|public
name|T
name|ruby
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"ruby"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/sql.html">SQL      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|sql (String text)
specifier|public
name|T
name|sql
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"sql"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/simple.html">Simple      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|language
argument_list|(
literal|"simple"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|language
argument_list|(
literal|"xpath"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a> with the specified result type      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expressiopn      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text, Class resultType)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
name|resultType
parameter_list|)
block|{
name|XPathExpression
name|expression
init|=
operator|new
name|XPathExpression
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|expression
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a> with the specified result type and set of namespace      * prefixes and URIs      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text, Class resultType, Namespaces namespaces)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
name|resultType
parameter_list|,
name|Namespaces
name|namespaces
parameter_list|)
block|{
return|return
name|xpath
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|,
name|namespaces
operator|.
name|getNamespaces
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a> with the specified result type and set of namespace      * prefixes and URIs      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text, Class resultType, Map<String, String> namespaces)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
name|resultType
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|XPathExpression
name|expression
init|=
operator|new
name|XPathExpression
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|expression
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
name|expression
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a> with the specified set of namespace prefixes and URIs      *       * @param text the expression to be evaluated      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text, Namespaces namespaces)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|,
name|Namespaces
name|namespaces
parameter_list|)
block|{
return|return
name|xpath
argument_list|(
name|text
argument_list|,
name|namespaces
operator|.
name|getNamespaces
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a> with the specified set of namespace prefixes and URIs      *       * @param text the expression to be evaluated      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text, Map<String, String> namespaces)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|XPathExpression
name|expression
init|=
operator|new
name|XPathExpression
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|expression
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|language
argument_list|(
literal|"xquery"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      * with the specified result type      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expressiopn      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text, Class resultType)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
name|resultType
parameter_list|)
block|{
name|XQueryExpression
name|expression
init|=
operator|new
name|XQueryExpression
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|expression
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      * with the specified result type and set of namespace prefixes and URIs      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text, Class resultType, Namespaces namespaces)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
name|resultType
parameter_list|,
name|Namespaces
name|namespaces
parameter_list|)
block|{
return|return
name|xquery
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|,
name|namespaces
operator|.
name|getNamespaces
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      * with the specified result type and set of namespace prefixes and URIs      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text, Class resultType, Map<String, String> namespaces)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
name|resultType
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|XQueryExpression
name|expression
init|=
operator|new
name|XQueryExpression
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|expression
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
name|expression
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      * with the specified set of namespace prefixes and URIs      *       * @param text the expression to be evaluated      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text, Namespaces namespaces)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|,
name|Namespaces
name|namespaces
parameter_list|)
block|{
return|return
name|xquery
argument_list|(
name|text
argument_list|,
name|namespaces
operator|.
name|getNamespaces
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      * with the specified set of namespace prefixes and URIs      *       * @param text the expression to be evaluated      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text, Map<String, String> namespaces)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|XQueryExpression
name|expression
init|=
operator|new
name|XQueryExpression
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|expression
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|setExpressionType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Evaluates a given language name with the expression text      *       * @param language the name of the language      * @param expression the expression in the given language      * @return the builder to continue processing the DSL      */
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
name|setLanguage
argument_list|(
name|language
argument_list|)
expr_stmt|;
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
DECL|method|setLanguage (String language)
specifier|public
name|void
name|setLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
block|}
end_class

end_unit

