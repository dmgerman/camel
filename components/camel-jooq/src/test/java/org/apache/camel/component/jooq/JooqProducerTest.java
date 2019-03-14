begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jooq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jooq
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
name|ExchangePattern
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
name|ProducerTemplate
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
name|RoutesBuilder
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
name|jooq
operator|.
name|db
operator|.
name|tables
operator|.
name|records
operator|.
name|BookStoreRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jooq
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jooq
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jooq
operator|.
name|ResultQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|jooq
operator|.
name|db
operator|.
name|Tables
operator|.
name|BOOK_STORE
import|;
end_import

begin_class
DECL|class|JooqProducerTest
specifier|public
class|class
name|JooqProducerTest
extends|extends
name|BaseJooqTest
block|{
annotation|@
name|Test
DECL|method|testCRUD ()
specifier|public
name|void
name|testCRUD
parameter_list|()
block|{
name|ProducerTemplate
name|producerTemplate
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
comment|// Insert and select
name|BookStoreRecord
name|bookStoreRecord
init|=
operator|new
name|BookStoreRecord
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:insert"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|bookStoreRecord
argument_list|)
expr_stmt|;
name|ResultQuery
name|querySelect
init|=
name|create
operator|.
name|selectFrom
argument_list|(
name|BOOK_STORE
argument_list|)
operator|.
name|where
argument_list|(
name|BOOK_STORE
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|Result
name|actual
init|=
operator|(
name|Result
operator|)
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:select"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|querySelect
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|actual
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bookStoreRecord
argument_list|,
name|actual
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// Update and select
name|String
name|newName
init|=
literal|"testNew"
decl_stmt|;
name|Query
name|query
init|=
name|create
operator|.
name|update
argument_list|(
name|BOOK_STORE
argument_list|)
operator|.
name|set
argument_list|(
name|BOOK_STORE
operator|.
name|NAME
argument_list|,
name|newName
argument_list|)
operator|.
name|where
argument_list|(
name|BOOK_STORE
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:update"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|querySelect
operator|=
name|create
operator|.
name|selectFrom
argument_list|(
name|BOOK_STORE
argument_list|)
operator|.
name|where
argument_list|(
name|BOOK_STORE
operator|.
name|NAME
operator|.
name|eq
argument_list|(
name|newName
argument_list|)
argument_list|)
expr_stmt|;
name|actual
operator|=
operator|(
name|Result
operator|)
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:select"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|querySelect
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|actual
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|newName
argument_list|,
operator|(
operator|(
name|BookStoreRecord
operator|)
name|actual
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Delete and select
name|query
operator|=
name|create
operator|.
name|delete
argument_list|(
name|BOOK_STORE
argument_list|)
operator|.
name|where
argument_list|(
name|BOOK_STORE
operator|.
name|NAME
operator|.
name|eq
argument_list|(
name|newName
argument_list|)
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:delete"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|querySelect
operator|=
name|create
operator|.
name|selectFrom
argument_list|(
name|BOOK_STORE
argument_list|)
operator|.
name|where
argument_list|(
name|BOOK_STORE
operator|.
name|NAME
operator|.
name|eq
argument_list|(
name|newName
argument_list|)
argument_list|)
expr_stmt|;
name|actual
operator|=
operator|(
name|Result
operator|)
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:select"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|querySelect
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|actual
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
block|{
name|JooqComponent
name|jooqComponent
init|=
operator|(
name|JooqComponent
operator|)
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"jooq"
argument_list|)
decl_stmt|;
name|JooqConfiguration
name|jooqConfiguration
init|=
operator|new
name|JooqConfiguration
argument_list|()
decl_stmt|;
name|jooqConfiguration
operator|.
name|setDatabaseConfiguration
argument_list|(
name|create
operator|.
name|configuration
argument_list|()
argument_list|)
expr_stmt|;
name|jooqComponent
operator|.
name|setConfiguration
argument_list|(
name|jooqConfiguration
argument_list|)
expr_stmt|;
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
block|{
name|from
argument_list|(
literal|"direct:insert"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jooq://org.apache.camel.component.jooq.db.tables.records.BookStoreRecord"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:update"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jooq://org.apache.camel.component.jooq.db.tables.records.BookStoreRecord/execute"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:delete"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jooq://org.apache.camel.component.jooq.db.tables.records.BookStoreRecord/execute"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:select"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jooq://org.apache.camel.component.jooq.db.tables.records.BookStoreRecord/fetch"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

