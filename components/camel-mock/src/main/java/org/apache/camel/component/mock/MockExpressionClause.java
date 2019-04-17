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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|Message
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
name|support
operator|.
name|ExpressionAdapter
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

begin_comment
comment|/**  * Represents an expression clause within the DSL which when the expression is  * complete the clause continues to another part of the DSL  *<p/>  * This implementation is a derived copy of the<tt>org.apache.camel.builder.ExpressionClause</tt> from camel-core,  * that are specialized for being used with the mock component and separated from camel-core.  */
end_comment

begin_class
DECL|class|MockExpressionClause
specifier|public
class|class
name|MockExpressionClause
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Expression
implements|,
name|Predicate
block|{
DECL|field|delegate
specifier|private
name|MockExpressionClauseSupport
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|method|MockExpressionClause (T result)
specifier|public
name|MockExpressionClause
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
name|MockExpressionClauseSupport
argument_list|<>
argument_list|(
name|result
argument_list|)
expr_stmt|;
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
comment|/**      * A functional expression of the exchange      */
DECL|method|exchange (final Function<Exchange, Object> function)
specifier|public
name|T
name|exchange
parameter_list|(
specifier|final
name|Function
argument_list|<
name|Exchange
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * An expression of an inbound message      */
DECL|method|message ()
specifier|public
name|T
name|message
parameter_list|()
block|{
return|return
name|inMessage
argument_list|()
return|;
block|}
comment|/**      * A functional expression of an inbound message      */
DECL|method|message (final Function<Message, Object> function)
specifier|public
name|T
name|message
parameter_list|(
specifier|final
name|Function
argument_list|<
name|Message
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|inMessage
argument_list|(
name|function
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
name|delegate
operator|.
name|inMessage
argument_list|()
return|;
block|}
comment|/**      * A functional expression of an inbound message      */
DECL|method|inMessage (final Function<Message, Object> function)
specifier|public
name|T
name|inMessage
parameter_list|(
specifier|final
name|Function
argument_list|<
name|Message
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * An expression of an outbound message      */
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
comment|/**      * A functional expression of an outbound message      */
DECL|method|outMessage (final Function<Message, Object> function)
specifier|public
name|T
name|outMessage
parameter_list|(
specifier|final
name|Function
argument_list|<
name|Message
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
return|;
block|}
block|}
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
name|delegate
operator|.
name|body
argument_list|()
return|;
block|}
comment|/**      * A functional expression of an inbound message body      */
DECL|method|body (final Function<Object, Object> function)
specifier|public
name|T
name|body
parameter_list|(
specifier|final
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * A functional expression of an inbound message body      */
DECL|method|body (final Supplier<Object> supplier)
specifier|public
name|T
name|body
parameter_list|(
specifier|final
name|Supplier
argument_list|<
name|Object
argument_list|>
name|supplier
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|supplier
operator|.
name|get
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * A functional expression of an inbound message body and headers      */
DECL|method|body (final BiFunction<Object, Map<String, Object>, Object> function)
specifier|public
name|T
name|body
parameter_list|(
specifier|final
name|BiFunction
argument_list|<
name|Object
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
return|;
block|}
block|}
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
name|delegate
operator|.
name|body
argument_list|(
name|expectedType
argument_list|)
return|;
block|}
comment|/**      * A functional expression of an inbound message body converted to the expected type      */
DECL|method|body (Class<B> expectedType, final Function<B, Object> function)
specifier|public
parameter_list|<
name|B
parameter_list|>
name|T
name|body
parameter_list|(
name|Class
argument_list|<
name|B
argument_list|>
name|expectedType
parameter_list|,
specifier|final
name|Function
argument_list|<
name|B
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|expectedType
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * A functional expression of an inbound message body converted to the expected type and headers      */
DECL|method|body (Class<B> expectedType, final BiFunction<B, Map<String, Object>, Object> function)
specifier|public
parameter_list|<
name|B
parameter_list|>
name|T
name|body
parameter_list|(
name|Class
argument_list|<
name|B
argument_list|>
name|expectedType
parameter_list|,
specifier|final
name|BiFunction
argument_list|<
name|B
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|expectedType
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
return|;
block|}
block|}
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
comment|/**      * A functional expression of an outbound message body      */
DECL|method|outBody (final Function<Object, Object> function)
specifier|public
name|T
name|outBody
parameter_list|(
specifier|final
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * A functional expression of an outbound message body and headers      */
DECL|method|outBody (final BiFunction<Object, Map<String, Object>, Object> function)
specifier|public
name|T
name|outBody
parameter_list|(
specifier|final
name|BiFunction
argument_list|<
name|Object
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
return|;
block|}
block|}
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
name|delegate
operator|.
name|outBody
argument_list|(
name|expectedType
argument_list|)
return|;
block|}
comment|/**      * A functional expression of an outbound message body converted to the expected type      */
DECL|method|outBody (Class<B> expectedType, final Function<B, Object> function)
specifier|public
parameter_list|<
name|B
parameter_list|>
name|T
name|outBody
parameter_list|(
name|Class
argument_list|<
name|B
argument_list|>
name|expectedType
parameter_list|,
specifier|final
name|Function
argument_list|<
name|B
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|expectedType
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * A functional expression of an outbound message body converted to the expected type and headers      */
DECL|method|outBody (Class<B> expectedType, final BiFunction<B, Map<String, Object>, Object> function)
specifier|public
parameter_list|<
name|B
parameter_list|>
name|T
name|outBody
parameter_list|(
name|Class
argument_list|<
name|B
argument_list|>
name|expectedType
parameter_list|,
specifier|final
name|BiFunction
argument_list|<
name|B
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|expression
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|expectedType
argument_list|)
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
return|;
block|}
block|}
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
comment|/**      * An expression of the inbound message attachments      */
DECL|method|attachments ()
specifier|public
name|T
name|attachments
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|attachments
argument_list|()
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
name|delegate
operator|.
name|exchangeProperty
argument_list|(
name|name
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
name|delegate
operator|.
name|exchangeProperties
argument_list|()
return|;
block|}
comment|// Languages
comment|// -------------------------------------------------------------------------
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html">bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *       * @param bean the name of the bean looked up the registry      * @return the builder to continue processing the DSL      */
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
comment|/**      * Evaluates an expression using the<a      * href="http://camel.apache.org/bean-language.html">bean language</a>      * which basically means the bean is invoked to determine the expression      * value.      *       * @param bean the name of the bean looked up the registry      * @param method the name of the method to invoke on the bean      * @return the builder to continue processing the DSL      */
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
comment|/**      * Evaluates a<a      * href="http://camel.apache.org/java-script.html">JavaScript      * expression</a>      *       * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      * @deprecated JavaScript is deprecated in Java 11 onwards      */
annotation|@
name|Deprecated
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
comment|/**      * Evaluates a<a      * href="http://camel.apache.org/jsonpath.html">Json Path      * expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
name|delegate
operator|.
name|jsonpath
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
DECL|method|getExpressionType ()
specifier|public
name|ExpressionFactory
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
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
return|;
block|}
else|else
block|{
name|Expression
name|exp
init|=
name|delegate
operator|.
name|getExpressionType
argument_list|()
operator|.
name|createExpression
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
return|;
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
name|getExpressionValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|ExpressionToPredicateAdapter
argument_list|(
name|getExpressionValue
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
else|else
block|{
name|Expression
name|exp
init|=
name|delegate
operator|.
name|getExpressionType
argument_list|()
operator|.
name|createExpression
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|ExpressionToPredicateAdapter
argument_list|(
name|exp
argument_list|)
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

