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
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_comment
comment|/**  * Unit tests for {@link AntPathMatcher}.  */
end_comment

begin_class
DECL|class|AntPathMatcherTest
specifier|public
class|class
name|AntPathMatcherTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
block|{
name|AntPathMatcher
name|matcher
init|=
operator|new
name|AntPathMatcher
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"*.txt"
argument_list|,
literal|"blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"???.txt"
argument_list|,
literal|"abc.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"abc.t?t"
argument_list|,
literal|"abc.tnt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"???.txt"
argument_list|,
literal|"abcd.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"**/*.txt"
argument_list|,
literal|"blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"**/*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"**/*.txt"
argument_list|,
literal|"foo/bar/blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.txt"
argument_list|,
literal|"foo/bar/blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.??"
argument_list|,
literal|"foo/bar/blah.gz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.txt"
argument_list|,
literal|"blah/blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCaseSensitive ()
specifier|public
name|void
name|testCaseSensitive
parameter_list|()
block|{
name|AntPathMatcher
name|matcher
init|=
operator|new
name|AntPathMatcher
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.txt"
argument_list|,
literal|"foo/BLAH.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"FOO/**/*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.TXT"
argument_list|,
literal|"foo/blah.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"foo/**/*.TXT"
argument_list|,
literal|"foo/blah.txt"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"FOO/**/*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"FOO/**/*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|match
argument_list|(
literal|"FOO/**/*.txt"
argument_list|,
literal|"foo/blah.txt"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

