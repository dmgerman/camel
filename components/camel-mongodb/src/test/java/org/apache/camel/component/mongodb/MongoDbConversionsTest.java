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
name|io
operator|.
name|ByteArrayInputStream
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
name|Map
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
name|DefaultDBEncoder
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
name|converter
operator|.
name|IOConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|BSONObject
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
DECL|class|MongoDbConversionsTest
specifier|public
class|class
name|MongoDbConversionsTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|testInsertMap ()
specifier|public
name|void
name|testInsertMap
parameter_list|()
throws|throws
name|InterruptedException
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m1
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|m1Nested
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|m1Nested
operator|.
name|put
argument_list|(
literal|"nested1"
argument_list|,
literal|"nestedValue1"
argument_list|)
expr_stmt|;
name|m1Nested
operator|.
name|put
argument_list|(
literal|"nested2"
argument_list|,
literal|"nestedValue2"
argument_list|)
expr_stmt|;
name|m1
operator|.
name|put
argument_list|(
literal|"field1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|m1
operator|.
name|put
argument_list|(
literal|"field2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|m1
operator|.
name|put
argument_list|(
literal|"nestedField"
argument_list|,
name|m1Nested
argument_list|)
expr_stmt|;
name|m1
operator|.
name|put
argument_list|(
literal|"_id"
argument_list|,
literal|"testInsertMap"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insertMap"
argument_list|,
name|m1
argument_list|)
decl_stmt|;
name|BasicDBObject
name|b
init|=
name|testCollection
operator|.
name|find
argument_list|(
operator|new
name|BasicDBObject
argument_list|(
literal|"_id"
argument_list|,
literal|"testInsertMap"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertMap' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertPojo ()
specifier|public
name|void
name|testInsertPojo
parameter_list|()
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
literal|"direct:insertPojo"
argument_list|,
operator|new
name|MyPojoTest
argument_list|()
argument_list|)
decl_stmt|;
name|DBObject
name|b
init|=
name|testCollection
operator|.
name|find
argument_list|(
operator|new
name|BasicDBObject
argument_list|(
literal|"_id"
argument_list|,
literal|"testInsertPojo"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertPojo' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertJsonString ()
specifier|public
name|void
name|testInsertJsonString
parameter_list|()
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
literal|"direct:insertJsonString"
argument_list|,
literal|"{\"fruits\": [\"apple\", \"banana\", \"papaya\"], \"veggie\": \"broccoli\", \"_id\": \"testInsertJsonString\"}"
argument_list|)
decl_stmt|;
comment|//assertTrue(result instanceof WriteResult);
name|DBObject
name|b
init|=
name|testCollection
operator|.
name|find
argument_list|(
operator|new
name|BasicDBObject
argument_list|(
literal|"_id"
argument_list|,
literal|"testInsertJsonString"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertJsonString' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertJsonInputStream ()
specifier|public
name|void
name|testInsertJsonInputStream
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
literal|"direct:insertJsonString"
argument_list|,
name|IOConverter
operator|.
name|toInputStream
argument_list|(
literal|"{\"fruits\": [\"apple\", \"banana\"], \"veggie\": \"broccoli\", \"_id\": \"testInsertJsonString\"}\n"
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|DBObject
name|b
init|=
name|testCollection
operator|.
name|find
argument_list|(
operator|new
name|BasicDBObject
argument_list|(
literal|"_id"
argument_list|,
literal|"testInsertJsonString"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertJsonString' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertBsonInputStream ()
specifier|public
name|void
name|testInsertBsonInputStream
parameter_list|()
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
name|DefaultDBEncoder
name|encoder
init|=
operator|new
name|DefaultDBEncoder
argument_list|()
decl_stmt|;
name|BSONObject
name|bsonObject
init|=
operator|new
name|BasicDBObject
argument_list|()
decl_stmt|;
name|bsonObject
operator|.
name|put
argument_list|(
literal|"_id"
argument_list|,
literal|"testInsertBsonString"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insertJsonString"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|encoder
operator|.
name|encode
argument_list|(
name|bsonObject
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|DBObject
name|b
init|=
name|testCollection
operator|.
name|find
argument_list|(
operator|new
name|BasicDBObject
argument_list|(
literal|"_id"
argument_list|,
literal|"testInsertBsonString"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertBsonString' _id"
argument_list|,
name|b
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
name|from
argument_list|(
literal|"direct:insertMap"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=insert"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:insertPojo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=insert"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:insertJsonString"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=insert"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:insertJsonStringWriteResultInString"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=insert"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|class|MyPojoTest
specifier|private
class|class
name|MyPojoTest
block|{
DECL|field|number
specifier|public
name|int
name|number
init|=
literal|123
decl_stmt|;
DECL|field|text
specifier|public
name|String
name|text
init|=
literal|"hello"
decl_stmt|;
DECL|field|array
specifier|public
name|String
index|[]
name|array
init|=
block|{
literal|"daVinci"
block|,
literal|"copernico"
block|,
literal|"einstein"
block|}
decl_stmt|;
comment|// CHECKSTYLE:OFF
DECL|field|_id
specifier|public
name|String
name|_id
init|=
literal|"testInsertPojo"
decl_stmt|;
comment|// CHECKSTYLE:ON
block|}
block|}
end_class

end_unit

