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
name|junit
operator|.
name|Before
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|CamelContext
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
name|Expression
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
name|ExpressionBuilder
operator|.
name|*
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
name|contains
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ExpressionBuilderTest
specifier|public
class|class
name|ExpressionBuilderTest
extends|extends
name|TestSupport
block|{
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|exchange
specifier|protected
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testRegexTokenize ()
specifier|public
name|void
name|testRegexTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|expression
init|=
name|regexTokenizeExpression
argument_list|(
name|headerExpression
argument_list|(
literal|"location"
argument_list|)
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"Islington"
block|,
literal|"London"
block|,
literal|"UK"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
name|expected
argument_list|)
expr_stmt|;
name|Predicate
name|predicate
init|=
name|contains
argument_list|(
name|regexTokenizeExpression
argument_list|(
name|headerExpression
argument_list|(
literal|"location"
argument_list|)
argument_list|,
literal|","
argument_list|)
argument_list|,
name|constantExpression
argument_list|(
literal|"London"
argument_list|)
argument_list|)
decl_stmt|;
name|assertPredicate
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|predicate
operator|=
name|contains
argument_list|(
name|regexTokenizeExpression
argument_list|(
name|headerExpression
argument_list|(
literal|"location"
argument_list|)
argument_list|,
literal|","
argument_list|)
argument_list|,
name|constantExpression
argument_list|(
literal|"Manchester"
argument_list|)
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRegexReplaceAll ()
specifier|public
name|void
name|testRegexReplaceAll
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|expression
init|=
name|regexReplaceAll
argument_list|(
name|headerExpression
argument_list|(
literal|"location"
argument_list|)
argument_list|,
literal|"London"
argument_list|,
literal|"Westminster"
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
literal|"Islington,Westminster,UK"
argument_list|)
expr_stmt|;
name|expression
operator|=
name|regexReplaceAll
argument_list|(
name|headerExpression
argument_list|(
literal|"location"
argument_list|)
argument_list|,
literal|"London"
argument_list|,
name|headerExpression
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
literal|"Islington,James,UK"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTokenize ()
specifier|public
name|void
name|testTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|expression
init|=
name|tokenizeExpression
argument_list|(
name|headerExpression
argument_list|(
literal|"location"
argument_list|)
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"Islington"
block|,
literal|"London"
block|,
literal|"UK"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
name|expected
argument_list|)
expr_stmt|;
name|Predicate
name|predicate
init|=
name|contains
argument_list|(
name|tokenizeExpression
argument_list|(
name|headerExpression
argument_list|(
literal|"location"
argument_list|)
argument_list|,
literal|","
argument_list|)
argument_list|,
name|constantExpression
argument_list|(
literal|"London"
argument_list|)
argument_list|)
decl_stmt|;
name|assertPredicate
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|predicate
operator|=
name|contains
argument_list|(
name|tokenizeExpression
argument_list|(
name|headerExpression
argument_list|(
literal|"location"
argument_list|)
argument_list|,
literal|","
argument_list|)
argument_list|,
name|constantExpression
argument_list|(
literal|"Manchester"
argument_list|)
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTokenizeLines ()
specifier|public
name|void
name|testTokenizeLines
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|expression
init|=
name|regexTokenizeExpression
argument_list|(
name|bodyExpression
argument_list|()
argument_list|,
literal|"[\r|\n]"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World\nBye World\rSee you again"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"Hello World"
block|,
literal|"Bye World"
block|,
literal|"See you again"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSortLines ()
specifier|public
name|void
name|testSortLines
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|expression
init|=
name|sortExpression
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
operator|.
name|getExpression
argument_list|()
argument_list|,
operator|new
name|SortByName
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Jonathan,Claus,James,Hadrian"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"Claus"
block|,
literal|"Hadrian"
block|,
literal|"James"
block|,
literal|"Jonathan"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelContextPropertiesExpression ()
specifier|public
name|void
name|testCamelContextPropertiesExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
literal|"CamelTestKey"
argument_list|,
literal|"CamelTestValue"
argument_list|)
expr_stmt|;
name|Expression
name|expression
init|=
name|camelContextPropertyExpression
argument_list|(
literal|"CamelTestKey"
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
literal|"CamelTestValue"
argument_list|)
expr_stmt|;
name|expression
operator|=
name|camelContextPropertiesExpression
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|properties
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong properties size"
argument_list|,
name|properties
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseSimpleOrFallbackToConstantExpression ()
specifier|public
name|void
name|testParseSimpleOrFallbackToConstantExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"world"
argument_list|,
name|parseSimpleOrFallbackToConstantExpression
argument_list|(
literal|"world"
argument_list|,
name|camelContext
argument_list|)
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
name|assertEquals
argument_list|(
literal|"Hello there!"
argument_list|,
name|parseSimpleOrFallbackToConstantExpression
argument_list|(
literal|"${body}"
argument_list|,
name|camelContext
argument_list|)
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
name|assertEquals
argument_list|(
literal|"Hello there!"
argument_list|,
name|parseSimpleOrFallbackToConstantExpression
argument_list|(
literal|"$simple{body}"
argument_list|,
name|camelContext
argument_list|)
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
annotation|@
name|Test
DECL|method|testFunction ()
specifier|public
name|void
name|testFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
name|messageExpression
argument_list|(
name|m
lambda|->
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"name"
argument_list|)
argument_list|)
argument_list|,
name|exchange
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
name|messageExpression
argument_list|(
name|m
lambda|->
name|m
operator|.
name|getHeader
argument_list|(
literal|"name"
argument_list|)
argument_list|)
argument_list|,
name|exchange
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
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
block|}
DECL|class|SortByName
specifier|private
specifier|static
class|class
name|SortByName
implements|implements
name|Comparator
argument_list|<
name|String
argument_list|>
block|{
DECL|method|compare (java.lang.String o1, java.lang.String o2)
specifier|public
name|int
name|compare
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|o1
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|compareToIgnoreCase
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

