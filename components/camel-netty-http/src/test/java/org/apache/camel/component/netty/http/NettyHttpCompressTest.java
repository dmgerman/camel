begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpContentDecompressor
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
name|BindToRegistry
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|NettyHttpCompressTest
specifier|public
class|class
name|NettyHttpCompressTest
extends|extends
name|BaseNettyTest
block|{
comment|// setup the decompress decoder here
annotation|@
name|BindToRegistry
argument_list|(
literal|"myDecoders"
argument_list|)
DECL|method|addChannelHandlers ()
specifier|public
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|addChannelHandlers
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|decoders
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|decoders
operator|.
name|add
argument_list|(
operator|new
name|HttpContentDecompressor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|decoders
return|;
block|}
annotation|@
name|Test
DECL|method|testContentType ()
specifier|public
name|void
name|testContentType
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|data
init|=
literal|"Hello World"
operator|.
name|getBytes
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"content-type"
argument_list|,
literal|"text/plain; charset=\"UTF-8\""
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Accept-Encoding"
argument_list|,
literal|"compress, gzip"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foo?decoders=#myDecoders"
argument_list|,
name|data
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// The decoded out has some space to clean up.
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
operator|.
name|trim
argument_list|()
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
literal|"netty-http:http://0.0.0.0:{{port}}/foo?compression=true"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"content-type"
argument_list|)
operator|.
name|constant
argument_list|(
literal|"text/plain; charset=\"UTF-8\""
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

