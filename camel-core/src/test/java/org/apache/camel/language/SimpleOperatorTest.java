begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SimpleOperatorTest
specifier|public
class|class
name|SimpleOperatorTest
extends|extends
name|LanguageTestSupport
block|{
annotation|@
name|Override
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
name|jndi
operator|.
name|bind
argument_list|(
literal|"generator"
argument_list|,
operator|new
name|MyFileNameGenerator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|testValueWithSpace ()
specifier|public
name|void
name|testValueWithSpace
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
literal|"Hello Big World"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.body} == 'Hello Big World'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testAnd ()
specifier|public
name|void
name|testAnd
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc and ${in.header.bar} == 123"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc and ${in.header.bar} == 444"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == def and ${in.header.bar} == 123"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == def and ${in.header.bar} == 444"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc and ${in.header.bar}> 100"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc and ${in.header.bar}< 200"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testAndWithQuotation ()
specifier|public
name|void
name|testAndWithQuotation
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc' and ${in.header.bar} == '123'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc' and ${in.header.bar} == '444'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'def' and ${in.header.bar} == '123'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'def' and ${in.header.bar} == '444'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc' and ${in.header.bar}> '100'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc' and ${in.header.bar}< '200'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testOr ()
specifier|public
name|void
name|testOr
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc or ${in.header.bar} == 123"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc or ${in.header.bar} == 444"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == def or ${in.header.bar} == 123"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == def or ${in.header.bar} == 444"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc or ${in.header.bar}< 100"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc or ${in.header.bar}< 200"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == def or ${in.header.bar}< 200"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == def or ${in.header.bar}< 100"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrWithQuotation ()
specifier|public
name|void
name|testOrWithQuotation
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc' or ${in.header.bar} == '123'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc' or ${in.header.bar} == '444'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'def' or ${in.header.bar} == '123'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'def' or ${in.header.bar} == '444'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc' or ${in.header.bar}< '100'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc' or ${in.header.bar}< '200'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'def' or ${in.header.bar}< '200'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'def' or ${in.header.bar}< '100'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqualOperator ()
specifier|public
name|void
name|testEqualOperator
parameter_list|()
throws|throws
name|Exception
block|{
comment|// string to string comparison
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'abc'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == abc"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'def'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == def"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == '1'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// integer to string comparioson
name|assertExpression
argument_list|(
literal|"${in.header.bar} == '123'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} == 123"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} == '444'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} == 444"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} == '1'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testNotEqualOperator ()
specifier|public
name|void
name|testNotEqualOperator
parameter_list|()
throws|throws
name|Exception
block|{
comment|// string to string comparison
name|assertExpression
argument_list|(
literal|"${in.header.foo} != 'abc'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} != abc"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} != 'def'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} != def"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} != '1'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// integer to string comparioson
name|assertExpression
argument_list|(
literal|"${in.header.bar} != '123'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} != 123"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} != '444'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} != 444"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} != '1'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testGreatherThanOperator ()
specifier|public
name|void
name|testGreatherThanOperator
parameter_list|()
throws|throws
name|Exception
block|{
comment|// string to string comparison
name|assertExpression
argument_list|(
literal|"${in.header.foo}> 'aaa'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}> aaa"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}> 'def'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}> def"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// integer to string comparioson
name|assertExpression
argument_list|(
literal|"${in.header.bar}> '100'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}> 100"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}> '123'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}> 123"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}> '200'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testGreatherThanOrEqualOperator ()
specifier|public
name|void
name|testGreatherThanOrEqualOperator
parameter_list|()
throws|throws
name|Exception
block|{
comment|// string to string comparison
name|assertExpression
argument_list|(
literal|"${in.header.foo}>= 'aaa'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}>= aaa"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}>= 'abc'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}>= abc"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}>= 'def'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// integer to string comparioson
name|assertExpression
argument_list|(
literal|"${in.header.bar}>= '100'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}>= 100"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}>= '123'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}>= 123"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}>= '200'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testLessThanOperator ()
specifier|public
name|void
name|testLessThanOperator
parameter_list|()
throws|throws
name|Exception
block|{
comment|// string to string comparison
name|assertExpression
argument_list|(
literal|"${in.header.foo}< 'aaa'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}< aaa"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}< 'def'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}< def"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// integer to string comparioson
name|assertExpression
argument_list|(
literal|"${in.header.bar}< '100'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}< 100"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}< '123'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}< 123"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}< '200'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testLessThanOrEqualOperator ()
specifier|public
name|void
name|testLessThanOrEqualOperator
parameter_list|()
throws|throws
name|Exception
block|{
comment|// string to string comparison
name|assertExpression
argument_list|(
literal|"${in.header.foo}<= 'aaa'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}<= aaa"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}<= 'abc'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}<= abc"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}<= 'def'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// integer to string comparioson
name|assertExpression
argument_list|(
literal|"${in.header.bar}<= '100'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}<= 100"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}<= '123'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}<= 123"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}<= '200'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsNull ()
specifier|public
name|void
name|testIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} == null"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.none} == null"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == 'null'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.none} == 'null'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsNotNull ()
specifier|public
name|void
name|testIsNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} != null"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.none} != null"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} != 'null'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.none} != 'null'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testRightOperatorIsSimpleLanauge ()
specifier|public
name|void
name|testRightOperatorIsSimpleLanauge
parameter_list|()
throws|throws
name|Exception
block|{
comment|// operator on right side is also using ${ } placeholders
name|assertExpression
argument_list|(
literal|"${in.header.foo} == ${in.header.foo}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} == ${in.header.bar}"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testRightOperatorIsBeanLanauge ()
specifier|public
name|void
name|testRightOperatorIsBeanLanauge
parameter_list|()
throws|throws
name|Exception
block|{
comment|// operator on right side is also using ${ } placeholders
name|assertExpression
argument_list|(
literal|"${in.header.foo} == ${bean:generator.generateFilename}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} == ${bean:generator.generateId}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar}>= ${bean:generator.generateId}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstains ()
specifier|public
name|void
name|testConstains
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} contains 'a'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} contains a"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} contains 'ab'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} contains 'abc'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} contains 'def'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} contains def"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testNotConstains ()
specifier|public
name|void
name|testNotConstains
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} not contains 'a'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not contains a"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not contains 'ab'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not contains 'abc'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not contains 'def'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not contains def"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testRegex ()
specifier|public
name|void
name|testRegex
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} regex '^a..$'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} regex '^ab.$'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} regex ^ab.$"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} regex ^d.*$"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} regex '^\\d{3}'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} regex '^\\d{2}'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} regex ^\\d{3}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} regex ^\\d{2}"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testNotRegex ()
specifier|public
name|void
name|testNotRegex
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} not regex '^a..$'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not regex '^ab.$'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not regex ^ab.$"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not regex ^d.*$"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} not regex '^\\d{3}'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} not regex '^\\d{2}'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} not regex ^\\d{3}"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} not regex ^\\d{2}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testIn ()
specifier|public
name|void
name|testIn
parameter_list|()
throws|throws
name|Exception
block|{
comment|// string to string
name|assertExpression
argument_list|(
literal|"${in.header.foo} in 'foo,abc,def'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} in ${bean:generator.generateFilename}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} in foo,abc,def"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} in 'foo,def'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// integer to string
name|assertExpression
argument_list|(
literal|"${in.header.bar} in '100,123,200'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} in 100,123,200"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} in ${bean:generator.generateId}"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} in '100,200'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testNotIn ()
specifier|public
name|void
name|testNotIn
parameter_list|()
throws|throws
name|Exception
block|{
comment|// string to string
name|assertExpression
argument_list|(
literal|"${in.header.foo} not in 'foo,abc,def'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not in ${bean:generator.generateFilename}"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not in foo,abc,def"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not in 'foo,def'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// integer to string
name|assertExpression
argument_list|(
literal|"${in.header.bar} not in '100,123,200'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} not in 100,123,200"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} not in ${bean:generator.generateId}"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} not in '100,200'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testIs ()
specifier|public
name|void
name|testIs
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} is 'java.lang.String'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} is 'java.lang.Integer'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} is 'String'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} is 'Integer'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} is String"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} is Integer"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} is com.mycompany.DoesNotExist"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Syntax error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testIsNot ()
specifier|public
name|void
name|testIsNot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} not is 'java.lang.String'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not is 'java.lang.Integer'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not is 'String'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not is 'Integer'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not is String"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not is Integer"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} not is com.mycompany.DoesNotExist"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Syntax error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testRange ()
specifier|public
name|void
name|testRange
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.bar} range 100..200"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} range 200..300"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} range 200..300"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${bean:generator.generateId} range 123..130"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${bean:generator.generateId} range 120..123"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${bean:generator.generateId} range 120..122"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${bean:generator.generateId} range 124..130"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} range abc..200"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Syntax error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} range abc.."
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Syntax error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} range 100.200"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Syntax error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertExpression
argument_list|(
literal|"${in.header.bar} range 100..200 and ${in.header.foo} == abc"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} range 200..300 and ${in.header.foo} == abc"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} range 200..300 or ${in.header.foo} == abc"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} range 200..300 or ${in.header.foo} == def"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testNotRange ()
specifier|public
name|void
name|testNotRange
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${in.header.bar} not range 100..200"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.bar} not range 200..300"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo} not range 200..300"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${bean:generator.generateId} not range 123..130"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${bean:generator.generateId} not range 120..123"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${bean:generator.generateId} not range 120..122"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${bean:generator.generateId} not range 124..130"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} not range abc..200"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Syntax error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} not range abc.."
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Syntax error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|assertExpression
argument_list|(
literal|"${in.header.foo} not range 100.200"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Syntax error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getLanguageName ()
specifier|protected
name|String
name|getLanguageName
parameter_list|()
block|{
return|return
literal|"simple"
return|;
block|}
DECL|class|MyFileNameGenerator
specifier|public
class|class
name|MyFileNameGenerator
block|{
DECL|method|generateFilename (Exchange exchange)
specifier|public
name|String
name|generateFilename
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|"abc"
return|;
block|}
DECL|method|generateId (Exchange exchange)
specifier|public
name|int
name|generateId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|123
return|;
block|}
block|}
block|}
end_class

end_unit

