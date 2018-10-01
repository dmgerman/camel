begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|builder
operator|.
name|RouteBuilder
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
name|Exchange
operator|.
name|CONTENT_TYPE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|MediaType
operator|.
name|AUDIO_MPEG
import|;
end_import

begin_class
DECL|class|RestletProducerBinaryStreamTest
specifier|public
class|class
name|RestletProducerBinaryStreamTest
extends|extends
name|RestletTestSupport
block|{
annotation|@
name|Test
DECL|method|shouldHandleBinaryOctetStream ()
specifier|public
name|void
name|shouldHandleBinaryOctetStream
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|response
init|=
name|template
operator|.
name|request
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/application/octet-stream?streamRepresentation=true"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"application/octet-stream"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldHandleBinaryAudioMpeg ()
specifier|public
name|void
name|shouldHandleBinaryAudioMpeg
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|response
init|=
name|template
operator|.
name|request
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/audio/mpeg?streamRepresentation=true"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"audio/mpeg"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldAutoClose ()
specifier|public
name|void
name|shouldAutoClose
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|response
init|=
name|template
operator|.
name|request
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/application/octet-stream?streamRepresentation=true&autoCloseStream=true"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"application/octet-stream"
argument_list|)
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
operator|(
name|InputStream
operator|)
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
try|try
block|{
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should be closed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/application/octet-stream"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|constant
argument_list|(
name|APPLICATION_OCTET_STREAM
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/audio/mpeg"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|constant
argument_list|(
name|AUDIO_MPEG
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getAllBytes ()
specifier|private
specifier|static
name|byte
index|[]
name|getAllBytes
parameter_list|()
block|{
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
literal|256
index|]
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
literal|256
condition|;
name|i
operator|++
control|)
block|{
name|data
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|Byte
operator|.
name|MIN_VALUE
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
block|}
end_class

end_unit

