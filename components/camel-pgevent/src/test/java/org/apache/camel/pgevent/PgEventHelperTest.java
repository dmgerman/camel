begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.pgevent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|pgevent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|impossibl
operator|.
name|postgres
operator|.
name|api
operator|.
name|jdbc
operator|.
name|PGConnection
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
name|component
operator|.
name|pgevent
operator|.
name|PgEventHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|PgEventHelperTest
specifier|public
class|class
name|PgEventHelperTest
block|{
annotation|@
name|Rule
DECL|field|expectedException
specifier|public
name|ExpectedException
name|expectedException
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testToPGConnectionWithNullConnection ()
specifier|public
name|void
name|testToPGConnectionWithNullConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
expr_stmt|;
name|PgEventHelper
operator|.
name|toPGConnection
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToPGConnectionWithNonWrappedConnection ()
specifier|public
name|void
name|testToPGConnectionWithNonWrappedConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|originalConnection
init|=
name|mock
argument_list|(
name|PGConnection
operator|.
name|class
argument_list|)
decl_stmt|;
name|PGConnection
name|actualConnection
init|=
name|PgEventHelper
operator|.
name|toPGConnection
argument_list|(
name|originalConnection
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|originalConnection
argument_list|,
name|actualConnection
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToPGConnectionWithWrappedConnection ()
specifier|public
name|void
name|testToPGConnectionWithWrappedConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|wrapperConnection
init|=
name|mock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
decl_stmt|;
name|PGConnection
name|unwrappedConnection
init|=
name|mock
argument_list|(
name|PGConnection
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|wrapperConnection
operator|.
name|isWrapperFor
argument_list|(
name|PGConnection
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|wrapperConnection
operator|.
name|unwrap
argument_list|(
name|PGConnection
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|unwrappedConnection
argument_list|)
expr_stmt|;
name|PGConnection
name|actualConnection
init|=
name|PgEventHelper
operator|.
name|toPGConnection
argument_list|(
name|wrapperConnection
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|unwrappedConnection
argument_list|,
name|actualConnection
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToPGConnectionWithInvalidWrappedConnection ()
specifier|public
name|void
name|testToPGConnectionWithInvalidWrappedConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|)
expr_stmt|;
name|Connection
name|wrapperConnection
init|=
name|mock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|wrapperConnection
operator|.
name|isWrapperFor
argument_list|(
name|PGConnection
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|PgEventHelper
operator|.
name|toPGConnection
argument_list|(
name|wrapperConnection
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

