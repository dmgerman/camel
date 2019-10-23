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
name|File
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
name|attachment
operator|.
name|AttachmentMessage
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
name|util
operator|.
name|IOHelper
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
name|MultipartEntityBuilder
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
name|HttpClientBuilder
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
name|BaseJettyTest
block|{
DECL|method|createMultipartRequestEntity ()
specifier|private
name|HttpEntity
name|createMultipartRequestEntity
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
literal|"src/test/resources/log4j2.properties"
argument_list|)
decl_stmt|;
return|return
name|MultipartEntityBuilder
operator|.
name|create
argument_list|()
operator|.
name|addTextBody
argument_list|(
literal|"comment"
argument_list|,
literal|"A binary file of some kind"
argument_list|)
operator|.
name|addBinaryBody
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|file
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
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
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/test"
argument_list|)
decl_stmt|;
name|post
operator|.
name|setEntity
argument_list|(
name|createMultipartRequestEntity
argument_list|()
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|post
argument_list|)
decl_stmt|;
name|int
name|status
init|=
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response status"
argument_list|,
literal|200
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|IOHelper
operator|.
name|loadText
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
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
annotation|@
name|Test
DECL|method|testSendMultiPartFormFromCamelHttpComponnent ()
specifier|public
name|void
name|testSendMultiPartFormFromCamelHttpComponnent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/test"
argument_list|,
name|createMultipartRequestEntity
argument_list|()
argument_list|,
name|String
operator|.
name|class
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// Set the jetty temp directory which store the file for multi
comment|// part form
comment|// camel-jetty will clean up the file after it handled the
comment|// request.
comment|// The option works rightly from Camel 2.4.0
name|getContext
argument_list|()
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
literal|"CamelJettyTempDir"
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
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
name|AttachmentMessage
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong attachement size"
argument_list|,
literal|2
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
literal|"log4j2.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should get the DataHandle log4j2.properties"
argument_list|,
name|data
argument_list|)
expr_stmt|;
comment|// This assert is wrong, but the correct content-type
comment|// (application/octet-stream)
comment|// will not be returned until Jetty makes it available -
comment|// currently the content-type
comment|// returned is just the default for FileDataHandler (for
comment|// the implentation being used)
comment|// assertEquals("Get a wrong content type",
comment|// "text/plain", data.getContentType());
name|assertEquals
argument_list|(
literal|"Got the wrong name"
argument_list|,
literal|"log4j2.properties"
argument_list|,
name|data
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the data from the DataHandle"
argument_list|,
name|data
operator|.
name|getDataSource
argument_list|()
operator|.
name|getInputStream
argument_list|()
operator|.
name|available
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// The other form date can be get from the message
comment|// header
comment|// For binary attachment, header should also be
comment|// populated by DataHandler but not payload
name|Object
name|header
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"log4j2.properties"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|DataHandler
operator|.
name|class
argument_list|,
name|header
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
argument_list|,
name|header
argument_list|)
expr_stmt|;
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

