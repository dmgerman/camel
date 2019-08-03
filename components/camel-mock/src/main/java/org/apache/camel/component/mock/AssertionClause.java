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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|StreamCache
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
name|PredicateAssertHelper
import|;
end_import

begin_comment
comment|/**  * A builder of assertions on message exchanges  */
end_comment

begin_class
DECL|class|AssertionClause
specifier|public
specifier|abstract
class|class
name|AssertionClause
extends|extends
name|MockExpressionClauseSupport
argument_list|<
name|MockValueBuilder
argument_list|>
implements|implements
name|Runnable
block|{
DECL|field|mock
specifier|protected
specifier|final
name|MockEndpoint
name|mock
decl_stmt|;
DECL|field|currentIndex
specifier|protected
specifier|volatile
name|int
name|currentIndex
decl_stmt|;
DECL|field|predicates
specifier|private
specifier|final
name|Set
argument_list|<
name|Predicate
argument_list|>
name|predicates
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|previous
specifier|private
specifier|final
name|Expression
name|previous
init|=
operator|new
name|PreviousTimestamp
argument_list|()
decl_stmt|;
DECL|field|next
specifier|private
specifier|final
name|Expression
name|next
init|=
operator|new
name|NextTimestamp
argument_list|()
decl_stmt|;
DECL|method|AssertionClause (MockEndpoint mock)
specifier|public
name|AssertionClause
parameter_list|(
name|MockEndpoint
name|mock
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|mock
operator|=
name|mock
expr_stmt|;
block|}
comment|// Builder methods
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|expression (Expression expression)
specifier|public
name|MockValueBuilder
name|expression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
comment|// must override this method as we provide null in the constructor
name|super
operator|.
name|expression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
operator|new
name|PredicateValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|language (ExpressionFactory expression)
specifier|public
name|MockValueBuilder
name|language
parameter_list|(
name|ExpressionFactory
name|expression
parameter_list|)
block|{
comment|// must override this method as we provide null in the constructor
name|super
operator|.
name|expression
argument_list|(
name|expression
operator|.
name|createExpression
argument_list|(
name|mock
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|PredicateValueBuilder
argument_list|(
name|getExpressionValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Adds the given predicate to this assertion clause      */
DECL|method|predicate (Predicate predicate)
specifier|public
name|AssertionClause
name|predicate
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|addPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds the given predicate to this assertion clause      */
DECL|method|predicate ()
specifier|public
name|MockExpressionClause
argument_list|<
name|AssertionClause
argument_list|>
name|predicate
parameter_list|()
block|{
name|MockExpressionClause
argument_list|<
name|AssertionClause
argument_list|>
name|clause
init|=
operator|new
name|MockExpressionClause
argument_list|<>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|addPredicate
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
comment|/**      * Adds a {@link TimeClause} predicate for message arriving.      */
DECL|method|arrives ()
specifier|public
name|TimeClause
name|arrives
parameter_list|()
block|{
specifier|final
name|TimeClause
name|clause
init|=
operator|new
name|TimeClause
argument_list|(
name|previous
argument_list|,
name|next
argument_list|)
decl_stmt|;
name|addPredicate
argument_list|(
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
name|clause
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
literal|"arrives "
operator|+
name|clause
operator|.
name|toString
argument_list|()
operator|+
literal|" exchange"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
comment|/**      * Performs any assertions on the given exchange      */
DECL|method|applyAssertionOn (MockEndpoint endpoint, int index, Exchange exchange)
specifier|protected
name|void
name|applyAssertionOn
parameter_list|(
name|MockEndpoint
name|endpoint
parameter_list|,
name|int
name|index
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
for|for
control|(
name|Predicate
name|predicate
range|:
name|predicates
control|)
block|{
name|currentIndex
operator|=
name|index
expr_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|// if the value is StreamCache then ensure its readable before evaluating any predicates
comment|// by resetting it (this is also what StreamCachingAdvice does)
if|if
condition|(
name|value
operator|instanceof
name|StreamCache
condition|)
block|{
operator|(
operator|(
name|StreamCache
operator|)
name|value
operator|)
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
name|PredicateAssertHelper
operator|.
name|assertMatches
argument_list|(
name|predicate
argument_list|,
literal|"Assertion error at index "
operator|+
name|index
operator|+
literal|" on mock "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|" with predicate: "
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addPredicate (Predicate predicate)
specifier|protected
name|void
name|addPredicate
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|predicates
operator|.
name|add
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|PreviousTimestamp
specifier|private
specifier|final
class|class
name|PreviousTimestamp
implements|implements
name|Expression
block|{
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
name|Date
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|currentIndex
operator|>
literal|0
operator|&&
name|mock
operator|.
name|getReceivedCounter
argument_list|()
operator|>
literal|0
condition|)
block|{
name|answer
operator|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|currentIndex
operator|-
literal|1
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|RECEIVED_TIMESTAMP
argument_list|,
name|Date
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|NextTimestamp
specifier|private
specifier|final
class|class
name|NextTimestamp
implements|implements
name|Expression
block|{
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
name|Date
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|currentIndex
operator|<
name|mock
operator|.
name|getReceivedCounter
argument_list|()
operator|-
literal|1
condition|)
block|{
name|answer
operator|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|currentIndex
operator|+
literal|1
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|RECEIVED_TIMESTAMP
argument_list|,
name|Date
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
block|}
comment|/**      * Public class needed for fluent builders      */
DECL|class|PredicateValueBuilder
specifier|public
specifier|final
class|class
name|PredicateValueBuilder
extends|extends
name|MockValueBuilder
block|{
DECL|method|PredicateValueBuilder (Expression expression)
specifier|public
name|PredicateValueBuilder
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onNewPredicate (Predicate predicate)
specifier|protected
name|Predicate
name|onNewPredicate
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|predicate
operator|=
name|super
operator|.
name|onNewPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
name|addPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
return|return
name|predicate
return|;
block|}
annotation|@
name|Override
DECL|method|onNewValueBuilder (Expression exp)
specifier|protected
name|MockValueBuilder
name|onNewValueBuilder
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|PredicateValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

