begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|okhttp3
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|OkHttpClient
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|RequestBody
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|Response
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
name|Processor
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

begin_class
DECL|class|MultiPartFormOkHttpTest
specifier|public
class|class
name|MultiPartFormOkHttpTest
extends|extends
name|BaseJettyTest
block|{
DECL|method|createMultipartRequest ()
specifier|private
name|Request
name|createMultipartRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|MediaType
name|mediaType
init|=
name|MediaType
operator|.
name|parse
argument_list|(
literal|"multipart/form-data; boundary=---011000010111000001101001"
argument_list|)
decl_stmt|;
name|RequestBody
name|body
init|=
name|RequestBody
operator|.
name|create
argument_list|(
name|mediaType
argument_list|,
literal|"-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"test\"\r\n\r\nsome data here\r\n-----011000010111000001101001--"
argument_list|)
decl_stmt|;
name|Request
name|request
init|=
operator|new
name|Request
operator|.
name|Builder
argument_list|()
operator|.
name|url
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/test"
argument_list|)
operator|.
name|post
argument_list|(
name|body
argument_list|)
operator|.
name|addHeader
argument_list|(
literal|"content-type"
argument_list|,
literal|"multipart/form-data; boundary=---011000010111000001101001"
argument_list|)
operator|.
name|addHeader
argument_list|(
literal|"cache-control"
argument_list|,
literal|"no-cache"
argument_list|)
operator|.
name|addHeader
argument_list|(
literal|"postman-token"
argument_list|,
literal|"a9fd95b6-04b9-ea7a-687e-ff828ea00774"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|request
return|;
block|}
annotation|@
name|Test
DECL|method|testSendMultiPartFormFromOkHttpClient ()
specifier|public
name|void
name|testSendMultiPartFormFromOkHttpClient
parameter_list|()
throws|throws
name|Exception
block|{
name|OkHttpClient
name|client
init|=
operator|new
name|OkHttpClient
argument_list|()
decl_stmt|;
name|Request
name|request
init|=
name|createMultipartRequest
argument_list|()
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|newCall
argument_list|(
name|request
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|code
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Thanks"
argument_list|,
name|response
operator|.
name|body
argument_list|()
operator|.
name|string
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/test"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Should have attachment"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|hasAttachments
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachment
argument_list|(
literal|"test"
argument_list|)
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"form-data; name=\"test\""
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachmentObject
argument_list|(
literal|"test"
argument_list|)
operator|.
name|getHeader
argument_list|(
literal|"content-disposition"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have data"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"some data here"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Thanks"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

