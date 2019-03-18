begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|CamelExecutionException
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
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|support
operator|.
name|JaxWsServiceFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http_jetty
operator|.
name|JettyHTTPServerEngineFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|CxfProducerSessionTest
specifier|public
class|class
name|CxfProducerSessionTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|PORT
specifier|private
specifier|static
specifier|final
name|int
name|PORT
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
DECL|field|SIMPLE_SERVER_ADDRESS
specifier|private
specifier|static
specifier|final
name|String
name|SIMPLE_SERVER_ADDRESS
init|=
literal|"http://127.0.0.1:"
operator|+
name|PORT
operator|+
literal|"/CxfProducerSessionTest/test"
decl_stmt|;
DECL|field|REQUEST_MESSAGE_EXPRESSION
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_MESSAGE_EXPRESSION
init|=
literal|"<ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\"><arg0>${in.body}</arg0></ns1:echo>"
decl_stmt|;
DECL|field|NAMESPACES
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|NAMESPACES
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"ns1"
argument_list|,
literal|"http://cxf.component.camel.apache.org/"
argument_list|)
decl_stmt|;
DECL|field|PARAMETER_XPATH
specifier|private
specifier|static
specifier|final
name|String
name|PARAMETER_XPATH
init|=
literal|"/ns1:echoResponse/return/text()"
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"cxf://"
operator|+
name|SIMPLE_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.EchoService&dataFormat=PAYLOAD&synchronous=true"
decl_stmt|;
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|startServer ()
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start a simple front service
name|JaxWsServiceFactoryBean
name|svrFBean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|svrFBean
operator|.
name|setServiceClass
argument_list|(
name|EchoService
operator|.
name|class
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|svrBean
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|(
name|svrFBean
argument_list|)
decl_stmt|;
name|svrBean
operator|.
name|setAddress
argument_list|(
name|SIMPLE_SERVER_ADDRESS
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceClass
argument_list|(
name|EchoService
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceBean
argument_list|(
operator|new
name|EchoServiceSessionImpl
argument_list|()
argument_list|)
expr_stmt|;
comment|// make the Jetty server support sessions
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|JettyHTTPServerEngineFactory
name|jettyFactory
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|JettyHTTPServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|jettyFactory
operator|.
name|createJettyHTTPServerEngine
argument_list|(
name|PORT
argument_list|,
literal|"http"
argument_list|)
operator|.
name|setSessionSupport
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|destroyServer ()
specifier|public
specifier|static
name|void
name|destroyServer
parameter_list|()
block|{
comment|// If we don't destroy this the session support will spill over to other
comment|// tests and they will fail
name|JettyHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|PORT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSession ()
specifier|public
name|void
name|testNoSession
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"New New World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|response
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"New New World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeSession ()
specifier|public
name|void
name|testExchangeSession
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:exchange"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Old New World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|response
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:exchange"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Old New World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInstanceSession ()
specifier|public
name|void
name|testInstanceSession
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:instance"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Old New World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|response
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:instance"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Old Old World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testSessionWithInvalidPayload ()
specifier|public
name|void
name|testSessionWithInvalidPayload
parameter_list|()
throws|throws
name|Throwable
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:invalid"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
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
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
name|REQUEST_MESSAGE_EXPRESSION
argument_list|)
operator|.
name|to
argument_list|(
name|url
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|xpath
argument_list|(
name|PARAMETER_XPATH
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|NAMESPACES
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
name|REQUEST_MESSAGE_EXPRESSION
argument_list|)
operator|.
name|to
argument_list|(
name|url
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|xpath
argument_list|(
name|PARAMETER_XPATH
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|NAMESPACES
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
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
name|REQUEST_MESSAGE_EXPRESSION
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#instanceCookieHandler"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|xpath
argument_list|(
name|PARAMETER_XPATH
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|NAMESPACES
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
name|REQUEST_MESSAGE_EXPRESSION
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#instanceCookieHandler"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|xpath
argument_list|(
name|PARAMETER_XPATH
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|NAMESPACES
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
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
name|REQUEST_MESSAGE_EXPRESSION
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#exchangeCookieHandler"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|xpath
argument_list|(
name|PARAMETER_XPATH
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|NAMESPACES
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
name|REQUEST_MESSAGE_EXPRESSION
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#exchangeCookieHandler"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|xpath
argument_list|(
name|PARAMETER_XPATH
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|NAMESPACES
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:invalid"
argument_list|)
operator|.
name|to
argument_list|(
name|url
operator|+
literal|"&cookieHandler=#exchangeCookieHandler"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
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
block|}
end_class

end_unit

