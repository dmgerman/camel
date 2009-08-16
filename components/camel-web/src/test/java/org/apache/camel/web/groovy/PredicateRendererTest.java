begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|groovy
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
name|builder
operator|.
name|PredicateBuilder
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

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|PredicateRendererTest
specifier|public
class|class
name|PredicateRendererTest
extends|extends
name|PredicateRendererTestSupport
block|{
DECL|method|testNot ()
specifier|public
name|void
name|testNot
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"not(header(\"name\").isEqualTo(\"Claus\"))"
decl_stmt|;
name|Predicate
name|predicate
init|=
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
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
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
name|String
name|expectedPredicate
init|=
literal|"and(header(\"name\").isEqualTo(\"James\"), header(\"size\").isGreaterThanOrEqualTo(10))"
decl_stmt|;
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
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|and
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
name|String
name|expectedPredicate
init|=
literal|"or(header(\"name\").isEqualTo(\"Hiram\"), header(\"size\").isGreaterThanOrEqualTo(10))"
decl_stmt|;
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
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|or
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
name|String
name|expectedPredicate
init|=
literal|"in(header(\"name\").isEqualTo(\"Hiram\"), header(\"name\").isEqualTo(\"James\"))"
decl_stmt|;
name|Predicate
name|predicate
init|=
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
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
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
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").in(\"Hiram\", \"Jonathan\", \"James\", \"Claus\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
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
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsEqualToString ()
specifier|public
name|void
name|testIsEqualToString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isEqualTo(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
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
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsEqualToConstant ()
specifier|public
name|void
name|testIsEqualToConstant
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isEqualTo(100)"
decl_stmt|;
name|Predicate
name|predicate
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
literal|100
argument_list|)
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsNotEqualTo ()
specifier|public
name|void
name|testIsNotEqualTo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isNotEqualTo(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|constant
argument_list|(
literal|"James"
argument_list|)
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsLessThan ()
specifier|public
name|void
name|testIsLessThan
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isLessThan(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isLessThan
argument_list|(
name|constant
argument_list|(
literal|"James"
argument_list|)
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsLessThanOrEqualTo ()
specifier|public
name|void
name|testIsLessThanOrEqualTo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isLessThanOrEqualTo(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isLessThanOrEqualTo
argument_list|(
name|constant
argument_list|(
literal|"James"
argument_list|)
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsGreaterThan ()
specifier|public
name|void
name|testIsGreaterThan
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isGreaterThan(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
name|constant
argument_list|(
literal|"James"
argument_list|)
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsGreaterThanOrEqualTo ()
specifier|public
name|void
name|testIsGreaterThanOrEqualTo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isGreaterThanOrEqualTo(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isGreaterThanOrEqualTo
argument_list|(
name|constant
argument_list|(
literal|"James"
argument_list|)
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|testContains ()
specifier|public
name|void
name|testContains
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").contains(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|contains
argument_list|(
name|constant
argument_list|(
literal|"James"
argument_list|)
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
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
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isNull()"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isNull
argument_list|()
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
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
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isNotNull()"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isNotNull
argument_list|()
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
comment|// TODO: fix this test!
DECL|method|fixmeTestIsInstanceOf ()
specifier|public
name|void
name|fixmeTestIsInstanceOf
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").isNull()"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isNull
argument_list|()
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
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
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").startsWith(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"James"
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
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
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").endsWith(\"James\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"James"
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
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
name|String
name|expectedPredicate
init|=
literal|"header(\"name\").regex(\"[a-zA-Z]+,London,UK\")"
decl_stmt|;
name|Predicate
name|predicate
init|=
name|header
argument_list|(
literal|"name"
argument_list|)
operator|.
name|regex
argument_list|(
literal|"[a-zA-Z]+,London,UK"
argument_list|)
decl_stmt|;
name|assertMatch
argument_list|(
name|expectedPredicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

