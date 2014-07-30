begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
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
name|AvailablePortFinder
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
name|jboss
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
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|string
operator|.
name|StringDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|string
operator|.
name|StringEncoder
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
DECL|class|NettyHttpGetWithInvalidMessageTest
specifier|public
class|class
name|NettyHttpGetWithInvalidMessageTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|REQUEST_STRING
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_STRING
init|=
literal|"user: Willem\n"
operator|+
literal|"GET http://localhost:8101/test HTTP/1.1\n"
operator|+
literal|"another: value\n Host: localhost\n"
decl_stmt|;
DECL|field|port1
specifier|private
name|int
name|port1
decl_stmt|;
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
comment|// setup the String encoder and decoder
name|StringDecoder
name|stringDecoder
init|=
operator|new
name|StringDecoder
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"string-decoder"
argument_list|,
name|stringDecoder
argument_list|)
expr_stmt|;
name|StringEncoder
name|stringEncoder
init|=
operator|new
name|StringEncoder
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"string-encoder"
argument_list|,
name|stringEncoder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|decoders
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelHandler
argument_list|>
argument_list|()
decl_stmt|;
name|decoders
operator|.
name|add
argument_list|(
name|stringDecoder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|encoders
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelHandler
argument_list|>
argument_list|()
decl_stmt|;
name|encoders
operator|.
name|add
argument_list|(
name|stringEncoder
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"encoders"
argument_list|,
name|encoders
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"decoders"
argument_list|,
name|decoders
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|testNettyHttpServer ()
specifier|public
name|void
name|testNettyHttpServer
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeService
argument_list|(
literal|8100
argument_list|)
expr_stmt|;
block|}
comment|//@Test
DECL|method|testJettyHttpServer ()
specifier|public
name|void
name|testJettyHttpServer
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeService
argument_list|(
name|port1
argument_list|)
expr_stmt|;
block|}
DECL|method|invokeService (int port)
specifier|private
name|void
name|invokeService
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port
operator|+
literal|"?encoders=#encoders&decoders=#decoders&sync=true"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|REQUEST_STRING
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|out
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
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the 404 response."
argument_list|,
name|result
operator|.
name|indexOf
argument_list|(
literal|"404 Not Found"
argument_list|)
operator|>
literal|0
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
name|port1
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|8100
argument_list|)
expr_stmt|;
comment|// set up a netty http proxy
name|from
argument_list|(
literal|"netty-http:http://localhost:"
operator|+
name|port1
operator|+
literal|"/test"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"Bye ${header.user}."
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

