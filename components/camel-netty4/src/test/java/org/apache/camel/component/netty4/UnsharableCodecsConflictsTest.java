begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|buffer
operator|.
name|BigEndianHeapChannelBuffer
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
DECL|class|UnsharableCodecsConflictsTest
specifier|public
class|class
name|UnsharableCodecsConflictsTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|LENGTH_HEADER
specifier|static
specifier|final
name|byte
index|[]
name|LENGTH_HEADER
init|=
block|{
literal|0x00
block|,
literal|0x00
block|,
literal|0x40
block|,
literal|0x00
block|}
decl_stmt|;
comment|// 4096 bytes
DECL|field|processor
specifier|private
name|Processor
name|processor
init|=
operator|new
name|P
argument_list|()
decl_stmt|;
DECL|field|port1
specifier|private
name|int
name|port1
decl_stmt|;
DECL|field|port2
specifier|private
name|int
name|port2
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
comment|// we can share the decoder between multiple netty consumers, because they have the same configuration
comment|// and we use a ChannelHandlerFactory
name|ChannelHandlerFactory
name|decoder
init|=
name|ChannelHandlerFactories
operator|.
name|newLengthFieldBasedFrameDecoder
argument_list|(
literal|1048576
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"length-decoder"
argument_list|,
name|decoder
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"length-decoder2"
argument_list|,
name|decoder
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|canSupplyMultipleCodecsToEndpointPipeline ()
specifier|public
name|void
name|canSupplyMultipleCodecsToEndpointPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|sPort1
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|byte
index|[]
name|sPort2
init|=
operator|new
name|byte
index|[
literal|16383
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|sPort1
argument_list|,
operator|(
name|byte
operator|)
literal|0x38
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|sPort2
argument_list|,
operator|(
name|byte
operator|)
literal|0x39
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bodyPort1
init|=
operator|(
operator|new
name|String
argument_list|(
name|LENGTH_HEADER
argument_list|)
operator|+
operator|new
name|String
argument_list|(
name|sPort1
argument_list|)
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bodyPort2
init|=
operator|(
operator|new
name|String
argument_list|(
name|LENGTH_HEADER
argument_list|)
operator|+
operator|new
name|String
argument_list|(
name|sPort2
argument_list|)
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
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
name|expectedBodiesReceived
argument_list|(
operator|new
name|String
argument_list|(
name|sPort2
argument_list|)
operator|+
literal|"9"
argument_list|)
expr_stmt|;
name|Socket
name|server1
init|=
name|getSocket
argument_list|(
literal|"localhost"
argument_list|,
name|port1
argument_list|)
decl_stmt|;
name|Socket
name|server2
init|=
name|getSocket
argument_list|(
literal|"localhost"
argument_list|,
name|port2
argument_list|)
decl_stmt|;
try|try
block|{
name|sendSopBuffer
argument_list|(
name|bodyPort2
argument_list|,
name|server2
argument_list|)
expr_stmt|;
name|sendSopBuffer
argument_list|(
name|bodyPort1
argument_list|,
name|server1
argument_list|)
expr_stmt|;
name|sendSopBuffer
argument_list|(
operator|new
name|String
argument_list|(
literal|"9"
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|,
name|server2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|server1
operator|.
name|close
argument_list|()
expr_stmt|;
name|server2
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
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
name|port1
operator|=
name|getPort
argument_list|()
expr_stmt|;
name|port2
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"netty4:tcp://localhost:"
operator|+
name|port1
operator|+
literal|"?decoder=#length-decoder&sync=false"
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4:tcp://localhost:"
operator|+
name|port2
operator|+
literal|"?decoder=#length-decoder2&sync=false"
argument_list|)
operator|.
name|process
argument_list|(
name|processor
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
DECL|method|getSocket (String host, int port)
specifier|private
specifier|static
name|Socket
name|getSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
name|Socket
name|s
init|=
operator|new
name|Socket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|s
operator|.
name|setSoTimeout
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
return|return
name|s
return|;
block|}
DECL|method|sendSopBuffer (byte[] buf, Socket server)
specifier|public
specifier|static
name|void
name|sendSopBuffer
parameter_list|(
name|byte
index|[]
name|buf
parameter_list|,
name|Socket
name|server
parameter_list|)
throws|throws
name|Exception
block|{
name|BufferedOutputStream
name|dataOut
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
name|server
operator|.
name|getOutputStream
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|dataOut
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
expr_stmt|;
name|dataOut
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|dataOut
argument_list|)
expr_stmt|;
name|server
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
DECL|class|P
class|class
name|P
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|String
argument_list|(
operator|(
operator|(
name|BigEndianHeapChannelBuffer
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|)
operator|.
name|array
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

