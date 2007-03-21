begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_comment
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|ExpressionBuilder
specifier|public
class|class
name|ExpressionBuilder
block|{
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
name|getHeaders
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
name|getHeaders
argument_list|()
operator|.
name|getHeader
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
comment|/**      * Returns the expression for the exchanges inbound message body converted to the given type      */
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
return|return
name|exchange
operator|.
name|getOut
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
literal|"outBody"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a tokenize expression which will tokenize the string with the given token      */
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
name|String
name|text
init|=
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
decl_stmt|;
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
block|}
end_class

end_unit

