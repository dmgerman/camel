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
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|bulk
operator|.
name|BulkWriteResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|DeleteManyModel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|DeleteOneModel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|InsertOneModel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|ReplaceOneModel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|UpdateManyModel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|UpdateOneModel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|WriteModel
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
name|CamelExecutionException
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
name|fail
import|;
end_import

begin_class
DECL|class|MongoDbBulkWriteOperationTest
specifier|public
class|class
name|MongoDbBulkWriteOperationTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|testBulkWrite ()
specifier|public
name|void
name|testBulkWrite
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Test that the collection has 0 documents in it
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
name|pumpDataIntoTestCollection
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|WriteModel
argument_list|<
name|Document
argument_list|>
argument_list|>
name|bulkOperations
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|InsertOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Pierre Curie"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|UpdateOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"2"
argument_list|)
argument_list|,
operator|new
name|Document
argument_list|(
literal|"$set"
argument_list|,
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Charles Darwin"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
operator|new
name|UpdateManyModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Curie"
argument_list|)
argument_list|,
operator|new
name|Document
argument_list|(
literal|"$set"
argument_list|,
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Marie Curie"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
operator|new
name|ReplaceOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"1"
argument_list|)
argument_list|,
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Albert Einstein"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DeleteOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"3"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DeleteManyModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Bohr"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|BulkWriteResult
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:bulkWrite"
argument_list|,
name|bulkOperations
argument_list|,
name|BulkWriteResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|// 1 insert
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getInsertedCount
argument_list|()
argument_list|,
literal|"Records inserted should be 2 : "
argument_list|)
expr_stmt|;
comment|// 1 updateOne + 100 updateMany + 1 replaceOne
name|assertEquals
argument_list|(
literal|102
argument_list|,
name|result
operator|.
name|getMatchedCount
argument_list|()
argument_list|,
literal|"Records matched should be 102 : "
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|102
argument_list|,
name|result
operator|.
name|getModifiedCount
argument_list|()
argument_list|,
literal|"Records modified should be 102 : "
argument_list|)
expr_stmt|;
comment|// 1 deleteOne + 100 deleteMany
name|assertEquals
argument_list|(
literal|101
argument_list|,
name|result
operator|.
name|getDeletedCount
argument_list|()
argument_list|,
literal|"Records deleted should be 101 : "
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOrderedBulkWriteWithError ()
specifier|public
name|void
name|testOrderedBulkWriteWithError
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Test that the collection has 0 documents in it
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
name|pumpDataIntoTestCollection
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|WriteModel
argument_list|<
name|Document
argument_list|>
argument_list|>
name|bulkOperations
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|InsertOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Pierre Curie"
argument_list|)
argument_list|)
argument_list|,
comment|// this insert failed and bulk stop
operator|new
name|InsertOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|InsertOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Descartes"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|UpdateOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"5"
argument_list|)
argument_list|,
operator|new
name|Document
argument_list|(
literal|"$set"
argument_list|,
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Marie Curie"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DeleteOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:bulkWrite"
argument_list|,
name|bulkOperations
argument_list|,
name|BulkWriteResult
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Bulk operation should throw Exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|extractAndAssertCamelMongoDbException
argument_list|(
name|e
argument_list|,
literal|"duplicate key error"
argument_list|)
expr_stmt|;
comment|// count = 1000 records + 1 inserted
name|assertEquals
argument_list|(
literal|1001
argument_list|,
name|testCollection
operator|.
name|countDocuments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testUnorderedBulkWriteWithError ()
specifier|public
name|void
name|testUnorderedBulkWriteWithError
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Test that the collection has 0 documents in it
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
name|pumpDataIntoTestCollection
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|WriteModel
argument_list|<
name|Document
argument_list|>
argument_list|>
name|bulkOperations
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|InsertOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Pierre Curie"
argument_list|)
argument_list|)
argument_list|,
comment|// this insert failed and bulk continue
operator|new
name|InsertOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|InsertOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Descartes"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|UpdateOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"5"
argument_list|)
argument_list|,
operator|new
name|Document
argument_list|(
literal|"$set"
argument_list|,
operator|new
name|Document
argument_list|(
literal|"scientist"
argument_list|,
literal|"Marie Curie"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DeleteOneModel
argument_list|<>
argument_list|(
operator|new
name|Document
argument_list|(
literal|"_id"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:unorderedBulkWrite"
argument_list|,
name|bulkOperations
argument_list|,
name|BulkWriteResult
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Bulk operation should throw Exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|extractAndAssertCamelMongoDbException
argument_list|(
name|e
argument_list|,
literal|"duplicate key error"
argument_list|)
expr_stmt|;
comment|// count = 1000 + 2 inserted + 1 deleted
name|assertEquals
argument_list|(
literal|1001
argument_list|,
name|testCollection
operator|.
name|countDocuments
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|from
argument_list|(
literal|"direct:bulkWrite"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=bulkWrite"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unorderedBulkWrite"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|MongoDbConstants
operator|.
name|BULK_ORDERED
argument_list|)
operator|.
name|constant
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=bulkWrite"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

