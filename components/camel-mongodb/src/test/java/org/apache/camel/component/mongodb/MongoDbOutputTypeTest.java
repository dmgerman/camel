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
name|com
operator|.
name|mongodb
operator|.
name|DBCursor
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|MongoDbOutputTypeTest
specifier|public
class|class
name|MongoDbOutputTypeTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|testFindAllDBCursor ()
specifier|public
name|void
name|testFindAllDBCursor
parameter_list|()
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
name|pumpDataIntoTestCollection
argument_list|()
expr_stmt|;
comment|// Repeat ten times, obtain 10 batches of 100 results each time
name|int
name|numToSkip
init|=
literal|0
decl_stmt|;
specifier|final
name|int
name|limit
init|=
literal|100
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|MongoDbConstants
operator|.
name|NUM_TO_SKIP
argument_list|,
name|numToSkip
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|MongoDbConstants
operator|.
name|LIMIT
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:findAllDBCursor"
argument_list|,
operator|(
name|Object
operator|)
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type DBCursor"
argument_list|,
name|result
operator|instanceof
name|DBCursor
argument_list|)
expr_stmt|;
name|DBCursor
name|resultCursor
init|=
operator|(
name|DBCursor
operator|)
name|result
decl_stmt|;
comment|// Ensure that all returned documents contain all fields
while|while
condition|(
name|resultCursor
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DBObject
name|dbObject
init|=
name|resultCursor
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"DBObject in returned list should contain all fields"
argument_list|,
name|dbObject
operator|.
name|get
argument_list|(
literal|"_id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"DBObject in returned list should contain all fields"
argument_list|,
name|dbObject
operator|.
name|get
argument_list|(
literal|"scientist"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"DBObject in returned list should contain all fields"
argument_list|,
name|dbObject
operator|.
name|get
argument_list|(
literal|"fixedField"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|numToSkip
operator|=
name|numToSkip
operator|+
name|limit
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFindAllDBObjectList ()
specifier|public
name|void
name|testFindAllDBObjectList
parameter_list|()
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
name|pumpDataIntoTestCollection
argument_list|()
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:findAllDBObjectList"
argument_list|,
operator|(
name|Object
operator|)
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type List"
argument_list|,
name|result
operator|instanceof
name|List
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|DBObject
argument_list|>
name|resultList
init|=
operator|(
name|List
argument_list|<
name|DBObject
argument_list|>
operator|)
name|result
decl_stmt|;
name|assertListSize
argument_list|(
literal|"Result does not contain 1000 elements"
argument_list|,
name|resultList
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
comment|// Ensure that all returned documents contain all fields
for|for
control|(
name|DBObject
name|dbObject
range|:
name|resultList
control|)
block|{
name|assertNotNull
argument_list|(
literal|"DBObject in returned list should contain all fields"
argument_list|,
name|dbObject
operator|.
name|get
argument_list|(
literal|"_id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"DBObject in returned list should contain all fields"
argument_list|,
name|dbObject
operator|.
name|get
argument_list|(
literal|"scientist"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"DBObject in returned list should contain all fields"
argument_list|,
name|dbObject
operator|.
name|get
argument_list|(
literal|"fixedField"
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Exchange
name|resultExchange
range|:
name|getMockEndpoint
argument_list|(
literal|"mock:resultFindAll"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|"Result total size header should equal 1000"
argument_list|,
literal|1000
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MongoDbConstants
operator|.
name|RESULT_TOTAL_SIZE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInitFindWithWrongOutputType ()
specifier|public
name|void
name|testInitFindWithWrongOutputType
parameter_list|()
block|{
try|try
block|{
name|RouteBuilder
name|taillableRouteBuilder
init|=
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
name|from
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=findById&dynamicity=true&outputType=DBCursor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dummy"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|template
operator|.
name|getCamelContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|taillableRouteBuilder
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Endpoint should not be initialized with a non compatible outputType"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Exception is not of type IllegalArgumentException"
argument_list|,
name|exception
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInitTailWithWrongOutputType ()
specifier|public
name|void
name|testInitTailWithWrongOutputType
parameter_list|()
block|{
try|try
block|{
name|RouteBuilder
name|taillableRouteBuilder
init|=
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
name|from
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.cappedTestCollection}}&tailTrackIncreasingField=increasing&outputType=DBCursor"
argument_list|)
operator|.
name|id
argument_list|(
literal|"tailableCursorConsumer1"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|template
operator|.
name|getCamelContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|taillableRouteBuilder
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Endpoint should not be initialized with a non compatible outputType"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Exception is not of type IllegalArgumentException"
argument_list|,
name|exception
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
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
literal|"direct:findAllDBCursor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=findAll&dynamicity=true&outputType=DBCursor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultFindAllDBCursor"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:findAllDBObjectList"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=findAll&outputType=DBObjectList"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultFindAllDBObjectList"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

