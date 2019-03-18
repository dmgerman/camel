begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gson
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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

begin_class
DECL|class|GsonDataFormatTest
specifier|public
class|class
name|GsonDataFormatTest
block|{
annotation|@
name|Test
DECL|method|testString ()
specifier|public
name|void
name|testString
parameter_list|()
throws|throws
name|Exception
block|{
name|testJson
argument_list|(
literal|"\"A string\""
argument_list|,
literal|"A string"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMap ()
specifier|public
name|void
name|testMap
parameter_list|()
throws|throws
name|Exception
block|{
name|testJson
argument_list|(
literal|"{value=123}"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"value"
argument_list|,
literal|123.0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testList ()
specifier|public
name|void
name|testList
parameter_list|()
throws|throws
name|Exception
block|{
name|testJson
argument_list|(
literal|"[{value=123}]"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"value"
argument_list|,
literal|123.0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testJson (String json, Object expected)
specifier|private
name|void
name|testJson
parameter_list|(
name|String
name|json
parameter_list|,
name|Object
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|unmarshalled
decl_stmt|;
name|GsonDataFormat
name|gsonDataFormat
init|=
operator|new
name|GsonDataFormat
argument_list|()
decl_stmt|;
name|gsonDataFormat
operator|.
name|doStart
argument_list|()
expr_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|json
operator|.
name|getBytes
argument_list|()
argument_list|)
init|)
block|{
name|unmarshalled
operator|=
name|gsonDataFormat
operator|.
name|unmarshal
argument_list|(
literal|null
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|unmarshalled
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

