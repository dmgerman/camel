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
name|io
operator|.
name|netty
operator|.
name|buffer
operator|.
name|ByteBuf
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
DECL|class|NettyRfc5425LongMessageTest
specifier|public
class|class
name|NettyRfc5425LongMessageTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|uri
specifier|private
specifier|static
name|String
name|uri
decl_stmt|;
DECL|field|serverPort
specifier|private
specifier|static
name|int
name|serverPort
decl_stmt|;
DECL|field|MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"<34>1 2003-10-11T22:14:15.003Z mymachine.example.com su - ID47 - "
operator|+
literal|"Lorem ipsum dolor sit amet, tempor democritum vix ad, est partiendo laboramus ei. "
operator|+
literal|"Munere laudem commune vis ad, et qui altera singulis. Ut assum deleniti sit, vix constituto assueverit appellantur at, et meis voluptua usu. "
operator|+
literal|"Quem imperdiet in ius, mei ex dictas mandamus, ut pri tation appetere oportere. Et est harum dictas. \n Omnis quaestio mel te, ex duo autem molestie. "
operator|+
literal|"Ei sed dico minim, nominavi facilisis evertitur quo an, te adipiscing contentiones his. Cum partem deseruisse at, ne iuvaret mediocritatem pro. "
operator|+
literal|"Ex prima utinam convenire usu, volumus legendos nec et, natum putant quo ne. Invidunt necessitatibus at ius, ne eum wisi dicat mediocrem. "
operator|+
literal|"\n Cu usu odio labores sententiae. Ex eos duis singulis necessitatibus, dico omittam vix at. Sit iudico option detracto an, sit no modus exerci oportere. "
operator|+
literal|"Vix dicta munere at, no vis feugiat omnesque convenire. Duo at quod illum dolor, nec amet tantas iisque no, mei quod graece volutpat ea.\n "
operator|+
literal|"Ornatus legendos theophrastus id mei. Cum alia assum abhorreant et, nam indoctum intellegebat ei. Unum constituto quo cu. "
operator|+
literal|"Vero tritani sit ei, ea commodo menandri usu, ponderum hendrerit voluptatibus sed te. "
operator|+
literal|"\n Semper aliquid fabulas ei mel. Vix ei nullam malorum bonorum, movet nemore scaevola cu vel. "
operator|+
literal|"Quo ut esse dictas incorrupte, ex denique splendide nec, mei dicit doming omnium no. Nulla putent nec id, vis vide ignota eligendi in."
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
literal|1
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedBodiesReceived
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|MESSAGE
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
comment|// Here we need to turn the request body into ByteBuf
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|ByteBuf
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

