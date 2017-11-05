begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

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
name|java
operator|.
name|io
operator|.
name|Reader
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
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
name|assertArrayEquals
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
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyInt
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|same
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
name|verify
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
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|NonXmlFilterReaderTest
specifier|public
class|class
name|NonXmlFilterReaderTest
block|{
DECL|field|nonXmlFilterReader
specifier|private
name|NonXmlFilterReader
name|nonXmlFilterReader
decl_stmt|;
annotation|@
name|Mock
DECL|field|nonXmlCharFiltererMock
specifier|private
name|NonXmlCharFilterer
name|nonXmlCharFiltererMock
decl_stmt|;
annotation|@
name|Mock
DECL|field|readerMock
specifier|private
name|Reader
name|readerMock
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|nonXmlFilterReader
operator|=
operator|new
name|NonXmlFilterReader
argument_list|(
name|readerMock
argument_list|)
expr_stmt|;
name|nonXmlFilterReader
operator|.
name|nonXmlCharFilterer
operator|=
name|nonXmlCharFiltererMock
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRead ()
specifier|public
name|void
name|testRead
parameter_list|()
throws|throws
name|IOException
block|{
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
literal|10
index|]
decl_stmt|;
name|when
argument_list|(
name|readerMock
operator|.
name|read
argument_list|(
name|same
argument_list|(
name|buffer
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|3
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
specifier|public
name|Integer
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
try|try
init|(
name|ConstantReader
name|reader
init|=
operator|new
name|ConstantReader
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|}
argument_list|)
init|)
block|{
name|Object
index|[]
name|args
init|=
name|invocation
operator|.
name|getArguments
argument_list|()
decl_stmt|;
return|return
name|reader
operator|.
name|read
argument_list|(
operator|(
name|char
index|[]
operator|)
name|args
index|[
literal|0
index|]
argument_list|,
operator|(
name|Integer
operator|)
name|args
index|[
literal|1
index|]
argument_list|,
operator|(
name|Integer
operator|)
name|args
index|[
literal|2
index|]
argument_list|)
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|int
name|result
init|=
name|nonXmlFilterReader
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|readerMock
argument_list|)
operator|.
name|read
argument_list|(
name|same
argument_list|(
name|buffer
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|3
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|nonXmlCharFiltererMock
argument_list|)
operator|.
name|filter
argument_list|(
name|same
argument_list|(
name|buffer
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|3
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of chars read"
argument_list|,
literal|3
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"Wrong buffer contents"
argument_list|,
operator|new
name|char
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadEOS ()
specifier|public
name|void
name|testReadEOS
parameter_list|()
throws|throws
name|IOException
block|{
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
literal|10
index|]
decl_stmt|;
name|when
argument_list|(
name|readerMock
operator|.
name|read
argument_list|(
name|any
argument_list|(
name|char
index|[]
operator|.
expr|class
argument_list|)
argument_list|,
name|anyInt
argument_list|()
argument_list|,
name|anyInt
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|int
name|result
init|=
name|nonXmlFilterReader
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of chars read"
argument_list|,
operator|-
literal|1
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"Buffer should not have been affected"
argument_list|,
operator|new
name|char
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
DECL|class|ConstantReader
specifier|static
class|class
name|ConstantReader
extends|extends
name|Reader
block|{
DECL|field|constant
specifier|private
name|char
index|[]
name|constant
decl_stmt|;
DECL|method|ConstantReader (char[] constant)
name|ConstantReader
parameter_list|(
name|char
index|[]
name|constant
parameter_list|)
block|{
name|this
operator|.
name|constant
operator|=
name|constant
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{         }
annotation|@
name|Override
DECL|method|read (char[] cbuf, int off, int len)
specifier|public
name|int
name|read
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|length
init|=
name|Math
operator|.
name|min
argument_list|(
name|len
argument_list|,
name|constant
operator|.
name|length
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|constant
argument_list|,
literal|0
argument_list|,
name|cbuf
argument_list|,
name|off
argument_list|,
name|length
argument_list|)
expr_stmt|;
return|return
name|length
return|;
block|}
block|}
block|}
end_class

end_unit

