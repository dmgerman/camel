begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|ValueBuilder
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
name|builder
operator|.
name|ExpressionBuilder
operator|.
name|bodyExpression
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
name|builder
operator|.
name|ExpressionBuilder
operator|.
name|headerExpression
import|;
end_import

begin_comment
comment|/**  * A builder of assertions on message exchanges  *   * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|AssertionClause
specifier|public
specifier|abstract
class|class
name|AssertionClause
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
implements|implements
name|Runnable
block|{
DECL|field|predicates
specifier|private
name|List
argument_list|<
name|Predicate
argument_list|<
name|E
argument_list|>
argument_list|>
name|predicates
init|=
operator|new
name|ArrayList
argument_list|<
name|Predicate
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// Builder methods
comment|// -------------------------------------------------------------------------
comment|/**      * Adds the given predicate to this assertion clause      */
DECL|method|predicate (Predicate<E> predicate)
specifier|public
name|AssertionClause
argument_list|<
name|E
argument_list|>
name|predicate
parameter_list|(
name|Predicate
argument_list|<
name|E
argument_list|>
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
comment|/**      * Returns a predicate and value builder for headers on an exchange      */
DECL|method|header (String name)
specifier|public
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|header
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|headerExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|PredicateValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound body on an exchange      */
DECL|method|body ()
specifier|public
name|PredicateValueBuilder
name|body
parameter_list|()
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|bodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|PredicateValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound message body as a      * specific type      */
DECL|method|bodyAs (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|PredicateValueBuilder
name|bodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|bodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|PredicateValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound body on an      * exchange      */
DECL|method|outBody ()
specifier|public
name|PredicateValueBuilder
name|outBody
parameter_list|()
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|bodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|PredicateValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound message body as a      * specific type      */
DECL|method|outBody (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|PredicateValueBuilder
name|outBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|bodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|PredicateValueBuilder
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Performs any assertions on the given exchange      */
DECL|method|applyAssertionOn (MockEndpoint endpoint, int index, E exchange)
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
name|E
name|exchange
parameter_list|)
block|{
for|for
control|(
name|Predicate
argument_list|<
name|E
argument_list|>
name|predicate
range|:
name|predicates
control|)
block|{
name|predicate
operator|.
name|assertMatches
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|" "
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addPredicate (Predicate<E> predicate)
specifier|protected
name|void
name|addPredicate
parameter_list|(
name|Predicate
argument_list|<
name|E
argument_list|>
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
DECL|class|PredicateValueBuilder
specifier|public
class|class
name|PredicateValueBuilder
extends|extends
name|ValueBuilder
argument_list|<
name|E
argument_list|>
block|{
DECL|method|PredicateValueBuilder (Expression<E> expression)
specifier|public
name|PredicateValueBuilder
parameter_list|(
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|onNewPredicate (Predicate<E> predicate)
specifier|protected
name|Predicate
argument_list|<
name|E
argument_list|>
name|onNewPredicate
parameter_list|(
name|Predicate
argument_list|<
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
name|addPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
return|return
name|predicate
return|;
block|}
block|}
block|}
end_class

end_unit

