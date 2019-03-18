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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test HTTP client sends Accept header in HTTP GET  */
end_comment

begin_class
DECL|class|HttpAcceptHeaderTest
specifier|public
class|class
name|HttpAcceptHeaderTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testAccept ()
specifier|public
name|void
name|testAccept
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http://localhost:{{port}}/myaccept"
argument_list|,
literal|null
argument_list|,
literal|"Accept"
argument_list|,
literal|"application/myjson"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"You called me as GET and accepted: application/myjson"
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/myaccept"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"You called me as ${header.CamelHttpMethod} and accepted: ${header.Accept}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

