begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
operator|.
name|helper
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
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|assertSame
import|;
end_import

begin_class
DECL|class|LoadingByteArrayOutputStreamTest
specifier|public
class|class
name|LoadingByteArrayOutputStreamTest
block|{
DECL|field|out
specifier|private
name|LoadingByteArrayOutputStream
name|out
decl_stmt|;
DECL|field|bytes
specifier|private
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|}
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|out
operator|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|defaultConstructor ()
specifier|public
name|void
name|defaultConstructor
parameter_list|()
block|{
name|out
operator|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
block|{
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
return|return
name|buf
return|;
block|}
block|}
expr_stmt|;
name|assertEquals
argument_list|(
literal|1024
argument_list|,
name|out
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toByteArrayShouldReturnTheSameArray ()
specifier|public
name|void
name|toByteArrayShouldReturnTheSameArray
parameter_list|()
block|{
name|byte
index|[]
name|byteArray1
init|=
name|out
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|byte
index|[]
name|byteArray2
init|=
name|out
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|byteArray1
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|byteArray1
argument_list|,
name|byteArray2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toByteArrayShouldReturnANewArray ()
specifier|public
name|void
name|toByteArrayShouldReturnANewArray
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|byteArray1
init|=
name|out
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|byteArray1
operator|=
name|out
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|byteArray1
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createInputStream ()
specifier|public
name|void
name|createInputStream
parameter_list|()
block|{
name|ByteArrayInputStream
name|in
init|=
name|out
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

