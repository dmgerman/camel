begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
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
name|spi
operator|.
name|Language
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
name|support
operator|.
name|DefaultExchange
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|JsonPathLanguageTest
specifier|public
class|class
name|JsonPathLanguageTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testExpressionArray ()
specifier|public
name|void
name|testExpressionArray
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/books.json"
argument_list|)
argument_list|)
expr_stmt|;
name|Language
name|lan
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"jsonpath"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|lan
operator|.
name|createExpression
argument_list|(
literal|"$.store.book[*].author"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|authors
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Authors {}"
argument_list|,
name|authors
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|authors
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|authors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Nigel Rees"
argument_list|,
name|authors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Evelyn Waugh"
argument_list|,
name|authors
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|exp
operator|=
name|lan
operator|.
name|createExpression
argument_list|(
literal|"$.store.bicycle.price"
argument_list|)
expr_stmt|;
name|String
name|price
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong result"
argument_list|,
literal|"19.95"
argument_list|,
name|price
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExpressionField ()
specifier|public
name|void
name|testExpressionField
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/type.json"
argument_list|)
argument_list|)
expr_stmt|;
name|Language
name|lan
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"jsonpath"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|lan
operator|.
name|createExpression
argument_list|(
literal|"$.kind"
argument_list|)
decl_stmt|;
name|String
name|kind
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|kind
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"full"
argument_list|,
name|kind
argument_list|)
expr_stmt|;
name|exp
operator|=
name|lan
operator|.
name|createExpression
argument_list|(
literal|"$.type"
argument_list|)
expr_stmt|;
name|String
name|type
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"customer"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExpressionPojo ()
specifier|public
name|void
name|testExpressionPojo
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
name|pojo
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|pojo
operator|.
name|put
argument_list|(
literal|"kind"
argument_list|,
literal|"full"
argument_list|)
expr_stmt|;
name|pojo
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"customer"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|pojo
argument_list|)
expr_stmt|;
name|Language
name|lan
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"jsonpath"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|lan
operator|.
name|createExpression
argument_list|(
literal|"$.kind"
argument_list|)
decl_stmt|;
name|String
name|kind
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|kind
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"full"
argument_list|,
name|kind
argument_list|)
expr_stmt|;
name|exp
operator|=
name|lan
operator|.
name|createExpression
argument_list|(
literal|"$.type"
argument_list|)
expr_stmt|;
name|String
name|type
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"customer"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPredicate ()
specifier|public
name|void
name|testPredicate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Test books.json file
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/books.json"
argument_list|)
argument_list|)
expr_stmt|;
name|Language
name|lan
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"jsonpath"
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|lan
operator|.
name|createPredicate
argument_list|(
literal|"$.store.book[?(@.price< 10)]"
argument_list|)
decl_stmt|;
name|boolean
name|cheap
init|=
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should have cheap books"
argument_list|,
name|cheap
argument_list|)
expr_stmt|;
name|pre
operator|=
name|lan
operator|.
name|createPredicate
argument_list|(
literal|"$.store.book[?(@.price> 30)]"
argument_list|)
expr_stmt|;
name|boolean
name|expensive
init|=
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Should not have expensive books"
argument_list|,
name|expensive
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSuppressException ()
specifier|public
name|void
name|testSuppressException
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/type.json"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonPathLanguage
name|lan
init|=
operator|(
name|JsonPathLanguage
operator|)
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"jsonpath"
argument_list|)
decl_stmt|;
name|lan
operator|.
name|setOption
argument_list|(
name|Option
operator|.
name|SUPPRESS_EXCEPTIONS
argument_list|)
expr_stmt|;
name|Expression
name|exp
init|=
name|lan
operator|.
name|createExpression
argument_list|(
literal|"$.foo"
argument_list|)
decl_stmt|;
name|String
name|nofoo
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|nofoo
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

