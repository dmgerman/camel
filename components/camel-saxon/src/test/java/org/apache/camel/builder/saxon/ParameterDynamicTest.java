begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.saxon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|saxon
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|om
operator|.
name|Item
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|om
operator|.
name|StructuredQName
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|query
operator|.
name|DynamicQueryContext
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|query
operator|.
name|XQueryExpression
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|value
operator|.
name|BooleanValue
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|value
operator|.
name|ObjectValue
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
operator|.
name|XQueryBuilder
operator|.
name|xquery
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
DECL|class|ParameterDynamicTest
specifier|public
class|class
name|ParameterDynamicTest
block|{
DECL|field|TEST_QUERY
specifier|private
specifier|static
specifier|final
name|String
name|TEST_QUERY
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"xquery version \"3.0\" encoding \"UTF-8\";\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"declare variable $extParam as xs:boolean external := false();\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"if($extParam) then(true()) else (false())"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
DECL|field|conf
specifier|private
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
DECL|field|query
specifier|private
name|XQueryExpression
name|query
decl_stmt|;
DECL|field|context
specifier|private
name|DynamicQueryContext
name|context
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|conf
operator|.
name|setCompileWithTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|query
operator|=
name|conf
operator|.
name|newStaticQueryContext
argument_list|()
operator|.
name|compileQuery
argument_list|(
name|TEST_QUERY
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DynamicQueryContext
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is what Camel XQueryBuilder executes, which leads to a parameter binding type error.      */
annotation|@
name|Test
DECL|method|testObjectParameter ()
specifier|public
name|void
name|testObjectParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|setParameter
argument_list|(
name|StructuredQName
operator|.
name|fromClarkName
argument_list|(
literal|"extParam"
argument_list|)
argument_list|,
operator|new
name|ObjectValue
argument_list|<>
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Item
name|result
init|=
name|query
operator|.
name|iterator
argument_list|(
name|context
argument_list|)
operator|.
name|next
argument_list|()
decl_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|BooleanValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
operator|(
operator|(
name|BooleanValue
operator|)
name|result
operator|)
operator|.
name|getBooleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
comment|/**      * This is what Camel XQueryBuilder should execute to allow Saxon to bind the parameter type properly.      */
annotation|@
name|Test
DECL|method|testBooleanParameter ()
specifier|public
name|void
name|testBooleanParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|setParameter
argument_list|(
name|StructuredQName
operator|.
name|fromClarkName
argument_list|(
literal|"extParam"
argument_list|)
argument_list|,
name|BooleanValue
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Item
name|result
init|=
name|query
operator|.
name|iterator
argument_list|(
name|context
argument_list|)
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|BooleanValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
operator|(
operator|(
name|BooleanValue
operator|)
name|result
operator|)
operator|.
name|getBooleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXQueryBuilder ()
specifier|public
name|void
name|testXQueryBuilder
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
operator|new
name|DefaultCamelContext
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
literal|"<foo><bar>abc_def_ghi</bar></foo>"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"extParam"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|xquery
argument_list|(
name|TEST_QUERY
argument_list|)
operator|.
name|asString
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"extParam"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|result
operator|=
name|xquery
argument_list|(
name|TEST_QUERY
argument_list|)
operator|.
name|asString
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

