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
DECL|class|MongoDbExceptionHandlingTest
specifier|public
class|class
name|MongoDbExceptionHandlingTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|testInduceParseException ()
specifier|public
name|void
name|testInduceParseException
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
comment|// notice missing quote at the end of Einstein
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:findOneByQuery"
argument_list|,
literal|"{\"scientist\": \"Einstein}"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|extractAndAssertCamelMongoDbException
argument_list|(
name|e
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testErroneousDynamicOperation ()
specifier|public
name|void
name|testErroneousDynamicOperation
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
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:findOneByQuery"
argument_list|,
literal|"{\"scientist\": \"Einstein\"}"
argument_list|,
name|MongoDbConstants
operator|.
name|OPERATION_HEADER
argument_list|,
literal|"dummyOp"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|extractAndAssertCamelMongoDbException
argument_list|(
name|e
argument_list|,
literal|"Operation specified on header is not supported. Value: dummyOp"
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

