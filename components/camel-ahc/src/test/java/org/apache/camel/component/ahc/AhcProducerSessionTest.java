begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
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
name|Cookie
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
name|BindToRegistry
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|AhcProducerSessionTest
specifier|public
class|class
name|AhcProducerSessionTest
extends|extends
name|BaseAhcTest
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"instanceCookieHandler"
argument_list|)
DECL|field|instanceCookieHandler
name|InstanceCookieHandler
name|instanceCookieHandler
init|=
operator|new
name|InstanceCookieHandler
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"exchangeCookieHandler"
argument_list|)
DECL|field|exchangeCookieHandler
name|ExchangeCookieHandler
name|exchangeCookieHandler
init|=
operator|new
name|ExchangeCookieHandler
argument_list|()
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
literal|"New New World"
argument_list|,
literal|"New New World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
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
literal|"Old New World"
argument_list|,
literal|"Old Old World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:instance"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:instance"
argument_list|,
literal|"World"
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
literal|"Old New World"
argument_list|,
literal|"Old New World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:exchange"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:exchange"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|getTestServerEndpointSessionUrl ()
specifier|private
name|String
name|getTestServerEndpointSessionUrl
parameter_list|()
block|{
comment|// session handling will not work for localhost
return|return
name|getProtocol
argument_list|()
operator|+
literal|"://127.0.0.1:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/session"
return|;
block|}
DECL|method|getTestServerEndpointSessionUri ()
specifier|private
name|String
name|getTestServerEndpointSessionUri
parameter_list|()
block|{
return|return
literal|"jetty:"
operator|+
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?sessionSupport=true"
return|;
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ahc:"
operator|+
name|getTestServerEndpointSessionUrl
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"ahc:"
operator|+
name|getTestServerEndpointSessionUrl
argument_list|()
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
literal|"ahc:"
operator|+
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?cookieHandler=#instanceCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ahc:"
operator|+
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?cookieHandler=#instanceCookieHandler"
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
literal|"ahc:"
operator|+
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?cookieHandler=#exchangeCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ahc:"
operator|+
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?cookieHandler=#exchangeCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|getTestServerEndpointSessionUri
argument_list|()
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
name|Object
name|cookiesObj
init|=
name|message
operator|.
name|getHeader
argument_list|(
literal|"Cookie"
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
name|message
operator|.
name|setBody
argument_list|(
literal|"Old "
operator|+
name|body
argument_list|)
expr_stmt|;
comment|/*                                  * If we are in a session we should also have a cookie header with two                                  * cookies. This test checks that the cookies are in one line.                                  * We can also get the cookies with request.getCookies() but this will                                  * always give us two cookies even if there are two cookie headers instead                                  * of one multi-value cookie header.                                  */
if|if
condition|(
name|cookiesObj
operator|instanceof
name|String
operator|&&
operator|(
operator|(
name|String
operator|)
name|cookiesObj
operator|)
operator|.
name|contains
argument_list|(
literal|"othercookie=value"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
operator|(
operator|(
name|String
operator|)
name|cookiesObj
operator|)
operator|.
name|contains
argument_list|(
literal|"JSESSIONID="
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"JSESSIONID missing"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"JSESSIONID missing"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|error
argument_list|(
literal|"othercookie=value is missing in cookie"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"othercookie=value is missing in cookie"
argument_list|)
throw|;
block|}
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
name|message
operator|.
name|setBody
argument_list|(
literal|"New "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|getResponse
argument_list|()
operator|.
name|addCookie
argument_list|(
operator|new
name|Cookie
argument_list|(
literal|"othercookie"
argument_list|,
literal|"value"
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

