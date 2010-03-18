begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|Message
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|mime
operator|.
name|MultipartEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|mime
operator|.
name|content
operator|.
name|FileBody
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|mime
operator|.
name|content
operator|.
name|StringBody
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|DefaultHttpClient
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
DECL|class|MultiPartFormTest
specifier|public
class|class
name|MultiPartFormTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendMultiPartForm ()
specifier|public
name|void
name|testSendMultiPartForm
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpPost
name|httppost
init|=
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:9080/test"
argument_list|)
decl_stmt|;
name|FileBody
name|bin
init|=
operator|new
name|FileBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/main/resources/META-INF/NOTICE.txt"
argument_list|)
argument_list|)
decl_stmt|;
name|StringBody
name|comment
init|=
operator|new
name|StringBody
argument_list|(
literal|"A binary file of some kind"
argument_list|)
decl_stmt|;
name|MultipartEntity
name|reqEntity
init|=
operator|new
name|MultipartEntity
argument_list|()
decl_stmt|;
name|reqEntity
operator|.
name|addPart
argument_list|(
literal|"bin"
argument_list|,
name|bin
argument_list|)
expr_stmt|;
name|reqEntity
operator|.
name|addPart
argument_list|(
literal|"comment"
argument_list|,
name|comment
argument_list|)
expr_stmt|;
name|httppost
operator|.
name|setEntity
argument_list|(
name|reqEntity
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httppost
argument_list|)
decl_stmt|;
name|HttpEntity
name|resEntity
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response status"
argument_list|,
literal|"HTTP/1.1 200 OK"
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"resEntity should not be null"
argument_list|,
name|resEntity
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|context
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
name|resEntity
operator|.
name|getContent
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong result"
argument_list|,
literal|"A binary file of some kind"
argument_list|,
name|result
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"jetty://http://localhost:9080/test"
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong attachement size"
argument_list|,
literal|1
argument_list|,
name|in
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// The file name is attachment id
name|DataHandler
name|data
init|=
name|in
operator|.
name|getAttachment
argument_list|(
literal|"NOTICE.txt"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should get the DataHandle NOTICE.txt"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong content type"
argument_list|,
literal|"text/plain"
argument_list|,
name|data
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
comment|// The other form date can be get from the message header
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
literal|"comment"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

