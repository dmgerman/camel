begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Expression
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SimpleParserExpressionTest
specifier|public
class|class
name|SimpleParserExpressionTest
extends|extends
name|ExchangeTestSupport
block|{
DECL|method|testSimpleParserEol ()
specifier|public
name|void
name|testSimpleParserEol
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
literal|"Hello"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleSingleQuote ()
specifier|public
name|void
name|testSimpleSingleQuote
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
literal|"'Hello'"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"'Hello'"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleSingleQuoteWithFunction ()
specifier|public
name|void
name|testSimpleSingleQuoteWithFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"'Hello ${body} how are you?'"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"'Hello World how are you?'"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleSingleQuoteWithFunctionBodyAs ()
specifier|public
name|void
name|testSimpleSingleQuoteWithFunctionBodyAs
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"'Hello ${bodyAs(String)} how are you?'"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"'Hello World how are you?'"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleSingleQuoteEol ()
specifier|public
name|void
name|testSimpleSingleQuoteEol
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
literal|"'Hello' World"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"'Hello' World"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleFunction ()
specifier|public
name|void
name|testSimpleFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"${body}"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleSingleQuoteWithEscape ()
specifier|public
name|void
name|testSimpleSingleQuoteWithEscape
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
literal|"Pay 200\\$ today"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Pay 200$ today"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleSingleQuoteWithEscapeEnd ()
specifier|public
name|void
name|testSimpleSingleQuoteWithEscapeEnd
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
literal|"Pay 200\\$"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Pay 200$"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleUnaryInc ()
specifier|public
name|void
name|testSimpleUnaryInc
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"122"
argument_list|)
expr_stmt|;
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"${body}++"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleUnaryDec ()
specifier|public
name|void
name|testSimpleUnaryDec
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"122"
argument_list|)
expr_stmt|;
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"${body}--"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"121"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEscape ()
specifier|public
name|void
name|testSimpleEscape
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
comment|// we escape the $ which mean it will not be a function
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
literal|"Hello \\${body\\} how are you?"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|parser
operator|.
name|parseExpression
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello ${body} how are you?"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

