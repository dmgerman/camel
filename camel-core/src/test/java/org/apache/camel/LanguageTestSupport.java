begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * A useful base class for testing the language plugins in Camel  * @version $Revision$  */
end_comment

begin_class
DECL|class|LanguageTestSupport
specifier|public
specifier|abstract
class|class
name|LanguageTestSupport
extends|extends
name|ExchangeTestSupport
block|{
DECL|method|getLanguageName ()
specifier|protected
specifier|abstract
name|String
name|getLanguageName
parameter_list|()
function_decl|;
comment|/**      * Asserts that the given predicate expression evaluated on the current language and message      * exchange evaluates to true      */
DECL|method|assertPredicate (String expression)
specifier|protected
name|void
name|assertPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|assertPredicate
argument_list|(
name|exchange
argument_list|,
name|expression
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the given predicate expression evaluated on the current language and message      * exchange evaluates to false      */
DECL|method|assertPredicateFails (String expression)
specifier|protected
name|void
name|assertPredicateFails
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|assertPredicate
argument_list|(
name|exchange
argument_list|,
name|expression
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the given predicate expression evaluated on the current language and message      * exchange evaluates to the expected value      */
DECL|method|assertPredicate (String expression, boolean expected)
specifier|protected
name|void
name|assertPredicate
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|expected
parameter_list|)
block|{
name|assertPredicate
argument_list|(
name|exchange
argument_list|,
name|expression
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|assertPredicate (Exchange exchange, String expression, boolean expected)
specifier|protected
name|void
name|assertPredicate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|expression
parameter_list|,
name|boolean
name|expected
parameter_list|)
block|{
name|assertPredicate
argument_list|(
name|getLanguageName
argument_list|()
argument_list|,
name|expression
argument_list|,
name|exchange
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that this language expression evaluates to the given value on the given exchange      */
DECL|method|assertExpression (Exchange exchange, String expressionText, Object expectedValue)
specifier|protected
name|void
name|assertExpression
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|expressionText
parameter_list|,
name|Object
name|expectedValue
parameter_list|)
block|{
name|assertExpression
argument_list|(
name|exchange
argument_list|,
name|getLanguageName
argument_list|()
argument_list|,
name|expressionText
argument_list|,
name|expectedValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that this language expression evaluates to the given value on the current exchange      */
DECL|method|assertExpression (String expressionText, Object expectedValue)
specifier|protected
name|void
name|assertExpression
parameter_list|(
name|String
name|expressionText
parameter_list|,
name|Object
name|expectedValue
parameter_list|)
block|{
name|assertExpression
argument_list|(
name|exchange
argument_list|,
name|expressionText
argument_list|,
name|expectedValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

