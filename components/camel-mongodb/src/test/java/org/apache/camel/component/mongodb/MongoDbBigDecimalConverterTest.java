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
name|math
operator|.
name|BigDecimal
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|MongoDbBigDecimalConverterTest
specifier|public
class|class
name|MongoDbBigDecimalConverterTest
extends|extends
name|AbstractMongoDbTest
block|{
DECL|class|NumberClass
specifier|private
class|class
name|NumberClass
block|{
comment|// CHECKSTYLE:OFF
DECL|field|_id
specifier|public
name|String
name|_id
init|=
literal|"testBigDecimalConvert"
decl_stmt|;
comment|// CHECKSTYLE:ON
DECL|field|aNumber
specifier|public
name|BigDecimal
name|aNumber
init|=
operator|new
name|BigDecimal
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|bNumber
specifier|public
name|BigDecimal
name|bNumber
init|=
operator|new
name|BigDecimal
argument_list|(
literal|12345L
argument_list|)
decl_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBigDecimalAutoConversion ()
specifier|public
name|void
name|testBigDecimalAutoConversion
parameter_list|()
block|{
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
name|NumberClass
name|testClass
init|=
operator|new
name|NumberClass
argument_list|()
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
name|testClass
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|Document
argument_list|)
expr_stmt|;
name|Document
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
name|testClass
operator|.
name|_id
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
name|assertTrue
argument_list|(
name|testClass
operator|.
name|aNumber
operator|.
name|equals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
operator|(
name|double
operator|)
name|b
operator|.
name|get
argument_list|(
literal|"aNumber"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testClass
operator|.
name|bNumber
argument_list|,
operator|new
name|BigDecimal
argument_list|(
operator|(
name|double
operator|)
name|b
operator|.
name|get
argument_list|(
literal|"bNumber"
argument_list|)
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

