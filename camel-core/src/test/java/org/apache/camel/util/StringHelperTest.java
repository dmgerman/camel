begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

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
name|Locale
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
comment|/**  * Unit test for StringHelper  */
end_comment

begin_class
DECL|class|StringHelperTest
specifier|public
class|class
name|StringHelperTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testSimpleSanitized ()
specifier|public
name|void
name|testSimpleSanitized
parameter_list|()
block|{
name|String
name|out
init|=
name|StringHelper
operator|.
name|sanitize
argument_list|(
literal|"hello"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should not contain : "
argument_list|,
name|out
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should not contain . "
argument_list|,
name|out
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotFileFriendlySimpleSanitized ()
specifier|public
name|void
name|testNotFileFriendlySimpleSanitized
parameter_list|()
block|{
name|String
name|out
init|=
name|StringHelper
operator|.
name|sanitize
argument_list|(
literal|"c:\\helloworld"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should not contain : "
argument_list|,
name|out
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should not contain . "
argument_list|,
name|out
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCountChar ()
specifier|public
name|void
name|testCountChar
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
literal|"Hello World"
argument_list|,
literal|'x'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
literal|"Hello World"
argument_list|,
literal|'e'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
literal|"Hello World"
argument_list|,
literal|'l'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
literal|"Hello World"
argument_list|,
literal|' '
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
literal|""
argument_list|,
literal|' '
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
literal|null
argument_list|,
literal|' '
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveQuotes ()
specifier|public
name|void
name|testRemoveQuotes
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" "
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|"'foo'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|"'foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|"foo'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|"\"foo\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|"\"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|"foo\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
literal|"'foo\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveLeadingAndEndingQuotes ()
specifier|public
name|void
name|testRemoveLeadingAndEndingQuotes
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" "
argument_list|,
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
literal|"'Hello World'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
literal|"\"Hello World\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello 'Camel'"
argument_list|,
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
literal|"Hello 'Camel'"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHasUpper ()
specifier|public
name|void
name|testHasUpper
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasUpperCase
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasUpperCase
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasUpperCase
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasUpperCase
argument_list|(
literal|"com.foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasUpperCase
argument_list|(
literal|"com.foo.123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|hasUpperCase
argument_list|(
literal|"com.foo.MyClass"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|hasUpperCase
argument_list|(
literal|"com.foo.My"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Note, this is not a FQN
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|hasUpperCase
argument_list|(
literal|"com.foo.subA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsClassName ()
specifier|public
name|void
name|testIsClassName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isClassName
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isClassName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isClassName
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isClassName
argument_list|(
literal|"com.foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isClassName
argument_list|(
literal|"com.foo.123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|isClassName
argument_list|(
literal|"com.foo.MyClass"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|isClassName
argument_list|(
literal|"com.foo.My"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Note, this is not a FQN
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isClassName
argument_list|(
literal|"com.foo.subA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHasStartToken ()
specifier|public
name|void
name|testHasStartToken
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|null
argument_list|,
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|""
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|""
argument_list|,
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|"Hello World"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|"Hello World"
argument_list|,
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|"${body}"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|"${body}"
argument_list|,
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|"$simple{body}"
argument_list|,
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|"${body}"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|"${body}"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
comment|// $foo{ is valid because its foo language
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
literal|"$foo{body}"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsQuoted ()
specifier|public
name|void
name|testIsQuoted
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"abc'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"\"abc'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"abc\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"'abc\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"\"abc'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"abc'def'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"abc'def'ghi"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"'def'ghi"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"'abc'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|isQuoted
argument_list|(
literal|"\"abc\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReplaceAll ()
specifier|public
name|void
name|testReplaceAll
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|null
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|"foobar"
argument_list|,
literal|"###"
argument_list|,
literal|"DOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|"foo.bar"
argument_list|,
literal|"."
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooDOTbar"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|"foo.bar"
argument_list|,
literal|"."
argument_list|,
literal|"DOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooDOTbar"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|"foo###bar"
argument_list|,
literal|"###"
argument_list|,
literal|"DOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|"foo###bar"
argument_list|,
literal|"###"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooDOTbarDOTbaz"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|"foo.bar.baz"
argument_list|,
literal|"."
argument_list|,
literal|"DOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooDOTbarDOTbazDOT"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|"foo.bar.baz."
argument_list|,
literal|"."
argument_list|,
literal|"DOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DOTfooDOTbarDOTbazDOT"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|".foo.bar.baz."
argument_list|,
literal|"."
argument_list|,
literal|"DOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooDOT"
argument_list|,
name|StringHelper
operator|.
name|replaceAll
argument_list|(
literal|"foo."
argument_list|,
literal|"."
argument_list|,
literal|"DOT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveInitialCharacters ()
specifier|public
name|void
name|testRemoveInitialCharacters
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|StringHelper
operator|.
name|removeStartingCharacters
argument_list|(
literal|"foo"
argument_list|,
literal|'/'
argument_list|)
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StringHelper
operator|.
name|removeStartingCharacters
argument_list|(
literal|"/foo"
argument_list|,
literal|'/'
argument_list|)
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StringHelper
operator|.
name|removeStartingCharacters
argument_list|(
literal|"//foo"
argument_list|,
literal|'/'
argument_list|)
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBefore ()
specifier|public
name|void
name|testBefore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Hello "
argument_list|,
name|StringHelper
operator|.
name|before
argument_list|(
literal|"Hello World"
argument_list|,
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
argument_list|,
name|StringHelper
operator|.
name|before
argument_list|(
literal|"Hello World Again"
argument_list|,
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|before
argument_list|(
literal|"Hello Again"
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StringHelper
operator|.
name|before
argument_list|(
literal|"mykey:ignore"
argument_list|,
literal|":"
argument_list|,
literal|"mykey"
operator|::
name|equals
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|StringHelper
operator|.
name|before
argument_list|(
literal|"ignore:ignore"
argument_list|,
literal|":"
argument_list|,
literal|"mykey"
operator|::
name|equals
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAfter ()
specifier|public
name|void
name|testAfter
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|" World"
argument_list|,
name|StringHelper
operator|.
name|after
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" World Again"
argument_list|,
name|StringHelper
operator|.
name|after
argument_list|(
literal|"Hello World Again"
argument_list|,
literal|"Hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|after
argument_list|(
literal|"Hello Again"
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StringHelper
operator|.
name|after
argument_list|(
literal|"ignore:mykey"
argument_list|,
literal|":"
argument_list|,
literal|"mykey"
operator|::
name|equals
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|StringHelper
operator|.
name|after
argument_list|(
literal|"ignore:ignore"
argument_list|,
literal|":"
argument_list|,
literal|"mykey"
operator|::
name|equals
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBetween ()
specifier|public
name|void
name|testBetween
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo bar"
argument_list|,
name|StringHelper
operator|.
name|between
argument_list|(
literal|"Hello 'foo bar' how are you"
argument_list|,
literal|"'"
argument_list|,
literal|"'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo bar"
argument_list|,
name|StringHelper
operator|.
name|between
argument_list|(
literal|"Hello ${foo bar} how are you"
argument_list|,
literal|"${"
argument_list|,
literal|"}"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|between
argument_list|(
literal|"Hello ${foo bar} how are you"
argument_list|,
literal|"'"
argument_list|,
literal|"'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StringHelper
operator|.
name|between
argument_list|(
literal|"begin:mykey:end"
argument_list|,
literal|"begin:"
argument_list|,
literal|":end"
argument_list|,
literal|"mykey"
operator|::
name|equals
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|StringHelper
operator|.
name|between
argument_list|(
literal|"begin:ignore:end"
argument_list|,
literal|"begin:"
argument_list|,
literal|":end"
argument_list|,
literal|"mykey"
operator|::
name|equals
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBetweenOuterPair ()
specifier|public
name|void
name|testBetweenOuterPair
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"bar(baz)123"
argument_list|,
name|StringHelper
operator|.
name|betweenOuterPair
argument_list|(
literal|"foo(bar(baz)123)"
argument_list|,
literal|'('
argument_list|,
literal|')'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|betweenOuterPair
argument_list|(
literal|"foo(bar(baz)123))"
argument_list|,
literal|'('
argument_list|,
literal|')'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|betweenOuterPair
argument_list|(
literal|"foo(bar(baz123"
argument_list|,
literal|'('
argument_list|,
literal|')'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringHelper
operator|.
name|betweenOuterPair
argument_list|(
literal|"foo)bar)baz123"
argument_list|,
literal|'('
argument_list|,
literal|')'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|StringHelper
operator|.
name|betweenOuterPair
argument_list|(
literal|"foo(bar)baz123"
argument_list|,
literal|'('
argument_list|,
literal|')'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"'bar', 'baz()123', 123"
argument_list|,
name|StringHelper
operator|.
name|betweenOuterPair
argument_list|(
literal|"foo('bar', 'baz()123', 123)"
argument_list|,
literal|'('
argument_list|,
literal|')'
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StringHelper
operator|.
name|betweenOuterPair
argument_list|(
literal|"foo(bar)baz123"
argument_list|,
literal|'('
argument_list|,
literal|')'
argument_list|,
literal|"bar"
operator|::
name|equals
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|StringHelper
operator|.
name|betweenOuterPair
argument_list|(
literal|"foo[bar)baz123"
argument_list|,
literal|'('
argument_list|,
literal|')'
argument_list|,
literal|"bar"
operator|::
name|equals
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsJavaIdentifier ()
specifier|public
name|void
name|testIsJavaIdentifier
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|StringHelper
operator|.
name|isJavaIdentifier
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isJavaIdentifier
argument_list|(
literal|"foo.bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isJavaIdentifier
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|StringHelper
operator|.
name|isJavaIdentifier
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNormalizeClassName ()
specifier|public
name|void
name|testNormalizeClassName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Should get the right class name"
argument_list|,
literal|"my.package-info"
argument_list|,
name|StringHelper
operator|.
name|normalizeClassName
argument_list|(
literal|"my.package-info"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the right class name"
argument_list|,
literal|"Integer[]"
argument_list|,
name|StringHelper
operator|.
name|normalizeClassName
argument_list|(
literal|"Integer[] \r"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the right class name"
argument_list|,
literal|"Hello_World"
argument_list|,
name|StringHelper
operator|.
name|normalizeClassName
argument_list|(
literal|"Hello_World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the right class name"
argument_list|,
literal|""
argument_list|,
name|StringHelper
operator|.
name|normalizeClassName
argument_list|(
literal|"////"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChangedLines ()
specifier|public
name|void
name|testChangedLines
parameter_list|()
block|{
name|String
name|oldText
init|=
literal|"Hello\nWorld\nHow are you"
decl_stmt|;
name|String
name|newText
init|=
literal|"Hello\nWorld\nHow are you"
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|changed
init|=
name|StringHelper
operator|.
name|changedLines
argument_list|(
name|oldText
argument_list|,
name|newText
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|changed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|oldText
operator|=
literal|"Hello\nWorld\nHow are you"
expr_stmt|;
name|newText
operator|=
literal|"Hello\nWorld\nHow are you today"
expr_stmt|;
name|changed
operator|=
name|StringHelper
operator|.
name|changedLines
argument_list|(
name|oldText
argument_list|,
name|newText
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|changed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|changed
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|oldText
operator|=
literal|"Hello\nWorld\nHow are you"
expr_stmt|;
name|newText
operator|=
literal|"Hello\nCamel\nHow are you today"
expr_stmt|;
name|changed
operator|=
name|StringHelper
operator|.
name|changedLines
argument_list|(
name|oldText
argument_list|,
name|newText
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|changed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|changed
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|changed
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|oldText
operator|=
literal|"Hello\nWorld\nHow are you"
expr_stmt|;
name|newText
operator|=
literal|"Hello\nWorld\nHow are you today\nand tomorrow"
expr_stmt|;
name|changed
operator|=
name|StringHelper
operator|.
name|changedLines
argument_list|(
name|oldText
argument_list|,
name|newText
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|changed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|changed
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|changed
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrimToNull ()
specifier|public
name|void
name|testTrimToNull
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|StringHelper
operator|.
name|trimToNull
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StringHelper
operator|.
name|trimToNull
argument_list|(
literal|" abc"
argument_list|)
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StringHelper
operator|.
name|trimToNull
argument_list|(
literal|" abc "
argument_list|)
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|StringHelper
operator|.
name|trimToNull
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|StringHelper
operator|.
name|trimToNull
argument_list|(
literal|"\t"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|StringHelper
operator|.
name|trimToNull
argument_list|(
literal|" \t "
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|StringHelper
operator|.
name|trimToNull
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHumanReadableBytes ()
specifier|public
name|void
name|testHumanReadableBytes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"0 B"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"32 B"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|32
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0 KB"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|1024
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.7 KB"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|1730
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"108.0 KB"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|110592
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"6.8 MB"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|7077888
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"432.0 MB"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|452984832
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"27.0 GB"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|28991029248L
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.7 TB"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|,
literal|1855425871872L
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHumanReadableBytesNullLocale ()
specifier|public
name|void
name|testHumanReadableBytesNullLocale
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"1.3 KB"
argument_list|,
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
literal|null
argument_list|,
literal|1280
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHumanReadableBytesDefaultLocale ()
specifier|public
name|void
name|testHumanReadableBytesDefaultLocale
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|StringHelper
operator|.
name|humanReadableBytes
argument_list|(
literal|110592
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

