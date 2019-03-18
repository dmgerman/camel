begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSession
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
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
operator|.
name|HttpMessage
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
name|http
operator|.
name|common
operator|.
name|cookie
operator|.
name|ExchangeCookieHandler
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
name|http
operator|.
name|common
operator|.
name|cookie
operator|.
name|InstanceCookieHandler
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RestletProducerSessionTest
specifier|public
class|class
name|RestletProducerSessionTest
extends|extends
name|RestletTestSupport
block|{
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"restlet:http://127.0.0.1:"
operator|+
name|portNum
operator|+
literal|"/session?restletMethod=POST"
decl_stmt|;
annotation|@
name|Test
DECL|method|testProducerNoSession ()
specifier|public
name|void
name|testProducerNoSession
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"{New New World}"
argument_list|,
literal|"{New New World}"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"{World}"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"{World}"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerInstanceSession ()
specifier|public
name|void
name|testProducerInstanceSession
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"{Old New World}"
argument_list|,
literal|"{Old Old World}"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:instance"
argument_list|,
literal|"{World}"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:instance"
argument_list|,
literal|"{World}"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerExchangeSession ()
specifier|public
name|void
name|testProducerExchangeSession
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"{Old New World}"
argument_list|,
literal|"{Old New World}"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:exchange"
argument_list|,
literal|"{World}"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:exchange"
argument_list|,
literal|"{World}"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
name|jndiRegistry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndiRegistry
operator|.
name|bind
argument_list|(
literal|"instanceCookieHandler"
argument_list|,
operator|new
name|InstanceCookieHandler
argument_list|()
argument_list|)
expr_stmt|;
name|jndiRegistry
operator|.
name|bind
argument_list|(
literal|"exchangeCookieHandler"
argument_list|,
operator|new
name|ExchangeCookieHandler
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndiRegistry
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|url
argument_list|)
operator|.
name|to
argument_list|(
name|url
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:instance"
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#instanceCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#instanceCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:exchange"
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#exchangeCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#exchangeCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://127.0.0.1:"
operator|+
name|portNum
operator|+
literal|"/session?sessionSupport=true"
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
name|HttpMessage
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|HttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpSession
name|session
init|=
name|message
operator|.
name|getRequest
argument_list|()
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|.
name|length
argument_list|()
operator|>
literal|2
condition|)
block|{
name|body
operator|=
name|body
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|body
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"bar"
operator|.
name|equals
argument_list|(
name|session
operator|.
name|getAttribute
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
condition|)
block|{
name|body
operator|=
literal|"{Old "
operator|+
name|body
operator|+
literal|"}"
expr_stmt|;
block|}
else|else
block|{
name|session
operator|.
name|setAttribute
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|body
operator|=
literal|"{New "
operator|+
name|body
operator|+
literal|"}"
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
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

