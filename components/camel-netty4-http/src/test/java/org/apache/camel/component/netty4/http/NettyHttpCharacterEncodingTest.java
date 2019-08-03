begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Exchange
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
name|Processor
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
DECL|class|NettyHttpCharacterEncodingTest
specifier|public
class|class
name|NettyHttpCharacterEncodingTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Test
DECL|method|testSendToNetty ()
specifier|public
name|void
name|testSendToNetty
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/myapp/myservice"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World Thai Elephant \u0E08"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/html; charset=utf-8"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// convert the response to a String
name|String
name|body
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Response message is Thai Elephant \u0E08"
argument_list|,
name|body
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
literal|"netty4-http:http://localhost:{{port}}/myapp/myservice"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyBookService
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBookService
specifier|public
class|class
name|MyBookService
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// just get the body as a string
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// for unit testing make sure we got right message
name|assertEquals
argument_list|(
literal|"Hello World Thai Elephant \u0E08"
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// send a html response
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/html; charset=utf-8"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Response message is Thai Elephant \u0E08"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

