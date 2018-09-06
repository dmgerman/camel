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
name|BinaryPredicate
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
name|Predicate
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * A useful base class for {@link Predicate} implementations  *  * @version   */
end_comment

begin_class
DECL|class|BinaryPredicateSupport
specifier|public
specifier|abstract
class|class
name|BinaryPredicateSupport
implements|implements
name|BinaryPredicate
block|{
DECL|field|left
specifier|private
specifier|final
name|Expression
name|left
decl_stmt|;
DECL|field|right
specifier|private
specifier|final
name|Expression
name|right
decl_stmt|;
DECL|method|BinaryPredicateSupport (Expression left, Expression right)
specifier|protected
name|BinaryPredicateSupport
parameter_list|(
name|Expression
name|left
parameter_list|,
name|Expression
name|right
parameter_list|)
block|{
name|notNull
argument_list|(
name|left
argument_list|,
literal|"left"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|right
argument_list|,
literal|"right"
argument_list|)
expr_stmt|;
name|this
operator|.
name|left
operator|=
name|left
expr_stmt|;
name|this
operator|.
name|right
operator|=
name|right
expr_stmt|;
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
name|left
operator|+
literal|" "
operator|+
name|getOperationText
argument_list|()
operator|+
literal|" "
operator|+
name|right
return|;
block|}
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|matchesReturningFailureMessage
argument_list|(
name|exchange
argument_list|)
operator|==
literal|null
return|;
block|}
DECL|method|matchesReturningFailureMessage (Exchange exchange)
specifier|public
name|String
name|matchesReturningFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// we must not store any state, so we can be thread safe
comment|// and thus we offer this method which returns a failure message if
comment|// we did not match
name|String
name|answer
init|=
literal|null
decl_stmt|;
comment|// must be thread safe and store result in local objects
name|Object
name|leftValue
init|=
name|left
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|rightValue
init|=
name|right
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matches
argument_list|(
name|exchange
argument_list|,
name|leftValue
argument_list|,
name|rightValue
argument_list|)
condition|)
block|{
name|answer
operator|=
name|leftValue
operator|+
literal|" "
operator|+
name|getOperator
argument_list|()
operator|+
literal|" "
operator|+
name|rightValue
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|matches (Exchange exchange, Object leftValue, Object rightValue)
specifier|protected
specifier|abstract
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
function_decl|;
DECL|method|getOperationText ()
specifier|protected
specifier|abstract
name|String
name|getOperationText
parameter_list|()
function_decl|;
DECL|method|getLeft ()
specifier|public
name|Expression
name|getLeft
parameter_list|()
block|{
return|return
name|left
return|;
block|}
DECL|method|getRight ()
specifier|public
name|Expression
name|getRight
parameter_list|()
block|{
return|return
name|right
return|;
block|}
DECL|method|getOperator ()
specifier|public
name|String
name|getOperator
parameter_list|()
block|{
return|return
name|getOperationText
argument_list|()
return|;
block|}
block|}
end_class

end_unit

