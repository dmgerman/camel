begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.syslog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|syslog
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
name|component
operator|.
name|syslog
operator|.
name|netty
operator|.
name|Rfc5425Encoder
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
name|syslog
operator|.
name|netty
operator|.
name|Rfc5425FrameDecoder
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
name|spi
operator|.
name|DataFormat
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
name|spi
operator|.
name|Registry
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
name|junit
operator|.
name|BeforeClass
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
DECL|class|NettyRfc5425Test
specifier|public
class|class
name|NettyRfc5425Test
extends|extends
name|CamelTestSupport
block|{
DECL|field|uri
specifier|private
specifier|static
name|String
name|uri
decl_stmt|;
DECL|field|uriClient
specifier|private
specifier|static
name|String
name|uriClient
decl_stmt|;
DECL|field|serverPort
specifier|private
specifier|static
name|int
name|serverPort
decl_stmt|;
DECL|field|rfc3164Message
specifier|private
specifier|final
name|String
name|rfc3164Message
init|=
literal|"<165>Aug  4 05:34:00 mymachine myproc[10]: %% It's\n         time to make the do-nuts.  %%  Ingredients: Mix=OK, Jelly=OK #\n"
operator|+
literal|"         Devices: Mixer=OK, Jelly_Injector=OK, Frier=OK # Transport:\n"
operator|+
literal|"         Conveyer1=OK, Conveyer2=OK # %%"
decl_stmt|;
DECL|field|rfc5424Message
specifier|private
specifier|final
name|String
name|rfc5424Message
init|=
literal|"<34>1 2003-10-11T22:14:15.003Z mymachine.example.com su - ID47 - BOM'su root' failed for lonvick on /dev/pts/8"
decl_stmt|;
DECL|field|rfc5424WithStructuredData
specifier|private
specifier|final
name|String
name|rfc5424WithStructuredData
init|=
literal|"<34>1 2003-10-11T22:14:15.003Z mymachine.example.com su - ID47 "
operator|+
literal|"[exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"] BOM'su root' failed for lonvick on /dev/pts/8"
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"decoder"
argument_list|)
DECL|field|decoder
specifier|private
name|Rfc5425FrameDecoder
name|decoder
init|=
operator|new
name|Rfc5425FrameDecoder
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"encoder"
argument_list|)
DECL|field|encoder
specifier|private
name|Rfc5425Encoder
name|encoder
init|=
operator|new
name|Rfc5425Encoder
argument_list|()
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initPort ()
specifier|public
specifier|static
name|void
name|initPort
parameter_list|()
block|{
name|serverPort
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|uri
operator|=
literal|"netty:tcp://localhost:"
operator|+
name|serverPort
operator|+
literal|"?sync=false&allowDefaultCodec=false&decoders=#decoder&encoder=#encoder"
expr_stmt|;
name|uriClient
operator|=
name|uri
operator|+
literal|"&useByteBuf=true"
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendingCamel ()
specifier|public
name|void
name|testSendingCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:syslogReceiver"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock2
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:syslogReceiver2"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedBodiesReceived
argument_list|(
name|rfc3164Message
argument_list|,
name|rfc5424Message
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|uriClient
argument_list|,
name|rfc3164Message
operator|.
name|getBytes
argument_list|(
literal|"UTF8"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|uriClient
argument_list|,
name|rfc5424Message
operator|.
name|getBytes
argument_list|(
literal|"UTF8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStructuredData ()
specifier|public
name|void
name|testStructuredData
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:syslogReceiver"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:checkStructuredData"
argument_list|,
name|rfc5424WithStructuredData
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
name|context
argument_list|()
operator|.
name|getRegistry
argument_list|(
name|Registry
operator|.
name|class
argument_list|)
operator|.
name|bind
argument_list|(
literal|"rfc5426FrameDecoder"
argument_list|,
operator|new
name|Rfc5425FrameDecoder
argument_list|()
argument_list|)
expr_stmt|;
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
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DataFormat
name|syslogDataFormat
init|=
operator|new
name|SyslogDataFormat
argument_list|()
decl_stmt|;
comment|// we setup a Syslog listener on a random port.
name|from
argument_list|(
name|uri
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|syslogDataFormat
argument_list|)
operator|.
name|process
argument_list|(
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
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|SyslogMessage
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:syslogReceiver"
argument_list|)
operator|.
name|marshal
argument_list|(
name|syslogDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:syslogReceiver2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:checkStructuredData"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|syslogDataFormat
argument_list|)
operator|.
name|process
argument_list|(
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
name|ex
parameter_list|)
block|{
name|Object
name|body
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|Rfc5424SyslogMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"]"
argument_list|,
operator|(
operator|(
name|Rfc5424SyslogMessage
operator|)
name|body
operator|)
operator|.
name|getStructuredData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:syslogReceiver"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

