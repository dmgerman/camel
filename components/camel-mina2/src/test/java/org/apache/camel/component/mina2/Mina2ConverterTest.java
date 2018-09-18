begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|buffer
operator|.
name|IoBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|Mina2ConverterTest
specifier|public
class|class
name|Mina2ConverterTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testToByteArray ()
specifier|public
name|void
name|testToByteArray
parameter_list|()
block|{
name|byte
index|[]
name|in
init|=
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|IoBuffer
name|bb
init|=
name|IoBuffer
operator|.
name|wrap
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|byte
index|[]
name|out
init|=
name|Mina2Converter
operator|.
name|toByteArray
argument_list|(
name|bb
argument_list|)
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
name|out
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|in
index|[
name|i
index|]
argument_list|,
name|out
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
throws|throws
name|UnsupportedEncodingException
block|{
name|String
name|in
init|=
literal|"Hello World \u4f60\u597d"
decl_stmt|;
name|IoBuffer
name|bb
init|=
name|IoBuffer
operator|.
name|wrap
argument_list|(
name|in
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|Mina2Converter
operator|.
name|toString
argument_list|(
name|bb
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World \u4f60\u597d"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToStringTwoTimes ()
specifier|public
name|void
name|testToStringTwoTimes
parameter_list|()
throws|throws
name|UnsupportedEncodingException
block|{
name|String
name|in
init|=
literal|"Hello World \u4f60\u597d"
decl_stmt|;
name|IoBuffer
name|bb
init|=
name|IoBuffer
operator|.
name|wrap
argument_list|(
name|in
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|Mina2Converter
operator|.
name|toString
argument_list|(
name|bb
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World \u4f60\u597d"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// should NOT be possible to convert to string without affecting the ByteBuffer
name|out
operator|=
name|Mina2Converter
operator|.
name|toString
argument_list|(
name|bb
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInputStream ()
specifier|public
name|void
name|testToInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|in
init|=
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|IoBuffer
name|bb
init|=
name|IoBuffer
operator|.
name|wrap
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|Mina2Converter
operator|.
name|toInputStream
argument_list|(
name|bb
argument_list|)
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|in
control|)
block|{
name|int
name|out
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|b
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testToByteBuffer ()
specifier|public
name|void
name|testToByteBuffer
parameter_list|()
block|{
name|byte
index|[]
name|in
init|=
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|IoBuffer
name|bb
init|=
name|Mina2Converter
operator|.
name|toIoBuffer
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
comment|// convert back to byte[] and see if the bytes are equal
name|bb
operator|.
name|flip
argument_list|()
expr_stmt|;
comment|// must flip to change direction to read
name|byte
index|[]
name|out
init|=
name|Mina2Converter
operator|.
name|toByteArray
argument_list|(
name|bb
argument_list|)
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
name|out
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|in
index|[
name|i
index|]
argument_list|,
name|out
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

