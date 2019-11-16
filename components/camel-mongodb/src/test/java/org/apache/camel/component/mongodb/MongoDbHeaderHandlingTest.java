begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|result
operator|.
name|UpdateResult
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
name|Processor
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
name|bson
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|Filters
operator|.
name|eq
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
name|mongodb
operator|.
name|MongoDbConstants
operator|.
name|MONGO_ID
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
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
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|MongoDbHeaderHandlingTest
specifier|public
class|class
name|MongoDbHeaderHandlingTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|testInHeadersTransferredToOutOnCount ()
specifier|public
name|void
name|testInHeadersTransferredToOutOnCount
parameter_list|()
block|{
comment|// a read operation
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|countDocuments
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|result
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:count"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"irrelevant body"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"abc"
argument_list|,
literal|"def"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|Long
argument_list|,
literal|"Result is not of type Long"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|result
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
literal|"Test collection should not contain any records"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"def"
argument_list|,
name|result
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|"An input header was not returned"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInHeadersTransferredToOutOnInsert ()
specifier|public
name|void
name|testInHeadersTransferredToOutOnInsert
parameter_list|()
block|{
name|Exchange
name|result
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:insert"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"{\"_id\":\"testInsertString\", \"scientist\":\"Einstein\"}"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"abc"
argument_list|,
literal|"def"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// TODO: WriteResult isn't return when inserting
comment|// assertTrue(result.getOut().getBody() instanceof WriteResult);
name|assertEquals
argument_list|(
literal|"def"
argument_list|,
name|result
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|"An input header was not returned"
argument_list|)
expr_stmt|;
name|Document
name|b
init|=
name|testCollection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertString"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|b
argument_list|,
literal|"No record with 'testInsertString' _id"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteResultAsHeaderWithWriteOp ()
specifier|public
name|void
name|testWriteResultAsHeaderWithWriteOp
parameter_list|()
block|{
comment|// Prepare test
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|countDocuments
argument_list|()
argument_list|)
expr_stmt|;
name|Object
index|[]
name|req
init|=
operator|new
name|Object
index|[]
block|{
operator|new
name|Document
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testSave1"
argument_list|)
operator|.
name|append
argument_list|(
literal|"scientist"
argument_list|,
literal|"Einstein"
argument_list|)
operator|.
name|toJson
argument_list|()
block|,
operator|new
name|Document
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testSave2"
argument_list|)
operator|.
name|append
argument_list|(
literal|"scientist"
argument_list|,
literal|"Copernicus"
argument_list|)
operator|.
name|toJson
argument_list|()
block|}
decl_stmt|;
comment|// Object result =
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insert"
argument_list|,
name|req
argument_list|)
expr_stmt|;
comment|// assertTrue(result instanceof WriteResult);
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|testCollection
operator|.
name|countDocuments
argument_list|()
argument_list|,
literal|"Number of records persisted must be 2"
argument_list|)
expr_stmt|;
comment|// Testing the save logic
specifier|final
name|Document
name|record1
init|=
name|testCollection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testSave1"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Einstein"
argument_list|,
name|record1
operator|.
name|get
argument_list|(
literal|"scientist"
argument_list|)
argument_list|,
literal|"Scientist field of 'testSave1' must equal 'Einstein'"
argument_list|)
expr_stmt|;
name|record1
operator|.
name|put
argument_list|(
literal|"scientist"
argument_list|,
literal|"Darwin"
argument_list|)
expr_stmt|;
comment|// test that as a payload, we get back exactly our input, but enriched
comment|// with the CamelMongoDbWriteResult header
name|Exchange
name|resultExch
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:save"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|record1
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|resultExch
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|Document
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultExch
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|equals
argument_list|(
name|record1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultExch
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MongoDbConstants
operator|.
name|WRITERESULT
argument_list|)
operator|instanceof
name|UpdateResult
argument_list|)
expr_stmt|;
name|Document
name|record2
init|=
name|testCollection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testSave1"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Darwin"
argument_list|,
name|record2
operator|.
name|get
argument_list|(
literal|"scientist"
argument_list|)
argument_list|,
literal|"Scientist field of 'testSave1' must equal 'Darwin' after save operation"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteResultAsHeaderWithReadOp ()
specifier|public
name|void
name|testWriteResultAsHeaderWithReadOp
parameter_list|()
block|{
name|Exchange
name|resultExch
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:getDbStats"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"irrelevantBody"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"abc"
argument_list|,
literal|"def"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|resultExch
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|Document
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExch
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MongoDbConstants
operator|.
name|WRITERESULT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"def"
argument_list|,
name|resultExch
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"abc"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// tested routes
name|from
argument_list|(
literal|"direct:count"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=count&dynamicity=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:save"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=save&writeResultAsHeader=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getDbStats"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=getDbStats&writeResultAsHeader=true"
argument_list|)
expr_stmt|;
comment|// supporting routes
name|from
argument_list|(
literal|"direct:insert"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=insert"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

