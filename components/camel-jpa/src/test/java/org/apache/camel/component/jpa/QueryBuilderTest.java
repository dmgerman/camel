begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jpa
package|;
end_package

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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|QueryBuilderTest
specifier|public
class|class
name|QueryBuilderTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testQueryBuilder ()
specifier|public
name|void
name|testQueryBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|QueryBuilder
name|q
init|=
name|QueryBuilder
operator|.
name|query
argument_list|(
literal|"select x from SendEmail x"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Query: select x from SendEmail x"
argument_list|,
name|q
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNamedQueryBuilder ()
specifier|public
name|void
name|testNamedQueryBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|QueryBuilder
name|q
init|=
name|QueryBuilder
operator|.
name|namedQuery
argument_list|(
literal|"step1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Named: step1"
argument_list|,
name|q
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNativeQueryBuilder ()
specifier|public
name|void
name|testNativeQueryBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|QueryBuilder
name|q
init|=
name|QueryBuilder
operator|.
name|nativeQuery
argument_list|(
literal|"select count(*) from SendEmail"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NativeQuery: select count(*) from SendEmail"
argument_list|,
name|q
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testQueryBuilderWithParameters ()
specifier|public
name|void
name|testQueryBuilderWithParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|QueryBuilder
name|q
init|=
name|QueryBuilder
operator|.
name|query
argument_list|(
literal|"select x from SendEmail x where x.id = :a"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|q
operator|.
name|parameters
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Query: select x from SendEmail x where x.id = :a Parameters: [1]"
argument_list|,
name|q
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testQueryBuilderWithParametersMap ()
specifier|public
name|void
name|testQueryBuilderWithParametersMap
parameter_list|()
throws|throws
name|Exception
block|{
name|QueryBuilder
name|q
init|=
name|QueryBuilder
operator|.
name|query
argument_list|(
literal|"select x from SendEmail x where x.id = :a"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|q
operator|.
name|parameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Query: select x from SendEmail x where x.id = :a Parameters: {a=1}"
argument_list|,
name|q
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

