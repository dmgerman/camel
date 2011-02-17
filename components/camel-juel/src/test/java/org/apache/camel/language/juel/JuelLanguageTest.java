begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.juel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|juel
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JuelLanguageTest
specifier|public
class|class
name|JuelLanguageTest
extends|extends
name|LanguageTestSupport
block|{
DECL|method|testElExpressions ()
specifier|public
name|void
name|testElExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${exchange}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.headers.foo}"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.body}"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
block|}
DECL|method|testElPredicates ()
specifier|public
name|void
name|testElPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPredicate
argument_list|(
literal|"${in.headers.foo.startsWith('a')}"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"${in.headers.foo == 'abc'}"
argument_list|)
expr_stmt|;
name|assertPredicateFails
argument_list|(
literal|"${in.headers.foo == 'badString'}"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"${in.headers['foo'] == 'abc'}"
argument_list|)
expr_stmt|;
name|assertPredicateFails
argument_list|(
literal|"${in.headers['foo'] == 'badString'}"
argument_list|)
expr_stmt|;
block|}
DECL|method|getLanguageName ()
specifier|protected
name|String
name|getLanguageName
parameter_list|()
block|{
return|return
literal|"el"
return|;
block|}
block|}
end_class

end_unit

