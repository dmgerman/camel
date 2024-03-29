begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|builder
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

begin_class
DECL|class|RecordableInputStreamTest
specifier|public
class|class
name|RecordableInputStreamTest
extends|extends
name|Assert
block|{
DECL|field|DATA
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|DATA
decl_stmt|;
static|static
block|{
name|DATA
operator|=
operator|new
name|byte
index|[
literal|512
index|]
expr_stmt|;
specifier|final
name|int
name|radix
init|=
literal|0x7f
operator|-
literal|0x20
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
literal|512
condition|;
name|i
operator|++
control|)
block|{
name|DATA
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|i
operator|%
name|radix
operator|+
literal|0x20
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testReadAndGetTextsBufferPurge ()
specifier|public
name|void
name|testReadAndGetTextsBufferPurge
parameter_list|()
throws|throws
name|Exception
block|{
name|RecordableInputStream
name|ris
init|=
operator|new
name|RecordableInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|DATA
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|64
index|]
decl_stmt|;
comment|// 8 * 64 = 512
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
comment|// read in 64 bytes
name|int
name|n
init|=
name|ris
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|64
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|64
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|offset
init|=
name|i
operator|*
literal|64
decl_stmt|;
comment|// consume the first 32 bytes
name|String
name|text
init|=
name|ris
operator|.
name|getText
argument_list|(
literal|32
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|String
argument_list|(
name|DATA
argument_list|,
name|offset
argument_list|,
literal|32
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|32
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// consume the other 32 bytes
name|text
operator|=
name|ris
operator|.
name|getText
argument_list|(
literal|32
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|String
argument_list|(
name|DATA
argument_list|,
name|offset
operator|+
literal|32
argument_list|,
literal|32
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ris
operator|.
name|record
argument_list|()
expr_stmt|;
block|}
name|ris
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadAndGetTextsAutoStopRecord ()
specifier|public
name|void
name|testReadAndGetTextsAutoStopRecord
parameter_list|()
throws|throws
name|Exception
block|{
name|RecordableInputStream
name|ris
init|=
operator|new
name|RecordableInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|DATA
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|64
index|]
decl_stmt|;
comment|// read 64 bytes
name|int
name|n
init|=
name|ris
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|64
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|64
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// consume the 64 bytes
name|String
name|text
init|=
name|ris
operator|.
name|getText
argument_list|(
literal|64
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|String
argument_list|(
name|DATA
argument_list|,
literal|0
argument_list|,
literal|64
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// read the next 64 bytes
name|n
operator|=
name|ris
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|64
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// turn back on the recording and read the next 64 bytes
name|ris
operator|.
name|record
argument_list|()
expr_stmt|;
name|n
operator|=
name|ris
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|64
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|64
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// consume the 64 bytes
name|text
operator|=
name|ris
operator|.
name|getText
argument_list|(
literal|64
argument_list|)
expr_stmt|;
comment|// 64 * 2 = 128
name|assertEquals
argument_list|(
operator|new
name|String
argument_list|(
name|DATA
argument_list|,
literal|128
argument_list|,
literal|64
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ris
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

