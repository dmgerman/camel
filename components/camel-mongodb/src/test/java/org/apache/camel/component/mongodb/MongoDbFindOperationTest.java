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
name|com
operator|.
name|mongodb
operator|.
name|BasicDBObjectBuilder
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
DECL|class|MongoDbFindOperationTest
specifier|public
class|class
name|MongoDbFindOperationTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|testFindAllNoCriteriaOperation ()
specifier|public
name|void
name|testFindAllNoCriteriaOperation
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
literal|"direct:findAll"
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
literal|"Result does not contain all entries in collection"
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
name|Exchange
name|resultExchange
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultFindAll"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|"Result page size header should equal 1000"
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
name|RESULT_PAGE_SIZE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindAllNoCriteriaWithFilterOperation ()
specifier|public
name|void
name|testFindAllNoCriteriaWithFilterOperation
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
name|pumpDataIntoTestCollection
argument_list|()
expr_stmt|;
name|DBObject
name|fieldFilter
init|=
name|BasicDBObjectBuilder
operator|.
name|start
argument_list|()
operator|.
name|add
argument_list|(
literal|"_id"
argument_list|,
literal|0
argument_list|)
operator|.
name|add
argument_list|(
literal|"fixedField"
argument_list|,
literal|0
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:findAll"
argument_list|,
operator|(
name|Object
operator|)
literal|null
argument_list|,
name|MongoDbConstants
operator|.
name|FIELDS_FILTER
argument_list|,
name|fieldFilter
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
literal|"Result does not contain all entries in collection"
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
name|assertNull
argument_list|(
literal|"DBObject in returned list should not contain field _id"
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
literal|"DBObject in returned list does not contain field 'scientist'"
argument_list|,
name|dbObject
operator|.
name|get
argument_list|(
literal|"scientist"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"DBObject in returned list should not contain field fixedField"
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
name|Exchange
name|resultExchange
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultFindAll"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|"Result page size header should equal 1000"
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
name|RESULT_PAGE_SIZE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindAllIterationOperation ()
specifier|public
name|void
name|testFindAllIterationOperation
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
name|HashMap
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
literal|"direct:findAll"
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
literal|"Result does not contain 100 elements"
argument_list|,
name|resultList
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Id of first record is not as expected"
argument_list|,
name|numToSkip
operator|+
literal|1
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
operator|(
name|String
operator|)
name|resultList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|"_id"
argument_list|)
argument_list|)
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
name|numToSkip
operator|=
name|numToSkip
operator|+
name|limit
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
name|assertEquals
argument_list|(
literal|"Result page size header should equal 100"
argument_list|,
literal|100
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
name|RESULT_PAGE_SIZE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFindOneByQuery ()
specifier|public
name|void
name|testFindOneByQuery
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
name|pumpDataIntoTestCollection
argument_list|()
expr_stmt|;
name|DBObject
name|query
init|=
name|BasicDBObjectBuilder
operator|.
name|start
argument_list|(
literal|"scientist"
argument_list|,
literal|"Einstein"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|DBObject
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:findOneByQuery"
argument_list|,
name|query
argument_list|,
name|DBObject
operator|.
name|class
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
name|assertNotNull
argument_list|(
literal|"DBObject in returned list should contain all fields"
argument_list|,
name|result
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
name|result
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
name|result
operator|.
name|get
argument_list|(
literal|"fixedField"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindOneById ()
specifier|public
name|void
name|testFindOneById
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
name|pumpDataIntoTestCollection
argument_list|()
expr_stmt|;
name|DBObject
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:findById"
argument_list|,
literal|"240"
argument_list|,
name|DBObject
operator|.
name|class
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
name|assertEquals
argument_list|(
literal|"The ID of the retrieved DBObject should equal 240"
argument_list|,
literal|"240"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|"_id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The scientist name of the retrieved DBObject should equal Einstein"
argument_list|,
literal|"Einstein"
argument_list|,
name|result
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
name|result
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
name|result
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
name|result
operator|.
name|get
argument_list|(
literal|"fixedField"
argument_list|)
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
literal|"direct:findAll"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=findAll&dynamicity=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultFindAll"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:findOneByQuery"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=findOneByQuery&dynamicity=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultFindOneByQuery"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:findById"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=findById&dynamicity=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultFindById"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

