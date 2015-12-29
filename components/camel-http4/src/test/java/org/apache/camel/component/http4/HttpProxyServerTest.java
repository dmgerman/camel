begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|HashMap
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
name|component
operator|.
name|http4
operator|.
name|handler
operator|.
name|HeaderValidationHandler
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
name|URISupport
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
name|codec
operator|.
name|BinaryDecoder
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
name|codec
operator|.
name|DecoderException
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
name|codec
operator|.
name|binary
operator|.
name|Base64
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
name|Header
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
name|HttpException
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
name|HttpRequestInterceptor
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
name|HttpResponseInterceptor
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
name|ProtocolException
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
name|auth
operator|.
name|AUTH
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
name|bootstrap
operator|.
name|HttpServer
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
name|bootstrap
operator|.
name|ServerBootstrap
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
name|HttpProcessor
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
name|ImmutableHttpProcessor
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
name|ResponseContent
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
comment|/**  *  * @version   */
end_comment

begin_class
DECL|class|HttpProxyServerTest
specifier|public
class|class
name|HttpProxyServerTest
extends|extends
name|BaseHttpTest
block|{
DECL|field|proxy
specifier|private
name|HttpServer
name|proxy
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedHeaders
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expectedHeaders
operator|.
name|put
argument_list|(
literal|"Proxy-Connection"
argument_list|,
literal|"Keep-Alive"
argument_list|)
expr_stmt|;
name|proxy
operator|=
name|ServerBootstrap
operator|.
name|bootstrap
argument_list|()
operator|.
name|setHttpProcessor
argument_list|(
name|getBasicHttpProcessor
argument_list|()
argument_list|)
operator|.
name|setConnectionReuseStrategy
argument_list|(
name|getConnectionReuseStrategy
argument_list|()
argument_list|)
operator|.
name|setResponseFactory
argument_list|(
name|getHttpResponseFactory
argument_list|()
argument_list|)
operator|.
name|setExpectationVerifier
argument_list|(
name|getHttpExpectationVerifier
argument_list|()
argument_list|)
operator|.
name|setSslContext
argument_list|(
name|getSSLContext
argument_list|()
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"*"
argument_list|,
operator|new
name|HeaderValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|,
name|expectedHeaders
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|proxy
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
name|proxy
operator|!=
literal|null
condition|)
block|{
name|proxy
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getBasicHttpProcessor ()
specifier|protected
name|HttpProcessor
name|getBasicHttpProcessor
parameter_list|()
block|{
name|List
argument_list|<
name|HttpRequestInterceptor
argument_list|>
name|requestInterceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|HttpRequestInterceptor
argument_list|>
argument_list|()
decl_stmt|;
name|requestInterceptors
operator|.
name|add
argument_list|(
operator|new
name|RequestProxyBasicAuth
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|HttpResponseInterceptor
argument_list|>
name|responseInterceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|HttpResponseInterceptor
argument_list|>
argument_list|()
decl_stmt|;
name|responseInterceptors
operator|.
name|add
argument_list|(
operator|new
name|ResponseContent
argument_list|()
argument_list|)
expr_stmt|;
name|responseInterceptors
operator|.
name|add
argument_list|(
operator|new
name|ResponseProxyBasicUnauthorized
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableHttpProcessor
name|httpproc
init|=
operator|new
name|ImmutableHttpProcessor
argument_list|(
name|requestInterceptors
argument_list|,
name|responseInterceptors
argument_list|)
decl_stmt|;
return|return
name|httpproc
return|;
block|}
annotation|@
name|Test
DECL|method|testDifferentHttpProxyConfigured ()
specifier|public
name|void
name|testDifferentHttpProxyConfigured
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpEndpoint
name|http1
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http4://www.google.com?proxyAuthHost=myproxy&proxyAuthPort=1234"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|http2
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http4://www.google.com?test=parameter&proxyAuthHost=myotherproxy&proxyAuthPort=2345"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// HttpClientBuilder doesn't support get the configuration here
comment|//As the endpointUri is recreated, so the parameter could be in different place, so we use the URISupport.normalizeUri
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint uri of http1"
argument_list|,
literal|"http4://www.google.com?proxyAuthHost=myproxy&proxyAuthPort=1234"
argument_list|,
name|URISupport
operator|.
name|normalizeUri
argument_list|(
name|http1
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint uri of http2"
argument_list|,
literal|"http4://www.google.com?proxyAuthHost=myotherproxy&proxyAuthPort=2345&test=parameter"
argument_list|,
name|URISupport
operator|.
name|normalizeUri
argument_list|(
name|http2
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the same EndpointKey"
argument_list|,
name|http1
operator|.
name|getEndpointKey
argument_list|()
argument_list|,
name|http2
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|httpGetWithProxyAndWithoutUser ()
specifier|public
name|void
name|httpGetWithProxyAndWithoutUser
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getProxyHost
argument_list|()
operator|+
literal|":"
operator|+
name|getProxyPort
argument_list|()
operator|+
literal|"?proxyAuthHost="
operator|+
name|getProxyHost
argument_list|()
operator|+
literal|"&proxyAuthPort="
operator|+
name|getProxyPort
argument_list|()
argument_list|,
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
block|{             }
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|private
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxy
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
return|;
block|}
DECL|method|getProxyPort ()
specifier|private
name|int
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxy
operator|.
name|getLocalPort
argument_list|()
return|;
block|}
DECL|class|RequestProxyBasicAuth
class|class
name|RequestProxyBasicAuth
implements|implements
name|HttpRequestInterceptor
block|{
DECL|method|process (final HttpRequest request, final HttpContext context)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|HttpRequest
name|request
parameter_list|,
specifier|final
name|HttpContext
name|context
parameter_list|)
throws|throws
name|HttpException
throws|,
name|IOException
block|{
name|String
name|auth
init|=
literal|null
decl_stmt|;
name|String
name|requestLine
init|=
name|request
operator|.
name|getRequestLine
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// assert we set a write GET URI
if|if
condition|(
name|requestLine
operator|.
name|contains
argument_list|(
literal|"http4://localhost"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Get a wrong proxy GET url"
argument_list|)
throw|;
block|}
name|Header
name|h
init|=
name|request
operator|.
name|getFirstHeader
argument_list|(
name|AUTH
operator|.
name|PROXY_AUTH_RESP
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|h
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|auth
operator|=
name|s
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|auth
operator|!=
literal|null
condition|)
block|{
name|int
name|i
init|=
name|auth
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|ProtocolException
argument_list|(
literal|"Invalid Authorization header: "
operator|+
name|auth
argument_list|)
throw|;
block|}
name|String
name|authscheme
init|=
name|auth
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|authscheme
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"basic"
argument_list|)
condition|)
block|{
name|String
name|s
init|=
name|auth
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|byte
index|[]
name|credsRaw
init|=
name|s
operator|.
name|getBytes
argument_list|(
literal|"ASCII"
argument_list|)
decl_stmt|;
name|BinaryDecoder
name|codec
init|=
operator|new
name|Base64
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|creds
init|=
operator|new
name|String
argument_list|(
name|codec
operator|.
name|decode
argument_list|(
name|credsRaw
argument_list|)
argument_list|,
literal|"ASCII"
argument_list|)
decl_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"proxy-creds"
argument_list|,
name|creds
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DecoderException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ProtocolException
argument_list|(
literal|"Malformed BASIC credentials"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
DECL|class|ResponseProxyBasicUnauthorized
class|class
name|ResponseProxyBasicUnauthorized
implements|implements
name|HttpResponseInterceptor
block|{
DECL|method|process (final HttpResponse response, final HttpContext context)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|HttpResponse
name|response
parameter_list|,
specifier|final
name|HttpContext
name|context
parameter_list|)
throws|throws
name|HttpException
throws|,
name|IOException
block|{
if|if
condition|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
operator|==
name|HttpStatus
operator|.
name|SC_PROXY_AUTHENTICATION_REQUIRED
condition|)
block|{
name|response
operator|.
name|addHeader
argument_list|(
name|AUTH
operator|.
name|PROXY_AUTH
argument_list|,
literal|"Basic realm=\"test realm\""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

