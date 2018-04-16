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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SimpleParserPredicateTest
specifier|public
class|class
name|SimpleParserPredicateTest
extends|extends
name|ExchangeTestSupport
block|{
DECL|method|testSimpleBooleanValue ()
specifier|public
name|void
name|testSimpleBooleanValue
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
literal|"true"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"false"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|pre
operator|=
name|parser
operator|.
name|parsePredicate
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEq ()
specifier|public
name|void
name|testSimpleEq
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
literal|"${body} == 'foo'"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEqNumeric ()
specifier|public
name|void
name|testSimpleEqNumeric
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
literal|123
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} == 123"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEqFunctionFunction ()
specifier|public
name|void
name|testSimpleEqFunctionFunction
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
literal|122
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"val"
argument_list|,
literal|122
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} == ${header.val}"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEqFunctionNumeric ()
specifier|public
name|void
name|testSimpleEqFunctionNumeric
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
literal|122
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} == 122"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleGtFunctionNumeric ()
specifier|public
name|void
name|testSimpleGtFunctionNumeric
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
literal|122
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body}> 120"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
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
literal|122
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body}++ == 123"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
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
literal|122
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body}-- == 121"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEqFunctionBoolean ()
specifier|public
name|void
name|testSimpleEqFunctionBoolean
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
literal|"${header.high} == true"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEqFunctionBooleanSpaces ()
specifier|public
name|void
name|testSimpleEqFunctionBooleanSpaces
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
literal|"${header.high}   ==     true"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleLogicalAnd ()
specifier|public
name|void
name|testSimpleLogicalAnd
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${header.high} == true&& ${header.foo} == 123"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleLogicalOr ()
specifier|public
name|void
name|testSimpleLogicalOr
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${header.high} == false || ${header.foo} == 123"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleLogicalAndAnd ()
specifier|public
name|void
name|testSimpleLogicalAndAnd
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${header.high} == true&& ${header.foo} == 123&& ${header.bar} == 'beer'"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleManyAndLogical ()
specifier|public
name|void
name|testSimpleManyAndLogical
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
operator|+
name|i
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"${header.foo"
argument_list|)
operator|.
name|append
argument_list|(
name|i
argument_list|)
operator|.
name|append
argument_list|(
literal|"} == "
argument_list|)
operator|.
name|append
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
literal|9
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"&& "
argument_list|)
expr_stmt|;
block|}
block|}
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleManyOrLogical ()
specifier|public
name|void
name|testSimpleManyOrLogical
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"${header.foo"
argument_list|)
operator|.
name|append
argument_list|(
name|i
argument_list|)
operator|.
name|append
argument_list|(
literal|"} == "
argument_list|)
operator|.
name|append
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
literal|9
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" || "
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|" || ${body} == 'Hello'"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleExpressionPredicate ()
specifier|public
name|void
name|testSimpleExpressionPredicate
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
literal|"number"
argument_list|,
literal|"1234"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${in.header.number} regex '\\d{4}'"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleMap ()
specifier|public
name|void
name|testSimpleMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo bar"
argument_list|,
literal|"456"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body[foo]} == 123"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body['foo bar']} == 456"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|pre
operator|=
name|parser
operator|.
name|parsePredicate
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
comment|// the predicate has whitespace in the function
name|parser
operator|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body[foo bar]} == 456"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|pre
operator|=
name|parser
operator|.
name|parsePredicate
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
comment|// no header with that name
name|parser
operator|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body[unknown]} == 456"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|pre
operator|=
name|parser
operator|.
name|parsePredicate
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myList"
argument_list|,
name|list
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|testSimpleIn ()
specifier|public
name|void
name|testSimpleIn
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key3"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body[key]} in ${ref:myList}"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body[key2]} in ${ref:myList}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|pre
operator|=
name|parser
operator|.
name|parsePredicate
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body[key3]} in ${ref:myList}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|pre
operator|=
name|parser
operator|.
name|parsePredicate
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not match"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleInEmpty ()
specifier|public
name|void
name|testSimpleInEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} in ',,gold,silver'"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"gold"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match gold"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"silver"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match silver"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match empty"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"bronze"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not match bronze"
argument_list|,
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

