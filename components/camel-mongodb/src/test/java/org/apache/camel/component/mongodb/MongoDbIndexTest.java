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
name|ArrayList
import|;
end_import

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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|StreamSupport
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
name|client
operator|.
name|ListIndexesIterable
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
name|MongoCollection
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
name|MongoCursor
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
name|bson
operator|.
name|conversions
operator|.
name|Bson
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|Indexes
operator|.
name|ascending
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
name|Indexes
operator|.
name|descending
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

begin_class
DECL|class|MongoDbIndexTest
specifier|public
class|class
name|MongoDbIndexTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|testInsertDynamicityEnabledDBAndCollectionAndIndex ()
specifier|public
name|void
name|testInsertDynamicityEnabledDBAndCollectionAndIndex
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
name|mongo
operator|.
name|getDatabase
argument_list|(
literal|"otherDB"
argument_list|)
operator|.
name|drop
argument_list|()
expr_stmt|;
name|db
operator|.
name|getCollection
argument_list|(
literal|"otherCollection"
argument_list|)
operator|.
name|drop
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"The otherDB database should not exist"
argument_list|,
name|StreamSupport
operator|.
name|stream
argument_list|(
name|mongo
operator|.
name|listDatabaseNames
argument_list|()
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|anyMatch
argument_list|(
literal|"otherDB"
operator|::
name|equals
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|"{\"_id\": \"testInsertDynamicityEnabledDBAndCollection\", \"a\" : 1, \"b\" : 2}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|MongoDbConstants
operator|.
name|DATABASE
argument_list|,
literal|"otherDB"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|MongoDbConstants
operator|.
name|COLLECTION
argument_list|,
literal|"otherCollection"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Document
argument_list|>
name|objIndex
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Document
name|index1
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
name|index1
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Document
name|index2
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
name|index2
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|objIndex
operator|.
name|add
argument_list|(
name|index1
argument_list|)
expr_stmt|;
name|objIndex
operator|.
name|add
argument_list|(
name|index2
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|MongoDbConstants
operator|.
name|COLLECTION_INDEX
argument_list|,
name|objIndex
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:dynamicityEnabled"
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Response isn't of type WriteResult"
argument_list|,
name|Document
operator|.
name|class
argument_list|,
name|result
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|MongoCollection
argument_list|<
name|Document
argument_list|>
name|localDynamicCollection
init|=
name|mongo
operator|.
name|getDatabase
argument_list|(
literal|"otherDB"
argument_list|)
operator|.
name|getCollection
argument_list|(
literal|"otherCollection"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|ListIndexesIterable
argument_list|<
name|Document
argument_list|>
name|indexInfos
init|=
name|localDynamicCollection
operator|.
name|listIndexes
argument_list|(
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|MongoCursor
argument_list|<
name|Document
argument_list|>
name|iterator
init|=
name|indexInfos
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|Document
name|key1
init|=
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|key2
init|=
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No index on the field a"
argument_list|,
name|key1
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
operator|&&
literal|1
operator|==
name|key1
operator|.
name|getInteger
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No index on the field b"
argument_list|,
name|key2
operator|.
name|containsKey
argument_list|(
literal|"b"
argument_list|)
operator|&&
operator|-
literal|1
operator|==
name|key2
operator|.
name|getInteger
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|b
init|=
name|localDynamicCollection
operator|.
name|find
argument_list|(
operator|new
name|Document
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertDynamicityEnabledDBAndCollection"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertDynamicityEnabledDBAndCollection' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|b
operator|=
name|testCollection
operator|.
name|find
argument_list|(
operator|new
name|Document
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertDynamicityEnabledDBOnly"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"There is a record with 'testInsertDynamicityEnabledDBAndCollection' _id in the test collection"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The otherDB database should exist"
argument_list|,
name|StreamSupport
operator|.
name|stream
argument_list|(
name|mongo
operator|.
name|listDatabaseNames
argument_list|()
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|anyMatch
argument_list|(
literal|"otherDB"
operator|::
name|equals
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertDynamicityEnabledCollectionAndIndex ()
specifier|public
name|void
name|testInsertDynamicityEnabledCollectionAndIndex
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
name|mongo
operator|.
name|getDatabase
argument_list|(
literal|"otherDB"
argument_list|)
operator|.
name|drop
argument_list|()
expr_stmt|;
name|db
operator|.
name|getCollection
argument_list|(
literal|"otherCollection"
argument_list|)
operator|.
name|drop
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"The otherDB database should not exist"
argument_list|,
name|StreamSupport
operator|.
name|stream
argument_list|(
name|mongo
operator|.
name|listDatabaseNames
argument_list|()
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|anyMatch
argument_list|(
literal|"otherDB"
operator|::
name|equals
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|"{\"_id\": \"testInsertDynamicityEnabledCollectionAndIndex\", \"a\" : 1, \"b\" : 2}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|MongoDbConstants
operator|.
name|COLLECTION
argument_list|,
literal|"otherCollection"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Bson
argument_list|>
name|objIndex
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|ascending
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|descending
argument_list|(
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|MongoDbConstants
operator|.
name|COLLECTION_INDEX
argument_list|,
name|objIndex
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:dynamicityEnabled"
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Response isn't of type WriteResult"
argument_list|,
name|Document
operator|.
name|class
argument_list|,
name|result
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|MongoCollection
argument_list|<
name|Document
argument_list|>
name|localDynamicCollection
init|=
name|db
operator|.
name|getCollection
argument_list|(
literal|"otherCollection"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|MongoCursor
argument_list|<
name|Document
argument_list|>
name|indexInfos
init|=
name|localDynamicCollection
operator|.
name|listIndexes
argument_list|(
name|Document
operator|.
name|class
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|indexInfos
operator|.
name|next
argument_list|()
expr_stmt|;
name|Document
name|key1
init|=
name|indexInfos
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|key2
init|=
name|indexInfos
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No index on the field a"
argument_list|,
name|key1
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
operator|&&
literal|1
operator|==
name|key1
operator|.
name|getInteger
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No index on the field b"
argument_list|,
name|key2
operator|.
name|containsKey
argument_list|(
literal|"b"
argument_list|)
operator|&&
operator|-
literal|1
operator|==
name|key2
operator|.
name|getInteger
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|b
init|=
name|localDynamicCollection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertDynamicityEnabledCollectionAndIndex"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertDynamicityEnabledCollectionAndIndex' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|b
operator|=
name|testCollection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertDynamicityEnabledDBOnly"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"There is a record with 'testInsertDynamicityEnabledDBAndCollection' _id in the test collection"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"The otherDB database should not exist"
argument_list|,
name|mongo
operator|.
name|getUsedDatabases
argument_list|()
operator|.
name|contains
argument_list|(
literal|"otherDB"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertDynamicityEnabledCollectionOnlyAndURIIndex ()
specifier|public
name|void
name|testInsertDynamicityEnabledCollectionOnlyAndURIIndex
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
name|mongo
operator|.
name|getDatabase
argument_list|(
literal|"otherDB"
argument_list|)
operator|.
name|drop
argument_list|()
expr_stmt|;
name|db
operator|.
name|getCollection
argument_list|(
literal|"otherCollection"
argument_list|)
operator|.
name|drop
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"The otherDB database should not exist"
argument_list|,
name|StreamSupport
operator|.
name|stream
argument_list|(
name|mongo
operator|.
name|listDatabaseNames
argument_list|()
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|anyMatch
argument_list|(
literal|"otherDB"
operator|::
name|equals
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|"{\"_id\": \"testInsertDynamicityEnabledCollectionOnlyAndURIIndex\", \"a\" : 1, \"b\" : 2}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|MongoDbConstants
operator|.
name|COLLECTION
argument_list|,
literal|"otherCollection"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:dynamicityEnabledWithIndexUri"
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Response isn't of type WriteResult"
argument_list|,
name|Document
operator|.
name|class
argument_list|,
name|result
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|MongoCollection
argument_list|<
name|Document
argument_list|>
name|localDynamicCollection
init|=
name|db
operator|.
name|getCollection
argument_list|(
literal|"otherCollection"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|MongoCursor
argument_list|<
name|Document
argument_list|>
name|indexInfos
init|=
name|localDynamicCollection
operator|.
name|listIndexes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Document
name|key1
init|=
name|indexInfos
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"No index on the field a"
argument_list|,
name|key1
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
operator|&&
literal|"-1"
operator|.
name|equals
argument_list|(
name|key1
operator|.
name|getString
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|b
init|=
name|localDynamicCollection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertDynamicityEnabledCollectionOnlyAndURIIndex"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertDynamicityEnabledCollectionOnlyAndURIIndex' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|b
operator|=
name|testCollection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertDynamicityEnabledCollectionOnlyAndURIIndex"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"There is a record with 'testInsertDynamicityEnabledCollectionOnlyAndURIIndex' _id in the test collection"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"The otherDB database should not exist"
argument_list|,
name|StreamSupport
operator|.
name|stream
argument_list|(
name|mongo
operator|.
name|listDatabaseNames
argument_list|()
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|anyMatch
argument_list|(
literal|"otherDB"
operator|::
name|equals
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testInsertAutoCreateCollectionAndURIIndex ()
specifier|public
name|void
name|testInsertAutoCreateCollectionAndURIIndex
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
name|db
operator|.
name|getCollection
argument_list|(
literal|"otherCollection"
argument_list|)
operator|.
name|deleteOne
argument_list|(
operator|new
name|Document
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|"{\"_id\": \"testInsertAutoCreateCollectionAndURIIndex\", \"a\" : 1, \"b\" : 2}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:dynamicityDisabled"
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Response isn't of type WriteResult"
argument_list|,
name|WriteResult
operator|.
name|class
argument_list|,
name|result
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|MongoCollection
argument_list|<
name|Document
argument_list|>
name|collection
init|=
name|db
operator|.
name|getCollection
argument_list|(
literal|"otherCollection"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|MongoCursor
argument_list|<
name|Document
argument_list|>
name|indexInfos
init|=
name|collection
operator|.
name|listIndexes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Document
name|key1
init|=
name|indexInfos
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|key2
init|=
name|indexInfos
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No index on the field b"
argument_list|,
name|key1
operator|.
name|containsKey
argument_list|(
literal|"b"
argument_list|)
operator|&&
literal|"-1"
operator|.
name|equals
argument_list|(
name|key1
operator|.
name|getString
argument_list|(
literal|"b"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No index on the field a"
argument_list|,
name|key2
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
operator|&&
literal|"1"
operator|.
name|equals
argument_list|(
name|key2
operator|.
name|getString
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|b
init|=
name|collection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertAutoCreateCollectionAndURIIndex"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No record with 'testInsertAutoCreateCollectionAndURIIndex' _id"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|b
operator|=
name|testCollection
operator|.
name|find
argument_list|(
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
literal|"testInsertAutoCreateCollectionAndURIIndex"
argument_list|)
argument_list|)
operator|.
name|first
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"There is a record with 'testInsertAutoCreateCollectionAndURIIndex' _id in the test collection"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"The otherDB database should not exist"
argument_list|,
name|StreamSupport
operator|.
name|stream
argument_list|(
name|mongo
operator|.
name|listDatabaseNames
argument_list|()
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|anyMatch
argument_list|(
literal|"otherDB"
operator|::
name|equals
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
name|from
argument_list|(
literal|"direct:dynamicityEnabled"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=insert&dynamicity=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:dynamicityEnabledWithIndexUri"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&collectionIndex={\"a\":1}&operation=insert&dynamicity=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:dynamicityDisabled"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection=otherCollection&collectionIndex={\"a\":1,\"b\":-1}&operation=insert&dynamicity=false"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

