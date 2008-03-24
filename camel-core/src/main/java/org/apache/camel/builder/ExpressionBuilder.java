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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|Message
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExpressionBuilder
specifier|public
specifier|final
class|class
name|ExpressionBuilder
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ExpressionBuilder ()
specifier|private
name|ExpressionBuilder
parameter_list|()
block|{     }
comment|/**      * Returns an expression for the header value with the given name      *      * @param headerName the name of the header the expression will return      * @return an expression object which will return the header value      */
DECL|method|headerExpression (final String headerName)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|headerExpression
parameter_list|(
specifier|final
name|String
name|headerName
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|header
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
condition|)
block|{
comment|// lets try the exchange header
name|header
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|headerName
argument_list|)
expr_stmt|;
block|}
return|return
name|header
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"header("
operator|+
name|headerName
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression for the inbound message headers      *      * @see Message#getHeaders()      * @return an expression object which will return the inbound headers      */
DECL|method|headersExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|headersExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"headers"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression for the out header value with the given name      *      * @param headerName the name of the header the expression will return      * @return an expression object which will return the header value      */
DECL|method|outHeaderExpression (final String headerName)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|outHeaderExpression
parameter_list|(
specifier|final
name|String
name|headerName
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|header
init|=
name|out
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
condition|)
block|{
comment|// lets try the exchange header
name|header
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|headerName
argument_list|)
expr_stmt|;
block|}
return|return
name|header
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"outHeader("
operator|+
name|headerName
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression for the outbound message headers      *      * @see Message#getHeaders()      * @return an expression object which will return the inbound headers      */
DECL|method|outHeadersExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|outHeadersExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"outHeaders"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression for the property value with the given name      *      * @see Exchange#getProperty(String)      * @param propertyName the name of the property the expression will return      * @return an expression object which will return the property value      */
DECL|method|propertyExpression (final String propertyName)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|propertyExpression
parameter_list|(
specifier|final
name|String
name|propertyName
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"property("
operator|+
name|propertyName
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression for the property value with the given name      *      * @see Exchange#getProperties()      * @return an expression object which will return the properties      */
DECL|method|propertiesExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|propertiesExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getProperties
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"properties"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression for a system property value with the given name      *      * @param propertyName the name of the system property the expression will      *                return      * @return an expression object which will return the system property value      */
DECL|method|systemPropertyExpression (final String propertyName)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|systemPropertyExpression
parameter_list|(
specifier|final
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|systemPropertyExpression
argument_list|(
name|propertyName
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns an expression for a system property value with the given name      *      * @param propertyName the name of the system property the expression will      *                return      * @return an expression object which will return the system property value      */
DECL|method|systemPropertyExpression (final String propertyName, final String defaultValue)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|systemPropertyExpression
parameter_list|(
specifier|final
name|String
name|propertyName
parameter_list|,
specifier|final
name|String
name|defaultValue
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"systemProperty("
operator|+
name|propertyName
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression for the contant value      *      * @param value the value the expression will return      * @return an expression object which will return the constant value      */
DECL|method|constantExpression (final Object value)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|constantExpression
parameter_list|(
specifier|final
name|Object
name|value
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|""
operator|+
name|value
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the exchanges inbound message body      */
DECL|method|bodyExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|bodyExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"body"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the exchanges inbound message body converted      * to the given type      */
DECL|method|bodyExpression (final Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|,
name|T
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|bodyExpression
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"bodyAs["
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the out messages body      */
DECL|method|outBodyExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|outBodyExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|out
operator|.
name|getBody
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"outBody"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the exchanges outbound message body converted      * to the given type      */
DECL|method|outBodyExpression (final Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|,
name|T
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|outBodyExpression
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|out
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"outBodyAs["
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the fault messages body      */
DECL|method|faultBodyExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|faultBodyExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getFault
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"faultBody"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the exchanges fault message body converted      * to the given type      */
DECL|method|faultBodyExpression (final Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|,
name|T
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|faultBodyExpression
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getFault
argument_list|()
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"faultBodyAs["
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the exchange      */
DECL|method|exchangeExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|exchangeExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"exchange"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the IN message      */
DECL|method|inMessageExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|inMessageExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"inMessage"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the expression for the OUT message      */
DECL|method|outMessageExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|outMessageExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getOut
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"outMessage"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression which converts the given expression to the given      * type      */
DECL|method|convertTo (final Expression expression, final Class type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|convertTo
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|,
specifier|final
name|Class
name|type
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|value
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|""
operator|+
name|expression
operator|+
literal|".convertTo("
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|".class)"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a tokenize expression which will tokenize the string with the      * given token      */
DECL|method|tokenizeExpression (final Expression<E> expression, final String token)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|tokenizeExpression
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|,
specifier|final
name|String
name|token
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|String
name|text
init|=
name|evaluateStringExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringTokenizer
name|iter
init|=
operator|new
name|StringTokenizer
argument_list|(
name|text
argument_list|,
name|token
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|iter
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"tokenize("
operator|+
name|expression
operator|+
literal|", "
operator|+
name|token
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a tokenize expression which will tokenize the string with the      * given regex      */
DECL|method|regexTokenize (final Expression<E> expression, String regexTokenizer)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|regexTokenize
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|,
name|String
name|regexTokenizer
parameter_list|)
block|{
specifier|final
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regexTokenizer
argument_list|)
decl_stmt|;
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|String
name|text
init|=
name|evaluateStringExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|pattern
operator|.
name|split
argument_list|(
name|text
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"regexTokenize("
operator|+
name|expression
operator|+
literal|", "
operator|+
name|pattern
operator|.
name|pattern
argument_list|()
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Transforms the expression into a String then performs the regex      * replaceAll to transform the String and return the result      */
DECL|method|regexReplaceAll (final Expression<E> expression, String regex, final String replacement)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|regexReplaceAll
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|,
name|String
name|regex
parameter_list|,
specifier|final
name|String
name|replacement
parameter_list|)
block|{
specifier|final
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|)
decl_stmt|;
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|String
name|text
init|=
name|evaluateStringExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|pattern
operator|.
name|matcher
argument_list|(
name|text
argument_list|)
operator|.
name|replaceAll
argument_list|(
name|replacement
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"regexReplaceAll("
operator|+
name|expression
operator|+
literal|", "
operator|+
name|pattern
operator|.
name|pattern
argument_list|()
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Transforms the expression into a String then performs the regex      * replaceAll to transform the String and return the result      */
DECL|method|regexReplaceAll (final Expression<E> expression, String regex, final Expression<E> replacementExpression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|regexReplaceAll
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|,
name|String
name|regex
parameter_list|,
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|replacementExpression
parameter_list|)
block|{
specifier|final
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|)
decl_stmt|;
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|String
name|text
init|=
name|evaluateStringExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|replacement
init|=
name|evaluateStringExpression
argument_list|(
name|replacementExpression
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|==
literal|null
operator|||
name|replacement
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|pattern
operator|.
name|matcher
argument_list|(
name|text
argument_list|)
operator|.
name|replaceAll
argument_list|(
name|replacement
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"regexReplaceAll("
operator|+
name|expression
operator|+
literal|", "
operator|+
name|pattern
operator|.
name|pattern
argument_list|()
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Appends the String evaluations of the two expressions together      */
DECL|method|append (final Expression<E> left, final Expression<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|append
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|left
parameter_list|,
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|right
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|evaluateStringExpression
argument_list|(
name|left
argument_list|,
name|exchange
argument_list|)
operator|+
name|evaluateStringExpression
argument_list|(
name|right
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"append("
operator|+
name|left
operator|+
literal|", "
operator|+
name|right
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Evaluates the expression on the given exchange and returns the String      * representation      *      * @param expression the expression to evaluate      * @param exchange the exchange to use to evaluate the expression      * @return the String representation of the expression or null if it could      *         not be evaluated      */
DECL|method|evaluateStringExpression (Expression<E> expression, E exchange)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|String
name|evaluateStringExpression
parameter_list|(
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|,
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|value
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns an expression for the given system property      */
DECL|method|systemProperty (final String name)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
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
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
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
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns an expression which returns the string concatenation value of the various      * expressions      *      * @param expressions the expression to be concatenated dynamically      * @return an expression which when evaluated will return the concatenated values      */
DECL|method|concatExpression (final Collection<Expression> expressions)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|concatExpression
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
return|return
name|concatExpression
argument_list|(
name|expressions
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns an expression which returns the string concatenation value of the various      * expressions      *      * @param expressions the expression to be concatenated dynamically      * @param expression the text description of the expression      * @return an expression which when evaluated will return the concatenated values      */
DECL|method|concatExpression (final Collection<Expression> expressions, final String expression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|concatExpression
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|,
specifier|final
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
range|:
name|expressions
control|)
block|{
name|String
name|text
init|=
name|evaluateStringExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
return|return
name|expression
return|;
block|}
else|else
block|{
return|return
literal|"concat"
operator|+
name|expressions
return|;
block|}
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

