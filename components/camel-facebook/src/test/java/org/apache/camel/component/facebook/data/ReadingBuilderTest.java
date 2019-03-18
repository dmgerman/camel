begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook.data
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
operator|.
name|data
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|Locale
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
name|facebook4j
operator|.
name|Reading
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
name|facebook
operator|.
name|FacebookConstants
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
name|junit
operator|.
name|Assert
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
name|Assert
operator|.
name|assertFalse
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
name|assertNotEquals
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
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * Test {@link ReadingBuilder}.   */
end_comment

begin_class
DECL|class|ReadingBuilderTest
specifier|public
class|class
name|ReadingBuilderTest
block|{
annotation|@
name|Test
DECL|method|testCopy ()
specifier|public
name|void
name|testCopy
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Reading
name|source
init|=
operator|new
name|Reading
argument_list|()
decl_stmt|;
name|source
operator|.
name|fields
argument_list|(
literal|"field1"
argument_list|,
literal|"field2"
argument_list|)
expr_stmt|;
name|source
operator|.
name|filter
argument_list|(
literal|"testFilter"
argument_list|)
expr_stmt|;
name|source
operator|.
name|limit
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|source
operator|.
name|locale
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
name|source
operator|.
name|metadata
argument_list|()
expr_stmt|;
name|source
operator|.
name|offset
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|source
operator|.
name|since
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|source
operator|.
name|until
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|source
operator|.
name|withLocation
argument_list|()
expr_stmt|;
name|Reading
name|copy
init|=
name|ReadingBuilder
operator|.
name|copy
argument_list|(
name|source
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null copy"
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Copy not equal"
argument_list|,
name|source
operator|.
name|toString
argument_list|()
argument_list|,
name|copy
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// skip since and until
name|copy
operator|=
name|ReadingBuilder
operator|.
name|copy
argument_list|(
name|source
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|"Copy equal"
argument_list|,
name|source
operator|.
name|toString
argument_list|()
argument_list|,
name|copy
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"since"
argument_list|,
name|copy
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"since="
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"until"
argument_list|,
name|copy
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"until="
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetProperties ()
specifier|public
name|void
name|testSetProperties
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Reading
name|reading
init|=
operator|new
name|Reading
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"fields"
argument_list|,
literal|"field1,field2"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"filter"
argument_list|,
literal|"testFilter"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"limit"
argument_list|,
literal|"100"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"metadata"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"offset"
argument_list|,
literal|"1000"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|facebookDate
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|FacebookConstants
operator|.
name|FACEBOOK_DATE_FORMAT
argument_list|)
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"since"
argument_list|,
name|facebookDate
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"until"
argument_list|,
literal|"arbitrary date, to be validated by Facebook call"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"withLocation"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|// set properties on Reading
name|ReadingBuilder
operator|.
name|setProperties
argument_list|(
name|reading
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

