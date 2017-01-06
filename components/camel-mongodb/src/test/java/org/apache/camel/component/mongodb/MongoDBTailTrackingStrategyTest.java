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
name|org
operator|.
name|bson
operator|.
name|types
operator|.
name|BSONTimestamp
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
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsNull
operator|.
name|notNullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|MongoDBTailTrackingStrategyTest
specifier|public
class|class
name|MongoDBTailTrackingStrategyTest
block|{
DECL|field|INCREASING_FIELD_NAME
specifier|private
specifier|static
specifier|final
name|String
name|INCREASING_FIELD_NAME
init|=
literal|"ts"
decl_stmt|;
annotation|@
name|Test
DECL|method|testExtractLastValForLiterals ()
specifier|public
name|void
name|testExtractLastValForLiterals
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|expected
init|=
literal|1483701465
decl_stmt|;
name|DBObject
name|o
init|=
name|mock
argument_list|(
name|DBObject
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|o
operator|.
name|get
argument_list|(
name|INCREASING_FIELD_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|Object
name|lastVal
init|=
name|MongoDBTailTrackingEnum
operator|.
name|LITERAL
operator|.
name|extractLastVal
argument_list|(
name|o
argument_list|,
name|INCREASING_FIELD_NAME
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|lastVal
argument_list|,
name|is
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateQueryForLiterals ()
specifier|public
name|void
name|testCreateQueryForLiterals
parameter_list|()
block|{
name|Integer
name|lastVal
init|=
literal|1483701465
decl_stmt|;
name|BasicDBObject
name|basicDBObject
init|=
name|MongoDBTailTrackingEnum
operator|.
name|LITERAL
operator|.
name|createQuery
argument_list|(
name|lastVal
argument_list|,
name|INCREASING_FIELD_NAME
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|actual
init|=
name|basicDBObject
operator|.
name|get
argument_list|(
name|INCREASING_FIELD_NAME
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|actual
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|instanceof
name|BasicDBObject
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
operator|(
name|BasicDBObject
operator|)
name|actual
operator|)
operator|.
name|get
argument_list|(
literal|"$gt"
argument_list|)
argument_list|,
name|is
argument_list|(
name|lastVal
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractLastValForTimestamp ()
specifier|public
name|void
name|testExtractLastValForTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|DBObject
name|o
init|=
name|mock
argument_list|(
name|DBObject
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|int
name|lastVal
init|=
literal|1483701465
decl_stmt|;
name|when
argument_list|(
name|o
operator|.
name|get
argument_list|(
name|INCREASING_FIELD_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|BSONTimestamp
argument_list|(
name|lastVal
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|res
init|=
name|MongoDBTailTrackingEnum
operator|.
name|TIMESTAMP
operator|.
name|extractLastVal
argument_list|(
name|o
argument_list|,
name|INCREASING_FIELD_NAME
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|res
argument_list|,
name|is
argument_list|(
name|lastVal
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtracCreateQueryForTimestamp ()
specifier|public
name|void
name|testExtracCreateQueryForTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|lastVal
init|=
literal|1483701465
decl_stmt|;
name|BasicDBObject
name|basicDBObject
init|=
name|MongoDBTailTrackingEnum
operator|.
name|TIMESTAMP
operator|.
name|createQuery
argument_list|(
name|lastVal
argument_list|,
name|INCREASING_FIELD_NAME
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|actual
init|=
name|basicDBObject
operator|.
name|get
argument_list|(
name|INCREASING_FIELD_NAME
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|actual
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|instanceof
name|BasicDBObject
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
operator|(
name|BasicDBObject
operator|)
name|actual
operator|)
operator|.
name|get
argument_list|(
literal|"$gt"
argument_list|)
operator|instanceof
name|BSONTimestamp
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|BSONTimestamp
name|bsonTimestamp
init|=
call|(
name|BSONTimestamp
call|)
argument_list|(
operator|(
name|BasicDBObject
operator|)
name|actual
argument_list|)
operator|.
name|get
argument_list|(
literal|"$gt"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|bsonTimestamp
operator|.
name|getTime
argument_list|()
argument_list|,
name|is
argument_list|(
name|lastVal
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

