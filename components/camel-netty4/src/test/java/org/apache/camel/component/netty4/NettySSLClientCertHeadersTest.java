begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
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
DECL|class|NettySSLClientCertHeadersTest
specifier|public
class|class
name|NettySSLClientCertHeadersTest
extends|extends
name|BaseNettyTest
block|{
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
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"ksf"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/keystore.jks"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"tsf"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/keystore.jks"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testSSLInOutWithNettyConsumer ()
specifier|public
name|void
name|testSSLInOutWithNettyConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ibm jdks dont have sun security algorithms
if|if
condition|(
name|isJavaVendor
argument_list|(
literal|"ibm"
argument_list|)
condition|)
block|{
return|return;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_CLIENT_CERT_SUBJECT_NAME
argument_list|,
literal|"CN=arlu15, OU=Sun Java System Application Server, O=Sun Microsystems, L=Santa Clara, ST=California, C=US"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_CLIENT_CERT_ISSUER_NAME
argument_list|,
literal|"CN=arlu15, OU=Sun Java System Application Server, O=Sun Microsystems, L=Santa Clara, ST=California, C=US"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_CLIENT_CERT_SERIAL_NO
argument_list|,
literal|"1210701502"
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// needClientAuth=true so we can get the client certificate details
name|from
argument_list|(
literal|"netty:tcp://localhost:{{port}}?sync=true&ssl=true&passphrase=changeit&keyStoreFile=#ksf&trustStoreFile=#tsf"
operator|+
literal|"&needClientAuth=true&sslClientCertHeaders=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:{{port}}?sync=true&ssl=true&passphrase=changeit&keyStoreFile=#ksf&trustStoreFile=#tsf"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

