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
name|ByteArrayOutputStream
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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HttpsURLConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|RuntimeCamelException
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

begin_class
DECL|class|HttpsAsyncRouteTest
specifier|public
class|class
name|HttpsAsyncRouteTest
extends|extends
name|HttpsRouteTest
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|port1
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|port2
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// ensure jsse clients can validate the self signed dummy localhost cert,
comment|// use the server keystore as the trust store for these tests
name|URL
name|trustStoreUrl
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"jsse/localhost.p12"
argument_list|)
decl_stmt|;
name|setSystemProp
argument_list|(
literal|"javax.net.ssl.trustStore"
argument_list|,
name|trustStoreUrl
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|setSystemProp
argument_list|(
literal|"javax.net.ssl.trustStorePassword"
argument_list|,
literal|"changeit"
argument_list|)
expr_stmt|;
name|setSystemProp
argument_list|(
literal|"javax.net.ssl.trustStoreType"
argument_list|,
literal|"PKCS12"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|restoreSystemProperties
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSystemProp (String key, String value)
specifier|protected
name|void
name|setSystemProp
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|String
name|originalValue
init|=
name|System
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|originalValues
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|originalValue
operator|!=
literal|null
condition|?
name|originalValue
else|:
name|NULL_VALUE_MARKER
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|restoreSystemProperties ()
specifier|protected
name|void
name|restoreSystemProperties
parameter_list|()
block|{
for|for
control|(
name|Object
name|key
range|:
name|originalValues
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|originalValues
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|NULL_VALUE_MARKER
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|System
operator|.
name|clearProperty
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|setProperty
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testEndpoint ()
specifier|public
name|void
name|testEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
comment|// these tests does not run well on Windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MockEndpoint
name|mockEndpointA
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:a"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpointA
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpointB
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:b"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpointB
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|invokeHttpEndpoint
argument_list|()
expr_stmt|;
name|mockEndpointA
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockEndpointB
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
name|mockEndpointA
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
name|info
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
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testEndpointWithoutHttps ()
specifier|public
name|void
name|testEndpointWithoutHttps
parameter_list|()
throws|throws
name|Exception
block|{
comment|// these tests does not run well on Windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MockEndpoint
name|mockEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:a"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/test"
argument_list|,
name|expectedBody
argument_list|,
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expect exception on access to https endpoint via http"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|expected
parameter_list|)
block|{         }
name|assertTrue
argument_list|(
literal|"mock endpoint was not called"
argument_list|,
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testHelloEndpoint ()
specifier|public
name|void
name|testHelloEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
comment|// these tests does not run well on Windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"https://localhost:"
operator|+
name|port1
operator|+
literal|"/hello"
argument_list|)
decl_stmt|;
name|HttpsURLConnection
name|connection
init|=
operator|(
name|HttpsURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|SSLContext
name|ssl
init|=
name|SSLContext
operator|.
name|getInstance
argument_list|(
literal|"TLSv1.2"
argument_list|)
decl_stmt|;
name|ssl
operator|.
name|init
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setSSLSocketFactory
argument_list|(
name|ssl
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|connection
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|int
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|is
operator|.
name|read
argument_list|()
operator|)
operator|>=
literal|0
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|String
name|data
init|=
operator|new
name|String
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<b>Hello World</b>"
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testHelloEndpointWithoutHttps ()
specifier|public
name|void
name|testHelloEndpointWithoutHttps
parameter_list|()
throws|throws
name|Exception
block|{
comment|// these tests does not run well on Windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
try|try
block|{
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/hello"
argument_list|)
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"expected SocketException on use ot http"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SocketException
name|expected
parameter_list|)
block|{         }
block|}
annotation|@
name|Override
DECL|method|invokeHttpEndpoint ()
specifier|protected
name|void
name|invokeHttpEndpoint
parameter_list|()
throws|throws
name|IOException
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getHttpProducerScheme
argument_list|()
operator|+
literal|"localhost:"
operator|+
name|port1
operator|+
literal|"/test"
argument_list|,
name|expectedBody
argument_list|,
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getHttpProducerScheme
argument_list|()
operator|+
literal|"localhost:"
operator|+
name|port2
operator|+
literal|"/test"
argument_list|,
name|expectedBody
argument_list|,
literal|"Content-Type"
argument_list|,
literal|"application/xml"
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
name|URISyntaxException
block|{
name|JettyHttpComponent
name|componentJetty
init|=
operator|(
name|JettyHttpComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|)
decl_stmt|;
name|componentJetty
operator|.
name|setSslPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
name|componentJetty
operator|.
name|setSslKeyPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
name|URL
name|keyStoreUrl
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"jsse/localhost.p12"
argument_list|)
decl_stmt|;
name|componentJetty
operator|.
name|setKeystore
argument_list|(
name|keyStoreUrl
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:https://localhost:"
operator|+
name|port1
operator|+
literal|"/test?async=true&useContinuation=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|Processor
name|proc
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<b>Hello World</b>"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|from
argument_list|(
literal|"jetty:https://localhost:"
operator|+
name|port1
operator|+
literal|"/hello?async=true&useContinuation=false"
argument_list|)
operator|.
name|process
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:https://localhost:"
operator|+
name|port2
operator|+
literal|"/test?async=true&useContinuation=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

