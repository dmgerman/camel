begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|Comparator
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
comment|/**  * A builder of expressions or predicates based on values.  */
end_comment

begin_class
DECL|class|ValueBuilder
specifier|public
class|class
name|ValueBuilder
implements|implements
name|Expression
implements|,
name|Predicate
block|{
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|field|not
specifier|private
name|boolean
name|not
decl_stmt|;
DECL|method|ValueBuilder (Expression expression)
specifier|public
name|ValueBuilder
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
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
return|return
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
return|;
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
return|return
name|PredicateBuilder
operator|.
name|toPredicate
argument_list|(
name|getExpression
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|expression
operator|.
name|toString
argument_list|()
return|;
block|}
comment|// Predicate builders
comment|// -------------------------------------------------------------------------
DECL|method|isNotEqualTo (Object value)
specifier|public
name|Predicate
name|isNotEqualTo
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isNotEqualTo
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isEqualTo (Object value)
specifier|public
name|Predicate
name|isEqualTo
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isEqualTo
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isEqualToIgnoreCase (Object value)
specifier|public
name|Predicate
name|isEqualToIgnoreCase
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isEqualToIgnoreCase
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isLessThan (Object value)
specifier|public
name|Predicate
name|isLessThan
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isLessThan
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isLessThanOrEqualTo (Object value)
specifier|public
name|Predicate
name|isLessThanOrEqualTo
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isLessThanOrEqualTo
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isGreaterThan (Object value)
specifier|public
name|Predicate
name|isGreaterThan
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isGreaterThan
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isGreaterThanOrEqualTo (Object value)
specifier|public
name|Predicate
name|isGreaterThanOrEqualTo
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isGreaterThanOrEqualTo
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isInstanceOf (Class<?> type)
specifier|public
name|Predicate
name|isInstanceOf
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isInstanceOf
argument_list|(
name|expression
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isNull ()
specifier|public
name|Predicate
name|isNull
parameter_list|()
block|{
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isNull
argument_list|(
name|expression
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isNotNull ()
specifier|public
name|Predicate
name|isNotNull
parameter_list|()
block|{
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isNotNull
argument_list|(
name|expression
argument_list|)
argument_list|)
return|;
block|}
DECL|method|not (Predicate predicate)
specifier|public
name|Predicate
name|not
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|not
argument_list|(
name|predicate
argument_list|)
argument_list|)
return|;
block|}
DECL|method|in (Object... values)
specifier|public
name|Predicate
name|in
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|List
argument_list|<
name|Predicate
argument_list|>
name|predicates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|right
operator|=
name|ExpressionBuilder
operator|.
name|convertToExpression
argument_list|(
name|right
argument_list|,
name|expression
argument_list|)
expr_stmt|;
name|Predicate
name|predicate
init|=
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|isEqualTo
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
decl_stmt|;
name|predicates
operator|.
name|add
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
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
name|predicates
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
DECL|method|in (Predicate... predicates)
specifier|public
name|Predicate
name|in
parameter_list|(
name|Predicate
modifier|...
name|predicates
parameter_list|)
block|{
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|in
argument_list|(
name|predicates
argument_list|)
argument_list|)
return|;
block|}
DECL|method|startsWith (Object value)
specifier|public
name|Predicate
name|startsWith
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|startsWith
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
DECL|method|endsWith (Object value)
specifier|public
name|Predicate
name|endsWith
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|endsWith
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Create a predicate that the left hand expression contains the value of      * the right hand expression      *       * @param value the element which is compared to be contained within this      *                expression      * @return a predicate which evaluates to true if the given value expression      *         is contained within this expression value      */
DECL|method|contains (Object value)
specifier|public
name|Predicate
name|contains
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|right
init|=
name|asExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|contains
argument_list|(
name|expression
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a predicate which is true if this expression matches the given      * regular expression      *       * @param regex the regular expression to match      * @return a predicate which evaluates to true if the expression matches the      *         regex      */
DECL|method|regex (String regex)
specifier|public
name|Predicate
name|regex
parameter_list|(
name|String
name|regex
parameter_list|)
block|{
return|return
name|onNewPredicate
argument_list|(
name|PredicateBuilder
operator|.
name|regex
argument_list|(
name|expression
argument_list|,
name|regex
argument_list|)
argument_list|)
return|;
block|}
comment|// Expression builders
comment|// -------------------------------------------------------------------------
DECL|method|tokenize ()
specifier|public
name|ValueBuilder
name|tokenize
parameter_list|()
block|{
return|return
name|tokenize
argument_list|(
literal|"\n"
argument_list|)
return|;
block|}
DECL|method|tokenize (String token)
specifier|public
name|ValueBuilder
name|tokenize
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|tokenizeExpression
argument_list|(
name|expression
argument_list|,
name|token
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
DECL|method|tokenize (String token, int group, boolean skipFirst)
specifier|public
name|ValueBuilder
name|tokenize
parameter_list|(
name|String
name|token
parameter_list|,
name|int
name|group
parameter_list|,
name|boolean
name|skipFirst
parameter_list|)
block|{
return|return
name|tokenize
argument_list|(
name|token
argument_list|,
literal|""
operator|+
name|group
argument_list|,
name|skipFirst
argument_list|)
return|;
block|}
DECL|method|tokenize (String token, String group, boolean skipFirst)
specifier|public
name|ValueBuilder
name|tokenize
parameter_list|(
name|String
name|token
parameter_list|,
name|String
name|group
parameter_list|,
name|boolean
name|skipFirst
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|tokenizeExpression
argument_list|(
name|expression
argument_list|,
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
operator|&&
name|skipFirst
condition|)
block|{
comment|// wrap in skip first (if group then it has its own skip first logic)
name|newExp
operator|=
name|ExpressionBuilder
operator|.
name|skipFirstExpression
argument_list|(
name|newExp
argument_list|)
expr_stmt|;
block|}
name|newExp
operator|=
name|ExpressionBuilder
operator|.
name|groupIteratorExpression
argument_list|(
name|newExp
argument_list|,
name|token
argument_list|,
name|group
argument_list|,
name|skipFirst
argument_list|)
expr_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Tokenizes the string conversion of this expression using the given      * regular expression      */
DECL|method|regexTokenize (String regex)
specifier|public
name|ValueBuilder
name|regexTokenize
parameter_list|(
name|String
name|regex
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|regexTokenizeExpression
argument_list|(
name|expression
argument_list|,
name|regex
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Replaces all occurrences of the regular expression with the given      * replacement      */
DECL|method|regexReplaceAll (String regex, String replacement)
specifier|public
name|ValueBuilder
name|regexReplaceAll
parameter_list|(
name|String
name|regex
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|regexReplaceAll
argument_list|(
name|expression
argument_list|,
name|regex
argument_list|,
name|replacement
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Replaces all occurrences of the regular expression with the given      * replacement      */
DECL|method|regexReplaceAll (String regex, Expression replacement)
specifier|public
name|ValueBuilder
name|regexReplaceAll
parameter_list|(
name|String
name|regex
parameter_list|,
name|Expression
name|replacement
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|regexReplaceAll
argument_list|(
name|expression
argument_list|,
name|regex
argument_list|,
name|replacement
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Converts the current value to the given type using the registered type      * converters      *       * @param type the type to convert the value to      * @return the current builder      */
DECL|method|convertTo (Class<?> type)
specifier|public
name|ValueBuilder
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|convertToExpression
argument_list|(
name|expression
argument_list|,
name|type
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Converts the current value to a String using the registered type converters      *       * @return the current builder      */
DECL|method|convertToString ()
specifier|public
name|ValueBuilder
name|convertToString
parameter_list|()
block|{
return|return
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Appends the string evaluation of this expression with the given value      *      * @param value the value or expression to append      * @return the current builder      */
DECL|method|append (Object value)
specifier|public
name|ValueBuilder
name|append
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|append
argument_list|(
name|expression
argument_list|,
name|asExpression
argument_list|(
name|value
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Prepends the string evaluation of this expression with the given value      *      * @param value the value or expression to prepend      * @return the current builder      */
DECL|method|prepend (Object value)
specifier|public
name|ValueBuilder
name|prepend
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|prepend
argument_list|(
name|expression
argument_list|,
name|asExpression
argument_list|(
name|value
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Sorts the current value using the given comparator. The current value must be convertable      * to a {@link List} to allow sorting using the comparator.      *      * @param comparator  the comparator used by sorting      * @return the current builder      */
DECL|method|sort (Comparator<?> comparator)
specifier|public
name|ValueBuilder
name|sort
parameter_list|(
name|Comparator
argument_list|<
name|?
argument_list|>
name|comparator
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|sortExpression
argument_list|(
name|expression
argument_list|,
name|comparator
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
comment|/**      * Negates the built expression.      *      * @return the current builder      */
DECL|method|not ()
specifier|public
name|ValueBuilder
name|not
parameter_list|()
block|{
name|not
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * A strategy method to allow derived classes to deal with the newly created      * predicate in different ways      */
DECL|method|onNewPredicate (Predicate predicate)
specifier|protected
name|Predicate
name|onNewPredicate
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
if|if
condition|(
name|not
condition|)
block|{
return|return
name|PredicateBuilder
operator|.
name|not
argument_list|(
name|predicate
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|predicate
return|;
block|}
block|}
DECL|method|asExpression (Object value)
specifier|protected
name|Expression
name|asExpression
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Expression
condition|)
block|{
return|return
operator|(
name|Expression
operator|)
name|value
return|;
block|}
else|else
block|{
return|return
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
DECL|method|onNewValueBuilder (Expression exp)
specifier|protected
name|ValueBuilder
name|onNewValueBuilder
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
block|}
end_class

end_unit

