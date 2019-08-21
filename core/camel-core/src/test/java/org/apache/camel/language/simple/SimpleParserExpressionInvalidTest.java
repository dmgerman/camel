begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
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
name|ExchangeTestSupport
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
name|simple
operator|.
name|types
operator|.
name|SimpleIllegalSyntaxException
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
comment|/**  *  */
end_comment

begin_class
DECL|class|SimpleParserExpressionInvalidTest
specifier|public
class|class
name|SimpleParserExpressionInvalidTest
extends|extends
name|ExchangeTestSupport
block|{
annotation|@
name|Test
DECL|method|testSimpleUnbalanceFunction ()
specifier|public
name|void
name|testSimpleUnbalanceFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"${body is a nice day"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parseExpression
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SimpleIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|19
argument_list|,
name|e
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSimpleNestedUnbalanceFunction ()
specifier|public
name|void
name|testSimpleNestedUnbalanceFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"${body${foo}"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parseExpression
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SimpleIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|e
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSimpleUnknownFunction ()
specifier|public
name|void
name|testSimpleUnknownFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"Hello ${foo} how are you?"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parseExpression
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SimpleIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|e
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSimpleNestedUnknownFunction ()
specifier|public
name|void
name|testSimpleNestedUnknownFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"Hello ${bodyAs(${foo})} how are you?"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
comment|// nested functions can only be syntax evaluated when evaluating an
comment|// exchange at runtime
name|parser
operator|.
name|parseExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SimpleIllegalSyntaxException
name|e
parameter_list|)
block|{
comment|// its a nested function is it reset the index
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testNoEndFunction ()
specifier|public
name|void
name|testNoEndFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"Hello ${body"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parseExpression
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SimpleIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|e
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

