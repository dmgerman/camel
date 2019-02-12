begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Predicate
import|;
end_import

begin_comment
comment|/**  * To adapt {@link Predicate} as an {@link org.apache.camel.Expression}  */
end_comment

begin_class
DECL|class|PredicateToExpressionAdapter
specifier|public
specifier|final
class|class
name|PredicateToExpressionAdapter
implements|implements
name|Expression
block|{
DECL|field|predicate
specifier|private
specifier|final
name|Predicate
name|predicate
decl_stmt|;
DECL|method|PredicateToExpressionAdapter (Predicate predicate)
specifier|public
name|PredicateToExpressionAdapter
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
name|boolean
name|matches
init|=
name|predicate
operator|.
name|matches
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
name|exchange
argument_list|,
name|matches
argument_list|)
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
return|return
name|predicate
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Converts the given predicate into an {@link org.apache.camel.Expression}      */
DECL|method|toExpression (final Predicate predicate)
specifier|public
specifier|static
name|Expression
name|toExpression
parameter_list|(
specifier|final
name|Predicate
name|predicate
parameter_list|)
block|{
return|return
operator|new
name|PredicateToExpressionAdapter
argument_list|(
name|predicate
argument_list|)
return|;
block|}
block|}
end_class

end_unit
