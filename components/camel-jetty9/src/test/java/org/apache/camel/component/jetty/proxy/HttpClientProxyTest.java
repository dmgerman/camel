begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.proxy
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
operator|.
name|proxy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
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
name|ProxyBuilder
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
name|jetty
operator|.
name|BaseJettyTest
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
name|HttpOperationFailedException
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|HttpClientProxyTest
specifier|public
class|class
name|HttpClientProxyTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testHttpClientNoProxyOk ()
specifier|public
name|void
name|testHttpClientNoProxyOk
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:cool"
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
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpClientNoProxyException ()
specifier|public
name|void
name|testHttpClientNoProxyException
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:cool"
argument_list|,
literal|"Kaboom"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|HttpOperationFailedException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cause
operator|.
name|getResponseBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getResponseBody
argument_list|()
operator|.
name|contains
argument_list|(
literal|"MyAppException"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testHttpClientProxyOk ()
specifier|public
name|void
name|testHttpClientProxyOk
parameter_list|()
throws|throws
name|Exception
block|{
name|MyCoolService
name|proxy
init|=
operator|new
name|ProxyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|endpoint
argument_list|(
literal|"direct:cool"
argument_list|)
operator|.
name|build
argument_list|(
name|MyCoolService
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|proxy
operator|.
name|hello
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpClientProxyException ()
specifier|public
name|void
name|testHttpClientProxyException
parameter_list|()
throws|throws
name|Exception
block|{
name|MyCoolService
name|proxy
init|=
operator|new
name|ProxyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|endpoint
argument_list|(
literal|"direct:cool"
argument_list|)
operator|.
name|build
argument_list|(
name|MyCoolService
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|proxy
operator|.
name|hello
argument_list|(
literal|"Kaboom"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|HttpOperationFailedException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cause
operator|.
name|getResponseBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getResponseBody
argument_list|()
operator|.
name|contains
argument_list|(
literal|"MyAppException"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:cool"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:{{port}}/myapp/myservice"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp/myservice"
argument_list|)
operator|.
name|bean
argument_list|(
name|MyCoolServiceBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

