begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|ResolveEndpointFailedException
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
name|mock
operator|.
name|MockEndpoint
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
name|mina
operator|.
name|common
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|CumulativeProtocolDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolCodecFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolDecoderOutput
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolEncoderOutput
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|textline
operator|.
name|LineDelimiter
import|;
end_import

begin_comment
comment|/**  * Unit test with custom codec.  */
end_comment

begin_class
DECL|class|MinaCustomCodecTest
specifier|public
class|class
name|MinaCustomCodecTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|uri
specifier|protected
name|String
name|uri
init|=
literal|"mina:tcp://localhost:11300?sync=true&codec=#myCodec"
decl_stmt|;
DECL|field|badUri
specifier|protected
name|String
name|badUri
init|=
literal|"mina:tcp://localhost:11300?sync=true&codec=#XXX"
decl_stmt|;
DECL|method|testMyCodec ()
specifier|public
name|void
name|testMyCodec
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|uri
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testTCPEncodeUTF8InputIsString ()
specifier|public
name|void
name|testTCPEncodeUTF8InputIsString
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|myUri
init|=
literal|"mina:tcp://localhost:9080?encoding=UTF-8&sync=false"
decl_stmt|;
name|this
operator|.
name|context
operator|.
name|addRoutes
argument_list|(
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
name|myUri
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
comment|// include a UTF-8 char in the text \u0E08 is a Thai elephant
name|String
name|body
init|=
literal|"Hello Thai Elephant \u0E08"
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|myUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testBadConfiguration ()
specifier|public
name|void
name|testBadConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|badUri
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
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
name|jndi
operator|.
name|bind
argument_list|(
literal|"myCodec"
argument_list|,
operator|new
name|MyCodec
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
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
name|uri
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyCodec
specifier|private
class|class
name|MyCodec
implements|implements
name|ProtocolCodecFactory
block|{
DECL|method|getEncoder ()
specifier|public
name|ProtocolEncoder
name|getEncoder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ProtocolEncoder
argument_list|()
block|{
specifier|public
name|void
name|encode
parameter_list|(
name|IoSession
name|ioSession
parameter_list|,
name|Object
name|message
parameter_list|,
name|ProtocolEncoderOutput
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteBuffer
name|bb
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|32
argument_list|)
operator|.
name|setAutoExpand
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|String
name|s
init|=
operator|(
name|String
operator|)
name|message
decl_stmt|;
name|bb
operator|.
name|put
argument_list|(
name|s
operator|.
name|getBytes
argument_list|(
literal|"US-ASCII"
argument_list|)
argument_list|)
expr_stmt|;
name|bb
operator|.
name|flip
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|bb
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dispose
parameter_list|(
name|IoSession
name|ioSession
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
block|}
return|;
block|}
DECL|method|getDecoder ()
specifier|public
name|ProtocolDecoder
name|getDecoder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CumulativeProtocolDecoder
argument_list|()
block|{
specifier|protected
name|boolean
name|doDecode
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|ByteBuffer
name|in
parameter_list|,
name|ProtocolDecoderOutput
name|out
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|in
operator|.
name|remaining
argument_list|()
operator|>
literal|0
condition|)
block|{
name|byte
index|[]
name|buf
init|=
name|MinaConverter
operator|.
name|toByteArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
operator|new
name|String
argument_list|(
name|buf
argument_list|,
literal|"US-ASCII"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

