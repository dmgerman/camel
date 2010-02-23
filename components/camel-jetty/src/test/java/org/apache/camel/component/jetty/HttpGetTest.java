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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|mock
operator|.
name|MockEndpoint
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
name|HttpRequest
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
name|HttpStatus
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
name|StringEntity
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
name|localserver
operator|.
name|LocalTestServer
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
name|protocol
operator|.
name|HTTP
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
name|protocol
operator|.
name|HttpContext
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
name|protocol
operator|.
name|HttpRequestHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpGetTest
specifier|public
class|class
name|HttpGetTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|expectedText
specifier|protected
name|String
name|expectedText
init|=
literal|"<html"
decl_stmt|;
DECL|field|localServer
specifier|protected
name|LocalTestServer
name|localServer
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|=
operator|new
name|LocalTestServer
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|HttpRequestHandler
argument_list|()
block|{
specifier|public
name|void
name|handle
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|HttpResponse
name|response
parameter_list|,
name|HttpContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
literal|"GET"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getRequestLine
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|response
operator|.
name|setStatusCode
argument_list|(
name|HttpStatus
operator|.
name|SC_METHOD_FAILURE
argument_list|)
expr_stmt|;
return|return;
block|}
name|response
operator|.
name|setStatusCode
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
name|response
operator|.
name|setEntity
argument_list|(
operator|new
name|StringEntity
argument_list|(
name|expectedText
argument_list|,
name|HTTP
operator|.
name|ISO_8859_1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|localServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|localServer
operator|!=
literal|null
condition|)
block|{
name|localServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testHttpGet ()
specifier|public
name|void
name|testHttpGet
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|mockEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"exchange"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"in"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|in
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Headers: "
operator|+
name|headers
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be more than one header but was: "
operator|+
name|headers
argument_list|,
name|headers
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|String
name|body
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
name|log
operator|.
name|debug
argument_list|(
literal|"Body: "
operator|+
name|body
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a body!"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|body
operator|=
name|body
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"body should contain: "
operator|+
name|expectedText
operator|+
literal|" but was: "
operator|+
name|body
argument_list|,
name|body
operator|.
name|contains
argument_list|(
name|expectedText
argument_list|)
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://"
operator|+
name|localServer
operator|.
name|getServiceHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getServicePort
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

