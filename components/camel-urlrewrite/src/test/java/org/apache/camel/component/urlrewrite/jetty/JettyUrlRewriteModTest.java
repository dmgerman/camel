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
DECL|class|JettyUrlRewriteModTest
specifier|public
class|class
name|JettyUrlRewriteModTest
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
name|setModRewriteConfText
argument_list|(
literal|"RewriteRule page/([^/\\.]+)/?$ index.php?page=$1 [L]"
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
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp/page/software/"
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
literal|"/myapp2/index.php?page=software"
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
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp?matchOnUriPrefix=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jetty:http://localhost:{{port2}}/myapp2?bridgeEndpoint=true&throwExceptionOnFailure=false&urlRewrite=#myRewrite"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:{{port2}}/myapp2?matchOnUriPrefix=true"
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

