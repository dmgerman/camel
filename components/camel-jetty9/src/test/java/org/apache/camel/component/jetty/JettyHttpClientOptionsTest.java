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
name|http
operator|.
name|HttpProducer
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
name|HttpCommonEndpoint
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
name|client
operator|.
name|HttpClient
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
comment|/**  * Unit test for http client options.  */
end_comment

begin_class
DECL|class|JettyHttpClientOptionsTest
specifier|public
class|class
name|JettyHttpClientOptionsTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testCustomHttpBinding ()
specifier|public
name|void
name|testCustomHttpBinding
parameter_list|()
throws|throws
name|Exception
block|{
comment|// assert jetty was configured with our timeout
name|HttpCommonEndpoint
name|jettyEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http://localhost:{{port}}/myapp/myservice?httpClient.soTimeout=5555"
argument_list|,
name|HttpCommonEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Jetty endpoint should not be null "
argument_list|,
name|jettyEndpoint
argument_list|)
expr_stmt|;
name|HttpProducer
name|producer
init|=
operator|(
name|HttpProducer
operator|)
name|jettyEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong http client parameter"
argument_list|,
literal|5555
argument_list|,
name|producer
operator|.
name|getHttpClient
argument_list|()
operator|.
name|getParams
argument_list|()
operator|.
name|getSoTimeout
argument_list|()
argument_list|)
expr_stmt|;
comment|// send and receive
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/myapp/myservice"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProxySettingOfJettyHttpClient ()
specifier|public
name|void
name|testProxySettingOfJettyHttpClient
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup the Proxy setting through the URI
name|HttpCommonEndpoint
name|jettyEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"jetty://http://localhost:{{port}}/proxy/setting?proxyHost=192.168.0.1&proxyPort=9090"
argument_list|,
name|HttpCommonEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Jetty endpoint should not be null "
argument_list|,
name|jettyEndpoint
argument_list|)
expr_stmt|;
name|JettyHttpProducer
name|producer
init|=
operator|(
name|JettyHttpProducer
operator|)
name|jettyEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|assertProxyAddress
argument_list|(
name|producer
operator|.
name|getClient
argument_list|()
argument_list|,
literal|"192.168.0.1"
argument_list|,
literal|9090
argument_list|)
expr_stmt|;
comment|// setup the context properties
name|context
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"http.proxyHost"
argument_list|,
literal|"192.168.0.2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"http.proxyPort"
argument_list|,
literal|"8080"
argument_list|)
expr_stmt|;
name|jettyEndpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"jetty://http://localhost:{{port}}/proxy2/setting"
argument_list|,
name|HttpCommonEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|producer
operator|=
operator|(
name|JettyHttpProducer
operator|)
name|jettyEndpoint
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|assertProxyAddress
argument_list|(
name|producer
operator|.
name|getClient
argument_list|()
argument_list|,
literal|"192.168.0.2"
argument_list|,
literal|8080
argument_list|)
expr_stmt|;
name|context
operator|.
name|getProperties
argument_list|()
operator|.
name|clear
argument_list|()
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
literal|"jetty:http://localhost:{{port}}/myapp/myservice?httpClient.soTimeout=5555"
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
return|;
block|}
DECL|method|assertProxyAddress (HttpClient client, String expectedHost, int expectedPort)
specifier|private
name|void
name|assertProxyAddress
parameter_list|(
name|HttpClient
name|client
parameter_list|,
name|String
name|expectedHost
parameter_list|,
name|int
name|expectedPort
parameter_list|)
block|{
name|CamelHttpClient
name|camelHttpClient
init|=
operator|(
name|CamelHttpClient
operator|)
name|client
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong http proxy host parameter"
argument_list|,
name|expectedHost
argument_list|,
name|camelHttpClient
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong http proxy port paramerter"
argument_list|,
name|expectedPort
argument_list|,
name|camelHttpClient
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

