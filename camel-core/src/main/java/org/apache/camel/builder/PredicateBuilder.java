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
name|Arrays
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
name|util
operator|.
name|ExpressionToPredicateAdapter
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

begin_comment
comment|/**  * A helper class for working with predicates  */
end_comment

begin_class
DECL|class|PredicateBuilder
specifier|public
specifier|final
class|class
name|PredicateBuilder
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|PredicateBuilder ()
specifier|private
name|PredicateBuilder
parameter_list|()
block|{     }
comment|/**      * Converts the given expression into an {@link Predicate}      */
DECL|method|toPredicate (final Expression expression)
specifier|public
specifier|static
name|Predicate
name|toPredicate
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
name|ExpressionToPredicateAdapter
operator|.
name|toPredicate
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * A helper method to return the logical not of the given predicate      */
DECL|method|not (final Predicate predicate)
specifier|public
specifier|static
name|Predicate
name|not
parameter_list|(
specifier|final
name|Predicate
name|predicate
parameter_list|)
block|{
name|notNull
argument_list|(
name|predicate
argument_list|,
literal|"predicate"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Predicate
argument_list|()
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|!
name|predicate
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
literal|"not ("
operator|+
name|predicate
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * A helper method to combine multiple predicates by a logical AND      */
DECL|method|and (final Predicate left, final Predicate right)
specifier|public
specifier|static
name|Predicate
name|and
parameter_list|(
specifier|final
name|Predicate
name|left
parameter_list|,
specifier|final
name|Predicate
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
name|Predicate
argument_list|()
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
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
comment|/**      * A helper method to combine two predicates by a logical OR.      * If you want to combine multiple predicates see {@link #in(Predicate...)}      */
DECL|method|or (final Predicate left, final Predicate right)
specifier|public
specifier|static
name|Predicate
name|or
parameter_list|(
specifier|final
name|Predicate
name|left
parameter_list|,
specifier|final
name|Predicate
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
name|Predicate
argument_list|()
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
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
comment|/**      * Concat the given predicates into a single predicate, which matches      * if at least one predicates matches.      *      * @param predicates predicates      * @return a single predicate containing all the predicates      */
DECL|method|or (List<Predicate> predicates)
specifier|public
specifier|static
name|Predicate
name|or
parameter_list|(
name|List
argument_list|<
name|Predicate
argument_list|>
name|predicates
parameter_list|)
block|{
name|Predicate
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Predicate
name|predicate
range|:
name|predicates
control|)
block|{
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|predicate
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|or
argument_list|(
name|answer
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Concat the given predicates into a single predicate, which matches      * if at least one predicates matches.      *      * @param predicates predicates      * @return a single predicate containing all the predicates      */
DECL|method|or (Predicate... predicates)
specifier|public
specifier|static
name|Predicate
name|or
parameter_list|(
name|Predicate
modifier|...
name|predicates
parameter_list|)
block|{
return|return
name|or
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|predicates
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * A helper method to return true if any of the predicates matches.      */
DECL|method|in (final Predicate... predicates)
specifier|public
specifier|static
name|Predicate
name|in
parameter_list|(
specifier|final
name|Predicate
modifier|...
name|predicates
parameter_list|)
block|{
name|notNull
argument_list|(
name|predicates
argument_list|,
literal|"predicates"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Predicate
argument_list|()
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
for|for
control|(
name|Predicate
name|in
range|:
name|predicates
control|)
block|{
if|if
condition|(
name|in
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
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
literal|"in ("
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|predicates
argument_list|)
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * A helper method to return true if any of the predicates matches.      */
DECL|method|in (List<Predicate> predicates)
specifier|public
specifier|static
name|Predicate
name|in
parameter_list|(
name|List
argument_list|<
name|Predicate
argument_list|>
name|predicates
parameter_list|)
block|{
return|return
name|in
argument_list|(
name|predicates
operator|.
name|toArray
argument_list|(
operator|new
name|Predicate
index|[
literal|0
index|]
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isEqualTo (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|isEqualTo
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceEquals
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
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
DECL|method|isEqualToIgnoreCase (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|isEqualToIgnoreCase
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceEquals
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|leftValue
argument_list|,
name|rightValue
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|"=~"
return|;
block|}
block|}
return|;
block|}
DECL|method|isNotEqualTo (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|isNotEqualTo
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|true
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceNotEquals
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
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
literal|"!="
return|;
block|}
block|}
return|;
block|}
DECL|method|isLessThan (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|isLessThan
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceCompare
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
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
DECL|method|isLessThanOrEqualTo (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|isLessThanOrEqualTo
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceCompare
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
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
DECL|method|isGreaterThan (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|isGreaterThan
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceCompare
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
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
DECL|method|isGreaterThanOrEqualTo (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|isGreaterThanOrEqualTo
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceCompare
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|leftValue
argument_list|,
name|rightValue
argument_list|)
operator|>=
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
DECL|method|contains (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|contains
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
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
DECL|method|containsIgnoreCase (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|containsIgnoreCase
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|containsIgnoreCase
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
literal|"~~"
return|;
block|}
block|}
return|;
block|}
DECL|method|isNull (final Expression expression)
specifier|public
specifier|static
name|Predicate
name|isNull
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
argument_list|(
name|expression
argument_list|,
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
literal|null
argument_list|)
argument_list|)
block|{
specifier|protected
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
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
condition|)
block|{
comment|// the left operator is null so its true
return|return
literal|true
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceEquals
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
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
comment|// leave the operation text as "is not" as Camel will insert right and left expression around it
comment|// so it will be displayed as: XXX is null
return|return
literal|"is"
return|;
block|}
block|}
return|;
block|}
DECL|method|isNotNull (final Expression expression)
specifier|public
specifier|static
name|Predicate
name|isNotNull
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
argument_list|(
name|expression
argument_list|,
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
literal|null
argument_list|)
argument_list|)
block|{
specifier|protected
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
block|{
if|if
condition|(
name|leftValue
operator|!=
literal|null
condition|)
block|{
comment|// the left operator is not null so its true
return|return
literal|true
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|typeCoerceNotEquals
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
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
comment|// leave the operation text as "is not" as Camel will insert right and left expression around it
comment|// so it will be displayed as: XXX is not null
return|return
literal|"is not"
return|;
block|}
block|}
return|;
block|}
DECL|method|isInstanceOf (final Expression expression, final Class<?> type)
specifier|public
specifier|static
name|Predicate
name|isInstanceOf
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
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
name|Predicate
argument_list|()
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
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
argument_list|,
name|Object
operator|.
name|class
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
name|getCanonicalName
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|startsWith (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|startsWith
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
name|String
name|leftStr
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
name|leftValue
argument_list|)
decl_stmt|;
name|String
name|rightStr
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
name|rightValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|leftStr
operator|!=
literal|null
operator|&&
name|rightStr
operator|!=
literal|null
condition|)
block|{
return|return
name|leftStr
operator|.
name|startsWith
argument_list|(
name|rightStr
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|"startsWith"
return|;
block|}
block|}
return|;
block|}
DECL|method|endsWith (final Expression left, final Expression right)
specifier|public
specifier|static
name|Predicate
name|endsWith
parameter_list|(
specifier|final
name|Expression
name|left
parameter_list|,
specifier|final
name|Expression
name|right
parameter_list|)
block|{
return|return
operator|new
name|BinaryPredicateSupport
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|leftValue
parameter_list|,
name|Object
name|rightValue
parameter_list|)
block|{
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|&&
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// they are equal
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|leftValue
operator|==
literal|null
operator|||
name|rightValue
operator|==
literal|null
condition|)
block|{
comment|// only one of them is null so they are not equal
return|return
literal|false
return|;
block|}
name|String
name|leftStr
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
name|leftValue
argument_list|)
decl_stmt|;
name|String
name|rightStr
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
name|rightValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|leftStr
operator|!=
literal|null
operator|&&
name|rightStr
operator|!=
literal|null
condition|)
block|{
return|return
name|leftStr
operator|.
name|endsWith
argument_list|(
name|rightStr
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|protected
name|String
name|getOperationText
parameter_list|()
block|{
return|return
literal|"endsWith"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a predicate which is true if the expression matches the given      * regular expression      *      * @param expression the expression to evaluate      * @param regex the regular expression to match against      * @return a new predicate      */
DECL|method|regex (final Expression expression, final String regex)
specifier|public
specifier|static
name|Predicate
name|regex
parameter_list|(
specifier|final
name|Expression
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
comment|/**      * Returns a predicate which is true if the expression matches the given      * regular expression      *      * @param expression the expression to evaluate      * @param pattern the regular expression to match against      * @return a new predicate      */
DECL|method|regex (final Expression expression, final Pattern pattern)
specifier|public
specifier|static
name|Predicate
name|regex
parameter_list|(
specifier|final
name|Expression
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
name|Predicate
argument_list|()
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|value
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
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
literal|".matches('"
operator|+
name|pattern
operator|+
literal|"')"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Concat the given predicates into a single predicate, which      * only matches if all the predicates matches.      *      * @param predicates predicates      * @return a single predicate containing all the predicates      */
DECL|method|and (List<Predicate> predicates)
specifier|public
specifier|static
name|Predicate
name|and
parameter_list|(
name|List
argument_list|<
name|Predicate
argument_list|>
name|predicates
parameter_list|)
block|{
name|Predicate
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Predicate
name|predicate
range|:
name|predicates
control|)
block|{
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|predicate
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|and
argument_list|(
name|answer
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Concat the given predicates into a single predicate, which only matches      * if all the predicates matches.      *      * @param predicates predicates      * @return a single predicate containing all the predicates      */
DECL|method|and (Predicate... predicates)
specifier|public
specifier|static
name|Predicate
name|and
parameter_list|(
name|Predicate
modifier|...
name|predicates
parameter_list|)
block|{
return|return
name|and
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|predicates
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * A constant predicate.      *      * @param answer the constant matches      * @return a predicate that always returns the given answer.      */
DECL|method|constant (final boolean answer)
specifier|public
specifier|static
name|Predicate
name|constant
parameter_list|(
specifier|final
name|boolean
name|answer
parameter_list|)
block|{
return|return
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
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
literal|""
operator|+
name|answer
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

