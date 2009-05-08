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
name|ExpressionIllegalSyntaxException
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
name|language
operator|.
name|simple
operator|.
name|SimpleLanguage
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SimpleTest
specifier|public
class|class
name|SimpleTest
extends|extends
name|LanguageTestSupport
block|{
DECL|method|testConstantExpression ()
specifier|public
name|void
name|testConstantExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleExpressions ()
specifier|public
name|void
name|testSimpleExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"id"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"body"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"in.body"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"in.header.foo"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"header.foo"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
block|}
DECL|method|testLanguagesInContext ()
specifier|public
name|void
name|testLanguagesInContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// evaluate so we know there is 1 language in the context
name|assertExpression
argument_list|(
literal|"id"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getLanguageNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"simple"
argument_list|,
name|context
operator|.
name|getLanguageNames
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testComplexExpressions ()
specifier|public
name|void
name|testComplexExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"hey ${in.header.foo}"
argument_list|,
literal|"hey abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"hey ${in.header.foo}!"
argument_list|,
literal|"hey abc!"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"hey ${in.header.foo}-${in.header.foo}!"
argument_list|,
literal|"hey abc-abc!"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"hey ${in.header.foo}${in.header.foo}"
argument_list|,
literal|"hey abcabc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}${in.header.foo}"
argument_list|,
literal|"abcabc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${in.header.foo}!"
argument_list|,
literal|"abc!"
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidComplexExpression ()
specifier|public
name|void
name|testInvalidComplexExpression
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|assertExpression
argument_list|(
literal|"hey ${foo"
argument_list|,
literal|"bad expression!"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Caught expected exception: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testPredicates ()
specifier|public
name|void
name|testPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPredicate
argument_list|(
literal|"body"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"header.foo"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"header.madeUpHeader"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testExceptionMessage ()
specifier|public
name|void
name|testExceptionMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Just testing"
argument_list|)
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"exception.message"
argument_list|,
literal|"Just testing"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"Hello ${exception.message} World"
argument_list|,
literal|"Hello Just testing World"
argument_list|)
expr_stmt|;
block|}
DECL|method|testIllegalSyntax ()
specifier|public
name|void
name|testIllegalSyntax
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|assertExpression
argument_list|(
literal|"hey ${xxx} how are you?"
argument_list|,
literal|""
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
name|ExpressionIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Illegal syntax: xxx"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|assertExpression
argument_list|(
literal|"${xxx}"
argument_list|,
literal|""
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
name|ExpressionIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Illegal syntax: xxx"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
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
block|}
end_class

end_unit

