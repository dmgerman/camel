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
name|compare
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|PredicateSupport
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
name|impl
operator|.
name|BinaryPredicateSupport
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
name|util
operator|.
name|ObjectHelper
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
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

begin_comment
comment|/**  * A helper class for working with predicates  *  * @version $Revision: 520261 $  */
end_comment

begin_class
DECL|class|PredicateBuilder
specifier|public
class|class
name|PredicateBuilder
block|{
comment|/**      * A helper method to combine multiple predicates by a logical AND      */
DECL|method|and (final Predicate<E> left, final Predicate<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|and
parameter_list|(
specifier|final
name|Predicate
argument_list|<
name|E
argument_list|>
name|left
parameter_list|,
specifier|final
name|Predicate
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
return|return
operator|new
name|PredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|left
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
operator|&&
name|right
operator|.
name|matches
argument_list|(
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
literal|"("
operator|+
name|left
operator|+
literal|") and ("
operator|+
name|right
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * A helper method to combine multiple predicates by a logical OR      */
DECL|method|or (final Predicate<E> left, final Predicate<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|or
parameter_list|(
specifier|final
name|Predicate
argument_list|<
name|E
argument_list|>
name|left
parameter_list|,
specifier|final
name|Predicate
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
return|return
operator|new
name|PredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|left
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
operator|||
name|right
operator|.
name|matches
argument_list|(
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
literal|"("
operator|+
name|left
operator|+
literal|") or ("
operator|+
name|right
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
DECL|method|isEqualTo (final Expression<E> left, final Expression<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isEqualTo
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
name|BinaryPredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
block|{
specifier|protected
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
block|{
return|return
name|ObjectHelper
operator|.
name|equals
argument_list|(
name|leftValue
argument_list|,
name|rightValue
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|"=="
return|;
block|}
block|}
return|;
block|}
DECL|method|isNotEqualTo (final Expression<E> left, final Expression<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isNotEqualTo
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
name|BinaryPredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
block|{
specifier|protected
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
block|{
return|return
operator|!
name|ObjectHelper
operator|.
name|equals
argument_list|(
name|leftValue
argument_list|,
name|rightValue
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|"=="
return|;
block|}
block|}
return|;
block|}
DECL|method|isLessThan (final Expression<E> left, final Expression<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isLessThan
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
name|BinaryPredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
block|{
specifier|protected
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
block|{
return|return
name|compare
argument_list|(
name|leftValue
argument_list|,
name|rightValue
argument_list|)
operator|<
literal|0
return|;
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|"<"
return|;
block|}
block|}
return|;
block|}
DECL|method|isLessThanOrEqualTo (final Expression<E> left, final Expression<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isLessThanOrEqualTo
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
name|BinaryPredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
block|{
specifier|protected
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
block|{
return|return
name|compare
argument_list|(
name|leftValue
argument_list|,
name|rightValue
argument_list|)
operator|<=
literal|0
return|;
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|"<="
return|;
block|}
block|}
return|;
block|}
DECL|method|isGreaterThan (final Expression<E> left, final Expression<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isGreaterThan
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
name|BinaryPredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
block|{
specifier|protected
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
block|{
return|return
name|compare
argument_list|(
name|leftValue
argument_list|,
name|rightValue
argument_list|)
operator|>
literal|0
return|;
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|">"
return|;
block|}
block|}
return|;
block|}
DECL|method|isGreaterThanOrEqualTo (final Expression<E> left, final Expression<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isGreaterThanOrEqualTo
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
name|BinaryPredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
block|{
specifier|protected
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
block|{
return|return
name|compare
argument_list|(
name|leftValue
argument_list|,
name|rightValue
argument_list|)
operator|<
literal|0
return|;
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|">="
return|;
block|}
block|}
return|;
block|}
DECL|method|contains (final Expression<E> left, final Expression<E> right)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|contains
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
name|BinaryPredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
block|{
specifier|protected
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
block|{
return|return
name|ObjectHelper
operator|.
name|contains
argument_list|(
name|leftValue
argument_list|,
name|rightValue
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|"contains"
return|;
block|}
block|}
return|;
block|}
DECL|method|isNull (final Expression<E> expression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isNull
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
return|return
name|isEqualTo
argument_list|(
name|expression
argument_list|,
operator|(
name|Expression
argument_list|<
name|E
argument_list|>
operator|)
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
literal|null
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isNotNull (final Expression<E> expression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isNotNull
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
return|return
name|isNotEqualTo
argument_list|(
name|expression
argument_list|,
operator|(
name|Expression
argument_list|<
name|E
argument_list|>
operator|)
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
literal|null
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isInstanceOf (final Expression<E> expression, final Class type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|isInstanceOf
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|,
specifier|final
name|Class
name|type
parameter_list|)
block|{
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|type
argument_list|,
literal|"type"
argument_list|)
expr_stmt|;
return|return
operator|new
name|PredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|matches
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
name|type
operator|.
name|isInstance
argument_list|(
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
name|expression
operator|+
literal|" instanceof "
operator|+
name|type
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|super
operator|.
name|assertionFailureMessage
argument_list|(
name|exchange
argument_list|)
operator|+
literal|" for<"
operator|+
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
operator|+
literal|">"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a predicate which is true if the expression matches the given regular expression      *      * @param expression the expression to evaluate      * @param regex the regular expression to match against      * @return a new predicate      */
DECL|method|regex (final Expression<E> expression, final String regex)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|regex
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
name|regex
parameter_list|)
block|{
return|return
name|regex
argument_list|(
name|expression
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate which is true if the expression matches the given regular expression      *      * @param expression the expression to evaluate      * @param pattern the regular expression to match against      * @return a new predicate      */
DECL|method|regex (final Expression<E> expression, final Pattern pattern)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|regex
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|,
specifier|final
name|Pattern
name|pattern
parameter_list|)
block|{
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|pattern
argument_list|,
literal|"pattern"
argument_list|)
expr_stmt|;
return|return
operator|new
name|PredicateSupport
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|matches
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
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|matcher
operator|.
name|matches
argument_list|()
return|;
block|}
return|return
literal|false
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
name|expression
operator|+
literal|".matches("
operator|+
name|pattern
operator|+
literal|")"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|super
operator|.
name|assertionFailureMessage
argument_list|(
name|exchange
argument_list|)
operator|+
literal|" for<"
operator|+
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
operator|+
literal|">"
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

