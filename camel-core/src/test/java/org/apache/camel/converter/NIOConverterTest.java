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
name|File
import|;
end_import

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
name|nio
operator|.
name|ByteBuffer
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
name|ContextTestSupport
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|NIOConverterTest
specifier|public
class|class
name|NIOConverterTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testToByteArray ()
specifier|public
name|void
name|testToByteArray
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|out
init|=
name|NIOConverter
operator|.
name|toByteArray
argument_list|(
name|bb
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|NIOConverter
operator|.
name|toString
argument_list|(
name|bb
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteBuffer ()
specifier|public
name|void
name|testToByteBuffer
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|NIOConverter
operator|.
name|toByteBuffer
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteBufferString ()
specifier|public
name|void
name|testToByteBufferString
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|NIOConverter
operator|.
name|toByteBuffer
argument_list|(
literal|"Hello"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteBufferFile ()
specifier|public
name|void
name|testToByteBufferFile
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/nio"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|ByteBuffer
name|bb
init|=
name|NIOConverter
operator|.
name|toByteBuffer
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/nio/hello.txt"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|NIOConverter
operator|.
name|toString
argument_list|(
name|bb
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteBufferShort ()
specifier|public
name|void
name|testToByteBufferShort
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|NIOConverter
operator|.
name|toByteBuffer
argument_list|(
name|Short
operator|.
name|valueOf
argument_list|(
literal|"2"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|bb
operator|.
name|position
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bb
operator|.
name|getShort
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteBufferInteger ()
specifier|public
name|void
name|testToByteBufferInteger
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|NIOConverter
operator|.
name|toByteBuffer
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"2"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|bb
operator|.
name|position
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bb
operator|.
name|getInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteBufferLong ()
specifier|public
name|void
name|testToByteBufferLong
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|NIOConverter
operator|.
name|toByteBuffer
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|"2"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|bb
operator|.
name|position
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bb
operator|.
name|getLong
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteBufferDouble ()
specifier|public
name|void
name|testToByteBufferDouble
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|NIOConverter
operator|.
name|toByteBuffer
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|"2"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|bb
operator|.
name|position
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2.0d
argument_list|,
name|bb
operator|.
name|getDouble
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteBufferFloat ()
specifier|public
name|void
name|testToByteBufferFloat
parameter_list|()
block|{
name|ByteBuffer
name|bb
init|=
name|NIOConverter
operator|.
name|toByteBuffer
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
literal|"2"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|bb
operator|.
name|position
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2.0f
argument_list|,
name|bb
operator|.
name|getFloat
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToInputStream ()
specifier|public
name|void
name|testToInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteBuffer
name|bb
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|NIOConverter
operator|.
name|toInputStream
argument_list|(
name|bb
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|is
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

