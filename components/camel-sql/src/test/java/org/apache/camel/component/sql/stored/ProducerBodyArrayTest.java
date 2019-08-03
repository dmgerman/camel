begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
package|;
end_package

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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|After
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
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|datasource
operator|.
name|embedded
operator|.
name|EmbeddedDatabase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|datasource
operator|.
name|embedded
operator|.
name|EmbeddedDatabaseBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|datasource
operator|.
name|embedded
operator|.
name|EmbeddedDatabaseType
import|;
end_import

begin_class
DECL|class|ProducerBodyArrayTest
specifier|public
class|class
name|ProducerBodyArrayTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|db
name|EmbeddedDatabase
name|db
decl_stmt|;
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
name|db
operator|=
operator|new
name|EmbeddedDatabaseBuilder
argument_list|()
operator|.
name|setType
argument_list|(
name|EmbeddedDatabaseType
operator|.
name|DERBY
argument_list|)
operator|.
name|addScript
argument_list|(
literal|"sql/storedProcedureTest.sql"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|db
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteStoredProcedure ()
specifier|public
name|void
name|shouldExecuteStoredProcedure
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:query"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Integer
index|[]
name|numbers
init|=
operator|new
name|Integer
index|[]
block|{
literal|1
block|,
literal|2
block|}
decl_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:query"
argument_list|,
name|numbers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"resultofsub"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlStoredConstants
operator|.
name|SQL_STORED_UPDATE_COUNT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// required for the sql component
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"sql-stored"
argument_list|,
name|SqlStoredComponent
operator|.
name|class
argument_list|)
operator|.
name|setDataSource
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:query"
argument_list|)
operator|.
name|to
argument_list|(
literal|"sql-stored:SUBNUMBERS(INTEGER ${body[0]},INTEGER ${body[1]},OUT INTEGER resultofsub)"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:query"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

