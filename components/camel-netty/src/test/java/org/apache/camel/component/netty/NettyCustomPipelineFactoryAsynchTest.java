begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
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
package|;
end_package

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
name|concurrent
operator|.
name|TimeUnit
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
name|Produce
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
name|ProducerTemplate
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
name|netty
operator|.
name|handlers
operator|.
name|ClientChannelHandler
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
name|netty
operator|.
name|handlers
operator|.
name|ServerChannelHandler
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
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|ChannelDownstreamHandler
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
name|ChannelPipeline
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
name|ChannelUpstreamHandler
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
name|Channels
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
name|frame
operator|.
name|DelimiterBasedFrameDecoder
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
name|frame
operator|.
name|Delimiters
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
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|timeout
operator|.
name|ReadTimeoutHandler
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
name|util
operator|.
name|CharsetUtil
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
DECL|class|NettyCustomPipelineFactoryAsynchTest
specifier|public
class|class
name|NettyCustomPipelineFactoryAsynchTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|NettyCustomPipelineFactoryAsynchTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|producerTemplate
specifier|protected
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|field|clientPipelineFactory
specifier|private
name|TestClientChannelPipelineFactory
name|clientPipelineFactory
decl_stmt|;
DECL|field|serverPipelineFactory
specifier|private
name|TestServerChannelPipelineFactory
name|serverPipelineFactory
decl_stmt|;
DECL|field|response
specifier|private
name|String
name|response
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
operator|new
name|JndiRegistry
argument_list|(
name|createJndiContext
argument_list|()
argument_list|)
decl_stmt|;
name|clientPipelineFactory
operator|=
operator|new
name|TestClientChannelPipelineFactory
argument_list|()
expr_stmt|;
name|serverPipelineFactory
operator|=
operator|new
name|TestServerChannelPipelineFactory
argument_list|()
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"cpf"
argument_list|,
name|clientPipelineFactory
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"spf"
argument_list|,
name|serverPipelineFactory
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|sendRequest ()
specifier|private
name|void
name|sendRequest
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Async request
name|response
operator|=
operator|(
name|String
operator|)
name|producerTemplate
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:5110?clientPipelineFactory=#cpf&textline=true"
argument_list|,
literal|"Forest Gump describing Vietnam..."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomClientPipelineFactory ()
specifier|public
name|void
name|testCustomClientPipelineFactory
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"netty:tcp://localhost:5110?serverPipelineFactory=#spf&textline=true"
argument_list|)
operator|.
name|process
argument_list|(
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
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Forrest Gump: We was always taking long walks, and we was always looking for a guy named 'Charlie'"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Beginning Test ---> testCustomClientPipelineFactory()"
argument_list|)
expr_stmt|;
block|}
name|sendRequest
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Completed Test ---> testCustomClientPipelineFactory()"
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forrest Gump: We was always taking long walks, and we was always looking for a guy named 'Charlie'"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|clientPipelineFactory
operator|.
name|isfactoryInvoked
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|serverPipelineFactory
operator|.
name|isfactoryInvoked
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|TestClientChannelPipelineFactory
specifier|public
class|class
name|TestClientChannelPipelineFactory
extends|extends
name|ClientPipelineFactory
block|{
DECL|field|maxLineSize
specifier|private
name|int
name|maxLineSize
init|=
literal|1024
decl_stmt|;
DECL|field|invoked
specifier|private
name|boolean
name|invoked
decl_stmt|;
DECL|method|getPipeline ()
specifier|public
name|ChannelPipeline
name|getPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
name|ChannelPipeline
name|channelPipeline
init|=
name|Channels
operator|.
name|pipeline
argument_list|()
decl_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"decoder-DELIM"
argument_list|,
operator|new
name|DelimiterBasedFrameDecoder
argument_list|(
name|maxLineSize
argument_list|,
literal|true
argument_list|,
name|Delimiters
operator|.
name|lineDelimiter
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"decoder-SD"
argument_list|,
operator|new
name|StringDecoder
argument_list|(
name|CharsetUtil
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"encoder-SD"
argument_list|,
operator|new
name|StringEncoder
argument_list|(
name|CharsetUtil
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"handler"
argument_list|,
operator|new
name|ClientChannelHandler
argument_list|(
name|producer
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|channelPipeline
return|;
block|}
DECL|method|isfactoryInvoked ()
specifier|public
name|boolean
name|isfactoryInvoked
parameter_list|()
block|{
return|return
name|invoked
return|;
block|}
block|}
DECL|class|TestServerChannelPipelineFactory
specifier|public
class|class
name|TestServerChannelPipelineFactory
extends|extends
name|ServerPipelineFactory
block|{
DECL|field|maxLineSize
specifier|private
name|int
name|maxLineSize
init|=
literal|1024
decl_stmt|;
DECL|field|invoked
specifier|private
name|boolean
name|invoked
decl_stmt|;
DECL|method|getPipeline ()
specifier|public
name|ChannelPipeline
name|getPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
name|ChannelPipeline
name|channelPipeline
init|=
name|Channels
operator|.
name|pipeline
argument_list|()
decl_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"encoder-SD"
argument_list|,
operator|new
name|StringEncoder
argument_list|(
name|CharsetUtil
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"decoder-DELIM"
argument_list|,
operator|new
name|DelimiterBasedFrameDecoder
argument_list|(
name|maxLineSize
argument_list|,
literal|true
argument_list|,
name|Delimiters
operator|.
name|lineDelimiter
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"decoder-SD"
argument_list|,
operator|new
name|StringDecoder
argument_list|(
name|CharsetUtil
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"handler"
argument_list|,
operator|new
name|ServerChannelHandler
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|channelPipeline
return|;
block|}
DECL|method|isfactoryInvoked ()
specifier|public
name|boolean
name|isfactoryInvoked
parameter_list|()
block|{
return|return
name|invoked
return|;
block|}
block|}
block|}
end_class

end_unit

