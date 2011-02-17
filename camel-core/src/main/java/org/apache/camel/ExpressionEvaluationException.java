begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * An exception thrown if evaluation of the expression failed.  *  * @version   */
end_comment

begin_class
DECL|class|ExpressionEvaluationException
specifier|public
class|class
name|ExpressionEvaluationException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2939802714638174540L
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
specifier|transient
name|Expression
name|expression
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
specifier|transient
name|Exchange
name|exchange
decl_stmt|;
DECL|method|ExpressionEvaluationException (Expression expression, Exchange exchange, Throwable cause)
specifier|public
name|ExpressionEvaluationException
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

