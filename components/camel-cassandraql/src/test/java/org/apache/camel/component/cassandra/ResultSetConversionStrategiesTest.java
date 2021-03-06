begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cassandra
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|Row
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
name|assertSame
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
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link ResultSetConversionStrategy} implementations  */
end_comment

begin_class
DECL|class|ResultSetConversionStrategiesTest
specifier|public
class|class
name|ResultSetConversionStrategiesTest
block|{
DECL|method|ResultSetConversionStrategiesTest ()
specifier|public
name|ResultSetConversionStrategiesTest
parameter_list|()
block|{     }
annotation|@
name|Test
DECL|method|testAll ()
specifier|public
name|void
name|testAll
parameter_list|()
block|{
name|ResultSetConversionStrategy
name|strategy
init|=
name|ResultSetConversionStrategies
operator|.
name|fromName
argument_list|(
literal|"ALL"
argument_list|)
decl_stmt|;
name|ResultSet
name|resultSet
init|=
name|mock
argument_list|(
name|ResultSet
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
name|Collections
operator|.
name|nCopies
argument_list|(
literal|20
argument_list|,
name|mock
argument_list|(
name|Row
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|resultSet
operator|.
name|all
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|rows
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|strategy
operator|.
name|getBody
argument_list|(
name|resultSet
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|rows
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOne ()
specifier|public
name|void
name|testOne
parameter_list|()
block|{
name|ResultSetConversionStrategy
name|strategy
init|=
name|ResultSetConversionStrategies
operator|.
name|fromName
argument_list|(
literal|"ONE"
argument_list|)
decl_stmt|;
name|ResultSet
name|resultSet
init|=
name|mock
argument_list|(
name|ResultSet
operator|.
name|class
argument_list|)
decl_stmt|;
name|Row
name|row
init|=
name|mock
argument_list|(
name|Row
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|resultSet
operator|.
name|one
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|strategy
operator|.
name|getBody
argument_list|(
name|resultSet
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|Row
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|row
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLimit ()
specifier|public
name|void
name|testLimit
parameter_list|()
block|{
name|ResultSetConversionStrategy
name|strategy
init|=
name|ResultSetConversionStrategies
operator|.
name|fromName
argument_list|(
literal|"LIMIT_10"
argument_list|)
decl_stmt|;
name|ResultSet
name|resultSet
init|=
name|mock
argument_list|(
name|ResultSet
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
name|Collections
operator|.
name|nCopies
argument_list|(
literal|20
argument_list|,
name|mock
argument_list|(
name|Row
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|resultSet
operator|.
name|iterator
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|rows
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|strategy
operator|.
name|getBody
argument_list|(
name|resultSet
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
operator|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|body
operator|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

