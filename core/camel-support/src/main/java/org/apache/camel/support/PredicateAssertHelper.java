begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A helper for doing {@link Predicate} assertions.  */
end_comment

begin_class
DECL|class|PredicateAssertHelper
specifier|public
specifier|final
class|class
name|PredicateAssertHelper
block|{
DECL|method|PredicateAssertHelper ()
specifier|private
name|PredicateAssertHelper
parameter_list|()
block|{
comment|// Utility class
block|}
DECL|method|assertMatches (Predicate predicate, String text, Exchange exchange)
specifier|public
specifier|static
name|void
name|assertMatches
parameter_list|(
name|Predicate
name|predicate
parameter_list|,
name|String
name|text
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|predicate
argument_list|,
literal|"predicate"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|exchange
argument_list|,
literal|"exchange"
argument_list|)
expr_stmt|;
if|if
condition|(
name|predicate
operator|instanceof
name|BinaryPredicate
condition|)
block|{
comment|// with binary evaluations as we can get more detailed information
name|BinaryPredicate
name|eval
init|=
operator|(
name|BinaryPredicate
operator|)
name|predicate
decl_stmt|;
name|String
name|evalText
init|=
name|eval
operator|.
name|matchesReturningFailureMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|evalText
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|text
operator|+
name|predicate
operator|+
literal|" evaluated as: "
operator|+
name|evalText
operator|+
literal|" on "
operator|+
name|exchange
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|doAssertMatches
argument_list|(
name|predicate
argument_list|,
name|text
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doAssertMatches (Predicate predicate, String text, Exchange exchange)
specifier|private
specifier|static
name|void
name|doAssertMatches
parameter_list|(
name|Predicate
name|predicate
parameter_list|,
name|String
name|text
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
operator|!
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|predicate
operator|+
literal|" on "
operator|+
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|text
operator|+
name|predicate
operator|+
literal|" on "
operator|+
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

