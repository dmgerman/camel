begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|jsse
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|CamelContext
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
DECL|class|FilterParametersTest
specifier|public
class|class
name|FilterParametersTest
extends|extends
name|AbstractJsseParametersTest
block|{
annotation|@
name|Test
DECL|method|testPropertyPlaceholders ()
specifier|public
name|void
name|testPropertyPlaceholders
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|createPropertiesPlaceholderAwareContext
argument_list|()
decl_stmt|;
name|FilterParameters
name|filter
init|=
operator|new
name|FilterParameters
argument_list|()
decl_stmt|;
name|filter
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|filter
operator|.
name|getInclude
argument_list|()
operator|.
name|add
argument_list|(
literal|"{{filterParameters.include}}"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|getExclude
argument_list|()
operator|.
name|add
argument_list|(
literal|"{{filterParameters.exclude}}"
argument_list|)
expr_stmt|;
name|FilterParameters
operator|.
name|Patterns
name|patterns
init|=
name|filter
operator|.
name|getPatterns
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|includes
init|=
name|patterns
operator|.
name|getIncludes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|excludes
init|=
name|patterns
operator|.
name|getExcludes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|includes
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|excludes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|includes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|excludes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Matcher
name|includeMatcher
init|=
name|includes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|matcher
argument_list|(
literal|"include"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|includeMatcher
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
name|Matcher
name|excludeMatcher
init|=
name|excludes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|matcher
argument_list|(
literal|"exclude"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|excludeMatcher
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetIncludePatterns ()
specifier|public
name|void
name|testGetIncludePatterns
parameter_list|()
block|{
name|FilterParameters
name|filter
init|=
operator|new
name|FilterParameters
argument_list|()
decl_stmt|;
name|filter
operator|.
name|getInclude
argument_list|()
operator|.
name|add
argument_list|(
literal|"asdfsadfsadfsadf"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|includes
init|=
name|filter
operator|.
name|getIncludePatterns
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|excludes
init|=
name|filter
operator|.
name|getExcludePatterns
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|includes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|includes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|excludes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|excludes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|includes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Matcher
name|matcher
init|=
name|includes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|matcher
argument_list|(
literal|"asdfsadfsadfsadf"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetExcludePatterns ()
specifier|public
name|void
name|testGetExcludePatterns
parameter_list|()
block|{
name|FilterParameters
name|filter
init|=
operator|new
name|FilterParameters
argument_list|()
decl_stmt|;
name|filter
operator|.
name|getExclude
argument_list|()
operator|.
name|add
argument_list|(
literal|"asdfsadfsadfsadf"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|includes
init|=
name|filter
operator|.
name|getIncludePatterns
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|excludes
init|=
name|filter
operator|.
name|getExcludePatterns
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|excludes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|excludes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|includes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|includes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|excludes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Matcher
name|matcher
init|=
name|excludes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|matcher
argument_list|(
literal|"asdfsadfsadfsadf"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
block|{
name|FilterParameters
name|filter
init|=
operator|new
name|FilterParameters
argument_list|()
decl_stmt|;
name|filter
operator|.
name|getInclude
argument_list|()
operator|.
name|add
argument_list|(
literal|"asdf.*"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|getExclude
argument_list|()
operator|.
name|add
argument_list|(
literal|"aa"
argument_list|)
expr_stmt|;
name|FilterParameters
operator|.
name|Patterns
name|patterns
init|=
name|filter
operator|.
name|getPatterns
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|includes
init|=
name|patterns
operator|.
name|getIncludes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|excludes
init|=
name|patterns
operator|.
name|getExcludes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|includes
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|excludes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|includes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|excludes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Matcher
name|includeMatcher
init|=
name|includes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|matcher
argument_list|(
literal|"asdfsadfsadfsadf"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|includeMatcher
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
name|Matcher
name|excludeMatcher
init|=
name|excludes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|matcher
argument_list|(
literal|"aa"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|excludeMatcher
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

