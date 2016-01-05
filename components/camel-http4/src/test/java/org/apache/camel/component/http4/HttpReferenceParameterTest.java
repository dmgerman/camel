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
name|DefaultHttpBinding
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
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
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
name|BasicHttpContext
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for resolving reference parameters.  *  * @version   */
end_comment

begin_class
DECL|class|HttpReferenceParameterTest
specifier|public
class|class
name|HttpReferenceParameterTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_URI_1
specifier|private
specifier|static
specifier|final
name|String
name|TEST_URI_1
init|=
literal|"http4://localhost:8080?httpBinding=#customBinding&httpClientConfigurer=#customConfigurer&httpContext=#customContext"
decl_stmt|;
DECL|field|TEST_URI_2
specifier|private
specifier|static
specifier|final
name|String
name|TEST_URI_2
init|=
literal|"http4://localhost:8081?httpBinding=#customBinding&httpClientConfigurer=#customConfigurer&httpContext=#customContext"
decl_stmt|;
DECL|field|endpoint1
specifier|private
name|HttpEndpoint
name|endpoint1
decl_stmt|;
DECL|field|endpoint2
specifier|private
name|HttpEndpoint
name|endpoint2
decl_stmt|;
DECL|field|testBinding
specifier|private
name|TestHttpBinding
name|testBinding
decl_stmt|;
DECL|field|testConfigurer
specifier|private
name|TestClientConfigurer
name|testConfigurer
decl_stmt|;
DECL|field|testHttpContext
specifier|private
name|HttpContext
name|testHttpContext
decl_stmt|;
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
name|this
operator|.
name|testBinding
operator|=
operator|new
name|TestHttpBinding
argument_list|()
expr_stmt|;
name|this
operator|.
name|testConfigurer
operator|=
operator|new
name|TestClientConfigurer
argument_list|()
expr_stmt|;
name|this
operator|.
name|testHttpContext
operator|=
operator|new
name|BasicHttpContext
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint1
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|TEST_URI_1
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint2
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|TEST_URI_2
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpBinding ()
specifier|public
name|void
name|testHttpBinding
parameter_list|()
block|{
name|assertSame
argument_list|(
name|testBinding
argument_list|,
name|endpoint1
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|testBinding
argument_list|,
name|endpoint2
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpClientConfigurer ()
specifier|public
name|void
name|testHttpClientConfigurer
parameter_list|()
block|{
name|assertSame
argument_list|(
name|testConfigurer
argument_list|,
name|endpoint1
operator|.
name|getHttpClientConfigurer
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|testConfigurer
argument_list|,
name|endpoint2
operator|.
name|getHttpClientConfigurer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpContext ()
specifier|public
name|void
name|testHttpContext
parameter_list|()
block|{
name|assertSame
argument_list|(
name|testHttpContext
argument_list|,
name|endpoint1
operator|.
name|getHttpContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|testHttpContext
argument_list|,
name|endpoint2
operator|.
name|getHttpContext
argument_list|()
argument_list|)
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
literal|"customBinding"
argument_list|,
name|testBinding
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"customConfigurer"
argument_list|,
name|testConfigurer
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"customContext"
argument_list|,
name|testHttpContext
argument_list|)
expr_stmt|;
return|return
name|registry
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start1"
argument_list|)
operator|.
name|to
argument_list|(
name|TEST_URI_1
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|to
argument_list|(
name|TEST_URI_2
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|class|TestHttpBinding
specifier|private
specifier|static
class|class
name|TestHttpBinding
extends|extends
name|DefaultHttpBinding
block|{     }
DECL|class|TestClientConfigurer
specifier|private
specifier|static
class|class
name|TestClientConfigurer
implements|implements
name|HttpClientConfigurer
block|{
DECL|method|configureHttpClient (HttpClientBuilder clientBuilder)
specifier|public
name|void
name|configureHttpClient
parameter_list|(
name|HttpClientBuilder
name|clientBuilder
parameter_list|)
block|{         }
block|}
block|}
end_class

end_unit

