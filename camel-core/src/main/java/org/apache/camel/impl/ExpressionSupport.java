begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|builder
operator|.
name|PredicateBuilder
operator|.
name|evaluateValuePredicate
import|;
end_import

begin_comment
comment|/**  * A useful base class for {@link Predicate} and {@link Expression} implementations  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExpressionSupport
specifier|public
specifier|abstract
class|class
name|ExpressionSupport
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
implements|implements
name|Expression
argument_list|<
name|E
argument_list|>
implements|,
name|Predicate
argument_list|<
name|E
argument_list|>
block|{
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
name|value
init|=
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|evaluateValuePredicate
argument_list|(
name|value
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
if|if
condition|(
operator|!
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|text
operator|+
literal|" "
operator|+
name|assertionFailureMessage
argument_list|(
name|exchange
argument_list|)
operator|+
literal|" for exchange: "
operator|+
name|exchange
argument_list|)
throw|;
block|}
block|}
DECL|method|assertionFailureMessage (E exchange)
specifier|protected
specifier|abstract
name|String
name|assertionFailureMessage
parameter_list|(
name|E
name|exchange
parameter_list|)
function_decl|;
block|}
end_class

end_unit

