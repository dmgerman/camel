begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|impl
package|;
end_package

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
name|mllp
operator|.
name|MllpWriteException
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
name|test
operator|.
name|util
operator|.
name|PayloadBuilder
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpEndpoint
operator|.
name|END_OF_BLOCK
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpEndpoint
operator|.
name|END_OF_DATA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpEndpoint
operator|.
name|START_OF_BLOCK
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
name|assertNotNull
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
name|assertNull
import|;
end_import

begin_class
DECL|class|MllpBufferedSocketMessageWriterTest
specifier|public
class|class
name|MllpBufferedSocketMessageWriterTest
extends|extends
name|MllpSocketWriterTestSupport
block|{
DECL|field|mllpSocketWriter
name|MllpSocketWriter
name|mllpSocketWriter
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
name|mllpSocketWriter
operator|=
operator|new
name|MllpBufferedSocketWriter
argument_list|(
name|fakeSocket
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteMessage ()
specifier|public
name|void
name|testWriteMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|expected
init|=
name|PayloadBuilder
operator|.
name|build
argument_list|(
name|START_OF_BLOCK
argument_list|,
name|TEST_MESSAGE
argument_list|,
name|END_OF_BLOCK
argument_list|,
name|END_OF_DATA
argument_list|)
decl_stmt|;
name|mllpSocketWriter
operator|.
name|writeEnvelopedPayload
argument_list|(
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|fakeSocket
operator|.
name|payload
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteNullMessage ()
specifier|public
name|void
name|testWriteNullMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|message
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|expected
init|=
name|PayloadBuilder
operator|.
name|build
argument_list|(
name|START_OF_BLOCK
argument_list|,
name|END_OF_BLOCK
argument_list|,
name|END_OF_DATA
argument_list|)
decl_stmt|;
name|mllpSocketWriter
operator|.
name|writeEnvelopedPayload
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|fakeSocket
operator|.
name|payload
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteEmptyMessage ()
specifier|public
name|void
name|testWriteEmptyMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|message
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
name|byte
index|[]
name|expected
init|=
name|PayloadBuilder
operator|.
name|build
argument_list|(
name|START_OF_BLOCK
argument_list|,
name|END_OF_BLOCK
argument_list|,
name|END_OF_DATA
argument_list|)
decl_stmt|;
name|mllpSocketWriter
operator|.
name|writeEnvelopedPayload
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|fakeSocket
operator|.
name|payload
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MllpWriteException
operator|.
name|class
argument_list|)
DECL|method|testGetOutputStreamFailure ()
specifier|public
name|void
name|testGetOutputStreamFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|fakeSocket
operator|.
name|fakeSocketOutputStream
operator|=
literal|null
expr_stmt|;
try|try
block|{
name|mllpSocketWriter
operator|.
name|writeEnvelopedPayload
argument_list|(
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MllpWriteException
name|expectedEx
parameter_list|)
block|{
name|verifyException
argument_list|(
name|expectedEx
argument_list|)
expr_stmt|;
throw|throw
name|expectedEx
throw|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MllpWriteException
operator|.
name|class
argument_list|)
DECL|method|testWriteToUnconnectedSocket ()
specifier|public
name|void
name|testWriteToUnconnectedSocket
parameter_list|()
throws|throws
name|Exception
block|{
name|fakeSocket
operator|.
name|connected
operator|=
literal|false
expr_stmt|;
try|try
block|{
name|mllpSocketWriter
operator|.
name|writeEnvelopedPayload
argument_list|(
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MllpWriteException
name|expectedEx
parameter_list|)
block|{
name|verifyException
argument_list|(
name|expectedEx
argument_list|)
expr_stmt|;
throw|throw
name|expectedEx
throw|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MllpWriteException
operator|.
name|class
argument_list|)
DECL|method|testWriteToClosedSocket ()
specifier|public
name|void
name|testWriteToClosedSocket
parameter_list|()
throws|throws
name|Exception
block|{
name|fakeSocket
operator|.
name|closed
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|mllpSocketWriter
operator|.
name|writeEnvelopedPayload
argument_list|(
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MllpWriteException
name|expectedEx
parameter_list|)
block|{
name|verifyException
argument_list|(
name|expectedEx
argument_list|)
expr_stmt|;
throw|throw
name|expectedEx
throw|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MllpWriteException
operator|.
name|class
argument_list|)
DECL|method|testWriteFailure ()
specifier|public
name|void
name|testWriteFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|fakeSocket
operator|.
name|fakeSocketOutputStream
operator|.
name|failOnWriteArray
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|mllpSocketWriter
operator|.
name|writeEnvelopedPayload
argument_list|(
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MllpWriteException
name|expectedEx
parameter_list|)
block|{
name|verifyException
argument_list|(
name|expectedEx
argument_list|)
expr_stmt|;
throw|throw
name|expectedEx
throw|;
block|}
block|}
DECL|method|verifyException (MllpWriteException expectedEx)
specifier|private
name|void
name|verifyException
parameter_list|(
name|MllpWriteException
name|expectedEx
parameter_list|)
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|expectedEx
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|,
name|expectedEx
operator|.
name|getHl7Message
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|expectedEx
operator|.
name|getHl7Acknowledgement
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|,
name|expectedEx
operator|.
name|getMllpPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

