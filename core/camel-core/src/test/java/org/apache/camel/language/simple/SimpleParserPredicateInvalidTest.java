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
DECL|class|SimpleParserPredicateInvalidTest
specifier|public
class|class
name|SimpleParserPredicateInvalidTest
extends|extends
name|ExchangeTestSupport
block|{
annotation|@
name|Test
DECL|method|testSimpleEqFunctionInvalid ()
specifier|public
name|void
name|testSimpleEqFunctionInvalid
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
literal|"Hello"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"high"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${header.high} == abc"
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
name|parsePredicate
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
DECL|method|testSimpleInvalidSymbol ()
specifier|public
name|void
name|testSimpleInvalidSymbol
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
literal|"Hello"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"high"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${header.high} = true"
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
name|parsePredicate
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
literal|15
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
DECL|method|testSimpleUnevenSingleQuote ()
specifier|public
name|void
name|testSimpleUnevenSingleQuote
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
literal|"foo"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} == 'foo"
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
name|parsePredicate
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
literal|14
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
DECL|method|testSimpleUnevenDoubleQuote ()
specifier|public
name|void
name|testSimpleUnevenDoubleQuote
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
literal|"foo"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} == \"foo"
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
name|parsePredicate
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
literal|14
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
DECL|method|testSimpleTwoAnd ()
specifier|public
name|void
name|testSimpleTwoAnd
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
literal|"foo"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} == 'foo'&&&& ${header} == 123"
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
name|parsePredicate
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
literal|20
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
DECL|method|testSimpleTwoOr ()
specifier|public
name|void
name|testSimpleTwoOr
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
literal|"foo"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} == 'foo' || || ${header} == 123"
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
name|parsePredicate
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
literal|20
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
DECL|method|testSimpleTwoEq ()
specifier|public
name|void
name|testSimpleTwoEq
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
literal|"foo"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} == == 'foo'"
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
name|parsePredicate
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
literal|13
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

