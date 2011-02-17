begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Message
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
name|TestSupport
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
name|DefaultCamelContext
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
name|DefaultExchange
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|Builder
operator|.
name|constant
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|PredicateBuilder
operator|.
name|in
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|PredicateBuilder
operator|.
name|not
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|PredicateBuilderTest
specifier|public
class|class
name|PredicateBuilderTest
extends|extends
name|TestSupport
block|{
DECL|field|exchange
specifier|protected
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|testRegexPredicates ()
specifier|public
name|void
name|testRegexPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"location"
argument_list|)
operator|.
name|regex
argument_list|(
literal|"[a-zA-Z]+,London,UK"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"location"
argument_list|)
operator|.
name|regex
argument_list|(
literal|"[a-zA-Z]+,Westminster,[a-zA-Z]+"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPredicates ()
specifier|public
name|void
name|testPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|constant
argument_list|(
literal|"James"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|not
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|constant
argument_list|(
literal|"Claus"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"10"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailingPredicates ()
specifier|public
name|void
name|testFailingPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|constant
argument_list|(
literal|"Hiram"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
name|constant
argument_list|(
literal|100
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|not
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|isLessThan
argument_list|(
name|constant
argument_list|(
literal|100
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompoundOrPredicates ()
specifier|public
name|void
name|testCompoundOrPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|Predicate
name|p1
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|constant
argument_list|(
literal|"Hiram"
argument_list|)
argument_list|)
decl_stmt|;
name|Predicate
name|p2
init|=
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|isGreaterThanOrEqualTo
argument_list|(
name|constant
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|Predicate
name|or
init|=
name|PredicateBuilder
operator|.
name|or
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
decl_stmt|;
name|assertMatches
argument_list|(
name|or
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompoundAndPredicates ()
specifier|public
name|void
name|testCompoundAndPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|Predicate
name|p1
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|constant
argument_list|(
literal|"James"
argument_list|)
argument_list|)
decl_stmt|;
name|Predicate
name|p2
init|=
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|isGreaterThanOrEqualTo
argument_list|(
name|constant
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|Predicate
name|and
init|=
name|PredicateBuilder
operator|.
name|and
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
decl_stmt|;
name|assertMatches
argument_list|(
name|and
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompoundAndOrPredicates ()
specifier|public
name|void
name|testCompoundAndOrPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|Predicate
name|p1
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|constant
argument_list|(
literal|"Hiram"
argument_list|)
argument_list|)
decl_stmt|;
name|Predicate
name|p2
init|=
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
name|constant
argument_list|(
literal|100
argument_list|)
argument_list|)
decl_stmt|;
name|Predicate
name|p3
init|=
name|header
argument_list|(
literal|"location"
argument_list|)
operator|.
name|contains
argument_list|(
literal|"London"
argument_list|)
decl_stmt|;
name|Predicate
name|and
init|=
name|PredicateBuilder
operator|.
name|and
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
decl_stmt|;
name|Predicate
name|andor
init|=
name|PredicateBuilder
operator|.
name|or
argument_list|(
name|and
argument_list|,
name|p3
argument_list|)
decl_stmt|;
name|assertMatches
argument_list|(
name|andor
argument_list|)
expr_stmt|;
block|}
DECL|method|testPredicateIn ()
specifier|public
name|void
name|testPredicateIn
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMatches
argument_list|(
name|in
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Hiram"
argument_list|)
argument_list|,
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"James"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testValueIn ()
specifier|public
name|void
name|testValueIn
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|in
argument_list|(
literal|"Hiram"
argument_list|,
literal|"Jonathan"
argument_list|,
literal|"James"
argument_list|,
literal|"Claus"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEmptyHeaderValueIn ()
specifier|public
name|void
name|testEmptyHeaderValueIn
parameter_list|()
throws|throws
name|Exception
block|{
comment|// there is no header with xxx
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"xxx"
argument_list|)
operator|.
name|in
argument_list|(
literal|"Hiram"
argument_list|,
literal|"Jonathan"
argument_list|,
literal|"James"
argument_list|,
literal|"Claus"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStartsWith ()
specifier|public
name|void
name|testStartsWith
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"J"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"James"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"C"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"99"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"9"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|99
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEndsWith ()
specifier|public
name|void
name|testEndsWith
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"mes"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"James"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"world"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"99"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"9"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertMatches
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|99
argument_list|)
argument_list|)
expr_stmt|;
name|assertDoesNotMatch
argument_list|(
name|header
argument_list|(
literal|"size"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"Hello there!"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"location"
argument_list|,
literal|"Islington,London,UK"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"size"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMatches (Predicate predicate)
specifier|protected
name|void
name|assertMatches
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|assertPredicateMatches
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDoesNotMatch (Predicate predicate)
specifier|protected
name|void
name|assertDoesNotMatch
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|assertPredicateDoesNotMatch
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

