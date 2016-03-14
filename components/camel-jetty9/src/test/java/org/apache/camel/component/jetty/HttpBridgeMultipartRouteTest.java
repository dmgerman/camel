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
name|File
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
name|component
operator|.
name|http
operator|.
name|HttpEndpoint
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
name|DefaultHeaderFilterStrategy
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|HttpBridgeMultipartRouteTest
specifier|public
class|class
name|HttpBridgeMultipartRouteTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|port1
specifier|private
name|int
name|port1
decl_stmt|;
DECL|field|port2
specifier|private
name|int
name|port2
decl_stmt|;
DECL|class|MultipartHeaderFilterStrategy
specifier|private
specifier|static
class|class
name|MultipartHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
block|{
DECL|method|MultipartHeaderFilterStrategy ()
name|MultipartHeaderFilterStrategy
parameter_list|()
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
DECL|method|initialize ()
specifier|protected
name|void
name|initialize
parameter_list|()
block|{
name|setLowerCase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setOutFilterPattern
argument_list|(
literal|"(?i)(Camel|org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testHttpClient ()
specifier|public
name|void
name|testHttpClient
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|jpg
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/java.jpg"
argument_list|)
decl_stmt|;
name|String
name|body
init|=
literal|"TEST"
decl_stmt|;
name|Part
index|[]
name|parts
init|=
operator|new
name|Part
index|[]
block|{
operator|new
name|StringPart
argument_list|(
literal|"body"
argument_list|,
name|body
argument_list|)
block|,
operator|new
name|FilePart
argument_list|(
name|jpg
operator|.
name|getName
argument_list|()
argument_list|,
name|jpg
argument_list|)
block|}
decl_stmt|;
name|PostMethod
name|method
init|=
operator|new
name|PostMethod
argument_list|(
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/hello"
argument_list|)
decl_stmt|;
name|MultipartRequestEntity
name|requestEntity
init|=
operator|new
name|MultipartRequestEntity
argument_list|(
name|parts
argument_list|,
name|method
operator|.
name|getParams
argument_list|()
argument_list|)
decl_stmt|;
name|method
operator|.
name|setRequestEntity
argument_list|(
name|requestEntity
argument_list|)
expr_stmt|;
name|HttpClient
name|client
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|executeMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|String
name|responseBody
init|=
name|method
operator|.
name|getResponseBodyAsString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|body
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|String
name|numAttachments
init|=
name|method
operator|.
name|getResponseHeader
argument_list|(
literal|"numAttachments"
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|numAttachments
argument_list|,
literal|"1"
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
block|{
name|port1
operator|=
name|getPort
argument_list|()
expr_stmt|;
name|port2
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|serviceProc
init|=
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
comment|// put the number of attachments in a response header
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"numAttachments"
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
literal|"body"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|HttpEndpoint
name|epOut
init|=
name|getContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"?bridgeEndpoint=true&throwExceptionOnFailure=false"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|epOut
operator|.
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|MultipartHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/hello?enableMultipartFilter=false"
argument_list|)
operator|.
name|to
argument_list|(
name|epOut
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:"
operator|+
name|port1
operator|+
literal|"?matchOnUriPrefix=true"
argument_list|)
operator|.
name|process
argument_list|(
name|serviceProc
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

