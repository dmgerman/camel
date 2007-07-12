begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
comment|/**  * A useful base class for {@link Predicate} implementations  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|BinaryPredicateSupport
specifier|public
specifier|abstract
class|class
name|BinaryPredicateSupport
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
implements|implements
name|Predicate
argument_list|<
name|E
argument_list|>
block|{
DECL|field|left
specifier|private
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|left
decl_stmt|;
DECL|field|right
specifier|private
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|right
decl_stmt|;
DECL|method|BinaryPredicateSupport (Expression<E> left, Expression<E> right)
specifier|protected
name|BinaryPredicateSupport
parameter_list|(
name|Expression
argument_list|<
name|E
argument_list|>
name|left
parameter_list|,
name|Expression
argument_list|<
name|E
argument_list|>
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
DECL|method|matches (E exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|leftValue
init|=
name|left
operator|.
name|evaluate
argument_list|(
name|exchange
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
argument_list|)
decl_stmt|;
return|return
name|matches
argument_list|(
name|exchange
argument_list|,
name|leftValue
argument_list|,
name|rightValue
argument_list|)
return|;
block|}
DECL|method|assertMatches (String text, E exchange)
specifier|public
name|void
name|assertMatches
parameter_list|(
name|String
name|text
parameter_list|,
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|leftValue
init|=
name|left
operator|.
name|evaluate
argument_list|(
name|exchange
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
throw|throw
operator|new
name|AssertionError
argument_list|(
name|text
operator|+
name|assertionFailureMessage
argument_list|(
name|exchange
argument_list|,
name|leftValue
argument_list|,
name|rightValue
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|matches (E exchange, Object leftValue, Object rightValue)
specifier|protected
specifier|abstract
name|boolean
name|matches
parameter_list|(
name|E
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
DECL|method|assertionFailureMessage (E exchange, Object leftValue, Object rightValue)
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|E
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
return|return
name|this
operator|+
literal|" failed on "
operator|+
name|exchange
operator|+
literal|" with left value<"
operator|+
name|leftValue
operator|+
literal|"> right value<"
operator|+
name|rightValue
operator|+
literal|">"
return|;
block|}
block|}
end_class

end_unit

