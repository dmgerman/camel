begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|netty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|ExchangeTimedOutException
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
name|LoggingLevel
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
name|main
operator|.
name|Main
import|;
end_import

begin_comment
comment|/**  * Netty client which calls the server every half-second with a random word.  */
end_comment

begin_class
DECL|class|MyClient
specifier|public
specifier|final
class|class
name|MyClient
block|{
DECL|method|MyClient ()
specifier|private
name|MyClient
parameter_list|()
block|{     }
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|MyRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup correlation manager and its timeout (when a request has not received a response within the given time millis)
name|MyCorrelationManager
name|manager
init|=
operator|new
name|MyCorrelationManager
argument_list|()
decl_stmt|;
comment|// set timeout for each request message that did not receive a reply message
name|manager
operator|.
name|setTimeout
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// set the logging level when a timeout was hit, ny default its DEBUG
name|manager
operator|.
name|setTimeoutLoggingLevel
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|)
expr_stmt|;
name|main
operator|.
name|bind
argument_list|(
literal|"myEncoder"
argument_list|,
operator|new
name|MyCodecEncoderFactory
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|bind
argument_list|(
literal|"myDecoder"
argument_list|,
operator|new
name|MyCodecDecoderFactory
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|bind
argument_list|(
literal|"myManager"
argument_list|,
name|manager
argument_list|)
expr_stmt|;
name|main
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
DECL|class|MyRouteBuilder
specifier|public
specifier|static
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|words
specifier|private
name|String
index|[]
name|words
init|=
operator|new
name|String
index|[]
block|{
literal|"foo"
block|,
literal|"bar"
block|,
literal|"baz"
block|,
literal|"beer"
block|,
literal|"wine"
block|,
literal|"cheese"
block|}
decl_stmt|;
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
DECL|method|increment ()
specifier|public
name|int
name|increment
parameter_list|()
block|{
return|return
operator|++
name|counter
return|;
block|}
DECL|method|word ()
specifier|public
name|String
name|word
parameter_list|()
block|{
name|int
name|ran
init|=
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|(
name|words
operator|.
name|length
argument_list|)
decl_stmt|;
return|return
name|words
index|[
name|ran
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets build a special custom error message for timeout
name|onException
argument_list|(
name|ExchangeTimedOutException
operator|.
name|class
argument_list|)
comment|// here we tell Camel to continue routing
operator|.
name|continued
argument_list|(
literal|true
argument_list|)
comment|// after it has built this special timeout error message body
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"#${header.corId}:${header.word}-Time out error!!!"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:trigger"
argument_list|)
comment|// set correlation id as unique incrementing number
operator|.
name|setHeader
argument_list|(
literal|"corId"
argument_list|,
name|method
argument_list|(
name|this
argument_list|,
literal|"increment"
argument_list|)
argument_list|)
comment|// set random word to use in request
operator|.
name|setHeader
argument_list|(
literal|"word"
argument_list|,
name|method
argument_list|(
name|this
argument_list|,
literal|"word"
argument_list|)
argument_list|)
comment|// build request message as a string body
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"#${header.corId}:${header.word}"
argument_list|)
argument_list|)
comment|// log request before
operator|.
name|log
argument_list|(
literal|"Request:  ${id}:${body}"
argument_list|)
comment|// call netty server using a single shared connection and using custom correlation manager
comment|// to ensure we can correltly map the request and response pairs
operator|.
name|to
argument_list|(
literal|"netty4:tcp://localhost:4444?sync=true&encoders=#myEncoder&decoders=#myDecoder"
operator|+
literal|"&producerPoolEnabled=false&correlationManager=#myManager"
argument_list|)
comment|// log response after
operator|.
name|log
argument_list|(
literal|"Response: ${id}:${body}"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

