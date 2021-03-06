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
name|util
operator|.
name|StopWatch
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
name|TimeUtils
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
DECL|class|HttpProxyRouteTest
specifier|public
class|class
name|HttpProxyRouteTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|10
decl_stmt|;
annotation|@
name|Test
DECL|method|testHttpProxy ()
specifier|public
name|void
name|testHttpProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Sending "
operator|+
name|size
operator|+
literal|" messages to a http endpoint which is proxied/bridged"
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}?foo="
operator|+
name|i
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye "
operator|+
name|i
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Time taken: "
operator|+
name|TimeUtils
operator|.
name|printDuration
argument_list|(
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpProxyWithDifferentPath ()
specifier|public
name|void
name|testHttpProxyWithDifferentPath
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/proxy"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/otherEndpoint"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/proxy/path"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/otherEndpoint/path"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpProxyHostHeader ()
specifier|public
name|void
name|testHttpProxyHostHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/proxyServer"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong host header"
argument_list|,
literal|"localhost:"
operator|+
name|getPort2
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpProxyFormHeader ()
specifier|public
name|void
name|testHttpProxyFormHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http://localhost:{{port}}/form"
argument_list|,
literal|"username=abc&pass=password"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/x-www-form-urlencoded"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response message"
argument_list|,
literal|"username=abc&pass=password"
argument_list|,
name|out
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
block|{
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:{{port}}/bye?throwExceptionOnFailure=false&bridgeEndpoint=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/proxy?matchOnUriPrefix=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:{{port}}/otherEndpoint?throwExceptionOnFailure=false&bridgeEndpoint=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/bye"
argument_list|)
operator|.
name|transform
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|prepend
argument_list|(
literal|"Bye "
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/otherEndpoint?matchOnUriPrefix=true"
argument_list|)
operator|.
name|transform
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/proxyServer"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:{{port2}}/host?bridgeEndpoint=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:{{port2}}/host"
argument_list|)
operator|.
name|transform
argument_list|(
name|header
argument_list|(
literal|"host"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check the from request
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/form?bridgeEndpoint=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
comment|// just take out the message body and send it back
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|request
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|request
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

