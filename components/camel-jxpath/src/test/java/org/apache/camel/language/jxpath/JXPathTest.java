begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.jxpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|jxpath
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
name|LanguageTestSupport
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
name|language
operator|.
name|ExpressionEvaluationException
import|;
end_import

begin_comment
comment|/**  * Test for {@link JXPathExpression} and {@link JXPathLanguage}  */
end_comment

begin_class
DECL|class|JXPathTest
specifier|public
class|class
name|JXPathTest
extends|extends
name|LanguageTestSupport
block|{
comment|/** 	 * Test JXPath expressions 	 */
DECL|method|testJXPathExpressions ()
specifier|public
name|void
name|testJXPathExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"./in/body"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"in/body"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"in/headers"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"in/headers/@foo"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Test JXPath predicates 	 */
DECL|method|testJXPathPredicates ()
specifier|public
name|void
name|testJXPathPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPredicate
argument_list|(
literal|"in/headers/@foo = 'abc'"
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Test exceptions being thrown appropriately 	 */
DECL|method|testExceptions ()
specifier|public
name|void
name|testExceptions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertInvalidExpression
argument_list|(
literal|".@."
argument_list|)
expr_stmt|;
name|assertInvalidExpression
argument_list|(
literal|"ins/body"
argument_list|)
expr_stmt|;
name|assertInvalidPredicate
argument_list|(
literal|"in/body"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertInvalidExpression (String expression)
specifier|private
name|void
name|assertInvalidExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
try|try
block|{
name|assertExpression
argument_list|(
name|expression
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected an ExpressionEvaluationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionEvaluationException
name|e
parameter_list|)
block|{
comment|//nothing to do -- test success
block|}
block|}
DECL|method|assertInvalidPredicate (String predicate)
specifier|private
name|void
name|assertInvalidPredicate
parameter_list|(
name|String
name|predicate
parameter_list|)
block|{
try|try
block|{
name|assertPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected an ExpressionEvaluationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionEvaluationException
name|e
parameter_list|)
block|{
comment|//nothing to do -- test success
block|}
block|}
annotation|@
name|Override
DECL|method|getLanguageName ()
specifier|protected
name|String
name|getLanguageName
parameter_list|()
block|{
return|return
literal|"jxpath"
return|;
block|}
block|}
end_class

end_unit

