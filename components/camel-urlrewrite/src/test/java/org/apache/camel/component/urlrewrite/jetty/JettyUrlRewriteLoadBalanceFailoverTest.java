begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.urlrewrite.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|urlrewrite
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
name|urlrewrite
operator|.
name|BaseUrlRewriteTest
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
name|urlrewrite
operator|.
name|http
operator|.
name|HttpUrlRewrite
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|JettyUrlRewriteLoadBalanceFailoverTest
specifier|public
class|class
name|JettyUrlRewriteLoadBalanceFailoverTest
extends|extends
name|BaseUrlRewriteTest
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
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|HttpUrlRewrite
name|myRewrite
init|=
operator|new
name|HttpUrlRewrite
argument_list|()
decl_stmt|;
name|myRewrite
operator|.
name|setConfigFile
argument_list|(
literal|"example/urlrewrite2.xml"
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myRewrite"
argument_list|,
name|myRewrite
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testHttpUriRewrite ()
specifier|public
name|void
name|testHttpUriRewrite
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we should failover from app2 to app3 all the time
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http4://localhost:{{port}}/myapp/products/1234"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort2
argument_list|()
operator|+
literal|"/myapp3/products/index.jsp?product_id=1234"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http4://localhost:{{port}}/myapp/products/5678"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort2
argument_list|()
operator|+
literal|"/myapp3/products/index.jsp?product_id=5678"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http4://localhost:{{port}}/myapp/products/3333"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort2
argument_list|()
operator|+
literal|"/myapp3/products/index.jsp?product_id=3333"
argument_list|,
name|out
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// just disable error handler as we use failover load balancer
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e1
comment|// we want to use the failover loadbalancer
comment|// to have it to react we must set throwExceptionOnFailure=true, which is also the default value
comment|// so we can omit configuring this option
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp?matchOnUriPrefix=true"
argument_list|)
operator|.
name|loadBalance
argument_list|()
operator|.
name|failover
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"jetty:http://localhost:{{port2}}/myapp2?bridgeEndpoint=true&throwExceptionOnFailure=true&urlRewrite=#myRewrite"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jetty:http://localhost:{{port2}}/myapp3?bridgeEndpoint=true&throwExceptionOnFailure=true&urlRewrite=#myRewrite"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"jetty:http://localhost:{{port2}}/myapp2?matchOnUriPrefix=true"
argument_list|)
operator|.
name|log
argument_list|(
literal|"I am going to fail"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:{{port2}}/myapp3?matchOnUriPrefix=true"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"${header.CamelHttpUrl}?${header.CamelHttpQuery}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

