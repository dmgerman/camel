begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|LanguageTestSupport
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

begin_class
DECL|class|SpelTest
specifier|public
class|class
name|SpelTest
extends|extends
name|LanguageTestSupport
block|{
annotation|@
name|Test
DECL|method|testSpelExpressions ()
specifier|public
name|void
name|testSpelExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"#{exchange}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{exchange.getIn().body}"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{getRequest().body}"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.body}"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{message.body}"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.Headers['foo']}"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{getRequest().Headers['foo']}"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.Headers['foo'] == 'abc'}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.headers['bar'] == 123}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.headers['bar']> 10}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.Headers.foo}"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{getRequest().Headers.foo}"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.Headers.foo == 'abc'}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.headers.bar == 123}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{request.headers.bar> 10}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"#{6 / -3}"
argument_list|,
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSpelPredicates ()
specifier|public
name|void
name|testSpelPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPredicate
argument_list|(
literal|"#{request.headers['foo'].startsWith('a')}"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"#{request.headers['foo'] == 'abc'}"
argument_list|)
expr_stmt|;
name|assertPredicateFails
argument_list|(
literal|"#{request.headers['foo'] == 'badString'}"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"#{request.headers.foo.startsWith('a')}"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"#{request.headers.foo == 'abc'}"
argument_list|)
expr_stmt|;
name|assertPredicateFails
argument_list|(
literal|"#{request.headers.foo == 'badString'}"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"#{message.headers.foo == 'abc'}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResponseCreatesOutMessage ()
specifier|public
name|void
name|testResponseCreatesOutMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"#{response.body}"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exchange
operator|.
name|hasOut
argument_list|()
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

