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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|http
operator|.
name|Consts
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
name|HttpHost
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
name|NameValuePair
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
name|config
operator|.
name|RequestConfig
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
name|entity
operator|.
name|UrlEncodedFormEntity
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
name|CloseableHttpResponse
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
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
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
name|HttpClients
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
name|message
operator|.
name|BasicNameValuePair
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
DECL|class|HttpBridgeBigFormPostRouteTest
specifier|public
class|class
name|HttpBridgeBigFormPostRouteTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|LARGE_HEADER_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|LARGE_HEADER_VALUE
init|=
literal|"Lorem Ipsum is simply dummy text of the printing and typesetting industry. "
operator|+
literal|"Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley "
operator|+
literal|"of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap "
operator|+
literal|"into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of "
operator|+
literal|"Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus "
operator|+
literal|"PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting "
operator|+
literal|"industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer "
operator|+
literal|"took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, "
operator|+
literal|"but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s "
operator|+
literal|"with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing "
operator|+
literal|"software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing "
operator|+
literal|"and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an "
operator|+
literal|"unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five "
operator|+
literal|"centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the "
operator|+
literal|"1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing "
operator|+
literal|"software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and "
operator|+
literal|"typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown "
operator|+
literal|"printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, "
operator|+
literal|"but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the "
operator|+
literal|"release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus "
operator|+
literal|"PageMaker including versions of Lorem Ipsum."
operator|+
literal|"Lorem Ipsum is simply dummy text of the printing and typesetting industry. "
operator|+
literal|"Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley "
operator|+
literal|"of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap "
operator|+
literal|"into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of "
operator|+
literal|"Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus "
operator|+
literal|"PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting "
operator|+
literal|"industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer "
operator|+
literal|"took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, "
operator|+
literal|"but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s "
operator|+
literal|"with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing "
operator|+
literal|"software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing "
operator|+
literal|"and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an "
operator|+
literal|"unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five "
operator|+
literal|"centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the "
operator|+
literal|"1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing "
operator|+
literal|"software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and "
operator|+
literal|"typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown "
operator|+
literal|"printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, "
operator|+
literal|"but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the "
operator|+
literal|"release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus "
operator|+
literal|"PageMaker including versions of Lorem Ipsum."
decl_stmt|;
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
name|List
argument_list|<
name|NameValuePair
argument_list|>
name|nvps
init|=
operator|new
name|ArrayList
argument_list|<
name|NameValuePair
argument_list|>
argument_list|()
decl_stmt|;
name|nvps
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"param1"
argument_list|,
name|LARGE_HEADER_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|nvps
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"param2"
argument_list|,
name|LARGE_HEADER_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|nvps
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"param3"
argument_list|,
name|LARGE_HEADER_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|HttpEntity
name|entity
init|=
operator|new
name|UrlEncodedFormEntity
argument_list|(
name|nvps
argument_list|,
name|Consts
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|HttpPost
name|httpPost
init|=
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/hello"
argument_list|)
decl_stmt|;
name|httpPost
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|HttpHost
name|proxy
init|=
operator|new
name|HttpHost
argument_list|(
literal|"localhost"
argument_list|,
literal|8888
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|RequestConfig
name|config
init|=
name|RequestConfig
operator|.
name|custom
argument_list|()
operator|.
name|setProxy
argument_list|(
name|proxy
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|httpPost
operator|.
name|setConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|CloseableHttpClient
name|httpClient
init|=
name|HttpClients
operator|.
name|createDefault
argument_list|()
decl_stmt|;
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|httpClient
operator|.
name|execute
argument_list|(
name|httpPost
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{         }
finally|finally
block|{
name|httpClient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/hello?matchOnUriPrefix=true"
argument_list|)
operator|.
name|removeHeaders
argument_list|(
literal|"formMetaData"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"?bridgeEndpoint=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

