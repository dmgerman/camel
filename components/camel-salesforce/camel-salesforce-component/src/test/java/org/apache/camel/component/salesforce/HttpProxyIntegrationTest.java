begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
package|;
end_package

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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|Version
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|Versions
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
name|eclipse
operator|.
name|jetty
operator|.
name|proxy
operator|.
name|ConnectHandler
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
name|server
operator|.
name|Server
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
name|server
operator|.
name|ServerConnector
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
name|util
operator|.
name|B64Code
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
name|util
operator|.
name|StringUtil
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpHeader
operator|.
name|PROXY_AUTHENTICATE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpHeader
operator|.
name|PROXY_AUTHORIZATION
import|;
end_import

begin_comment
comment|/**  * Test HTTP proxy configuration for Salesforce component.  */
end_comment

begin_class
DECL|class|HttpProxyIntegrationTest
specifier|public
class|class
name|HttpProxyIntegrationTest
extends|extends
name|AbstractSalesforceTestBase
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|HttpProxyIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|HTTP_PROXY_HOST
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_PROXY_HOST
init|=
literal|"localhost"
decl_stmt|;
DECL|field|HTTP_PROXY_USER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_PROXY_USER_NAME
init|=
literal|"camel-user"
decl_stmt|;
DECL|field|HTTP_PROXY_PASSWORD
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_PROXY_PASSWORD
init|=
literal|"camel-user-password"
decl_stmt|;
DECL|field|HTTP_PROXY_REALM
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_PROXY_REALM
init|=
literal|"proxy-realm"
decl_stmt|;
DECL|field|server
specifier|private
specifier|static
name|Server
name|server
decl_stmt|;
DECL|field|httpProxyPort
specifier|private
specifier|static
name|int
name|httpProxyPort
decl_stmt|;
annotation|@
name|Test
DECL|method|testGetVersions ()
specifier|public
name|void
name|testGetVersions
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGetVersions
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|doTestGetVersions
argument_list|(
literal|"Xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doTestGetVersions (String suffix)
specifier|private
name|void
name|doTestGetVersions
parameter_list|(
name|String
name|suffix
parameter_list|)
throws|throws
name|Exception
block|{
comment|// test getVersions doesn't need a body
comment|// assert expected result
name|Object
name|o
init|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:getVersions"
operator|+
name|suffix
argument_list|,
operator|(
name|Object
operator|)
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Version
argument_list|>
name|versions
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Versions
condition|)
block|{
name|versions
operator|=
operator|(
operator|(
name|Versions
operator|)
name|o
operator|)
operator|.
name|getVersions
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|versions
operator|=
operator|(
name|List
argument_list|<
name|Version
argument_list|>
operator|)
name|o
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|versions
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Versions: {}"
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BeforeClass
DECL|method|setupServer ()
specifier|public
specifier|static
name|void
name|setupServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start a local HTTP proxy using Jetty server
name|server
operator|=
operator|new
name|Server
argument_list|()
expr_stmt|;
comment|/*         final SSLContextParameters contextParameters = new SSLContextParameters();         final SslContextFactory sslContextFactory = new SslContextFactory();         sslContextFactory.setSslContext(contextParameters.createSSLContext());         ServerConnector connector = new ServerConnector(server, sslContextFactory); */
name|ServerConnector
name|connector
init|=
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|)
decl_stmt|;
name|connector
operator|.
name|setHost
argument_list|(
name|HTTP_PROXY_HOST
argument_list|)
expr_stmt|;
name|server
operator|.
name|addConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
specifier|final
name|String
name|authenticationString
init|=
literal|"Basic "
operator|+
name|B64Code
operator|.
name|encode
argument_list|(
name|HTTP_PROXY_USER_NAME
operator|+
literal|":"
operator|+
name|HTTP_PROXY_PASSWORD
argument_list|,
name|StringUtil
operator|.
name|__ISO_8859_1
argument_list|)
decl_stmt|;
name|ConnectHandler
name|connectHandler
init|=
operator|new
name|ConnectHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|boolean
name|handleAuthentication
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|String
name|address
parameter_list|)
block|{
comment|// validate proxy-authentication header
specifier|final
name|String
name|header
init|=
name|request
operator|.
name|getHeader
argument_list|(
name|PROXY_AUTHORIZATION
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|authenticationString
operator|.
name|equals
argument_list|(
name|header
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Missing header "
operator|+
name|PROXY_AUTHORIZATION
argument_list|)
expr_stmt|;
comment|// ask for authentication header
name|response
operator|.
name|setHeader
argument_list|(
name|PROXY_AUTHENTICATE
operator|.
name|toString
argument_list|()
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Basic realm=\"%s\""
argument_list|,
name|HTTP_PROXY_REALM
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Request contains required header "
operator|+
name|PROXY_AUTHORIZATION
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|connectHandler
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting proxy server..."
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|httpProxyPort
operator|=
name|connector
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Started proxy server on port {}"
argument_list|,
name|httpProxyPort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createComponent ()
specifier|protected
name|void
name|createComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|createComponent
argument_list|()
expr_stmt|;
specifier|final
name|SalesforceComponent
name|salesforce
init|=
operator|(
name|SalesforceComponent
operator|)
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"salesforce"
argument_list|)
decl_stmt|;
comment|// set HTTP proxy settings
name|salesforce
operator|.
name|setHttpProxyHost
argument_list|(
name|HTTP_PROXY_HOST
argument_list|)
expr_stmt|;
name|salesforce
operator|.
name|setHttpProxyPort
argument_list|(
name|httpProxyPort
argument_list|)
expr_stmt|;
name|salesforce
operator|.
name|setIsHttpProxySecure
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|salesforce
operator|.
name|setHttpProxyUsername
argument_list|(
name|HTTP_PROXY_USER_NAME
argument_list|)
expr_stmt|;
name|salesforce
operator|.
name|setHttpProxyPassword
argument_list|(
name|HTTP_PROXY_PASSWORD
argument_list|)
expr_stmt|;
name|salesforce
operator|.
name|setHttpProxyAuthUri
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"http://%s:%s"
argument_list|,
name|HTTP_PROXY_HOST
argument_list|,
name|httpProxyPort
argument_list|)
argument_list|)
expr_stmt|;
name|salesforce
operator|.
name|setHttpProxyRealm
argument_list|(
name|HTTP_PROXY_REALM
argument_list|)
expr_stmt|;
comment|// set HTTP client properties
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"timeout"
argument_list|,
literal|"60000"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"removeIdleDestinations"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|salesforce
operator|.
name|setHttpClientProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|tearDownAfterClass ()
specifier|public
specifier|static
name|void
name|tearDownAfterClass
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelTestSupport
operator|.
name|tearDownAfterClass
argument_list|()
expr_stmt|;
comment|// stop the proxy server after component
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping proxy server..."
argument_list|)
expr_stmt|;
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopped proxy server"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateRouteBuilder ()
specifier|protected
name|RouteBuilder
name|doCreateRouteBuilder
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
comment|// testGetVersion
name|from
argument_list|(
literal|"direct:getVersions"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getVersions"
argument_list|)
expr_stmt|;
comment|// allow overriding format per endpoint
name|from
argument_list|(
literal|"direct:getVersionsXml"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getVersions?format=XML"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

