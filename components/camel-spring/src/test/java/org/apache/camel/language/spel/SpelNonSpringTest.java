begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.spel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|spel
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
name|CamelContext
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
name|ExpressionEvaluationException
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
name|impl
operator|.
name|DefaultCamelContext
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
name|SimpleRegistry
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
name|spel
operator|.
name|bean
operator|.
name|Dummy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test access to beans defined in non-Spring context from SpEL expressions/predicates.  */
end_comment

begin_class
DECL|class|SpelNonSpringTest
specifier|public
class|class
name|SpelNonSpringTest
extends|extends
name|LanguageTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"myDummy"
argument_list|,
operator|new
name|Dummy
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testSpelBeanExpressions ()
specifier|public
name|void
name|testSpelBeanExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"#{@myDummy.foo == 'xyz'}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{@myDummy.bar == 789}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{@myDummy.bar.toString()}"
argument_list|,
literal|"789"
argument_list|)
expr_stmt|;
try|try
block|{
name|assertExpression
argument_list|(
literal|"#{@notFound}"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionEvaluationException
name|ex
parameter_list|)
block|{
name|assertStringContains
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"Could not resolve bean reference against Registry"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSpelBeanPredicates ()
specifier|public
name|void
name|testSpelBeanPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPredicate
argument_list|(
literal|"@myDummy.foo == 'xyz'"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"@myDummy.bar == 789"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"@myDummy.bar instanceof T(Integer)"
argument_list|)
expr_stmt|;
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
literal|"spel"
return|;
block|}
block|}
end_class

end_unit

