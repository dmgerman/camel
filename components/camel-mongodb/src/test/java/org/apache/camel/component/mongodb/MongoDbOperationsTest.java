begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Formatter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|BasicDBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|DBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|WriteResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|util
operator|.
name|JSON
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
name|properties
operator|.
name|PropertiesComponent
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
name|spring
operator|.
name|SpringCamelContext
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
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|MongoDbOperationsTest
specifier|public
class|class
name|MongoDbOperationsTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|testCountOperation ()
specifier|public
name|void
name|testCountOperation
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
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:count"
argument_list|,
literal|"irrelevantBody"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type Long"
argument_list|,
name|result
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test collection should not contain any records"
argument_list|,
literal|0L
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// Insert a record and test that the endpoint now returns 1
name|testCollection
operator|.
name|insert
argument_list|(
operator|(
name|DBObject
operator|)
name|JSON
operator|.
name|parse
argument_list|(
literal|"{a:60}"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:count"
argument_list|,
literal|"irrelevantBody"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type Long"
argument_list|,
name|result
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test collection should contain 1 record"
argument_list|,
literal|1L
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|testCollection
operator|.
name|remove
argument_list|(
operator|new
name|BasicDBObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// test dynamicity
name|dynamicCollection
operator|.
name|insert
argument_list|(
operator|(
name|DBObject
operator|)
name|JSON
operator|.
name|parse
argument_list|(
literal|"{a:60}"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:count"
argument_list|,
literal|"irrelevantBody"
argument_list|,
name|MongoDbConstants
operator|.
name|COLLECTION
argument_list|,
name|dynamicCollectionName
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type Long"
argument_list|,
name|result
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Dynamic collection should contain 1 record"
argument_list|,
literal|1L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertString ()
specifier|public
name|void
name|testInsertString
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insert"
argument_list|,
literal|"{\"_id\":\"testInsertString\", \"scientist\":\"Einstein\"}"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|WriteResult
argument_list|)
expr_stmt|;
name|DBObject
name|b
init|=
name|testCollection
operator|.
name|findOne
argument_list|(
literal|"testInsertString"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertString' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertArrayStrings ()
specifier|public
name|void
name|testInsertArrayStrings
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
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
literal|"{\"_id\":\"testInsertArrayStrings\", \"scientist\":\"Einstein\"}"
block|,
literal|"{\"_id\":\"testInsertArrayStrings2\", \"scientist\":\"Copernicus\"}"
block|}
decl_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insert"
argument_list|,
name|req
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|WriteResult
argument_list|)
expr_stmt|;
name|DBObject
name|b
init|=
name|testCollection
operator|.
name|findOne
argument_list|(
literal|"testInsertArrayStrings"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertArrayStrings' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|b
operator|=
name|testCollection
operator|.
name|findOne
argument_list|(
literal|"testInsertArrayStrings2"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertArrayStrings2' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSave ()
specifier|public
name|void
name|testSave
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare test
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
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
literal|"{\"_id\":\"testSave1\", \"scientist\":\"Einstein\"}"
block|,
literal|"{\"_id\":\"testSave2\", \"scientist\":\"Copernicus\"}"
block|}
decl_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insert"
argument_list|,
name|req
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|WriteResult
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of records persisted must be 2"
argument_list|,
literal|2
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// Testing the save logic
name|DBObject
name|record1
init|=
name|testCollection
operator|.
name|findOne
argument_list|(
literal|"testSave1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Scientist field of 'testSave1' must equal 'Einstein'"
argument_list|,
literal|"Einstein"
argument_list|,
name|record1
operator|.
name|get
argument_list|(
literal|"scientist"
argument_list|)
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
name|result
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:save"
argument_list|,
name|record1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|WriteResult
argument_list|)
expr_stmt|;
name|record1
operator|=
name|testCollection
operator|.
name|findOne
argument_list|(
literal|"testSave1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Scientist field of 'testSave1' must equal 'Darwin' after save operation"
argument_list|,
literal|"Darwin"
argument_list|,
name|record1
operator|.
name|get
argument_list|(
literal|"scientist"
argument_list|)
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
block|}
annotation|@
name|Test
DECL|method|testUpdate ()
specifier|public
name|void
name|testUpdate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare test
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
literal|null
decl_stmt|;
name|Formatter
name|f
init|=
operator|new
name|Formatter
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|body
operator|=
name|f
operator|.
name|format
argument_list|(
literal|"{\"_id\":\"testSave%d\", \"scientist\":\"Einstein\"}"
argument_list|,
name|i
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|f
operator|.
name|format
argument_list|(
literal|"{\"_id\":\"testSave%d\", \"scientist\":\"Einstein\", \"extraField\": true}"
argument_list|,
name|i
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insert"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|100L
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// Testing the update logic
name|DBObject
name|extraField
init|=
operator|new
name|BasicDBObject
argument_list|(
literal|"extraField"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of records with 'extraField' flag on must equal 50"
argument_list|,
literal|50L
argument_list|,
name|testCollection
operator|.
name|count
argument_list|(
name|extraField
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of records with 'scientist' field = Darwin on must equal 0"
argument_list|,
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|(
operator|new
name|BasicDBObject
argument_list|(
literal|"scientist"
argument_list|,
literal|"Darwin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|DBObject
name|updateObj
init|=
operator|new
name|BasicDBObject
argument_list|(
literal|"$set"
argument_list|,
operator|new
name|BasicDBObject
argument_list|(
literal|"scientist"
argument_list|,
literal|"Darwin"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:update"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|extraField
block|,
name|updateObj
block|}
argument_list|,
name|MongoDbConstants
operator|.
name|MULTIUPDATE
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|WriteResult
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of records with 'scientist' field = Darwin on must equal 50 after update"
argument_list|,
literal|50
argument_list|,
name|testCollection
operator|.
name|count
argument_list|(
operator|new
name|BasicDBObject
argument_list|(
literal|"scientist"
argument_list|,
literal|"Darwin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare test
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
literal|null
decl_stmt|;
name|Formatter
name|f
init|=
operator|new
name|Formatter
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|body
operator|=
name|f
operator|.
name|format
argument_list|(
literal|"{\"_id\":\"testSave%d\", \"scientist\":\"Einstein\"}"
argument_list|,
name|i
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|f
operator|.
name|format
argument_list|(
literal|"{\"_id\":\"testSave%d\", \"scientist\":\"Einstein\", \"extraField\": true}"
argument_list|,
name|i
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insert"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|100L
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// Testing the update logic
name|DBObject
name|extraField
init|=
operator|new
name|BasicDBObject
argument_list|(
literal|"extraField"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of records with 'extraField' flag on must equal 50"
argument_list|,
literal|50L
argument_list|,
name|testCollection
operator|.
name|count
argument_list|(
name|extraField
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:remove"
argument_list|,
name|extraField
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|WriteResult
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of records with 'extraField' flag on must be 0 after remove"
argument_list|,
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|(
name|extraField
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDbStats ()
specifier|public
name|void
name|testDbStats
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:getDbStats"
argument_list|,
literal|"irrelevantBody"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type DBObject"
argument_list|,
name|result
operator|instanceof
name|DBObject
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The result should contain keys"
argument_list|,
operator|(
operator|(
name|DBObject
operator|)
name|result
operator|)
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testColStats ()
specifier|public
name|void
name|testColStats
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// Add some records to the collection (and do it via camel-mongodb)
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
literal|null
decl_stmt|;
name|Formatter
name|f
init|=
operator|new
name|Formatter
argument_list|()
decl_stmt|;
name|body
operator|=
name|f
operator|.
name|format
argument_list|(
literal|"{\"_id\":\"testSave%d\", \"scientist\":\"Einstein\"}"
argument_list|,
name|i
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insert"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:getColStats"
argument_list|,
literal|"irrelevantBody"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type DBObject"
argument_list|,
name|result
operator|instanceof
name|DBObject
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The result should contain keys"
argument_list|,
operator|(
operator|(
name|DBObject
operator|)
name|result
operator|)
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOperationHeader ()
specifier|public
name|void
name|testOperationHeader
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
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// check that the count operation was invoked instead of the insert operation
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:insert"
argument_list|,
literal|"irrelevantBody"
argument_list|,
name|MongoDbConstants
operator|.
name|OPERATION_HEADER
argument_list|,
literal|"count"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type Long"
argument_list|,
name|result
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test collection should not contain any records"
argument_list|,
literal|0L
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// check that the count operation was invoked instead of the insert operation
name|result
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:insert"
argument_list|,
literal|"irrelevantBody"
argument_list|,
name|MongoDbConstants
operator|.
name|OPERATION_HEADER
argument_list|,
name|MongoDbOperation
operator|.
name|count
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type Long"
argument_list|,
name|result
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test collection should not contain any records"
argument_list|,
literal|0L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/mongodb/mongoComponentTest.xml"
argument_list|)
expr_stmt|;
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
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
name|PropertiesComponent
name|pc
init|=
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:mongodb.test.properties"
argument_list|)
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
name|pc
argument_list|)
expr_stmt|;
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
literal|"direct:insert"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=insert"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:save"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=save"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:update"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=update"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:remove"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=remove"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getDbStats"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=getDbStats"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getColStats"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=getColStats"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

