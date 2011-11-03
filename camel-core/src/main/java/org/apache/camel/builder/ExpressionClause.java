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

begin_comment
comment|/**  * Represents an expression clause within the DSL which when the expression is  * complete the clause continues to another part of the DSL  *   * @version   */
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
DECL|field|delegate
specifier|private
name|ExpressionClauseSupport
argument_list|<
name|T
argument_list|>
name|delegate
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
name|delegate
operator|=
operator|new
name|ExpressionClauseSupport
argument_list|<
name|T
argument_list|>
argument_list|(
name|result
argument_list|)
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
return|return
name|delegate
operator|.
name|expression
argument_list|(
name|expression
argument_list|)
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
name|delegate
operator|.
name|constant
argument_list|(
name|value
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
name|delegate
operator|.
name|exchange
argument_list|()
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
name|delegate
operator|.
name|inMessage
argument_list|()
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
name|delegate
operator|.
name|outMessage
argument_list|()
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
name|delegate
operator|.
name|body
argument_list|()
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
name|delegate
operator|.
name|body
argument_list|(
name|expectedType
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
name|delegate
operator|.
name|outBody
argument_list|()
return|;
block|}
comment|/**      * An expression of an outbound message body converted to the expected type      */
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
name|delegate
operator|.
name|outBody
argument_list|(
name|expectedType
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
name|delegate
operator|.
name|header
argument_list|(
name|name
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
name|delegate
operator|.
name|headers
argument_list|()
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
name|delegate
operator|.
name|outHeader
argument_list|(
name|name
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
name|delegate
operator|.
name|outHeaders
argument_list|()
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
name|delegate
operator|.
name|property
argument_list|(
name|name
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
name|delegate
operator|.
name|properties
argument_list|()
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
return|return
name|delegate
operator|.
name|method
argument_list|(
name|bean
argument_list|)
return|;
block|}
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *      * @param instance the instance of the bean      * @return the builder to continue processing the DSL      */
DECL|method|method (Object instance)
specifier|public
name|T
name|method
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|method
argument_list|(
name|instance
argument_list|)
return|;
block|}
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *       * @param beanType the Class of the bean which we want to invoke      * @return the builder to continue processing the DSL      */
DECL|method|method (Class<?> beanType)
specifier|public
name|T
name|method
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|method
argument_list|(
name|beanType
argument_list|)
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
return|return
name|delegate
operator|.
name|method
argument_list|(
name|bean
argument_list|,
name|method
argument_list|)
return|;
block|}
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *      * @param instance the instance of the bean      * @param method the name of the method to invoke on the bean      * @return the builder to continue processing the DSL      */
DECL|method|method (Object instance, String method)
specifier|public
name|T
name|method
parameter_list|(
name|Object
name|instance
parameter_list|,
name|String
name|method
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|method
argument_list|(
name|instance
argument_list|,
name|method
argument_list|)
return|;
block|}
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html>bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *       * @param beanType the Class of the bean which we want to invoke      * @param method the name of the method to invoke on the bean      * @return the builder to continue processing the DSL      */
DECL|method|method (Class<?> beanType, String method)
specifier|public
name|T
name|method
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
return|return
name|delegate
operator|.
name|method
argument_list|(
name|beanType
argument_list|,
name|method
argument_list|)
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
name|delegate
operator|.
name|el
argument_list|(
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
name|delegate
operator|.
name|groovy
argument_list|(
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
name|delegate
operator|.
name|javaScript
argument_list|(
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
name|delegate
operator|.
name|jxpath
argument_list|(
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
name|delegate
operator|.
name|ognl
argument_list|(
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
name|delegate
operator|.
name|mvel
argument_list|(
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
name|delegate
operator|.
name|php
argument_list|(
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
name|delegate
operator|.
name|python
argument_list|(
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/ref-language.html">Ref      * expression</a>      *       * @param ref refers to the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|delegate
operator|.
name|ref
argument_list|(
name|ref
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/ruby.html">Ruby      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|delegate
operator|.
name|ruby
argument_list|(
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
name|delegate
operator|.
name|sql
argument_list|(
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/spel.html">SpEL      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|delegate
operator|.
name|spel
argument_list|(
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
name|delegate
operator|.
name|simple
argument_list|(
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://camel.apache.org/simple.html">Simple      * expression</a>      *      * @param text the expression to be evaluated      * @param resultType the result type      * @return the builder to continue processing the DSL      */
DECL|method|simple (String text, Class<?> resultType)
specifier|public
name|T
name|simple
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|simple
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|)
return|;
block|}
comment|/**      * Evaluates a token expression on the message body      *      * @param token the token      * @return the builder to continue processing the DSL      */
DECL|method|tokenize (String token)
specifier|public
name|T
name|tokenize
parameter_list|(
name|String
name|token
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|tokenize
argument_list|(
name|token
argument_list|)
return|;
block|}
comment|/**      * Evaluates a token expression on the given header      *      * @param token the token      * @param headerName name of header to tokenize      * @return the builder to continue processing the DSL      */
DECL|method|tokenize (String token, String headerName)
specifier|public
name|T
name|tokenize
parameter_list|(
name|String
name|token
parameter_list|,
name|String
name|headerName
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|tokenize
argument_list|(
name|token
argument_list|,
name|headerName
argument_list|)
return|;
block|}
comment|/**      * Evaluates a token expression on the given header      *      * @param token the token      * @param headerName name of header to tokenize      * @param regex whether the token is a regular expression or not      * @return the builder to continue processing the DSL      */
DECL|method|tokenize (String token, String headerName, boolean regex)
specifier|public
name|T
name|tokenize
parameter_list|(
name|String
name|token
parameter_list|,
name|String
name|headerName
parameter_list|,
name|boolean
name|regex
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|tokenize
argument_list|(
name|token
argument_list|,
name|headerName
argument_list|,
name|regex
argument_list|)
return|;
block|}
comment|/**      * Evaluates a token pair expression on the message body      *      * @param startToken the start token      * @param endToken   the end token      * @return the builder to continue processing the DSL      */
DECL|method|tokenizePair (String startToken, String endToken)
specifier|public
name|T
name|tokenizePair
parameter_list|(
name|String
name|startToken
parameter_list|,
name|String
name|endToken
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|tokenizePair
argument_list|(
name|startToken
argument_list|,
name|endToken
argument_list|)
return|;
block|}
comment|/**      * Evaluates a XML token expression on the message body with XML content      *      * @param tagName the the tag name of the child nodes to tokenize      * @return the builder to continue processing the DSL      */
DECL|method|tokenizeXML (String tagName)
specifier|public
name|T
name|tokenizeXML
parameter_list|(
name|String
name|tagName
parameter_list|)
block|{
return|return
name|tokenizeXML
argument_list|(
name|tagName
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Evaluates a token pair expression on the message body with XML content      *      * @param tagName the the tag name of the child nodes to tokenize      * @param inheritNamespaceTagName  parent or root tag name that contains namespace(s) to inherit      * @return the builder to continue processing the DSL      */
DECL|method|tokenizeXML (String tagName, String inheritNamespaceTagName)
specifier|public
name|T
name|tokenizeXML
parameter_list|(
name|String
name|tagName
parameter_list|,
name|String
name|inheritNamespaceTagName
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|tokenizeXMLPair
argument_list|(
name|tagName
argument_list|,
name|inheritNamespaceTagName
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
name|delegate
operator|.
name|xpath
argument_list|(
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a> with the specified result type      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text, Class<?> resultType)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|xpath
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a> with the specified result type and set of namespace      * prefixes and URIs      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text, Class<?> resultType, Namespaces namespaces)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|,
name|Namespaces
name|namespaces
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|xpath
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|,
name|namespaces
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://camel.apache.org/xpath.html">XPath      * expression</a> with the specified result type and set of namespace      * prefixes and URIs      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text, Class<?> resultType, Map<String, String> namespaces)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
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
return|return
name|delegate
operator|.
name|xpath
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|,
name|namespaces
argument_list|)
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
name|delegate
operator|.
name|xpath
argument_list|(
name|text
argument_list|,
name|namespaces
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
return|return
name|delegate
operator|.
name|xpath
argument_list|(
name|text
argument_list|,
name|namespaces
argument_list|)
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
name|delegate
operator|.
name|xquery
argument_list|(
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      * with the specified result type      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text, Class<?> resultType)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|xquery
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      * with the specified result type and set of namespace prefixes and URIs      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text, Class<?> resultType, Namespaces namespaces)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|,
name|Namespaces
name|namespaces
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|xquery
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|,
name|namespaces
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a      * href="http://camel.apache.org/xquery.html">XQuery expression</a>      * with the specified result type and set of namespace prefixes and URIs      *       * @param text the expression to be evaluated      * @param resultType the return type expected by the expression      * @param namespaces the namespace prefix and URIs to use      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text, Class<?> resultType, Map<String, String> namespaces)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
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
return|return
name|delegate
operator|.
name|xquery
argument_list|(
name|text
argument_list|,
name|resultType
argument_list|,
name|namespaces
argument_list|)
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
name|delegate
operator|.
name|xquery
argument_list|(
name|text
argument_list|,
name|namespaces
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
return|return
name|delegate
operator|.
name|xquery
argument_list|(
name|text
argument_list|,
name|namespaces
argument_list|)
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
return|return
name|delegate
operator|.
name|language
argument_list|(
name|language
argument_list|,
name|expression
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getLanguage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getExpression
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setExpression (String expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|delegate
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExpressionValue ()
specifier|public
name|Expression
name|getExpressionValue
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getExpressionValue
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setExpressionValue (Expression expressionValue)
specifier|protected
name|void
name|setExpressionValue
parameter_list|(
name|Expression
name|expressionValue
parameter_list|)
block|{
name|delegate
operator|.
name|setExpressionValue
argument_list|(
name|expressionValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExpressionType ()
specifier|public
name|ExpressionDefinition
name|getExpressionType
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getExpressionType
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setExpressionType (ExpressionDefinition expressionType)
specifier|protected
name|void
name|setExpressionType
parameter_list|(
name|ExpressionDefinition
name|expressionType
parameter_list|)
block|{
name|delegate
operator|.
name|setExpressionType
argument_list|(
name|expressionType
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

