begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
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
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * Test case for {@link IOConverter}  */
end_comment

begin_class
DECL|class|IOConverterTest
specifier|public
class|class
name|IOConverterTest
extends|extends
name|TestCase
block|{
DECL|field|TESTDATA
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TESTDATA
init|=
literal|"My test data"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
DECL|method|testToBytes ()
specifier|public
name|void
name|testToBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/converter/dummy.txt"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"get the wrong byte size"
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'#'
argument_list|,
operator|(
name|char
operator|)
name|data
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'!'
argument_list|,
operator|(
name|char
operator|)
name|data
index|[
name|data
operator|.
name|length
operator|-
literal|2
index|]
argument_list|)
expr_stmt|;
comment|// should end with a new line
name|assertEquals
argument_list|(
literal|'\n'
argument_list|,
operator|(
name|char
operator|)
name|data
index|[
name|data
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopy ()
specifier|public
name|void
name|testCopy
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|TESTDATA
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|bis
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TESTDATA
argument_list|,
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEquals (byte[] data1, byte[] data2)
specifier|private
name|void
name|assertEquals
parameter_list|(
name|byte
index|[]
name|data1
parameter_list|,
name|byte
index|[]
name|data2
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|data1
operator|.
name|length
argument_list|,
name|data2
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|data1
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|data1
index|[
name|i
index|]
argument_list|,
name|data2
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

