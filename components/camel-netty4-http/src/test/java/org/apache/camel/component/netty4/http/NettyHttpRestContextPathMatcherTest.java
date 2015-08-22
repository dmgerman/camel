begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
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
operator|.
name|http
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
name|Ignore
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|.
name|HTTP_METHOD
import|;
end_import

begin_class
annotation|@
name|Ignore
DECL|class|NettyHttpRestContextPathMatcherTest
specifier|public
class|class
name|NettyHttpRestContextPathMatcherTest
extends|extends
name|BaseNettyTest
block|{
comment|// TODO: implement the logic for this in a better way
annotation|@
name|Test
DECL|method|shouldReturnCustomResponseForOptions ()
specifier|public
name|void
name|shouldReturnCustomResponseForOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/foo"
argument_list|,
literal|""
argument_list|,
name|HTTP_METHOD
argument_list|,
literal|"OPTIONS"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"expectedOptionsResponse"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldPreferStrictMatchOverPrefixMatch ()
specifier|public
name|void
name|shouldPreferStrictMatchOverPrefixMatch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/path2/foo"
argument_list|,
literal|""
argument_list|,
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"exact"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldPreferOptionsForEqualPaths ()
specifier|public
name|void
name|shouldPreferOptionsForEqualPaths
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/path3"
argument_list|,
literal|""
argument_list|,
name|HTTP_METHOD
argument_list|,
literal|"POST"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"postPath3"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|response
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/path3"
argument_list|,
literal|""
argument_list|,
name|HTTP_METHOD
argument_list|,
literal|"OPTIONS"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"optionsPath3"
argument_list|,
name|response
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
literal|"netty4-http:http://0.0.0.0:{{port}}/path1?httpMethodRestrict=POST"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"somePostResponse"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4-http:http://0.0.0.0:{{port}}?matchOnUriPrefix=true&httpMethodRestrict=OPTIONS"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"expectedOptionsResponse"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4-http:http://0.0.0.0:{{port}}/path2/foo"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"exact"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4-http:http://0.0.0.0:{{port}}/path2?matchOnUriPrefix=true"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"wildcard"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4-http:http://0.0.0.0:{{port}}/path3?httpMethodRestrict=POST"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"postPath3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4-http:http://0.0.0.0:{{port}}/path3?httpMethodRestrict=OPTIONS"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"optionsPath3"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

