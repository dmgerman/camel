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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
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
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|PostMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|multipart
operator|.
name|FilePart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|multipart
operator|.
name|MultipartRequestEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|multipart
operator|.
name|Part
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|multipart
operator|.
name|StringPart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|servlets
operator|.
name|MultiPartFilter
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
DECL|class|MultiPartFormWithCustomFilterTest
specifier|public
class|class
name|MultiPartFormWithCustomFilterTest
extends|extends
name|BaseJettyTest
block|{
DECL|class|MyMultipartFilter
specifier|private
specifier|static
class|class
name|MyMultipartFilter
extends|extends
name|MultiPartFilter
block|{
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
comment|// set a marker attribute to show that this filter class was used
operator|(
operator|(
name|HttpServletResponse
operator|)
name|response
operator|)
operator|.
name|addHeader
argument_list|(
literal|"MyMultipartFilter"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|super
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|chain
argument_list|)
expr_stmt|;
block|}
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
name|HttpClient
name|httpclient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/log4j2.properties"
argument_list|)
decl_stmt|;
name|PostMethod
name|httppost
init|=
operator|new
name|PostMethod
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/test"
argument_list|)
decl_stmt|;
name|Part
index|[]
name|parts
init|=
block|{
operator|new
name|StringPart
argument_list|(
literal|"comment"
argument_list|,
literal|"A binary file of some kind"
argument_list|)
block|,
operator|new
name|FilePart
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|file
argument_list|)
block|}
decl_stmt|;
name|MultipartRequestEntity
name|reqEntity
init|=
operator|new
name|MultipartRequestEntity
argument_list|(
name|parts
argument_list|,
name|httppost
operator|.
name|getParams
argument_list|()
argument_list|)
decl_stmt|;
name|httppost
operator|.
name|setRequestEntity
argument_list|(
name|reqEntity
argument_list|)
expr_stmt|;
name|int
name|status
init|=
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|httppost
argument_list|)
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
name|httppost
operator|.
name|getResponseBodyAsString
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
name|assertNotNull
argument_list|(
literal|"Did not use custom multipart filter"
argument_list|,
name|httppost
operator|.
name|getResponseHeader
argument_list|(
literal|"MyMultipartFilter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMultiPartFormOverrideEnableMultpartFilterFalse ()
specifier|public
name|void
name|testSendMultiPartFormOverrideEnableMultpartFilterFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClient
name|httpclient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/log4j2.properties"
argument_list|)
decl_stmt|;
name|PostMethod
name|httppost
init|=
operator|new
name|PostMethod
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/test2"
argument_list|)
decl_stmt|;
name|Part
index|[]
name|parts
init|=
block|{
operator|new
name|StringPart
argument_list|(
literal|"comment"
argument_list|,
literal|"A binary file of some kind"
argument_list|)
block|,
operator|new
name|FilePart
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|file
argument_list|)
block|}
decl_stmt|;
name|MultipartRequestEntity
name|reqEntity
init|=
operator|new
name|MultipartRequestEntity
argument_list|(
name|parts
argument_list|,
name|httppost
operator|.
name|getParams
argument_list|()
argument_list|)
decl_stmt|;
name|httppost
operator|.
name|setRequestEntity
argument_list|(
name|reqEntity
argument_list|)
expr_stmt|;
name|int
name|status
init|=
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|httppost
argument_list|)
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
name|assertNotNull
argument_list|(
literal|"Did not use custom multipart filter"
argument_list|,
name|httppost
operator|.
name|getResponseHeader
argument_list|(
literal|"MyMultipartFilter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myMultipartFilter"
argument_list|,
operator|new
name|MyMultipartFilter
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
comment|// Set the jetty temp directory which store the file for multi part form
comment|// camel-jetty will clean up the file after it handled the request.
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
literal|"jetty://http://localhost:{{port}}/test?multipartFilterRef=myMultipartFilter"
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
comment|// This assert is wrong, but the correct content-type (application/octet-stream)
comment|// will not be returned until Jetty makes it available - currently the content-type
comment|// returned is just the default for FileDataHandler (for the implentation being used)
comment|//assertEquals("Get a wrong content type", "text/plain", data.getContentType());
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
comment|// Test to ensure that setting a multipartFilterRef overrides the enableMultipartFilter=false parameter
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/test2?multipartFilterRef=myMultipartFilter&enableMultipartFilter=false"
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

